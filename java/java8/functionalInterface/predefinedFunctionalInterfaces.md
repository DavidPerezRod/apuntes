# Interfaces funcionales predefinidas

Se encuentran en el paquete java.util.function y puedes encontrar su especificación en la  [página oficial](https://docs.oracle.com/javase/8/docs/api/java/util/function/package-summary.html).

¿Por qué pueden ser de utilidad las interfaces funcionales?. Son varios los motivos.

* Evitan que se tenga que estar definiendo constantemente interfaces funcionales específicas para nuestro código, ya que cubren un rango amplio de situaciones.

* Proporcionan algunos métodos por defecto y estáticos de bastante útiles.

* Pero su mayor uso lo tenemos con los streams y las operaciones basadas en streams.

Los tipos de interfaces predefinidas son cuatro:

* consumer
* supplier
* predicate
* function

## Consumer

Se trata de una interfaz funcional para representar que "alguien" consume "algo". Así que su representación inmediata es un método que recibe un parámetro, el que consume, y que no devuelve nada.

```java
package java.util.function;

@FunctionalInterface
public interface Consumer<T> {
    void accept(T var1);
    default Consumer<T> andThen(Consumer<? super T> after) {
        Objects.requireNonNull(after);
        return (t) -> {
            this.accept(t);
            after.accept(t);
        };
    }
}
```

Un aspecto importante, es que la generación es perezosa, y no se realiza hasta que se invoca al método accept de la interfaz.

Un ejemplo que hace uso de esta implementación es el siguiente:

```java
public static void main(String[] args) {
    Consumer<Integer> printNum = num -> System.out.print("Number: " + num);

    Consumer<Integer> invert = num -> System.out.print(", Inverted: " + (-num));

    Consumer<Integer> doubleNum = num -> System.out.print(", Double, " + num * 2);

    Consumer<Integer> result = printNum
                                 .andThen(invert)
                                 .andThen(doubleNum);
    result.accept(10);
}
```

## Suplier

Es el caso contrario al consumer, este tipo de elementos proporcionan una salida. No acepta ningún argumento y proporciona una salida.

```java
package java.util.function;

@FunctionalInterface
public interface Supplier<T> {
    T get();
}

public static void main(String[] args) {
    Supplier<Integer> supplier = () -> new Random().nextInt(100);
    System.out.println(supplier.get());
}
```

### Predicate

Un predicato es algo que se puede afirmar o negar sobre algo en lógica proposicional. En términos prácticos, afirma o niega algo a partir de la condición lógica que implemente.
package java.util.function;

```java
@FunctionalInterface
public interface Predicate<T> {
    boolean test(T var1);

    default Predicate<T> and(Predicate<? super T> other) { ... }

    default Predicate<T> negate() { ... }

    default Predicate<T> or(Predicate<? super T> other) { ... }

    static <T> Predicate<T> isEqual(Object targetRef) { ... }

    static <T> Predicate<T> not(Predicate<? super T> target) { ... }
}
```

Un ejemplo de uso, puede ser el siguiente, en el que se comprueba si un número dado es par.

```java
public static void main(String[] args) {
    int num = 10;
    Predicate<Integer> isEven = num1 -> num1 % 2 == 0;
    if(isEven.test(num)) {
        System.out.println(num + " is even!");
    }
}
```

De forma similar a como se encadenan consumidores:

```java
@FunctionalInterface
public interface Function<T, R> {
    R apply(T var1);

    default <V> Function<V, R> compose(Function<? super V, ? extends T> before) { ... }

    default <V> Function<T, V> andThen(Function<? super R, ? extends V> after) { ... }

    static <T> Function<T, T> identity() { ... }
}
    return (t) -> {
        return this.test(t) && other.test(t);
    };
}

default Predicate<T> or(Predicate<? super T> other) {
    Objects.requireNonNull(other);
    return (t) -> {
        return this.test(t) || other.test(t);
    };
}
```

Ejemplo de uso, un número par y divisible por 5:

```java
public static void main(String[] args) {

    int num = 10;
    Predicate<Integer> isEven = num1 -> num1 % 2 == 0;
    Predicate<Integer> isDivisibleBy5 = num1 -> num1 % 5 == 0;
    Predicate<Integer> filter = isEven.and(isDivisibleBy5);
    if(filter.test(num)) {
        System.out.println(num + " is even and divisible by 5!");
    }
}
```
El método negate, sirve para negar el resultado del método test:

```java
default Predicate<T> negate() {
    return (t) -> {
        return !this.test(t);
    };
}
```

## Function

Este tipo de funciones predefinidas, se comportan como una función matemática, dada una entrada, se realiza una transformación sobre ella, proporcionando una nueva salida. Advertir que el tipo de salida que se va a obtener está parametrizado en el método por defecto apply

```java
@FunctionalInterface
public interface Function<T, R> {
    R apply(T var1);

    default <V> Function<V, R> compose(Function<? super V, ? extends T> before) { ... }

    default <V> Function<T, V> andThen(Function<? super R, ? extends V> after) { ... }

    static <T> Function<T, T> identity() { ... }
}
```

Como se ve en la definición, dispone de una serie de métodos estáticos y por defectos, que evalúan la identidad y composición de funciones, así como la concatenación de éstas.

La implementación de compose es la siguiente:

```java
default <V> Function<V, R> compose(Function<? super V, ? extends T> before) {
    Objects.requireNonNull(before);
    return (v) -> {
        return this.apply(before.apply(v));
    };
}
```

Es decir, el método compose devuelve una función f3(x), resultado de componer f1(f2(x)). Primero se aplica f2 y su resultado se le pasa a f1.

El mtodo andThen, es similar al de consumer o predicate:

```java
default <V> Function<T, V> andThen(Function<? super R, ? extends V> after) {
    Objects.requireNonNull(after);
    return (t) -> {
        return after.apply(this.apply(t));
    };
}
```

La diferencia entre compose y andThen es que el primero proporciona composición de funciones, y el segundo encadena funciones.

f1.compose(f2) -> se aplica f2 antes de f1
f1.andThen(f2) -> se aplica f1 y despueś f2

Por último, la implementación del método identity es sencilla:

```java
static <T> Function<T, T> identity() {
    return (t) -> {
        return t;
    };
}
```

¿De qué sirve este método? Para pasar una nueva función como parámetro a una operación de streams sin cambiar nada en la operación original.

A partir de estos cuatro tipos básicos, se pueden encontrar hasta 43 interfaces funcionales predefinidas:

**Interfaces funcionales con dos argumentos**
|signatura|descripión|
|---------|----------|
|BiConsumer<T, U>| consume dos elementos T y U|
|BiPredicate<T, U>| un predicado con dos entradas|
|BiFunction<T, U, R>|toma dos entradas T y U, y genera una salida R|
|UnaryOperator< T >|hereda de Function, toma una entrada T y genera una salida del mismo tipo T|
|BinaryOperator< T >|hereda de Function. Toma dos entradas de tipo T y genera una salida del mismo tipo T|


**Funcional interfaces para tipos primitivos - Function**
|interfaz funcional|método abstracto|
|---------|----------|
|IntFunction< R >| public R apply(**int** i)|
|LongFunction< R >| public R apply(**long** l)|
|DoubleFunction< R >| public R apply(**double** d)|
|ToIntFunction< T >| public **int** applyAsInt(T t)|
|ToLongFunction< T >| public **long** applyAsLong(T t)|
|ToDoubleFunction< T >| public **double** applyAsDouble(T t)|
|IntToLongFunction|public long applyAsLong(int i)|
|IntToDoubleFunction|public double applyAsDouble(int i)|
|LongToIntFunction|public int applyAsInt(long i)|
|LongToDoubleFunction|public double applyAsDouble(long i)|
|DoubleToInFunction|public int applyAsInt(double i)|
|DoubleToLongFunction|public int applyAsLong(double i)|
|ToIntBiFunction|public int applyAsInt(T t, U u)|
|ToLongBiFunction|public long applyAsLong(T t, U u)|
|ToDoubleBiFunction|public double applyAsLong(T t, U u)|

**Funcional interfaces para tipos primitivos - Consumer**
|interfaz funcional|método abstracto|
|---------|----------|
|IntConsumer| public void accept(int i)|
|LongConsumer| public void accept(long l)|
|DoubleConsumer| public void accept(double d)|
|ObjIntConsumer< T >| public void accept(T t, int i)|
|ObjLongConsumer< T >| public void accept(T t, long l)|
|ObjDoubleConsumer< T >| public void accept(T t, double d)|

**Funcional interfaces para tipos primitivos - supplier**
|interfaz funcional|método abstracto|
|---------|----------|
|IntSupplier| public int getAsInt()|
|BooleanSupplier| public boolean getAsBoolean()|
|DoubleSupplier| public double getAsDouble()|
|LongSupplier| public long getAsLong()|

**Versiones primitivas de operadores unarios**
|interfaz funcional|método abstracto|
|---------|----------|
|IntUnaryOperator| public int applyAsInt(int i)|
|LongUnaryOperator| public long applyAsLong(long l)|
|DoubleUnaryOperator| public double applyAsDouble(double l)|

**Versiones primitivas de operadores binarios**
|interfaz funcional|método abstracto|
|---------|----------|
|IntBinaryOperator| public int applyAsInt(int a, int b)|
|LongBinaryOperator| public long applyAsLong(long a, long b)|
|DoubleBinaryOperator| public double applyAsDouble(double a, double b)|

[<< local classes](./../lambdaExpresions/localClasses.md)

[>> :: operator](./../operador::.md)

[índice](./../index.md)