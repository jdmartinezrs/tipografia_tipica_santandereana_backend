# Estado actual del proyecto

Este documento resume la implementación vigente. Para instrucciones de instalación y ejecución, consultar [README.md](README.md). Para evaluación técnica y próximos pasos, consultar [ANALISIS_ARQUITECTURA.md](ANALISIS_ARQUITECTURA.md).

## Stack y ejecución

- Java configurado en `pom.xml`: 21.
- Spring Boot configurado en `pom.xml`: 4.1.1.
- Persistencia: PostgreSQL con Spring Data JPA.
- Esquema: Hibernate usa `ddl-auto=validate`; las tablas deben existir antes de iniciar.
- Puerto HTTP: 8080.
- Ejecución: Maven Wrapper (`.\mvnw.cmd spring-boot:run` en Windows).

## Estructura actual

El código está organizado por dominio bajo `com.miproyecto.backend`:

- `tipografia`: controlador REST, DTOs, servicio, repositorio y entidades de tipografía, variante y archivo de fuente.
- `artista`: CRUD REST con controlador, DTOs, servicio y repositorio.
- `categoria` y `peso`: entidades y repositorios.
- `compra`, `descarga`, `licencia` y `usuario`: entidades del dominio actualmente representadas en el código.

Los flujos implementados para tipografías y artistas se describen en README. La presencia de una entidad no implica que ya exista una API CRUD o un flujo de negocio para ese dominio.

## Base de datos

La configuración de referencia está en `src/main/resources/application-example.properties`. Ajusta URL, usuario y contraseña por entorno. No guardar credenciales reales en el repositorio. El esquema debe corresponder a las entidades JPA; el proyecto no crea ni actualiza automáticamente las tablas.

## Convenciones observadas

- Controladores REST reciben DTOs, y usan Bean Validation donde está implementada.
- Servicios coordinan operaciones y límites transaccionales.
- Repositorios usan Spring Data JPA.
- Las respuestas HTTP de catálogo no exponen directamente las entidades JPA.

## Pendientes conocidos

- Unificar el manejo de errores HTTP y agregar pruebas automatizadas.
- Incorporar migraciones versionadas para cambios del esquema.
- Completar reglas y servicios de compra, pago, descarga y autenticación antes de habilitar esos flujos.
- Revisar la configuración de secretos, SQL logging, CORS y seguridad para cada entorno.
