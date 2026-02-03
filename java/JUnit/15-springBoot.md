# Spring Boot

La principales características de Spring Boot a nivel de test son:

* Hay que incluir la configuración spring-boot-starter-test, la cual incluye:
  * dependencias habituales de test
    * JUnit (Spring Boot 2.1 JUnit4 y Spring Boot 2.2 Juni5)
    * Spring Framework Testing 
    * AssertJ (Fluent assertions)
    * Hamcrest
    * Mockit
    * JSONAssert (assertions for JSON)
    * JsonPath (XPath for JSON)
  * anotaciones y soporte específico de spring boot
  * auto-configuración

La anotación necesaria para cargar la configuración del contexto es @SpringBootTest:

* si se está utilizando JUnit4, además hay que utilizar @RunWith(SpringRunner.class)
* la anotación ya incluye @ExtendsWith(SpringExtension.class)
* por defecto, busca por @SpringBootConfiguration. Esta anotación se incluye con @SpringBootApplication
* Por defecto no arranca el servidor de aplicaciones

El motivo de que Spring Boot Test no arranque un servidor de aplicaciones, es que el entorno web se puede configurar de varias maneras con @SpringBootTest(webEnvironment=<option>), donde option puede ser:

* MOCK - esta es la opción por defecto, carga un entorno web mock
* RANDOM_PORT - proporciona un servidor web embebido que escucha en un puerto aleatorio
* DEFINED_PORT - proporciona un servidor web escuchando en el puerto 8080, o el puerto configurado con server.port en appplication.properties
* NONE - sin entorno web

La anotación @SpringBootTest hace un scan completo del proyecto, y carga por defecto todas las configuraciones existentes por debajo del paquete en el que se encuentre la anotación @SpringBootApplication, que es la que realmente hace el scan. El inconveniente, por supuesto es el de aplicaciones más pesadas, complejas y costosas.

Así que en la medida de lo posible hay que evitarla y tratar de utilizar configuraciones ligeras como alternartiva a lo anterior.

# Anotaciones Spring Boot 


Anotación | Funcionalidad
---------|----------
 @TestComponent | permite definir componentes de test|
 @TestConfiguration | configuración java de test|
 @LocalServerPort | injecta el puerto del servidor en ejecución |
 @MockBean | injecta un mock de mockito|
 @SpyBean | injecta un spy de mockito|
 @DataJdbTest | entorno y configuración de BBDD y repositorios JPA|
 @DataLdapTest | |
 @DataMongoTest | |
 @DataNeo4jTest | |
 @DataRedisTest | |  
 @JdbcTest | |
 @JooqTest | |
 @JsonTest | |
 @RestClientTest | |
 @WebFluxTest | |
 @WebMvcTest | |

Pero además hay que tener en cuenta que se trata de un ámbito en crecimiento, y que por lo tanto pueden aparecer más anotaciones. Por este motivo, es importante revisar de vez en cuando el apéndice D de la documentación oficial de Spring Boot.

## Configuración por defecto

