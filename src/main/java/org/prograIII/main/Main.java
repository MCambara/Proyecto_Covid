package org.prograIII.main;

import org.prograIII.db.dabaBaseConnection.DatabaseConnection; // Import correcto
import org.prograIII.db.dao.RegionDao;
import org.prograIII.db.model.RegionModel;
import org.prograIII.db.service.RegionService;
import org.prograIII.util.ProvinceLoader;  // Asegúrate de que tienes un loader para provincias
import org.prograIII.util.RegionLoader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

public class Main {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        try (Connection connection = DatabaseConnection.getConnection()) {  // Corregido aquí también
            // Crear DAO y servicio para regiones
            RegionDao regionDao = new RegionDao(connection);
            RegionService regionService = new RegionService(regionDao);

            // Cargar regiones desde la API
            Map<Integer, Map<String, String>> regionsFromApi = RegionLoader.loadRegions();

            // Mostrar las regiones obtenidas de la API en los logs
            logger.info("[INFO] Regions loaded from API:");
            for (Map<String, String> regionData : regionsFromApi.values()) {
                String iso = regionData.get("iso");
                String name = regionData.get("name");
                logger.info("Region: {} ({})", name, iso);
            }

            // Cargar provincias desde la API de provincias
            Map<Integer, ProvinceLoader> provincesFromApi = ProvinceLoader.loadProvinces();

            // Mostrar las provincias obtenidas de la API en los logs
            logger.info("[INFO] Provinces loaded from API:");
            for (ProvinceLoader provinceLoader : provincesFromApi.values()) {
                // Aquí usamos ProvinceLoader, que tiene el método toString()
                logger.info("Province: {}", provinceLoader); // Imprime la provincia con su información
            }

            // Guardar las regiones en la base de datos (opcional)
            for (Map<String, String> regionData : regionsFromApi.values()) {
                String iso = regionData.get("iso");
                String name = regionData.get("name");

                // Crear el modelo de la región
                RegionModel regionModel = new RegionModel(0, iso, name);

                // Guardar la región en la base de datos
                regionService.saveRegion(regionModel);
                logger.info("Region saved: {} ({})", name, iso);
            }

            // Obtener todas las regiones desde la base de datos
            logger.info("[INFO] Regions from database:");
            var regionsFromDb = regionService.getAllRegions();
            regionsFromDb.forEach(region ->
                    logger.info("Region ID: {}, ISO: {}, Name: {}", region.getId(), region.getIso(), region.getName())
            );

        } catch (SQLException e) {
            logger.error("Error connecting to the database: {}", e.getMessage());
        }
    }
}
