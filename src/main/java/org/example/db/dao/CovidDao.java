package org.example.db.dao;

import org.example.db.model.CovidModel;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CovidDao {
    private final Connection connection;

    public CovidDao(Connection connection) {
        this.connection = connection;
    }

    public void insert(CovidModel model) throws SQLException {
        String sql = "INSERT INTO covid_report (date, city, province, data) VALUES (?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setDate(1, new java.sql.Date(model.getDate().getTime()));
            stmt.setString(2, model.getCity());
            stmt.setString(3, model.getProvince());
            stmt.setString(4, model.getData());

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    model.setReportId(rs.getInt(1));
                }
            }
        }
    }

    public List<CovidModel> getAll() throws SQLException {
        List<CovidModel> reports = new ArrayList<>();
        String sql = "SELECT * FROM covid_report";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                reports.add(new CovidModel(
                        rs.getInt("reportId"),
                        rs.getDate("date"),
                        rs.getString("city"),
                        rs.getString("province"),
                        rs.getString("data")
                ));
            }
        }
        return reports;
    }
}