package com.joysistvi.ecommerce.services;

import com.joysistvi.ecommerce.model.Payment;
import com.joysistvi.ecommerce.repository.PaymentRepo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

/**
 * PaymentService handles business logic and reference generation for customer payments.
 */
public class PaymentService {

    public static final String METHOD_COD =
            "CASH_ON_DELIVERY";

    public static final String METHOD_GCASH =
            "GCASH";

    public static final String METHOD_BANK =
            "BANK_TRANSFER";

    public static final String STATUS_PENDING =
            "PENDING";

    public static final String STATUS_PAID =
            "PAID";

    public static final String STATUS_FAILED =
            "FAILED";

    private final PaymentRepo paymentRepo;

    // Constructor injecting PaymentRepo dependency.
    public PaymentService(
            PaymentRepo paymentRepo
    ) {
        this.paymentRepo = paymentRepo;
    }

    // Validates payment parameters and processes transaction via repository.
    public Payment processPayment(
            int orderId,
            String paymentMethod,
            int amount
    ) {

        if (orderId <= 0) {
            System.out.println(
                    "Invalid order ID."
            );
            return null;
        }

        if (amount <= 0) {
            System.out.println(
                    "Payment amount must be greater than zero."
            );
            return null;
        }

        String normalizedMethod =
                normalizePaymentMethod(
                        paymentMethod
                );

        if (!isSupportedPaymentMethod(
                normalizedMethod
        )) {
            System.out.println(
                    "Unsupported payment method."
            );
            return null;
        }

        String paymentStatus =
                determineInitialStatus(
                        normalizedMethod
                );

        String paidAt =
                getCurrentDateTime();

        String transactionReference =
                generateTransactionReference(
                        normalizedMethod,
                        orderId
                );

        Payment payment = new Payment(
                0,
                normalizedMethod,
                amount,
                paymentStatus,
                paidAt,
                transactionReference,
                orderId
        );

        int generatedPaymentId =
                paymentRepo.createPayment(payment);

        if (generatedPaymentId <= 0) {
            System.out.println(
                    "Payment could not be saved."
            );
            return null;
        }

        payment.setId(generatedPaymentId);

        return payment;
    }

    // Retrieves payment details matching payment ID.
    public Payment getPaymentById(
            int paymentId
    ) {

        if (paymentId <= 0) {
            System.out.println(
                    "Invalid payment ID."
            );
            return null;
        }

        return paymentRepo.getPaymentById(
                paymentId
        );
    }

    // Retrieves list of payments recorded for a specific order ID.
    public List<Payment> getPaymentsByOrderId(
            int orderId
    ) {

        if (orderId <= 0) {
            return Collections.emptyList();
        }

        return paymentRepo
                .getPaymentsByOrderId(orderId);
    }

    // Retrieves all payments recorded in system.
    public List<Payment> getAllPayments() {
        return paymentRepo.getAllPayments();
    }

    // Updates status string of specified payment ID.
    public boolean updatePaymentStatus(
            int paymentId,
            String newStatus
    ) {

        if (paymentId <= 0) {
            System.out.println(
                    "Invalid payment ID."
            );
            return false;
        }

        String normalizedStatus =
                normalizePaymentStatus(
                        newStatus
                );

        if (!isValidPaymentStatus(
                normalizedStatus
        )) {
            System.out.println(
                    "Invalid payment status."
            );
            return false;
        }

        return paymentRepo.updatePaymentStatus(
                paymentId,
                normalizedStatus
        );
    }

    // Normalizes input payment method string to uppercase enum standard.
    private String normalizePaymentMethod(
            String paymentMethod
    ) {

        if (paymentMethod == null) {
            return "";
        }

        return paymentMethod
                .trim()
                .toUpperCase(Locale.ROOT)
                .replace(" ", "_");
    }

    // Normalizes payment status string.
    private String normalizePaymentStatus(
            String paymentStatus
    ) {

        if (paymentStatus == null) {
            return "";
        }

        return paymentStatus
                .trim()
                .toUpperCase(Locale.ROOT);
    }

    // Checks if payment method is supported.
    private boolean isSupportedPaymentMethod(
            String paymentMethod
    ) {

        return paymentMethod.equals(METHOD_COD)
                || paymentMethod.equals(METHOD_GCASH)
                || paymentMethod.equals(METHOD_BANK);
    }

    // Checks if payment status value is valid.
    private boolean isValidPaymentStatus(
            String paymentStatus
    ) {

        return paymentStatus.equals(STATUS_PENDING)
                || paymentStatus.equals(STATUS_PAID)
                || paymentStatus.equals(STATUS_FAILED);
    }

    // Determines initial payment status based on payment method.
    private String determineInitialStatus(
            String paymentMethod
    ) {

        if (paymentMethod.equals(METHOD_COD)) {
            return STATUS_PENDING;
        }

        return STATUS_PAID;
    }

    // Generates unique transaction reference code for payment receipt.
    private String generateTransactionReference(
            String paymentMethod,
            int orderId
    ) {

        String prefix;

        switch (paymentMethod) {

            case METHOD_GCASH:
                prefix = "GCASH";
                break;

            case METHOD_BANK:
                prefix = "BANK";
                break;

            default:
                prefix = "COD";
                break;
        }

        String randomCode =
                UUID.randomUUID()
                        .toString()
                        .substring(0, 8)
                        .toUpperCase(Locale.ROOT);

        return prefix
                + "-ORD-"
                + orderId
                + "-"
                + randomCode;
    }

    // Returns current date and time formatted string.
    private String getCurrentDateTime() {

        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern(
                        "yyyy-MM-dd HH:mm:ss"
                );

        return LocalDateTime
                .now()
                .format(formatter);
    }
}