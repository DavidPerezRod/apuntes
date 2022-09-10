# Reutilización

 Hay fundamentalmente 3 elmentos con los que podemos reutilizar contenidos entre páginas HTML:

1. Fichero **_config.yml_**
2. Front matter
3. Liquid Templates

## _config.yml

El elemento básico de configuración en Jekyll es el fichero _config.yml. De la misma forma que se puede acceder a variable de página declaradas en el bloque yml, de la propia página html mediante la palabra **_page_**, se puede acceder a variables globales declaradas en el fichero _config.yml, mediante **_site._**.

```html
<title> {% if page.title %}{{page.title}}{% else %}{{site.title}}.{% endif %} </title>
<meta name="description" content="{% if page.description %}{{page.description}}{% else %}{{site.description}}.{% endif %}">
```

Para este tipo de cambios, hay que parar y reiniciar el servidor, no sirve con ejecutar **_bundle exec jekyll serve_**, hay que pararlo y volverlo a ejecutar:

1. ctr + C
2. Jekyll serve

## Jekyll front matter

Jekyll utiliza este tipo de notación para la configuración de las páginas. Es lo primero que debe aparecere en la página, y es procesado como contenido especial.

<pre>
    <code>
        ---
            layout: default
            title: Blabla bla,
            robots: noindex
        ---
    </code>
</pre>

## Liquid Templates

Liquid le permite a Jekyll procesar las templates, para crear y personalizar temas. Creado por Shopify, permite comprar muchos temas ecommerce.

Liquid tiene dos tipos de elementos:

*  output markup
   *  Se utiliza para inyectar contenido de salida. Su sintaxis, contenido encerrado entre dobles llaves {{page.title}}
*  tag markup
   *  Se utilza para ejecutar lógica. Su sintaxis es similar {% if page.title %}


## Secuencia de acciones del servidor Jekyll

1. lee el fichero de configuración
2. lee el resto de ficheros para procesar su sección front matter
3. utiliza liquid para procesar contenido y templates
4. lee los assets
5. genera la salida final a la carpeta _sites

## Reutilización

Dentro de la carpeta _layouts se puede crear el default.html que será la plantilla para la creación de cualquier página del site. 

Este fichero debe tener el head con las referncias a los estilos y a los javascript, y el cuerpo con las secciones comunes a todas las página, en principio como mínimo, header y footer. Además entre las dos secciones, añadiremos **_{{content}}_**, y del resto de páginas eliminamos la cabecera y el pie. Jekyll. Ahora bien, cómo sabe jekyll que debe inyectar en el resto de páginas el contenido de la página por defecto, añadiendo en cada una de las páginas el yml front matter.


A continuación hay que añadir una sección condicional a la página por defecto, para que en caso de que la página en la que se inyecta ya tenga título, no lo añada, pero en caso contratio, que añada el título por defecto. La forma de añadir una sección condicional es la siguiente:

```ruby
{% if page.title %}{{page.title}}{% else %} Ratón de bibioteca: silencio por favor.{% endif %}
```

Sin embargo, y aunque este procedimiento funciona, no es lo más eficiente, ya que implica modificar todos los ficheros HTML cada vez que haya un cambio en un contenido común, y obliga a que todas las páginas repitan ciertos contenidos. La mejor forma de evitar estos dos inconvenientes, es trabajar con includes. La forma de declararlos es mediante {% include subpage-header.html %}. La idea, extraer a ficheros las secciones comunes, con identidad propia, o susceptibles de ser modificadas.

Todos estos cambios el servidor los coge en caliente, así que no es necesario volverlo a ejecutar.

Hay tres tipos de variables importantes:

* site. Se utiliza para acceder desde las páginas HTML tanto a las variables declaradas en el fichero de configuración _config.yml, como a ciertas características del sitio:
  * site.pages lista todas las páginas del site
  * site.post lista de todos los posts
  * site.data es una variable que contiene información sobre archivos externos 
* page. Se utilza para acceder desde las páginas html a las variables declaradas en su propia sección front matter.
* content. Se utiliza en la página por defecto, para indicar dónde debemos inyectar el contenido del resto de páginas html

## Declaración de constantes

Jekyll permite definir tus propias variables globales para ser reutilizardas donde se necesiten. Jekull soporta la carga de datos en formatos YAML, JSON y CSV desde ficheros almacenados en el directorio _data.

Después la forma de acceder a los datos, será accediendo mediente liquid al fichero con site.data.nombre_fichero_datos.

