package com.concesionario.db.dao;

import com.concesionario.model.vehiculo.Vehiculo;
import java.util.*;
import java.util.stream.Collectors;

// Implementación en memoria del DAO de vehículos (para desarrollo y pruebas)
public class VehiculoDAOMemoria implements VehiculoDAO {
    // Mapa que simula la base de datos en memoria
    private final Map<String, Vehiculo> vehiculos = new HashMap<>();

    @Override
    public boolean guardar(Vehiculo vehiculo) {
        if (vehiculos.containsKey(vehiculo.getMatricula())) {
            return false; // No permitir duplicados de matrícula
        }
        vehiculos.put(vehiculo.getMatricula(), vehiculo);
        return true;
    }

    @Override
    public boolean actualizar(Vehiculo vehiculo) {
        if (vehiculos.containsKey(vehiculo.getMatricula())) {
            vehiculos.put(vehiculo.getMatricula(), vehiculo);
            return true;
        }
        return false;
    }

    @Override
    public boolean eliminar(String matricula) {
        return vehiculos.remove(matricula) != null;
    }

    @Override
    public Optional<Vehiculo> buscarPorId(String matricula) {
        return Optional.ofNullable(vehiculos.get(matricula));
    }

    @Override
    public List<Vehiculo> obtenerTodos() {
        return new ArrayList<>(vehiculos.values());
    }

    @Override
    public List<Vehiculo> obtenerPorTipo(String tipo) {
        String tipoUpper = tipo.toUpperCase();
        return vehiculos.values().stream()
                .filter(v -> v.getTipoVehiculo().toUpperCase().equals(tipoUpper))
                .collect(Collectors.toList());
    }

    @Override
    public boolean existe(String matricula) {
        return vehiculos.containsKey(matricula);
    }

    @Override
    public boolean actualizarPosicion(String matricula, int posicionX, int posicionY) {
        Vehiculo v = vehiculos.get(matricula);
        if (v != null) {
            v.asignarPosicionParking(posicionX, posicionY);
            return true;
        }
        return false;
    }
}