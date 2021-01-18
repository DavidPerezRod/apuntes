# ASSUMPTIONS

Una assumptions (_supuesto_) es diferente de una assertion (_afirmacion_). Una assertion fallará cuando falle la comprobación, mientras que uns assumption comprueba que se produzcan unas condiciones determinadas para la ejecución del test. Si no se dan dichas condiciones, no se ejecuta el test.
Su uso está indicado para comprobar situaciones en las cuales no tiene sentido continuar con la ejecución del test.
Se recomienda no heredar la clase, y utilizar los métodos de la clase mediante imports estáticos.

Si se ejecutan los dos test del ejemplo, el primero se deshabilita, mientras que el segunto se ejecuta.
<pre>
    <code>
    @Test
    void testAssumtionIsFalse() {
        assumeTrue("GURU".equalsIgnoreCase(System.getenv("GURU_RUNTIME")));
    }

    @Test
    void testAssumtionIsTrue() {
        assumeTrue("GURU".equalsIgnoreCase("GURU"));
    }
    </code>
</pre>

Este tipo de test es útil para controlar los test en enetornos de integración en los que alguno de los elementos externos puede fallar.

También existen una serie de anotaciones que permiten comprobar determinadas condiciones predeterminadas como el sistema operativo, usuario o la versión de java, como son:

<pre>
    <code>
        @EnabledIfEnvironmentVariable(named = "USER", matches = "fred")

        @EnabledOnJre(JRE.JAVA_11)

        @EnabledOnOs(OS.WINDOWS)
    </code>
</pre>