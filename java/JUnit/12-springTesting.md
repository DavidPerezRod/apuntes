[< indice](./0-index.md)

# Spring

Spring proporciona mecanismos para generar Mocks de:

* Environment - Mocks de entorno y de fuentes de propiedades
* JNDI
* Servlet API
* Spring Web Reactive - test para entornos reactivos
* ReflectionTestUtils - utiliza reflexión para la modificación de campos privados
  * Puede hacer autowired de campos privados, aunque se considera una mala práctica
  * Se puede utilizar también para intervenir los eventos del ciclo de vida
* Utilidades AOP
* Spring MVC Test. Se trata de un framework bastante robusto para probar las interacciones de controladores
  * MockHttpServletRequest
  * MockHttpSession
  * ModleAndViewAssert

Además permite probar web request sin necesidad de ejecutar un contenedor. De hecho, los test son mucho más rápidos si no se ejecuta el contexto.

## Test de Integeración

Spring también proporciona otras características útiles para la realización de test de integración:

* Cargar el contexto Spring. Aunque es una operación relativamente cara, Spring cachea el contexto entre test para mejorar el rendimiento.
* Inyección de dependencias
* Gestión de transacciones. Por defecto Spring hace un rollback de las interacciones con BBDD.
  * configura una instancia de JdbcTemplate de soporte a test
  * Ademas de una serie de utilidades para interaccionar con la BBDD:
    * countRowsInTable
    * countRowsInTableWhere
    * deleteFromTables
    * deleteFromTablesWhere
    * dropTables
  * Porporciona soporte para las BBDD in-memory más habituales:
    * H2
    * HSQL
    * Derby

En cuanto a las anotaciones que Spring proporciona en su API de test, encontramos las siguientes:

|anotación|nivel|despcripción|
|---------|-----|------------|
|@BootstrapWith|clase|anotación para configurar cómo arranca el test context|
|@ContextConfiguration|clase|configura al application context|
|@WebAppConfiguration|clase|configura el web application context|
|@ContextHierarchy|clase|anotación para configurar múltiples @ContestConfigurations|
|@ActiveProfiles|clase|configura los perfiles  que se van a probar|
|@TestPropertySource|clase|configura las fuentes de propiedades para test|
|@DirtiesContext|clase o método| Recarga el contexto después de los test - es una opción más lenta|
|@TestExecutionListeners|clase|configura listener para la ejecución de test|
|@Commit|clase o método|commits a BBDD|
|@Rollbak|clase o método|rollbacks a BBDD|
|@BeforeTransaction|método|ejecuta el método antes de que se inicie la transacción|
|@AfterTransaction|método|ejecuta el método después de completar la transacción|
|@Sql|método|configura scripts SQL antes de ejecutra un test|
|@SqlConfig| - | congiruación de parse o de scripts SQL|
|@SqlGroup| - | configuración de scripts SQL agrupados|
|@IfProfileValue|-|Activa el test para determinados entornos|
|@ProfileValueSourceConfiguration|clase|configura cómo se obtienen los valores de perfiles|
|@Timed|-|comprueba que el test se ejecute en un periodo de tiempo concreto
|@Repeat|-|Repite un test un número determinado de veces|

Además hay otras anotaciones específicas de Spring 5

|anotación|nivel|despcripción|
|---------|-----|------------|
|@SpringJUnitConfig|-|combina las anotaciones @ContextConfiguration con  @ExtendWith(SpringExtension.class) para congiurar spring context|
|@SpringJUnitWebConfig|-|combina @ContextConfiguration y @WebAppConfiguration con @ExtendWith(SpringExtension.class) para configurar Spring Context|
|@EnabledIf|-|ejecución condicional del test|
|@DisabledIf|-|ejecución condicionada del test|

Los perfiles, propiedades y dependencias específicas se pueden configurar en el pom de la siguiente manera:

```xml
<profiles>
    <profile>
        <id>HSQLDB</id>
        <activation>
            <activeByDefault>true</activeByDefault>
        </activation>
        <properties>
            <db.script>hsqldb</db.script>
            <jpa.database>HSQL</jpa.database>
            <jdbc.driverClassName>org.hsqldb.jdbcDriver</jdbc.driverClassName>
            <jdbc.url>jdbc:hsqldb:mem:petclinic</jdbc.url>
            <jdbc.username>sa</jdbc.username>
            <jdbc.password/>
        </properties>
        <dependencies>
            <dependency>
                <groupId>org.hsqldb</groupId>
                <artifactId>hsqldb</artifactId>
                <version>${hsqldb.version}</version>
                <scope>runtime</scope>
            </dependency>
        </dependencies>
    </profile>
</profiles>
```

Es importante advertir, que JUnit y Mockito, proporcionan suficientes utilidades como para dar una covertura de test alta, sin necesidad de cargar los contextos de Spring. Así que siempre que se quiera probar la interacción entre elementos y su funcionalidad básica, es posible que no se necesite recurrir a test de spring.