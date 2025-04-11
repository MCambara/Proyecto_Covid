package org.prograIII.covidApis;

import org.prograIII.util.ProvinceLoader;
import org.json.JSONException;
import org.prograIII.util.RegionLoader;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.*;


public class CovidProvinces {

    private static final String API_URL = "https://covid-19-statistics.p.rapidapi.com/provinces?iso=";
    private static final String API_KEY = "2505eda46amshc60713983b5e807p1da25ajsn36febcbf4a71";
    private static final String API_HOST = "covid-19-statistics.p.rapidapi.com";

    //metodo que llamamos en main para llamar a la api, guardar los datos en una mapa, y mostrar toda la informacion
    public Map<String, List<ProvinceLoader>> fetchAllRegionData() throws JSONException {
        int counter = 0;
        Set<String> isoSet = getIsoSet();
        Map<String, List<ProvinceLoader>> regionDataMap = new HashMap<>();
        for (String iso : isoSet) {
            JSONObject response = fetchDataByIso(iso);
            storeProvinceData(iso, response, regionDataMap);
            System.out.println("test"+counter);
            counter++;
        }

        return regionDataMap;
    }

    //llamamos la primera api para obtener un set de iso
    private Set<String> getIsoSet() {
        Map<Integer, Map<String, String>> regions = RegionLoader.loadRegions();
        Set<String> isoSet = new HashSet<>();

        for (Map<String, String> values : regions.values()) {
            String iso = values.get("iso");
            if (iso != null && !iso.isBlank()) {
                isoSet.add(iso);
            }
        }

        return isoSet;
    }

    //obtenemos los datos de cada iso
    private JSONObject fetchDataByIso(String iso) throws JSONException {
        try {
            URL url = new URL(API_URL + iso);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("X-RapidAPI-Key", API_KEY);
            conn.setRequestProperty("X-RapidAPI-Host", API_HOST);

            if (conn.getResponseCode() == 200) {
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = in.readLine()) != null) {
                    response.append(line);
                }
                in.close();
                return new JSONObject(response.toString());
            } else {
                return new JSONObject("{\"error\":\"Failed to fetch data for ISO " + iso + "\"}");
            }

        } catch (Exception e) {
            e.printStackTrace();
            return new JSONObject("{\"error\":\"Exception for ISO " + iso + "\"}");
        }
    }

    //guardamos los datos obtenidos en una lista de la clase ProvinceLoader
    private void storeProvinceData(String iso, JSONObject response, Map<String, List<ProvinceLoader>> dataMap) throws JSONException {
        JSONArray dataArray = response.optJSONArray("data");
        if (dataArray == null) return;

        List<ProvinceLoader> regions = new ArrayList<>();

        for (int i = 0; i < dataArray.length(); i++) {
            JSONObject obj = dataArray.getJSONObject(i);

            String province = obj.optString("province", "");
            String name = obj.optString("name", "");
            double lat = obj.optDouble("lat", 0.0);
            double lng = obj.optDouble("long", 0.0);

            regions.add(new ProvinceLoader(iso, province, name, lat, lng));
        }

        dataMap.put(iso, regions);
    }
}