package org.prograIII.main;

import org.prograIII.covidApis.CovidReports;
import org.prograIII.util.RegionLoader;
import org.prograIII.covidApis.CovidProvinces;
import org.prograIII.util.ProvinceLoader;
import org.prograIII.util.ReportLoader;
import org.prograIII.db.dao.RegionDao;
import org.prograIII.db.dao.ReportDao; // Importamos el ReportDao
import org.prograIII.db.model.RegionModel;
import org.prograIII.db.model.ProvinceModel;
import org.prograIII.db.model.ReportModel; // Importamos el ReportModel
import org.prograIII.db.service.ProvinceService;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Main {

    private static final RegionDao regionDao = new RegionDao();
    private static final ProvinceService provinceService = new ProvinceService(); // Usamos el service para provincias
    private static final ReportDao reportDao = new ReportDao(); // Usamos el ReportDao

    public static void main(String[] args) throws Exception {
        System.out.println("[INFO] Iniciando prueba de RegionLoader...");
        Map<Integer, Map<String, String>> regiones = RegionLoader.loadRegions();

        if (regiones.isEmpty()) {
            System.out.println("[INFO] No se encontraron regiones.");
        } else {
            regiones.forEach((index, data) ->
                    System.out.println(index + " => ISO: " + data.get("iso") + ", Name: " + data.get("name"))
            );
            insertRegionsToDatabase(regiones);
        }

        CovidProvinces service = new CovidProvinces();
        Map<String, List<ProvinceLoader>> allData = service.fetchAllRegionData();
        allData.forEach((iso, regionList) -> {
            System.out.println("ISO: " + iso);
            for (ProvinceLoader info : regionList) {
                System.out.println("  - " + info);

                ProvinceModel province = new ProvinceModel(
                        info.getIso(),
                        info.getProvince(),
                        info.getName(),
                        info.getLat(),
                        info.getLng()
                );

                boolean success = provinceService.saveProvince(province); // Usamos el service
                if (success) {
                    System.out.println("[INFO] Provincia insertada: " + info.getProvince());
                } else {
                    System.err.println("[ERROR] No se pudo insertar la provincia: " + info.getProvince());
                }
            }
        });

        System.out.println("[INFO] Iniciando obtención de datos de COVID...");
        Set<String> isoSet = getIsoSetFromRegionLoader(regiones);
        String fechaConsulta = "2022-03-09";

        CovidReports covidReportsService = new CovidReports();
        Map<String, List<ReportLoader>> covidReports = covidReportsService.fetchCovidDataForAllProvinces(isoSet, fechaConsulta);

        covidReports.forEach((iso, reportList) -> {
            System.out.println("ISO: " + iso);
            for (ReportLoader report : reportList) {
                System.out.println("  - " + report);

                // Creamos el modelo de Reporto a insertar en la base de datos
                ReportModel reportModel = new ReportModel(
                        0, // El id es autoincrementado, por lo que pasamos 0
                        report.getDate(),
                        report.getConfirmed(),
                        report.getDeaths(),
                        report.getRecovered(),
                        report.getIso(),
                        report.getRegionName(),
                        report.getProvince()
                );

                // Insertamos el reporte en la base de datos
                boolean reportSuccess = reportDao.save(reportModel);
                if (reportSuccess) {
                    System.out.println("[INFO] Reporte insertado en la base de datos: " + report);
                } else {
                    System.err.println("[ERROR] No se pudo insertar el reporte: " + report);
                }
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

    private static void insertRegionsToDatabase(Map<Integer, Map<String, String>> regiones) {
        for (Map.Entry<Integer, Map<String, String>> entry : regiones.entrySet()) {
            Map<String, String> data = entry.getValue();
            String iso = data.get("iso");
            String name = data.get("name");

            RegionModel region = new RegionModel(0, iso, name);
            boolean success = regionDao.save(region);
            if (success) {
                System.out.println("[INFO] Región insertada: " + iso);
            } else {
                System.err.println("[ERROR] Error al insertar la región: " + iso);
            }
        }
    }
}
