package org.prograIII.thread;

import org.prograIII.db.dabaBaseConnection.DatabaseConnection;
import org.prograIII.db.dao.RegionDao;
import org.prograIII.db.model.RegionModel;
import org.prograIII.db.service.RegionService;
import org.prograIII.util.RegionLoader;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import java.sql.Connection;
import java.util.Map;

@Component
public class CovidThread implements Runnable {

    @Value("${app.initial-delay}")
    private int initialDelay;

    @Value("${app.target-date}")
    private String targetDate;

    private RegionService regionService;

    @PostConstruct
    public void startThread() {
        // Crear conexión a la base de datos
        try (Connection connection = DatabaseConnection.getConnection()) {
            // Crear DAO y servicio para manejar las regiones
            RegionDao regionDao = new RegionDao(connection);
            regionService = new RegionService(regionDao);
        } catch (Exception e) {
            System.err.println("Error al conectar con la base de datos: " + e.getMessage());
        }

        Thread thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run() {
        try {
            System.out.println("[INFO] Esperando " + initialDelay + " milisegundos antes de iniciar...");
            Thread.sleep(initialDelay);

            System.out.println("[INFO] Iniciando ejecución con fecha objetivo: " + targetDate);

            // Obtener las regiones desde la API
            Map<Integer, Map<String, String>> regiones = RegionLoader.loadRegions();

            if (regiones.isEmpty()) {
                System.out.println("[INFO] No se encontraron regiones.");
            } else {
                System.out.println("[INFO] Guardando regiones en la base de datos...");
                // Guardar cada región en la base de datos
                for (Map<String, String> regionData : regiones.values()) {
                    String iso = regionData.get("iso");
                    String name = regionData.get("name");

                    // Crear objeto RegionModel y guardarlo
                    RegionModel region = new RegionModel(0, iso, name);
                    regionService.saveRegion(region);  // Guardar en la base de datos
                }
            }

            System.out.println("[INFO] Ejecución del hilo completada.");

        } catch (InterruptedException e) {
            System.err.println("[ERROR] Hilo interrumpido: " + e.getMessage());
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            System.err.println("[ERROR] Error durante la ejecución del hilo: " + e.getMessage());
        }
    }
}
