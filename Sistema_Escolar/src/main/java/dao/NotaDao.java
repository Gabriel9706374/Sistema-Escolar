package dao;

import java.sql.*;

public class NotaDao {

    public static boolean salvar(int professorId, int alunoId, int turmaId, int disciplinaId,
                                 double nota1, double nota2) {
        if (!notaValida(nota1) || !notaValida(nota2)) {
            return false;
        }

        String verificaProfessorTurma = "SELECT 1 FROM professor_turma WHERE professor_id=? AND turma_id=?";
        String verificaProfessorDisciplina = "SELECT 1 FROM professor_disciplina WHERE professor_id=? AND disciplina_id=?";
        String verificaAlunoTurma = "SELECT 1 FROM aluno_turma WHERE aluno_id=? AND turma_id=?";
        String verificaTurmaDisciplina = "SELECT 1 FROM turma_disciplina WHERE turma_id=? AND disciplina_id=?";

        try (Connection c = conexao.conectar()) {
            if (!relacionamentoExiste(c, verificaProfessorTurma, professorId, turmaId)) return false;
            if (!relacionamentoExiste(c, verificaProfessorDisciplina, professorId, disciplinaId)) return false;
            if (!relacionamentoExiste(c, verificaAlunoTurma, alunoId, turmaId)) return false;
            if (!relacionamentoExiste(c, verificaTurmaDisciplina, turmaId, disciplinaId)) return false;

            double notaFinal = calcularNotaFinal(nota1, nota2);
            String status = calcularStatus(notaFinal);

            String sql = """
                INSERT INTO nota(aluno_id,turma_id,disciplina_id,nota1,nota2,nota_final,status)
                VALUES(?,?,?,?,?,?,?)
                ON CONFLICT(aluno_id,turma_id,disciplina_id)
                DO UPDATE SET
                    nota1=excluded.nota1,
                    nota2=excluded.nota2,
                    nota_final=excluded.nota_final,
                    status=excluded.status
                """;

            try (PreparedStatement p = c.prepareStatement(sql)) {
                p.setInt(1, alunoId);
                p.setInt(2, turmaId);
                p.setInt(3, disciplinaId);
                p.setDouble(4, nota1);
                p.setDouble(5, nota2);
                p.setDouble(6, notaFinal);
                p.setString(7, status);
                return p.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            System.out.println("Erro ao salvar nota: " + e.getMessage());
            return false;
        }
    }

    public static boolean alterar(int id, int professorId, int alunoId, int turmaId, int disciplinaId,
                                  double nota1, double nota2) {
        if (!notaValida(nota1) || !notaValida(nota2)) {
            return false;
        }

        String verificaProfessorTurma = "SELECT 1 FROM professor_turma WHERE professor_id=? AND turma_id=?";
        String verificaProfessorDisciplina = "SELECT 1 FROM professor_disciplina WHERE professor_id=? AND disciplina_id=?";
        String verificaAlunoTurma = "SELECT 1 FROM aluno_turma WHERE aluno_id=? AND turma_id=?";
        String verificaTurmaDisciplina = "SELECT 1 FROM turma_disciplina WHERE turma_id=? AND disciplina_id=?";

        try (Connection c = conexao.conectar()) {
            if (!relacionamentoExiste(c, verificaProfessorTurma, professorId, turmaId)) return false;
            if (!relacionamentoExiste(c, verificaProfessorDisciplina, professorId, disciplinaId)) return false;
            if (!relacionamentoExiste(c, verificaAlunoTurma, alunoId, turmaId)) return false;
            if (!relacionamentoExiste(c, verificaTurmaDisciplina, turmaId, disciplinaId)) return false;

            double notaFinal = calcularNotaFinal(nota1, nota2);
            String status = calcularStatus(notaFinal);

            String sql = """
                UPDATE nota
                SET aluno_id=?, turma_id=?, disciplina_id=?, nota1=?, nota2=?, nota_final=?, status=?
                WHERE id=?
                """;

            try (PreparedStatement p = c.prepareStatement(sql)) {
                p.setInt(1, alunoId);
                p.setInt(2, turmaId);
                p.setInt(3, disciplinaId);
                p.setDouble(4, nota1);
                p.setDouble(5, nota2);
                p.setDouble(6, notaFinal);
                p.setString(7, status);
                p.setInt(8, id);
                return p.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            System.out.println("Erro ao alterar nota: " + e.getMessage());
            return false;
        }
    }

    public static boolean salvarPorNomes(String aluno, String turma, String disciplina,
                                         double nota1, double nota2) {
        try (Connection c = conexao.conectar()) {
            int alunoId = buscarId(c, "SELECT id FROM aluno WHERE nome=?", aluno);
            int turmaId = buscarId(c, "SELECT id FROM turma WHERE nome=?", turma);
            int disciplinaId = buscarId(c, "SELECT id FROM disciplina WHERE materia=?", disciplina);

            if (alunoId < 0 || turmaId < 0 || disciplinaId < 0 ||
                    !notaValida(nota1) || !notaValida(nota2)) {
                return false;
            }

            double notaFinal = calcularNotaFinal(nota1, nota2);
            String status = calcularStatus(notaFinal);
            String sql = """
                INSERT INTO nota(aluno_id,turma_id,disciplina_id,nota1,nota2,nota_final,status)
                VALUES(?,?,?,?,?,?,?)
                ON CONFLICT(aluno_id,turma_id,disciplina_id)
                DO UPDATE SET nota1=excluded.nota1,nota2=excluded.nota2,
                              nota_final=excluded.nota_final,status=excluded.status
                """;

            try (PreparedStatement p = c.prepareStatement(sql)) {
                p.setInt(1, alunoId);
                p.setInt(2, turmaId);
                p.setInt(3, disciplinaId);
                p.setDouble(4, nota1);
                p.setDouble(5, nota2);
                p.setDouble(6, notaFinal);
                p.setString(7, status);
                return p.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            System.out.println("Erro ao salvar nota: " + e.getMessage());
            return false;
        }
    }

    public static boolean salvarPorNomes(String aluno, String turma, double nota1, double nota2) {
        try (Connection c = conexao.conectar()) {
            String sql = """
                SELECT d.materia
                FROM turma_disciplina td
                JOIN disciplina d ON d.id=td.disciplina_id
                JOIN aluno_turma at ON at.turma_id=td.turma_id
                JOIN aluno a ON a.id=at.aluno_id
                JOIN turma t ON t.id=td.turma_id
                WHERE a.nome=? AND t.nome=?
                ORDER BY d.materia
                """;
            try (PreparedStatement p = c.prepareStatement(sql)) {
                p.setString(1, aluno);
                p.setString(2, turma);
                try (ResultSet r = p.executeQuery()) {
                    if (!r.next()) return false;
                    String disciplina = r.getString("materia");
                    if (r.next()) {
                        System.out.println("Informe a disciplina para salvar a nota.");
                        return false;
                    }
                    return salvarPorNomes(aluno, turma, disciplina, nota1, nota2);
                }
            }
        } catch (SQLException e) {
            System.out.println("Erro ao localizar disciplina da nota: " + e.getMessage());
            return false;
        }
    }

    public static boolean excluir(int id) {
        try (Connection c = conexao.conectar();
             PreparedStatement p = c.prepareStatement("DELETE FROM nota WHERE id=?")) {
            p.setInt(1, id);
            return p.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Erro ao excluir nota: " + e.getMessage());
            return false;
        }
    }

    private static boolean notaValida(double nota) {
        return nota >= 0 && nota <= 10;
    }

    private static double calcularNotaFinal(double nota1, double nota2) {
        return (nota1 + nota2) / 2.0;
    }

    private static String calcularStatus(double notaFinal) {
        return notaFinal >= 6 ? "APROVADO" : "REPROVADO";
    }

    private static boolean relacionamentoExiste(Connection c, String sql, int primeiroId, int segundoId)
            throws SQLException {
        try (PreparedStatement p = c.prepareStatement(sql)) {
            p.setInt(1, primeiroId);
            p.setInt(2, segundoId);
            try (ResultSet r = p.executeQuery()) {
                return r.next();
            }
        }
    }

    private static int buscarId(Connection c, String sql, String valor) throws SQLException {
        try (PreparedStatement p = c.prepareStatement(sql)) {
            p.setString(1, valor);
            try (ResultSet r = p.executeQuery()) {
                return r.next() ? r.getInt(1) : -1;
            }
        }
    }
}
