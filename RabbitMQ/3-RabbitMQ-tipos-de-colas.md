
# 3. Tipos de colas en RabbitMQ

En RabbitMQ hay tres grandes tipos de colas/datos para la mayoría de casos: **classic queues**, **quorum queues** y **streams**. Cada una resuelve un problema distinto.

## 3.1 Classic queues (colas clásicas)

Son el tipo de cola original de RabbitMQ y **siguen siendo el tipo por defecto si no se indica lo contrario**.

### 3.1.1 Características principales

- No replicadas: un solo nodo mantiene los mensajes.
- Pueden ser:
  - **Durables** + mensajes persistentes (sobreviven a reinicios del nodo).
  - **No durables** + mensajes transitorios.
- Soportan muchas “features clásicas”:
  - Exclusividad de cola.
  - TTL de cola y de mensaje.
  - Límites de longitud (nº de mensajes o tamaño total).
  - Prioridades de mensaje (0–255, configurable con `x-max-priority`).
  - Prioridad de consumidor.
  - Dead Letter Exchanges (DLX).

### 3.1.2 Ventajas

- Sencillas, maduras y muy conocidas.
- Latencia baja y rendimiento muy bueno en colas cortas.
- Soportan más combinaciones de características (prioridades, exclusivas, no durables, etc.) que otros tipos.
- Perfectas para casos **no críticos** donde un fallo de nodo no es dramático.

### 3.1.3 Inconvenientes

- **No hay replicación**: si el nodo que aloja la cola se pierde definitivamente, se pierden los mensajes de esa cola.
- El enfoque antiguo de alta disponibilidad eran las “mirrored classic queues”, que hoy se considera problemático y está deprecado frente a quorum queues.
- Si se acumulan muchos mensajes (backlog grande), el rendimiento cae: las colas están pensadas para ser drenadas.

### 3.1.4 Cuándo usarlas

- Integraciones sencillas, tráfico moderado, donde puedes aceptar perder mensajes en un desastre de nodo.
- Casos donde necesitas:
  - Muchas features de cola “clásica” (por ejemplo, muchas prioridades).
  - Colas exclusivas por conexión (RPC simple, colas temporales de pruebas, etc.).
- Entornos de desarrollo/local, POCs y demos.

---

## 3.2 Quorum queues

Son el tipo moderno de cola replicada de RabbitMQ, basadas en el algoritmo de consenso **Raft**. Están pensadas para **alta disponibilidad** y **seguridad de datos**.

### 3.2.1 Características principales

- Siempre **durables** y **replicadas** entre varios nodos (líder + followers).
- Semántica **FIFO** fuerte (por cola).
- Diseñadas para “at-least-once delivery” con fuerte garantía de no pérdida de mensajes.
- Soportan:
  - TTL de mensajes y de cola.
  - Límites de longitud y de tamaño.
  - DLX y manejo de “poison messages” (por ejemplo con contador de entregas y `delivery-limit`).
  - En versiones recientes, prioridades de mensaje simplificadas:
    - Dos categorías efectivas: prioridad normal y alta.
    - Política de reparto favorable a la prioridad alta.

### 3.2.2 Ventajas

- Alta tolerancia a fallos: si cae el nodo líder, otro follower toma el liderazgo sin perder mensajes.
- Modelo de replicación moderno, más predecible y menos problemático que las mirrored classic queues.
- Buen rendimiento y latencia razonablemente estable para cargas de producción serias.
- Mejor soporte para gestión de mensajes problemáticos (poison messages).

### 3.2.3 Inconvenientes

- Más consumo de CPU, RAM y disco que una classic simple (hay replicación y log Raft).
- No existen variantes no durables o exclusivas; están pensadas para datos importantes.
- Implementación de prioridad más simple si vienes de classic con muchas prioridades distintas.

### 3.2.4 Cuándo usarlas

- **Opción recomendada** para la mayoría de casos de negocio donde **no quieres perder mensajes** y el sistema debe seguir funcionando ante fallos de nodo.
- Microservicios críticos (pagos, pedidos, workflows que no se pueden repetir sin control).
- Sustituto natural de las antiguas mirrored classic queues.
- Cuando quieres que “lo normal” sea producción HA sin tener que inventar nada raro.


## 3.3 Streams

RabbitMQ Streams introducen un modelo de **log append-only** (similar a Kafka) pensado para **alto throughput**, **baja latencia** y **relectura** de mensajes.

### 3.3.1 Conceptos previos

#### 3.3.1.1 “Modelo de log append-only”

En vez de una cola clásica de RabbitMQ donde:

* el mensaje entra,
* un consumidor lo lee,
* y el broker lo “retira” cuando se confirma (ACK),

en **Streams** hay algo más parecido a un *log*:

* Los mensajes se **añaden al final** (append) de una secuencia ordenada.
* No se “consumen y borran” al leerlos; se **quedan almacenados** según una política de retención (tiempo, tamaño, etc.).
* Cada consumidor mantiene su **posición (offset)** dentro del stream: “voy por el mensaje n”.

Eso es lo que lo hace “similar a Kafka”: ambos trabajan sobre un log ordenado, append-only, donde los consumidores avanzan su puntero.

#### 3.3.1.2 “Pensado para alto throughput”

El modelo append-only permite optimizar mucho el rendimiento porque:

