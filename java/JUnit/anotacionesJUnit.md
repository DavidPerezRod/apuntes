
Anotación | Funcionalidad
---------|----------
 @Test | indica que el método es un test 
 @ParametrizedTest | indica que el método es un test parametrizado
 @RepeatedTest | repite el test n veces
 @TestFactory | métod de factoría de test, para test dinámicos
 @TestInstance | configura el ciclo de vida de las instancias de test
 @TestTemplate | crea una plantilla que puede ser utilizada desde múltiples test
 @DisplayName | denomina al test mediante un nombre más legible para humanos. Aunque lo ideal es que los métodos tengan nombres significativos en sí mismos sin necesidad de utilizar esta anotación
 @BeforeEach | método que debe ejecutarse antes que los casos de test
 @AfterEach | método que debe ejecutarse después de los casos de test
 @BeforeAll | modo estático que debe ejecutarse antes que los casos de test de la clase actual
 @AfterAll | método estático que se ejecuta después de casos de test de la clase actual.
 @Nested | crea una clase de test anidada
 @Tag | declara tags para filtrar test
 @Disabled | inhabilita test o clases de test, dependiendo de si lo escribimos en el método o la clase
 @ExtendedWith | permite registrar extensiones

La forma de indicar a **maven** la versión de JUnit con la que se está trabajando es mediante el tag <junit-platform.version> dentro del bloque <properties>.

Para la configuración de JUnit5 ademś hay que añadir al bloque de dependencias, las dos siguientes:

    <dependencies>
        <dependency>
            <groupId>org.junit.jupiter</groupId>
            <artifactId>junit-jupiter-api</artifactId>
            <version>${junit-platform.version}</version>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>org.junit.jupiter</groupId>
            <artifactId>junit-jupiter-engine</artifactId>
            <version>${junit-platform.version}</version>
            <scope>test</scope>
        </dependency>
    </dependencies>

Por otro lado los plugins de **surfire** y **failsafe** deben ser 2.2, 2-0 o superiores. El primero es para la ejecución de test JUnit desde maven, y el segundo para enlazar la fase de test en el ciclo de vida de maven.

Para ejecutar test con Maven, desde el directorio del proyecto podemos ejecutar **./mvnw clean test**, o bien **mvn clean test**. El primero ejecuta el wrapper maven del proyecto y el segundo ejecuta directamente Maven. La diferencia, es que pueden ser versiones distintas de Maven, ya que la primera es la que esté instalada en el IDE.


