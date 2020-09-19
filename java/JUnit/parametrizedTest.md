# Parametrized Test

Por defect JUnit5 permite la inyección de parámetros en métodos meidane el API Parameter Resolver. El API incluye 3 tipos de resolvers:

* TestInfo: proporciona información de nombre, métodos, clases y tags
* RepetitionInfo: proporiona información de repetición de test
* TestReporter: permite publicar información de ejecución para la generación de informes.

Por ejemplo, dos parámtros predefinidos que pueden utilizarse con la anotación @RepeatedTest son **RepetitionInfo** y **TestInfo**.
 
Adicionalmente, se puede utilizar la dependencia junit-jupiter-params. Entre las anotaciones que proporciona, tenemos:

* @ParameterizedTest --> indica que se parametriza el método. Permite declarar patrones de escritura en log.
  * @ValueSource --> declara la lista de parámetros que va a recibir.
  * @EnumSource(EnumType.class) --> pasa como parámetros la lista de enumerados
  * @CsvSource({
          "FL, 1, 1",
          "OH, 2, 2",
          "MI, 1, 1",
  })  --> permite pasar como argumento una lista de valores como si el origen fuese un fichero csv.
  * @CsvFileSource(resources= "/input.csv", numLinesToSkip = 1)  --> permite pasar como argumento un fichero en el que está la lista de valores que se pasarán como parámetro.
  * @MethodSource("getArgs") --> permite pasar como argumento un método en el que se definen los aprámetros que se van a recibir.
  * @ArgumentsSource(CustomerProviders.class) - permite pasar como argumento una clase que implmente ArgumentsProvider, que facilite la construcción de los parámetros.
