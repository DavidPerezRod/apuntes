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

Otros comandos de utilidad:

|COMANDO|DESCRIPCIÓN|
|-------|-----------|
|db|muestra la BBDD a la que estamos conectados|
|show dbs|muestra las BBDD disponibles en el servidor|
|show collections|muestra las colecciones disponibles en la BBDD|


Características de la consola MongoDB:

* interpreta Javascript
* hasta la v2.2 el motor que utilizaba era SpiderMonkety
* desde la v2.4 utiliza el motor V8 de google.

Comandos:

* print() --> se comporta de forma similar a java. Los documentos los escribe como array de objects.
* prinJson() --> para escribir documentos.