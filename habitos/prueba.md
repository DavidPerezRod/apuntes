1. ¿Qué novedades trae Java 8? Explica alguna/s de ella/s
En mi opinión las novedades más iportantes de Java 8 son: 
  * Expresiones lambda: mecanismo para implementar los métodos de las interfaces funcionales como funciones anónimas.   
  * Streams como mecanismo que permite realizar operaciones declarativas sobre colecciones, ya sea de forma secuencial o paralela.
  * La nueva api Java.time que permite el manejo de fechas y horas de forma más sencilla
  
También introudce otras novedades de métodos por defecto y métodos estáticos para interfaces, el operador :: para hacer referencia a método

2. Dada la lista: implementa un método Java 8 que...
3. En qué te fijas a la hora de hacer un code review a un compañero

Cuando voy a hacer un code review, trato de seguir los siguientes pasos, aunque a un nivel intuitivo, sin un esquema previo: 
  * trato de entender bien qué es lo que se pretende lograr a nivel funcional
  * Después le pido que me explique con sus palabras cómo ha estructurado el código para conseguirlo
  * verifico que el código sea claro y fácil de entender.
  Para este punto suelo fijarme ámbito de declaración de variables, tratamiento de iteraciones, nombres de variables y métodos.
  * Después me fijo en elementos básicos de diseño, como repetición de código, independencia funcional y cohesion y acoplamiento
  * Por último presto atención a la cobertura de test.
4. SCRUM. ¿Has trabajado con ello? ¿cuéntanos en qué consiste y qué eventos recuerdas?

Sí, he trabajado con metodología SCRUM. En lugar de dar una definición domgmática de qué es, lo definiría  como una metodología basada en un conjunto de rituales repetidos secuencialmente, destinados a reducir la complejidad del desarrollo y la identificación precoz de problemas y desviaciones en los hitos.

Los principales eventos que recuerdo son:

* refinamiento. El ritual en el que se detalla las siguientes tareas del backlog que van a ser abordadas y su estimación
* sprint planing. Se planifican las tareas que se van a aboradar en el sprint
* sprint, que es el periodo durante el cual se avanza en el desarrollo y entrega de un hito.
* daily. Reuniones diarias en las que se identifican bloqueos que pueden repercutir en la consecución de los hitos planificados
* demo. Acto en el que se presentan al responsable del producto los hitos conseguidos
* retrospectiva. Reunión del equipo de desarrollo destinada a idnetificar áreas de mejora
  
5. ¿Qué modificadores de acceso o visibilidad conoces en Java?

Los modificadores de acceso a atributos y métodos que conozco en Java, de más restrictivos a menos restrictivos son:
* private. Solo se puede acceder desde la propia clase
* acceso por defecto o de paquete, solo se puede acceder a los elementos de la propia clase o paquete
* protected, que permite el acceso desde la propia clase, desde el paquete o desde el ámbito de una clase hija
* público que permite el acceso desde la clase, el paquete, una subclase u otro paquete.

6. Diferencias entre una clase abstracta y un interfaz. 

Creo que las diferencias entre una y otra, más allá de las cuestiones técnicas (que en una clase abstracta puede implementar métodos y una interfaz no, que ninguna de las dos pueda ser instanciada directamente, o de la sintaxis específica de cada una) es la función que desempeñan a nivel de diseño. Una clase abstracta trata de definir el comportamiento por defecto de una jerarquía de objetos, mientras que una interfaz define el contrato de uso de un objeto.

7. ¿Qué es Maven y para qué se usa? Diría que maven es una utilidad para gestionar los principales aspectos de la compilación de proyectos Java. Sin embargo, es cierto que Maven no solo compila, ya que también gestiona las dependencias, empaqueta, despliegua el software, y ejecuta los test.
8. ¿Qué es git y para que se utiliza? Lista los comandos Git que conozcas.

Git es una herramienta de control de versiones distribuida.

