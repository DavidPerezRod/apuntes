# Docker — Almacenamiento y capa de escritura (board)

## 1) Capa de escritura del contenedor: por qué “no” almacenar ahí

Docker ejecuta contenedores sobre un filesystem por capas (típicamente `overlay2`): capas **solo-lectura** que provienen de la imagen + **una capa writable** específica del contenedor.

Esa capa writable tiene tres consecuencias prácticas:

1. **Volatilidad**: si eliminas el contenedor, su capa writable desaparece.
2. **Rendimiento**: el copy-on-write y las particularidades del union filesystem suelen penalizar I/O frente a escribir “fuera” de la RW layer.
3. **Operativa**: copias de seguridad, migraciones y gestión del estado se vuelven más frágiles si dependes de esa capa.

Por tanto, cualquier **estado** (BBDD, uploads, artefactos de negocio, colas persistentes, caches que deban sobrevivir, etc.) debe ir a mecanismos de persistencia: **volúmenes** o **bind mounts** (o `tmpfs` si es efímero y quieres RAM).


## 2) Tipos de almacenamiento en Docker

Docker ofrece tres formas principales de montar datos en contenedores:

### A) Volúmenes

Son gestionados por Docker y viven fuera del ciclo de vida del contenedor. En Linux suelen residir bajo la carpeta de datos de Docker (por ejemplo, dentro de `/var/lib/docker/volumes/...`).

Características clave:

- Son **independientes del contenedor** (el contenedor puede morir y el volumen sigue).
- Son más **portables** que un bind mount porque no dependen de rutas concretas del host.
- Se integran bien con **drivers** (NFS, plugins, etc.) si se necesita almacenamiento remoto.
- Se gestionan con comandos Docker (`ls`, `inspect`, `prune`).

#### Ejemplos

```bash
docker volume create mydata                    # crea un volumen mydata
docker volume ls                               # muestra los volúmenes existentes
docker volume inspect mydata                   # muestra los metadatos del volumen mydata
```

```json
# salida de docker volume inspect mydata
[
    {
        "CreatedAt": "2026-02-10T21:59:51+01:00",
        "Driver": "local",
        "Labels": null,
        "Mountpoint": "/var/lib/docker/volumes/mydata/_data",
        "Name": "mydata",
        "Options": null,
        "Scope": "local"
    }
]

```

```bash
# asocia el volumen a postgresql
docker run -d --name pg \
  -e POSTGRES_PASSWORD=secret \
  -v mydata:/var/lib/postgresql/data \
  postgres:16


# ejemplo de creación y asociación de oto volumen
docker volume create mysql-data

docker run -d --name mysql \
  -e MYSQL_ROOT_PASSWORD=secret \
  -v mysql-data:/var/lib/mysql \
  mysql:8

# ejemplo de creación de volumen anónio (sin nombre)
docker run -d --name demo -v /data alpine sleep 999999
docker inspect demo --format '{{json .Mounts}}'
```

### B) Bind mounts

Montan una ruta del host directamente dentro del contenedor.

Características clave:

- Son perfectos para **desarrollo** (edición directa de archivos, hot reload, etc.).
- Tienen **dependencia fuerte del host** (rutas, permisos, diferencias Windows/macOS/Linux).
- En entornos con Docker Desktop (macOS/Windows) pueden penalizar rendimiento en algunos casos.

#### Ejemplo

```bash
docker run --rm -it \
  -v "$PWD/app:/app" \
  -w /app \
  node:20 bash
```

A diferencia de los volúmenes docker no puede ni listar ni inspeccionar volúmenes de tipo bind. Se puede hacer un ls en el host, o inspeccionar desde el contenedor:

```bash
docker inspect demo --format '{{json .Mounts}}'
```

1.  `docker run` Arranca un contenedor nuevo.

2. `--rm` Cuando el comando termine, borra el contenedor automáticamente (no deja residuos).

3. `-it`
- `-i` mantiene STDIN abierto.
- `-t` crea una pseudo-TTY.

4. `-v "$PWD:/mnt"`
Esto es lo importante: **bind mount**.

- `$PWD` = tu directorio actual en el host (ej. `/home/david/proyecto`).
- `:/mnt` = dónde lo verás dentro del contenedor.

O sea: “monta mi carpeta actual del host en `/mnt` dentro del contenedor”.

5. `alpine` La imagen. Alpine es un Linux mínimo con utilidades básicas (como `ls`).

6. `sh -c "ls -la /mnt"`
Lo que ejecuta el contenedor.

- `sh -c "..."` permite pasar un comando como string.
- `ls -la /mnt` lista el contenido del directorio montado.



### C) tmpfs mounts

Montan un filesystem en memoria (RAM). Se pierde al parar el contenedor.

Características clave:

- **No persistente**.
- Muy útil si necesitas **velocidad** o reducir escrituras a disco.
- Adecuado para temporales, caches efímeras, datos intermedios, etc.

Otros mecanismos (pipes, etc.) existen pero son casos nicho y no suelen entrar en lo “core” de Docker storage.

