# Concesionario de Vehículos

Sistema de gestión para concesionarios de vehículos, desarrollado en Java con Gradle.

## Descripción

Esta aplicación permite gestionar un concesionario de vehículos, combinando una fábrica de vehículos con un sistema de gestión de parking. El programa ofrece múltiples funcionalidades para crear, gestionar, visualizar, modificar, mover, disparar y eliminar vehículos, todo con persistencia en bases de datos SQL y MongoDB.

## Características

- **Gestión de vehículos:** Creación, modificación y eliminación de tres tipos de vehículos: Coches, Tractores y Tanques militares.
- **Parking 9x9:** Interfaz gráfica para visualizar y gestionar la distribución de vehículos en un parking de 81 plazas.
- **Persistencia de datos:** Almacenamiento en bases de datos SQL (MySQL) y MongoDB.
- **Funcionalidades específicas:** Cada tipo de vehículo tiene acciones especiales (ej: los tanques pueden disparar a otros vehículos).

## Estructura del Proyecto

El proyecto sigue el patrón MVP (Modelo-Vista-Presentador):

- **Modelo:** Clases que representan los datos y la lógica de negocio.
- **Vista:** Interfaz gráfica desarrollada con Swing.
- **Presentador:** Intermediario entre el modelo y la vista, maneja la lógica de presentación.

## Requisitos

- Java 11 o superior
- Gradle 7.0 o superior
- MySQL (opcional, para persistencia en SQL)
- MongoDB (opcional, para persistencia en MongoDB)

## Configuración

1. Clonar el repositorio:
   ```
   git clone https://github.com/yourusername/concesionario.git
   ```

2. Configurar las bases de datos (opcional):
    - Editar el archivo `src/main/resources/config.properties` con tus credenciales:
   ```
   # Configuración de base de datos MySQL
   jdbc.url=jdbc:mysql://localhost:3306
   jdbc.usuario=tuUsuario
   jdbc.password=tuContraseña

   # Configuración de MongoDB
   mongodb.uri=mongodb://localhost:27017
   mongodb.database=concesionario
   mongodb.collection=vehiculos

   # Otros ajustes
   usar.memoria=true
   ```

3. Compilar el proyecto:
   ```
   ./gradlew build
   ```

## Ejecución

Para ejecutar la aplicación:

```
./gradlew run
```

O para limpiar, compilar y ejecutar en un solo paso:

```
./gradlew cleanCompileRun
```

## Funcionalidades Principales

### Creación de Vehículos
- La aplicación permite crear conjuntos de tres vehículos (un coche, un tractor y un tanque).
- Cada vehículo tiene atributos comunes (matrícula, marca, potencia, año, precio) y específicos según su tipo.

### Parking de Vehículos
- Sistema de parking con 81 plazas (matriz 9x9).
- Interfaz gráfica que visualiza los vehículos con códigos de colores.
- Opciones de estacionamiento y retiro de vehículos.

### Funciones Especiales
- **Coches:** Encender/apagar motor y luces.
- **Tractores:** Cambiar artilugio (Aplanador, Arador, Regador).
- **Tanques:** Cargar armamento y disparar en cuatro direcciones.

## Contribución

1. Hacer fork del repositorio
2. Crear una rama para tu funcionalidad: `git checkout -b feature/nueva-funcionalidad`
3. Hacer commit de tus cambios: `git commit -am 'Añadir nueva funcionalidad'`
4. Hacer push a la rama: `git push origin feature/nueva-funcionalidad`
5. Enviar un pull request

## Licencia

Este proyecto está licenciado bajo la licencia MIT - ver el archivo [LICENSE](LICENSE) para más detalles.