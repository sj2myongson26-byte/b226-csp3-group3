/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.joysistvi.ecommerce.repository;

import com.joysistvi.ecommerce.model.Product;
import java.util.List;

/**
 *
 * @author ktagl
 */
/**
 * ProductRepo defines data access contracts for product catalog management.
 */
public interface ProductRepo {

    // Retrieves all active products from catalog.
    List<Product> getAllProducts();

    // Retrieves single product by ID.
    Product getProductById(int id);

    // Searches products by keyword matching.
    List<Product> searchProducts(String keyword);

    // Retrieves products filtered by category name.
    List<Product> getProductsByCategory(String category);

    // Retrieves distinct non-empty product categories.
    List<String> getAllCategories();

    // Adds a new product to catalog.
    boolean addProduct(Product product);

    // Updates product details in database.
    boolean updateProduct(Product product);

    // Deletes product from catalog by ID.
    boolean deleteProduct(int id);
}

