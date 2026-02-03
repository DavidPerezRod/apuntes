# Password Security

Probablemente la característica más básica de este aspecto de Spring Security es evietar que las contraseñas se puedan almacenar en "claro" en las BBDD de la aplicación.

## Password Encoding

En algunos sistemas, el tratamiento de password no es del todo adecuado, bien porque se almacenan en BBDD como texto plano, o bien porque se guardan encriptadas, pero se desencriptan para verificar su validez.

Una opción intermedia es generar una hash a partir de la password. En este escenario, lo que se hace es aplicar un algoritmo de un solo sentido (no se puede obtener la password por medio de la hash).
Sin embargo un inconveniente que puede tener esta aproximación es que dada la hash, se puede tratar de hacer un ataque de diccionario probando diferentes algoritmos sobre diversas passwords para tratar de adivinar qué esconde la hash.

Una solución para evitar este tipo de ataques es lo que se conoce com Password Salt, que consiste en añadir cadenas de bits aleatorios a la función generadora de claves. Cada bit de "sal" duplica la cantidad de almacenamiento y computación requeridas para un ataque de diccionario.

Para tener mayor seguridad, el valor de sal se guarda separado de la BBDD de contraseñas, ya que supone una ventaja cuando la base de datos es robada, ya que puede no servir de mucho si no se tiene la "sal".

Dentro del área de seguridad, las funciones hash suponen una "carrera armamentística", de manetra que a medida que el poder de computación se incrementa, se encuentran más vulnerabilidades. 

Spring Security soporta compatibilidad con funciones hash descatalogadas o antiguas, por compatibilidad con legacy systems, pero este tipo de encoders están descatalogados (deprecated).

Por este motivo, Spring 5 introduce lo que se denomina Delegating Password Encoder, cuya función es almacenar las hashes de password en múltiples formatos y tener la capacidad de poderlas migrar posteriormente. Esto se consigue almacenando el hash encoder y el hash value.

Spring Security recomienda utilizar los siguientes tipos de codificaciones "one way"

* BCrypt (Default)
* Pbkdf2
* SCrypt

Todos estos algoritmos de encriptación se consideran "lentos", y computacionalmente costosos contra ataques de fuerza bruta.

Algunos de los encoders que se pueden utilizar en Spring Security, así como la forma de utilizarlos, es:

```java
    //este encoder está descatalogado y se desaconseja su uso, ya que no realiza ninguna operación de codificación. Sin embargo es una posibilidad que proporciona Spring para trabajar con sistemas legacy.
    @Test
    void testNoOP(){
        PasswordEncoder noOp= NoOpPasswordEncoder.getInstance();
        System.out.println(noOp.encode(PASSWORD));
    }
```

Se se declara un PasswordEncoder de este tipo, entonces, se puede eliminar la indicación {noop} que se introdujo a la hora de definir las secciones InMemoryAuthentication. Una forma de declarlo puede ser la siguiente, dentro de la propia clase SecurityConfig (la que herede de WebSecurityConfigurerAdapter):

```java
    @Bean
    PasswordEncoder passwordEncoder(){
        return NoOpPasswordEncoder.getInstance();
    }
```

### MD5

MD5 es uno de los algoritmos de reducción criptográficos diseñados por el profesor Ronald Rivest del MIT (Massachusetts Institute of Technology, Instituto Tecnológico de Massachusetts). Fue desarrollado en 1991 como reemplazo del algoritmo MD4 después de que Hans Dobbertin descubriese su debilidad.

A pesar de su amplia difusión actual, la sucesión de problemas de seguridad detectados desde que, en 1996, Hans Dobbertin anunciase una colisión de hash, plantea una serie de dudas acerca de su uso futuro.

```java
    void hashingExample(){
        String passwordEncoded1= DigestUtils.md5DigestAsHex((PASSWORD.getBytes()));
        String passwordEncoded2= DigestUtils.md5DigestAsHex((PASSWORD.getBytes()));
        System.out.println(DigestUtils.md5DigestAsHex((PASSWORD.getBytes())));
        System.out.println(DigestUtils.md5DigestAsHex((PASSWORD.getBytes())));

        String salted= PASSWORD + "ThisIsMySALTVALUE";
        String saltedPassword=DigestUtils.md5DigestAsHex((salted.getBytes()));
        System.out.println(DigestUtils.md5DigestAsHex((salted.getBytes())));
        assertEquals(passwordEncoded1, passwordEncoded2);
        assertNotEquals(passwordEncoded1, saltedPassword);
        assertNotEquals(passwordEncoded2, saltedPassword);
    }
```

### LDAP

Se trata de un algoritmo de codificación bastante antiguo, pero muy utilizado.

Spring lo desaconseja su uso y es por ello que lo marca como descatalogado. LDAP utiliza una "salt" aleatoria por cada ejecución, de forma que si se ejecuta dos veces la misma encriptación genera salidas diferentes:

```java
    @Test
    void testLDAP(){
        PasswordEncoder ldap= new LdapShaPasswordEncoder();
        System.out.println(ldap.encode(PASSWORD));
        System.out.println(ldap.encode(PASSWORD));
    }
```