package test;

import dao.AcademicoDao;
import dao.AlunoDao;
import dao.NotaDao;
import dao.ProfessorDao;
import dao.conexao;
import model.Aluno;
import model.Professor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import static org.junit.jupiter.api.Assertions.*;

public class NotaDaoTest {

    @BeforeEach
    public void preparar() {
        conexao.criarTabelas();
        limparBanco.limpar();

        Professor professor = new Professor(0, "Carlos", 30, "carlos@email.com", "1234");
        Aluno aluno = new Aluno(0, "Joao", 18, "joao@email.com", "1234", "ATIVO");

        assertTrue(ProfessorDao.cadastrar(professor));
        assertTrue(AlunoDao.cadastrar(aluno));
        assertTrue(AcademicoDao.cadastrarDisciplina("Matematica"));
        assertTrue(AcademicoDao.cadastrarTurma("Turma A", 2026));

        int professorId = AcademicoDao.listarProfessores().get(0).getId();
        int alunoId = AcademicoDao.listarAlunos().get(0).getId();
        int disciplinaId = AcademicoDao.listarDisciplinas().get(0).getId();
        int turmaId = AcademicoDao.listarTurmas().get(0).getId();

        assertTrue(AcademicoDao.adicionarProfessorDisciplina(professorId, disciplinaId));
        assertTrue(AcademicoDao.adicionarProfessorTurma(professorId, turmaId));
        assertTrue(AcademicoDao.adicionarAlunoTurma(alunoId, turmaId));
        assertTrue(AcademicoDao.adicionarTurmaDisciplina(turmaId, disciplinaId));
    }

    @Test
    public void deveSalvarNotaEAprovarAluno() {
        assertTrue(NotaDao.salvarPorNomes("Joao", "Turma A", "Matematica", 8, 6));

        try (Connection c = conexao.conectar();
             PreparedStatement p = c.prepareStatement("SELECT nota_final,status FROM nota");
             ResultSet r = p.executeQuery()) {

            assertTrue(r.next());
            assertEquals(7.0, r.getDouble("nota_final"));
            assertEquals("APROVADO", r.getString("status"));

        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void deveReprovarAluno() {
        assertTrue(NotaDao.salvarPorNomes("Joao", "Turma A", "Matematica", 5, 6));

        try (Connection c = conexao.conectar();
             PreparedStatement p = c.prepareStatement("SELECT nota_final,status FROM nota");
             ResultSet r = p.executeQuery()) {

            assertTrue(r.next());
            assertEquals(5.5, r.getDouble("nota_final"));
            assertEquals("REPROVADO", r.getString("status"));

        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void deveAtualizarNotaExistente() {
        assertTrue(NotaDao.salvarPorNomes("Joao", "Turma A", "Matematica", 5, 5));
        assertTrue(NotaDao.salvarPorNomes("Joao", "Turma A", "Matematica", 10, 10));

        try (Connection c = conexao.conectar();
             PreparedStatement p = c.prepareStatement("SELECT COUNT(*),nota_final,status FROM nota");
             ResultSet r = p.executeQuery()) {

            assertTrue(r.next());
            assertEquals(1, r.getInt(1));
            assertEquals(10.0, r.getDouble(2));
            assertEquals("APROVADO", r.getString(3));

        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void naoDeveSalvarNotaAcimaDe10() {
        assertFalse(NotaDao.salvarPorNomes("Joao", "Turma A", "Matematica", 11, 8));
    }

    @Test
    public void naoDeveSalvarNotaAbaixoDe0() {
        assertFalse(NotaDao.salvarPorNomes("Joao", "Turma A", "Matematica", -1, 8));
    }

    @Test
    public void deveExcluirNota() {
        assertTrue(NotaDao.salvarPorNomes("Joao", "Turma A", "Matematica", 8, 8));

        try (Connection c = conexao.conectar();
             PreparedStatement p = c.prepareStatement("SELECT id FROM nota");
             ResultSet r = p.executeQuery()) {

            assertTrue(r.next());
            int id = r.getInt("id");

            assertTrue(NotaDao.excluir(id));

        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
    @Test
    public void deveAlterarNota() {
        int professorId = AcademicoDao.listarProfessores().get(0).getId();
        int alunoId = AcademicoDao.listarAlunos().get(0).getId();
        int disciplinaId = AcademicoDao.listarDisciplinas().get(0).getId();
        int turmaId = AcademicoDao.listarTurmas().get(0).getId();

        assertTrue(NotaDao.salvar(professorId, alunoId, turmaId, disciplinaId, 5, 5));

        try (Connection c = conexao.conectar();
             PreparedStatement p = c.prepareStatement("SELECT id FROM nota")) {
            try (ResultSet r = p.executeQuery()) {
                assertTrue(r.next());
                int id = r.getInt("id");
                assertTrue(NotaDao.alterar(id, professorId, alunoId, turmaId, disciplinaId, 9, 8));
            }
        } catch (Exception e) {
            fail(e.getMessage());
        }

        try (Connection c = conexao.conectar();
             PreparedStatement p = c.prepareStatement("SELECT nota_final,status FROM nota");
             ResultSet r = p.executeQuery()) {
            assertTrue(r.next());
            assertEquals(8.5, r.getDouble("nota_final"));
            assertEquals("APROVADO", r.getString("status"));
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void devePermitirNotasEmDisciplinasDiferentes() {
        assertTrue(AcademicoDao.cadastrarDisciplina("Portugues"));
        int turmaId = AcademicoDao.listarTurmas().get(0).getId();
        int portuguesId = AcademicoDao.listarDisciplinas().stream()
                .filter(d -> d.getMateria().equals("Portugues"))
                .findFirst().get().getId();
        int professorId = AcademicoDao.listarProfessores().get(0).getId();
        int alunoId = AcademicoDao.listarAlunos().get(0).getId();

        assertTrue(AcademicoDao.adicionarTurmaDisciplina(turmaId, portuguesId));
        assertTrue(AcademicoDao.adicionarProfessorDisciplina(professorId, portuguesId));
        assertTrue(NotaDao.salvar(professorId, alunoId, turmaId, portuguesId, 9, 9));

        try (Connection c = conexao.conectar();
             PreparedStatement p = c.prepareStatement("SELECT COUNT(*) FROM nota WHERE disciplina_id=?")) {
            p.setInt(1, portuguesId);
            try (ResultSet r = p.executeQuery()) {
                assertTrue(r.next());
                assertEquals(1, r.getInt(1));
            }
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

}
