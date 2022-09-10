# Variables de entorno

En linux se pueden listar las variables e entorno con el comando env.

En los contenedores se pueden inyectar variables de entorno para que la aplicación sea configurable, como passw, o usuarios de BBDD, URL de destino de APIS, etc.

Las variables de entorno de un contenedor se pueden declarar en el momento de su creación:

```sh
docker create -P -e varibale1=valor1 -e variable2=valor2 NOMBRE_IMAGEN
```

Para conocer las variables de entorno declaradas en un contenedor, habrá que ejecutar su bash, y utilizar el comando env.