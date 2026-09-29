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
    public void deveCriarRelacionamentosSeparadamente() {
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

        assertEquals(1, AcademicoDao.listarProfessorDisciplina().size());
        assertEquals(1, AcademicoDao.listarProfessorTurma().size());
        assertEquals(1, AcademicoDao.listarAlunoTurma().size());
        assertEquals(1, AcademicoDao.listarTurmaDisciplina().size());
    }

    @Test
    public void deveRemoverRelacionamentosSeparadamente() {
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

        assertTrue(AcademicoDao.removerProfessorDisciplina(professorId, disciplinaId));
        assertTrue(AcademicoDao.removerProfessorTurma(professorId, turmaId));
        assertTrue(AcademicoDao.removerAlunoTurma(alunoId, turmaId));
        assertTrue(AcademicoDao.removerTurmaDisciplina(turmaId, disciplinaId));

        assertEquals(0, AcademicoDao.listarProfessorDisciplina().size());
        assertEquals(0, AcademicoDao.listarProfessorTurma().size());
        assertEquals(0, AcademicoDao.listarAlunoTurma().size());
        assertEquals(0, AcademicoDao.listarTurmaDisciplina().size());
    }
}
