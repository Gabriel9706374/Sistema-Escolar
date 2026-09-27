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

        assertTrue(AcademicoDao.relacionar(
                "Carlos",
                "Joao",
                "Matematica",
                "Turma A"
        ));
    }

    @Test
    public void deveSalvarNotaEAprovarAluno() {
        assertTrue(NotaDao.salvarPorNomes("Joao", "Turma A", 8, 6));

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
        assertTrue(NotaDao.salvarPorNomes("Joao", "Turma A", 5, 6));

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
        assertTrue(NotaDao.salvarPorNomes("Joao", "Turma A", 5, 5));
        assertTrue(NotaDao.salvarPorNomes("Joao", "Turma A", 10, 10));

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
        assertFalse(NotaDao.salvarPorNomes("Joao", "Turma A", 11, 8));
    }

    @Test
    public void naoDeveSalvarNotaAbaixoDe0() {
        assertFalse(NotaDao.salvarPorNomes("Joao", "Turma A", -1, 8));
    }

    @Test
    public void deveExcluirNota() {
        assertTrue(NotaDao.salvarPorNomes("Joao", "Turma A", 8, 8));

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
}
