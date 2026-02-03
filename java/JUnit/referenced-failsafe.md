# SURFIRE

Este plugin se utiliza para ejecutar test de integración, aunque requiere una configuración adicional, que es el tag <executions>

<pre>
    <code>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-failsafe-plugin</artifactId>
                <version>2.22.0</version>
                <configuration>
                    <argLine>
                        --illegal-access=permit
                    </argLine>
                </configuration>
                <executions>
                    <execution>
                        <goals>
                            <goal>integration-test</goal>
                            <goal>verify</goal>
                        </goals>
                    </execution>
                </executions>
            </plugin>
    </code>
</pre>

