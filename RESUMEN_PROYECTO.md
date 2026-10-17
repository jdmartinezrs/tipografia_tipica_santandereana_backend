# Resumen del Proyecto - Casa Tipografía Santandereana

## Fecha
17 de Octubre de 2026

## Inicio del Proyecto

### Configuración Inicial
- **Ubicación del proyecto**: `C:\Users\Windows 10\Documents\juan\demo`
- **Tecnología**: Spring Boot 4.1.1
- **Java**: 25.0.4.1 (Eclipse Adoptium)
- **Maven**: 3.10.0
- **Build Tool**: Maven Wrapper (mvnw.cmd)

### Configuración de Base de Datos
- **Motor**: MySQL 9.3
- **Base de datos**: `casatipografiasantandereana`
- **Usuario**: `Ficha_3234206`
- **URL JDBC**: `jdbc:mysql://localhost:3306/casatipografiasantandereana?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=America/Bogota`
- **Puerto de aplicación**: 8080

### Archivo de Configuración
`src/main/resources/application.properties`:
```properties
spring.application.name=demo
spring.datasource.url=jdbc:mysql://localhost:3306/casatipografiasantandereana?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=America/Bogota
spring.datasource.username=Ficha_3234206
spring.datasource.password=claveProyecto
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
server.port=8080
```

## APIs Desarrolladas

### 1. API de Productos (Existente)
**Controller**: `ProductoController.java`
**Endpoints**:
- `GET /api/productos` - Listar todos los productos
- `GET /api/productos/{id}` - Obtener producto por ID
- `POST /api/productos` - Crear nuevo producto
- `PUT /api/productos/{id}` - Actualizar producto
- `DELETE /api/productos/{id}` - Eliminar producto

**Modelo**: `Producto.java`
- `id` (Long) - Primary key
- `nombre` (String, max 120 caracteres)
- `precio` (BigDecimal, precision 12, scale 2)

### 2. API de Tipografías (Nuevo)
**Controller**: `TipografiaController.java`
**Endpoints**:
- `GET /api/tipografias` - Listar todas las tipografías
- `GET /api/tipografias/{id}` - Obtener tipografía por ID
- `POST /api/tipografias` - Crear nueva tipografía
- `PUT /api/tipografias/{id}` - Actualizar tipografía
- `DELETE /api/tipografias/{id}` - Eliminar tipografía

**Modelo**: `Tipografia.java`
- `tipografiaId` (Integer) - Primary key
- `nombre` (String, max 100 caracteres)
- `precio` (BigDecimal, precision 10, scale 2)
- `categoria` (Enum CategoriaTipografia)
- `artista` (Artista) - Foreign key
- `peso` (Peso) - Foreign key

### 3. Entidades Relacionadas

#### Artista
**Modelo**: `Artista.java`
- `artistaId` (Integer) - Primary key
- `nombre` (String, max 100 caracteres)

**Repository**: `ArtistaRepository.java`

#### Peso
**Modelo**: `Peso.java`
- `pesoId` (Integer) - Primary key
- `nombre` (String, max 50 caracteres)

**Repository**: `PesoRepository.java`

#### CategoriaTipografia (Enum)
**Modelo**: `CategoriaTipografia.java`
- `SERIF` ("Serif")
- `SANS_SERIF` ("Sans Serif")
- `SCRIPT` ("Script")
- `DISPLAY` ("Display")
- `MONOSPACE` ("Monospace")

**Converter**: `CategoriaTipografiaConverter.java`
- Convierte entre valores de enum y strings con espacios para la base de datos

## Estructura de Tablas en MySQL

### Tabla producto
```sql
CREATE TABLE producto (
    id BIGINT NOT NULL AUTO_INCREMENT,
    nombre VARCHAR(120) NOT NULL,
    precio DECIMAL(12,2) NOT NULL,
    PRIMARY KEY (id)
) ENGINE=InnoDB;
```

### Tabla tipografia
```sql
CREATE TABLE tipografia (
    tipografia_id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    precio DECIMAL(10,2) NOT NULL,
    categoria VARCHAR(20) NOT NULL,
    artista_id INT NOT NULL,
    peso_id INT NOT NULL,
    CONSTRAINT fk_tipografia_artista FOREIGN KEY (artista_id) REFERENCES Artista(artista_id),
    CONSTRAINT fk_tipografia_peso FOREIGN KEY (peso_id) REFERENCES Peso(peso_id)
) ENGINE=InnoDB;
```

