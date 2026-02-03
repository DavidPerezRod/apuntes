

Docker es una plataforma de código abierto que permite a los desarrolladores crear, empaquetar y distribuir aplicaciones y sus dependencias en contenedores. Un contenedor es una unidad de software liviana y portátil que incluye todo lo necesario para ejecutar una aplicación, como el código, las bibliotecas, las herramientas del sistema y las configuraciones.

## Qué lo diferencia de una máquina virtual

* **Nivel de virtualización**: Una máquina virtual emula un sistema operativo completo, incluido el kernel, lo que permite ejecutar múltiples sistemas operativos simultáneamente en un mismo servidor físico. Cada VM tiene su propio sistema operativo y recursos asignados, lo que resulta en una mayor sobrecarga y consumo de recursos.
Sin embargo Docker no necesita tener todo el sistema corriendo en memoria, las aplicaciones se ejecutan como procesos aislados dentro de un mismo sistema, haciendo una virtualización por contenedores. No se crea un sistema huésped completo ya que los contenedores comparten el mismo kernel. Esta filosofía permite aislar unas aplicaciones de otras, sin quer necesiten asumir la sobrecarga de un sistema huésped separado.

* **Tamaño y velocidad**: Debido a su enfoque de virtualización ligera, los contenedores de Docker son mucho más livianos y rápidos de crear y ejecutar en comparación con las máquinas virtuales. Como comparten el kernel del host y solo contienen las dependencias y las bibliotecas necesarias para ejecutar una aplicación específica, se reduce tanto el tamaño como el tiempo de inicio. En contraste, una máquina virtual incluye un sistema operativo completo y puede requerir varios gigabytes de espacio en disco y tiempo para iniciarse.

* **Portabilidad y consistencia**: Docker proporciona una mayor portabilidad y consistencia en diferentes entornos. Las imágenes de Docker son autocontenidas y portables, lo que significa que se pueden ejecutar en cualquier host de Docker compatible, independientemente del sistema operativo subyacente. Esto facilita la migración de aplicaciones entre diferentes entornos.

![comparación](imagenes/vm-docker.png)

En consecuencia, hace que sea una tecnología adecuada para la escalabilidad, alta disponibilidad y portabilidad, ya que además de ahorrar recursos frente a la virtualización, permite instalar aplicaciones multiplataforma en diferentes infraestructuras, sin necesidad de crear una configuración específica para el hardware o software del host. Lo hace mediante imágenes, que son copias portables para instalar en los contenedores, ya que contienen bibliotecas, binarios y recursos que necesitan las aplicaciones.

## Arquitectura

* **Cliente de Docker**: El cliente de Docker es el componente que interactúa con el Docker Engine y permite a los usuarios enviar comandos y solicitudes al daemon de Docker. El cliente de Docker proporciona una interfaz de línea de comandos (CLI) y una API REST que permiten a los usuarios gestionar contenedores, imágenes, redes y otros aspectos de Docker. A través del cliente de Docker, los usuarios pueden ejecutar comandos como docker run, docker build y docker ps para interactuar con los contenedores y las imágenes.

* **Docker Host**: Es el corazón de Docker y el componente principal responsable de la creación y ejecución de los contenedores. Docker Engine incluye el daemon de Docker (dockerd) y la interfaz de línea de comandos (CLI) de Docker. El daemon de Docker gestiona los contenedores y las imágenes, mientras que la CLI proporciona una interfaz para interactuar con el daemon y ejecutar comandos de Docker.

* **Imágenes de Docker**: Las imágenes de Docker son la base para la creación de contenedores. Una imagen es un paquete estático y autocontenido que incluye todo lo necesario para ejecutar una aplicación, como el código, las bibliotecas, las dependencias y las configuraciones. Las imágenes se crean a partir de archivos llamados Dockerfiles, que especifican las instrucciones para construir el entorno de ejecución de la aplicación.

* **Contenedores de Docker**: Los contenedores son instancias en ejecución de imágenes de Docker. Un contenedor se crea a partir de una imagen y se ejecuta en un entorno aislado. Cada contenedor es independiente y tiene su propio sistema de archivos, procesos, redes y recursos asignados. Los contenedores proporcionan una forma ligera y portátil de ejecutar aplicaciones, y pueden ser detenidos, iniciados, reiniciados y eliminados según sea necesario.

* **Registros de Docker**: Los registros de Docker son repositorios donde se almacenan y se comparten las imágenes de Docker. El registro público oficial es Docker Hub, pero también existen otros registros privados y locales. Los registros permiten a los desarrolladores almacenar, compartir y distribuir sus imágenes personalizadas. Además, los registros pueden tener funciones de control de acceso y seguridad para proteger las imágenes.

* **Docker Compose**: Docker Compose es una herramienta que permite definir y gestionar aplicaciones compuestas por varios contenedores. Con Docker Compose, puedes definir la configuración de los servicios, la red, los volúmenes y otros aspectos de la aplicación en un archivo YAML. Esto simplifica la gestión de aplicaciones que requieren múltiples contenedores y permite definir relaciones y dependencias entre ellos.

![arquitectura](imagenes/docker-architecture-609x270.webp)

## Sistema de archivos

Docker utiliza el sistema de archivos en capas AuFS, que permite gestionar espacios de solo lectura y espacios de escritura, y fusionarlos en ejecución.

Es esta característica la que permite que los contenedores utilicen el mismo conjunto de librería o imágenes,y distintas aplicaciones:

![filosofía docker](imagenes/sharing-layers.jpg)

Además no solo se pueden crear contenedores con las mismas librerías y diferentes aplicaciones, sino que se puede hacer composición imágenes en un contenedor:

