# 2. Conceptos básicos de RabbitMQ

Además de los [elementos esenciales que componen el sistema de mensajería AMQP](1-introduccion.md) otros conceptos que conviene manejar son los que se ven en esta sección:

## 2.1 Durabilidad de un mensaje

| Concepto RabbitMQ   | Rol                                                                                                        |
| ------------------- | ---------------------------------------------------------------------------------------------------------- |
| **Durabilidad**       | RabbitMQ garantiza que un mensaje no se perderá en el servidor a pesar de fallos o reinicios    |
| **Transient**       | Es la configuración por defecto de los mensajes en Rabbit, lo cual significa que los mensajes serán efímeros y se perderán entre reinicios y/o fallos    |
| **Durable**       | Para hacer que un mensaje sea persistente a fallos, hay que hacer dos cosas <br> - Declarar la cola como "durable" durante su creación, que indicará a Rabbit que debe guardar su configuración durante los reinicios <br> - Declarar el mensaje como persistente cuando se crea, esto se hace poniendo la propiedad  **"Delivery Mode"** como _persistent_ |

### 2.1.1 Valores por defecto y combinaciones

| Cola | Mensaje | Efecto ante reinicio del broker |
| ---- | ------- | ------------------------------ |
| `durable=false` (por defecto) | `delivery_mode=1` transient (por defecto) | Se borra la cola y se pierden los mensajes. |
| `durable=true` | `delivery_mode=1` transient | La cola sobrevive, los mensajes no. |
| `durable=false` | `delivery_mode=2` persistent | El broker intenta persistir, pero al desaparecer la cola en el reinicio se pierden los mensajes asociados. |
| `durable=true` | `delivery_mode=2` persistent | Modelo recomendado: cola y mensajes sobreviven al reinicio. Añadir publisher confirms para asegurarse de que el broker los grabó. |

### 2.1.2 Ejemplo (Spring) publicando un mensaje durable

```java
// Declarar la cola durable en una @Configuration
@Bean
public Queue ordersQueue() {
    return QueueBuilder.durable("orders.queue").build();
}

// Publicar un mensaje persistente
@Autowired
private RabbitTemplate rabbitTemplate;

public void publishOrder(String payload) {
    Message message = MessageBuilder.withBody(payload.getBytes(StandardCharsets.UTF_8))
            .setDeliveryMode(MessageDeliveryMode.PERSISTENT) // delivery_mode=2
            .build();
    rabbitTemplate.convertAndSend("", "orders.queue", message);
}
```


## 2.2 Características avanzadas de colas en RabbitMQ

### 2.2.1 Cola exclusiva

#### Definición

Una **cola exclusiva** (`exclusive = true`) es una cola que:

- Está asociada **a una única conexión TCP** al broker.
- Solo puede tener **consumidores** en esa conexión.
- Se **elimina automáticamente** cuando esa conexión se cierra.

> Exclusiva = ligada a una conexión concreta (no a un “usuario” o a un “microservicio” de forma abstracta).

#### ¿Qué significa “la conexión que la creó”?

En RabbitMQ las aplicaciones se conectan al broker vía TCP (normalmente AMQP sobre `amqp://...`). Cada vez que se abre una conexión (por ejemplo, usando un `ConnectionFactory` en Java), esa conexión puede:

- Declarar colas (`queueDeclare`).
- Declarar exchanges.
- Crear consumidores, etc.

Cuando se dice que una cola es **exclusiva**, se está diciendo:

- “Esta cola solo puede tener consumidores que cuelguen de **esta conexión** concreta que la ha declarado”.

Otros puntos importantes:

- **Otros productores SÍ pueden publicar mensajes** a esa cola a través de un exchange.
  - La exclusividad afecta a la parte clientes, **consumidores y operaciones de control**.
- Ninguna otra conexión puede:
  - Crear un consumidor sobre esa cola.
  - Redefinirla.
  - Usarla como si fuera suya para consumir.

