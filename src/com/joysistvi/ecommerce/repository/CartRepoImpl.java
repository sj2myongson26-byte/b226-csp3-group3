/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.joysistvi.ecommerce.repository;

import com.joysistvi.ecommerce.config.dbconnection;
import com.joysistvi.ecommerce.model.Cart;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ktagl
 */
/**
 * CartRepoImpl implements CartRepo data access for shopping cart management in MySQL database.
 */
public class CartRepoImpl implements CartRepo{

    private final dbconnection db = new dbconnection();

    // Retrieves all active shopping carts from database.
    @Override
    public List<Cart> getAllCartItems() {
        List<Cart> cartList = new ArrayList<>();
        String query = "SELECT * FROM cart";

        try (Connection conn = db.connect();
             PreparedStatement prep = conn.prepareStatement(query);
             ResultSet res = prep.executeQuery()) {

            while (res.next()) {
                Cart cart = new Cart(
                        res.getInt("cart_id"),
                        res.getString("created_at"),
                        res.getString("updated_at"),
                        res.getInt("customer_id")
                );
                cartList.add(cart);
            }
        } catch (SQLException e) {
            System.out.println("Cart Items Error: " + e.getMessage());
        }

        return cartList;
    }

    // Fetches single cart record matching cart ID.
    @Override
    public Cart getCartById(int id) {
        String query = "SELECT * FROM cart WHERE cart_id = ?";

        try (Connection conn = db.connect();
             PreparedStatement prep = conn.prepareStatement(query)) {

            prep.setInt(1, id);

            try (ResultSet res = prep.executeQuery()) {
                if (res.next()) {
                    return new Cart(
                            res.getInt("cart_id"),
                            res.getString("created_at"),
                            res.getString("updated_at"),
                            res.getInt("customer_id")
                    );
                }
            }
        } catch (SQLException e) {
            System.out.println("Get Cart By ID Error: " + e.getMessage());
        }

        return null;
    }

    // Fetches cart record associated with customer ID.
    @Override
    public Cart getCartByCustomerId(int customerId) {
        String query = "SELECT * FROM cart WHERE customer_id = ?";

        try (Connection conn = db.connect();
             PreparedStatement prep = conn.prepareStatement(query)) {

            prep.setInt(1, customerId);

            try (ResultSet res = prep.executeQuery()) {
                if (res.next()) {
                    return new Cart(
                            res.getInt("cart_id"),
                            res.getString("created_at"),
                            res.getString("updated_at"),
                            res.getInt("customer_id")
                    );
                }
            }
        } catch (SQLException e) {
            System.out.println("Get Cart By Customer ID Error: " + e.getMessage());
        }

        return null;
    }

    // Inserts a new customer cart record into database.
    @Override
    public boolean addCart(Cart cart) {
        String query = "INSERT INTO cart (customer_id) VALUES (?)";

        try (Connection conn = db.connect();
             PreparedStatement prep = conn.prepareStatement(query)) {

            prep.setInt(1, cart.getCustomer_id());
            return prep.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Add Cart Error: " + e.getMessage());
        }

        return false;
    }

    // Updates cart record in database.
    @Override
    public boolean updateCart(Cart cart) {
        String query = "UPDATE cart SET customer_id = ? WHERE cart_id = ?";

        try (Connection conn = db.connect();
             PreparedStatement prep = conn.prepareStatement(query)) {

            prep.setInt(1, cart.getCustomer_id());
            prep.setInt(2, cart.getId());

            return prep.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Update Cart Error: " + e.getMessage());
        }

        return false;
    }

    // Deletes cart record from database by ID.
    @Override
    public boolean deleteCart(int id) {
        String query = "DELETE FROM cart WHERE cart_id = ?";

        try (Connection conn = db.connect();
             PreparedStatement prep = conn.prepareStatement(query)) {

            prep.setInt(1, id);
            return prep.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Delete Cart Error: " + e.getMessage());
        }

        return false;
    }

