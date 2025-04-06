/*package com.concesionario.db.dao;

import com.concesionario.model.FabricaVehiculos;
import com.concesionario.model.vehiculo.Coche;
import com.concesionario.model.vehiculo.Tanque;
import com.concesionario.model.vehiculo.Tractor;
import com.concesionario.model.vehiculo.Vehiculo;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

// Implementación DAO para MongoDB
public class VehiculoDAOMongo implements VehiculoDAO {

    private final String mongoUri;
    private final String dbName;
    private final String collectionName;

    // Constructor
    public VehiculoDAOMongo() {
        try {
            // Cargar configuración de archivo properties
            Properties props = new Properties();
            try (FileInputStream fis = new FileInputStream("src/main/resources/config.properties")) {
                props.load(fis);
            }

            this.mongoUri = props.getProperty("mongodb.uri");
            this.dbName = props.getProperty("mongodb.database");
            this.collectionName = props.getProperty("mongodb.collection");

        } catch (Exception e) {
            System.err.println("Error al cargar la configuración de MongoDB: " + e.getMessage());
            throw new RuntimeException("No se pudo inicializar MongoDB DAO", e);
        }
    }

    // Obtiene la colección de vehículos
    private MongoCollection<Document> getCollection() {
        MongoClient mongoClient = new MongoClient(new MongoClientURI(mongoUri));
        MongoDatabase database = mongoClient.getDatabase(dbName);
        return database.getCollection(collectionName);
    }

    // Convierte un vehículo a documento BSON
    private Document vehiculoADocument(Vehiculo vehiculo) {
        Document doc = new Document("_id", vehiculo.getMatricula())
                .append("tipo", vehiculo.getTipoVehiculo().toUpperCase())
                .append("marca", vehiculo.getMarca())
                .append("potenciaMotor", vehiculo.getPotenciaMotor())
                .append("anioFabricacion", vehiculo.getAnioFabricacion())
                .append("precio", vehiculo.getPrecio());

        // Agregar posición si está aparcado
        if (vehiculo.estaAparcado()) {
            doc.append("posicion", new Document("x", vehiculo.getPosicionX())
                    .append("y", vehiculo.getPosicionY()));
        } else {
            doc.append("posicion", null);
        }

        // Agregar campos específicos según el tipo de vehículo
        if (vehiculo instanceof Coche) {
            Coche coche = (Coche) vehiculo;
            doc.append("detalles", new Document("numeroPuertas", coche.getNumeroPuertas())
                    .append("tipoCombustible", coche.getTipoCombustible())
                    .append("automatico", coche.isAutomatico())
                    .append("motorEncendido", coche.isMotorEncendido())
                    .append("lucesEncendidas", coche.isLucesEncendidas()));
        } else if (vehiculo instanceof Tractor) {
            Tractor tractor = (Tractor) vehiculo;
            doc.append("detalles", new Document("artilugio", tractor.getArtilugio())
                    .append("tipoTractor", tractor.getTipoTractor())
                    .append("tieneCabina", tractor.isTieneCabina())
                    .append("motorEncendido", tractor.isMotorEncendido())
                    .append("lucesEncendidas", tractor.isLucesEncendidas()));
        } else if (vehiculo instanceof Tanque) {
            Tanque tanque = (Tanque) vehiculo;
            doc.append("detalles", new Document("armamento", tanque.getArmamento())
                    .append("blindajeMm", tanque.getBlindajeMm())
                    .append("velocidadMaxima", tanque.getVelocidadMaxima())
                    .append("armamentoCargado", tanque.isArmamentoCargado())
                    .append("direccionCanon", tanque.getDireccionCanon().toString()));
        }

        return doc;
    }

    // Convierte un documento BSON a vehículo
    private Vehiculo documentAVehiculo(Document doc) {
        String matricula = doc.getString("_id");
        String tipo = doc.getString("tipo");
        String marca = doc.getString("marca");
        int potenciaMotor = doc.getInteger("potenciaMotor");
        int anioFabricacion = doc.getInteger("anioFabricacion");
        double precio = doc.getDouble("precio");

        // Obtener posición si existe
        int posicionX = -1;
        int posicionY = -1;
        Document posicion = (Document) doc.get("posicion");
        if (posicion != null) {
            posicionX = posicion.getInteger("x");
            posicionY = posicion.getInteger("y");
        }

        // Obtener detalles específicos
        Document detalles = (Document) doc.get("detalles");
        if (detalles == null) detalles = new Document();

        // Crear vehículo según el tipo
        switch (tipo) {
            case "COCHE":
                int numeroPuertas = detalles.getInteger("numeroPuertas", 4);
                String tipoCombustible = detalles.getString("tipoCombustible");
                boolean automatico = detalles.getBoolean("automatico", false);

                Coche coche = new Coche(matricula, marca, potenciaMotor, anioFabricacion, precio,
                        posicionX, posicionY, numeroPuertas, tipoCombustible, automatico);

                if (detalles.getBoolean("motorEncendido", false)) coche.toggleMotor();
                if (detalles.getBoolean("lucesEncendidas", false)) coche.toggleLuces();

                return coche;

            case "TRACTOR":
                String artilugio = detalles.getString("artilugio");
                String tipoTractor = detalles.getString("tipoTractor");
                boolean tieneCabina = detalles.getBoolean("tieneCabina", true);

                Tractor tractor = new Tractor(matricula, marca, potenciaMotor, anioFabricacion, precio,
                        posicionX, posicionY, artilugio != null ? artilugio : "Arador",
                        tipoTractor, tieneCabina);

                if (detalles.getBoolean("motorEncendido", false)) tractor.toggleMotor();
                if (detalles.getBoolean("lucesEncendidas", false)) tractor.toggleLuces();

                return tractor;

            case "TANQUE":
                String armamento = detalles.getString("armamento");
                double blindajeMm = detalles.getDouble("blindajeMm", 100.0);
                int velocidadMaxima = detalles.getInteger("velocidadMaxima", 50);

                Tanque tanque = new Tanque(matricula, marca, potenciaMotor, anioFabricacion, precio,
                        posicionX, posicionY, armamento != null ? armamento : "Cañón estándar",
                        blindajeMm, velocidadMaxima);

                if (detalles.getBoolean("armamentoCargado", false)) tanque.cargarArmamento();

                String direccionCanon = detalles.getString("direccionCanon");
                if (direccionCanon != null) {
                    try {
                        tanque.apuntarCanon(Tanque.Direccion.valueOf(direccionCanon));
                    } catch (IllegalArgumentException e) {
                        // Usar dirección por defecto
                    }
                }

                return tanque;

            default:
                throw new IllegalArgumentException("Tipo de vehículo desconocido: " + tipo);
        }
    }

    @Override
    public boolean guardar(Vehiculo vehiculo) {
        try {
            Document doc = vehiculoADocument(vehiculo);
            getCollection().insertOne(doc);
            return true;
        } catch (Exception e) {
            System.err.println("Error al guardar vehículo en MongoDB: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean actualizar(Vehiculo vehiculo) {
        try {
            Document doc = vehiculoADocument(vehiculo);
            getCollection().replaceOne(Filters.eq("_id", vehiculo.getMatricula()), doc);
            return true;
        } catch (Exception e) {
            System.err.println("Error al actualizar vehículo en MongoDB: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean eliminar(String matricula) {
        try {
            getCollection().deleteOne(Filters.eq("_id", matricula));
            return true;
        } catch (Exception e) {
            System.err.println("Error al eliminar vehículo en MongoDB: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Optional<Vehiculo> buscarPorId(String matricula) {
        try {
            Document doc = getCollection().find(Filters.eq("_id", matricula)).first();
            if (doc != null) {
                return Optional.of(documentAVehiculo(doc));
            }
        } catch (Exception e) {
            System.err.println("Error al buscar vehículo en MongoDB: " + e.getMessage());
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public List<Vehiculo> obtenerTodos() {
        List<Vehiculo> vehiculos = new ArrayList<>();
        try {
            getCollection().find().forEach(doc -> {
                try {
                    vehiculos.add(documentAVehiculo(doc));
                } catch (Exception e) {
                    System.err.println("Error al convertir documento: " + e.getMessage());
                }
            });
        } catch (Exception e) {
            System.err.println("Error al obtener vehículos de MongoDB: " + e.getMessage());
            e.printStackTrace();
        }
        return vehiculos;
    }

    @Override
    public List<Vehiculo> obtenerPorTipo(String tipo) {
        List<Vehiculo> vehiculos = new ArrayList<>();
        try {
            getCollection().find(Filters.eq("tipo", tipo.toUpperCase())).forEach(doc -> {
                try {
                    vehiculos.add(documentAVehiculo(doc));
                } catch (Exception e) {
                    System.err.println("Error al convertir documento: " + e.getMessage());
                }
            });
        } catch (Exception e) {
            System.err.println("Error al obtener vehículos por tipo de MongoDB: " + e.getMessage());
            e.printStackTrace();
        }
        return vehiculos;
    }

    @Override
    public boolean existe(String matricula) {
        try {
            return getCollection().countDocuments(Filters.eq("_id", matricula)) > 0;
        } catch (Exception e) {
            System.err.println("Error al verificar existencia en MongoDB: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean actualizarPosicion(String matricula, int posicionX, int posicionY) {
        try {
            Bson filter = Filters.eq("_id", matricula);
            Bson update;

            if (posicionX >= 0 && posicionY >= 0) {
                Document posicion = new Document("x", posicionX).append("y", posicionY);
                update = Updates.set("posicion", posicion);
            } else {
                update = Updates.set("posicion", null);
            }

            getCollection().updateOne(filter, update);
            return true;
        } catch (Exception e) {
            System.err.println("Error al actualizar posición en MongoDB: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}

 */