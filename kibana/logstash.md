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

Permite hacer transformaciones los sobre los datos que recibe logstasth, o las transformaciones hechas en Grok. Por ejemplo, los valores numéricos, a pesar del parseo, seguirá siendo de cadena. Las casuísticas que más se utilizan son:

La forma de declarar el filtro es la siguiente:

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

  mutate{

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

A partir de aquí, igual que en Grok, se pueden ir declarando en su bloque las transformaciones requeridas.

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

  mutate{
    convert => {"duration" => "integer"}
    remove_field => {"message", "prospector", "input"}

    if ![type]{
      mutate => {
        add_field => {"type" => "JAVA_ERROR"}
      }

    if [type] == "LOGIN"{
        convert => {"status" => "integer"}
        add_field => {"login" => true}

        if [status] > 300{
          replace => {"login" => false} 
        }
      }      
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

Lo que se ha hecho en el bloque mutate:

```yml
  mutate{
    convert => {"duration" => "integer"}
    remove_field => {"message", "prospector", "input"}

    if ![type]{
      mutate => {
        add_field => {"type" => "JAVA_ERROR"}
      }

    if [type] == "LOGIN"{
        convert => {"status" => "integer"}
        add_field => {"login" => true}

        if [status] > 300{
          replace => {"login" => false} 
        }
      }      
    }
  }
}
}
```

Ha sido:

* convertir el campo duration a integer
* borrar el campo "message"
* si el evento no contiene el campo type, se añade un campo type con valor "JAVA_ERROR"
* si el evento contiene el campo type, y éste tiene el valor LOGIN:
  * se convierte el campo status a entero
  * se añade el campo login a true
* si el evento contiene el campo type, y éste no tiene el valor LOGIN:
  * se añade el campo login a false

Sin embargo, y aunque no se ha explicado en su sección, parte de la conversión de tipos cadena a entero que se está haciendo con mutate, se podría haber hecho en Grok, como se muestra en el ejemplo para los campos status y duration:

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
      "%{COMMON_LOG} - %{WORD:type}\|%{NUMBER:status:int}\|%{USERNAME:user}\|%{IP:origin}", 
      "%{COMMON_LOG} - %{WORD:type}\|%{NUMBER:duration:int}\|%{DATA:action}\|%{WORD:status}\|%{?<resto>.*}"
      ]
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

Con lo que finalmente quedaría:

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
      "%{COMMON_LOG} - %{WORD:type}\|%{NUMBER:status:float}\|%{USERNAME:user}\|%{IP:origin}", 
      "%{COMMON_LOG} - %{WORD:type}\|%{NUMBER:duration:float}\|%{DATA:action}\|%{WORD:status}\|%{?<resto>.*}"
      ]
    }
  } 

  mutate{
    remove_field => {"message", "prospector", "input"}

    if ![type]{
      mutate => {
        add_field => {"type" => "JAVA_ERROR"}
      }

    if [type] == "LOGIN"{
        add_field => {"login" => true}

        if [status] > 300{
          replace => {"login" => false} 
        }
      }      
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

### Date

Como es de esperar por su nombre, con este plugin lo que permite hacer es realizar el tratamiento de los campos fecha, para transformar de String a Date. Su uso es bastante sencillo:

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
      "%{COMMON_LOG} - %{WORD:type}\|%{NUMBER:status:float}\|%{USERNAME:user}\|%{IP:origin}", 
      "%{COMMON_LOG} - %{WORD:type}\|%{NUMBER:duration:float}\|%{DATA:action}\|%{WORD:status}\|%{?<resto>.*}"
      ]
    }
  } 

  mutate{
    remove_field => {"message", "prospector", "input"}
    rename => {"@timestamp", "logstashProcessTime", "input"}

    if ![type]{
      mutate => {
        add_field => {"type" => "JAVA_ERROR"}
      }

    if [type] == "LOGIN"{
        add_field => {"login" => true}

        if [status] > 300{
          replace => {"login" => false} 
        }
      }      
    }
  }

  date
    match => ["date", "yyyy-MM-dd HH:mm:ss"]
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

