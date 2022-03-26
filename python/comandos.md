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

## Operadores