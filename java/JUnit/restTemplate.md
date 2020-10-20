# RestTemplate

Para este caso, SpringBoot ya define sus propias clases de test que facilitan probar este tipo específico de controladores.

En primer lugar, hay que anotar la clase con @SpringBootTest, y después inyectar la clase TestRestTemplate:

```java
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BeerControllerITest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    void testListBeer() {
        BeerPagedList beerPagedList= testRestTemplate.getForObject("/api/v1/beer/", BeerPagedList.class);
        assertThat(beerPagedList.getContent()).hasSize(2);
    }
} 
```
