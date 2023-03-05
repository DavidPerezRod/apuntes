# The Data Dichotomy: Rethinking the Way We Treat Data and Services

En términos generales, los microservicios suponen no solo una solución al mundo distribuido actual, sino a la propia organización empresarial con ámbitos funcionales distribuidos. Ésta no es una realidad nueva, pero los microservicios si suponen un avance en la forma de transmitir los datos entre ellas, ya que resulta más cohesionada a nivel tecnológico.

Los microservicios se pueden desplegar de forma independiente. Es este atributo, más que ningún otro, el que les da su valor, y que les permite escalar. Escalar en ambos mundos, el de una población masiva de usuarios y los datos que generan, y escalar en términos empresariales, a medida que sus equipos y su organización crecen.

**_Ventajas_**

* se pueden desplegar de forma independiente. Esto les permite escalar a nivel de desarrollo, qué quiere decir esto, que los equipos que trabajan en ellos pueden crecer, y ser independientes.

**_Desventajas_**

* relación entre microservicios. Si un servicio implementa una característica que necesita otro microservicio, es necesario hacer cambios en ambos. En este sentido, se pierde la ventaja de despliegue independiente, porque habría que coordinar el despliegue de ambos microservicios. Así pues la coordinación de los equipos y los ciclos de liberación, repercuten negativamente en una metodología ágil de desarrollo.

**_Consecuencias_**

* Es necesario evitar los cambios transversales, separando limpiamente las responsabilidades de los servicios. Sin embargo, en los servicios empresariales, muchas veces se comparte el flujo de datos, de forma que resulta muy difícil realizar esta separación de responsabilidades. 

### La dicotomía de datos

Es probable que el problema se deba a la naturaleza de cada uno de los elementos, microservicios, componentes y datos.

Por un lado el a la hora de diseñar componentes de software se busca independencia funcional, y encapsulación. Ambas propiedades apuntan en la dirección de ocultar la estructura de sus propios datos, para poder evolucionar de forma independiente a cualquier otro sistema.

Sin embargo, por otro lado, el diseño de BBDD sigue unos principios completamente distintos, cuyo objetivo es exponer los datos.

Estos dos objetivos contrapuestos, harán presión para que o bien el API de nuestros microservicios se amplíe para exponer más información y evitar cambios por requisitos de terceros, o bien se amplíe el dataset expuesto. Además toda esta problemática será directamente proporcional al volumen y complejidad de los datos "ocultos" por el microservicio.

La consecuencia, es que generaremos sistemas con algo parecido a BBDD compartidas, que tienen retos operativos y de ingeniería significativos.

Uno de los enfoques más comunes es el de disponer de una BBDD exclusiva por microservicio. Y aquí surje otro problema, podemos tener distintos microservicios consumiendo los mismos datos, haciendo una interpretación diferente de éstos. Y cuantas más copias mutables existan del mismo dato, más divergiran éstos con el tiempo.

El principio que debería regir la estructura y funcionamiento de los microservicios es consierar el dato como el elmento más importante, teniendo en cuenta la necesidad de consumir datos procedentes del exterior y compartir los propios, al mismo tiempo que se oculta el estado interno de los microservicios.

### El problema tecnológico

Parte del problema reside en la naturaleza de las tecnologías empleadas para la construcción de microservicios:

* Las interfaces de servicio son poco adecuadas para compartir datos a cualquier nivel de escala
* la mensajería no proporciona referencias históricas y facilita la corrupción de los datos con el tiempo.
* las BBDD compartidas aglutinan demasiada información en un solo lugar y dificulta la evolución de los productos.

### El ciclo de la insuficiencia de datos

Llegados, a este punto, y aunque no existe una solución técnica infalible para este problema, si se puede llegar a un compromiso.

Este compromiso particular implica un grado de centralización. Se puede utilizar un registro distribuido para ello, ya que proporciona flujos retentivos y escalables. Pero para ello se necesita que los servicios sean capaces de unirse y operar en estos flujos compartidos, y al mismo tiempo evitar construir servicios complicados y centralizados 'Servicios de Dios' que hacen este tipo de procesamiento.  Así que un mejor enfoque es integrar el procesamiento de flujos en cada servicio consumidor. Esto significa que los servicios pueden unir varios conjuntos de datos compartidos e iterar sobre ellos a su propio ritmo

