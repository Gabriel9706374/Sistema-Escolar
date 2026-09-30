package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.Aluno;

public class AlunoDao {
    public static boolean existeAluno() {
        try (Connection c = conexao.conectar();
             PreparedStatement p = c.prepareStatement("SELECT 1 FROM aluno LIMIT 1")) {
            try (ResultSet r = p.executeQuery()) {
                return r.next();
            }
        } catch (SQLException e) {
            return false;
        }
    }

    public static boolean cadastrar(Aluno aluno) {
        String sql = "INSERT INTO aluno (nome,idade,email,senha,status) VALUES (?,?,?,?,?)";
        try (Connection c = conexao.conectar();
             PreparedStatement p = c.prepareStatement(sql)) {
            p.setString(1, aluno.getNome());
            p.setInt(2, aluno.getIdade());
            p.setString(3, aluno.getEmail());
            p.setString(4, aluno.getSenha());
            p.setString(5, aluno.getStatus());
            p.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Erro ao cadastrar aluno: " + e.getMessage());
            return false;
        }
    }

    public static List<Aluno> listar() {
        List<Aluno> lista = new ArrayList<>();
        String sql = "SELECT id,nome,idade,email,senha,status FROM aluno ORDER BY id";

        try (Connection c = conexao.conectar();
             PreparedStatement p = c.prepareStatement(sql);
             ResultSet r = p.executeQuery()) {

            while (r.next()) {
                lista.add(new Aluno(
                        r.getInt("id"),
                        r.getString("nome"),
                        r.getInt("idade"),
                        r.getString("email"),
                        r.getString("senha"),
                        r.getString("status")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Erro ao listar alunos: " + e.getMessage());
        }

        return lista;
    }

    public static boolean alterar(Aluno aluno) {
        String sql = "UPDATE aluno SET nome=?, idade=?, email=?, senha=?, status=? WHERE id=?";

        try (Connection c = conexao.conectar();
             PreparedStatement p = c.prepareStatement(sql)) {
            p.setString(1, aluno.getNome());
            p.setInt(2, aluno.getIdade());
            p.setString(3, aluno.getEmail());
            p.setString(4, aluno.getSenha());
            p.setString(5, aluno.getStatus());
            p.setInt(6, aluno.getId());
            return p.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Erro ao alterar aluno: " + e.getMessage());
            return false;
        }
    }

    public static boolean excluir(int id) {
        try (Connection c = conexao.conectar();
             PreparedStatement p = c.prepareStatement("DELETE FROM aluno WHERE id=?")) {
            p.setInt(1, id);
            return p.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Erro ao excluir aluno: " + e.getMessage());
            return false;
        }
    }

    public static boolean autenticar(String email, String senha) {
        try (Connection c = conexao.conectar();
             PreparedStatement p = c.prepareStatement(
                     "SELECT 1 FROM aluno WHERE email=? AND senha=? AND status='ATIVO'")) {
            p.setString(1, email);
            p.setString(2, senha);
            try (ResultSet r = p.executeQuery()) {
                return r.next();
            }
        } catch (SQLException e) {
            return false;
        }
    }
}
