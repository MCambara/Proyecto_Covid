package org.prograIII.db.dao;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.prograIII.db.model.RegionModel;
import org.prograIII.db.dabaBaseConnection.DatabaseConnection;
import org.springframework.stereotype.Repository;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Repository
public class RegionDao {

    private static final Logger logger = LogManager.getLogger(RegionDao.class);

    // Guardar una región
    public boolean save(RegionModel region) {
        String query = "INSERT INTO regions (iso, name) VALUES (?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, region.getIso());
            stmt.setString(2, region.getName());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error(e);
            return false;
        }
    }

    // Obtener todas las regiones
    public List<RegionModel> getAll() {
        List<RegionModel> regions = new ArrayList<>();
        String query = "SELECT * FROM regions";
        try (Connection connection = DatabaseConnection.getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                RegionModel region = new RegionModel(
                        rs.getInt("id"),
                        rs.getString("iso"),
                        rs.getString("name")
                );
                regions.add(region);
            }
        } catch (SQLException e) {
            logger.error(e);
        }
        return regions;
    }

    // Obtener una región por ID
    public RegionModel getById(int id) {
        RegionModel region = null;
        String query = "SELECT * FROM regions WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    region = new RegionModel(
                            rs.getInt("id"),
                            rs.getString("iso"),
                            rs.getString("name")
                    );
                }
            }
        } catch (SQLException e) {
            logger.error(e);
        }
        return region;
    }
}
