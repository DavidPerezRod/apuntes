# Aspectos

**Términos:**

* Aspect: es una funcionalidad genérica aplicable a múltiples objetos. Cada aspecto trata una sola funcionalidad.
* Join point: es el punto de ejecución donde se puede aplicar un aspecto como la llamada a un método, su retorno o el acceso a una propiedad.
* Advice: es la acción que se realiza en un pointcut.
* Pointcut: es una expresión que busca joint points, tiene un advice asociado que se ejecuta en todos los joint points que concuerdan con la expresión.
weaving: proceso que aplica los aspectos a las clases, puede ser en tiempo de compilación o en tiempo de ejecución.

Elementos transversales a los que se puede aplicar:

* trazas
* métricas de rendimiento
* seguridad
* cachés
* transacciones


# Excepciones


* @ControllerAdvice: permite declarar métodos relacionados con el manejo de excepciones que serán compartidas entre múltiples controladores. Evita la duplicidad de código y la generación de jerarquías, promoviendo el tratamiento homogéneo de excepciones.

    Esta anotación ofrece además la posibilidad de aplicarse de forma específica a un conjunto de controladores, en lugar de a todos los controladore de la aplicación. Se le puede pasar como parámetro el conjunto de paquetes, clases, o anotaciones, a cuyos controladores se les aplicará el control de excepciones.

    ```java
    @ControllerAdvice(basePackages={"com.myPackage.mySubpackageA","com.myPackage.mySubpackageA"})

    @ControllerAdvice(assignableTypes = {ThisInterface.class, ThatInterface.class})

    @ControllerAdvice(annotations= MyAnnotation.class)
    ```

    Esta anotación da soporte, a su vez, a 3 anotaciones distintas:

    * @ExceptionHandler: Los métodos anotados de esta manera se encargarán de manejar las excepciones 
    * @ModelAttribute: Esta anotación permite completar la información de un modelo expuesto vía web view.
    * @InitBinder: Permite inicializar el WebDataBinder que se utilizará para inicializar los formularios asociados al controlador.

* @RestControllerAdvice: el funcionamiento es casi idéntico al anterior, pero su uso está enfocado al API Rest

*  @ExceptionHandler: anota un método como encargado de realizar acciones en caso de que se lance una tipo específico de excepción. Se anota el método, y se especifica como parémetro de la anotación el tipo de excepción. **Nivel de retención runtime**

```java
@ExceptionHandler(NullPointerException.class)
public void nullPointerHandler(){
    logger.log(Level.ERROR, "NullPointerException!!!");
}
```