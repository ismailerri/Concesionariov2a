package com.concesionario.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

// Clase simple para logging de mensajes
public class Logger {

    // Niveles de log
    public enum Level {
        INFO, WARNING, ERROR, DEBUG
    }

    // Nivel actual de logging
    private static Level currentLevel = Level.INFO;

    // Privatizar el constructor para evitar instanciación
    private Logger() {}

    // Establecer el nivel de logging
    public static void setLevel(Level level) {
        currentLevel = level;
    }

    // Log de nivel INFO
    public static void info(String message) {
        if (currentLevel.ordinal() <= Level.INFO.ordinal()) {
            log(Level.INFO, message);
        }
    }

    // Log de nivel WARNING
    public static void warning(String message) {
        if (currentLevel.ordinal() <= Level.WARNING.ordinal()) {
            log(Level.WARNING, message);
        }
    }

    // Log de nivel ERROR
    public static void error(String message) {
        if (currentLevel.ordinal() <= Level.ERROR.ordinal()) {
            log(Level.ERROR, message);
        }
    }

    // Log de nivel ERROR con excepción
    public static void error(String message, Throwable throwable) {
        if (currentLevel.ordinal() <= Level.ERROR.ordinal()) {
            log(Level.ERROR, message + ": " + throwable.getMessage());
            throwable.printStackTrace();
        }
    }

    // Log de nivel DEBUG
    public static void debug(String message) {
        if (currentLevel.ordinal() <= Level.DEBUG.ordinal()) {
            log(Level.DEBUG, message);
        }
    }

    // Método principal de logging
    private static void log(Level level, String message) {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        System.out.println("[" + now.format(formatter) + "] [" + level + "] " + message);
    }
}