> ¿Exclusive=true y autoDelete=false? RabbitMQ lo permite, pero el efecto práctico es que cuando la conexión dueña se cierra, la cola se borra igualmente (exclusive manda). `autoDelete=false` solo evita que se elimine cuando se quede sin consumidores; no evita la eliminación al cerrarse la conexión propietaria.

#### ¿Cómo encaja esto con RPC (remote procedure call)?

En el patrón típico de **RPC sobre RabbitMQ** (request–reply), suele pasar lo siguiente:

1. El **cliente** abre una conexión al broker.
2. Sobre esa conexión, el cliente:
   - Declara una **cola exclusiva y auto-delete** (por ejemplo, con nombre aleatorio) para recibir la respuesta:
     - `exclusive = true`
     - `autoDelete = true`
3. El cliente envía un **mensaje de petición** a una cola “pública” (por ejemplo, `rpc.requests`) a través de un exchange, con:
   - `reply_to = <nombre_de_mi_cola_exclusiva>`
   - Un `correlation_id` si quiere matchear respuesta con petición.
4. El **servidor RPC**:
   - Está suscrito a la cola `rpc.requests`.
   - Procesa la petición.
   - Publica la **respuesta** en el exchange correspondiente con `routing_key` = nombre de la cola de respuesta (`reply_to`), que es la **cola exclusiva del cliente**.
5. El **único que consume de esa cola exclusiva** es el propio cliente (en la misma conexión donde la creó).
6. Cuando el cliente cierra la conexión (o se cae el proceso), RabbitMQ elimina esa cola.

Aclaración clave:

- En este caso hay “dos extremos” (cliente y servidor), pero:
  - **Quien “crea” la cola exclusiva es el cliente**, con su conexión.
  - El servidor **no consume** de esa cola; solo **publica** mensajes hacia ella (el broker los enruta).
  - La exclusividad garantiza que **ningún otro consumidor** (en otra conexión) pueda interferir en las respuestas del cliente.


### 2.2.2 TTL de cola y de mensaje

**TTL (Time To Live)** es el tiempo de vida máximo.

#### 2.2.2.1 TTL de mensaje

Define cuánto tiempo puede permanecer un **mensaje** en la cola antes de considerarse caducado.

Se puede configurar:

- A nivel de **cola** (`x-message-ttl`): se aplica a todos los mensajes publicados en esa cola.
- A nivel de **mensaje** (propiedad `expiration`): cada mensaje puede traer su propio TTL.

Cuando un mensaje caduca:

- No se entrega al consumidor.
- Se **descarta** o se envía a un **Dead Letter Exchange (DLX)** si está configurado.

#### 2.2.2.2 TTL de cola

Se configura con el argumento `x-expires`:

- Indica cuánto tiempo puede estar una **cola** sin usarse (sin consumidores, sin nuevas publicaciones, sin conexiones) antes de que RabbitMQ la elimine automáticamente.
- Es útil para colas **temporales** (por ejemplo, generadas dinámicamente para pruebas o sesiones).

Resumen:

- **TTL de mensaje** → caduca el mensaje.
- **TTL de cola** → caduca la cola.


### 2.2.3 Límites de longitud (número de mensajes o tamaño total)

RabbitMQ permite controlar cuánto puede crecer una cola:

- **Número máximo de mensajes** (`x-max-length`).
- **Tamaño máximo total en bytes** (`x-max-length-bytes`).

Objetivo:

- Evitar que una cola se convierta en un **backlog infinito** que consuma toda la memoria o el disco del broker.

Cuando se alcanza el límite:

- El comportamiento por defecto suele ser **drop-head**:
  - Se van eliminando los **mensajes más antiguos** para hacer espacio a los nuevos.
- También se puede combinar con DLX para derivar esos mensajes expulsados a otra cola.

### 2.2.4 Prioridades de mensaje (0–255, x-max-priority)

Permiten que algunos mensajes se procesen **antes que otros**, aunque hayan llegado más tarde.

#### 2.2.4.1 Cómo se configura

