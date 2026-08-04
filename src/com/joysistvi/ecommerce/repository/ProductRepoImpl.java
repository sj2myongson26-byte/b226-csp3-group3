package com.joysistvi.ecommerce.repository;

import com.joysistvi.ecommerce.config.dbconnection;
import com.joysistvi.ecommerce.model.Product;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * ProductRepoImpl implements ProductRepo data access for product inventory in MySQL database.
 */
public class ProductRepoImpl implements ProductRepo {

    private final dbconnection db = new dbconnection();

    // Retrieves all active products from database catalog.
    @Override
    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();
        String query = "SELECT products.product_id, products.product_name, products.description, products.price, products.quantity, products.category, products.status FROM products WHERE products.status = 'ACTIVE'";

        try (Connection conn = db.connect();
             PreparedStatement prep = conn.prepareStatement(query);
             ResultSet res = prep.executeQuery()) {

            while (res.next()) {
                Product product = new Product(
                        res.getInt("product_id"),
                        res.getString("product_name"),
                        res.getString("description"),
                        res.getDouble("price"),
                        res.getInt("quantity"),
                        res.getString("category"),
                        res.getString("status")
                );

                products.add(product);
            }

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }

        return products;
    }

    // Fetches single product record matching product ID.
    @Override
    public Product getProductById(int id) {
        String query = "SELECT * FROM products WHERE product_id = ?";

        try (Connection conn = db.connect();
             PreparedStatement prep = conn.prepareStatement(query)) {

            prep.setInt(1, id);

            try (ResultSet res = prep.executeQuery()) {
                if (res.next()) {
                    return new Product(
                            res.getInt("product_id"),
                            res.getString("product_name"),
                            res.getString("description"),
                            res.getDouble("price"),
                            res.getInt("quantity"),
                            res.getString("category"),
                            res.getString("created_at"),
                            res.getString("updated_at"),
                            res.getString("status")
                    );
                }
            }

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }

        return null;
    }

    // Searches products by keyword matching in product name.
    @Override
    public List<Product> searchProducts(String keyword) {
        List<Product> products = new ArrayList<>();
        String query = "SELECT product_id, product_name, description, price, quantity, category, status FROM products WHERE status = 'ACTIVE' AND product_name LIKE ?";

        try (Connection conn = db.connect();
             PreparedStatement prep = conn.prepareStatement(query)) {

            prep.setString(1, "%" + keyword + "%");

            try (ResultSet res = prep.executeQuery()) {
                while (res.next()) {
                    Product product = new Product(
                            res.getInt("product_id"),
                            res.getString("product_name"),
                            res.getString("description"),
                            res.getDouble("price"),
                            res.getInt("quantity"),
                            res.getString("category"),
                            res.getString("status")
                    );
                    products.add(product);
                }
            }

        } catch (SQLException e) {
            System.out.println("Search Product Error: " + e.getMessage());
        }

        return products;
    }

    // Fetches all active products matching specified category.
    @Override
    public List<Product> getProductsByCategory(String category) {
        List<Product> products = new ArrayList<>();
        String query = "SELECT product_id, product_name, description, price, quantity, category, status FROM products WHERE status = 'ACTIVE' AND category LIKE ?";

        try (Connection conn = db.connect();
             PreparedStatement prep = conn.prepareStatement(query)) {

            prep.setString(1, "%" + category + "%");

            try (ResultSet res = prep.executeQuery()) {
                while (res.next()) {
                    Product product = new Product(
                            res.getInt("product_id"),
                            res.getString("product_name"),
                            res.getString("description"),
                            res.getDouble("price"),
                            res.getInt("quantity"),
                            res.getString("category"),
                            res.getString("status")
                    );
                    products.add(product);
                }
            }

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }

        return products;
    }

    // Retrieves distinct list of categories from active products.
    @Override
    public List<String> getAllCategories() {
        List<String> categories = new ArrayList<>();
        String query = "SELECT DISTINCT category FROM products WHERE status = 'ACTIVE' AND category IS NOT NULL AND category != ''";

        try (Connection conn = db.connect();
             PreparedStatement prep = conn.prepareStatement(query);
             ResultSet res = prep.executeQuery()) {

            while (res.next()) {
                categories.add(res.getString("category"));
            }

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }

        return categories;
    }

    // Inserts a new product into database catalog.
    @Override
    public boolean addProduct(Product product) {
        String query = "INSERT INTO products (product_name, description, price, quantity, category, status) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = db.connect();
             PreparedStatement prep = conn.prepareStatement(query)) {

            prep.setString(1, product.getName());
            prep.setString(2, product.getDescription());
            prep.setDouble(3, product.getPrice());
            prep.setInt(4, product.getQuantity());
            prep.setString(5, product.getCategory());
            prep.setString(6, product.getStatus() != null ? product.getStatus() : "ACTIVE");

            return prep.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error inserting product: " + e.getMessage());
        }
        return false;
    }

    // Updates an existing product details in database.
    @Override
    public boolean updateProduct(Product product) {
        String query = "UPDATE products SET product_name = ?, description = ?, price = ?, quantity = ?, category = ?, status = ? WHERE product_id = ?";

        try (Connection conn = db.connect();
             PreparedStatement prep = conn.prepareStatement(query)) {

            prep.setString(1, product.getName());
            prep.setString(2, product.getDescription());
            prep.setDouble(3, product.getPrice());
            prep.setInt(4, product.getQuantity());
            prep.setString(5, product.getCategory());
            prep.setString(6, product.getStatus() != null ? product.getStatus() : "ACTIVE");
            prep.setInt(7, product.getId());

            return prep.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Update Product Error: " + e.getMessage());
        }
        return false;
    }

    // Soft-deletes product by setting status to INACTIVE in database.
    @Override
    public boolean deleteProduct(int id) {
        String query = "UPDATE products SET status = 'INACTIVE' WHERE product_id = ?";

        try (Connection conn = db.connect();
             PreparedStatement prep = conn.prepareStatement(query)) {

            prep.setInt(1, id);
            return prep.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Delete Product Error: " + e.getMessage());
        }
        return false;
    }
}