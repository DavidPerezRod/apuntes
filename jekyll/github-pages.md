# Github Pages

Características:

* No hay programación en servidor
* utiliza solo páginas estáticas, html, css y javascript
* utiliza solo HTTP, no HTTPS, de forma que no se debe utilizar para operaciones sensibles como enviar passwords o números de tarjetas de crédito.
* Tiene un soporte especial para sites creados con Jekyll

Además GitHub Pages, tiene dos tipos de sites, User Pages o Project Pages

|User Pages|Project Pages|
|----------|-------------|
|un site de empresas o personal|para proyectos|
||se guarda en el mismo repositorio que el proyecto|
|http://username.github.io|http://username.github.io/repository-name|
|un site por usuario|un website por proyecto|

Para iniciar un site en github, una vez logado, hay que seguir 4 pasos:

* inicializar un repositorio en local
* añadir el repositorio a github
* crear una branch github pages en el repositorio
* esperar a que github construya el site
* crear una rama gh-pages

## Configurando un dominio propio en GitHub

Se deben seguir los siguiente pasos:

* configurar el site en la cuenta GitHub
* añadir un fichero CNAME con una línea, el nombre del dominio
* ir al gestor de dominios
* Aña