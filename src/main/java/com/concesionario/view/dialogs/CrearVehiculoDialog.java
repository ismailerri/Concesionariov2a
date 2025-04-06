package com.concesionario.view.dialogs;

import com.concesionario.model.vehiculo.Coche;
import com.concesionario.model.vehiculo.Tanque;
import com.concesionario.model.vehiculo.Tractor;
import com.concesionario.model.vehiculo.Vehiculo;
import com.concesionario.presenter.VehiculosPresenter;
import com.concesionario.util.Constants;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

// Diálogo para crear manualmente los tres tipos de vehículos
public class CrearVehiculoDialog extends JDialog {

    // Campos comunes para todos los vehículos
    private JTextField txtMatriculaCoche;
    private JTextField txtMarcaCoche;
    private JComboBox<Integer> cbPotenciaCoche;
    private JTextField txtAnioCoche;
    private JTextField txtPrecioCoche;

    private JTextField txtMatriculaTractor;
    private JTextField txtMarcaTractor;
    private JComboBox<Integer> cbPotenciaTractor;
    private JTextField txtAnioTractor;
    private JTextField txtPrecioTractor;

    private JTextField txtMatriculaTanque;
    private JTextField txtMarcaTanque;
    private JComboBox<Integer> cbPotenciaTanque;
    private JTextField txtAnioTanque;
    private JTextField txtPrecioTanque;

    // Campos específicos para Coche
    // NO INCLUIDOS en el PDF

    // Campos específicos para Tractor
    private JComboBox<String> cbArtilugio;

    // Campos específicos para Tanque
    private JTextField txtArmamento;

    // Resultado del diálogo
    private boolean confirmado = false;
    private List<Vehiculo> vehiculosCreados = new ArrayList<>();

    // Referencia al presentador para validación de matrículas
    private VehiculosPresenter presenter;

    // Constructor
    public CrearVehiculoDialog(JFrame parent, VehiculosPresenter presenter) {
        super(parent, "Crear Vehículos", true);

        this.presenter = presenter;

        // Configurar diálogo
        setSize(800, 600);
        setLocationRelativeTo(parent);
        setResizable(true);

        // Inicializar componentes
        initComponents();
    }

