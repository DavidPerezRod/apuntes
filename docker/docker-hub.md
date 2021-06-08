# Docker Hub

Se trata de un repositorio para distribuir imágenes. Es un host gratuito, en el que se pueden subir las imágenes de forma gratuita. Además proporciona una serie de utilidades gratuitas como:

* creación automática de imágenes por medio de un dockerfile, compatible con bitbucket y git.
* webhost para notificaciones.

Muchas veces será necesario contratar la parte de pago. Aunque por lo general todos los sistemas cloud, tienen su propio repositorio de registro de imágenes docker. Amazon, Google, Docker y Azure.

* Subir imágenes al repositorio:

1. **_LOGIN:_** docker login. Que por defecto, si no se le indica ningún repositorio, va a apuntar a dockerhub.  
  
2. **_CREAR TAG:_** docker tag NOMBRE_IMAGEN usuario/NOMBRE_IMAGEN:ETIQUETA

3. **_HACER PUSH DE LA ETIQUETA CREADA CON EL USUARIO:_** docker push usuario/NOMBRE_IMAGEN:ETIQUETA

* Bajar imágenes al repositorio:

1. **_LOGIN:_** docker login. Que por defecto, si no se le indica ningún repositorio va a apuntar por defecto contra dockerhub.  
  
2. **_BAJAR IMAGEN_** docker pull NOMBRE_IMAGEN


Después para manejar imágenes, las nuevas imágenes habrá que nombrarlas sobre el tag ya creado, por ejemplo, si creásemos una nueva imagen helloworld, con el tag 3.0. Deberíamos proceder creando la nueva imagen del repositorio así:

```sh
docker tag helloworld:3.0 usuario/helloworld:3.0
```

A la hora de hacer los push, los repositorios docker, reconocen las capas que se han cambiado en la nueva imagen, y solo suben esas.