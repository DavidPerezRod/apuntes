* hay que añadir el paquete a través del starter de spring boot
* En la clase @SpringBootApplication, hay que incluir también la anotación @EnableFeignClients.
* @FeignClient. Se declara en la interfaz del ciente, para indicar que es un cliente feign. Y además hay que añdir el valor con el nombre y la url del servicio
  * @FeignClient (name = "nombre-servicio", url="url")
* En los métodos de la interfaz indicaremos con @GetMapping, el endpoint que se va a consumir. La nomenclatura debe esr la misma que la del @GetMapping del endpint del servicio que se va a cosumir