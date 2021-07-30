# Oauth2

Por sus siglas *_Open Authorization_* se trata de un protocolo estándar abierto, que permite la autenticación segura de API (interfaz para el intercambio de datos entre aplicaciones, interfaces de usuario y páginas web). Así que es necesario que la API autorice dicha transferencia para evitar que un tercero se puediese hacer con datos sensibles.

Con este protocolo se permite a las aplicaciones un acceso limitado a los datos de los usuarios, sin tener que proporcionar sus credenciales, por lo que se desacopla la autenticación de la autorización de datos. 

* Autenticación: el proceso de verificación de la identidad del usuario
* Autorización:  proceso mediante el cual se controla el acceso a determinados recursos.

OAuth permite delegar la autorización de acceso a las APIs, una vez que se ha producido la autenticación. **_No es un protocolo de autenticación_**

Así pues se trata de un protocolo de autorización, que proporciona una definición de flujos específicos para acceder alos datos del usuario desde aplicaciones consumidoras mediante el uso de diferentes tipos de tokens, entre ellos el JWT.

Por su parte OpenId es un protocolo de autenticación que amplia OAuth 2.0 dotándolo de una capa de autenticación.

## API Keys

Normalmente se trata de una cadena de caracteres, cuyo valor es único, y que se asigna a un consumidor para eu la utilice en cada una de las solicitudes al API. Le permiten operar de un modo similar al de usuario/passw tradicional, pudiéndose utilizar frecuentemente como método de autenticación del usuario.

Sin embargo este procedimiento es el menos seguro de los que se recomiendan debido a:

* Delega la responsabilidad de almacenamiento de la clave, generalemnte en el consumidor. Así pues está expuesta a las medidas que éste tome para protegerla.
* Falta de control específico. No es posible limitar el acceso de usuarios o APIS mediante esta clave
* No caduca en el tiempo, aunque se puede implementar un sistema de rotado de claves.
* Fue pensada como método de identificación, no como autenticación o autorización

Su uso está indicado para una infraestructura propia, y fundamentalemtne para funciones de cosulta.




## Referencias 

* [IONOS Digital Guide](https://www.ionos.es/digitalguide/servidores/seguridad/oauth-y-su-version-oauth2/)
* [Paradigma Digital](https://www.paradigmadigital.com/dev/oauth-2-0-equilibrio-y-usabilidad-en-la-securizacion-de-apis/)