# Pruebas de seguridad de aplicaciones

Las pruebas de seguridad de las aplicaciones (AST), que son herramientas que automatizan la comprobación, el análisis y la notificación de las vulnerabilidades de seguridad, son una parte indispensable del desarrollo de software. En un marco moderno de DevOps en el que la seguridad se desplaza hacia la izquierda, las AST deben considerarse obligatorias. Y esto nunca ha sido más importante si se tiene en cuenta que Forrester informa que el método de ataque externo más común sigue siendo las debilidades de las aplicaciones y las vulnerabilidades del software.

El mercado del AST se divide en cuatro grandes categorías:

* Las [pruebas estáticas de seguridad de aplicaciones (SAST)](./sast.md) son pruebas de caja blanca que analizan el código fuente desde dentro mientras los componentes están en reposo.

* Las [pruebas dinámicas de seguridad de aplicaciones (DAST)](./dast.md) son un tipo de pruebas de seguridad de caja negra en las que se realizan ataques a una aplicación desde el exterior.

Las pruebas de seguridad de aplicaciones interactivas (IAST) funcionan desde el interior de una aplicación a través de la instrumentación del código para detectar e informar de los problemas mientras la aplicación se está ejecutando.
Juntamente con Hybrid Tools combinan la observación interna y externa de una aplicación en ejecución que se está probando con DAST simultáneamente. Por lo general, se implementa como un agente dentro del entorno de tiempo de ejecución de prueba (por ejemplo, instrumentando la Máquina Virtual de Java [JVM] o .NET CLR) que observa operaciones o ataques desde dentro de la aplicación e identifica vulnerabilidades. Los enfoques híbridos han estado disponibles durante mucho tiempo, pero más recientemente se han categorizado y discutido usando el término IAST. Las herramientas IAST utilizan una combinación de técnicas de análisis estático y dinámico. Pueden probar si las vulnerabilidades conocidas en el código son realmente explotables en la aplicación en ejecución. Las herramientas IAST utilizan el conocimiento del flujo de la aplicación y el flujo de datos para crear escenarios avanzados de ataque y utilizan los resultados del análisis dinámico de forma recursiva: a medida que se realiza un análisis dinámico, la herramienta aprenderá cosas sobre la aplicación en función de cómo responde a los casos de prueba. Algunas herramientas utilizarán este conocimiento para crear casos de prueba adicionales, que luego podrían generar más conocimiento para más casos de prueba y así sucesivamente. Las herramientas IAST son adeptas a reducir el número de falsos positivos, y funcionan bien en entornos Agile y DevOps donde las herramientas tradicionales DAST y SAST independientes pueden requerir demasiado tiempo para el ciclo de desarrollo.

El análisis de la composición del software (SCA) explora su base de código para proporcionar visibilidad a los componentes del software de código abierto, incluyendo el cumplimiento de la licencia y las vulnerabilidades de seguridad.

# Referencias

[visión global](https://www.a2secure.com/blog/herramientas-de-prueba-de-seguridad-de-aplicaciones-ast/)