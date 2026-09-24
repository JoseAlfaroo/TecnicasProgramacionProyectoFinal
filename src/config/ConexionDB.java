package config;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConexionDB {
    private static ConexionDB instance;
    private static String url;
    private static String user;
    private static String password;

    private ConexionDB() {
        try (InputStream input = ConexionDB.class.getClassLoader().getResourceAsStream("db.properties")) {
            Properties prop = new Properties();
            if (input == null) {
                System.err.println("Error: No se encontró el archivo db.properties");
            } else {
                prop.load(input);
                url = prop.getProperty("db.url");
                user = prop.getProperty("db.user");
                password = prop.getProperty("db.password");
                Class.forName("com.mysql.cj.jdbc.Driver");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static synchronized ConexionDB getInstance() {
        if (instance == null) {
            instance = new ConexionDB();
        }
        return instance;
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }
}