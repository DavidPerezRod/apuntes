# Prometheus `prometheus.yml`

En el fichero de configuración de prometheus, prometheus.yml se configuran 4 bloques:

## 1) `global`

**Qué es**
- Defaults globales que se aplican a *scrapes* y a evaluación de reglas, salvo que un job los sobrescriba.

**Características relevantes**
- `scrape_interval`
  - Frecuencia de recolección.
  - Trade-off: +resolución vs +carga (targets + Prometheus).
- `scrape_timeout`
  - Tiempo máximo esperando respuesta del target.
  - Regla práctica: `scrape_timeout < scrape_interval` para evitar solapes.
- `evaluation_interval`
  - Frecuencia con la que Prometheus evalúa rules (recording/alerting).
  - Impacta latencia de alertas y “frescura” de series precomputadas.

**Notas típicas**
- Puedes dejar valores “rápidos” (15s) para labs; en prod suele ajustarse por coste/capacidad.
- Se puede sobrescribir por job (muy común).



## 2) `scrape_configs`
**Qué es**
- La lista de “jobs”/fuentes que Prometheus va a scrapear.
- Es el bloque central: sin esto no hay datos útiles.

**Características relevantes**
- `job_name` (por job)
  - Identificador lógico del job.
  - Se añade la label `job="<job_name>"` a todas las series del job.
  - En la práctica es necesario: si falta suele invalidar el config.
- `static_configs`
  - Targets definidos a mano: `targets: ["host:port"]`.
  - Ideal para local/docker; menos común en K8s (allí se usa discovery).
- `metrics_path` (opcional, por job)
  - Ruta HTTP (default `/metrics`).
- `scheme` (opcional, por job)
  - `http`/`https` (default `http`).
- `labels` (dentro de `static_configs`)
  - Labels fijas aplicadas a todo lo scrapeado por esos targets.
  - Útil para `env`, `cluster`, `app`…
  - Ojo con:
    - pisar labels existentes del target,
    - cardinalidad alta (no meter valores variables tipo IDs).
- Overrides por job
  - `scrape_interval`/`scrape_timeout` por job para targets más/menos críticos.

**Notas típicas**
- `localhost:9090` solo tiene sentido si Prometheus “vive” en ese mismo namespace/host/red.
  - En contenedores, `localhost` suele ser el propio contenedor, no tu host.



## 3) `rule_files`
**Qué es**
- Lista de ficheros con reglas que Prometheus carga y evalúa periódicamente.

**Qué aporta**
- Recording rules
  - Precomputan series agregadas/derivadas (mejor rendimiento en dashboards).
  - Estandarizan “métricas de negocio/servicio” repetidas.
- Alerting rules
  - Definen condiciones que generan alertas (ej. error rate, latencia, down).

**Características relevantes**
- Se evalúan cada `evaluation_interval` (global) y según el `for:` de cada alerta.
- Sin `rule_files` no hay alertas “de verdad” (aunque tengas Alertmanager).



## 4) `alerting`
**Qué es**
- Define a qué Alertmanager(s) Prometheus envía las alertas generadas por las alerting rules.

**Características relevantes**
- `alertmanagers` + `static_configs` + `targets`
  - Lista de endpoints `host:port` de Alertmanager.
- Prometheus NO “enruta” alertas
  - Solo las entrega; el routing/dedupe/silences lo hace Alertmanager.

**Notas típicas**
- Si los targets están comentados o vacíos, Prometheus no enviará nada.
- En setups modernos (Operator), esto suele venir gestionado por CRDs.



## Lectura rápida del fichero de tu imagen
- `global`: scrapes y evaluación cada 15s.
- `alerting`: presente pero sin alertmanager activo (target comentado).
- `rule_files`: comentado → no hay reglas cargadas.
- `scrape_configs`: scrape a `localhost:9090` + label fija `app="prometheus"`.
- Importante: `job_name` aparece comentado; lo normal es activarlo para que el config sea válido.
