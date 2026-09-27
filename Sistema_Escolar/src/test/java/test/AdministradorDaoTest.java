package test;

import dao.AdministradorDao;
import dao.conexao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AdministradorDaoTest {

    @BeforeEach
    public void preparar() {
        conexao.criarTabelas();
        limparBanco.limpar();
    }

    @Test
    public void deveCadastrarAdministrador() {
        assertTrue(AdministradorDao.cadastrar("admin@email.com", "1234"));
        assertTrue(AdministradorDao.existeAdministrador());
    }

    @Test
    public void deveAutenticarAdministrador() {
        AdministradorDao.cadastrar("admin@email.com", "1234");
        assertTrue(AdministradorDao.autenticar("admin@email.com", "1234"));
    }

    @Test
    public void naoDeveAutenticarSenhaErrada() {
        AdministradorDao.cadastrar("admin@email.com", "1234");
        assertFalse(AdministradorDao.autenticar("admin@email.com", "9999"));
    }

    @Test
    public void naoDeveCadastrarEmailDuplicado() {
        AdministradorDao.cadastrar("admin@email.com", "1234");
        assertFalse(AdministradorDao.cadastrar("admin@email.com", "5678"));
    }
}
