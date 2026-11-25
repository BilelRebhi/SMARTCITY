# MS4 — EcoCity & Environmental Monitoring

**Owner:** Isra Zguir
**Backend folder:** `Backend/ecocity-service` (port `8084`)
**Database:** `ecocity_db` (PostgreSQL + PostGIS), owned only by MS4
**Status:** Design validated for session 4; the Spring Boot project is scaffolded, business logic is not implemented yet.

## Purpose

EcoCity monitors air quality and noise across the neighborhoods of the city. Virtual monitoring stations
record simulated readings, configured thresholds detect abnormal values, and pollution alerts,
dashboards and environmental reports help citizens and municipal staff understand local conditions.

## Actors

| Actor | Role in MS4 |
|---|---|
| Municipal administrator | Creates zones and virtual stations, configures air-quality and noise thresholds |
| Environmental operator | Watches dashboards, acknowledges and resolves alerts, generates reports |
| Citizen | Checks air quality and noise in their zone, compares neighborhoods, views history |

## Main functionalities

1. Manage monitoring stations per zone (create, list, activate / deactivate).
2. Record readings: simulated every 5 minutes (`samplingIntervalSec = 300`) or posted manually.
3. Track indicators: `PM2_5`, `PM10`, `NO2`, `CO2`, `NOISE_DB`.
4. Configure thresholds per indicator with a severity (`LOW`, `MEDIUM`, `HIGH`).
5. Raise pollution alerts automatically when a reading exceeds an active threshold.
6. Acknowledge and resolve alerts (`OPEN → ACKNOWLEDGED → RESOLVED`).
7. View reading history by station, indicator and period.
8. Compare zones (average and maximum values).
9. Environmental dashboard and reports per zone and period.

## Class diagram

![MS4 class diagram](Diagrams/ms4-ecocity-class-diagram.png)

| Class | Responsibility |
|---|---|
| `Zone` | City neighborhood; `boundary` stored as a PostGIS polygon |
| `MonitoringStation` | Virtual station in a zone; `generateReading()` produces simulated values |
| `Measurement` | One reading (indicator, value, unit, time, quality); exposed by the API as a **reading** |
| `Threshold` | Maximum allowed value for an indicator, with its severity |
| `PollutionAlert` | Created when a measurement exceeds a threshold; `acknowledge()` / `resolve()` |
| `EnvReport` | Average and maximum values for a zone over a period |

Enumerations: `Indicator`, `StationStatus`, `MeasurementQuality`, `AlertSeverity`, `AlertStatus`.

## Monitoring workflow

1. Every 5 minutes, each `ACTIVE` station generates a reading (or a reading is posted to `POST /api/readings`).
2. MS4 validates the value, unit and timestamp, and sets its quality (`VALID`, `SUSPECT`, `INVALID`).
   Invalid input is rejected with `400`.
3. The reading is stored and compared with the active threshold of its indicator.
4. If the value is over the limit, a `PollutionAlert` is created with status `OPEN`.
   Only one open alert is kept per station and indicator.
5. MS4 publishes the `PollutionAlertRaised` event to RabbitMQ.
6. An environmental operator acknowledges, then resolves the alert; MS4 publishes `PollutionAlertResolved`.

## API contract

All routes go through the API Gateway and require a Keycloak JWT.

| Method | Endpoint | Purpose | Responses |
|---|---|---|---|
| POST | `/api/monitoring-stations` | Create a station in a zone | `201`, `400`, `404` zone, `409` code exists |
| GET | `/api/monitoring-stations?zoneId=` | List stations | `200` |
| PATCH | `/api/monitoring-stations/{id}/status` | Activate / deactivate a station | `200`, `400`, `404` |
| POST | `/api/readings` | Record or simulate a reading | `201`, `400` invalid value/unit, `404` station |
| GET | `/api/readings?stationId=&indicator=&from=&to=` | Reading history | `200`, `400` |
| POST | `/api/thresholds` | Configure a threshold | `201`, `400`, `409` active threshold exists |
| PUT | `/api/thresholds/{id}` | Update a threshold | `200`, `400`, `404` |
| GET | `/api/pollution-alerts?status=&zoneId=` | List alerts | `200` |
| PATCH | `/api/pollution-alerts/{id}/status` | Acknowledge / resolve an alert | `200`, `404`, `409` invalid transition |
| GET | `/api/zones/compare?zoneIds=&indicator=&from=&to=` | Compare zones | `200`, `400`, `404` |
| GET | `/api/zones/my-zone/conditions` | Conditions in the signed-in citizen's zone (header `X-Citizen-Id`, set by the Gateway; calls MS1) | `200`, `404`, `503` MS1 unavailable |
| GET | `/api/environmental-reports?zoneId=&from=&to=` | Generate / read a report | `200`, `400`, `404` |

Common error codes: `401` missing or invalid token, `403` role not allowed.

## Events (asynchronous, RabbitMQ)

Exchange `ecocity.events` (topic).

| Event | Routing key | Payload | Consumers |
|---|---|---|---|
| `PollutionAlertRaised` | `ecocity.alert.raised` | `eventId`, `alertId`, `stationId`, `zoneId`, `indicator`, `value`, `thresholdValue`, `unit`, `severity`, `triggeredAt` | MS2 (`HIGH` alerts → urban incident), MS3 (traffic-related peaks: `NO2`, `PM10`, `NOISE_DB`) |
| `PollutionAlertResolved` | `ecocity.alert.resolved` | `eventId`, `alertId`, `zoneId`, `resolvedAt` | MS2, MS3 (close the related follow-up) |

Example message:

```json
{
  "eventId": "6f1c2a9e-3b7d-4c11-9a52-1e0f4d2b7c88",
  "alertId": 42,
  "stationId": 7,
  "zoneId": 3,
  "indicator": "PM10",
  "value": 98.4,
  "thresholdValue": 50.0,
  "unit": "µg/m³",
  "severity": "HIGH",
  "triggeredAt": "2026-11-19T08:15:00Z"
}
```

## Interactions with other microservices

| Flow | Type | Example |
|---|---|---|
| MS4 → MS1 | Synchronous (OpenFeign through Eureka) | `GET /api/citizens/{id}` returns the citizen's neighborhood, used by `GET /api/zones/my-zone/conditions`. If MS1 is down, MS4 answers `503` and the rest of MS4 keeps working. |
| MS4 → MS2 | Asynchronous (RabbitMQ) | `PollutionAlertRaised` with severity `HIGH` → MS2 opens an incident for the zone. |
| MS4 → MS3 | Asynchronous (RabbitMQ) | `PollutionAlertRaised` for `NO2` / `PM10` / `NOISE_DB` → MS3 can adapt traffic information. |

MS4 never reads another service's database, and no service reads `ecocity_db`.

## Database choice

PostgreSQL (`ecocity_db`), owned only by MS4 (database-per-service):

- relational model with foreign keys (zone → station → measurement → alert) and ACID transactions;
- SQL aggregations (`AVG`, `MAX` by zone and period) for dashboards, comparisons and reports;
- PostGIS for station locations and zone boundaries.

## Technologies

Spring Boot 4, Spring Data JPA, Bean Validation, OpenFeign, Spring AMQP (RabbitMQ), Eureka client,
PostgreSQL + PostGIS, Angular (EcoCity pages), Docker.

## Presentation slides

`Presentation/ms4-overview.svg`, `Presentation/ms4-workflow.svg`,
`Presentation/ms4-interactions.svg`, `Presentation/ms4-api-database.svg`.
