package com.joysistvi.ecommerce.model;

/**
 * Order represents a customer order entity stored in the database.
 */
public class Order {
    private int id;
    private String order_date;
    private String status;
    private int total_amount;
    private String address;
    private String payment_method;
    private int customer_id;

    // Full constructor initializing order entity fields.
    public Order(int id, String order_date, String status, int total_amount, String address, String payment_method, int customer_id) {
        this.id = id;
        this.order_date = order_date;
        this.status = status;
        this.total_amount = total_amount;
        this.address = address;
        this.payment_method = payment_method;
        this.customer_id = customer_id;
    }

    // Returns order ID.
    public int getId() {
        return id;
    }

    // Sets order ID.
    public void setId(int id) {
        this.id = id;
    }

    // Returns order placement date.
    public String getOrder_date() {
        return order_date;
    }

    // Sets order placement date.
    public void setOrder_date(String order_date) {
        this.order_date = order_date;
    }

    // Returns current order status.
    public String getStatus() {
        return status;
    }

    // Sets current order status.
    public void setStatus(String status) {
        this.status = status;
    }

    // Returns total amount for the order.
    public int getTotal_amount() {
        return total_amount;
    }

    // Sets total amount for the order.
    public void setTotal_amount(int total_amount) {
        this.total_amount = total_amount;
    }

    // Returns shipping address.
    public String getAddress() {
        return address;
    }

    // Sets shipping address.
    public void setAddress(String address) {
        this.address = address;
    }

    // Returns selected payment method.
    public String getPayment_method() {
        return payment_method;
    }

    // Sets selected payment method.
    public void setPayment_method(String payment_method) {
        this.payment_method = payment_method;
    }

    // Returns customer ID.
    public int getCustomer_id() {
        return customer_id;
    }

    // Sets customer ID.
    public void setCustomer_id(int customer_id) {
        this.customer_id = customer_id;
    }
}
