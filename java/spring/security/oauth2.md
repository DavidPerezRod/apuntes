# Oauth2

Por sus siglas *_Open Authorization_* se trata de un protocolo estándar abierto, que permite la autenticación segura de API (interfaz para el intercambio de datos entre aplicaciones, interfaces de usuario y páginas web). Así que es necesario que la API autorice dicha transferencia para evitar que un tercero se puediese hacer con datos sensibles.

Con este protocolo se permite a las aplicaciones un acceso limitado a los datos de los usuarios, sin tener que proporcionar sus credenciales, por lo que se desacopla la autenticación de la autorización de datos. 

* Autenticación: el proceso de verificación de la identidad del usuario
* Autorización:  proceso mediante el cual se controla el acceso a determinados recursos.

OAuth permite delegar la autorización de acceso a las APIs, una vez que se ha producido la autenticación. **_No es un protocolo de autenticación_**

Así pues se trata de un protocolo de autorización, que proporciona una definición de flujos específicos para acceder alos datos del usuario desde aplicaciones consumidoras mediante el uso de diferentes tipos de tokens, entre ellos el JWT.

Por su parte OpenId es un protocolo de autenticación que amplia OAuth 2.0 dotándolo de una capa de autenticación.

## API Keys


## Referencias 

* [IONOS Digital Guide](https://www.ionos.es/digitalguide/servidores/seguridad/oauth-y-su-version-oauth2/)
* [Paradigma Digital](https://www.paradigmadigital.com/dev/oauth-2-0-equilibrio-y-usabilidad-en-la-securizacion-de-apis/)