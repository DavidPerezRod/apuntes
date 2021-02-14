# Digital Signature

## 1 - Introducción

El establecimiento de relaciones entre dos entidades se basa en la seguridad y la confianza entre ellas. En las relaciones electrónicas se deben dar las mismas condiciones, es necesario asegurar:

* La confianza de Identidad de todas las entidades: **_Autenticación_**
* La privacidad de la información: **_Confidencialidad_**
* Que los documentos intercambiados no sean modificados: **_Integridad_**
* Que el documento es de quien dice provenir: **_No repudio_**

Los medios por los cuales se puede asegurar la confianza entre las partes son tres:

* Criptografía de clave pública. Permite implementar sistemas de firma digital y cifrado sin necesidad de compartir secretos.
* Firma electrónica. Mediante la Firma electrónica se garantiza la integridad, y mediante la criptografía se garantiza la confidencialidad de emisor y receptor.
* Certificados electónicos. Permiten garantizar la Autenticidad del receptor del mensaje o del emisor del mismo, mediante la identidad de clave pública.

## 2 - PKI (infraestructura de clave pública)

Una PKI es el conjunto de servicios de seguridad que hacen posible el uso y administración de certificados digitales y de criptografía de clave pública, en sistemas de computación distribuidos, incluyendo claves, certificados y políticas

La expresión infraestructura de clave pública deriva de la criptografía de clave pública, la tecnología en que se basa la PKI.

En un entorno de PKI se utilizan dos claves criptográficas diferentes para fines de cifrado y descifrado, conocidas como claves públicas y privadas (algoritmos asimétricos).

### Conceptos clave

* Criptografía: técnica que convierte (cifra) un texto inteligible (texto en claro) en otro ininteligible (criptograma), según un procedimiento y una clave determinados. De esta forma la información original sólo será accesible a quien conozca dicho procedimientos y clave. Los dos sistemas de cifrados principales son el simétrico (o de clave secreta) y el asimétrico (o de clave pública). Este último es el utilizado en la implementación de una PKI.

* Firma electrónica: es el conjunto de datos en forma electrónica, consignados junto a otros o asociados con ellos, que pueden ser utilizados como medio de identificación del firmante o signatario.

* Certificado electrónico: es un documento que contiene diversos datos, entre ellos el nombre de un usuario y su clave pública, que es firmado por una Autoridad de Certificación (CA), en la que confían tanto el emisor como el receptor. El usuario que tenga un certificado expedido por ella, se autenticará ante el otro, en tanto que su clave pública está firmada por dicha autoridad.

Los posibles estados del certificado son: 

* Emisión: momento de creación e inicio de su vigencia.
* Expiración: finalización del período de vigencia. Es necesaria la renovación.
* Revocación: la clave privada asociada al certificado se ha visto comprometida. Es necesario el cambio de los datos asociados al certificado. Se incluye en las CRLs de la CA.
* Suspensión: revocación temporal. Se incluye en las CRLs de la CA aunque es reversible.

Durante el proceso de emisión de un certificado digital, se crean dos claves simultáneamente: una privada y una pública. La clave pública es administrada por la autoridad de Certificación (CA) y forma parte del certificado que será emitido. La clave privada, en cambio, es almacenada en la computadora (u otros medios, por ejemplo, tarjeta inteligente) del solicitante del certificado y está totalmente controlada por él mismo. Ni la autoridad certificante ni un tercero pueden tener acceso a esta clave privada.

Dichas claves tienen la característica que lo que cierra una, abre la otra y viceversa. La clave pública se distribuye alrededor del mundo (a través de CA’s que publican estás claves juntamente con los certificados de a quiénes pertenecen), mientras que la clave privada permanece secreta bajo toda circunstancia.

**_PKI - Infraestructura de Clave Pública_**

1. ***_Política y prácticas_***: requisitos y normas para la emisión y gestión de las claves y certificados y las obligaciones de todas las entidades de PKI. Determinar el nivel de confianza que ofrece el certificado.

2. **_Autoridad de certificación(CA)_**: Entidad fiable encargada de garantizar, de forma unívoca y segura, la identidad asociada a una clave pública. Para ello genera certificados digitales que son firmados con su clave privada (clave raíz). Su clave pública debe ser reconocida para validar el certificado.

La CA asume la responsabilidad de la autenticidad de la clave pública del certificado, para permitir un entorno de comunicación seguro.

Igualmente, es la encargada de: 

Consultar con la Autoridad de Registro (RA) para determinar si acepta o rehúsa la petición de certificado de los usuarios.

