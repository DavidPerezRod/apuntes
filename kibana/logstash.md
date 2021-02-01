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

## Input

La fuente de entrada es lo primero que se necesita definir. En este caso la entrada va a ser Beats, aunque no es la única, ya que soporta muchas entradas. Además, la salida que con la que se va a trabjar en esta documentación, es Elasticsearch. Al conjunto de acciones que se producen desde la entrada hasta la salida, pasando por los filtros y los codec, es lo que se conoce com pipeline.

### Stdin

Recoge todo lo que se le indique por la entrada estándar. En general no se utiliza mucho, salvo para pruebas de testing. Por ejemplo, es útil para probar los codecs.

Por ejemplo, en la configuración básica de test, utilicé:

```yml
input {
  stdin {
    codec => multiline {
      pattern => "fin"
      negate => "true"
      what => "next"
    }
  }
}

output {
  stdout {
      #codec => json_lines
  }
}
```

De la configuración anterior, es importante destacar la forma en la que actúa el codec. Es similar a la vista para Filebeat. El patrón es el identificador a partir del cual construir la estructura de salida, negate, si lo que buscamos es que aparezca la expresión o que no aparezca. Y por último what, cuyo valor puede ser previous o next, dependiendo de si se tiene que unir al evento anterior o al posterior.

En el ejemplo, todas las líneas que no sean fin, se unirán a un evento único que se lanzará cuando detecte la palabra fin.

Otro codec muy útil es el codec json

```yml
input {
  stdin {
    codec => json

  }
}

output {
  stdout {
      #codec => json_lines
  }
}
```

Que agrega los campos del json a la estructura nativa logstash. La diferencia entre indicar este codec o no hacerlo, es que si la entrada es un json, pero no se utiliza el codec, lo procesará como si fuese una única línea, y sería necesario procesarlo en la fase de filtrado.

### File

Antes de la existencia de Filebeat, se utilizaba para recolectar los logs de las aplicaciones. Sin embargo, siguen existiendo muchas arquitecturas que siguen utilizándolo.

En logstas, se pueden utilizar diferentes configuraciones, por lo que voy a anexar la configuración file a la previa de stdin.

```yml
input {
  stdin {
    codec => json
  }

  file {
      path => "/Users/usuario/carpeta_logs/patron_log.log
      exclude => "*.gz"
      start_position => "beginning"
      sincedb_path => ""/Users/usuario/carpeta_logs/sincedb"

      codec => multiline {
        pattern => "fin"
        negate => "true"
        what => "next"
    }

  }

}

output {
  stdout {
      #codec => json_lines
  }
}
```

El codec file:

* no permite utilizar rutas dinámicas, solo estáticas. Aunque esto a la hora de recolectar logs, no suele ser un problema, ya que los logs se suelen dejar en una ubicación estática.
* Se pueden excluir tipos de fichero con exclude.
* start_position. Posición desde la que se quiere empezar a leer el/los ficheros.
* sincedb_path. Para lecturas sucesivas del fichero, si se produce un apagado de logstash. Retoma la lectura desde la última posición leída.

Al igual que ocurre con la entrada estándar, por defecto este tipo de entrada no agrupa líneas, así qeu será necesario configurar el filtro. Al igual que en la entrada estándar el codec que hay que utiliszar es el multiline

```yml
input {
  stdin {
    codec => json
  }

  file {
      path => "/Users/usuario/carpeta_logs/patron_log.log
      exclude => "*.gz"
      start_position => "beginning"
      sincedb_path => ""/Users/usuario/carpeta_logs/sincedb"

      codec => multiline {
        pattern => "^(DEBUG|INFO|ERROR|TRACE|FATAL).*"
        negate => "true"
        what => "previous"
    }

  }

}

output {
  stdout {
      #codec => json_lines
  }
}
```

### Beats

Actualmente se trata prácticamente del estandar para trabajar con logstash. En la configuración podemos encontrar los siguientes valores:

* port -> puerto en el que va a estar escuchando
* host -> hosts desde los que se van a procesar peticiones. Por defecto 0.0.0.0 permite recibir desde cualquier host.

También se pueden añadir configuraciones más específicas de conexión SSL y cifrado.

## Filtros

### Grok

#### Parseo

La idea es fragmentar las líneas mensaje de entrada en las secciones conlas que querremos trabajar posteriormente. Es probablemente la herramienta que más recursos requiere, así que lo ideal es que los logs lleguen en json para evitar hacer transformaciones.

Para empezar a trabajar en el filtro, se debe añadir la sección filter al fichero de configuración, y es en esa sección en la que se declara grok como filtro:

