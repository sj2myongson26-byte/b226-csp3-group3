package com.joysistvi.ecommerce.model;

/**
 * Payment represents a payment transaction record entity.
 */
public class Payment {

    private int id;
    private String payment_method;
    private int amount;
    private String status;
    private String paid_at;
    private String reference;
    private int order_id;

    // Full constructor initializing payment transaction fields.
    public Payment(
            int id,
            String payment_method,
            int amount,
            String status,
            String paid_at,
            String reference,
            int order_id
    ) {
        this.id = id;
        this.payment_method = payment_method;
        this.amount = amount;
        this.status = status;
        this.paid_at = paid_at;
        this.reference = reference;
        this.order_id = order_id;
    }

    // Returns payment record ID.
    public int getId() {
        return id;
    }

    // Sets payment record ID.
    public void setId(int id) {
        this.id = id;
    }

    // Returns selected payment method.
    public String getPayment_method() {
        return payment_method;
    }

    // Sets selected payment method.
    public void setPayment_method(String payment_method) {
        this.payment_method = payment_method;
    }

    // Returns payment amount.
    public int getAmount() {
        return amount;
    }

    // Sets payment amount.
    public void setAmount(int amount) {
        this.amount = amount;
    }

    // Returns payment status string.
    public String getStatus() {
        return status;
    }

    // Sets payment status string.
    public void setStatus(String status) {
        this.status = status;
    }

    // Returns payment completion timestamp.
    public String getPaid_at() {
        return paid_at;
    }

    // Sets payment completion timestamp.
    public void setPaid_at(String paid_at) {
        this.paid_at = paid_at;
    }

    // Returns unique transaction reference code.
    public String getReference() {
        return reference;
    }

    // Sets transaction reference code.
    public void setReference(String reference) {
        this.reference = reference;
    }

    // Returns associated order ID.
    public int getOrder_id() {
        return order_id;
    }

    // Sets associated order ID.
    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    // Formats payment entity details into readable string.
    @Override
    public String toString() {
        return "Payment{" +
                "id=" + id +
                ", payment_method='" + payment_method + '\'' +
                ", amount=" + amount +
                ", status='" + status + '\'' +
                ", paid_at='" + paid_at + '\'' +
                ", reference='" + reference + '\'' +
                ", order_id=" + order_id +
                '}';
    }
}