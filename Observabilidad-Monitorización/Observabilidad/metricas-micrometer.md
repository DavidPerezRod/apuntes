## Tooling: Micrometer (instrumentación en Java/Spring)

Micrometer es la **fachada (vendor-neutral)** estándar en el ecosistema Java/Spring para **definir y registrar métricas** sin acoplarte a un backend concreto (Prometheus, Datadog, New Relic, CloudWatch, etc.). Tu código emite *meters* (Counter, Timer, Gauge, …) contra un `MeterRegistry`, y el **registry** se encarga de “publicar/exportar” al sistema de monitorización.

### Cómo encaja con las 3 características de un sistema de métricas

- **Dimensionality (dimensionalidad)**: en Micrometer una métrica se identifica por **nombre + tags** (tags = labels/dimensiones).
- **Rate aggregation (agregación por tasa)**: Micrometer emite *counters* y *timers*; la **tasa** (`rate`) y agregaciones se calculan **en el backend** (Prometheus/Grafana, Datadog, etc.), no en el código.
- **Publishing (publicación)**: se realiza a través del `MeterRegistry` concreto (p. ej. `PrometheusMeterRegistry`) y el endpoint de scrape (`/actuator/prometheus`) o el mecanismo de export correspondiente (push/pull según backend).


## 1) Conceptos clave

### `MeterRegistry`

- Es el **punto de entrada** donde el servicio **registra** y **actualiza** métricas.
- En Spring Boot, lo normal es **inyectarlo** (DI) en servicios/componentes para instrumentar sin estado global.
- En tests o local “sin backend”, se puede usar `SimpleMeterRegistry` para validar que tus counters/timers/gauges se crean y evolucionan.

### `Metrics.globalRegistry`

Es un **registro global y estático** (singleton) donde puedes registrar meters sin tener una referencia explícita a un `MeterRegistry`.

- Internamente suele ser un **`CompositeMeterRegistry`** (ver apartado específico), es decir: un “contenedor” que puede delegar en uno o varios registries reales (Prometheus, Datadog, etc.).
- Su utilidad es cubrir escenarios donde **no se tiene DI** o no se dispone de un registry accesible:
  - librerías reutilizables que no dependen de Spring,
  - código muy genérico/estático,
  - utilidades inicializadas fuera del ciclo de vida del contenedor,
  - aplicaciones que no usan un framework de inyección.

**Por qué es útil en librerías o código genérico**
- Permite instrumentar sin acoplarse a un framework (Spring, Guice, CDI, etc.).
- Reduce fricción de adopción: el usuario “conecta” su registry al global y la librería empieza a emitir métricas.
- Facilita el patrón *drop-in*: se integra la librería y “aparecen” métricas sin wiring invasivo.

**Por qué en microservicios Spring se prefiere inyección de `MeterRegistry`**
- **Testabilidad y aislamiento**: sin estado global compartido entre tests.
- **Control explícito del wiring**: registry alineado con `commonTags`, `MeterFilter`, lifecycle y backend configurado.
- **Menos acoplamiento oculto**: la dependencia queda visible en el constructor, lo que mejora mantenibilidad y gobierno de instrumentación.

### Ejemplo típico (inyección por constructor)

~~~java
@Service
public class PaymentService {

    private final MeterRegistry meterRegistry;

