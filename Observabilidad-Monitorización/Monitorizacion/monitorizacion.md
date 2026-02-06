# Monitorización: qué debe responder

La monitorización operativa (monitoring + alerting) tiene sentido cuando responde, de forma **accionable**, a cuatro preguntas: disponibilidad, corrección, cumplimiento de SLO y margen operativo. Las tres primeras detectan si el sistema “está y funciona”; la cuarta evita que te enteres tarde de que *va a dejar de funcionar*.

## 1) ¿Está disponible?

El objetivo aquí es saber si el servicio está **reachable** y responde a lo mínimo imprescindible. Esto cubre “está up”, pero evitando falsos positivos (por ejemplo, procesos vivos que no aceptan tráfico real).

En la práctica se suele medir con *uptime* y checks simples (health endpoints, conectividad básica). Para alerting conviene que el criterio sea claro: “no está accesible desde el punto donde se consume” (no solo “el proceso existe”).

## 2) ¿Está correcto?

No basta con responder: debe hacerlo **bien** en las rutas críticas. Este bloque confirma que el sistema sigue cumpliendo su contrato funcional en condiciones reales: tasa de errores, respuestas inválidas, degradación por dependencias (DB, colas, terceros), etc.

Aquí encajan checks sintéticos (pocas transacciones representativas) y señales de errores reales (5xx, timeouts, errores de negocio relevantes). La clave es que la señal sea interpretable: *si salta, alguien puede actuar*.

## 3) ¿Cumple los SLOs?

La frontera entre “funciona” y “funciona como el usuario espera” se captura con **SLI/SLO**. Esta parte evita dos problemas típicos: (1) tener muchas métricas pero no saber si hay impacto, y (2) alertar por ruido técnico sin degradación real.

Lo normal es medir SLI de **tasa de éxito** y **latencia** (p95/p99) en rutas relevantes, y compararlos con objetivos (SLO). Para alerting, los patrones potentes son los basados en **error budget burn rate** (cuando el sistema consume el budget demasiado rápido).

## 4) ¿Tiene margen operativo?

Aunque hoy se cumpla el SLO, el sistema puede estar **al borde**: saturación de CPU/memoria, pools de threads al límite, DB con alta contención, colas creciendo, conexiones agotadas, etc. Esta capa detecta el “va a caer” antes de que se materialice en errores.

La idea es vigilar **capacidad y saturación** (no “infra por amor al arte”), conectándolo a síntomas: backlog, latencia creciente bajo carga, throttling, GC pressure, límites de rate, etc.

## Señales recomendadas (estructura mínima)

Si quieres un set compacto que cubra bien el terreno, funciona mapearlo a “golden signals”:

- **Traffic**: volumen de peticiones/eventos.
- **Errors**: tasa de fallos (y su tipología relevante).
- **Latency**: p95/p99 por rutas críticas.
- **Saturation**: indicadores de cola/contención/recursos al límite.

## Alerting: condición imprescindible

Una alerta buena es **accionable**: tiene owner, severidad, umbral razonable, y un runbook mínimo (qué comprobar primero y cómo mitigar). Si una alerta no cambia el comportamiento de nadie, es ruido.

## Resumen en tabla

| Pregunta | Qué valida | Señales típicas | Fallo típico si no se cubre |
|---|---|---|---|
| ¿Está disponible? | Reachability / up | uptime, healthchecks, blackbox | “Está vivo pero no sirve tráfico” |
| ¿Está correcto? | Correctness funcional | 5xx, timeouts, checks sintéticos, dependencias | “Responde, pero mal” |
| ¿Cumple SLOs? | Experiencia percibida | SLI de éxito/latencia vs SLO, burn rate | “No sabes si hay impacto real” |
| ¿Tiene margen? | Capacidad / saturación | CPU/mem, pools, colas, DB contention | “Te enteras tarde: cae en cascada” |

## Acrónimos y definiciones operativas

En monitorización y operación de sistemas se usan acrónimos que, si no se definen con precisión, acaban convirtiéndose en “palabras talismán”. Esta sección fija definiciones **operativas** (qué significan y para qué se usan).

