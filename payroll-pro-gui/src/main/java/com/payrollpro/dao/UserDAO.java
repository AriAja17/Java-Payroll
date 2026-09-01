package com.payrollpro.dao;

import com.payrollpro.config.DatabaseConfig;
import com.payrollpro.model.User;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    /** Login: cek username + password. Return User jika valid, null jika tidak. */
    public User login(String username, String password) throws SQLException {
        String sql = "SELECT * FROM users WHERE username=? AND is_active=TRUE";
        try (PreparedStatement ps = DatabaseConfig.getConnection().prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String hash = rs.getString("password_hash");
                    if (BCrypt.checkpw(password, hash)) {
                        User u = map(rs);
                        updateLastLogin(username);
                        return u;
                    }
                }
            }
        }
        return null;
    }

    public List<User> getAll() throws SQLException {
        List<User> list = new ArrayList<>();
        String sql = "SELECT * FROM users ORDER BY username";
        try (Statement st = DatabaseConfig.getConnection().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) list.add(map(rs));
        }
        return list;
    }

    public void insert(User u, String plainPassword) throws SQLException {
        String hash = BCrypt.hashpw(plainPassword, BCrypt.gensalt(12));
        String sql  = "INSERT INTO users (username,password_hash,nama_lengkap,role,is_active) VALUES (?,?,?,?,?)";
        try (PreparedStatement ps = DatabaseConfig.getConnection().prepareStatement(sql)) {
            ps.setString(1, u.getUsername());
            ps.setString(2, hash);
            ps.setString(3, u.getNamaLengkap());
            ps.setString(4, u.getRole());
            ps.setBoolean(5, true);
            ps.executeUpdate();
        }
    }

    public void update(User u) throws SQLException {
        String sql = "UPDATE users SET nama_lengkap=?,role=?,is_active=? WHERE username=?";
        try (PreparedStatement ps = DatabaseConfig.getConnection().prepareStatement(sql)) {
            ps.setString(1, u.getNamaLengkap());
            ps.setString(2, u.getRole());
            ps.setBoolean(3, u.isActive());
            ps.setString(4, u.getUsername());
            ps.executeUpdate();
        }
    }

    public void ubahPassword(String username, String passwordBaru) throws SQLException {
        String hash = BCrypt.hashpw(passwordBaru, BCrypt.gensalt(12));
        String sql  = "UPDATE users SET password_hash=? WHERE username=?";
        try (PreparedStatement ps = DatabaseConfig.getConnection().prepareStatement(sql)) {
            ps.setString(1, hash);
            ps.setString(2, username);
            ps.executeUpdate();
        }
    }

    private void updateLastLogin(String username) throws SQLException {
        String sql = "UPDATE users SET last_login=NOW() WHERE username=?";
        try (PreparedStatement ps = DatabaseConfig.getConnection().prepareStatement(sql)) {
            ps.setString(1, username);
            ps.executeUpdate();
        }
    }

    private User map(ResultSet rs) throws SQLException {
        User u = new User();
        u.setId(rs.getInt("id"));
        u.setUsername(rs.getString("username"));
        u.setPasswordHash(rs.getString("password_hash"));
        u.setNamaLengkap(rs.getString("nama_lengkap"));
        u.setRole(rs.getString("role"));
        u.setActive(rs.getBoolean("is_active"));
        Timestamp ll = rs.getTimestamp("last_login");
        if (ll != null) u.setLastLogin(ll.toLocalDateTime());
        return u;
    }
}
