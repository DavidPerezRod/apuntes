# MOCKITO

Un _mock_es un objeto simulado, que se utiliza para construir test. Imita el comportamiento de objetos reales de forma controlada. 

Es decir, se trata de una implementación alternativa de un objeto para reemplazarlo en un test.

Mockito es el framework más utilizado, y se caracteriza por su facilidad para inyectar los mocks mediante inyección de dependencias.

Entre los tipos de mocks que se pueden manejar con mockito, encontramos:
    * Dummy. Objeto utilizado para hacer comipilar el código
    * Fake. Objeto que tiene una implementación pero no está preparado para producción
    * Stub. Objeto con respuestas predefinidas a la invocación de sus métodos. 
    * Mock. Objeto con respuestas predefinidas a la invocación de sus métodos, de los que se espera un comportamiento concreto.
    * Spy. Son wrappers de los objetos cuyo comportamiento se quiere verificar, permiten interceptar llamdas que hace éste.

Otros términos importantes en este contexto son:
    * Verify. Se utilizar para verificar el número de veces que el método de un mock ha sido llamado
    * Argument Matcher. Permite comprobar los argumentos pasados a un método.
    * Argument captor. Captura los argumentos pasados a un método. Permite realizar aserciones sobre el número de argumentos pasados a un método.

En cuanto a las anotaciones que se utilizan para cada una de estas funcionalidades, tenemos:

|Anotacion|Descripción|
|----------|----------|
|@Mock| crea un mock de un objeto|
|@Spy| crea un spy de un objeto|
|@InjectMocks| injecta mocks o spies en la clase a probar
|@Captor| Captura argumentos en el mock|