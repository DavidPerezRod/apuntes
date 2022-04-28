# Feature Toggle

Feature Toggle es una técnica es una técnica que permite cambiar el comportamiento del software sin cambiar el código. Se dividen en varias categorías de uso, y es importante tener en cuenta esa categorización a la hora de implementar y gestionar los toggles.

Su diseño se basa en la función Toggle Router, que permite identificar dinámicamente qué comportamiento está activo.

Hacer que el router Toggle tome decisiones basadas en una configuración Toggle, y hacer que esa configuración sea específica del entorno. Activar la nueva función sólo en un entorno de preproducción.

Permitir que la configuración de Toggle se modifique en tiempo de ejecución a través de algún tipo de interfaz de usuario de administración. Utilizar esa interfaz de administración para activar la nueva función en un entorno de prueba.

Enseñar al enrutador de conmutación cómo tomar decisiones dinámicas de conmutación por solicitud. Estas decisiones tienen en cuenta el Contexto Toggle, por ejemplo, buscando una cookie especial o una cabecera HTTP. Normalmente el Contexto Toggle se utiliza como un proxy para identificar al usuario que realiza la petición.

El equipo decide utilizar la infraestructura de la Bandera de Características para llevar a cabo un Lanzamiento Canario, activando la nueva característica sólo para un pequeño porcentaje de su base total de usuarios - una cohorte "canaria".


## Categorías

Las conmutaciones de funciones pueden clasificarse en dos dimensiones principales: la duración de la conmutación de funciones y el dinamismo de la decisión de conmutación. Hay otros factores que hay que tener en cuenta -quién gestionará la alternancia de funciones, por ejemplo-, pero considero que la longevidad y el dinamismo son dos grandes factores que pueden ayudar a orientar la gestión de las alternancias.

* Toggles de liberación

Los Release Toggles permiten que rutas de código incompletas y no probadas se envíen a producción como código latente que puede que nunca se active.

Son banderas de características utilizadas para permitir el desarrollo basado en el tronco para los equipos que practican la entrega continua. Permiten que las características en progreso se verifiquen en una rama de integración compartida (por ejemplo, master o trunk) mientras que todavía permite que esa rama se despliegue a la producción en cualquier momento. Los Release Toggles permiten que las rutas de código incompletas y no probadas se envíen a producción como código latente que puede que nunca se active.

* Exmperiment Toggles

Los Toggles de Experimento se utilizan para realizar pruebas multivariadas o A/B. Cada usuario del sistema es colocado en una cohorte y en tiempo de ejecución el router Toggle enviará consistentemente a un usuario dado por un codepath o el otro, basado en la cohorte en la que se encuentra

Al rastrear el comportamiento agregado de diferentes cohortes podemos comparar el efecto de diferentes rutas de código. Esta técnica se utiliza comúnmente para hacer optimizaciones basadas en datos a cosas tales como el flujo de compra de un sistema de comercio electrónico, o el texto de la llamada a la acción en un botón.

* Ops Toggles

Estos indicadores se utilizan para controlar los aspectos operativos del comportamiento de nuestro sistema. Podemos introducir un Ops Toggle cuando se lanza una nueva característica que tiene implicaciones de rendimiento poco claras para que los operadores del sistema puedan desactivar o degradar esa característica rápidamente en producción si es necesario.

La mayoría de los Ops Toggles durarán relativamente poco: una vez que se haya ganado la confianza en los aspectos operativos de una nueva función, el indicador debería retirarse.

* Permissioning Toggles

Estas banderas se utilizan para cambiar las características o la experiencia del producto que reciben ciertos usuarios. Por ejemplo, podemos tener un conjunto de características "premium" que sólo activamos para nuestros clientes de pago. O tal vez tengamos un conjunto de características "alfa" que sólo están disponibles para los usuarios internos y otro conjunto de características "beta" que sólo están disponibles para los usuarios internos más los usuarios beta. Me refiero a esta técnica de activar las nuevas características para un conjunto de usuarios internos o beta como un Champagne Brunch - una oportunidad temprana para "beber su propio champán".

Un Champagne Brunch es similar en muchos aspectos a un Canary Release. La diferencia entre ambos es que una función Canary Released se expone a una cohorte de usuarios seleccionada al azar, mientras que una función Champagne Brunch se expone a un conjunto específico de usuarios.

![Toggles Types](./toggles_types.png)

## Elección de tipo de Toggle

Los Toggles que toman decisiones de enrutamiento en tiempo de ejecución necesitan necesariamente enrutadores Toggle más sofisticados, junto con una configuración más compleja para esos enrutadores.

