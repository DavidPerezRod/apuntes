# Introducción

Elasticsearch es un motor de búsqueda que almacena información de forma indexada.

**Ventajas**

Se podrían enumerar varias ventajas que brinda esta herramienta. Algunas de las más destacables son las siguientes:

* Al estar desarrollado en Java, es compatible en todas las plataformas donde Java lo sea.
* Elasticsearch es rápido. Como Elasticsearch está desarrollado sobre Lucene, es excelente en la búsqueda de texto completo. Elasticsearch también es una plataforma de búsqueda en casi tiempo real, lo que implica que la latencia entre el momento en que se indexa un documento hasta el momento en que se puede buscar en él es muy breve: típicamente, un segundo. Como resultado, Elasticsearch está bien preparado para casos de uso con restricciones de tiempo como analítica de seguridad y monitoreo de infraestructura.
* Elasticsearch es distribuido por naturaleza. Los documentos almacenados en Elasticsearch se distribuyen en distintos contenedores conocidos como shards, que están duplicados para brindar copias redundantes de los datos en caso de que falle el hardware. La naturaleza distribuida de Elasticsearch le permite escalar horizontalmente a cientos (o incluso miles) de servidores y gestionar petabytes de datos.
* Simple realización de respaldos de los datos almacenados.
* Utiliza objetos JSON como respuesta, por lo que es fácil de invocar desde varios lenguajes de programación.

**Desventajas**

Como todo, ElasticSearch posee algunas desventajas:

* Sólo soporta como tipos de respuesta JSON, lo que lo limita al no soportar otros lenguajes, como CSV o XML.
* Algunas situaciones pueden generar casos de split-brain.

Elasticsearch usar una estructura de datos llamada índice invertido, que está diseñado para permitir búsquedas de texto completo muy rápidas. Un índice invertido hace una lista de cada palabra única que aparece en cualquier documento e identifica todos los documentos en que ocurre cada palabra.

Durante el proceso de indexación, Elasticsearch almacena documentos y construye un índice invertido para poder buscar datos en el documento casi en tiempo real. La indexación comienza con la API de índice, a través de la cual puedes agregar o actualizar un documento JSON en un índice específico.

## Indice Invertido

Un índice invertido, es similar a un glosario. Por ejemplo, dados los siguientes cuatro ejemplos:

* {"title":"Torrente, el brazo tonto de la ley"}
* {"title":"Ciudad sin ley"}
* {"title":"Dos tontos muy tontos"}
* {"title":"Ciudades modernas"}

El índice invertido, en el que previamente se hace un análisis de plurales, género, etc., para sacarle más rendimiento, podría tener este aspecto:

||término|id|
|--|--|--|
|title|braz|1|
|title|ciudad|2,4|
|title|dos|3|
|title|ley|1,2|
|title|modern|4|
|title|tont|1,3|
|title|torrent|1|

El identificador corresponde a los documentos en los que aparece el término. Así que se almacena el término a indexar y los documentos en los que aparece. De esta forma cuando se busca por un término, a Elasticsearch no le hace falta buscar en todos los documentos, sabe exáctamente en cuáles está almacenado.  

A continuación entra en juego el proceso de tokenización. Elasticsearch tiene dos formas de tokenización por defecto. Mediante espacios en blanco, de forma que cada término encerrado entre dos espacios en blando lo analiza, o bien por camel case, en este caso separa las palabras entre mayusculas
 
## Conceptos básicos

Elasticsearch funciona en modo **cluster** compuesto por nodos, aunque solo haya un nodo, aunque lo normal es que haya al menos 3. Además, esta configuración será flexible, ya que en cualquier momento se podrán añadir más nodos al cluster.

Otro concepto es el de **índice** que es la únidad en la que se va a almacenar la información. Lo habitual es tener un índice para cada aplicación. Los índices pueden estar almacenados en un único nodo, o que se reparta entre los índices.

