# Network

Es un subsistema de red que provee docker. Docker además de aportar este ámbito para el desarrollo de drivers de red que lo hagan compatible con determinados sistemas, ofrece una serie de drivers de red por defecto. 

Si se ejecuta el comando: 

```sh
docker network ls
```

muestra las redes creadas por defecto en docker.

Los principales drivers por defecto son:

* Bridge, es el driver de red por defecto que habilita la comunicación entre contenedores, y entre éstos y el host.

* Host, cualquier contendor del host va a poder obtener una ip de la red real en el host. Pero esto elimina el aislamiento de red entre los contenedores y el host anfitrión.

* Null, sirve para levantar un contenedor sin conexión a internet, que puede ser útil en algunos casos como por ejemplo un contenedor en el que se quieren ejecutar solamente scripts.

* Overlay, se utiliza en clustering. Para crear una red de contenedores en el host, con ips del mismo rango.

* Macvlan. Sirve para asignar levantar contendores con dirección mac únicas dentro de la red, y podrá acceder a todos los recursos de la red Host.

Para crear una red en docker se utiliza el comando:

```sh
docker network create web01 --driver bridge
```

Una vez creado, al igual que el resto de elementos docker, podemos conocer sus propiedades mediante el comando inspect:

```sh
docker network inspect web01
```

Una vez creada la red, es posible asignar los contenedores a éstas, en el momento de su creación:

```sh
docker create --name NOMBRE_IMAGEN --network NOMBRE_RED ID_IMAGEN
```

Una vez creado, y después de arrancarlo mediante start, si se ejecuta:

```sh
docker inspect NOMBRE_IMAGEN
```

Se puede comprobar que el contenedor pertenece a la red asignada. Si los contenedores se crean con redes diferentes, como estarán en distintos rangos de red, no se podrán comunicar entre ellos.

También se puede conectar un contenedor arrancado con una nueva red mediante:

```sh
docker network connect NOMBRE_RED NOMBRE_CONTENDOR
```

En este caso el contendor estaŕa dentro de dos redes. De la misma forma, existe el comando disconnect para sacar el contenedor de una red.

Con:

```sh
docker network prune
```

Se pueden borrar todas las redes creadas, menos las redes por defecto.