package dao;

import java.sql.*;

public class NotaDao {
    public static boolean salvarPorNomes(String aluno,String turma,double nota1,double nota2){
        double notaFinal=(nota1+nota2)/2.0;
        String status=notaFinal>=6?"APROVADO":"REPROVADO";
        try(Connection c=conexao.conectar()){
            int alunoId=buscarId(c,"SELECT id FROM aluno WHERE nome=?",aluno);
            int turmaId=buscarId(c,"SELECT id FROM turma WHERE nome=?",turma);
            int disciplinaId = -1;
            try (PreparedStatement pDisciplina = c.prepareStatement("SELECT disciplina_id FROM turma_disciplina WHERE turma_id=? ORDER BY disciplina_id LIMIT 1")) {
                pDisciplina.setInt(1, turmaId);
                try (ResultSet r = pDisciplina.executeQuery()) {
                    if (r.next()) disciplinaId = r.getInt(1);
                }
            }
            if(alunoId<0||turmaId<0||disciplinaId<0)return false;
            String sql="INSERT INTO nota(aluno_id,turma_id,disciplina_id,nota1,nota2,nota_final,status) VALUES(?,?,?,?,?,?,?) ON CONFLICT(aluno_id,turma_id,disciplina_id) DO UPDATE SET nota1=excluded.nota1,nota2=excluded.nota2,nota_final=excluded.nota_final,status=excluded.status";
            try(PreparedStatement p=c.prepareStatement(sql)){p.setInt(1,alunoId);p.setInt(2,turmaId);p.setInt(3,disciplinaId);p.setDouble(4,nota1);p.setDouble(5,nota2);p.setDouble(6,notaFinal);p.setString(7,status);p.executeUpdate();return true;}
        }catch(SQLException e){System.out.println("Erro ao salvar nota: "+e.getMessage());return false;}
    }
    public static boolean excluir(int id){try(Connection c=conexao.conectar();PreparedStatement p=c.prepareStatement("DELETE FROM nota WHERE id=?")){p.setInt(1,id);return p.executeUpdate()>0;}catch(SQLException e){return false;}}
    private static int buscarId(Connection c,String sql,String valor)throws SQLException{try(PreparedStatement p=c.prepareStatement(sql)){p.setString(1,valor);try(ResultSet r=p.executeQuery()){return r.next()?r.getInt(1):-1;}}}
}
