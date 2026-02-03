---
layout: post
title:  "Autorización a Recursos: OAuth 2.0 "
tags: [ Seguridad, Identidad Digital, Comunicaciones ]
featured_image_thumbnail: assets/images/posts/2022/20220402-oauth/20220402-oauth-thumbnail.jpg
featured_image: assets/images/posts/2022/20220402-oauth/20220402-oauth.jpg
featured: true
hidden: true
---

OAuth 2.0, ***Open Authorization 2.0***, es la segunda versión del estándar abierto Open Authorization. Propuesto por Blaine Cook y Chris Messina, es un framework de autorización que permite a servcios cliente obtener acceso limitado a la información de la cuenta de un usuario en un servicio proveedor, mediante comunicación HTTP.
La clave está en que el servicio cliente delega la autenticación de un usuario al servicio que aloja la cuenta de éste, y obtiene autorización de acceso a la cuenta.

De esta forma se permite a aplicaciones de terceros tener un acceso limitado a los datos de los usuarios, sin tener que proporcionar sus credenciales, por lo que desacopla la autenticación de la autorización de datos.

<!--more-->

### Actores

Los actores que intervienen en una operación OAuth 2.0 son cuatro:

* Propietario del Recurso (usuario)
* Servidor de autorización
* Servidor de recursos
* Cliente (aplicación de terceros)

#### Usuario

Es el propietario del recurso que se va a compartir entre el Servidor de Recursos y la aplicación de terceros. 

### Servidor de Autorización

Verifica la identidad del usuario (autenticación) y genera tokens de acceso a la aplicación (autorización)

#### conceptos
* Autenticación: el proceso de verificación de la identidad del usuario
* Autorización: proceso mediante el cual se controla el acceso a determinados recursos.
* Token de acceso: Es el elemento que otorga el Servidor de Autorización a la aplicación de terceros, para realizar solicitudes en nombre del usuario. Aunque a priori, no es necesario que tengan ningún formato en particular, en lo que respecta a la aplicación cliente es una cadena opaca. Se utilizará en la comunicación entre el Servidor de Recursos y la Aplicación Cliente para verificar los privilegios de acceso a los recursos solicictados.

### Servidor de Recursos

Alojar las cuentas de usuario. En general desde el punto de vista de la comunicación entre cliente y proveedor, no suele existir diferencia entre Servidor de Autorización y Servidor de Recursos. El cliente se comunica de forma transparente mediante protocolo HTTP con ambos sin conocer cuál es cuál.

### Aplicación cliente

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

* Photo by [Greg Bulla](https://unsplash.com/@gregbulla?utm_source=unsplash&utm_medium=referral&utm_content=creditCopyText) on [Unsplash](https://unsplash.com/)
* [OAuth - Wikipedia](https://es.wikipedia.org/wiki/OAuth)
* [IONOS Digital Guide](https://www.ionos.es/digitalguide/servidores/seguridad/oauth-y-su-version-oauth2/)
* [Paradigma Digital](https://www.paradigmadigital.com/dev/oauth-2-0-equilibrio-y-usabilidad-en-la-securizacion-de-apis/)
* [Una introducción a OAuth2](https://www.digitalocean.com/community/tutorials/una-introduccion-a-oauth-2-es)