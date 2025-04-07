package org.example.db.service;
import org.example.db.dao.CovidDao;
import org.example.db.model.CovidModel;
import java.sql.SQLException;
import java.util.List;

public class CovidService {
    private final CovidDao dao;

    public CovidService(CovidDao dao) {
        this.dao = dao;
    }

    public void saveReport(CovidModel report) {
        try {
            dao.insert(report);
            System.out.println("Report saved with ID: " + report.getReportId());
        } catch (SQLException e) {
            System.err.println("Error saving report: " + e.getMessage());
        }
    }

    public List<CovidModel> getAllReports() {
        try {
            return dao.getAll();
        } catch (SQLException e) {
            System.err.println("Error retrieving reports: " + e.getMessage());
            return List.of();
        }
    }
}