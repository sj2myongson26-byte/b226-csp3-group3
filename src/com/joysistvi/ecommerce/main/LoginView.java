/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.joysistvi.ecommerce.main;

import com.joysistvi.ecommerce.controller.UserController;
import com.joysistvi.ecommerce.model.User;
import static com.joysistvi.ecommerce.utils.ClearScreen.clearScreen;
import static com.joysistvi.ecommerce.utils.Scan.scanner;

/**
 *
 * @author ktagl
 */
public class LoginView {

    private final UserController userController;

    // Constructor injecting the user controller dependency.
    public LoginView(UserController userController) {
        this.userController = userController;
    }

    // Prompts user for credentials and verifies login details against database.
    public User login() {
        while (true) {
            clearScreen();
            System.out.println("===========================================");
            System.out.println("             ECOMMERCE LOGIN               ");
            System.out.println("===========================================");
            String username;
            String password;

            // Loop to prompt and validate username length.
            while (true) {
                System.out.print("Username (0 to cancel): ");
                username = scanner.nextLine();

                // Exits login loop if user enters 0.
                if (username.equals("0")) {
                    return null;
                }

                if (username.length() >= 5) {
                    break;
                } else {
                    System.out.println("Username must be at least 5 character long. Please Enter Again!");
                }
            }

            // Loop to prompt and validate password length.
            while (true) {
                System.out.print("Password (0 to cancel): ");
                password = scanner.nextLine();

                // Exits login loop if user enters 0.
                if (password.equals("0")) {
                    return null;
                }

                if (password.length() >= 5) {
                    break;
                } else {
                    System.out.println("Password must be at least 5 character long. Please Enter Again!");
                }
            }

            // Authenticate user credentials via controller.
            User user = userController.login(username, password);

            // Return user object if credentials are correct.
            if (user != null) {
                System.out.println("Login Successful!\n");
                return user;
            }
            System.out.println("Invalid username or password. Please Try Again.");
            System.out.print("Press Enter to try again...");
            scanner.nextLine();
        }
    }
}
