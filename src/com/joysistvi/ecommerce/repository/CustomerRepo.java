/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.joysistvi.ecommerce.repository;

import com.joysistvi.ecommerce.model.Customer;

import java.util.List;

/**
 *
 * @author myongson
 */
/**
 * CustomerRepo defines data access contracts for customer profile operations.
 */
public interface CustomerRepo {

    // Adds a new customer profile.
    boolean addCustomer(Customer customer);

    // Retrieves customer details matching user ID.
    Customer getCustomerByUserId(int userId);

    // Retrieves all customer records in database.
    List<Customer> getAllCustomers();

    // Updates customer profile information.
    boolean updateCustomer(Customer customer);

    // Deletes customer record by ID.
    boolean deleteCustomer(int id);
}
