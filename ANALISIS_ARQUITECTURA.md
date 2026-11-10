# Análisis de la arquitectura actual

## Alcance

Revisión estática del backend ubicado en `src/main/java` y de su configuración y documentación. No incluye ejecución de la aplicación, inspección de la base de datos ni pruebas.

## Resumen

El proyecto tiene una base razonable para un backend pequeño: está organizado por dominios, usa Spring Boot, JPA, validación de entrada y DTOs en el módulo más desarrollado. La estructura apunta a un monolito modular, una elección simple y adecuada para el tamaño actual.

La arquitectura todavía está incompleta y desigual: tipografías concentra las capas HTTP y de negocio; artistas ya tiene un CRUD similar; los demás dominios son principalmente entidades y algunos repositorios. Antes de sumar funciones de negocio críticas, conviene cerrar la configuración de secretos, alinear el modelo y el esquema, y establecer una estrategia consistente de errores, pruebas y migraciones.

## Lo bueno

- **Organización por dominio.** Paquetes como `tipografia`, `artista`, `compra` y `usuario` mantienen juntas las piezas del mismo concepto y dejan espacio para completar cada módulo sin introducir microservicios prematuramente.
- **Separación de responsabilidades en tipografías.** `TipografiaController` maneja HTTP, `TipografiaService` coordina operaciones y transacciones, los repositorios abstraen persistencia y los DTOs definen el contrato de la API.
- **Las entidades no se exponen directamente en la API de tipografías.** Esto reduce el acoplamiento entre el esquema JPA y las respuestas HTTP.
- **Validación declarativa de entrada.** `@Valid` y Bean Validation verifican campos esenciales antes de ejecutar el servicio.
- **Relaciones perezosas y lectura explícita para tipografías.** `ManyToOne(fetch = LAZY)` evita cargar relaciones automáticamente en cada consulta y `@EntityGraph` declara las relaciones necesarias para construir las respuestas.
- **Control de concurrencia en tipografías.** `@Version` permite detectar actualizaciones concurrentes en vez de sobrescribir silenciosamente cambios.
- **Validación del esquema existente.** `ddl-auto=validate` evita que Hibernate modifique tablas por su cuenta al iniciar.
- **El CRUD de artistas sigue el mismo flujo general.** Cuenta con DTOs, validación, servicio transaccional y controlador, y comprueba la relación con tipografías antes de permitir el borrado.

## Lo malo o incompleto

- **Hay una credencial de base de datos escrita directamente en `application.properties`.** Si el archivo se versionó o la clave se compartió, debe considerarse expuesta y rotarse; luego debe leerse de una variable de entorno o de un gestor de secretos. `application-example.properties` ya muestra una alternativa con `DB_PASSWORD`, pero la configuración activa no la sigue.
- **Las capas están implementadas solo en parte.** Artista, categoría, compra, descarga, licencia, peso y usuario no tienen el mismo nivel de API/servicio que tipografía. Esto es normal durante el desarrollo, pero todavía no constituye una arquitectura uniforme para todo el dominio.
- **La documentación quedó desactualizada y se contradice.** `README.md` describe PostgreSQL, Java 21 y la configuración actual; `RESUMEN_PROYECTO.md` habla de MySQL, credenciales, modelos y estados previos. Puede inducir a ejecutar comandos equivocados o a creer que el esquema actual es otro.
- **El modelo parece conservar conceptos de dos iteraciones.** `Tipografia` tiene un campo `peso` marcado `@Transient`, mientras el modelo actual contiene `Variante` relacionada con `Peso`. También coexisten `CategoriaTipografia` (enum) y la entidad `Categoria`. Hay que confirmar cuál es el modelo vigente y retirar o documentar lo que ya no use la aplicación.
- **No existe un formato común de errores HTTP.** Los servicios lanzan `ResponseStatusException`, pero no hay un manejador global que garantice respuestas uniformes para validaciones, registros inexistentes, conflictos de unicidad o errores de integridad.



- **Las lecturas no están preparadas para catálogos grandes.** `findAll()` devuelve todos los registros. Para tipografías, artistas y otros listados públicos será necesario paginar y definir orden y filtros.
- **Faltan límites de validación en algunos campos.** Por ejemplo, la descripción de tipografía no tiene tamaño máximo declarado y no se valida formato de URL en campos de imagen. La validación debe corresponder a los límites reales de las columnas y a las reglas de producto.
- **La política CORS está fija a `http://localhost:4200`.** Es cómoda en desarrollo, pero conviene externalizar los orígenes por entorno y definir con precisión los métodos y encabezados permitidos al desplegar.