    public PaymentService(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    public void doWork() {
        meterRegistry.counter("business.operations", "operation", "payment_confirm")
                     .increment();
    }
}
~~~

### `CompositeMeterRegistry`

`CompositeMeterRegistry` es un `MeterRegistry` que **no exporta por sí mismo** a un backend concreto. Su función es **agregar** (componer) varios registries “reales” y **delegar** en todos ellos.

### Qué resuelve (y por qué existe)

- **Multi-backend**: publicar la misma métrica en más de un sistema (p. ej. Prometheus + OTLP/Datadog) sin duplicar instrumentación.
- **Migraciones**: mantener dos backends a la vez durante un periodo de transición.
- **Librerías**: permitir que una librería registre métricas sin saber a qué backend final irán; el consumidor decide qué registries se conectan.

### Cómo funciona (modelo mental)

- Tu código registra un meter **una sola vez** (por nombre + tags).
- El composite mantiene ese “catálogo” de meters y **lo replica**/delegará sobre cada registry conectado.
- Cuando incrementas un counter o registras un timer, el composite propaga la actualización a todos los registries miembros.

### Trade-offs / riesgos operativos

- **Coste multiplicado**: publicar a N backends implica *N veces* almacenamiento/ingesta/consulta (y posibles cuotas/costes).
- **Semánticas divergentes**: no todos los backends representan igual histogramas/percentiles; el mismo meter puede verse distinto.
- **Orden de inicialización**: si conectas registries tarde, puedes perder contexto o crear inconsistencias (depende del patrón de creación/registro).
- **Duplicación accidental**: incluir sin querer dos exporters puede generar publicación doble (y confusión en dashboards).

### Cuándo usarlo (regla práctica)

- **Sí**: migración controlada, necesidades multi-backend justificadas, plataformas con observabilidad “dual”.
- **No por defecto**: si solo necesitas un backend, mantener un único registry reduce complejidad y coste.

### Ejemplo (conceptual)

~~~java
CompositeMeterRegistry composite = new CompositeMeterRegistry();

PrometheusMeterRegistry prom = /* ... */;
MeterRegistry other = /* ... */;

composite.add(prom);
composite.add(other);

Counter c = composite.counter("business.operations", "operation", "payment_confirm");
c.increment(); // se refleja en ambos registries
~~~

**Nota Spring Boot**: cuando hay más de un exportador/registry configurado, Spring Boot puede acabar exponiendo un `MeterRegistry` que actúa como agregador (comportamiento equivalente a un composite), de forma que tu código sigue inyectando `MeterRegistry` sin enterarse de cuántos backends hay detrás.


## 2) Tipos de *meters* y qué genera en Prometheus

Micrometer soporta (entre otros):

- **Counter**: acumulativo monótono (en Prometheus suele acabar como `*_total`).
- **Gauge**: valor instantáneo (sube/baja).
- **Timer**: duración y conteo (en Prometheus expone `_count`, `_sum` y opcionalmente `_bucket` si hay histograma).
- **DistributionSummary**: distribución de un valor **no temporal** (tamaños, bytes, etc.) → `_count`, `_sum` y opcionalmente buckets.
- **LongTaskTimer**: tareas largas “en curso” (útil para jobs o procesos con duración no trivial).
- **FunctionCounter / TimeGauge**: variantes basadas en funciones/lecturas.


## 3) Micrometer: uso e inyección de `MeterRegistry`, `CompositeMeterRegistry` y “operadores” (meters)

Esta sección aterriza dos ideas:

1) **Qué significa `builder(...).register(registry)`**  
2) **Qué ocurre si hay varios registries y un `CompositeMeterRegistry`**  

Además incluye un patrón didáctico de **inyección escalada**:
- (Fase 1) crear registries “leaf” (específicos),
- (Fase 2) componerlos en un `CompositeMeterRegistry`,
- (Fase 3) definir “operadores” (Counter/Timer/Gauge/Summary) como **beans** registrados contra el composite.



###  Qué hace realmente `register(registry)`

En Micrometer, el `builder()` **no registra** nada. Solo construye la definición del meter.

- `Counter.builder("x")...` = **define** nombre, tags, descripción, unidades, etc.
- `.register(registry)` = **registra** (alta) ese meter en el `MeterRegistry` y devuelve la instancia final.

### Clave semántica
Un meter se identifica por su **ID**:
- **nombre + conjunto de tags** (y algún metadato adicional interno).

Si vuelves a pedir/registrar el mismo ID en el mismo registry, Micrometer tiende a:
- **devolver el mismo meter** (reutilización), o
- en algunos casos, **rechazar** si intentas registrar algo “incompatible” con el mismo ID.



###  Qué cambia con `CompositeMeterRegistry`

`CompositeMeterRegistry` es un `MeterRegistry` que **delegará** en varios registries “reales” (*leaf registries*).

