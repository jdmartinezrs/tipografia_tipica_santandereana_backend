# Tipografía Típica de Santander

API REST para administrar el catálogo de tipografías. La aplicación usa Spring Boot, Spring Data JPA y PostgreSQL.

## Arquitectura

El código está organizado como un monolito modular por dominio. Cada módulo agrupa las clases relacionadas con una parte del negocio:

```text
com.miproyecto.backend
├── artista/       model, repository
├── categoria/     model, repository
├── compra/        model
├── descarga/      model
├── licencia/      model
├── peso/          model, repository
├── tipografia/    controller, dto, model, repository, service
└── usuario/       model
```

Las variantes y los archivos de fuentes pertenecen al módulo `tipografia`; el detalle de compra pertenece a `compra`.

El flujo de solicitudes para tipografías es:

```text
HTTP → TipografiaController → DTO + Bean Validation → TipografiaService
     → repositorios Spring Data JPA → PostgreSQL
```

El controlador expone los endpoints y recibe DTOs validados con `@Valid`. El servicio aplica las operaciones transaccionales y coordina los repositorios. Los DTOs separan el contrato HTTP de las entidades JPA.

## Tecnologías y dependencias

- Java 21 y Spring Boot 4.1.1
- `spring-boot-starter-web`: API REST con Spring MVC
- `spring-boot-starter-data-jpa`: persistencia y repositorios JPA
- `spring-boot-starter-validation`: Bean Validation para solicitudes
- `org.postgresql:postgresql` (runtime): driver de PostgreSQL
- `spring-boot-devtools` (runtime, opcional): recarga durante desarrollo
- `spring-boot-starter-test` (test): dependencias para pruebas

## Base de datos y configuración

La aplicación se conecta a PostgreSQL en `localhost:5433`, base `tipografia_tipica_santander`, usuario `juan_admin`. Hibernate valida el esquema existente (`ddl-auto=validate`) y no lo crea ni actualiza.

La contraseña se toma de la variable de entorno `DB_PASSWORD`; no se guarda en el repositorio.

PowerShell:

```powershell
$env:DB_PASSWORD = "tu_contraseña"
mvn spring-boot:run
```

Git Bash:

```bash
export DB_PASSWORD='tu_contraseña'
mvn spring-boot:run
```

## API de tipografías

Base URL: `http://localhost:8080/api/tipografias`

| Método | Ruta | Acción |
|---|---|---|
| GET | `/api/tipografias` | Lista tipografías |
| GET | `/api/tipografias/{id}` | Obtiene una tipografía |
| POST | `/api/tipografias` | Crea una tipografía |
| PUT | `/api/tipografias/{id}` | Actualiza una tipografía |
| DELETE | `/api/tipografias/{id}` | Elimina una tipografía |

Ejemplo de cuerpo para POST y PUT:

```json
{
  "nombre": "Santander Serif",
  "slug": "santander-serif",
  "descripcion": "Serif elegante",
  "imagenPreview": "previews/santander-serif.png",
  "precioFamilia": 48.00,
  "categoriaId": 1,
  "artistaId": 1,
  "estado": "PUBLICADA"
}
```

`precioFamilia` y `estado` pueden ser `null`; el precio ausente representa una fuente sin precio de familia y el estado ausente se inicializa como `OCULTA`. `categoriaId` y `artistaId` deben referir registros existentes.
