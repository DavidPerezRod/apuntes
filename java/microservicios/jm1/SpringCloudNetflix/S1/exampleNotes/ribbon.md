
* Una vez que se incluye en el starter, se puee modificar la anotación @FeignClient, y en lugar de indicar name y url, se puede indicar solamente el name con el nombre que se le haya dado al servicio en el applicatión properties.
* También hay que añadir la anotación @RibbonClient(name ="mismo-nombre-servicio-que-en-el-cliente-feign") en la clase @SpringBootApplication
* en application.properties hay que añadir la declaración: nombre-servicio.ribbon.listOfServers= host:puerto, host:puerto.
* El nombre de @FeignClient, @RibbonClient y el prefijo "nombre-servicio" de application.properties del punto anterior debe ser el mismo.
* Si no se utiliza Feign, hay que utilizar la anotación @LoadBalanced en el punto que declare la RestTemplate que vayamos a utilizar. sección1-20-minuto 2. Además en la parte de la url del restTemplate no hay que hacer mención a host, sino al nombre del servicio. Quedaría algo así como http://nombre-servicio/mappings. Sección1-20-minuto 3