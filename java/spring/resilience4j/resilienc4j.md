# Análisis

## Comparativa soluciones

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

## Resilience4j

Los principales patrones que implementa Resilience4J para dotar de tolerancia a fallos, son:

* [Circuit breaker](https://docs.microsoft.com/es-es/azure/architecture/patterns/circuit-breaker): dejar de hacer peticiones cuando el servicio invocado falla repetidamente
* [Retry](https://docs.microsoft.com/es-es/azure/architecture/patterns/retry): realizar reintentos frente a fallos ocasionales
* [Bulkhead](https://docs.microsoft.com/es-es/azure/architecture/patterns/bulkhead): limita el número de peticiones concurrentes salientes a un servicio.
* [Rate limit](https://microservice-api-patterns.org/patterns/quality/qualityManagementAndGovernance/RateLimit.html): limita el número de llamadas que recibe un servicio en un periodo de tiempo.
* [Cache](https://docs.microsoft.com/en-us/azure/architecture/patterns/cache-aside): Trata de almacenar en caché el resultado de peticiones satisfactorias, asumiendo que una proporción de peticiones van a ser similares.
* [Time limiter](https://medium.com/@jonathansychan/rate-limiting-and-throttling-for-a-more-efficient-backend-7feb1a76acc8): limita el tiempo de ejecución de una función para no esperar indifinidamente a una respuesta.

Algunas de las ventajas de Resilience4J frente a Hystrix, más ampliamente utilizado, son:

* se ejecuta en el mismo hilo que la aplicación principal
* la implementación de los patrones no requiere programar clases adicionales
* permite tanto programación declarativa (reactiva o funcional) e imperativa.
* la configuración se puede hacer por:
  * código Java
  * anotaciones
  * fichero de configuración para Spring Boot2

### Integración mediante Spring Boot2

Para la integración es necesario incluir las dependencias:

* resilience4j-spring-boot2
* resilience4j-all
* resilience4j-reactor
* org.springframework.boot:spring-boot-starter-actuator
* org.springframework.boot:spring-boot-starter-aop
* **_io.github.resilience4j:resilience4j-reactor_** Solo en caso de estar utilizando webflux

### Propiedades configurables de los patrones

Cada patrón tiene un conjunto de propiedades de configuración, que por su extensión es mejor no detallar aquí y consultarlo directamente en la documentación de Resilience4j:

* [Circuit Breaker](https://resilience4j.readme.io/docs/circuitbreaker)
* [Bulkhead](https://resilience4j.readme.io/docs/bulkhead)
* [RateLimiter](https://resilience4j.readme.io/docs/ratelimiter)
* [Retry](https://resilience4j.readme.io/docs/retry)
* [TimeLimiter](https://resilience4j.readme.io/docs/retry)
* [Cache](https://resilience4j.readme.io/docs/cache)

### Aspectos

Además de las propiedades configurables para cada uno de los patrones, también existen anotaciones para activar cada uno de ellos en los servicios que se deseen proteger.

Las anotaciones son:

* @CircuitBreaker(name = BACKEND, fallbackMethod = "fallback")
* @RateLimiter(name = BACKEND, fallbackMethod = "fallback")
* @Bulkhead(name = BACKEND, fallbackMethod = "fallback")
* @Retry(name = BACKEND, fallbackMethod = "fallback")
* @TimeLimiter(name = BACKEND, fallbackMethod = "fallback")

Todas tiene un atributo name, que debe coincidir con el nombre dado en el fichero de configuración a una sección dentro del patrón correspondiente, y fallbackMethod, que indica el método de recuperación que tratará el problema en caso de producirse.

Algunas consideraciones adicionales que se deben tener en cuenta sobre las anotaciones son:

* el método fallback debe estar definido en la misma clase
* si hay múltiples métodos fallback definidos con el mismo nombre, se invocará al que tenga una signatura más parecida a la de la excepción que lo haya provocado.
* se puede definir un método fallback genérico con un único parámetro que sea la excepción.

Por defecto el orden en el que se aplican las anotaciones será:

* Bulkhead
* TimeLimiter
* RateLimiter
* CircuitBreaker
* Retry

Aunque este orden puede cambiarse en el fichero de configuración.

### Métricas

Todas las métricas producidas por cada uno de los patrones, pueden ser publicadas mediante Actuator/Micrometer y Prometheus para ser explotadas en otros sistemas como grafana.

## Referencias:

* [Comparativa soluciones](https://quickbooks-engineering.intuit.com/resiliency-two-alternatives-for-fault-tolerance-to-deprecated-hystrix-de58870a8c3f)
* [Proyecto Resilience4j](https://resilience4j.readme.io/docs/getting-started)