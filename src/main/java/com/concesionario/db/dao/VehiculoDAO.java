package com.concesionario.db.dao;

import com.concesionario.model.vehiculo.Vehiculo;
import java.util.List;
import java.util.Optional;

// Interfaz para el Data Access Object de vehículos
public interface VehiculoDAO {
    // Guardar un nuevo vehículo
    boolean guardar(Vehiculo vehiculo);

    // Actualizar un vehículo existente
    boolean actualizar(Vehiculo vehiculo);

    // Eliminar un vehículo por su matrícula
    boolean eliminar(String matricula);

    // Buscar un vehículo por su matrícula
    Optional<Vehiculo> buscarPorId(String matricula);

    // Obtener todos los vehículos
    List<Vehiculo> obtenerTodos();

    // Obtener vehículos por tipo (Coche, Tractor, Tanque)
    List<Vehiculo> obtenerPorTipo(String tipo);

    // Verificar si existe un vehículo con la matrícula dada
    boolean existe(String matricula);

    // Actualizar la posición de un vehículo en el parking
    boolean actualizarPosicion(String matricula, int posicionX, int posicionY);
}
