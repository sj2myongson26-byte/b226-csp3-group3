/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.joysistvi.ecommerce.repository;

import com.joysistvi.ecommerce.model.Cart;
import com.joysistvi.ecommerce.model.CartItem;

import java.util.List;

/**
 *
 * @author ktagl
 */
/**
 * CartRepo defines data access contracts for cart operations.
 */
public interface CartRepo {

    // Retrieves all cart records.
    List<Cart> getAllCartItems();

    // Fetches cart record by ID.
    Cart getCartById(int id);

    // Fetches cart by customer ID.
    Cart getCartByCustomerId(int customerId);

    // Inserts a new cart record.
    boolean addCart(Cart cart);

    // Updates cart record in database.
    boolean updateCart(Cart cart);

    // Deletes cart record by ID.
    boolean deleteCart(int id);

    // Adds a product item to a specific cart.
    boolean addCartItem(int cartId, int productId, int quantity, int unitPrice);

    // Updates item quantity in cart.
    boolean updateCartItemQuantity(int cartItemId, int newQuantity);

    // Removes an item from cart.
    boolean removeCartItem(int cartItemId);

    // Fetches all items belonging to a cart ID.
    List<CartItem> getCartItemsByCartId(int cartId);

    // Fetches or creates an active cart for customer.
    Cart getOrCreateCartByCustomerId(int customerId);
}