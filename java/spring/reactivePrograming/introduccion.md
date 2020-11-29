# Introducción

La programación reactiva es una nueva especificación en el API Java, denominada reactive streams, que aparece en la versión 9. 

El manifiesto "reactive" fue publicado en 2013, y los sistemas reactivos, implican un paradigma de arquitectura y de diseño en respuesta a su naturaleza cloud y demanda masiva que sufren.

Pero la programación reactiva implica otra granularidad. Se basa en técnicas de programación asíncrona no bloqueante centradas en la captura de eventos. Se basa en la concepción de que todo flujo puede ser observado por un observador (observer and observable philosophy).

Sin embargo no hay que confundirlo con la programción funcional reactiva, que es una cosa distinta, en la que se intentan combinar dos paradigmas de programación, la programación reactiva, y la programación funcional.

Dicho esto, el manifiesto reactivo se articula en torno a 4 conceptos:

* **Responsive**
  * el sistema debe responder de forma adecuada
  * la sensibilidad (responsiveness), es la piedra angular de la usabilidad y utilidad
  * también implica que los problemas deben ser detectados rápidamente para tratarlos de forma efectiva.
  * los sistemas responsivos proporcionan unos tiempos de respuesta rápidos y consistentes
  * el comportamiento consistente simplifica la gestión de errores, genera confianza en el usuario final, y anima a seguir usando el sistema  
* **Resilient**
  * el sistema debe seguir respondiendo a pesar de la existencia de fallo
  * la resiliencia se logra mediante replicación, contención, aislamiento y delegación
  * los fallos se aislan en el interior de cada componente
  * las partes del sistema puedne fallar sin comprometer el sistema como conjunto
  * la recuperación del fallo de componentes, es delegada al resto por redundancia.
  * se asegura la alta disponibilidad por replicación  
* **Elastic**
  * el sistema responde ante variación de la carga de trabajo
  * el sistema puede variar los recursos que utiliza en función de la variación de la demanda
  * los sitemas reactivos logran la  eslasticidad de forma rentable a nivel de recursos software y hardware
* **Message driven** Es probablemente uno de los aspectos más importantes desde el punto de vista de los desarrolladores java.
  * los sistemas reactivos confían en la mensajería asíncrona como frontera entre componentes, porque proporciona bajo acoplamiento, aislamiento y transparencia de ubicación (el código que envía el mensaje, permanece estable con independencia de a dónde tenga que enviar el contenido)
  * la transparencia de ubicación posibilita la gestión de fallos
  * la comunicación mediante mensajería asíncrona permite la gestión de carga, elasticidad y control de flujo
  * la comunicación no bloqueante permite que los destinatarios solo consuman recursos cuando estén activos, disminuyendo la sobrecarga de sistemas.

Teniendo en cuenta lo anterios, se puede decir que la programación reactiva:

* se centra en la ejecución asíncrona no bloqueante, mediante streams.
* es otro recurso más a la hora de contruir sistemas reactivos.
* "Los programas reactivos también mantienen una interacción continua con su entorno, pero una velocidad que está determinada por el entorno, no por el programa en sí. Los programas interactivos funcionan a su propio ritmo y se ocupan principalmente de la comunicación, mientras que los programas reactivos solo funcionan en respuesta a demandas externas y, en su mayoría, se ocupan del manejo preciso de interrupciones. Los programas en tiempo real suelen ser reactivos" - Gerard Berry._
* Los casos de uso más habituales en los que se puede encontrar este tipo de programación son:
  * llamadas de servicios externos
  * consumidores de mensajes concurrentes masivos
  * hojas de cálculo
  * abstracción de procesamiento asíncrono (abstrae el hecho de que el progrma se síncrono o asíncrono)
* Algunas de sus características de la programación reactiva son:
  * trabaja con streams de datos
    * un stream es una secuencia de eventos ordenada en el tiempo
    * Son datos de cualquier tipo:
      * click de ratón
      * mensajes JMS
      * llamadas RESTfull
      * entradas de Twitter
      * listas datos de BBDD
  * lo hace de forma asíncrona
    * los eventos se capturan de forma asíncrona
    * un tipo de funciones se definenen para ser ejecutadas frente a la emisión de eventos
    * otro tipo lo hacen si se produce un error
    * otros cuando se completa el procesamiento del evento.
    * lo importante aquí es la independencia funcional entre tipos de funciones
  * es no bloqueante
  * fallos como mensajes
  * backpressure - (Resistencia o fuerza que se opone al flujo de datos deseado a través del software). Quizás la forma más fácil de entenderlo sea desde la velocidad de cómputo, algunos de los ejemplos:
    * transformación de datos de entrada en salida
    * lectura y escritura de ficheros.
    * comunicación entre servidores
    * presentación UI
* **Streams de datos**
  