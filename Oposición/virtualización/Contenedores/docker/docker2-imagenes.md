# Docker - Imágenes

## Layers

Piensa en una imagen Docker como:

* un sistema de ficheros empaquetado (más unos metadatos de arranque). 
* un artefacto: algo que puedes guardar, versionar, mover y usar como base.

Lo habitual es empezar como mínimo por la imagen de un SO, y a partir de ahí se utilizan comando Docker que permiten cambiar esa imagen base para crear algo nuevo. Las instrucciones más habituales son:

| Instrucción    | Para qué sirve                                      | Impacto típico   | Ejemplo                                                                                                 | Qué hace el ejemplo                                                                       |         |                                                                                    |
| -------------- | --------------------------------------------------- | ---------------- | ------------------------------------------------------------------------------------------------------- | ----------------------------------------------------------------------------------------- | ------- | ---------------------------------------------------------------------------------- |
| `FROM`         | Define la imagen base (A→B)                         | Base de capas    | `FROM ubuntu:24.04`                                                                                     | Parte del filesystem y metadatos de Ubuntu 24.04; tu imagen añadirá capas encima.         |         |                                                                                    |
| `RUN`          | Ejecuta comandos en build                           | Cambia FS (capa) | `RUN apt-get update && apt-get install -y curl`                                                         | Instala `curl` dentro de la imagen (queda “horneado” como capa).                          |         |                                                                                    |
| `CMD`          | Comando por defecto al arrancar el contenedor       | Metadato         | `CMD ["./app"]`                                                                                         | Si no lo sobrescribes al ejecutar, el contenedor lanzará `./app`.                         |         |                                                                                    |
| `ENTRYPOINT`   | Ejecutable fijo del contenedor                      | Metadato         | `ENTRYPOINT ["bash","-lc"]`                                                                             | Fuerza a usar `bash -lc …` como ejecutor; `CMD` suele actuar como argumentos por defecto. |         |                                                                                    |
| `COPY`         | Copia archivos del contexto al FS de la imagen      | Cambia FS (capa) | `COPY target/app.jar /opt/app/app.jar`                                                                  | Copia el JAR compilado desde tu máquina al interior de la imagen.                         |         |                                                                                    |
| `ADD`          | Como `COPY` + extras                                | Cambia FS (capa) | `ADD app.tar.gz /opt/app/`                                                                              | Copia y **descomprime** el tarball en `/opt/app/`.                                        |         |                                                                                    |
| `WORKDIR`      | Directorio de trabajo para siguientes instrucciones | Metadato         | `WORKDIR /opt/app`                                                                                      | A partir de aquí, `RUN`, `CMD`, etc. se ejecutan “como si” estuvieras en `/opt/app`.      |         |                                                                                    |
| `ENV`          | Variables de entorno en imagen/runtime              | Metadato         | `ENV JAVA_OPTS="-Xms256m -Xmx512m"`                                                                     | Define una variable disponible en runtime para configurar la JVM (sin secretos).          |         |                                                                                    |
| `ARG`          | Variables solo de build                             | Metadato build   | `ARG VERSION=1.0.0`                                                                                     | Define un parámetro de build (solo existe durante `docker build`).                        |         |                                                                                    |
| `LABEL`        | Metadatos (trazabilidad/OCI)                        | Metadato         | `LABEL org.opencontainers.image.revision="abc123"`                                                      | Añade metadatos: útil para saber qué commit generó la imagen.                             |         |                                                                                    |
| `EXPOSE`       | Documenta puertos                                   | Metadato         | `EXPOSE 8080`                                                                                           | Indica que la app escucha en 8080 (no publica el puerto por sí solo).                     |         |                                                                                    |
| `USER`         | Usuario por defecto en runtime                      | Metadato         | `USER 10001`                                                                                            | El proceso del contenedor se ejecutará como UID 10001 (hardening).                        |         |                                                                                    |
| `VOLUME`       | Declara puntos de volumen                           | Metadato         | `VOLUME ["/data"]`                                                                                      | Declara que `/data` está pensado para persistencia externa (volumen).                     |         |                                                                                    |
| `HEALTHCHECK` | Chequeo de salud | Metadato | `HEALTHCHECK CMD curl -f http://localhost:8080/actuator/health || exit 1` | Docker marcará el contenedor como “healthy/unhealthy” según responda ese endpoint. |
| `SHELL`        | Cambia el shell usado por `RUN`                     | Metadato         | `SHELL ["/bin/bash","-lc"]`                                                                             | Hace que siguientes `RUN` usen bash (útil para `pipefail`, etc.).                         |         |                                                                                    |
| `STOPSIGNAL`   | Señal para detener contenedor                       | Metadato         | `STOPSIGNAL SIGTERM`                                                                                    | Define la señal que Docker enviará al detener el contenedor (shutdown más limpio).        |         |                                                                                    |
| `ONBUILD`      | Instrucciones diferidas al ser base de otra imagen  | Metadato         | `ONBUILD COPY . /src`                                                                                   | Cuando alguien haga `FROM esta-imagen`, se ejecutará ese `COPY` en su build.              |         |                                                                                    |
| `MAINTAINER`   | Autor (antiguo)                                     | Obsoleto         | `MAINTAINER "Nombre <mail>"`                                                                            | Está deprecado; se prefiere `LABEL`.                                                      |         |                                                                                    |
| `COPY --from=` | Copia desde otra etapa (multi-stage)                | Cambia FS (capa) | `COPY --from=builder /opt/app/app.jar /opt/app/app.jar`                                                 | Copia solo el artefacto final desde la etapa `builder` a la imagen runtime (más pequeña). |         |                                                                                    |


