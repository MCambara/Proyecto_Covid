package org.prograIII.util;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ProvinceLoader {

    private String iso;
    private String province;
    private String name;
    private double lat;
    private double lng;

    // Constructor para almacenar la información de la provincia
    public ProvinceLoader(String iso, String province, String name, double lat, double lng) {
        this.iso = iso;
        this.province = province;
        this.name = name;
        this.lat = lat;
        this.lng = lng;
    }

    // Método para cargar provincias desde la API
    public static Map<Integer, ProvinceLoader> loadProvinces() {
        Map<Integer, ProvinceLoader> provinces = new HashMap<>();

        // URL de la API para obtener las provincias
        String apiUrl = "https://covid-19-statistics.p.rapidapi.com/provinces";

        // Realiza la solicitud HTTP GET
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(apiUrl);

            // Configura los headers necesarios
            request.setHeader("X-RapidAPI-Key", "2505eda46amshc60713983b5e807p1da25ajsn36febcbf4a71");
            request.setHeader("X-RapidAPI-Host", "covid-19-statistics.p.rapidapi.com");
            request.setHeader("Content-Type", "application/json");

            // Ejecuta la solicitud
            HttpResponse response = client.execute(request);
            String jsonResponse = EntityUtils.toString(response.getEntity());

            // Procesar la respuesta JSON
            JSONArray provincesArray = new JSONArray(jsonResponse);

            // Recorre las provincias obtenidas de la API
            for (int i = 0; i < provincesArray.length(); i++) {
                JSONObject provinceObj = provincesArray.getJSONObject(i);
                String iso = provinceObj.getString("iso");
                String province = provinceObj.getString("province");
                String name = provinceObj.getString("name");
                double lat = provinceObj.getDouble("lat");
                double lng = provinceObj.getDouble("lng");

                // Crear una instancia de ProvinceLoader para la provincia
                ProvinceLoader provinceLoader = new ProvinceLoader(iso, province, name, lat, lng);

                // Agregar la provincia al mapa
                provinces.put(i + 1, provinceLoader);  // Usamos i+1 como índice (puedes usar otro si prefieres)
            }
        } catch (Exception e) {
            System.err.println("Error al cargar provincias desde la API: " + e.getMessage());
        }

        return provinces;
    }

    // Métodos getters y setters si es necesario para acceder a los datos de la provincia
    public String getIso() {
        return iso;
    }

    public String getProvince() {
        return province;
    }

    public String getName() {
        return name;
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }
}
