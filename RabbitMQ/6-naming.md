# 6. Guía de naming para RabbitMQ
*(exchanges, colas, routing keys, DLX, etc.)*

## 6.1 Principios generales de naming

- Usar **minúsculas** y un **separador consistente**:
  - Lo más común: `.` (a veces `_`)
  - Ejemplos:
    - `user.registered.email`
    - `billing.payment.requested`
- Priorizar **semántica de negocio** sobre detalles técnicos:
  - Malo: `microserviceA_queue_1`
  - Mejor: `payments.invoice.created.queue`
- **Versionar** cuando cambie el esquema del mensaje:
  - `user.registered.v1`, `user.registered.v2`
  - O versionar el exchange: `x.user.v1`, `x.user.v2`
- Maximizar la **consistencia**:
  - Mantener el mismo patrón en todos los dominios y microservicios.

## 6.2 Naming de Exchanges

### 6.2.1 Exchanges de dominio (topic/direct)

- Basados en dominio, a menudo con prefijo `x.`:
  - `x.user`
  - `x.payment`
  - `x.order`
- Con versionado:
  - `x.user.v1`
  - `x.payment.v1`
- Estilo “event bus”:
  - `x.user.events`
  - `x.billing.events`
  - **Significa**: un exchange por dominio/servicio que recibe todos los eventos, y cada evento se diferencia solo por la routing key (`user.registered.v1`, `billing.invoice.paid.v1`). Centralizas publicación en un único exchange de dominio y usas patrones/bindings para afinar la entrega.

**Estrategia típica:**

- `x.user` → todos los eventos relacionados con usuarios  
- `x.payment` → eventos relacionados con pagos

Las routing keys afinan qué tipo de evento es.

### 6.2.2 Exchanges internos / técnicos

- Dead-letter exchanges:
  - `x.payment.dlx`
  - `x.user.dlx`
- Exchanges de reintentos:
  - `x.payment.retry`
- Exchanges para RPC:
  - `x.document.rpc`
  - Nota: el atributo **exclusive** aplica a colas, no a exchanges. En RPC se suele usar un exchange normal (direct/topic); lo habitual es que la *reply queue* sea exclusiva/auto-delete por cliente, no el exchange.

**Regla:** que se vea claramente que son de **infraestructura**, no de negocio.

## 6.3 Naming de Colas

Objetivo: que el nombre cuente **qué consume** y **quién lo consume**.

### 6.3.1 Colas “principales” por servicio

Objetivo: nombre legible que diga **quién consume** y **qué evento/tema consume**.

Patrones claros:

- `<servicio>.<dominio|evento>.q` (corto y directo)  
  - `notification.user.registered.q`
  - `analytics.user.activity.q`
- `<servicio>.<dominio>.<acción>.q` (cuando hay varias acciones)  
  - `invoice-service.invoice.created.q`
  - `invoice-service.invoice.paid.q`
- Basado en la routing key si ya es expresiva (mismo verbo/sustantivo):  
  - Routing key: `payment.requested.v1` → Cola: `payments.payment.requested.q`
  - Routing key: `payment.completed.v1` → Cola: `payments.payment.completed.q`
  - Ligado 1 a 1: la cola lleva el mismo “núcleo” que la routing key que consume.

**Regla:** que al leerla se entienda “la cola del servicio X para el evento Y”.

### 6.3.2 Dead-letter queues (DLQ)

Patrón extendido:

- Misma base + sufijo `dlq`:
  - `invoice-service.invoice.created.dlq`
  - `payments.payment.requested.dlq`
- DLQ compartidas por dominio:
  - `billing.dlq`
  - `user.dlq`

En microservicios, suele ser buena idea:

- Cola normal: `payments.payment.requested.q`
- Cola DLQ: `payments.payment.requested.dlq`

### 6.3.3 Colas de retry

Si se implementa retry con colas + TTL:

- `payments.payment.requested.retry.1m`
- `payments.payment.requested.retry.5m`

o más simple:

- `payments.payment.requested.retry`

## 6.4 Routing Keys: buenas prácticas

Se suele usar un estilo **tipo DNS**, jerárquico.

### 6.4.1 Eventos de dominio

Patrón muy común:

`<dominio>.<entidad>.<acción>[.<versión>]`

Ejemplos:

- `user.registered.v1`
- `user.email.changed.v1`
- `payment.initiated.v1`
- `payment.completed.v1`

