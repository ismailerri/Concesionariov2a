package com.concesionario.util;

import java.awt.Color;

// Constantes globales para la aplicación
public class Constants {
    // Tamaño del parking
    public static final int PARKING_SIZE = 9;

    // Tipos de vehículos
    public static final String TIPO_COCHE = "COCHE";
    public static final String TIPO_TRACTOR = "TRACTOR";
    public static final String TIPO_TANQUE = "TANQUE";

    // Valores de potencia válidos
    public static final int[] POTENCIAS_VALIDAS = {50, 100, 150, 200};

    // Artilugios válidos para tractores
    public static final String[] ARTILUGIOS_VALIDOS = {"Aplanador", "Arador", "Regador"};

    // Colores para los vehículos en el parking
    public static final Color COLOR_COCHE = Color.GREEN;
    public static final Color COLOR_TRACTOR = Color.YELLOW;
    public static final Color COLOR_TANQUE = Color.RED;
    public static final Color COLOR_VACIO = Color.LIGHT_GRAY;

    // Mensajes de error
    public static final String ERROR_POTENCIA_INVALIDA = "La potencia del motor debe ser 50, 100, 150 o 200";
    public static final String ERROR_ARTILUGIO_INVALIDO = "El artilugio debe ser Aplanador, Arador o Regador";
    public static final String ERROR_MATRICULA_DUPLICADA = "Ya existe un vehículo con esa matrícula";
    public static final String ERROR_PLAZA_OCUPADA = "La plaza seleccionada ya está ocupada";
    public static final String ERROR_POSICION_INVALIDA = "Posición fuera de los límites del parking";
    public static final String ERROR_TANQUE_NO_CARGADO = "El armamento del tanque no está cargado";

    // Mensajes de éxito
    public static final String EXITO_CREACION_VEHICULOS = "Vehículos creados correctamente";
    public static final String EXITO_ACTUALIZACION_VEHICULO = "Vehículo actualizado correctamente";
    public static final String EXITO_ELIMINACION_VEHICULO = "Vehículo eliminado correctamente";
    public static final String EXITO_ESTACIONAMIENTO = "Vehículo estacionado correctamente";
    public static final String EXITO_RETIRADA = "Vehículo retirado del parking correctamente";
    public static final String EXITO_ARMAMENTO_CARGADO = "Armamento cargado correctamente";
    public static final String EXITO_DISPARO = "¡Disparo exitoso! Vehículo eliminado";
}