Cada instrucción que añade o cambia cosas va creando un nuevo delta de cambios. Ese delta es una capa.


## Ejemplo


```dockerfile
# ============================================================
# Dockerfile: "Imagen B" construida a partir de "Imagen A"
# ============================================================

# 1) PARTIR DE OTRA IMAGEN (A -> B)
# ------------------------------------------------------------
# FROM selecciona una imagen base ("A"). Tu imagen final ("B")
# NO copia todo el filesystem de A como un duplicado gigante:
# REUTILIZA las capas de A y solo añade las capas nuevas que
# generen las instrucciones de este Dockerfile.
FROM ubuntu:24.04

# (Dato mental útil)
# - "ubuntu:24.04" es un TAG (mutable). Para máxima reproducibilidad
#   podrías fijar por digest:
#   FROM ubuntu@sha256:...
# Eso garantiza el mismo contenido exacto.

# 2) METADATOS Y REPRODUCIBILIDAD: ENV / LABEL
# ------------------------------------------------------------
# ENV queda dentro de la imagen (es metadato y también se expone al runtime).
# No metas secretos aquí: se quedarán en la imagen.
ENV APP_HOME=/opt/app
LABEL org.opencontainers.image.title="demo-image-layers" \
      org.opencontainers.image.description="Ejemplo didáctico de capas y build" \
      org.opencontainers.image.source="local" \
      org.opencontainers.image.licenses="UNLICENSED"

# 3) CADA PASO QUE CAMBIA EL FS GENERA UNA CAPA (CONCEPTUALMENTE)
# ------------------------------------------------------------
# RUN ejecuta comandos DURANTE el build. Los cambios que produzca
# (paquetes instalados, ficheros creados, etc.) quedan "horneados"
# en la imagen como una nueva capa read-only.
RUN apt-get update \
 && apt-get install -y --no-install-recommends ca-certificates curl \
 && rm -rf /var/lib/apt/lists/*

# 4) COPY/ADD: meter tu app crea otra capa
# ------------------------------------------------------------
# COPY copia archivos desde el contexto de build (tu máquina) a la imagen.
# Esto también crea una capa (un delta).
WORKDIR ${APP_HOME}
COPY app/ ${APP_HOME}/

# 5) EJEMPLO DE ARCHIVO GENERADO EN BUILD (queda en imagen)
# ------------------------------------------------------------
# Lo que crees aquí queda dentro de la imagen, inmutable.
RUN echo "Build time: $(date -u +%Y-%m-%dT%H:%M:%SZ)" > ${APP_HOME}/BUILD_INFO.txt

# 6) USER: seguridad por defecto (recomendación)
# ------------------------------------------------------------
# Crear usuario en build y usarlo en runtime.
RUN useradd -r -u 10001 -m -d ${APP_HOME} appuser \
 && chown -R appuser:appuser ${APP_HOME}
USER appuser

# 7) EXPOSE: solo documentación (no abre puertos por sí mismo)
# ------------------------------------------------------------
EXPOSE 8080

# 8) ENTRYPOINT/CMD: qué se ejecuta cuando arranca el contenedor
# ------------------------------------------------------------
# Esto NO ejecuta nada en build. Solo define el comportamiento al "docker run".
# En runtime, el contenedor verá el filesystem de la imagen (RO) + su capa RW.
ENTRYPOINT ["bash", "-lc"]
CMD ["./start.sh"]

# ============================================================
# LECTURA CLAVE (runtime):
#
# - La imagen (todas estas capas) es read-only.
# - Cuando haces `docker run`, Docker crea un contenedor y le pone
#   una capa superior read-write (la "capa del contenedor").
# - Si la app escribe logs en ${APP_HOME}/logs, esos archivos van a
#   la capa RW del contenedor (y se pierden al borrar el contenedor),
#   salvo que uses un VOLUMEN.
# ============================================================
```

