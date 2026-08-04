/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.joysistvi.ecommerce.adminView;

import com.joysistvi.ecommerce.controller.ReportController;
import com.joysistvi.ecommerce.model.Report;

import static com.joysistvi.ecommerce.utils.ClearScreen.clearScreen;
import static com.joysistvi.ecommerce.utils.Scan.scanner;

/**
 * ReportView provides the Admin UI menu for managing sales reports (CRUD).
 */
public class ReportView {

    private final ReportController reportController;

    // Constructor initializing ReportController instance.
    public ReportView(ReportController reportController) {
        this.reportController = reportController;
    }

    // Displays main report management menu choices.
    public void dashboard() {
        while (true) {
            clearScreen();
            System.out.println("==========================================================================================");
            System.out.println("                                    SALES REPORT MANAGEMENT                               ");
            System.out.println("==========================================================================================");
            System.out.println("1. Add Report");
            System.out.println("2. View Report");
            System.out.println("3. Update Report");
            System.out.println("4. Delete Report");
            System.out.println("5. Back");
            System.out.print("Choose: ");

            if (!scanner.hasNextInt()) {
                System.out.println("NUMBER ONLY!");
                scanner.nextLine();
                continue;
            }

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {

                case 1:
                    addReport();
                    break;

                case 2:
                    viewReport();
                    break;

                case 3:
                    updateReport();
                    break;

                case 4:
                    deleteReport();
                    break;

                case 5:
                    return;

                default:
                    System.out.println("Invalid Input");
            }

            System.out.println("\nPress Enter to continue...");
            scanner.nextLine();
        }
    }

