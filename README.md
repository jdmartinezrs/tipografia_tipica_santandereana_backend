# Tipografía Típica de Santander

Backend REST para el catálogo de tipografías, desarrollado con Java, Spring Boot, Spring Data JPA y PostgreSQL.

## Requisitos

- JDK 21 o compatible con la versión configurada en `pom.xml`.
- PostgreSQL accesible desde la aplicación.
- Maven Wrapper incluido en el proyecto (`mvnw.cmd` en Windows y `mvnw` en Linux/macOS).

## Ejecutar localmente

La configuración de ejemplo apunta a PostgreSQL en `localhost:5433`, base `tipografia_tipica_santander`, usuario `juan_admin`, y valida que las tablas ya existan (`spring.jpa.hibernate.ddl-auto=validate`). Ajusta la conexión según tu entorno. La aplicación escucha en el puerto 8080.

En PowerShell, desde la carpeta del proyecto:

```powershell
$env:SPRING_DATASOURCE_URL = "jdbc:postgresql://localhost:5433/tipografia_tipica_santander"
$env:SPRING_DATASOURCE_USERNAME = "juan_admin"
$env:SPRING_DATASOURCE_PASSWORD = "tu_clave_local"
.\mvnw.cmd spring-boot:run
```

En Linux o macOS:

```bash
export SPRING_DATASOURCE_URL='jdbc:postgresql://localhost:5433/tipografia_tipica_santander'
export SPRING_DATASOURCE_USERNAME='juan_admin'
export SPRING_DATASOURCE_PASSWORD='tu_clave_local'
./mvnw spring-boot:run
```

Las variables `SPRING_DATASOURCE_*` son propiedades estándar de Spring Boot y permiten configurar la conexión por entorno. `application-example.properties` también documenta el uso de `DB_PASSWORD`. No incluyas contraseñas reales en commits ni en documentación.

## Arquitectura

El backend es un monolito organizado por dominio. El módulo de tipografías implementa el flujo completo HTTP → DTO y validación → servicio transaccional → repositorios JPA. El módulo de artistas sigue esa separación para su CRUD. Otros dominios tienen actualmente entidades y algunos repositorios, y se completarán según los casos de uso requeridos.

```text
com.miproyecto.backend
├── artista/       controller, dto, model, repository, service
├── categoria/     model, repository
├── compra/        model
├── descarga/      model
├── licencia/      model
├── peso/          model, repository
├── tipografia/    controller, dto, model, repository, service
└── usuario/       model
```

Las variantes y archivos de fuentes son entidades del dominio de tipografías. El detalle de compra pertenece a `compra`.

## API disponible

Base URL: `http://localhost:8080`

### Tipografías

| Método | Ruta | Descripción |
|---|---|---|
| GET | `/api/tipografias` | Lista tipografías |
| GET | `/api/tipografias/{id}` | Consulta por ID |
| POST | `/api/tipografias` | Crea una tipografía (201) |
| PUT | `/api/tipografias/{id}` | Actualiza una tipografía |
| DELETE | `/api/tipografias/{id}` | Elimina una tipografía (204) |

Cuerpo de ejemplo para `POST` y `PUT`:

```json
{
  "nombre": "Santander Serif",
  "slug": "santander-serif",
  "descripcion": "Serif elegante",
  "imagenPreview": "previews/santander-serif.png",
  "precioFamilia": 48000.00,
  "categoriaId": 1,
  "artistaId": 1,
  "estado": "OCULTA"
}
```

`nombre` y `slug` son obligatorios. `categoriaId` y `artistaId` deben identificar registros existentes. `precioFamilia` es opcional y, si se envía, debe ser mayor que cero. `estado` acepta `PUBLICADA` u `OCULTA`; si se omite, se usa `OCULTA`.

### Artistas

| Método | Ruta | Descripción |
|---|---|---|
| GET | `/api/artistas` | Lista artistas |
| GET | `/api/artistas/{id}` | Consulta por ID |
| POST | `/api/artistas` | Crea un artista (201) |
| PUT | `/api/artistas/{id}` | Actualiza un artista |
| DELETE | `/api/artistas/{id}` | Elimina un artista (204) |

Cuerpo de ejemplo para `POST` y `PUT`:

```json
{
  "nombre": "Ana Gómez",
  "pais": "Colombia",
  "biografia": "Diseñadora tipográfica.",
  "fotoUrl": "https://ejemplo.com/ana-gomez.jpg",
  "estado": "ACTIVO"
}
```

`nombre` y `pais` son obligatorios. `estado` acepta `ACTIVO` o `INACTIVO` y, si se omite, se usa `ACTIVO`. No se puede eliminar un artista que tenga tipografías asociadas (409). Los IDs inexistentes responden 404.

## Persistencia

Hibernate está configurado para validar el esquema, no para crearlo ni actualizarlo. La base debe estar preparada antes de iniciar la aplicación. Hay un script de normalización PostgreSQL en `src/main/resources/db/normalizar_tipos_postgresql.sql`; revisa su contenido y respáldate antes de aplicarlo a una base con datos.

## Configuración relevante

- `src/main/resources/application.properties`: valores de configuración activa.
- `src/main/resources/application-example.properties`: referencia de configuración con contraseña por variable de entorno.
- `pom.xml`: versión de Java, Spring Boot y dependencias.
- `ANALISIS_ARQUITECTURA.md`: revisión de fortalezas, riesgos y recomendaciones.

En producción, configura credenciales y orígenes CORS mediante el entorno de despliegue, desactiva el registro detallado de SQL y habilita autenticación y autorización antes de exponer endpoints.
