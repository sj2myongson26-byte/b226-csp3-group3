package com.joysistvi.ecommerce.controller;

import com.joysistvi.ecommerce.model.CartItem;
import com.joysistvi.ecommerce.services.CartService;

import java.util.List;

/**
 * CartController manages cart requests between presentation views and CartService.
 */
public class CartController {
    private final CartService cartService = new CartService();

    // Adds a product item to a customer's active shopping cart.
    public boolean handleAddItemToCustomerCart(int customerId, int productId, int quantity, int unitPrice) {
        return cartService.addItemToCustomerCart(customerId, productId, quantity, unitPrice);
    }

    // Retrieves all items stored in a customer's shopping cart.
    public List<CartItem> getUserCartItems(int userId) {
        return cartService.getCartItemsByCustomerId(userId);
    }

    // Updates the quantity of a specific item inside the shopping cart.
    public boolean handleUpdateQuantity(int cartItemId, int newQuantity) {
        return cartService.updateCartItemQuantity(cartItemId, newQuantity);
    }

    // Removes an item from the customer's shopping cart.
    public boolean handleRemoveItem(int cartItemId) {
        return cartService.removeCartItem(cartItemId);
    }

    // Clears all items from a customer's cart after successful checkout.
    public boolean handleClearCart(int userId) {
        return cartService.clearCartByCustomerId(userId);
    }
}