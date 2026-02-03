# Introducción

Hay fundamentalmente cuatro tipos de BBDD NoSQL:

* clave-valor (Redis, Voldemort y Dynamo)
* BBDD documentales (MongoDB, CouchDB). Los datos se almacenan en documentos, y éstos en colecciones.
* Wide-Column databases (Casandra y HBase). Son BBDD que se ejecutan en memoria, lo que les aporta mayor rendimiento. Definen un almacenamiento en tablas bidimensionales, en donde las primeras columnas son los índices(de una a tres columnas), que almacenan de forma unívoca las filas horizontales que almacenan los registros. A continuación, se define un número indeterminado de columnas de donde se construye la estructura de campos de los registros de forma totalmente flexible, basado en un formato clave/valor
* BBDD orienadas a grafos. Son BBDD en las que las relaciones entre entidades se representan mediante grafos. Los nodos representan entidadse y las líneas, relaciones entre estas. (Neo4J, Infinete Graph)


La principales características de Redis son:

* Su potencia 
* Velocidad de acceso a los datos en memoria
* Es probablemente la más popular de las BBDD NoSQL. Fundamentalmente porque al tratarse de un motor de BBDD en memoria elimina la sobrecarga de acceso a disco y red.
* Al igual que otras BBDD NoSQL, Redis trabaja con una distribución maestro-esclavos, en la que en caso de fallo del nodo maestro alguno de los esclavos puede tomar esta responsabiliad
* Es una solución de open source (BS - Berkeley Software Distribution)
* De forma predeterminada no tiene índices, aunque pueden ser creados como datos adicionales

Enter los inconvenientes:

* la cantidad de datos que podemos almacenar dependen del tamaño de la memoria. Aunque Redis puede configurarse para que almacene la información a disco
* No dispone de query language, pero dispone de soporte LUA.

## Usos habituales

* Almacenamiento y gestión de sesiones de usuario. La utilización de sesiones sin estado es muy común en las aplicaciones actuales, en las que se crea la sesión cuando se autentica el usuario, y permanece activa hasta que cierra la sesión o ésta expira por tiempo. Redis permite el almacenamiento, gestión y expiración de sesión de forma automática basándose en tiempo de almacenamiento predefinido.
* Caché. Su uso se ha extendido tanto como caché de BBDD como caché de API's, para optimizar tanto el tráfico de red como el acceso a disco. Además Redis permite definir el tiempo de validez de los datos cacheados, basándose en la frecuencia con que cambian los datos.
* Pub/Sub
* Leaderboards para juegos. Las BBDD convenionales son lentas para la velocidad que se necesita en este tipo de escenarios. Para este tipo de soluciones Redis ofrece un tipo de datos específico, el Redis Sorted Set, en la que se guarda una lista ordenada de usuarios por puntuación.
* Busquedas geoespaciales. Redis define pares de objetos basados en longitud y latitud, así como comandos geoespaciales específicos que permiten identificar los objetos cercanos a un punto geoespacial, distancia entre puntos, o lugares dentro de un determinado radio de acción.


## Instalación con docker

comando con el que lo he instalado --> docker run --name redis-zerti -p 6379:6379 -d redis redis-server --appendonly yes

docker run --name recorder-redis -p 6379:6379 -d redis:alpine

docker exec -it redis-zerti /bin/bash  --> docker cli

## Referencias 