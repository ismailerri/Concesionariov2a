package com.concesionario.model.vehiculo;

// Clase que representa un Coche en el concesionario
public class Coche extends Vehiculo {
    // Atributos específicos del coche
    private int numeroPuertas;
    private String tipoCombustible;
    private boolean automatico;

    // Estado del motor y luces
    private boolean motorEncendido;
    private boolean lucesEncendidas;

    // Constructor básico
    public Coche(String matricula, String marca, int potenciaMotor,
                 int anioFabricacion, double precio) {
        super(matricula, marca, potenciaMotor, anioFabricacion, precio);
        inicializarValoresPorDefecto();
    }

    // Constructor con posición en el parking
    public Coche(String matricula, String marca, int potenciaMotor,
                 int anioFabricacion, double precio, int posicionX, int posicionY) {
        super(matricula, marca, potenciaMotor, anioFabricacion, precio, posicionX, posicionY);
        inicializarValoresPorDefecto();
    }

    // Constructor completo
    public Coche(String matricula, String marca, int potenciaMotor,
                 int anioFabricacion, double precio, int posicionX, int posicionY,
                 int numeroPuertas, String tipoCombustible, boolean automatico) {
        super(matricula, marca, potenciaMotor, anioFabricacion, precio, posicionX, posicionY);
        this.numeroPuertas = numeroPuertas;
        this.tipoCombustible = tipoCombustible;
        this.automatico = automatico;
        motorEncendido = false;
        lucesEncendidas = false;
    }

    // Inicializa valores por defecto para constructores básicos
    private void inicializarValoresPorDefecto() {
        numeroPuertas = 4;
        tipoCombustible = "Gasolina";
        automatico = false;
        motorEncendido = false;
        lucesEncendidas = false;
    }

    @Override
    public String getTipoVehiculo() {
        return "Coche";
    }

    // Enciende o apaga el motor del coche
    public boolean toggleMotor() {
        motorEncendido = !motorEncendido;
        return motorEncendido;
    }

    // Enciende o apaga las luces del coche
    public boolean toggleLuces() {
        lucesEncendidas = !lucesEncendidas;
        return lucesEncendidas;
    }

    // Getters y setters
    public int getNumeroPuertas() {
        return numeroPuertas;
    }

    public void setNumeroPuertas(int numeroPuertas) {
        this.numeroPuertas = numeroPuertas;
    }

    public String getTipoCombustible() {
        return tipoCombustible;
    }

    public void setTipoCombustible(String tipoCombustible) {
        this.tipoCombustible = tipoCombustible;
    }

    public boolean isAutomatico() {
        return automatico;
    }

    public void setAutomatico(boolean automatico) {
        this.automatico = automatico;
    }

    public boolean isMotorEncendido() {
        return motorEncendido;
    }

    public boolean isLucesEncendidas() {
        return lucesEncendidas;
    }

    @Override
    public String toString() {
        return super.toString() + String.format(" - Puertas: %d - Combustible: %s - Automático: %s - Motor: %s - Luces: %s",
                numeroPuertas, tipoCombustible,
                automatico ? "Sí" : "No",
                motorEncendido ? "Encendido" : "Apagado",
                lucesEncendidas ? "Encendidas" : "Apagadas");
    }
}