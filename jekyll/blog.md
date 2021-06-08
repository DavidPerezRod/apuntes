# Blog

El blogging es una de las funcionalidades integradas en Jekyll, lo cual se traduce en que puedes publicar artículos mediante la gestión de un directorio de archivos de texto. Este directorio, es el directorio _post. Los ficheros de post contenidos en esta carpeta, habitualmente estarán escritos en HTML o markdown.

Todos los ficheros deberán tener su Front Matter, y posteriormente Jekyll los convertirá de su fuente a páginas HTML como parte del sitio web.

Las ventajas fundamentales de estra estructura

* no tener que prestar atención a cuestiones de seguridad
* el sitio será más rápido porque no hay que ir BBDD, ni traducir paǵinas, ni servir contenido dinámico.

Sin embargo, Jekyll impone una condición a los ficheros de post, su nombre debe seguir el formato:

* **_year-month-dat-title.markup_**

Esto es porque será la forma en la que resolverá su url localhost/categoría/año/mes/dia/nombre-fichero.html.

La forma de hacer los post, es igual a la de las páginas html. Añadiremos un fichero post.html a la carpeta layouts que será la plantilla que utilizaremos por defecto para construir el resto de htmls. Al igual que el default, utilizará las variables page.variable definidas en cada una de las páginas de post, para rellenar el contenido, y un bloque **_{{content}}_** que será sustituido por la página de blog de la navegación.

Pero todavía se puede mejorar esta aproximación utilizando para no repetir el Front Matter en cada una de las páginas de blog. Para evitarlo, habrá que declarar las variables por defecto que vamos a incluir en el Front Matter de cada blog, en el fichero de configuración.

Los fichero de post que no declare alguna de las variables por defecto del fichero de configuración, tomará su valor de éste.
