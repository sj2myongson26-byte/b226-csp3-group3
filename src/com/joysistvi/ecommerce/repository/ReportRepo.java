/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.joysistvi.ecommerce.repository;

import com.joysistvi.ecommerce.model.Report;

import java.util.List;

/**
 *
 * @author ktagl
 */
/**
 * ReportRepo defines data access contracts for sales and analytics report operations.
 */
public interface ReportRepo {

    // Retrieves all generated report records.
    List<Report> getallReport();

    // Checks report existence by report ID.
    Report checkReportId(int id);

    // Creates a new analytics report.
    boolean createReport(Report report);

    // Updates an existing analytics report.
    boolean updateReport(Report report);

    // Deletes a report record by ID.
    boolean deleteReport(int id);
}