```yml
input {
  beats {
    port => 5044
    codec => multiline {
      pattern => ""
    }
  }
}

filter{
  grok{

  } 
}

output {
  elasticsearch {
    hosts => ["http://elasticsearch:9200"]
    user => "elastic"
    password => "changeme"
    index => "%{[@metadata][beat]}-%{[@metadata][version]}"
  }
}
```

A partir de aquí, hay que indicarle varias cosas:

* match: es la propiedad que sirve para indicarle la expresión que debe encontrar. Además hay que tener en cuenta que cuando se trabaja con beats, éste envía el contenido del mensaje en el campo message. Así pues se declara el tipo de filtro, y la expresión match, compuesta por campo y expresión en formato json.

Para facilitar el trabajo de filtrado con grok, kibana entre sus herramientas de la sección "Dev Tools" tiene un debugger de grok. También hay otras herramientas en red como grokdebug que incluye alguna otra utilidad porque aporta sugerencias de patrones genéricos.

Sin embargo, este tipo de patrones no son de uso obligatorio, ya que se pueden utilizar expresiones regulares genéricas

##### Patrones Grok

La sintaxis de un patrón grok es %{PATTERN: key} donde pattern es la expresión regular y key es el nombre que le vamos a dar al campo en la salida

```yml
input {
  beats {
    port => 5044
  }
}

filter{
  grok{
    match => {"message" => "%{WORD:level} %{TIMESTAMP_ISO8601:date} \[%{WORD:thread}\] %{JAVACLASS:class} - %{WORD:type}\|%{NUMBER:status}\|%{USERNAME:user}\|%{IP:origin}"}
  } 
}

output {
  elasticsearch {
    hosts => ["http://elasticsearch:9200"]
    user => "elastic"
    password => "changeme"
    index => "%{[@metadata][beat]}-%{[@metadata][version]}"
  }
}
```

El patrón anterior sirve para separar trazas de log del tipo:

INFO 2021-01-30 16:49:19 [main] a.b.t.loggenerator.LogGenerator - LOGIN|250|Zipi|236.240.241.84

Sin embargo, rara vez servirá indicar un único patrón para el filtrado, ya que por ejemplo, son el patrón anterior, si tuviésemos un otra traza de log del tipo INFO 2021-01-30 16:49:28 [main] a.b.t.loggenerator.LogGenerator - PERFORMANCE|0.027|SEND EMAIL|FAILURE|Pantuflo, no nos serviría, necesitaríamos una expresión regular del tipo: %{WORD:level} %{TIMESTAMP_ISO8601:date} \[%{WORD:thread}\] %{JAVACLASS:class} - %{WORD:type}\|%{NUMBER:duration}\|%{DATA:action}\|%{WORD:status}\|%{USERNAME:user}. 

La forma de declarar un array de expresiones es la siguiente:

```yml
input {
  beats {
    port => 5044
  }
}

filter{
  grok{
    match => {"message" => [
      "%{WORD:level} %{TIMESTAMP_ISO8601:date} \[%{WORD:thread}\] %{JAVACLASS:class} - %{WORD:type}\|%{NUMBER:status}\|%{USERNAME:user}\|%{IP:origin}", 
      "%{WORD:level} %{TIMESTAMP_ISO8601:date} \[%{WORD:thread}\] %{JAVACLASS:class} - %{WORD:type}\|%{NUMBER:duration}\|%{DATA:action}\|%{WORD:status}\|%{USERNAME:user}"
      ]
    }
  } 
}

output {
  elasticsearch {
    hosts => ["http://elasticsearch:9200"]
    user => "elastic"
    password => "changeme"
    index => "%{[@metadata][beat]}-%{[@metadata][version]}"
  }
}
```

Una característica de Grok que se debe tener en cuenta, es que por defecto no identifica como una única línea de log, aquellas que presentan saltos de línea. Este aspecto es importante tenerlo en cuenta cuando se quieren procesar excepciones. Para indicarle este hecho, antes del patrón hay que indicárselo mediante (?m). De forma que se tendría algo similar a:

```yml
input {
  beats {
    port => 5044
  }
}

filter{
  grok{
    match => {"message" => [
      "%{WORD:level} %{TIMESTAMP_ISO8601:date} \[%{WORD:thread}\] %{JAVACLASS:class} - %{WORD:type}\|%{NUMBER:status}\|%{USERNAME:user}\|%{IP:origin}", 
      "(?m)%{WORD:level} %{TIMESTAMP_ISO8601:date} \[%{WORD:thread}\] %{JAVACLASS:class} - %{WORD:type}\|%{NUMBER:duration}\|%{DATA:action}\|%{WORD:status}\|%{USERNAME:user}"
      ]
    }
  } 
}

output {
  elasticsearch {
    hosts => ["http://elasticsearch:9200"]
    user => "elastic"
    password => "changeme"
    index => "%{[@metadata][beat]}-%{[@metadata][version]}"
  }
}
```

