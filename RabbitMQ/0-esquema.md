# Esquema rápido (secciones 1 y 2)

Resumen muy condensado de los puntos 1 y 2 para consulta rápida.

## 1. Introducción a AMQP/RabbitMQ

- Objetivo: mensajería fiable y flexible entre servicios (colas, pub/sub, routing).
- AMQP: protocolo estándar; interoperable; soporta TLS, acks y patrones variados.
- Piezas:
  - Publisher → Exchange → Queue → Consumer (routing key y bindings intermedios).
  - Exchanges: distribuyen según routing key y bindings; colas almacenan; consumidores leen y ack.
- Por qué RabbitMQ: implementación popular de AMQP 0.9.1, simple de operar y con patrones variados.

## 2. Conceptos básicos (durabilidad y colas)

- Durabilidad real requiere **dos cosas**: cola `durable=true` + mensaje `delivery_mode=2` (persistent).
  - Si falta una: la cola sobrevive pero los mensajes no, o la cola desaparece llevándose los mensajes.
  - Valores por defecto: `durable=false`, `delivery_mode=1` (transient).
- Cola exclusiva (`exclusive=true`):
  - Solo la conexión que la creó puede consumir/controlarla.
  - Se elimina al cerrarse esa conexión, incluso si `autoDelete=false` (la exclusividad manda).
  - Otros productores pueden publicar hacia ella si el routing lo permite.
- TTLs y límites:
  - TTL de mensaje (`x-message-ttl` o `expiration`); TTL de cola (`x-expires`).
  - Límites de longitud (`x-max-length`, `x-max-length-bytes`) evitan backlogs infinitos.
- Patrón RPC típico:
  - Cliente crea cola exclusiva+auto-delete para respuestas (`reply_to`), envía petición con `correlation_id`.
  - Servidor lee de la cola pública y publica la respuesta en la cola exclusiva del cliente.
