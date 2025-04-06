package com.concesionario.view.dialogs;

import com.concesionario.model.vehiculo.Vehiculo;
import com.concesionario.util.Constants;
import com.concesionario.view.components.VehiculoForm;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

// Clase para crear diálogos con formularios de vehículos
public class VehiculoDialog extends JDialog {

    private VehiculoForm formulario;
    private boolean confirmado = false;

    // Constructor para crear un nuevo vehículo
    public VehiculoDialog(JFrame parent, String tipoVehiculo) {
        this(parent, tipoVehiculo, null);
    }

    // Constructor para editar un vehículo existente
    public VehiculoDialog(JFrame parent, String tipoVehiculo, Vehiculo vehiculo) {
        super(parent, vehiculo == null ? "Crear " + tipoVehiculo : "Editar " + tipoVehiculo, true);

        // Inicializar componentes
        initComponents(tipoVehiculo, vehiculo);

        // Configuración del diálogo
        this.setSize(500, 500);
        this.setLocationRelativeTo(parent);
        this.setResizable(false);
    }

    // Inicializa los componentes del diálogo
    private void initComponents(String tipoVehiculo, Vehiculo vehiculo) {
        // Panel principal
        JPanel panelPrincipal = new JPanel(new BorderLayout(10, 10));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Crear formulario
        formulario = new VehiculoForm(tipoVehiculo, vehiculo);

        // Panel de botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        JButton btnCancelar = new JButton("Cancelar");
        JButton btnGuardar = new JButton(vehiculo == null ? "Crear" : "Guardar");

        // Acciones de los botones
        btnCancelar.addActionListener(e -> {
            confirmado = false;
            dispose();
        });

        btnGuardar.addActionListener(e -> {
            try {
                // Validar formulario y confirmar
                if (vehiculo == null) {
                    // Creación: Simplemente verificamos que se pueden obtener los datos
                    formulario.crearVehiculo(tipoVehiculo);
                } else {
                    // Edición: Verificamos que se puede actualizar
                    formulario.actualizarVehiculo(vehiculo);
                }

                confirmado = true;
                dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        "Error en los datos: " + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        // Añadir botones al panel
        panelBotones.add(btnCancelar);
        panelBotones.add(btnGuardar);

        // Añadir componentes al panel principal
        panelPrincipal.add(formulario, BorderLayout.CENTER);
        panelPrincipal.add(panelBotones, BorderLayout.SOUTH);

        // Añadir panel principal al diálogo
        this.add(panelPrincipal);
    }

    // Obtiene el vehículo creado (null si se canceló)
    public Vehiculo getVehiculo(String tipoVehiculo) {
        if (confirmado) {
            try {
                return formulario.crearVehiculo(tipoVehiculo);
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }

    // Indica si se confirmó la operación
    public boolean isConfirmado() {
        return confirmado;
    }
}