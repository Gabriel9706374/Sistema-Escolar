package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.Disciplina;
import model.Turma;

public class AcademicoDao {
    public static boolean cadastrarDisciplina(String materia){
        try(Connection c=conexao.conectar();PreparedStatement p=c.prepareStatement("INSERT INTO disciplina(materia) VALUES(?)")){p.setString(1,materia);p.executeUpdate();return true;}catch(SQLException e){return false;}
    }
    public static boolean cadastrarTurma(String nome,int ano){
        try(Connection c=conexao.conectar();PreparedStatement p=c.prepareStatement("INSERT INTO turma(nome,ano) VALUES(?,?)")){p.setString(1,nome);p.setInt(2,ano);p.executeUpdate();return true;}catch(SQLException e){return false;}
    }
    public static List<Disciplina> listarDisciplinas(){
        List<Disciplina> l=new ArrayList<>(); try(Connection c=conexao.conectar();PreparedStatement p=c.prepareStatement("SELECT id,materia FROM disciplina ORDER BY id");ResultSet r=p.executeQuery()){while(r.next())l.add(new Disciplina(r.getInt(1),r.getString(2)));}catch(SQLException e){} return l;
    }
    public static List<Turma> listarTurmas(){
        List<Turma> l=new ArrayList<>(); try(Connection c=conexao.conectar();PreparedStatement p=c.prepareStatement("SELECT id,nome,ano FROM turma ORDER BY id");ResultSet r=p.executeQuery()){while(r.next())l.add(new Turma(r.getInt(1),r.getString(2),r.getInt(3)));}catch(SQLException e){} return l;
    }
    public static boolean relacionar(String professor,String aluno,String disciplina,String turma){
        try(Connection c=conexao.conectar()){
            c.setAutoCommit(false);
            int professorId=buscarId(c,"SELECT id FROM professor WHERE nome=?",professor);
            int alunoId=buscarId(c,"SELECT id FROM aluno WHERE nome=?",aluno);
            int disciplinaId=buscarId(c,"SELECT id FROM disciplina WHERE materia=?",disciplina);
            int turmaId=buscarId(c,"SELECT id FROM turma WHERE nome=?",turma);
            if(professorId<0||alunoId<0||disciplinaId<0||turmaId<0){c.rollback();return false;}
            inserirRelacionamento(c,"INSERT OR IGNORE INTO professor_disciplina(professor_id,disciplina_id) VALUES(?,?)",professorId,disciplinaId);
            inserirRelacionamento(c,"INSERT OR IGNORE INTO professor_turma(professor_id,turma_id) VALUES(?,?)",professorId,turmaId);
            inserirRelacionamento(c,"INSERT OR IGNORE INTO turma_disciplina(turma_id,disciplina_id) VALUES(?,?)",turmaId,disciplinaId);
            inserirRelacionamento(c,"INSERT OR IGNORE INTO aluno_turma(aluno_id,turma_id) VALUES(?,?)",alunoId,turmaId);
            c.commit(); return true;
        }catch(SQLException e){return false;}
    }
    private static int buscarId(Connection c,String sql,String valor)throws SQLException{try(PreparedStatement p=c.prepareStatement(sql)){p.setString(1,valor);try(ResultSet r=p.executeQuery()){return r.next()?r.getInt(1):-1;}}}
    private static void inserirRelacionamento(Connection c,String sql,int a,int b)throws SQLException{try(PreparedStatement p=c.prepareStatement(sql)){p.setInt(1,a);p.setInt(2,b);p.executeUpdate();}}
}