**SLI (Service Level Indicator)**  
Métrica cuantitativa que mide un aspecto observable del servicio desde la perspectiva del consumidor. Ejemplos típicos: tasa de éxito (2xx/3xx vs total), latencia p95/p99 de una ruta crítica, disponibilidad percibida por un probe externo. Un SLI debe ser medible y estable en el tiempo.

**SLO (Service Level Objective)**  
Objetivo numérico para un SLI durante una ventana temporal. Ejemplo: “p99 < 300 ms para `GET /v1/foo` durante 30 días” o “≥ 99.9% de éxito mensual”. El SLO traduce expectativas de servicio en un umbral operativo.

**SLA (Service Level Agreement)**  
Acuerdo contractual (o formal) con consecuencias (penalizaciones, créditos, etc.) que suele apoyarse en SLO/SLI, pero no es lo mismo. Un equipo puede operar por SLOs sin tener SLAs.

**Error Budget (presupuesto de error)**  
Cantidad tolerada de “incumplimiento” del SLO en una ventana. Si el SLO es 99.9% mensual, el error budget es 0.1% del tiempo/requests de esa ventana. Se usa como mecanismo de gobernanza: si consumes budget rápido, priorizas fiabilidad sobre cambios.

**Burn Rate (tasa de consumo del error budget)**  
Velocidad a la que se consume el error budget. Permite alertas que detectan degradaciones “rápidas” antes de que el SLO mensual se rompa: si el burn rate es alto, aunque el SLO aún no esté incumplido, vas en camino de romperlo.

**MTTD (Mean Time To Detect)**  
Tiempo medio desde que empieza un incidente (o degradación relevante) hasta que el equipo lo detecta. En la práctica suele medirse como:  
`MTTD = timestamp(detección/alerta confirmada) - timestamp(inicio del impacto)`  
Reducir MTTD depende de observabilidad, calidad de alertas y cobertura de SLIs.

**MTTR (Mean Time To Restore / Recover / Resolve)**  
Tiempo medio desde la detección del incidente hasta la restauración del servicio (o resolución aceptada). Ojo: en algunas organizaciones “R” significa *Restore* (volver al servicio) y en otras *Resolve* (cerrar definitivamente). Conviene declarar qué se entiende. Forma típica:  
`MTTR = timestamp(servicio restaurado) - timestamp(detección)`  
Reducir MTTR depende de runbooks, automatización, diseño para resiliencia, y capacidad de diagnóstico (trazas/logs/métricas correlacionables).

**MTBF (Mean Time Between Failures)**  
Tiempo medio entre fallos. Se usa más en fiabilidad clásica; en software moderno puede ser menos estable por cambios frecuentes, pero es útil en ciertos contextos (p.ej., infra o componentes con fallos discretos).

**MTTA (Mean Time To Acknowledge)**  
Tiempo medio desde que salta una alerta hasta que alguien la reconoce/triagea. No es lo mismo que detectar: mide capacidad de respuesta operativa (on-call, routing, severidad).

**RTO (Recovery Time Objective)**  
Tiempo objetivo máximo para restaurar un servicio tras un incidente mayor (sobre todo continuidad de negocio). Es un objetivo de negocio/operación, no una métrica “automática”.

**RPO (Recovery Point Objective)**  
Pérdida máxima de datos aceptable medida en tiempo (p.ej., “como mucho 15 minutos de datos”). Relacionado con backups/replicación y diseño de persistencia.

**On-call**  
Modelo de guardia para respuesta a incidentes. Importa porque condiciona MTTA/MTTR y la calidad exigible a las alertas (si despiertas a alguien por ruido, el sistema se degrada socialmente).

**Runbook**  
Guía operativa accionable para diagnosticar y mitigar una alerta/incidente. Un runbook bueno reduce MTTR: pasos concretos, checks, mitigaciones, enlaces a dashboards y comandos.

**Golden Signals**  
Conjunto de señales “mín
