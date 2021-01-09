# Docker compose

Se trata de una herramienta que permite ejecutar entornos multicontenedor, en concreto ejecutar contenedores con toda sus características, con un solo fichero de configuración yml.

Un ejemplo de configuración docker compose, podría ser la siguiente:

```yml
  1 version: '3'
  2 services:
  3     web: #nombre que se le da al servico
  4         image: nombre-imagen:etiqueta
  5         ports:
  6         - "8080:80" #mapeo de puerto host-contenedor
```

Donde se indica que se va a utilizar la versión 3 de Compose, que se va a crear un servicio, "web", basado en la imagene nginx:latest y que hay que redirigir el puerto 8080 del contenerdor al puerto 80 del host.

Para ejecutar un fichero yaml con una configuración docker, hay que ejecutar el comando:

```sh
docker-compose up
```

Con la configuración anterior, docker-compose, si no tenemos la imagen, se la bajará y ejecutará el contendor. El nombre que le dará será: directorio-ejecución_nombre-servicio_ordinal.

En el terminal desde el que se haya ejecutado el comando, se qudará con la salida estándar del contenedor. Desde esta consola si se ejecuta ctrl+c se para el contenedor pero no se borra. Si se quiere evitar este coportamiento, abrá que ejecutar docker-compose con la opción -d

Para borrar un contendor creado con docker compose, primero hay que pararlo, y luego eliminarlo:

```sh
docker-compose stop
docker-compose rm
```

Con la opción logs -f, se listan todos los logs de los contenedores.

Además de esta opción docker-compose permite trabajar ejecutar contenedores a partir de ficheros dockerfile.

Para ello en lugar de pasar como parámetro el nombre de una imagen, se pasa el de un subdirectorio:

```yml
  1 version: '3'
  2 services:
  3     web: #nombre que se le da al servico
  4         build: ./code #construye a partir de los dockerfile que encuentre en el dicrectorio
  5         ports:
  6         - "8080:80" #mapeo de puerto host-contenedor
```

Como mínimo, en el directorio que le indiquemos, tiene que haber un dockerfile. Para crear el contenedor, en este caso se utilizaría la opción build:

```sh
docker-compose build 
```
De esta forma crea la imagen a partir de los dockerfile, y depués se ejecuta con docker-compose up.

Por último, la imagen a la que se haga referencia en el yaml, no tiene por qué estar en el repositorio remoto, puede ser una imagen local.
