# Estructura

Una vez visto el concepto de arquitectura hexagonal, ¿qué implicaciones tienen en la organización de código?. De nuevo, para dar respuesta a esta duda, el artículo que me ha parecido más interesante de los que he visto, es el de [HGraca](https://herbertograca.com/author/hgraca/) [_"Cómo combinar diferentes propuestas de arquitectura a la hora de organizar la estructura de una aplicación"_](https://herbertograca.com/2017/11/16/explicit-architecture-01-ddd-hexagonal-onion-clean-cqrs-how-i-put-it-all-together/#application-core-organisation) en el que explora cómo organizar nuestro software a nivel de capas, teniendo en cuenta varias propuestas de arquitectura.

Sin embargo, no profundiza en cómo organizar la paquetería asociada a cada una. Para tratar de dar respuesta a este último aspecto, en la última sección incluyo un conjunto de enlaces en los que se exploran diferentes alternativas.

Siguiendo su artículo, y atendiendo a algunas recomendaciones de diseño por orden de aparición, tendríamos:

## EBI - Entidad Frontera Interactor (1992)

Aunque dada a conocer por Robert C. Martin, el patrón fue publicado por Ivar Jacobson en 1992. Originalmente era Entidad-Interfaz-Control, pero se cambió su nombre para no confundirlo con elementos de lenguajes de programación.

En esta propuesta, se identifican tres tipos de objetos genéricos en el diseño de aplicaciones:

* Entity. Contienen todos los datos utilizados por el sistema y los cálculos asociados a estos
* Boundary. Objetos que modelan todas las interacciones con el sistema
* Control. Objetos interactor que orquestan un caso de uso, así como los objetos que contengan un comportamiento relevante para éste y que no sean una entidad o una interfaz.

## Principios SOLID (2000)

* S Principio de responsabilidad única. Un objeto solo debería tener una única responsabilidad.
* O Principio de abierto/cerrado. Las “entidades de software deben estar abiertas para su extensión, pero cerradas para su modificación
* L Principio de sustitución de Liskov. Los “objetos de un programa deberían ser reemplazables por instancias de sus subtipos sin alterar el correcto       funcionamiento del programa”
* I Principio de segregación de la interfaz. “Muchas interfaces cliente específicas son mejores que una interfaz de propósito general”.
* D Principio de inversión de la dependencia. Implica que el que en genearl se debe “depender de abstracciones, no depender de implementaciones”. Este principio es crítico para proteger el "dominio de aplicción"

## Domain Driven Design (2003)

En la propuesta de [Eric Evans en su libro Domain-Driven-Design: Tackling Complexity in the Heart of Software](https://www.dddcommunity.org/book/evans_2003/), propone cuatro capas para el diseño de aplicaciones DDD: interfaz de usuario, aplicación, dominio e infraestructura. De éstas, nos vamos a quedar con las tres últimas, para la organización de las responsabilidades en aplicaciones backend.

* **Aplicación:** es la parte que permite interactuar con el actor que utiliza nuestro sistema
* **Dominio:** es la lógica de negocio del sistema.
* **Infraestructura:** conecta el núcleo con las tecnologías de backend.

## Arquitectura hexagonal (2005)

La arquitectura hexagonal hace hincapié en dos ejes

* separar el interior del dominio del exterios
* hacerlo mediante puertos y adaptadores

Un ejemplo puede ser 1el siguiente:

![ejemplo de arquitectura hexagonal](./images/hexagonal-architecture-example.png)

## Arquitectura Cebolla  (2008)

Define 4 principios:

* La aplicación se construye en torno a un modelo de objetos independiente
* Las capas internas definen interfaces.  Las capas externas implementan las interfaces
* La dirección del acoplamiento es hacia el centro
* Todo el código del núcleo de la aplicación puede compilarse y ejecutarse por separado de la infraestructura

Además distingue las siguientes capas:

## Capa de aplicación

Define cómo se utilizan los casos de uso, procesos que pueden ser desencadenados en nuestro Núcleo de Aplicación por una o varias Interfaces de Usuario.Por lo tanto esta capa contiene los Servicios de Aplicación (y sus interfaces).

## Dominio

Contiene los datos y la lógica de manipulación de éstos. Es independiente de los procesos de negocio que desencadenan la lógica y ajenos a la capa de aplicación. Existen dos capas en su interior:

### Servicios de dominio

Fundamentalmente, se trata de la lógica que vincula las entidades del dominio, pero que no es intrínseca a ellas.

Por lo tanto, los servicios de dominio, no deberían saber nada de la capa de aplicación o la infraestructura, pero si puede tener conocimiento de otros servicios de dominio o de los objetos de modelo de dominio

### Modelo de dominio

El modelo de dominio no depende de nada externo, y contiene los objetos que dan identidad al negocio.Hexagonal Architecture by example - a hands-on introduction Estos objetos son entidades, objetos valor, enums, y cualquier otro objeto necesario. También es donde se ativan los eventos de dominio cuando se produce una regla que lo desencadena

## Conclusión preliminar - Capas funcionales y su relación

* existen al menos 2 responsabilidades funcionales respecto a la naturaleza de la inforación que manejan, aplicación y modelo.
* dichas áreas pueden diferenciarse en N capas atendiento a su responsabilidad (interacción con el actor, interaccion con la infraestructura, orquestación del caso de uso, entidades del modelo, etc.). Es aquí donde existe más discrepancia respecto a su organización
* cada capa solo debe hablar con sus capas adyacentes.
* extendiendo un poco más el modelo de puertos y adaptadores, la comunicación entre capas debe hacerse por puertos y adaptadores.

Pero sin embargo, esta estructura basada en responsabilidades y áreas funcionales no asegura el desacoplamiento de código de forma explícita, es la buena utilización de los principios SOLID la que puede asegurarla. Es pues a través de la combinación de ambos tipos de recomendaciones de los que se puede conseguir el desacoplamiento de los componentes de la arquitectura.

## Componentes

Hasta este punto, se ha mencionado los elementos de grano fino implicados en una arquitectura hexagonal, pero la segregación de código de grano fino también implica la segregación de grano grueso, que trata de segregar el código según subdominios y contextos delimitados.

Esto implica paquetes por características o paquetes por componente, en contraposición al paquete por capa. [Robert C. Martin - screaming architecture](https://blog.cleancoder.com/uncle-bob/2011/09/30/Screaming-Architecture.html) y también [Simon Brown en Paquete por componente y pruebas alineadas con arquitectura](http://www.codingthearchitecture.com/2015/03/08/package_by_component_and_architecturally_aligned_testing.html)). [Artículo estructura por componentes](./paquete_componente.md) defienden esta aproximación.

Dichas secciones de código son transversales a las capas descritas, tales como autenticación, autorización, etc., pero siempre relacionadas con el dominio. **_Pero no debemos olvidar que los componenes se benefician también de un bajo acoplamiento y alta cohesión como los elementos de grano fino_**.

Un componente desacoplado, significa que no tiene conocimiento de otros componentes, es decir no tiene ninguna referencia a ninguna unidad de código de otro componente, ni tan siquiera a las interfaces. Esto significa que ni la inyección de depencencias ni la inversión de control (que la clase dependa de abstracciones o interfaces) son suficientes para desacoplar componentes. Por este motivo se necesita algún tipo de elemento de arquitectura como eventos, núcleo compartido, consistencia eventual, servicios de descubrimiento, etc.

Sin embargo, podemos hacer que A utilice un despachador de eventos para enviar un evento de aplicación que será entregado a cualquier componente que lo escuche, incluido B, y el oyente de eventos en B desencadenará la acción deseada. Esto significa que el componente A dependerá de un despachador de eventos, pero estará desacoplado de B.

El inconveniente, es que si el evento en sí "vive" en A, esto significa que B conoce la existencia de A, y está acoplado a A.

Para eliminar esta dependencia, se puede crear una biblioteca con un conjunto de funcionalidades del núcleo de la aplicación que será compartido entre todos los componentes, el Kernel Compartido.

Esto significa que los componentes dependerán del Kernel Compartido pero estarán desacoplados entre sí. El Kernel Compartido contendrá funcionalidad como eventos de la aplicación y del dominio, pero también puede contener objetos de Especificación, y cualquier cosa que tenga sentido compartir, teniendo en cuenta que debe ser lo mínimo posible porque cualquier cambio en el Kernel Compartido afectará a todos los componentes de la aplicación.

Además, si tenemos un sistema políglota, digamos un ecosistema de microservicios donde están escritos en diferentes lenguajes, el Kernel Compartido necesita ser agnóstico al lenguaje para que pueda ser entendido por todos los componentes, sea cual sea el lenguaje en el que hayan sido escritos.Por ejemplo, en lugar de que el Kernel Compartido contenga una clase de Evento, contendrá la descripción del evento (es decir, el nombre, las propiedades, tal vez incluso los métodos, aunque estos serían más útiles en un objeto de Especificación) en un lenguaje agnóstico como JSON, para que todos los componentes/micro-servicios puedan interpretarlo y tal vez incluso auto-generar sus propias implementaciones concretas.

Este enfoque funciona tanto en aplicaciones monolíticas como en aplicaciones distribuidas como ecosistemas de microservicios. Sin embargo, cuando los eventos sólo pueden ser entregados de forma asíncrona, para contextos en los que la lógica de activación en otros componentes debe hacerse inmediatamente, este enfoque no será suficiente. El componente A tendrá que hacer una llamada HTTP directa al componente B. En este caso, para tener los componentes desacoplados, necesitaremos un servicio de descubrimiento al que A preguntará dónde debe enviar la solicitud para desencadenar la acción deseada, o alternativamente hacer la solicitud al servicio de descubrimiento que puede proxy al servicio relevante y eventualmente devolver una respuesta al solicitante. Este enfoque acoplará los componentes al servicio de descubrimiento, pero los mantendrá desacoplados entre sí.

Otros aspectos que se deben tener en cuenta a la hora de compartir información entre componenetes, es que: Cuando un componente necesita utilizar datos que pertenecen a otro componente, digamos que un componente de facturación necesita utilizar el nombre del cliente que pertenece al componente de cuentas, el componente de facturación contendrá un objeto de consulta que consultará el almacenamiento de datos para esos datos. Esto significa simplemente que el componente de facturación puede conocer cualquier conjunto de datos, pero debe utilizar los datos que no le "pertenecen" como de sólo lectura, por medio de consultas

La consecuencia inmediata de este planteamiento, es que será solo el propietario de los datos el que podrá cambiarlos. El resto de componentes podrán utilizar solamente una copia local de los mismos.

Buscando he encontrado varias propuestas sobre cómo estructurar el código:

* [Auntentia: Francisco Acebes](https://github.com/aacebes/Architectures)
* [Pandemonio digital](https://pandemoniodigital.es/arquitectura/2020/12/21/arquitectura-hexagonal-spring-boot.html)
* [Mesa Redonda: arquitectura hexagonal](https://leanmind.es/es/blog/mesa-redonda-arquitectura-y-organizacion-de-directorios/)
* [Hexagonal Architecture by example - a hands-on introduction](https://blog.allegro.tech/2020/05/hexagonal-architecture-by-example.html)
* [Ejemplo de Arquitectura Hexagonal con Spring Data](https://refactorizando.com/ejemplo-de-arquitectura-hexagonal/)

Probablemente la más purista de todas sea la última, me parece que respeta los principio descritos en arquitectura hexagonal, pero la propuesta de *_Mesa Redonda_* me parece muy interesante.

En el repositorio de Autentia hay aproximaciones a tipos de propuestas de arquitectura, y en pandemonio digital se enfrentan alguna propuestas.

# Referencias

* [DDD, Hexagonal, Onion, Clean, CQRS, … How I put it all together](https://herbertograca.com/2017/11/16/explicit-architecture-01-ddd-hexagonal-onion-clean-cqrs-how-i-put-it-all-together/#application-core-organisation)
* [Arquitectura – Onion Architecture](https://code2read.wordpress.com/2015/07/16/arquitectura-onion-architecture-2/)
* [¿Por qué utilizo Clean Architecture?](http://xurxodev.com/por-que-utilizo-clean-architecture-en-mis-proyectos/)