* Emitir el certificado.
* Gestionar las Listas de Revocación de Certificados (CRLs)
* Renovar los certificados
* Proporcionar:
 * Servicios de respaldo y archivado seguro de claves de cifrado.
 * Infraestructura de seguridad para la confianza, políticas de operación segura, información de auditoría.

3. **_Autoridad de registro (RA)_**: Entidad encargada de verificar los datos de las personas que solicitan el certificado para posteriormente aprobarlos y exportarlos a la CA.

 Igualmente se encarga de:

* los procesos administrativos relacionados con los certificados
* Indicar a la CA si debe emitir el certificado.
* Autorizar la asociación entre una clave pública y el titular de un certificado
* Gestionar el ciclo de vida de un certificado:
  * Revocación
  * Expiración
  * Renovación (extensión periodo validez del certificado, respetando el plan de claves)
  * Reemisión del par de claves del usuario
  * Actualización de datos del certificado

 Se pueden considerar dos tipos de Registro:

 * Presencial: el usuario se persona en la RA y entrega toda la documentación requerida. Si se aprueba la solicitud sus datos son pasados a la CA para que emita el certificado, que una vez emitido se proporciona al usuario.

 * Remoto: el usuario realiza un pre-registro en la RA y posteriormente entrega telemáticamente toda la documentación requerida. Si se aprueba la solicitud sus datos son pasados a la CA para que emita el certificado, que una vez emitido se proporciona al usuario.

4. **_Repositorio de Certificados_**: Contenedor de todos los certificados y de las listas de los certificados no válidos (CRL) antes de su caducidad (por robo, extravío, etc.) Cuando la CA emite un certificado o CRL, lo envía al Directorio. La CA también guarda el certificado o CRL en su base de datos local Estos directorios son accedidos con el protocolo LDAP (Light-weight Directory Access Protocol). 

5. **_Autoridad de Validación (VA, Validation Authority)_**: sirve para comprobar on-line la validez de un certificado. Suele proporcionar dos servicios de validación:

* El tradicional, permitiendo la descarga de CRLs para que sean interpretadas por el usuario. 
* Mediante protocolo OCSP (Online Certification Status Protocol). Los usuarios y las aplicaciones que deseen obtener el estado de un certificado, sólo tienen que realizar una petición OCSP a la VA para obtener dicho estado. La CA actualiza la información de la VA cada vez que se modifica el estado de un certificado, con lo que, a diferencia de las CRL’s, se dispone de información en tiempo real

 OCSP fue creado para solventar ciertas deficiencias de las CRL. Cuando se despliega una PKI (Infraestructura de Clave Pública), es preferible la validación de los certificados mediante OCSP sobre el uso de CRL por varias razones:

 * OCSP puede proporcionar una información más adecuada y reciente del estado de revocación de un certificado.
 * OCSP elimina la necesidad de que los clientes tengan que obtener y procesar las CRL, ahorrando de este modo tráfico de red y procesado por parte del cliente.
 * El contenido de las CRL puede considerarse información sensible, análogamente a la lista de morosos de un banco.
 * Un "OCSP responder" puede implementar mecanismos de tarificación para pasarle el coste de la validación de las transacciones al vendedor, más bien que al cliente.
 * OCSP soporta el encadenamiento de confianza de las peticiones OCSP entre los "responders". Esto permite que los clientes se comuniquen con un "responder" de confianza para lanzar una petición a una autoridad de certificación alternativa dentro de la misma PKI.
 * Una consulta sobre el estado de un certificado sobre una CRL, debe recorrerla completa secuencialmente para decir si es válido o no. Un "OCSP responder" en el fondo, usa un motor de base de datos para consultar el estado del certificado solicitado, con todas las ventajas y estructura para facilitar las consultas. Esto se manifiesta aún más cuando el tamaño de la CRL es muy grande.

 Una diferencia importante entre CRL y OCSP, es que una CRL puede ser almacenada temporalmente para hacer consultas locales, en cambio para usar OCSP se requiere de conexión con el "OCSP responder". Si bien la CRL está disponible sin conexión, mientras más tiempo esté sin actualizarse, se hace menos confiable la información que nos brinde, porque pueden haberse revocado algunos certificados entre actualizaciones.

 Esto da lugar a una diferencia en la eficiencia del uso del ancho de banda para efectos exclusivos de consultas por estado de certificados.

6. **_Autoridad de sellado de tiempo (TSA, TimeStamp Authority) (opcional)_**: Servidor on-line que pone una marca de tiempo firmada por la clave privada raíz en una transacción. Sirve para dar fe del momento en el que se hizo la transacción. Se utilizan en comunicaciones donde el instante es crítico, como operaciones de bolsa, cambio de dinero, etc.

