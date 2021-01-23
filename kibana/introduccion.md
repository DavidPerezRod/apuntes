# Introducción

El stack ELK es un conjunto de herramientas para la monitorización, analítica de logs y explotación de información. Cada una de éstas herramientas es una herramienta muy especializada, y probablemente quizás salvo elasticsearch, ninguna está específicamente centrada en el tratamiento de logs. En concreto:

* elasticsearch: motor de búsqueda e indexación
* logstash: gestión y tratamiento de logs, cercano a un ETL
* kibana: monitorización y dashboard
* filebeat: recolección de logs. Esta funcionalidad antes estaba en logstash, y ahora se ha sacado de ahí. Logstash sigue manteniendo la misma funcionalidad, pero ya no se utiliza tanto.

Es por este motivo, por el de filebeat, por el que y.a no se denomina ELK stack, se llama elastic stack.

## Beats

 Existen varias aplicaciones de beats, pero todas comparten la misma filosofía recoger datos de un sistema y enviarlos a otro. De manera oficial, se ofrecen 7 beats:

* filebeat
* metricbeat
* packetbeat
* winlogbeat
* auditbeat
* heartbeat
* functionbeat

La forma de instalación, estructura básica y puesta en marcha es la misma para todos.

## Logstash

Logstash es una herramienta para la administración de logs. Esta herramienta se puede utilizar para recolectar, analizar (parsing) y guardar los logs para futuras búsquedas.2​ La aplicación está basada en jRuby y requiere de JVM para ejecutarse.

Logstash soporta un número de entradas, códecs, filtros y salidas. Las entradas son las fuentes de datos. Los códecs esencialmente convierten un formato de entrada en un formato aceptado por Logstash, así como también transforman del formato de Logstash al formato deseado de salida. Estos son utilizados comúnmente si la fuente de datos no es una línea de texto plano. Los filtros son acciones que se utilizan para procesar en los eventos y permiten modificarlos o eliminar eventos luego de ser procesados. Finalmente, las salidas son los destinos donde los datos procesados deben ser derivados

En Logstash y con una infraestructura distribuida, cada servidor web debe ser configurado para ejecutar Lumberjack (es opcional pero altamente recomendado para economizar recursos). Lumberjack hace un reenvío de los logs a un servidor corriendo Logstash con una entrada de Lumberjack

## elasticsearch

Elasticsearch es un servidor de búsqueda basado en Lucene. Provee un motor de búsqueda de texto completo, distribuido y con capacidad de multitenencia con una interfaz web RESTful y con documentos JSON

### Ventajas
  
Algunas de las más destacables son las siguientes:

* Al estar desarrollado en Java, es compatible en todas las plataformas donde Java lo sea.
* Tiene una gran velocidad de respuesta.
* Es distribuido, lo que lo hace fácilmente escalable y adaptable a las distintas situaciones.
* Simple realización de respaldos de los datos almacenados.
* Utiliza objetos JSON como respuesta, por lo que es fácil de invocar desde varios lenguajes de programación.

### Desventajas

* Sólo soporta como tipos de respuesta JSON, lo que lo limita al no soportar otros lenguajes, como CSV o XML.
* Algunas situaciones pueden generar casos de split-brain.

Elasticsearch puede ser usado para buscar todo tipo de documentos. La búsqueda es escalable y casi en tiempo real, soportando multi-tenencia.​ “Es distribuido, haciendo que los índices se puedan dividir en fragmentos y cada uno teniendo cero o más réplicas. Cada nodo alberga uno o más fragmentos, actuando como un coordinador para delegar operaciones a los fragmentos correctos. El rebalanceo y ruteo se realizan automáticamente ”.

Utiliza Lucene e intenta hacer todas sus funciones disponibles a través de JSON y Java API. Soporta facetado y percolación,​ que puede ser útil para notificar si nuevos documentos coinciden con consultas registradas.

Otra funcionalidad llamada "gateway" maneja la persistencia a largo plazo del índice;​ por ejemplo, se puede recuperar un índice del gateway en caso de una caída del servidor. Soporta peticiones GET en tiempo real y esto lo hace válido para una solución NoSQL,60​ pero carece de transacciones distribuidas.

### Kibana