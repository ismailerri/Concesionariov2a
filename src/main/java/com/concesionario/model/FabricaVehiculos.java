package com.concesionario.model;

import com.concesionario.model.vehiculo.Coche;
import com.concesionario.model.vehiculo.Tanque;
import com.concesionario.model.vehiculo.Tractor;
import com.concesionario.model.vehiculo.Vehiculo;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

// Clase factoría para crear vehículos
public class FabricaVehiculos {
    // Constantes para tipos de vehículos
    public static final String TIPO_COCHE = "COCHE";
    public static final String TIPO_TRACTOR = "TRACTOR";
    public static final String TIPO_TANQUE = "TANQUE";

    // Generador de números aleatorios
    private static final Random random = new Random();

    // Arrays con datos para generación aleatoria
    private static final String[] MARCAS_COCHES = {"Toyota", "Ford", "Volkswagen", "BMW", "Mercedes"};
    private static final String[] MARCAS_TRACTORES = {"John Deere", "New Holland", "Massey Ferguson", "Fendt", "Case IH"};
    private static final String[] ARTILUGIOS = {"Aplanador", "Arador", "Regador"};
    private static final String[] MARCAS_TANQUES = {"Leopard", "Abrams", "Challenger", "T-90", "Merkava"};
    private static final String[] ARMAMENTOS = {"Cañón 120mm", "Ametralladora pesada", "Misiles antitanque", "Cañón 125mm"};

    // Crea una matrícula aleatoria con formato 1234 ABC
    public static String generarMatricula() {
        StringBuilder sb = new StringBuilder();

        // 4 números
        for (int i = 0; i < 4; i++) {
            sb.append(random.nextInt(10));
        }

        sb.append(" ");

        // 3 letras (sin vocales para evitar palabras inapropiadas)
        String consonantes = "BCDFGHJKLMNPQRSTVWXYZ";
        for (int i = 0; i < 3; i++) {
            sb.append(consonantes.charAt(random.nextInt(consonantes.length())));
        }

        return sb.toString();
    }

    // Crea un conjunto de 3 vehículos (1 de cada tipo)
    public static List<Vehiculo> crearConjuntoVehiculos() {
        List<Vehiculo> conjunto = new ArrayList<>();

        conjunto.add(crearCocheAleatorio());
        conjunto.add(crearTractorAleatorio());
        conjunto.add(crearTanqueAleatorio());

        return conjunto;
    }

    // Crea un coche con valores aleatorios
    public static Coche crearCocheAleatorio() {
        String matricula = generarMatricula();
        String marca = MARCAS_COCHES[random.nextInt(MARCAS_COCHES.length)];
        int potenciaMotor = obtenerPotenciaAleatoria();
        int anioFabricacion = 2000 + random.nextInt(24); // Años entre 2000 y 2023
        double precio = 15000 + random.nextInt(50000);

        return new Coche(matricula, marca, potenciaMotor, anioFabricacion, precio);
    }

    // Crea un tractor con valores aleatorios
    public static Tractor crearTractorAleatorio() {
        String matricula = generarMatricula();
        String marca = MARCAS_TRACTORES[random.nextInt(MARCAS_TRACTORES.length)];
        int potenciaMotor = obtenerPotenciaAleatoria();
        int anioFabricacion = 2000 + random.nextInt(24); // Años entre 2000 y 2023
        double precio = 30000 + random.nextInt(70000);
        String artilugio = ARTILUGIOS[random.nextInt(ARTILUGIOS.length)];

        return new Tractor(matricula, marca, potenciaMotor, anioFabricacion, precio, artilugio);
    }

    // Crea un tanque con valores aleatorios
    public static Tanque crearTanqueAleatorio() {
        String matricula = generarMatricula();
        String marca = MARCAS_TANQUES[random.nextInt(MARCAS_TANQUES.length)];
        int potenciaMotor = obtenerPotenciaAleatoria();
        int anioFabricacion = 2000 + random.nextInt(24); // Años entre 2000 y 2023
        double precio = 1000000 + random.nextInt(5000000);
        String armamento = ARMAMENTOS[random.nextInt(ARMAMENTOS.length)];

        return new Tanque(matricula, marca, potenciaMotor, anioFabricacion, precio, armamento);
    }

    // Obtiene una potencia válida de forma aleatoria (50/100/150/200)
    private static int obtenerPotenciaAleatoria() {
        int[] potenciasValidas = {50, 100, 150, 200};
        return potenciasValidas[random.nextInt(potenciasValidas.length)];
    }

    // Método para recrear un vehículo existente a partir de datos de BD
    public static Vehiculo recrearVehiculo(String tipo, String matricula, String marca, String modelo,
                                           int potenciaMotor, int anioFabricacion, double precio,
                                           int posicionX, int posicionY, Object... atributosEspecificos) {

        switch (tipo.toUpperCase()) {
            case TIPO_COCHE:
                return new Coche(matricula, marca, potenciaMotor, anioFabricacion, precio,
                        posicionX, posicionY);

            case TIPO_TRACTOR:
                validarAtributos(atributosEspecificos, 1);
                String artilugio = (String) atributosEspecificos[0];
                return new Tractor(matricula, marca, potenciaMotor, anioFabricacion, precio,
                        posicionX, posicionY, artilugio);

            case TIPO_TANQUE:
                validarAtributos(atributosEspecificos, 1);
                String armamento = (String) atributosEspecificos[0];
                return new Tanque(matricula, marca, potenciaMotor, anioFabricacion, precio,
                        posicionX, posicionY, armamento);

            default:
                throw new IllegalArgumentException("Tipo de vehículo no válido: " + tipo);
        }
    }

    // Método auxiliar para validar que se proporcionen suficientes atributos
    private static void validarAtributos(Object[] atributos, int cantidadEsperada) {
        if (atributos == null || atributos.length < cantidadEsperada) {
            throw new IllegalArgumentException("Faltan atributos específicos para crear el vehículo");
        }
    }
}