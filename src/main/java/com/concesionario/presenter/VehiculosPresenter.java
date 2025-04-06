package com.concesionario.presenter;

import com.concesionario.model.FabricaVehiculos;
import com.concesionario.model.GestorDatos;
import com.concesionario.model.vehiculo.Vehiculo;
import com.concesionario.presenter.contract.VehiculosContract;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

// Implementación del presentador de vehículos
public class VehiculosPresenter implements VehiculosContract.Presenter {

    private final VehiculosContract.View view;
    private final GestorDatos gestorDatos;

    // Constructor
    public VehiculosPresenter(VehiculosContract.View view) {
        this.view = view;
        this.gestorDatos = new GestorDatos();
    }

    // Constructor con inyección de dependencias para testing
    public VehiculosPresenter(VehiculosContract.View view, GestorDatos gestorDatos) {
        this.view = view;
        this.gestorDatos = gestorDatos;
    }

    @Override
    public void crearConjuntoVehiculos() {
        List<Vehiculo> nuevosVehiculos = FabricaVehiculos.crearConjuntoVehiculos();

        if (gestorDatos.guardarConjuntoVehiculos(nuevosVehiculos)) {
            view.mostrarMensajeExito("Se han creado 3 vehículos correctamente");
            view.actualizarListaVehiculos(gestorDatos.obtenerTodosLosVehiculos());
        } else {
            view.mostrarMensajeError("Error al crear los vehículos");
        }
    }

    @Override
    public List<Vehiculo> obtenerTodosLosVehiculos() {
        return gestorDatos.obtenerTodosLosVehiculos();
    }

    @Override
    public Optional<Vehiculo> buscarVehiculo(String matricula) {
        return gestorDatos.buscarVehiculo(matricula);
    }

    @Override
    public boolean actualizarVehiculo(Vehiculo vehiculo) {
        boolean resultado = gestorDatos.actualizarVehiculo(vehiculo);
        if (resultado) {
            view.mostrarMensajeExito("Vehículo actualizado correctamente");
            view.actualizarListaVehiculos(gestorDatos.obtenerTodosLosVehiculos());
        } else {
            view.mostrarMensajeError("Error al actualizar el vehículo");
        }
        return resultado;
    }

    @Override
    public boolean eliminarVehiculo(String matricula) {
        boolean resultado = gestorDatos.eliminarVehiculo(matricula);
        if (resultado) {
            view.mostrarMensajeExito("Vehículo eliminado correctamente");
            view.actualizarListaVehiculos(gestorDatos.obtenerTodosLosVehiculos());
        } else {
            view.mostrarMensajeError("Error al eliminar el vehículo");
        }
        return resultado;
    }

    @Override
    public void cerrar() {
        gestorDatos.cerrar();
        view.cerrarAplicacion();
    }

    public boolean guardarVehiculo(Vehiculo vehiculo) {
        boolean resultado = gestorDatos.guardarVehiculo(vehiculo);
        if (resultado) {
            view.actualizarListaVehiculos(gestorDatos.obtenerTodosLosVehiculos());
        }
        return resultado;
    }

    public List<Vehiculo> obtenerVehiculosNoAparcados() {
        // Obtenemos todos los vehículos desde el gestor de datos
        List<Vehiculo> todosLosVehiculos = gestorDatos.obtenerTodosLosVehiculos();

        // Filtramos aquellos que no están aparcados
        return todosLosVehiculos.stream()
                .filter(vehiculo -> !vehiculo.estaAparcado())
                .collect(Collectors.toList());
    }


}
