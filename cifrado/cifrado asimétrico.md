# CIFRADO ASIMÉTRICO - CIFRADO DE CLAVE PÚBLICA

Se trata de un método de cifrado en el que se utilizan dos claves complementarias, de forma que todos los mensajes cifrados con una clave solo pueden ser descifrados con la otra y viceversa.
La clave pública se comprtirá con muchos usuarios, y la clave privada se mantendrá confidencial. Además una clave, no puede cifrar y descifrar al mismo tiempo. 

El algoritmo en el que se basa el sistema es: 

    1. Generar dos números primos muy grandes, p y q
    2. Hacer n=p*q
    3. Calcular z=(p-1)*(q-1)
    4. Elegir un pequeño número k, co-primo de z, de modo que el Máximo Común Divisor entre z y k sea 1, con 1<k<z
    5. Encontrar un número j tal que el (j mod z) sea 1
    6. Publicar k y n como la clave pública.
    7. Guardar j como la clave privada.

Luego, para el cifrado se usan estas expresiones, basadas en los anteriores valores:

    * mensaje_cifrado = (mensaje_plano)^k mod n
    * mensaje_plano = (mensaje_cifrado)^j mod n

**No es un algoritmo que pueda resistir un ataque cuántido, es seguro para las computadoras actuales.**