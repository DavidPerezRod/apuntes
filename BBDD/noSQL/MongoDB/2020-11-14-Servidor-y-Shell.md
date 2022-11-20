# Servidor y Shell

# Servidor

Como ya se ha indicado en la sección de instalación, los principales comandos para arrancar y parar MongoDB son:

Ejecución de MongoDB

```shell
$ sudo systemctl status mongodb
$ sudo systemctl stop mongodb
$ sudo systemctl start mongodb
$ sudo systemctl restart mongodb
```

Configurar si MongoDB inicia automáticamente cuando se inicia el sistema o no

```shell
$ sudo systemctl disable mongodb
$ sudo systemctl enable mongodb
```

Otro comando útil para ver las opciones con las que podemos arrancar la BBD son:

```shell
$ mongod --help
```

De entre éstas, las más interesantes son:

* **mongod -f [--config] arg**: para indicar la configuraciónd de arranque.
* **mongod --port arg**: para indicar el puerto en el que debe arrancar **_27017 por defecto_**
* **mongod --logpath arg**: para indicar en que archivo debe escribir los mensajes
* **mongod --dbpath arg**: indica el directorio en el que se va a almacenar la BBDD. Por defecto, mongo está configurado para escribir en /data/db/

# Shell

Una vez arrancado el servidor, nos podemos conectar a él indicando dirección y puerto:

* mongo --port 27017 --host localhost 

o bien

* localhost:2707

Si además nos queremos conectar a una BBDD concreta:

* localhost:2707/nombre_BBDD

Y si le queremos cargar datos de entrada:

* localhost:2707/nombre_BBDD --shell archivo.js

Otros comandos de utilidad:

|COMANDO|DESCRIPCIÓN|
|-------|-----------|
|help|indica todos los comandos que se pueden ejecutar en la shell|
|db.nombre_coleccion.help()|indica las opciones que se pueden ejecutar en la colección|
|db|muestra la BBDD a la que estamos conectados|
|show dbs|muestra las BBDD disponibles en el servidor|
|show collections|muestra las colecciones disponibles en la BBDD|
|show dbs|muestra las BBDD que tenemos|
|show collections| muestra las collecciones que tenemos|
|use nombre_BBDD|cambia a la BBDD que indicamos|
|db.nombre_colecction.count()|devuelve el número de documentos en la colección|
|db.nombre_colecction.finOne()|devuelve la estructura de documentos de la colección|
|help|indica todos los comandos que se pueden ejecutar en la shell|
|load(nombre_archivo.js)|carga el contenido del archivo en la BBDD en al que nos encontremos. En estos archivos, además de datos se pueden ejecutar scripts enteros, con [comandos equivalentes a los de la shell](https://docs.mongodb.com/v2.4/core/server-side-javascript/|

Características de la consola MongoDB:

* interpreta Javascript
* hasta la v2.2 el motor que utilizaba era SpiderMonkey
* desde la v2.4 utiliza el motor V8 de google.

Comandos:

* print() --> se comporta de forma similar a java. Los documentos los escribe como array de objects.
* prinJson() --> para escribir documentos.
