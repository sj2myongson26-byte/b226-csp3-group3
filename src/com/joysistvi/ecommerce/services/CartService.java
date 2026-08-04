package com.joysistvi.ecommerce.services;

import com.joysistvi.ecommerce.model.Cart;
import com.joysistvi.ecommerce.model.CartItem;
import com.joysistvi.ecommerce.repository.CartRepo;
import com.joysistvi.ecommerce.repository.CartRepoImpl;

import java.util.Collections;
import java.util.List;

/**
 * CartService handles business logic and validation for shopping cart management.
 */
public class CartService {

    private final CartRepo cartRepo = new CartRepoImpl();

    // Retrieves all cart records from repository.
    public List<Cart> getAllCartItems() {
        return cartRepo.getAllCartItems();
    }

    // Retrieves a single cart by cart ID.
    public Cart getCartById(int id) {
        return cartRepo.getCartById(id);
    }

    // Retrieves cart record by customer ID.
    public Cart getCartByCustomerId(int customerId) {
        return cartRepo.getCartByCustomerId(customerId);
    }

    // Adds a new cart record to repository.
    public boolean addCart(Cart cart) {
        return cartRepo.addCart(cart);
    }

    // Updates cart record in repository.
    public boolean updateCart(Cart cart) {
        return cartRepo.updateCart(cart);
    }

    // Deletes cart record by ID.
    public boolean deleteCart(int id) {
        return cartRepo.deleteCart(id);
    }

    // Adds item directly to specified cart ID.
    public boolean addCartItem(int cartId, int productId, int quantity, int unitPrice) {
        return cartRepo.addCartItem(cartId, productId, quantity, unitPrice);
    }

    // Retrieves cart items stored in specified cart ID.
    public List<CartItem> getCartItemsByCartId(int cartId) {
        return cartRepo.getCartItemsByCartId(cartId);
    }

    // Retrieves all items stored in customer's active shopping cart.
    public List<CartItem> getCartItemsByCustomerId(int customerId) {
        Cart cart = cartRepo.getOrCreateCartByCustomerId(customerId);
        if (cart == null) {
            return Collections.emptyList();
        }
        return cartRepo.getCartItemsByCartId(cart.getId());
    }

    // Adds product item to customer's active cart.
    public boolean addItemToCustomerCart(int customerId, int productId, int quantity, int unitPrice) {
        Cart cart = cartRepo.getOrCreateCartByCustomerId(customerId);
        if (cart == null) {
            return false;
        }
        return cartRepo.addCartItem(cart.getId(), productId, quantity, unitPrice);
    }

    // Updates quantity of specific item in customer's cart.
    public boolean updateCartItemQuantity(int cartItemId, int newQuantity) {
        return cartRepo.updateCartItemQuantity(cartItemId, newQuantity);
    }

    // Removes an item from customer's cart by item ID.
    public boolean removeCartItem(int cartItemId) {
        return cartRepo.removeCartItem(cartItemId);
    }

    // Clears all items from customer's cart after successful checkout.
    public boolean clearCartByCustomerId(int customerId) {
        Cart cart = cartRepo.getOrCreateCartByCustomerId(customerId);
        if (cart == null) {
            return false;
        }
        List<CartItem> items = cartRepo.getCartItemsByCartId(cart.getId());
        for (CartItem item : items) {
            cartRepo.removeCartItem(item.getId());
        }
        return true;
    }
}