7. **_Almacenamiento de claves privadas (opcional)_**: las claves privadas entregadas se almacenan para la posterior comprobación y para la recuperación en caso de pérdida. La posesión de estas claves permite la absoluta suplantación de identidad. Por lo tanto, se deben almacenar en bunkers altamente protegidos del robo físico.

8. **_Autoridad de Recuperación de Claves (KA, Key Archive)_**, Almacena y recupera según el estándar PKCS#12 y contraseñas (de los PKCS#12)
Para mantener la seguridad en el acceso de las claves, se suelen definir dos roles de administración:

* Administrador de PKCS#12: accede sólo a los ficheros PKCS#12 que contienen las claves pública y privada, y el certificado, todo ello codificado por una contraseña.
* Administrador de contraseñas: accede sólo a las contraseñas que permiten descifrar los ficheros PKCS#12.

Se precisa de la actuación conjunta de los dos administradores para acceder a las claves privadas.

## 3- Firma electrónica

### Tipos de firma

Sufijos tipos de firma

sufijo | Significado | Observaciones
--------|-------------|-------------
BASELINE-B|Basic Electronic Signature|Firma básica. Símplemente cumple los requisitos legales de la Directiva para firma electrónica avanzada. Combina los antiguos -BES y -EPES
XAdES-T (timestamp)|Signature with a timestamp|Es un XAdES-EPES al que se le añade una segunda firma realizada por una TSA (Time Stamp Authority). Esta segunda firma aporta información específica sobre la fecha y hora exacta de la firma. Nivel de protección adicional contra el no repudio.
XAdES-LT |Signature with a timestamp Long Term Data Certificates|Firma de larga vigencia. Además del TimeStamp incluye los datos de verificación de estado del certificdo y consulta OCSP. Dertermina el estado de vigencia de un certificado, por medios diferentes a al uso de Listas de Revocación de Certificados (CRL). Los mensajes OCSP se codifican en ASN.1 y habitualmente se transmiten sobre el protocolo HTTP. La naturaleza de las peticiones y respuestas de OCSP hace que a los servidores OCSP se les conozca como "OCSP responders".
XAdES-LTA |Signature with a timestamp Long Term Data Certificates and Archive Timestamp |Por medio de de renovación de timestamps, por ejemplo cada año, se puede prevenir el debilitamiento de firmas, durante los almacenamientos a largo plano. Este nivel es equivalente al antiguo A.
|||
|||**_FORMATOS ANTIGUOS NO SOPORTADOS EN LA ACTUALIDAD_**
|||
|~~XAdES-BES~~|Antigua Basic Electronic Signature|Firma básica que simplemente cumple los requisitos legales de la Directiva para firma electrónica avanzada|
|~~XAdES-EPES~~||XAdES-BES al que se le incorpora información sobre la política de firma, como pudiera ser aquella información sobre el certificado empleado y la CA que lo emitió|
|~~XAdES-C (complete)~~||Es un XAdES-T al que se se le añaden referencias sobre los certificados y listas de revocación utilizadas para la validación del propio certificado utilizado para la firma. Por ejemplo: fue firmado por Certificado CCC emitidos por CA AAA y cuya CRL RRRR fue consultada en el momento de la validación|
|~~XAdES-X~~ (extended)|| Es un XAdES-C al que se le añade información sobre la fecha y hora de los datos introducido para la extensión C|
|~~XAdES-XL~~(extended long-term)||Es un XAdES-X al que se le incorporan los certificados (sólo clave pública) y las fuentes de validación que se usaron. A diferencia del -C, donde sólo se incluía una referencia (un puntero), en este formato se embebe toda esa información. Por ejemplo, en el caso de una CRL, se incorpora la lista firmada de certificados revocados que fue consultada en ese momento. Esto se utiliza para garantizar la validación muchos años después de la firma incluso en el caso que la CA que emitió el certificado, o la fuente de validación (CRL) que se consultó, ya no esté disponibles (publicadas por ejemplo). Es decir, garantiza la validación off-line a largo plazo|
|~~XAdES-A~~ (archivado)||Este formato incluye toda la información anterior pero incluye meta-información asociada a políticas de refirmado. Una política de refirmado establece un período de caducidad de la firma digital, y superado este tiempo, se procede a un refirmado. El escenario ideal para este formato de firma son aquellos documentos cuya validez sea muy elevada: hipotecas, títulos universitarios, escrituras, etc. 15, 20, 50 años, etc|