## UnionFS / OverlayFS

¿Cómo puede Docker coger capas y hacer que se vea un único árbol como si fuera un filesystem normal?

La idea general se llama union filesystem: una técnica para presentar varios directorios/capas como si fueran un único filesystem. No es que existan varios “/” distintos; existen varios “trozos” de filesystem, y el union filesystem proporciona una vista unificada.

En Linux moderno, Docker suele usar OverlayFS (driver overlay2) para implementar esa unión. OverlayFS trabaja con cuatro conceptos:

**1. lowerdir**: una o varias capas read-only (las de la imagen).
**2. upperdir**: una capa read-write (la del contenedor en runtime, o la capa que se está materializando durante ciertos pasos).
**2. workdir**: un directorio auxiliar interno que necesita OverlayFS para operar.
**3. merged**: el punto de montaje donde aparece la vista final, lo que se percibs como “el filesystem del contenedor”.

Así que:

* Durante el build, las instrucciones como RUN o COPY generan un delta que pasa a ser una capa más de la imagen (read-only). Esas capas quedan guardadas, identificadas, y pueden compartirse entre imágenes.

* En runtime (docker run), Docker monta todas las capas read-only de la imagen como lowerdir (en realidad puede ser una composición de muchos lowers) y añade encima un upperdir específico para ese contenedor. El resultado “visible” es merged, que tiene apariencia de un filesystem normal.

Lo importante: cuando un proceso dentro del contenedor escribe:

* no escribe en la imagen (que es read-only). 
* escribe en upperdir. Y cuando intenta modificar algo de lowerdir, OverlayFS copia el fichero a upperdir y es esa copia la que modifica (copy-on-write CoW o copy-up). 
* Por eso la imagen se mantiene intacta y el contenedor puede comportarse como si tuviera un disco normal.
* Cuando se borra un fichero no se borra lowerdir; OverlayFS crea una marca en upperdir (un whiteout) que sirve para indicar que en la vista unificada, el fichero se considera eliminado/oculto”, y se mantiene la ilusión consistente del filesystem final.

**_Esta tecnología explica por qué los contenedores pueden iniciarse tan rápido: toda la información inicial ya está almacenada y la imagen es accesible por el contenedor, solo se tiene que crar la capa upperdir_**

## Formas de crear imágenes (4 enfoques)

En la práctica, Docker te permite llegar a una imagen por varias vías. Las más habituales (y que suelen listarse como “4 formas”) son:

1) **Desde un Dockerfile (`docker build`)**  
Es la forma “canónica”: declarativa, reproducible y versionable. El resultado es una imagen construida a partir de una imagen base (`FROM`) + una secuencia de cambios (capas).

2) **Desde un contenedor existente (`docker commit`)**  
Tomas un contenedor que ya tiene cambios (instalaciones, ficheros creados, etc.) y “congelas” su estado como imagen. Útil para prototipos o forense, pero mala práctica para trabajo serio: es poco reproducible y suele generar imágenes “opacas”.

