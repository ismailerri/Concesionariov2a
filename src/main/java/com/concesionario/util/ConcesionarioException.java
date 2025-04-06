package com.concesionario.util;

// Clase para excepciones personalizadas de la aplicación
public class ConcesionarioException extends Exception {

    // Tipos de error
    public enum ErrorType {
        // Errores de base de datos
        DATABASE_ERROR("Error de base de datos"),
        CONNECTION_ERROR("Error de conexión"),

        // Errores de modelo
        INVALID_VEHICLE_DATA("Datos de vehículo inválidos"),
        DUPLICATE_PLATE("Matrícula duplicada"),
        VEHICLE_NOT_FOUND("Vehículo no encontrado"),

        // Errores de parking
        PARKING_FULL("Parking lleno"),
        INVALID_POSITION("Posición inválida"),
        POSITION_OCCUPIED("Posición ocupada"),

        // Errores de tanque
        WEAPON_NOT_LOADED("Armamento no cargado"),
        NO_TARGET("No hay objetivo para disparar"),

        // Otros errores
        VALIDATION_ERROR("Error de validación"),
        GENERAL_ERROR("Error general");

        private final String description;

        ErrorType(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }

    private final ErrorType errorType;

    // Constructor con solo tipo de error
    public ConcesionarioException(ErrorType errorType) {
        super(errorType.getDescription());
        this.errorType = errorType;
    }

    // Constructor con tipo de error y mensaje personalizado
    public ConcesionarioException(ErrorType errorType, String message) {
        super(message);
        this.errorType = errorType;
    }

    // Constructor con tipo de error, mensaje y causa
    public ConcesionarioException(ErrorType errorType, String message, Throwable cause) {
        super(message, cause);
        this.errorType = errorType;
    }

    // Obtiene el tipo de error
    public ErrorType getErrorType() {
        return errorType;
    }
}
