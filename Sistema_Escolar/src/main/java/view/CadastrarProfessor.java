package view;

import dao.ProfessorDao;
import model.Professor;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class CadastrarProfessor extends JFrame {

    private JTextField campo_id;
    private JTextField campo_nome;
    private JTextField campo_idade;
    private JTextField campo_email;
    private JPasswordField campo_senha;
    private JTable tabela;

    public CadastrarProfessor() {
        setTitle("Cadastrar Professor");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(750, 500);
        setLocationRelativeTo(null);

        JPanel campos = new JPanel(new GridLayout(5, 2, 5, 5));

        campos.add(new JLabel("ID:"));
        campo_id = new JTextField();
        campo_id.setEditable(false);
        campos.add(campo_id);

        campos.add(new JLabel("Nome:"));
        campo_nome = new JTextField();
        campos.add(campo_nome);

        campos.add(new JLabel("Idade:"));
        campo_idade = new JTextField();
        campos.add(campo_idade);

        campos.add(new JLabel("Email:"));
        campo_email = new JTextField();
        campos.add(campo_email);

        campos.add(new JLabel("Senha:"));
        campo_senha = new JPasswordField();
        campos.add(campo_senha);

        JPanel botoes = new JPanel();

        JButton cadastrar = new JButton("Cadastrar");
        JButton alterar = new JButton("Alterar");
        JButton excluir = new JButton("Excluir");
        JButton limpar = new JButton("Limpar");
        JButton voltar = new JButton("Voltar");

        botoes.add(cadastrar);
        botoes.add(alterar);
        botoes.add(excluir);
        botoes.add(limpar);
        botoes.add(voltar);

        tabela = new JTable();
        JScrollPane rolagem = new JScrollPane(tabela);

        setLayout(new BorderLayout(10, 10));
        add(campos, BorderLayout.NORTH);
        add(rolagem, BorderLayout.CENTER);
        add(botoes, BorderLayout.SOUTH);

        cadastrar.addActionListener(e -> cadastrarProfessor());
        alterar.addActionListener(e -> alterarProfessor());
        excluir.addActionListener(e -> excluirProfessor());
        limpar.addActionListener(e -> limparCampos());
        voltar.addActionListener(e -> voltar());

        tabela.getSelectionModel().addListSelectionListener(e -> selecionarProfessor());

        carregarTabela();
    }

    private void carregarTabela() {
        String[] colunas = {"ID", "Nome", "Idade", "Email", "Senha"};
        DefaultTableModel modelo = new DefaultTableModel(colunas, 0);

        List<Professor> professores = ProfessorDao.listar();

        for (Professor professor : professores) {
            modelo.addRow(new Object[]{
                professor.getId(),
                professor.getNome(),
                professor.getIdade(),
                professor.getEmail(),
                professor.getSenha()
            });
        }

        tabela.setModel(modelo);
    }

    private void selecionarProfessor() {
        int linha = tabela.getSelectedRow();

        if (linha >= 0) {
            campo_id.setText(tabela.getValueAt(linha, 0).toString());
            campo_nome.setText(tabela.getValueAt(linha, 1).toString());
            campo_idade.setText(tabela.getValueAt(linha, 2).toString());
            campo_email.setText(tabela.getValueAt(linha, 3).toString());
            campo_senha.setText(tabela.getValueAt(linha, 4).toString());
        }
    }

    private boolean camposPreenchidos() {
        return !campo_nome.getText().trim().isEmpty()
                && !campo_idade.getText().trim().isEmpty()
                && !campo_email.getText().trim().isEmpty()
                && campo_senha.getPassword().length > 0;
    }

    private void cadastrarProfessor() {
        if (!camposPreenchidos()) {
            JOptionPane.showMessageDialog(this, "Preencha todos os campos!");
            return;
        }

        try {
            int idade = Integer.parseInt(campo_idade.getText().trim());

            if (idade < 0) {
                JOptionPane.showMessageDialog(this, "A idade não pode ser negativa.");
                return;
            }

            Professor professor = new Professor(
                    0,
                    campo_nome.getText().trim(),
                    idade,
                    campo_email.getText().trim(),
                    new String(campo_senha.getPassword())
            );

            if (ProfessorDao.cadastrar(professor)) {
                JOptionPane.showMessageDialog(this, "Professor cadastrado com sucesso!");
                limparCampos();
                carregarTabela();
            } else {
                JOptionPane.showMessageDialog(this, "Não foi possível cadastrar. Verifique se o email já existe.");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "A idade deve ser um número.");
        }
    }

    private void alterarProfessor() {
        if (campo_id.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Selecione um professor na tabela.");
            return;
        }

        if (!camposPreenchidos()) {
            JOptionPane.showMessageDialog(this, "Preencha todos os campos!");
            return;
        }

        try {
            int id = Integer.parseInt(campo_id.getText());
            int idade = Integer.parseInt(campo_idade.getText().trim());

            if (idade < 0) {
                JOptionPane.showMessageDialog(this, "A idade não pode ser negativa.");
                return;
            }

            Professor professor = new Professor(
                    id,
                    campo_nome.getText().trim(),
                    idade,
                    campo_email.getText().trim(),
                    new String(campo_senha.getPassword())
            );

            if (ProfessorDao.alterar(professor)) {
                JOptionPane.showMessageDialog(this, "Professor alterado com sucesso!");
                limparCampos();
                carregarTabela();
            } else {
                JOptionPane.showMessageDialog(this, "Não foi possível alterar o professor.");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "ID e idade devem ser números.");
        }
    }

    private void excluirProfessor() {
        if (campo_id.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Selecione um professor na tabela.");
            return;
        }

        int resposta = JOptionPane.showConfirmDialog(
                this,
                "Deseja excluir este professor?",
                "Confirmar exclusão",
                JOptionPane.YES_NO_OPTION
        );

        if (resposta == JOptionPane.YES_OPTION) {
            try {
                int id = Integer.parseInt(campo_id.getText());

                if (ProfessorDao.excluir(id)) {
                    JOptionPane.showMessageDialog(this, "Professor excluído com sucesso!");
                    limparCampos();
                    carregarTabela();
                } else {
                    JOptionPane.showMessageDialog(this, "Não foi possível excluir o professor.");
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "ID inválido.");
            }
        }
    }

    private void limparCampos() {
        campo_id.setText("");
        campo_nome.setText("");
        campo_idade.setText("");
        campo_email.setText("");
        campo_senha.setText("");
        tabela.clearSelection();
    }

    private void voltar() {
        Administrador administrador = new Administrador();
        administrador.setVisible(true);
        this.setVisible(false);
    }

    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(() -> new CadastrarProfessor().setVisible(true));
    }
}