Nota de estilo: los eventos suelen ir en **pasiva/perfecto** (“completed”, “registered”, “created”) para indicar hechos ya ocurridos, no órdenes.

**Ventajas:**

- Suscripciones con comodines:
  - `user.*.v1` → todos los eventos de usuario
  - `user.email.*` → todo lo relacionado con email
  - `*.completed.v1` → todos los “completed” de cualquier entidad

### 6.4.2 Comandos / mensajes de trabajo

Para comandos (no solo eventos):

- Comandos:
  - `payments.command.authorize`
  - `document.command.render`
- Respuestas:
  - `payments.reply.authorize`
  - `document.reply.render`

Estilo “task queue”:

- `invoice.generate`
- `invoice.cancel`

Notas de estilo:

- Los comandos suelen ir en **infinitivo/imperativo** (“generate”, “authorize”) para marcar acción/intención y distinguirlos de eventos en pasiva.
- Las respuestas pueden mantener el verbo del comando (`authorize`) o usar sufijos (`.ok`, `.error`) según convenga.

**Regla:** no mezclar acciones distintas bajo la misma routing key salvo que realmente vayan al mismo handler semántico.

## 6.5 Bindings: expresar la intención

Los bindings son relaciones (exchange–queue–pattern).

**Buenas prácticas:**

- Evitar patrones excesivamente genéricos (`#`) salvo para logging/monitorización/debug.
- Usar `*` cuando agrupa de forma lógica:
  - Cola de auditoría de usuario:
    - Binding: `user.*.v1`
  - Cola de notificaciones de usuario:
    - `user.registered.v1`
    - `user.password.reset.v1`
- Servicios genéricos (ej. `notification-service`):
  - Cola: `notification.email.q`
  - Bindings a distintos exchanges:
    - `x.user` → `user.registered.v1`
    - `x.billing` → `billing.invoice.overdue.v1`

**Regla:** la lógica de suscripción debe ser legible solo viendo bindings y nombres.

## 6.6 Naming de Dead Letter Exchanges (DLX) y DLQ

### 6.6.1 DLX y DLQ por cola

Ejemplo:

- Cola normal:
  - `payments.payment.requested.q`
  - `x-dead-letter-exchange` → `x.payments.dlx`
  - `x-dead-letter-routing-key` → `payments.payment.requested.dlq`
- DLX:
  - `x.payments.dlx`
- DLQ:
  - `payments.payment.requested.dlq`

### 6.6.2 DLX compartido por dominio

- DLX por dominio:
  - `x.billing.dlx`
- Varias DLQ:
  - `billing.invoice.created.dlq`
  - `billing.invoice.paid.dlq`

**Regla:** el nombre debe dejar claro:

1. Dominio o contexto.
2. Que es DLX (`.dlx`) o DLQ (`.dlq`).

## 6.7 Convención por microservicio

Modelo típico en microservicios:

**Exchanges:**

- `x.<service>.events`
  - `x.user.events`
  - `x.payment.events`

**Routing keys:**

- `<entidad>.<evento>.v1`
  - `user.registered.v1`
  - `payment.authorized.v1`

**Colas (por consumidor):**

- `<consumer-service>.<publisher-service>.<evento>.q`
  - `notification.user.user.registered.q` (algo redundante)
- Simplificando:
  - `notification.user.registered.q`

**Regla fuerte:**

- Exchange + routing key → quién publica y qué evento es.
- Nombre de la cola → quién lo consume.

## 6.8 Antipatrones de naming

Evitar:

1. **Nombres genéricos y opacos**:
   - `queue1`, `queue2`, `testQueue`, `microserviceQueue`
2. **Incluir información volátil**:
   - Entorno (`dev`, `uat`, `prod`) en nombre de cola/exchange si ya se diferencia por vhost.
   - Tenant en nombre, si se puede resolver mejor por:
     - vhost
     - cabeceras
     - contenido del mensaje
3. **Naming basado solo en servicios**:
   - `serviceA.out`, `serviceB.in` sin contexto de dominio ni evento.
4. **Un único exchange global con routing keys caóticas**:
   - Termina siendo un “monolito de eventos” imposible de gestionar.
5. **No versionar** cuando cambian los esquemas de los mensajes:
   - Obliga a migraciones Big Bang y rompe consumidores viejos.

## 6.9 Propuesta de convención concreta (ejemplo)

Supongamos dominios:

- `user`
- `billing`
- `notification`

