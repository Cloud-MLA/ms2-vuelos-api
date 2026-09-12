# ms2-vuelos-api

MS2 — Vuelos / Operaciones · Java + Spring Boot + PostgreSQL 16

Parte del Proyecto Parcial CS2032 — Cloud Computing (2026-2). Contexto y arquitectura: [`cloud-computing-proyecto`](https://github.com/btoroled/cloud-computing-proyecto) · Plan de tareas: [`plan/backend.md` §5](https://github.com/btoroled/cloud-computing-proyecto/blob/main/docs/plan/backend.md) · Dueño: Mariano.

> **Bloqueante del Backend:** `GET /vuelos/{id}/exists` (MS2-07) habilita MS1-05, MS3-05 y MS4-02. **✅ Ya disponible.**

## Estado del proyecto

Fase

Tarea

Estado

F0

MS2-01 Scaffold Spring Boot + Docker + endpoint de prueba

✅

F0

MS2-02 Entidades JPA + migraciones Flyway (8 tablas) + E/R

✅

F1

MS2-03 CRUD aerolíneas

✅

F1

MS2-04 CRUD aeronaves + asientos

✅

F1

MS2-05 CRUD vuelos + filtros combinables

✅

F1

MS2-06 Máquina de estados (`PATCH /vuelos/{id}/estado`)

✅

F1

MS2-07 `GET /vuelos/{id}/exists`

✅

F1

MS2-08 Empleados + tripulación (N–M)

✅

F1

Seed ~2,000 vuelos

✅

F2

MS2-09 Carga masiva ≥20,000 (vuelo, asiento)

⬜ Pendiente

F2

MS2-10 Tuning JVM t3.small

✅ (ya incluido en el Dockerfile desde F0)

F2

MS2-11 Swagger + pruebas `@SpringBootTest`

⚠️ Swagger listo, tests pendientes

F2

MS2-12 README + tag v1.0 → GHCR

🔶 En progreso (este documento)

F2

DS-07 ingesta-ms2 (Postgres → CSV → S3)

⬜ Bloqueado por DS-04 (bucket S3, a cargo de Benja)

## Puesta en marcha (base desde la plantilla · BE-TX-02)

### Requisitos

-   Docker Desktop
-   (Opcional) Python 3.10+ si quieres correr el script de seed

### Levantar local (app + Postgres en contenedores)

```bash
git clone <url-de-tu-fork-o-el-repo>
cd ms2-vuelos-api
cp .env.example .env
docker compose up --build
```

Esto levanta:

-   **Postgres 16** en el puerto `5434` (mapeado desde el `5432` interno, para no chocar con instalaciones locales)
-   **La API** en el puerto `8002`

Flyway aplica automáticamente las 8 migraciones al arrancar (no se requiere ningún paso manual).

### Verificar que todo funciona

```bash
curl http://localhost:8002/actuator/health
curl http://localhost:8002/api/vuelos/ping
```

### Swagger / OpenAPI

[http://localhost:8002/docs](http://localhost:8002/docs)

JSON crudo de la especificación:

[http://localhost:8002/v3/api-docs](http://localhost:8002/v3/api-docs)

### Postman

Colección lista para importar en `postman/MS2-Vuelos-API.postman_collection.json`. Incluye los 15 endpoints organizados por recurso, con bodies de ejemplo. Solo edita la variable `baseUrl` de la colección según dónde esté corriendo tu instancia.

### Cargar datos de prueba (seed)

Genera ~2,000 vuelos + aerolíneas, aeronaves, asientos, empleados/tripulación coherentes entre sí, con un `SEED` fijo (reproducible):

```bash
cd seed
pip install -r requirements.txt
python generar_seed.py
```

> Ajusta `DB_HOST`/`DB_PORT` en `generar_seed.py` según dónde corras el script (`localhost:5434` si corres el script en tu máquina apuntando al Postgres del `docker-compose` local; `localhost:5432` si lo corres dentro de la red de Docker).

## Despliegue de prueba (VM de Mariano)

Mientras la VM-DB oficial del equipo (a cargo de Benja) no esté lista, la API se prueba en una VM propia con Postgres corriendo en el mismo contenedor temporalmente.

-   Imagen publicada en Docker Hub: `marianoutec/ms2-api-app:latest`
-   URL de prueba: `http://<IP-de-la-VM>:8002`

Cuando la VM-DB esté disponible, el cambio consiste en editar el `.env` (host/usuario/contraseña reales) y quitar el servicio `postgres` del `docker-compose.yml` de la VM — no requiere tocar código.

## Arquitectura de la aplicación

src/main/java/.../ms2_vuelos_api/ ├── model/ 8 entidades JPA (Aerolinea, Aeronave, Vuelo, Asiento, │ Empleado, Tripulacion, OperativoTierra, OperaTripulacion) │ + OperaTripulacionId (clase auxiliar para PK compuesta) ├── repository/ Interfaces JpaRepository + Specifications (filtros dinámicos) ├── dto/ Request/Response — nunca se expone la entidad JPA directamente ├── service/ Lógica de negocio y validaciones ├── controller/ Endpoints REST └── exception/ GlobalExceptionHandler + excepciones de dominio, en el formato del contrato de errores común

### Por qué SQL (Flyway) y entidades Java (JPA) a la vez

Cumplen roles distintos, no se duplican:

-   Los scripts SQL de Flyway (`src/main/resources/db/migration/`) crean la tabla **física** en Postgres.
-   Las entidades Java (`@Entity`) le indican a Spring Boot cómo leer y escribir esa tabla desde el código.

`ddl-auto=validate` compara ambos mundos al arrancar la aplicación, para confirmar que la estructura de las entidades coincide exactamente con las tablas reales — si no coinciden, la app no arranca (evita inconsistencias silenciosas).

### Por qué hay 9 clases en `model/` si el modelo tiene 8 entidades

`OperaTripulacionId` no es una entidad ni representa una tabla — es una clase auxiliar requerida por JPA para modelar la llave primaria compuesta de `opera_tripulacion` (`vuelo_id` + `tripulacion_empleado_id`), vía `@IdClass`.

### Filtros combinables en `GET /vuelos`

Implementados con `JpaSpecificationExecutor` (`repository/spec/VueloSpecifications.java`): construye la consulta SQL dinámicamente según qué parámetros llegan (`num`, `estado`, `tipo`, `fecha`), combinándolos con `AND` — sin necesidad de un método por cada combinación posible.

### Generación del `id` de vuelo

Como el rango de `vuelo.id` es fijo y compartido (1–25,000), el siguiente ID se calcula con `SELECT COALESCE(MAX(id), 0) + 1` (vía `@Query` en el repository) en vez de traer todas las filas a memoria. Es simple y suficiente para el alcance actual (pruebas manuales, Hito 1); no es 100% seguro ante escrituras concurrentes simultáneas — se documenta como limitación conocida, no como bug.

### Máquina de estados (`PATCH /vuelos/{id}/estado`)

Definida en `service/EstadoVueloMaquina.java`:

Programado → Embarcando, Retrasado, Cancelado Embarcando → Despegado, Retrasado, Cancelado Retrasado → Embarcando, Despegado, Cancelado Despegado → Aterrizado Aterrizado → (estado final) Cancelado → (estado final)

Una transición fuera de esta tabla responde `422 TRANSICION_ESTADO_INVALIDA`. Al pasar a `Despegado`, `horaReal` se registra automáticamente con la hora actual.

## Convenciones

- **Puerto interno:** `8002`. **Health:** `/actuator/health`.
- **Errores:** formato común (`@RestControllerAdvice` en `exception/GlobalExceptionHandler.java`), ver [contrato de errores](https://github.com/btoroled/cloud-computing-proyecto/blob/main/docs/contratos/errores.md).
- **Enums y rangos de ID:** [diccionario compartido](https://github.com/btoroled/cloud-computing-proyecto/blob/main/docs/contratos/enums.md) (`vuelo.id` 1–25,000).
-   **Imagen:** `git tag vX.Y && git push --tags` → `ghcr.io/cloud-mla/ms2-vuelos-api:vX.Y`.

## Endpoints implementados

| Método | Ruta | Descripción |
|---|---|---|
| GET | `/actuator/health` | Estado del servicio |
| GET | `/api/vuelos/ping` | Endpoint de prueba |
| GET | `/api/vuelos/aerolineas` · `/{ruc}` | Listar / buscar aerolínea |
| POST | `/api/vuelos/aerolineas` | Crear aerolínea |
| GET | `/api/vuelos/aeronaves` · `/{placa}` | Listar / buscar aeronave |
| GET | `/api/vuelos/aeronaves/{placa}/asientos` | Asientos por avión |
| POST | `/api/vuelos/aeronaves` | Crear aeronave |
| GET | `/api/vuelos` | Listar vuelos, filtros `?num=&estado=&tipo=&fecha=` (combinables) |
| GET | `/api/vuelos/{id}` | Buscar vuelo por id |
| GET | `/api/vuelos/{id}/exists` | Endpoint liviano de validación para MS1/MS3 |
| POST | `/api/vuelos` | Crear vuelo |
| PATCH | `/api/vuelos/{id}/estado` | Cambiar estado (máquina de estados) |
| GET | `/api/vuelos/{id}/tripulacion` | Tripulación asignada a un vuelo |
| POST | `/api/vuelos/{id}/tripulacion` | Asignar tripulante a un vuelo |
| GET | `/api/vuelos/empleados/{id}` · `?tipo=Tripulacion` | Consultar empleados |

## Errores comunes probados

| Código | HTTP | Caso |
|---|---|---|
| `VALIDACION` | 400 | Campo con formato/valor inválido (ej. `alianza` fuera del enum) |
| `NO_ENCONTRADO` | 404 | Recurso por ID/RUC/placa inexistente |
| `CONFLICTO_ESTADO` | 409 | Recurso duplicado (ej. RUC ya existe) |
| `TRANSICION_ESTADO_INVALIDA` | 422 | Cambio de estado no permitido por la máquina de estados |

## Pendientes (Fase 2)

- [ ] MS2-09: generador + carga masiva ≥20,000 en `vuelo` y `asiento`
- [ ] MS2-11: pruebas `@SpringBootTest`
- [ ] MS2-12: tag `v1.0` y publicación en GHCR
- [ ] DS-07: contenedor `ingesta-ms2` (Postgres → CSV → S3), bloqueado hasta que exista el bucket (DS-04, a cargo de Benja)
- [ ] Migrar despliegue de prueba desde VM propia hacia la VM-DB oficial del equipo cuando esté lista
