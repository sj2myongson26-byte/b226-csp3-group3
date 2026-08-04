package com.joysistvi.ecommerce.services;

import com.joysistvi.ecommerce.model.Product;
import com.joysistvi.ecommerce.repository.ProductRepo;
import com.joysistvi.ecommerce.repository.ProductRepoImpl;
import java.util.List;

/**
 * ProductService manages business logic and validation for product catalog operations.
 */
public class ProductService {

    private final ProductRepo productRepo = new ProductRepoImpl();

    // Retrieves all active products from catalog repository.
    public List<Product> getAllProducts() {
        return productRepo.getAllProducts();
    }

    // Fetches single product details by product ID.
    public Product getProductById(int id) {
        return productRepo.getProductById(id);
    }

    // Searches products by keyword matching in product name.
    public List<Product> searchProducts(String keyword) {
        return productRepo.searchProducts(keyword);
    }

    // Retrieves products filtered by category name.
    public List<Product> getProductsByCategory(String category) {
        return productRepo.getProductsByCategory(category);
    }

    // Retrieves distinct product categories available in catalog.
    public List<String> getAllCategories() {
        return productRepo.getAllCategories();
    }

    // Adds a new product to catalog repository.
    public boolean addProduct(Product product) {
        return productRepo.addProduct(product);
    }

    // Updates existing product details in repository.
    public boolean updateProduct(Product product) {
        return productRepo.updateProduct(product);
    }

    // Deletes product from catalog by product ID.
    public boolean deleteProduct(int id) {
        return productRepo.deleteProduct(id);
    }
}
