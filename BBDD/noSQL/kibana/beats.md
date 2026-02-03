# Beats

## MetricBeat

Es un proceso ligero pensado para enviar estadísticas de aplicaciones. Inicialmente se utilizaba para sacar métrcas del sistema, así que está pensado para ayudar en la monitorización de servidores. Alkgunos de los sistemas más habituales en los que se puede integrar son:

* Apache
* HAProxy
* MongoDB
* MySQL
* Nginx
* PostgreSQL
* Redis
* System
* Zookeeper

Su salida puede mandarse directamente a elasticsearch, logstash, redis o kafka. Además para el caso de kibana ya tiene una serie de dashboard por defecto, que pueden ser activados en el fichero yml de configuración.

Con el comando: ./metricbeat modules list, se pueden lisatar todos los módulos disponibles, y cuáles de ellos están activos. Por defecto están a configurados com disabled, pero podemos activarlos mediante: ./metricbeat modules enable elasticsearch. Cada uno de estos ficheros contiene los detalles de monitorización.

Una vez cambiada la configuración se puede probar mediante: ./metricbeat test config -c fichero.yml

## PacketBeat

Esta orientado a la monitorización en tiempo real del tráfico de red de nuestro servidor. Es útil para auditar la cominucación entre loshost de la red. Trabaja decodificando el protocolo de capa de aplicación (HTTP, MySQL, Redis, etc) correlacionando peticiones y respuestas, y registrando los campos más relevantes de las transacciones. La información la puede escribir directamente a elasticsearch o bien a una cola con Redis o Logstash.

Los principales protocolos soportados son:

* ICMP (v4 and v6)
* DHCP (v4)
* DNS
* HTTP
* AMQP 0.9.1
* Cassandra
* Mysql
* PostgreSQL
* Redis
* Thrift-RPC
* MongoDB
* Memcache
* NFS
* TLS
* SIP/SDP (beta)

Packetbeat puede ejecutarse en los mismos servidores que los procesos a monitorizar o en sus propios servidores. Cuando se ejecuta en servidores dedicados, Packetbeat puede obtener el tráfico de los puertos espejo. Esta configuración es buena si no se quiere incurrir en gastos adicionales de la aplicación monitorizada.

Por medio del comando ./packetbeat devices, podremos conocer todas las interfaces de red del host.

## FileBeat

Se trata de un proceso ligero par la centralización y distribución de logs. Se instala como agente en los servidores, y se le indica los ficheros de logs o ubicaciones de las que debe recolectarlos, y enviarlos a elasticsearch o logstash para su indexación. Se divide en dos etapas fundamentales, la de recolección, se activa uno por cada fichero de log. Y el de agregación de la informacíon, como paso previo al envío a su destino final.

### outputs

Filebeat cuanta con varias salidas preconfiguradas en su yml. Entre ellas las más relevantes:

* elasticsearch: si se pueden mandar directamente los logs a la BBDD. Esta es la salida por defecto.
* logstash: si es necesario hacer transformaciones en los fichero de log
* salida estándar

```yml
#output.elasticsearch:
  # Boolean flag to enable or disable the output module.
  #enabled: true

  # Array of hosts to connect to.
  # Scheme and port can be left out and will be set to the default (http and 9200)
  # In case you specify and additional path, the scheme is required: http://localhost:9200/path
  # IPv6 addresses should always be defined as: https://[2001:db8::1]:9200
 # hosts: ["localhost:9200"]

#output.console:
#  pretty: true

output.logstash:
  # Boolean flag to enable or disable the output module.
  #enabled: true

  # The Logstash hosts
  hosts: ["logstash:5044"]

```

Ademaś, filebeat también permite trabajar con una configuración de salida en cluster, en la que se puede indicar un array de host entre los corchetes, además de activar la propiedad loadbalance a true.

```yml

output.logstash:
  # Boolean flag to enable or disable the output module.
  #enabled: true

  # The Logstash hosts
  hosts: ["logstash1:5044", "logstash2:5044"]
  loadbalance: true
```