#### ejemplo

 ```bash
 docker run --rm -it \
  --tmpfs /tmp:rw,size=64m \
  alpine sh
```

## 3) Volúmenes: variantes y características útiles

### Volúmenes “nombrados” vs “anónimos”

- **Nombrado**: lo crea/gestiona el usuario y se reutiliza explícitamente.
- **Anónimo**: Docker lo crea con un identificador aleatorio. Es útil para pruebas rápidas, pero tiende a dejar residuos si no se limpia.

### Montaje read-only

Tanto volúmenes como bind mounts pueden montarse en modo solo lectura para reducir riesgos (p. ej., inspeccionar datos sin modificarlos).

### Drivers de volumen (concepto)

El driver por defecto es `local`. Para infra más avanzada puedes usar drivers/plugins, normalmente para almacenamiento remoto. 



## 4) Contenedor de datos (data container): patrón clásico y alternativa moderna

Históricamente se usaba un “data-only container” para “poseer” datos y compartirlos con otros contenedores. Hoy, el enfoque recomendado suele ser usar volúmenes nombrados directamente, pero el patrón sigue siendo útil para entender una idea operativa: separar el contenedor “de aplicación” del mecanismo que mantiene el estado.

Puntos clave:

- El “contenedor de datos” no existe por sí mismo como una entidad mágica: es un contenedor (normalmente simple) cuyo propósito es **mantener y exponer montajes**.
- Permite **compartir** datos entre contenedores sin acoplarte al filesystem interno de cada contenedor.
- En la práctica moderna, se sustituye casi siempre por:
  - volúmenes nombrados gestionados por Docker, o
  - bind mounts (en desarrollo).
- Aun así, la idea “contenedor utilitario” sigue siendo relevante: usar un contenedor temporal para tareas de **seed**, **backup** o **restore** sobre datos montados.

## Ejemplos

* Montaje  clásico

```bash
docker create --name data-box -v /var/lib/myapp alpine

docker run --rm --volumes-from data-box \
  alpine ls -la /var/lib/myapp
```

## 5) Importación y exportación: dos conceptos distintos (y la confusión típica)

Cuando se habla de “importar/exportar” en Docker hay que separar claramente dos capas:

1. **Importar/exportar contenedores o imágenes** (filesystem / capas de imagen).
2. **Importar/exportar datos persistentes** (volúmenes o rutas montadas).

La confusión habitual es asumir que al exportar un contenedor “viajan” sus datos persistentes. No es así si esos datos están fuera del filesystem del contenedor (por ejemplo, en volúmenes).

Diferencias conceptuales:

- Exportar/importar *filesystem del contenedor* sirve para capturar el estado del contenedor como sistema de ficheros (su capa writable + lo que aporta la imagen).
- Guardar/cargar *imágenes* sirve para transportar la imagen (capas, metadatos), típicamente para moverla entre hosts o registries sin reconstruir.
- Ninguna de esas operaciones es, por defecto, un “backup de datos persistentes” si los datos viven en volúmenes/bind mounts.

Regla mental: si los datos están montados desde fuera del contenedor, exportar/importar contenedor/imagen **no es el mecanismo correcto** para mover esos datos.

### Ejemplos

* Exportar volumen
```bash
docker export mycontainer > mycontainer.tar
```

* Importar volumen
```bash
docker import mycontainer.tar myimage:from-export
```


## 6) Backup/restore real de datos: exportar/importar un volumen

Si lo que quieres es preservar el estado de una aplicación (por ejemplo una BBDD), lo relevante es el **contenido del volumen** (o del directorio del host si usas bind mount), no el filesystem del contenedor.

Concepto operativo:

- Para hacer backup de un volumen, se monta ese volumen en un contenedor “utilitario” y se empaqueta su contenido (por ejemplo, a un archivo tar comprimido) hacia una ruta del host u otro almacenamiento.
- Para restaurar, se invierte el proceso: se monta el volumen destino y se vuelca dentro el contenido empaquetado.
- Si el volumen ya contiene datos, hay que decidir estrategia:
  - restauración “encima” (merge) o
  - restauración “limpia” (borrar antes y restaurar).

Si en vez de volumen usas bind mount, el backup/restore se vuelve trivial desde el host porque los datos ya están en el filesystem del host: el mecanismo sería el de backup de cualquier directorio (tar, rsync, etc.).

Idea clave: **backup de datos** se hace sobre el **almacenamiento persistente** (volumen o path del host), no sobre la capa writable del contenedor.

### Ejemplo de exportación

```bash
docker run --rm \
  -v myapp-data:/data:ro \
  -v "$PWD/backup:/backup" \
  alpine sh -c 'cd /data && tar -czf /backup/myapp-data.tgz .'
```
### Ejemplo de importación

```bash
docker run --rm \
  -v myapp-data:/data \
  -v "$PWD/backup:/backup" \
  alpine sh -c 'cd /data && tar -xzf /backup/myapp-data.tgz'
```

Qué cambia si el montaje es un bind:

```bash
tar -czf mydir.tgz -C /ruta/en/host .

tar -xzf mydir.tgz -C /ruta/en/host
```
que los datos están en el propio host.