    // Prompts admin for new report details and saves it to database.
    private void addReport() {
        while (true) {
            System.out.println("=== ADD REPORT ===");
            System.out.println("1. ADD REPORT");
            System.out.println("2. BACK");
            System.out.print("Choose an option: ");

            if (!scanner.hasNextInt()) {
                System.out.println("NUMBER ONLY!");
                scanner.nextLine();
                continue;
            }

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    int adminId;
                    String type;
                    String date;
                    double sales;
                    int orders;
                    String notes;

                    while (true) {
                        System.out.print("Admin ID: ");

                        if (scanner.hasNextInt()) {
                            adminId = scanner.nextInt();
                            scanner.nextLine();
                            break;
                        }

                        System.out.println("Invalid Admin ID.");
                        scanner.nextLine();
                    }

                    while (true) {
                        System.out.print("Report Type: ");
                        type = scanner.nextLine().trim();

                        if (!type.isEmpty()) {
                            break;
                        }

                        System.out.println("Report type cannot be empty.");
                    }

                    while (true) {
                        System.out.print("Report Date (YYYY-MM-DD): ");
                        date = scanner.nextLine().trim();

                        if (!date.isEmpty()) {
                            break;
                        }

                        System.out.println("Date cannot be empty.");
                    }

                    while (true) {
                        System.out.print("Total Sales: ");

                        if (scanner.hasNextDouble()) {
                            sales = scanner.nextDouble();
                            scanner.nextLine();
                            break;
                        }

                        System.out.println("Numbers only.");
                        scanner.nextLine();
                    }

                    while (true) {
                        System.out.print("Total Orders: ");

                        if (scanner.hasNextInt()) {
                            orders = scanner.nextInt();
                            scanner.nextLine();
                            break;
                        }

                        System.out.println("Numbers only.");
                        scanner.nextLine();
                    }

                    System.out.print("Notes: ");
                    notes = scanner.nextLine();

                    Report report = new Report(adminId, type, date, sales, orders, notes);

                    if (reportController.createReport(report)) {
                        System.out.println("Report added successfully!");
                    } else {
                        System.out.println("Failed to add report.");
                    }

                    System.out.println("Press Enter to continue...");
                    scanner.nextLine();
                    break;

                case 2:
                    return;

                default:
                    System.out.println("Invalid Input!");
            }
        }
    }

    // Displays all reports or a specific report in formatted ASCII tables.
    void viewReport() {
        while (true) {
            System.out.println("=== VIEW REPORT ===");
            System.out.println("1. VIEW ALL REPORT");
            System.out.println("2. VIEW SPECIFIC REPORT");
            System.out.println("3. BACK");
            System.out.print("Choose an option: ");

            if (!scanner.hasNextInt()) {
                System.out.println("NUMBER ONLY!");
                scanner.nextLine();
                continue;
            }

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    String border = "+------+------------+------------------+------------------------+--------------+----------+---------------------------+";
                    String headerFormat = "| %-4s | %-10s | %-16s | %-22s | %-12s | %-8s | %-25s |%n";
                    String rowFormat = "| %-4d | %-10d | %-16s | %-22s | %-12.2f | %-8d | %-25s |%n";

                    if (reportController.getallReport().isEmpty()) {
                        System.out.println("No reports found.");
                        break;
                    }

                    System.out.println(border);
                    System.out.printf(headerFormat, "ID", "Admin ID", "Type", "Date", "Sales ($)", "Orders", "Notes");
                    System.out.println(border);

                    for (Report report : reportController.getallReport()) {
                        System.out.printf(rowFormat,
                                report.getId(),
                                report.getAdmin_id(),
                                report.getType(),
                                report.getDate(),
                                report.getSales(),
                                report.getOrders(),
                                report.getNotes());
                    }

                    System.out.println(border);
                    break;

                case 2:
                    System.out.print("Enter Report ID: ");

                    if (!scanner.hasNextInt()) {
                        System.out.println("NUMBER ONLY!");
                        scanner.nextLine();
                        break;
                    }

                    int id = scanner.nextInt();
                    scanner.nextLine();

                    Report report = reportController.checkReportId(id);

                    if (report == null) {
                        System.out.println("Report does not exist.");
                    } else {
                        String borderSpec = "+------+------------+------------------+------------------------+--------------+----------+---------------------------+";
                        String headerFormatSpec = "| %-4s | %-10s | %-16s | %-22s | %-12s | %-8s | %-25s |%n";
                        String rowFormatSpec = "| %-4d | %-10d | %-16s | %-22s | %-12.2f | %-8d | %-25s |%n";

                        System.out.println(borderSpec);
                        System.out.printf(headerFormatSpec, "ID", "Admin ID", "Type", "Date", "Sales ($)", "Orders", "Notes");
                        System.out.println(borderSpec);

                        System.out.printf(rowFormatSpec,
                                report.getId(),
                                report.getAdmin_id(),
                                report.getType(),
                                report.getDate(),
                                report.getSales(),
                                report.getOrders(),
                                report.getNotes());

                        System.out.println(borderSpec);
                    }

                    break;

                case 3:
                    return;

                default:
                    System.out.println("Invalid Input!.Please Try Again!");
            }

            System.out.println("Press Enter to continue...");
            scanner.nextLine();
        }
    }

    // Prompts admin for updated report details and updates database.
    private void updateReport() {
        while (true) {
            System.out.println("=== UPDATE REPORT ===");
            System.out.println("1. UPDATE REPORT");
            System.out.println("2. BACK");
            System.out.print("Choose an option: ");

            if (!scanner.hasNextInt()) {
                System.out.println("NUMBER ONLY!");
                scanner.nextLine();
                continue;
            }

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    int id;
                    while (true) {
                        System.out.print("Enter Report ID: ");

                        if (!scanner.hasNextInt()) {
                            System.out.println("Report ID must be a number.");
                            scanner.nextLine();
                            continue;
                        }
                        
                        id = scanner.nextInt();
                        scanner.nextLine();

                        Report existing = reportController.checkReportId(id);

                        if (existing != null) {
                            break;
                        }

                        System.out.println("Report does not exist.");
                    }

                    int adminId;

                    while (true) {
                        System.out.print("Admin ID: ");

                        if (!scanner.hasNextInt()) {
                            System.out.println("Admin ID must be a number.");
                            scanner.nextLine();
                            continue;
                        }

                        adminId = scanner.nextInt();
                        scanner.nextLine();

                        if (adminId <= 0) {
                            System.out.println("Admin ID cannot be empty.");
                            continue;
                        }

                        break;
                    }

                    String type;

                    while (true) {
                        System.out.print("Report Type: ");
                        type = scanner.nextLine().trim();

                        if (type.isEmpty()) {
                            System.out.println("Report type cannot be empty.");
                            continue;
                        }

                        if (type.length() > 100) {
                            System.out.println("Maximum of 100 characters only.");
                            continue;
                        }

                        break;
                    }

                    String date;

                    while (true) {
                        System.out.print("Report Date (yyyy-MM-dd HH:mm:ss): ");
                        date = scanner.nextLine().trim();

                        if (date.isEmpty()) {
                            System.out.println("Report date cannot be empty.");
                            continue;
                        }

                        break;
                    }

                    double sales;

                    while (true) {
                        System.out.print("Total Sales: ");

                        if (!scanner.hasNextDouble()) {
                            System.out.println("Sales must be a valid number.");
                            scanner.nextLine();
                            continue;
                        }

                        sales = scanner.nextDouble();
                        scanner.nextLine();

                        if (sales < 0) {
                            System.out.println("Sales cannot be empty.");
                            continue;
                        }

                        break;
                    }

                    int orders;

                    while (true) {
                        System.out.print("Total Orders: ");

                        if (!scanner.hasNextInt()) {
                            System.out.println("Orders must be a whole number.");
                            scanner.nextLine();
                            continue;
                        }

                        orders = scanner.nextInt();
                        scanner.nextLine();

                        if (orders < 0) {
                            System.out.println("Orders cannot be empty.");
                            continue;
                        }

                        break;
                    }

                    String notes;

                    while (true) {
                        System.out.print("Notes: ");
                        notes = scanner.nextLine().trim();

                        if (!notes.isEmpty()) {
                            break;
                        }

                        System.out.println("Notes cannot be empty.");
                    }

                    Report updated = new Report(id, adminId, type, date, sales, orders, notes);

                    if (reportController.updateReport(updated)) {
                        System.out.println("Report updated successfully!");
                    } else {
                        System.out.println("Failed to update report.");
                    }

                    System.out.println("Press Enter to continue...");
                    scanner.nextLine();
                    break;

                case 2:
                    return;

                default:
                    System.out.println("Invalid Input!.Please Try Again!");
            }
        }
    }

    // Prompts admin for report ID and deletes selected report record.
    private void deleteReport() {
        while (true) {
            System.out.println("=== DELETE REPORT ===");
            System.out.println("1. DELETE REPORT");
            System.out.println("2. BACK");
            System.out.print("Choose an option: ");

            if (!scanner.hasNextInt()) {
                System.out.println("NUMBER ONLY!");
                scanner.nextLine();
                continue;
            }

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    System.out.print("Enter Report ID: ");

                    if (!scanner.hasNextInt()) {
                        System.out.println("Invalid ID!");
                        scanner.nextLine();
                        break;
                    }

                    int id = scanner.nextInt();
                    scanner.nextLine();

                    Report report = reportController.checkReportId(id);

                    if (report == null) {
                        System.out.println("Report not found.");
                        break;
                    }

                    System.out.print("Are you sure you want to delete this report? (Yes/No): ");
                    String confirm = scanner.nextLine();

                    if (confirm.equalsIgnoreCase("YES")) {
                        if (reportController.deleteReport(id)) {
                            System.out.println("Report deleted successfully!");
                        } else {
                            System.out.println("Failed to delete report.");
                        }
                    } else {
                        System.out.println("Delete cancelled.");
                    }

                    System.out.println("Press Enter to continue...");
                    scanner.nextLine();
                    break;

                case 2:
                    return;

                default:
                    System.out.println("Invalid Input!.Please Try Again!");
            }
        }
    }
}
