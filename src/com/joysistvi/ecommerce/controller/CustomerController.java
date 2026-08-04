package com.joysistvi.ecommerce.controller;

import com.joysistvi.ecommerce.model.Customer;
import com.joysistvi.ecommerce.services.CustomerService;
import java.util.List;

/**
 *
 * @author myongson
 */

/**
 * CustomerController manages customer profile interactions between UI views and CustomerService.
 */
public class CustomerController {

    private final CustomerService customerService;

    // Initializes controller with customer service instance.
    public CustomerController() {
        this.customerService = new CustomerService();
    }

    // Fetches customer details associated with a specific user ID.
    public Customer getCustomerByUserId(int userId) {
        return customerService.getCustomerByUserId(userId);
    }

    // Retrieves all customer records in the system.
    public List<Customer> getAllCustomers() {
        return customerService.getAllCustomers();
    }

    // Updates customer profile information in database.
    public boolean updateCustomer(Customer customer) {
        return customerService.updateCustomer(customer);
    }
}
