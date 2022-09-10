# Interfaz Funcional

Una interfaz funcional es una interfaz que suscribe un contrato de función única, y que a diferencia de otras interfaces puede ser instanciada tanto por clases como por referencia en expresiones lambda.

## Características

* Tiene un único método abstracto.
* Puede implementar uno o más métodos default.
* Puede implementar uno o más métodos static
* Se puede definir mediante la anotación **_@functionalInterface_**. En este caso será el compilador el que compruebe que la definición de la interfaz funcional sea correcta.
* La herencia de métodos declarados en la interfaz **_Object_** no son considerados nuevas funciones.
* Cualquier interfaz que herede de una interfaz funcional, y no contenga un método abstracto entonces será otra interfaz funcional.
* Puede declarar cualquier método de java.lang.Object además del propio método abstracto, ya que cualquier objeto que la implemente hereda de Object. Pero es necesario que implemente al menos un método abstracto original.

## ejemplos

### correctos
* **_ejemplo trivial_**
```java 
interface Runnable {
    void run();
}
```

* **_declaración de método abstracto y default_**

```java
interface Foo {
    int m();
    default void m2();
}
```

* **_Se declara un único método abstracto heredado de dos interfaces con la misma signatura_**

```java     
interface X { int m(Iterable<String> arg); }
interface Y { int m(Iterable<String> arg); }
interface Z extends X, Y {}        
```

* **_mismo caso que el anterior pero con la declaración de un subtipo. Y es una subsignatura de m_**

```java
interface X { Iterable m(Iterable<String> arg); }
interface Y { Iterable<String> m(Iterable arg); }
interface Z extends X, Y {}        
```

### incorrectos

* **_Se hereda el método equals de Object, pero no se declara ningún nuevo método_**

```java
interface NonFunc {
    boolean equals(Object obj);
}
```

* **_Cambia la signatura de clone, que es protegida, en realidad declara dos nuevos métodos_**

```java
interface Foo {
    int m();
    Object clone();
}
```

Información completa sobre la especificación en <https://docs.oracle.com/javase/specs/jls/se8/html/jls-9.html#jls-9.8>

### Anotación @FunctionalInterface

Esta anotación indica al compilador la intención de declarar una interfaz funcional, de forma que éste debe comprobar tanto la declarción de métodos como la herencia.

_**Es importante comprender que la anotación no convierte la declaración en una interfaz funcional**_

### Herencia

* Si una interfaz hereda de una interfaz funcional y no declara ningún método abstracto, entonces será una interfaz funcional
  
* Si una interfaz define exactamente los mismos métodos abstractos que la interfaz funcional de la que hereda, estaremos ante otra interfaz funcional.

* Cualquier otra situación en la que una interfaz anotación @FunctionalInterfaz herede de una interfaz funcional y declare nuevos métods abstractos, se obtendrá un error de compilación
  
* De la misma forma, si una una interfaz hereda de una interfaz funcional y declara nuevos métodos abstractos, estaremos ante una interfaz corriente, no ante un interfaz funcioanl.

[<< default y static interface methods](../defaultAndStaticInterfaceMethods/defaultAndStaticInterfaceMethods.md)

[lambda expresions >>](../lambdaExpresions/lambdaExpresions.md)

[índice](./../index.md)