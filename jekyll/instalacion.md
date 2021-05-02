# COMANDOS

* Instalación en Mint:
Las instrucciones están sacadas de [este enlace](https://www.unixtutorial.org/how-to-install-jekyll-in-linux-mint-19/). He tenido que instalar:

  * **Ruby Bundler**: sudo apt install ruby
  * **bundler gem**: sudo gem install bundler
  * **Ruby dev package**: sudo apt install ruby-dev
  * **compilardor g++**: apt update && apt install build-essential g++
  * **Jekyll gem**: sudo gem install jekyll
* Una vez instalado, para probar su ejecución ejecutamos **_bundle exec jekyll serve_**
* Para crear un sitio de prueba básico: **_jekyll new nombre_sitio_**

Una vez generado el sitio y arrancado jekyll, si se quiere ver la página, hay que hacerlo abriendo un navegador en el puerto 4000 del localhost.

Una vez que se arranca el servidor, en el directorio del proyecto, se crea un carpeta _site. En este directorio, se entcuentra el contenido estático tanto los HTML como los css.

Dentro del fichero de configuración _config.yml, se encuentra el tema que se está utilizando y con "bundle info nombre_del_tema" obtendremos la ubicación de la dependencia.

Esta información es importante porque nos dice dónde está ubicado el tema que se está utilizando, en el cual encontramos una estructura de directorios con los estilos y lo elementos estáticos que reutiliza nuestro sitio.

A continuación los pasos que hay que seguir para dejar la instalación limpia de la plantilla por defecto es:

1. Se borra la plantilla de _config.yml
2. Se elimina de Gemfile
3. se elimina el contenido de _posts
4. se ejecuta bundle, para que actualice y elimine las dependencias
5. se crea una carpeta _layouts
6. se crea una carpeta _includes
7. se copia el contenido de la nueva plantilla en el directorio raíz del site.

Cuando se arranca Jekyll desde la carpeta del site, utiliza el fichero de configuración y el resto de los recursos para construir el sitio web. Lo hace en el interior de la carpeta _site, que es lo que habría que mover al servidor web.

Jekyll corre como proceso, de forma que si se elimina en la carpeta padre alguno de los recursos utilizados para construir el site, los eliminará dentro de éste.

