# Dependency injection

Por defect JUnit5 permite la inyección de parámetros en métodos meidane el API Parameter Resolver. El API incluye 3 tipos de resolvers:

* TestInfo: proporciona información de nombre, métodos, clases y tags
* RepetitionInfo: proporiona información de repetición de test
* TestReporter: permite publicar información de ejecución para la generación de informes.

Por ejemplo, dos parámtros predefinidos que pueden utilizarse con la anotación @RepeatedTest son **RepetitionInfo** y **TestInfo**.
 
Adicionalmente, se puede utilizar la dependencia junit-jupiter-params. Entre las anotaciones que proporciona, tenemos:

* @ParameterizedTest --> indica que se parametriza el método
  @ValueSource --> declara la lista de parámetros que va a recibir.