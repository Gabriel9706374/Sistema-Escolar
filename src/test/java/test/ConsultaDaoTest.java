package test;

import dao.AcademicoDao;
import dao.AlunoDao;
import dao.ConsultaDao;
import dao.NotaDao;
import dao.ProfessorDao;
import dao.conexao;
import model.Aluno;
import model.Professor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ConsultaDaoTest {

    @BeforeEach
    public void preparar() {
        conexao.criarTabelas();
        limparBanco.limpar();

        assertTrue(ProfessorDao.cadastrar(
                new Professor(0, "Carlos", 30, "carlos@email.com", "1234")
        ));

        assertTrue(AlunoDao.cadastrar(
                new Aluno(0, "Joao", 18, "joao@email.com", "1234", "ATIVO")
        ));

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

        assertTrue(NotaDao.salvarPorNomes(
                "Joao", "Turma A", "Matematica", 8, 6
        ));
    }

    @Test
    public void deveConsultarResumoAdministrador() {
        List<Object[]> lista = ConsultaDao.resumoAdministrador();

        assertEquals(1, lista.size());
        assertEquals("Carlos", lista.get(0)[0]);
        assertEquals("Joao", lista.get(0)[1]);
        assertEquals("Matematica", lista.get(0)[2]);
        assertEquals("Turma A", lista.get(0)[3]);
    }

    @Test
    public void deveConsultarNotasDoProfessor() {
        List<Object[]> lista =
                ConsultaDao.notasProfessor("carlos@email.com");

        assertEquals(1, lista.size());
        assertEquals("Turma A", lista.get(0)[1]);
        assertEquals("Matematica", lista.get(0)[2]);
        assertEquals("Joao", lista.get(0)[3]);
        assertEquals(8.0, (Double) lista.get(0)[4]);
        assertEquals(6.0, (Double) lista.get(0)[5]);
        assertEquals(7.0, (Double) lista.get(0)[6]);
        assertEquals("APROVADO", lista.get(0)[7]);
    }

    @Test
    public void deveConsultarNotasDoAluno() {
        List<Object[]> lista =
                ConsultaDao.notasAluno("joao@email.com");

        assertEquals(1, lista.size());
        assertEquals("Matematica", lista.get(0)[0]);
        assertEquals("Turma A", lista.get(0)[1]);
        assertEquals(8.0, (Double) lista.get(0)[2]);
        assertEquals(6.0, (Double) lista.get(0)[3]);
        assertEquals(7.0, (Double) lista.get(0)[4]);
        assertEquals("APROVADO", lista.get(0)[5]);
    }
}
