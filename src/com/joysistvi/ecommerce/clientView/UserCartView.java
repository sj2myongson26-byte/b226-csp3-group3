package com.joysistvi.ecommerce.clientView;

import com.joysistvi.ecommerce.controller.CartController;
import com.joysistvi.ecommerce.controller.ProductController;
import com.joysistvi.ecommerce.model.CartItem;
import com.joysistvi.ecommerce.model.Product;
import com.joysistvi.ecommerce.model.User;

import java.text.DecimalFormat;
import static com.joysistvi.ecommerce.utils.ClearScreen.clearScreen;
import static com.joysistvi.ecommerce.utils.Scan.scanner;
import java.util.List;

/**
 *
 * @author ktagl
 */
/**
 * UserCartView displays and manages customer cart items, quantity updates, and checkout menu options.
 */
public class UserCartView {

    private final CartController cartController = new CartController();
    private final ProductController productController = new ProductController();
    private final DecimalFormat currencyFormat = new DecimalFormat("#,##0.00");
    private List<CartItem> lastLoadedCartItems;

    // Overloaded display entry point default without user object.
    public void display() {
        display(null);
    }

    // Renders active user cart table view and processes customer menu selections.
    public void display(User user) {
        boolean exitCart = false;
        while (!exitCart) {
            clearScreen();
            System.out.println("==========================================================================================");
            System.out.println("                                   YOUR SHOPPING CART                                     ");
            System.out.println("==========================================================================================");

            int userId = (user != null) ? user.getId() : 1;
            List<CartItem> cartItems = cartController.getUserCartItems(userId);
            this.lastLoadedCartItems = cartItems;

            if (cartItems == null || cartItems.isEmpty()) {
                System.out.println("                      Your shopping cart is currently empty.                      ");
                System.out.println("==========================================================================================");
                System.out.println("1. Browse & Add Products to Cart");
                System.out.println("2. Return to Main Dashboard");
                System.out.print("Choose option: ");

                if (!scanner.hasNextInt()) {
                    System.out.println("PLEASE ENTER ONLY NUMBERS");
                    scanner.nextLine();
                    continue;
                }
                int choice = scanner.nextInt();
                scanner.nextLine();

                if (choice == 1) {
                    UserProductView productView = new UserProductView();
                    productView.addToCartDirectly(user);
                } else if (choice == 2) {
                    exitCart = true;
                } else {
                    System.out.println("Invalid choice. Enter 1 or 2.");
                    promptReturn();
                }
            } else {
                System.out.printf("%-10s | %-25s | %-16s | %-10s | %-16s\n", "Product ID", "Product Name", "Unit Price", "Quantity", "Total Price");
                System.out.println("------------------------------------------------------------------------------------------");

                double grandTotal = 0;
                for (CartItem item : cartItems) {
                    Product product = productController.getProductById(item.getProduct_id());
                    String productName = (product != null) ? product.getName() : "Product #" + item.getProduct_id();
                    double price = (product != null && product.getPrice() > 0) ? product.getPrice() : item.getUnit_price();
                    double itemTotal = item.getQuantity() * price;
                    grandTotal += itemTotal;

                    System.out.printf("%-10d | %-25s | PHP %-12s | %-10d | PHP %-12s\n",
                            item.getProduct_id(),
                            truncate(productName, 25),
                            currencyFormat.format(price),
                            item.getQuantity(),
                            currencyFormat.format(itemTotal)
                    );
                }
                System.out.println("------------------------------------------------------------------------------------------");
                System.out.printf("GRAND TOTAL: PHP %s\n", currencyFormat.format(grandTotal));
                System.out.println("==========================================================================================");

                System.out.println("\nOptions:");
                System.out.println("1. Add More Products to Cart");
                System.out.println("2. Update Item Quantity");
                System.out.println("3. Remove Item from Cart");
                System.out.println("4. Clear Entire Cart");
                System.out.println("5. Proceed to Checkout / Payment");
                System.out.println("6. Return to Main Dashboard");
                System.out.print("Choose option: ");

                if (!scanner.hasNextInt()) {
                    System.out.println("PLEASE ENTER ONLY NUMBERS");
                    scanner.nextLine();
                    continue;
                }
                int choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 1:
                        UserProductView productView = new UserProductView();
                        productView.addToCartDirectly(user);
                        break;
                    case 2:
                        updateQuantityFlow();
                        break;
                    case 3:
                        removeItemFlow();
                        break;
                    case 4:
                        clearCartFlow(user);
                        break;
                    case 5:
                        checkoutFlow(user);
                        break;
                    case 6:
                        exitCart = true;
                        break;
                    default:
                        System.out.println("Invalid option. Enter 1-6.");
                        promptReturn();
                }
            }
        }
    }

    // Prints current cart items table summary for update and remove operations.
    private void printCartItemsTable() {
        if (lastLoadedCartItems == null || lastLoadedCartItems.isEmpty()) {
            System.out.println("Your cart is currently empty.");
            return;
        }
        System.out.println("\n--- CURRENT CART ITEMS ---");
        System.out.printf("%-10s | %-25s | %-16s | %-10s | %-16s\n", "Product ID", "Product Name", "Unit Price", "Quantity", "Total Price");
        System.out.println("------------------------------------------------------------------------------------------");
        double grandTotal = 0;
        for (CartItem item : lastLoadedCartItems) {
            Product product = productController.getProductById(item.getProduct_id());
            String productName = (product != null) ? product.getName() : "Product #" + item.getProduct_id();
            double price = (product != null && product.getPrice() > 0) ? product.getPrice() : item.getUnit_price();
            double itemTotal = item.getQuantity() * price;
            grandTotal += itemTotal;

            System.out.printf("%-10d | %-25s | PHP %-12s | %-10d | PHP %-12s\n",
                    item.getProduct_id(),
                    truncate(productName, 25),
                    currencyFormat.format(price),
                    item.getQuantity(),
                    currencyFormat.format(itemTotal)
            );
        }
        System.out.println("------------------------------------------------------------------------------------------");
        System.out.printf("GRAND TOTAL: PHP %s\n", currencyFormat.format(grandTotal));
    }

    // Prompts customer to enter product ID and update item quantity in cart.
    private void updateQuantityFlow() {
        printCartItemsTable();
        System.out.print("\nEnter Product ID to update quantity: ");
        if (!scanner.hasNextInt()) {
            System.out.println("Invalid input. Product ID must be a number.");
            scanner.nextLine();
            promptReturn();
            return;
        }
        int productId = scanner.nextInt();
        scanner.nextLine();

        int cartItemId = findCartItemIdByProductId(productId);
        if (cartItemId <= 0) {
            System.out.println("Product ID " + productId + " is not in your cart.");
            promptReturn();
            return;
        }

        System.out.print("Enter New Quantity (0 to remove): ");
        if (!scanner.hasNextInt()) {
            System.out.println("Invalid input. Quantity must be a number.");
            scanner.nextLine();
            promptReturn();
            return;
        }
        int newQty = scanner.nextInt();
        scanner.nextLine();

        boolean success = cartController.handleUpdateQuantity(cartItemId, newQty);
        if (success) {
            System.out.println("SUCCESS: Item quantity updated!");
        } else {
            System.out.println("FAILED: Could not update item quantity.");
        }
        promptReturn();
    }

    // Prompts customer to enter product ID and removes item from cart.
    private void removeItemFlow() {
        printCartItemsTable();
        System.out.print("\nEnter Product ID to remove from cart: ");
        if (!scanner.hasNextInt()) {
            System.out.println("Invalid input. Product ID must be a number.");
            scanner.nextLine();
            promptReturn();
            return;
        }
        int productId = scanner.nextInt();
        scanner.nextLine();

        int cartItemId = findCartItemIdByProductId(productId);
        if (cartItemId <= 0) {
            System.out.println("Product ID " + productId + " is not in your cart.");
            promptReturn();
            return;
        }

        boolean success = cartController.handleRemoveItem(cartItemId);
        if (success) {
            System.out.println("SUCCESS: Item removed from cart!");
        } else {
            System.out.println("FAILED: Could not remove item from cart.");
        }
        promptReturn();
    }

    // Prompts customer confirmation and clears all items from cart.
    private void clearCartFlow(User user) {
        int userId = (user != null) ? user.getId() : 1;
        System.out.print("\nAre you sure you want to clear all items from your cart? (Y/N): ");
        String confirm = scanner.nextLine().trim();
        if (confirm.equalsIgnoreCase("Y")) {
            boolean success = cartController.handleClearCart(userId);
            if (success) {
                System.out.println("SUCCESS: Your shopping cart has been cleared!");
            } else {
                System.out.println("FAILED: Could not clear shopping cart.");
            }
        } else {
            System.out.println("Clear cart cancelled.");
        }
        promptReturn();
    }

    // Helper method to resolve cart item ID from product ID.
    private int findCartItemIdByProductId(int productId) {
        if (lastLoadedCartItems != null) {
            for (CartItem item : lastLoadedCartItems) {
                if (item.getProduct_id() == productId) {
                    return item.getId();
                }
            }
        }
        return 0;
    }

    // Helper method pausing UI until user presses Enter key.
    private void promptReturn() {
        System.out.print("\nPress Enter to continue...");
        scanner.nextLine();
    }

    // Handles cart checkout flow, order creation, and payment redirection.
    private void checkoutFlow(User user) {
        if (lastLoadedCartItems == null || lastLoadedCartItems.isEmpty()) {
            System.out.println("Your cart is empty. Cannot proceed to checkout.");
            promptReturn();
            return;
        }

        int userId = (user != null) ? user.getId() : 1;
        double grandTotal = 0;
        List<com.joysistvi.ecommerce.model.OrderItem> orderItems = new java.util.ArrayList<>();

        for (CartItem item : lastLoadedCartItems) {
            Product product = productController.getProductById(item.getProduct_id());
            double price = (product != null && product.getPrice() > 0) ? product.getPrice() : item.getUnit_price();
            double subtotal = item.getQuantity() * price;
            grandTotal += subtotal;

            orderItems.add(new com.joysistvi.ecommerce.model.OrderItem(
                    0,
                    item.getQuantity(),
                    (int) price,
                    (int) subtotal,
                    0,
                    item.getProduct_id()
            ));
        }

        System.out.print("Enter Shipping Address: ");
        String address = scanner.nextLine();
        if (address.trim().isEmpty()) {
            address = "Default Address";
        }

        com.joysistvi.ecommerce.model.Order newOrder = new com.joysistvi.ecommerce.model.Order(
                0,
                null,
                "PENDING",
                (int) grandTotal,
                address,
                "PENDING_SELECTION",
                userId
        );

        com.joysistvi.ecommerce.controller.OrderController orderController = new com.joysistvi.ecommerce.controller.OrderController();
        boolean orderPlaced = orderController.placeOrder(newOrder, orderItems);

        if (orderPlaced) {
            List<com.joysistvi.ecommerce.model.Order> userOrders = orderController.getCustomerOrders(userId);
            int orderId = 0;
            if (userOrders != null && !userOrders.isEmpty()) {
                orderId = userOrders.get(userOrders.size() - 1).getId();
            }

            for (CartItem item : lastLoadedCartItems) {
                cartController.handleRemoveItem(item.getId());
            }

            System.out.println("Order created successfully! Proceeding to Payment...");
            UserPaymentView paymentView = new UserPaymentView();
            paymentView.displayPaymentMenu(orderId, (int) grandTotal);
        } else {
            System.out.println("FAILED: Could not place order. Please try again.");
            promptReturn();
        }
    }

    // Truncates long product titles for clean table layout display.
    private String truncate(String text, int maxLength) {
        if (text == null) return "";
        if (text.length() <= maxLength) return text;
        return text.substring(0, maxLength - 3) + "...";
    }
}
