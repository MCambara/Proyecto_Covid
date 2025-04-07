package org.prograIII.main;

import org.prograIII.util.RegionLoader;

import java.util.Map;

public class Main {
    public static void main(String[] args) {
        System.out.println("[INFO] Iniciando prueba de RegionLoader...");

        Map<Integer, Map<String, String>> regiones = RegionLoader.loadRegions();

        if (regiones.isEmpty()) {
            System.out.println("[INFO] No se encontraron regiones.");
        } else {
            regiones.forEach((index, data) ->
                    System.out.println(index + " => ISO: " + data.get("iso") + ", Name: " + data.get("name"))
            );
        }

        System.out.println("[INFO] Prueba finalizada.");
    }
}
