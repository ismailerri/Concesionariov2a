package com.concesionario.presenter;

import com.concesionario.model.GestorDatos;
import com.concesionario.model.Parking;
import com.concesionario.model.vehiculo.Tanque;
import com.concesionario.model.vehiculo.Vehiculo;
import com.concesionario.presenter.contract.ParkingContract;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

// Implementación del presentador del parking
public class ParkingPresenter implements ParkingContract.Presenter {

    private final ParkingContract.View view;
    private final GestorDatos gestorDatos;
    private final Parking parking;

    // Constructor
    public ParkingPresenter(ParkingContract.View view) {
        this.view = view;
        this.gestorDatos = new GestorDatos();
        this.parking = new Parking();
        cargarVehiculosDesdeBaseDeDatos();
    }

    // Constructor con inyección de dependencias para testing
    public ParkingPresenter(ParkingContract.View view, GestorDatos gestorDatos, Parking parking) {
        this.view = view;
        this.gestorDatos = gestorDatos;
        this.parking = parking;
        cargarVehiculosDesdeBaseDeDatos();
    }

    // Carga los vehículos desde la base de datos al parking
    private void cargarVehiculosDesdeBaseDeDatos() {
        List<Vehiculo> vehiculos = gestorDatos.obtenerTodosLosVehiculos();
        for (Vehiculo vehiculo : vehiculos) {
            if (vehiculo.estaAparcado()) {
                parking.estacionarVehiculo(vehiculo, vehiculo.getPosicionX(), vehiculo.getPosicionY());
            }
        }
        view.actualizarVistaParking(parking.getMatrizParking());
    }

    @Override
    public Vehiculo[][] obtenerMatrizParking() {
        return parking.getMatrizParking();
    }

    @Override
    public boolean plazaOcupada(int x, int y) {
        return parking.plazaOcupada(x, y);
    }

    @Override
    public Optional<Vehiculo> obtenerVehiculoEnPosicion(int x, int y) {
        return parking.obtenerVehiculoEnPosicion(x, y);
    }

    @Override
    // Revisión del método estacionarVehiculo en ParkingPresenter.java

    public boolean estacionarVehiculo(String matricula, int x, int y) {
        System.out.println("Intentando estacionar vehículo con matrícula: " + matricula + " en [" + x + "," + y + "]");

        Optional<Vehiculo> optVehiculo = gestorDatos.buscarVehiculo(matricula);

        if (optVehiculo.isPresent()) {
            Vehiculo vehiculo = optVehiculo.get();
            System.out.println("Vehículo encontrado: " + vehiculo.getTipoVehiculo() + " " + vehiculo.getMatricula());

            if (parking.estacionarVehiculo(vehiculo, x, y)) {
                System.out.println("Vehículo estacionado en el parking correctamente");

                // Actualizar posición en bases de datos
                boolean actualizado = gestorDatos.actualizarPosicionVehiculo(matricula, x, y);

                if (!actualizado) {
                    // Si falla la actualización en BD, revertimos el estacionamiento
                    System.out.println("Error al actualizar posición en la BD, revertimos el estacionamiento");
                    parking.retirarVehiculo(matricula);
                    view.mostrarMensajeError("Error al actualizar la posición en la base de datos");
                    return false;
                }

                view.actualizarVistaParking(parking.getMatrizParking());
                view.mostrarMensajeExito("Vehículo estacionado correctamente");
                return true;
            } else {
                System.out.println("No se pudo estacionar el vehículo en esa posición");
                view.mostrarMensajeError("No se puede estacionar el vehículo en esa posición");
            }
        } else {
            System.out.println("Vehículo no encontrado con matrícula: " + matricula);
            view.mostrarMensajeError("Vehículo no encontrado");
        }

        return false;
    }

    @Override
    public boolean retirarVehiculoDeParking(String matricula) {
        Optional<Vehiculo> optVehiculo = gestorDatos.buscarVehiculo(matricula);

        if (optVehiculo.isPresent() && parking.retirarVehiculo(matricula)) {
            boolean actualizado = gestorDatos.actualizarPosicionVehiculo(matricula, -1, -1);

            if (!actualizado) {
                view.mostrarMensajeError("Error al actualizar la posición en la base de datos");
                return false;
            }

            view.actualizarVistaParking(parking.getMatrizParking());
            view.mostrarMensajeExito("Vehículo retirado del parking correctamente");
            return true;
        }

        view.mostrarMensajeError("No se pudo retirar el vehículo del parking");
        return false;
    }

    @Override
    public boolean dispararTanque(String matriculaTanque, Tanque.Direccion direccion) {
        // Verificar que el vehículo existe y es un tanque
        Optional<Vehiculo> optVehiculo = gestorDatos.buscarVehiculo(matriculaTanque);
        if (!optVehiculo.isPresent() || !(optVehiculo.get() instanceof Tanque)) {
            view.mostrarMensajeError("El vehículo no es un tanque o no existe");
            return false;
        }

        Tanque tanque = (Tanque) optVehiculo.get();
        tanque.apuntarCanon(direccion);

        if (!tanque.isArmamentoCargado()) {
            view.mostrarMensajeError("El armamento del tanque no está cargado");
            return false;
        }

        // Procesar el disparo
        String matriculaObjetivo = parking.procesarDisparoTanque(matriculaTanque);

        // Si hay un objetivo válido, eliminarlo
        if (matriculaObjetivo != null &&
                !matriculaObjetivo.equals("No hay ningún vehículo en esa dirección para disparar") &&
                !matriculaObjetivo.equals("El armamento no está cargado") &&
                !matriculaObjetivo.equals("El tanque no está en el parking, no puede disparar")) {

            boolean eliminado = gestorDatos.eliminarVehiculo(matriculaObjetivo);

            if (eliminado) {
                view.actualizarVistaParking(parking.getMatrizParking());
                view.mostrarMensajeExito("¡Disparo exitoso! Vehículo eliminado");
                return true;
            } else {
                view.mostrarMensajeError("No se pudo eliminar el objetivo");
                return false;
            }
        } else if (matriculaObjetivo != null) {
            view.mostrarMensajeError(matriculaObjetivo);
        } else {
            view.mostrarMensajeError("Error al procesar el disparo");
        }

        return false;
    }

    @Override
    public List<Vehiculo> obtenerVehiculosNoAparcados() {
        return gestorDatos.obtenerTodosLosVehiculos().stream()
                .filter(v -> !v.estaAparcado())
                .collect(Collectors.toList());
    }
}