### Si haces `register(composite)`
- El meter queda registrado “a través” del composite.
- Y el composite se encarga de que:
  - las operaciones (`increment`, `record`, etc.) se **propaguen** a todos los registries miembros.

### Implicación práctica
- Instrumentas **una vez**.
- Publicas en **N backends** (si hay N registries detrás).

### Importante: “añadir registries después”
Técnicamente puedes añadir registries al composite “en caliente”, pero operativamente **es un antipatrón**:
- introduces dependencia del orden de inicialización,
- puedes ver ventanas de tiempo donde un backend “nuevo” no refleja lo anterior,
- dificulta razonar sobre por qué ciertas series aparecen o no.

En Spring Boot, la práctica estándar es: **registries se configuran al arranque**, no dinámicamente.



###  Inyección escalada

Nota: esto es un patrón “explicativo”. En un Spring Boot típico con Actuator/exporters, normalmente **no creas registries a mano**; se auto-configuran. Pero el siguiente montaje ilustra perfectamente lo que preguntas.

#### Fase 1 — Crear registries específicos (leaf registries)

Aquí uso `SimpleMeterRegistry` para que el ejemplo sea autocontenido. En la vida real, tus leaf registries serían `PrometheusMeterRegistry`, `DatadogMeterRegistry`, OTLP, etc.

~~~java
@Configuration
public class LeafRegistriesConfig {

    @Bean
    public MeterRegistry registryA() {
        return new SimpleMeterRegistry();
    }

    @Bean
    public MeterRegistry registryB() {
        return new SimpleMeterRegistry();
    }
}
~~~

#### Fase 2 — Componerlos en un `CompositeMeterRegistry`

Este bean depende de los leaf registries: cuando se crea, ya los recibe y los añade.

~~~java
@Configuration
public class CompositeRegistryConfig {

    @Bean
    public CompositeMeterRegistry compositeMeterRegistry(
            MeterRegistry registryA,
            MeterRegistry registryB
    ) {
        CompositeMeterRegistry composite = new CompositeMeterRegistry();
        composite.add(registryA);
        composite.add(registryB);
        return composite;
    }
}
~~~

**Qué garantiza esto**  
- Como el composite **depende** de `registryA` y `registryB`, Spring construye primero los leaf registries.
- Luego construye el composite ya “cableado”.
- Así evitas el problema de “añadir registries tarde”.

#### Fase 3 — Definir “operadores” (meters) como beans registrados contra el composite

Aquí se ve explícitamente tu pregunta: **`register(registry)` registra el meter en el registry**.  
Si el registry es composite, queda instrumentado para todos los miembros.

Incluyo más que counters:
- `Counter` (eventos acumulativos)
- `Timer` (latencias)
- `DistributionSummary` (tamaños/cantidades no temporales)
- `Gauge` (estado instantáneo con `AtomicInteger`)

~~~java
@Configuration
public class OperatorsConfig {

    // Gauge suele necesitar un "holder" estable
    @Bean
    public AtomicInteger inFlightEmployees() {
        return new AtomicInteger(0);
    }

    @Bean
    public Counter empleadosCounter(CompositeMeterRegistry registry) {
        return Counter.builder("numero.empleados")
                .description("Incrementos de empleados por oficina")
                .tag("oficina", "Benito Juárez")
                .register(registry);
    }

    @Bean
    public Timer empleadosProcessTimer(CompositeMeterRegistry registry) {
        return Timer.builder("empleados.proceso.duration")
                .description("Duración de operaciones relacionadas con empleados")
                .tag("oficina", "Benito Juárez")
                .register(registry);
    }

    @Bean
    public DistributionSummary empleadosBatchSize(CompositeMeterRegistry registry) {
        return DistributionSummary.builder("empleados.batch.size")
                .description("Tamaño de lotes procesados (no temporal)")
                .baseUnit("items")
                .tag("oficina", "Benito Juárez")
                .register(registry);
    }

