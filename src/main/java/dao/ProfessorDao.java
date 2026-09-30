package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.Professor;

public class ProfessorDao {
    public static boolean existeProfessor(){try(Connection c=conexao.conectar();PreparedStatement p=c.prepareStatement("SELECT 1 FROM professor LIMIT 1")){try(ResultSet r=p.executeQuery()){return r.next();}}catch(SQLException e){return false;}}
    public static boolean cadastrar(Professor professor) {
        String sql = "INSERT INTO professor (nome, idade, email, senha) VALUES (?, ?, ?, ?)";
        try (Connection c = conexao.conectar(); PreparedStatement p = c.prepareStatement(sql)) {
            p.setString(1, professor.getNome()); p.setInt(2, professor.getIdade());
            p.setString(3, professor.getEmail()); p.setString(4, professor.getSenha());
            p.executeUpdate(); return true;
        } catch (SQLException e) { System.out.println("Erro ao cadastrar professor: " + e.getMessage()); return false; }
    }
    public static List<Professor> listar() {
        List<Professor> lista = new ArrayList<>();
        String sql = "SELECT id,nome,idade,email,senha FROM professor ORDER BY id";
        try (Connection c=conexao.conectar(); PreparedStatement p=c.prepareStatement(sql); ResultSet r=p.executeQuery()) {
            while(r.next()) lista.add(new Professor(r.getInt("id"),r.getString("nome"),r.getInt("idade"),r.getString("email"),r.getString("senha")));
        } catch(SQLException e){System.out.println("Erro ao listar professores: "+e.getMessage());}
        return lista;
    }
    public static boolean alterar(Professor professor) {
        String sql = "UPDATE professor SET nome=?, idade=?, email=?, senha=? WHERE id=?";
        try (Connection c = conexao.conectar(); PreparedStatement p = c.prepareStatement(sql)) {
            p.setString(1, professor.getNome());
            p.setInt(2, professor.getIdade());
            p.setString(3, professor.getEmail());
            p.setString(4, professor.getSenha());
            p.setInt(5, professor.getId());
            return p.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Erro ao alterar professor: " + e.getMessage());
            return false;
        }
    }

    public static boolean excluir(int id) {
        try(Connection c=conexao.conectar(); PreparedStatement p=c.prepareStatement("DELETE FROM professor WHERE id=?")){p.setInt(1,id); return p.executeUpdate()>0;}catch(SQLException e){return false;}
    }
    public static boolean autenticar(String email,String senha){
        try(Connection c=conexao.conectar(); PreparedStatement p=c.prepareStatement("SELECT 1 FROM professor WHERE email=? AND senha=?")){p.setString(1,email);p.setString(2,senha);try(ResultSet r=p.executeQuery()){return r.next();}}catch(SQLException e){return false;}
    }
}
