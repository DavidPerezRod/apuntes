# Creación de una imagen Docker

Los pasos que se deben seguir si se quiere crear una imagen docker son:

1. crear un fichero Dockerfile
2. en su interior instalar un sistma operativo. Por ejemplo:

```dockerfile
#creamos una imagen a partir de esta versión de ubuntu.
#la palabra from, es una palabra reservada, que indica que nos bajaremos una imagen oficial del dockerhub
FROM ubuntu:18.04 

#actualizamos apt e instlamos nginx. Un servidor web/proxy inverso ligero de alto rendimiento, y un proxy para servidores de correo
RUN apt-get update
RUN apt-get install nginx -y

#creamos la página de incio que serviremos cuando accedamos al servidor de correo
RUN echo 'hello world' > /var/www/html/index.html

#CMD es otra palabra reservada con la que indicamos con qué comando van a correr los contenedores que utilicen esta imagen.
CMD nginx -g 'daemon off;'
```

Después para crear la imagen a partir de este contenido hay que ejecutar:

```sh
docker build rutafichero
```

Cada imagen creada va a tener unos metadatos, como fecha de creación y comando para correr el contenedor.

Para saber las imágenes que tenemos, se ejecuta el comando:

```sh
docker images
```

Después de ejecutar el fichero Dockerfile anterior tendremos dos imágenes, la de ubuntu que hemos descargado de dockerhub, y la que hemos creado nosotros con la de ubuntu y nginx.

En el fichero de creación no hemos indicado ni su nombre ni su tag, de forma que al listar las imágenes estos dos elementos aparecerán como **_none_**

Para dar nombre y etiqueta a la imagen, al crearla se debe utilizar el parámetro -t, seguido del nombre y etiqueta

```sh
docker build -t nombre:tag rutafichero
```

para el **_borrado_** de imágenes se ejecuta el comando:

```sh
#IMAGE_ID será el identificador de la imagen en el sistema
docker image rm IMAGE_ID
```

Cuando se borra una imagen, el sistema informa de todas las capas que se eliminan como consecuencia de su borrado. Es recomendable marcar como  **_latest_** para que no sea necesario conocer el listado de todas las imágenes del sistema. Para conseguirlo se utilizaría el comando tag, que crea un nuevo tag sobre la misma imagen, de forma que en el listado aparecerá dos veces, una con el tag de versión, y otra como latest.

```sh
docker tag nombre:tag nombre:latest
```

Para conocer los metadatos de la imagen, se utiliza el comando inspect:

```sh
docker inspect IMAGE_ID
```

que devuelve un JSON con toda la información de los metadatos.

