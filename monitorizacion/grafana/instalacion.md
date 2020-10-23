# Instalación

La instalación se puede hacer fácilmente [siguiendo las instrucciones de su web](https://grafana.com/docs/grafana/latest/installation/debian/)

Una vez instalado, grafana por defecto corre en el puerto 3000

Por defecto, con la instalación, no se ejecuta grafana. Los comandos para hacerlo y verificarlo:

* sudo systemctl daemon-reload
* sudo systemctl start grafana-server
* sudo systemctl status grafana-server

También se puede instalar como proceso:

* sudo systemctl enable grafana-server.service

Para parar el servicio:

* sudo systemctl stop grafana-server

Para desinstalar grafana, lo primero es parar el servidor, y después se puede hacer borrando solo la aplicación o la aplicación y las dependencias:

* sudo apt-get remove grafana.
* sudo apt-get remove --auto-remove grafana **_borrado de dependencias_**

Si además tambnién se queiren borrar los ficheros de configuración local/config, entonces podemos ejecutar cualquiera de los dos comandos siguientes:

* sudo apt-get purge grafana
* sudo apt-get purge --auto-remove grafana

Para aceder a su consola, basta con escribir en un navegador: <http://localhost:3000/login>

El usuario y password por defecto de la instalación es admin/admin. A continuación pide cambiarla.

Para cambiar el puerto de la instalación, tenemos que modificar el fichero /etc/grafana/grafana.ini. Buscamos 3000 y lo modificamos por el valor que deseemos.