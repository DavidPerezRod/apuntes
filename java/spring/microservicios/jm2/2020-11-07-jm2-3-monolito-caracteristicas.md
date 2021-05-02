# Monolito tradicional

En arquitectura de software un monolito es una única aplicación que habitualmente la encontramos como:

* un único código fuente
* un único sistema de compilación
* un único ejecutable
* en sistemas empresariales se puede volver una aplicación muy grande
* millones de paquetes y clases

Y desde un punto de vista más formal, se puede decir que sus características son:

* todo el código se almacena junto
* habitualmente utiliza una única base d edatos
* las versiones/releases se liberan como una única versión
* su escalabilidad es de todo o nada, no se pueden escalar de forma independendiente componentes de la aplicación,  se escala el conjunto de ésta.
  
La principal ventaja de una aplicación monolítica, es que al tratarse de un único proyecto, proporciona simplicidad desde tres puentos de vista:

* del desarrollo
* del despiegue
* de tests

Sin embargo son más sus inconvenientes:

* en la medida en que crecen los requisitos, crece el monolito
* puede conducir a anti-patrones como el "spaghetti code" o "Big Ball of Mud"
  * y en consecuencia su dificultad de modificación y mantenimiento. Un pequeño cambio puede requerir un desarrollo completo. Un adaptativo.
* el monolito se puede ver fuertenement acoplado a la tecnología
  * y en consecuencia es difícil cambiar de tecnología
* dificultad para implementar una estrategia CI/CD