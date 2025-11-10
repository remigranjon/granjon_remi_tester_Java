package com.parkit.parkingsystem.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class DataBaseConfig {

    private static final Logger logger = LogManager.getLogger("DataBaseConfig");
    private static final Map<String, String> envVars = loadEnvFile();
    
    /**
     * Charge les variables du fichier .env
     */
    protected static Map<String, String> loadEnvFile() {
        Map<String, String> envMap = new HashMap<>();
        Path envPath = Paths.get(".env");
        
        if (Files.exists(envPath)) {
            try {
                Files.lines(envPath)
                    .filter(line -> !line.trim().isEmpty() && !line.trim().startsWith("#"))
                    .forEach(line -> {
                        String[] parts = line.split("=", 2);
                        if (parts.length == 2) {
                            envMap.put(parts[0].trim(), parts[1].trim());
                        }
                    });
                logger.info("Fichier .env chargé avec " + envMap.size() + " variables");
            } catch (IOException e) {
                logger.warn("Erreur lors de la lecture du fichier .env: " + e.getMessage());
            }
        } else {
            logger.info("Aucun fichier .env trouvé, utilisation des variables système");
        }
        return envMap;
    }
    
    /**
     * Récupère une variable d'environnement (priorité : système > .env > défaut)
     */
    static String getEnvVar(String key, String defaultValue) {
        String systemValue = System.getenv(key);
        if (systemValue != null) {
            return systemValue;
        }
        return envVars.getOrDefault(key, defaultValue);
    }

    public Connection getConnection() throws ClassNotFoundException, SQLException {
        logger.info("Create DB connection");
        Class.forName("com.mysql.cj.jdbc.Driver");
        
        // Récupérer les paramètres avec priorité : système > .env > défaut
        String dbName = getEnvVar("DB_PROD_NAME", "prod");
        String user = getEnvVar("DB_USER", "p4_user");
        String password = getEnvVar("DB_PASSWORD", "p4_password");
        
        String url = "jdbc:mysql://localhost:3306/" + dbName;
        logger.info("Connecting to database: " + url + " with user: " + user);
        
        return DriverManager.getConnection(url, user, password);
    }

    public void closeConnection(Connection con){
        if(con!=null){
            try {
                con.close();
                logger.info("Closing DB connection");
            } catch (SQLException e) {
                logger.error("Error while closing connection",e);
            }
        }
    }

    public void closePreparedStatement(PreparedStatement ps) {
        if(ps!=null){
            try {
                ps.close();
                logger.info("Closing Prepared Statement");
            } catch (SQLException e) {
                logger.error("Error while closing prepared statement",e);
            }
        }
    }

    public void closeResultSet(ResultSet rs) {
        if(rs!=null){
            try {
                rs.close();
                logger.info("Closing Result Set");
            } catch (SQLException e) {
                logger.error("Error while closing result set",e);
            }
        }
    }
}
