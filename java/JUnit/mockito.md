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
    * verify(). Se utilizar para verificar el número de veces que el método de un mock ha sido llamado
    * when(). Permite definir cuál va ser el comportamiento que va a tener un mock cuando se invoque a un método.
    * argumentMatcher. Permite comprobar los argumentos pasados a un método.
    * argumentCaptor. Captura los argumentos pasados a un método. Permite realizar aserciones sobre el número de argumentos pasados a un método.

En cuanto a las anotaciones que se utilizan para cada una de estas funcionalidades, tenemos:

|Anotacion|Descripción|
|----------|----------|
|@Mock| crea un mock de un objeto|
|@Spy| crea un spy de un objeto|
|@InjectMocks| injecta mocks o spies en la clase a probar. Anotaríamos así, el objeto que vamos a probar en el test, para que mockito le inyecte los mocks declarados
|@Captor| Captura argumentos en el mock|

## Mocking

Con mockito, tenemos la posibilidad de instanciar un mock, de forma inline (Tipo variable=mock(Tipo.class)) o bien por anotación. 

En el caso de utilzar las anotaciones para inyectar mocks, es necesario declarar método de inicialización previo, para que mockito inicialice los test:

```java
@BeforeEach
void setUp(){
    MockitoAnnotations.initMocks(this);
}

@Mock
Map<String, Object> mapMock;
```

Una forma de simplificar el código anterior, es utilizando a nivel de clase la anotación @ExtendsWith(MockitoExtension.class), que nos evita escribir la inicialización del framework a nivel de clase:

```java
@ExtendWith(MockitoExtension.class)
public class JUnitExtensionTest {

    @Mock
    Map<String, Object> mapMock;

    @Test
    void testMock() {
        mapMock.put("keyValue", "foo");
    }
}
```

### verify
 
 Ejemplos de uso de verify:

```java
verify(testedRepository,times(1)).deleteById(1l);
verify(testedRepository,atMost(5)).deleteById(1l);
verify(testedRepository,atLeastOnce()).deleteById(1l);
verify(testedRepository,atLeast(1)).deleteById(1l);        
verify(testedRepository, never()).deleteById(5l);       
```

### when

```java
when(mockObject.method()).thenReturn(type);
```

## Comprobación de argumentos

La comprobación de argumentos de llamada, puede ir desde una forma flexible, o genérica, en la que solo se comprueba el tipo con el que se realiza la llamada, como en el ejemplo siguiente, a cosas más concretas:

```java
@Test
void testArgument() {
    Pojo pojo= new Pojo();
    service.delete(pojo);
    verify(pojoRepository).delete(any(Pojo.class));
}
```

Probablemnte, la siguiente, sea comprobar el tipo, para lo cual, mockitio proporciona una relación bastante extensa de métodos.

```java
@Test
void testArgument() {
    Pojo pojo= service.findById(1l);;
    verify(pojoRepository).findById(anyLong());
}
```