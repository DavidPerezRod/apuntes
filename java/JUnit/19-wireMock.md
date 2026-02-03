# WireMock

Se trata de una utilidad que facilita las pruebas de los clientes de servicios. Actúa como un mock de un servicio, que puede ser configurado para proporcionar un tipo de respuestas, sus características son:

* es un simulador HTTP basado en APIs
  * HTTP response stubbing
  * respuestas basadas en stubbing de URL, header y body
  * verificación de request
  * standalone
  * retardos configurables
  * permite grabar acciones  y reproducir comportamientos
* puede configurarse para proporcionar respuestas
* puede utilizarse para verificar peticiones
* es como mockito, pero para servicios web

Wiremock se puede descargar en formato jar, y una vez ejecutado, crea dos directorios _files y mappings. Es en este segundo, en el que habrá que crear los ficherosd e configuración json.

A la utilidad de grabación, una vez levantado, se puede acceder mediante la url http://localhost:8080/__admin/recorder. Con ella se accede a la utilidad de grabación desde la consola de administración. En esta caso wiremock actúa como un proxy a través del cual hacemos la petición. De forma que:
* si utilizamos postman para hacer la petición local, postman debe hacer la petición a través de  wiremock. Levantamos nuestro servidor en un puerto, wiremock en otro puerto distinto, y request de postman en lugar de tener el puerto del servidor tendrá la de wiremock
* en caso de hacer una petición remota, en wiremock escribimos la url de la petición remota, y después en postman apuntamos al localhost:puertoWiremock y path de la url externa