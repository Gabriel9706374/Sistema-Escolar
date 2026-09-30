package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ConsultaDao {
    public static List<Object[]> resumoAdministrador(){
        List<Object[]> l=new ArrayList<>();
        String sql="""
            SELECT p.nome, a.nome, d.materia, t.nome
            FROM professor p
            JOIN professor_turma pt ON pt.professor_id=p.id
            JOIN turma t ON t.id=pt.turma_id
            JOIN turma_disciplina td ON td.turma_id=t.id
            JOIN disciplina d ON d.id=td.disciplina_id
            LEFT JOIN aluno_turma at ON at.turma_id=t.id
            LEFT JOIN aluno a ON a.id=at.aluno_id
            ORDER BY p.nome,a.nome,d.materia,t.nome
            """;
        try(Connection c=conexao.conectar();PreparedStatement p=c.prepareStatement(sql);ResultSet r=p.executeQuery()){
            while(r.next()) l.add(new Object[]{r.getString(1),r.getString(2),r.getString(3),r.getString(4)});
        }catch(SQLException e){System.out.println("Erro ao consultar dados: "+e.getMessage());}
        return l;
    }

    public static List<Object[]> notasProfessor(String email){
        List<Object[]> l=new ArrayList<>();
        String sql="""
            SELECT n.id,t.nome,d.materia,a.nome,n.nota1,n.nota2,n.nota_final,n.status
            FROM nota n
            JOIN aluno a ON a.id=n.aluno_id
            JOIN turma t ON t.id=n.turma_id
            JOIN disciplina d ON d.id=n.disciplina_id
            JOIN professor_turma pt ON pt.turma_id=t.id
            JOIN professor_disciplina pd ON pd.disciplina_id=d.id
            JOIN professor p ON p.id=pt.professor_id AND p.id=pd.professor_id
            WHERE p.email=?
            ORDER BY t.nome,d.materia,a.nome
            """;
        try(Connection c=conexao.conectar();PreparedStatement p=c.prepareStatement(sql)){
            p.setString(1,email);
            try(ResultSet r=p.executeQuery()){
                while(r.next()) l.add(new Object[]{r.getInt(1),r.getString(2),r.getString(3),r.getString(4),
                        r.getDouble(5),r.getDouble(6),r.getDouble(7),r.getString(8)});
            }
        }catch(SQLException e){System.out.println("Erro ao consultar notas: "+e.getMessage());}
        return l;
    }

    public static List<Object[]> notasAluno(String email){
        List<Object[]> l=new ArrayList<>();
        String sql="""
            SELECT d.materia,t.nome,n.nota1,n.nota2,n.nota_final,n.status
            FROM nota n
            JOIN aluno a ON a.id=n.aluno_id
            JOIN disciplina d ON d.id=n.disciplina_id
            JOIN turma t ON t.id=n.turma_id
            WHERE a.email=?
            ORDER BY t.nome,d.materia
            """;
        try(Connection c=conexao.conectar();PreparedStatement p=c.prepareStatement(sql)){
            p.setString(1,email);
            try(ResultSet r=p.executeQuery()){
                while(r.next()) l.add(new Object[]{r.getString(1),r.getString(2),r.getDouble(3),r.getDouble(4),r.getDouble(5),r.getString(6)});
            }
        }catch(SQLException e){System.out.println("Erro ao consultar notas do aluno: "+e.getMessage());}
        return l;
    }

    public static List<Object[]> turmasProfessor(String email){
        return listarDoProfessor("""
            SELECT DISTINCT t.id,t.nome
            FROM professor_turma pt
            JOIN professor p ON p.id=pt.professor_id
            JOIN turma t ON t.id=pt.turma_id
            WHERE p.email=?
            ORDER BY t.nome
            """, email);
    }

    public static List<Object[]> disciplinasProfessor(String email){
        return listarDoProfessor("""
            SELECT DISTINCT d.id,d.materia
            FROM professor_disciplina pd
            JOIN professor p ON p.id=pd.professor_id
            JOIN disciplina d ON d.id=pd.disciplina_id
            WHERE p.email=?
            ORDER BY d.materia
            """, email);
    }

    public static List<Object[]> alunosProfessor(String email){
        return listarDoProfessor("""
            SELECT DISTINCT a.id,a.nome
            FROM professor_turma pt
            JOIN professor p ON p.id=pt.professor_id
            JOIN aluno_turma at ON at.turma_id=pt.turma_id
            JOIN aluno a ON a.id=at.aluno_id
            WHERE p.email=?
            ORDER BY a.nome
            """, email);
    }

    private static List<Object[]> listarDoProfessor(String sql,String email){
        List<Object[]> l=new ArrayList<>();
        try(Connection c=conexao.conectar();PreparedStatement p=c.prepareStatement(sql)){
            p.setString(1,email);
            try(ResultSet r=p.executeQuery()){
                while(r.next()) l.add(new Object[]{r.getInt(1),r.getString(2)});
            }
        }catch(SQLException e){System.out.println("Erro ao consultar dados do professor: "+e.getMessage());}
        return l;
    }
}