    // Inserts product item or increments quantity if product already exists in cart.
    @Override
    public boolean addCartItem(int cartId, int productId, int quantity, int unitPrice) {
        String checkQuery = "SELECT cart_item_id, quantity FROM cart_items WHERE cart_id = ? AND product_id = ?";
        String updateQuery = "UPDATE cart_items SET quantity = quantity + ?, unit_price = ? WHERE cart_item_id = ?";
        String insertQuery = "INSERT INTO cart_items (cart_id, product_id, quantity, unit_price) VALUES (?, ?, ?, ?)";

        try (Connection conn = db.connect()) {
            try (PreparedStatement checkPrep = conn.prepareStatement(checkQuery)) {
                checkPrep.setInt(1, cartId);
                checkPrep.setInt(2, productId);
                try (ResultSet res = checkPrep.executeQuery()) {
                    if (res.next()) {
                        int cartItemId = res.getInt("cart_item_id");
                        try (PreparedStatement updatePrep = conn.prepareStatement(updateQuery)) {
                            updatePrep.setInt(1, quantity);
                            updatePrep.setInt(2, unitPrice);
                            updatePrep.setInt(3, cartItemId);
                            return updatePrep.executeUpdate() > 0;
                        }
                    }
                }
            }

            try (PreparedStatement insertPrep = conn.prepareStatement(insertQuery)) {
                insertPrep.setInt(1, cartId);
                insertPrep.setInt(2, productId);
                insertPrep.setInt(3, quantity);
                insertPrep.setInt(4, unitPrice);
                return insertPrep.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            System.out.println("Add Cart Item Error: " + e.getMessage());
        }

        return false;
    }

    // Updates quantity of specific item in cart or removes item if new quantity is zero.
    @Override
    public boolean updateCartItemQuantity(int cartItemId, int newQuantity) {
        if (newQuantity <= 0) {
            return removeCartItem(cartItemId);
        }

        String query = "UPDATE cart_items SET quantity = ? WHERE cart_item_id = ?";

        try (Connection conn = db.connect();
             PreparedStatement prep = conn.prepareStatement(query)) {

            prep.setInt(1, newQuantity);
            prep.setInt(2, cartItemId);
            return prep.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Update Cart Item Quantity Error: " + e.getMessage());
        }

        return false;
    }

    // Removes single item record from shopping cart by item ID.
    @Override
    public boolean removeCartItem(int cartItemId) {
        String query = "DELETE FROM cart_items WHERE cart_item_id = ?";

        try (Connection conn = db.connect();
             PreparedStatement prep = conn.prepareStatement(query)) {

            prep.setInt(1, cartItemId);
            return prep.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Remove Cart Item Error: " + e.getMessage());
        }

        return false;
    }

    // Retrieves all items stored in a specific cart ID.
    @Override
    public List<com.joysistvi.ecommerce.model.CartItem> getCartItemsByCartId(int cartId) {
        List<com.joysistvi.ecommerce.model.CartItem> items = new ArrayList<>();
        String query = "SELECT * FROM cart_items WHERE cart_id = ?";

        try (Connection conn = db.connect();
             PreparedStatement prep = conn.prepareStatement(query)) {

            prep.setInt(1, cartId);

            try (ResultSet res = prep.executeQuery()) {
                while (res.next()) {
                    com.joysistvi.ecommerce.model.CartItem item = new com.joysistvi.ecommerce.model.CartItem(
                            res.getInt("cart_item_id"),
                            res.getInt("quantity"),
                            res.getInt("unit_price"),
                            res.getString("added_at"),
                            res.getInt("cart_id"),
                            res.getInt("product_id")
                    );
                    items.add(item);
                }
            }
        } catch (SQLException e) {
            System.out.println("Get Cart Items Error: " + e.getMessage());
        }

        return items;
    }

    // Fetches existing active cart or auto-creates cart record for user.
    @Override
    public Cart getOrCreateCartByCustomerId(int userId) {
        int customerId = ensureCustomerExistsByUserId(userId);
        if (customerId <= 0) {
            System.out.println("Failed to get or create customer record.");
            return null;
        }

        Cart existingCart = getCartByCustomerId(customerId);
        if (existingCart != null) {
            return existingCart;
        }

        String insertQuery = "INSERT INTO cart (customer_id) VALUES (?)";
        try (Connection conn = db.connect();
             PreparedStatement prep = conn.prepareStatement(insertQuery, java.sql.Statement.RETURN_GENERATED_KEYS)) {

            prep.setInt(1, customerId);
            int affectedRows = prep.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = prep.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int cartId = generatedKeys.getInt(1);
                        return getCartById(cartId);
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Create Cart Error: " + e.getMessage());
        }

        return getCartByCustomerId(customerId);
    }

    // Helper method to ensure customer record exists for user ID.
    private int ensureCustomerExistsByUserId(int userId) {
        String checkQuery = "SELECT customer_id FROM customer WHERE user_id = ?";
        String insertQuery = "INSERT INTO customer (user_id, phone, address) VALUES (?, '', '')";

        try (Connection conn = db.connect()) {
            try (PreparedStatement checkPrep = conn.prepareStatement(checkQuery)) {
                checkPrep.setInt(1, userId);
                try (ResultSet res = checkPrep.executeQuery()) {
                    if (res.next()) {
                        return res.getInt("customer_id");
                    }
                }
            }

            try (PreparedStatement insertPrep = conn.prepareStatement(insertQuery, java.sql.Statement.RETURN_GENERATED_KEYS)) {
                insertPrep.setInt(1, userId);
                int affected = insertPrep.executeUpdate();
                if (affected > 0) {
                    try (ResultSet generatedKeys = insertPrep.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            return generatedKeys.getInt(1);
                        }
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Ensure Customer Exists Error: " + e.getMessage());
        }

        try (Connection conn = db.connect();
             PreparedStatement checkPrep = conn.prepareStatement(checkQuery)) {
            checkPrep.setInt(1, userId);
            try (ResultSet res = checkPrep.executeQuery()) {
                if (res.next()) {
                    return res.getInt("customer_id");
                }
            }
        } catch (SQLException ignored) {}

        return 0;
    }
}