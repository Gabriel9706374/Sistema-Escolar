package test;

import dao.ProfessorDao;
import dao.conexao;
import model.Professor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ProfessorDaoTest {

    @BeforeEach
    public void preparar() {
        conexao.criarTabelas();
        limparBanco.limpar();
    }

    private Professor professor() {
        return new Professor(0, "Carlos", 30, "carlos@email.com", "1234");
    }

    @Test
    public void deveCadastrarProfessor() {
        assertTrue(ProfessorDao.cadastrar(professor()));
        assertTrue(ProfessorDao.existeProfessor());
    }

    @Test
    public void deveListarProfessor() {
        ProfessorDao.cadastrar(professor());

        List<Professor> lista = ProfessorDao.listar();

        assertEquals(1, lista.size());
        assertEquals("Carlos", lista.get(0).getNome());
    }

    @Test
    public void deveAutenticarProfessor() {
        ProfessorDao.cadastrar(professor());
        assertTrue(ProfessorDao.autenticar("carlos@email.com", "1234"));
    }

    @Test
    public void naoDeveAutenticarSenhaErrada() {
        ProfessorDao.cadastrar(professor());
        assertFalse(ProfessorDao.autenticar("carlos@email.com", "9999"));
    }

    @Test
    public void deveAlterarProfessor() {
        ProfessorDao.cadastrar(professor());

        Professor cadastrado = ProfessorDao.listar().get(0);
        cadastrado.setNome("Carlos Silva");
        cadastrado.setIdade(31);

        assertTrue(ProfessorDao.alterar(cadastrado));

        Professor atualizado = ProfessorDao.listar().get(0);
        assertEquals("Carlos Silva", atualizado.getNome());
        assertEquals(31, atualizado.getIdade());
    }

    @Test
    public void deveExcluirProfessor() {
        ProfessorDao.cadastrar(professor());

        int id = ProfessorDao.listar().get(0).getId();

        assertTrue(ProfessorDao.excluir(id));
        assertEquals(0, ProfessorDao.listar().size());
    }
}
