package com.joysistvi.ecommerce.repository;

import com.joysistvi.ecommerce.config.dbconnection;
import com.joysistvi.ecommerce.model.Report;

import java.sql.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * ReportRepoImpl implements ReportRepo data access for analytics reports in MySQL database.
 */
public class ReportRepoImpl implements ReportRepo {

    private final dbconnection db = new dbconnection();

    // Retrieves all generated sales analytics reports from database.
    @Override
    public List<Report> getallReport() {
        List<Report> reports = new ArrayList<>();
        String query = "SELECT * FROM reports";

        try (Connection conn = db.connect(); PreparedStatement prep = conn.prepareStatement(query); ResultSet rs = prep.executeQuery()) {
            while (rs.next()) {
                Report report = new Report(
                        rs.getInt("report_id"),
                        rs.getInt("admin_id"),
                        rs.getString("report_type"),
                        rs.getString("report_date"),
                        rs.getInt("total_sales"),
                        rs.getInt("total_orders"),
                        rs.getString("notes")
                );
                reports.add(report);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reports;
    }

    // Fetches single report record matching report ID.
    @Override
    public Report checkReportId(int id) {
        String query = "SELECT * FROM reports where report_id = ?";

        try (Connection conn = db.connect(); PreparedStatement prep = conn.prepareStatement(query)) {
            prep.setInt(1, id);

            try (ResultSet rs = prep.executeQuery()) {
                if (rs.next()) {
                    return new Report(
                            rs.getInt("report_id"),
                            rs.getInt("admin_id"),
                            rs.getString("report_type"),
                            rs.getString("report_date"),
                            rs.getInt("total_sales"),
                            rs.getInt("total_orders"),
                            rs.getString("notes")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Inserts a new sales report into database.
    @Override
    public boolean createReport(Report report) {
        String query = "INSERT INTO reports (admin_id,report_type,report_date,total_sales,total_orders,notes) VALUES(?,?,?,?,?,?)";

        try (Connection conn = db.connect(); PreparedStatement prep = conn.prepareStatement(query)) {
            prep.setInt(1, report.getAdmin_id());
            prep.setString(2, report.getType());
            prep.setString(3, report.getDate());
            prep.setDouble(4, report.getSales());
            prep.setInt(5, report.getOrders());
            prep.setString(6, report.getNotes());

            return prep.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Updates an existing report record in database.
    @Override
    public boolean updateReport(Report report) {
        String query = "UPDATE reports SET admin_id = ?, report_type = ?,report_date = ?,total_sales = ?,total_orders = ?,notes = ? WHERE report_id = ?";

        try (Connection conn = db.connect(); PreparedStatement prep = conn.prepareStatement(query)) {
            prep.setInt(1, report.getAdmin_id());
            prep.setString(2, report.getType());
            prep.setString(3, report.getDate());
            prep.setDouble(4, report.getSales());
            prep.setInt(5, report.getOrders());
            prep.setString(6, report.getNotes());
            prep.setInt(7, report.getId());

            return prep.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Deletes a report record from database by ID.
    @Override
    public boolean deleteReport(int id) {
        String query = "DELETE FROM reports WHERE report_id = ?";

        try (Connection conn = db.connect(); PreparedStatement prep = conn.prepareStatement(query)) {
            prep.setInt(1, id);

            return prep.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