    @Bean
    public Gauge empleadosInFlightGauge(
            CompositeMeterRegistry registry,
            AtomicInteger inFlightEmployees
    ) {
        // registra un Gauge que “lee” el valor del AtomicInteger
        return Gauge.builder("empleados.in_flight", inFlightEmployees, AtomicInteger::get)
                .description("Operaciones en vuelo (in-flight)")
                .tag("oficina", "Benito Juárez")
                .register(registry);
    }
}
~~~

###  Uso desde un servicio (inyección de “operadores”)

~~~java
@Service
public class EmployeeService {

    private final Counter empleadosCounter;
    private final Timer processTimer;
    private final DistributionSummary batchSummary;
    private final AtomicInteger inFlight;

    public EmployeeService(
            Counter empleadosCounter,
            Timer empleadosProcessTimer,
            DistributionSummary empleadosBatchSize,
            AtomicInteger inFlightEmployees
    ) {
        this.empleadosCounter = empleadosCounter;
        this.processTimer = empleadosProcessTimer;
        this.batchSummary = empleadosBatchSize;
        this.inFlight = inFlightEmployees;
    }

    public void altaEmpleados(int nuevos, Runnable negocio) {
        inFlight.incrementAndGet();
        try {
            empleadosCounter.increment(nuevos);
            batchSummary.record(nuevos);
            processTimer.record(negocio);
        } finally {
            inFlight.decrementAndGet();
        }
    }
}
~~~

**Qué has conseguido con este enfoque**
- Los meters están **centralizados** y gobernados (naming/tags consistentes).
- Se registran **una vez** en el composite.
- Las operaciones se replican a **todos los leaf registries**.



## 4) Variantes útiles (cuando los tags no son fijos)

Si `oficina` no es fija, suele ser mejor **inyectar `MeterRegistry`** y pedir el meter con tags en el momento, siempre con **baja cardinalidad**.

~~~java
@Service
public class EmployeeService2 {

    private final MeterRegistry registry;

    public EmployeeService2(MeterRegistry registry) {
        this.registry = registry;
    }

    public void altaEmpleados(String oficina, int nuevos) {
        // oficina debe ser catálogo pequeño y estable
        registry.counter("numero.empleados", "oficina", oficina).increment(nuevos);
    }
}
~~~



## 5) Reglas operativas (para que no se vuelva “chungo”)

- **No mezcles** `Metrics.globalRegistry` con DI en Spring salvo que tengas una razón muy clara (librería sin acceso al contenedor).
- **Evita** añadir registries al composite dinámicamente durante ejecución; hazlo al arranque.
- Si defines meters como beans:
  - tags fijos → perfecto.
  - tags variables → mejor inyectar `MeterRegistry` y crear por demanda, con cardinalidad controlada.
- Composite implica **coste multiplicado** (N backends → N ingestiones/almacenamientos).



## 6) Mapa directo a tus 2 preguntas

1) **“¿`register(registry)` registra el counter?”**  
   Sí: `register(...)` es el punto en el que el meter queda dado de alta en el registry.

2) **“¿Qué pasa con varios registries y un composite?”**  
   Si registras contra el composite, el meter se instrumenta una vez y sus operaciones se delegan a todos los registries miembros. Por eso el composite es el mecanismo estándar para multi-backend/migraciones.


## 4) Dependencias y wiring típico (Spring Boot 3 + Prometheus)

### Maven (típico)

~~~xml
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>

<dependency>
  <groupId>io.micrometer</groupId>
  <artifactId>micrometer-registry-prometheus</artifactId>
</dependency>
~~~

### `application.yml` mínimo para scrape

~~~yaml
management:
  endpoints:
    web:
      exposure:
        include: health,info,prometheus
  endpoint:
    health:
      probes:
        enabled: true
~~~

- Endpoint habitual: `GET /actuator/prometheus`
- Prometheus se configura para hacer *scrape* periódicamente a ese endpoint.

## Convenciones de nombres y “transformadores” (Micrometer → backend)

Cuando defines una métrica con Micrometer, el **nombre que tú escribes** no siempre es el **nombre final** que verás en el backend. Hay dos pasos: primero existe un **nombre lógico** (en tu código, con su semántica y tags), y después el `MeterRegistry` aplica una **convención de exportación** (naming convention) y, según el tipo de backend, puede “materializar” series adicionales (`_count`, `_sum`, `_bucket`, etc.). A esto se le suma una capa de **gobierno** (`MeterFilter`) para normalizar o bloquear lo que no cumple el contrato de métricas.

