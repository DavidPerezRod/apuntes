<a id="glosario"></a>
# Glosario de términos

Referencias breves para conceptos recurrentes en monitorización y observabilidad.

## Fiabilidad y objetivos
- <a id="sli-term"></a>**Service Level Indicator (SLI)**: métrica cuantificable que representa la experiencia del usuario (p. ej. tasa de errores HTTP 5xx, latencia p95).
- <a id="slo-term"></a>**Service Level Objective (SLO)**: objetivo para un [SLI](acronimos.md#sli), expresado como umbral y ventana (p. ej. 99.9% de éxito en 30 días).
- <a id="error-budget"></a>**Error budget**: margen permitido de incumplimiento respecto al [SLO](acronimos.md#slo) antes de activar medidas de protección.

## Señales y datos
- <a id="golden-signals"></a>**Golden signals**: conjunto base de métricas (latencia, tráfico, errores, saturación) para monitorizar servicios.
- <a id="cardinalidad"></a>**Cardinalidad**: número de combinaciones distintas de etiquetas en una métrica; crece el coste y puede romper el backend si no se controla.
- <a id="muestreo"></a>**Muestreo (sampling)**: reducción controlada del volumen de datos (p. ej. trazas) manteniendo representatividad estadística.
- <a id="saturacion"></a>**Saturación**: uso de un recurso cerca de su límite de capacidad (CPU, conexiones, cola, disco); aumenta la latencia y la probabilidad de errores.
- <a id="outlier"></a>**Outlier (valor atípico)**: dato que se desvía significativamente del patrón o distribución habitual; puede indicar ruido, error de medición o un síntoma real a investigar.

## Operación
- <a id="alert-fatigue"></a>**Alert fatigue**: desensibilización por exceso de alertas irrelevantes o ruidosas.
- <a id="runbook"></a>**Runbook**: guía operativa paso a paso para responder a una alerta o incidente.
