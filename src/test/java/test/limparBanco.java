package test;

import dao.conexao;
import java.sql.Connection;
import java.sql.Statement;

public class limparBanco {

    public static void limpar() {
        try (Connection c = conexao.conectar();
             Statement comando = c.createStatement()) {

            comando.executeUpdate("DELETE FROM nota");
            comando.executeUpdate("DELETE FROM aluno_turma");
            comando.executeUpdate("DELETE FROM turma_disciplina");
            comando.executeUpdate("DELETE FROM professor_turma");
            comando.executeUpdate("DELETE FROM professor_disciplina");
            comando.executeUpdate("DELETE FROM administrador");
            comando.executeUpdate("DELETE FROM aluno");
            comando.executeUpdate("DELETE FROM professor");
            comando.executeUpdate("DELETE FROM disciplina");
            comando.executeUpdate("DELETE FROM turma");

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
