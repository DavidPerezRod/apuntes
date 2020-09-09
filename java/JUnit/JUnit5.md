# JUnit5

* JUnit5 parte del proyecto JUnit Lambda
  * su primera release se libero en 2017
  * utiliza aspectos de Java 8 como:
    * Lambda expressions
    * Streams
  * Se rediseño para mejorar su integración y extensibilidad

Los módulos principales son:

* JUnit Platform. Permite lanzar framework de test en la JVM
* JUnit Jupiter. Modelo de programación para escribir test y extensiones a JUnit.
* JUnit Vintage. Permite la ejecución de test JUnit3 y JUnit4

Los test JUnit se pueden ejecutar por linea de comandos de dos formas:

* ./mvnw clean test --> ejecuta el wrapper maven incluido en el proyecto, generado por el IDE  
* mvn clean test --> ejecuta maven desde el entorno.

Algunas de las nuevas características añadidas en JUnit5 son:
* **@tags**: que permiten agrupar test. Se puede aplicar tanto a nivel de clase como a nivel de método.
Con intellij, se puede utilizar esta característica para crear una nueva configuración de arranque de tipo JUnit, cuyo _"test kind"_ sea tag, para ejecutar a la vez todos los test marcados con el mismo tag.
Per su potencia está en agrupar un conjunto de test bajo una misma característica, para ser utilizados en integración contínua (CI)
* **test anidados (@nested)** Permite declarar clases anidadas de test. 
  * los before each, se ejecutan una vez por cada nivel de anidamiento. Si hay 3 clases con 3 @BeforeEach, por cada test de la clase más anidada se ejecutarán los 3 @BeforeEach de cada clase.
* Interfaces. Es esta versión se pueden crear interfaces para agrupar características de las que deben heredar otros test. El ejemplo más sencillo, declarar una interfaz con un tag, que es implmentado por clases hijas, que permite agruparlas para ejecuciones o CI.
  * Otro mecanismo añadido junto al anterior, es el de poder generar métodos por defecto en las interfaces.
  * @TestInstance es una anotación relacionada, que permite declarar el método por defecto sin hacerlo estático.
  
  Estas últimas características son bastante importantes porque permiten ampliar las buenas prácticas de desarrollo a los test, de forma que se pueda abstraer comportamiento común, de forma que no repitamos código.