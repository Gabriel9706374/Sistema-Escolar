package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class conexao {

    private static final String URL = "jdbc:sqlite:" + System.getProperty("sistema.escolar.db", "sistema_escolar.db");

    public static Connection conectar() {
        try {
            Connection conexao = DriverManager.getConnection(URL);
            try (Statement comando = conexao.createStatement()) {
                comando.execute("PRAGMA foreign_keys = ON");
            }
            return conexao;
        } catch (SQLException e) {
            System.out.println("Erro ao conectar com o banco: " + e.getMessage());
            return null;
        }
    }

    public static void criarTabelas() {
        String tabelaAdministrador = """
            CREATE TABLE IF NOT EXISTS administrador (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nome TEXT NOT NULL,
                email TEXT NOT NULL UNIQUE,
                senha TEXT NOT NULL
            )
            """;

        String tabelaProfessor = """
            CREATE TABLE IF NOT EXISTS professor (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nome TEXT NOT NULL,
                idade INTEGER CHECK (idade >= 0),
                email TEXT NOT NULL UNIQUE,
                senha TEXT NOT NULL
            )
            """;

        String tabelaAluno = """
            CREATE TABLE IF NOT EXISTS aluno (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nome TEXT NOT NULL,
                idade INTEGER CHECK (idade >= 0),
                email TEXT NOT NULL UNIQUE,
                senha TEXT NOT NULL,
                status TEXT NOT NULL DEFAULT 'ATIVO'
            )
            """;

        String tabelaDisciplina = """
            CREATE TABLE IF NOT EXISTS disciplina (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                materia TEXT NOT NULL UNIQUE
            )
            """;

        String tabelaTurma = """
            CREATE TABLE IF NOT EXISTS turma (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nome TEXT NOT NULL,
                ano INTEGER NOT NULL
            )
            """;

        String tabelaProfessorDisciplina = """
            CREATE TABLE IF NOT EXISTS professor_disciplina (
                professor_id INTEGER NOT NULL,
                disciplina_id INTEGER NOT NULL,
                PRIMARY KEY (professor_id, disciplina_id),
                FOREIGN KEY (professor_id) REFERENCES professor(id) ON DELETE CASCADE,
                FOREIGN KEY (disciplina_id) REFERENCES disciplina(id) ON DELETE CASCADE
            )
            """;

        String tabelaProfessorTurma = """
            CREATE TABLE IF NOT EXISTS professor_turma (
                professor_id INTEGER NOT NULL,
                turma_id INTEGER NOT NULL,
                PRIMARY KEY (professor_id, turma_id),
                FOREIGN KEY (professor_id) REFERENCES professor(id) ON DELETE CASCADE,
                FOREIGN KEY (turma_id) REFERENCES turma(id) ON DELETE CASCADE
            )
            """;

        String tabelaTurmaDisciplina = """
            CREATE TABLE IF NOT EXISTS turma_disciplina (
                turma_id INTEGER NOT NULL,
                disciplina_id INTEGER NOT NULL,
                PRIMARY KEY (turma_id, disciplina_id),
                FOREIGN KEY (turma_id) REFERENCES turma(id) ON DELETE CASCADE,
                FOREIGN KEY (disciplina_id) REFERENCES disciplina(id) ON DELETE CASCADE
            )
            """;

        String tabelaAlunoTurma = """
            CREATE TABLE IF NOT EXISTS aluno_turma (
                aluno_id INTEGER NOT NULL,
                turma_id INTEGER NOT NULL,
                PRIMARY KEY (aluno_id, turma_id),
                FOREIGN KEY (aluno_id) REFERENCES aluno(id) ON DELETE CASCADE,
                FOREIGN KEY (turma_id) REFERENCES turma(id) ON DELETE CASCADE
            )
            """;

        String tabelaNota = """
            CREATE TABLE IF NOT EXISTS nota (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                aluno_id INTEGER NOT NULL,
                turma_id INTEGER NOT NULL,
                disciplina_id INTEGER NOT NULL,
                nota1 REAL CHECK (nota1 >= 0 AND nota1 <= 10),
                nota2 REAL CHECK (nota2 >= 0 AND nota2 <= 10),
                nota_final REAL CHECK (nota_final >= 0 AND nota_final <= 10),
                status TEXT NOT NULL DEFAULT 'PENDENTE',
                UNIQUE (aluno_id, turma_id, disciplina_id),
                FOREIGN KEY (aluno_id) REFERENCES aluno(id) ON DELETE CASCADE,
                FOREIGN KEY (turma_id) REFERENCES turma(id) ON DELETE CASCADE,
                FOREIGN KEY (disciplina_id) REFERENCES disciplina(id) ON DELETE CASCADE
            )
            """;

        Connection conexao = conectar();
        if (conexao == null) {
            return;
        }

        try (conexao; Statement comando = conexao.createStatement()) {
            comando.executeUpdate(tabelaAdministrador);
            comando.executeUpdate(tabelaProfessor);
            comando.executeUpdate(tabelaAluno);
            comando.executeUpdate(tabelaDisciplina);
            comando.executeUpdate(tabelaTurma);
            comando.executeUpdate(tabelaProfessorDisciplina);
            comando.executeUpdate(tabelaProfessorTurma);
            comando.executeUpdate(tabelaTurmaDisciplina);
            comando.executeUpdate(tabelaAlunoTurma);
            comando.executeUpdate(tabelaNota);

            System.out.println("Banco de dados e tabelas criados com sucesso!");
        } catch (SQLException e) {
            System.out.println("Erro ao criar as tabelas: " + e.getMessage());
        }
    }
}
