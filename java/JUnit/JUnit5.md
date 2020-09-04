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