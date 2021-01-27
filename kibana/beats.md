# Beats

## MetricBeat

Es un proceso ligero pensado para enviar estadísticas e aplicaciones. Inicialmente se utilizaba para sacar métrcas del sistema. Esta pensado para ayudar en la monitorización de servidores. Alkgunos de los sistemas más habituales en los que se puede integrar son:

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

Con el comando: ./metricbeat modules list, se pueden lisatar todos los módulos disponibles, y cuáles de ellos están activos. Por defecto están a *.*.disabled, podemos activarlos mediante: ./metricbeat modules enable elasticsearch. Cada uno de estos ficheros contiene los detalles de monitorización.

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

