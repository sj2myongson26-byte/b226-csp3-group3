package com.joysistvi.ecommerce.model;


/**
 *
 * @author myongson
 */

/**
 * Customer represents a customer profile entity containing address and phone contact details.
 */

public class Customer {

    private int id;
    private String phoneNumber;
    private String address;
    private int user_id;

    // Constructor initializing customer profile entity fields.
    public Customer(int id, String phoneNumber, String address, int user_id) {
        this.id = id;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.user_id = user_id;
    }

    // Returns customer ID.
    public int getId() {
        return id;
    }

    // Sets customer ID.
    public void setId(int id) {
        this.id = id;
    }

    // Returns phone number string.
    public String getPhoneNumber() {
        return phoneNumber;
    }

    // Sets phone number string.
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    // Returns shipping address string.
    public String getAddress() {
        return address;
    }

    // Sets shipping address string.
    public void setAddress(String address) {
        this.address = address;
    }

    // Returns associated user ID.
    public int getUser_id() {
        return user_id;
    }

    // Sets associated user ID.
    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }
}