La regla que evita el 80% de confusiones es simple: en Micrometer defines un nombre “limpio” y estable (con puntos), y dejas que el registry del backend haga las transformaciones (por ejemplo, en Prometheus: `.` → `_`, counters → `_total`, timers → `_seconds_*`).



### 1) Convención lógica (Micrometer): el contrato que escribes en el código

En el código Micrometer, una métrica se identifica por su **ID**: `name + tags` (y opcionalmente `baseUnit`, descripción, etc.). Ese ID es el contrato semántico que tú controlas. Aquí el objetivo es que el nombre sea estable y legible, y que los tags mantengan baja cardinalidad. Por ejemplo, medir “empleados creados” por oficina es un caso de counter razonable si `oficina` es un catálogo pequeño y controlado.

En Spring, lo canónico es inyectar `MeterRegistry` y registrar los meters contra ese registry. Si luego el registry es un composite o uno concreto, es un detalle del wiring, no de tu lógica.

~~~java
@Configuration
public class MetricsNamingExampleConfig {

    @Bean
    public Counter empleadosCounter(MeterRegistry registry) {
        // Nombre lógico: sin sufijos específicos de backend.
        // Tags: baja cardinalidad.
        return Counter.builder("empleados.creados")
                .description("Altas de empleados (evento acumulativo)")
                .tag("oficina", "benito_juarez")
                .register(registry);
    }

    @Bean
    public Timer altaEmpleadoTimer(MeterRegistry registry) {
        // Timer = “duración de una operación”
        // Micrometer lo trata como un instrumento de duración; el backend decide cómo representarlo.
        return Timer.builder("empleados.alta.duration")
                .description("Duración de la operación de alta de empleado")
                .tag("oficina", "benito_juarez")
                .register(registry);
    }

    @Bean
    public DistributionSummary payloadSummary(MeterRegistry registry) {
        // DistributionSummary = “distribución de una cantidad no temporal”
        // baseUnit ayuda a que el exportador use sufijos coherentes.
        return DistributionSummary.builder("empleados.payload.size")
                .description("Tamaño del payload procesado en alta")
                .baseUnit("bytes")
                .tag("oficina", "benito_juarez")
                .register(registry);
    }
}

@Service
class EmployeeService {

    private final Counter empleadosCounter;
    private final Timer altaEmpleadoTimer;
    private final DistributionSummary payloadSummary;

    EmployeeService(Counter empleadosCounter, Timer altaEmpleadoTimer, DistributionSummary payloadSummary) {
        this.empleadosCounter = empleadosCounter;
        this.altaEmpleadoTimer = altaEmpleadoTimer;
        this.payloadSummary = payloadSummary;
    }

    public void altaEmpleado(int payloadBytes, Runnable negocio) {
        empleadosCounter.increment();         // evento
        payloadSummary.record(payloadBytes);  // cantidad no temporal
        altaEmpleadoTimer.record(negocio);    // duración
    }
}
~~~

En este punto, lo importante es lo que **no** haces: no añades `_total`, no escribes `_seconds`, no intentas “pre-compatibilizar” con un backend. Tu contrato se queda en el plano Micrometer.



### 2) Convención exportada (backend): cómo lo “traduce” el `MeterRegistry`

El registry aplica una `NamingConvention` que convierte tu nombre lógico al formato exigido por el backend. En Prometheus, el resultado típico es que el nombre pasa a *snake_case* (`.` → `_`) y aparecen sufijos/series derivados por tipo.

Por ejemplo, si tu contrato Micrometer es:

- Counter: `empleados.creados{oficina="benito_juarez"}`
- Timer: `empleados.alta.duration{oficina="benito_juarez"}`
- Summary (DistributionSummary): `empleados.payload.size{oficina="benito_juarez", baseUnit="bytes"}`

en Prometheus **normalmente** verás algo conceptualmente equivalente a:

