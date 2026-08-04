package com.joysistvi.ecommerce.controller;

import com.joysistvi.ecommerce.model.Report;
import com.joysistvi.ecommerce.services.ReportService;

import java.util.List;

/**
 * ReportController manages sales and analytics report operations between UI views and ReportService.
 */
public class ReportController {

    public ReportService service = new ReportService();

    // Retrieves all generated sales report records.
    public List<Report> getallReport() {
        return service.getallReport();
    }

    // Fetches single report record by report ID.
    public Report checkReportId(int id) {
        return service.checkReportId(id);
    }

    // Creates a new sales report record.
    public boolean createReport(Report report) {
        return service.createReport(report);
    }

    // Updates an existing report record in database.
    public boolean updateReport(Report report) {
        return service.updateReport(report);
    }

    // Deletes a report record from database by ID.
    public boolean deleteReport(int id) {
        return service.deleteReport(id);
    }
}
