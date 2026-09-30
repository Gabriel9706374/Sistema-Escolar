package dao;

import java.sql.*;

public class AdministradorDao {

    public static boolean existeAdministrador() {
        String sql = "SELECT 1 FROM administrador LIMIT 1";

        try (Connection c = conexao.conectar();
             PreparedStatement p = c.prepareStatement(sql);
             ResultSet r = p.executeQuery()) {
            return r.next();
        } catch (SQLException e) {
            return false;
        }
    }

    public static boolean cadastrar(String email, String senha) {
        String sql = "INSERT INTO administrador (nome, email, senha) VALUES (?, ?, ?)";

        try (Connection c = conexao.conectar();
             PreparedStatement p = c.prepareStatement(sql)) {
            p.setString(1, "");
            p.setString(2, email);
            p.setString(3, senha);
            p.executeUpdate();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public static boolean autenticar(String email, String senha) {
        try (Connection c = conexao.conectar();
             PreparedStatement p = c.prepareStatement("SELECT 1 FROM administrador WHERE email=? AND senha=?")) {
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
