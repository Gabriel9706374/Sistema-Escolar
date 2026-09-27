package view;

import dao.AdministradorDao;
import javax.swing.*;
import java.awt.*;

public class CadastrarAdministrador extends JFrame {
    private JTextField campo_email;
    private JPasswordField campo_senha;

    public CadastrarAdministrador() {
        setTitle("Cadastro do Administrador");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel titulo = new JLabel("Cadastro do Administrador");
        JLabel label_email = new JLabel("Email:");
        JLabel label_senha = new JLabel("Senha:");
        campo_email = new JTextField(20);
        campo_senha = new JPasswordField(20);
        JButton botao_cadastrar = new JButton("Cadastrar");

        botao_cadastrar.addActionListener(e -> cadastrar());

        JPanel painel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(6, 6, 6, 6);

        c.gridx = 0; c.gridy = 0; c.gridwidth = 2;
        painel.add(titulo, c);
        c.gridwidth = 1;
        c.gridx = 0; c.gridy = 1; painel.add(label_email, c);
        c.gridx = 1; painel.add(campo_email, c);
        c.gridx = 0; c.gridy = 2; painel.add(label_senha, c);
        c.gridx = 1; painel.add(campo_senha, c);
        c.gridx = 0; c.gridy = 3; c.gridwidth = 2;
        painel.add(botao_cadastrar, c);

        add(painel);
        pack();
        setLocationRelativeTo(null);
    }

    private void cadastrar() {
        String email = campo_email.getText().trim();
        String senha = new String(campo_senha.getPassword());

        if (email.isEmpty() || senha.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Preencha email e senha.");
            return;
        }

        if (AdministradorDao.cadastrar(email, senha)) {
            JOptionPane.showMessageDialog(this, "Administrador cadastrado com sucesso!");
            Login login = new Login();
            login.setVisible(true);
            this.setVisible(false);
        } else {
            JOptionPane.showMessageDialog(this, "Não foi possível cadastrar o administrador. Verifique se o email já existe.");
        }
    }
}
