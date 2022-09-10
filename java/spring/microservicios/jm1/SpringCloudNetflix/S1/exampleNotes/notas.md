1. Cada servicio debe tener un nombre en el fichero application.properties, que es el que le identificará cuando se migre a Eureka. 
   * se hace mediante la propiedad spring.application.name
2. También se define el puerto mediente
   * server.port
3. Para H2 sin necesidad de declarar nada en el yml de la aplicación o en el fichero de configuración equivalente, se puede declarar un fichero import.sql que SpringBoot ya reconoce de forma automática.
4. La forma de conectar con la consola h2, es en el puerto de arranque configurado para el servidor (el que aparece en la consola). En el caso del ejemplo el 8001, y con el path que indica springboot en la consola. En nuestro caso h2-console.

Otra cosa que hay que tener en cuenta es la url una vez que sale la página de la consola para logarnos, también sale en la consola. En el caso del ejemplo es jdbc:h2:mem.testdb, sin password.

En el caso de intellij, la ruta que hay que poner es distinta, se indica en la consola de arranque de springboot. El formato es parecido a este: jdbc:h2:mem:55b33c9e-6c6d-4198-8ae1-540a6daae831, y es lo que hay que incluir en la configuración en el campo JDBC URL. El resto no se modifica, se deja el mismo usuario y password

5. Crear una clase de configuración de SpringBoot para el autoregistro de componentes, y en la que también se indica el fichero de propiedades que se va a utilizar.
   @Configuration
   @PropertySource("classpath:services.properties")
6. @Primary. Marca un componenete como instancia por defecto a inyectar cuando existe más de un componenete que implmenta la misma interfaz. La otra forma de resolverlo es mediante @Qualifier.