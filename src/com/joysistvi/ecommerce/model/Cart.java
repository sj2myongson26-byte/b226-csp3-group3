package com.joysistvi.ecommerce.model;

/**
 * Cart represents a customer shopping cart record entity.
 */
public class Cart {

    private int id;
    private String created_at;
    private String updated_at;
    private int customer_id;

    // Full constructor initializing cart entity fields.
    public Cart(int id, String created_at, String updated_at, int customer_id) {
        this.id = id;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.customer_id = customer_id;
    }

    // Constructor initializing minimal cart fields.
    public Cart(int id, String created_at){
        this.id = id;
        this.created_at = created_at;
    }

    // Returns cart ID.
    public int getId() {
        return id;
    }

    // Sets cart ID.
    public void setId(int id) {
        this.id = id;
    }

    // Returns creation timestamp.
    public String getCreated_at() {
        return created_at;
    }

    // Sets creation timestamp.
    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    // Returns update timestamp.
    public String getUpdated_at() {
        return updated_at;
    }

    // Sets update timestamp.
    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
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