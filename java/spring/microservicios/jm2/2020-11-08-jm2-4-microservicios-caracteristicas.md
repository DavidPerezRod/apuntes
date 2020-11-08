# Características de los microservicios

En términos generales, se puede decir que un microservicio tiene las siguientes características:

* cada servicio tiene su propio repositorio
* están aislados de otros servicios
  * su empaquedado y despliegue es independiente del resto de servicios
* en consecuencia los microservicios están poco acoplados
  * cuando interactúan con otro microservicios, su interacción debe ser agnóstica de tecnología (en el sentido de no depender de un distribuidor, framework o stack tecnológico)

En cuanto a su arquitectura, ésta presenta algunas características específicas:

* las aplicaciones se componene de microservicios individuales
* cada microservicio puede tener su propia BBBDD
* cada microservicio puede desplegarse independientemente
* cada microservicio es escalable
* es posible CI/CD porque cada microservicio es más pequeño y menos complejo

En consecuencia, los beneficios que todo ello aporta son:

* cada microservicio es más fácil de entender y desarrollar, porque el conocimiento requerído es más específico de dominio
* mejora la calidad del software, ya que el ámbito de cada microservicio es más reducido
* la demanda se puede gestionar independientemente del software
* aunmenta la fiabilidad, porque los bugs quedan aislados
* cada microservicio puede desarrollarse con la tecnología o stack tecnológico más conveniente

Sin embargo, no todo son beneficios. El desarrollo de arquitecturas basadas en microservicios tiene algunos inconvenientes:

* es más difícil desarrollar test de integración
* el despliegue, aunque posible, es complejo. Ahora en lugar de desplegar una única aplicación, hay que desplegar muchas
* Aumenta el coste operacional, ya que cada microservicio es una pequeña aplicación y el coste es el conjunto de costes operacionales de todos los microservicios.
  * Cada microservicio tiene su propio repositorio, proceso de despliegue, BBDD, etc
* costes adicionales de hardware. Cada microservicio necesita su propio hardware.

Un punto clave y polémico, es cuál debe ser el tamaño de un microservicio. Y esto es porque su tamñano podría ir desde lo necesario para resolver un único end-point, a una funcionalidad mucho más amplia. Lo cierto es que no hay ningún criterio definitivo, y quizás lo que tiene más sentido, es que el tamaño varíe dependiendo del caso de uso del escenario en el que nos encontremos. Del mismo modo que a simple vista un único end-point puede parecer poca funcionalidad para considerarlo un microservicio, si a éste le añadimos un requisito de millones de accesos, por cuestiones de escalabilidad, sí puede tener servicio aislarlo en un único microservicio.

Los motivos que pueden llevarnos a adoptar microservicios puede ser:

* la descomposición de una aplicación monolítica