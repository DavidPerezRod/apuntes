# Comandos docker

## Run

Este comando se utiliza para crear y ejecutar un nuevo contenedor a partir de una imagen. Cuando se ejecuta docker run, Docker realiza las siguientes acciones:

* Verifica si la imagen especificada existe localmente en el sistema
* Descarga la imagen desde un registro si no está presente en el sistema.
* Crea un nuevo contenedor a partir de la imagen.
* Inicia el contenedor y lo pone en ejecución.

El comando docker run se usa generalmente para crear y ejecutar un contenedor desde cero. Puede incluir opciones y configuraciones adicionales para personalizar el contenedor, como la exposición de puertos, la asignación de volúmenes, la configuración de variables de entorno, entre otros.

Su sintaxis básica es:

``` css
docker run [opciones] nombre_imagen [comando]
```

**opciones**: Son configuraciones adicionales que se pueden especificar al ejecutar el contenedor. Algunas opciones comunes son:

* -d o --detach: Ejecuta el contenedor en segundo plano (modo detached).
* -p o --publish: Expone los puertos del contenedor al host.
* -v o --volume: Monta volúmenes o directorios del host en el contenedor.
* --name: Asigna un nombre al contenedor en lugar de uno generado automáticamente.
* -e o --env: Establece variables de entorno dentro del contenedor.
* -it: Permite la interacción en tiempo real con el contenedor (modo interactivo y asignación de la terminal).
* --network: Especifica la red a la que debe conectarse el contenedor. Esta opción permite conectar el contenedor a una red específica y permitir la comunicación con otros contenedores o servicios.
* --restart: Configura el reinicio automático del contenedor en caso de fallos o reinicios del sistema. Puedes establecer diferentes políticas de reinicio, como "always" (siempre reiniciar) o "on-failure" (solo reiniciar en caso de fallo).
  
**nombre_imagen**: Especifica el nombre de la imagen a partir de la cual se creará el contenedor.

**comando**: Es opcional y se utiliza para ejecutar un comando específico dentro del contenedor en lugar del comando predeterminado definido en la imagen.

Algunos ejemplos de uso del comando docker run son:

Ejecutar un contenedor en segundo plano (detached) a partir de una imagen:

```css
docker run -d nombre_imagen
```

Asignar un nombre al contenedor:

```css
docker run --name nombre_contenedor nombre_imagen
```

Exponer puertos del contenedor al host:

```css
docker run -p puerto_host:puerto_contenedor nombre_imagen
```

Montar un volumen del host en el contenedor:

```css
docker run -v ruta_host:ruta_contenedor nombre_imagen
```

Establecer variables de entorno dentro del contenedor:

```css
docker run -e VARIABLE=valor nombre_imagen
```

Establecer una política de reinicio

```css
docker run --restart=always nombre_imagen.
```

## Start

Este comando se utiliza para iniciar un contenedor que ya ha sido creado y se encuentra detenido. El contenedor debe haber sido creado previamente. Cuando se ejecuta docker start, Docker realiza las siguientes acciones:

1. Verificación del estado: Docker verifica si el contenedor se encuentra en el estado detenido y listo para ser iniciado.

2. Inicio del contenedor: Si el contenedor se encuentra en el estado detenido, Docker lo inicia y reanuda su ejecución. La aplicación dentro del contenedor continúa desde el punto en que se detuvo.

2. Asociación de la terminal (si se utiliza la opción -a): Si se especifica la opción -a, Docker asocia la entrada estándar, salida y error del contenedor con la terminal actual.

Su sintaxis básica es:

```css
docker start [opciones] nombre_contenedor

docker start -a mi_contenedor
```

Esto iniciará el contenedor "mi_contenedor" y mostrará la salida directamente la terminal. Se podrá interactuar con la aplicación que se está ejecutando dentro del contenedor y ver la salida en tiempo real.

Aunque si el contenedor no está configurado para recibir interacciones desde la terminal o no está ejecutando una aplicación que produzca salida interactiva, la opción -a puede no tener un efecto significativo.
