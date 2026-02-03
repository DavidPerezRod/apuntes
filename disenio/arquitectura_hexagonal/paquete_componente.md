# Estructura de paquetería

## Paquete por capas

Mediante este enfoque, lo que se hace es agrupar cosas del mismo tipo, así que las capas son el principal mecanismo de organización del código.

Los argumentos utilizados para justificar este enfoque:
* separación de intereses
* cada capa puede probarse de forma aislada respecto a las demás.

El principal inconveniente de esta estructura es **_que en Java hay qeu marcar las clases cmo públicas para que la relación entre elementos funcione_**.

## Paquete por características

En este enfoque todo lo relacionado con una característica (o conjunto de características) reside en un solo lugar. Es posible combinarlo con una estructura por capas, pero las capas residen en los paquetes de las características. *_La estratificación es el mecanismo de organización secundario_*.

Ventajas: 
* navegavilidad a la hora de identificar el lugar en el que cambiar una característica. 
* permite ocultar las clases específicas de las características y mantenerlas fuera de la vista del resto de código.

Sin embargo, el problema de la ocultación de código si no se crean mecanismos adicionales de boundary, sigue siendo el mismo si unas características tienen dependencia funcional con otras, las clases deben ser públicas.

## Paquete por componente

Se trata de un enfoque híbrido, con mayor modularidad y un estilo de codificación orientado a la arquitectura.

Su premisa básica es organizar el código por elementos de grano grueso, los componentes. Desde este punto de vista un componente es una combianción de la lógica de negocio y acceso a datos relacionados. Tendrán una interfaz pública y detalles de implementación protegidos por paquete. Si este componente A, está relacionado con otros componentes, estará obligado a relacionarse con ellos mediante su interfaz pública. La estratificación arquitectónica deja de ser un elmento principal, y si se los modificadores de acceso Java se utilizan de forma adecuada. 

Obviamente, este tipo de norma puede afectar a los test, pero en este caso el autor ([Simon Brown](https://simonbrown.je/)) indica que para evitar: **_el típico triángulo de pruebas (muchas pruebas de "unidad", menos pruebas de "integración" más lentas y aún menos pruebas de interfaz de usuario más lentas)_** intenta evitar el término "pruebas unitarias" porque todo el mundo tiene una visión diferente de lo que es una "unidad". En su lugar, he adoptado una estrategia en la que algunas clases pueden y deben ser probadas de forma aislada. Esto incluye cosas como las clases de dominio, las clases de utilidad, los controladores web (con componentes simulados), etc.

La razón es que **_Si tengo un componente que almacena datos en una base de datos MySQL, quiero probar todo desde la interfaz pública hasta la base de datos MySQL. Esto se llama típicamente "pruebas de integración", pero de nuevo, este término significa cosas diferentes para diferentes personas._**

A menudo dibujamos "componentes" en una pizarra cuando tenemos discusiones de arquitectura, pero esos componentes son difíciles de encontrar en el código resultante. El empaquetamiento del código por capas es una de las principales razones por las que existe este desajuste entre el diagrama y el código. Los que estén familiarizados con mi modelo C4 probablemente habrán notado el uso de los términos "clase" y "componente". Esto no es una coincidencia. La arquitectura y las pruebas están más relacionadas de lo que quizás hemos admitido en el pasado. 


# Referencias 

* [package by component and architecturally aligned testing](http://www.codingthearchitecture.com/2015/03/08/package_by_component_and_architecturally_aligned_testing.html)
