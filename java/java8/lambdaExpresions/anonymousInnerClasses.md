### Clases anónimas

Es un mecanismo que permite declarar e instanciar una clase al mismo tiempo. Son similares a las [clases locales](./clasesLocales.md) salvo por el hecho de que no tienen nombre. Su uso está indicado para situaciones en las que solo se debe utilizar la clase una vez.

Su sintaxis<sup>[1](https://docs.oracle.com/javase/tutorial/java/javaOO/anonymousclasses.html#syntax-of-anonymous-classes)</sup> es la siguiente:

```java
HelloWorld frenchGreeting = new HelloWorld() {
    String name = "tout le monde";
    public void greet() {
        greetSomeone("tout le monde");
    }
    public void greetSomeone(String someone) {
        name = someone;
        System.out.println("Salut " + name);
    }
}    
```
[<< anonymous inner classes vs lambda expresions](./anonymousInnerClassVSlambdaExpresions.md)

[inner classes >>](./innerClases.md)

[índice](../index.md)