* Las escrituras son básicamente **secuenciales** (no andan modificando cosas en medio), lo cual encaja muy bien con disco/SSD.
* Se pueden hacer **batches** de mensajes y operaciones más eficientes a nivel de IO.
* Varios consumidores pueden leer el **mismo stream** sin duplicar datos ni crear mil colas.

Resultado: el broker puede **aceptar y servir muchos mensajes por segundo** sin degradarse como lo haría una cola “normal” saturada de consumidores.


#### 3.3.1.3 “Baja latencia”

Al no tener que:

* reordenar mensajes,
* moverlos entre colas,
* ni realizar operaciones complejas para cada ACK,

la ruta “productor → stream → consumidor” se vuelve muy directa:

* el mensaje se escribe al final del log,
* el consumidor que está suscrito casi en tiempo real lo lee en cuanto aparece,
* todo el pipeline está optimizado para que el **tiempo de ida y vuelta sea muy bajo**.

Es decir, está pensado tanto para throughput como para que los mensajes lleguen rápido, no solo “en masa”.


#### 3.3.1.4 “Relectura de mensajes”

Como los mensajes **no desaparecen al ser consumidos** (hasta que la retención los purga):

* Puedes **volver a leer** desde un offset anterior:

  * para reprocesar datos,
  * para reconstruir estados,
  * para enganchar un nuevo servicio que necesita “ponerse al día” con el histórico.
* Diferentes consumidores pueden estar en posiciones distintas:

  * uno leyendo casi en tiempo real,
  * otro haciendo un backfill histórico.

Esto es la gran diferencia práctica frente a la cola clásica de RabbitMQ, donde **una vez consumido y ACKeado, el mensaje se fue**.


### 3.3.2 Características principales de Streams

- Estructura **no destructiva**: leer no elimina el mensaje; los consumidores gestionan su **offset**.
- Retención configurable por:
  - Tiempo (ej. “guarda los últimos 7 días”).
  - Tamaño en bytes.
- Semántica at-least-once, con publisher confirms y opciones de deduplicación.
- Clientes especializados (Java, Go, etc.) con protocolo de streams para tirar millones de mensajes/segundo por nodo.
- Pueden particionarse como **super streams** para escalar horizontalmente (varias particiones).

### 3.3.3 Ventajas

- Muy alta capacidad y rendimiento para flujo de eventos.
- Posibilidad de **reprocesar** o “viajar en el tiempo” (analytics, replay de eventos, depuración).
- Mantienes el ecosistema RabbitMQ, pero con semántica de plataforma de streaming.

### 3.3.4 Inconvenientes

- Más complejos de usar que una cola FIFO simple.
- No son la mejor opción cuando solo quieres una cola tradicional que se vacía.
- Necesitan pensar en retención, almacenamiento a largo plazo, offsets, etc.

### 3.3.5 Cuándo usarlos

- Event sourcing, logs de dominio, auditoría, analítica, integración con sistemas tipo Kafka.
- Sistemas donde:
  - Muchos consumidores leen el mismo historial.
  - Necesitas replay parcial o completo.
  - El volumen de mensajes por segundo es muy alto.

---

## 3.4 Classic vs Quorum: resumen rápido

| Aspecto              | Classic Queue                                  | Quorum Queue                                             |
|----------------------|-----------------------------------------------|----------------------------------------------------------|
| Replicación          | No                                            | Sí (Raft, líder + followers)                            |
| Durabilidad          | Opcional (durable o no durable)               | Siempre durable                                          |
| Tolerancia a fallos  | Baja (depende de un solo nodo)                | Alta (soporta caída de nodos)                           |
| Prioridades          | Completa (0–255)                              | Simplificada (normal/alta)                              |
| Features exóticas    | Más combinaciones (exclusivas, no durables…)  | Más restringidas y enfocadas a alta disponibilidad      |
| Uso típico           | Casos simples, no críticos                    | Producción crítica donde no quieres perder mensajes     |


## 3.5 Otras opciones y matices que conviene conocer

No son tipos totalmente distintos de cola, pero sí **modos** o **extensiones** importantes:

### 3.5.1 Lazy Queues

- Modo de funcionamiento (especialmente para classic, pero también hay equivalentes para quorum).
- Optimizadas para **colas largas**: escriben antes a disco y mantienen menos mensajes en RAM.
- Útiles cuando sabes que habrá backlog grande y prefieres sacrificar algo de latencia por estabilidad.

### 3.5.2 Single Active Consumer (SAC)

- Garantiza que solo **un consumidor** esté activo a la vez aunque haya varios conectados.
- El resto quedan en “standby”.
- Útil cuando necesitas procesar en **estricto orden** o evitar paralelismo en procesos sensibles, pero quieres seguir teniendo alta disponibilidad de consumidores.

### 3.5.3 Super Streams

- Permiten particionar un stream en varias particiones para escalar horizontalmente.
- Concepto similar a las particiones de Kafka:
  - Más particiones → más throughput.
  - Necesitas pensar en la clave de particionado para mantener orden donde sea necesario.

### 3.5.4 Classic Mirrored Queues (deprecadas)

- Modelo antiguo de alta disponibilidad para classic queues.
- Requieren configuración y mantenimiento más complejos, con comportamientos sutiles en fallos.
- Hoy en día se consideran un patrón legacy: si te los encuentras, la estrategia recomendada es migrar a **quorum queues**.
    
