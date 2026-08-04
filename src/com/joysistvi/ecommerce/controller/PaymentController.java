package com.joysistvi.ecommerce.controller;

import com.joysistvi.ecommerce.model.Payment;
import com.joysistvi.ecommerce.services.PaymentService;
import java.util.List;

/**
 * PaymentController manages payment operations between presentation views and PaymentService.
 */
public class PaymentController {

    private final PaymentService paymentService;

    // Default constructor initializing payment service instance.
    public PaymentController() {
        this.paymentService = new PaymentService(new com.joysistvi.ecommerce.repository.PaymentRepoImpl());
    }

    // Constructor injecting payment service dependency.
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    // Processes a new order payment and records receipt details.
    public Payment processPayment(int orderId, String paymentMethod, int amount) {
        return paymentService.processPayment(orderId, paymentMethod, amount);
    }

    // Fetches single payment record by payment ID.
    public Payment getPaymentById(int paymentId) {
        return paymentService.getPaymentById(paymentId);
    }

    // Retrieves list of payments recorded for a specific order ID.
    public List<Payment> getPaymentsByOrderId(int orderId) {
        return paymentService.getPaymentsByOrderId(orderId);
    }
}