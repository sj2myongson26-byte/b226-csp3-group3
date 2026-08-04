/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.joysistvi.ecommerce.main;

import com.joysistvi.ecommerce.config.dbconnection;
import com.joysistvi.ecommerce.controller.UserController;
import com.joysistvi.ecommerce.model.User;
import static com.joysistvi.ecommerce.utils.Scan.scanner;
import static com.joysistvi.ecommerce.utils.SplashScreen.splashScreen;
import com.joysistvi.ecommerce.view.AdminDashBoard;
import com.joysistvi.ecommerce.view.UserDashBoard;

/**
 *
 * @author ktagl
 */
public class Main {

    // Main entry point for launching the E-Commerce Console Application.
    public static void main(String[] args) {
        try {
            // Displays animated splash screen logo on startup.
            splashScreen();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        UserController userController = new UserController();

        // Main application loop for user authentication.
        while (true) {
            System.out.println("===========================================");
            System.out.println("    WELCOME TO ECOMMERCE APP BY GROUP 3");
            System.out.println("===========================================");
            System.out.println("1. Login");
            System.out.println("2. Register");
            System.out.println("3. Exit");  
            System.out.print("Choose: ");

            if (!scanner.hasNextInt()) {
                System.out.println("PLEASE ENTER ONLY NUMBERS");
                scanner.nextLine();
            }

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {

                case 1:
                    // Authenticate user and route to appropriate dashboard based on role.
                    LoginView login = new LoginView(userController);
                    User user = login.login();
                    
                    if(user != null){
                        if(user.getRole().equalsIgnoreCase("ADMIN")){
                            AdminDashBoard adminDB = new AdminDashBoard(user);
                            adminDB.dashboard();
                        } else {
                            UserDashBoard userDB = new UserDashBoard();
                            userDB.dashboard(user); 
                        }
                    }
                    break;

                case 2:
                    // Opens user registration form for creating new accounts.
                    RegisterView register = new RegisterView(userController);
                    register.register();
                    break;

                case 3:
                    // Safely terminates the application.
                    System.exit(0);

                default:
                    System.out.println("Please Choose only 1-3, Try Again!!");

            }
        }

    }
}
