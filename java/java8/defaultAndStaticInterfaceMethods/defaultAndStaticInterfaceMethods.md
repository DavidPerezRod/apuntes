# Default Method

Se trata de una caracterítica nueva proporcionada a los interfaces a partir de la versión 8 de Java.

Un _default method_ es un método declarado en una interfaz con modificador _default_, y proporciona una implementación por defecto para cada clase que implemente la interfaz. Sin embargo, admite la sobrecarga, de forma que si una clase hereda el interfaz y necesita una funcionalidad distinta a la proporcionada por defecto, puede sobrescribirlo.

## ejemplo

```java
public interface Interf{
    default void method(){
        System.out.println("default method");
    }
}
```

## Características

* Se pueden invocar directamente desde la instancia de una clase que lo implemente
* Pueen ser sobrescritos por cualquier clase que herede la interfaz.

# Static Method
Al igual que la anterior, se trata de una característica proporcionada a partir de la versión 8 de Java.

Un _static method_ es un método declarado en una interfaz con modificador _static_ y un cuerpo que proporciona la implementación. Se trata de un método de interfaz, que puede ser invocado sin hacer referencia a la instancia de un objeto particular

## Características
* una interfaz no hereda métodos estáticos de sus superinterfaces

La documentación Oracle sobre las características de los métodos _static_ y _default_ de las interfaces está en: <https://docs.oracle.com/javase/specs/jls/se8/html/jls-9.html#jls-9.4.3> y de _static methods_ en <https://docs.oracle.com/javase/specs/jls/se8/html/jls-8.html#jls-8.4.3.2>

## Diferencias entre una interfaz con métodos por defecto y una clase abstracta

|interface with default methods| abstract class|
|------------------------------|---------------|
|las variables son publicas, estáticas y final. No se pueden declarar variables de instancia| se pueden declarar variables de instancia, accesibles por clases hijas|
| No conoce el estado de los objetos| forma parte del estado de los objetos|
|no puede declarse bloques estáticos en su interior | si se pueden declarar métodos estáticos |
| no se pueden sobrescribir métodos de objetos | se pueden sobrescribir métodos de objetos.

Las características anteriores, hacen que cuando se hereda una interfaz con métodos estáticos, éstos no puedan ser invocados a nivel de instancia, ya que la interfaz con conoce nada sobre el estado de los objetos. Sus métodos estáticos, siempre deben ser invocados a nivel de tipo.

Sin embargo los métodos estáticos de las interfaces, tienen una propiedades distintas a las de las clases. Los tres ejemplos que se declaran a continuación son tipos de sobrescritura válidos para métodos estáticos de interfaces, no se obtienen errores de compilación.


```java
interface Interf{
    public static void m1(){

    }
}

class Text implements Interf{
    public static void m1(){

    }
}
```

```java
interface Interf{
    public static void m1(){

    }
}

class Text implements Interf{
    public void m1(){
        
    }
}
```

```java
interface Interf{
    public static void m1(){

    }
}

class Text implements Interf{
    private static void m1(){
        
    }
}
```
[índice](../index.md)

[interfaz funcional >>](./../functionalInterface/functionalInterface.md)