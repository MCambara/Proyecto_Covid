package org.prograIII.db.service;

import org.prograIII.db.dao.RegionDao;
import org.prograIII.db.model.RegionModel;

import java.util.List;

public class RegionService {
    private final RegionDao regionDao;

    public RegionService() {
        this.regionDao = new RegionDao();
    }

    // Guardar una región
    public boolean saveRegion(RegionModel region) {
        return regionDao.save(region);
    }

    // Obtener todas las regiones
    public List<RegionModel> getAllRegions() {
        return regionDao.getAll();
    }

    // Obtener una región por ID
    public RegionModel getRegionById(int id) {
        return regionDao.getById(id);
    }
}
