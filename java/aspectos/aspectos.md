6.1.1. Conceptos de AOP

Conceptos centrales de AOP

* "Aspect": Una modularización de una responsabilidad que atraviesa múltiples objetos. En Spring los aspectos pueden definirse en función de la estructura (paquete, métodos, modificadores), o de la anotación @Aspect.

* "Advice": Acción realizada por un aspecto. Tipos advise:
  * "alrededor" (@Aroung)
  * "antes" (@Before)
  * "después" (@After).

* "Join point": Un punto durante la ejecución de un programa. En Spring AOP, un punto de unión siempre representa la ejecución de un método. La información del Join point está disponible en el cuerpo del advice mediante objetos de tipo org.aspectj.lang.JoinPoint.

* Pointcut: Un predicado que coincide con los "Join point". Los advise se asocian con estas expresiones/pointcut y se ejecutan en los join point que coinciden con la definición del pointcut.

* Introduction (inter-type declaration): Declarar métodos o campos adicionales en un tipo. Spring AOP permite introducir nuevas interfaces (y su correspondiente implementación) en cualquier objeto proxy.

* Target Object: Objeto sobre el que se ejecuta el advise.

* Proxy AOP: Un objeto creado por el marco AOP para implementar los contratos de aspectos (asesorar ejecuciones de métodos y demás). En Spring Framework, un proxy AOP será un proxy dinámico del JDK o un proxy CGLIB. La creación de proxies es transparente para los usuarios de los estilos de declaración de aspectos basados en esquemas y @AspectJ introducidos en Spring 2.0.

```java
    //identificar la clase
    Logger logger = LoggerFactory.getLogger(proceedingJoinPoint.getTarget().getClass()); 

    //extraer la infromación de entrada
    ExecutedMethodLogInfo methodLogInfo = logInfoCollector.createLogExecutedMethodInfo(proceedingJoinPoint);
    Object value;
    try {
        //ejecutar el joinPoint
        value = proceedingJoinPoint.proceed();
    } catch (Throwable throwable) {
        //recoger la información de error de la ejecución (KO)
        logger.error(buildLoggerExceptionInfo(throwable, methodLogInfo));
        repositoryLogService.register(builRepositoryLogServiceExceptionInfo(throwable, methodLogInfo));
        throw throwable;
    }
    //recoger la información de salida de la ejecución (OK)
    methodLogInfo = logInfoCollector.completeLogSuccessExecutedMethodInfo(value, methodLogInfo);
    repositoryLogService.register(methodLogInfo);
    logger.info(methodLogInfo.toString(zertiMessageResolver));
    return methodLogInfo;
```


Tejido: Vinculación de aspectos con otros tipos de aplicaciones u objetos para crear un objeto asesorado. Esto puede hacerse en tiempo de compilación (utilizando el compilador AspectJ, por ejemplo), en tiempo de carga o en tiempo de ejecución. Spring AOP, al igual que otros frameworks AOP de Java puro, realiza el weaving en tiempo de ejecución.

Tipos de asesoramiento:

    Asesoramiento previo: Consejo que se ejecuta antes de un punto de unión, pero que no tiene la capacidad de impedir que el flujo de ejecución proceda al punto de unión (a menos que lance una excepción).

    Asesoramiento después de la devolución: Consejo que se ejecuta después de que un punto de unión se complete normalmente: por ejemplo, si un método retorna sin lanzar una excepción.

    Después de lanzar un consejo: Consejo que se ejecuta si un método sale lanzando una excepción.

    Consejo después (finally): Consejo que se ejecutará independientemente del medio por el que un punto de unión salga (retorno normal o excepcional).

    Consejo alrededor: Asesoramiento que rodea a un punto de unión, como la invocación de un método. Es el tipo de consejo más potente. El asesoramiento alrededor puede realizar un comportamiento personalizado antes y después de la invocación del método. También es responsable de elegir si se procede al punto de unión o se acorta la ejecución del método aconsejado devolviendo su propio valor de retorno o lanzando una excepción.

El asesoramiento en torno a un método es el tipo de asesoramiento más general. Dado que Spring AOP, al igual que AspectJ, proporciona una gama completa de tipos de asesoramiento, recomendamos que utilice el tipo de asesoramiento menos potente que pueda implementar el comportamiento requerido. Por ejemplo, si sólo necesita actualizar una caché con el valor de retorno de un método, es mejor que implemente un consejo after returning que un consejo around, aunque un consejo around puede conseguir lo mismo. El uso del tipo de consejo más específico proporciona un modelo de programación más simple con menos potencial de errores. Por ejemplo, no es necesario invocar el método proceed() en el JoinPoint utilizado para el consejo around, y por lo tanto no puede fallar al invocarlo.

En Spring 2.0, todos los parámetros de asesoramiento están tipados estáticamente, de modo que se trabaja con parámetros de asesoramiento del tipo apropiado (el tipo del valor de retorno de la ejecución de un método, por ejemplo) en lugar de matrices de objetos.

El concepto de puntos de unión, que coinciden con los pointcuts, es la clave de la AOP que la distingue de las tecnologías más antiguas que sólo ofrecen interceptación. Los pointcuts permiten orientar los consejos independientemente de la jerarquía orientada a objetos. Por ejemplo, un consejo en torno a la gestión declarativa de las transacciones puede aplicarse a un conjunto de métodos que abarcan varios objetos (como todas las operaciones comerciales de la capa de servicios).

Traducción realizada con la versión gratuita del traductor www.DeepL.com/Translator