package com.mycompany.sistema_escolar;

import dao.conexao;
import view.Menu;

public class Sistema_Escolar {

    public static void main(String[] args) {
        conexao.criarTabelas();

        Menu menu = new Menu();
        menu.setVisible(true);
    }
}
