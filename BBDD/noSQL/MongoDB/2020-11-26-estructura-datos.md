# Estructuras de datos

La estructura de datos afecta al modelo, el cual incluye colecciones, colecciones limitadas, documentos e índices. Además hay que tener en cuenta los recursos GridFS para docuementos mayores de 16 MB, ya que Mongo no admite documentos mayores de este tamaño.  

Al ser una BBDD orientada a documentos no tiene la normalización de los modelos relacionales. Es schemaless, ya que su estructura es implícita y flexible. Entendiendo por flexibilidad que la estructura de los docuemntos de una colección puede presentar variación. Esto implica que Mongo no tiene ni transacciones ni joins, aunque si tiene algunos mecanismos para paliarlo.

Así pues, al diseñar una BBDD Mongo se debe tener en cuenta:

* qué colecciones va a haber
* qué documentos van a contener
* qué estructura van a tener los documentos
* decidir si los documentos se enlazan o se contienen.
  * la ventaja de contener el subdocumento es que simplifica la búsqueda
  * sin embargo si se enlazan, hay que hacer dos consultas

## Criterios a tener en cuenta

* patrones de acceso a la información almacenada. ¿cómo se va a acceder a la información almacenada?.
  * Lo ideal sería una sola consulta en la que se devuelva todos los datos necesarios. Evitar el enlazado
* Particionamiento. Decidir cuál es el criterio para repartir los datos de una colección entre los servidores.
* Atomicidad. Una sola operación que se ejecuta de form atómica en el cliente, se traduce en varias operaciones Mongo. Si la operación afecta a un solo docuemento, tenemos atomicidad.

## Normalización

Es un concepto procedente del diseño de bases de datos relacionales. Los objetivos de la normalización son:

* evitar redundancia (que la información no esté repetida en tablas o registros)
* evitar anomalías (que el mismo dato se represente de distinta forma en registros o tablas)
* que cuando haya cambios externos que impliquen actualización del modelo, no implique cambios en lo existente
* que la estructura de la información sea independiente del patrón de acceso.

Evitar la redundancia, no es un criterio básico en Mongo, se debe intentar evitar la redundancia, pero no debe ser un criterio principal.

En cuanto a los cambios externos, como las colecciones de MongoDB puede trabajar con estructuras de documentos distintas, tampoco es un objetivo básico en el diseño de colecciones.

En cuanto al patrón de acceso, claramente la estrategia es opuesta a la de las BBDD relacionales, ya que las operaciones de nuestra aplicación, o las que se ejecutan más frecuentemente, se deben ejecutar sobre un único documento.

## Estrategias de almacenamiento

* Bloqueo a nivel de BBDD. La operaciones de escritura implican el bloqueo de toda la BBDD.
* Estretegias de almacenamiento. Se refiere a la estrategia de diseño del espacio de almacenamiento de los documentos, ya que la modificación de este espacio es vital para el rendimiento de la BBDD:
  * exact fit allocation. El espacio reservado es prácticamente el mismo que el del documento. Es la estrategia por defecto hasta 2.4
    * el espacio de disco duro utilizado se optimiza.
    * si nuestra aplicación tiene actualizaciones que aumentan el tamaño de los documentos, el rendimiento disminuye.
  * Use power of 2 sizes. Trabaja con potencias de 2 bytes, con un mínimo de 32 bytes.
    * es la estrategia por defecto desde la versión 2.6
    * ofrece más espacio para la redimensión de documentos. 
    * Siempre se va a utilizar el espacio más pequeño en el que quepa el documento.
    * es la estrategia adecuada para actualizaciones frecuentes que impliquen crecimiento.
    * el proceso de movimiento es más rápido porque se trabaja con tamaños predefinidos
    * se hace un uso de disco duro más ineficiente

## Colecciones limitadas

Su nombre en inglés es "capped collections". Se llaman así porque tienen un tamaño limitado, que no cambia.

* se declaran de forma explícita. Su tamaño se indica cuando se declara la colección.
* operaciones:
  * se pueden insertar documentos libremente. Pero los documentos se guardan en el orden en que se insertaron
    * implica que no son necesarios los índices
    * las consultas funcionan muy rápido.
  * no se pueden borrar documentos
  * se pueden actualizar si el documento no cambia de tamañano, si no, no.
  * no se pueden particionar.
  * si las nuevas inserciones superan el espacio reservado para la colección, entonces Mongo borra los documentos más antiguos e inserta el nuevo en parte de su espacio (el que ocupe), y así sucesivamente, es como una especie de cola circular.

## GridFS

Cuando un documento es mayor a 16 MB, el lugar de BSON, la estructura de almacenamiento es GridFS, que implica fragmentos de 246 KB.