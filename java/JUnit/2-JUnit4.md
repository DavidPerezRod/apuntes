# JUnit4

* Se liberó en 2006 y está basado en:
  * Java 5
  * considerado la versión estándar
  * por todas las características añadidas a Java desde entonces, se considera que ha envejecido demasiado.

## Migracion Junit4 a Junit5

Las principales diferencias entre las dos versiones son :

JUnit4 | JUnit5
---------|----------
 @Before | @BeforeEach
 @After | @AfterEach
 @BeforeClass | @BeforeAll
 @AfterClass | @AfterAll
 @Ignored | @Disabled
 @Category | @Tag 
 ---------|----------
 @RunWith(SpringJUnit4ClassRunner.class) | @ExtendedWith(SpringExtension.class)
 @Test(expected= Foo.class) | Assertions.assertThrows(FooException.class)
 @Test(timeout=1) | Assertions.assertTimeout(Duration.. )
 ---------|----------
 @Rule | **deja de existir** @ExtendsWith
 @ClassRule | **deja de existir** @ExtendsWith
 
 Las anotaciones, se encuentran en el paquete org.junit.jupiter.api, las assertions en org.junit.jupiter.api.Assertions y las asunciones en el paquete org.junit.jupiter.api.assumptions

 En cualqueir caso, JUnit5 soporta retrocompatibilidad con JUnit4, lo único que hay que hacer es incluir la dependencia junit-vintage-engine al classpath. Sin embargo **no hay que olvider** que JUnit5 requiere ser ejecutado con la versión java 1.8 como mínimo.

Además de ésta, hay que tener en cuenta otras características:

@Tag vs @Category. La anotación @Tag a diferencia de @Category, requiere todo el nombre cualificado

  * @Tag ("com.example.nombreClase")
  * @Category (nombreClase.class)

Por último, las reglas JUnit4 no se soportan de forma nativa en JUnit5, así que hay que tener mucho cuidado con ellas, porque pueden generar problemas al migrar entre versiones.

## Actualización de dependencias JUnit5

Además de las [dependencias JUnit5](../JUnit/JUnit5.md), es necesario incluir:

```xml
<dependency>
  <groupId>org.junit.vintage</groupId>
  <artifactId>junit-vintage-engine</artifactId>
  <version>${versión definida en bloque properties}</version>
</dependency>
```

## Migración JUnit4 a JUnit5

Se pueden seguir los siguientes pasos de forma ordenada para convertir los test JUnit4 a JUnit5:

* Transformación de anotaciones. Cambiar las anotaciones JUnit4 por sus equivalentes en JUnit5
* Cambiar los imports de las anotaciones comunies para que apunten al paquete org.junit.jupiter.api
* Eliminar las dependecias con JUnit4. Esta dependencia, si se trata de un proyecto springboot, estará en el artefacto spring-boot-starter-test. Para eliminarla, hay que hacer un exclusion de la dependencia, en el propio tag.

```xml
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-test</artifactId>
  <scope>test</scope>
  <exclusions>
    <exclusion>
      <groupId>junit</groupId>
      <artifactId>junit</artifactId>
    </exclusion>
  </exclusions>
</dependency>
```
  