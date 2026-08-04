package com.joysistvi.ecommerce.model;

/**
 * CartItem represents an individual line item entity within a customer's cart.
 */
public class CartItem {

    private int id;
    private int quantity;
    private int unit_price;
    private String added_at;
    private int cart_id;
    private int product_id;

    // Full constructor initializing cart item fields.
    public CartItem(int id, int quantity, int unit_price, String added_at, int cart_id, int product_id) {
        this.id = id;
        this.quantity = quantity;
        this.unit_price = unit_price;
        this.added_at = added_at;
        this.cart_id = cart_id;
        this.product_id = product_id;
    }

    // Constructor initializing line item without ID and added timestamp.
    public CartItem(int quantity, int unit_price, int cart_id, int product_id) {
        this.quantity = quantity;
        this.unit_price = unit_price;
        this.cart_id = cart_id;
        this.product_id = product_id;
    }

    // Returns item ID.
    public int getId() {
        return id;
    }

    // Sets item ID.
    public void setId(int id) {
        this.id = id;
    }

    // Returns item quantity.
    public int getQuantity() {
        return quantity;
    }

    // Sets item quantity.
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    // Returns unit price.
    public int getUnit_price() {
        return unit_price;
    }

    // Sets unit price.
    public void setUnit_price(int unit_price) {
        this.unit_price = unit_price;
    }

    // Returns timestamp when item was added.
    public String getAdded_at() {
        return added_at;
    }

    // Sets added timestamp.
    public void setAdded_at(String added_at) {
        this.added_at = added_at;
    }

    // Returns cart ID.
    public int getCart_id() {
        return cart_id;
    }

    // Sets cart ID.
    public void setCart_id(int cart_id) {
        this.cart_id = cart_id;
    }

    // Returns product ID.
    public int getProduct_id() {
        return product_id;
    }

    // Sets product ID.
    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }
}