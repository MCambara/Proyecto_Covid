package org.prograIII.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GsonManager {

    private static final Gson gson = new GsonBuilder().create();

    public static Gson getGson() {
        return gson;
    }
}