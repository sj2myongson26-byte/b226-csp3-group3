/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.joysistvi.ecommerce.repository;

import com.joysistvi.ecommerce.model.User;

/**
 *
 * @author ktagl
 */
/**
 * UserRepo defines data access contracts for user authentication and credentials.
 */
public interface UserRepo {

    // Registers a new user account with hashed password.
    boolean register(String username, String password, String role);

    // Verifies user login credentials.
    User login(String username, String password);

    // Checks user record matching username.
    User checkUsername(String username);

    // Checks user record matching user ID.
    User checkUserId(int id);

    // Updates user password with BCrypt hashing.
    boolean updatePassword(int userId, String newPassword);
}
