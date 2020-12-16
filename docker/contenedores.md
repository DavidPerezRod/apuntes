# Contenedores

Para crear un contenedor docker, se utiliza el comando create:

```sh
docker create IMAGE_REPOSITORY
```

Para conocer los contendores existentes, estén o no en ejecución, se utiliza:

```sh
docker ps -a
```
Entre la información que devuelve, está IMAGE, que indica a partir de qué imagen se ha creado el contendor, y está el comando con el que se ejecuta. Que en nuestro caso, será el que le hemos indicado en los metadatos de Dockerfile.

El nombre de la imagen, si no se indica nada, será aleatorio.

Para crear un contenedor con un nombre concreto, utilizaremos:

```sh
docker create --name NOMBRE IMAGE_REPOSITORY
```

Para la eliminación de contendores, se puede identificar el contendor a borrar, bien por su identificador, bien por su nombre.

```sh
docker rm NOMBRE_CONTENEDOR

docker rm ID_CONTENDOR
```

Para arrancar el contenedor:

```sh
docker start CONTAINER_ID

docker start NOMBRE_ID
```

Para conocer los contenedores en ejecución:

```sh
docker ps
```

Un contenedor en ejecución no puede ser borrado, antes debemos detenerlo:

```sh
docker stop CONTAINER_ID
```

Esto manda un apagado ordenado del sistema kill -15. Un kill -9 haría un apagado instantáneo.

También existe la posibilidad de borrarlo cuando se encuentra en ejecución, con la opción -f.

```sh
docker rm ID_CONTENDOR -f
```

Al igual que con la imagen:

```sh
docker inspect NOMBRE_ID

docker inspect CONTAINER_ID 
```

Devuelve todos los metadatos del contenedor.

Por último, si quisiésemos crear y ejecutar un contendero al mismo tiempo, utilizaríamos el comando:

```sh
docker run -d SEGUIDO_DEL_RESTO_DE_PARÁMETROS_DE_CREACIÓN
```