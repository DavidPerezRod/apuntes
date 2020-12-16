# Persistencia con volúmenes

En ocasiones, será necesario acceder a los procesos que corren en el contendor. Para poder hacerlo, Docker proporciona el comando exec:

```sh
docker exec CONTENEDOR_ID ls
```

Aunque probablemnte su uso más habitual, o más práctico, sea para ejecutar bash en el contenedor, desde el host. Para ello se ejecutará:

```sh
docker exec -it CONTENEDOR_ID bash
```

Si una vez dentro, se ejecuta:

```sh
tail -f /var/log/nginx/access.log
```

 Se podrá ver toda la actividad que se hace sobre el contenedor. Sin embargo hay que tener en cuenta que **cualquier cambio que se haga en el sistema del contenedor no se va a persistir en la imagen a partir de las cuales fue crado, de forma que si se borra y se vuele a crear, los cambios se perderán.** Se trata pues de infraestructura inmutable.

Pero sin embargo para algún tipo de infrastructura es necesario la existencia de contenedores persistentes. El comando:

```sh
docker volume
```

permite gestionar los volúmenes docker. En cada contenedor creado, se tiene asociado un volumen en el host que lo hospeda, donde se almacenará la información que se cree en el contenedor. En concreto, docker crea una carpeta en **/var/lib/docker/volumes** por cada contenedor en el host. Para poder listar los volúmenes existentes se ejecutará:

```sh
docker volume ls
```

Y para crear volúmenes nuevos:

```sh
docker volume create nombre_volumen
```

Para conocer los metadatos del volumen, se procede de la misma forma que para las imágenes y los contenedores:

```sh
docker volume inspect NOMBRE_VOLUMEN
```

Exiten tres tipos de almacenamiento en docker:

![tipos de volúmenes docker](./imagenes/types-of-mounts-volume.png)

* **_volumes_**: los datos se almacenan dentro del sistema de ficheros del host. Es el mecanismo preferido para persistir los datos, y  **solo Docker tiene permisos sobre esta ubicación**. Un volumen puede ser montado por diferentes contenedores a la vez.
* **_bind mounts_**: Se utiliza para mapear cualquier sitio del sistema de ficheros dentro de tu contenedor. A diferencia de los volúmenes, a través de este mecanismo es posible acceder a la ruta mapeada y modificar los ficheros.
* **_tmpfs_**: Se trata de un almacenamiento temporal en memoria. Se suele utilizar para el almacenamiento de configuraciones y espacios efímeros que desparecerán cada vez que el contenedor se pare.