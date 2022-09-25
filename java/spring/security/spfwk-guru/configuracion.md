# Configuración

## Entendiendo cómo se aplican cadenas de filtros

En este primer ejemplo, lo que se hace es coger la configuración HTTP por defecto de WebSecurityConfigurerAdapter, y copiarla en la clase que declaramos para sobrescribirla:

```java
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().anyRequest().authenticated().and().formLogin().and().httpBasic();
    }

```

Este código indica que se todas las peticiones deben estar autenticadas.

Sin embargo, si por ejemplo queremos establecer alguna excepción a esa configuración, podríamos declarar una regla authorizeRequest, que se aplicase antes que la configuración por defecto:

```java
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests(
                authorize -> {
                    authorize.antMatchers("/").permitAll()
                })
            .authorizeRequests().anyRequest().authenticated().and().formLogin().and().httpBasic();
    }

```

Y se podría complicar tanto com se quisiese:

```java
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests(authorize -> authorize.antMatchers("/", "/webjars/**", "/login", "/resources/**").permitAll()
                .antMatchers("/example/find", "/example*").permitAll())
                .authorizeRequests().anyRequest().authenticated().and().formLogin().and().httpBasic();
    }
```

Es más incluso se pueden especificar paths restringidos a determinados verbos:

```java
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests(authorize -> authorize.antMatchers("/", "/webjars/**", "/login", "/resources/**").permitAll()
                .antMatchers("/example/find", "/example*").permitAll()
                .antMatchers(HttpMethod.GET, "/api/v1/example/**").permitAll())
                .authorizeRequests().anyRequest().authenticated().and().formLogin().and().httpBasic();
    }
```