Aunque el patrón indicado no serviría para identificar logs de excepciones, se ha utilizado de ejemplo para mostrar el aspecto que tendría.

En caso de definir un patrón que no aplicase a nada, durante el procesamiento se producirán errores de tipo "Grok parese failure"

##### Expresiones regulares

Logstash permite mezclar los dos tipos de expresiones, tanto las expresiones regulares como las expresiones Grok en un solo patrón. Sin embargo, la forma en que se expresan es distinta. 

Las expresiones reguarles dse expresan entre paréntesis, cambiando el orden, en lugar de expresión-nombre es nombre-expresión:

(?<nombre_campo>expresion_regular)

```yml
input {
  beats {
    port => 5044
  }
}

filter{
  grok{
    match => {"message" => [
      "%{WORD:level} %{TIMESTAMP_ISO8601:date} \[%{WORD:thread}\] %{JAVACLASS:class} - %{WORD:type}\|%{NUMBER:status}\|%{USERNAME:user}\|%{IP:origin}", 
      "(?m)%{WORD:level} %{TIMESTAMP_ISO8601:date} \[%{WORD:thread}\] %{JAVACLASS:class} - %{WORD:type}\|%{NUMBER:duration}\|%{DATA:action}\|%{WORD:status}\|%{?<resto>.*}"
      ]
    }
  } 
}

output {
  elasticsearch {
    hosts => ["http://elasticsearch:9200"]
    user => "elastic"
    password => "changeme"
    index => "%{[@metadata][beat]}-%{[@metadata][version]}"
  }
}
```

Sin embargo, por motivos de claridad, es conveniente utilizar los patrones predefinidos Grok, y utilizar las expresiones regulares únicamente para personalizar alguna expresión para la que no exista el equivalente Grok.

Aunque aún en este caso, tampoco se suele trabajar de esta forma, lo que se hace es declarar un nuevo patrón Grok para poder reutilizarlo en otras expresiones. La forma de declarar un patrón en un fichero de configuración es la siguiente:

```yml
input {
  beats {
    port => 5044
  }
}

filter{
  grok{
    pattern_dir => "patterns_directory"
    match => {"message" => [
      "%{WORD:level} %{TIMESTAMP_ISO8601:date} \[%{WORD:thread}\] %{JAVACLASS:class} - %{WORD:type}\|%{NUMBER:status}\|%{USERNAME:user}\|%{IP:origin}", 
      "(?m)%{WORD:level} %{TIMESTAMP_ISO8601:date} \[%{WORD:thread}\] %{JAVACLASS:class} - %{WORD:type}\|%{NUMBER:duration}\|%{DATA:action}\|%{WORD:status}\|%{?<resto>.*}"
      ]
    }
  } 
}

output {
  elasticsearch {
    hosts => ["http://elasticsearch:9200"]
    user => "elastic"
    password => "changeme"
    index => "%{[@metadata][beat]}-%{[@metadata][version]}"
  }
}
```

En el directorio de patrones, declararíamos un fichero de texto con el patrón. Un patrón por línea con la estructura:

NOMBRE_PATRON EXPRESIÓN

Por ejemplo:

COMMON_LOG %{WORD:level} %{TIMESTAMP_ISO8601:date} \[%{WORD:thread}\] %{JAVACLASS:class} - %{WORD:type}\|%{NUMBER:status}\|%{USERNAME:user}\|%{IP:origin}"

Pueden exisiter de uno a n ficheros con uno a n patrones.

La forma de incluir un patrón declarado en un fichero, como parte de la expresión regular, quedaría:

```yml
input {
  beats {
    port => 5044
  }
}

filter{
  grok{
    patterns_dir => "patterns_directory"
    match => {"message" => [
      "%{COMMON_LOG} - %{WORD:type}\|%{NUMBER:status}\|%{USERNAME:user}\|%{IP:origin}", 
      "%{COMMON_LOG} - %{WORD:type}\|%{NUMBER:duration}\|%{DATA:action}\|%{WORD:status}\|%{?<resto>.*}"
      ]
    }
  } 
}

output {
  elasticsearch {
    hosts => ["http://elasticsearch:9200"]
    user => "elastic"
    password => "changeme"
    index => "%{[@metadata][beat]}-%{[@metadata][version]}"
  }
}
```

De esta forma es más legible mantenible.

### Mutate

Permite hacer transformaciones los sobre los datos que recibe logstasth. 