### 6.9.1 Exchanges

- Eventos de dominio:
  - `x.user`
  - `x.billing`
  - `x.notification`
- DLX por dominio:
  - `x.user.dlx`
  - `x.billing.dlx`

### 6.9.2 Routing keys

- User:
  - `user.registered.v1`
  - `user.email.changed.v1`
- Billing:
  - `billing.invoice.created.v1`
  - `billing.invoice.paid.v1`

### 6.9.3 Colas y DLQ

Ejemplo: `notification-service` envía email cuando se registra un usuario.

- Cola principal:
  - `notification.user.registered.q`
- Binding:
  - Exchange: `x.user`
  - Pattern: `user.registered.v1`
- DLQ:
  - `notification.user.registered.dlq`
- Config de DLX/DLQ:
  - En la cola `notification.user.registered.q`:
    - `x-dead-letter-exchange` = `x.user.dlx`
    - `x-dead-letter-routing-key` = `notification.user.registered.dlq`

**Lectura rápida:**

- `notification.user.registered.dlq` → DLQ del flujo de “user registered” consumido por `notification-service`.

## 6.10 Tabla resumen de naming propuesto

| Elemento                      | Patrón recomendado                      | Ejemplo                                | Nota rápida                                            |
| ----------------------------- | --------------------------------------- | -------------------------------------- | ------------------------------------------------------- |
| Exchange de dominio           | `x.<dominio>[.events][.vN]`             | `x.user`, `x.billing`, `x.payment.v1`  | Prefijo `x.` y versión si cambia el contrato.          |
| Dead-letter exchange (DLX)    | `x.<dominio>.dlx`                       | `x.user.dlx`, `x.billing.dlx`          | Uno por dominio suele bastar.                          |
| Routing key de evento         | `<dominio>.<entidad|acción>.vN`         | `user.registered.v1`, `payment.paid.v1`| Estilo DNS; versionar al cambiar el esquema.           |
| Routing key de comando/respuesta | `<dominio>.command|reply.<acción>`   | `payments.command.authorize`           | Diferenciar comandos de replies.                       |
| Cola principal (consumidor)   | `<servicio>.<dominio|evento>.q`         | `notification.user.registered.q`       | Nombre debe reflejar quién consume y qué consume.      |
| Dead-letter queue (DLQ)       | `<cola-base>.dlq` o `<dominio>.dlq`     | `notification.user.registered.dlq`     | Misma base que la cola normal + sufijo `dlq`.          |
| Cola de reintento             | `<cola-base>.retry[.<ttl>]`             | `notification.user.registered.retry.5m`| TTL en el nombre si ayuda a identificar el delay.      |

## 6.11 Versionar: ¿exchange o mensaje?

Sí tiene sentido la duda: se puede versionar el punto de entrada (exchange/routing key) o el contenido del mensaje. Consecuencias:

- **Versionar el exchange (p.ej., `x.user.v1` → `x.user.v2`):**
  - Pros: aísla completamente el tráfico; consumidores viejos y nuevos no se pisan; permite migraciones limpias.
  - Contras: hay que recrear bindings/colas; mayor “sprawl” de recursos; duplicas topología mientras conviven versiones.
  - Útil cuando: cambia el modelo de enrutado o la semántica de las routing keys; necesitas coexistencia clara entre buses.

- **Versionar la routing key/evento (p.ej., `user.registered.v1` → `.v2` en el mismo exchange):**
  - Pros: misma topología; solo ajustas bindings; los productores apuntan al mismo exchange.
  - Contras: si usas patrones genéricos (`user.*`) podrías consumir versiones no deseadas; necesitas disciplina en bindings.
  - Útil cuando: el contracto del mensaje cambia pero la estructura de ruteo es válida; quieres minimizar cambios de infra.

- **Versionar solo el payload (campo `version`, `schemaVersion`, etc.):**
  - Pros: cero cambios de infra; más sencillo para productores.
  - Contras: consumidores deben validar versión en runtime; riesgo de mezclar esquemas en la misma cola.
  - Útil cuando: el cambio es backward compatible o los consumidores soportan múltiples versiones.

**Regla práctica:**

- Mantén un exchange por dominio estable (`x.<dominio>`) y versiona las routing keys (`<dominio>.<evento>.vN`).
- Sube versión de exchange solo si necesitas cambiar la estrategia de enrutado o aislar completamente tráfico legado.
