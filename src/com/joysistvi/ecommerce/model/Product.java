package com.joysistvi.ecommerce.model;

/**
 * Product represents a product catalog item entity in the database.
 */
public class Product {
    private int id;
    private String name;
    private String description;
    private double price;
    private int quantity;
    private String category;
    private String created_at;
    private String updated_at;
    private String status;

    // Full constructor initializing all product entity fields including timestamps.
    public Product(int id, String name, String description, double price, int quantity, String category, String created_at, String updated_at, String status) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.quantity = quantity;
        this.category = category;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.status = status;
    }

    // Constructor initializing core product entity fields.
    public Product(int id, String name, String description, double price, int quantity, String category, String status) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.quantity = quantity;
        this.category = category;
        this.status = status;
    }

    // Returns product ID.
    public int getId() {
        return id;
    }

    // Sets product ID.
    public void setId(int id) {
        this.id = id;
    }

    // Returns product name.
    public String getName() {
        return name;
    }

    // Sets product name.
    public void setName(String name) {
        this.name = name;
    }

    // Returns product description string.
    public String getDescription() {
        return description;
    }

    // Sets product description string.
    public void setDescription(String description) {
        this.description = description;
    }

    // Returns product unit price.
    public double getPrice() {
        return price;
    }

    // Sets product unit price.
    public void setPrice(double price) {
        this.price = price;
    }

    // Returns product stock quantity.
    public int getQuantity() {
        return quantity;
    }

    // Sets product stock quantity.
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    // Returns product category string.
    public String getCategory() {
        return category;
    }

    // Sets product category string.
    public void setCategory(String category) {
        this.category = category;
    }

    // Returns product creation timestamp.
    public String getCreated_at() {
        return created_at;
    }

    // Sets product creation timestamp.
    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    // Returns product update timestamp.
    public String getUpdated_at() {
        return updated_at;
    }

    // Sets product update timestamp.
    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    // Returns product status (ACTIVE or INACTIVE).
    public String getStatus() {
        return status;
    }

    // Sets product status.
    public void setStatus(String status) {
        this.status = status;
    }
}
