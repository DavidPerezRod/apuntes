# Profiles

Una utilidad que proporciona Spring para trabajar con test, es la de perfiles activos. Permite que un test, a la hora de inyectar dependencias, tenga en cuanta solo los test anotados con dicho perfil, o las clases sin un perfile específico.

A la hora de configurar todos los posibles candidatos, hay que tener en cuenta que si existen varios candidatos para un mismo perfil, anotados con @Profile para el perfil, o sin perfil declarado, será necesario marcar con @Primary el bean que se quiera utilizar por defecto.

Otro aspecto de utilidad es que una misma clase puede ser anotada con más de un perfil

```java
@Component
@Profile({"externalized", "laurel-properties"})
@Primary
public class PropertiesWordProducer implements WordProducer{

    private String word;

    @Value("${say.word}")
    public void setWord(String word){
        this.word=word;
    }

    @Override
    public String getWord() {
        return word;
    }
}

@TestPropertySource("classpath:yanny.properties")
@ActiveProfiles("externalized")
@SpringJUnitConfig(classes = PropertiesWordProducerTest.TestConfig.class)
class PropertiesWordProducerTest {

    @Configuration
    @ComponentScan("org.springframework.samples.petclinic.sfg")
    static class TestConfig{

    }

    @Autowired
    HearingInterpreter hearingInterpreter;

    @Test
    void whatIheard(){
        String word= hearingInterpreter.whatIheard();

        assertEquals("YaNNy", word);
    }
}
```
En esta sección también se ha incluido, cómo acceder a un fichero de propiedades en el que se hayan declarado valores que sean necesarios para el test. 

Dichos valores se inyectan mediante la anotación @Value, con notación SpEL, y se referecencian mediante @TestPropertySource y el classpath.