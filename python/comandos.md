# Introducción

## Tabla de comandos

Python es un lenguaje orientado a objetos, así que todos sus elementos van a ser clases.

|comando|descripción|sintaxis|
|-------|-----------|--------|
|print|escribe por consola|print(valor) o print(texto, valor)
|id|obtiene la dirección de memoria de la variable|id(variable)
|type|obtiene el tipo de la variable|type(variable)
|input|procesa la entradad de usuario|input() o input (variable). Este segundo caso concatena la entra de usuario a la variable
|int()|hacel el casting a entero|int (valor)
|str()|hacel el casting a cadena|str (valor)

Definición de tipos. En python no hace falta declarar los tipos de las variables, estos se asignan de forma dinámica depenciendo del 
contenido de la variable. 

Se puede indicar la intención con la sintaxis:

X: str = "hola"

Pero será el intérprete el que acaba definiendo su tipo. De forma que si declaramos:

X: int = "hola"

No se obtiene un error, la variable simplemente será de tipo cadena. Además las variables son dinámicas y pueden cambiar en cualquier momento el tipo de dato que representan

## Operadores