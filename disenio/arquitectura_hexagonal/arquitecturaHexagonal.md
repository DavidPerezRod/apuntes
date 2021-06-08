# Arquitectura Hexagonal

Se trata de un tipo de arquitectura limpia que permite separar responsabilidades mediante capas, y definir reglas de dependencia entre ellas.

Las consecuencias que se derivan de este tipo de arquitecturas son sistemas con las siguientes características: 

* Independientes de framework
* Testeables
* Independientes de la UI
* Independientes de BBDD
* Independientes de agentes externos
* Más adaptables
* Reutilizables
* Mantenibles

## Motivación

Este tipo de arquitecturas surgen para evitar la infiltración de la lógica del negocio en la interfaz de usuario, y con ello:

* mejorar la automatización de pruebas
* evita el acoplamiento con detalles de infraestructura
* impedir el cambio de uso de la aplicación
* favorecer el uso del API de negocio de diferentes maneras (usurio, comandos, lotes, etc). 

La raíz del problema reside en la asimetría entre las entidades internas y externas, y no tanto en las partes izquierda (UI) y derecha (legacy). El problema está entre el interior y el exterior, la regla es que los detalles del código interior no deben filtrarse al exterior.

Dada a conocer por [Alistair Cockburn](http://web.archive.org/web/20180121161736/http://alistair.cockburn.us/Hexagonal+Architecture), también se conoce como arquitectura de puertos y adaptadores. Éstos tiene como finalidad desacoplar las capas de nuestra aplicación, permitiendo que evolucionen de forma aislada.

Los puertos son las interfaces que definen la interacción con el exterior y exponen únicamente datos de nuestro dominio, dejando que toda la lógica de transformación esté de puertas afuera y no se contamine el interior. Y los adaptadores son precisamente la forma de conectar el exterior con los puertos, implementando la comunicación y la conversión de datos entre el dominio y lo que se necesite fuera.  Los adaptadores no pertenecen al core como tal y podrían implementarse cada uno completamente por separado si quisiésemos mientras dependan del puerto que usan/implementan.

El motivo de representar este tipo de arquitecturas como un hexágono, es arbitrario, lo que pretende reflejar es el hecho de que las comunicaciones con el exterior tienen la misma naturaleza y deben asumir las mismas reglas.

En esete sentido, el significado de puerto es similar al de los puertos de comunicación de sistemas operativos o los de dispositivos electrónicos en los que cualquier dispositivo puede ser enchufado a él. El propósito también es el mismo, la conversación entre los dos dispositivos, y su protocolo el API (Application Protocol Interface). En muchos lenguajes este puerto se definirá como una

Después para cada dispositivo externo hay un adaptador, éstos pueden ser tanto de entrada como de salida, dependiendo del sentido de la comunicación. Por lo general habrá varios adaptadores para cada tipo de puerto, uno por tipo de tecnología que puede conectarse a ese puerto.

En cuanto al adaptador, lo más habitual es que si éste implemnte un puerto (interfaz A), se le inyecte una interfaz B cuando el adaptador es instanciado. A partir de aquí, donde quiera que se necesite la interfaz A el adaptador recibirá peticiones que transforma gracias al objeto interior que implementa la interfaz B.

Hay dos tipos de puertos y adaptadores. Los primarios o driving, son los que conectan la UI con la aplicación, y los secundarios que conectan la aplicación con el conjunto de tecnologías que utiliza el backend. Esta clasificación responde a que es en front donde se suelen iniciar las acciones de la aplicación

Además presentan otra diferencia, en los que conectan la UI con la aplicación, el adaptador pertence a la capa de aplicación (API) del caso de uso, mientras que en el caso de backend, es éste el que inyecta el adaptador específico que habilita la comunicación con la herramienta. El núcleo en ambos casos solo debe conocer la interfaz.

![images](./images/hexagonal-arch-4-ports-adapters2.png)

## ¿Ventajas ?

Adicionalmente, tener nuestro sistema separado por responsabilidades facilitará:
* la reutilización de sus elementos.
* el desarrollo de pruebas unitarias

Aunque se suele representar con forma hexagonal, el número de lados no es lo relevante, sino lo que representan, un puerto de entrada o salida. Los puertos pueden ser:
* de entrada. Sirven para hacer peticiones a la aplicación 
* de salida. Sirven para solicitar información desde la aplicación
* de entrada/salida. En general son adaptadores bidireccionales a un recurso. No representan comunicación bidireccional, sino que la aplicación es capaz de escuchar peticiones del recurso y hacerle peticiones.
https://www.adictosaltrabajo.com/2019/07/02/capas-cebollas-y-colmenas-arquitecturas-en-el-backend/es.

Serán el resto de elementos, UI, BDD, Test los que deban implementar los interfaces, de forma que el modelo nunca sabe con quién se está comunicando, su respuesta es la misma independientemente del interlocutor.

Respecto a estas interfaces surgen dos conceptos:

* puerto. Es la definición de una interfaz pública
* adapter. Es la especialización de un puerto para un contexto concreto

## Arquitectura hexagonal y DDD

Al ser el dominio el elemento en torno al cual se define este enfoque, encaja muy bien con la idea de DDD, ya que se puede implementar su lógica sin atender a detalles específicos.


Sin embargo un punto de vista muy interesante es el de Javier Vélez Reyes, en el que desmonta paso a paso esta propuesta de arquitectura, indicando que se trata más de un patrón o recomendación de buenas prácticas, y que símplemnte aglutina conceptos que ya existían

## Referencias

* [Alistair Cockburn](http://web.archive.org/web/20180121161736/http://alistair.cockburn.us/Hexagonal+Architecture)
* [Edu Slaguero: Arquitectura hexagonal](https://medium.com/@edusalguero/arquitectura-hexagonal-59834bb44b7f)
* [Arquitectura Hexagonal](https://medium.com/@edusalguero/arquitectura-hexagonal-59834bb44b7f)
* [Allegro Tech Blog](https://blog.allegro.tech/2020/05/hexagonal-architecture-by-example.html)
* [hgraca](https://herbertograca.com/2017/09/14/ports-adapters-architecture/)
* [Capas, cebollas y colmenas: arquitecturas en el backend](https://www.adictosaltrabajo.com/2019/07/02/capas-cebollas-y-colmenas-arquitecturas-en-el-backend/)
* [Ni Nueva Ni Arquitectura Ni Hexagonal](https://javiervelezreyes.com/ni-nueva-ni-arquitectura-ni-hexagonal/)