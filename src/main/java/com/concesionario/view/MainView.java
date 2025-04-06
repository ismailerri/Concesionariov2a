package com.concesionario.view;

import com.concesionario.model.vehiculo.Coche;
import com.concesionario.model.vehiculo.Tanque;
import com.concesionario.model.vehiculo.Tractor;
import com.concesionario.model.vehiculo.Vehiculo;
import com.concesionario.presenter.ParkingPresenter;
import com.concesionario.presenter.VehiculosPresenter;
import com.concesionario.presenter.contract.ParkingContract;
import com.concesionario.presenter.contract.VehiculosContract;
import com.concesionario.view.dialogs.CrearVehiculoDialog;
import com.concesionario.util.Constants;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

// Vista principal de la aplicación usando Swing
public class MainView extends JFrame implements VehiculosContract.View, ParkingContract.View {

    // Presentadores (patrón MVP)
    private VehiculosPresenter vehiculosPresenter;
    private ParkingPresenter parkingPresenter;

    // Componentes de la interfaz
    private JTable tablaVehiculos;
    private DefaultTableModel modeloTabla;
    private JPanel panelParking;
    private JButton[][] botonesParking;
    private JButton btnCrearVehiculos;

    // Definir colores para la nueva interfaz
    private static final Color COLOR_BOTON = new Color(59, 153, 252);  // Color azul para botones
    private static final Color COLOR_FONDO = new Color(245, 245, 245);  // Color gris claro para el fondo
    private static final Font FUENTE_TITULO = new Font("Arial", Font.BOLD, 28);
    private static final Font FUENTE_TEXTO = new Font("Arial", Font.PLAIN, 16);
    private static final Font FUENTE_BOTON = new Font("Arial", Font.BOLD, 18);

