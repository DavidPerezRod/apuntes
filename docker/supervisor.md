# Supervisor

Es un proceso de control y monitorización de procesos de linux. De cara a docker, su principal virtud, es que se puede hacer que haya más de un proceso corriendo en un contenedor.

Supervisor será el proceso principal del contendor, y él será el encargado de iniciar el resto de procesos que necesite nuestro contenedor. Incluso los monitorizará y relanzará en caso de errores.

Para utilizarlo en primer lugar es necesario crear un fichero de configuración de supervisor:

```sh
[program:nginx]
command=/usr/sbin/nginx -g 'daemon off;'
autostart=true
autorestart=true
```
El cual indica que supervisor tiene que ejecutar nginx, hacerlo de forma automática, y reiniciarlo en caso de caida.

A continuación se edita el fichero Dockerfile añadiendo lo siguiente:

* la instalación de supervisor
* el fichero de configuración que hemos creado
* ejecutando supervisor como proceso principal

```Dockerfile
  1 FROM ubuntu:18.04
  2 
  3 RUN apt-get update
  4 RUN apt-get install nginx -y
  5 RUN apt-get install supervisor -y
  6 RUN echo 'hello world 4.0' > /var/www/html/index.html
  7 
  8 ADD supervisor_services.conf /etc/supervisor/conf.d
  9 
 10 EXPOSE 80
 11 
 12 #CMD nginx -g 'daemon off;'
 13 CMD supervisor -n -c /etc/supervisor/supervisord.conf
```

Una vez creada la imagen, el contendor, y arrancado este útlimo, la forma más fácil de comprobar que nginx se ejecuta de forma automática tras caerse, es matar el proceso mediante pkill. Accedemos a la bash del contendor, hacemos un ps aux o ps  -A, matamos el proceso, y si volvemos a hacer el ps, vemos que su identificador de proceso ha cambiado.