Una forma de conseguirlo es el uso de plataformas streaming. Dentro de este tipo de tecnologías está Kafka, que es adecuada cuando los datos compartidos deben guardar un estado del sistema.

El uso de un registro distribuido nos empuja por un camino bastante trillado, uno que utiliza la mensajería para hacer que los servicios sean dirigidos por eventos. En general, se considera que estos enfoques proporcionan una mejor escalabilidad y un mejor desacoplamiento que sus hermanos de solicitud-respuesta, ya que trasladan el control del flujo del emisor al receptor. Esto aumenta la autonomía de cada servicio. Para ser justos, tiene un coste: se necesita un intermediario

Así, en lugar de un sistema de mensajería tradicional, se pueden aprovechar algunas propiedades adicionales. El transporte puede ser escalado linealmente de la misma manera que un sistema de archivos distribuido. Los datos también pueden conservarse en el registro a largo plazo. Así que es mensajería, pero también es almacenamiento. Almacenamiento que escala, y sin los peligros del estado mutable compartido.

Mientras que los datos se almacenan en flujos compartidos a los que todos los servicios pueden acceder, el procesamiento de éstos es específco, de forma que cada microservicio decide qué conjunto de datos debe utilizar.

A veces un servicio necesita un conjunto de datos local e histórico en un motor de base de datos de su elección. El truco aquí es asegurar que la copia puede ser regenerada desde el origen a voluntad, volviendo al registro distribuido. Así, los problemas de divergencia de datos con el tiempo son mucho menos frecuentes.

Los servicios son esencialmente impulsados por eventos, lo que significa que, a medida que los conjuntos de datos crecen, los servicios pueden seguir reaccionando rápidamente a los eventos del negocio.

Esto hace que sea mucho más fácil construir servicios más simples que no necesitan preocuparse por la escala, la adición de nuevos servicios no requiere que los servicios anteriores cambien. Esto facilita la incorporación de nuevos servicios.

# Build Services on a Backbone of Events

Aunque a priori, parece natural que los microservicios se construyan sobre un protocolo de peticiones y respuestas rest, este punto de vista puede ir cambiando a medida que el número de servicios crece gradualemnte y la red de interacciones síncronas lo hace también. Ya que pueden aumentar los problemas de disponibilidad.

Obviamente, una de las primeras medidas que se pueden tomar para evitar este problema es el de implementar comunicaciones asíncornas. Como primera medida, lo más fácil es identificar los servicios que de hecho tienen una naturaleza asíncrona en la vida real.

Otra medida, es evitar todos aquellos job de BBDD destinados a actualizar estados resultantes de las relaciones entre los servicios. Por lo que probablemente aquí tendremos otra relación asíncrona.

