package com.joysistvi.ecommerce.services;

import com.joysistvi.ecommerce.model.Report;
import com.joysistvi.ecommerce.repository.ReportRepo;
import com.joysistvi.ecommerce.repository.ReportRepoImpl;

import java.util.List;

/**
 * ReportService handles business logic and calculations for sales analytics reports.
 */
public class ReportService {

    public ReportRepo repo = new ReportRepoImpl();

    // Retrieves all generated sales report records.
    public List<Report> getallReport() {
        return repo.getallReport();
    }

    // Fetches report record by report ID.
    public Report checkReportId(int id) {
        return repo.checkReportId(id);
    }

    // Creates a new sales analytics report in repository.
    public boolean createReport(Report report) {
        return repo.createReport(report);
    }

    // Updates an existing report record in repository.
    public boolean updateReport(Report report) {
        return repo.updateReport(report);
    }

    // Deletes a report record from repository by ID.
    public boolean deleteReport(int id) {
        return repo.deleteReport(id);
    }
}
