package com.concesionario.model.vehiculo;

import java.util.Arrays;
import java.util.List;

// Clase que representa un Tractor en el concesionario
public class Tractor extends Vehiculo {
    // Constantes
    private static final List<String> ARTILUGIOS_VALIDOS = Arrays.asList("Aplanador", "Arador", "Regador");

    // Atributos específicos del tractor
    private String artilugio;
    private String tipoTractor;
    private boolean tieneCabina;

    // Estado del motor y luces
    private boolean motorEncendido;
    private boolean lucesEncendidas;

    // Constructor básico que requiere artilugio
    public Tractor(String matricula, String marca, int potenciaMotor,
                   int anioFabricacion, double precio, String artilugio) {
        super(matricula, marca, potenciaMotor, anioFabricacion, precio);
        validarArtilugio(artilugio);
        this.artilugio = artilugio;
        inicializarValoresPorDefecto();
    }

    // Constructor con posición en el parking
    public Tractor(String matricula, String marca, int potenciaMotor,
                   int anioFabricacion, double precio, int posicionX, int posicionY,
                   String artilugio) {
        super(matricula, marca, potenciaMotor, anioFabricacion, precio, posicionX, posicionY);
        validarArtilugio(artilugio);
        this.artilugio = artilugio;
        inicializarValoresPorDefecto();
    }

    // Constructor completo
    public Tractor(String matricula, String marca, int potenciaMotor,
                   int anioFabricacion, double precio, int posicionX, int posicionY,
                   String artilugio, String tipoTractor, boolean tieneCabina) {
        super(matricula, marca, potenciaMotor, anioFabricacion, precio, posicionX, posicionY);
        validarArtilugio(artilugio);
        this.artilugio = artilugio;
        this.tipoTractor = tipoTractor;
        this.tieneCabina = tieneCabina;
        motorEncendido = false;
        lucesEncendidas = false;
    }

    // Inicializa valores por defecto
    private void inicializarValoresPorDefecto() {
        tipoTractor = "Agrícola";
        tieneCabina = true;
        motorEncendido = false;
        lucesEncendidas = false;
    }

    // Valida que el artilugio sea uno de los permitidos
    private void validarArtilugio(String artilugio) {
        if (!ARTILUGIOS_VALIDOS.contains(artilugio)) {
            throw new IllegalArgumentException("El artilugio debe ser uno de: " +
                    String.join(", ", ARTILUGIOS_VALIDOS));
        }
    }

    @Override
    public String getTipoVehiculo() {
        return "Tractor";
    }

    // Enciende o apaga el motor del tractor
    public boolean toggleMotor() {
        motorEncendido = !motorEncendido;
        return motorEncendido;
    }

    // Enciende o apaga las luces del tractor
    public boolean toggleLuces() {
        lucesEncendidas = !lucesEncendidas;
        return lucesEncendidas;
    }

    // Cambia el artilugio del tractor
    public void setArtilugio(String artilugio) {
        validarArtilugio(artilugio);
        this.artilugio = artilugio;
    }

    // Getters y setters
    public String getArtilugio() {
        return artilugio;
    }

    public String getTipoTractor() {
        return tipoTractor;
    }

    public void setTipoTractor(String tipoTractor) {
        this.tipoTractor = tipoTractor;
    }

    public boolean isTieneCabina() {
        return tieneCabina;
    }

    public void setTieneCabina(boolean tieneCabina) {
        this.tieneCabina = tieneCabina;
    }

    public boolean isMotorEncendido() {
        return motorEncendido;
    }

    public boolean isLucesEncendidas() {
        return lucesEncendidas;
    }

    @Override
    public String toString() {
        return super.toString() + String.format(" - Artilugio: %s - Tipo: %s - Cabina: %s - Motor: %s - Luces: %s",
                artilugio, tipoTractor,
                tieneCabina ? "Sí" : "No",
                motorEncendido ? "Encendido" : "Apagado",
                lucesEncendidas ? "Encendidas" : "Apagadas");
    }
}