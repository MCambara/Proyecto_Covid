package org.prograIII.thread;

import org.prograIII.covidApis.CovidReports;
import org.prograIII.covidApis.CovidProvinces;
import org.prograIII.db.dao.ProvinceDao;
import org.prograIII.db.dao.RegionDao;
import org.prograIII.db.dao.ReportDao; // Importamos el ReportDao
import org.prograIII.db.model.ProvinceModel;
import org.prograIII.db.model.RegionModel;
import org.prograIII.db.model.ReportModel; // Importamos el ReportModel
import org.prograIII.util.ProvinceLoader;
import org.prograIII.util.RegionLoader;
import org.prograIII.util.ReportLoader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import jakarta.annotation.PostConstruct;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
public class CovidThread implements Runnable {
    private static final Logger logger = LogManager.getLogger(CovidThread.class);

    @Value("${app.initial-delay}")
    private int initialDelay;

    @Value("${app.target-date}")
    private String targetDate;

    private final RegionDao regionDao;
    private final ProvinceDao provinceDao;
    private final ReportDao reportDao; // Agregamos el ReportDao

    public CovidThread(RegionDao regionDao, ProvinceDao provinceDao, ReportDao reportDao) {
        this.regionDao = regionDao;
        this.provinceDao = provinceDao;
        this.reportDao = reportDao; // Inyectamos el ReportDao
    }

    @PostConstruct
    public void startThread() {
        Thread thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run() {
        try {
            logger.info("[INFO] Esperando {} ms antes de iniciar...", initialDelay);
            Thread.sleep(initialDelay);

            logger.info("[INFO] Iniciando ejecución con fecha objetivo: {}", targetDate);
            Map<Integer, Map<String, String>> regiones = RegionLoader.loadRegions();

            logger.info("[INFO] Regiones obtenidas:");
            regiones.forEach((index, data) ->
                    logger.info("{} => ISO: {}, Name: {}", index, data.get("iso"), data.get("name"))
            );

            insertRegionsToDatabase(regiones);
            insertProvincesToDatabase();

            // Obtener y guardar reportes de COVID
            insertCovidReportsToDatabase(regiones);

            logger.info("[INFO] Ejecución del hilo completada.");

        } catch (InterruptedException e) {
            logger.error("[ERROR] Hilo interrumpido: {}", e.getMessage());
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            logger.error("[ERROR] Error en el hilo: {}", e.getMessage());
        }
    }

    private void insertRegionsToDatabase(Map<Integer, Map<String, String>> regiones) {
        for (Map<String, String> data : regiones.values()) {
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

    private void insertProvincesToDatabase() {
        CovidProvinces service = new CovidProvinces();
        Map<String, List<ProvinceLoader>> allData = service.fetchAllRegionData();
        allData.forEach((iso, regionList) -> {
            logger.info("ISO: " + iso);
            for (ProvinceLoader info : regionList) {
                logger.info("  - " + info);

                ProvinceModel province = new ProvinceModel(
                        info.getIso(),
                        info.getProvince(),
                        info.getName(),
                        info.getLat(),
                        info.getLng()
                );

                boolean success = provinceDao.save(province);
                if (success) {
                    logger.info("[INFO] Provincia insertada: " + info.getProvince());
                } else {
                    logger.error("[ERROR] No se pudo insertar la provincia: {}", info.getProvince());
                }
            }
        });
    }

    private void insertCovidReportsToDatabase(Map<Integer, Map<String, String>> regiones) {
        Set<String> isoSet = getIsoSetFromRegionLoader(regiones);
        String fechaConsulta = targetDate;  // Usamos la fecha objetivo

        CovidReports covidReportsService = new CovidReports();
        Map<String, List<ReportLoader>> covidReports = covidReportsService.fetchCovidDataForAllProvinces(isoSet, fechaConsulta);

        covidReports.forEach((iso, reportList) -> {
            logger.info("ISO: " + iso);
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
    }

    private Set<String> getIsoSetFromRegionLoader(Map<Integer, Map<String, String>> regiones) {
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
