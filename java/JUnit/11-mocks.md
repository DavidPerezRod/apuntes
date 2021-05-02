# Mocks

Test double, es un objeto utilizado en los test para sustiuir al original, y modelar su comportamiento a nuestro antojo, de forma que podamos comprobar el comportamiento que tienen los objetos que interactúan con el original.

Este concepto es básico, ya que es la forma de aislar el código que estamos probando en los test unitarios, y asegurarnos que el comportamiento solo depende del objeto bajo test.

A los test double, se los suele denominar mocks, pero en realidad hay cinto tipos distintos:

* Dummy: objetos que necesitamos para ejecutar el test pero que no hacen nada, o da lo mismo lo que hagan porque no afectan al test.

* Stub: es como un dummy pues sus métodos no hacen nada, pero devuelven cierto valor que necesitamos para ejecutar nuestro test con respecto a ciertas condiciones.
  
* Spy: permite “espiar” el uso que se hace del propio objeto, por ejemplo el número de veces que se ejecuta un método, los argumentos que se le van pasando (se les puede hacer un assert), etc.

* Mock: es un stub en el que sus métodos sí implementan un comportamiento pues esperan recibir unos valores y en función de ellos devuelve una respuesta.

* Fake: es un objeto “correcta y completamente” implementado y que es totalmente equivalente al objeto real al que simula pero falsea algún comportamiento que no puede ser aplicado en los tests. Ejemplo típico: la implementación de un datasource que en lugar de conectarse a una base de datos MySQL tal y como hace el datasource de la aplicación real, se conecta a una base de datos embebida en memoria para que los tests sean más rápidos y portables.
