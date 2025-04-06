package com.concesionario.presenter.contract;

import com.concesionario.model.vehiculo.Vehiculo;
import java.util.List;
import java.util.Optional;

// Interfaz de contrato para el patrón MVP
public interface VehiculosContract {

    // Interfaz para la vista
    interface View {
        // Muestra un mensaje de éxito
        void mostrarMensajeExito(String mensaje);

        // Muestra un mensaje de error
        void mostrarMensajeError(String mensaje);

        // Actualiza la lista de vehículos
        void actualizarListaVehiculos(List<Vehiculo> vehiculos);

        // Cierra la aplicación
        void cerrarAplicacion();
    }

    // Interfaz para el presentador
    interface Presenter {
        // Crea un conjunto de vehículos (siempre 3: Coche, Tractor, Tanque)
        void crearConjuntoVehiculos();

        // Solicita todos los vehículos
        List<Vehiculo> obtenerTodosLosVehiculos();

        // Busca un vehículo por matrícula
        Optional<Vehiculo> buscarVehiculo(String matricula);

        // Actualiza un vehículo existente
        boolean actualizarVehiculo(Vehiculo vehiculo);

        // Elimina un vehículo
        boolean eliminarVehiculo(String matricula);

        // Cierra recursos y finaliza la aplicación
        void cerrar();

        boolean guardarVehiculo(Vehiculo vehiculo);

    }
}