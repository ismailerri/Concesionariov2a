package com.concesionario.view.components;

import com.concesionario.model.vehiculo.Coche;
import com.concesionario.model.vehiculo.Tanque;
import com.concesionario.model.vehiculo.Tractor;
import com.concesionario.model.vehiculo.Vehiculo;
import com.concesionario.util.Constants;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.Arrays;

// Componente para formularios de vehículos en Swing
public class VehiculoForm extends JPanel {

    // Campos comunes
    private JTextField txtMatricula;
    private JTextField txtMarca;
    private JComboBox<Integer> cbPotencia;
    private JTextField txtAnio;
    private JTextField txtPrecio;

    // Campos específicos para Coche
    private JTextField txtNumeroPuertas;
    private JTextField txtCombustible;
    private JCheckBox chkAutomatico;

    // Campos específicos para Tractor
    private JComboBox<String> cbArtilugio;
    private JTextField txtTipoTractor;
    private JCheckBox chkCabina;

    // Campos específicos para Tanque
    private JTextField txtArmamento;
    private JTextField txtBlindaje;
    private JTextField txtVelocidad;

    // Constructor para formulario de creación
    public VehiculoForm(String tipoVehiculo) {
        this(tipoVehiculo, null);
    }

    // Constructor para formulario de edición
    public VehiculoForm(String tipoVehiculo, Vehiculo vehiculo) {
        // Configurar layout
        this.setLayout(new GridLayout(0, 2, 10, 10));
        this.setBorder(new EmptyBorder(20, 10, 10, 10));

        // Inicializar campos
        inicializarCamposComunes(vehiculo);

        // Cargar campos específicos según tipo
        switch (tipoVehiculo.toUpperCase()) {
            case Constants.TIPO_COCHE:
                inicializarCamposCoche(vehiculo instanceof Coche ? (Coche)vehiculo : null);
                break;
            case Constants.TIPO_TRACTOR:
                inicializarCamposTractor(vehiculo instanceof Tractor ? (Tractor)vehiculo : null);
                break;
            case Constants.TIPO_TANQUE:
                inicializarCamposTanque(vehiculo instanceof Tanque ? (Tanque)vehiculo : null);
                break;
            default:
                throw new IllegalArgumentException("Tipo de vehículo no válido: " + tipoVehiculo);
        }
    }

    // Inicializa los campos comunes a todos los vehículos
    private void inicializarCamposComunes(Vehiculo vehiculo) {
        // Matrícula
        this.add(new JLabel("Matrícula:"));
        txtMatricula = new JTextField();
        if (vehiculo != null) {
            txtMatricula.setText(vehiculo.getMatricula());
            txtMatricula.setEditable(false); // No permitir editar matrícula en modo edición
        }
        this.add(txtMatricula);

        // Marca
        this.add(new JLabel("Marca:"));
        txtMarca = new JTextField();
        if (vehiculo != null) {
            txtMarca.setText(vehiculo.getMarca());
        }
        this.add(txtMarca);

        // Potencia
        this.add(new JLabel("Potencia (CV):"));
        cbPotencia = new JComboBox<>(new Integer[]{50, 100, 150, 200});
        if (vehiculo != null) {
            cbPotencia.setSelectedItem(vehiculo.getPotenciaMotor());
        } else {
            cbPotencia.setSelectedItem(100); // Valor por defecto
        }
        this.add(cbPotencia);

        // Año de fabricación
        this.add(new JLabel("Año de fabricación:"));
        txtAnio = new JTextField();
        if (vehiculo != null) {
            txtAnio.setText(String.valueOf(vehiculo.getAnioFabricacion()));
        }
        this.add(txtAnio);

        // Precio
        this.add(new JLabel("Precio (€):"));
        txtPrecio = new JTextField();
        if (vehiculo != null) {
            txtPrecio.setText(String.format("%.2f", vehiculo.getPrecio()));
        }
        this.add(txtPrecio);
    }

    // Inicializa los campos específicos para coches
    private void inicializarCamposCoche(Coche coche) {
        // Número de puertas
        this.add(new JLabel("Número de puertas:"));
        txtNumeroPuertas = new JTextField();
        if (coche != null) {
            txtNumeroPuertas.setText(String.valueOf(coche.getNumeroPuertas()));
        } else {
            txtNumeroPuertas.setText("4"); // Valor por defecto
        }
        this.add(txtNumeroPuertas);

        // Tipo de combustible
        this.add(new JLabel("Tipo de combustible:"));
        txtCombustible = new JTextField();
        if (coche != null) {
            txtCombustible.setText(coche.getTipoCombustible());
        } else {
            txtCombustible.setText("Gasolina"); // Valor por defecto
        }
        this.add(txtCombustible);

        // Automático
        this.add(new JLabel("Automático:"));
        chkAutomatico = new JCheckBox();
        if (coche != null) {
            chkAutomatico.setSelected(coche.isAutomatico());
        }
        this.add(chkAutomatico);
    }

