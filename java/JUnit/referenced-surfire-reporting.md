# SURFIRE REPORTING

Surfire, proporciona informes de los resultados de ejecuciónd de test. Por defecto, estos informes son en formato xml. Sin embargo, y con el fin de mejorar la legibilidad, propociona un plugin adicional que genera la misma salida en formato HTML.

Para configurar esta utilidad en maven, hay que añadir un nuevo plugin a la sección de plugins del bloque build:

    <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-site-plugin</artifactId>
        <version>3.7.1</version>
    </plugin>

Y un nuevo bloque al pom.xml

    <reporting>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-surefire-report-plugin</artifactId>
                <version>2.22.0</version>
            </plugin>
        </plugins>
    </reporting>

Para que maven genere los informes, surfire añade una nueva etapa al ciclo maven, **site**. La salida se genera en target, en la carpta site, y se puede acceder a la navegación de los informes a tavés del fichero **index.html**.

Estos informes pueden ser versionados con el fin de mantener un histórico de la evolución del desarrollo. Además, esta utilidad proporciona información sobre tiempo de ejecución, test deshabilitados, test no ejecutados, etc. 