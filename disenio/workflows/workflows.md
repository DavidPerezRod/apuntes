
https://openphoto.net/gallery/image/view/33102

foto https://www.pexels.com/photo/white-diagram-paper-under-pliers-1178498/

https://www.pexels.com/photo/person-in-blue-shirt-wearing-brown-beanie-writing-on-white-dry-erase-board-7368/


# Introducción

Un workflow gestiona el flujo de un proceso de negocio a través de microservicios mediante la visualización, el funcionamiento y la presentación de informes sobre el proceso.

Un motor de flujo de trabajo no tiene que ser necesariamente implementado como un componente único y central. Puede optar por múltiples motores de flujo de trabajo descentralizados para aumentar aún más la independencia del equipo.

Un motor de flujo de trabajo ofrece muchas ventajas, como por ejemplo

* Gestión del estado - Persiste el estado de cada instancia de un proceso de negocio (por ejemplo, cada pedido realizado en un sitio web de comercio electrónico)
* Procesos explícitos - Hace que los procesos de negocio sean explícitos en lugar de enterrarlos en el código (es decir, son más fáciles de entender y modificar para los equipos)
* Correlación y coordinación de mensajes - Fusiona los mensajes que pertenecen a una misma instancia de proceso y decide los siguientes pasos - los lenguajes de modelado de procesos (por ejemplo, el modelo y la notación de procesos empresariales) implementan automáticamente patrones de mensajes como secuencias, sincronización, exclusión mutua y tiempos de espera
* Compensación de problemas - Compensa si un proceso de negocio se encuentra con un problema que requiere que se deshagan los pasos previamente completados
* Gestión del tiempo de espera - Hace un seguimiento del paso del tiempo y, si un mensaje no llega a tiempo, actúa automáticamente o cambia a otra ruta en el flujo del proceso
* Gestión de errores - Permite especificar el comportamiento que debe tener lugar cuando se produce un error (por ejemplo, reintentar una acción, tomar otro camino)
* Transparencia del estado - Permite a los equipos de operaciones supervisar el estado de las instancias del proceso en tiempo real
* Colaboración - Proporciona modelos gráficos de los procesos de negocio que facilitan el debate entre las partes interesadas del negocio, los desarrolladores y los equipos de operaciones

# Técnicas para la automatización de procesos basados en microservicios

## Consideraciones sobre microservicios

Los microservicios bien diseñados tienen los siguientes atributos:

* Ciclos de vida independientes: los equipos deben ser capaces de desarrollar, desplegar y tomar decisiones tecnológicas para sus microservicios sin involucrar a otros servicios o equipos. Iniciar, detener o cambiar un microservicio no debe interrumpir otros servicios.
* Interfaces estables: las interfaces de los microservicios no deben romperse durante las actualizaciones. Si se requieren cambios incompatibles en una interfaz, deben implementarse versionando la interfaz.
* Comunicación asíncrona - Un microservicio necesita comunicarse con otros servicios, pero el par puede no estar disponible inmediatamente. La comunicación asíncrona a través de la mensajería puede eliminar la dependencia de la disponibilidad de otros servicios.
* Resiliencia y tolerancia a fallos: un microservicio debe seguir funcionando si otros servicios del sistema causan problemas.
* Degradación gradual: si un microservicio falla, el resto del sistema debe seguir funcionando lo mejor posible (por ejemplo, volviendo a un flujo predeterminado).
* Escalabilidad independiente - Cada microservicio debe tener acceso a los recursos necesarios para responder a los cambios en la carga del sistema sin afectar a otros servicios.
* Almacenamiento local de datos - Cada microservicio debe almacenar una copia local de los datos necesarios para realizar su tarea.

## Alineamiento de microservicios y Dominio de negocio

Sin embargo, a pesar de las consideraciones anteriores, en ocasiones la compañía puede considerar adecuado que los microservicios soporten un proceso de negocio en su conjunto, lo que implica que aunque cada microservicio funciona de forma independiente, el proceso en su conjunto requiere de una cadena lógica de eventos.

En este escenario, una solución puede ser alinear un único microservicio con la responsabilidad de atender al conjunto de eventos que requiere el proceso de negocio. **_ ¿Se adapata el API Gategay a esta necesidad? _**

## Coreografía y orquestación

Los microservicios se asocian a menudo con el concepto de comunicación basada en eventos, entendiendo por evento una acción significativo que ocurre en un sistema. Cuando la comunicación está dirigida por eventos, los servicios emiten eventos sin saber qué otros servicios los recogerán. Los servicios también reaccionan a los eventos sin saber de dónde proceden. Cada servicio simplemente se suscribe a temas que son relevantes para su propósito y recibe una notificación cuando se emite un evento relacionado con ese tema. La comunicación impulsada por eventos entre microservicios se conoce como "coreografía".

La ventaja de este tipo de comunicación, es evidente, dota de independencia a los microservicios, pero tiene un hándicap,  muchas actividades empresariales requieren una cadena lógica de eventos. Y aunque el proceso de negocio se pueda implementar mediante una cadena de eventos, ésta no está definida explícitamente en ninguna parte, lo que dificulta la comprensión, la supervisión y la resolución de problemas del flujo de actividades a gran escala.

Por contra, en la comunicación basada en comandos un servicio solicita a otro que realice una acción. El receptor puede aceptar o rechazar la orden, pero no puede ignorarla. A diferencia de la comunicación basada en eventos, el emisor sabe qué servicio recibirá su orden. La comunicación impulsada por comandos entre microservicios se conoce como "orquestación". Este enfoque permite que el microservicio que implementa el flujo de la actividad empresarial controle las acciones y los tiempos de otros servicios, manteniendo así una visión general del proceso. Es en este tipo de enfoque donde un workflow toma relevancia, ya  permite modelar el flujo explícitamente y supervisarlo mientras se ejecuta en tiempo real.

