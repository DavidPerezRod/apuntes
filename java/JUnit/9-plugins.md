# Plugins

En ambos plugins, se genera el informe de test pasados en la carpeta que lleva su nombre dentro de target.

1. ### [Surefire](./referenced-surefire.md)
2. ### [failsafe](./referenced-failsafe.md)
3. ### [Build Helper Maven Plugin](https://www.mojohaus.org/build-helper-maven-plugin/usage.html)

El primero es para test unitarios y el segundo para test de integración. En [este vinculo](https://anotherdayanotherbug.wordpress.com/2015/02/23/como-configurar-tus-tests-de-integracion-con-maven/) se explica muy bien cómo configurarlo.
El tercero permite definir nuevos goals al proyecto en los que asociar los fuentes que van a formar parte de los test de integración.