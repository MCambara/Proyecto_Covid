package org.prograIII.db.dao;

import org.prograIII.db.dabaBaseConnection.DatabaseConnection;
import org.prograIII.db.model.ReportModel;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class ReportDao {

    public boolean save(ReportModel report) {
        String sql = "INSERT INTO covid_reports (date, confirmed, deaths, recovered, iso, region_name, province) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, report.getDate());
            stmt.setInt(2, report.getConfirmed());
            stmt.setInt(3, report.getDeaths());
            stmt.setInt(4, report.getRecovered());
            stmt.setString(5, report.getIso());
            stmt.setString(6, report.getRegionName());
            stmt.setString(7, report.getProvince());

            return stmt.executeUpdate() > 0;
        } catch (Exception e) {
            System.err.println("[ERROR] Error al insertar reporte: " + report + " -> " + e.getMessage());
            return false;
        }
    }
}
