
prepare project
add to pom.xml
```xml
<project>
<!-- ... -->
<profiles>
    <profile>
        <id>createResource</id>
        <build>
            <plugins>
                <plugin>
                    <groupId>de.zebrajaeger</groupId>
                    <artifactId>opencms-resource-plugin</artifactId>
                    <version>1.0-SNAPSHOT</version>
                    <executions>
                        <execution>
                            <goals>
                                <goal>createResource</goal>
                            </goals>
                        </execution>
                    </executions>
                </plugin>

            </plugins>
        </build>
    </profile>

</profiles>
<!-- ... -->
</project>
```
usage
```
 mvn generate-sources -PcreateResource -DnewResourceName=FooBar
```