1. Al declarar la cola, añades el argumento:
   - `x-max-priority = N` (por ejemplo, 10).
2. Eso indica que los mensajes podrán tener una prioridad entre 0 y N.
3. Cuando publicas un mensaje, puedes establecer la propiedad `priority` (por ejemplo, 0–10 en este caso).

Ejemplo (Spring AMQP):

```java
// Declarar cola con prioridad habilitada (durable)
@Bean
public Queue priorityQueue() {
    return QueueBuilder.durable("orders.priority")
            .withArgument("x-max-priority", 10)
            .build();
}

// Publicar con prioridad alta (p.ej. 8 de 0-10)
@Autowired
private RabbitTemplate rabbitTemplate;

public void publishUrgentOrder(String payload) {
    Message message = MessageBuilder.withBody(payload.getBytes(StandardCharsets.UTF_8))
            .setPriority(8) // priority header
            .setDeliveryMode(MessageDeliveryMode.PERSISTENT)
            .build();
    rabbitTemplate.convertAndSend("", "orders.priority", message);
}
```

#### 2.2.4.2 Cómo se comporta

- Cuanto **mayor prioridad**, **antes se entregan** esos mensajes.
- Dentro de cada nivel de prioridad, se respeta el orden de llegada (FIFO por prioridad).

Ejemplo:

- Prioridad 9 → mensajes “urgentes”.
- Prioridad 1 → mensajes “normales”.

El broker intentará vaciar primero los de prioridad 9.

#### 2.2.4.3 Coste

- Gestionar prioridades añade complejidad interna al manejo de la cola.
- Si tienes muchas colas grandes con prioridad, puede impactar en el rendimiento.
- En el consumidor no “aplicas” prioridad: recibes lo que el broker ya reordenó según el header `priority`. La prioridad del consumidor es otro concepto aparte (`2.2.5`).


### 2.2.5 Prioridad de consumidor

Aquí la prioridad no está en el mensaje, sino en el **consumidor**.

#### 2.2.5.1 Qué es

Cuando creas un consumidor (`basic.consume`), puedes pasar el argumento:

- `x-priority = valor`.

RabbitMQ utiliza ese valor para decidir a qué consumidor de la **misma cola** le va entregando los mensajes:

- Consumidores con **mayor prioridad** reciben mensajes **antes**.
- Solo si esos consumidores con prioridad alta no dan abasto o no están disponibles, el broker empieza a enviar mensajes a los de prioridad más baja.

#### 2.2.5.2 Ejemplos de uso

Escenarios típicos:

1. **Activo / backup**:
   - Consumidor A con prioridad 10 (principal).
   - Consumidor B con prioridad 5 (respaldo).
   - Mientras A esté disponible, se llevará casi todo el tráfico.
   - Si A cae, B pasa a procesar.

2. **Canary / producción**:
   - Varios consumidores de producción con prioridad alta.
   - Uno o pocos consumidores “canary” con prioridad más baja, que solo reciben mensajes cuando hay más carga o de forma controlada.
3. **Centro de datos principal** (DC1) y **secundario** (DC2). Ayuda a dirigir el tráfico
   - Consumidores en DC1 con prioridad alta (quieren procesar el tráfico cuando todo va bien).
   - Consumidores en DC2 con prioridad más baja (backup, para casos de fallo o de aumento de carga).


### 2.2.6 Dead Letter Exchanges (DLX)

Un **Dead Letter Exchange (DLX)** es un exchange al que RabbitMQ envía mensajes que no se han podido procesar “normalmente” en una cola.

#### 2.2.6.1 Cuándo un mensaje se convierte en “dead letter”

Un mensaje se marca como “dead letter” y se envía al DLX cuando ocurre alguna de estas situaciones (entre otras):

- Expira su **TTL de mensaje**.
- Es expulsado de la cola por límite de longitud (`x-max-length`, `x-max-length-bytes`).
- Un consumidor hace `basic.reject` o `basic.nack` con `requeue = false`.
- Se ha alcanzado un número de reentregas definido (cuando implementas un mecanismo de contador de entregas + límite).