    // Constructor
    public MainView() {
        this.vehiculosPresenter = new VehiculosPresenter(this);
        this.parkingPresenter = new ParkingPresenter(this);

        // Configuración básica de la ventana
        setTitle("Gestor de Concesionario");
        setSize(950, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(COLOR_FONDO);

        // Agregar evento de cierre para liberar recursos
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                vehiculosPresenter.cerrar();
            }
        });

        // Inicializar componentes de la interfaz
        inicializarComponentes();

        // Cargar datos iniciales
        actualizarListaVehiculos(vehiculosPresenter.obtenerTodosLosVehiculos());
        actualizarVistaParking(parkingPresenter.obtenerMatrizParking());

        // Verificar si hay vehículos para habilitar/deshabilitar el botón de crear
        verificarBotonCrearVehiculos();
    }

    private void inicializarComponentes() {
        // Panel principal
        JPanel panelPrincipal = new JPanel();
        panelPrincipal.setLayout(new BorderLayout());
        panelPrincipal.setBackground(COLOR_FONDO);

        // Panel de menú principal
        JPanel panelMenu = new JPanel();
        panelMenu.setLayout(new BoxLayout(panelMenu, BoxLayout.Y_AXIS));
        panelMenu.setBackground(COLOR_FONDO);
        panelMenu.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));

        // Título
        JLabel lblTitulo = new JLabel("Bienvenido al concesionario de vehículos");
        lblTitulo.setFont(FUENTE_TITULO);
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Texto descriptivo
        JTextArea txtDescripcion = new JTextArea(
                "Sistema de gestión integral para concesionarios de vehículos. " +
                        "Esta aplicación permite administrar un inventario de diferentes tipos de vehículos " +
                        "(coches, tractores y tanques militares), organizar su disposición en el parking " +
                        "y realizar operaciones básicas como añadir, mostrar, actualizar y eliminar vehículos. " +
                        "El sistema utiliza un patrón MVC y ofrece la posibilidad de persistencia de datos " +
                        "tanto en memoria como en bases de datos SQL y MongoDB."
        );
        txtDescripcion.setFont(FUENTE_TEXTO);
        txtDescripcion.setLineWrap(true);
        txtDescripcion.setWrapStyleWord(true);
        txtDescripcion.setOpaque(false);
        txtDescripcion.setEditable(false);
        txtDescripcion.setAlignmentX(Component.CENTER_ALIGNMENT);
        txtDescripcion.setBorder(BorderFactory.createEmptyBorder(20, 0, 30, 0));

        // Botones principales
        btnCrearVehiculos = crearBoton("Añadir vehículos");
        JButton btnMostrarVehiculos = crearBoton("Mostrar vehículos");
        JButton btnActualizarVehiculos = crearBoton("Actualizar vehículos");
        JButton btnEliminarVehiculos = crearBoton("Eliminar vehículos");
        JButton btnVerParking = crearBoton("Ver parking");
        JButton btnSalir = crearBoton("Salir del programa");

        // Acciones de los botones
        btnCrearVehiculos.addActionListener(e -> mostrarDialogoCrearVehiculos());
        btnMostrarVehiculos.addActionListener(e -> mostrarDialogoListaVehiculos());
        btnActualizarVehiculos.addActionListener(e -> mostrarDialogoActualizarVehiculo());
        btnEliminarVehiculos.addActionListener(e -> mostrarDialogoEliminarVehiculo());
        btnVerParking.addActionListener(e -> mostrarDialogoVerParking());
        btnSalir.addActionListener(e -> {
            vehiculosPresenter.cerrar();
            System.exit(0);
        });

        // Agregar componentes al panel de menú
        panelMenu.add(lblTitulo);
        panelMenu.add(txtDescripcion);
        panelMenu.add(Box.createRigidArea(new Dimension(0, 20)));
        panelMenu.add(btnCrearVehiculos);
        panelMenu.add(Box.createRigidArea(new Dimension(0, 15)));
        panelMenu.add(btnMostrarVehiculos);
        panelMenu.add(Box.createRigidArea(new Dimension(0, 15)));
        panelMenu.add(btnActualizarVehiculos);
        panelMenu.add(Box.createRigidArea(new Dimension(0, 15)));
        panelMenu.add(btnEliminarVehiculos);
        panelMenu.add(Box.createRigidArea(new Dimension(0, 15)));
        panelMenu.add(btnVerParking);
        panelMenu.add(Box.createRigidArea(new Dimension(0, 15)));
        panelMenu.add(btnSalir);

        // Añadir el panel de menú al panel principal
        panelPrincipal.add(new JScrollPane(panelMenu), BorderLayout.CENTER);

        // Añadir el panel principal a la ventana
        getContentPane().add(panelPrincipal);
    }

    private JButton crearBoton(String texto) {
        JButton boton = new JButton(texto);
        boton.setFont(FUENTE_BOTON);
        boton.setBackground(COLOR_BOTON);
        boton.setForeground(Color.BLACK);
        boton.setFocusPainted(false);
        boton.setAlignmentX(Component.CENTER_ALIGNMENT);
        boton.setMaximumSize(new Dimension(500, 60));
        boton.setPreferredSize(new Dimension(500, 60));

        // Bordes redondeados
        boton.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        return boton;
    }

    // Método para mostrar diálogo de creación de vehículos

    private void mostrarDialogoCrearVehiculos() {
        // Ya no verificamos el número de vehículos aquí
        // Siempre permitimos crear un nuevo conjunto de 3 vehículos

        // Mostrar diálogo de creación manual
        CrearVehiculoDialog dialog = new CrearVehiculoDialog(this, vehiculosPresenter);
        dialog.setVisible(true);

        // Procesar resultado
        if (dialog.isConfirmado()) {
            List<Vehiculo> nuevosVehiculos = dialog.getVehiculosCreados();

            if (nuevosVehiculos != null && nuevosVehiculos.size() == 3) {
                StringBuilder mensaje = new StringBuilder("Se han creado los siguientes vehículos:\n\n");

                // Guardar los vehículos
                boolean todosGuardados = true;
                for (Vehiculo v : nuevosVehiculos) {
                    mensaje.append("- ").append(v.getTipoVehiculo()).append(": ")
                            .append(v.getMarca())
                            .append(" (").append(v.getMatricula()).append(")\n");

                    // Guardar cada vehículo
                    if (!vehiculosPresenter.guardarVehiculo(v)) {
                        todosGuardados = false;
                        break;
                    }
                }

                if (todosGuardados) {
                    JOptionPane.showMessageDialog(this,
                            mensaje.toString(),
                            "Vehículos Creados",
                            JOptionPane.INFORMATION_MESSAGE);

                    actualizarListaVehiculos(vehiculosPresenter.obtenerTodosLosVehiculos());
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Error al guardar los vehículos",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    // Método para mostrar lista de vehículos
    private void mostrarDialogoListaVehiculos() {
        // Crear diálogo modal
        JDialog dialogo = new JDialog(this, "Lista de Vehículos", true);
        dialogo.setSize(800, 500);
        dialogo.setLocationRelativeTo(this);

        // Panel principal del diálogo
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));
        panel.setBackground(COLOR_FONDO);

        // Tabla de vehículos
        String[] columnas = {"Matrícula", "Tipo", "Marca", "Potencia", "Año", "Precio (€)", "Posición"};
        DefaultTableModel modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        // Llenar la tabla con los vehículos
        List<Vehiculo> vehiculos = vehiculosPresenter.obtenerTodosLosVehiculos();
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

        // Crear la tabla
        JTable tabla = new JTable(modeloTabla);
        tabla.setRowHeight(25);
        tabla.setFont(new Font("Arial", Font.PLAIN, 14));
        tabla.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));

        // Botón para cerrar el diálogo
        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.setFont(new Font("Arial", Font.BOLD, 14));
        btnCerrar.addActionListener(e -> dialogo.dispose());

        // Panel para el botón
        JPanel panelBoton = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelBoton.setBackground(COLOR_FONDO);
        panelBoton.add(btnCerrar);

        // Añadir componentes
        panel.add(new JScrollPane(tabla), BorderLayout.CENTER);
        panel.add(panelBoton, BorderLayout.SOUTH);

        // Añadir panel al diálogo
        dialogo.add(panel);
        dialogo.setVisible(true);
    }

    // Método para mostrar diálogo de actualización de vehículo
    private void mostrarDialogoActualizarVehiculo() {
        // Obtener todos los vehículos
        List<Vehiculo> vehiculos = vehiculosPresenter.obtenerTodosLosVehiculos();

        if (vehiculos.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "No hay vehículos en el sistema",
                    "Actualizar Vehículo",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // Array para el combobox
        String[] opciones = new String[vehiculos.size()];
        for (int i = 0; i < vehiculos.size(); i++) {
            Vehiculo v = vehiculos.get(i);
            opciones[i] = v.getTipoVehiculo() + ": " + v.getMarca() +
                    " (Matrícula: " + v.getMatricula() + ")";
        }

        // Pedir al usuario que elija un vehículo
        String seleccion = (String) JOptionPane.showInputDialog(this,
                "Seleccione el vehículo a actualizar:",
                "Actualizar Vehículo",
                JOptionPane.QUESTION_MESSAGE,
                null,
                opciones,
                opciones[0]);

        if (seleccion == null) return;

        // Obtener la matrícula del vehículo seleccionado
        String matricula = seleccion.substring(seleccion.lastIndexOf("Matrícula: ") + 11, seleccion.length() - 1);
        Optional<Vehiculo> optVehiculo = vehiculosPresenter.buscarVehiculo(matricula);

        if (!optVehiculo.isPresent()) {
            JOptionPane.showMessageDialog(this,
                    "Vehículo no encontrado",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        Vehiculo vehiculo = optVehiculo.get();

        // Crear panel con campos para actualizar
        JPanel panel = new JPanel(new GridLayout(0, 2, 10, 10));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Campos comunes a todos los vehículos
        JTextField txtMarca = new JTextField(vehiculo.getMarca());
        // No hay modelo disponible
        JTextField txtPotencia = new JTextField(String.valueOf(vehiculo.getPotenciaMotor()));
        JTextField txtAnio = new JTextField(String.valueOf(vehiculo.getAnioFabricacion()));
        JTextField txtPrecio = new JTextField(String.valueOf(vehiculo.getPrecio()));

        panel.add(new JLabel("Matrícula:"));
        panel.add(new JLabel(vehiculo.getMatricula()));
        panel.add(new JLabel("Tipo:"));
        panel.add(new JLabel(vehiculo.getTipoVehiculo()));
        panel.add(new JLabel("Marca:"));
        panel.add(txtMarca);
        panel.add(new JLabel("Potencia (CV):"));
        panel.add(txtPotencia);
        panel.add(new JLabel("Año Fabricación:"));
        panel.add(txtAnio);
        panel.add(new JLabel("Precio (€):"));
        panel.add(txtPrecio);

        // No mostramos campos específicos según el tipo de vehículo
        // Solo permitimos editar los campos comunes a todos los vehículos

        // Mostrar diálogo
        int resultado = JOptionPane.showConfirmDialog(this,
                panel,
                "Actualizar Vehículo",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);

        if (resultado != JOptionPane.OK_OPTION) return;

        try {
            // Actualizar campos comunes
            vehiculo.setMarca(txtMarca.getText());
            // No actualizamos modelo porque no existe
            vehiculo.setPotenciaMotor(Integer.parseInt(txtPotencia.getText()));
            vehiculo.setAnioFabricacion(Integer.parseInt(txtAnio.getText()));
            vehiculo.setPrecio(Double.parseDouble(txtPrecio.getText()));

            // No se actualizan los campos específicos de cada tipo de vehículo
            // Solo se actualizan los campos comunes a todos los vehículos

            // Actualizar en el controlador
            vehiculosPresenter.actualizarVehiculo(vehiculo);

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "Error en el formato de los datos numéricos",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this,
                    e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    // Método para mostrar diálogo de eliminación de vehículo
    private void mostrarDialogoEliminarVehiculo() {
        // Obtener todos los vehículos
        List<Vehiculo> vehiculos = vehiculosPresenter.obtenerTodosLosVehiculos();

        if (vehiculos.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "No hay vehículos en el sistema",
                    "Eliminar Vehículo",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // Array para el combobox
        String[] opciones = new String[vehiculos.size()];
        for (int i = 0; i < vehiculos.size(); i++) {
            Vehiculo v = vehiculos.get(i);
            opciones[i] = v.getTipoVehiculo() + ": " + v.getMarca() +
                    " (Matrícula: " + v.getMatricula() + ")";
        }

        // Pedir al usuario que elija un vehículo
        String seleccion = (String) JOptionPane.showInputDialog(this,
                "Seleccione el vehículo a eliminar:",
                "Eliminar Vehículo",
                JOptionPane.QUESTION_MESSAGE,
                null,
                opciones,
                opciones[0]);

        if (seleccion == null) return;

        // Obtener la matrícula del vehículo seleccionado
        String matricula = seleccion.substring(seleccion.lastIndexOf("Matrícula: ") + 11, seleccion.length() - 1);

        // Confirmar eliminación
        int confirmacion = JOptionPane.showConfirmDialog(this,
                "¿Está seguro de que desea eliminar el vehículo con matrícula " + matricula + "?",
                "Confirmar Eliminación",
                JOptionPane.YES_NO_OPTION);

        if (confirmacion != JOptionPane.YES_OPTION) return;

        // Eliminar vehículo
        vehiculosPresenter.eliminarVehiculo(matricula);
    }

    // Método para mostrar el panel del parking
    private void mostrarDialogoVerParking() {
        // Crear diálogo modal
        JDialog dialogo = new JDialog(this, "Gestión de Parking", true);
        dialogo.setSize(800, 700);
        dialogo.setLocationRelativeTo(this);

        // Panel principal del diálogo
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));
        panel.setBackground(COLOR_FONDO);

        // Panel de la matriz del parking (9x9)
        JPanel panelParking = new JPanel(new GridLayout(9, 9, 2, 2));

        // Crear botones
        JButton[][] botonesParking = new JButton[9][9];
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                final int fila = i;
                final int columna = j;

                JButton boton = new JButton();
                boton.setPreferredSize(new Dimension(70, 70));

                // Añadir acción al hacer clic
                boton.addActionListener(e -> {
                    if (parkingPresenter.plazaOcupada(columna, fila)) {
                        // Si hay un vehículo, mostrar opciones
                        Optional<Vehiculo> optVehiculo = parkingPresenter.obtenerVehiculoEnPosicion(columna, fila);
                        if (optVehiculo.isPresent()) {
                            Vehiculo v = optVehiculo.get();
                            mostrarOpcionesVehiculoParking(v, dialogo);
                        }
                    } else {
                        // Si está vacía, intentar aparcar
                        mostrarOpcionesPlazaVacia(columna, fila, dialogo);
                    }
                });

                botonesParking[i][j] = boton;
                panelParking.add(boton);
            }
        }

        // Actualizar botones inicialmente con la matriz del parking
        actualizarVistaBotonesParking(botonesParking, parkingPresenter.obtenerMatrizParking());

        // Leyenda para los colores
        JPanel panelLeyenda = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelLeyenda.setBackground(COLOR_FONDO);

        panelLeyenda.add(new JLabel("Leyenda: "));

        JPanel labelCoche = new JPanel();
        labelCoche.setBackground(Color.GREEN);
        labelCoche.setPreferredSize(new Dimension(20, 20));
        panelLeyenda.add(labelCoche);
        panelLeyenda.add(new JLabel("Coche"));

        JPanel labelTractor = new JPanel();
        labelTractor.setBackground(Color.YELLOW);
        labelTractor.setPreferredSize(new Dimension(20, 20));
        panelLeyenda.add(labelTractor);
        panelLeyenda.add(new JLabel("Tractor"));

        JPanel labelTanque = new JPanel();
        labelTanque.setBackground(Color.RED);
        labelTanque.setPreferredSize(new Dimension(20, 20));
        panelLeyenda.add(labelTanque);
        panelLeyenda.add(new JLabel("Tanque"));

        JPanel labelVacio = new JPanel();
        labelVacio.setBackground(Color.LIGHT_GRAY);
        labelVacio.setPreferredSize(new Dimension(20, 20));
        panelLeyenda.add(labelVacio);
        panelLeyenda.add(new JLabel("Vacío"));

        // Botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelBotones.setBackground(COLOR_FONDO);

        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.addActionListener(e -> dialogo.dispose());
        panelBotones.add(btnCerrar);

        // Añadir componentes al panel principal
        panel.add(panelLeyenda, BorderLayout.NORTH);
        panel.add(panelParking, BorderLayout.CENTER);
        panel.add(panelBotones, BorderLayout.SOUTH);

        // Añadir panel al diálogo
        dialogo.add(panel);
        dialogo.setVisible(true);
    }

    // Mostrar opciones para un vehículo ya aparcado
    private void mostrarOpcionesVehiculoParking(Vehiculo vehiculo, JDialog parent) {
        List<String> opciones = new ArrayList<>();
        opciones.add("Retirar del parking");

        // Si es un tanque, añadir opciones específicas
        if (vehiculo instanceof Tanque) {
            opciones.add("Cargar armamento");
            opciones.add("Disparar");
        }

        String[] opcionesArray = opciones.toArray(new String[0]);

        int seleccion = JOptionPane.showOptionDialog(parent,
                vehiculo.getTipoVehiculo() + ": " + vehiculo.getMarca() + " (" + vehiculo.getMatricula() + ")",
                "Opciones de vehículo",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                opcionesArray,
                opcionesArray[0]);

        if (seleccion >= 0) {
            String opcionSeleccionada = opcionesArray[seleccion];

            switch (opcionSeleccionada) {
                case "Retirar del parking":
                    parkingPresenter.retirarVehiculoDeParking(vehiculo.getMatricula());
                    break;
                case "Cargar armamento":
                    if (vehiculo instanceof Tanque) {
                        Tanque tanque = (Tanque) vehiculo;
                        tanque.cargarArmamento();
                        mostrarMensajeExito("Armamento cargado correctamente");
                    }
                    break;
                case "Disparar":
                    if (vehiculo instanceof Tanque) {
                        mostrarDialogoDispararTanque((Tanque) vehiculo, parent);
                    }
                    break;
            }
        }
    }

    // Mostrar opciones para plaza vacía
    private void mostrarOpcionesPlazaVacia(int x, int y, JDialog parent) {
        List<Vehiculo> vehiculosDisponibles = parkingPresenter.obtenerVehiculosNoAparcados();

        if (vehiculosDisponibles.isEmpty()) {
            JOptionPane.showMessageDialog(parent,
                    "No hay vehículos disponibles para aparcar",
                    "Información", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // Crear selector
        String[] opciones = new String[vehiculosDisponibles.size()];
        for (int k = 0; k < vehiculosDisponibles.size(); k++) {
            opciones[k] = vehiculosDisponibles.get(k).getTipoVehiculo() + ": " +
                    vehiculosDisponibles.get(k).getMarca() + " (" +
                    vehiculosDisponibles.get(k).getMatricula() + ")";
        }

        String seleccion = (String) JOptionPane.showInputDialog(parent,
                "Seleccione vehículo para aparcar en [" + x + "," + y + "]:",
                "Aparcar vehículo", JOptionPane.QUESTION_MESSAGE,
                null, opciones, opciones[0]);

        if (seleccion != null) {
            // Extraer matrícula
            String matricula = seleccion.substring(
                    seleccion.lastIndexOf("(") + 1,
                    seleccion.lastIndexOf(")")
            );

            // Aparcar
            parkingPresenter.estacionarVehiculo(matricula, x, y);
        }
    }

    // Mostrar diálogo para seleccionar dirección de disparo
    private void mostrarDialogoDispararTanque(Tanque tanque, JDialog parent) {
        String[] direcciones = {"Delante", "Atrás", "Izquierda", "Derecha"};

        String seleccion = (String) JOptionPane.showInputDialog(parent,
                "Seleccione dirección de disparo:",
                "Disparar tanque", JOptionPane.QUESTION_MESSAGE,
                null, direcciones, direcciones[0]);

        if (seleccion != null) {
            Tanque.Direccion direccion = null;

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
            }

            if (direccion != null) {
                parkingPresenter.dispararTanque(tanque.getMatricula(), direccion);
            }
        }
    }

    // Actualiza la vista de los botones del parking con la matriz dada
    private void actualizarVistaBotonesParking(JButton[][] botones, Vehiculo[][] matriz) {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                Vehiculo vehiculo = matriz[i][j];
                JButton btn = botones[i][j];

                if (vehiculo != null) {
                    // Establecer color según tipo de vehículo
                    Color color = Color.LIGHT_GRAY;
                    if (vehiculo instanceof Coche) {
                        color = Color.GREEN;
                    } else if (vehiculo instanceof Tractor) {
                        color = Color.YELLOW;
                    } else if (vehiculo instanceof Tanque) {
                        color = Color.RED;
                    }

                    btn.setBackground(color);
                    btn.setText(vehiculo.getTipoVehiculo().substring(0, 1) + ": " +
                            vehiculo.getMatricula().substring(0, 4));
                    btn.setToolTipText(vehiculo.toString());
                } else {
                    // Plaza vacía
                    btn.setBackground(Color.LIGHT_GRAY);
                    btn.setText("[" + j + "," + i + "]");
                    btn.setToolTipText("Plaza vacía en posición [" + j + "," + i + "]");
                }
            }
        }
    }

    // Implementación de los métodos de la interfaz VehiculosContract.View
    @Override
    public void actualizarListaVehiculos(List<Vehiculo> vehiculos) {
        if (modeloTabla != null) {
            // Limpiar tabla
            modeloTabla.setRowCount(0);

            // Obtener todos los vehículos y agregarlos a la tabla
            for (Vehiculo vehiculo : vehiculos) {
                String posicion = vehiculo.estaAparcado() ?
                        "[" + vehiculo.getPosicionX() + "," + vehiculo.getPosicionY() + "]" : "No estacionado";

                // Ya no mostramos los atributos específicos
                String atributosEspecificos = "";

                modeloTabla.addRow(new Object[]{
                        vehiculo.getMatricula(),
                        vehiculo.getTipoVehiculo(),
                        vehiculo.getMarca(),
                        vehiculo.getPotenciaMotor() + " CV",
                        vehiculo.getAnioFabricacion(),
                        String.format("%.2f", vehiculo.getPrecio()),
                        posicion,
                        atributosEspecificos
                });
            }
        }

        // Verificar el botón de crear vehículos
        verificarBotonCrearVehiculos();
    }

    @Override
    public void mostrarMensajeExito(String mensaje) {
        JOptionPane.showMessageDialog(this,
                mensaje,
                "Éxito",
                JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public void mostrarMensajeError(String mensaje) {
        JOptionPane.showMessageDialog(this,
                mensaje,
                "Error",
                JOptionPane.ERROR_MESSAGE);
    }

    @Override
    public void cerrarAplicacion() {
        System.exit(0);
    }

    // Implementación de los métodos de la interfaz ParkingContract.View
    @Override
    public void actualizarVistaParking(Vehiculo[][] matrizParking) {
        if (botonesParking != null) {
            actualizarVistaBotonesParking(botonesParking, matrizParking);
        }
    }

    // Verifica si el botón de crear vehículos debe estar habilitado
    private void verificarBotonCrearVehiculos() {
        if (btnCrearVehiculos != null) {
            // El botón siempre debe estar habilitado, ya que siempre
            // creamos exactamente 3 vehículos a la vez
            btnCrearVehiculos.setEnabled(true);
            btnCrearVehiculos.setToolTipText("Crear un nuevo conjunto de 3 vehículos");
        }
    }

    // Metodo principal para mostrar la ventana
    public void mostrar() {
        setVisible(true);
    }
}