package com.joysistvi.ecommerce.clientView;

import com.joysistvi.ecommerce.controller.PaymentController;
import com.joysistvi.ecommerce.model.Payment;
import com.joysistvi.ecommerce.model.User;
import com.joysistvi.ecommerce.services.PaymentService;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Scanner;

/**
 * UserPaymentView handles checkout payment processing, receipt generation, and transaction history views.
 */
public class UserPaymentView {

    private final Scanner scanner;
    private final PaymentController paymentController;
    private final DecimalFormat moneyFormat;

    // Default constructor initializing scanner and payment controller instances.
    public UserPaymentView() {
        this(new Scanner(System.in), new PaymentController());
    }

    // Constructor injecting scanner and payment controller dependencies.
    public UserPaymentView(
            Scanner scanner,
            PaymentController paymentController
    ) {
        this.scanner = scanner;
        this.paymentController = paymentController;
        this.moneyFormat =
                new DecimalFormat("#,##0");
    }

    // Overloaded constructor with custom DecimalFormat formatter.
    public UserPaymentView(Scanner scanner, PaymentController paymentController, DecimalFormat moneyFormat) {
        this.scanner = scanner;
        this.paymentController = paymentController;
        this.moneyFormat = moneyFormat;
    }

    // Renders interactive payment selection menu for an order.
    public Payment displayPaymentMenu(
            int orderId,
            int totalAmount
    ) {

        while (true) {

            printHeader();

            System.out.println(
                    "Order ID     : " + orderId
            );

            System.out.println(
                    "Total Amount : PHP "
                            + moneyFormat.format(
                            totalAmount
                    )
            );

            System.out.println();
            System.out.println(
                    "Choose Payment Method:"
            );
            System.out.println(
                    "1. Cash on Delivery"
            );
            System.out.println(
                    "2. GCash"
            );
            System.out.println(
                    "3. Bank Transfer"
            );
            System.out.println(
                    "4. Cancel"
            );

            int choice = readInteger(
                    "Enter your choice: "
            );

            switch (choice) {

                case 1:
                    return confirmPayment(
                            orderId,
                            totalAmount,
                            PaymentService.METHOD_COD
                    );

                case 2:
                    return processGCashPayment(
                            orderId,
                            totalAmount
                    );

                case 3:
                    return processBankTransfer(
                            orderId,
                            totalAmount
                    );

                case 4:
                    System.out.println(
                            "Payment cancelled."
                    );
                    return null;

                default:
                    System.out.println(
                            "Please choose only from 1 to 4."
                    );
            }
        }
    }

    // Displays payment receipt lookup by payment ID.
    public void displayPaymentById() {

        int paymentId = readInteger(
                "Enter payment ID: "
        );

        Payment payment =
                paymentController
                        .getPaymentById(paymentId);

        if (payment == null) {
            System.out.println(
                    "Payment not found."
            );
            return;
        }

        printReceipt(payment);
    }

    // Displays complete payment transaction history for an order ID.
    public void displayPaymentHistory(
            int orderId
    ) {

        List<Payment> payments =
                paymentController
                        .getPaymentsByOrderId(
                                orderId
                        );

        System.out.println();
        System.out.println(
                "======================================"
        );
        System.out.println(
                "          PAYMENT HISTORY"
        );
        System.out.println(
                "======================================"
        );

        if (payments.isEmpty()) {
            System.out.println(
                    "No payments found for Order #"
                            + orderId
            );
            return;
        }

        for (Payment payment : payments) {
            printReceipt(payment);
        }
    }

    // Prompts customer for GCash mobile number and confirms payment.
    private Payment processGCashPayment(
            int orderId,
            int totalAmount
    ) {

        System.out.println();
        System.out.println(
                "---------- GCASH PAYMENT ----------"
        );

        String gcashNumber =
                readRequiredText(
                        "Enter GCash number: "
                );

        if (!isValidMobileNumber(
                gcashNumber
        )) {
            System.out.println(
                    "Invalid GCash number."
            );
            System.out.println(
                    "Use an 11-digit number starting with 09."
            );
            return null;
        }

        System.out.println(
                "GCash Number: " + gcashNumber
        );

        return confirmPayment(
                orderId,
                totalAmount,
                PaymentService.METHOD_GCASH
        );
    }

    // Prompts customer for Bank Transfer details and confirms payment.
    private Payment processBankTransfer(
            int orderId,
            int totalAmount
    ) {

        System.out.println();
        System.out.println(
                "------- BANK TRANSFER -------"
        );

        String bankName =
                readRequiredText(
                        "Enter bank name: "
                );

        String accountName =
                readRequiredText(
                        "Enter account name: "
                );

        System.out.println(
                "Bank Name    : " + bankName
        );

        System.out.println(
                "Account Name : " + accountName
        );

        return confirmPayment(
                orderId,
                totalAmount,
                PaymentService.METHOD_BANK
        );
    }

