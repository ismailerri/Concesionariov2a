package com.concesionario.model.vehiculo;

// Clase que representa un Tanque (vehículo militar) en el concesionario
public class Tanque extends Vehiculo {
    // Atributos específicos del tanque
    private String armamento;
    private double blindajeMm;
    private int velocidadMaxima;

    // Estado del armamento
    private boolean armamentoCargado;
    private Direccion direccionCanon;

    // Enumeración para las direcciones de disparo
    public enum Direccion {
        DELANTE, ATRAS, IZQUIERDA, DERECHA
    }

    // Constructor básico
    public Tanque(String matricula, String marca, int potenciaMotor,
                  int anioFabricacion, double precio, String armamento) {
        super(matricula, marca, potenciaMotor, anioFabricacion, precio);
        this.armamento = armamento;
        inicializarValoresPorDefecto();
    }

    // Constructor con posición en el parking
    public Tanque(String matricula, String marca, int potenciaMotor,
                  int anioFabricacion, double precio, int posicionX, int posicionY,
                  String armamento) {
        super(matricula, marca, potenciaMotor, anioFabricacion, precio, posicionX, posicionY);
        this.armamento = armamento;
        inicializarValoresPorDefecto();
    }

    // Constructor completo
    public Tanque(String matricula, String marca, int potenciaMotor,
                  int anioFabricacion, double precio, int posicionX, int posicionY,
                  String armamento, double blindajeMm, int velocidadMaxima) {
        super(matricula, marca, potenciaMotor, anioFabricacion, precio, posicionX, posicionY);
        this.armamento = armamento;
        this.blindajeMm = blindajeMm;
        this.velocidadMaxima = velocidadMaxima;
        armamentoCargado = false;
        direccionCanon = Direccion.DELANTE;
    }

    // Inicializa valores por defecto
    private void inicializarValoresPorDefecto() {
        blindajeMm = 100.0;
        velocidadMaxima = 50;
        armamentoCargado = false;
        direccionCanon = Direccion.DELANTE;
    }

    @Override
    public String getTipoVehiculo() {
        return "Tanque";
    }

    // Carga el armamento del tanque
    public boolean cargarArmamento() {
        armamentoCargado = true;
        return armamentoCargado;
    }

    // Verifica si el armamento está cargado
    public boolean isArmamentoCargado() {
        return armamentoCargado;
    }

    // Establece la dirección del cañón
    public void apuntarCanon(Direccion direccion) {
        this.direccionCanon = direccion;
    }

    // Obtiene la dirección actual del cañón
    public Direccion getDireccionCanon() {
        return direccionCanon;
    }

    // Dispara en la dirección actual si el armamento está cargado
    // Devuelve la matrícula del vehículo eliminado o null si no hay objetivo
    public String disparar(Vehiculo[][] matriz) {
        // Si el tanque no está aparcado, no puede disparar
        if (!estaAparcado()) {
            return "El tanque no está en el parking, no puede disparar";
        }

        // Si el armamento no está cargado, no puede disparar
        if (!armamentoCargado) {
            return "El armamento no está cargado";
        }

        // Calcular coordenadas del objetivo según la dirección
        int targetX = getPosicionX();
        int targetY = getPosicionY();

        switch (direccionCanon) {
            case DELANTE:
                targetY--;  // En la matriz, y decrece hacia "arriba"
                break;
            case ATRAS:
                targetY++;
                break;
            case IZQUIERDA:
                targetX--;
                break;
            case DERECHA:
                targetX++;
                break;
        }

        // Verificar si las coordenadas están dentro de los límites
        if (targetX < 0 || targetX >= matriz[0].length ||
                targetY < 0 || targetY >= matriz.length) {
            return "No hay ningún vehículo en esa dirección para disparar";
        }

        // Verificar si hay un vehículo en esa posición
        Vehiculo objetivo = matriz[targetY][targetX];
        if (objetivo == null) {
            return "No hay ningún vehículo en esa dirección para disparar";
        }

        // Descargar el armamento después de disparar
        armamentoCargado = false;

        // Devolver la matrícula del vehículo objetivo
        return objetivo.getMatricula();
    }

    // Getters y setters
    public String getArmamento() {
        return armamento;
    }

    public void setArmamento(String armamento) {
        this.armamento = armamento;
    }

    public double getBlindajeMm() {
        return blindajeMm;
    }

    public void setBlindajeMm(double blindajeMm) {
        this.blindajeMm = blindajeMm;
    }

    public int getVelocidadMaxima() {
        return velocidadMaxima;
    }

    public void setVelocidadMaxima(int velocidadMaxima) {
        this.velocidadMaxima = velocidadMaxima;
    }

    @Override
    public String toString() {
        return super.toString() + String.format(" - Armamento: %s - Blindaje: %.1f mm - Velocidad Máx: %d km/h - Armamento cargado: %s",
                armamento, blindajeMm, velocidadMaxima, armamentoCargado ? "Sí" : "No");
    }
}