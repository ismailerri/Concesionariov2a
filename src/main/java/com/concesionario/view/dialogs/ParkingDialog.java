package com.concesionario.view.dialogs;

import com.concesionario.model.vehiculo.Tanque;
import com.concesionario.model.vehiculo.Vehiculo;
import com.concesionario.presenter.ParkingPresenter;
import com.concesionario.view.components.ParkingGrid;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

// Diálogo para gestionar el parking según requisitos del PDF
public class ParkingDialog extends JDialog {

    private ParkingGrid parkingGrid;
    private ParkingPresenter presenter;

    // Constructor
    public ParkingDialog(JFrame parent, ParkingPresenter presenter) {
        super(parent, "Gestión de Parking", true);
        this.presenter = presenter;

        // Inicializar componentes
        initComponents();

        // Configurar diálogo
        this.setSize(800, 700);
        this.setLocationRelativeTo(parent);
        this.setResizable(true);
    }

    // Inicializa los componentes del diálogo
    private void initComponents() {
        // Panel principal
        JPanel panelPrincipal = new JPanel(new BorderLayout(10, 10));
        panelPrincipal.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Panel de información
        JPanel panelInfo = new JPanel(new BorderLayout());
        JLabel lblInfo = new JLabel("Haz clic en una plaza para ver opciones");
        panelInfo.add(lblInfo, BorderLayout.WEST);

        // Leyenda
        JPanel panelLeyenda = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));

        // Coche (C)
        JPanel panelCoche = new JPanel();
        panelCoche.setBackground(Color.GREEN);
        panelCoche.setPreferredSize(new Dimension(20, 20));
        panelLeyenda.add(panelCoche);
        panelLeyenda.add(new JLabel("C (Coche)"));

        // Tractor (T)
        JPanel panelTractor = new JPanel();
        panelTractor.setBackground(Color.YELLOW);
        panelTractor.setPreferredSize(new Dimension(20, 20));
        panelLeyenda.add(panelTractor);
        panelLeyenda.add(new JLabel("T (Tractor)"));

        // Tanque (M)
        JPanel panelTanque = new JPanel();
        panelTanque.setBackground(Color.RED);
        panelTanque.setPreferredSize(new Dimension(20, 20));
        panelLeyenda.add(panelTanque);
        panelLeyenda.add(new JLabel("M (Militar)"));

        // Vacío (-)
        JPanel panelVacio = new JPanel();
        panelVacio.setBackground(Color.LIGHT_GRAY);
        panelVacio.setPreferredSize(new Dimension(20, 20));
        panelLeyenda.add(panelVacio);
        panelLeyenda.add(new JLabel("- (Vacío)"));

        // Añadir leyenda al panel de información
        panelInfo.add(panelLeyenda, BorderLayout.EAST);

        // Grid del parking
        parkingGrid = new ParkingGrid();

        // Configurar callback para clics en celdas
        parkingGrid.setCellClickListener((x, y) -> {
            if (presenter.plazaOcupada(x, y)) {
                // Plaza ocupada: mostrar opciones para el vehículo
                Optional<Vehiculo> optVehiculo = presenter.obtenerVehiculoEnPosicion(x, y);
                if (optVehiculo.isPresent()) {
                    mostrarOpcionesVehiculoParking(optVehiculo.get());
                }
            } else {
                // Plaza vacía: mostrar opciones para aparcar
                mostrarOpcionesPlazaVacia(x, y);
            }
        });

        // Actualizar grid con datos del parking
        parkingGrid.actualizarParking(presenter.obtenerMatrizParking());

        // Panel de botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.addActionListener(e -> dispose());
        panelBotones.add(btnCerrar);

        // Añadir componentes al panel principal
        panelPrincipal.add(panelInfo, BorderLayout.NORTH);
        panelPrincipal.add(parkingGrid, BorderLayout.CENTER);
        panelPrincipal.add(panelBotones, BorderLayout.SOUTH);

        // Añadir panel principal al diálogo
        this.add(panelPrincipal);
    }

    // Muestra opciones para un vehículo estacionado
    private void mostrarOpcionesVehiculoParking(Vehiculo vehiculo) {
        // Crear opciones según el tipo de vehículo
        List<String> opciones = new ArrayList<>();
        opciones.add("Eliminar vehículo"); // Según PDF: plaza ocupada -> eliminar vehículo
        opciones.add("Mover vehículo"); // Permitir mover a otras coordenadas

        // Si es un tanque, añadir opciones específicas
        if (vehiculo instanceof Tanque) {
            Tanque tanque = (Tanque) vehiculo;
            opciones.add("Cargar armamento");
            if (tanque.isArmamentoCargado()) {
                opciones.add("Disparar");
            }
        }

        // Convertir a array
        String[] opcionesArray = opciones.toArray(new String[0]);

        // Mostrar menú de opciones
        String seleccion = (String) JOptionPane.showInputDialog(
                this,
                vehiculo.getTipoVehiculo() + ": " + vehiculo.getMarca() + " (" + vehiculo.getMatricula() + ")",
                "Opciones de vehículo",
                JOptionPane.QUESTION_MESSAGE,
                null,
                opcionesArray,
                opcionesArray[0]
        );

        // Procesar selección
        if (seleccion != null) {
            switch (seleccion) {
                case "Eliminar vehículo":
                    if (presenter.retirarVehiculoDeParking(vehiculo.getMatricula())) {
                        JOptionPane.showMessageDialog(this,
                                "Vehículo retirado correctamente",
                                "Éxito",
                                JOptionPane.INFORMATION_MESSAGE);
                        parkingGrid.actualizarParking(presenter.obtenerMatrizParking());
                    }
                    break;
                case "Mover vehículo":
                    mostrarDialogoMoverVehiculo(vehiculo);
                    break;
                case "Cargar armamento":
                    if (vehiculo instanceof Tanque) {
                        ((Tanque) vehiculo).cargarArmamento();
                        JOptionPane.showMessageDialog(this,
                                "Armamento cargado correctamente",
                                "Éxito",
                                JOptionPane.INFORMATION_MESSAGE);
                    }
                    break;
                case "Disparar":
                    if (vehiculo instanceof Tanque) {
                        mostrarDialogoDispararTanque((Tanque) vehiculo);
                    }
                    break;
            }
        }
    }

    // Diálogo para mover un vehículo a nuevas coordenadas
    private void mostrarDialogoMoverVehiculo(Vehiculo vehiculo) {
        // Panel para introducir nuevas coordenadas
        JPanel panel = new JPanel(new GridLayout(2, 2, 5, 5));

        // Campos para coordenadas X e Y
        JSpinner spinnerX = new JSpinner(new SpinnerNumberModel(0, 0, 8, 1));
        JSpinner spinnerY = new JSpinner(new SpinnerNumberModel(0, 0, 8, 1));

        panel.add(new JLabel("Coordenada X (0-8):"));
        panel.add(spinnerX);
        panel.add(new JLabel("Coordenada Y (0-8):"));
        panel.add(spinnerY);

        // Mostrar diálogo
        int resultado = JOptionPane.showConfirmDialog(this, panel,
                "Mover " + vehiculo.getTipoVehiculo() + " " + vehiculo.getMatricula(),
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (resultado == JOptionPane.OK_OPTION) {
            int nuevaX = (Integer) spinnerX.getValue();
            int nuevaY = (Integer) spinnerY.getValue();

            // Verificar si la plaza está ocupada
            if (presenter.plazaOcupada(nuevaX, nuevaY)) {
                JOptionPane.showMessageDialog(this,
                        "La plaza seleccionada ya está ocupada",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Retirar del parking primero
            presenter.retirarVehiculoDeParking(vehiculo.getMatricula());

            // Estacionar en la nueva posición
            if (presenter.estacionarVehiculo(vehiculo.getMatricula(), nuevaX, nuevaY)) {
                JOptionPane.showMessageDialog(this,
                        "Vehículo movido correctamente",
                        "Éxito",
                        JOptionPane.INFORMATION_MESSAGE);
                parkingGrid.actualizarParking(presenter.obtenerMatrizParking());
            } else {
                JOptionPane.showMessageDialog(this,
                        "Error al mover el vehículo",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Mostrar diálogo para elegir dirección de disparo
    private void mostrarDialogoDispararTanque(Tanque tanque) {
        // Opciones de dirección según el PDF
        String[] direcciones = {"Delante", "Atrás", "Izquierda", "Derecha"};

        // Mostrar selector
        String seleccion = (String) JOptionPane.showInputDialog(
                this,
                "Seleccione dirección de disparo",
                "Disparar tanque",
                JOptionPane.QUESTION_MESSAGE,
                null,
                direcciones,
                direcciones[0]
        );

        // Procesar selección
        if (seleccion != null) {
            Tanque.Direccion direccion;
            switch (seleccion) {
                case "Delante":
                    direccion = Tanque.Direccion.DELANTE;
                    break;
                case "Atrás":
                    direccion = Tanque.Direccion.ATRAS;
                    break;
                case "Izquierda":
                    direccion = Tanque.Direccion.IZQUIERDA;
                    break;
                case "Derecha":
                    direccion = Tanque.Direccion.DERECHA;
                    break;
                default:
                    return;
            }

            // Ejecutar disparo
            tanque.apuntarCanon(direccion);
            boolean resultado = presenter.dispararTanque(tanque.getMatricula(), direccion);

            if (resultado) {
                JOptionPane.showMessageDialog(this,
                        "¡Disparo exitoso! El vehículo objetivo ha sido eliminado",
                        "Disparo exitoso",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                        "No hay ningún vehículo en esa dirección para disparar o el disparo falló",
                        "Disparo fallido",
                        JOptionPane.WARNING_MESSAGE);
            }

            // Actualizar vista del parking
            parkingGrid.actualizarParking(presenter.obtenerMatrizParking());
        }
    }

    // Muestra opciones para una plaza vacía
    // Añadir al principio del método mostrarOpcionesPlazaVacia en ParkingDialog.java:

    private void mostrarOpcionesPlazaVacia(int x, int y) {
        System.out.println("Mostrando opciones para plaza vacía en [" + x + "," + y + "]");

        // Según PDF: plaza vacía -> añadir vehículo
        List<Vehiculo> vehiculosDisponibles = presenter.obtenerVehiculosNoAparcados();
        System.out.println("Vehículos disponibles: " + vehiculosDisponibles.size());

        // Imprimir los vehículos disponibles para debug
        for (Vehiculo v : vehiculosDisponibles) {
            System.out.println("  - " + v.getTipoVehiculo() + ": " + v.getMarca() +
                    " (" + v.getMatricula() + "), aparcado: " + v.estaAparcado());
        }

        if (vehiculosDisponibles.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "No hay vehículos disponibles para aparcar",
                    "Información",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // Resto del método igual...
    }

    // Actualiza la vista del parking
    public void actualizarVistaParking() {
        if (parkingGrid != null) {
            parkingGrid.actualizarParking(presenter.obtenerMatrizParking());
        }
    }
}