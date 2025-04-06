package com.concesionario.model.vehiculo;

import java.util.Arrays;
import java.util.Objects;

// Clase abstracta base para todos los tipos de vehículos del concesionario
public abstract class Vehiculo {
    // Atributos básicos según los requisitos del proyecto
    private final String matricula;
    private String marca;
    private int potenciaMotor;
    private int anioFabricacion;
    private double precio;
    private int posicionX;
    private int posicionY;

    // Array de potencias válidas para optimizar validación
    private static final int[] POTENCIAS_VALIDAS = {50, 100, 150, 200};

    // Constructor con todos los atributos para vehículos sin posición asignada
    public Vehiculo(String matricula, String marca, int potenciaMotor,
                    int anioFabricacion, double precio) {
        this(matricula, marca, potenciaMotor, anioFabricacion, precio, -1, -1);
    }

    // Constructor completo con posición en el parking
    public Vehiculo(String matricula, String marca, int potenciaMotor,
                    int anioFabricacion, double precio, int posicionX, int posicionY) {
        // Validar potencia del motor (según requisitos: 50, 100, 150 o 200)
        if (!isPotenciaValida(potenciaMotor)) {
            throw new IllegalArgumentException("La potencia del motor debe ser 50, 100, 150 o 200");
        }

        this.matricula = matricula;
        this.marca = marca;
        this.potenciaMotor = potenciaMotor;
        this.anioFabricacion = anioFabricacion;
        this.precio = precio;
        this.posicionX = posicionX;
        this.posicionY = posicionY;
    }

    // Metodo que debe implementar cada tipo de vehículo para identificarse
    public abstract String getTipoVehiculo();

    // Asigna una posición en el parking al vehículo
    public void asignarPosicionParking(int x, int y) {
        this.posicionX = x;
        this.posicionY = y;
    }

    // Verifica si el vehículo está aparcado en el parking

    public boolean estaAparcado() {
        return posicionX >= 0 && posicionY >= 0;
    }

    // Validación de potencia del motor
    private boolean isPotenciaValida(int potencia) {
        return Arrays.stream(POTENCIAS_VALIDAS).anyMatch(p -> p == potencia);
    }

    // Getters y setters
    public String getMatricula() {
        return matricula;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public int getPotenciaMotor() {
        return potenciaMotor;
    }

    public void setPotenciaMotor(int potenciaMotor) {
        if (!isPotenciaValida(potenciaMotor)) {
            throw new IllegalArgumentException("La potencia del motor debe ser 50, 100, 150 o 200");
        }
        this.potenciaMotor = potenciaMotor;
    }

    public int getAnioFabricacion() {
        return anioFabricacion;
    }

    public void setAnioFabricacion(int anioFabricacion) {
        this.anioFabricacion = anioFabricacion;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public int getPosicionX() {
        return posicionX;
    }

    public void setPosicionX(int posicionX) {
        this.posicionX = posicionX;
    }

    public int getPosicionY() {
        return posicionY;
    }

    public void setPosicionY(int posicionY) {
        this.posicionY = posicionY;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vehiculo vehiculo = (Vehiculo) o;
        return Objects.equals(matricula, vehiculo.matricula);
    }

    @Override
    public int hashCode() {
        return Objects.hash(matricula);
    }

    @Override
    public String toString() {
        return String.format("%s - Matrícula: %s - Marca: %s - Potencia: %d CV - Año: %d - Precio: %.2f€ - Posición: [%d,%d]",
                getTipoVehiculo(), matricula, marca, potenciaMotor, anioFabricacion, precio, posicionX, posicionY);
    }
}

