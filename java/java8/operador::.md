# Operador ::

El operador :: se utiliza para escribir expresiones lambdas de forma más compacta. Su formato es:

    referencia::nombreDelMetodo

Un par de ejemplos:


```java
//-----------
//ejemplo 1
//-----------
File::canRead // en lugar de File f -> f.canRead();

//-----------
//ejemplo 2
//-----------
import java.util.stream.*; 
  
class GFG { 
    public static void main(String[] args) 
    { 
  
        // Get the stream 
        Stream<String> stream 
            = Stream.of("Geeks", "For", 
                        "Geeks", "A", 
                        "Computer", 
                        "Portal"); 
  
        // Print the stream 
        stream.forEach(s -> System.out.println(s)); 
    } 
} 

//Esta clase y su método, puede escribirse de forma más compacta
class GFG { 
    public static void main(String[] args) 
    { 
  
        // Get the stream 
        Stream<String> stream 
            = Stream.of("Geeks", "For", 
                        "Geeks", "A", 
                        "Computer", 
                        "Portal"); 
  
        // Print the stream 
        // using double colon operator 
        stream.forEach(System.out::println); 
    } 
} 

```

Los casos en los que se puede recurrir al operador :: para escribir expresiones lambda es para acceder a:
* métodos estáticos
* métodos de instancia
* métodos en super
* constructores

## 1. Métodos estáticos

```java
import java.util.*;
  
class GFG {
  
    // static function to be called
    static void someFunction(String s)
    {
        System.out.println(s);
    }
  
    public static void main(String[] args)
    {
  
        List<String> list = new ArrayList<String>();
        list.add("Geeks");
        list.add("For");
        list.add("GEEKS");
  
        // call the static method
        // using double colon operator
        list.forEach(GFG::someFunction);
    }
}
```

## 2. Métodos de instancia

```java
import java.util.*;
  
class GFG {
  
    // instance function to be called
    void someFunction(String s)
    {
        System.out.println(s);
    }
  
    public static void main(String[] args)
    {
  
        List<String> list = new ArrayList<String>();
        list.add("Geeks");
        list.add("For");
        list.add("GEEKS");
  
        // call the instance method
        // using double colon operator
        list.forEach((new GFG())::someFunction);
    }
}
```

## 3. super methods

```java

import java.util.*;
import java.util.function.*;
  
class Test {
  
    // super function to be called
    String print(String str)
    {
        return ("Hello " + str + "\n");
    }
}
  
class GFG extends Test {
  
    // instance method to override super method
    @Override
    String print(String s)
    {
        // call the super method
        // using double colon operator
        Function<String, String>
            func = super::print;
  
        String newValue = func.apply(s);
        newValue += "Bye " + s + "\n";
        System.out.println(newValue);super methods
  
    // Driver code
    public static void main(String[] args)
    {
        List<String> list = new ArrayList<String>();
        list.add("Geeks");
        list.add("For");
        list.add("GEEKS");
  
        // call the instance method
        // using double colon operator
        list.forEach(new GFG()::print);
    }
}
```

# 4. método de instancia de un tipo arbitrario

```java
import java.util.*;  
  
class Test {  
    String str=null;
      
    Test(String s)
    {
        this.str=s;
    }
    // instance function to be called  
    void someFunction()  
    {  
        System.out.println(this.str);  
    }  
}  
  
class GFG {  
  
    public static void main(String[] args)  
    {  
  
        List<Test> list = new ArrayList<Test>();  
        list.add(new Test("Geeks"));  
        list.add(new Test("For"));  
        list.add(new Test("GEEKS"));  
  
        // call the instance method  
        // using double colon operator  
        list.forEach(Test::someFunction);  
    }  
} 
```
# 5. Constructores de clase

```java
import java.util.*;
  
class GFG {
  
    // Class constructor
    public GFG(String s)
    {
        System.out.println("Hello " + s);
    }
  
    // Driver code 
    public static void main(String[] args)
    {
  
        List<String> list = new ArrayList<String>();
        list.add("Geeks");
        list.add("For");
        list.add("GEEKS");
  
        // call the class constructor
        // using double colon operator
        list.forEach(GFG::new);
    }
}
```
[<< funciones predefinidas](./functionalInterface/predefinedFunctionalInterfaces.md)

[streams >> ](./streams/streams.md)

[índice](./index.md)