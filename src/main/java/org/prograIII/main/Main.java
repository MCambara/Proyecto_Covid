package org.prograIII.main;

import org.prograIII.covidApis.CovidReports;
import org.prograIII.util.RegionLoader;
import org.prograIII.covidApis.CovidProvinces;
import org.prograIII.util.ProvinceLoader;
import org.prograIII.util.ReportLoader;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

        // Instanciamos el servicio para obtener los reportes de COVID
        System.out.println("[INFO] Iniciando obtención de datos de COVID...");
        // 1. Carga de regiones
        Set<String> isoSet = getIsoSetFromRegionLoader(regiones);

        // 2. Fecha que se quiere consultar (modificable)
        String fechaConsulta = "2022-03-09";

        // 3. Lógica para consultar los reportes
        CovidReports covidReportsService = new CovidReports();
        Map<String, List<ReportLoader>> covidReports = covidReportsService.fetchCovidDataForAllProvinces(isoSet, fechaConsulta);

        // 4. Mostrar resultados
        covidReports.forEach((iso, reportList) -> {
            System.out.println("ISO: " + iso);
            for (ReportLoader report : reportList) {
                System.out.println("  - " + report);
            }
        });

        System.out.println("[INFO] Consulta terminada para la fecha: " + fechaConsulta);
    }

    private static Set<String> getIsoSetFromRegionLoader(Map<Integer, Map<String, String>> regiones) {
        Set<String> isoSet = new HashSet<>();
        for (Map<String, String> values : regiones.values()) {
            String iso = values.get("iso");
            if (iso != null && !iso.isBlank()) {
                isoSet.add(iso);
            }
        }
        return isoSet;
    }
}
