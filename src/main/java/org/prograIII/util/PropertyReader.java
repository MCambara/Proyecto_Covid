package org.prograIII.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertyReader {
    private static final Properties props = new Properties();

    static {
        try (InputStream input = PropertyReader.class.getClassLoader()
                .getResourceAsStream("application.properties")) {
            if (input == null) {
                throw new IOException("Archivo de propiedades no encontrado");
            }
            props.load(input);
        } catch (IOException e) {
            throw new RuntimeException("Error cargando el archivo de propiedades", e);
        }
    }

    public static String getProperty(String key) {
        return props.getProperty(key);
    }

    // Método para obtener la URL de la base de datos
    public static String getDbUrl() {
        return getProperty("db.url");
    }

    // Método para obtener el nombre de usuario de la base de datos
    public static String getDbUsername() {
        return getProperty("db.username");
    }

    // Método para obtener la contraseña de la base de datos
    public static String getDbPassword() {
        return getProperty("db.password");
    }

    // Métodos para configuración de la API de COVID
    public static String getCovidApiBaseUrl() {
        return getProperty("covid.api.base-url");
    }

    public static String getCovidApiKey() {
        return getProperty("covid.api.key");
    }

    public static String getCovidApiHost() {
        return getProperty("covid.api.host");
    }

    // Métodos para parámetros de la aplicación
    public static String getAppInitialDelay() {
        return getProperty("app.initial-delay");
    }

    public static String getAppTargetDate() {
        return getProperty("app.target-date");
    }

    // Método para obtener el nivel de logging
    public static String getLoggingLevel() {
        return getProperty("logging.level.org.prograIII");
    }
}
