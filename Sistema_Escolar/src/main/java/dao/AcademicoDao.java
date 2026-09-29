package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.Aluno;
import model.Disciplina;
import model.Professor;
import model.Turma;

public class AcademicoDao {

    public static boolean cadastrarDisciplina(String materia) {
        String sql = "INSERT INTO disciplina(materia) VALUES(?)";
        try (Connection c = conexao.conectar();
             PreparedStatement p = c.prepareStatement(sql)) {
            p.setString(1, materia);
            p.executeUpdate();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public static boolean alterarDisciplina(Disciplina disciplina) {
        String sql = "UPDATE disciplina SET materia=? WHERE id=?";
        try (Connection c = conexao.conectar();
             PreparedStatement p = c.prepareStatement(sql)) {
            p.setString(1, disciplina.getMateria());
            p.setInt(2, disciplina.getId());
            return p.executeUpdate() > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    public static boolean excluirDisciplina(int id) {
        String sql = "DELETE FROM disciplina WHERE id=?";
        try (Connection c = conexao.conectar();
             PreparedStatement p = c.prepareStatement(sql)) {
            p.setInt(1, id);
            return p.executeUpdate() > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    public static boolean cadastrarTurma(String nome, int ano) {
        String sql = "INSERT INTO turma(nome,ano) VALUES(?,?)";
        try (Connection c = conexao.conectar();
             PreparedStatement p = c.prepareStatement(sql)) {
            p.setString(1, nome);
            p.setInt(2, ano);
            p.executeUpdate();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public static boolean alterarTurma(Turma turma) {
        String sql = "UPDATE turma SET nome=?, ano=? WHERE id=?";
        try (Connection c = conexao.conectar();
             PreparedStatement p = c.prepareStatement(sql)) {
            p.setString(1, turma.getNome());
            p.setInt(2, turma.getAno());
            p.setInt(3, turma.getId());
            return p.executeUpdate() > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    public static boolean excluirTurma(int id) {
        String sql = "DELETE FROM turma WHERE id=?";
        try (Connection c = conexao.conectar();
             PreparedStatement p = c.prepareStatement(sql)) {
            p.setInt(1, id);
            return p.executeUpdate() > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    public static List<Disciplina> listarDisciplinas() {
        List<Disciplina> lista = new ArrayList<>();
        String sql = "SELECT id,materia FROM disciplina ORDER BY id";
        try (Connection c = conexao.conectar();
             PreparedStatement p = c.prepareStatement(sql);
             ResultSet r = p.executeQuery()) {
            while (r.next()) {
                lista.add(new Disciplina(r.getInt("id"), r.getString("materia")));
            }
        } catch (SQLException e) {
            System.out.println("Erro ao listar disciplinas: " + e.getMessage());
        }
        return lista;
    }

    public static List<Turma> listarTurmas() {
        List<Turma> lista = new ArrayList<>();
        String sql = "SELECT id,nome,ano FROM turma ORDER BY id";
        try (Connection c = conexao.conectar();
             PreparedStatement p = c.prepareStatement(sql);
             ResultSet r = p.executeQuery()) {
            while (r.next()) {
                lista.add(new Turma(r.getInt("id"), r.getString("nome"), r.getInt("ano")));
            }
        } catch (SQLException e) {
            System.out.println("Erro ao listar turmas: " + e.getMessage());
        }
        return lista;
    }

    public static List<Professor> listarProfessores() {
        List<Professor> lista = new ArrayList<>();
        String sql = "SELECT id,nome,idade,email,senha FROM professor ORDER BY nome";
        try (Connection c = conexao.conectar();
             PreparedStatement p = c.prepareStatement(sql);
             ResultSet r = p.executeQuery()) {
            while (r.next()) {
                lista.add(new Professor(r.getInt("id"), r.getString("nome"), r.getInt("idade"),
                        r.getString("email"), r.getString("senha")));
            }
        } catch (SQLException e) {
            System.out.println("Erro ao listar professores: " + e.getMessage());
        }
        return lista;
    }

    public static List<Aluno> listarAlunos() {
        List<Aluno> lista = new ArrayList<>();
        String sql = "SELECT id,nome,idade,email,senha,status FROM aluno ORDER BY nome";
        try (Connection c = conexao.conectar();
             PreparedStatement p = c.prepareStatement(sql);
             ResultSet r = p.executeQuery()) {
            while (r.next()) {
                lista.add(new Aluno(r.getInt("id"), r.getString("nome"), r.getInt("idade"),
                        r.getString("email"), r.getString("senha"), r.getString("status")));
            }
        } catch (SQLException e) {
            System.out.println("Erro ao listar alunos: " + e.getMessage());
        }
        return lista;
    }

    public static boolean adicionarProfessorDisciplina(int professorId, int disciplinaId) {
        return inserirRelacionamento("INSERT OR IGNORE INTO professor_disciplina(professor_id, disciplina_id) VALUES(?, ?)", professorId, disciplinaId);
    }

    public static boolean removerProfessorDisciplina(int professorId, int disciplinaId) {
        return removerRelacionamento("DELETE FROM professor_disciplina WHERE professor_id=? AND disciplina_id=?", professorId, disciplinaId);
    }

    public static List<Object[]> listarProfessorDisciplina() {
        return listarRelacionamento("""
            SELECT p.id, p.nome, d.id, d.materia
            FROM professor_disciplina pd
            JOIN professor p ON p.id=pd.professor_id
            JOIN disciplina d ON d.id=pd.disciplina_id
            ORDER BY p.nome, d.materia
            """);
    }

    public static boolean adicionarProfessorTurma(int professorId, int turmaId) {
        return inserirRelacionamento("INSERT OR IGNORE INTO professor_turma(professor_id, turma_id) VALUES(?, ?)", professorId, turmaId);
    }

    public static boolean removerProfessorTurma(int professorId, int turmaId) {
        return removerRelacionamento("DELETE FROM professor_turma WHERE professor_id=? AND turma_id=?", professorId, turmaId);
    }

    public static List<Object[]> listarProfessorTurma() {
        return listarRelacionamento("""
            SELECT p.id, p.nome, t.id, t.nome
            FROM professor_turma pt
            JOIN professor p ON p.id=pt.professor_id
            JOIN turma t ON t.id=pt.turma_id
            ORDER BY p.nome, t.nome
            """);
    }

    public static boolean adicionarAlunoTurma(int alunoId, int turmaId) {
        return inserirRelacionamento("INSERT OR IGNORE INTO aluno_turma(aluno_id, turma_id) VALUES(?, ?)", alunoId, turmaId);
    }

    public static boolean removerAlunoTurma(int alunoId, int turmaId) {
        return removerRelacionamento("DELETE FROM aluno_turma WHERE aluno_id=? AND turma_id=?", alunoId, turmaId);
    }

    public static List<Object[]> listarAlunoTurma() {
        return listarRelacionamento("""
            SELECT a.id, a.nome, t.id, t.nome
            FROM aluno_turma at
            JOIN aluno a ON a.id=at.aluno_id
            JOIN turma t ON t.id=at.turma_id
            ORDER BY a.nome, t.nome
            """);
    }

    public static boolean adicionarTurmaDisciplina(int turmaId, int disciplinaId) {
        return inserirRelacionamento("INSERT OR IGNORE INTO turma_disciplina(turma_id, disciplina_id) VALUES(?, ?)", turmaId, disciplinaId);
    }

    public static boolean removerTurmaDisciplina(int turmaId, int disciplinaId) {
        return removerRelacionamento("DELETE FROM turma_disciplina WHERE turma_id=? AND disciplina_id=?", turmaId, disciplinaId);
    }

    public static List<Object[]> listarTurmaDisciplina() {
        return listarRelacionamento("""
            SELECT t.id, t.nome, d.id, d.materia
            FROM turma_disciplina td
            JOIN turma t ON t.id=td.turma_id
            JOIN disciplina d ON d.id=td.disciplina_id
            ORDER BY t.nome, d.materia
            """);
    }

    private static boolean inserirRelacionamento(String sql, int primeiroId, int segundoId) {
        try (Connection c = conexao.conectar();
             PreparedStatement p = c.prepareStatement(sql)) {
            p.setInt(1, primeiroId);
            p.setInt(2, segundoId);
            return p.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Erro ao adicionar relacionamento: " + e.getMessage());
            return false;
        }
    }

    private static boolean removerRelacionamento(String sql, int primeiroId, int segundoId) {
        try (Connection c = conexao.conectar();
             PreparedStatement p = c.prepareStatement(sql)) {
            p.setInt(1, primeiroId);
            p.setInt(2, segundoId);
            return p.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Erro ao remover relacionamento: " + e.getMessage());
            return false;
        }
    }

    private static List<Object[]> listarRelacionamento(String sql) {
        List<Object[]> lista = new ArrayList<>();
        try (Connection c = conexao.conectar();
             PreparedStatement p = c.prepareStatement(sql);
             ResultSet r = p.executeQuery()) {
            while (r.next()) {
                lista.add(new Object[]{r.getInt(1), r.getString(2), r.getInt(3), r.getString(4)});
            }
        } catch (SQLException e) {
            System.out.println("Erro ao listar relacionamentos: " + e.getMessage());
        }
        return lista;
    }
    public static boolean relacionar(String professor, String aluno, String disciplina, String turma) {

    try (Connection c = conexao.conectar()) {

        int professorId = buscarId(c,
                "SELECT id FROM professor WHERE nome = ?", professor);

        int alunoId = buscarId(c,
                "SELECT id FROM aluno WHERE nome = ?", aluno);

        int disciplinaId = buscarId(c,
                "SELECT id FROM disciplina WHERE materia = ?", disciplina);

        int turmaId = buscarId(c,
                "SELECT id FROM turma WHERE nome = ?", turma);

        if (professorId == -1 || alunoId == -1
                || disciplinaId == -1 || turmaId == -1) {
            return false;
        }

        adicionarProfessorDisciplina(professorId, disciplinaId);
        adicionarProfessorTurma(professorId, turmaId);
        adicionarAlunoTurma(alunoId, turmaId);
        adicionarTurmaDisciplina(turmaId, disciplinaId);

        return true;

    } catch (SQLException e) {
        System.out.println("Erro ao relacionar: " + e.getMessage());
        return false;
    }
}

private static int buscarId(Connection c, String sql, String valor) throws SQLException {

    try (PreparedStatement p = c.prepareStatement(sql)) {

        p.setString(1, valor);

        try (ResultSet r = p.executeQuery()) {

            if (r.next()) {
                return r.getInt("id");
            }
        }
    }

    return -1;
}
}
