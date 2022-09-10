# HTTP Basic Authentication

HTTP Basic Authentication es parte de la especificación HTTP, cuyo origen está en las especificaciones RFC 2617 y su actualización 7617. Proporciona una vía estándar para el envío de nombre de usuario y su password desde los clientes. Hay dos formas de hacerlo:

* uno es mediante URL encoding, que es menos habitual, cuyo formato es: https://username:password@www.example.com. Sin embargo, el usuario y password no van encriptados, cualquiera que intercepte la petición puede obtener sus valores.
* HTTP Header. Se envía una cabecera cuya clave es Authorization, y el valor es el mismo username:password codificados en base64. Sin embargo este método tampoco es seguro.

## Problemas de Basic Authentication

* No son seguros ya que decodificar Base64 es trivial.
* además, HTTP Basic Authentication, envía las credenciales en cada petición, lo que incrementa el riesgo.
* para proteger las credenciales se recomienda utilizar HTTPS.

### Spring Boot Security Autoconfiguration

Cuando Spring Boot detecta Spring Security en el classpath, configura Spring Security para trabajar con HTTP Basic Authentication. El nombre de usuario se configura mediante las propiedades:

* spring.security.user.name
* spring.security.user.password

Si no se configura el usuario por defecto será "user" y la pasword se configura de forma aleatoria mediante UUID que saldrá por consola.

En cuanto a los paths, Spring security securiza todos los paths excepto actuator endpoint y el health endpoint.

### Test de Spring Security

Se puede utilizar dos tipos de pruebas básicas. La primera para lógica de seguridad y la segunda para autenticación

```java
    @WithMockUser("spring")
    @Test
    void findBeers() throws Exception{
        mockMvc.perform(get("/beers/find"))
                .andExpect(status().isOk())
                .andExpect(view().name("beers/findBeers"))
                .andExpect(model().attributeExists("beer"));
```

En este primer ejemplo la anotación @WithMockUser, permite probar la lógica asociada al usuario "spring". 

Sin embargo, en este otro ejemplo:

```java

    @Test
    void findBeersWithHttpBasic() throws Exception{
        mockMvc.perform(get("/beers/find").with(httpBasic("spring", "guru")))
                .andExpect(status().isOk())
                .andExpect(view().name("beers/findBeers"))
                .andExpect(model().attributeExists("beer"));
    }
```

Lo que se prueba es la autenticación propiamente dicha. Será necesario tener configurado un usuario "Spring" con password "guru" para que el test funcione, si no, fallará la autenticación en Spring y fallará el test.