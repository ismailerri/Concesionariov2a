package com.concesionario.model;

import com.concesionario.model.vehiculo.Vehiculo;
import com.concesionario.util.Constants;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

// Clase que gestiona el parking de vehículos (matriz 9x9)
public class Parking {
    // Tamaño del parking
    public static final int TAMANIO = 9;

    // Matriz que representa el parking
    private Vehiculo[][] plazas;

    // Mapa para acceso rápido por matrícula
    private Map<String, Vehiculo> vehiculosMap;

    // Constructor
    public Parking() {
        plazas = new Vehiculo[TAMANIO][TAMANIO];
        vehiculosMap = new HashMap<>();
    }

    // Estaciona un vehículo en una posición específica
    public boolean estacionarVehiculo(Vehiculo vehiculo, int x, int y) {
        // Validar coordenadas
        if (!sonCoordenadasValidas(x, y)) return false;

        // Verificar si la plaza está ocupada
        if (plazas[y][x] != null) return false;

        // Si el vehículo ya estaba estacionado, liberar su plaza anterior
        if (vehiculo.estaAparcado()) {
            plazas[vehiculo.getPosicionY()][vehiculo.getPosicionX()] = null;
        }

        // Estacionar el vehículo en la nueva posición
        plazas[y][x] = vehiculo;
        vehiculo.asignarPosicionParking(x, y);

        // Agregar al mapa por matrícula
        vehiculosMap.put(vehiculo.getMatricula(), vehiculo);

        return true;
    }

    // Retira un vehículo del parking por su matrícula
    public boolean retirarVehiculo(String matricula) {
        Vehiculo vehiculo = vehiculosMap.get(matricula);
        if (vehiculo != null && vehiculo.estaAparcado()) {
            plazas[vehiculo.getPosicionY()][vehiculo.getPosicionX()] = null;
            vehiculosMap.remove(matricula);
            vehiculo.asignarPosicionParking(-1, -1);
            return true;
        }
        return false;
    }

    // Busca un vehículo por su matrícula
    public Optional<Vehiculo> buscarVehiculo(String matricula) {
        return Optional.ofNullable(vehiculosMap.get(matricula));
    }

    // Obtiene el vehículo en una posición específica
    public Optional<Vehiculo> obtenerVehiculoEnPosicion(int x, int y) {
        if (sonCoordenadasValidas(x, y)) {
            return Optional.ofNullable(plazas[y][x]);
        }
        return Optional.empty();
    }

    // Verifica si las coordenadas son válidas
    private boolean sonCoordenadasValidas(int x, int y) {
        return x >= 0 && x < TAMANIO && y >= 0 && y < TAMANIO;
    }

    // Verifica si una plaza está ocupada
    public boolean plazaOcupada(int x, int y) {
        if (sonCoordenadasValidas(x, y)) {
            return plazas[y][x] != null;
        }
        return false; // Plazas fuera de rango se consideran no disponibles
    }

    // Devuelve el mapa de vehículos
    public Map<String, Vehiculo> getVehiculos() {
        return new HashMap<>(vehiculosMap);
    }

    // Devuelve una copia de la matriz del parking
    public Vehiculo[][] getMatrizParking() {
        Vehiculo[][] copia = new Vehiculo[TAMANIO][TAMANIO];
        for (int i = 0; i < TAMANIO; i++) {
            System.arraycopy(plazas[i], 0, copia[i], 0, TAMANIO);
        }
        return copia;
    }

    // Cuenta el número de plazas ocupadas
    public int contarPlazasOcupadas() {
        return vehiculosMap.size();
    }

    // Devuelve el número total de plazas
    public int getTotalPlazas() {
        return TAMANIO * TAMANIO;
    }

    // Procesa los disparos de los tanques
    public String procesarDisparoTanque(String matriculaTanque) {
        Vehiculo tanque = vehiculosMap.get(matriculaTanque);
        if (tanque != null && tanque instanceof com.concesionario.model.vehiculo.Tanque) {
            com.concesionario.model.vehiculo.Tanque t = (com.concesionario.model.vehiculo.Tanque) tanque;
            String matriculaObjetivo = t.disparar(plazas);

            // Si se identificó un objetivo, devolverlo para que el controlador lo elimine
            if (matriculaObjetivo != null && !matriculaObjetivo.equals("No hay ningún vehículo delante para disparar")) {
                return matriculaObjetivo;
            }
        }
        return null;
    }

    // Elimina todos los vehículos del parking
    public void limpiarParking() {
        plazas = new Vehiculo[TAMANIO][TAMANIO];
        vehiculosMap.clear();
    }
}