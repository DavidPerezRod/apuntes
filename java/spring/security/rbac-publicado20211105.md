# Role Based Access Control (RBAC)

## Definición

El control de acceso basado en roles es un paradigma de segurdad que permite controlar las acciones que los usuarios de un sistema/organización pueden realizar sobre los recursos del mismo, en función de los privilegios por rol. Se basa en una estructura de tres niveles:

* usuarios
* roles
* grupos

Tiene como objetivo asegurar la confidencialidad, integridad y disponibilidad de la información.

Normalmente empresas y organizaciones, quieren proteger sus datos de accesos y cambios no autorizados. Tradicionalmente para garantizar la seguridad, las el control de acceso se gestionaba de forma individual mediante *listas de contol de acceso* (ACL - access control list). El problema de este tipo de mecanismo de control es que tanto el coste de mantenimiento, como el número de errores de gestión, aumentan a medida que aumenta el número de usuarios a gestionar.

Sin embargo RBAC es un modelo en el que se puede aplicar una política de seguridad a un nivel más específico, mediante el **_principio de seguridad del privilegio mínimo_**. Este principio se basa en la idea de que un usuario debe disponer de los privilegios mínimos necesarios para realizar su trabajo. Los roles abstraen los procesos del trabajo de una organización, pueden variar entre organizaciones, y pueden referirse a aspectos diversos como departamentos, ubicaciones, centros, o funciones de los empleados.

Los roles deben estar orientados a la estructura organizativa de la empresa. Se deben identificar todas las autorizaciones que cada empleado de la organización necesita, tales como acceso a internet, aplicaciones, recursos de red, o inicio de sesión.

## Implementación

Lo primero que debe hacerse al implementar un mecanismo RBAC es determinar qué funciones se van a desempeñar en cada puesto o departamento, para en base a ellas, identificar las herramientas y/o recuros que se necesitan para llevarlas a cabo.

En segundo lugar se procederá a la asignación de roles, donde habrá que que tener en cuenta que:

* los roles se asignan a los empleados según sus tareas o puesto en la empresa
* se puede asignar uno o más roles por usuario
* Se pueden combinar los privilegios por rol con la asignación de privilegios de forma individual.

Por último, será necesario identificar el **_Ámbito_** al que se aplica el control de acceso, entendiento por ámbito el conjunto de de recursos. Los más habituales incluyen:

* Derechos a los datos (read, read and write, full access)
* Derechos de acceso a aplicaciones
* Autorización de solicitudes

{% include image-caption.html imageurl="/assets/images/posts/2021/20211105rbac/20211105model-image.jpg#wide"
title="Esquema" caption="puertos y adaptadores" %}
  
### Asignación de permisos

La asignación de permisos, se realiza a tres niveles:

* Se deben identificar las autorizaciones que necesitan los indivuduos que pertenecen a un mismo **_grupo de trabajo_**, como es el caso de un **_departamento_**. Las autorizaciones correspondientes, se asignan a todos los empleados de cada departamento.
* A otro nivel de abstracción, es necesario definir funciones de un puesto y sus tareas implicadas, que pueden corresponder a un subconjunto dentro del departamento, o a un rol desempeñado de forma individual.
* Por último es posible asignar a cada empleado los roles adicionales que necesite

## Modelos

* **_RBAC plano_**. Es el modelo base.
  * A los usuarios se les asignan roles que tienen asignados privilegios, de forma que a través de un rol, los usuario adquieren permisos.
  * Requiere que el usuario-rol y el permiso-rol se pueda asignar de muchos a muchos
    * Un usario puede ser asignado a muchos roles
    * Un rol puede ser asignado a muchos usuarios
    * Un permiso puede ser asignado a muchos roles
    * _Implica la revisión del rol de los usuarios_
* **_RBAC jerárquico_**
  * Incluye las características del RBAC Plano, pero además los roles se organizan por jerarquías
    * Los roles superiores contienen los permisos de los roles inferiores
  * Deja la definición de jerarquías abierta, para que cada organización define la suya.
* **_RBAC restringido_**
  * Tiene en cuenta la separación de funciones, como un sistema creado para minimizar la comisión de actos fraudulentos. Se basa en la necesidad de implicar a más de un miembro o empleado para completar una tarea.
  * Determinados accesos y acciones requieren de los permisos de más de un rol
  * Puede ser estático o dinámico (implica activación de roles)
* **_RBAC simétrico_**
  * Implica que se puedan revisar y ajustar los roles y permisos de cada función de forma periódica, para poder adaptarse a los cambios de forma efectiva.

## Ventajas

Entre las principales ventajas de RBAC destacan:

* **_Flexibilidad_**. Los cambis en la estructura organizativa o autorizaciones se pueden modificar rápidamente.
* **_Administración_**. Su administración es mucho menos constosa que la gestión de permisos individuales, y se cometen muchos menos errores en su gestión
* **_Eficiencia_**. Gracias a la mejora en la administración el proceso es más eficiente, pudiendo autmatizarse y disminuyendo los tiempos de espera.
* **_Seguridad_**. Aumenta la seguridad gracias al **_Principio del Menor Privilegio_**
* **_Transparencia_**. Al estar basada en roles, la administración se simplifica, y es más fácil de entender la política aplicada.

## Desventajas

Sin embargo, la adopción de este sistema de control de accesos conyeva una serie de inconvenientes a tener en cuenta:

* **_Elaboración_**. Transferir la estructura organizativa a un modelo de control de accesos es laborioso.
* **_Conveniencia_**. Para modelos organizativos pequeños, la asignación de permisos individuales puede ser más ágil.

## Fuentes

* [Oracle. Guía de administgración del sistema: servicios de seguridad](https://docs.oracle.com/cd/E24842_01/html/E23286/rbac-1.html)
* [Ionos Digital Guide. Role based access control (RBAC): ¿Cómo funciona el control de acceso basado en roles?](https://www.ionos.es/digitalguide/servidores/seguridad/que-es-el-role-based-access-control-rbac/)
* [Manage Engine. Prevenga los ataques basados en archivos con un control de acceso efectivo basado en roles](https://www.manageengine.com/es/device-control/role-based-access-control.html)
* [Atico 34. Control de acceso basado en roles (RBAC): Una forma de mejorar la seguridad del sistema](https://protecciondatos-lopd.com/empresas/control-de-acceso-basado-en-roles-rbac/)
* [Foto de Gráfico creado por rawpixel.com](http://www.rawpixel.com)