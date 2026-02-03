# PKI - Infraestructura de clave pública

Es un conjunto de componentes y servicios que permtien generar y administrar, la expedición, revocacion y validación de certificados digitales. Sus principales componentes son:

* Autoridad de Certificación (CA). Emisor del certificado, que además determina su validez den el tiempo
* Autoridad de registro (RA). Interface de comunicación entre el usuario y la CA para expedir/revocar certificados
* Autoridad de validación (VA). Centraliza y organiza la lista de certificados emitidos, caducados y revocados. Pone esta información a disposición de los usuarios.

En principio pueden generar toda la variedad de certificados.
