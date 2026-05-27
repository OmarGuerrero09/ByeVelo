package modelo1;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Autor: Edwin Omar Guerrero Godina
 * Clase para gestionar la conexión a la base de datos MySQL.
 */
public class ConexionDB {
    // Apuntamos a la nueva base de datos ByeVelo
    private static final String DB_URL = "jdbc:mysql://localhost:3306/byevelo";
    private static final String DB_USER = "byeVelo";
    private static final String DB_PASSWORD = "Byevelo2026";

    public Connection conectar() {
        Connection conexion = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conexion = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            System.out.println("Conexión exitosa a la base de datos ByeVelo.");
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("Error de conexión: " + e.getMessage());
        }
        return conexion;
    }
}