**Cómo forzar el `requeue=false` en Spring AMQP**

- Vía código (granular): lanzar `AmqpRejectAndDontRequeueException` desde el listener para traducir la excepción a `nack/reject` con `requeue=false` y que el mensaje vaya al DLX en vez de volver a la cola.

```java
@RabbitListener(queues = "orders.process")
public void consume(String payload) {
    try {
        process(payload);
    } catch (RecoverableBusinessException e) {
        // Reintentos internos, log, etc.
    } catch (Exception e) {
        // Lo declaramos irrecuperable → a DLQ
        throw new AmqpRejectAndDontRequeueException("Irrecuperable, no requeue", e);
    }
}
```

- Vía configuración YAML (global por contenedor): desactivar el requeue de rechazados.

```yaml
spring:
  rabbitmq:
    listener:
      simple:
        default-requeue-rejected: false
      direct:
        default-requeue-rejected: false
```

Beneficios y cuándo usar cada enfoque:

- Excepción en código:
  - ✅ Decide caso a caso qué mensajes son irrecuperables, mezclando reglas de negocio.
  - ✅ No afecta a otros listeners: solo los que lanzan la excepción evitan el requeue.
  - ⚠️ Necesita disciplina: si no se lanza, el mensaje se reencola y puede generar bucles.
  - Úsalo cuando: distintas colas tengan políticas diferentes o quieras marcar errores irrecuperables de forma explícita desde el handler.
- Configuración YAML:
  - ✅ Sencillo y centralizado: todos los errores no controlados van directos a DLQ (sin requeue).
  - ✅ Menos try/catch en el código; evita loops de reentrega infinitos por defecto.
  - ⚠️ Más agresivo: cualquier excepción manda el mensaje fuera de la cola; si era transitoria, se pierde la oportunidad de reintentar en caliente.
  - Úsalo cuando: prefieras defaults seguros contra loops y tengas reintentos gestionados con DLQ/TTL, o en entornos donde los fallos suelen ser no recuperables.

**Valores por defecto en Spring AMQP**

- `default-requeue-rejected = true` → cualquier excepción no gestionada reencola el mensaje (sin DLX se puede quedar en loop).
- `acknowledge-mode = auto` → Spring hace el `ack` si el listener termina sin excepciones; si lanza excepción y no cambias el requeue, el broker reencola.

**¿Y `acknowledge-mode: manual`?**

Esto cambia el modo de confirmación del listener a manual: el consumidor debe hacer `ack/nack/reject` de cada entrega. Te da control absoluto (ej. diferenciar transitorios, retrasar el ack hasta terminar varias operaciones), pero requiere código explícito y evita los “atajos” automáticos de Spring.

```yaml
spring:
  rabbitmq:
    listener:
      simple:
        acknowledge-mode: manual
```

Ejemplo con control manual:

```java
@RabbitListener(queues = "orders.process")
public void consume(Message msg, Channel channel) throws IOException {
    try {
        process(msg);
        channel.basicAck(msg.getMessageProperties().getDeliveryTag(), false);
    } catch (TransientException e) {
        // Queremos reintentar → requeue=true
        channel.basicNack(msg.getMessageProperties().getDeliveryTag(), false, true);
    } catch (Exception e) {
        // Irrecuperable → requeue=false (va a DLQ si la hay)
        channel.basicReject(msg.getMessageProperties().getDeliveryTag(), false);
    }
}
```

Comparativa rápida:

- `default-requeue-rejected: false` (auto-ack gestionado por Spring):
  - ✅ Menos código; cualquier excepción manda a DLQ (sin requeue).
  - ⚠️ No diferencia transitorios vs. irrecuperables salvo que lances la excepción específica.
- `acknowledge-mode: manual`:
  - ✅ Control fino por mensaje: decides en tiempo real requeue sí/no, incluso batch de acks.
  - ⚠️ Más código y riesgo de olvidos (si no ack/nack, los mensajes quedan unacked).
  - Úsalo cuando necesites lógica de reintentos diferenciada por tipo de error o coordinar el ack con varias operaciones externas antes de confirmar.