![composicion](imagenes/composicion.jpg)

Las capas AuFS (Advanced Multi-Layered Unification File System) son un sistema de archivos de superposición

AuFS permite combinar varias capas en una sola vista coherente y proporciona un mecanismo eficiente para gestionar y almacenar imágenes de Docker. Se caracteriza porque:

* **Capas de solo lectura**: Las capas base en AuFS son de solo lectura y contienen los archivos y directorios originales de una imagen de Docker. Estas capas base son compartidas entre los contenedores que utilizan la misma imagen, lo que permite un uso eficiente del espacio en disco.

* **Capa de escritura**: Cada contenedor tiene su propia capa de escritura que se superpone a las capas base. Esta capa de escritura captura los cambios realizados en el sistema de archivos del contenedor. Los cambios se almacenan en esta capa de escritura, mientras que las capas base permanecen inalteradas.

* **Capas de superposición**: Si se crean varios contenedores a partir de la misma imagen base, cada contenedor tendrá su propia capa de escritura independiente, pero compartirá las mismas capas base de solo lectura. Esto significa que las capas de solo lectura se comparten entre contenedores, lo que permite un uso eficiente del almacenamiento y un despliegue rápido de los contenedores.

* **Compartición y eficiencia**: El sistema de archivos AuFS utiliza una técnica llamada unificación para combinar y presentar las diferentes capas en una única vista coherente del sistema de archivos. Esto permite que los cambios realizados en una capa de escritura se reflejen en la vista combinada sin afectar las capas base o las capas de otros contenedores.

## Contenedores

Y en cuanto a su cómo se organiza un contenedor, está compuesto de los siguientes elementos, que le permiten funcionar de manera aislada y ejecutar una aplicación específica. Éstos son:

* **Imagen base**: Una imagen base es el punto de partida para crear un contenedor. Representa un sistema operativo o una distribución específica con una configuración y un conjunto de herramientas predefinidos. Un contenedor se crea a partir de una imagen base y puede personalizarse y ampliarse agregando capas adicionales.

* **Sistema de archivos**: Cada contenedor tiene su propio sistema de archivos aislado. El sistema de archivos se deriva de la imagen base y puede modificarse durante la ejecución del contenedor. Los cambios en el sistema de archivos dentro del contenedor no afectan al sistema de archivos del host ni a otros contenedores.

* **Proceso de inicio**: El proceso de inicio o el proceso principal es el primer proceso que se ejecuta en el contenedor cuando se inicia. Por lo general, es el proceso principal de la aplicación que se está ejecutando dentro del contenedor. Cuando el proceso principal termina, el contenedor se detiene.

* **Espacio de red**: Cada contenedor tiene su propio espacio de red aislado. Esto significa que los contenedores pueden tener interfaces de red virtuales y direcciones IP únicas dentro de su espacio de red. Se pueden configurar reglas de red para permitir o bloquear la conectividad de red entre contenedores y con el host.

* **Variables de entorno**: Los contenedores pueden tener variables de entorno definidas que proporcionan información de configuración específica para la aplicación dentro del contenedor. Las variables de entorno pueden incluir información como claves de API, contraseñas, configuraciones de conexión a bases de datos, etc.

* **Puertos expuestos**: Un contenedor puede exponer puertos para permitir la comunicación con la aplicación que se está ejecutando dentro del contenedor. Estos puertos pueden mapearse a puertos específicos en el host o pueden utilizarse puertos dinámicos asignados automáticamente.

* **Recursos asignados**: Se pueden asignar recursos específicos al contenedor, como CPU, memoria y límites de uso de disco. Estos recursos aseguran que el contenedor tenga acceso a los recursos necesarios y que no consuma más recursos de los asignados.

Un contenedor solo vive mientras el proceso que ejecuta está vivo. Una vez finalizado, el contenedor se cierra. Por eso, si ejecutamos un contendor cuyo contenido es únicamente un sistema operativo, si éste no ejecuta ningún servicio, al finalizar se ccerrará, ya que no hay ningún proceso ejecutándose por defecto.

## Comando básicos

* **docker run** : se usa para ejecutar un contenedor desde una imagen. Ejecutará una instancia de la imagen que se le indique en el host docker si ya existe. Si la imagen no está presente en el host, la descarga de Docker Hub. El software de la imagen correrá en primer plano. Si se quiere evitar la salida por consolo se puede ejecutar el contenedor en segundo plano con la opción -d “docker run -d imagen".

* **docker ps**: enumera todos los contenedores en ejecución y alguna información básica sobre ellos, como el ID del contenedor, el nombre de la imagen que utilizamos para ejecutar los contenedores, el estado actual y el nombre del contenedor. Para ver todos los contenedores aunque no estén ejecutandose, se debe usar la opción -a que lista todos los contenedores en ejecución, los parados y los finalizados.

* **docker stop** : seguido del identificador o del nombre de un contenedor, o su nombre, detiene su ejecucióin.

* **docker rm** : seguido del identificador del contendor o de su nombre, lo elimina permanentemente.

* **docker images**: muestra la lista de imágenes disponibles y sus tamaños.

* **docker rmi** : sirve para eliminar imágenes. Se debe detener y eliminar todos los contenedores dependientes para poder eliminar una imagen.
  
* **docker pull** : seguido del nombre de una imagen, la extrae del repositorio en el que se encuentre.

* **docker exec**: ejecuta un comando dentro del contenedor. Por ejemplo, imprimir el contenido del archivo /etc/hosts “docker exec contenedor_id cat /etc/hosts”. O si queremos ejecutar bash: "docker exec -t -i mycontainer /bin/bash"