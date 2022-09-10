* verificar los accesos y atributos inmutables. 
* ¿debe ser la clase inmutable?. Si es así el objeto debe devolver siempre otro objeto, y nunca una referencia a sí mismo
* ¿es un value object?. Si es que sí, se deben implementar los métodos que permiten la comparación de objetos
  * equals
  * hashcode
  * equal null
* ¿Hay que refactorizar? 
  * eliminar código duplicado?
    * generar clases base
    * composición
    * recodificar métodos
    * equals, hashcode, etc
  * evitar la instanciación directa de entidades
    * implementar mecanismos de creación
* [emplear TDD para dirigir el diseño](../java/spring/aproximacion_TDD.md)

```plantuml
@startuml
skinparam svgLinkTarget _parent
component [MyComp1] as comp1 [[http://plantuml.com]] 
component [MyComp2] as comp2 [[file://C:/example.html]]
@enduml
```