package com.joysistvi.ecommerce.controller;

import com.joysistvi.ecommerce.model.Product;
import com.joysistvi.ecommerce.services.ProductService;
import java.util.List;

/**
 * ProductController handles product catalog requests between UI views and ProductService.
 */
public class ProductController {

    private final ProductService productService = new ProductService();

    // Retrieves all active products from database.
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    // Fetches single product details by product ID.
    public Product getProductById(int id) {
        return productService.getProductById(id);
    }

    // Searches products by keyword matching in product name.
    public List<Product> searchProducts(String keyword) {
        return productService.searchProducts(keyword);
    }

    // Retrieves products filtered by category name.
    public List<Product> getProductsByCategory(String category) {
        return productService.getProductsByCategory(category);
    }

    // Retrieves distinct product categories available in catalog.
    public List<String> getAllCategories() {
        return productService.getAllCategories();
    }

    // Adds a new product to the catalog.
    public boolean handleAddProduct(Product product) {
        return productService.addProduct(product);
    }

    // Updates an existing product's details in database.
    public boolean handleUpdateProduct(Product product) {
        return productService.updateProduct(product);
    }

    // Deletes a product from the database by ID.
    public boolean handleDeleteProduct(int id) {
        return productService.deleteProduct(id);
    }
}
