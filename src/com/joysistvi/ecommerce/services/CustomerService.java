package com.joysistvi.ecommerce.services;

import com.joysistvi.ecommerce.model.Customer;
import com.joysistvi.ecommerce.repository.CustomerRepo;
import com.joysistvi.ecommerce.repository.CustomerRepoImpl;

import java.util.List;

/**
 *
 * @author myongson
 */

/**
 * CustomerService manages business logic and validation for customer profiles.
 */
public class CustomerService {

    private final CustomerRepo customerRepo;

    // Initializes service with default CustomerRepo implementation.
    public CustomerService() {
        this.customerRepo = new CustomerRepoImpl();
    }

    // Adds a new customer profile record.
    public boolean addCustomer(Customer customer) {
        return customerRepo.addCustomer(customer);
    }

    // Fetches customer details associated with user ID.
    public Customer getCustomerByUserId(int userId) {
        return customerRepo.getCustomerByUserId(userId);
    }

    // Retrieves all customer records in system.
    public List<Customer> getAllCustomers() {
        return customerRepo.getAllCustomers();
    }

    // Updates customer profile information in database.
    public boolean updateCustomer(Customer customer) {
        return customerRepo.updateCustomer(customer);
    }

    // Deletes customer record from database by ID.
    public boolean deleteCustomer(int id) {
        return customerRepo.deleteCustomer(id);
    }
}