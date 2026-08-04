/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.joysistvi.ecommerce.repository;


import com.joysistvi.ecommerce.config.dbconnection;
import com.joysistvi.ecommerce.model.Order;
import com.joysistvi.ecommerce.model.OrderItem;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ktagl
 */
/**
 * OrderRepoImpl implements OrderRepo data access operations for customer orders and items in MySQL.
 */
public class OrderRepoImpl implements OrderRepo {

    private final dbconnection dbconfig = new dbconnection();

    // Helper method to establish and return active JDBC database connection.
    private Connection getConnection() throws SQLException {
        Connection conn = dbconfig.connect();
        if (conn == null) {
            throw new SQLException("Failed to obtain database connection from dbconnection class.");
        }
        return conn;
    }

    // Creates new transaction order, batch inserts line items, and deducts product stock.
    @Override
    public boolean createOrder(Order order, List<OrderItem> items) {
        String insertOrderSql = "INSERT INTO orders (status, total_amount, shipping_address, payment_method,customer_id) VALUES (?, ?, ?, ?, ?)";
        String insertItemSql = "INSERT INTO order_items (quantity, unit_price, subtotal, order_id, product_id) VALUES (?, ?, ?, ?, ?)";
        String deductStockSql = "UPDATE products SET quantity = GREATEST(0, quantity - ?) WHERE product_id = ?";

        Connection conn = null;
        try {
            conn = getConnection();
            conn.setAutoCommit(false);

            PreparedStatement orderStmt = conn.prepareStatement(insertOrderSql, Statement.RETURN_GENERATED_KEYS);
            orderStmt.setString(1, order.getStatus());
            orderStmt.setInt(2, order.getTotal_amount());
            orderStmt.setString(3, order.getAddress());

            orderStmt.setString(4, order.getPayment_method() != null ? order.getPayment_method() : "1");
            orderStmt.setInt(5, order.getCustomer_id());

            int affectedRows = orderStmt.executeUpdate();
            if (affectedRows == 0) {
                conn.rollback();
                return false;
            }

            ResultSet generatedKeys = orderStmt.getGeneratedKeys();
            int generatedOrderId = -1;
            if (generatedKeys.next()) {
                generatedOrderId = generatedKeys.getInt(1);
            } else {
                conn.rollback();
                return false;
            }

            PreparedStatement itemStmt = conn.prepareStatement(insertItemSql);
            PreparedStatement stockStmt = conn.prepareStatement(deductStockSql);

            for (OrderItem item : items) {
                itemStmt.setInt(1, item.getQuantity());
                itemStmt.setInt(2, item.getUnit_price());
                itemStmt.setInt(3, item.getSubtotal());
                itemStmt.setInt(4, generatedOrderId);
                itemStmt.setInt(5, item.getProduct_id());
                itemStmt.addBatch();

                stockStmt.setInt(1, item.getQuantity());
                stockStmt.setInt(2, item.getProduct_id());
                stockStmt.addBatch();
            }
            itemStmt.executeBatch();
            stockStmt.executeBatch();

            conn.commit();
            return true;

        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            e.printStackTrace();
            return false;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    // Fetches single order details matching specified order ID.
    @Override
    public Order getOrderById(int id) {
        String sql = "SELECT * FROM orders WHERE order_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToOrder(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Retrieves all customer order records from database.
    @Override
    public List<Order> getAllOrders() {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT * FROM orders";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                orders.add(mapResultSetToOrder(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }

    // Fetches order records filtered by status enum value.
    @Override
    public List<Order> getOrdersByStatus(String status) {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT * FROM orders WHERE status = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, status);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    orders.add(mapResultSetToOrder(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }

    // Retrieves all orders placed by a specific customer ID.
    @Override
    public List<Order> getOrderByCustomersId(int customerId) {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT * FROM orders WHERE customer_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, customerId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                orders.add(mapResultSetToOrder(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }

    // Retrieves list of purchased items for a specific order ID.
    @Override
    public List<OrderItem> getOrderItemsByOrderId(int orderId) {
        List<OrderItem> items = new ArrayList<>();
        String sql = "SELECT * FROM order_items WHERE order_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, orderId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int itemId = 0;
                try {
                    itemId = rs.getInt("order_item_id");
                } catch (SQLException ex) {
                    try {
                        itemId = rs.getInt("id");
                    } catch (SQLException ignored) {}
                }
                OrderItem item = new OrderItem(
                        itemId,
                        rs.getInt("quantity"),
                        rs.getInt("unit_price"),
                        rs.getInt("subtotal"),
                        rs.getInt("order_id"),
                        rs.getInt("product_id")
                );
                items.add(item);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return items;
    }

    // Updates status string of specified order ID in database.
    @Override
    public boolean updateOrderStatus(int orderId, String newStatus) {
        String sql = "UPDATE orders SET status = ? WHERE order_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, newStatus);
            stmt.setInt(2, orderId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Updates order fields in database.
    @Override
    public boolean updateOrder(Order order) {
        String sql = "UPDATE orders SET status = ?, total_amount = ?, shipping_address = ?, payment_method = ?, customer_id = ? " +
                "WHERE order_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)){

        stmt.setString(1, order.getStatus());
        stmt.setInt(2, order.getTotal_amount());
        stmt.setString(3, order.getAddress());
        stmt.setString(4, order.getPayment_method() != null ? order.getPayment_method() : "1");
        stmt.setInt(5, order.getCustomer_id());
        stmt.setInt(6, order.getId());
        return stmt.executeUpdate() > 0;
    } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
}

// Deletes specified order record, restores product inventory, and deletes associated line items within transaction.
@Override
public boolean deleteOrder(int id) {
    String restoreStockSql = "UPDATE products p JOIN order_items oi ON p.product_id = oi.product_id SET p.quantity = p.quantity + oi.quantity WHERE oi.order_id = ?";
    String deleteItemSql = "DELETE FROM order_items WHERE order_id = ?";
    String deleteOrderSql = "DELETE FROM orders WHERE order_id = ?";

    Connection conn = null;
    try {
        conn = getConnection();
        conn.setAutoCommit(false);

        try (PreparedStatement restoreStmt = conn.prepareStatement(restoreStockSql)) {
            restoreStmt.setInt(1, id);
            restoreStmt.executeUpdate();
        }

        try (PreparedStatement itemStmt = conn.prepareStatement(deleteItemSql)) {
            itemStmt.setInt(1, id);
            itemStmt.executeUpdate();
        }
        boolean success = false;
        try (PreparedStatement orderStmt = conn.prepareStatement(deleteOrderSql)) {
            orderStmt.setInt(1, id);
            success = orderStmt.executeUpdate() > 0;
        }
        conn.commit();
        return success;

    } catch (SQLException e) {
        if (conn != null) {
            try {conn.rollback();} catch (SQLException ex) { ex.printStackTrace(); }
        }
        e.printStackTrace();
        return false;
    } finally {
        if (conn != null) {
            try {conn.setAutoCommit(true); conn.close();} catch (SQLException ex) { ex.printStackTrace(); }
        }
    }
    }

    // Maps database ResultSet row to Order model object.
    private Order mapResultSetToOrder(ResultSet rs) throws SQLException {
        return new Order(
                rs.getInt("order_id"),
                rs.getString("order_date"),
                rs.getString("status"),
                rs.getInt("total_amount"),
                rs.getString("shipping_address"),
                rs.getString("payment_method"),
                rs.getInt("customer_id")
        );
    }
}