Filebeat también admite la posibilidad de configurar un balanceador de carga como host de salida, pero es menos recomendable, porque mientras que con la anterior se ejecuta un round robin, con el balanceador, Filebeat suele quedarse con el primer host que éste le proporcione.

Otra propiedad que se puede configurar en la salida, es el número de pipelines. Cada pipeline representa un envío para el que no hay que esperar la respuesta antes de enviar otro. De forma que si por ejemplo hay cuatro pipelines, Filebeat podría tener hasta cuatro envíos abiertos esperando la respuesta.
Por defecto, solo se puede enviar un paquete de información por vez.

```yml
output.logstash:
  # Boolean flag to enable or disable the output module.
  #enabled: true

  # The Logstash hosts
  hosts: ["logstash1:5044", "logstash2:5044"]
  pipelining: 2
```

Otra configuración relacionada con ésta, es el número de workers, que representa el número de hilos que se pueden tener abiertos simultáneamente.

```yml
output.logstash:
  # Boolean flag to enable or disable the output module.
  #enabled: true

  # The Logstash hosts
  hosts: ["logstash1:5044", "logstash2:5044"]
  pipelining: 2
  workers: 4
```

Sin embargo, hay que tener mucho cuidado con estas dos configuraciones, ya que Filebeat corre en la misma máquina en la que se ejecuta el proceso principal del que se quieren enviar los logs, de forma que cuanto mayores sean los recursos que le demos a Filebeat, menores serán los recursos de los que pueda disfrutar la 
aplicación.

También relacionado con el rendimiento, aunque muy útil, es la configuración de filtros. Ya que todo trabajo de filtrado en los logs, previo a enviar la información a la salida, requerirá un procesamiento y por lo tanto, recursos y retardo en el envío de logs. Aunque es cierto que por otro lado evitamos tráfico de red.

No hay una fórmula óptima, siempre va a depender de las características de la red y de las máquinas con las que estamos trabajando. Por supuesto, si todas las máquinas son muy potentes, cuando antes filtremos la información para evitar tráfico de red, mejor.

### Filtros

 Se trata de una utilidad muy importante a la hora de seleccionar qué tipo de trazas queremos que lleguen a logstash o elasticsearch. Por ejemplo si lo que se está enviando son trazas de log, en entornos productivos, no querremos que lleguen las de tipo debug.

 Filebeat, permite configurar los filtros de forma inclusiva, indicando cuáles queremos que viajen, o exclusivas, indicando qué trazas no queremos enviar:

```yml
exclude_lines: ['^DEBUG']
```

Otro aspecto a tener en cuenta de los filtros, es la recolección multilinea, por ejemplo con java o c, las excepciones son multilinea. De forma natural, filebeat va a crear un evento por línea. Para el caso de las excepciones, este comportamiento no es el más indicado, ya que perderemos el conjunto de la información, imprescindible para poder interpretar el error de forma adecuada.

Filebeat ofrece la posibilidad de configurar este comportamiento mediante las opciones multilínea. Necesitaremos identificar el patrón común para las líneas que conforman el bloque multilínea. Por ejemplo, para filtrar el bloque de excepción del siguiente extracto:

```log
INFO 2021-01-25 08:24:00 [main] a.b.t.loggenerator.LogGenerator - PERFORMANCE|2.843|CREATE USER|FAILURE|Zipi
DEBUG 2021-01-25 08:24:00 [main] a.b.t.loggenerator.LogGenerator - Creating Login Trace
INFO 2021-01-25 08:24:00 [main] a.b.t.loggenerator.LogGenerator - LOGIN|204|Superman|173.1.41.65
DEBUG 2021-01-25 08:24:00 [main] a.b.t.loggenerator.LogGenerator - Creating Performance Trace
INFO 2021-01-25 08:24:00 [main] a.b.t.loggenerator.LogGenerator - PERFORMANCE|2.999|LOGIN|SUCCESS|Mortadelo
DEBUG 2021-01-25 08:24:00 [main] a.b.t.loggenerator.LogGenerator - Creating Error Trace
ERROR 2021-01-25 08:24:00 [main] a.b.t.loggenerator.LogGenerator - User can not be null. Can not fetch information from the database|
java.lang.Exception: User can not be null. Can not fetch information from the database
	at am.ballesteros.training.loggenerator.LogGeneratorKt.generateExceptionTrace(LogGenerator.kt:66)
	at am.ballesteros.training.loggenerator.LogGeneratorKt.generateTraces(LogGenerator.kt:31)
	at am.ballesteros.training.loggenerator.LogGeneratorKt$main$1.invoke(LogGenerator.kt:17)
	at am.ballesteros.training.loggenerator.LogGeneratorKt$main$1.invoke(LogGenerator.kt)
	at com.xenomachina.argparser.SystemExitExceptionKt.mainBody(SystemExitException.kt:74)
	at com.xenomachina.argparser.SystemExitExceptionKt.mainBody$default(SystemExitException.kt:72)
	at am.ballesteros.training.loggenerator.LogGeneratorKt.main(LogGenerator.kt:14)
```

