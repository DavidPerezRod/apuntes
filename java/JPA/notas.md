* @Entity. Es la anotación principal para marcar una clase como entidad JPA. Es un anotació a nivel de clase
* @Table. Anotación que indica la tabla solbre la que actúa la entidad. @Table(name = "nombre de la tabla")
* @Id campo clave de la clase
* @GeneratedValue (Strategy = "")--> secuencial para la clave. Además se indica la estrategia de generación de el valor
* @Column(name = ""). Cuando el nombre del atributo no coincide exáctemente con el de la columna. Aquí se indica el nombre de la columna de BBDD
* @Temporal. Indica que el campo es de hora/fecha/timestamp
* @Query cuando se implementa algún método fuera de la interfaz CrudRepository, en el que se quiere especificar la consulta que debe ejecutar.
* @Transactional. Anotación sobre el método que lanza la acción sobre el DAO.
* Uso de @Transactional
  
  El siguiente texto está extraído del enlace siguiente de la junta de andalucía: http://www.juntadeandalucia.es/servicios/madeja/contenido/recurso/170


  Tradicionalmente, los desarrolladores de J2EE han tenido dos opciones para la gestión de transacciones: las transacciones globales o locales. Las transacciones globales son gestionadas por el servidor de aplicaciones, utilizando el API de transacciones Java (JTA). Las transacciones locales son recursos específicos: el ejemplo más común sería una transacción asociada con una conexión JDBC. Esta elección tiene profundas implicaciones. Por ejemplo, las transacciones globales proporcionan la capacidad de trabajar con múltiples recursos transaccionales (bases de datos relacionales y, normalmente, las colas de mensajes). Con las transacciones locales, el servidor de aplicaciones no está involucrado en la gestión de transacciones y no puede ayudar a asegurar la corrección a través de múltiples recursos.
Transacciones globales

Las transacciones globales tienen una desventaja importante, en ese código tiene que usar JTA, y JTA es una API difícil de utilizar (en parte debido a su modelo de excepción). Además, un UserTransaction JTA normalmente tiene que proceder de JNDI. Por lo tanto, tenemos que utilizar tanto JNDI como JTA. Obviamente, todo uso de las transacciones globales limita la reutilización de código de la aplicación, ya que JTA normalmente sólo está disponible en un entorno de servidor de aplicaciones. Anteriormente, la mejor forma de utilizar las transacciones globales fue a través de EJB CMT (transacción administrada por contenedor).

CMT es una forma de gestión de transacciones declarativa (a diferencia de la gestión de transacciones programática). EJB CMT elimina la necesidad de transacción relacionados con búsquedas JNDI aunque, por supuesto, el uso de EJB requiere el uso de JNDI. Se elimina la mayor parte de la necesidad (aunque no del todo) de escribir código Java para el control de las transacciones. El inconveniente importante es que la CMT está ligada a JTA y un entorno de servidor de aplicaciones. Además, sólo está disponible si se opta por aplicar la lógica de negocio en EJB, o al menos detrás de una fachada de EJB transaccional. Lo negativo en relación a EJB es que, en general, no se trata de una propuesta atractiva, especialmente en la cara de las alternativas de peso para la gestión de transacciones declarativa.
Transacciones locales

Las transacciones locales pueden ser más fáciles de usar, pero tiene desventajas importantes: no pueden trabajar a través de múltiples recursos transaccionales. Por ejemplo, el código que gestiona las transacciones utilizando una conexión de JDBC no se puede ejecutar en una transacción JTA global. Otra desventaja es que las transacciones locales tienden a ser invasoras en el modelo de programación.

Spring resuelve estos problemas. Permite a los desarrolladores de aplicaciones para utilizar un modelo de programación coherente en cualquier entorno. Debe escribir el código una vez, y pueden beneficiarse de diferentes estrategias de manejo de transacciones en diferentes entornos. El marco de Spring proporciona la gestión de transacciones declarativas y programáticas. La gestión de transacciones declarativa es la preferida por la mayoría de los usuarios y se recomienda en la mayoría de los casos.

Con la gestión de transacciones programática, los desarrolladores trabajan con la abstracción de la transacción Spring Framework, que puede correr sobre cualquier infraestructura de operación subyacente. Con el modelo de declaración preferido, los desarrolladores suelen escribir poco o ningún código relacionado con la gestión de transacciones y, por lo tanto, no dependen de la transacción de Spring Framework.
Estrategia de transacción

La clave para la captación de transacciones de Spring es la noción de una estrategia de operación. Una estrategia de operación se define por la interfaz de org.springframework.transaction.PlatformTransactionManager, que se muestra a continuación:

public interface PlatformTransactionManager {

    TransactionStatus getTransaction(TransactionDefinition definition)
        throws TransactionException;

