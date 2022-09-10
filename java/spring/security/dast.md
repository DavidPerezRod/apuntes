# DAST (dynamic application security testing)

Este tipo de herramientas lo que hacen es comunicarse con el front-end de una aplicación web, para identificar vulnerabilidades de seguridad de aplicación y de arquitectura. Intentan detectar vulnerabilidades en cadenas de consulta, cabeceras, fragmentos, verbos e inyección en DOM. Comprueba todos los puntos de acceso HTTP y HTML y también emula acciones aleatorias y comportamientos de los usuarios para encontrar vulnerabilidades. 

En general facilitan la revisión automatizada de una aplicación, con el proposito expreso de detectar vulnerabilidades de seguridad y analizar el cumplimiento de requisitos normativos. Las vulnerabilidades buscadas son validación de entrada/salida, problemas específicos de aplicación o errores de configuración del servidor.

Esto incluye una serie de riesgos de seguridad de los diez principales de OWASP, como el cross-site scripting, los errores de inyección como la inyección SQL o la inyección de comandos, el path traversal y la configuración insegura del servidor.

Las vulnerabilidades más comunes son:

* Cross-site scripting
* Inyección SQL
* Revelación de rutas
* Denegación de servicio
* Ejecución de código
* Corrupción de memoria
* Falsificación de peticiones entre sitios
* Revelación de información
* Archivo arbitrario
* Inclusión de archivos locales
* Inclusión de archivos remotos
* Desbordamiento del búfer
* Otros (inyección de PHP, inyección de Javascript, etc.)

Entre las fortalezas de este tipo de herramienta encontramos:

* Están actualizándose constantemente, por lo que las aplicaciones también pueden ser escaneadas constantemente para adaptarse a nuevas vulnerabilidades descubiertas
* A tratarse de escáneres de aplicaciones web, no dependen del lenguaje de la aplicación.
* bajo nivel de falsos positivos

Entre sus debilidades destaca:

* Como los datos de aplicación pueden ser sobrescritos, o bien se inyecta código malicioso, los sitios escaneados no pueden ser los prductivos, suelen ser entornos de control de calidad.
* No pueden implementar todas las variantes de ataques de una vulnerabilidad
* No pueden cubrir el 100% de la funcionalidad de la aplicación
* Al no examinar el código, no puede indicar los puntos específicos de la aplicación que dan lugar a la vulnerabilidad.
* No son escalables, ya que dependen mucho de las pruebas que puedan escribir los expertos en seguridad
* Los escaneos pueden llevar mucho tiempo y en general no se pueden utilizar hasta el final del ciclo de vida de desarrollo, por lo que se tiende a detectar las vulnerabilidades en etapas del ciclo de vida en los que el coste de su solución es mayor

## Referencias

* [wikipedia](https://en.wikipedia.org/wiki/Dynamic_application_security_testing)
* [white source software](https://www.whitesourcesoftware.com/resources/blog/dast-dynamic-application-security-testing/)