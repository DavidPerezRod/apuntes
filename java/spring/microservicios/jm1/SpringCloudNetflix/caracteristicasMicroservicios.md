## Características arquitectura netflix
 
 * Realizan una única función.
 * Independientes. No conocen la implementación de otros servicios. Deberían poder escalarse, desplegarse, monitorizarse y probarse de forma independiente
 * Registro y auto-descubrimiento de servicios. Cada vez que se despliega un servicio, éste la pasa todos los datos significativos para su contacto, ip, puerto, nobmre, etc. al servidor.(Eureka)
 * Auto escalado. Se puede configurar dependiendo de la demanda
 * Confiabilidad y tolerancia a fallos (hystrix)
 * balanceo de carga (ribbon)
 * Configuración centralizada. (Spring flow config)

## Ventajas
* Nueva tecnología y adopción de procesos. Porque los servicios son independientes entre sí
* Reducción de costo debidos al autoescalamiento dinámico
* Ciclos de liberación más rápidos. Ya que los componenetes son mucho más pequeños
* El trabajo se puede dividir en pequeños equipos, ya que los desarrollos son más pequeños e independientes. Solo se debe conocer el contrato de las interfaces.