# Arquitectura [EBI](https://ebi.readthedocs.io/en/latest/) (Entidad-Limite-Interactor)

Aunque fue dada a conocer por Robert C. Martin, la propuso Ivar Jacobson en 1992. Su nombre original, Entidad-Interfaz-Control, se cambió para evitar confusines con conceptos similares de los lenguajes de programación. Y se basa en la creencia de que *La arquitectura de una aplicación se rige por sus casos de uso*.

Su propósito es producir una arquitectura agnóstica a la implementación, que no está atada a una plataforma, aplicación, lenguaje o marco específico; y cuyos elementos estén unidos por un acoplamiento lo más débil posible.



![representación](./images/entity_interface_control.jpg)

Sus responsabilidades:

## Entity

Las entidades contienen los datos utilizados por el sistema y los cálculos asociados a estos. Cada entidad representa un concepto relevante dentro del dominio del problema, y mantiene la identidad de los datos persistentes.

Adicionalmente, Jacobson propone que la naturaleza de la lógica de la entidad, es tal, que si la estructura de los datos que contiene cambia, las operaciones sobre estos datos también tendrán que cambiar. Por eso deben estar ubicados juntos.

Sin embargo, esta recomendación va en contra de lo que actualmente se conoce como entidades anémicas [modelo anémico](https://es.wikipedia.org/wiki/Modelo_de_dominio_an%C3%A9mico).

## Boundary (interfaz)

Se trata de los objetos que modelan todas las interacciones con el sistema. En otras palabras, cualquier interacción del sistema con un actor, debe pasar por un objeto Boundary.

**_Es sin duda de este concepto del que surge el actual de puertos y adaptadores._** A nivel de arquitectura, su responsabilidad es la misma.

## Interactor (control)

Los objetos interactor, según Jacobson son aquellos que orquestan un caso de uso, así como los objetos que contengan un comportamiento relevante para éste y que no sean una entidad o una interfaz.

A su vez H Graca, los compara con los Servicios de Aplicación (que orquestan casos de uso) y servicios de dominio (que contienen el comportamiento de dominio que no recae en entidades.)

La relevancia de este último tipo de objeto es muy importante, ya que si la comunicación entre interfaces y entidades fuese directa, éstas últimas podrían acabar propagando su lógica a los actores externos con los que se comunican.

## Conclusión

Como conclusión, se puede añadir que este tipo de especialización de objetos que identificó Jacobson, responde claramente al principio de responsabilidad única, en el que cada elemento debe tener un único motivo (o agente externo) que justifique sus cambios.

Según el propio Graca:

_Del mismo modo que en el patrón MVC el Modelo representa todo el back-end, todas las entidades, servicios y sus relaciones, el patrón EBI ve el Límite como una conexión completa con el mundo exterior y no sólo una vista, un controlador o una interfaz (la construcción del lenguaje). El Boundary representa toda la capa de presentación que en MVC corresponde a la Vista y al Controlador. La Entidad en EBI representa las Entidades reales que contienen datos y su comportamiento asociado, mientras que los objetos Interactores hacen la conexión entre la capa de presentación y las Entidades, son lo que yo llamaría Servicios de Aplicación y Servicios de Dominio._

_El patrón EBI es para el back-end lo que MVC es para el front-end. No son una alternativa el uno al otro, son complementarios. Si los juntáramos en un solo patrón podríamos llamarlo algo así como Vista-Controlador-Interactor-Entidad._

## Referencias

* [EBI Architecture](https://herbertograca.com/2017/08/24/ebi-architecture/#more-10264)
* [Entity-Boundary-Interactor](https://ebi.readthedocs.io/en/latest/core.html)