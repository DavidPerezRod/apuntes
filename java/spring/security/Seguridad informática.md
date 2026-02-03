# Seguridad informática

Integridad: La finalidad de este principio es asegurar que los datos no han sido                           
alterados ni destruidos de modo no autorizado. Para evitar este tipo de riesgos se                           
debe dotar al sistema de mecanismos que prevengan y detecten cuándo se produce                         
un fallo de integridad y que puedan tratar y resolver los errores que se han                             
descubierto. 
 
Confidencialidad: Se define la confidencialidad como el hecho de que los datos o                         
información estén únicamente al alcance del conocimiento de las personas,                   
entidades o mecanismos autorizados, en los momentos autorizados y de una                     
manera autorizada.  
 
Disponibilidad: La disponibilidad está asociada a la fiabilidad técnica de los                     
componentes del sistema de información y se define como el grado en el que un                             
dato está en el lugar, momento y forma en que es requerido por el usuario                             
autorizado. 
 
Responsabilidad. Todas las acciones relevantes de seguridad del software o de los                       
usuarios se deben almacenar y rastrear, con atribución de responsabilidad. Este                     
rastreo debe ser posible en ambos casos, es decir, mientras y después de que la                             
acción registrada ocurra. Según la política de seguridad para el sistema se debería                         
indicar cuáles acciones se consideran como seguridad relevante, lo que podría hacer                       
parte de los requisitos de auditoría. 
 
No repudio. Esta propiedad le permite al software y a los usuarios refutar o denegar                             
responsabilidades de acciones que ha ejecutado. Esto asegura que la                   
responsabilidad no puede ser derribada o evadida. 

## OWASP Top 10 

El Proyecto de seguridad de aplicaciones web abiertas (OWASP) es un comunidad 
abierta dedicada a permitir a las organizaciones desarrollar, comprar y mantener 
aplicaciones seguras. 
 
El desarrollo de software se ha vuelto cada vez más complejo y la dificultad de lograr 
la seguridad de las aplicaciones aumenta exponencialmente. El rápido ritmo de los 
modernos procesos de desarrollo de software hace que los riesgos más comunes 
sean esenciales para descubrir y resolver de forma rápida y precisa.  
El proyecto OWASP presenta los 10 errores de seguridad más comunes en el 
desarrollo del software: 
 
A1:2017 - Inyección 
Estas fallas ocurren cuando se envían datos no confiables a un intérprete, como                         
parte de un comando o consulta y permiten la ejecución de comandos involuntarios                         
y/o acceso a los datos sin la debida autorización. 
 
A2:2017 - Pérdida de Autenticación y Gestión de Sesiones 
Funciones implementadas incorrectamente relacionadas a autenticación y gestión               
de sesiones en la aplicación, permiten a los atacantes comprometer usuarios,                     
contraseñas, token de sesiones o explotar otras fallas de implementación para                     
asumir la identidad de otros usuarios. 
 
A3:2017 - Exposición de Datos Sensibles 
El mal manejo a los datos sensibles permite a los atacantes robar o modificar dichos                             
datos con el fin de cometer delitos. Los datos sensibles requieren métodos de                         
protección adicionales, como el cifrado en almacenamiento y tránsito. 
 
A4:2017 - Entidad Externa de XML (XXE) 
Muchos procesadores XML antiguos o mal configurados evalúan referencias a                   
entidades externas en documentos XML. Las entidades externas pueden utilizarse                   
para revelar archivos, escanear puertos de la LAN, ejecutar código de forma remota                         
y realizar ataques de denegación de servicio (DoS). 
 
A5:2017 - Pérdida de Control de Acceso 
Esta falla se debe a la falta de restricción o validaciones sobre los permisos que                             
tienen los usuarios autenticados en la aplicación.  
 
A6:2017 - Configuración de Seguridad Incorrecta 
Hace referencia a la falta o mala configuración de seguridad de todo el conjunto de                             
elementos que comprende el despliegue de una aplicación web, desde la misma                       
aplicación hasta la configuración del sistema operativo o el servidor web.  
 
A7:2017 - Secuencia de Comandos en Sitios Cruzados (XSS) 
Los XSS ocurren cuando una aplicación toma datos no confiables y los envía al                           
navegador web sin una validación y codificación apropiada los cuales permiten                     
ejecutar comandos en el navegador de la víctima. 
 
A8:2017 - Deserialización Insegura 
Estos defectos ocurren cuando una aplicación recibe objetos serializados dañinos y                     
estos objetos pueden ser manipulados o borrados por el atacante para realizar                       
ataques de repetición, inyecciones o elevar sus privilegios de ejecución. 
 
A9:2017 - Uso de Componentes con Vulnerabilidades Conocidas 
La implementación de un componente externo se debe analizar a fondo debido a                         
que estos pueden contener vulnerabilidades debilitando las defensas de las                   
aplicaciones y permitir diversos ataques e impactos. 
 
A10:2017 - Registro y Monitoreo Insuficientes 
El registro y monitoreo insuficiente, junto a la falta de respuesta ante incidentes                         
permiten a los atacantes mantener el ataque en el tiempo, pivotear a otros sistemas                           
y manipular, extraer o destruir datos. 