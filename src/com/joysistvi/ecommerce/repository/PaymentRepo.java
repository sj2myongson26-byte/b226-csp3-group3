package com.joysistvi.ecommerce.repository;

import com.joysistvi.ecommerce.model.Payment;

import java.util.List;

/**
 * PaymentRepo defines data access contracts for payment processing.
 */
public interface PaymentRepo {

    // Creates a new payment record in database.
    int createPayment(Payment payment);

    // Retrieves payment details by payment ID.
    Payment getPaymentById(int paymentId);

    // Retrieves list of payments for a specific order ID.
    List<Payment> getPaymentsByOrderId(int orderId);

    // Retrieves all recorded payments.
    List<Payment> getAllPayments();

    // Updates status of specified payment record.
    boolean updatePaymentStatus(
            int paymentId,
            String paymentStatus
    );
}