Lo que se ha hecho en el ejemplo es modificar el campo @timestamp por logstashProcessTime (que no tiene que ver con el plugin date), y se ha transformado el valor del campo date a un formato fecha. Además el valor del campo date pasará a ser el nuevo valor de @timestamp

Hay dos transformaciones bastante importantes que debemos tener en cuenta si se trabaja en una aplicación multi-idioma y multi-region, locale y timezone.

### Translate

Este plugin no tiene que ver con el idioma, se refiere a la transformación de determinados valores estándar a sus descripciones:

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
      "%{COMMON_LOG} - %{WORD:type}\|%{NUMBER:status:float}\|%{USERNAME:user}\|%{IP:origin}", 
      "%{COMMON_LOG} - %{WORD:type}\|%{NUMBER:duration:float}\|%{DATA:action}\|%{WORD:status}\|%{?<resto>.*}"
      ]
    }
  } 

  mutate{
    remove_field => {"message", "prospector", "input"}
    rename => {"@timestamp", "logstashProcessTime", "input"}

    if ![type]{
      mutate => {
        add_field => {"type" => "JAVA_ERROR"}
      }

    if [type] == "LOGIN"{
        add_field => {"login" => true}

        if [status] > 300{
          mutate{
            replace => {"login" => false} 
          }
        }

        translate {
          field => "status"
          destination => "statusText"
          dictionary => {
             "200" => "Login Correcto"
             "201" => "Login correcto tras varios intentos"
             "202" => "Login con actualización de password"
             "204" => "Login automático"
             "250" => "Login recordado"
             "100" => "Usuario suplantado"
             "150" => "usuario suplantado automáticamente"
          }
        }
      }      
    }
  }

  date
    match => ["date", "yyyy-MM-dd HH:mm:ss"]
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

Del código translate generado tenemos:

* La sección se ha metido dentro de la sección condicional para que solo se utilice en esa situación
* le indicamos el campo al que debe aplicarse mediante "filed"
* Le indicamos el nuevo campo en el que queremos aplicar el resultado
* Le indicamos el diccionario de términos que debe utilizar

En cuanto al diccionario, se pueden utilizar ficheros, así como diccionarios de terceros.

### Geoip

Se trata de un plugin orientado a extraer la información de geolocoalización asociada a una ip. Dispone de varios campos settings interesantes como:

* target para indicar el campo en el que queremos mostrar la información de geolocalización, aunqeu por defecto lo hace en el campo geoip
* fields. Para indicar qué información de ip queremos mostrar, aunque por defecto mostrará toda.

