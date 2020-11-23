# Response Codes

Códigos de respuesta Rest API:

1. Serie 100
   * 100 - continue
   * 101 - switching Protocols
   * 102 - processing

2. Serie 200
   * 200 - ok
   * 201 - created
   * 202 - Accepted
   * 203 - non-authoritative information
   * 204 - no content
   * 205 - reset content
   * 206 - partial content
   * 207 - multi-status
   * 208 - already reported
   * 226 - IM used

3. Serie 300
   * 300 - multiple choices
   * 301 - moved permanently
   * 302 - found
   * 303 - check other
   * 304 - not modified
   * 306 - use proxy
   * 307 - temporary redirect
   * 308 - permanent redirect

4. serie 400
   * 400 - bad request
   * 401 - unauthorised
   * 402 - payment required
   * 403 - forbidden
   * 404 - not found
   * 405 - method not allowed
   * 405 - not acceptable
   * 406 - not acceptable
   * 407 - proxy authentication required
   * 408 - request timeout
   * 409 - conflict
   * 410 - gone
   * 411 - length required
   * 412 - precondition failed
   * 413 - payload too large
   * 414 - uri too long
   * 415 - unsupported media type
   * 416 - range not satisfiable
   * 417 - expectation failed
   * 418 - i'm a teapot
   * 421 - misdirected request
   * 422 – Unprocessable Entity
   * 423 – Locked
   * 424 – Failed Dependency
   * 426 – Upgrade Required
   * 428 – Precondition Required
   * 429 – Too Many Requests
   * 431 – Request Header Fields Too Large
   * 451 – Unavailable For Legal Reasons
  
5. Specific to the server-side error.
   * 500 – Internal Server Error
   * 501 – Not Implemented
   * 502 – Bad Gateway
   * 503 – Service Unavailable
   * 504 – Gateway Timeout
   * 505 – HTTP Version Not Supported
   * 506 – Variant Also Negotiates
   * 507 – Insufficient Storage
   * 508 – Loop Detected
   * 510 – Not Extended
   * 511 –  Network Authentication Required

En cuanto a los métodos soportados por el API REST, tenemos:

|MÉTODO|DESCRIPCIÓN|
|------|-----------|
|GET|Es utilizado únicamente para consultar información al servidor, muy parecidos a realizar un SELECT a la base de datos. No soporta el envío del payload|
|POST|Es utilizado para solicitar la creación de un nuevo registro, es decir, algo que no existía previamente, es decir, es equivalente a realizar un INSERT en la base de datos. Soporta el envío del payload|
|PUT|Se utiliza para actualizar por completo un registro exi1stente, es decir, es parecido a realizar un UPDATE a la base de datos. Soporta el envío del payload|
|PATCH|Este método es similar al método PUT, pues permite actualizar un registro existente, sin embargo, este se utiliza cuando actualizar solo un fragmento del registro y no en su totalidad, es equivalente a realizar un UPDATE a la base de datos. Soporta el envío del payload|
|DELETE|Este método se utiliza para eliminar un registro existente, es similar a DELETE a la base de datos. No soporta el envío del payload.
|HEAD|Este método se utilizar para obtener información sobre un determinado recurso sin retornar el registro. Este método se utiliza a menudo para probar la validez de los enlaces de hipertexto, la accesibilidad y las modificaciones recientes|
|CONNECT|Se utiliza para establecer una comunicación bidireccional con el servidor. En la práctica no es necesario ejecutarlo, si no el mismo API de HTTP se encarga de ejecutarlo para establecer la comunicación previo a lanzar alguna solicitud al servidor.
|OPTIONS|Este método es utilizado para describir las opciones de comunicación para el recurso de destino. Es muy utilizado con CORS (Cross-Origin Resource Sharing) para validar si el servidor acepta peticiones de diferentes origines