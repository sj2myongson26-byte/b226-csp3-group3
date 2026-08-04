/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.joysistvi.ecommerce.repository;

import com.joysistvi.ecommerce.model.Order;
import com.joysistvi.ecommerce.model.OrderItem;
import java.util.List;

/**
 *
 * @author ktagl
 */
/**
 * OrderRepo defines data access contracts for customer order management.
 */
public interface OrderRepo {

    // Creates a new order along with its line items.
    boolean createOrder(Order order, List<OrderItem> items);

    // Retrieves order details by order ID.
    Order getOrderById(int id);

    // Retrieves orders filtered by status string.
    List<Order> getOrdersByStatus(String status);

    // Retrieves all orders in the system.
    List<Order> getAllOrders();

    // Retrieves orders placed by a specific customer ID.
    List<Order> getOrderByCustomersId(int customerId);

    // Retrieves line items belonging to a specific order ID.
    List<OrderItem> getOrderItemsByOrderId(int orderId);

    // Updates status of specified order ID.
    boolean updateOrderStatus(int orderId, String newStatus);

    // Updates complete order record in database.
    boolean updateOrder(Order order);

    // Deletes an order and its line items by order ID.
    boolean deleteOrder(int id);
}
