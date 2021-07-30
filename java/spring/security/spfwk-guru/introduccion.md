# Introducción

En el contexto de computación, existen algunos niveles de seguridad:

* hardware. Previene ejecución de código no autorizado
* sistema operactivo. acceso al computador y acciones que puedes realizar
* bases de datos. acceso a la base de datos y acciones que se pueden realizar
* borkers. lectura y escritura de mensajes en colas
* red.
* aplicación Acceso a las aplicaciones y acciones en su interior.

## Términos clave en seguridad

* Identidad. Desde el punto de vista de la aplicación un actor único
* credenciales. Habitualmente id y password
* Autenticación (quién). Proceso de verificación de la identidad del peticionario
* Autorización (qué ). Privilegios del usuario par realizar acciones.

De todos estos niveles, Spring Security se centra, como es de esperar en el último, y en concreto quién puede hacer qué en el contexto de la aplicación.

Spring security proporciona:

* protección contra las brechas de seguridad más habituales
* integración con productos de terceros como LDAP
* proporciona utilizadades para codificación de passwords.

## Principales proveedores de autenticación en Spring

* In Memory
* JDBC
* Custom
* LDAP / Active Directory
* Keycloak
* ACL
* OpenID
* CAS

En el caso de gestionar localmente la autenticación sin productos de terceros, Spring permite los siguietes métodos para almacenar y verificar contraseñas:

* NoOP Password Encoder --> basado en texto plano. No se recomienda pero en ocasiones puede ser necesario para con legacy
* BCrypt
* Argon2
* Pbkdf2
* SCrypt
* Custom --> no recomendado

## Principales módulos en spring security

* Core - módulos core de spring security
* Remoting - para operaciones RMI
* Web - para aplicaciones web
* Config - proporciona soporte para los aspectos de configuración
* LDAP - para la integración de proveedores LDP de identidad
* OAuth 2.0 Core - core de autorización OAuth 2.0 y Open ID
* OAuth 2.0 Client - soporte para clientes OAuth 2.0 y OpenId.
* OAuth 2.0 JOSE - Soporte para Javascript Object Signing and Encryption
* OAuth 2.0 Resource Server - soporte para servidores de recursos OAth 2.0
* ACL - soporte para Access Control List
* CAS - soporte para Central Autenticacion Service
* OpenId - autenticación con servicores externos OpenId
* Test - soporte para testing de spring securtity.

## OWASP (Open Web Application Security Project)

Es una organización sin ánimo de lucro cuyo objetivo es mejorar la seguridad del software. Proporciona:

* recursos y herramientas
* comunidad
* educación y entrenamientos

### Top 10 vulnerabilidades

* Injection - Inyección de código malintencionado. En general Spring ha hecho un buen trabajo en este aspecto y utilizando la codificación adecuada, su riesgo es mínimo.
* Broken authentication - Generalmente debido a una codificación in house. Para evitarlo, al igual que la anterior, lo más conveniente es utilizar el framework.
* Sensitive data exposure - La principal forma de evitar este problema es no exponer stac traces.
* XML External entities - Se tratra de un error sobre el que se ha evolucionado mucho, y que se debía a procesadores XML pobremente configurados. La mejor forma de evitarlos, es tener dicho procesadores actualizados con todos sus parches, y además muchas de las aplicaciones web actuales intercambian datos en formato JSON.
* Broken access control. Se debe a generalmente a que no se aplican correctamente las restricciones de usuario. La mejor forma de mitigarlo es mediante test que verifiquen dichas restricciones.
* Security misconfigurations - La mejor forma de mitigarlo es mediante auditorías de seguridad.
* CrossSite Scripting - Permite inyectar código HTML o Javascript. La mejor forma de evitar este tipo de ataques es mediante validaciones adecuadas y escaping.
* Insecure deserialization - Puede permitir la ejecución de código remoto. La mejor forma de mitigarlo es actualizar los parches del open source utilizado
* Using components with known vulnerabilities - La mejor forma de mitigarlo es aplicar las actualizaciones con frecuencia.
* Insuficient Loggiing & Monitoring. La forma de mitigarlo es monitorizar adecuadamente los sistemas.

### Cross-site scrpting worms (xss)

Este tipo de ataques se caracterizan porque dentro los campos input de un formulario, se introduce un texto ejecutable, habitualmente javascript. En términos generales los servidores aceptan esos textos, sobre todo si no se utiliza ningún tipo de enconding o "sanitización".

Así pues las medidas que se pueden tomar son:

* eliminar de los inputs los cara caracteres javascript
* codificar los caracteres especiales como HTML

Hay muchas otras medidas que se pueden tomar a nivel de HTML, que se pueden encontrar entre las recomendaciones OWASP.

Por su parte, Spring Security puede ayudar con las siguientes medidas:

* Spring pone todas las cabeceras X-XSS-Protection a 1. mode=block. Esto lo que hace es indicarle al navegador que bloquee el código XSS cuando lo detecte. Aunque los navegadores modernos están empezando a deprecar esta medida en favor del Content Security Policy (CSP)
* En el caso de Content Security Policy, Spring no implementa un valor por defecto para este tipo de cabecera
  * puede ser configurado fácilmente desde Spring
  * mirar las recomendaciones de buenas prácticas sugeridas por OWASP.


### Spring security mitigación

Spring security ha sido construido para mitigar :

* Cross-Site Request Forgery (CSRF)
* Cross-Site Scripting (XSS)

Y además aporta otro tipo de soluciones genéricas:

* Security HTTP Response Headers
* Redirect to HTTPS