Para decisiones simples de enrutamiento estático una configuración de toggle puede ser un simple On o Off para cada característica con un router de toggle que sólo es responsable de transmitir ese estado estático de on/off al punto de Toggle

También podemos dividir nuestras categorías de conmutación en aquellas que son esencialmente de naturaleza transitoria frente a las que son de larga duración y pueden estar en funcionamiento durante años. Esta distinción debería influir mucho en nuestro enfoque a la hora de implementar los puntos de conmutación de una función. Si estamos añadiendo un Toggle de Liberación que será eliminado en unos pocos días, entonces probablemente podemos salirnos con la suya con un Toggle Point que haga una simple comprobación if/else en un Toggle Router

* Desacoplar puntos de decisión de la lógica de decisión.
* Inversión de control
* Evitación de condicionales (patrón estrategia)

## Configuración del Toggle

* Configuración hardcodeada - **Tiempo de compilación**
* Configuración de Toggle parametrizada - variables de entorno o línea de comandos. **requiere redespliegue o inicio del proceso**
* Fichero de configuración - 
  * Con un archivo de configuración de Toggle Configuration se puede reconfigurar una bandera de característica simplemente cambiando ese archivo en lugar de reconstruir el código de la aplicación en sí. Sin embargo, aunque no es necesario reconstruir la aplicación para activar una función, en la mayoría de los casos es probable que tengas que volver a desplegar la aplicación para reconfigurar un indicador.
* configuración en BBDD
  * Esto suele ir acompañado de la creación de algún tipo de interfaz de usuario de administración que permite a los operadores de sistemas, probadores y gestores de productos ver y modificar los indicadores de características y su configuración.
* Configuración distribuida
  * El uso de una base de datos de propósito general que ya forma parte de la arquitectura del sistema para almacenar la configuración de la aplicación es muy común; es un lugar obvio para ir una vez que las banderas de características se introducen y comienzan a ganar tracción. Sin embargo, hoy en día hay una serie de almacenes jerárquicos de valores-clave de propósito especial que se adaptan mejor a la gestión de la configuración de las aplicaciones: servicios como Zookeeper, etcd o Consul. Estos servicios forman un clúster distribuido que proporciona una fuente compartida de configuración del entorno para todos los nodos conectados al clúster. La configuración puede ser modificada dinámicamente siempre que sea necesario, y todos los nodos del clúster son informados automáticamente del cambio - una característica extra muy útil. La gestión de la configuración de conmutación mediante estos sistemas significa que podemos tener enrutadores de conmutación en todos y cada uno de los nodos de una flota que toman decisiones basadas en la configuración de conmutación que se coordina en toda la flota.
* Sobrescritura de la configuración
  * La realidad de muchos sistemas es más sofisticada, con capas de configuración anuladas que provienen de varias fuentes. En el caso de Toggle Configuration es bastante común tener una configuración por defecto junto con anulaciones específicas del entorno. Estas anulaciones pueden provenir de algo tan simple como un archivo de configuración adicional o algo sofisticado como un clúster 
* Sobrescritura por petición
  * Un enfoque alternativo a las anulaciones de configuración específicas del entorno es permitir que el estado de activación/desactivación de un conmutador se anule por solicitud mediante una cookie especial, un parámetro de consulta o una cabecera HTTP. Esto tiene algunas ventajas sobre la anulación de la configuración completa. Si un servicio tiene un equilibrio de carga, puede estar seguro de que la anulación se aplicará independientemente de la instancia de servicio a la que acceda. También puede anular los indicadores de características en un entorno de producción sin afectar a otros usuarios, y es menos probable que deje accidentalmente una anulación en su lugar. Si el mecanismo de anulación por solicitud utiliza cookies persistentes, entonces alguien que pruebe tu sistema puede configurar su propio conjunto personalizado de anulaciones de alternancia que permanecerá aplicado de forma consistente en su navegador.
  
  * La desventaja de este enfoque por solicitud es que introduce el riesgo de que los usuarios finales curiosos o malintencionados puedan modificar el estado de las funciones por sí mismos. Algunas organizaciones pueden sentirse incómodas con la idea de que algunas características no liberadas puedan ser públicamente accesibles para una parte suficientemente determinada. Firmar criptográficamente su configuración de anulación es una opción para aliviar esta preocupación, pero de todos modos este enfoque aumentará la complejidad - y la superficie de ataque - de su sistema de alternancia de características.
  
## Referencias

* https://martinfowler.com/articles/feature-toggles.html
* https://www.baeldung.com/spring-reloading-properties

* https://thepracticaldeveloper.com/practical-guide-feature-toggles-spring-boot/
* https://reflectoring.io/spring-boot-feature-flags/