### Tabla artista
```sql
CREATE TABLE artista (
    artista_id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL
) ENGINE=InnoDB;
```

### Tabla peso
```sql
CREATE TABLE peso (
    peso_id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL
) ENGINE=InnoDB;
```

## Problemas Resueltos

### 1. Nombre de Base de Datos
**Problema**: El nombre de la base de datos en la configuración (`casatipografiaSantandereana`) no coincidía con el nombre real en MySQL (`casatipografiasantandereana` - todo minúsculas).

**Solución**: Se corrigió el nombre en `application.properties` para coincidir exactamente con el nombre en MySQL.

### 2. Permisos de Administrador
**Problema**: Windows Firewall/Defender solicitaba permisos de administrador al intentar usar el puerto 8080.

**Solución**: Se cambió el puerto a diferentes alternativas (8081, 8082, 8083, 9090) hasta encontrar uno disponible. Finalmente se mató el proceso en el puerto 8080 y se configuró la aplicación en ese puerto.

### 3. Conversión de Enum con Espacios
**Problema**: La base de datos tenía valores de categoría con espacios (ej: "Serif", "Sans Serif"), pero el enum Java inicial usaba mayúsculas sin espacios (SERIF, SANS_SERIF), causando error: `No enum constant com.miproyecto.backend.model.CategoriaTipografia.Serif`.

**Solución**:
- Se modificó el enum `CategoriaTipografia` para incluir nombres de display con espacios
- Se creó un `AttributeConverter` (`CategoriaTipografiaConverter`) para manejar la conversión entre valores de la base de datos y el enum Java
- Se configuró el converter con `@Converter(autoApply = true)` para aplicarse automáticamente

## Comandos Ejecutados

### Verificar Maven Wrapper
```bash
./mvnw.cmd -v
```

### Ejecutar Aplicación
```bash
cd demo
./mvnw.cmd spring-boot:run
```

### Ver Base de Datos MySQL
```bash
mysql -u Ficha_3234206 -p
SHOW DATABASES;
```

### Matar Proceso en Puerto
```bash
netstat -ano | findstr :8080
taskkill //F //PID <PID>
```

## Endpoints Disponibles para Pruebas

### Productos
- **GET All**: `http://localhost:8080/api/productos`
- **GET by ID**: `http://localhost:8080/api/productos/{id}`
- **POST**: `http://localhost:8080/api/productos`
- **PUT**: `http://localhost:8080/api/productos/{id}`
- **DELETE**: `http://localhost:8080/api/productos/{id}`

### Tipografías
- **GET All**: `http://localhost:8080/api/tipografias`
- **GET by ID**: `http://localhost:8080/api/tipografias/{id}`
- **POST**: `http://localhost:8080/api/tipografias`
- **PUT**: `http://localhost:8080/api/tipografias/{id}`
- **DELETE**: `http://localhost:8080/api/tipografias/{id}`

## Archivos Creados/Modificados

### Archivos Nuevos
- `src/main/java/com/miproyecto/backend/model/Artista.java`
- `src/main/java/com/miproyecto/backend/model/Peso.java`
- `src/main/java/com/miproyecto/backend/model/Tipografia.java`
- `src/main/java/com/miproyecto/backend/model/CategoriaTipografia.java`
- `src/main/java/com/miproyecto/backend/model/CategoriaTipografiaConverter.java`
- `src/main/java/com/miproyecto/backend/repository/ArtistaRepository.java`
- `src/main/java/com/miproyecto/backend/repository/PesoRepository.java`
- `src/main/java/com/miproyecto/backend/repository/TipografiaRepository.java`
- `src/main/java/com/miproyecto/backend/controller/TipografiaController.java`

### Archivos Modificados
- `src/main/resources/application.properties` - Configuración de base de datos y puerto

## Estado Actual
✅ Aplicación Spring Boot corriendo correctamente en puerto 8080
✅ Conexión a MySQL establecida y funcional
✅ Tablas creadas automáticamente por Hibernate (ddl-auto=update)
✅ APIs de Productos y Tipografías disponibles
✅ Conversión de enum con espacios implementada correctamente
✅ Listo para pruebas con Thunder Client
