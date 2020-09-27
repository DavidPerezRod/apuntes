### Anonymous Inner Classes vs Lamba expresions

Una de las características de las expresiones lambda, es que ayudan a simplificar el tipo de implementación que hasta el momento se hacía para algunos usos de las clases anónimas. 

Un ejemplo de ello es esta implementación de runnable.

* con clase anónima

```java
class Test {
    public static void main(String[] args) {
        Runnable r = new Runnable() {
            public void run() {
                for (int i = 0; i < 10; i++) {
                    System.out.println("Child Thread");
                }
            }
        };
        Thread t = new Thread(r);
        t.start();
        for (int i = 0; i < 10; i++) {
            System.out.println("Main Thread");
        }
    }
}
```

* con expresión lambda

```java
class Test {
    public static void main(String[] args) {
        Runnable r = () -> {
            for (int i = 0; i < 10; i++) 
                System.out.println("Child Thread");
        };
        Thread t = new Thread(r);
        t.start();
        for (int i = 0; i < 10; i++) {
            System.out.println("Main Thread");
        }
    }
}
```

Esto no quiere decir que una clase anónima siempre pueda sustituirse por una expresión lambda. Son cosas diferentes. Solamente se puede hacer en los casos en los que una clase anónima se utilice para definir la implementación de una interfaz funcional.

Tres ejemplos:

* clase anónima heredando de una clase simple: 

```java
class text(){}

Text t= new Text(){/* nueva implementación */}
```

* clase anónima heredando de una clase abstracta: 

```java
abstract class text(){}

Text t= new Text(){/* nueva implementación */}
```

* clase anónima heredando una interfaz: 

```java
interface text(){
    void m1();
    void m2();
    void m3();
}

Text t= new Text(){/* nueva implementación */}
```

Ninguno de los casos anteriores puede ser sustituido por una expresión lambda.

Tabla resumen implementación herencia entre clases anónimas y expresiones lambda:

||clase anónima|lamda expresion|
|-|-------------|------------------|
|clase convencionaL|SI|NO|
|clase abstracta|SI|NO|
|interfaz convencional|SI|NO|
|interfaz funcional|SI|SI|

Sin embargo, hay otras diferencias a tener en cuenta

|clase anónima|lamda expresion|
|-------------|------------------|
|es una clase sin nombre|es una función sin nombre (función anónima)|
|pueden declarar variables de instancia|cualquier variable se considera local|
|puede instanciarse|no pueden instanciarse| 
|el operador this siepmpre se refiere a la propia clas anónima|el operador this siempre se refiere a la clase externa que contiene la expresión lambda|
||es la mejor opción para implementar interfaces funcionales|
|en compilación genera un fichero propio||
|la memoria es reservada bajo demanda cuando se instancian objetos|reside permanentemente en la memoria de la jvm|

Sin embargo y pese a que las clases anónimas abarcan un rango más amplio de implementación, las expresiones lambda tienen una serie de ventajas:

* habilitan la programación funcional en java
* reducen la longitud del código haciéndolo más legible
* se pueden asignar procedimientos/funciones a variables
* por lo tanto se pueden pasar procedimiento/funciones como parámetros.
* soportan procesamiento paralelo.

[<< lambda expresions](./lambdaExpresions.md)

[anonymous inner classes >>](./anonymousInnerClasses.md
)

[índice](../index.md)
