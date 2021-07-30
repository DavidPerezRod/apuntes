# Static application security testing (SAST)

Para aclarar, si te estás preguntando qué significa el 101, significa que es lo más básico, normalmente los cursos de introducción en una universidad están acompañados del 101; para este artículo eso quiere decir que el 101 significa que hablo de lo más básico en términos de cómo mantener segura tu aplicación web.

Las pruebas estáticas de seguridad de aplicaciones se orientan a la revisión del código fuente de aplicaciones, para identificar posibles fuentes de vulnerabilidad. La técnica se extendió a finales de los 90, cuando se empezó a integrar javascript y flash en las aplicaciones web. A diferencia de las pruebas dinámicas (Dynamic application security testing - DAST) para las que el software es una caja negra, las pruebas estáticas se centran en el código de la aplicación. Escanean el código, componenete y arquitectura en busca de vulnerabilidades. Se estima que las herramientas de análisis dinámico, pueden detectar un 50% de las vulnerabilidades existentes.

Hay dos momentos del ciclo de desarrollo de software en los que se suele utilizar este tipo de test, al principio del proceos y cuando todos los componenete sde código se juntan en un entorno de pruebas consistente.

La precisión de las herramientas SAST viene determinada por su ámbito de anáiss y las técnicas espcíficas utilizadas. Los ámbitos a los que se le puede aplicar son:

* nivel de función
* nivel de archivo o clase
* nivel de aplicación

Cuanto antes se solucione una vulnerabilidad en el ciclo de desarrollo de software más barata es su reparación, en concreto 10 veces menores que en ciclo de pruebas y 100 veces menor que en el ciclo de producción. Este tipo de pruebas se ejecutan automáticamente y no requieren interacción. Cuando se integran en un contexto de CI/CD, este tipo de herramientas pueden utilizarse para detener el proceso de integración si se detectan vulnerabilidades críticas.

El principal inconveniente de este tipo de herramientas son los falsos positivos, que aumentan el tiempo de investigación y reducen la confianza en ellas.

* Las ventajas de SAST incluyen:
  * SAST descubren vulnerabilidades complejas durante las primeras etapas de desarrollo de un software.
  * Amplia compatibilidad con diferentes lenguajes de programación.
  * Fácil integración con los entornos existentes en diferentes etapas del ciclo de desrrollo.
  * Se necesita poco tiempo para examinar el código.

* Los inconvenientes de SAST son los siguientes:
  * No se puede probar la aplicación en el entorno real.
  * Las vulnerabilidades en la lógica de la aplicación o la configuración no son detectables.
  * Los desarrolladores tienen que lidiar con muchos falsos positivos y falsos negativos.
  * El resultado es un informe estático que rápidamente se vuelve obsoleto.
  * SAST depende del idioma en términos de analizar el código fuente subyacente y el binario. La mayoría de las herramientas SAST se especializan solo en unos pocos lenguajes informáticos

## Fuentes

* [wikipedia](https://en.wikipedia.org/wiki/Static_application_security_testing)
* [icho.pro](https://ichi.pro/es/estatico-vs-dinamico-en-pruebas-de-seguridad-de-aplicaciones-59822352127429)
* [OWAS SAST](https://owasp.org/www-community/Source_Code_Analysis_Tools)