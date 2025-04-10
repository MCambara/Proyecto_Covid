package org.prograIII.db.dao;

import org.prograIII.db.model.RegionModel;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RegionDao {
    private final Connection connection;

    public RegionDao(Connection connection) {
        this.connection = connection;
    }

    // Insertar una nueva región en la base de datos
    public void insert(RegionModel region) throws SQLException {
        String sql = "INSERT INTO regions (iso, name) VALUES (?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, region.getIso());
            stmt.setString(2, region.getName());

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    region.setId(rs.getInt(1));  // Asignar el ID generado a la región
                }
            }
        }
    }

    // Obtener todas las regiones
    public List<RegionModel> getAll() throws SQLException {
        List<RegionModel> regions = new ArrayList<>();
        String sql = "SELECT * FROM regions";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                regions.add(new RegionModel(
                        rs.getInt("id"),
                        rs.getString("iso"),
                        rs.getString("name")
                ));
            }
        }
        return regions;
    }
}