3) **Importando un filesystem/archivo raíz (`docker import`)**  
Creas una imagen a partir de un tarball con un root filesystem (por ejemplo una raíz de Linux que ya tienes). Es una forma “bruta”: puede servir para casos especiales, pero pierdes el valor de un Dockerfile como receta.

4) **Vía registro/CI como artefacto (`pull`/`build` en pipeline, BuildKit, etc.)**  
Aquí “crear” se entiende como *generar la imagen como artefacto de una cadena de build reproducible* (CI), y publicarla en un registro. En oposición suele aparecer como “generación desde código fuente en un entorno consistente”: la idea es que el build no dependa de tu máquina, sino de un entorno controlado (imagen builder, pipeline, buildpack, etc.), de forma que el resultado sea repetible.

> Para estudiar y para producción, quédate con: **Dockerfile + CI + registro**. `commit/import` existen, pero no son el camino principal.

---

## Caché de Docker y por qué el orden de instrucciones importa

Docker (y especialmente BuildKit) intenta **reutilizar resultados** de pasos anteriores cuando el contexto no cambia. A nivel mental:

- Cada instrucción “aprovechable” puede reutilizarse si Docker considera que **sus inputs** no han cambiado.
- Si una instrucción no puede reutilizarse, se rehace y **todo lo que venga después** suele rehacerse también (porque depende de ese resultado).

Esto es la base del consejo clásico:

> Ordena el Dockerfile de **lo que cambia menos** a **lo que cambia más**.

### Qué “rompe” la caché (intuitivamente)
- Cambios en ficheros que copias (`COPY`, `ADD`).
- Cambios en argumentos/variables de build (`ARG`) usados por una instrucción.
- Cambios en el propio comando (`RUN ...`).
- Cambios en la imagen base (tag mutable como `latest`).

---

## Optimización de creación: capas “más volátiles arriba” (y por qué aparecen dangling images)

### La idea
Si colocas arriba (tarde) lo que cambia a menudo, Docker puede:
- reutilizar capas anteriores (costosas)
- y solo reconstruir el tramo final

Si lo haces al revés (metes al principio lo que cambia cada 5 minutos), fuerzas reconstrucciones completas.

### Dangling images (qué son realmente)
En builds frecuentes, puedes generar imágenes intermedias o antiguas que ya no tienen etiqueta apuntándoles. Son “restos” del proceso (especialmente si cambias tags o haces builds repetidas). No son necesariamente un problema, pero consumen disco.

- Limpieza típica: `docker image prune` (y variantes).

---

## Reglas prácticas de optimización (las que has listado, con sentido)

### 1) Ordenar pasos de mayor a menor frecuencia de cambio (volátiles al final)
Ejemplo mental: si tu código cambia más que tus dependencias:
- primero instala dependencias
- después copia el código

Esto puede marcar la diferencia entre “reconstruyo 5 segundos” vs “reconstruyo 5 minutos”.

### 2) Ser específico en las instrucciones (“grano fino”)
Evita `COPY . .` al principio si eso incluye cosas que cambian sin necesidad.
Mejor:
- copiar primero ficheros estables (manifest de dependencias)
- ejecutar instalación
- copiar el resto del código

### 3) Identificar instrucciones cacheables
En la práctica, suelen ser cacheables (si sus inputs no cambian):
- instalación de dependencias basada en ficheros de lock (`package-lock.json`, `pom.xml`, `poetry.lock`, etc.)
- pasos de compilación si el árbol de fuentes no cambió
- `RUN apt-get install ...` si no cambió el comando ni el estado previo

### 4) Eliminar dependencias innecesarias
Menos dependencias:
- menos tiempo de build
- menos superficie de ataque
- imagen más pequeña

### 5) Eliminar caché del gestor de paquetes (en la misma capa)
En Debian/Ubuntu:
- `rm -rf /var/lib/apt/lists/*`  
Porque si no lo borras en la misma instrucción `RUN`, el “peso” queda guardado en la capa.

### 6) Usar imágenes oficiales (cuando aplique)
Suelen tener:
- mantenimiento razonable
- parches de seguridad
- prácticas estándar

No significa “siempre”, pero como norma general para estudiar y arrancar, sí.

### 7) Usar etiquetas específicas (evitar `latest`)
`latest` puede cambiar sin avisar → builds no reproducibles y caché impredecible.

