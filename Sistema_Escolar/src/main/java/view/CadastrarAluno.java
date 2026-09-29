package view;

import dao.AlunoDao;
import model.Aluno;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class CadastrarAluno extends JFrame {

    private JTextField campo_id;
    private JTextField campo_nome;
    private JTextField campo_idade;
    private JTextField campo_email;
    private JPasswordField campo_senha;
    private JTable tabela;

    public CadastrarAluno() {
        setTitle("Cadastrar Aluno");
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

        cadastrar.addActionListener(e -> cadastrarAluno());
        alterar.addActionListener(e -> alterarAluno());
        excluir.addActionListener(e -> excluirAluno());
        limpar.addActionListener(e -> limparCampos());
        voltar.addActionListener(e -> voltar());

        tabela.getSelectionModel().addListSelectionListener(e -> selecionarAluno());

        carregarTabela();
    }

    private void carregarTabela() {
        String[] colunas = {"ID", "Nome", "Idade", "Email", "Senha", "Status"};
        DefaultTableModel modelo = new DefaultTableModel(colunas, 0);

        List<Aluno> alunos = AlunoDao.listar();

        for (Aluno aluno : alunos) {
            modelo.addRow(new Object[]{
                aluno.getId(),
                aluno.getNome(),
                aluno.getIdade(),
                aluno.getEmail(),
                aluno.getSenha(),
                aluno.getStatus()
            });
        }

        tabela.setModel(modelo);
    }

    private void selecionarAluno() {
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

    private boolean idadeValida() {
        try {
            int idade = Integer.parseInt(campo_idade.getText().trim());

            if (idade < 0) {
                JOptionPane.showMessageDialog(this, "A idade não pode ser negativa.");
                return false;
            }

            return true;
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "A idade deve ser um número.");
            return false;
        }
    }

    private void cadastrarAluno() {
        if (!camposPreenchidos()) {
            JOptionPane.showMessageDialog(this, "Preencha todos os campos!");
            return;
        }

        if (!idadeValida()) {
            return;
        }

        int idade = Integer.parseInt(campo_idade.getText().trim());

        Aluno aluno = new Aluno(
                0,
                campo_nome.getText().trim(),
                idade,
                campo_email.getText().trim(),
                new String(campo_senha.getPassword()),
                "ATIVO"
        );

        if (AlunoDao.cadastrar(aluno)) {
            JOptionPane.showMessageDialog(this, "Aluno cadastrado com sucesso!");
            limparCampos();
            carregarTabela();
        } else {
            JOptionPane.showMessageDialog(this,
                    "Não foi possível cadastrar. Verifique se o email já existe.");
        }
    }

    private void alterarAluno() {
        if (campo_id.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Selecione um aluno na tabela.");
            return;
        }

        if (!camposPreenchidos()) {
            JOptionPane.showMessageDialog(this, "Preencha todos os campos!");
            return;
        }

        if (!idadeValida()) {
            return;
        }

        try {
            int id = Integer.parseInt(campo_id.getText());
            int idade = Integer.parseInt(campo_idade.getText().trim());

            Aluno aluno = new Aluno(
                    id,
                    campo_nome.getText().trim(),
                    idade,
                    campo_email.getText().trim(),
                    new String(campo_senha.getPassword()),
                    "ATIVO"
            );

            if (AlunoDao.alterar(aluno)) {
                JOptionPane.showMessageDialog(this, "Aluno alterado com sucesso!");
                limparCampos();
                carregarTabela();
            } else {
                JOptionPane.showMessageDialog(this, "Não foi possível alterar o aluno.");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "ID e idade devem ser números.");
        }
    }

    private void excluirAluno() {
        if (campo_id.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Selecione um aluno na tabela.");
            return;
        }

        int resposta = JOptionPane.showConfirmDialog(
                this,
                "Deseja excluir este aluno?",
                "Confirmar exclusão",
                JOptionPane.YES_NO_OPTION
        );

        if (resposta == JOptionPane.YES_OPTION) {
            try {
                int id = Integer.parseInt(campo_id.getText());

                if (AlunoDao.excluir(id)) {
                    JOptionPane.showMessageDialog(this, "Aluno excluído com sucesso!");
                    limparCampos();
                    carregarTabela();
                } else {
                    JOptionPane.showMessageDialog(this, "Não foi possível excluir o aluno.");
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
        java.awt.EventQueue.invokeLater(() -> new CadastrarAluno().setVisible(true));
    }
}
