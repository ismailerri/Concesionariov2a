package com.concesionario.model;

import com.concesionario.db.dao.VehiculoDAO;
import com.concesionario.db.dao.VehiculoDAOMemoria;
import com.concesionario.model.vehiculo.Vehiculo;

import java.util.List;
import java.util.Optional;

// Clase para gestionar los datos de los vehículos y su persistencia
public class GestorDatos {
    // DAO para acceso a datos
    private final VehiculoDAO dao;

    // Constructor que siempre usa implementación en memoria
    public GestorDatos() {
        this.dao = new VehiculoDAOMemoria();
        System.out.println("Usando implementación en memoria para desarrollo");
    }

    // Guarda un vehículo
    public boolean guardarVehiculo(Vehiculo vehiculo) {
        return dao.guardar(vehiculo);
    }

    // Guarda un conjunto de vehículos (transaccional)
    public boolean guardarConjuntoVehiculos(List<Vehiculo> vehiculos) {
        boolean todosGuardados = true;

        for (Vehiculo vehiculo : vehiculos) {
            if (!guardarVehiculo(vehiculo)) {
                todosGuardados = false;
                // Si falla uno, intentamos eliminar los que ya se guardaron
                for (Vehiculo v : vehiculos) {
                    if (!v.equals(vehiculo)) { // No intentar eliminar el que acaba de fallar
                        eliminarVehiculo(v.getMatricula());
                    }
                }
                break;
            }
        }

        return todosGuardados;
    }

    // Actualiza un vehículo existente
    public boolean actualizarVehiculo(Vehiculo vehiculo) {
        return dao.actualizar(vehiculo);
    }

    // Elimina un vehículo por su matrícula
    public boolean eliminarVehiculo(String matricula) {
        return dao.eliminar(matricula);
    }

    // Busca un vehículo por su matrícula
    public Optional<Vehiculo> buscarVehiculo(String matricula) {
        return dao.buscarPorId(matricula);
    }

    // Obtiene todos los vehículos
    public List<Vehiculo> obtenerTodosLosVehiculos() {
        return dao.obtenerTodos();
    }

    // Obtiene vehículos por tipo
    public List<Vehiculo> obtenerVehiculosPorTipo(String tipo) {
        return dao.obtenerPorTipo(tipo);
    }

    // Actualiza la posición de un vehículo en el parking
    public boolean actualizarPosicionVehiculo(String matricula, int posicionX, int posicionY) {
        return dao.actualizarPosicion(matricula, posicionX, posicionY);
    }

    // Verifica si la matrícula ya existe
    public boolean existeMatricula(String matricula) {
        return dao.existe(matricula);
    }

    // Cierra recursos (conexiones a BD, etc.)
    public void cerrar() {
        // En la implementación en memoria no hay nada que cerrar
    }
}