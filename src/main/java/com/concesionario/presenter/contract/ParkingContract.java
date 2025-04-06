package com.concesionario.presenter.contract;

import com.concesionario.model.vehiculo.Tanque;
import com.concesionario.model.vehiculo.Vehiculo;

import java.util.List;
import java.util.Optional;

// Interfaz de contrato para el patrón MVP relativo al parking
public interface ParkingContract {

    // Interfaz para la vista
    interface View {
        // Actualiza la visualización del parking
        void actualizarVistaParking(Vehiculo[][] matrizParking);

        // Muestra un mensaje de éxito
        void mostrarMensajeExito(String mensaje);

        // Muestra un mensaje de error
        void mostrarMensajeError(String mensaje);
    }

    // Interfaz para el presentador
    interface Presenter {
        // Obtiene la matriz del parking
        Vehiculo[][] obtenerMatrizParking();

        // Comprueba si una plaza está ocupada
        boolean plazaOcupada(int x, int y);

        // Obtiene el vehículo en una posición
        Optional<Vehiculo> obtenerVehiculoEnPosicion(int x, int y);

        // Estaciona un vehículo en una posición
        boolean estacionarVehiculo(String matricula, int x, int y);

        // Retira un vehículo del parking
        boolean retirarVehiculoDeParking(String matricula);

        // Dispara un tanque y elimina el vehículo impactado
        boolean dispararTanque(String matriculaTanque, Tanque.Direccion direccion);

        // Obtiene vehículos no aparcados
        List<Vehiculo> obtenerVehiculosNoAparcados();
    }
}