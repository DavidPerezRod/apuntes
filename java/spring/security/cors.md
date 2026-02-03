# Cross-Origin Resource Access 

Intercambio de recuros de origen cruzado, es un mecanimo basado en cabeceras HTTP, que permite que un _user agent_ obtenga permiso para acceder a recursos desde un servidor, en un origen (dominio, protocolo o puerto) distinto al del documento que genera la nueva petición. 

Por razones de seguridad, los navegadores restringen las solicitudes HTTP de origen cruzado iniciadas dentro de un script.

Para securizar este tipo de comunicaciones, el [W3C](https://www.w3.org/) propone la utilización de un mecanismo de **Intercambio de Recursos de Origen Cruzado**, que da control de acceso a dominios cruzados entre navegadores y servidores web.

En qué situaciones está indicado:

* Invocaciones de las APIs XMLHttpRequest o  Fetch.
* Fuentes Web (para usos de fuente en dominios cruzados @font-face dentro de CSS), para que los servidores puedan mostrar fuentes TrueType que sólo puedan ser cargadas por sitios cruzados y usadas por sitios web que lo tengan permitido.
* Texturas WebGL.
* Imágenes dibujadas en patrones usando drawImage.
* Hojas de estilo (para acceso CSSOM).
* Scripts (para excepciones inmutadas).

CORS añade cabeceras HTTP que permiten a los servidores definir el conjunto de orígenes que tiene permiso para leer la información usando un explorador web. La especificación sugiere que los explroadores verifiquen la solicitud mediante HTTP OPTIONS, que requiere la posterior aprobación del servidor para enviar la verdadera solicitud con cookies y datos de autenticación

## Solicitudes simples

Una solicitud de sitio cruzado es aquella que cumple las siguientes condiciones:

* Los únicos métodos aceptados son:
    * GET
    * HEAD
    * POST.
* Aparte de las cabeceras establecidas automáticamente por el agente usuario (ej. Connection, User-Agent,etc.), las únicas cabeceras que están permitidas para establecer manualmente son:
    * Accept
    * Accept-Language
    * Content-Language
    * Content-Type
* Los únicos valores permitidos de la cabecera Content-Type son:
    * application/x-www-form-urlencoded
    * multipart/form-data
    * text/plain


## Referencias 

* [Mozilla](https://developer.mozilla.org/es/docs/Web/HTTP/CORS)