Por defecto el campo des que queremos obtener la información, se indica mediante source.


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
      "%{COMMON_LOG} - %{WORD:type}\|%{NUMBER:status:float}\|%{USERNAME:user}\|%{IP:origin}", 
      "%{COMMON_LOG} - %{WORD:type}\|%{NUMBER:duration:float}\|%{DATA:action}\|%{WORD:status}\|%{?<resto>.*}"
      ]
    }
  } 

  mutate{
    remove_field => {"message", "prospector", "input"}
    rename => {"@timestamp", "logstashProcessTime", "input"}

    if ![type]{
      mutate => {
        add_field => {"type" => "JAVA_ERROR"}
      }

    if [type] == "LOGIN"{
        add_field => {"login" => true}

        if [status] > 300{
          mutate{
            replace => {"login" => false} 
          }
        }

        translate {
          field => "status"
          destination => "statusText"
          dictionary => {
             "200" => "Login Correcto"
             "201" => "Login correcto tras varios intentos"
             "202" => "Login con actualización de password"
             "204" => "Login automático"
             "250" => "Login recordado"
             "100" => "Usuario suplantado"
             "150" => "usuario suplantado automáticamente"
          }
        }
      }      
    }
  }

  date
    match => ["date", "yyyy-MM-dd HH:mm:ss"]
  }

  geoip{
    source => "ip"
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

### Ruby

Probablemente sea el plugin más poderoso de logstash. Al estar éste escrito en ruby, lo que permite el plugin es escribir scripts Ruby para indicar comportamientos, con el riesgo inherente que ello conlleva.

### Otros filtros

* Aggregate Filter. Permite aggregar varios eventos en uno.
* Clone. Para clonar eventos
* CSV. Para cuando los eventos llegan en formato csv o parte de ellos. Habría que hacer una transformación Grok previa.
* json. Para cuando nuestros eventos son json o parte es json. Habría que hacer una transformación Grok previa.
* dissect. Es una versión de Grok basada en separadores, por ejemplo por guiones. Su rendimiento es mucho mejor, pero es menos potente porque no se pueden aplicar expresiones regulares.
* drop. Se puede utilizar para descartar determinados eventos, independientemente de los filtros previos que se hayan hecho. Nunca se enviará a la salida.
* split. Al contrario que el aggregate, que permite generar varios eventos a partir de un único evento. Por ejemplo si éste fuese un array, se podría genera un evento por cada posición del array.

## Output

La salida estándar de logstash es el stdout, que para propósitos de debug es muy adecuada, pero no es la única. Otro uso para el que supone un caso de uso igual de adecuado es para mostrar por consola los errores de parseo de Grok:

```yml

output {
  if "_grokparsefailure" in [tags] {
    stdout{}
  }
}
```

Pero incluso esta salida se puede personalizar para que no esté sujeta solo a Grok, sino que se genere siempre que se produzca un fallo en cualquiera de los plugins.

### Elasticsearch

Ésta, suele ser la salida más habitual para logstash. Se puede configurar aproverchando la sección anterior, de forma que si hay algún error se envía a la salida estándar, y si no, se envía a elasticsearh.

```yml

output {
  if "_grokparsefailure" not in [tags] {
    elasticsearch {
      hosts => ["http://elasticsearch:9200"]
      user => "elastic"
      password => "changeme"
      index => "%{[@metadata][beat]}-%{[@metadata][version]}-%{+YYYY.MM.dd}"
    }    
  }else{
    stdout()
  }
}
```

Los principales aspectos que se deben tener en cuenta en la configuración de la salida de elastic, es el host de destino, que habitualmente será distinto al de logstash, y los índices en los que se deben guardar la información. Lo ideal es que exista un índice por aplicación, y configurarlo para que rote, ya que si no, se crearán índices muy grandes y difíciles de manejar.

### Otros plugins de salida

Como se indicó en la introducción existen multitud de plugins para fuentes de salida, que es lo que permiten que logstash pueda funcionar prácticamente como un ETL. Por categorías podemos encontrar plugins para:

* BBDD (en memoria como redis, mongodb)
* colas (kafka, redis, rabbitmq)
* monitorización (naggios, influxdb, ganglia, gelf)
* métricas
* mensajería
* etc.

## Combinación de entradas y salidas

La primera práctica indicada para la combinación de entradas y salidas, es crear una carpeta en la que se escribirán los diferentes ficheros de configuración para las posibles entradas y salidas.

A continuación, en lugar de arrancar logstash con el parámetro -f nombre_fichero, podemos arrancarlo con -f ruta_directorio. De esta forma arrancará las dos configuraciones.

En cuanto al funcionamiento, es algo más delicado, ya que lo que hará Logstash será un join de las secciones input, filter y output de todos los ficheros. Así que será necesario añadir secciones condicionales para que solo aplique cada sección a los eventos que deseemos. La forma de añadir estas secciones sera añadiendo condiciones siguiendo los siguientes pasos:

* Input: partiendo de la entrada, sección input, añadir variables a cada plugin que permitan saber desde que fuente se ha procesado el evento. Se hará por medio de add_field =>  {"nombre_campo" => "valor1"} que tendrá que ser distinto entre entradas, por ejemplo add_field => {"nombre_campo => "valor2"}
* Filter: se añadirán secciones condicionales del tipo if [nombre_campo] == "valorX" {filtros}
* Output: se añadirán secciones condicionales del tipo if [nombre_campo] == "valorX" {plugin}

Si se quiere todavía más flexibilidad, se pueden crear ficheros plugins de entrada, plugins de salida, y filtros, después tener otros ficheros en los que se utilicen.

## Monitorización

Logstash se puede monitorizar de dos formas:

* API: Logstash al arrancar expone un API en el puerto 9600. Este API, presenta un problema, y es que si se repiden plugins o filtros a menos que los identifiquemos por medio de id => "nombre_identificador" no se puede identificar cada uno de los elementos. Una vez resuelto, se irán almacenando métricas de cada uno de ellos que se pueden consutlar por el API.
  * Cada uno de los Plugins se puede configurar de forma personalizada, o se puede omitir si no queremos obtener métricas suyas.
  
* Kibana. Esta monitorización en primer lugar requiere ser configurada en el yml de logstash. Viene preconfigurada para xpack, lo que hay que hacer es:
  * descomentar la línea de habilitamiento
  * configurar la línea de url para hacer que apunte a nuestro elasticsearch
  * habilitar la monitorización de pipelines:

```yml
xpack.monitoring.enabled: false
xpack.monitoring.elasticsearch.hosts: ["http://elasticsearch:9200"]
xpack.monitoring.collection.pipeline.details.enabled: true
```

## Configuración avanzada

Además del fichero logstash.yml donde se pueden configurar multitud de aspectos, existe otro fichero, startup.options en el que se pueden configurar otros aspectos del funcionamiento del proceso logstash, así como la máquina virtual. Encontramos aspectos tales como:

* carpeta de arranqeu por defecto
* ubibación del fichero yaml
* prioridad del proceso en el sistema operativo
* número de ficheros permitidos
* Heap de la máquina virtual

Aunque en téminos generales, esta configuración susele ser bastante acertada y no hace falta modificarlo.

En cuanto al logstash.yml, es el fichero en el que se pueden configurar todos los aspectos de funcionamiento de logstash. En general, muchos de ellos vienen preconfigurados, y solo hace falta descomentarlos. Entre ellos hay algunos que merece la pena activar como:

```yml
# número de hilos (cores de la máquinma) que se van a utilizar.
pipeline.workers: 2

# número de eventos que se pueden recuperar antes de empezar a filtrar. 
pipeline.batch.size: 125

#tiempo de espera máximo para procesar el contenido de memoria. Se complementa con el anterior, el que antes llegue a su topo, tiempo o eventos.
pipeline.batch.delay: 50
```

El número de hilos (workers) se utilizan tanto pora entradas, filtros y salidas. Como los de salida pueden quedarse en espera de las respuestas de destino, hay veces que puede ser conveniente poner un número igual al de cores o incluso superior, porque no siempre van a a estar todos trabajando.

En cuanto al número de eventos, dependerá de la memoria y CPU con la que se cuente, ya que cuantos más almacenemos, menos trabajo mandaremos a la CPU para su procesamiento. Sin embargo, si tenemos poca memoria, puede llegar interesarnos mandar más trabajo a la CPU y utilizar menos memoria.

Para ayudarnos en este tipo de configuraciones, nos podemos apoyar en la monitorización del sistema que hace Kibana, que nos ayuda a ver dónde se encuentran los problemas de nuestra máquina. ¡

Hay otras características que pueden activarse como la carga automática de la configuración, cada cuanto cargarla, aspectos de configuración, nivel de log que queremos que deje, o de la cola logstash en la que se almacenan los inputs antes de ser procesados. Por defecto es una cola basada en memoria, pero podemos configurar la para que sea persistente, que aunque es más lenta, es más segura.

## Pipelines

Los pipelines permiten tener distintos flujos (configuraciones) corriendo en Logstash. Estas configuraciones independientes, permiten que Logstash les asigne recursos de forma independiente, pudiendo balancearlos en función del que más los necesite.

Es muy útil cuando no se comparten plugins o filtros, porque son configuraciones independientes. La configuración por defecto de los pipelines se puede encontrar en el fichero pipelines.yml

Si se opta por configrar la aplicación por medio de este fichero, no es necesario utilizar el parámetro -f para indicar el fichero de configuración, ya que leerá el pipelines.yml donde ya se le indica la fuente de los recursos de configuración.