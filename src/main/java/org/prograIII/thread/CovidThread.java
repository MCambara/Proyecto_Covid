package org.prograIII.thread;

import org.prograIII.db.dabaBaseConnection.DatabaseConnection;
import org.prograIII.db.dao.RegionDao;
import org.prograIII.db.model.RegionModel;
import org.prograIII.db.service.RegionService;
import org.prograIII.util.RegionLoader;
import org.prograIII.util.PropertyReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.util.Map;

public class CovidThread implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(CovidThread.class);

    private int initialDelay;
    private String targetDate;
    private RegionService regionService;

    // Constructor para recibir los valores necesarios
    public CovidThread() {
        // Leer el valor de app.initial-delay desde el archivo properties
        this.initialDelay = Integer.parseInt(PropertyReader.getProperty("app.initial-delay"));
        this.targetDate = PropertyReader.getProperty("app.target-date");
    }

    // Método para iniciar el hilo manualmente
    public void startThread() {
        // Crear conexión a la base de datos manualmente
        try (Connection connection = DatabaseConnection.getConnection()) {
            // Crear DAO y servicio para manejar las regiones
            RegionDao regionDao = new RegionDao(connection);
            regionService = new RegionService(regionDao);

            // Crear y ejecutar el hilo
            Thread thread = new Thread(this);
            thread.start();

        } catch (Exception e) {
            logger.error("Error al conectar con la base de datos: {}", e.getMessage());
        }
    }

    @Override
    public void run() {
        try {
            logger.info("[INFO] Esperando {} milisegundos antes de iniciar...", initialDelay);
            Thread.sleep(initialDelay);  // Esperar el retraso inicial

            logger.info("[INFO] Iniciando ejecución con fecha objetivo: {}", targetDate);

            // Obtener las regiones desde la API
            Map<Integer, Map<String, String>> regiones = RegionLoader.loadRegions();

            if (regiones.isEmpty()) {
                logger.info("[INFO] No se encontraron regiones.");
            } else {
                logger.info("[INFO] Guardando regiones en la base de datos...");
                // Guardar cada región en la base de datos
                for (Map<String, String> regionData : regiones.values()) {
                    String iso = regionData.get("iso");
                    String name = regionData.get("name");

                    // Crear objeto RegionModel y guardarlo
                    RegionModel region = new RegionModel(0, iso, name);
                    regionService.saveRegion(region);  // Guardar en la base de datos
                }
            }

            logger.info("[INFO] Ejecución del hilo completada.");

        } catch (InterruptedException e) {
            logger.error("[ERROR] Hilo interrumpido: {}", e.getMessage());
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            logger.error("[ERROR] Error durante la ejecución del hilo: {}", e.getMessage());
        }
    }

    // Método main para iniciar el hilo de manera simple
    public static void main(String[] args) {
        // Crear instancia del hilo y arrancarlo
        CovidThread covidThread = new CovidThread();
        covidThread.startThread();
    }
}
