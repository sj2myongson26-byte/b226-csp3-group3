package com.joysistvi.ecommerce.repository;

import com.joysistvi.ecommerce.config.dbconnection;
import com.joysistvi.ecommerce.model.Payment;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * PaymentRepoImpl implements PaymentRepo data access operations for payment transactions in MySQL database.
 */
public class PaymentRepoImpl implements PaymentRepo {

    private final dbconnection database;

    // Initializes repository instance with dbconnection config.
    public PaymentRepoImpl() {
        database = new dbconnection();
    }

    // Inserts a new payment transaction record into database and returns generated ID.
    @Override
    public int createPayment(Payment payment) {

        String sql = """
                INSERT INTO payments (
                    order_id,
                    payment_method,
                    amount,
                    payment_status,
                    paid_at,
                    transaction_ref
                )
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        Connection connection = database.connect();

        if (connection == null) {
            System.out.println("Database connection failed.");
            return 0;
        }

        try (
                connection;
                PreparedStatement statement =
                        connection.prepareStatement(
                                sql,
                                Statement.RETURN_GENERATED_KEYS
                        )
        ) {

            statement.setInt(
                    1,
                    payment.getOrder_id()
            );

            statement.setString(
                    2,
                    payment.getPayment_method()
            );

            statement.setInt(
                    3,
                    payment.getAmount()
            );

            statement.setString(
                    4,
                    payment.getStatus()
            );

            statement.setString(
                    5,
                    payment.getPaid_at()
            );

            statement.setString(
                    6,
                    payment.getReference()
            );

            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                System.out.println(
                        "No payment record was inserted."
                );
                return 0;
            }

            try (
                    ResultSet generatedKeys =
                            statement.getGeneratedKeys()
            ) {

                if (generatedKeys.next()) {
                    int generatedId =
                            generatedKeys.getInt(1);

                    payment.setId(generatedId);

                    return generatedId;
                }
            }

        } catch (SQLException e) {
            System.out.println(
                    "Error creating payment: "
                            + e.getMessage()
            );
        }

        return 0;
    }

    // Fetches payment details matching payment ID.
    @Override
    public Payment getPaymentById(int paymentId) {

        String sql = """
                SELECT
                    payment_id,
                    payment_method,
                    amount,
                    payment_status,
                    paid_at,
                    transaction_ref,
                    order_id
                FROM payments
                WHERE payment_id = ?
                """;

        Connection connection = database.connect();

        if (connection == null) {
            System.out.println("Database connection failed.");
            return null;
        }

        try (
                connection;
                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setInt(1, paymentId);

            try (
                    ResultSet resultSet =
                            statement.executeQuery()
            ) {

                if (resultSet.next()) {
                    return mapPayment(resultSet);
                }
            }

        } catch (SQLException e) {
            System.out.println(
                    "Error retrieving payment: "
                            + e.getMessage()
            );
        }

        return null;
    }

    // Retrieves all payments recorded for a specific order ID.
    @Override
    public List<Payment> getPaymentsByOrderId(
            int orderId
    ) {

        List<Payment> payments =
                new ArrayList<>();

        String sql = """
                SELECT
                    payment_id,
                    payment_method,
                    amount,
                    payment_status,
                    paid_at,
                    transaction_ref,
                    order_id
                FROM payments
                WHERE order_id = ?
                ORDER BY payment_id DESC
                """;

        Connection connection = database.connect();

        if (connection == null) {
            System.out.println("Database connection failed.");
            return payments;
        }

        try (
                connection;
                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setInt(1, orderId);

            try (
                    ResultSet resultSet =
                            statement.executeQuery()
            ) {

                while (resultSet.next()) {
                    payments.add(
                            mapPayment(resultSet)
                    );
                }
            }

        } catch (SQLException e) {
            System.out.println(
                    "Error retrieving order payments: "
                            + e.getMessage()
            );
        }

        return payments;
    }

    // Retrieves all payment records stored in database.
    @Override
    public List<Payment> getAllPayments() {

        List<Payment> payments =
                new ArrayList<>();

        String sql = """
                SELECT
                    payment_id,
                    payment_method,
                    amount,
                    payment_status,
                    paid_at,
                    transaction_ref,
                    order_id
                FROM payments
                ORDER BY payment_id DESC
                """;

        Connection connection = database.connect();

        if (connection == null) {
            System.out.println("Database connection failed.");
            return payments;
        }

        try (
                connection;
                PreparedStatement statement =
                        connection.prepareStatement(sql);
                ResultSet resultSet =
                        statement.executeQuery()
        ) {

            while (resultSet.next()) {
                payments.add(
                        mapPayment(resultSet)
                );
            }

        } catch (SQLException e) {
            System.out.println(
                    "Error retrieving payments: "
                            + e.getMessage()
            );
        }

        return payments;
    }

    // Updates payment status string in database for specified payment ID.
    @Override
    public boolean updatePaymentStatus(
            int paymentId,
            String paymentStatus
    ) {

        String sql = """
                UPDATE payments
                SET payment_status = ?,
                    paid_at = CURRENT_TIMESTAMP
                WHERE payment_id = ?
                """;

        Connection connection = database.connect();

        if (connection == null) {
            System.out.println("Database connection failed.");
            return false;
        }

        try (
                connection;
                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setString(
                    1,
                    paymentStatus
            );

            statement.setInt(
                    2,
                    paymentId
            );

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println(
                    "Error updating payment status: "
                            + e.getMessage()
            );
        }

        return false;
    }

    // Maps database ResultSet columns into Payment model object.
    private Payment mapPayment(
            ResultSet resultSet
    ) throws SQLException {

        return new Payment(
                resultSet.getInt("payment_id"),
                resultSet.getString("payment_method"),
                resultSet.getInt("amount"),
                resultSet.getString("payment_status"),
                resultSet.getString("paid_at"),
                resultSet.getString("transaction_ref"),
                resultSet.getInt("order_id")
        );
    }
}