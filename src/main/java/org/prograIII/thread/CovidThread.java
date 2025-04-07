package org.prograIII.thread;

import org.prograIII.util.RegionLoader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import java.util.Map;

@Component
public class CovidThread implements Runnable {

    @Value("${app.initial-delay}")
    private int initialDelay;

    @Value("${app.target-date}")
    private String targetDate;

    @PostConstruct
    public void startThread() {
        Thread thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run() {
        try {
            System.out.println("[INFO] Esperando " + initialDelay + " milisegundos antes de iniciar...");
            Thread.sleep(initialDelay);

            System.out.println("[INFO] Iniciando ejecución con fecha objetivo: " + targetDate);

            Map<Integer, Map<String, String>> regiones = RegionLoader.loadRegions();

            System.out.println("[INFO] Regiones obtenidas de la API:");
            regiones.forEach((index, data) ->
                    System.out.println(index + " => ISO: " + data.get("iso") + ", Name: " + data.get("name"))
            );

            System.out.println("[INFO] Ejecución del hilo completada.");

        } catch (InterruptedException e) {
            System.err.println("[ERROR] Hilo interrumpido: " + e.getMessage());
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            System.err.println("[ERROR] Error durante la ejecución del hilo: " + e.getMessage());
        }
    }
}