## Funcionalidades DSS

### Firma digital

* Firma de documentos
 * firma visible para pdfs
* Firma de digest
* Firma de pdf
* Firma múltiples documentos
* Standalone / webServices (REST/SOAP)

### Funcionalidades lado servidor

* firma extendida ¿qué es?
* sellado de tiempo de documentos
* validación de firma
  * habría que proporcionar una interfaz que permitiese validación retrocompatible con versiones anteriores. En principio la librería parece que lo proporciona
* validación de certificado
* validación de timestamps
  * DSS - sección TSP sources
* SSL-certificate validation
  * Probablemente los tres puntos anteriores a nivel de DSS impliquen los siguiente puntos de la documentación:
  * Management of CRL and OCSP sources.
  * Trust Anchor(s) configuration
  * TLValidationJob
* ~~¿sumario de listas de confianza?~~
* ~~¿sumario de certificados de confianza?~~

## Glosario

Término | Significado
-------|-----------
| Digest | Código que siempre es el mismo si se aplica sobre los mismos datos. Una alteración de los datos implica un digest distinto |
| OCSP | Online Certificate Status Protocol | Protocolo de comprobación del Estado de un Certificado en Línea
| CRL | Lista de Revocación de Certificados |
| TSL | Trust-service Status List | listado de perfiles de Autoridades de certificación que se reconocen a nivel europeo.

* Certificado digital y almacen de claves. Lo estamos generando con openssl. ¿Qué más necesitaríamos?. Según la infraestructura de PKI en teroría debería ser una Autoridad de Certificación (CA) la que nos la proporcionase.

* Sello de tiempo dónde y quién lo genera. DSS proporciona funcionalidad para hacerlo, pero qué certificados o certificador es válido para hacerlo.
* Contenedores ASiC
* Hay que ofrecer retrocompatibilidad en validación de certificados
* La validación de certificados, la podríamos hacer nosotros aunque no fuésemos los firmantes.
* Acceso a OCSP, CRL, y TSL para validación
* políticas de validación (The validation process may be driven by a set of constraints that are contained in the XML fileconstraint.xml)
* Gestión de tokens de firma MS Capi, PKCS#11, PKCS12

## Referencias

* [Infraestructura de clave pública - UANATACA](https://web.uanataca.com/es/blog/transformacion-digital/pki)
* [Infraestructura de clave pública - INAP](https://espaciocompartir.inap.es/v3/pluginfile.php/3305/mod_resource/content/2/Introducci%C3%B3n%20a%20PKI.pdf) 
* [Criptografía - Wikipedia](https://es.wikipedia.org/wiki/Criptograf%C3%ADa_asim%C3%A9trica)
* [Formatos antiguos firma](https://www.viafirma.com/faq/es/xades-firma-electronica-avanzada-xml/)
* [Información ciudadana](https://firmaelectronica.gob.es/Home/Ciudadanos/Formatos-Firma.html)
* [Cifrado Java](http://www.jtech.ua.es/j2ee/2005-2006/modulos/seg/sesion01-apuntes.html)

## Articulos por revisar

### Arquitectura

* https://www.cryptomathic.com/news-events/blog/topic/digital-signatures
* https://www.cryptomathic.com/news-events/blog/three-deployment-versions-business-models-of-eidas-compliant-remote-signing-for-financial-institutions
* https://www.cryptomathic.com/news-events/blog/digital-signature-deployment-models-for-banking-operating-as-an-eidas-compliant-registration-authority-reduces-costs-and-preserves-customer-ownership

### Prestadores de servicios de confianza

* https://www.ivnosys.com/es/prestador-de-servicios-de-confianza-cualificado/
* https://web.uanataca.com/es/blog/firma-electronica/cualificada-simple-avanzada
* https://www.electronicid.eu/es/blog/post/firma-electronica-cualificada/es
* https://connective.eu/es/tres-tipos-de-firmas-electronicas-y-como-elegir-el-tipo-adecuado-para-tus-transacciones/
* https://www.sslmarket.es/ssl/certificados-de-firma-electronica-terminologia-y-clasificacion
* https://web.uanataca.com/es/faqs/certificado-digital/que-es-un-certificado-cualificado-de-firma-electronica
* https://clave.gob.es/clave_Home/dnin/definiciones.html
* https://www.viafirma.com/blog-xnoccio/es/proveedores-de-servicios-de-confianza/
* https://help.signaturit.com/hc/es/articles/360000879238--Qu%C3%A9-es-el-Sello-de-Firma-Electr%C3%B3nica-con-logo-QTSP-