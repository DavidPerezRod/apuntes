# Behaviour Driven Development

Habitualmente se abrevia como BDD, y su origen se remonta al año 2004 con Dan North. Su idea original era que el BDD debía ayudar a entender el TDD. 

En este sentido, los test unitarios se entienden como especificación de comportamientos, y por este motivo, sus nombre deben tomar el aspecto de una frase. Se trata de un cambio conceptual en cómo se acerca uno a los test.

El principal mecanismo de BDD, es escribir los test unitarios con la estructura given-when-then, que debe ayudar a pensar en cómo estructurar nuestros test:

* given: implica el escenario de partida para verificar un comportamiento. De modo que a nivel de código será la sección en la que se configuren los elementos que van a servir para poner a prueba el comportamiento.
  
* when: es el comportamiento/requisito que se quiere probar. A nivel de código es la acción/método que se quiere probar

* then: el comportamiento esperado según el requisito para la acción ejecutada con las circunstancia de partida. A nivel de código se verifican los resultados esperados.

Por su parte Mockito, ha añadido algo de soporte a esta aproximación:

* el método estático given, permite configurar mocks

* el método statico then, permite verifiar la interacción de mocks. 

Ejemplo de uso:

```java
@Test
void findByIdBddTest() {
    Speciality speciality= new Speciality();
    given(specialtyRepository.findById(1L)).willReturn(Optional.of(speciality));

    //when
    Speciality foundSpecialty = service.findById(1L);

    //then
    then(specialtyRepository).should().findById(anyLong());
    then(specialtyRepository).should(times(1)).findById(anyLong());
    then(specialtyRepository).shouldHaveNoMoreInteractions();
}
```