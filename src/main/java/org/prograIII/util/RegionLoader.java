package org.prograIII.util;

import com.google.gson.reflect.TypeToken;
import org.prograIII.covidApis.CovidRegions;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RegionLoader {

    public static Map<Integer, Map<String, String>> loadRegions() {
        CovidRegions api = new CovidRegions();
        String json = api.fetchRegionJson();

        if (json == null) return new HashMap<>();

        Type responseType = new TypeToken<ApiResponse>() {}.getType();
        ApiResponse response = GsonManager.getGson().fromJson(json, responseType);

        Map<Integer, Map<String, String>> regionMap = new HashMap<>();
        int index = 1;

        for (Region region : response.data) {
            Map<String, String> values = new HashMap<>();
            values.put("iso", region.iso);
            values.put("name", region.name);
            regionMap.put(index++, values);
        }

        return regionMap;
    }
    // Clases internas para deserialización
    static class ApiResponse {
        List<Region> data;
    }

    static class Region {
        String iso;
        String name;
    }
}
