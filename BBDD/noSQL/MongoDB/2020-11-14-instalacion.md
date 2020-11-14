# Instalación

* Actualizar paquetes

```shell
$ sudo apt update && sudo apt upgrade -y
```

Instalar MongoDB

```shell
$ sudo apt install mongodb
```

MongoDB se inicia automáticamente despues la instalación

Comandos:

Verificar estado

```shell
$ sudo systemctl status mongodb
```

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

Iniciar el shell de MongoDB

```shell
$ mongo
```

Desinstalar MongoDB

```shell
$ sudo systemctl stop mongodb
$ sudo apt purge mongodb
$ sudo apt autoremove
```

Una vez instalado, mongo escribe crea sus propias carpetas en 

* /var/lib/mongodb
* /var/log/mongodb
* /etc/mongodb.conf
* /data/db

