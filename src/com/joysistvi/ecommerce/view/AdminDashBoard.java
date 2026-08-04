/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.joysistvi.ecommerce.view;

import com.joysistvi.ecommerce.adminView.ReportView;
import com.joysistvi.ecommerce.controller.ReportController;
import com.joysistvi.ecommerce.model.User;
import static com.joysistvi.ecommerce.utils.ClearScreen.clearScreen;
import static com.joysistvi.ecommerce.utils.Scan.scanner;

/**
 *
 * @author ktagl
 */
public class AdminDashBoard {

    private User user;
    private final ReportView reportView;

    public AdminDashBoard(User user){
        this.user = user;

        ReportController reportController = new ReportController();
        reportView = new ReportView(reportController);
    }

    public void dashboard() {
        while (true) {
            clearScreen();
            System.out.println("=================================================");
            System.out.println("  WELCOME " + (user != null && user.getUsername() != null? user.getUsername().toUpperCase():"ADMIN") +" TO ECOMMERCE ADMIN DASHBOARD");
            System.out.println("=================================================");
            System.out.println("1. Product Management");
            System.out.println("2. Order Management");
            System.out.println("3. Manage Reports");
            System.out.println("4. Logout");
            System.out.print("Choose: ");

            if (!scanner.hasNextInt()) {
                System.out.println("PLEASE ENTER ONLY NUMBERS");
                scanner.nextLine();
                continue;
            }

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    com.joysistvi.ecommerce.adminView.ProductView productView = new com.joysistvi.ecommerce.adminView.ProductView();
                    productView.dashboard();
                    break;
                case 2:
                    com.joysistvi.ecommerce.adminView.OrderView orderView = new com.joysistvi.ecommerce.adminView.OrderView();
                    orderView.dashboard();
                    break;
                case 3:
                    reportView.dashboard();
                    break;
                case 4:
                    System.out.println("Logging out...");
                    return;
                default:
                    System.out.println("Please choose a valid option (1-4).");
            }
        }
    }
}
