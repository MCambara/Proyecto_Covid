package org.prograIII.db.dao;

import org.prograIII.db.dabaBaseConnection.DatabaseConnection;
import org.prograIII.db.model.ProvinceModel;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class ProvinceDao {

    public boolean save(ProvinceModel province) {
        String sql = "INSERT INTO provinces (iso, province, name, lat, lng) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, province.getIso());
            stmt.setString(2, province.getProvince());
            stmt.setString(3, province.getName());
            stmt.setDouble(4, province.getLat());
            stmt.setDouble(5, province.getLng());

            return stmt.executeUpdate() > 0;
        } catch (Exception e) {
            System.err.println("[ERROR] Error al insertar provincia: " + province + " -> " + e.getMessage());
            return false;
        }
    }
}
