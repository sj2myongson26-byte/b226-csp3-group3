package com.joysistvi.ecommerce.adminView;

import com.joysistvi.ecommerce.controller.ProductController;
import com.joysistvi.ecommerce.model.Product;

import static com.joysistvi.ecommerce.utils.ClearScreen.clearScreen;
import static com.joysistvi.ecommerce.utils.Scan.scanner;
import java.util.List;

/**
 * ProductView provides the Admin UI for full Product Management (CRUD operations).
 */
public class ProductView {

    private final ProductController productController = new ProductController();

    // Main Dashboard loop for Admin Product Management
    public void dashboard() {
        while (true) {
            clearScreen();
            System.out.println("==========================================================================================");
            System.out.println("                                ADMIN PRODUCT MANAGEMENT                                  ");
            System.out.println("==========================================================================================");
            System.out.println("1. View All Products");
            System.out.println("2. Add New Product");
            System.out.println("3. Update Product");
            System.out.println("4. Delete Product");
            System.out.println("5. Back to Admin Dashboard");
            System.out.print("Choose option: ");

            if (!scanner.hasNextInt()) {
                System.out.println("PLEASE ENTER ONLY NUMBERS!");
                scanner.nextLine();
                promptReturn();
                continue;
            }

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    viewAllProducts();
                    promptReturn();
                    break;
                case 2:
                    addProduct();
                    promptReturn();
                    break;
                case 3:
                    updateProduct();
                    promptReturn();
                    break;
                case 4:
                    deleteProduct();
                    promptReturn();
                    break;
                case 5:
                    return; // Return to Admin Main Dashboard
                default:
                    System.out.println("Invalid choice. Please enter 1-5.");
                    promptReturn();
            }
        }
    }

    // Displays all products stored in the database
    private void viewAllProducts() {
        List<Product> products = productController.getAllProducts();
        System.out.println("\n--------------------------------------------------------------------------------------------------");
        System.out.printf("%-5s | %-22s | %-15s | %-16s | %-8s | %-10s\n", "ID", "Name", "Category", "Price", "Stock", "Status");
        System.out.println("--------------------------------------------------------------------------------------------------");

        if (products == null || products.isEmpty()) {
            System.out.println("                          No products found in the database.                      ");
        } else {
            for (Product p : products) {
                String priceStr = String.format("PHP %.2f", p.getPrice());
                System.out.printf("%-5d | %-22s | %-15s | %-16s | %-8d | %-10s\n",
                        p.getId(),
                        truncate(p.getName(), 22),
                        truncate(p.getCategory() != null ? p.getCategory() : "N/A", 15),
                        priceStr,
                        p.getQuantity(),
                        p.getStatus() != null ? p.getStatus() : "ACTIVE"
                );
            }
        }
        System.out.println("--------------------------------------------------------------------------------------------------");
    }

    // Prompts admin for new product details and saves to DB
    private void addProduct() {
        System.out.println("\n--- ADD NEW PRODUCT ---");
        System.out.println("(Enter 0 at Product Name to cancel)\n");

        System.out.print("Product Name (0 to cancel): ");
        String name = scanner.nextLine().trim();
        if (name.equals("0")) {
            System.out.println("Add product cancelled.");
            return;
        }
        if (name.isEmpty()) {
            System.out.println("Product name cannot be empty.");
            return;
        }

        System.out.print("Category: ");
        String category = scanner.nextLine().trim();

        System.out.print("Price (PHP): ");
        if (!scanner.hasNextDouble()) {
            System.out.println("Invalid price amount.");
            scanner.nextLine();
            return;
        }
        double price = scanner.nextDouble();
        scanner.nextLine();

        System.out.print("Initial Stock Quantity: ");
        if (!scanner.hasNextInt()) {
            System.out.println("Invalid quantity number.");
            scanner.nextLine();
            return;
        }
        int quantity = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Description: ");
        String description = scanner.nextLine().trim();

        // Construct Product model (id = 0 for auto increment)
        Product newProduct = new Product(
                0,
                name,
                description,
                price,
                quantity,
                category,
                "ACTIVE"
        );

        boolean success = productController.handleAddProduct(newProduct);
        if (success) {
            System.out.println("SUCCESS: Product '" + name + "' added successfully!");
        } else {
            System.out.println("FAILED: Could not add product. Please try again.");
        }
    }

    // Updates an existing product's details
    private void updateProduct() {
        viewAllProducts();
        System.out.print("\nEnter Product ID to update (0 to cancel): ");
        if (!scanner.hasNextInt()) {
            System.out.println("Invalid product ID.");
            scanner.nextLine();
            return;
        }

        int id = scanner.nextInt();
        scanner.nextLine();

        if (id == 0) {
            System.out.println("Update cancelled.");
            return;
        }

        Product existing = productController.getProductById(id);
        if (existing == null) {
            System.out.println("Product with ID " + id + " does not exist.");
            return;
        }

        System.out.println("\nUpdating Product #" + id + " (" + existing.getName() + "):");
        System.out.println("(Press ENTER to keep current value)");

        System.out.print("New Name [" + existing.getName() + "]: ");
        String name = scanner.nextLine().trim();
        if (!name.isEmpty()) existing.setName(name);

        System.out.print("New Category [" + (existing.getCategory() != null ? existing.getCategory() : "N/A") + "]: ");
        String category = scanner.nextLine().trim();
        if (!category.isEmpty()) existing.setCategory(category);

        System.out.print("New Price (PHP) [" + existing.getPrice() + "]: ");
        String priceInput = scanner.nextLine().trim();
        if (!priceInput.isEmpty()) {
            try {
                double price = Double.parseDouble(priceInput);
                existing.setPrice(price);
            } catch (NumberFormatException e) {
                System.out.println("Invalid price input. Keeping old price.");
            }
        }

        System.out.print("New Stock Quantity [" + existing.getQuantity() + "]: ");
        String qtyInput = scanner.nextLine().trim();
        if (!qtyInput.isEmpty()) {
            try {
                int qty = Integer.parseInt(qtyInput);
                existing.setQuantity(qty);
            } catch (NumberFormatException e) {
                System.out.println("Invalid stock input. Keeping old quantity.");
            }
        }

        System.out.print("New Status (ACTIVE/INACTIVE) [" + (existing.getStatus() != null ? existing.getStatus() : "ACTIVE") + "]: ");
        String status = scanner.nextLine().trim();
        if (!status.isEmpty()) existing.setStatus(status.toUpperCase());

        boolean success = productController.handleUpdateProduct(existing);
        if (success) {
            System.out.println("SUCCESS: Product #" + id + " updated successfully!");
        } else {
            System.out.println("FAILED: Could not update product.");
        }
    }

    // Deletes a product from the database by ID
    private void deleteProduct() {
        viewAllProducts();
        System.out.print("\nEnter Product ID to delete (0 to cancel): ");
        if (!scanner.hasNextInt()) {
            System.out.println("Invalid product ID.");
            scanner.nextLine();
            return;
        }

        int id = scanner.nextInt();
        scanner.nextLine();

        if (id == 0) {
            System.out.println("Delete action cancelled.");
            return;
        }

        Product existing = productController.getProductById(id);
        if (existing == null) {
            System.out.println("Product with ID " + id + " does not exist.");
            return;
        }

        System.out.print("Are you sure you want to delete '" + existing.getName() + "'? (Y/N): ");
        String confirm = scanner.nextLine().trim();
        if (confirm.equalsIgnoreCase("Y")) {
            boolean success = productController.handleDeleteProduct(id);
            if (success) {
                System.out.println("SUCCESS: Product #" + id + " deleted successfully!");
            } else {
                System.out.println("FAILED: Could not delete product.");
            }
        } else {
            System.out.println("Delete action cancelled.");
        }
    }

    // Helper method to pause execution until user presses Enter
    private void promptReturn() {
        System.out.print("\nPress Enter to continue...");
        scanner.nextLine();
    }

    // Helper method to format long text in table rows
    private String truncate(String text, int maxLength) {
        if (text == null) return "";
        if (text.length() <= maxLength) return text;
        return text.substring(0, maxLength - 3) + "...";
    }
}