Preferible:
- `ubuntu:24.04`
- y para máxima reproducibilidad: `ubuntu@sha256:...`

### 8) Preferir versiones mínimas (con criterio)
Imágenes más pequeñas tienden a:
- bajar más rápido
- reducir superficie de ataque

Pero “mínima” no siempre es “mejor” si pierdes herramientas necesarias (debug) o compatibilidad (por ejemplo, `alpine` y libc/musl).

### 9) “Generación desde código fuente en un entorno consistente” (qué significa)
La idea no es “compilar en tu portátil y copiar el binario”, sino:
- compilar **dentro de un entorno controlado**
- que siempre tenga las mismas versiones de toolchain, libs, etc.

Eso reduce el típico “en mi máquina funciona”.

En Docker esto suele materializarse como:
- una etapa `builder` con JDK/Node/Go, etc.
- que compila el artefacto de forma reproducible
- y luego copias el resultado a una imagen runtime limpia

### 10) Obtener dependencias en un paso separado
Patrón típico:
- copiar primero ficheros de dependencias
- instalar dependencias
- copiar el resto

Así, si solo cambias código, no reinstalas dependencias.

### 11) Eliminar dependencias de compilación con multi-stage
No quieres meter toolchain en runtime.
Multi-stage te deja:
- builder “pesado”
- runtime “ligero”

---

## Multi-stage builds (por qué y cómo encaja)

**Objetivo:** separar:
- entorno de compilación (pesado, con toolchain)
- entorno de ejecución (mínimo, solo runtime)

Patrón conceptual:

1) `FROM ... as builder`  
Compilas dentro. Aquí van `RUN apt-get install build-essential`, `mvn package`, etc.

2) `FROM ...` (runtime)  
Copias solo el artefacto final: binario/JAR/dist, y nada más.

Ventajas:
- imagen final más pequeña
- menos vulnerabilidades (sin compiladores, sin headers, etc.)
- builds más “limpios” y repetibles

---

## ONBUILD (qué es y cuándo aparece)

`ONBUILD` define “triggers” que se ejecutan **cuando otra imagen use esta como base**.

Se usa para crear imágenes “plantilla” (por ejemplo, una base para apps que siempre hacen `COPY` y `RUN build`).

Trade-off clave:
- puede sorprender: el Dockerfile del consumidor dispara acciones “ocultas”
- hoy se usa menos porque multi-stage y plantillas de CI suelen ser más explícitas

---

## Etiquetado y nombres de imágenes (lo mínimo sólido)

Una referencia típica:

- `registry/namespace/repo:tag`
- o `registry/namespace/repo@sha256:digest`

Ejemplos:
- `docker.io/library/ubuntu:24.04` (implícito `docker.io`)
- `ghcr.io/mi-org/miapp:1.2.3`

Puntos clave:
- **Tag** es un “alias” mutable.
- **Digest** identifica contenido exacto (inmutable).

Buenas prácticas de tag:
- versiones semánticas (`1.2.3`)
- tags de canal (`1.2`, `1`, `stable`) si sabes lo que haces
- evita `latest` en despliegues

---

## Registros Docker (¿merecen la pena?)

Sí, en cuanto pasas de “solo local” a cualquier cosa seria (equipo, CI/CD, despliegue).

Un **registro** te da:
- distribución de imágenes (pull en otros hosts/clusters)
- versionado de artefactos
- control de acceso
- integración con CI (publicar artefactos por commit/tag)
- escaneo de vulnerabilidades (según proveedor)

Opciones típicas:
- públicos: Docker Hub, GHCR
- cloud: ECR (AWS), Artifact Registry (GCP), ACR (Azure)
- self-hosted: registry OCI propio (cuando necesitas control total)

Criterio simple:
- aprendizaje local: opcional
- trabajo en equipo / Kubernetes / CI: prácticamente imprescindible

---

## Comandos mínimos de apoyo (sin entrar en “recetas”)

- Ver cómo se aprovecha caché: `docker build ...` (observa qué pasos “Using cache”)
- Ver capas generadas: `docker history <imagen>`
- Ver tamaño y detalle: `docker image inspect <imagen>`
- Limpieza de restos: `docker image prune` (con cuidado)