    void commit(TransactionStatus status) throws TransactionException;

    void rollback(TransactionStatus status) throws TransactionException;
}

Tengamos en cuenta que, de acuerdo con la filosofía del marco de Spring, PlatformTransactionManager es una interfaz y, por lo tanto, puede ser fácilmente implementada cuando sea necesario. Tampoco está vinculada a una estrategia de búsqueda como JNDI. Las implementaciones PlatformTransactionManager se definen como cualquier otro objeto (o bean) en el contenedor de Spring Framework COI. Este beneficio sólo se hace con un objetivo de abstracción. Incluso cuando se trabaja con JTA, los códigos de transacción pueden ser probados con mayor facilidad que si se utiliza directamente JTA.

Una vez más, en consonancia con la filosofía de Spring, el TransactionException que puede ser lanzado por cualquiera de los métodos de la interfaz de PlatformTransactionManager siendo unchecked (es decir, se extiende la clase java.lang.RuntimeException).

Los fallos en la infraestructura de transacción son casi siempre definitivos. En pocos casos el código de aplicación puede recuperarse de un error de transacción, el desarrollador de aplicaciones aún puede elegir capturar y manejar TransactionException. El punto importante es que los desarrolladores no están obligados a hacerlo.

El método getTransaction (..) devuelve un objeto TransactionStatus, en función de un parámetro TransactionDefinition. El TransactionStatus devuelto podría representar una operación nueva o ya existente (si hay una transacción coincidente en la pila de llamadas actual con la implicación de que un TransactionStatus se asocia con un hilo de ejecución).

La interfaz de TransactionDefinition especifica:

    Aislamiento: el grado de aislamiento de esta transacción sobre otras transacciones. Por ejemplo, ¿esta transacción puede verse comprometida por la escritura de otras transacciones?
    Propagación: en general, todo código que se ejecuta dentro de un ámbito de transacción se ejecutará en esa transacción. Sin embargo, hay varias opciones que especifican el comportamiento si se ejecuta un método de transacción cuando el contexto de la transacción ya existe: por ejemplo, sólo tiene que seguir corriendo en la operación existente (el caso más común), o la suspensión de la operación existente y la creación de una nueva transacción. Spring ofrece todas las opciones de transacción de propagación familiares de EJB CMT.
    Tiempo de espera: ¿cuánto tiempo tiene la transacción para ejecutarse antes del tiempo de espera (y automáticamente se deshace de la infraestructura de transacción subyacente).
    Estado de sólo lectura: una transacción de lectura única no puede modificar ningún dato. Las transacciones de sólo lectura pueden ser una optimización de utilidad en algunos casos (como cuando se utiliza Hibernate).

La interfaz de TransactionStatus proporciona una forma sencilla para el código de transacción para controlar la ejecución de transacciones y estado de la transacción de consulta. Los conceptos deben estar familiarizados, ya que son comunes a todas las API de transacciones:

public interface TransactionStatus {

    boolean isNewTransaction();

    void setRollbackOnly();

    boolean isRollbackOnly();
}

Independientemente de si se opta por la gestión de transacciones declarativa o programática en Spring, la definición de PlatformTransactionManager correcta es absolutamente esencial. En Spring , esta importante definición generalmente se realiza mediante la inyección de dependencias. Las implementaciones PlatformTransactionManager normalmente requieren el conocimiento del entorno en el que trabajan: JDBC, JTA, Hibernate, etc
Transacciones declarativas

La mayoría de los usuarios de Spring eligen la gestión de transacciones declarativa. Es la opción con el menor impacto en el código de aplicación, y por lo tanto es más coherente con los ideales de un contenedor no invasivo de peso ligero.

Puede ser útil comenzar por considerar EJB CMT y explicar las similitudes y diferencias con la gestión de transacciones declarativa en el marco de Spring. El enfoque básico es similar: es posible especificar el comportamiento de la transacción (o falta de ella) hasta el nivel de método individual. Es posible hacer una llamada a setRollbackOnly () dentro de un contexto de transacción si es necesario. Las diferencias son:

    A diferencia de EJB CMT, que está vinculado a JTA, la gestión de transacciones declarativa Spring Framework funciona en cualquier entorno. Puede trabajar con JDBC, JDO, Hibernate u otras transacciones , con los cambios de configuración solamente.
    El framework Spring permite la gestión de transacciones declarativas que deben aplicarse a cualquier clase, no sólo a las clases especiales, tales como EJB.
    Ofrece normas de reversión declarativa: una característica que no tiene equivalente EJB. El retroceso se puede controlar de forma declarativa, no sólo mediante programación.
    Spring da la oportunidad de personalizar el comportamiento transaccional, mediante AOP. Por ejemplo, si desea insertar un comportamiento personalizado en el caso del desmantelamiento de la transacción, se puede. También se puede añadir el asesoramiento arbitrario, junto con el asesoramiento de transacciones. Con EJB CMT, no tiene manera de influir en la gestión de otra transacción del contenedor de setRollbackOnly ().
    No es compatible con la propagación de los contextos de transacciones a través de las llamadas remotas, así como con servidores de alta aplicación final. Si se necesita esta característica,es recomendable utilizar EJB.

