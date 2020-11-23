# Excepciones

En primer lugar, hay que entender los conceptos generales de las excepciones java:

1. la jerarquía de excepciones java

![excepciones](throwable.png)

* java.lang.Throwable es la clase base de todas las excepciones
* java.lang.Exception es la superclase de las excepciones "normales"
* java.lang.RuntimeException. Superclase de todas las excepciones normales que son excepciones sin marcar.
* Java.lang.Error. Es la superclase de todas las excepciones de "error fatal". Situaciones en las que la JVM considera inseguro o imprudente que una aplicación intentara recuperarse del error.

2. Entender que no es recomendable declarar subtipos personalizados de de Throwable, ya que la mayoría de soluciones y librerías basadas en java consideran que Error y Exception son los únicos tipos que heredan de Exception.

https://javadesdecero.es/intermedio/manejo-de-excepciones/
https://www.fdi.ucm.es/profesor/gmendez/docs/prog0607/Tema5-Excepciones.pdf
https://sites.google.com/site/jiqpoo/excepciones
http://www.juntadeandalucia.es/servicios/madeja/contenido/recurso/214
https://www.unirioja.es/cu/jearansa/0910/archivos/EIPR_Tema05.pdf
https://elvex.ugr.es/decsai/java/pdf/B2-excepciones.pdf
https://ifgeekthen.everis.com/es/uso-de-excepciones-en-java
https://jarroba.com/excepciones-exception-en-java-con-ejemplos/
https://www.grycap.upv.es/gmolto/docs/eda/EDA_Tema_2_gmolto.pdf


