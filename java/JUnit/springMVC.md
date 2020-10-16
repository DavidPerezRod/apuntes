# Spring MVC Test

Spring MCV Test es un framework para simular las interacciones de los controladores Spring MVC.

Proporciona medios para hacer test unitarios, así como test de integración, lo cual a nivel de MVC, quiere decir que se proporciona mecanismos para poder probar controladores sin necesidad de desplegar en un servidor de aplicaciones, pero también proporciona mecanismos para realizar test de integración desplegando en servidor de aplicaciones.

La ventaja de realizar test sin desplegar en servidor de aplicaciones es que nos ahorramos la complejidad asociada a los aspectos de la red.

En el primer caso, además podemos distinguir entre test sin contexto de aplicación y test con contexto de aplicación.

De las tres posibilidades, los test unitarios son los más rápidos y ligeros, ya que si ejecutamos test con el contexto de aplicación, éste deberá cargar todos los beans configurados para ser guardados en el contexto. En general es recomendable utilizar esta posibilidad solo cuando se quieran probar ciertas configuraciones más complejas.

Por otro lado, sin deplegar un servidor de aplicaciones, no se podrán probar algunos aspectos que dependen de éste:
    * HTML
    * JSP
    * Thymeleaf
    * etc.

Los principales elementos de Spring MVC son:

    * MockHttpServletRequest
    * MockHttpServletResponse
    * DispatcherServlet (todas las peticiones y respuestas se erutan por éste)
    * MockHttpServletRequestBuilder
    * MockMvcResultMatchers

## Standalone Setup

Para hacer una configuración independiente del contexto de aplicación o del servidor de aplicaciones, es necesario servirse de los builders de Spring MVC Test

En el siguiente ejemplo, se configura un controller y se puede verificar su comportamiento, añadiendo la claúsula _andExpectations_

```java
@ExtendWith(MockitoExtension.class)
class VetControllerTest {

    @Mock
    ClinicService clinicService;

    @Mock
    Map<String, Object> model;
    @Test
    void testNewOwnerPostNotValid() throws Exception {
        mockMvc.perform(post("/owners/new")
                .param("firstName", "Jimmy")
                .param("lastName", "Buffett")
                .param("city", "Key West"))
                .andExpect(status().isOk())
                .andExpect(model().attributeHasErrors("owner"))
                .andExpect(model().attributeHasFieldErrors("owner", "telephone"))
                .andExpect(model().attributeHasFieldErrors("owner", "address"));
    }

    @InjectMocks
    VetController controller;

    List<Vet> vetsList = new ArrayList<>();

    @BeforeEach
    void setUp() {
        vetsList.add(new Vet());
        given(clinicService.findVets()).willReturn(vetsList);
        mockMvc= MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void testControllerShowVetList() throws Exception{
        mockMvc.perform(get("/vets.html")).andExpect(status().isOk());
    }
}
```
  
Pero además de comprobar el status como en el ejemplo anterior, se pueden comprobar otros atributos de la respuesta, como el nombre de la vista, o atributos del modelo. Y se puede hacer concatenándolos en una sola sentencia:

```java
    @Test
    void testControllerShowVetList() throws Exception{
        mockMvc.perform(get("/vets.html")).andExpect(status().isOk()).andExpect(model().attributeExists("vets")).andExpect(view().name("vets/vetList"));
    }
```

## Spring Context

También se pueden inyectar los mocks mediante configuración XML en el contexto de Spring. En este caso se genera un xml a nivel de recursos de test, y en él se declaran los recursos que se van a inyectar:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns="http://www.springframework.org/schema/beans"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
        http://www.springframework.org/schema/beans/spring-beans.xsd">

    <bean class="org.mockito.Mockito" factory-method="mock">
        <constructor-arg value="org.springframework.samples.petclinic.service.ClinicService"/>
    </bean>
</beans>
```

Después nos servimos de esta declaración para inyectar las dependencias en la clase, accediendo a la configuración mediante la anotación @SpringJUnitWebConfig:

```java
@SpringJUnitWebConfig(locations = {"classpath:spring/mvc-test-config.xml"})
class OwnerControllerTest {

    @Autowired
    OwnerController ownerController;

    @Autowired
    ClinicService clinicService;

    @Test
    void tempTest() {
        
    }
}
```

En el ejemplo anterios además de la configuración standalone, se ve cómo probar valores para la respuesta, atributos, nombre de la vista, etc. También es posible verificar parámetros:

```java
    @Test
    void testFindByNameNotFound() throws Exception {
        mockMvc.perform(get("/owners")
                .param("lastName", "Dont fine ME!"))
                .andExpect(status().isOk())
                .andExpect(view().name("owners/findOwners"));
    }
```

Un problema que podemos encontrar con la gestión que los métodos de Spring hacen de los mocks, es que al inyectarlos en una clase, como es el caso de clinicService en el ejemplo de arriba, el mock es reutilizado entre métodos. Esto tiene el inconveniente de que el número de veces que se utiliza el mock como argumento va aumentando entre llamadas de diferentes test, y métodos como then().should() en su signatura por defecto, fallan porque el mock parámetro no se utiliza una sola vez.

Par solucionarlo es necesario reiniciar el objetos testeado entre métodos:

```java
@AfterEach
void tearDown(){
    reset(clinicService);
}
```

Otra de las características que propociona MockMvc de Spring es la de validar los atributos de entrada, salida, status de la respuesta, redirección, modelo, o vista:

```java
@Test
void testNewOwnerPostNotValid() throws Exception {
    mockMvc.perform(post("/owners/new")
            .param("firstName", "Jimmy")
            .param("lastName", "Buffett")
            .param("city", "Key West"))
            .andExpect(status().isOk())
            .andExpect(model().attributeHasErrors("owner"))
            .andExpect(model().attributeHasFieldErrors("owner", "telephone"))
            .andExpect(model().attributeHasFieldErrors("owner", "address"))
            .andExpect(view().name("owners/createOrUpdateOwnerForm"));
}
``'

En el ejemplo, tanto el teléfono como la dirección forman parte del formulario, pero no se han añadido como parámetro post, y se comprueba su ausencia.