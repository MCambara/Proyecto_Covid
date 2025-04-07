package org.prograIII.db.dabaBaseConnection;

import org.prograIII.util.PropertyReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    public static Connection getConnection() throws SQLException {
        // Obtiene valores del application.properties
        String url = PropertyReader.getProperty("db.url");
        String user = PropertyReader.getProperty("db.username");
        String password = PropertyReader.getProperty("db.password");

        return DriverManager.getConnection(url, user, password);
    }
}