El concepto de las normas de reversión es importante ya que nos permiten especificar qué excepciones deberían causar el rollback. Nos permite especificar esta declaración, en la configuración, no en el código Java
Rollback de las transacciones

Esta sección describe cómo se puede controlar la vuelta atrás de las transacciones de manera declarativa simple. El método recomendado para indicar a la infraestructura de transacción de Spring que el trabajo de una transacción se revierte es una excepción de código que se ejecuta actualmente en el contexto de una transacción. La infraestructura de transacciones de Spring capturará cualquier excepción no controlada, y la insertará en la pila de llamadas, siendo la marca para la vuelta atrás.

Sin embargo, tenga en cuenta que la infraestructura del código de transacción, por defecto, sólo marca una transacción para la reversión en tiempo de ejecución, las excepciones sin control, es decir, cuando la excepción que se tiene es una instancia o subclase de RuntimeException. Las excepciones controladas que se lanzan desde un método de transacción no darán lugar a la vuelta atrás.

Se puede configurar qué tipo de excepción se marca para deshacer una transacción. A continuación se muestra un fragmento que indica cómo se puede configurar una marca de reversión en el fichero de configuración XML, sobre un tipo de excepción específica:

<tx:advice id="txAdvice" transaction-manager="txManager">
  <tx:attributes>
     <tx:method name="get*" read-only="false" rollback-for="NoProductInStockException"/>
     <tx:method name="*"/>
  </tx:attributes>
</tx:advice>

Otra forma de indicar a la infraestructura de transacción que se requiere una reversión es hacerlo mediante programación. Aunque es sencillo, de esta manera es bastante invasiva, y crea dependencias a la infraestructura de su código de transacción. A continuación se muestra un ejemplo con un fragmento de código que hace rollback:

public void resolvePosition() {
    try {
        // some business logic...
    } catch (NoProductInStockException ex) {
        // trigger rollback programmatically
        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
    }
}

Configuración de <tx:advice/>

Los diferentes escenarios de una transacción que pueden ser especificados utilizando la etiqueta <tx:advice/> son los siguientes. El valor predeterminado <tx:advice/> es:

    El ajuste de propagación es REQUIRED
    El nivel de aislamiento es DEFAULT
    La transacción es de lectura / escritura
    Los valores predeterminados del sistema de operación subyacente para el tiempo de espera de una transacción, o ninguno, si los tiempos de espera no son compatibles
    Cualquier RuntimeException activará la reversión, y cualquier excepción controlada no

Además del enfoque basado en XML declarativo a la configuración de la transacción, también se puede utilizar un enfoque basado en la anotación en la configuración de la transacción. Declarar la semántica de transacciones directamente en el código fuente Java sitúa las declaraciones mucho más cerca del código afectado y, en general, no hay mucho peligro de acoplamiento indebido, puesto que el código que está destinado a ser utilizado transaccionalmente es casi siempre desplegado.

La anotación @transaccional puede ser colocada antes de una definición de interfaz, un método en una interfaz, una definición de clase, o un método público en una clase. Sin embargo, tenga en cuenta que la mera presencia de la anotación @transaccional no es suficiente para convertir en realidad en el comportamiento transaccional. En este caso es necesaria la presencia del elemento para el funcionamiento de los interruptores en el comportamiento transaccional.

La recomendación es que sólo se anoten clases concretas con la anotación @transaccional, en lugar de anotar las interfaces. Por supuesto, se puede colocar la anotación @transaccional en una interfaz (o un método de interfaz), pero esto sólo funcionará correctamente si está utilizando una interfaz basada en proxies.

El hecho de que las anotaciones no se heredan significa que si usted está utilizando una clase basada en un proxy, entonces, la configuración de transacción no será reconocida por la clase proxy basada en la infraestructura y el objeto no se verá envuelto en un proxy transaccional (que sería un error) .
Configuración de @Transactional

La anotación @transaccional es un metadato que especifica que una interfaz, clase o método debe tener semántica transaccional. La configuración por defecto @transaccional es:

    El ajuste de propagación es PROPAGATION_REQUIRED
    El nivel de aislamiento es ISOLATION_DEFAULT
    La transacción es de lectura / escritura
    Los valores predeterminados del sistema de operación subyacente para el tiempo de espera de una transacción, o ninguno, si los tiempos de espera no son compatibles
    Cualquier RuntimeException activará la reversión, y cualquier excepción controlada no