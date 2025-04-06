package com.concesionario;

import com.concesionario.view.MainView;

import javax.swing.*;

// Clase principal que inicia la aplicación
public class Main {

    public static void main(String[] args) {
        System.out.println("Iniciando sistema de gestión de concesionario...");

        // Iniciar con interfaz gráfica
        SwingUtilities.invokeLater(() -> {
            try {
                // Intentar usar el look and feel del sistema
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                // No inicializar bases de datos en modo desarrollo
                // new DBInitializer();

                // Crear y mostrar la vista principal
                MainView mainView = new MainView();
                mainView.mostrar();
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null,
                        "Error al iniciar la aplicación: " + e.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                System.exit(1);
            }
        });
    }
}