package org.prograIII.covidApis;

import org.json.JSONArray;
import org.json.JSONObject;
import org.prograIII.util.ReportLoader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CovidReports {

    private static final String BASE_API_URL = "https://covid-19-statistics.p.rapidapi.com/reports?date=";
    private static final String API_KEY = "2505eda46amshc60713983b5e807p1da25ajsn36febcbf4a71";
    private static final String API_HOST = "covid-19-statistics.p.rapidapi.com";
    private static final Logger logger = LogManager.getLogger(CovidReports.class);

    // Now receives the date as a parameter
    public Map<String, List<ReportLoader>> fetchCovidDataForAllProvinces(Set<String> isoSet, String date) {
        Map<String, List<ReportLoader>> covidDataMap = new HashMap<>();
        int counter = 0;

        for (String iso : isoSet) {
            try {
                JSONObject response = fetchDataByIso(iso, date);
                List<ReportLoader> reportList = parseReportData(response);
                covidDataMap.put(iso, reportList);
                counter += reportList.size();
            } catch (Exception e) {
                logger.error("[ERROR] Error processing ISO: {} - {}", iso, e.getMessage());
            }
        }

        logger.info("[INFO] Total reports processed: {}", counter);
        return covidDataMap;
    }

    // Receives the date as a parameter
    private JSONObject fetchDataByIso(String iso, String date) {
        try {
            String fullUrl = BASE_API_URL + date + "&iso=" + iso;
            URL url = new URL(fullUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("X-RapidAPI-Key", API_KEY);
            conn.setRequestProperty("X-RapidAPI-Host", API_HOST);

            int responseCode = conn.getResponseCode();
            if (responseCode == 200) {
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder response = new StringBuilder();
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                return new JSONObject(response.toString());
            } else {
                logger.warn("[WARN] Query failed for ISO {}. Code: {}", iso, responseCode);
                return new JSONObject();
            }
        } catch (Exception e) {
            logger.error("[ERROR] Exception while querying ISO {}: {}", iso, e.getMessage());
            return new JSONObject();
        }
    }

    private List<ReportLoader> parseReportData(JSONObject response) {
        List<ReportLoader> reportList = new ArrayList<>();

        JSONArray dataArray = response.optJSONArray("data");
        if (dataArray == null) return reportList;

        for (int i = 0; i < dataArray.length(); i++) {
            JSONObject obj = dataArray.getJSONObject(i);

            String date = obj.optString("date");
            int confirmed = obj.optInt("confirmed");
            int deaths = obj.optInt("deaths");
            int recovered = obj.optInt("recovered");

            JSONObject region = obj.optJSONObject("region");
            String iso = region.optString("iso");
            String name = region.optString("name");
            String province = region.optString("province");

            ReportLoader report = new ReportLoader(date, confirmed, deaths, recovered, iso, name, province);
            reportList.add(report);
        }

        return reportList;
    }
}