Se observa que la mejor estrategia, es utilizar con un bloque las líneas que no van precedidas de ningún tipo de nivel de traza. Para esto Filebeat da el soporte multilínea (sección ### Multiline options del fichero de configuración).

Hay que indicarle el patrón con el que se inician las líneas que quieren agruparse. En el ejemplo, todo lo que no se inicie por con el nivel de traza, es un evento que hay  que adjuntarlo a un evento, previo o posterior. De nuevo, en el caso del ejemplo, a la línea anterior.

```yml
# The regexp Pattern that has to be matched. The example pattern matches all lines starting with 
multiline.pattern: ^(DEBUG|INFO|ERROR|TRACE|FATAL).*
```

También hay que indicar si es una expresión negada o no. En el ejemplo sí.

```yml
multiline.pattern: ^(DEBUG|INFO|ERROR|TRACE|FATAL).*
multiline.negate: true
```

Y lo último, es indicar a que evento hay que unirlo, lo configuraremos al evento previo.

```yml
multiline.pattern: ^(DEBUG|INFO|ERROR|TRACE|FATAL).*
multiline.negate: true
multiline.match: after #el último que ha creado la coincidencia
```

### Módulos preconfigurados

Además de la opción anterior en la que la configuración se hace manualmente, existen módulos prefonfigurados. Los módulos preconfigurados se pueden explorar mediante el comando ./filebeat modules list

Si se produce un error de tipo: "Error in modules manager: modules management requires 'filebeat.config.modules.path' setting", será necesario activar en el beats de configuración el procesamiento de módulos:

```yml
# Enable filebeat config reloading
filebeat.config:
  #inputs:
    #enabled: false
    #path: inputs.d/*.yml
    #reload.enabled: true
    #reload.period: 10s
  modules:
    #enabled: false
    path: modules.d/*.yml
    #reload.enabled: true
    #reload.period: 10s
```

Para activar cualquiera de los módulos preconfigurados: "./fileabeat modules enable logstash system". El módulo system, es para sistemas basados en unix.

En el caso del módulo de configuración de logstash, encontramos:

```yml
- module: logstash
  log:
    enabled: true
    var.paths: ["/path/to/log/logstash.log*"]
  slowlog:
    enabled: true
    var.paths: ["/path/to/log/logstash-slowlog.log*"]
```

Slowlog, es para todo lo que vaya dando problemas de velocidad. Aunque inicialmente indica que tratará de autodescubrir las rutas, lo mejor es indicarlas, porque funciona si se ha utilizado una instalación basada en paquetería, pero si no, no.

### Tipos de entrada

Además de la entrada de tipo log, filebeat admite otros tipos de entrada con sus configuraciones específcas:

* Stdin
* Redis
* UDP
* Docker
* TCP
* Syslog.

### Dashboard

Otra de las características que se pueden personalizar desde el fichero de configuración, son los dashboard de kibana. Viene configurado por defecto, y lo único que habría que hacer sería descomentar la sección y configurar el host de kibana:

```yml
setup.kibana:

  # Kibana Host
  # Scheme and port can be left out and will be set to the default (http and 5601)
  # In case you specify and additional path, the scheme is required: http://localhost:5601/path
  # IPv6 addresses should always be defined as: https://[2001:db8::1]:5601
  #host: "kibana:5601" 
```