- Counter:
  - `empleados_creados_total{oficina="benito_juarez"}`
- Timer:
  - `empleados_alta_duration_seconds_count{oficina="benito_juarez"}`
  - `empleados_alta_duration_seconds_sum{oficina="benito_juarez"}`
  - (opcional) `empleados_alta_duration_seconds_max{...}`
  - (si habilitas histograma) `empleados_alta_duration_seconds_bucket{le="...", ...}`
- DistributionSummary:
  - `empleados_payload_size_bytes_count{...}`
  - `empleados_payload_size_bytes_sum{...}`
  - (si habilitas histograma) `empleados_payload_size_bytes_bucket{le="...", ...}`

La idea no es memorizar exactamente cada sufijo, sino entender el patrón: el backend puede “desdoblar” un solo instrumento Micrometer en varias series porque necesita exponer **count**, **sum** y (si aplica) **buckets** para histogramas. En Prometheus, un histograma se representa con buckets acumulativos (`_bucket` + label `le`), además del `count` y el `sum`.

Este es el motivo por el que, si tú escribieras `empleados.creados_total` en el nombre lógico, terminarías con redundancias como `*_total_total`. Por eso el contrato lógico debe ser limpio y el “sufijado” debe ser responsabilidad del registry/exporter.



### 3) Gobierno y seguridad semántica: `MeterFilter` para normalizar y evitar explosiones

La convención por sí sola no evita el problema típico: alguien introduce un tag con alta cardinalidad (`userId`, `documentUuid`, `email`) o crea nombres inconsistentes (“duración en ms” en unos sitios y “seconds” en otros). Para eso está `MeterFilter`: una cadena de filtros aplicada por el registry que te permite **renombrar**, **reescribir tags** o **denegar** métricas que no cumplen el contrato.

Un ejemplo razonable (sin convertir esto en un framework interno) es protegerte contra tags prohibidos y normalizar nombres legacy.

~~~java
@Configuration
public class MetricsGovernanceConfig {

    @Bean
    public MeterRegistryCustomizer<MeterRegistry> metricsGovernance() {
        return registry -> registry.config()
                // Ejemplo: common tags (identidad mínima, baja cardinalidad)
                .commonTags("service", "employee-service", "env", "dev")

                // 1) Denegar métricas que usen tags prohibidos (alta cardinalidad)
                .meterFilter(MeterFilter.deny(id ->
                        id.getTags().stream().anyMatch(t ->
                                t.getKey().equalsIgnoreCase("userId")
                                || t.getKey().equalsIgnoreCase("documentUuid")
                                || t.getKey().equalsIgnoreCase("email")
                                || t.getKey().equalsIgnoreCase("ip")
                        )))

                // 2) Renombrar una métrica legacy a tu contrato estándar
                .meterFilter(MeterFilter.renameTag(
                        "empleados.creados",   // para esta métrica
                        "office",              // tag viejo
                        "oficina"              // tag nuevo
                ));
    }
}
~~~

La capa de gobierno es lo que te permite decir “sí, el equipo puede instrumentar rápido”, pero sin permitir que cualquier decisión de tags/nombres se convierta en un problema de costes o de operabilidad. Además, como todo pasa por el registry, esta capa funciona igual si tu backend es Prometheus o si mañana tienes un `CompositeMeterRegistry` publicando a varios destinos: el contrato se mantiene.



### Cierre: cómo conectar esto con tu sección de “Convenciones mínimas”

Tu sección de convenciones (unidades, sufijos, semántica) es válida, pero el ajuste importante al integrarla con Micrometer es conceptual: las convenciones de sufijos (`_total`, `_seconds`, `_bucket`) suelen ser parte de la **convención exportada** del backend, no necesariamente del nombre lógico Micrometer. Por eso, en un dossier que pretende ser operable, queda redondo separar explícitamente:

1) “Cómo lo nombro en el código” (Micrometer),
2) “Cómo lo veré en Prometheus” (transformación),
3) “Cómo lo gobierno para que no se degrade” (`MeterFilter` y tags de baja cardinalidad).
