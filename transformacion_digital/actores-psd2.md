# Actores PSD2

Una vez descrito el propósito de la directiva europea PSD2, así como sus principales implicaciones, es conveniente comprender con mayor detalle los actores implicados, así como sus posibles interacciones. Puesto que lo habitual es que se les mencione por sus siglas en inglés, se hará referencia a todos ellos de esta forma.

Atendiendo al tipo de servicio que ofrecen se pueden clasificar en:

1. PSU (Payment Service User)
2. ASPSP (Account Servicing Payment Service Providers). Son los Proveedores de Servicios de Pago (PSP) y de los Servicios de Información de Cuentas (AIS). La idea es potenciar el mercado financiero a través de ellos
  
3. PSP (Payment Service Provider). Pueden ser o no entidades bancarias, pero en general son intermediarios de servicios financieros. Los principales representantes son PayPayl, Visa o Mastercard. PSD2 les supone incluir nuevas formas de verificación de identidad para permitir la autenticación en dos pasos, cumplir con el nuevo protocolo de envío de datos (3DS), y asegurar el cumplimiento del resto de estándares técnicos del RTS [Strong Customer Autentication](https://www.eba.europa.eu/regulation-and-policy/payment-services-and-electronic-money/regulatory-technical-standards-on-strong-customer-authentication-and-secure-communication-under-psd2)

4. Account Information Service Provider (AISP). Sus servicios se basan en recopilar información relativa a las cuentas de sus usuarios, fundamentalmente saldos y movimientos.
Es decir, este servicio permite al usuario tener una visión agregada de su liquidez y, por tanto, planificar sus operaciones de pago de una forma más eficiente y/o segura que en la actualida
Desde el punto de vista de la comunicación con la ASPSP, existen 4 intercambios esenciales, aunque no exclusivos:

* Obtención del listado de cuentas
* Lectura del detalle de la cuenta (balance)
* Obtención de transacciones de la cuenta
* Obtención de los balances de pago
  
5. Payment Initiation Service Provider (PISP). Servicio destinado a la iniciación de pagos entre dos entidades, el prestador del servicio y la entidad de pago.  Adicionalemnte les está permitido almacernar información de los pagos gestionados para que el usuario pueda consultarlos.
Este Servicio pertmite que las compras se puedan pagar sin disponer de una tarjeta de crédito, transfiriendo fondos desde una cuenta corriente del usuario ordenante a la del comercio, sin que ambas tengan que pertencer al mismo banco necesariamente.

Al igual que en el caso anterios existe un conjunto de operaciones esenciales:

* Inicio de pago
* Obtención de información del estado de pago
* Obtención de información del pago
* Cancelar inicio de pago

## Servicios

### PIS

Se trata de una nueva forma de pago, directamente desde nuestra cuenta bancaria, sin necesidad de contar con un medio de pago adicional. Las características específicas con las que cuenta un Servicio de Iniciación de Pagos (PIS), es que:

* Al tratarse de un proceso simplificado, la experiencia del usuario mejora porque no tiene que introducir los datos de una tarjeta.
* Aumenta la seguridad para el comercio, se paga por transferencia, pero el iniciador asegura al comerciooo que el consumidor ha iniciado la transferencia y que recibirá el pago.
* Se simplifican los trámites, al no ser necesarios contratos previos con el banco para llevar a cabo la transacción.

### AIS

Los agregadores de información sobre cuentas permiten al usuario ver todas sus cuentas pertenecientes a diferentes entidades financieras, conjuntamente. Pero no solo se trata de la aparición de nuevos actores, sino que las propias entidades financieras pueden ofrecer a su clientes servicios basados en esa información.

¿Qué bases de datos pueden ser agregadas mediante AIS?

Los datos que pueden utilizarse en los servicios agregación AIS son todos aquellos que deriven de cuentas bancarias o bancarizadas, tales como:

* Cuentas bancarias tanto corrientes como de ahorro
* Depósitos bancarios
* Cuentas asociadas a tarjetas de crédito
* Carteras de valores y títulos
* Fondos de inversión bancarizados
* Planes de pensiones y otros vehículos de ahorro
* Cuentas hipotecarias

### Confirmación de fondos

Esta comprobación tiene sentido como paso previo a la ejecución de un pago con una tarjeta de crédito o de débito, y siempre que esta sea accesible de manera online. Al igual que en los dos servicios previos es necesario el consentimiento del cliente titular de la tarjeta.

Los pasos que se llevan a cabo durante la confirmación de fondos, son:

* Antes de la primera solicitud, el usuario se compromete a dar consentimiento explícito a la entidad bancaria para que responda al PSP con la confirmación de fondos.
* El PSP crea una solicitud de confirmación de fondos que envía a la entidad bancaria.
* La entidad bancaria solicita al usuario el consentimiento para que pueda responder a la solicitud de confirmación de fondos.
* Una vez aprobado, se crea un token que permite la confirmación de fondos de manera desasistida.
* El banco contesta al PSP si el usuario tiene o no disponibilidad de fondos para poder llevar a cabo la operación. 

## Estándares de seguridad

RTS Regulatory Technical Standards

Están pensados para fortalecer la seguridad de los pagos que se realizan online, prestando especial atención a asegurar la correcta identificación del cliente.

Los requisitos deben ser implementados tanto en el proceso de pago como en la prestación de servicios accesorios (PIS y AIS). Entra las medidas está la mencionada SCA (String Customer Authentication)

Pero no es la única medida, para los pagos online, el estándar establece que se proporcione una clave de un solo uso, de forma que dicha clave no se pueda volver a utilizar, para evitar un acceso no autorizado.

Estos estándares deben ser implementados tanto por las entidades bancarias como por otros proveedores de servicios de pago (PSP)

## Webscrapping

Screen scraping o webscrapping es una técnica por medio de la cual se extraen datos mostrados por un aplicación con objeto de volcarlos en otro para posteriormente trabajar con ellos. No es exclusiva del sector bancario, también es utilizada en otros sectores como el márquetin digital.

En el caso de la normativa PSD2, esta técnica solo puede realizarse con el consentimiento del cliente, y únicamente cuando las APIs no respondan.

A pesar de ser légitimo en este caso, las entidades financieras son reticentes a su uso, ya que supone que mediante el uso de credenciales de seguridad del usuario, los datos financieros quedan a disposición de terceros, sin que la entidad pueda conocer qué, quién o cuándo se extrae la información.

## fuentes

* [Protagonistas y roles en el escenario general de la PSD2](https://www.bbvaapimarket.com/es/mundo-api/protagonistas-y-roles-en-el-escenario-general-de-la-psd2/)
* [PSD2: qué es y en qué consiste la nueva normativa de pagos online](https://willistowerswatsonupdate.es/riesgos-corporativos-y-directivos/psd2-nueva-normativa-pagos-online/)
* [¿Qué es la PSD2 y cuáles son sus implicaciones?](https://www.xeridia.com/noticias/que-es-la-psd2-y-cuales-son-sus-implicaciones)
* [Qué es la PSD2 y cómo afecta a las actividades online](https://www.electronicid.eu/es/blog/post/psd2-payment-services-directive-2/es)
* [Cuentas claras by Abanca](https://www.cuentasclaras.es/glosario/psd2-terminos-relacionados/)
* [Nuevos actores en la banca: iniciación de pagos y agregadores de cuentas](https://vasscompany.com/iniciacion-pagos-agregadores-cuentas/)
* [Descubre cómo puede beneficiarse tu negocio de la confirmación de fondos](https://www.bbvaapimarket.com/es/mundo-api/descubre-como-beneficiarse-negocio-confirmacion-de-fondos/)
* [¿Qué es la directiva PSD2 y cómo te afecta? ](https://www.helpmycash.com/banco/psd2/)
* [¿Cómo funcionan los servicios de agregación de cuentas (AIS) con PSD2?](https://www.bbvaapimarket.com/es/mundo-api/como-funcionan-servicios-agregacion-cuentas-ais-psd2/)