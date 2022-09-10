
* Hacer una jerarquía de excepciones a partir de Runtime Excepcion para que no sean checked
  * un problema que veo de hacer todo unchecked, es que para las checked exception va a ser difícil saber cuándo el error que se ha producido es por problemas de negocio (problemte un error serie 400) o cuando es por un error de programación (500)
  * la posible solución es ser muy extrictos en las validaciones ¿por anotaciones?. En ese caso todos los errores de validación lanzarían una excecpión de tipo **MethodArgumentNotValidException** que ya permitiría diferenciar entre un error 400 y un 500.
  * la jerarquía de excepciones debe ser heredable/oculta para que los micros puedan reutilizarla, redefinirla.**¿lo hacemos a través del cliente?**
  * ¿hacer dos jerarquías? Excepciones/notificaciones. La primeras para log, las segundas para errores 400/200/300
* La complejidad de la jerarquía vendrá dada por:
  * complejidad del sistema que se está desarrollando
  * granularidad de tipos de excepción
  * Estrategia a seguir en la diferenciación de excepciones: por funcionalidad, por capa, por subsistema asociado, etc
* La JA propone el uso de ThrowsAdvice para sus excepciones críticas del sistema.
  * Fallos de configuración.
  * Caídas del sistema.
  * Problemas relacionados con la instalación.
  * Base de datos no disponible.
* También proponen:
  * **ServicioDeExcepciones**: clase utilizada para realizar un tratamiento centralizado de las excepciones ocurridas en la capa de negocio y modelo del sistema. Será un bean manejado por Spring. Esta clase implementa los métodos necesarios para particularizar los mensajes mostrados en el caso de excepciones generadas por restricciones de base de datos.

  * **ExceptionHelper**: clase con utilidades genéricas para la manipulación de la jerarquía de clases de excepciones del Sistema.

    Todas las excepciones del sistema tienen que ser registradas en el momento en que son encapsuladas por una excepción de la jerarquía de excepciones establecida. Para su registro se hace uso del sistema de Log. Cada una de las excepciones tiene características distintas para las siguientes propiedades:

    * Nivel de criticidad con el que se graba en el Log.
    * Indicador de impresión de la pila de errores.
    * Mensaje de error impreso en el Log para usuarios administradores (con fines de registro).
    * Mensaje de error para mostrar a los usuarios.
  * Plantean que la jerarquía de excepciones lleve implícito un nivel de log. Ver su jerarquía, tiene sentido.
  * Se propone la siguiente nomenclatura del código de error:

    <sistema>.error.<subsistema>|comun.<descriptor I de error>.<descriptor II de error>

    **con esta segunda parte no estoy muy de acuerde**
    Si el error es generado por una restricción de base de datos la nomenclatura deberá seguir la siguiente estructura:

    <sistema>.validacion.constraint.<nombre_de_constraint>
excepciones.properties

* Evitar en la medida de lo posible el uso de excepciones para los casos en los que el error se puede producir con relativa frecuencia, ya que su coste en procesamiento no es trivial. Es mejor dejar que salte a controlarla... **pero claro, para eso hay que transformarla en unchecked**
* Aquí si parece que hay un cambio programático. Fowler dice que si se lanza una excepción por validación, realmente se está esperando el error,y por lo tanto no debería ser tratado con la excecpón. **aquí es donde tiene sentido utilizar el wrapper de validaciones en lugar de la anotación de validaciones**.
  * a favor desde el punto de vista de la usabilidad, es que con la clase wrapper se devuelve el error de todos los campos que están fallando.
  * **tendría sentido hacer una anotación que permitiese cortar entre capas, de forma que se lance una excepción genérica para que no se tenga que programar el if notification.asErrors() en cada clase de controlador**
  * Lo que plantea en el artículo es un recurso adicional al manejo de excepciones. Lo que quiero decir es que al pasar como parámetro un objeto en el que se van guardando los mensajes de error en lugar de las excepciones, obliga a:
  * pasar un parámetro adicional entre todas las capas
  * controlar en algún punto el corte que se produce entre capas y traducir estos mensajes a otros entendibles por la capa siguiente en la pila.
  * podría ser conveniente para los errores de negocio, pero no para los de programa. 
* **a tener en cuenta la clase de validación java produce un error 400 si el fallo se produce en los parámetros de entrada o un 500 si se produce en el return
* si adoptásemos la solución de los wrapper, ésta debería ser implementable por bloques para no afectar a los tiempos de desarrollo de los micros que ya tenemos. Es decir primero excepciones genéricas que sean compatibles con el sistema que ya tenemos, y luego añadir el sistema de notificación. Una forma podría ser que las excepciones trabajasen tanto con pojos como con clases de validación. Bien por tipos genéricos, bien por interfaces.

Las excepciones deben proporcionar el contexto adecuado para determinar el motivo y la ubicación del error. Java no indica el cometido de la función fallida, es un tipo de información que sería conveniente añadir para mostrar en log, pero tal vez el service, debería escribirla a log y traducirla en una salida para el controlador.

Patrón del caso especial de Martin Fowler.

Y si la parte de expcepciones fuera genérica para el advisor y solo recibiera los mensajes del servicio

**tratamientos específico para cada tipo de excepción y cada tipo de micro**

https://docs.spring.io/spring-framework/docs/current/reference/html/core.html#validation