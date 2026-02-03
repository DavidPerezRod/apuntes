# Dossier — Observabilidad en Sistemas Distribuidos basados en Microservicios

> Objetivo del dossier: proporcionar un marco **consultable** y **operable** para instrumentar, explotar y evolucionar la observabilidad en arquitecturas de microservicios, con foco en SRE (SLO/SLI), diagnóstico y adopción incremental.

---

## Índice (capítulos)

- [00 — Visión global (overview)](#00--visión-global-overview)
- [01 — Los tres pilares (visión rápida + correlación)](#01--los-tres-pilares-visión-rápida--correlación)
- [02 — Métricas (deep-dive)](#02--métricas-deep-dive)
- [03 — Logs (deep-dive)](#03--logs-deep-dive)
- [04 — Trazas (deep-dive)](#04--trazas-deep-dive)
- [05 — Correlación y diseño de señales (cross-cutting)](#05--correlación-y-diseño-de-señales-cross-cutting)
- [06 — SRE operativo: SLO/SLI, alerting y respuesta](#06--sre-operativo-slosli-alerting-y-respuesta)
- [07 — Platform view: Kubernetes y dependencias](#07--platform-view-kubernetes-y-dependencias)
- [08 — Guía de implantación (roadmap incremental)](#08--guía-de-implantación-roadmap-incremental)
- [Templates reutilizables](#templates-reutilizables)

---

## 00 — Visión global (overview)

### Objetivo del capítulo
Alinear el lenguaje y el modelo mental: qué es observabilidad, qué no lo es, y cómo se organiza el dossier para uso diario (operación y evolución).

### Conceptos clave
- **Telemetría**: señales de salida del sistema (métricas, logs, trazas, perfiles, eventos, etc.).
- **Observabilidad**: capacidad de inferir el estado interno del sistema a partir de telemetría.
- **Monitorización**: práctica operativa orientada a detección y alerta basada en señales definidas (normalmente más “predefinida”).
- **Analítica**: explotación de datos (telemetría + negocio) para entender comportamiento, tendencias, cohortes, embudos; suele tener horizontes temporales y objetivos distintos.

### Decisiones y trade-offs
- Observabilidad no es “tener herramientas”, es **diseñar señales** y **operarlas** con ownership y disciplina.
- Separar conceptualmente:
  - **Telemetría** (instrumentar)
  - **Monitorización** (operar con alerting/dashboards)
  - **Analítica** (aprender/optimizar con agregados y producto)

### Buenas prácticas
- Definir primero “qué preguntas operativas” debe responder el sistema.
- Empezar por mínimos viables (baseline) y evolucionar por iteraciones.
- Correlación desde el día 1 (traceId/spanId, requestId, tenant, etc.).

### Antipatrones
- Confundir observabilidad con “instalar Prometheus/Grafana”.
- Señales sin ownership: dashboards/alertas “huérfanas”.
- Telemetría no accionable: datos sin preguntas.

### Implementación típica / tooling (alto nivel)
- Instrumentación: OpenTelemetry / Micrometer / SDKs.
- Recolección: Collectors/agents.
- Almacenamiento: TSDB, log store, trace store.
- Explotación: dashboards, alerting, exploración ad-hoc.

### Resumen / checklist
- [ ] Existe una definición operativa de observabilidad.
- [ ] Se distinguen telemetría vs monitorización vs analítica.
- [ ] Hay baseline mínimo por servicio.
- [ ] Existe correlación entre pilares.

---

## 01 — Los tres pilares (visión rápida + correlación)

### Objetivo del capítulo
Presentar **métricas, logs y trazas en paralelo** (mismo peso), y dejar clara la **correlación** como “modo de uso” real.

### Tabla comparativa
| Pilar | Qué responde | Cuándo usarlo | Ejemplos | Riesgos típicos |
|------|--------------|---------------|----------|-----------------|
| **Métricas** | “¿Va bien o va mal?” (tendencias agregadas) | Detección temprana, alerting, capacidad | latencia p95/p99, error rate, RPS, saturación | cardinalidad, promedios, ruido por métricas inútiles |
| **Logs** | “¿Qué pasó exactamente?” (evento + contexto) | Diagnóstico, auditoría, análisis forense | errores con stacktrace, eventos de negocio, cambios de estado | PII, exceso de volumen, falta de estructura |
| **Trazas** | “¿Dónde se fue el tiempo y quién llamó a quién?” (flujo end-to-end) | Cuellos de botella, dependencias, RCA | spans por servicio, latencia por dependencia, reintentos/timeouts | sampling mal definido, trazas incompletas, spans mal nombrados |

### Regla práctica de uso
- **Métricas** te indican *que hay un problema*.
- **Trazas** suelen decirte *dónde está*.
- **Logs** te permiten confirmar *qué pasó* con el máximo detalle.

### Correlación (mínimo común)
- Desde métricas → saltar a trazas filtrando por servicio/endpoint/latencia.
- Desde una traza → saltar a logs por `traceId`/`spanId`.
- Campos comunes recomendados en logs: `traceId`, `spanId`, `requestId`, `tenant`, `service`, `env`, `version`.

### Resumen / checklist
- [ ] Tabla comparativa incluida en el dossier.
- [ ] Se explica cómo correlacionar (métrica → traza → logs).
- [ ] Se establecen campos mínimos comunes.

---

## 02 — Métricas (deep-dive)

### Objetivo del capítulo
Dominar el pilar de métricas como telemetría: qué son, cómo se interpretan, cómo se instrumentan bien y cómo se evitan fallos típicos (especialmente cardinalidad y semántica).

### Estructura del capítulo (propuesta interna)
1. Fundamentos de métricas (tipos, semántica, interpretación)
2. Instrumentación (auto vs manual) y exposición
3. Buenas prácticas (baseline + Golden Signals + SLIs)
4. Antipatrones (exclusivos de telemetría de métricas)
5. Tooling de generación/exposición (Micrometer/OTel)
6. Checklist

---

### Qué es
Las métricas son medidas numéricas del comportamiento del sistema a lo largo del tiempo. Se representan como **series temporales** y permiten **agregación** y **consulta eficiente** (por ejemplo, percentiles/histogramas, ratios, tasas).

### Para qué sirve
- Detección temprana de degradaciones.
- Alerting accionable.
- Capacidad y tendencias.
- Punto de entrada para decidir dónde profundizar (trazas/logs).

---

### Fundamentos (tipos y semántica)

#### Tipos principales
- **Counter**: contador monotónico (p.ej. `requests_total`).
- **Gauge**: valor instantáneo (p.ej. `heap_used_bytes`).
- **Histogram**: distribución (p.ej. latencia por buckets) → percentiles y colas.
- **Summary** (según ecosistema): percentiles precomputados (trade-offs de agregación).

#### Interpretación mínima
- Preferir **percentiles (p95/p99)** o histogramas en latencia (no solo promedio).
- Usar **tasas** (rate) y **ratios** en lugar de valores absolutos cuando se busca robustez.
- Separar señales técnicas vs negocio (SLIs) vs cliente (4xx/401 esperables).

---

### Buenas prácticas (métricas)

#### 1) Qué medir (mínimo por servicio)
**Golden Signals**:
- **Latencia (p95/p99)**: `http.server.requests` p95/p99; latencia DB; latencia upstream.
- **Tráfico/throughput**: RPS, msgs/s, operaciones de negocio/min.
- **Errores**: 5xx, timeouts, exceptions; separar 4xx y errores de negocio.
- **Saturación**: CPU, heap/GC, I/O, colas/pools, conexiones DB, backlog en consumidores.

#### 2) Instrumentación
- Auto-instrumentación (framework: HTTP server/client, DB, mensajería) + manual para negocio.
- Exposición por endpoint (Prometheus format) o exportación a collector (OTLP).
- En JVM/Spring: Micrometer como estándar de facto; OTel como estándar multi-lenguaje.

#### 3) Modelo dimensional (tags) y cardinalidad
- Tags útiles (baja cardinalidad): `service`, `env`, `region`, `cluster`, `namespace`, `method`, `status`, `route` normalizado.
- Evitar alta cardinalidad: `userId`, `orderId`, `documentUuid`, `ip`, `email`, `sessionId`.
- Si necesitas por entidad: suele ir a logs/trazas, no a métricas.

---

### Antipatrones (métricas) — exclusivos de telemetría de métricas

#### A1) “Medirlo todo” sin pregunta operativa
- **Impacto**: ruido, coste, dashboards inabarcables.
- **Corrección**: Golden Signals + SLIs; cada métrica responde a una decisión.

#### A2) Cardinalidad descontrolada (tags explosivos)
- **Impacto**: explosión de series; consultas lentas; coste y OOM en TSDB.
- **Corrección**: tags de baja cardinalidad y rutas normalizadas.

#### A3) Etiquetar endpoints crudos (paths con IDs)
- **Impacto**: cardinalidad alta + métricas inútiles para agregación.
- **Corrección**: `route` por plantilla (`/users/{id}`).

#### A4) Latencia solo como promedio
- **Impacto**: oculta colas/outliers.
- **Corrección**: p95/p99 e histogramas.

#### A5) Mezclar 4xx, 5xx y errores de negocio como “errores”
- **Impacto**: alertas por comportamiento esperado o invisibilidad de fallos reales.
- **Corrección**: separar técnico vs cliente vs negocio (SLIs específicos).

#### A6) Métricas incompletas: solo server-side (sin dependencias)
- **Impacto**: RCA imposible; culpas al componente equivocado.
- **Corrección**: métricas client-side hacia DB/HTTP/broker + panel de dependencias.

#### A7) Métricas inconsistentes (naming, unidades, semántica)
- **Impacto**: no comparables; PromQL/queries complejas; interpretación errónea.
- **Corrección**: convención mínima (unidades, counter/gauge/histogram, reutilizar estándar).

#### A8) Instrumentación costosa o con side effects
- **Impacto**: la observabilidad degrada el sistema.
- **Corrección**: métricas baratas en memoria; prohibir queries pesadas en scrape.

#### A9) Exposición insegura de métricas
- **Impacto**: fuga de información; riesgo de seguridad.
- **Corrección**: restringir por red; auth si aplica; separar health público vs métricas privadas.

---

### Herramientas / implementación típica (generación de métricas)
> Nota: aquí se listan herramientas **de instrumentación/exposición** (telemetría). El stack de monitorización (Prometheus/Grafana/Alertmanager) puede ir en un capítulo operativo (06/07) o en un anexo, para mantener coherencia.

- **Micrometer (JVM/Spring)**: instrumentación + métricas estándar + métricas de negocio.
- **OpenTelemetry Metrics**: estándar multi-lenguaje; exportación OTLP.
- **Exposición**:
  - Endpoint Prometheus format o exportación OTLP a collector.
  - En Spring Boot: Actuator + registry (p.ej. Prometheus registry).

---

### Resumen / checklist (métricas)
- [ ] Cada servicio expone Golden Signals (latencia p95/p99, tráfico, errores, saturación).
- [ ] Latencia con percentiles/histogramas (no solo promedio).
- [ ] Tags de baja cardinalidad; prohibidos IDs únicos (`userId`, `orderId`, etc.).
- [ ] Endpoints normalizados (`route`), no paths crudos con IDs.
- [ ] SLIs de negocio diferenciados de errores técnicos.
- [ ] Exposición barata (sin queries pesadas en scrape).
- [ ] Naming y unidades consistentes.
- [ ] Correlación disponible (enlace lógico con traceId/logs si aplica).

---

## 03 — Logs (deep-dive)

### Objetivo del capítulo
Convertir los logs en telemetría útil (no en “texto suelto”): estructura, contexto, coste, y prácticas para diagnóstico y auditoría.

### Qué es
Registros de eventos con contexto: desde errores técnicos hasta eventos de negocio.

### Para qué sirve
- Diagnóstico detallado y confirmación de hipótesis.
- Auditoría y forense.
- Evidencias (seguridad, compliance).

### Buenas prácticas (mínimo)
- **Structured logging** (JSON o formato consistente).
- Campos comunes: `timestamp`, `level`, `service`, `env`, `version`, `traceId`, `spanId`, `requestId`, `tenant`.
- **Separar** eventos de negocio vs errores técnicos.
- Control de volumen: sampling/leveling por entorno; límites de payload.
- Política de PII: enmascarado/redacción.

### Antipatrones
- PII en claro, tokens o secretos en logs.
- Logs no estructurados y sin contexto.
- Volumen ilimitado por debug permanente.
- Stacktraces sin correlación.

### Implementación típica / tooling (telemetría)
- Librerías: JSON encoders (Logback/Log4j2), MDC para contexto.
- Pipelines: collectors/forwarders y normalización.
- Almacenamiento y consulta: índices, retención y coste.

### Checklist
- [ ] Logs estructurados.
- [ ] Campo de correlación `traceId` presente.
- [ ] Política PII aplicada.
- [ ] Retención y niveles por entorno definidos.

---

## 04 — Trazas (deep-dive)

### Objetivo del capítulo
Entender flujos end-to-end y dependencias: dónde se consume el tiempo, dónde están los cuellos y cómo hacer RCA en microservicios.

### Qué es
Trazas distribuidas: un **trace** compuesto por **spans** que representan operaciones y dependencias.

### Para qué sirve
- Localizar cuellos de botella (latencia end-to-end).
- Identificar dependencias lentas, retries, timeouts.
- RCA en arquitecturas distribuidas.

### Buenas prácticas
- Propagación de contexto (trace context) consistente.
- Spans por dependencias (HTTP/DB/broker).
- Naming y atributos coherentes (servicio, endpoint, status, error, peer, etc.).
- Sampling definido: head/tail según necesidad y coste.
- Enlazar logs por `traceId`/`spanId`.

### Antipatrones
- Trazas sin spans de dependencias.
- Sampling “ciego” que elimina justo los casos problemáticos.
- Spans gigantes sin granularidad.
- Falta de atributos mínimos comunes.

### Implementación típica / tooling (telemetría)
- OpenTelemetry Tracing (SDK + auto-instrumentation).
- Collector OTLP.
- Backend de trazas (según stack).

### Checklist
- [ ] Propagación de contexto garantizada.
- [ ] Spans de dependencias presentes.
- [ ] Sampling documentado y revisable.
- [ ] Correlación con logs funcionando.

---

## 05 — Correlación y diseño de señales (cross-cutting)

### Objetivo del capítulo
Definir convenciones comunes y el “modo de uso” real: correlación práctica entre pilares y diseño sostenible de atributos/tags.

### Contenido
- Convenciones de naming (servicio, operación, endpoint/route).
- Atributos mínimos comunes (traceId/spanId/requestId/tenant/version).
- Estrategias de cardinalidad:
  - Métricas: baja cardinalidad estricta.
  - Logs: contexto rico pero controlado.
  - Trazas: atributos útiles para slicing, evitando explosión innecesaria.
- “Drill-down” estándar: dashboard → traza → logs.

### Checklist
- [ ] Campos comunes definidos.
- [ ] Convenciones documentadas.
- [ ] Correlación implementada y validada.
- [ ] Reglas de cardinalidad por pilar.

---

## 06 — SRE operativo: SLO/SLI, alerting y respuesta

### Objetivo del capítulo
Operar con impacto: SLIs bien elegidos, SLOs realistas, alertas accionables y runbooks.

### Contenido
- SLI vs SLO vs error budget.
- Alerting:
  - Preferible por impacto (burn rate) cuando hay SLO.
  - Umbrales solo cuando aportan señal estable (saturación real/disponibilidad).
- Runbooks: “5 pasos” + mitigación + verificación.
- Dedupe/routing/severidad/ownership.

### Checklist
- [ ] SLIs definidos por servicio crítico.
- [ ] SLOs versionados y revisados.
- [ ] Alertas con runbook y dashboard link.
- [ ] On-call hygiene (dedupe/routing).

---

## 07 — Platform view: Kubernetes y dependencias

### Objetivo del capítulo
Separar “salud de plataforma” de “salud de servicio” y observar dependencias compartidas.

### Contenido
- Kubernetes: nodes, CPU/mem, throttling, scheduling, restarts, network.
- Dependencias: DB (locks, latency, connections), broker (lag/backlog), caches, external providers.
- Dashboards:
  - por servicio (Golden Signals + dependencias + deploy markers)
  - por plataforma (cluster + shared deps)

### Checklist
- [ ] Dashboards de plataforma existentes.
- [ ] Señales de dependencias integradas.
- [ ] Capacity planning (tendencias) definido.

---

## 08 — Guía de implantación (roadmap incremental)

### Objetivo del capítulo
Adopción por niveles para evitar “big bang” y maximizar valor temprano.

### Niveles sugeridos
- **Nivel 0 — Baseline mínimo**: Golden Signals + logs estructurados + trazas básicas.
- **Nivel 1 — Correlación y ownership**: traceId end-to-end + runbooks básicos.
- **Nivel 2 — SLO y alerting por impacto**: error budgets + burn rate.
- **Nivel 3 — Optimización de coste/retención**: sampling/retención por capas, multi-cluster si aplica.

### Checklist
- [ ] Nivel actual identificado.
- [ ] Próximo nivel con tareas concretas.
- [ ] Ownership asignado.

---

# Templates reutilizables

## Template A — Capítulo “pilar” (Métricas / Logs / Trazas)
```md
# Pilar X — <Nombre>

## Qué es
## Para qué sirve
## Buenas prácticas
## Antipatrones
## Herramientas / implementación típica
## Resumen / checklist
