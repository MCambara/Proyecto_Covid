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
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@SpringBootApplication
public class Main {

    private static final RegionDao regionDao = new RegionDao();
    private static final ProvinceService provinceService = new ProvinceService(); // Usamos el service para provincias
    private static final ReportDao reportDao = new ReportDao(); // Usamos el ReportDao
    private static final Logger logger = LogManager.getLogger(Main.class);

    public static void main(String[] args) throws Exception {
        logger.info("[INFO] Iniciando prueba de RegionLoader...");
        Map<Integer, Map<String, String>> regiones = RegionLoader.loadRegions();

        if (regiones.isEmpty()) {
            logger.info("[INFO] No se encontraron regiones.");
        } else {
            regiones.forEach((index, data) ->
                    logger.info("{} => ISO: {}, Name: {}", index, data.get("iso"), data.get("name"))
            );
            insertRegionsToDatabase(regiones);
        }

        CovidProvinces service = new CovidProvinces();
        Map<String, List<ProvinceLoader>> allData = service.fetchAllRegionData();
        allData.forEach((iso, regionList) -> {
            logger.info("ISO: {}", iso);
            for (ProvinceLoader info : regionList) {
                logger.info("  - {}", info);

                ProvinceModel province = new ProvinceModel(
                        info.getIso(),
                        info.getProvince(),
                        info.getName(),
                        info.getLat(),
                        info.getLng()
                );

                boolean success = provinceService.saveProvince(province); // Usamos el service
                if (success) {
                    logger.info("[INFO] Provincia insertada: {}", info.getProvince());
                } else {
                    logger.error("[ERROR] No se pudo insertar la provincia: {}", info.getProvince());
                }
            }
        });

        logger.info("[INFO] Iniciando obtención de datos de COVID...");
        Set<String> isoSet = getIsoSetFromRegionLoader(regiones);
        String fechaConsulta = "2022-03-09";

        CovidReports covidReportsService = new CovidReports();
        Map<String, List<ReportLoader>> covidReports = covidReportsService.fetchCovidDataForAllProvinces(isoSet, fechaConsulta);

        covidReports.forEach((iso, reportList) -> {
            logger.info("ISO: {}", iso);
            for (ReportLoader report : reportList) {
                logger.info("  - {}", report);

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
                    logger.info("[INFO] Reporte insertado en la base de datos: {}", report);
                } else {
                    logger.error("[ERROR] No se pudo insertar el reporte: {}", report);
                }
            }
        });

        logger.info("[INFO] Consulta terminada para la fecha: {}", fechaConsulta);

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
                logger.info("[INFO] Región insertada: {}", iso);
            } else {
                logger.error("[ERROR] Error al insertar la región: {}", iso);
            }
        }
    }
}
