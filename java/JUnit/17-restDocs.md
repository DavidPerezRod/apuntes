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

En cuanto al proceso de configuración, puede hacerse tanto con gradle como con maven. En el caso de maven, la dependencia que debemos incluir es:

```xml
<dependency>
    <groupId>org.springframework.restdocs</groupId>
    <artifactId>spring-restdocs-mockmvc</artifactId>
    <scope>test</scope>
</dependency>

<!--la versión debe cogerla del pom parent de springframwork->
```

Además de ésta, también se debe añadir la dependencia con AsciiDoctor a nivel de compilación:

```xml
<plugin>
    <groupId>org.asciidoctor</groupId>
    <artifactId>asciidoctor-maven-plugin</artifactId>
    <version>1.5.3</version>
    <executions>
        <execution>
            <id>generate-docs</id>
            <phase>prepare-package</phase>
            <goals>
                <goal>process-asciidoc</goal>
            </goals>
            <configuration>
                <backend>html</backend>
                <doctype>book</doctype>
            </configuration>
        </execution>
    </executions>
    <dependencies>
        <dependency>
            <groupId>org.springframework.restdocs</groupId>
            <artifactId>spring-restdocs-asciidoctor</artifactId>
            <version>${spring-restdocs.version}</version>
        </dependency>
    </dependencies>
</plugin>
```

Por último, también se debe añadir carpeta, fichero y secciones específicos de asccidoctor. Carpeta asciidoc a nivel de main, fichero index.adoc, y en su interior, las secciones específicas que se quieran componer, por ejemplo:

```text
= SFG Brewery Order Service Docs
John Thompson;
:doctype: book
:icons: font
:source-highlighter: highlightjs

Sample application demonstrating how to use Spring REST Docs with JUnit 5.

`BeerOrderControllerTest` makes a call to a very simple service and produces three
documentation snippets.

One showing how to make a request using cURL:

include::{snippets}/orders/curl-request.adoc[]

One showing the HTTP request:

include::{snippets}/orders/http-request.adoc[]

And one showing the HTTP response:

include::{snippets}/orders/http-response.adoc[]

Response Body:
include::{snippets}/orders/response-body.adoc[]


Response Fields:
include::{snippets}/orders/response-fields.adoc[]
```

En cuanto a la configuración a nivel de anotaciones, hay varias formas de configurarlo, que se pueden ver en la [documentación oficial](https://docs.spring.io/spring-restdocs/docs/2.0.5.RELEASE/reference/html5/#getting-started-documentation-snippets). Probablemente la más sencilla, una vez añadida la inyección de mocks a través de spring con @Autowired de MockMvc, sea con la anotaciones @AutoConfigureRestDocs y @ExtendWith(RestDocumentationExtension.class)

## Documentación de parámetros.

Para documentar los parámetros, hay que añadir la acción .andDo después del expect, indicando lo que queremos.

```java
mockMvc.perform(get("/api/v1/beer/{beerId}", UUID.randomUUID().toString()).accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andDo(document("v1/beer", pathParameters(parameterWithName("beerId").description("UUID of desired beer to get"))));
```

Pero además es muy importante cambiar los imports, ya que lo anterior no funcionará con el import standar MockMvcRequestBuilders, hay que importar el específico de restDoc.

```java
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
```

## Documentación de parámetros.

La forma de probarlos y documentarlos es bastante similar a la anterior:

```java
mockMvc.perform(get("/api/v1/beer/{beerId}", UUID.randomUUID().toString()).accept(MediaType.APPLICATION_JSON)
        .param("isCold", "yes"))
        .andExpect(status().isOk())
        .andDo(document("v1/beer", pathParameters(parameterWithName("beerId").description("UUID of desired beer to get")),
                requestParameters(parameterWithName("isCold").description("Is very cold parameter"))));
```

## Documentación de Responses

Un aspecto que hay que tener en cuenta a la hora de documentar las respuestas es que si se queiren documentar los campos de ésta, hay que documentar todos, o de lo contrario restDocs produce un error. Los métodos que utilizaremos para ellos son:

```java
.responseFields(fieldWithPath("id").description("id of field"))));
```

## Documentación de Request

Ocurre lo mismo que en el caso anterio, hay que documentar todos los campos:

```java
.andDo(document("v1/beer", requestFields(
        fieldWithPath("id").ignored(),
        fieldWithPath("version").ignored(),
        fieldWithPath("createdDate").ignored(),
        fieldWithPath("lastModifiedDate").ignored(),
        fieldWithPath("beerName").ignored(),
        fieldWithPath("beerStyle").description("beer style"),
        fieldWithPath("upc").description("upc of beer").attributes(),
        fieldWithPath("price").description("dollar price"),
        fieldWithPath("quantityOnHand").ignored()
)));
```

## personalizar la URI de la documentación

Se puede hacer fácilmente mediante la anotación:

```java
@AutoConfigureRestDocs(uriScheme = "https", uriHost = "dev.springframework.guru", uriPort = 80)
```

## Generación de documentación

Para generarla, se debe ejecutar el paso package con maven.