Una buena forma de evitar este tipo de problemas de forma generalizada, es sin duda cambiar el modelo de programación imperativa en la que los servicios se relacionan por cadenas de órdenas, por otro en el que los servicios se relacionan por flujos de eventos. Además desde el punto de vista de las relaciones de backend, el modelo clásico, suele incurrir por este tipo de mensajería en la mezacla de responsabilidades. Ya que muchas veces el punto de entrada al backend, es el responsable de conocer las responsabilidades del resto de servicios (ver api gateway). Esta es una propiedad muy interesante de este tipo de arquitectura denominada  Receiver Driven Routing, en la que la lógica se traslada al receptor de eventos en lugar de al emisor, y por lo tanto la carga de responsabilidad se invierte, y se reduce el acoplamiento, que a su vez redunda en una arquitectura "enchufable" en la que los coponentes pueden sustiruirse con facilidad. Es la caracerística de las [plataformas streaming](https://www.confluent.io/blog/stream-data-platform-1/)

## Solicitudes. Comando, eventos y consultas.

Existen 3 formas en las que los servicios pueden relacionarse entre ellos:

* Comandos. Es algo que cambiará el estado del sistema, es una petición a otro servicio de la que se espera una respuesta
* Eventos. Son tanto un hecho como un desencadenante, en el sentido de que se expresa algo que ha sucedido como una notificación. Son datos que pueden ser reutilizados por cualquier servicio del sistema. Pero además implican menor acoplamiento que los comandos y las consultas.
* Consultas. No alteran el estado del sistema. Son una petición para buscar algo.

Puesto que las consultas, no implican la alteración del estado del sistema, se podría construir un modelo híbrido, en el que tanto eventos como comandos se implementan mediante un flujo de eventos, mietras que las consultas se hace por medio de comunicaciones síncronas tradicionales. Sin embargo, en sistemas complejos, éste tampoco es un buen planteamiento, ya que acopla a los servicios en tiempo de ejecución.

Una solución a este problema es utilizar el flujo de eventos para que cada servicio almacene datos localmente, en contraposición al tradicional [data-on-the-outside](http://cidrdb.org/cidr2005/papers/P12.pdf). Es decir, cada servicio se suscribirá al canal o los canales en los que se publique la información que necesiten, y evitar consultas a contextos específicos de cada servicio.

Esta estrategia se denomina "consulta por transferencia de estado a través de eventos" (Query by event carried state transfer), y tiene 3 ventajas:

* mayor desacoplamiento. Las consultas son locales, no implican llamadas entre contextos
* mayor autonomía. Los servicios no están limitados ni afectados por el API ofrecida por otros servicios.
* uniones eficientes. En comunicaciones síncronas, a medida que la carga de trabajo de los servicios aumenta, las consultas se convierten en una "unión de red" entre servicios. Por medio de este mecanismo evitamos dicha unión.

Sin embargo este enfoque, tiene sus inconvenientes. El principal, que todas aquellas consultas en las que los servicios eran stateless, se convertirán en stateful al tener que hacer seguimiento y conservación de los datos propagados a lo largo del tiempo. Otros problemas derivados son la duplicación del estado y vigilar su divergencia en el tiempo.

Una posible solución es el principio del escritor único.

## Principio de escritor único

Es un principio bastante sencillo, que implica que los eventos de un tipo específico, solo pueden ser propagados por un servicio. Esta pŕactica ayuda a controlar la consistencia y validez de los datos. A partir de aquí, los servicios deben responsabilizarse de la consistencia de sus datos locales, con su corrección de errores, gestión de situaciones de cambios de esquema, etc.

Este modelo puede parecer similar al bus empresarial, pero es sutilmente diferente, ya que el segundo se centra en la transferencia de un estado, vinculado efectivamente a las BBDD a través de una red. Sin embargo en la colaboración de eventos, es la cascada de eventos la que desencadena la acción de los servicios, y hace qu estos progresen hacia nuevos estados y etapas de negocio.

Además, este patrón, [Event Collaboration](https://martinfowler.com/eaaDev/EventCollaboration.html) se puede manejar tanto a nivel micro como macro, o hibridarse donde tenga sentido.

Por ejemplo para limitar el alcance de las consultas remotas, se puede utilizar el patrón "contexto agrupado" (clustered contexts), donde los flujos, son el único patrón de comunicación entre contextos. Pero los servicios dentro de un contexto, aprovechan tanto el procesamiento basado en eventos como las vistas basasdas en peticiones. Por otro lado, en determinados ámbitos, en los que se quiera evitar el mantenimiento de datos locales, y aprovechar la flexibilidad que brindan las consultas remotas, se puede emplear el [flujo de eventos sin estado] (https://martinfowler.com/articles/serverless.html). (revisar esta parte, puede haber confusión en el último párrafo, tal vez forma parte del cluster context, hay que ver qué dice fowler).

## Las 5 ventajas clave de los servicios basados en eventos:

* Desacoplamiento. Se eliminan los flujos síncronos (bloqueantes) de los comandos. Al romper los flujos de trabajo síncronos, se desacoplan los servicios y es más fácil tanto conectar otros nuevos como modificar los existents.
* flujos offline/asíncronos. Permiten una aproximación más natural a todos los procesos empresariales que de facto son asíncronos, respetando los que son síncronos.
* transferencia de estado. Los flujos de eventos proporcionan un mecanismo eficiente para transmitir datos, que posteriormente pueden ser reconstruidos, consultados y analizados dentro de un contexto determnado. El estado es el evento.
* uniones. Es más fácil combinar, unir o agrupar conjuntos de datos de diferentes servicios, ya que son rápidas y locales.
* trazabilidad. Al haber un único origen de datos, inmutable y persistente, es más fácil hacer el seguimiento de la información en el tiempo.

# Using Apache Kafka as a Scalable Event-Driven Backbone for Service Architectures

Apache Kafka es una especie de sistema de mensajería, pero tiene algunas diferencias con los típicos brokeres. Inicialmente no fue diseñado par implementar JMS y AMQP; fue diseñado para escalar, aunque lo cierto es que sigue siendo el sistema de mensajería más rápido

## Ventajas de su registro: una estructura para retener y distribuir mensajes

La estructura del registro Kafka, es símplemente una colección de mensajes anexados secuencialmente a un archivo. Así un servicio, cuando quiere leer mensajes de Kafka, busca la posición del último mensaje que leyó, y luego explora secuencialemnte leyendo todos los mensajes en orden, mientras va guardando sistemáticamente su nueva posición en el registro. Tanto las lecturas como las escrituras son secuenciales, lo que permite que se aprovechen de:

* Pre-fetch
* capas de caché
* agrupación de operaciones

De hecho cuando se leen mensajes de kafka, el servidor no los importa a la JVM, los datos se copian directamente del búfer de disco al bufer de red. La mayoría de los sitemas de mensajes tradicionales se construyen utilizando estructuras de índices, tablas hash o árboles B, sin embargo las operaciones secuenciales por lotes de Kafka ayudan al rendimiento general, en concreto en las recuperaciones tras interrupciones, que son situaciones que tienden a ralentizar a otros brokers.

Sin embargo kafka no es solo un registro, es una colección de registros:

* enruta mensajes entre productores y consumidores
* tiene replicación para propoprcionar tolerancia a fallos. Los clústeres kafka suelen empezar con 3 máquinas
* fiable
* multi-tenancy

## Segregar la carga en los ecosistemas multiservicio

En las arquitecturas de servicios es habitual que los servicios de una empresa compartan un único cluster, pero al hacerlo se abre la posibilidad de ataques inadvertidos de denegación de servicio que causen inestabilidad o inactividad.

Ante este tipo de situaciones, Kafka incluye una función de control de rendimiento que permite asignar una cantidad definida de ancho de banda a servicios específicos, garantizando que funcionen dentro de unos SLAs, y evita que puedan consumir ancho de banda de otros servicios del mismo cluster. Esta función puede aplicarse a instancias de un servicio o a grupos de carga.

## Garantías de ordenación

Fundamentalmente podemos encontrar 3 casos:

* En muchas ocasiones el orden en que se consumen los mensajes es iportante. Es decir mensajes que requieren un orden relativo deben ser enviados a la misma partición, para lo cual es necesario que compartan la misma clave. Así el flujo de actualización de la información utilizará esta clave (por ejemplo CustomerId) como clave de fragmentación, para hacer que al recibirlos se almacenen en la misma partición y queden ordenados.

* En otras ocasiones el ordenamiento basado en claves no será suficiente, y se necesitará un ordenamiento global, en este caso será necesario utilizar una partición por topic. El rendimiento en este caso, se limitará a una sola máquinna.

* Reintentos del productor. Esta situación la encontraremos cuando se produce algún fallo durante la transmisión. Cualquier mensaje que no se reenvé al broker será reintentado. Como estos se envían por lotes, hay que evitar que se produzca una reordenación de eventos y se reintentes lotes enteros tras algún fallo. Esta característica es configurable.

## Equilibrio de carga y servicios altamente disponibles

Esta situación se da cuando tenemos servicios de replicados en alta disponibilidad leyendo de un mismo topic. En este caso lo que hace Kafka es dividir sus particiones entre las n instancias, en relación uno a uno. De forma que si uno de los servicios falla, Kafka detecta el fallo y redirige todos los mensajes del servicio caído a otro de los disponibles. Cuando el servicio se recupere del fallo, Kafka vuelve a invertir la carga.

Esto tiene otra ventaja, y es que el orden está garantizado, incluso cuando los servicios fallan y se reinician.

## Claves compactadas 

Kafka también incluye un tipo especial de topic, que permite gestionar datos con clave primaria, y que se pueden introducir u obtener de una BBDD. Este tipo de topic conserva solo los eventos más recientes.

Este tipo de topic funciona como los [árboles LSM](https://programmerclick.com/article/1777964986/) simples. El tema se escanea periódicamente y los mensajes antiguos se eliminan si han sido sustituidos (en función de su clave). Sin embargo su actualización se produce mediante un proceso asíncrono, de forma que no siempre están actualizados.

Sin embargo permiten realizar algunas optimizaciones:

* reducen la rapidez con la que crece un conjunto de datos, ya que la información de los eventos antiguos se va eliminando.
* al hacer más pequeños los conjuntos de datos, es más fácil transportarlos de una máquina a otra.
* reduce los tiempos de carga cuando se quiere reprocesar temas enteros desde cero, cargar conjuntos de datos de origen de eventos o cargar datos en kafka Streams.

## Almacenamiento a largo plazo

Se trata de una de las principales diferencias de Kafka con otros sistemas de almacenamiento, Kafka puede guardar la información a largo plazo. Para ello utiliza un par de patrones:

* [Event Sourcing](https://martinfowler.com/eaaDev/EventSourcing.html): se almacena cada cambio de estado que hacen los servicios en cada topic, para que pueda ser reproducido en su totalidad
* Mantener el último evento para cada clave, utilizando un topic compactado
* Combinar los dos anteriores mediante Kafka Streams, que proporciona una semántica más rica a través de su función State Store que ayuuda en la autmatización de la descarga de datos.
  
## Segregación de topics públicos y privados

kafka permite diferenciar topics internos y externos, asignando mediante [TLS O SASL](https://www.it-swarm-es.com/es/encryption/seguridad-y-autenticacion-ssl-vs-sasl/1068623236/) permiso de escritura o lectura a los servicios. De forma que se puede hacer una clasificación y correspondencia de servicios y topics.

## Utilizar esquemas para gestionar la evolución de los datos en el tiempo

En este contexto, los esqeumas proporcionan un contrato que define cómo debe ser un mensaje. Un aspecto interesante a tener en cuenta si se trabaja con esquemas, es que estos se identifiquen con una versión y sean retrocompatibles, y si no lo son, que el esquema permita identificarlo. Aunque hay diverss opciones como Protobuf, JsonSchema, etc. el más habitual es Avro. Confluent, añade el Confluent Schema Registry que añade validación en tiempo de ejecución a mensajes codificados con Avro, asegurando que los mensajes incompatibles fallen en la publicación, locual protege al sistema frente a los datos no válidos.

Sin embargo en muchas ocasiones, será imposible mantener la retrocompatibilidad, y se tendrán versiones incompatibles para un mismo tipo de mensaje. La solución en este caso suele consistir en tener topic diferentes para versiones de mensaje incompatibles. Y si es necesaria la interacción entre servicios que utilicen dichas versiones, utilizar Kafka Streams para hacer trabajos de transformación entre versiones. Relacionado con esto, está el principio de escritor único, que facilita la supervisión y actualización de mensajes.

## Servicios basados en comandos

En ocasiones se darán escenarios en los que sean necesarios servicios basados en comandos, en lugar de servicios basados en consultas. O lo que es lo mismo una comunicación síncrona. La forma de simularlo será crear dos topics, uno de solicitud, y otro de respuesta.

Lo primero que hay que tener en cuenta, es que Kafka no está diseñado para este tipo de comunicaciones, ya que que no actúa como un protocolo ligero como HTTP. Sin embargo, también es cierto que proporciona una serie de características que de alguna forma los servicios basados en HTTP deben implementar: reintentos, circuit Breaker, monitorización, etc.

Aún así se pueden combinar de las siguientes formas:

* mezclar una protocolo sin estado HTTP en capas, sobre una columna vertebral de eventos.
* tener interfaces separadas en cada servicio, una para los eventos y otra para respuesta a peticiones. Los eventos se crean para todos los cambios de estado y HTTP para las respuestas a solicitudes. Pero se debe tener cuidado a medida que el sistema crece, y seguramente el patrón de contexto agrupado sea más adecuado, ya que promueve el uso de transferencia de estado con eventos, mientras compartimenta las consultas remotas.

## Referencias

*  [Confluent - Ben Stopford](https://www.confluent.io/blog/data-dichotomy-rethinking-the-way-we-treat-data-and-services/)
  