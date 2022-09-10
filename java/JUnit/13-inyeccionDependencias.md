[< indice](./0-index.md)

# Inyección de dependencias

Son varias las formas en las que se pueden establecer las dependecias en los test. Se puede hacer con JUnit4, JUnit5, Springframework, por medio de clases internas, o mediante componentScan de las clases de configuración de la aplicación.

* JUnit4. En este caso no se pueden inyectar directamente dependencias, en su lugar lo aconsejable, es crear un método @Before, en el que se asigna el tipo con el que ser quiere trabajar:

```java
public class HearingInterpreterTest{

    HearingInterpreter hearingInterpreter;

    @Before
    public void setUp() throws Exception{
        hearingInterpreter = new HearingInterpreter(new LaurelWordProducer());
    }

    @Test
    public void whatIheard(){
        String word= hearingInterpreter.whatIheard();

        assertEquals ("Laurel", word);
    }
}
```

* JUnit5. En JUnit5 aparece la anotación @SpringJUnitConfig que aúna @ContextConfiguration y @ExtendsWith

```java
@SpringJUnitConfig(classes = {BaseConfig.class, LaurelConfig.class})
class HearingInterpreterTest {

    @Autowired
    HearingInterpreter hearingInterpreter;

    @Test
    void whatIheard() {
        String word= hearingInterpreter.whatIheard();
        assertEquals("Laurel", word);
    }
}
```

* Spring. Aquí los mecanismos de inyección de dependencias son propios de Spring. En este caso hay que indicar las clases de configuración para la inyección de dependencias.

```java
@RunWith(SpringRunner.class)
@ContextConfiguration(classes= {BaseConfig.class, LaurelCongig.class})
public class HearingInterpreterTest{

    @Autowired
    HearingInterpreter hearingInterpreter;

    @Test
    public void whatIheard(){
        String word= hearingInterpreter.whatIheard();

        assertEquals ("Laurel", word);
    }
}
```

* Inner Class. Esta es otra forma de configurar los beans que se van a inyectar en el test, y hacerlo de una forma más compacta, además de permitir sobrescribir otras configuraciones existentes:

```java
@SpringJUnitConfig(classes = HearingInterpreterInnerClassTest.TestConfig.class)
class HearingInterpreterInnerClassTest {

    @Configuration
    static class TestConfig{

        @Bean
        HearingInterpreter hearingInterpreter(){
            return new HearingInterpreter(new LaurelWordProducer());
        }
    }

    @Autowired
    HearingInterpreter hearingInterpreter;

    @Test
    void whatIheard(){
        String word= hearingInterpreter.whatIheard();

        assertEquals("Laurel", word);
    }
}
```

* Mediante ComponentScan

En este caso, también se declara una clase interna, pero ésta no declara la configuración, en su lugar, hace un component scan de las clases de configuración que utilizará la aplicación.

```java
@SpringJUnitConfig(classes = HearingInterpreterCSTest.TestConfig.class)
class HearingInterpreterCSTest {

    @Configuration
    @ComponentScan("org.springframework.samples.petclinic.sfg")
    static class TestConfig{}

    @Autowired
    HearingInterpreter hearingInterpreter;

    @Test
    void whatIheard(){
        String word= hearingInterpreter.whatIheard();
        assertEquals("Laurel", word);
    }
}
```

