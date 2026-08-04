package com.joysistvi.ecommerce.clientView;

import com.joysistvi.ecommerce.controller.CustomerController;
import com.joysistvi.ecommerce.controller.ProductController;
import com.joysistvi.ecommerce.model.Customer;

import com.joysistvi.ecommerce.model.User;
import java.util.List;


import static com.joysistvi.ecommerce.utils.ClearScreen.clearScreen;
import static com.joysistvi.ecommerce.utils.Scan.scanner;

/**
 *
 * @author myongson
 */


/**
 * UserProfileView manages customer profile views, phone/address updates, password changes, and order history cards.
 */
public class UserProfileView {

    private final CustomerController customerController = new CustomerController();

    // Renders user profile management dashboard menu choices.
    public void display(int id) {
        boolean back = false;
        while (!back) {

            clearScreen();
            System.out.println("========================================");
            System.out.println("          USERS PROFILE             ");
            System.out.println("========================================");
            System.out.println("1. View Customers Profile");
            System.out.println("2. Update Customers Profile");
            System.out.println("3. Change Password");
            System.out.println("4. View Order History");
            System.out.println("5. Back to Main Dashboard");
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
                    displayCustomer(id);
                    promptReturn();
                    break;
                case 2:
                    updateCustomer(id);
                    promptReturn();
                    break;
                case 3:
                    changePassword(id);
                    break;
                case 4:
                    displayOrderHistory(id);
                    promptReturn();
                    break;
                case 5:
                    back = true;
                    break;
                default:
                    System.out.println("Invalid choice. Please enter 1-5.");
            }
        }
    }

    // Displays customer profile information including phone number and address.
    public void displayCustomer(int id) {

        Customer c = customerController.getCustomerByUserId(id);

        if (c == null) {
         System.out.println("\nCustomer profile not found.");
         return;
        }

        System.out.println("\n========== USER PROFILE ==========");
        System.out.println("Customer ID : " + c.getId());
        System.out.println("User ID     : " + c.getUser_id());
        System.out.println("Phone       : " + c.getPhoneNumber());
        System.out.println("Address     : " + c.getAddress());
        System.out.println("==================================");
    }

    // Prompts customer to update phone number and shipping address details.
    public void updateCustomer(int id) {

        Customer c = customerController.getCustomerByUserId(id);

        if (c == null) {
             System.out.println("\nCustomer profile not found.");
            return;
        }

        System.out.println("\n====== UPDATE PROFILE ======");

        System.out.print("Phone Number [" + c.getPhoneNumber() + "]: ");
        String phone = scanner.nextLine();
        if (!phone.isEmpty()) {
            c.setPhoneNumber(phone);
        }

        System.out.print("Address [" + c.getAddress() + "]: ");
        String address = scanner.nextLine();
        if (!address.isEmpty()) {
            c.setAddress(address);
        }

        Customer cust = new Customer(c.getId(),phone,address, c.getUser_id());
        cust.setId(c.getId());

        if (customerController.updateCustomer(cust)) {
            System.out.println("Customer Profile updated successfully!");
        } else {
            System.out.println("Failed to update Customer Profile.");
        }
    }

    // Handles user password change validation and BCrypt re-hashing.
    private void changePassword(int id){

        Customer c = customerController.getCustomerByUserId(id);

        if (c == null) {
            System.out.println("\nCustomer profile not found.");
            return;
        }

        System.out.println("\n====== CHANGE PASSWORD ======");

        System.out.print("Enter current password (0 to cancel): ");
        String currentPassword = scanner.nextLine();
        if (currentPassword.equals("0")) {
            System.out.println("Change password cancelled.");
            promptReturn();
            return;
        }

        System.out.print("Enter new password (0 to cancel): ");
        String newPassword = scanner.nextLine();
        if (newPassword.equals("0")) {
            System.out.println("Change password cancelled.");
            promptReturn();
            return;
        }

        System.out.print("Re-enter new password (0 to cancel): ");
        String confirmPassword = scanner.nextLine();
        if (confirmPassword.equals("0")) {
            System.out.println("Change password cancelled.");
            promptReturn();
            return;
        }

        if (currentPassword.isBlank() ||
                newPassword.isBlank() ||
                confirmPassword.isBlank()) {

            System.out.println("All fields are required.");
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            System.out.println("New passwords do not match.");
            promptReturn();
            return;
        }

        if (newPassword.length() < 5) {
            System.out.println("New password must be at least 5 characters long.");
            promptReturn();
            return;
        }

        com.joysistvi.ecommerce.controller.UserController userController = new com.joysistvi.ecommerce.controller.UserController();
        boolean success = userController.updatePassword(id, newPassword);

        if (success) {
            System.out.println("SUCCESS: Password updated successfully!");
        } else {
            System.out.println("FAILED: Could not update password.");
        }
        promptReturn();
    }

    // Displays customer order history cards in Shopee/Lazada UI style.
    private void displayOrderHistory(int userId) {
        com.joysistvi.ecommerce.controller.OrderController orderController = new com.joysistvi.ecommerce.controller.OrderController();
        com.joysistvi.ecommerce.controller.ProductController productController = new com.joysistvi.ecommerce.controller.ProductController();
        java.text.DecimalFormat df = new java.text.DecimalFormat("#,##0.00");

        List<com.joysistvi.ecommerce.model.Order> orders = orderController.getCustomerOrders(userId);

        System.out.println("\n==========================================================================================");
        System.out.println("                                     MY ORDERS / PURCHASES                                ");
        System.out.println("==========================================================================================");

        if (orders == null || orders.isEmpty()) {
            System.out.println("\n                     🛒  You haven't placed any orders yet.                      ");
            System.out.println("==========================================================================================");
            return;
        }

        for (com.joysistvi.ecommerce.model.Order o : orders) {
            String statusBadge = "[" + (o.getStatus() != null ? o.getStatus().toUpperCase() : "PENDING") + "]";
            String dateStr = o.getOrder_date() != null ? o.getOrder_date() : "Recently Placed";

            System.out.println("==========================================================================================");
            System.out.printf("ORDER #%-6d | Date: %-22s | Status: %s\n",
                    o.getId(),
                    dateStr,
                    statusBadge
            );
            System.out.println("------------------------------------------------------------------------------------------");

            List<com.joysistvi.ecommerce.model.OrderItem> items = orderController.getOrderItems(o.getId());
            if (items != null && !items.isEmpty()) {
                for (com.joysistvi.ecommerce.model.OrderItem item : items) {
                    com.joysistvi.ecommerce.model.Product p = productController.getProductById(item.getProduct_id());
                    String pName = (p != null) ? p.getName() : "Product #" + item.getProduct_id();
                    String pCat = (p != null && p.getCategory() != null) ? p.getCategory() : "General";

                    System.out.printf("  📦 %-32s | Category: %-15s | x%-3d | PHP %s\n",
                            pName,
                            pCat,
                            item.getQuantity(),
                            df.format(item.getUnit_price())
                    );
                }
            } else {
                System.out.println("  📦 Standard Order Package Details");
            }

            System.out.println("------------------------------------------------------------------------------------------");
            System.out.printf("Shipping Address: %-40s | Order Total: PHP %s\n",
                    o.getAddress() != null ? o.getAddress() : "Standard Address",
                    df.format(o.getTotal_amount())
            );
            System.out.println("==========================================================================================");
            System.out.println();
        }
    }

    // Helper method to truncate long text strings.
    private String truncate(String text, int maxLength) {
        if (text == null) return "";
        if (text.length() <= maxLength) return text;
        return text.substring(0, maxLength - 3) + "...";
    }

    // Helper method pausing UI execution until Enter key is pressed.
    private void promptReturn() {
        System.out.print("\nPress Enter to continue...");
        scanner.nextLine();
    }

    // Overloaded display entry point using user entity object.
    public void display(User user) {
        if (user != null) {
            display(user.getId());
        }
    }
}

