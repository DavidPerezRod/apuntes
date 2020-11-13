# Introducción a MongoDB

# Características generales

Los principales elementos que se manejan en MongoDB son colecciones y documentos. Por establecer una analogía con las BBDD relacionales, las colecciones jugarían un papel similar al de las tablas, y los documentos al de los registros. Sin embargo, los documentos, son estructuras json y las colecciones almacenan dichos documentos, cuyas estructuras dentro de una misma colección, pueden ser distintas.

Entre las ventajas que aporta MongoDB respecto a de las BBDD tradicionales, encontramos:

* el mapeo entre las estructuras de BBDD y software es más sencillo, pudiendo ser directo.
* Escalado:
  * MongoDB está preparado para asumir escalado horizontal de forma automática
  * mantiene el rendimiento frete a escalados horizontales
* Índices
  * se pueden crear índices de cualquier campo
  * alto rendimiento mediante índices
* Alta disponibilidad
  * La estructura básica suele estar compuesta de un servidor primario y dos secundarios. El primario suele actuar como fachada del sistema, recibe todas las operaciones, y los secundarios, de forma asíncrona,  le preguntar por las operaciones que tienen pendiente de consumir.
  * Por defecto tanto las lecturas como las escrituras se hacen desde el primario, pero puede estar configurado de otra forma.
  * Por este motivo se podrían realizar lecturas sucias. Si se pregunta a alguno de los servidores secundarios por alguna información disponible en el primario, pero que no está actualizada en el secundario
  * Este tipo de estructura, y la capacidad de reorganización de los servidores, lo que hace que sea un sistema con capacidad para recuperarse de fallos. Si por ejemplo el servidor primario se cae, esa responsabilidad es asumida por uno de los servidores secundario, y una vez resuelta la indisponibilidad, el servidor se vuelve a incorporar al sistema como secundario.

## Big Data, NoSQL, MongoDB

El motivo por el que surgen este tipo de BBDD es porque la naturaleza del dato, con la evolución de internet y los dispositovos conectados a esta, ha cambiado. Fundamentalmente, es el conjunto de tres características lo que hace insuficientes a las BBDD relacionales para trabajar afrontar este nuevo escenario. En concreto hablamos de:

* volumen. Cantidad de datos a almacenar
* variedad. La estructura del dato a almacenar presenta variedad
* velocidad. A la que hay que ingestar la información en la BBDD

En general, las BBDD relacionales funcionan bien frente a un tipo de escalado vertical, pero no frente a uno horizontal. Y es éste último el que mejor se adapta a este tipo de características.

Las nuevas BBDD que surgen para solucionar este tipo de problema, se agrupan bajo el nombre NoSQL, que habría que traducir como "no solo SQL".