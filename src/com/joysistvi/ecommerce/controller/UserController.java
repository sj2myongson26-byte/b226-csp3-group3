package com.joysistvi.ecommerce.controller;

import com.joysistvi.ecommerce.model.User;
import com.joysistvi.ecommerce.services.UserService;

/**
 * UserController handles user authentication and account updates.
 */
public class UserController {

    public UserService service = new UserService();
    
    // Registers a new user account.
    public boolean register(String username, String password, String role) {
        return service.register(username, password, role);
    }
     
    // Authenticates user login credentials.
    public User login(String username, String password) {
        return service.login(username, password);
    }
     
    // Checks if username already exists in database.
    public User checkUsername(String username) {
        return service.checkUsername(username);
    }

    // Updates user password with BCrypt hashing.
    public boolean updatePassword(int userId, String newPassword) {
        return service.updatePassword(userId, newPassword);
    }
}
