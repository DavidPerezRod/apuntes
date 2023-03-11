# HTML

Una página HTML tiene una estructura estándar:



<!DOCTYPE HTML> -> define la versión de html
<HTML>
    <HEAD> -> datos no visibles. Son metadatos de la página. Codigicación, estílo, etc
    </HEAD>
    <BODY> -> contenido visible dela página web
    </BODY>
</HTML>

## Etiqueas

* **HTML**
    * Atributos
        * lang. Define el idioma que se va a utilizar
* **HEAD** Datos no visibles, metadatos de la página
* **TITLE** Dentro del head, el título de la pestaña del navegador
* **BODY** contenido visible de la página web
* **H** encabezados
    * H1. Solo debería haber uno solo en la web
* **P** parrafo
* **BR** Salto de línea, no tiene contenido, así que es un tag autocerrado
* **HR** Salto de línea, separado por una línea, no tiene contenido, así que es un tag autocerrado
* **STRONG** negrita
* **EM** cursiva
* **I** cursiva
* **BLOCKQUOTE** cursiva
* **OL** lista ordenada
* **UL** lista no ordenada
* **LI** elementos de una lista

Las listas se pueden agrupar entre ellas. Es decir dentro de una eitqueta <LI> se puede declarar otra lista ordenada o desordenada
* **SPAM** no hace nada, pero permite poner un atributo de identficación, o clases css. Así que principalmente se trata de una etiqueta de maquetación

* **IMG** Esta etiqueta es un solo tag cerrado, que se maneja por medio de sus atributos
    * **alt** texto alternativo por si no carga la imagen
    * **title** texto que debe mostrar cuando el ratón pasa por encima de la imagen
    * **src** par indicar la ruta absoluta o relativa en la que se encuentra la imagen
    * **with** ancho de la imagen
    * **height** alto de la imagen

    **_Para no deformar la imagen lo ideal es tocar uno solo de los dos parámtros, altura o anchura_**