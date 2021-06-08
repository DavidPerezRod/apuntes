# MVC Rest Controller

## Json

### JsonPath

JsonPath es un query language para documentos json, de forma similar a xpath en XML. Permite consultar y extreaer propiedfades de un couemtnos json.

Una expresión JsonPath, empieza con el símbolo dolar ($), el cual se refiere al elemento raiz de la query. Éste va seguido por la secuencia de elementos hijos, separados por punto (.) o por corchetes ([]).

Los principales operadores de JsonPath son:

|operador|funcionalidad|
|--------|-------------|
|$ | acceso al elemento raíz |
|@ | objeto o elmento actual |
|. | hijo del elmento actual |
|[ ] | operador que permite acceder a un hijo del elemento actual por su índice o nombre |
|* | devuelve todos los objetos o elementos (¿hijos del elmento actual) |
|, | operador union. Devuelve la unión de todos los hijos o ínidces indicados |
|: | operador array, permite obtener una colección a partir de una sección de elementos  [start:end:step] |
|? (<expresion>)| filtra los elementos por una determinada expresión. Ésta debe producir un resultado booleano.

A nivel de test, esta herramienta es de mucha utilidad para evaluar la respuesta de los servicios rest. La forma de hacerlo, es a través del método estático andExpect del mockMvc

```java
@Test
void testGetBeerById() throws Exception {
    given(beerService.findBeerById(any())).willReturn(beerDto);
    mockMvc.perform(get("/api/v1/beer/"+ beerDto.getId()))
            .andExpect(status().isOk())
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(jsonPath("$.id", is(beerDto.getId().toString())))
            .andExpect(jsonPath("$.beerName", is("Beer1")))
    ;
}
```

### @WebMvcTest

Con esta anotación, Spring Boot incluye  las utilidades que después se van a necesitar para testear el controlador. Hay que indicarle cuál es el controller a probar: @WebMvcTest(nombreClase.class)

Una vez incluida esta anotación, en lugar de utilizar las anotaciones @Mock para inyectar los mocks de mockito, tendremos que utilizar las anostaciones @MockBean de SpringBoot. 

Otros aspectos a tener en cuenta:

* se elimna la anotación @ExtendWith(MockitoExtension.class) a nivel de clase
* hace falta declarar @Autowird el atributo MockMvc
* hay que tener en cuenta que los atributos  se van a compartir entre todos los test, de forma que hay que inicializarlos entre sus ejecuciones:

```java
    @AfterEach
    void tearDown(){
        reset(service);
    }
```