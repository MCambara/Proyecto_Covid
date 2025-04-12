package org.prograIII.thread;

import org.prograIII.covidApis.CovidReports;
import org.prograIII.util.RegionLoader;
import org.prograIII.covidApis.CovidProvinces;
import org.prograIII.util.ProvinceLoader;
import org.prograIII.util.ReportLoader;
import org.prograIII.db.dao.RegionDao;
import org.prograIII.db.dao.ReportDao;
import org.prograIII.db.model.RegionModel;
import org.prograIII.db.model.ProvinceModel;
import org.prograIII.db.model.ReportModel;
import org.prograIII.db.service.ProvinceService;
import org.springframework.stereotype.Component;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

@Component
public class CovidThread implements Runnable {

    private static final RegionDao regionDao = new RegionDao();
    private static final ProvinceService provinceService = new ProvinceService();
    private static final ReportDao reportDao = new ReportDao();
    private static final Logger logger = LogManager.getLogger(CovidThread.class);

    @Override
    public void run() {
        try {
            logger.info("[INFO] Starting RegionLoader test...");
            Map<Integer, Map<String, String>> regions = RegionLoader.loadRegions();

            if (regions.isEmpty()) {
                logger.info("[INFO] No regions found.");
            } else {
                regions.forEach((index, data) ->
                        logger.info("{} => ISO: {}, Name: {}", index, data.get("iso"), data.get("name"))
                );
                insertRegionsToDatabase(regions);
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

                    boolean success = provinceService.saveProvince(province);
                    if (success) {
                        logger.info("[INFO] Province inserted: {}", info.getProvince());
                    } else {
                        logger.error("[ERROR] Could not insert province: {}", info.getProvince());
                    }
                }
            });

            logger.info("[INFO] Starting to retrieve COVID data...");
            Set<String> isoSet = getIsoSetFromRegionLoader(regions);
            String queryDate = "2022-03-09";

            CovidReports covidReportsService = new CovidReports();
            Map<String, List<ReportLoader>> covidReports = covidReportsService.fetchCovidDataForAllProvinces(isoSet, queryDate);

            covidReports.forEach((iso, reportList) -> {
                logger.info("ISO: {}", iso);
                for (ReportLoader report : reportList) {
                    logger.info("  - {}", report);

                    ReportModel reportModel = new ReportModel(
                            0,
                            report.getDate(),
                            report.getConfirmed(),
                            report.getDeaths(),
                            report.getRecovered(),
                            report.getIso(),
                            report.getRegionName(),
                            report.getProvince()
                    );

                    boolean reportSuccess = reportDao.save(reportModel);
                    if (reportSuccess) {
                        logger.info("[INFO] Report inserted into the database: {}", report);
                    } else {
                        logger.error("[ERROR] Could not insert the report: {}", report);
                    }
                }
            });

            logger.info("[INFO] Query finished for date: {}", queryDate);

        } catch (Exception e) {
            logger.error("[ERROR] Error during thread execution: ", e);
        }
    }

    private static Set<String> getIsoSetFromRegionLoader(Map<Integer, Map<String, String>> regions) {
        Set<String> isoSet = new HashSet<>();
        for (Map<String, String> values : regions.values()) {
            String iso = values.get("iso");
            if (iso != null && !iso.isBlank()) {
                isoSet.add(iso);
            }
        }
        return isoSet;
    }

    private static void insertRegionsToDatabase(Map<Integer, Map<String, String>> regions) {
        for (Map.Entry<Integer, Map<String, String>> entry : regions.entrySet()) {
            Map<String, String> data = entry.getValue();
            String iso = data.get("iso");
            String name = data.get("name");

            RegionModel region = new RegionModel(0, iso, name);
            boolean success = regionDao.save(region);
            if (success) {
                logger.info("[INFO] Region inserted: {}", iso);
            } else {
                logger.error("[ERROR] Error inserting region: {}", iso);
            }
        }
    }

    // Method to start the thread with a 15-second delay
    public static void startThreadWithDelay() {
        try {
            logger.info("[INFO] Waiting 15 seconds before starting the thread...");
            Thread.sleep(15000); // 15 seconds
            new Thread(new CovidThread()).start(); // Start the thread after waiting
            logger.info("[INFO] Thread started.");
        } catch (InterruptedException e) {
            logger.error("[ERROR] Error waiting to start the thread: ", e);
        }
    }
}
