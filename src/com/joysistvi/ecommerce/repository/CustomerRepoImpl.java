package com.joysistvi.ecommerce.repository;

import com.joysistvi.ecommerce.config.dbconnection;
import com.joysistvi.ecommerce.model.Customer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author myongson
 */


/**
 * CustomerRepoImpl implements CustomerRepo data access operations for customer profiles in MySQL.
 */
public class CustomerRepoImpl implements CustomerRepo {

    private final dbconnection db = new dbconnection();

    // Retrieves all customer profile records from database.
    @Override
    public List<Customer> getAllCustomers() {
        List<Customer> customers = new ArrayList<>();
        String query = "SELECT * FROM customer";

        try (Connection conn = db.connect();
             PreparedStatement prep = conn.prepareStatement(query);
             ResultSet rs = prep.executeQuery()) {

            while (rs.next()) {
                Customer customer = new Customer(
                        rs.getInt("customer_id"),
                        rs.getString("phone"),
                        rs.getString("address"),
                        rs.getInt("user_id")
                );
                customers.add(customer);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return customers;
    }

    // Fetches single customer record matching customer ID.
    public Customer getCustomerById(int id) {
        String query = "SELECT * FROM customer WHERE customer_id = ?";

        try (Connection conn = db.connect();
             PreparedStatement prep = conn.prepareStatement(query)) {

            prep.setInt(1, id);
            try (ResultSet rs = prep.executeQuery()) {
                if (rs.next()) {
                    return new Customer(
                            rs.getInt("customer_id"),
                            rs.getString("phone"),
                            rs.getString("address"),
                            rs.getInt("user_id")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    // Fetches customer record associated with user ID.
    @Override
    public Customer getCustomerByUserId(int userId) {
        String query = "SELECT * FROM customer WHERE user_id = ?";

        try (Connection conn = db.connect();
             PreparedStatement prep = conn.prepareStatement(query)) {

            prep.setInt(1, userId);
            try (ResultSet rs = prep.executeQuery()) {
                if (rs.next()) {
                    return new Customer(
                            rs.getInt("customer_id"),
                            rs.getString("phone"),
                            rs.getString("address"),
                            rs.getInt("user_id")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    // Inserts a new customer profile into database.
    @Override
    public boolean addCustomer(Customer customer) {
        String query = "INSERT INTO customer (user_id, phone, address) VALUES (?, ?, ?)";

        try (Connection conn = db.connect();
             PreparedStatement prep = conn.prepareStatement(query)) {

            prep.setInt(1, customer.getUser_id());
            prep.setString(2, customer.getPhoneNumber());
            prep.setString(3, customer.getAddress());

            return prep.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    // Updates customer phone and address details in database.
    @Override
    public boolean updateCustomer(Customer customer) {
        String query = "UPDATE customer SET phone = ?, address = ? WHERE customer_id = ?";

        try (Connection conn = db.connect();
             PreparedStatement prep = conn.prepareStatement(query)) {

            prep.setString(1, customer.getPhoneNumber());
            prep.setString(2, customer.getAddress());
            prep.setInt(3, customer.getId());

            return prep.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    // Deletes customer record from database by ID.
    @Override
    public boolean deleteCustomer(int id) {
        String query = "DELETE FROM customer WHERE customer_id = ?";

        try (Connection conn = db.connect();
             PreparedStatement prep = conn.prepareStatement(query)) {

            prep.setInt(1, id);
            return prep.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }
}