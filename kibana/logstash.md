# Introducción

Es la herramienta del stack centrada en el tratamiento de logs. Con logstach, lo primero que se necesita es una fuente de datos. Se trata de una especie de ETL par logs. Aunque en principio lo vamos a utiizar con beats, lo cierto es que no necesariamente se ciñe a entradas de beats. Entre las entradas que soporta encontramos:

* logs
* redis
* elasticsearch
* mail
* RabbitMQ
* Kafka
* Amazon Web Services
* BBDD

Pero no es una lista exclusiva, es solo una muestra representativa de todas las entradas que soporta.

El tipo de información que recibe, es información sin procesra. Y es Logstach el que coge el mensaje y lo descompone para generar una nueva salida, con la información enriqucida. Estas fuentes de salida, al igual que ocurre con las de entrada, pueden ser muy diversas:

* elasticsearch
* logs
* redis
* Nagios
* BBDD
* mail
* RabbitMq
* Kafka
* Amazon Web Services

Desde el punto de vista interno, en logstash se distinguen tres etapas:

* input
* filter (todos los filtros que se nos ocurran, campos de entrada, nuevos campos de salida o procesmiento intermedio)
* output

Además hay otro concepto importante, el de codec. Que se refiere al formato en el que se quieren recibir o enviar los datos, json, multilne, etc. En el caso de algunas tecnologías de entrada o salida, no hace falta utilizar codecs, porque logstac conoce la sintaxis de la herramienta con la que se va a conectar.

A la cadena input(codec)+filter+output(codec) es a lo que se conoce como pipeline. Hasta hace poco, solo logstash solo podía trabajar con un pipeline, pero ahora se puede trabajar con varios pipeline diferentes, de forma que si alguna de las entradas salidas falla, no afecte al resto de los pipelines. Antes, la forma de solucionarlo era mediante varios procesos logstash cada uno con su pipeline.

## Plugin de entrada stdin