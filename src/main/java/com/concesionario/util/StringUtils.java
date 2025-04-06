package com.concesionario.util;

// Clase con utilidades para manipulación de cadenas
public class StringUtils {

    // Privatizar el constructor para evitar instanciación
    private StringUtils() {}

    // Verifica si una cadena está vacía o es nula
    public static boolean isEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }

    // Capitaliza la primera letra de una cadena
    public static String capitalize(String str) {
        if (isEmpty(str)) return str;
        return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
    }

    // Formatea un precio como cadena con 2 decimales y símbolo de euro
    public static String formatPrecio(double precio) {
        return String.format("%.2f €", precio);
    }

    // Formatea coordenadas de posición de parking
    public static String formatPosicion(int x, int y) {
        return String.format("[%d,%d]", x, y);
    }

    // Trunca una cadena a una longitud máxima
    public static String truncate(String str, int maxLength) {
        if (isEmpty(str) || str.length() <= maxLength) return str;
        return str.substring(0, maxLength) + "...";
    }

    // Extrae la matrícula de una cadena del formato "Tipo: Marca (Matrícula)"
    public static String extractMatricula(String str) {
        if (isEmpty(str)) return "";

        int startIndex = str.lastIndexOf("(");
        int endIndex = str.lastIndexOf(")");

        if (startIndex >= 0 && endIndex > startIndex) {
            return str.substring(startIndex + 1, endIndex);
        }

        return "";
    }
}