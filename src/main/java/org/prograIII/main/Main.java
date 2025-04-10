package org.prograIII.main;

import org.prograIII.db.dabaBaseConnection.DatabaseConnection;
import org.prograIII.db.dao.RegionDao;
import org.prograIII.db.model.RegionModel;
import org.prograIII.db.service.RegionService;
import org.prograIII.util.ProvinceLoader;  // Asegúrate de que tienes un loader para provincias
import org.prograIII.util.RegionLoader;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            // Crear DAO y servicio para regiones
            RegionDao regionDao = new RegionDao(connection);
            RegionService regionService = new RegionService(regionDao);

            // Cargar regiones desde la API
            Map<Integer, Map<String, String>> regionsFromApi = RegionLoader.loadRegions();

            // Mostrar las regiones obtenidas de la API en consola
            System.out.println("\n[INFO] Regions loaded from API:");
            for (Map<String, String> regionData : regionsFromApi.values()) {
                String iso = regionData.get("iso");
                String name = regionData.get("name");
                System.out.println("Region: " + name + " (" + iso + ")");
            }

            // Cargar provincias desde la API de provincias
            Map<Integer, ProvinceLoader> provincesFromApi = ProvinceLoader.loadProvinces();

            // Mostrar las provincias obtenidas de la API en consola
            System.out.println("\n[INFO] Provinces loaded from API:");
            for (ProvinceLoader provinceLoader : provincesFromApi.values()) {
                // Aquí usamos ProvinceLoader, que tiene el método toString()
                System.out.println(provinceLoader); // Imprime la provincia con su información
            }

            // Guardar las regiones en la base de datos (opcional)
            for (Map<String, String> regionData : regionsFromApi.values()) {
                String iso = regionData.get("iso");
                String name = regionData.get("name");

                // Crear el modelo de la región
                RegionModel regionModel = new RegionModel(0, iso, name);

                // Guardar la región en la base de datos
                regionService.saveRegion(regionModel);
                System.out.println("Region saved: " + name + " (" + iso + ")");
            }

            // Obtener todas las regiones desde la base de datos
            System.out.println("\n[INFO] Regions from database:");
            var regionsFromDb = regionService.getAllRegions();
            regionsFromDb.forEach(region ->
                    System.out.println("Region ID: " + region.getId() + ", ISO: " + region.getIso() + ", Name: " + region.getName())
            );

        } catch (SQLException e) {
            System.err.println("Error connecting to the database: " + e.getMessage());
        }
    }
}
