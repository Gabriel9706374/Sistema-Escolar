package test;

import dao.AcademicoDao;
import dao.AlunoDao;
import dao.ProfessorDao;
import dao.conexao;
import model.Aluno;
import model.Professor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AcademicoDaoTest {

    @BeforeEach
    public void preparar() {
        conexao.criarTabelas();
        limparBanco.limpar();
    }

    @Test
    public void deveCadastrarDisciplina() {
        assertTrue(AcademicoDao.cadastrarDisciplina("Matematica"));
        assertEquals(1, AcademicoDao.listarDisciplinas().size());
    }

    @Test
    public void deveCadastrarTurma() {
        assertTrue(AcademicoDao.cadastrarTurma("Turma A", 2026));
        assertEquals(1, AcademicoDao.listarTurmas().size());
    }

    @Test
    public void deveRelacionarProfessorAlunoDisciplinaETurma() {
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
    public void naoDeveRelacionarDadosInexistentes() {
        assertFalse(AcademicoDao.relacionar(
                "Professor inexistente",
                "Aluno inexistente",
                "Disciplina inexistente",
                "Turma inexistente"
        ));
    }
}
