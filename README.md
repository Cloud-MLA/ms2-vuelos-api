# ms2-vuelos-api
MS2 — Vuelos / Operaciones · Java + Spring Boot + PostgreSQL 16

Parte del Proyecto Parcial CS2032 — Cloud Computing (2026-2).
Contexto y arquitectura: [`cloud-computing-proyecto`](https://github.com/btoroled/cloud-computing-proyecto) ·
Plan de tareas: [`plan/backend.md` §5](https://github.com/btoroled/cloud-computing-proyecto/blob/main/docs/plan/backend.md) ·
Dueño: Mariano.

> **Bloqueante del Backend:** `GET /vuelos/{id}/exists` (MS2-07) habilita MS1-05, MS3-05 y MS4-02. Priorizar.

## Puesta en marcha (base desde la plantilla · BE-TX-02)

Ya trae los archivos comunes: `.editorconfig`, `.gitignore`, `.env.example`,
`.github/workflows/build-push-ghcr.yml`. Fuente: [plantilla común](https://github.com/Cloud-MLA/aeropuerto-infra-deploy/tree/main/plantilla).

**Pendiente de scaffold (MS2-01, Mariano):**
- Copiar `plantilla/docker/Dockerfile.java` como `Dockerfile` (multi-stage Maven + Temurin 21).
- Proyecto Spring Boot: `web`, `data-jpa`, `actuator`, `springdoc`, Flyway.
- `docker-compose.yml` local: app + `postgres:16`.
- `GET /actuator/health` y `GET /docs` (springdoc).
- `openapi.yaml` borrador (BE-TX-03).

## Convenciones

- **Puerto interno:** `8002`. **Health:** `/actuator/health`.
- **Errores:** [contrato común](https://github.com/btoroled/cloud-computing-proyecto/blob/main/docs/contratos/errores.md) (`@RestControllerAdvice`).
- **Enums y rangos de ID:** [diccionario compartido](https://github.com/btoroled/cloud-computing-proyecto/blob/main/docs/contratos/enums.md)
  (`vuelo.id` 1–25 000).
- **Imagen:** `git tag vX.Y && git push --tags` → `ghcr.io/cloud-mla/ms2-vuelos-api:vX.Y`.

## Endpoints (previstos)

| Método | Ruta | Descripción |
|---|---|---|
| GET | `/actuator/health` | Estado |
| CRUD | `/aerolineas` (PK `ruc`), `/aeronaves` (PK `placa`), `/vuelos` | Con filtros `?num=&estado=&tipo=&fecha=` |
| GET | `/aeronaves/{placa}/asientos` | Asientos por avión |
| PATCH | `/vuelos/{id}/estado` | Máquina de estados |
| GET | `/vuelos/{id}/exists` | Endpoint liviano para MS1/MS3 |
| POST/GET | `/vuelos/{id}/tripulacion` | Asignación N–M |
