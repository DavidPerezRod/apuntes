# Default Method

Se trata de una caracterítica nueva proporcionada a los interfaces a partir de la versión 8 de Java.

Un _default method_ es un método declarado en una interfaz con modificador _default_, y proporciona una implementación por defecto para cada clase que implemente la interfaz sin sobrescribirlo.

## ejemplo

<pre>
    <code>
        public interface Interf{
            default void method(){
                System.out.println("default method");
            }
        }
    </code>
</pre>

## Características

* Se pueden invocar directamente desde la instancia de una clase que lo implemente
* Pueen ser sobrescritos por cualquier clase que herede la interfaz.

# Static Method
Al igual que la anterior, se trata de una característica proporcionada a partir de la versión 8 de Java.

Un _static method_ es un método declarado en una interfaz con modificador _static_ y un cuerpo que proporciona la implementación. Se trata de un método de interfaz, que puede ser invocado sin hacer referencia a la instancia de un objeto particular

## Características
* una interfaz no hereda métodos estáticos de sus superinterfaces

Puedes encontrar información completa sobre las características de los métodos _static_ y _default_ de las interfaces en: <https://docs.oracle.com/javase/specs/jls/se8/html/jls-9.html#jls-9.4.3> y de _static methods_ en <https://docs.oracle.com/javase/specs/jls/se8/html/jls-8.html#jls-8.4.3.2>