Los principales comandos son:
* clone. Clona un repositorio en un directorio
* pull. Obtiene los últimos cambios del repositorio remoto.
* status. Muestra el estado en el que se encuentran los elementos del repositorio local
* stash. Hace una copia de los elementos del espacio de trabajo, y lo restaura al estado previo de esos cambios. 
* add. Añade los elementos nuevos al espacio de trabajo.
* commit. Añade los cambios existentes al repositorio local.
* push. Envía los cambios del repositorio local al repositorio remoto.
* rebase. Permite varias opciones sobre la historia del repositorio, tanto a nivel de comentarios de commits como del contenido de los propios commits.
* cherry-pick. Permite agrupar un conjunto producidos en uno o más commits y aplicarlos en otro punto.
* checkout. Permite alternar entre ramas del mismo repositorio.
* branch. Ayuda a gestionar las ramas de un repositorio
* diff. Muestra los cambios hecho a un fichero.

9. ¿Qué es un mock y por qué lo querías usar? 
Un mock, es un objeto destinado a simular el comportamiento de otro objeto en entorno productivo. Se utiliza en test unitarios o de integración con el fin de verificar el funcionamiento del software.

10. ¿Cómo le explicarías a alguien que no sabe lo que es Spring que le puede aportar en sus
proyectos? Spring es un framework de desarrollo java, compuesto por módulos/proyectos que proveen utilidades por defecto para evitar su desarrolladas de 0. 
Aunque probablemente en un inicio su ámbito estaba más ligado a los aspectos que componen su core, como la inyección de dependencias, programación orientada a aspetos, validación de datos, y utilidades JEE de acceso a datos y servicios web, en la actualidad dispone de múltiples proyectos para la integración de Java con las últimas tendencias tecnológicas

11. ¿Conoces la diferencia entre Spring y Spring Boot?. En realidad no creo que se tengan que tratar en términos de parecido o diferencia, considero que Spring Boot es una extensión de Spring, que facilita la tarea de configuración de la interconexión con elementos externos, como BBDD, Servidores de aplicaciones, balanceadores, entornos cloud, etc. 
Lo hace por medio de un starter en el que se definen las dependencias de las que va a constar la aplicación
Adicionalmente también permite empaquetar todos estos elementos en un .jar.

12. ¿Sabes qué es CQRS? ¿y Event Sourcing?No conocía los conceptos, he visto que el primero es un patrón de diseño que separa la responsabilidad de lectura y modificación de datos, de forma que métodos en un mismo objeto no pueden hacer las dos cosas a la vez. 
En cuanto al concepto de Event Sourcing, no lo conocía como tal, pero si el concepto de la representación de un estado como secuencia de eventos. De forma que si se almacenan estos se puede reproducir cualquier estado previo del sistema

12. Diferencias entre un IaaS y un PaaS. ¿Conoces alguno de cada tipo?. Sí son los acrónimos de Infraestructures as a Service y Platform as a Service. 
El primero proporciona almacenamiento, alojamiento, redes y capacidad de cómputo que pueden ser escalados de forma automática.
Por su parte un Paas es el conjunto de herramientas proporcionadas sobre un entorno Iaas que permiten el desarrolo de aplicaciones, tales como BBDD, ejecución de programas control de versiones, etc. OpenShift es un ejemplo

13. Service mesh, es una la solución para resolver el problema de fragamentación que se puede dar en una arquitectura basada en microservicios. Proporciona funcionalidades comunes a los microservicios 
    
14. Explica qué es TDD y en qué consiste la tringulación. TDD es el acrónimo de Test-Driven Depelopment, es una métodología o pŕactica de desarrollo en la que primero se escriben las pruebas unitarias con las salidas esperadas por los requisitos. Después mediante refactorización se va realizando el diseño del software.

El concepto de triangulación en TDD no lo conocía, pero consiste en aplicar sucesivos pasos de test y refactorización en los que se va aumentando la complejidad del algoritmos, hasta completar todas las posibilidades que se nos ocurran. 

15. 
