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

## Excepciones

Para poder trabajar con el tratamiento de excepciones, mockito permite simular su lanzamiento mediante la sentencia: 

```java
doThrow(new RuntimeException("msg")).when(clase).metodo();
```

También se puede utilizar la sintaxis BDD de mockito para este comportamiento:

```java
given(clase.metodo).willThrow(new RuntimeException(("msg")));
```

Y todavía hay una tercera forma en la cual se puede escribir este comportamiento:

```java
willThrow(new RuntimeException("msg")).given(clase).metodo();
```

Por si sola, esta sentencia no verifica nada, hay que combinarla con JUnit5:

```java
 assertThrows(RuntimeException.class, ()->clase.metodo())
```

## Lambda expresión

Mockito proporciona una sentencia para introducir expresiones lambda como argumentos a los métodos bajo test:

```java
given(repository.save(argThat((argument-> argument.getDescription().equals("expresion"))))).willReturn(expectedObject);
```

## Argument Captor

Este recurso de mockito sirve para validar los argumentos con los que se invocan los métodos. 

Hay varias formas de declararlo, quizás la más sencilla, sea mediante inyección de dependencias:

```java
//Declaración del atributo mediante inyección
@Captor
ArgumentCaptor<String> stringArgumentCaptor;


//utilización del atributo para la captura de argumentos
@Test
void processFindFormWildcardStringAnnotaspecialtyRepositoryindForm(owner, bindingResult, null);

    //then
    assertThat("%Buck%").isEqualToIgnoringCase(stringArgumentCaptor.getValue());
}
```

Aunque también se puede declarar como variable local cuando se necesite:

```java
@Test
void processFindFormWildcardString() {
    //given
    Owner owner= new Owner(1L, "Joe", "Buck");
    List<Owner> ownerList= new ArrayList<Owner>();
    final ArgumentCaptor<String> captor= ArgumentCaptor.forClass(String.class);
    given(ownerService.findAllByLastNameLike(captor.capture())).willReturn(ownerList);

    //when
    String viewName= controller.processFindForm(owner, bindingResult, null);

    //then
    assertThat("%Buck%").isEqualToIgnoringCase(captor.getValue());
}
```

## Answers

Es un mecanismo que se puede utilizar como alternativa a thenReturn o thenThrow cuando el método cuyo funcionamiento estamos comprobando, devuelve un resultado u otro en función de cálculos.

Por ejemplo, se puede dejar configurado un procesamiento genérico ante una invocación con @BeforeEach

```java
given(ownerService.findAllByLastNameLike(stringArgumentCaptor.capture())).willAnswer(invocationOnMock -> {
            List<Owner> owners = new ArrayList<>();
            String name = invocationOnMock.getArgument(0);

            if (name.equals("%Bock%")) {
                owners.add((new Owner(1L, "Joe", "Bock")));
                return owners;
            }
        });
```

## Verify order of interactions

Para verificar el orden en el que se invocan varios métodos en un mock, se utiliza la utilidad inOrder de Mockito. Primero, se declara los mocks que van a ser utilizados dentro del método a probar, y después de la acción **_when_** se verifica el orden, los métodos y los parámetros con que fueron invocados.

```java
//aquí no importa el orden en el que se pasan los parámetros
//no tienen por qué coincidir con el orden de invocación
InOrder inOrder= Mockito.inOrder(model, ownerService);

//la llamada a when puede o no incluir mocks, pero estos
//si deben haber sido inyectados de alguna forma. Por anotaciones 
//o por otro tipo de mecanismos
//when
String viewName= controller.whenAction(arguments);


//aquí el orden si es importante. Los asserts se deben declarar en el 
//orden en el que se produjeron las llamadas.
//inorder asserts
inOrder.verify(ownerService).findAllByLastNameLike(anyString());
inOrder.verify(model).addAttribute(anyString(),anyList());
```

## Verifiy number of interactions
Los métodos ofrecidos por mockito para verificar el número de interacciones son: 

```java
//verificar el número absoluto de interacciones
inOrder.verify(model, times(1)).addAttribute(anyString(),anyList());

//verificar que no se produce ninguna interacción
verifyZeroInteractions(model);

//verificar que no se producen más interacciones
verifyNoMoreInteractions(model);
```

La dfierencia entre noMoreInteractions y ZeroInteractions, es que la primera verifica que a partir de ese punto no se producen más interacciones, mientras que la segunda verfica que no se produzcan interacciones en cualquier momento del método.


## Mockito Timeout

Esta utilidad proporcionada por mockito, sirve para verificar que una determinada funcionalidad se ejecuta dentro de un intervalo de tiempo. 

El inconveniente de este tipo de utilidad, es que puede depender del entorno en el que se ejecuten los test, servidores, tecnologías de integración contínua, etc. 

```java
then(repository).should(timeout(100).times(2)).deleteById(1L);
```

### Mockito spies

Esta técnica proporcionada por mockito, permite trabajar con los objetos reales, y solo se reemplazará alguno de sus comportamientos. Actúan como un wrapper en torno a la implementación real 

La forma de declarar un objeto spy, es mediante la anotación @Spy

```java
@Spy
MapService Service;

@Test
void loadPetWithVisit(){
    //given
    Map<String, Object> model= new HashMap<>();
    Pet pet= new Pet(1L);
    petService.save(pet);

    //aquí le decimos que invoque al método real, en lugar
    //de simularlo
    given(petService.findById(anyLong())).willCallRealMethod();

    //when
    Visit visit = visitController.loadPetWithVisit(1L, model);

    //then
    assertThat(visit).isNotNull();
    assertThat(visit.getPet()).isNotNull();
    assertThat(visit.getPet().getId()).isEqualTo(1l);
}
```

Pero además el spy, también puede simular la llamada al método, con el método habitual willReturn, en lugar de willCallRealMethod.
