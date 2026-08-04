/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.joysistvi.ecommerce.controller;

import com.joysistvi.ecommerce.model.Order;
import com.joysistvi.ecommerce.model.OrderItem;
import com.joysistvi.ecommerce.repository.OrderRepo;
import com.joysistvi.ecommerce.repository.OrderRepoImpl;

import java.util.List;

/**
 *
 * @author ktagl
 */
/**
 * OrderController bridges order handling between presentation views and OrderRepo data access.
 */
public class OrderController {

    private final OrderRepo orderRepo;

    // Initializes controller with default OrderRepo implementation instance.
    public OrderController() {
        this.orderRepo = new OrderRepoImpl();
    }

    // Validates order payload and passes to repository to create new order record.
    public boolean placeOrder(Order order, List<OrderItem> items) {
        if (order == null || items == null || items.isEmpty()) {
            System.err.println("Cannot place order: Order details or items list is empty.");
            return false;
        }
        return orderRepo.createOrder(order, items);
    }

    // Fetches single order details matching specified order ID.
    public Order getOrderDetails(int orderId) {
        return orderRepo.getOrderById(orderId);
    }

    // Retrieves complete list of all orders stored in the system.
    public List<Order> getAllOrders() {
        return orderRepo.getAllOrders();
    }

    // Fetches order records filtered by status string.
    public List<Order> getOrdersByStatus(String status) {
        return orderRepo.getOrdersByStatus(status);
    }

    // Retrieves all orders placed by a specific customer ID.
    public List<Order> getCustomerOrders(int customerId) {
        return orderRepo.getOrderByCustomersId(customerId);
    }

    // Retrieves all purchased item details belonging to a specific order ID.
    public List<OrderItem> getOrderItems(int orderId) {
        return orderRepo.getOrderItemsByOrderId(orderId);
    }

    // Validates status string and updates status of specified order ID in database.
    public boolean updateStatus(int orderId, String newStatus) {
        if (newStatus == null || newStatus.trim().isEmpty()) {
            return false;
        }
        return orderRepo.updateOrderStatus(orderId, newStatus);
    }

    // Cancels and deletes specified order record from database.
    public boolean cancelOrder(int orderId) {
        return orderRepo.deleteOrder(orderId);
    }
}
