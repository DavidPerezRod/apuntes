# Introducción

## Tabla de comandos

Python es un lenguaje orientado a objetos, así que todos sus elementos van a ser clases.

|comando|descripción|sintaxis|
|-------|-----------|--------|
|print|escribe por consola|print(valor) o print(texto, valor). Se puede utilizar interpolación con f'cadena {variable}' dentro del printf. También permite utilizar comilla triple para escribir información en varias lineas: printf(''' texto multilinea1 texto multilinea2 ''')
|id|obtiene la dirección de memoria de la variable|id(variable)
|type|obtiene el tipo de la variable|type(variable)
|input|procesa la entradad de usuario|input() o input (variable). Este segundo caso concatena la entra de usuario a la variable
|int()|hacel el casting a entero|int (valor)
|str()|hacel el casting a cadena|str (valor)
|bool()|si la cadena tiene contenido True si no False|bool (valor)

Definición de tipos. En python no hace falta declarar los tipos de las variables, estos se asignan de forma dinámica depenciendo del 
contenido de la variable. 

Se puede indicar la intención con la sintaxis:

X: str = "hola"

Pero será el intérprete el que acaba definiendo su tipo. De forma que si declaramos:

X: int = "hola"

No se obtiene un error, la variable simplemente será de tipo cadena. Además las variables son dinámicas y pueden cambiar en cualquier momento el tipo de dato que representan

Otra forma de imprimir en Python es mediante interpoalción con format (f)

print(f'se pone el tenxto y entre llaves una variable a concatenar {nombreVariable}')

En python los valores booleanos son True y False, capitalizada la primera letra.

**_Ojo con los ámbitos de las variables. Estudiarlos_**

## Sentencias de control


|comando|sintaxis|
|-------|-----------|
|if|if condicion :|
|if - else|if condicion : else:|
|else if|elif condicion :|
|while|while condicion :|
|while - else |while condicion : else: ejecucion alternativa|
|for|for contador in variable :|
|for - range|for contador in range (valor) :|
|for - else |for contador in variable : else: ejecución alternativa|
|break|break|
|continua |continua|

### if

Python separa los bloques de las sentencias de control por ": + tabulación dentro del bloque". No utiliza llaves

Además para las sentencias de control, hay que tener en cuenta que en python cualquier condición de una variable distinta de vacío es True.

condicion = "hola"

if condicion: --> la ejecución entrará por el bloque if.

---------------

condición = ''

if condicion:
    ejecución 1
else:
    ejecución 2 --> la ejecución entrará por este otro bloque


Al igual que en Java, existe un operador ternario para hacer más compacta la escritura de algunas sentencias de control. Sin embargo su sintaxis es muy distinta:

(rama if) if condicion else (rama else)

Sin embargo por su dificultad de lectura se recomienda solo en expresiones muy simples.

### while

La gran diferencia con otros lenguajes, es que admite una condición de salida, esto es:

while condicion:
    ejecucion1
else: 
    ejecucion2

### for