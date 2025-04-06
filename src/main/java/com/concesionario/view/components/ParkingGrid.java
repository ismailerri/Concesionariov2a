package com.concesionario.view.components;

import com.concesionario.model.vehiculo.Coche;
import com.concesionario.model.vehiculo.Tanque;
import com.concesionario.model.vehiculo.Tractor;
import com.concesionario.model.vehiculo.Vehiculo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.function.BiConsumer;

// Componente para visualizar y gestionar el parking en Swing
public class ParkingGrid extends JPanel {

    // Tamaño del parking
    private static final int PARKING_SIZE = 9;

    // Colores para los vehículos
    private static final Color COLOR_COCHE = Color.GREEN;
    private static final Color COLOR_TRACTOR = Color.YELLOW;
    private static final Color COLOR_TANQUE = Color.RED;
    private static final Color COLOR_VACIO = Color.LIGHT_GRAY;

    // Matriz de botones para el parking
    private JButton[][] botonesParking;

    // Constructor
    public ParkingGrid() {
        // Configurar layout
        this.setLayout(new GridLayout(PARKING_SIZE, PARKING_SIZE, 2, 2));
        this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Inicializar matriz de botones
        botonesParking = new JButton[PARKING_SIZE][PARKING_SIZE];

        // Crear botones para cada celda
        for (int i = 0; i < PARKING_SIZE; i++) {
            for (int j = 0; j < PARKING_SIZE; j++) {
                JButton btn = new JButton();
                btn.setPreferredSize(new Dimension(60, 60));
                btn.setBackground(COLOR_VACIO);

                // Guardar posición como datos del botón
                btn.putClientProperty("fila", i);
                btn.putClientProperty("columna", j);

                // Guardar botón y añadir al panel
                botonesParking[i][j] = btn;
                this.add(btn);
            }
        }
    }

    // Establecer acción al hacer clic en una celda
    public void setCellClickListener(BiConsumer<Integer, Integer> clickCallback) {
        for (int i = 0; i < PARKING_SIZE; i++) {
            for (int j = 0; j < PARKING_SIZE; j++) {
                JButton btn = botonesParking[i][j];

                // Eliminar listeners anteriores
                for (ActionListener al : btn.getActionListeners()) {
                    btn.removeActionListener(al);
                }

                // Añadir nuevo listener
                final int fila = i;
                final int col = j;
                btn.addActionListener(e -> {
                    if (clickCallback != null) {
                        clickCallback.accept(col, fila);
                    }
                });
            }
        }
    }

    // Actualiza la visualización del parking con la matriz proporcionada
    public void actualizarParking(Vehiculo[][] matrizParking) {
        for (int i = 0; i < PARKING_SIZE; i++) {
            for (int j = 0; j < PARKING_SIZE; j++) {
                Vehiculo vehiculo = matrizParking[i][j];
                JButton btn = botonesParking[i][j];

                if (vehiculo != null) {
                    // Establecer color según tipo de vehículo
                    Color color = COLOR_VACIO;
                    if (vehiculo instanceof Coche) {
                        color = COLOR_COCHE;
                    } else if (vehiculo instanceof Tractor) {
                        color = COLOR_TRACTOR;
                    } else if (vehiculo instanceof Tanque) {
                        color = COLOR_TANQUE;
                    }

                    // Actualizar apariencia del botón
                    btn.setBackground(color);
                    btn.setText(vehiculo.getTipoVehiculo().substring(0, 1) + ": " +
                            vehiculo.getMatricula().substring(0, 4));
                    btn.setToolTipText(vehiculo.toString());
                } else {
                    // Plaza vacía
                    btn.setBackground(COLOR_VACIO);
                    btn.setText("[" + j + "," + i + "]");
                    btn.setToolTipText("Plaza vacía en posición [" + j + "," + i + "]");
                }
            }
        }
    }

    // Obtiene el botón en una posición específica
    public JButton getBotonAt(int x, int y) {
        if (x >= 0 && x < PARKING_SIZE && y >= 0 && y < PARKING_SIZE) {
            return botonesParking[y][x];
        }
        return null;
    }

}