# Qué es un API Rest

Una API de REST o API de RESTful, es una interfaz de programación de aplicaciones que se ajusta a los límites de la arquitectura REST, y permite la interacción con servicios web RESTful.

El concepto de Transferencia de estado representacional (REST) no define un protocolo ni es un estándar, sino más bien un paradigma de arquitectura. Que implicaciones tiene:

* La información que transfiere representa el estado de un recurso
* Utiliza el protocolo de comunicación es HTTP
* Utiliza elementos adicionales de información:
  * metadatos
  * autorización
  * URI
  * caché
  * cookies
  * cabeceras
  * respuesta
* La información se entrega mediante alguno de los siguienes formatos:
  * JSON. Es el formato más popular
  * HTML
  * XLT
  * Python
  * PHP
  * Texto sin formato

## Norma

Aparte del protocolo de comunicación utilizado, hay que tener en cuenta dos otros aspectos:

* Arquitectrua cliente-servidor compuesta de servidores, recursos, y gestión HTTP de solicitudes
* Comunicación cliente-servidor sin estado:
  * No se almacena información del cliente entre las solicitudes.
  * Cada una de ellas es independiente de las otras
* Los datos pueden almacenarse en caché. Se puede configurar un protocolo cliente-caché-servidor sin estado. Consiste en definir algunas respuestas a peticiones como cacheables, así el cliente puede ejecutar la misma respuesta para peticiones idénticas. *Sin embargo su uso no está recomendado*
* Interfaz uniforme para que la información se transfiera de forma estandarizada
  * Los recursos solicitados deben ser identificables e independientes de las representaciones enviadas al cliente.
  * El cliente debe poder manipular los recursos a través de la representación que recibe
  * Las respuestas del servidor deben ser autodescriptivas, con información qeu describa cómo procesarlas
  * Debe contener hipertexto o hipermedios, es decir que cuando se acceda a algún recurso, debe poder utilizar hipervínculos par buscar las demás acciones relacionadas con el recurso, que se encuentran disponibles.
* Organizada en un sistema de capas que participan en la recuperación de la información haciéndose cargo de distintas responsabilidades, seguridad, equilibrio de carga, disponibilidad, etc.
* Uso de hipermedios. El API Rest debe proporcionar al cliente los enlaces adecuados para ejecutar acciones concretas sobre los datos. Es obligatorio disponer del pincipio HATEOAS

Se trata pues, de un conjunto de pautas que pueden implementarse según sea necesario. Por este motivo las API REST son más rápidas y ligeras.

## Métodos en una API REST

Los objetos en REST siempre se manipulan a partir de la URI. Es la URI y ningún otro elemento el identificador único de cada recurso de ese sistema REST. La URI nos facilita acceder a la información para su modificación o borrado, o, por ejemplo, para compartir su ubicación exacta con terceros.

Las operaciones más importantes que nos permitirán manipular los recursos son:

* **GET** es usado para recuperar un recurso.
* **POST** se usa la mayoría de las veces para crear un nuevo recurso. También puede usarse para enviar datos a un recurso que ya existe para su procesamiento. En este segundo caso, no se crearía ningún recurso nuevo.
* **PUT** es útil para crear o editar un recurso. En el cuerpo de la petición irá la representación completa del recurso. En caso de existir, se reemplaza, de lo contrario se crea el nuevo recurso.
* **PATCH** realiza actualizaciones parciales. En el cuerpo de la petición se incluirán los cambios a realizar en el recurso. Puede ser más eficiente en el uso de la red que PUT ya que no envía el recurso completo.
* **DELETE** se usa para eliminar un recurso.

Otras operaciones menos comunes pero también destacables son:

* **HEAD** funciona igual que GET pero no recupera el recurso. Se usa sobre todo para testear si existe el recurso antes de hacer la petición GET para obtenerlo (un ejemplo de su utilidad sería comprobar si existe un fichero o recurso de gran tamaño y saber la respuesta que obtendríamos de la API REST antes de proceder a la descarga del recurso).
* **OPTIONS** permite al cliente conocer las opciones o requerimientos asociados a un recurso antes de iniciar cualquier petición sobre el mismo.

La idempotencia permite que la API sea tolerante a fallos. Por ejemplo, en el caso de una operación PUT para editar un recurso, si se obtubiese un fallo de Timeout, no se podría saber si el recurso fue creado o actualizado. Sin embargo, no es necesario comprobar su estado tras un fallo, y que en caso de que se haya creado el recurso, una nueva petición PUT no crearía otro más, algo que sí podría pasar con una operación como POST.

Dos términos a tener en cuenta son los de métodos seguros y métodos idempotentes:

* **Métodos seguros** son aquellos que no modifican recursos (serían GET, HEAD y OPTIONS)
* **Métodos idempotentes** son aquellos que se pueden llamar varias veces obteniendo el mismo resultado (GET, PUT, DELETE, HEAD y OPTIONS).

## Ventajas

* Separación entre cliente y servidor. El API Rest no solo separa la interfaz de comunicación entre cliente y servidor, sino que además al estar separados permite tener servidores diferentes para el front y el back.
* Visibilidad, fiabilidad y escalabilidad. Cualquier equipo de desarrollo puede migrar a nuevos servidores o realizar cambios en BBDD siempre y cuando los datos de todas las peticiones respeten la norma.
* La API Rest es independiente de plataforma o lenguaje. Lo único indispensable es que en las peticiones y respuestas se utilice siempre un lenguaje de intercambio sin estado.

## Glosario

* REST: Transferesncia de estado representacional
* [API](https://www.redhat.com/es/topics/api/what-is-api-design): Interfaz de programación de aplicaciones. Es un conjunto de definiciónes y protocolos que se utilizan para diseñar e integrar el software de aplicaciones. Es el contrato entre el proveedor de información y el usuario, que esablece el contenido que se necesita por parte del consumidor, y el que requiere el productor, presentan las funciones de las aplicaciones y los datos para que los usuarios y los desarrolladores puedan utilizarlas
* [HATEOAS (Hypermedia As The Engine Of Application State – Hipermedia Como Motor del Estado de la Aplicación)](https://www.ionos.es/digitalguide/paginas-web/desarrollo-web/hateoas-que-es-y-cual-es-su-funcion-en-las-api-rest/)

# Fuentes

* [RedHat - What is a REST API](https://www.redhat.com/es/topics/api/what-is-a-rest-api)
* [Idento - ¿Qué es una API REST?](https://www.idento.es/blog/desarrollo-web/que-es-una-api-rest/)