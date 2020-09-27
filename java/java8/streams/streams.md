# Streams

Un stream es una abstracción para procesar datos de forma declarativa. Permiten además aprovechar las arquitecturas multinúcleo sin necesidad de hacer programación multiproceso.

En términos más concretos, es una secuencia de elementos origen, que admiten una secuencia de operaciones concatenadas.

* Secuencia de elemntos. Stream proporciona una interfaz para conjuntos de vlares secuenciales de un tipo particular. Es importante entender que los streams no almacenan elementos, realizan cálculos sobre éstos.
  
* Origen. Los streams se alimentan de un origen de datos, como colecciones, matrices o recursos E/S

* Operaciones concatenadas. Los streams admiten la mayoría de las operaciones de los lenguajes funcionales como filter, map, reduce, find, match y sorted.

Pero además de éstas, las operaciones con streams tienen dos características:

* estructura de proceso. Muchas operaciones de de streams devuelven otro streams, de forma que permite la concatenación de operaciones. Y esta es la clave de obtner algunas optimizaciones como el procesamiento perezoso (laziness) y los cortocircuitos (short-circuiting). Solo la operación colect, no devuelve otro stream.

* Iteración interna. La iteración entre los elementos a los que se aplica la operacion es implícita.

Las principales operaciones que podemos utilizar con streams son:

* filter, para filtrar elmentos según un predicado
* sorted, para ordenar elementos según un comparador
* map, para extraer información


![procesamiento streams](./../imágenes/flujoOperaciones.png)

Un ejemplo concreto de concatenación de estas operaciones es el siguiente:

![ejemplo streams](./../imágenes/ejemplo.png)