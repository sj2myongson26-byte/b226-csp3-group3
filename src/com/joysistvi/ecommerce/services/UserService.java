package com.joysistvi.ecommerce.services;

import com.joysistvi.ecommerce.model.User;
import com.joysistvi.ecommerce.repository.UserRepo;
import com.joysistvi.ecommerce.repository.UserRepoImpl;

/**
 * UserService handles business logic and security validation for user accounts.
 */
public class UserService {
     
    public UserRepo repo = new UserRepoImpl();
    
    // Registers a new user account with BCrypt password hashing.
    public boolean register(String username, String password, String role) {
        return repo.register(username, password, role);
    }
    
    // Authenticates user credentials against BCrypt hash stored in repository.
    public User login(String username, String password) {
         return repo.login(username, password);
     }
    
    // Checks if username already exists in repository.
    public User checkUsername(String username) {
        return repo.checkUsername(username);
    }
    
    // Checks user account existence by user ID.
    public User checkUserId(int id) {
        return repo.checkUserId(id);
    }

    // Hashes new password and updates user credentials in repository.
    public boolean updatePassword(int userId, String newPassword) {
        return repo.updatePassword(userId, newPassword);
    }
}
