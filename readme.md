# Maven OpenCms Reource Plugin

Scaffolding plugin for an easy way to add a new resourceType to your OpenCms project that uses 
[the mediaworx berlin AG openCms Plugin for Maven](http://opencms.mediaworx.com/de/opencms-tools/).   

## Configuration options

### Variable "manifestDir"   
* Default value: "${project.basedir}/src/main/opencms/manifest"   
* Required: true   
* Type: java.io.File   
* Description: Path to manifest root directory.

### Variable "manifestStubFile"   
* Default value : "manifest_stub.xml"   
* Required: true    
* Type: java.lang.String   
* Description: File name of manifest stub file.

### Variable "vfsDir"  
* Default value: "${project.basedir}/src/main/opencms/vfs"   
* Required: true   
* Type: java.io.File   
* Description: Path to vfs root directory.

### Variable"newResourceName"  
* Default value: no default value   
* Required: true   
* Type: java.lang.String   
* Description: The name of the new resource type. 

### Variable"resourceId"  
* Default value: "auto"   
* Required: true   
* Type: java.lang.String   
* Possible values: "auto" or a java.lang.Integer value &gt; 0    
* Description: The id of the new resourceType. "Auto" looks for other resources in the manifest_Stub file 
and takes the highest resourceId +1 for the new resourceId.  

### Variable"icon"  
* Default value: "default.png"   
* Required: true   
* Type: java.lang.String   
* Description: The small icon for the new resourceType. 
These icons resides in vfs path "/system/workplace/resources/filetypes/".

### Variable"bigicon"  
* Default value: "default-big.png"   
* Required: true   
* Type: java.lang.String   
* Description: The big icon for the new resourceType. 
These icons resides in vfs path "/system/workplace/resources/filetypes/".

### Variable: "moduleName"  
* Default value: "${project.artifactId}"   
* Required: true   
* Description: The name of the OpenCms Module. With this value generates the plugin the path 
from manifest/vfs directory to the module root. 
* Example: the module path is "/system/modules/de.ikk.classic.cms.template/" 
then the moduleName must be "de.ikk.classic.cms.template"  .

### Variable "layout"  
* Default value: "resource"   
* Required: true   
* Type: de.zebrajaeger.opencms.resourceplugin.ResourceCreatorConfig    
* Possible values: "DISTRIBUTED", "RESOURCE" (case insensitive)    
* Description: The value "RESOURCE" means that we create all new files (schema, bundle, formatter, formatter config) 
in the same directory which name is the resource name. The value "DISTRIBUTED" let us create all the new 
files in different directories (schema in "schemas", bundle in "i18n", formatter and formatter config in "formatters"")  

## TODO
* add "resourceTypeSubDirectory"
* let de.zebrajaeger.opencms.resourceplugin.ResourceCreator.createDirectory 
create every non-exisitng directory (not only the last one in path)  
* throw exception if id aleready exists
* throw exception if resourceType name aleready exists
* use the plugin without modifiing the pom.xml

## prepare project
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