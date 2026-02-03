# Cloud and microservices

Los microservicios son un aspecto clave de las arquitecturas basadas en la nube, ya que estas se centran en la abstracción, redundancia y evitar fallos en un único punto mediante replicación.

Por este motivo se despliegan múltiples instancias de un microservicio en un entorno cloud, y que por razones de fiabilidad, se puede hacer balanceo de carga entre instancias. En este sentido, hay toda una rama de ingeniería del caos a la que pertenece Chaos Monkey de Netflix OSS que lo que pretende es encontrar fisuras de estabilidad en nuestro sistema productivo.

Dentro de este entorno, las principales herramientas de despligue que se pueden encontrar son:

* AWS Beanstalk
* AWS ECS/EKS
* Kubernetes
* Docker Swarm
* Red Hat OpenShift
* Clound Foundry

# Arquitectura de microservicios

En general, en una arquitectura de microservicios, lo primero que se encuentra, es un gateway, que puede ir acompañado de un balanceador de carga. Su misión es la de enrutar el tráfico a cada uno de los microservicios, de los que habrá más de una instancia corriendo. Por otro lado la comunicación entre microservicios, lo ideal es que sea mediante colas.

Gateway:

* Es el punto de entrada al excosistema. En general encontraremos que es la parte que se expone al exterior, pero no siempre tiene por qué estar expuesto a internet. Puede estar ubicado en una red interna de una empresa. 
* actúa como proxy, abstrayendo los microservicios, ya que quien invoque a nuestros servicios, no va a saber donde están ubicados
* balanceador de carga. Distribuytendo la carga entre los microservicios en ejecución.

Microservicios:

* como mínimo se ejecutará una instancia del microservicio, aunque este número dependera de los requisitos de fiabilidad y requisitos de carga.
* Lo normal será tener un mínimo de 3, pero su escalado suele hacerse de forma dinámica mediante herramientas que anticipan el crecimiento de la demanda.
* El escalado puede moverse en ambos sentidos, crecer o disminuir, ajustándose a las necesidades del sistema en cada momento.

BBDD:

* en general se tendrá una BBDD ejecutándose por microservicio
* sin embargo esta política acarrea un coste y una complejidad, así que aunqeu en general se debe tomar como una regla, dependerá de cada sistema.
* si se tiene una única base de datos para el ecosistema, si se deben tener diferentes esquemas por microservicio.
* habitualmente se contará con más de una tecnología de almacenamiento, mezclando entre SQL y no SQL.

Mensajería:

* lo habitual es exponer el API mediante un endpoint RESTFull
* la comunicación entre los microservicios del API, suele estar basada en un patrón event o command.
  * esta estrategia permite el desacoplamiento y la escalabilidad
  * puede utilizarse para definir el flujo del API

¿Servicios intermedios? (downstream services)

* en general, una acción sobre un microservicio, puede suponer la invocación de múltiples servicios intermedios.