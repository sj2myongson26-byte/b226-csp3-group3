package com.joysistvi.ecommerce.model;

/**
 * User represents an authenticated account entity containing credentials and system access role.
 */
public class User {

    private int id;
    private String username;
    private String password;
    private String role;

    // Full constructor initializing user entity fields.
    public User(int id, String username, String password, String role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    // Constructor initializing user credentials without ID.
    public User(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    // Constructor initializing user session details without password.
    public User(int id, String username, String role) {
        this.id = id;
        this.username = username;
        this.role = role;
    }

    // Constructor initializing raw user login credentials.
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // Default empty constructor.
    public User() {

    }

    // Returns user ID.
    public int getId() {
        return id;
    }

    // Sets user ID.
    public void setId(int id) {
        this.id = id;
    }

    // Returns username string.
    public String getUsername() {
        return username;
    }

    // Sets username string.
    public void setUsername(String username) {
        this.username = username;
    }

    // Returns BCrypt hashed password string.
    public String getPassword() {
        return password;
    }

    // Sets password string.
    public void setPassword(String password) {
        this.password = password;
    }

    // Returns user access role string (USER or ADMIN).
    public String getRole() {
        return role;
    }

    // Sets user access role string.
    public void setRole(String role) {
        this.role = role;
    }
}
