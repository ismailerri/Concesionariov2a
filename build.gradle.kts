plugins {
    id("java")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}
/*concesionario/
├── build.gradle                # Configuración principal de Gradle
├── settings.gradle             # Configuración de settings de Gradle
├── gradlew                     # Script de Gradle Wrapper para Unix
├── gradlew.bat                 # Script de Gradle Wrapper para Windows
├── gradle/                     # Directorio de configuración Gradle
│   └── wrapper/                # Wrapper de Gradle
│       ├── gradle-wrapper.jar
│       └── gradle-wrapper.properties
└── src/
    ├── main/
    │   ├── java/               # Código fuente Java
    │   │   └── com/
    │   │       └── concesionario/
    │   │           ├── Main.java            # Punto de entrada de la aplicación
    │   │           ├── model/               # Capa de modelo (MVP)
    │   │           │   ├── vehiculo/
    │   │           │   │   ├── Vehiculo.java         # Clase abstracta base
    │   │           │   │   ├── Coche.java            # Tipo de vehículo
    │   │           │   │   ├── Tractor.java          # Tipo de vehículo
    │   │           │   │   └── Tanque.java           # Tipo de vehículo
    │   │           │   ├── FabricaVehiculos.java     # Factory para crear vehículos
    │   │           │   ├── Parking.java              # Gestión del parking
    │   │           │   └── GestorDatos.java          # Gestión de datos
    │   │           ├── presenter/                    # Capa de presentador (MVP)
    │   │           │   ├── VehiculosPresenter.java   # Presentador principal
    │   │           │   ├── ParkingPresenter.java     # Presentador del parking
    │   │           │   └── contract/                 # Contratos MVP
    │   │           │       ├── VehiculosContract.java # Contrato del presentador
    │   │           │       └── ParkingContract.java   # Contrato del parking
    │   │           ├── view/                         # Capa de vista (MVP)
    │   │           │   ├── MainView.java             # Vista principal
    │   │           │   ├── components/               # Componentes de UI
    │   │           │   │   ├── ParkingGrid.java      # Grid del parking
    │   │           │   │   ├── VehiculoForm.java     # Formulario de vehículo
    │   │           │   │   └── VehiculoList.java     # Lista de vehículos
    │   │           │   └── dialogs/                  # Diálogos
    │   │           │       ├── CrearVehiculoDialog.java  # Diálogo de creación
    │   │           │       ├── EditarVehiculoDialog.java # Diálogo de edición
    │   │           │       └── ParkingDialog.java        # Diálogo del parking
    │   │           ├── db/                           # Capa de base de datos
    │   │           │   ├── DBInitializer.java        # Inicializador de BD
    │   │           │   ├── dao/                      # Data Access Objects
    │   │           │   │   ├── VehiculoDAO.java      # Interfaz DAO
    │   │           │   │   ├── VehiculoDAOSql.java   # Implementación SQL
    │   │           │   │   └── VehiculoDAOMongo.java # Implementación MongoDB
    │   │           │   └── entity/                   # Entidades de BD
    │   │           │       └── VehiculoEntity.java   # Entidad Vehículo
    │   │           └── util/                         # Utilidades
    │   │               ├── StringUtils.java          # Utilidades para Strings
    │   │               └── Constants.java            # Constantes
    │   └── resources/                                # Recursos
    │       ├── config.properties                     # Configuración de BD
    │       ├── images/                               # Imágenes
    │       │   ├── coche.png
    │       │   ├── tractor.png
    │       │   └── tanque.png
    │       └── styles/                               # Estilos
    │           └── application.css                   # CSS para la aplicación
    └── test/                                         # Tests unitarios
        └── java/
            └── com/
                └── concesionario/
                    ├── model/                        # Tests de modelo
                    │   ├── VehiculoTest.java
                    │   └── ParkingTest.java
                    ├── presenter/                    # Tests de presentador
                    │   └── VehiculosPresenterTest.java
                    └── db/                           # Tests de DB
                        └── VehiculoDAOTest.java*/