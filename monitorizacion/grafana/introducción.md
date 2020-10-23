# Introducción

Grafana es una herramienta para visualizar datos de serie temporales como Graphite, InfluxDB y OpenTSDB, aunque no exlusivamente. Originalmente comenzó como un componente de Kibana y que luego le fue realizado una bifurcación. Una de las principales diferencias con la primera, es que procesa y/o visualiza información recolectada por terceros, aunque  puede recopilar de forma nativa datos de Cloudwatch, Graphite, Elasticsearch, OpenTSDB, Prometheus, Hosted Metrics e InfluxDB.

![Pandora FMS Stack](../grafana/imagenes/Pandora%20FMS%20stack.png)

En cuanto a las BBDD, grafana soporta:

* SQLite
* MySQL
* PostgreSQL

El caso de la primera es especial, porque grafana lleva una embebida con la instalación por defecto.

También existen plugins para otras fuentes de datos, como es el [caso de Mongo](https://github.com/JamesOsgood/mongodb-grafana/blob/master/README.md)