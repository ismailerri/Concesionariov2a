package com.concesionario.view.components;

import com.concesionario.model.vehiculo.Vehiculo;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

// Componente para mostrar vehículos en una tabla
public class VehiculoTable extends JPanel {

    private JTable tabla;
    private DefaultTableModel modeloTabla;

    // Constructor
    public VehiculoTable() {
        this.setLayout(new BorderLayout());

        // Crear modelo de tabla
        String[] columnas = {"Matrícula", "Tipo", "Marca", "Potencia", "Año", "Precio (€)", "Posición"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // No permitir edición directa
            }
        };

        // Crear tabla con el modelo
        tabla = new JTable(modeloTabla);
        tabla.setRowHeight(25);
        tabla.setFont(new Font("Arial", Font.PLAIN, 14));
        tabla.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));

        // Añadir tabla en un scroll pane
        JScrollPane scrollPane = new JScrollPane(tabla);
        this.add(scrollPane, BorderLayout.CENTER);
    }

    // Actualiza los datos de la tabla
    public void actualizarDatos(List<Vehiculo> vehiculos) {
        // Limpiar tabla
        modeloTabla.setRowCount(0);

        // Añadir vehículos
        for (Vehiculo v : vehiculos) {
            String posicion = v.estaAparcado() ?
                    "[" + v.getPosicionX() + "," + v.getPosicionY() + "]" : "No estacionado";

            modeloTabla.addRow(new Object[]{
                    v.getMatricula(),
                    v.getTipoVehiculo(),
                    v.getMarca(),
                    v.getPotenciaMotor() + " CV",
                    v.getAnioFabricacion(),
                    String.format("%.2f", v.getPrecio()),
                    posicion
            });
        }
    }

    // Obtiene el vehículo seleccionado en base a su matrícula
    public String getSelectedMatricula() {
        int selectedRow = tabla.getSelectedRow();
        if (selectedRow >= 0) {
            return (String) tabla.getValueAt(selectedRow, 0); // La matrícula está en la columna 0
        }
        return null;
    }

    // Establece una selección por matrícula
    public void seleccionarMatricula(String matricula) {
        for (int i = 0; i < tabla.getRowCount(); i++) {
            if (matricula.equals(tabla.getValueAt(i, 0))) {
                tabla.setRowSelectionInterval(i, i);
                return;
            }
        }
    }

    // Obtiene la tabla para configuraciones adicionales
    public JTable getTabla() {
        return tabla;
    }

    // Obtiene el modelo de la tabla
    public DefaultTableModel getModeloTabla() {
        return modeloTabla;
    }
}