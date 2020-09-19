# FUNCIOANMIENTO

Dentro de la carpeta _layouts se puede crear el default.html que será la plantilla para la creación de cualquier página del site. Después en cada una de las páginas html del sitio, habrá que hacer referencia dicho contenido. La forma de conseguirlo es mediante el yaml frontend que se añadirá a cada fichero html

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