#### 2.2.6.2 Cómo se configura

En la cola “normal” defines argumentos como:

- `x-dead-letter-exchange`: nombre del exchange al que deben enviarse los mensajes muertos.
- `x-dead-letter-routing-key` (opcional): routing key a usar al reenviar al DLX.

En el DLX, a su vez, tienes bindings hacia otras colas (por ejemplo, una cola de errores, una de reintentos, una “parking lot”, etc.).

#### 2.2.6.3 Para qué sirve

- **Reintentos diferidos**:
  - Mensaje falla → va al DLX → se enruta a una cola de reintentos con TTL → cuando expira, vuelve a la cola original.
- **Parking lot**:
  - Mensajes que fallan repetidamente van a una cola “de aparcamiento” para inspección manual o tratamiento especial.
- **Auditoría y monitorización**:
  - Puedes analizar patrones de errores mirando lo que llega al DLX.

### 2.2.7 Tabla resumen de características avanzadas de colas en RabbitMQ

| Característica                      | Qué es (resumen)                                                                                  | Para qué sirve                                                                                          | Ejemplo típico                                                                                  |
|-------------------------------------|----------------------------------------------------------------------------------------------------|---------------------------------------------------------------------------------------------------------|--------------------------------------------------------------------------------------------------|
| Exclusividad de cola                | Cola ligada a **una sola conexión**: solo esa conexión puede declararla/consumirla. Se borra al cerrarse la conexión. | Asegurar que la cola de respuesta o de trabajo sea **privada** de un cliente/proceso concreto.         | Cola de respuesta en un patrón **RPC**: cada cliente crea su cola exclusiva para recibir replies. |
| TTL de mensaje (`x-message-ttl` / `expiration`) | Tiempo máximo que un **mensaje** puede permanecer en la cola antes de caducar.                    | Evitar mensajes obsoletos y automatizar la caducidad de peticiones viejas o eventos expirados.         | Mensajes de “código OTP” que solo tienen sentido durante 60 segundos.                           |
| TTL de cola (`x-expires`)           | Tiempo máximo que una **cola** puede estar sin uso antes de ser eliminada automáticamente.        | Limpiar colas temporales sin intervención manual, liberando recursos en el broker.                     | Colas de sesión o de pruebas que deben desaparecer si nadie las usa durante X minutos.          |
| Límite de longitud (`x-max-length`, `x-max-length-bytes`) | Tope en nº de mensajes o en tamaño total de la cola. Al superarlo, se expulsan mensajes (p.ej. los más antiguos). | Evitar que una cola crezca sin control y acabe saturando memoria o disco del broker.                   | Cola de logs o eventos donde solo interesa mantener, por ejemplo, los últimos 10.000 mensajes.  |
| Prioridades de mensaje (`x-max-priority`) | Permite que los mensajes tengan un campo `priority` (0–N) y que los de prioridad alta se entreguen antes. | Tratar primero mensajes **urgentes** frente a los normales, manteniendo orden FIFO dentro de cada nivel de prioridad. | Notificaciones críticas (prioridad alta) frente a notificaciones informativas (prioridad baja). |
| Prioridad de consumidor (`x-priority` en `basic.consume`) | Peso asignado a cada **consumidor** de una cola. Los de prioridad alta reciben mensajes antes que los de baja. | Dirigir el tráfico primero a ciertos consumidores (p.ej. en un DC principal) y usar otros como backup. | Consumidores en el **data center principal** con prioridad alta y en un segundo DC con prioridad baja. |
| Dead Letter Exchange (DLX)          | Exchange al que se envían mensajes que no se han podido procesar “normalmente” (TTL, rechazos, límites, etc.). | Separar mensajes problemáticos, implementar reintentos, colas de parking y auditoría de errores.       | Mensajes que fallan 3 veces pasan a una cola “parking_lot” para revisión manual.                |
