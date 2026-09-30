package test;

import dao.AlunoDao;
import dao.conexao;
import model.Aluno;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class AlunoDaoTest {

    @BeforeEach
    public void preparar() {
        conexao.criarTabelas();
        limparBanco.limpar();
    }

    private Aluno aluno() {
        return new Aluno(0, "Joao", 18, "joao@email.com", "1234", "ATIVO");
    }

    @Test
    public void deveCadastrarAluno() {
        assertTrue(AlunoDao.cadastrar(aluno()));
        assertTrue(AlunoDao.existeAluno());
    }

    @Test
    public void deveListarAluno() {
        AlunoDao.cadastrar(aluno());

        List<Aluno> lista = AlunoDao.listar();

        assertEquals(1, lista.size());
        assertEquals("Joao", lista.get(0).getNome());
    }

    @Test
    public void deveAutenticarAlunoAtivo() {
        AlunoDao.cadastrar(aluno());
        assertTrue(AlunoDao.autenticar("joao@email.com", "1234"));
    }

    @Test
    public void naoDeveAutenticarSenhaErrada() {
        AlunoDao.cadastrar(aluno());
        assertFalse(AlunoDao.autenticar("joao@email.com", "9999"));
    }

    @Test
    public void naoDeveAutenticarAlunoInativo() {
        Aluno inativo = new Aluno(0, "Joao", 18, "joao@email.com", "1234", "INATIVO");
        AlunoDao.cadastrar(inativo);
        assertFalse(AlunoDao.autenticar("joao@email.com", "1234"));
    }

    @Test
    public void deveExcluirAluno() {
        AlunoDao.cadastrar(aluno());

        int id = AlunoDao.listar().get(0).getId();

        assertTrue(AlunoDao.excluir(id));
        assertEquals(0, AlunoDao.listar().size());
    }
}
