package org.prograIII.main;

import org.prograIII.util.RegionLoader;
import org.prograIII.covidApis.CovidProvinces;
import org.prograIII.util.ProvinceLoader;

import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws Exception {
        System.out.println("[INFO] Iniciando prueba de RegionLoader...");

        Map<Integer, Map<String, String>> regiones = RegionLoader.loadRegions();

        if (regiones.isEmpty()) {
            System.out.println("[INFO] No se encontraron regiones.");
        } else {
            regiones.forEach((index, data) ->
                    System.out.println(index + " => ISO: " + data.get("iso") + ", Name: " + data.get("name"))
            );
        }

        CovidProvinces service = new CovidProvinces();
        Map<String, List<ProvinceLoader>> allData = service.fetchAllRegionData();
        allData.forEach((iso, regionList) -> {
            System.out.println("ISO: " + iso);
            for (ProvinceLoader info : regionList) {
                System.out.println("  - " + info);
            }
        });


        System.out.println("[INFO] Prueba finalizada.");
    }
}