## Estado de los procesos

Hasta este punto se han perfilado las implicaciones que tiene una arquitectura basada en microservicios así como el tipo de comunicación entre ésto últimos. No obstante, hay otro elemento fundamental a tener en cuenta para poder implementar un proceso de negocio, el estado, siento este cada una de las etapas en las que se descompone el flujo hasta completar su ejecución.

Esta consideración es importante, ya que por lo general un proceso de negocio no se completa inmediatamante, lo que requerirá que todas las instancias que consituyen un estado sean persistidas para poder ser recuperadas posteriormente y avanzar hacia nuevos estados, repitiendo esta secuencia siempre que sea necesaria, hasta finalizar el proceso.

Es en este ámbito, en el que un workflow es ideal para gestionar el estado de procesos de larga duración.

# Cómo empezar

Para empezar lo más conveniente es identificar los procesos que:

* Son más lentos
* Requieren un mayor esfuerzo manual
* Si están automatizados, cuáles son los más frágiles o los más difíciles de cambiar.

Estos puntos apuntarán las áreas en las que implementar un workflow añadirán valor de forma más rápida a las partes interesadas, tanto técnicas como empresariales.

## Consideraciones

* Dedicar tiempo a modelar los procesos de negocia, ayudará a entender dónde deben estar lo límites de los microservicios, evitando definir sus límites en la estructura organizativa, opciones tecnológicas o deuda técnica.
* No confiar en BBDD compartidas como *_"única fuente de verdad"_*, permitir que los datos de estado se distribuyan entre los microservicios. Así se garantiza que cada microservicio, tenga los datos que necesite, y que escale en respuesta a sus propias necesidades.
* Un workflow complementa una arquitectura de microservicios por las siguientes razones:
  * Puede persistir el estado de etapas del proceso, posibilitando la existencia de procesos de larga duración
  * Permite gestionar los tiempos de espera
  * Permite gestionar las alertas
  * Tratamiento de erorres durante transacciones comerciales.
  * Proporcionar informes de la eficiencia de los procesos
  * Permite orquestar microservicios, y aplicaciones empresariales heredadas.
  
Cuando algo va mal en un proceso empresarial, todos los pasos del proceso que ya se han ejecutado deben deshacerse en el orden correcto para que el sistema global vuelva a un estado coherente. En una arquitectura de aplicación tradicional con una interfaz de usuario, una capa de lógica de negocio y una base de datos relacional, este trabajo se suele gestionar mediante transacciones de base de datos. Sin embargo, este tipo de transacciones no son adecuadas para un sistema distribuido. En cambio, un motor de flujo de trabajo puede manejar las actividades de compensación necesarias, de forma automática y fiable, a través de múltiples.

## ¿Cuándo podría ser necesario a nivel de aplicación?

* Cuando existen procesos que deben ser ejecutados en un contexto diferente al del sistema principal
* Cuando estos procesos backend realizan tareas dependientes del estado de una tarea anterior

Con este último requisito, una implementación que utilice un único conjunto de llamadas a métodos de estilo procedimiental suele resultar inadecuada.

# Dudas

* Cómo utilizar el workflow. Solo para definición de flujo, o también para guardar el estado de los procesos.
* Identificar los patrones de flujo que van a intervenir a la hora de decantarse por un desarrollo personalizado, o por la adopción de soluciones de terceros.
* ¿Cómo controlaríamos los estados?¿Lo ideal sería controlarlos por código de respuesta http?. Esto permitiría jugar rangos. ¿Lo ideal sería producir una salida de estado + datos?. ¿Lo ideal sería guardarlo en una tabla sesión?
* ¿Deberían las salidas, ser una url y un verbo? ¿admitiría parámetros de cabecera, body, etc?
* Diferenciar entre estados bloqueantes, conjunto de estados síncronos, conjunto de estados bloqueantes, etc.
* El control debería estar en el API-Gateway

# Workflows comerciales

## Activiti

* Funciona con Spring Boot hasta la v1.5.4. Todavía no funciona con la v2.0.0.M1.
* Eatá orientado a workflows que se ejecutan como parte del contexto de la aplicación.
* Requiere definir flujos mediante algún model BPMN lo cual a su vez implica aprender a manejar la notación BPMN.
* Requiere configuración XML

## Spring Web Flow

* Está basado en Spring MVC
* Permite implementar flujos de una aplicación web
* Está recomendado para aplicaciones web con estado y navegación controlada, esto es:
  * Hay un punto de inicio y un punto final claros
  * El usuario debe pasar por un conjunto de pantallas en un orden específico
  * Los cambios no se finalizan hasta el último paso
  * Una vez completado no debería ser posible repetir una transacción accidentalmente
  * Proporciona solución a los siguientes problemas:
    * La visualización del flujo es muy difícil.
    * La aplicación tiene mucho código que accede a la sesión HTTP.
    * La aplicación de la navegación controlada es importante pero no es posible.
    * El soporte adecuado del botón de retroceso del navegador parece inalcanzable.
    * El navegador y el servidor se desincronizan con el uso del botón "Atrás".

# Referencias


* [ Amy Johnston, Microservices and Workflow Engines, DZone:](https://dzone.com/refcardz/microservices-and-workflow-engines)
* https://www.infoworld.com/article/2071865/use-spring-to-create-a-simple-workflow-engine.html?page=2
* https://www.javacodegeeks.com/2012/11/simple-workflow-engine-with-spring.html
* http://www.workflowpatterns.com/patterns/control/
* https://www.baeldung.com/spring-activiti