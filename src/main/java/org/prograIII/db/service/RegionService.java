package org.prograIII.db.service;

import org.prograIII.db.dao.RegionDao;
import org.prograIII.db.model.RegionModel;

import java.sql.SQLException;
import java.util.List;

public class RegionService {
    private final RegionDao dao;

    public RegionService(RegionDao dao) {
        this.dao = dao;
    }

    // Guardar una región en la base de datos
    public void saveRegion(RegionModel region) {
        try {
            dao.insert(region);
            System.out.println("Region saved with ID: " + region.getId());
        } catch (SQLException e) {
            System.err.println("Error saving region: " + e.getMessage());
        }
    }

    // Obtener todas las regiones de la base de datos
    public List<RegionModel> getAllRegions() {
        try {
            return dao.getAll();
        } catch (SQLException e) {
            System.err.println("Error retrieving regions: " + e.getMessage());
            return List.of();
        }
    }
}
