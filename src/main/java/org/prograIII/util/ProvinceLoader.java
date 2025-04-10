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

    public ProvinceLoader(String iso, String province, String name, double lat, double lng) {

    }

    // Método para cargar provincias desde la API
    public static Map<Integer, ProvinceLoader> loadProvinces() {
        Map<Integer, ProvinceLoader> provinces = new HashMap<>();

        // URL de la API para obtener las provincias (ajusta la URL según tu API)
        String apiUrl = "https://api.example.com/provinces";  // Reemplaza con la URL correcta de la API

        // Realiza la solicitud HTTP GET
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(apiUrl);

            // Configura los headers si es necesario (si tu API requiere autenticación)
            request.setHeader("X-API-Key", "tu_clave_de_api");

            // Ejecuta la solicitud
            HttpResponse response = client.execute(request);
            String jsonResponse = EntityUtils.toString(response.getEntity());

            // Procesar la respuesta JSON
            JSONArray provincesArray = new JSONArray(jsonResponse);

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
}
