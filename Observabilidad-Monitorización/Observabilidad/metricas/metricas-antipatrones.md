# Métricas - Antipatrones
Esta sección complementa las buenas prácticas anteriores. El objetivo es identificar patrones que degradan la observabilidad en producción porque: 
- Generan ruido
- Elevan costes
- Esconden problemas reales

## A1) “Medirlo todo” sin una pregunta operativa
- **Qué es**: instrumentar cientos/miles de métricas por servicio “por si acaso”, sin relación con decisiones operativas.
- **Impacto**: dashboards inabarcables, nadie sabe qué mirar en un incidente, costes y complejidad innecesarios.
- **Señales**: métricas que nadie consulta; paneles abandonados; alertas que nunca se atienden.
- **Corrección**: empezar por **[Golden Signals](../glosario.md#golden-signals "Métricas sobre aspectos esenciales") +  [SLIs](../acronimos.md#sli "Service Level Indicator") de negocio**; cada nueva métrica debe responder a una pregunta (“¿qué decisión tomaré si cambia?”).

## A2) Cardinalidad descontrolada (tags explosivos)
- **Qué es**: etiquetas con valores prácticamente únicos (p. ej. `userId`, `orderId`, `documentUuid`, `ip`, `email`, `sessionId`).
- **Impacto**: explosión de series temporales, saturación del [TSDB](../acronimos.md#tsdb "Time Series Database"), que generan consultas lentas y caras por consumo elevado de RAM.
- **Señales**: crecimiento acelerado de series que generan [OOM](../acronimos.md#oom "Out of memory"), consultas [PromQL](../acronimos.md#promql "Prometheus Query Language") lentas; costes inesperados (en SaaS).
- **Corrección**: mantener tags **de baja cardinalidad** (`service`, `env`, `region`, `status`, `method`, `route` normalizado).

## A3) Etiquetar endpoints “crudos” (paths con IDs embebidos)
- **Qué es**: usar el path real como etiqueta (`/users/123/orders/987`) o como nombre de métrica.
- **Impacto**: cardinalidad alta y métricas inútiles para agregación.
- **Señales**: miles de rutas distintas en dashboards; paneles que no agregan bien.
- **Corrección**: usar `route`/plantillas (`/users/{id}/orders/{id}`) o mecanismos del framework que normalizan rutas.

## A4) Latencia solo como promedio (avg)
- **Qué es**: monitorizar “latencia media” como señal principal.
- **Impacto**: se esconden colas y [outliers](../glosario.md#outlier "Valores atípicos").
- **Señales**: incidentes con quejas de “va lento” sin correlación con el promedio.
- **Corrección**: usar **p95/p99** y/o **histogramas** y separar latencia por tipo (HTTP, DB, proveedor externo).

## A5) Mezclar 4xx, 5xx y “errores de negocio” en el mismo saco
- **Qué es**: un único contador de “errores” sin distinguir causa ni impacto.
- **Impacto**: alertas por eventos esperables (401/403), o invisibilidad de fallos técnicos dentro del ruido.
- **Señales**: alertas frecuentes sin incidentes reales; métricas de error altas “siempre”.
- **Corrección**:
  - **Técnico**: 5xx, timeouts, exceptions, circuit breaker → alertable.
  - **Cliente**: 4xx → suele ser informativo (depende del caso).
  - **Negocio**: rechazos/validaciones → SLIs específicos, no “incidente” por defecto.

## A6) Métricas incompletas: solo server-side (sin dependencias)
- **Qué es**: métricas del servicio sin métricas de sus llamadas a DB, brokers, o servicios upstream.
- **Impacto**: no se identifica la causa (p. ej. un tercero lento) y se culpa al componente equivocado.
- **Señales**: latencia/errores suben sin explicación interna.
- **Corrección**: métricas de **client-side** (latencia/errores hacia DB/HTTP/broker), y panel “dependencias” en cada dashboard de servicio.


## A7) Métricas inconsistentes por naming y semántica
- **Qué es**: cada equipo nombra distinto lo mismo; contadores que a veces son gauges; unidades inconsistentes.
- **Impacto**: imposible comparar servicios; PromQL complejo; errores de interpretación.
- **Señales**: consultas diferentes para lo mismo; dashboards no reutilizables.
- **Corrección**: convención mínima:
  - **Unidades** explícitas (ms/s/bytes),
  - **Counters** monotónicos para “totales”, **gauges** para estado, **histogramas** para latencia,
  - reutilizar métricas estándar (HTTP/JVM/DB) y documentar las de negocio.

## A8) Instrumentación manual costosa o con side effects
- **Qué es**: calcular métricas realizando queries pesadas “en cada scrape” o en paths de alta frecuencia.
- **Impacto**: la observabilidad degrada el sistema; latencia extra y efectos en cascada.
- **Señales**: picos de CPU coinciden con scrape; latencia aumenta al habilitar métricas.
- **Corrección**: métricas deben ser **baratas**:
  - usar contadores/gauges actualizados en memoria,
  - evitar consultas a DB solo para “pintar un número”.

## A9) Exposición insegura de endpoints de métricas
- **Qué es**: dejar `/actuator/prometheus` o equivalentes accesibles públicamente.
- **Impacto**: fuga de información (topología, rutas, estados), riesgo de seguridad.
- **Señales**: endpoints accesibles desde fuera del cluster; ausencia de controles de red.
- **Corrección**: restringir por red (cluster-only), auth si aplica, y separar health público vs métricas privadas.