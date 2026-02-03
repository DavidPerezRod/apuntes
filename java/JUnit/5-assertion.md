# ASSERTIONS

En primer lugar, explicar que la traducción formal de assert es afirmar. Es decir son expresiones en las que afirmamos el valor esperado para una determinada variable.

Precisamente por este motivo, porque son sentencias en las que se quiere obtener una aproximación al lenguaje natural, existen varias librerías que proporcionan funciones assert, cuyo objetivo es el mismo, pero las funciones utilizan diferente aproximación al lenguaje natural, además de proporcionar assetions específicas, por ejemplo para json. Algunos ejemplos son:
    * java Assert
    * Hamcrest: Actualmente su interés reside fundamentalmente en que es una librería que fue ampliamente utilizada, y puede ser frecuente encontrarla en el mantenimiento de algunos sitemas, pero no ha sido actualizada desde el 2015.
    * AssertJ: proporciona sentencias assert más descriptivas que el api java.
    * Truth

 En [arquitecturajava](https://www.arquitecturajava.com/java-assert-librerias-y-enfoques/) profundizan algo más sobre el tema

Casi todo los métodos assert están sobrecargados para admintir un mensaje de error, es conveniente utilizar esta segunda signatura para que los mensajes de error sean más claros.

Algunas de las características que se incluyen en JUnit5 para el tratamiento de assertions son:

* incorpora el uso de lambas, incluso en assertions para excepciones (assertThrows) y timeouts (assertTimeout).

Un ejemplo de código de assertThrows presentará un aspecto como el siguiente:
<pre>
    <code>
    @Test
    @DisplayName("method to test handler exception in oups handler")
    void methodHandlerExceptionTest() {
        assertThrows(ValueNotFoundException.class, ()-> controller.oupsHandler());
    }
    </code>
</pre>

en el caso del timeout, existen dos tipos de asserts, uno que comprueba el tiempo, pero espera a que se ejecute el hilo hasta obtener la respuesta, y otro al que se le puede asignar un tiempo preventivo, que una vez transcurrido forzara la finalización del test. Los ejemplos de código son los siguientes:

<pre>
    <code>
    @Test
    void testTimeOut() {
        assertTimeout(Duration.ofMillis(2000), ()->Thread.sleep(2500));
        System.out.println("I got here");
    }

    @Test
    void testTimeOutPreemptively() {
        assertTimeoutPreemptively(Duration.ofMillis(100), () -> Thread.sleep(3000));
        System.out.println("I got here 234342434");
    }
    </code>
</pre>

Lo que se observa en estos ejemplos, es que aunque los dos fallan porque el sleep del thread es superior al tiempo de espera del assert, el segundo no espera a la finalización del hilo, falla a los 100 milisegundos

* permite crear grupos de assertions. Esta característica permite que se ejecuten todas en el test, indpendientemente de que falle alguna de ellas. En versiones anteriores, el test se detenía cuando encontraba el primer fallo.
<pre>  
    <code>assertAll("Test Props Set",
                () -> assertEquals( "Joe", person.getFirstName()),
                () -> assertEquals( "Doe", person.getLastName())
        );
    </code>
</pre>

[Un catálogo detallado de las assertions de Junit5](https://junit.org/junit5/docs/5.0.1/api/org/junit/jupiter/api/Assertions.html).
