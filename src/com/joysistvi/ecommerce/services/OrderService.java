package com.joysistvi.ecommerce.services;

import com.joysistvi.ecommerce.model.Order;
import com.joysistvi.ecommerce.model.OrderItem;
import com.joysistvi.ecommerce.repository.OrderRepo;
import com.joysistvi.ecommerce.repository.OrderRepoImpl;

import java.util.List;

/**
 * OrderService manages business logic and validation for customer order processing.
 */
public class OrderService {

    private final OrderRepo orderRepo = new OrderRepoImpl();

    // Validates order parameters and delegates order placement to repository.
    public boolean placeOrder(Order order, List<OrderItem> items) {
        if (order == null || items == null || items.isEmpty()) {
            return false;
        }
        return orderRepo.createOrder(order, items);
    }

    // Fetches order details matching specified order ID.
    public Order getOrderDetails(int orderId) {
        return orderRepo.getOrderById(orderId);
    }

    // Retrieves all customer orders stored in system.
    public List<Order> getAllOrders() {
        return orderRepo.getAllOrders();
    }

    // Fetches order records filtered by status enum value.
    public List<Order> getOrdersByStatus(String status) {
        return orderRepo.getOrdersByStatus(status);
    }

    // Retrieves all orders belonging to a specific customer ID.
    public List<Order> getCustomerOrders(int customerId) {
        return orderRepo.getOrderByCustomersId(customerId);
    }

    // Retrieves line items associated with an order ID.
    public List<OrderItem> getOrderItems(int orderId) {
        return orderRepo.getOrderItemsByOrderId(orderId);
    }

    // Updates status string of specified order ID.
    public boolean updateStatus(int orderId, String newStatus) {
        return orderRepo.updateOrderStatus(orderId, newStatus);
    }

    // Cancels and removes order record by ID.
    public boolean cancelOrder(int orderId) {
        return orderRepo.deleteOrder(orderId);
    }
}
