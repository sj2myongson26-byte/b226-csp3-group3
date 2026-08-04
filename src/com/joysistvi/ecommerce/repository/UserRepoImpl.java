package com.joysistvi.ecommerce.repository;

import com.joysistvi.ecommerce.config.dbconnection;
import com.joysistvi.ecommerce.model.User;
import org.mindrot.jbcrypt.BCrypt;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.*;

/**
 * UserRepoImpl implements UserRepo data access for user authentication and BCrypt password security.
 */
public class UserRepoImpl implements UserRepo {

    private final dbconnection db = new dbconnection();

    // Hashes password using BCrypt and inserts new user record into database.
    @Override
    public boolean register(String username, String password, String role) {
        String query = "INSERT INTO users(username, password,role) VALUES(?,?,?)";
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

        try (Connection conn = db.connect(); PreparedStatement prep = conn.prepareStatement(query)) {
            prep.setString(1, username);
            prep.setString(2, hashedPassword);
            prep.setString(3, role);

            return prep.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    // Verifies username and BCrypt password hash against database records.
    @Override
    public User login(String username, String password) {
        String query = "SELECT * FROM users WHERE username = ?";
        try (Connection conn = db.connect(); PreparedStatement prep = conn.prepareStatement(query)) {
            prep.setString(1, username);

            try (ResultSet rs = prep.executeQuery()) {
                if (rs.next()) {
                    String hashedPassword = rs.getString("password");
                    if (BCrypt.checkpw(password, hashedPassword)) {
                        return new User(
                                rs.getInt("user_id"),
                                rs.getString("username"),
                                hashedPassword,
                                rs.getString("role")
                        );
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    // Fetches user record matching specified user ID.
    @Override
    public User checkUserId(int id) {
        String query = "SELECT * FROM users WHERE user_id = ?";

        try (Connection conn = db.connect(); PreparedStatement prep = conn.prepareStatement(query)) {
            prep.setInt(1, id);

            try (ResultSet rs = prep.executeQuery()) {
                if (rs.next()) {
                    return new User(
                            rs.getInt("user_id"),
                            rs.getString("username"),
                            rs.getString("role")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    // Fetches user record matching specified username.
    @Override
    public User checkUsername(String username) {
        String query = "SELECT * FROM users WHERE username = ?";

        try (Connection conn = db.connect(); PreparedStatement prep = conn.prepareStatement(query)) {
            prep.setString(1, username);

            try (ResultSet rs = prep.executeQuery()) {
                if (rs.next()) {
                    return new User(
                            rs.getInt("user_id"),
                            rs.getString("username"),
                            rs.getString("role")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    // Hashes new password with BCrypt and updates user record in database.
    @Override
    public boolean updatePassword(int userId, String newPassword) {
        String query = "UPDATE users SET password = ? WHERE user_id = ?";
        String hashedPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());
        try (Connection conn = db.connect(); PreparedStatement prep = conn.prepareStatement(query)) {
            prep.setString(1, hashedPassword);
            prep.setInt(2, userId);
            return prep.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