Además, cada índice se repartirá en **shards**, que pueden ser primarios o réplicas. Cada uno de los índices se dividirá en tantos shards como lo indiquemos. Es por este motivo, que si el índice se descompone en varios shards, éstos pueden distribuirse entre los nodos del cluster.

Los shards y sus réplicas, nunca se almacenan en el mismo nodo, ya que la idea es que a partir de las réplicas, podamos reconstruir el cluster.

## Tipos de nodos

Elasticsearch tiene distintos tipos de nodos, y por defecto todos los nodos pueden realizar todas las tareas. Cuando se tiene un cluster muy pequeño, la separación de responsabilidades entre nodos, puede no tener mucho sentido, ya que todos los nodos deben poder hacer todas las tareas.

Los tipos de nodo son:

* Master. Es el encargado de gestionar el estado del cluster, así como de hacer las tareas más sencillas tales como:
  * crear y borrar índices
  * en qué shards debe almacenarse cada documento
  * lo nodos que forman parte del cluster
  * Es fundamental para el cluster, si no hay un nodo maestro, no se levantaría el cluster.
* Data. Son los que realmente pueden almacenar shards y réplicas.
  * por defecto todos los nodos pueden ser maestros y de datos. Esto tiene sentido mantenerlo si nuestro cluster es pequeño.
  * se encargan de todas las operaciones CRUD
* Ingesta. Hace unas labores similares a Logstash. Permite hacer un preporcesamiento de la información que llega a Elasticsearch.
* Coordinación. Son todos aquellos a los que se les deshabilita la opción de ser maestros, de almacenar datos y de hacer labores de ingesta.
  * Estos nodos reciben las peticiones de clientes externos y desempeñaría una labor similar a un balanceador. Recible los resultados, los agrupa y los redirige al cliente.
* Tribe. Es similar al anterior, pero lo que hace es coordinar búsquedas entre distintos clústeres de Elasticsearch, que no forman parte de su propio cluster. ES una forma de Federar clústeres.

## API Elasticsearch

Cualquier herramienta, ya sea cerebro, Kibana, etc, todos funcionan mediante el API rest que proporciona Elasticsearch, basada en JSON.

Los tipos de APIS que proporciona son:

* cluester. Proporciona información el cluster. Todas las peticiones que soporta son los verbos habituales del tecnilogía REST, get, put, delete, create
* catálogo. Es muy parecida a la de cluster. Tiene prácticamente las mismas opciones, pero la información que devuelve no es JSON.
* Índices. Permite gestionar los índices. Crearlos, borrarlos, abrirlos, cerrarlos, y muchas otras operaciones.
* Documentos. Permite definir tipo de documento que va a albergar, y los datos de los registros. En este sentido, es importante indicar que al ser una BBDD orientada a documentos, la estructura de éstos puede ser variable para un mismo documento.
* Búsqueda y agregación. Son las APIS más potentes.

Elasticsearch ofrece una serie de clientes de comunicación para diferentes lenguajes. Unos son los clientes oficiales y otros mantenidos por la comunidad.

## Mappings y templates

Al ser una BBDD documental y haber variabilidad de estructura, cuando se añade un campo, Elasticsearch trata de asignar el tipo más adecuado para él. Pero por medio de los templates, podemos definir tanto el índice/índices como sus sus shards, réplicas y mappings.

Esta opción es más adecuada porque si no, por cada campo de tipo texto, Elasticsearch va a crear de forma automática uno map de tipo text denominado _description_ que será tokenizable y analizable, y otro de tipo keyword quen no será ni tokenizado ni analizado.

El mapping se define por medio del campo properties. Así, un ejemplo de template con sus mappings sería:

```json
{
    "index_patterns": ["log-generator-*" ], 
    "order": 1, 
    "settings": {
        "number_of_shards": 3,
        "number_of_replicas": 1
        },
        "mappings":{
            "properties": {
                "geoip"
            }
        }
    }
}
``` 