    // Inicializa los campos específicos para tractores
    private void inicializarCamposTractor(Tractor tractor) {
        // Artilugio
        this.add(new JLabel("Artilugio:"));
        cbArtilugio = new JComboBox<>(Constants.ARTILUGIOS_VALIDOS);
        if (tractor != null) {
            cbArtilugio.setSelectedItem(tractor.getArtilugio());
        } else {
            cbArtilugio.setSelectedItem("Arador"); // Valor por defecto
        }
        this.add(cbArtilugio);

        // Tipo de tractor
        this.add(new JLabel("Tipo de tractor:"));
        txtTipoTractor = new JTextField();
        if (tractor != null) {
            txtTipoTractor.setText(tractor.getTipoTractor());
        } else {
            txtTipoTractor.setText("Agrícola"); // Valor por defecto
        }
        this.add(txtTipoTractor);

        // Cabina
        this.add(new JLabel("Tiene cabina:"));
        chkCabina = new JCheckBox();
        if (tractor != null) {
            chkCabina.setSelected(tractor.isTieneCabina());
        } else {
            chkCabina.setSelected(true); // Valor por defecto
        }
        this.add(chkCabina);
    }

    // Inicializa los campos específicos para tanques
    private void inicializarCamposTanque(Tanque tanque) {
        // Armamento
        this.add(new JLabel("Armamento:"));
        txtArmamento = new JTextField();
        if (tanque != null) {
            txtArmamento.setText(tanque.getArmamento());
        } else {
            txtArmamento.setText("Cañón estándar"); // Valor por defecto
        }
        this.add(txtArmamento);

        // Blindaje
        this.add(new JLabel("Blindaje (mm):"));
        txtBlindaje = new JTextField();
        if (tanque != null) {
            txtBlindaje.setText(String.format("%.1f", tanque.getBlindajeMm()));
        } else {
            txtBlindaje.setText("100.0"); // Valor por defecto
        }
        this.add(txtBlindaje);

        // Velocidad máxima
        this.add(new JLabel("Velocidad máx. (km/h):"));
        txtVelocidad = new JTextField();
        if (tanque != null) {
            txtVelocidad.setText(String.valueOf(tanque.getVelocidadMaxima()));
        } else {
            txtVelocidad.setText("50"); // Valor por defecto
        }
        this.add(txtVelocidad);
    }

    // Crea un vehículo a partir de los valores del formulario
    public Vehiculo crearVehiculo(String tipo) throws NumberFormatException, IllegalArgumentException {
        // Validar campos obligatorios
        if (txtMatricula.getText().isEmpty() || txtMarca.getText().isEmpty() ||
                txtAnio.getText().isEmpty() || txtPrecio.getText().isEmpty()) {
            throw new IllegalArgumentException("Todos los campos son obligatorios");
        }

        // Obtener valores comunes
        String matricula = txtMatricula.getText();
        String marca = txtMarca.getText();
        int potenciaMotor = (Integer) cbPotencia.getSelectedItem();
        int anioFabricacion = Integer.parseInt(txtAnio.getText());
        double precio = Double.parseDouble(txtPrecio.getText().replace(",", "."));

        // Crear vehículo según tipo
        switch (tipo.toUpperCase()) {
            case Constants.TIPO_COCHE:
                int numeroPuertas = Integer.parseInt(txtNumeroPuertas.getText());
                String tipoCombustible = txtCombustible.getText();
                boolean automatico = chkAutomatico.isSelected();
                return new Coche(matricula, marca, potenciaMotor, anioFabricacion, precio,
                        -1, -1, numeroPuertas, tipoCombustible, automatico);

            case Constants.TIPO_TRACTOR:
                String artilugio = (String) cbArtilugio.getSelectedItem();
                String tipoTractor = txtTipoTractor.getText();
                boolean tieneCabina = chkCabina.isSelected();
                return new Tractor(matricula, marca, potenciaMotor, anioFabricacion, precio,
                        -1, -1, artilugio, tipoTractor, tieneCabina);

            case Constants.TIPO_TANQUE:
                String armamento = txtArmamento.getText();
                double blindajeMm = Double.parseDouble(txtBlindaje.getText().replace(",", "."));
                int velocidadMaxima = Integer.parseInt(txtVelocidad.getText());
                return new Tanque(matricula, marca, potenciaMotor, anioFabricacion, precio,
                        -1, -1, armamento, blindajeMm, velocidadMaxima);

            default:
                throw new IllegalArgumentException("Tipo de vehículo no válido: " + tipo);
        }
    }

    // Actualiza un vehículo existente con los valores del formulario
    public void actualizarVehiculo(Vehiculo vehiculo) throws NumberFormatException, IllegalArgumentException {
        // Valores comunes
        vehiculo.setMarca(txtMarca.getText());
        vehiculo.setPotenciaMotor((Integer) cbPotencia.getSelectedItem());
        vehiculo.setAnioFabricacion(Integer.parseInt(txtAnio.getText()));
        vehiculo.setPrecio(Double.parseDouble(txtPrecio.getText().replace(",", ".")));

        // Valores específicos según tipo
        if (vehiculo instanceof Coche) {
            Coche coche = (Coche) vehiculo;
            coche.setNumeroPuertas(Integer.parseInt(txtNumeroPuertas.getText()));
            coche.setTipoCombustible(txtCombustible.getText());
            coche.setAutomatico(chkAutomatico.isSelected());

        } else if (vehiculo instanceof Tractor) {
            Tractor tractor = (Tractor) vehiculo;
            tractor.setArtilugio((String) cbArtilugio.getSelectedItem());
            tractor.setTipoTractor(txtTipoTractor.getText());
            tractor.setTieneCabina(chkCabina.isSelected());

        } else if (vehiculo instanceof Tanque) {
            Tanque tanque = (Tanque) vehiculo;
            tanque.setArmamento(txtArmamento.getText());
            tanque.setBlindajeMm(Double.parseDouble(txtBlindaje.getText().replace(",", ".")));
            tanque.setVelocidadMaxima(Integer.parseInt(txtVelocidad.getText()));
        }
    }
}