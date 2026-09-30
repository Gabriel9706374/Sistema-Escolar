package view;

import dao.ConsultaDao;
import dao.NotaDao;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class Professor extends JFrame {

    private final JComboBox<Item> comboTurma = new JComboBox<>();
    private final JComboBox<Item> comboAluno = new JComboBox<>();
    private final JComboBox<Item> comboDisciplina = new JComboBox<>();
    private final JTextField campoNota1 = new JTextField();
    private final JTextField campoNota2 = new JTextField();

    private final DefaultTableModel modeloTabela = new DefaultTableModel(
            new String[]{"ID", "Turma", "Disciplina", "Aluno", "Nota 1", "Nota 2", "Nota Final", "Status"}, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };

    private final JTable tabela = new JTable(modeloTabela);
    private int notaSelecionadaId = -1;

    public Professor() {
        setTitle("Notas - Professor");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(950, 600);
        setLocationRelativeTo(null);

        montarTela();
        carregarCombos();
        carregarTabela();
    }

    private void montarTela() {
        JPanel principal = new JPanel(new BorderLayout(10, 10));
        principal.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel titulo = new JLabel("Gerenciamento de Notas");
        titulo.setHorizontalAlignment(SwingConstants.CENTER);
        principal.add(titulo, BorderLayout.NORTH);

        JPanel campos = new JPanel(new GridLayout(3, 4, 8, 8));
        campos.add(new JLabel("Turma:"));
        campos.add(comboTurma);
        campos.add(new JLabel("Aluno:"));
        campos.add(comboAluno);
        campos.add(new JLabel("Disciplina:"));
        campos.add(comboDisciplina);
        campos.add(new JLabel("Nota 1:"));
        campos.add(campoNota1);
        campos.add(new JLabel("Nota 2:"));
        campos.add(campoNota2);
        campos.add(new JLabel(""));
        campos.add(new JLabel(""));

        JPanel botoes = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton cadastrar = new JButton("Cadastrar");
        JButton alterar = new JButton("Alterar");
        JButton excluir = new JButton("Excluir");
        JButton selecionar = new JButton("Selecionar");
        JButton limpar = new JButton("Limpar");
        JButton voltar = new JButton("Voltar");

        cadastrar.addActionListener(e -> cadastrarNota());
        alterar.addActionListener(e -> alterarNota());
        excluir.addActionListener(e -> excluirNota());
        selecionar.addActionListener(e -> selecionarNota());
        limpar.addActionListener(e -> limparCampos());
        voltar.addActionListener(e -> voltarMenu());

        botoes.add(cadastrar);
        botoes.add(alterar);
        botoes.add(excluir);
        botoes.add(selecionar);
        botoes.add(limpar);
        botoes.add(voltar);

        JPanel topo = new JPanel(new BorderLayout(8, 8));
        topo.add(campos, BorderLayout.CENTER);
        topo.add(botoes, BorderLayout.SOUTH);
        principal.add(topo, BorderLayout.NORTH);

        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabela.setAutoCreateRowSorter(true);
        principal.add(new JScrollPane(tabela), BorderLayout.CENTER);

        setContentPane(principal);
    }

    private void carregarCombos() {
        comboTurma.removeAllItems();
        comboAluno.removeAllItems();
        comboDisciplina.removeAllItems();

        for (Object[] item : ConsultaDao.turmasProfessor(Login.email_logado)) {
            comboTurma.addItem(new Item((Integer) item[0], (String) item[1]));
        }

        for (Object[] item : ConsultaDao.alunosProfessor(Login.email_logado)) {
            comboAluno.addItem(new Item((Integer) item[0], (String) item[1]));
        }

        for (Object[] item : ConsultaDao.disciplinasProfessor(Login.email_logado)) {
            comboDisciplina.addItem(new Item((Integer) item[0], (String) item[1]));
        }
    }

    private void carregarTabela() {
        modeloTabela.setRowCount(0);
        List<Object[]> lista = ConsultaDao.notasProfessor(Login.email_logado);
        for (Object[] linha : lista) {
            modeloTabela.addRow(linha);
        }
    }

    private void selecionarNota() {
        int linha = tabela.getSelectedRow();
        if (linha < 0) {
            JOptionPane.showMessageDialog(this, "Selecione uma nota na tabela!");
            return;
        }

        int linhaModelo = tabela.convertRowIndexToModel(linha);
        notaSelecionadaId = Integer.parseInt(modeloTabela.getValueAt(linhaModelo, 0).toString());

        selecionarComboPorTexto(comboTurma, modeloTabela.getValueAt(linhaModelo, 1).toString());
        selecionarComboPorTexto(comboDisciplina, modeloTabela.getValueAt(linhaModelo, 2).toString());
        selecionarComboPorTexto(comboAluno, modeloTabela.getValueAt(linhaModelo, 3).toString());

        campoNota1.setText(modeloTabela.getValueAt(linhaModelo, 4).toString());
        campoNota2.setText(modeloTabela.getValueAt(linhaModelo, 5).toString());
    }

    private void cadastrarNota() {
        Item turma = (Item) comboTurma.getSelectedItem();
        Item aluno = (Item) comboAluno.getSelectedItem();
        Item disciplina = (Item) comboDisciplina.getSelectedItem();

        if (turma == null || aluno == null || disciplina == null) {
            JOptionPane.showMessageDialog(this, "Cadastre os relacionamentos de turma, aluno e disciplina antes.");
            return;
        }

        try {
            double nota1 = lerNota(campoNota1.getText());
            double nota2 = lerNota(campoNota2.getText());
            int professorId = buscarProfessorId();

            if (NotaDao.salvar(professorId, aluno.id, turma.id, disciplina.id, nota1, nota2)) {
                JOptionPane.showMessageDialog(this, "Nota cadastrada com sucesso!");
                carregarTabela();
                limparCampos();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Não foi possível cadastrar. Verifique os relacionamentos e as notas.");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Digite notas válidas entre 0 e 10.");
        }
    }

    private void alterarNota() {
        if (notaSelecionadaId < 0) {
            JOptionPane.showMessageDialog(this, "Selecione uma nota antes de alterar.");
            return;
        }

        Item turma = (Item) comboTurma.getSelectedItem();
        Item aluno = (Item) comboAluno.getSelectedItem();
        Item disciplina = (Item) comboDisciplina.getSelectedItem();

        if (turma == null || aluno == null || disciplina == null) {
            JOptionPane.showMessageDialog(this, "Selecione turma, aluno e disciplina.");
            return;
        }

        try {
            double nota1 = lerNota(campoNota1.getText());
            double nota2 = lerNota(campoNota2.getText());
            int professorId = buscarProfessorId();

            if (NotaDao.alterar(notaSelecionadaId, professorId, aluno.id, turma.id, disciplina.id, nota1, nota2)) {
                JOptionPane.showMessageDialog(this, "Nota alterada com sucesso!");
                carregarTabela();
                limparCampos();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Não foi possível alterar. Verifique os relacionamentos e as notas.");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Digite notas válidas entre 0 e 10.");
        }
    }

    private void excluirNota() {
        int linha = tabela.getSelectedRow();
        if (linha < 0) {
            JOptionPane.showMessageDialog(this, "Selecione uma nota na tabela!");
            return;
        }

        int linhaModelo = tabela.convertRowIndexToModel(linha);
        int id = Integer.parseInt(modeloTabela.getValueAt(linhaModelo, 0).toString());

        int confirmacao = JOptionPane.showConfirmDialog(
                this,
                "Deseja excluir esta nota?",
                "Confirmar exclusão",
                JOptionPane.YES_NO_OPTION);

        if (confirmacao == JOptionPane.YES_OPTION && NotaDao.excluir(id)) {
            JOptionPane.showMessageDialog(this, "Nota excluída com sucesso!");
            carregarTabela();
            limparCampos();
        }
    }

    private int buscarProfessorId() {
        for (model.Professor professor : dao.ProfessorDao.listar()) {
            if (professor.getEmail().equals(Login.email_logado)) {
                return professor.getId();
            }
        }
        return -1;
    }

    private double lerNota(String texto) {
        double nota = Double.parseDouble(texto.trim().replace(',', '.'));
        if (nota < 0 || nota > 10) {
            throw new NumberFormatException();
        }
        return nota;
    }

    private void selecionarComboPorTexto(JComboBox<Item> combo, String texto) {
        for (int i = 0; i < combo.getItemCount(); i++) {
            if (combo.getItemAt(i).texto.equals(texto)) {
                combo.setSelectedIndex(i);
                return;
            }
        }
    }

    private void limparCampos() {
        notaSelecionadaId = -1;
        campoNota1.setText("");
        campoNota2.setText("");
        tabela.clearSelection();
    }

    private void voltarMenu() {
        Login.email_logado = "";
        Menu menu = new Menu();
        menu.setVisible(true);
        setVisible(false);
    }

    private static class Item {
        private final int id;
        private final String texto;

        Item(int id, String texto) {
            this.id = id;
            this.texto = texto;
        }

        @Override
        public String toString() {
            return texto;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Professor().setVisible(true));
    }
}
