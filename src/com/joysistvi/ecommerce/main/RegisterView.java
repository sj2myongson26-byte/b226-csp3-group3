/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.joysistvi.ecommerce.main;

import com.joysistvi.ecommerce.controller.UserController;
import com.joysistvi.ecommerce.model.User;
import static com.joysistvi.ecommerce.utils.ClearScreen.clearScreen;
import com.joysistvi.ecommerce.view.AdminDashBoard;
import com.joysistvi.ecommerce.view.UserDashBoard;
import java.util.Scanner;

public class RegisterView {

    private final UserController userController;
    private final Scanner scanner;

    // Constructor initializing user controller and scanner instances.
    public RegisterView(UserController userController) {
        this.userController = userController;
        this.scanner = new Scanner(System.in);
    }

    // Handles user registration, input validation, and auto-login upon success.
    public void register() {
        clearScreen();
        System.out.println("===========================================");
        System.out.println("            ECOMMERCE REGISTER             ");
        System.out.println("===========================================");
        String email;
        String password;
        String role;

        // Loop to prompt for username and check for existing duplicate usernames.
        while (true) {
            System.out.print("Username (0 to cancel): ");
            email = scanner.nextLine().trim();

            if (email.equals("0")) {
                System.out.println("Registration cancelled.");
                return;
            }

            if (email.length() < 5) {
                System.out.println("Username must be at least 5 characters long.");
                continue;
            }

            if (userController.checkUsername(email) != null) {
                System.out.println("Username already exists. Please choose another one.");
                continue;
            }

            break;
        }

        // Loop to prompt for password and validate minimum length requirements.
        while (true) {
            System.out.print("Password (0 to cancel): ");
            password = scanner.nextLine();

            if (password.equals("0")) {
                System.out.println("Registration cancelled.");
                return;
            }

            if (password.length() >= 5) {
                break;
            } else {
                System.out.println("Password must be at least 5 characters long.");
            }
        }

        // Default role for public registration is strictly USER for security.
        role = "USER";

        // Attempt account registration via user controller.
        boolean success = userController.register(email, password, role);

        // Auto-login and redirect to dashboard upon successful registration.
        if (success) {
            System.out.println("Registration successful!");

            User user = userController.login(email, password);

            if (user != null) {
                if (user.getRole().equalsIgnoreCase("ADMIN")) {
                    new AdminDashBoard(user).dashboard();
                } else {
                    new UserDashBoard().dashboard();
                }
            }
        } else {
            System.out.println("Registration failed.");
        }
    }
}
