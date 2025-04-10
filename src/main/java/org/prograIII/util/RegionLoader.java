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

public class RegionLoader {

    // Método para cargar regiones desde la API
    public static Map<Integer, Map<String, String>> loadRegions() {
        Map<Integer, Map<String, String>> regions = new HashMap<>();

        // URL de la API para obtener las regiones (puedes cambiarla según la API que estés usando)
        String apiUrl = "https://covid-19-statistics.p.rapidapi.com/regions";

        // Realiza la solicitud HTTP GET
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(apiUrl);

            // Configura los headers con la clave de API y host
            request.setHeader("X-RapidAPI-Host", "covid-19-statistics.p.rapidapi.com");
            request.setHeader("X-RapidAPI-Key", "2505eda46amshc60713983b5e807p1da25ajsn36febcbf4a71");

            // Ejecuta la solicitud
            HttpResponse response = client.execute(request);
            String jsonResponse = EntityUtils.toString(response.getEntity());

            // Procesar la respuesta JSON como un objeto (no un arreglo)
            JSONObject jsonObject = new JSONObject(jsonResponse);

            // Asegurarse de que 'data' existe y es un arreglo
            if (jsonObject.has("data") && jsonObject.get("data") instanceof JSONArray) {
                JSONArray regionsArray = jsonObject.getJSONArray("data");

                for (int i = 0; i < regionsArray.length(); i++) {
                    JSONObject regionObj = regionsArray.getJSONObject(i);
                    String iso = regionObj.getString("iso");
                    String name = regionObj.getString("name");

                    // Crear un mapa de cada región
                    Map<String, String> regionData = new HashMap<>();
                    regionData.put("iso", iso);
                    regionData.put("name", name);

                    // Agregar al mapa principal
                    regions.put(i + 1, regionData);  // Usamos i+1 como índice (puedes usar otro si prefieres)
                }
            } else {
                System.err.println("Error: El campo 'data' no es un arreglo o no existe.");
            }
        } catch (Exception e) {
            System.err.println("Error al cargar regiones desde la API: " + e.getMessage());
        }

        return regions;
    }
}
