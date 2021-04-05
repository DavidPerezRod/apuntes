En el estudio se han considerado las [4 opciones que ofrece Spring](https://spring.io/projects/spring-cloud-circuitbreaker) para resiliencia de sistemas cloud. 

Son 4:

* Hystrix
* Sentinel
* Resilience4j
* Spring Retry

De las 4, la que menos funcionalidad ofrece es Spring Retry, ya que es una solución orientada a reintentos de Spring Batch y Spring Ingration.

Por otro lado Hystrix, como se indica en su web, es un proyecto en mantenimiento y no se considera añadir nuevas funcionalidades. Netflix lo considera suficiente para sus necesidades.

En cuanto a Sentinel, las siguientes desventajas respecto a Resilience4J:

* que asume que el sistema siempre va a estar desplegado en la nube y replicado, para lo cual integra un conjunto de dependencias, se vayan a utilizar o no.
* menos alternativas de integración con sistemas de monitorización
* menor nivel de granularidad en el manejo de los patrones subyacentes, aunque superior al de Hystrix
* la recuperación de excepciones no es automática, requiere codificación.

Pero quizás la más importante es que la integración de Resilience4j con Spring Boot 2 permite dotar de plena funcioanlidad a travás de ficheros de configuración sin necesidad de programación.




# Referencias:

* https://quickbooks-engineering.intuit.com/resiliency-two-alternatives-for-fault-tolerance-to-deprecated-hystrix-de58870a8c3f
* 