- **Las entidades de usuario y compra contienen datos sensibles y financieros sin una capa de aplicación visible.** Hay campos de autenticación y estados de pago, pero no se observan aquí servicios, autorización, flujos de pago ni controles que garanticen el cálculo inmutable de totales y regalías.
- **No se ve una herramienta de migraciones.** `ddl-auto=validate` valida, pero no documenta ni aplica cambios de esquema. El SQL de normalización aislado no reemplaza un historial reproducible de migraciones.
- **La configuración activa registra SQL.** `spring.jpa.show-sql=true` resulta útil en desarrollo, pero puede generar ruido y exponer datos en logs si se mantiene en producción.

- **No se encontraron pruebas de aplicación en el código revisado.** La dependencia de pruebas existe, pero la estructura de pruebas no aparece en el listado. Esto deja sin protección los contratos, reglas y restricciones de negocio al cambiar el código.

## Qué recomiendo hacer

### Prioridad inmediata

1. **Sacar la clave de `application.properties` y rotarla.** Usar variables de entorno para todos los entornos y evitar incluir secretos en documentación, logs o commits.
2. **Unificar la documentación.** Actualizar `README.md` como guía operativa vigente y archivar o reescribir `RESUMEN_PROYECTO.md` para que no describa MySQL ni una versión antigua del modelo.
3. **Definir el modelo vigente de tipografías.** Confirmar si el peso pertenece a `Variante`; si es así, eliminar el campo transitorio y retirar el enum de categoría si ya no se utiliza, una vez comprobado que ningún flujo dependa de ellos.
4. **Añadir manejo común de errores.** Un `@RestControllerAdvice` puede devolver un formato estable para validación, `404`, conflictos (`409`) y errores de integridad sin filtrar detalles internos.
9. **Diseñar seguridad antes de publicar la API.** Definir autenticación, autorización por rol, acceso a archivos y datos personales. CORS por sí solo no protege endpoints.

### Siguiente etapa

5. **Adoptar migraciones versionadas.** Incorporar Flyway o Liquibase y crear una migración inicial que refleje el esquema real; aplicar cambios futuros mediante nuevas migraciones.
6. **Acordar convenciones para los módulos.** Para cada caso de uso expuesto: controlador, DTO de entrada/salida, servicio transaccional y repositorio. No es necesario crear endpoints CRUD para entidades internas si el producto no los requiere.
7. **Agregar paginación y filtros a los listados.** Usar `Pageable` o un contrato equivalente, con límites máximos y orden estable.
8. **Proteger las reglas de compra.** Calcular y validar importes en el servidor, guardar una instantánea de precio, licencia y distribución de regalías al momento de comprar, y evitar que cambios posteriores al catálogo alteren el historial.
9. **Diseñar seguridad antes de publicar la API.** Definir autenticación, autorización por rol, acceso a archivos y datos personales. CORS por sí solo no protege endpoints.
10. **Crear pruebas por capas.** Pruebas de servicio para reglas y transacciones, de repositorio para restricciones y relaciones, y de controlador para códigos HTTP y validación. Priorizar CRUD de catálogo y compra/pago.
11. **Separar configuración por entorno.** Mantener valores locales en configuración externa y desactivar SQL detallado en producción; configurar CORS y credenciales por ambiente.

## Estructura objetivo sugerida

Conservar el monolito modular y completar solo los módulos que el producto necesita:

```text
com.miproyecto.backend
├── artista/
│   ├── controller/
│   ├── dto/
│   ├── model/
│   ├── repository/
│   └── service/
├── tipografia/
│   ├── controller/
│   ├── dto/
│   ├── model/
│   ├── repository/
│   └── service/
├── compra/
├── usuario/
└── shared/
    └── error/   # respuestas y manejo global de errores
```

No hace falta forzar todas las carpetas desde el principio. La prioridad es que cada flujo expuesto tenga una ruta clara desde HTTP hasta persistencia, con reglas de negocio en servicios y contratos independientes de las entidades.

## Evaluación general

**Estado: buena base de monolito modular en etapa temprana; lista para seguir desarrollando el catálogo, pero necesita endurecer configuración, consistencia y reglas antes de producción o de procesar compras reales.**

La secuencia que más valor aporta ahora es: secretos y documentación, modelo de catálogo, errores y migraciones, después pruebas y seguridad de los flujos de compra.
