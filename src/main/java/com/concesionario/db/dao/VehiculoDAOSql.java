/*package com.concesionario.db.dao;

import com.concesionario.model.FabricaVehiculos;
import com.concesionario.model.vehiculo.Vehiculo;

import java.io.FileInputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

// Implementación DAO para MySQL
public class VehiculoDAOSql implements VehiculoDAO {

    private final String jdbcUrl;
    private final String usuario;
    private final String password;

    // Constructor
    public VehiculoDAOSql() {
        try {
            // Cargar configuración de archivo properties
            Properties props = new Properties();
            try (FileInputStream fis = new FileInputStream("src/main/resources/config.properties")) {
                props.load(fis);
            }

            this.jdbcUrl = props.getProperty("jdbc.url") + "/concesionario";
            this.usuario = props.getProperty("jdbc.usuario");
            this.password = props.getProperty("jdbc.password");

        } catch (Exception e) {
            System.err.println("Error al cargar la configuración: " + e.getMessage());
            throw new RuntimeException("No se pudo inicializar SQL DAO", e);
        }
    }

    // Obtiene conexión a la base de datos
    private Connection obtenerConexion() throws SQLException {
        return DriverManager.getConnection(jdbcUrl, usuario, password);
    }

    @Override
    public boolean guardar(Vehiculo vehiculo) {
        try (Connection conn = obtenerConexion()) {
            conn.setAutoCommit(false);

            try {
                // Insertar en la tabla principal de vehículos
                String sqlVehiculo = "INSERT INTO vehiculos VALUES (?, ?, ?, ?, ?, ?)";
                try (PreparedStatement pstmt = conn.prepareStatement(sqlVehiculo)) {
                    pstmt.setString(1, vehiculo.getMatricula());
                    pstmt.setString(2, vehiculo.getTipoVehiculo().toUpperCase());
                    pstmt.setString(3, vehiculo.getMarca());
                    pstmt.setInt(4, vehiculo.getPotenciaMotor());
                    pstmt.setInt(5, vehiculo.getAnioFabricacion());
                    pstmt.setDouble(6, vehiculo.getPrecio());
                    pstmt.executeUpdate();
                }

                // Insertar detalles específicos según el tipo de vehículo
                switch (vehiculo.getTipoVehiculo().toUpperCase()) {
                    case "COCHE":
                        insertarCoche(conn, vehiculo);
                        break;
                    case "TRACTOR":
                        insertarTractor(conn, vehiculo);
                        break;
                    case "TANQUE":
                        insertarTanque(conn, vehiculo);
                        break;
                }

                // Insertar en la tabla de posiciones si está aparcado
                if (vehiculo.estaAparcado()) {
                    String sqlParking = "INSERT INTO parking VALUES (?, ?, ?)";
                    try (PreparedStatement pstmt = conn.prepareStatement(sqlParking)) {
                        pstmt.setString(1, vehiculo.getMatricula());
                        pstmt.setInt(2, vehiculo.getPosicionX());
                        pstmt.setInt(3, vehiculo.getPosicionY());
                        pstmt.executeUpdate();
                    }
                }

                conn.commit();
                return true;

            } catch (SQLException e) {
                conn.rollback();
                System.err.println("Error al guardar vehículo: " + e.getMessage());
                e.printStackTrace();
                return false;
            } finally {
                conn.setAutoCommit(true);
            }

        } catch (SQLException e) {
            System.err.println("Error de conexión: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // Métodos auxiliares para insertar detalles según tipo de vehículo
    private void insertarCoche(Connection conn, Vehiculo vehiculo) throws SQLException {
        if (vehiculo instanceof com.concesionario.model.vehiculo.Coche) {
            com.concesionario.model.vehiculo.Coche coche = (com.concesionario.model.vehiculo.Coche) vehiculo;
            String sql = "INSERT INTO coches VALUES (?, ?, ?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, coche.getMatricula());
                pstmt.setInt(2, coche.getNumeroPuertas());
                pstmt.setString(3, coche.getTipoCombustible());
                pstmt.setBoolean(4, coche.isAutomatico());
                pstmt.executeUpdate();
            }
        }
    }

    private void insertarTractor(Connection conn, Vehiculo vehiculo) throws SQLException {
        if (vehiculo instanceof com.concesionario.model.vehiculo.Tractor) {
            com.concesionario.model.vehiculo.Tractor tractor = (com.concesionario.model.vehiculo.Tractor) vehiculo;
            String sql = "INSERT INTO tractores VALUES (?, ?, ?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, tractor.getMatricula());
                pstmt.setString(2, tractor.getArtilugio());
                pstmt.setString(3, tractor.getTipoTractor());
                pstmt.setBoolean(4, tractor.isTieneCabina());
                pstmt.executeUpdate();
            }
        }
    }

    private void insertarTanque(Connection conn, Vehiculo vehiculo) throws SQLException {
        if (vehiculo instanceof com.concesionario.model.vehiculo.Tanque) {
            com.concesionario.model.vehiculo.Tanque tanque = (com.concesionario.model.vehiculo.Tanque) vehiculo;
            String sql = "INSERT INTO tanques VALUES (?, ?, ?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, tanque.getMatricula());
                pstmt.setString(2, tanque.getArmamento());
                pstmt.setDouble(3, tanque.getBlindajeMm());
                pstmt.setInt(4, tanque.getVelocidadMaxima());
                pstmt.executeUpdate();
            }
        }
    }

    @Override
    public boolean actualizar(Vehiculo vehiculo) {
        try (Connection conn = obtenerConexion()) {
            conn.setAutoCommit(false);

            try {
                // Actualizar la tabla principal de vehículos
                String sqlVehiculo = "UPDATE vehiculos SET marca = ?, potencia_motor = ?, " +
                        "anio_fabricacion = ?, precio = ? WHERE matricula = ?";
                try (PreparedStatement pstmt = conn.prepareStatement(sqlVehiculo)) {
                    pstmt.setString(1, vehiculo.getMarca());
                    pstmt.setInt(2, vehiculo.getPotenciaMotor());
                    pstmt.setInt(3, vehiculo.getAnioFabricacion());
                    pstmt.setDouble(4, vehiculo.getPrecio());
                    pstmt.setString(5, vehiculo.getMatricula());
                    pstmt.executeUpdate();
                }

                // Actualizar detalles específicos según el tipo de vehículo
                switch (vehiculo.getTipoVehiculo().toUpperCase()) {
                    case "COCHE":
                        actualizarCoche(conn, vehiculo);
                        break;
                    case "TRACTOR":
                        actualizarTractor(conn, vehiculo);
                        break;
                    case "TANQUE":
                        actualizarTanque(conn, vehiculo);
                        break;
                }

                // Actualizar posición en el parking
                actualizarPosicionParking(conn, vehiculo);

                conn.commit();
                return true;

            } catch (SQLException e) {
                conn.rollback();
                System.err.println("Error al actualizar vehículo: " + e.getMessage());
                e.printStackTrace();
                return false;
            } finally {
                conn.setAutoCommit(true);
            }

        } catch (SQLException e) {
            System.err.println("Error de conexión: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // Métodos auxiliares para actualizar detalles según tipo de vehículo
    private void actualizarCoche(Connection conn, Vehiculo vehiculo) throws SQLException {
        if (vehiculo instanceof com.concesionario.model.vehiculo.Coche) {
            com.concesionario.model.vehiculo.Coche coche = (com.concesionario.model.vehiculo.Coche) vehiculo;
            String sql = "UPDATE coches SET numero_puertas = ?, tipo_combustible = ?, automatico = ? " +
                    "WHERE matricula = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, coche.getNumeroPuertas());
                pstmt.setString(2, coche.getTipoCombustible());
                pstmt.setBoolean(3, coche.isAutomatico());
                pstmt.setString(4, coche.getMatricula());
                pstmt.executeUpdate();
            }
        }
    }

    private void actualizarTractor(Connection conn, Vehiculo vehiculo) throws SQLException {
        if (vehiculo instanceof com.concesionario.model.vehiculo.Tractor) {
            com.concesionario.model.vehiculo.Tractor tractor = (com.concesionario.model.vehiculo.Tractor) vehiculo;
            String sql = "UPDATE tractores SET artilugio = ?, tipo_tractor = ?, cabina = ? " +
                    "WHERE matricula = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, tractor.getArtilugio());
                pstmt.setString(2, tractor.getTipoTractor());
                pstmt.setBoolean(3, tractor.isTieneCabina());
                pstmt.setString(4, tractor.getMatricula());
                pstmt.executeUpdate();
            }
        }
    }

    private void actualizarTanque(Connection conn, Vehiculo vehiculo) throws SQLException {
        if (vehiculo instanceof com.concesionario.model.vehiculo.Tanque) {
            com.concesionario.model.vehiculo.Tanque tanque = (com.concesionario.model.vehiculo.Tanque) vehiculo;
            String sql = "UPDATE tanques SET armamento = ?, blindaje_mm = ?, velocidad_maxima = ? " +
                    "WHERE matricula = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, tanque.getArmamento());
                pstmt.setDouble(2, tanque.getBlindajeMm());
                pstmt.setInt(3, tanque.getVelocidadMaxima());
                pstmt.setString(4, tanque.getMatricula());
                pstmt.executeUpdate();
            }
        }
    }

    private void actualizarPosicionParking(Connection conn, Vehiculo vehiculo) throws SQLException {
        // Verificar si ya existe en la tabla de parking
        boolean existeEnParking = false;
        try (PreparedStatement pstmt = conn.prepareStatement("SELECT 1 FROM parking WHERE matricula = ?")) {
            pstmt.setString(1, vehiculo.getMatricula());
            try (ResultSet rs = pstmt.executeQuery()) {
                existeEnParking = rs.next();
            }
        }

        if (vehiculo.estaAparcado()) {
            // Si está aparcado, actualizar o insertar
            if (existeEnParking) {
                // Actualizar
                try (PreparedStatement pstmt = conn.prepareStatement(
                        "UPDATE parking SET pos_x = ?, pos_y = ? WHERE matricula = ?")) {
                    pstmt.setInt(1, vehiculo.getPosicionX());
                    pstmt.setInt(2, vehiculo.getPosicionY());
                    pstmt.setString(3, vehiculo.getMatricula());
                    pstmt.executeUpdate();
                }
            } else {
                // Insertar
                try (PreparedStatement pstmt = conn.prepareStatement(
                        "INSERT INTO parking VALUES (?, ?, ?)")) {
                    pstmt.setString(1, vehiculo.getMatricula());
                    pstmt.setInt(2, vehiculo.getPosicionX());
                    pstmt.setInt(3, vehiculo.getPosicionY());
                    pstmt.executeUpdate();
                }
            }
        } else if (existeEnParking) {
            // Si no está aparcado pero existe en la tabla, eliminarlo
            try (PreparedStatement pstmt = conn.prepareStatement(
                    "DELETE FROM parking WHERE matricula = ?")) {
                pstmt.setString(1, vehiculo.getMatricula());
                pstmt.executeUpdate();
            }
        }
    }

    @Override
    public boolean eliminar(String matricula) {
        try (Connection conn = obtenerConexion();
             PreparedStatement pstmt = conn.prepareStatement("DELETE FROM vehiculos WHERE matricula = ?")) {
            pstmt.setString(1, matricula);
            int filasAfectadas = pstmt.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar vehículo: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Optional<Vehiculo> buscarPorId(String matricula) {
        try (Connection conn = obtenerConexion()) {
            // Obtener los datos básicos del vehículo
            String sql = "SELECT v.*, p.pos_x, p.pos_y FROM vehiculos v " +
                    "LEFT JOIN parking p ON v.matricula = p.matricula " +
                    "WHERE v.matricula = ?";

            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, matricula);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        String tipo = rs.getString("tipo").toUpperCase();

                        // Obtener datos específicos según el tipo de vehículo
                        switch (tipo) {
                            case "COCHE":
                                return buscarCochePorId(conn, rs);
                            case "TRACTOR":
                                return buscarTractorPorId(conn, rs);
                            case "TANQUE":
                                return buscarTanquePorId(conn, rs);
                            default:
                                return Optional.empty();
                        }
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar vehículo: " + e.getMessage());
            e.printStackTrace();
        }

        return Optional.empty();
    }

    private Optional<Vehiculo> buscarCochePorId(Connection conn, ResultSet rsVehiculo) throws SQLException {
        String matricula = rsVehiculo.getString("matricula");
        String marca = rsVehiculo.getString("marca");
        int potenciaMotor = rsVehiculo.getInt("potencia_motor");
        int anioFabricacion = rsVehiculo.getInt("anio_fabricacion");
        double precio = rsVehiculo.getDouble("precio");
        int posX = rsVehiculo.getInt("pos_x");
        int posY = rsVehiculo.getInt("pos_y");

        // Corregir si las posiciones son NULL en la base de datos
        if (rsVehiculo.wasNull()) {
            posX = -1;
            posY = -1;
        }

        // Obtener detalles específicos del coche
        String sqlCoche = "SELECT * FROM coches WHERE matricula = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sqlCoche)) {
            pstmt.setString(1, matricula);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    int numeroPuertas = rs.getInt("numero_puertas");
                    String tipoCombustible = rs.getString("tipo_combustible");
                    boolean automatico = rs.getBoolean("automatico");

                    com.concesionario.model.vehiculo.Coche coche =
                            new com.concesionario.model.vehiculo.Coche(
                                    matricula, marca, potenciaMotor, anioFabricacion, precio,
                                    posX, posY, numeroPuertas, tipoCombustible, automatico);
                    return Optional.of(coche);
                }
            }
        }

        return Optional.empty();
    }

    private Optional<Vehiculo> buscarTractorPorId(Connection conn, ResultSet rsVehiculo) throws SQLException {
        String matricula = rsVehiculo.getString("matricula");
        String marca = rsVehiculo.getString("marca");
        int potenciaMotor = rsVehiculo.getInt("potencia_motor");
        int anioFabricacion = rsVehiculo.getInt("anio_fabricacion");
        double precio = rsVehiculo.getDouble("precio");
        int posX = rsVehiculo.getInt("pos_x");
        int posY = rsVehiculo.getInt("pos_y");

        // Corregir si las posiciones son NULL en la base de datos
        if (rsVehiculo.wasNull()) {
            posX = -1;
            posY = -1;
        }

        // Obtener detalles específicos del tractor
        String sqlTractor = "SELECT * FROM tractores WHERE matricula = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sqlTractor)) {
            pstmt.setString(1, matricula);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String artilugio = rs.getString("artilugio");
                    String tipoTractor = rs.getString("tipo_tractor");
                    boolean tieneCabina = rs.getBoolean("cabina");

                    com.concesionario.model.vehiculo.Tractor tractor =
                            new com.concesionario.model.vehiculo.Tractor(
                                    matricula, marca, potenciaMotor, anioFabricacion, precio,
                                    posX, posY, artilugio, tipoTractor, tieneCabina);
                    return Optional.of(tractor);
                }
            }
        }

        return Optional.empty();
    }

    private Optional<Vehiculo> buscarTanquePorId(Connection conn, ResultSet rsVehiculo) throws SQLException {
        String matricula = rsVehiculo.getString("matricula");
        String marca = rsVehiculo.getString("marca");
        int potenciaMotor = rsVehiculo.getInt("potencia_motor");
        int anioFabricacion = rsVehiculo.getInt("anio_fabricacion");
        double precio = rsVehiculo.getDouble("precio");
        int posX = rsVehiculo.getInt("pos_x");
        int posY = rsVehiculo.getInt("pos_y");

        // Corregir si las posiciones son NULL en la base de datos
        if (rsVehiculo.wasNull()) {
            posX = -1;
            posY = -1;
        }

        // Obtener detalles específicos del tanque
        String sqlTanque = "SELECT * FROM tanques WHERE matricula = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sqlTanque)) {
            pstmt.setString(1, matricula);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String armamento = rs.getString("armamento");
                    double blindajeMm = rs.getDouble("blindaje_mm");
                    int velocidadMaxima = rs.getInt("velocidad_maxima");

                    com.concesionario.model.vehiculo.Tanque tanque =
                            new com.concesionario.model.vehiculo.Tanque(
                                    matricula, marca, potenciaMotor, anioFabricacion, precio,
                                    posX, posY, armamento, blindajeMm, velocidadMaxima);
                    return Optional.of(tanque);
                }
            }
        }

        return Optional.empty();
    }

    @Override
    public List<Vehiculo> obtenerTodos() {
        List<Vehiculo> vehiculos = new ArrayList<>();

        try (Connection conn = obtenerConexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(
                     "SELECT v.*, p.pos_x, p.pos_y FROM vehiculos v " +
                             "LEFT JOIN parking p ON v.matricula = p.matricula")) {

            while (rs.next()) {
                String matricula = rs.getString("matricula");
                String tipo = rs.getString("tipo").toUpperCase();

                Optional<Vehiculo> optVehiculo = Optional.empty();

                switch (tipo) {
                    case "COCHE":
                        optVehiculo = buscarCochePorId(conn, rs);
                        break;
                    case "TRACTOR":
                        optVehiculo = buscarTractorPorId(conn, rs);
                        break;
                    case "TANQUE":
                        optVehiculo = buscarTanquePorId(conn, rs);
                        break;
                }

                optVehiculo.ifPresent(vehiculos::add);
            }

        } catch (SQLException e) {
            System.err.println("Error al obtener todos los vehículos: " + e.getMessage());
            e.printStackTrace();
        }

        return vehiculos;
    }

    @Override
    public List<Vehiculo> obtenerPorTipo(String tipo) {
        List<Vehiculo> vehiculos = new ArrayList<>();

        try (Connection conn = obtenerConexion();
             PreparedStatement pstmt = conn.prepareStatement(
                     "SELECT v.*, p.pos_x, p.pos_y FROM vehiculos v " +
                             "LEFT JOIN parking p ON v.matricula = p.matricula " +
                             "WHERE v.tipo = ?")) {

            pstmt.setString(1, tipo.toUpperCase());

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Optional<Vehiculo> optVehiculo = Optional.empty();

                    switch (tipo.toUpperCase()) {
                        case "COCHE":
                            optVehiculo = buscarCochePorId(conn, rs);
                            break;
                        case "TRACTOR":
                            optVehiculo = buscarTractorPorId(conn, rs);
                            break;
                        case "TANQUE":
                            optVehiculo = buscarTanquePorId(conn, rs);
                            break;
                    }

                    optVehiculo.ifPresent(vehiculos::add);
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al obtener vehículos por tipo: " + e.getMessage());
            e.printStackTrace();
        }

        return vehiculos;
    }

    @Override
    public boolean existe(String matricula) {
        try (Connection conn = obtenerConexion();
             PreparedStatement pstmt = conn.prepareStatement("SELECT 1 FROM vehiculos WHERE matricula = ?")) {
            pstmt.setString(1, matricula);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            System.err.println("Error al verificar existencia: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean actualizarPosicion(String matricula, int posicionX, int posicionY) {
        try (Connection conn = obtenerConexion()) {
            conn.setAutoCommit(false);

            try {
                // Verificar si ya existe en la tabla de parking
                boolean existeEnParking = false;
                try (PreparedStatement pstmt = conn.prepareStatement("SELECT 1 FROM parking WHERE matricula = ?")) {
                    pstmt.setString(1, matricula);
                    try (ResultSet rs = pstmt.executeQuery()) {
                        existeEnParking = rs.next();
                    }
                }

                if (posicionX >= 0 && posicionY >= 0) {
                    // Actualizar o insertar la posición
                    if (existeEnParking) {
                        // Actualizar
                        try (PreparedStatement pstmt = conn.prepareStatement(
                                "UPDATE parking SET pos_x = ?, pos_y = ? WHERE matricula = ?")) {
                            pstmt.setInt(1, posicionX);
                            pstmt.setInt(2, posicionY);
                            pstmt.setString(3, matricula);
                            pstmt.executeUpdate();
                        }
                    } else {
                        // Insertar
                        try (PreparedStatement pstmt = conn.prepareStatement(
                                "INSERT INTO parking VALUES (?, ?, ?)")) {
                            pstmt.setString(1, matricula);
                            pstmt.setInt(2, posicionX);
                            pstmt.setInt(3, posicionY);
                            pstmt.executeUpdate();
                        }
                    }
                } else if (existeEnParking) {
                    // Eliminar la posición si las coordenadas son negativas
                    try (PreparedStatement pstmt = conn.prepareStatement(
                            "DELETE FROM parking WHERE matricula = ?")) {
                        pstmt.setString(1, matricula);
                        pstmt.executeUpdate();
                    }
                }

                conn.commit();
                return true;

            } catch (SQLException e) {
                conn.rollback();
                System.err.println("Error al actualizar posición: " + e.getMessage());
                e.printStackTrace();
                return false;
            } finally {
                conn.setAutoCommit(true);
            }

        } catch (SQLException e) {
            System.err.println("Error de conexión: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}

 */