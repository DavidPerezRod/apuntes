# Buenas prácticas

1. **Qué medir (mínimo por servicio)**
   El objetivo de las métricas debe ser cubrir un “mínimo viable” que permita detectar degradaciones con rapidez y, sobre todo, saber *dónde mirar* cuando algo va mal. Si no hay un criterio, se acaba midiendo demasiado o midiendo cosas que no sirven para operar.

   - **Golden Signals**
     Son cuatro categorías que, si están bien cubiertas, suelen explicar la mayoría de incidentes operativos:

     - **Latencia (p. ej. p95/p99)**
       Mide *cómo de rápido responde* el servicio. Es preferible usar percentiles (p95/p99) y no solo promedio, porque el promedio oculta colas y outliers.
       - Ejemplos: `http.server.requests` p95/p99, latencia de una query a DB, latencia de un proveedor externo.

     - **Tráfico/throughput (RPS, mensajes/s)**
       Mide *cuánta carga está absorbiendo* el servicio.
       - Ejemplos: requests por segundo, mensajes consumidos por segundo, operaciones de negocio por minuto (p. ej. “pagos confirmados/min”).

     - **Errores (ratio 4xx/5xx, timeouts, fallos lógicos)**
       Mide *cuántas operaciones fallan* y, si es posible, *por qué tipo*.
       - Técnicos: 5xx, excepciones, timeouts, fallos de DB, circuit breaker abierto.
       - De negocio: validaciones, “saldo insuficiente”, “OTP inválido” (esto no siempre es incidente; depende del SLO/impacto).

     - **Saturación**
       Mide *cómo de cerca estás del límite* (cuando te acercas, la latencia y los errores suelen subir).
       - **CPU**: si la CPU se sostiene alta, aumentan colas y latencia.
       - **Memoria**: presión de heap, GC, OOM; cuidado con “memoria libre” como única señal.
       - **Red / I/O**: ancho de banda, errores de red, latencia de red, I/O wait.
       - **Pools y colas**: thread pools, conexiones DB, colas en brokers, backlog en consumidores, etc.

2. **Instrumentación**
   La instrumentación es “hacer observable” el servicio: exponer métricas consistentes y comparables entre servicios.

   - Instrumentación automática del framework (HTTP server/client, DB, colas) + instrumentación manual para métricas de negocio.
   - Exposición mediante endpoint (por ejemplo `/metrics` o `/actuator/prometheus`) o exportación vía collector.
   - En Spring, típicamente con **Micrometer**; y a partir de ahí exportación a Prometheus u OpenTelemetry.

3. **Modelo dimensional (tags) y cardinalidad**
   Una métrica sin dimensiones suele ser poco útil; con demasiadas dimensiones, destruye el sistema de métricas.

   - Etiquetar con dimensiones útiles (baja cardinalidad): `service`, `env`, `region`, `cluster`, `namespace`, `method`, `status`, `route` normalizado.
   - Evitar cardinalidad alta (`userId`, `orderId`, `ip`, `email`, `documentUuid`): aumenta series únicas, degrada rendimiento y eleva costes.
   - Si necesitas visibilidad por entidad, suele pertenecer a **logs** o **trazas**, no a métricas.

# Cardinalidad de métricas (Prometheus/Micrometer)

## La idea clave

El problema real no es “cuántos labels” tiene una métrica, sino cuántas **combinaciones distintas** generan esos labels.

Cada combinación distinta de valores crea una **serie temporal nueva** guardada en la base de datos.

* Si se tiene un label `status` con 3 valores posibles (`200`, `400`, `500`), se tienen **3 series**.  
* Si se añade `method` con 2 valores (`GET`, `POST`), se tienen **3 × 2 = 6 series**.  
* Si se añade `route` con 50 rutas, se pasa a **6 × 50 = 300 series**.  
* si también se añade`instance` con 10 pods, aumenta hasta **300 × 10 = 3.000 series**.


Esa multiplicación es la razón de la regla 0: lo que “mata” es el número de **series totales**, no el número de labels en abstracto.

Un label “peligroso” como `user_id` revienta todo porque tiene muchísimos valores distintos: aunque sea un único label, si aparecen 100.000 usuarios distintos, ya son **100.000 series**.



## ¿Cuántos valores distintos son recomendables por label?

Heurísticas que pueden ayudar con este tipo de problemas:

- **2–10 valores** → lo ideal
- **10–50 valores** → normalmente aceptable
- **50–200 valores** → con cuidado (se debe vigilar crecimiento)
- **200–1.000 valores** → en general mala idea
- **> 1.000 valores** → no
- **No acotado / crece por request** (IDs, UUIDs, emails, IPs, traceId, requestId…) → prohibido como label

## Ejemplos

### Bien (cardinalidad acotada y útil)
- `method` → 4–8 valores (GET/POST/PUT/DELETE…)
- `status_class` → 3–5 valores (2xx/3xx/4xx/5xx…)
- `env` → 2–4 valores (dev/test/prod)
- `service` → 5–50 valores (depende de tu plataforma)
- `route` (plantilla) → 20–200 valores (depende de nº de endpoints)

### Mal (cardinalidad alta o no acotada)
- `userId` → miles/millones
- `documentUuid` → “infinito práctico”
- `url` completa (con ids, query params) → “infinito práctico”
- `error_message` literal → miles de variantes

---

## Regla simple para decidir (sin pensar mucho)

* **Si no puedes enumerar los posibles valores del label en una pizarra, no es un label.**
* **Si el valor puede ser distinto para cada request, no es un label: eso va a logs o trazas.**