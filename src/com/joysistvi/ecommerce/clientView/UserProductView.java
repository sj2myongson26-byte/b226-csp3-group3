/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.joysistvi.ecommerce.clientView;

import com.joysistvi.ecommerce.controller.ProductController;
import com.joysistvi.ecommerce.model.Product;
import com.joysistvi.ecommerce.model.User;

import static com.joysistvi.ecommerce.utils.ClearScreen.clearScreen;
import static com.joysistvi.ecommerce.utils.Scan.scanner;
import java.util.List;

/**
 *
 * @author ktagl
 */
/**
 * UserProductView handles product catalog browsing, details viewing, and direct cart insertions.
 */
public class UserProductView {

    private final ProductController productController = new ProductController();

    // Default display entry point without user context.
    public void display() {
        display(null);
    }

    // Renders interactive product catalog menu for customer browsing.
    public void display(User user) {
        boolean back = false;
        while (!back) {
            clearScreen();
            System.out.println("========================================");
            System.out.println("           AVAILABLE PRODUCTS           ");
            System.out.println("========================================");
            System.out.println("1. View All Products");
            System.out.println("2. Filter Products by Category");
            System.out.println("3. Search Products by Keyword");
            System.out.println("4. View Product Details by ID");
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
                    showAllProducts();
                    System.out.print("\nDo you want to add a product to cart? (Y/N): ");
                    String addConfirm = scanner.nextLine();
                    if (addConfirm.equalsIgnoreCase("Y")) {
                        addToCartDirectly(user);
                    } else {
                        promptReturn();
                    }
                    break;
                case 2:
                    showProductsByCategory(user);
                    promptReturn();
                    break;
                case 3:
                    searchProductsByKeyword(user);
                    promptReturn();
                    break;
                case 4:
                    showProductDetails(user);
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

    // Displays formatted table of all active products in inventory catalog.
    private void showAllProducts() {
        List<Product> products = productController.getAllProducts();
        System.out.println("\n--------------------------------------------------------------------------------------------------");
        System.out.printf("%-5s | %-22s | %-15s | %-16s | %-8s | %-10s\n", "ID", "Name", "Category", "Price", "Stock", "Status");
        System.out.println("--------------------------------------------------------------------------------------------------");

        if (products == null || products.isEmpty()) {
            System.out.println("                          No products found in the store.                         ");
        } else {
            for (Product p : products) {
                String priceStr = String.format("PHP %.2f", p.getPrice());
                System.out.printf("%-5d | %-22s | %-15s | %-16s | %-8d | %-10s\n",
                        p.getId(),
                        truncate(p.getName(), 22),
                        truncate(p.getCategory() != null ? p.getCategory() : "N/A", 15),
                        priceStr,
                        p.getQuantity(),
                        p.getStatus() != null ? p.getStatus() : "Available"
                );
            }
        }
        System.out.println("--------------------------------------------------------------------------------------------------");
    }

    // Prompts customer for product ID and displays detailed specs and direct add-to-cart option.
    private void showProductDetails(User user) {
        showAllProducts();
        System.out.print("\nEnter Product ID to view details: ");
        if (!scanner.hasNextInt()) {
            System.out.println("Invalid input. Product ID must be a number.");
            scanner.nextLine();
            return;
        }

        int id = scanner.nextInt();
        scanner.nextLine();

        Product p = productController.getProductById(id);
        if (p != null) {
            System.out.println("\n========================================");
            System.out.println("           PRODUCT DETAILS              ");
            System.out.println("========================================");
            System.out.println("ID         : " + p.getId());
            System.out.println("Name       : " + p.getName());
            System.out.println("Category   : " + (p.getCategory() != null ? p.getCategory() : "N/A"));
            System.out.println("Price      : PHP " + p.getPrice());
            System.out.println("Quantity   : " + p.getQuantity());
            System.out.println("Status     : " + (p.getStatus() != null ? p.getStatus() : "Available"));
            System.out.println("Description: " + (p.getDescription() != null ? p.getDescription() : "None"));
            System.out.println("========================================");

            System.out.print("\nDo you want to add this product to cart? (Y/N): ");
            String confirm = scanner.nextLine();
            if (confirm.equalsIgnoreCase("Y")) {
                if (p.getQuantity() <= 0) {
                    System.out.println("Sorry, product is out of stock.");
                    return;
                }
                System.out.print("Enter Quantity: ");
                if (scanner.hasNextInt()) {
                    int qty = scanner.nextInt();
                    scanner.nextLine();
                    if (qty > 0 && qty <= p.getQuantity()) {
                        int userId = (user != null) ? user.getId() : 1;
                        com.joysistvi.ecommerce.controller.CartController cartCtrl = new com.joysistvi.ecommerce.controller.CartController();
                        boolean ok = cartCtrl.handleAddItemToCustomerCart(userId, p.getId(), qty, (int) p.getPrice());
                        if (ok) {
                            System.out.println("SUCCESS: Added " + qty + " x " + p.getName() + " to cart!");
                        } else {
                            System.out.println("FAILED to add item to cart.");
                        }
                    } else {
                        System.out.println("Invalid quantity entered.");
                    }
                } else {
                    scanner.nextLine();
                    System.out.println("Invalid quantity.");
                }
            }
        } else {
            System.out.println("Product with ID " + id + " not found.");
        }
    }

    // Displays available product categories and lists filtered items.
    private void showProductsByCategory(User user) {
        List<String> categories = productController.getAllCategories();
        if (categories == null || categories.isEmpty()) {
            System.out.println("No product categories found in inventory.");
            return;
        }

        System.out.println("\nAVAILABLE CATEGORIES:");
        for (int i = 0; i < categories.size(); i++) {
            System.out.println((i + 1) + ". " + categories.get(i));
        }

        System.out.print("\nEnter Category Name or Number: ");
        String input = scanner.nextLine().trim();

        String selectedCategory = input;
        try {
            int index = Integer.parseInt(input) - 1;
            if (index >= 0 && index < categories.size()) {
                selectedCategory = categories.get(index);
            }
        } catch (NumberFormatException ignored) {}

        List<Product> products = productController.getProductsByCategory(selectedCategory);
        System.out.println("\n--------------------------------------------------------------------------------------------------");
        System.out.printf("%-5s | %-22s | %-15s | %-16s | %-8s | %-10s\n", "ID", "Name", "Category", "Price", "Stock", "Status");
        System.out.println("--------------------------------------------------------------------------------------------------");

        if (products == null || products.isEmpty()) {
            System.out.println("                   No products found under category: " + selectedCategory);
        } else {
            for (Product p : products) {
                String priceStr = String.format("PHP %.2f", p.getPrice());
                System.out.printf("%-5d | %-22s | %-15s | %-16s | %-8d | %-10s\n",
                        p.getId(),
                        truncate(p.getName(), 22),
                        truncate(p.getCategory() != null ? p.getCategory() : "N/A", 15),
                        priceStr,
                        p.getQuantity(),
                        p.getStatus() != null ? p.getStatus() : "Available"
                );
            }
        }
        System.out.println("--------------------------------------------------------------------------------------------------");

        if (products != null && !products.isEmpty()) {
            System.out.print("\nDo you want to add a product to cart? (Y/N): ");
            String confirm = scanner.nextLine().trim();
            if (confirm.equalsIgnoreCase("Y")) {
                addToCartDirectly(user);
            }
        }
    }

    // Prompts customer for search keyword and displays matching products.
    private void searchProductsByKeyword(User user) {
        System.out.print("\nEnter keyword to search products: ");
        String keyword = scanner.nextLine().trim();

        if (keyword.isEmpty()) {
            System.out.println("Search keyword cannot be empty.");
            return;
        }

        List<Product> products = productController.searchProducts(keyword);
        System.out.println("\n--------------------------------------------------------------------------------------------------");
        System.out.printf("%-5s | %-22s | %-15s | %-16s | %-8s | %-10s\n", "ID", "Name", "Category", "Price", "Stock", "Status");
        System.out.println("--------------------------------------------------------------------------------------------------");

        if (products == null || products.isEmpty()) {
            System.out.println("                   No products matching keyword: '" + keyword + "'");
        } else {
            for (Product p : products) {
                String priceStr = String.format("PHP %.2f", p.getPrice());
                System.out.printf("%-5d | %-22s | %-15s | %-16s | %-8d | %-10s\n",
                        p.getId(),
                        truncate(p.getName(), 22),
                        truncate(p.getCategory() != null ? p.getCategory() : "N/A", 15),
                        priceStr,
                        p.getQuantity(),
                        p.getStatus() != null ? p.getStatus() : "Available"
                );
            }
        }
        System.out.println("--------------------------------------------------------------------------------------------------");

        if (products != null && !products.isEmpty()) {
            System.out.print("\nDo you want to add a product to cart? (Y/N): ");
            String confirm = scanner.nextLine().trim();
            if (confirm.equalsIgnoreCase("Y")) {
                addToCartDirectly(user);
            }
        }
    }

    // Helper method pausing UI execution until Enter is pressed.
    private void promptReturn() {
        System.out.print("\nPress Enter to continue...");
        scanner.nextLine();
    }

    // Helper method to truncate long text strings.
    private String truncate(String text, int maxLength) {
        if (text == null) return "";
        if (text.length() <= maxLength) return text;
        return text.substring(0, maxLength - 3) + "...";
    }

    // Direct shortcut method to add products to user's cart from catalog screen.
    public void addToCartDirectly(User user) {
        System.out.print("\nEnter Product ID to add to cart (or 0 to cancel): ");
        if (!scanner.hasNextInt()) {
            System.out.println("Invalid input.");
            scanner.nextLine();
            return;
        }
        int pid = scanner.nextInt();
        scanner.nextLine();
        if (pid == 0) return;

        Product p = productController.getProductById(pid);
        if (p == null) {
            System.out.println("Product ID " + pid + " not found.");
            return;
        }
        if (p.getQuantity() <= 0) {
            System.out.println("Product is out of stock.");
            return;
        }

        System.out.print("Enter Quantity (or 0 to cancel): ");
        if (!scanner.hasNextInt()) {
            System.out.println("Invalid input.");
            scanner.nextLine();
            return;
        }
        int qty = scanner.nextInt();
        scanner.nextLine();
        if (qty <= 0) return;

        int userId = (user != null) ? user.getId() : 1;
        com.joysistvi.ecommerce.controller.CartController cartCtrl = new com.joysistvi.ecommerce.controller.CartController();
        boolean ok = cartCtrl.handleAddItemToCustomerCart(userId, p.getId(), qty, (int) p.getPrice());
        if (ok) {
            System.out.println("SUCCESS: Added " + qty + " x " + p.getName() + " to cart!");
        } else {
            System.out.println("FAILED to add item to cart.");
        }
    }
}


