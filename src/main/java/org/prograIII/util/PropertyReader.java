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
                throw new IOException("Properties file not found");
            }
            props.load(input);
        } catch (IOException e) {
            throw new RuntimeException("Error loading the properties file", e);
        }
    }

    public static String getProperty(String key) {
        return props.getProperty(key);
    }

    // Method to get the database URL
    public static String getDbUrl() {
        return getProperty("db.url");
    }

    // Method to get the database username
    public static String getDbUsername() {
        return getProperty("db.username");
    }

    // Method to get the database password
    public static String getDbPassword() {
        return getProperty("db.password");
    }

    // Methods for COVID API configuration
    public static String getCovidApiBaseUrl() {
        return getProperty("covid.api.base-url");
    }

    public static String getCovidApiKey() {
        return getProperty("covid.api.key");
    }

    public static String getCovidApiHost() {
        return getProperty("covid.api.host");
    }

    // Methods for application parameters
    public static String getAppInitialDelay() {
        return getProperty("app.initial-delay");
    }

    public static String getAppTargetDate() {
        return getProperty("app.target-date");
    }

    // Method to get the logging level
    public static String getLoggingLevel() {
        return getProperty("logging.level.org.prograIII");
    }
}
