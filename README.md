# OpenSkyAirlines

**OpenSkyAirlines** es una aplicación de línea de comandos (CLI) para gestionar vuelos de forma sencilla y eficiente. Está diseñada para conectarse a una base de datos **MariaDB** y facilita la administración de vuelos a través de comandos intuitivos.

## 🧰 Tecnologías utilizadas

- **Java** (versión 22)
- **MariaDB** (como sistema de gestión de base de datos)
- **Docker** y **Docker Compose** (para facilitar la instalación y el despliegue)
- **JDBC** (para la conexión a la base de datos)
- **JUnit 5** (para pruebas unitarias)
- **Maven** (como gestor de dependencias y construcción del proyecto)


## 📋 Requisitos previos

- Java 22 instalado ([Guía oficial de instalación](https://jdk.java.net/22/))
- Docker y Docker Compose instalados
- Tener el contenedor de la base de datos corriendo

### 1. Instala Docker

Para utilizar esta aplicación, primero necesitas tener **Docker** y **Docker Compose** instalados. Puedes seguir esta guía oficial para instalar Docker en tu sistema operativo:

👉 [Guía de instalación de Docker](https://docs.docker.com/get-docker/)

### 2. Levanta la base de datos

Una vez que tengas Docker instalado, simplemente ejecuta el siguiente comando en la raíz del proyecto para levantar la base de datos:

```bash
docker-compose up -d
```


## 🧪 Compilación y ejecución

```bash
mvn clean install
java -jar target/openskyairlines.jar
```