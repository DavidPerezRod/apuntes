# The Data Dichotomy: Rethinking the Way We Treat Data and Services

En términos generales, los microservicios suponen una solución al mundo distribuido actual, sino a la propia organización empresarial con ámbitos funcionales distribuidos. Ésta no es una realidad nueva, pero los microservicios si suponen un avance en la forma de transmitir los datos entre ellas, ya que resulta más cohesionada a nivel tecnológico.

Los microservicios se pueden desplegar de forma independiente. Es este atributo, más que ningún otro, el que les da su valor.  Les permite escalar. Para crecer. No tanto en el sentido de escalar a cuatrillones de usuarios o petabytes de datos (aunque pueden ayudar en eso), sino más bien escalar en términos de personas, a medida que sus equipos y su organización crecen.

## Ventajas

* se pueden desplegar de forma independiente. Esto les permite escalar a nivel de desarrollo, qué quiere decir esto, que los equipos que trabajan en ellos pueden crecer, y ser independientes.

## Desventajas

* relación entre microservicios. Si un servicio implementa una característica que necesita otro microservicio, es necesario hacer cambios en ambos. En este sentido, se pierde la ventaja de despliegue independiente, porque habría que coordinar el despliegue de ambos microservicios. Así pues la coordinación de los equipos y los ciclos de liberación, repercuten negativamente en una metodología ágil de desarrollo.


## Consecuencias

* Es necesario evitar los cambios transversales, separando limpiamente las responsabilidades de los servicios. Sin embargo, en los servicios empresariales, muchas veces se comparte el flujo de datos, de forma que resulta muy difícil realizar esta separación de responsabilidades. 

### La dicotomía de datos

Es probable que el problema se deba a la naturaleza de cada uno de los elementos, microservicios, componentes y datos. Mientras que a nivel de componente la independencia funcional y la encapsulación son importantes, y apuntan en la dirección de ocultar la estructura de sus propios datos, para poder evolucionar de forma independiente a cualquier otro sistema.

Pero por otro lado, los datos, almacenados en BBDD siguen unos objetivos completamente distintos, ya que su objetivo es exponer sus datos.

Estos dos objetivos contrapuestos, harán presión para que o bien el API de nuestros microservicios se amplie par exponer más información y evitar cambios por requisitos de terceros, o bien se ampliará el dataset expuesto en cada uno de sus funcionalidades, para conseguir el mismo objetivo. Además toda esta problemática será directamente proporcionarl al volumen y complejidad de los datos "ocultos" por el microservicio.

La consecuencia, es que generaremos sistemas con algo parecido a BBDD compartidas, que tienen retos operativos y de ingeniería significativos.


Uno de los enfoques más comunes es el de disponer de una BBDD exclusiva por microservicio. Y aquí surje otro problema, podemos tener distintos microservicios consumiendo los mismos datos, haciendo una interpretación diferente de éstos. Y cuantas más copias mutables existan del mismo dato, más divergiran éstos con el tiempo.

El principio que debería regir la estructura y funcionamiento de los microservicios, es consierar el dato como el elmento más importante, se debe ocultar el estado interno de nuestros microservicios, pero se debe tener en cuenta la existencia de "datos en el exterior".

### El problema tecnológico

Parte del problema reside en la naturaleza de las tecnologías empleadas para la construcción de microservicios:

* Las interfaces de servicio son poco adecuadas para compartir datos a cualquier nivel de escala
* la mensajería no proporciona referencias históricas y facilita la corrupción de los datos con el tiempo.
* las BBDD compartidas aglutinan demasiada información en un solo lugar y dificulta la evolución de los productos.

### El ciclo de la insuficiencia de datos.

Llegados, a este punto, y aunque no existe una solución técnica infalible para este problema, si se puede llegar a un compromiso. 

Este compromiso particular implica un grado de centralización. Podemos utilizar un registro distribuido para ello, ya que proporciona flujos retentivos y escalables. Pero necesitaremos que nuestros servicios sean capaces de unirse y operar en estos flujos compartidos, y al mismo tiempo evitar los complicados y centralizados 'Servicios de Dios' que hacen este tipo de procesamiento.  Así que un mejor enfoque es integrar el procesamiento de flujos en cada servicio consumidor. Esto significa que los servicios pueden unir varios conjuntos de datos compartidos e iterar sobre ellos a su propio ritmo

Una forma de conseguirlo es el uso de plataformas streaming. Dentro de este tipo de tecnologías está Kafka, que es adecuada cuando los datos compartidos deben guardar un estado del sistema.

El uso de un registro distribuido nos empuja por un camino bastante trillado, uno que utiliza la mensajería para hacer que los servicios sean dirigidos por eventos. En general, se considera que estos enfoques proporcionan una mejor escalabilidad y un mejor desacoplamiento que sus hermanos de solicitud-respuesta, ya que trasladan el control del flujo del emisor al receptor. Esto aumenta la autonomía de cada servicio. Para ser justos, tiene un coste: se necesita un intermediario

Así, en lugar de un sistema de mensajería tradicional, se pueden aprovechar algunas propiedades adicionales. El transporte puede ser escalado linealmente de la misma manera que un sistema de archivos distribuido. Los datos también pueden conservarse en el registro, a largo plazo. Así que es mensajería, pero también es almacenamiento. Almacenamiento que escala, y sin los peligros del estado mutable compartido.

Mientras que los datos se almacenan en flujos compartidos, a los que todos los servicios pueden acceder, las uniones y el procesamiento que realiza un servicio son privados. Cada microservicio decide que conjunto de datos debe utilizar, y puede encapsular la funcionalidad sobre ellos.

A veces un servicio necesita un conjunto de datos local e histórico en un motor de base de datos de su elección. El truco aquí es asegurar que la copia puede ser regenerada desde el origen a voluntad, volviendo al registro distribuido. Así, los problemas de divergencia de datos con el tiempo son mucho menos frecuentes.

Los servicios son esencialmente impulsados por eventos, lo que significa que, a medida que los conjuntos de datos crecen, los servicios pueden seguir reaccionando rápidamente a los eventos del negocio.

La preocupación por la escalabilidad se traslada de los servicios al broker. Esto hace que sea mucho más fácil construir servicios más simples que no necesitan preocuparse por la escala. La adición de nuevos servicios no requiere que los servicios anteriores cambien. Esto facilita la incorporación de nuevos servicios.

# Build Services on a Backbone of Events

Aunque a priori, parece natural que los microservicios se construyan sobre un protocolo de peticiones y respuestas rest, este punto de vista puede ir cambiando a medida que el número de servicios crece gradualemnte y la red de interacciones síncronas lo hace también. Ya que pueden aumentar los problemas de disponibilidad.

Obviamente, una de las primeras medidas que se pueden tomar para evitar este problema es el de implementar comunicaciones asíncornas. Como primera medida, lo más fácil es identificar los servicios que de hecho tienen una naturaleza asíncrona en la vida real.

Otra medida, es evitar todos aquellos job de BBDD destinados a actualizar estados resultantes de las relaciones entre los servicios. Por lo que probablemente aquí tendremos otra relación asíncrona.

Una buena forma de evitar este tipo de problemas de forma generalizada, sin duda es cambiar el modelo de programación imperativa en la que los servicios se relacionan por cadenas de órdenas, por otro en el que los servicios se relacionan por flujos de eventos.

## Solicitudes. Comando, eventos y consultas.