package com.concesionario.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

// Clase con utilidades para gestión de configuración
public class ConfigUtils {

    private static Properties properties;
    private static final String CONFIG_FILE = "src/main/resources/config.properties";

    // Privatizar el constructor para evitar instanciación
    private ConfigUtils() {}

    // Inicializa las propiedades cargándolas desde el archivo de configuración
    private static void initProperties() {
        if (properties == null) {
            properties = new Properties();
            try (FileInputStream fis = new FileInputStream(CONFIG_FILE)) {
                properties.load(fis);
            } catch (IOException e) {
                System.err.println("Error al cargar el archivo de configuración: " + e.getMessage());
                // Inicializar con valores por defecto
                setDefaultProperties();
            }
        }
    }

    // Establece valores por defecto en caso de que no se pueda cargar el archivo
    private static void setDefaultProperties() {
        properties = new Properties();
        // Valores por defecto para MySQL
        properties.setProperty("jdbc.url", "jdbc:mysql://localhost:3306");
        properties.setProperty("jdbc.usuario", "root");
        properties.setProperty("jdbc.password", "");

        // Valores por defecto para MongoDB
        properties.setProperty("mongodb.uri", "mongodb://localhost:27017");
        properties.setProperty("mongodb.database", "concesionario");
        properties.setProperty("mongodb.collection", "vehiculos");

        // Otros ajustes
        properties.setProperty("usar.memoria", "true");
    }

    // Obtiene una propiedad como String
    public static String getProperty(String key) {
        initProperties();
        return properties.getProperty(key);
    }

    // Obtiene una propiedad como String con valor por defecto
    public static String getProperty(String key, String defaultValue) {
        initProperties();
        return properties.getProperty(key, defaultValue);
    }

    // Obtiene una propiedad como boolean
    public static boolean getBooleanProperty(String key) {
        String value = getProperty(key);
        return value != null && value.equalsIgnoreCase("true");
    }

    // Obtiene una propiedad como entero
    public static int getIntProperty(String key, int defaultValue) {
        String value = getProperty(key);
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    // Obtiene la URL de conexión a la base de datos MySQL
    public static String getJdbcUrl() {
        return getProperty("jdbc.url") + "/concesionario";
    }

    // Obtiene el usuario para la conexión a MySQL
    public static String getJdbcUser() {
        return getProperty("jdbc.usuario");
    }

    // Obtiene la contraseña para la conexión a MySQL
    public static String getJdbcPassword() {
        return getProperty("jdbc.password");
    }

    // Obtiene la URI para la conexión a MongoDB
    public static String getMongoUri() {
        return getProperty("mongodb.uri");
    }

    // Obtiene el nombre de la base de datos MongoDB
    public static String getMongoDatabase() {
        return getProperty("mongodb.database");
    }

    // Obtiene el nombre de la colección MongoDB
    public static String getMongoCollection() {
        return getProperty("mongodb.collection");
    }

    // Verifica si se debe usar almacenamiento en memoria
    public static boolean usarMemoria() {
        return getBooleanProperty("usar.memoria");
    }
}