package org.prograIII.db.dabaBaseConnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    public static Connection getConnection() throws SQLException {
        // Definir los parámetros de conexión directamente en la clase
        String url = "jdbc:mysql://localhost:3306/proyecto_covid";
        String user = "root"; // Reemplazar con el nombre de usuario adecuado
        String password = "EggTortuga78"; // Reemplazar con la contraseña adecuada

        return DriverManager.getConnection(url, user, password);
    }
}