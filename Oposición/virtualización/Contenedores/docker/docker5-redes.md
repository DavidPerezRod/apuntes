# Docker — Redes (board)

Docker crea redes para conectar contenedores **sin que se tengan que tocar** rutas, NAT o reglas iptables a mano (aunque por debajo existan). Cada contenedor vive en su propio **network namespace** (interfaces, rutas, tabla ARP, etc.). Cuando lo conectas a una red Docker, Docker le añade una interfaz virtual y lo integra en el plano de red que corresponda.

Idea clave: **la conectividad (red)** es ortogonal a **la persistencia (volúmenes/binds/tmpfs)**. Un “data container” o un volumen pueden compartirse sin red; la red entra cuando dos procesos hablan por TCP/UDP.

## 1) Drivers / tipos de red en Docker (cuándo usar cada uno)

### `bridge` (por defecto en un host)
Es la red típica en Docker Engine local. Los contenedores se conectan a un bridge virtual (similar a un switch L2). Hay NAT hacia fuera.

- Uso típico: la mayoría de escenarios en un solo host.
- Recomendación práctica: usa **user-defined bridge** (tu propia red creada con `docker network create`) en vez del bridge por defecto, porque te da DNS interno y más control.

### `host`
El contenedor comparte la pila de red del host (no hay aislamiento de puertos).

- Pros: menos overhead, latencias menores, “lo que escucha el contenedor escucha el host”.
- Contras: pierde aislamiento; choques de puertos; menos portable.
- Uso típico: casos muy concretos (monitoring low-level, appliances, etc.).

### `none`
Sin red (solo loopback).

- Uso típico: jobs que no deben hablar con nada.

### `overlay` (multi-host)
Para Swarm (y tecnologías que implementen overlay). Permite que contenedores en hosts distintos se vean en una misma red lógica.

- Uso típico: clúster (Swarm); no es lo normal si estás solo con Docker Engine local.

### `macvlan` / `ipvlan`
El contenedor aparece como “otro equipo” en tu red física (puede tener su propia MAC/IP en la LAN).

- Pros: integración “real” en la LAN.
- Contras: complejidad (routing, switches, WiFi), seguridad y troubleshooting más exigentes.
- Uso típico: appliances, necesidades específicas de red corporativa.



## 2) Bridge “por defecto” vs user-defined bridge (por qué importa)

En redes `bridge` creadas por ti (user-defined):
- Docker ofrece **DNS interno** (puedes resolver por nombre de contenedor/servicio).
- El aislamiento es más limpio y el comportamiento es más predecible.
- Puedes conectar/desconectar contenedores a redes sin reiniciar (depende del caso).

En el bridge “default” (`docker0`), muchas cosas “funcionan”, pero el DNS/descubrimiento es menos cómodo y la configuración suele ser más limitada.



## 3) Descubrimiento de servicios (DNS interno)

En una red Docker user-defined, los contenedores se resuelven por nombre:
- En Compose, el nombre del servicio suele ser el hostname de facto dentro de la red.
- También puedes añadir aliases.

Esto elimina la necesidad de “hardcodear” IPs. La regla mental:
- **Dentro de la red Docker**: habla a `http://nombre-servicio:puerto`.
- **Desde el host o fuera**: necesitas publicación de puertos (`-p` / `ports`) o un proxy/ingress.



## 4) Publicación de puertos: “exponer” vs “publicar”

### Exponer (solo para comunicación interna)
- Dockerfile: `EXPOSE` (documenta intención, no publica al host).
- Compose: `expose` expone puertos **solo accesibles por otros servicios** en redes compartidas. :contentReference[oaicite:0]{index=0}

### Publicar (hacer accesible desde el host)
- `docker run -p hostPort:containerPort`
- Compose: `ports` publica puertos del contenedor al host. :contentReference[oaicite:1]{index=1}

Matiz importante: cuando publicas, puedes **restringir interfaz** en el host:
- Publicar en `127.0.0.1:...` lo hace accesible solo desde el propio host.
- Publicar en `0.0.0.0:...` (por defecto) lo expone en todas las interfaces.



## 5) Compose y redes: qué crea y cómo se nombra

Docker Compose define aplicaciones multi-contenedor y puede crear **redes y volúmenes** como parte del despliegue

Comportamiento por defecto (muy típico):
- Compose crea una red “default” por proyecto y conecta ahí los servicios (de hecho, verás mensajes tipo “Creating network … with the default driver”).
- El “nombre de proyecto” suele ser el nombre del directorio, salvo que lo cambies con `-p`. :contentReference[oaicite:4]{index=4}

Puedes definir redes explícitas a nivel raíz y asignar servicios a una o varias redes



## 6) Dependencias entre servicios: red ≠ readiness

- `depends_on` en Compose define orden de arranque, pero no garantiza que el servicio dependiente esté “listo” (solo que se ha iniciado). 
- La opción `links` existía para enlazar servicios con alias, pero se considera legacy; hoy se recomienda usar **redes** y DNS interno. 

Regla práctica:
- Para readiness real: healthchecks + lógica de reintento en el cliente (o un wait-for en entrypoint).



## 7) Comandos esenciales (operativa y troubleshooting)

### Inventario
- `docker network ls` → lista redes.
- `docker network inspect <red>` → detalles (subred, gateway, contenedores conectados).
- `docker inspect <contenedor>` → sección `.NetworkSettings.Networks` (IPs, redes).

### Conectar / desconectar
- `docker network connect <red> <contenedor>`
- `docker network disconnect <red> <contenedor>`

### Ver rutas y DNS desde dentro
- Ejecutar `ip a`, `ip route`, `cat /etc/resolv.conf`, `nslookup <servicio>` (si la imagen lo incluye) dentro del contenedor ayuda a aislar si el fallo es DNS, routing o puertos.



## 8) Errores típicos (y cómo pensarlos)

### “Desde un contenedor no llego a otro”
Causas típicas:
- No están en la misma red.
- Estás usando IPs en vez de nombres (y cambia).
- El servicio escucha en `127.0.0.1` dentro del contenedor (error clásico): en contenedor suele necesitar escuchar en `0.0.0.0`.

### “Desde el host no llego al contenedor”
Causas típicas:
- No has publicado el puerto (`-p` / `ports`).
- Has publicado en `127.0.0.1` y estás probando desde otra máquina.
- Firewall del host.

### “¿Por qué ‘localhost’ no funciona?”
`localhost` siempre significa “esta máquina”:
- Dentro de un contenedor, `localhost` es el contenedor, no el host.
- Para hablar con el host desde un contenedor:
  - En Docker Desktop suele existir `host.docker.internal`.
  - En Linux depende del setup (puede requerir gateway de la red o configuración adicional).



## 9) Buenas prácticas rápidas (que te evitan problemas)

- Usa **user-defined networks** para apps multi-contenedor (DNS y aislamiento).
- Separa redes por dominio de exposición:
  - `frontend` (lo que puede recibir tráfico externo) vs `backend` (solo interno).
- Publica puertos al host solo cuando sea necesario (principio de mínima exposición).
- No bases integraciones en IPs: usa nombres de servicio/contendor en la red Docker.
- No confundas “arrancado” (`depends_on`) con “listo”: añade healthchecks/retries.