    // Confirms payment prompt, records payment in DB, and updates order status to DELIVERED.
    private Payment confirmPayment(
            int orderId,
            int totalAmount,
            String paymentMethod
    ) {

        System.out.println();
        System.out.println(
                "Payment Method : "
                        + formatPaymentMethod(
                        paymentMethod
                )
        );

        System.out.println(
                "Amount         : PHP "
                        + moneyFormat.format(
                        totalAmount
                )
        );

        String confirmation =
                readRequiredText(
                        "Confirm payment? (Y/N): "
                );

        if (!confirmation.equalsIgnoreCase(
                "Y"
        )) {
            System.out.println(
                    "Payment was not confirmed."
            );
            return null;
        }

        Payment payment =
                paymentController.processPayment(
                        orderId,
                        paymentMethod,
                        totalAmount
                );

        if (payment == null) {
            System.out.println(
                    "Payment processing failed."
            );
            return null;
        }

        // Update Order status to DELIVERED and update payment_method in database
        com.joysistvi.ecommerce.controller.OrderController orderController = new com.joysistvi.ecommerce.controller.OrderController();
        com.joysistvi.ecommerce.model.Order currentOrder = orderController.getOrderDetails(orderId);
        if (currentOrder != null) {
            currentOrder.setPayment_method(paymentMethod);
            currentOrder.setStatus("DELIVERED");
            com.joysistvi.ecommerce.repository.OrderRepoImpl orderRepo = new com.joysistvi.ecommerce.repository.OrderRepoImpl();
            orderRepo.updateOrder(currentOrder);
        } else {
            orderController.updateStatus(orderId, "DELIVERED");
        }

        System.out.println();
        System.out.println(
                "Payment recorded successfully. Order status updated to COMPLETED!"
        );

        printReceipt(payment);

        return payment;
    }

    // Prints formatted payment receipt summary.
    public void printReceipt(
            Payment payment
    ) {

        System.out.println();
        System.out.println(
                "======================================"
        );
        System.out.println(
                "             PAYMENT RECEIPT"
        );
        System.out.println(
                "======================================"
        );

        System.out.println(
                "Payment ID       : "
                        + payment.getId()
        );

        System.out.println(
                "Order ID         : "
                        + payment.getOrder_id()
        );

        System.out.println(
                "Payment Method   : "
                        + formatPaymentMethod(
                        payment.getPayment_method()
                )
        );

        System.out.println(
                "Amount           : PHP "
                        + moneyFormat.format(
                        payment.getAmount()
                )
        );

        System.out.println(
                "Payment Status   : "
                        + payment.getStatus()
        );

        System.out.println(
                "Transaction Ref. : "
                        + payment.getReference()
        );

        System.out.println(
                "Payment Date     : "
                        + payment.getPaid_at()
        );

        System.out.println(
                "======================================"
        );
    }

    // Helper method to safely prompt and read integer inputs.
    private int readInteger(
            String message
    ) {

        while (true) {

            System.out.print(message);

            String input =
                    scanner.nextLine().trim();

            try {
                return Integer.parseInt(input);

            } catch (NumberFormatException e) {
                System.out.println(
                        "Please enter numbers only."
                );
            }
        }
    }

    // Helper method to read required non-empty text input.
    private String readRequiredText(
            String message
    ) {

        while (true) {

            System.out.print(message);

            String input =
                    scanner.nextLine().trim();

            if (!input.isEmpty()) {
                return input;
            }

            System.out.println(
                    "This field cannot be empty."
            );
        }
    }

    // Validates 11-digit GCash mobile number string.
    private boolean isValidMobileNumber(
            String mobileNumber
    ) {

        return mobileNumber.matches(
                "09\\d{9}"
        );
    }

    // Formats payment method enum key into user-friendly title.
    private String formatPaymentMethod(
            String paymentMethod
    ) {

        if (paymentMethod == null) {
            return "";
        }

        switch (paymentMethod) {

            case PaymentService.METHOD_COD:
                return "Cash on Delivery";

            case PaymentService.METHOD_GCASH:
                return "GCash";

            case PaymentService.METHOD_BANK:
                return "Bank Transfer";

            default:
                return paymentMethod;
        }
    }

    // Prints payment menu ASCII banner header.
    private void printHeader() {

        System.out.println();
        System.out.println(
                "======================================"
        );
        System.out.println(
                "             PAYMENT MENU"
        );
        System.out.println(
                "======================================"
        );
    }

    // Default display entry point implementation.
    public void display(User user) {
    }
}