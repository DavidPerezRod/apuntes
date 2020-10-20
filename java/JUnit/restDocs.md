# RestDocs

Se trata de un componente de la suite de spring orientado a generar documentación de los servicios rest. Se integra dentro de Spring MCV Test, y su objetivo es generar documentación HTML de las API's rest al mismo tiempo que se ejecutan nuestro test unitarios o de integración.

La idea es escribir el test y Spring REST Docs se engancha a su ejecución y apunta todo lo que vaya sucediendo. Toma nota de las rutas invocadas, de los parámetros utilizados, del cuerpo de la request, de las cabeceras usadas (incluyendo Accept), del status retornado, de los datos enviados como respuesta, etc. Y los deja apuntados como snippets: pequeños ficheros que resumen precisamente cada uno de esos puntos y que después puedes incluir en un documento plantilla donde a ti te parezca.

Cómo lo consigue, mediante hooks de los controller test. Cada snipet generado en test, es ensamblado después en la documentación final, vía Asciidoctor, aunque también se puede utilizar para ello markdown. Los tipos de clientes soportados son:

* Spring MCV Test
* WebTestClient (Webflux)
* REST Assured

En cuanto a los frameworks desde los que se puede utilizar Spring Rest Docs, estan:

* JUnit 5
* JUnit 4
* Spock
* Test NG (requiere configuración adicional)

Sin embargo, existen un conjunto de plugins que complementan la funcionalidad básica, y permiten documentar el código generado por otros frameworks:

* restdocs-wiremock: documenta los stubs WireMock
* restdoctext-jersey: plugin para utilizar RestDocs con el framework Jersey
* spring-auto-restdocs: documenta los parámetros de request y response
* restdocs-api-spec: genera documentación acorde a la especificación OpenAPI 2 y OpenAPI 3.

En cuanto a los snippets generados, contendrán información de:

* Curl-request
* http-request
* http-response
* httpie-request
* request-body
* response-body

En cuanto al proceso de configuración, puede hacerse tanto con gradle como con maven.

