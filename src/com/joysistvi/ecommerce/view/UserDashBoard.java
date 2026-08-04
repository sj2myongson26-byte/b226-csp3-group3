/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.joysistvi.ecommerce.view;

import com.joysistvi.ecommerce.clientView.UserCartView;
import com.joysistvi.ecommerce.clientView.UserPaymentView;
import com.joysistvi.ecommerce.clientView.UserProductView;
import com.joysistvi.ecommerce.clientView.UserProfileView;
import com.joysistvi.ecommerce.controller.PaymentController;
import com.joysistvi.ecommerce.model.User;
import static com.joysistvi.ecommerce.utils.ClearScreen.clearScreen;
import static com.joysistvi.ecommerce.utils.Scan.scanner;

/**
 *
 * @author ktagl
 */
public class UserDashBoard {

    private final PaymentController paymentController = new PaymentController();

    // Renders main client dashboard menu for logged-in users.
    public void dashboard(User user) {
        boolean logout = false;
        com.joysistvi.ecommerce.controller.CartController cartController = new com.joysistvi.ecommerce.controller.CartController();

        while (!logout) {
            clearScreen();
            int userId = (user != null) ? user.getId() : 1;
            java.util.List<com.joysistvi.ecommerce.model.CartItem> cartItems = cartController.getUserCartItems(userId);
            int cartCount = (cartItems != null) ? cartItems.size() : 0;
            String cartLabel = cartCount > 0 ? "View Cart (" + cartCount + " item" + (cartCount > 1 ? "s" : "") + ")" : "View Cart";

            System.out.println("=================================================");
            System.out.println("   WELCOME " + (user != null ? user.getUsername().toUpperCase() : "USER") + " TO ECOMMERCE DASHBOARD");
            System.out.println("=================================================");
            System.out.println("1. View Products");
            System.out.println("2. " + cartLabel);
            System.out.println("3. Checkout / Payment");
            System.out.println("4. View Profile");
            System.out.println("5. Logout");
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
                    // Displays available store products.
                    UserProductView productView = new UserProductView();
                    productView.display(user);
                    break;
                case 2:
                    // Opens customer shopping cart.
                    UserCartView cartView = new UserCartView();
                    cartView.display(user);
                    break;
                case 3:
                    // Directly initiates shopping cart checkout and payment.
                    UserCartView checkoutCartView = new UserCartView();
                    checkoutCartView.display(user);
                    break;
                case 4:
                    // Opens user profile management and order history.
                    UserProfileView profileView = new UserProfileView();
                    assert user != null;
                    profileView.display(user.getId());
                    break;
                case 5:
                    // Logs out user and returns to welcome menu.
                    System.out.println("Logging out...");
                    logout = true;
                    break;

                default:
                    System.out.println("Please choose a valid option (1-5).");
            }
        }
    }

    // Overloaded method for default user dashboard navigation.
    public void dashboard() {
        dashboard(null);
    }
}

