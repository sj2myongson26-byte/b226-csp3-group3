package com.joysistvi.ecommerce.model;

/**
 * OrderItem represents a individual purchased item entity attached to an order.
 */
public class OrderItem {
    private int id;
    private int quantity;
    private int unit_price;
    private int subtotal;
    private int order_id;
    private int product_id;

    // Full constructor initializing order line item fields.
    public OrderItem(int id, int quantity, int unit_price, int subtotal, int order_id, int product_id) {
        this.id = id;
        this.quantity = quantity;
        this.unit_price = unit_price;
        this.subtotal = subtotal;
        this.order_id = order_id;
        this.product_id = product_id;
    }

    // Returns order item ID.
    public int getId() {
        return id;
    }

    // Sets order item ID.
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

    // Returns item unit price.
    public int getUnit_price() {
        return unit_price;
    }

    // Sets item unit price.
    public void setUnit_price(int unit_price) {
        this.unit_price = unit_price;
    }

    // Returns item calculated subtotal amount.
    public int getSubtotal() {
        return subtotal;
    }

    // Sets item subtotal amount.
    public void setSubtotal(int subtotal) {
        this.subtotal = subtotal;
    }

    // Returns parent order ID.
    public int getOrder_id() {
        return order_id;
    }

    // Sets parent order ID.
    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    // Returns purchased product ID.
    public int getProduct_id() {
        return product_id;
    }

    // Sets purchased product ID.
    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }
}
