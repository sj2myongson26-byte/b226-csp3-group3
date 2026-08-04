package com.joysistvi.ecommerce.adminView;

import com.joysistvi.ecommerce.controller.OrderController;
import com.joysistvi.ecommerce.controller.ProductController;
import com.joysistvi.ecommerce.model.Order;
import com.joysistvi.ecommerce.model.OrderItem;
import com.joysistvi.ecommerce.model.Product;

import java.text.DecimalFormat;
import java.util.List;

import static com.joysistvi.ecommerce.utils.ClearScreen.clearScreen;
import static com.joysistvi.ecommerce.utils.Scan.scanner;

/**
 * OrderView provides the Admin UI for managing customer orders and updating statuses.
 */
public class OrderView {

    private final OrderController orderController;
    private final ProductController productController = new ProductController();
    private final DecimalFormat df = new DecimalFormat("#,##0.00");

    // Constructor with OrderController dependency
    public OrderView(OrderController orderController) {
        this.orderController = orderController;
    }

    // Default constructor initializing OrderController automatically
    public OrderView() {
        this.orderController = new OrderController();
    }

    // Main Dashboard loop for Admin Order Management
    public void dashboard() {
        while (true) {
            clearScreen();
            System.out.println("==========================================================================================");
            System.out.println("                                 ADMIN ORDER MANAGEMENT                                   ");
            System.out.println("==========================================================================================");
            System.out.println("1. View All Orders");
            System.out.println("2. Filter Orders by Status (PENDING, SHIPPING, DELIVERED, CANCELLED)");
            System.out.println("3. View Order Details by ID");
            System.out.println("4. Update Order Status");
            System.out.println("5. Delete Order Record");
            System.out.println("6. Back to Admin Dashboard");
            System.out.print("Choose: ");

            if (!scanner.hasNextInt()) {
                System.out.println("PLEASE ENTER ONLY NUMBERS");
                scanner.nextLine();
                promptReturn();
                continue;
            }

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    viewAllOrders();
                    promptReturn();
                    break;
                case 2:
                    filterOrdersByStatus();
                    promptReturn();
                    break;
                case 3:
                    viewOrderDetails();
                    promptReturn();
                    break;
                case 4:
                    updateOrder();
                    promptReturn();
                    break;
                case 5:
                    deleteOrder();
                    promptReturn();
                    break;
                case 6:
                    return; // Return to Admin Main Dashboard
                default:
                    System.out.println("Invalid choice. Please enter 1-6.");
                    promptReturn();
            }
        }
    }

    // Displays a table summary of all customer orders in the system
    private void viewAllOrders() {
        List<Order> orders = orderController.getAllOrders();
        System.out.println("\n--------------------------------------------------------------------------------------------------");
        System.out.printf("%-8s | %-12s | %-20s | %-16s | %-15s | %-12s\n",
                "Order ID", "Customer ID", "Order Date", "Total Amount", "Payment Method", "Status");
        System.out.println("--------------------------------------------------------------------------------------------------");

        if (orders == null || orders.isEmpty()) {
            System.out.println("                              No orders found in the system.                              ");
        } else {
            for (Order o : orders) {
                System.out.printf("%-8d | %-12d | %-20s | PHP %-12s | %-15s | %-12s\n",
                        o.getId(),
                        o.getCustomer_id(),
                        truncate(o.getOrder_date() != null ? o.getOrder_date() : "N/A", 20),
                        df.format(o.getTotal_amount()),
                        truncate(o.getPayment_method() != null ? o.getPayment_method() : "N/A", 15),
                        "[" + (o.getStatus() != null ? o.getStatus().toUpperCase() : "PENDING") + "]"
                );
            }
        }
        System.out.println("--------------------------------------------------------------------------------------------------");
    }

    // Prompts admin for order status enum filter and displays matching orders table
    private void filterOrdersByStatus() {
        System.out.println("\nSelect Status Filter:");
        System.out.println("1. PENDING");
        System.out.println("2. SHIPPING");
        System.out.println("3. DELIVERED");
        System.out.println("4. CANCELLED");
        System.out.print("Choose: ");

        if (!scanner.hasNextInt()) {
            System.out.println("PLEASE ENTER ONLY NUMBERS");
            scanner.nextLine();
            return;
        }

        int choice = scanner.nextInt();
        scanner.nextLine();

        String statusFilter = "";
        switch (choice) {
            case 1: statusFilter = "PENDING"; break;
            case 2: statusFilter = "SHIPPING"; break;
            case 3: statusFilter = "DELIVERED"; break;
            case 4: statusFilter = "CANCELLED"; break;
            default:
                System.out.println("Invalid selection.");
                return;
        }

        List<Order> orders = orderController.getOrdersByStatus(statusFilter);
        System.out.println("\n--------------------------------------------------------------------------------------------------");
        System.out.printf("%-8s | %-12s | %-20s | %-16s | %-15s | %-12s\n",
                "Order ID", "Customer ID", "Order Date", "Total Amount", "Payment Method", "Status");
        System.out.println("--------------------------------------------------------------------------------------------------");

        if (orders == null || orders.isEmpty()) {
            System.out.println("                          No [" + statusFilter + "] orders found.                          ");
        } else {
            for (Order o : orders) {
                System.out.printf("%-8d | %-12d | %-20s | PHP %-12s | %-15s | %-12s\n",
                        o.getId(),
                        o.getCustomer_id(),
                        truncate(o.getOrder_date() != null ? o.getOrder_date() : "N/A", 20),
                        df.format(o.getTotal_amount()),
                        truncate(o.getPayment_method() != null ? o.getPayment_method() : "N/A", 15),
                        "[" + (o.getStatus() != null ? o.getStatus().toUpperCase() : "PENDING") + "]"
                );
            }
        }
        System.out.println("--------------------------------------------------------------------------------------------------");
    }

    // Views detailed breakdown of a specific order by ID
    private void viewOrderDetails() {
        viewAllOrders();
        System.out.print("\nEnter Order ID to view details: ");
        if (!scanner.hasNextInt()) {
            System.out.println("Invalid input. Order ID must be a number.");
            scanner.nextLine();
            return;
        }

        int orderId = scanner.nextInt();
        scanner.nextLine();

        Order o = orderController.getOrderDetails(orderId);
        if (o == null) {
            System.out.println("Order with ID #" + orderId + " not found.");
            return;
        }

        System.out.println("\n==========================================================================================");
        System.out.println("                                   ORDER #" + o.getId() + " DETAILS                                   ");
        System.out.println("==========================================================================================");
        System.out.println("Customer ID     : " + o.getCustomer_id());
        System.out.println("Order Date      : " + (o.getOrder_date() != null ? o.getOrder_date() : "N/A"));
        System.out.println("Status          : [" + (o.getStatus() != null ? o.getStatus().toUpperCase() : "PENDING") + "]");
        System.out.println("Shipping Address: " + (o.getAddress() != null ? o.getAddress() : "N/A"));
        System.out.println("Payment Method  : " + (o.getPayment_method() != null ? o.getPayment_method() : "N/A"));
        System.out.println("Total Amount    : PHP " + df.format(o.getTotal_amount()));
        System.out.println("------------------------------------------------------------------------------------------");
        System.out.println("Items Purchased :");

        List<OrderItem> items = orderController.getOrderItems(orderId);
        if (items != null && !items.isEmpty()) {
            System.out.printf("  %-10s | %-30s | %-8s | %-12s | %-12s\n", "Product ID", "Product Name", "Qty", "Unit Price", "Subtotal");
            System.out.println("  ----------------------------------------------------------------------------------------");
            for (OrderItem item : items) {
                Product p = productController.getProductById(item.getProduct_id());
                String pName = (p != null) ? p.getName() : "Product #" + item.getProduct_id();
                System.out.printf("  %-10d | %-30s | x%-7d | PHP %-8s | PHP %-8s\n",
                        item.getProduct_id(),
                        truncate(pName, 30),
                        item.getQuantity(),
                        df.format(item.getUnit_price()),
                        df.format(item.getSubtotal())
                );
            }
        } else {
            System.out.println("  No item details recorded for this order.");
        }
        System.out.println("==========================================================================================");
    }

    // Updates an order's status (PENDING, SHIPPING, DELIVERED, CANCELLED)
    private void updateOrder() {
        viewAllOrders();
        System.out.print("\nEnter Order ID to update status (0 to cancel): ");
        if (!scanner.hasNextInt()) {
            System.out.println("Invalid Order ID.");
            scanner.nextLine();
            return;
        }

        int orderId = scanner.nextInt();
        scanner.nextLine();

        if (orderId == 0) {
            System.out.println("Update order cancelled.");
            return;
        }

        Order o = orderController.getOrderDetails(orderId);
        if (o == null) {
            System.out.println("Order #" + orderId + " not found.");
            return;
        }

        System.out.println("\nCurrent Status for Order #" + orderId + ": [" + o.getStatus() + "]");
        System.out.println("Select New Status:");
        System.out.println("1. PENDING");
        System.out.println("2. SHIPPING");
        System.out.println("3. DELIVERED");
        System.out.println("4. CANCELLED");
        System.out.print("Choose status (1-4): ");

        if (!scanner.hasNextInt()) {
            System.out.println("Invalid option.");
            scanner.nextLine();
            return;
        }
        int statusChoice = scanner.nextInt();
        scanner.nextLine();

        String newStatus;
        switch (statusChoice) {
            case 1: newStatus = "PENDING"; break;
            case 2: newStatus = "SHIPPING"; break;
            case 3: newStatus = "DELIVERED"; break;
            case 4: newStatus = "CANCELLED"; break;
            default:
                System.out.println("Invalid status option.");
                return;
        }

        boolean success = orderController.updateStatus(orderId, newStatus);
        if (success) {
            System.out.println("SUCCESS: Order #" + orderId + " status updated to [" + newStatus + "]!");
        } else {
            System.out.println("FAILED: Could not update order status.");
        }
    }

    // Deletes / cancels an order by ID
    private void deleteOrder() {
        viewAllOrders();
        System.out.print("\nEnter Order ID to delete (0 to cancel): ");
        if (!scanner.hasNextInt()) {
            System.out.println("Invalid Order ID.");
            scanner.nextLine();
            return;
        }

        int orderId = scanner.nextInt();
        scanner.nextLine();

        if (orderId == 0) {
            System.out.println("Delete order cancelled.");
            return;
        }

        Order o = orderController.getOrderDetails(orderId);
        if (o == null) {
            System.out.println("Order #" + orderId + " not found.");
            return;
        }

        System.out.print("Are you sure you want to delete Order #" + orderId + "? (Y/N): ");
        String confirm = scanner.nextLine().trim();
        if (confirm.equalsIgnoreCase("Y")) {
            boolean success = orderController.cancelOrder(orderId);
            if (success) {
                System.out.println("SUCCESS: Order #" + orderId + " deleted successfully!");
            } else {
                System.out.println("FAILED: Could not delete order.");
            }
        } else {
            System.out.println("Delete order cancelled.");
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