    // Inicializa los componentes del diálogo
    private void initComponents() {
        // Panel principal con pestañas
        JTabbedPane tabbedPane = new JTabbedPane();

        // Pestañas para cada tipo de vehículo
        tabbedPane.addTab("Coche", createCochePanel());
        tabbedPane.addTab("Tractor", createTractorPanel());
        tabbedPane.addTab("Tanque", createTanquePanel());

        // Panel de botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.addActionListener(e -> {
            confirmado = false;
            dispose();
        });

        JButton btnCrear = new JButton("Crear");
        btnCrear.addActionListener(e -> {
            if (validarFormularios()) {
                if (crearVehiculos()) {
                    confirmado = true;
                    dispose();
                }
            }
        });

        panelBotones.add(btnCancelar);
        panelBotones.add(btnCrear);

        // Añadir componentes al diálogo
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(tabbedPane, BorderLayout.CENTER);
        getContentPane().add(panelBotones, BorderLayout.SOUTH);
    }

    // Crea el panel para el formulario de coche
    private JPanel createCochePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Panel para los campos del formulario
        JPanel formPanel = new JPanel(new GridLayout(0, 2, 10, 10));

        // Campos comunes
        formPanel.add(new JLabel("Matrícula:"));
        txtMatriculaCoche = new JTextField();
        formPanel.add(txtMatriculaCoche);

        formPanel.add(new JLabel("Marca:"));
        txtMarcaCoche = new JTextField();
        formPanel.add(txtMarcaCoche);

        formPanel.add(new JLabel("Potencia (CV):"));
        cbPotenciaCoche = new JComboBox<>(new Integer[]{50, 100, 150, 200});
        formPanel.add(cbPotenciaCoche);

        formPanel.add(new JLabel("Año de fabricación:"));
        txtAnioCoche = new JTextField();
        formPanel.add(txtAnioCoche);

        formPanel.add(new JLabel("Precio (€):"));
        txtPrecioCoche = new JTextField();
        formPanel.add(txtPrecioCoche);

        // Nota informativa
        JLabel lblNota = new JLabel("<html><b>Nota:</b> Debes completar todos los formularios (Coche, Tractor y Tanque) para poder crear los vehículos.</html>");
        lblNota.setForeground(Color.BLUE);

        // Añadir panel de formulario y nota
        panel.add(formPanel, BorderLayout.NORTH);
        panel.add(lblNota, BorderLayout.SOUTH);

        return panel;
    }

    // Crea el panel para el formulario de tractor
    private JPanel createTractorPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Panel para los campos del formulario
        JPanel formPanel = new JPanel(new GridLayout(0, 2, 10, 10));

        // Campos comunes
        formPanel.add(new JLabel("Matrícula:"));
        txtMatriculaTractor = new JTextField();
        formPanel.add(txtMatriculaTractor);

        formPanel.add(new JLabel("Marca:"));
        txtMarcaTractor = new JTextField();
        formPanel.add(txtMarcaTractor);

        formPanel.add(new JLabel("Potencia (CV):"));
        cbPotenciaTractor = new JComboBox<>(new Integer[]{50, 100, 150, 200});
        formPanel.add(cbPotenciaTractor);

        formPanel.add(new JLabel("Año de fabricación:"));
        txtAnioTractor = new JTextField();
        formPanel.add(txtAnioTractor);

        formPanel.add(new JLabel("Precio (€):"));
        txtPrecioTractor = new JTextField();
        formPanel.add(txtPrecioTractor);

        // Campos específicos de tractor
        formPanel.add(new JLabel("Artilugio:"));
        cbArtilugio = new JComboBox<>(Constants.ARTILUGIOS_VALIDOS);
        formPanel.add(cbArtilugio);

        // Nota informativa
        JLabel lblNota = new JLabel("<html><b>Nota:</b> Debes completar todos los formularios (Coche, Tractor y Tanque) para poder crear los vehículos.</html>");
        lblNota.setForeground(Color.BLUE);

        // Añadir panel de formulario y nota
        panel.add(formPanel, BorderLayout.NORTH);
        panel.add(lblNota, BorderLayout.SOUTH);

        return panel;
    }

    // Crea el panel para el formulario de tanque
    private JPanel createTanquePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Panel para los campos del formulario
        JPanel formPanel = new JPanel(new GridLayout(0, 2, 10, 10));

        // Campos comunes
        formPanel.add(new JLabel("Matrícula:"));
        txtMatriculaTanque = new JTextField();
        formPanel.add(txtMatriculaTanque);

        formPanel.add(new JLabel("Marca:"));
        txtMarcaTanque = new JTextField();
        formPanel.add(txtMarcaTanque);

        formPanel.add(new JLabel("Potencia (CV):"));
        cbPotenciaTanque = new JComboBox<>(new Integer[]{50, 100, 150, 200});
        formPanel.add(cbPotenciaTanque);

        formPanel.add(new JLabel("Año de fabricación:"));
        txtAnioTanque = new JTextField();
        formPanel.add(txtAnioTanque);

        formPanel.add(new JLabel("Precio (€):"));
        txtPrecioTanque = new JTextField();
        formPanel.add(txtPrecioTanque);

        // Campos específicos de tanque
        formPanel.add(new JLabel("Armamento:"));
        txtArmamento = new JTextField("Cañón estándar");
        formPanel.add(txtArmamento);

        // Nota informativa
        JLabel lblNota = new JLabel("<html><b>Nota:</b> Debes completar todos los formularios (Coche, Tractor y Tanque) para poder crear los vehículos.</html>");
        lblNota.setForeground(Color.BLUE);

        // Añadir panel de formulario y nota
        panel.add(formPanel, BorderLayout.NORTH);
        panel.add(lblNota, BorderLayout.SOUTH);

        return panel;
    }

    // Valida los datos de los formularios
    private boolean validarFormularios() {
        // Validar que todos los campos estén completos
        if (!validarCamposCompletos()) {
            JOptionPane.showMessageDialog(this,
                    "Debes completar todos los formularios (Coche, Tractor y Tanque) para crear los vehículos.",
                    "Campos incompletos",
                    JOptionPane.WARNING_MESSAGE);
            return false;
        }

        // Validar formato de matrícula (4 números seguidos de 3 letras)
        if (!validarMatriculas()) {
            return false;
        }

        // Validar matrículas únicas
        if (!validarMatriculasUnicas()) {
            return false;
        }

        // Validar que valores numéricos sean correctos
        if (!validarValoresNumericos()) {
            return false;
        }

        return true;
    }

    // Valida que todos los campos estén completos
    private boolean validarCamposCompletos() {
        // Validar datos del coche
        if (txtMatriculaCoche.getText().isEmpty() || txtMarcaCoche.getText().isEmpty() ||
                txtAnioCoche.getText().isEmpty() || txtPrecioCoche.getText().isEmpty()) {
            return false;
        }

        // Validar datos del tractor
        if (txtMatriculaTractor.getText().isEmpty() || txtMarcaTractor.getText().isEmpty() ||
                txtAnioTractor.getText().isEmpty() || txtPrecioTractor.getText().isEmpty()) {
            return false;
        }

        // Validar datos del tanque
        if (txtMatriculaTanque.getText().isEmpty() || txtMarcaTanque.getText().isEmpty() ||
                txtAnioTanque.getText().isEmpty() || txtPrecioTanque.getText().isEmpty() ||
                txtArmamento.getText().isEmpty()) {
            return false;
        }

        return true;
    }

    // Valida que las matrículas tengan el formato correcto (4 números seguidos de 3 letras)
    private boolean validarMatriculas() {
        // Patrón: 4 dígitos, espacio, 3 letras mayúsculas
        Pattern pattern = Pattern.compile("\\d{4}\\s[A-Z]{3}");

        if (!pattern.matcher(txtMatriculaCoche.getText()).matches()) {
            JOptionPane.showMessageDialog(this,
                    "La matrícula del coche debe tener el formato: 4 números, espacio, 3 letras mayúsculas (Ej: 1234 ABC)",
                    "Formato de matrícula incorrecto",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (!pattern.matcher(txtMatriculaTractor.getText()).matches()) {
            JOptionPane.showMessageDialog(this,
                    "La matrícula del tractor debe tener el formato: 4 números, espacio, 3 letras mayúsculas (Ej: 1234 ABC)",
                    "Formato de matrícula incorrecto",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (!pattern.matcher(txtMatriculaTanque.getText()).matches()) {
            JOptionPane.showMessageDialog(this,
                    "La matrícula del tanque debe tener el formato: 4 números, espacio, 3 letras mayúsculas (Ej: 1234 ABC)",
                    "Formato de matrícula incorrecto",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }

    // Valida que las matrículas no estén duplicadas en el sistema
    private boolean validarMatriculasUnicas() {
        // Validar que no existan en el sistema
        if (presenter.buscarVehiculo(txtMatriculaCoche.getText()).isPresent()) {
            JOptionPane.showMessageDialog(this,
                    "La matrícula del coche ya existe en el sistema. Por favor, introduce una matrícula única.",
                    "Matrícula duplicada",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (presenter.buscarVehiculo(txtMatriculaTractor.getText()).isPresent()) {
            JOptionPane.showMessageDialog(this,
                    "La matrícula del tractor ya existe en el sistema. Por favor, introduce una matrícula única.",
                    "Matrícula duplicada",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (presenter.buscarVehiculo(txtMatriculaTanque.getText()).isPresent()) {
            JOptionPane.showMessageDialog(this,
                    "La matrícula del tanque ya existe en el sistema. Por favor, introduce una matrícula única.",
                    "Matrícula duplicada",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Validar que no estén duplicadas entre sí
        if (txtMatriculaCoche.getText().equals(txtMatriculaTractor.getText()) ||
                txtMatriculaCoche.getText().equals(txtMatriculaTanque.getText()) ||
                txtMatriculaTractor.getText().equals(txtMatriculaTanque.getText())) {
            JOptionPane.showMessageDialog(this,
                    "Hay matrículas duplicadas entre los vehículos. Cada vehículo debe tener una matrícula única.",
                    "Matrículas duplicadas",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }

    // Valida que los valores numéricos sean correctos
    private boolean validarValoresNumericos() {
        try {
            // Validar año (debe ser número positivo)
            int anioCoche = Integer.parseInt(txtAnioCoche.getText());
            int anioTractor = Integer.parseInt(txtAnioTractor.getText());
            int anioTanque = Integer.parseInt(txtAnioTanque.getText());

            if (anioCoche <= 0 || anioTractor <= 0 || anioTanque <= 0) {
                JOptionPane.showMessageDialog(this,
                        "El año de fabricación debe ser un número positivo.",
                        "Valor incorrecto",
                        JOptionPane.ERROR_MESSAGE);
                return false;
            }

            // Validar precio (debe ser número positivo)
            double precioCoche = Double.parseDouble(txtPrecioCoche.getText().replace(",", "."));
            double precioTractor = Double.parseDouble(txtPrecioTractor.getText().replace(",", "."));
            double precioTanque = Double.parseDouble(txtPrecioTanque.getText().replace(",", "."));

            if (precioCoche <= 0 || precioTractor <= 0 || precioTanque <= 0) {
                JOptionPane.showMessageDialog(this,
                        "El precio debe ser un número positivo.",
                        "Valor incorrecto",
                        JOptionPane.ERROR_MESSAGE);
                return false;
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "Los campos de año y precio deben ser números válidos.",
                    "Error de formato",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }

    // Crea los vehículos con los datos de los formularios
    private boolean crearVehiculos() {
        vehiculosCreados.clear();

        try {
            // Crear coche
            Coche coche = new Coche(
                    txtMatriculaCoche.getText(),
                    txtMarcaCoche.getText(),
                    (Integer) cbPotenciaCoche.getSelectedItem(),
                    Integer.parseInt(txtAnioCoche.getText()),
                    Double.parseDouble(txtPrecioCoche.getText().replace(",", "."))
            );
            vehiculosCreados.add(coche);

            // Crear tractor
            Tractor tractor = new Tractor(
                    txtMatriculaTractor.getText(),
                    txtMarcaTractor.getText(),
                    (Integer) cbPotenciaTractor.getSelectedItem(),
                    Integer.parseInt(txtAnioTractor.getText()),
                    Double.parseDouble(txtPrecioTractor.getText().replace(",", ".")),
                    (String) cbArtilugio.getSelectedItem()
            );
            vehiculosCreados.add(tractor);

            // Crear tanque
            Tanque tanque = new Tanque(
                    txtMatriculaTanque.getText(),
                    txtMarcaTanque.getText(),
                    (Integer) cbPotenciaTanque.getSelectedItem(),
                    Integer.parseInt(txtAnioTanque.getText()),
                    Double.parseDouble(txtPrecioTanque.getText().replace(",", ".")),
                    txtArmamento.getText()
            );
            vehiculosCreados.add(tanque);

            return true;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error al crear los vehículos: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    // Indica si se confirmó la creación
    public boolean isConfirmado() {
        return confirmado;
    }

    // Obtiene los vehículos creados
    public List<Vehiculo> getVehiculosCreados() {
        return vehiculosCreados;
    }
}