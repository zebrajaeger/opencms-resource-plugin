# Maven OpenCms Resource Plugin

Scaffolding plugin for an easy way to add a new resourceType to your OpenCms project that uses 
[the mediaworx berlin AG openCms Plugin for Maven](http://opencms.mediaworx.com/de/opencms-tools/).   

## Configuration options

### Parameter "manifestDir"   
* Default value: "${project.basedir}/src/main/opencms/manifest"   
* Required: true   
* Type: java.io.File   
* Description: Path to manifest root directory.

### Parameter "manifestStubFile"   
* Default value : "manifest_stub.xml"   
* Required: true    
* Type: java.lang.String   
* Description: File name of manifest stub file.

### Parameter "vfsDir"  
* Default value: "${project.basedir}/src/main/opencms/vfs"   
* Required: true   
* Type: java.io.File   
* Description: Path to vfs root directory.

### Parameter"newResourceName"  
* Default value: no default value   
* Required: true   
* Type: java.lang.String   
* Description: The name of the new resourceType. Multible values are supported as a comma-separated list of resource-type-names. 
In this case the 'resourceId'-variable should be 'auto'.

### Parameter"resourceId"  
* Default value: "auto"   
* Required: true   
* Type: java.lang.String   
* Possible values: "auto" or a java.lang.Integer value &gt; 0    
* Description: The id of the new resourceType. "Auto" looks for other resources in the manifest_Stub file 
and takes the highest resourceId +1 for the new resourceId.  

### Parameter"icon"  
* Default value: "plain.png"   
* Required: true   
* Type: java.lang.String   
* Description: The small icon for the new resourceType. 
These icons resides in vfs path "/system/workplace/resources/filetypes/".

### Parameter"bigicon"  
* Default value: "plain_big.png"   
* Required: true   
* Type: java.lang.String   
* Description: The big icon for the new resourceType. 
These icons resides in vfs path "/system/workplace/resources/filetypes/".

### Parameter: "moduleName"  
* Default value: "${project.artifactId}"   
* Required: true   
* Description: The name of the OpenCms Module. With this value generates the plugin the path 
from manifest/vfs directory to the module root. 
* Example: the module path is "/system/modules/de.ikk.classic.cms.template/" 
then the moduleName must be "de.ikk.classic.cms.template"  .

### Parameter "layout"  
* Default value: "resource"   
* Required: true   
* Type: de.zebrajaeger.opencms.resourceplugin.ResourceCreatorConfig    
* Possible values: "DISTRIBUTED", "RESOURCE" (case insensitive)    
* Description: The value "RESOURCE" means that we create all new files (schema, bundle, formatter, formatter config) 
in the same directory which name is the resource name. The value "DISTRIBUTED" let us create all the new 
files in different directories (schema in "schemas", bundle in "i18n", formatter and formatter config in "formatters"")  

### Parameter "resourceTypeSubDirectory"  
* Default value: "ce"   
* Required: true   
* Description: If variable "layout" is set to "RESOURCE" and this value is set, the new resourcetype 
folder will not be created in module root. Instead, in module root will be a new folder created (if not exists) 
with the name of this variable. The folder of the new resourceType is created within the new subfolder. 
* Example: We create a new resourceType "foo" and "resourceTypeSubDirectory" is "ce". 
The "moduleName" is "my.opencms.module". All the new files a created in vfs/manifest are in the folder 
"/system/modules/my.opencms.module/ce/foo/". To prevent create a additional subfolder, set this value to "" (empty string).

### Parameter "schemaTypeNamingStrategyClass"  
* Default value: "de.zebrajaeger.opencms.resourceplugin.namingstrategy.CamelCaseNamingStrategy"   
* Required: true
* Possible values are
** 'de.zebrajaeger.opencms.resourceplugin.namingstrategy.CamelCaseNamingStrategy'
** 'de.zebrajaeger.opencms.resourceplugin.namingstrategy.LowerCaseWithHyphenNamingStrategy'
** 'de.zebrajaeger.opencms.resourceplugin.namingstrategy.PassThroughNamingStrategy'
** or any other self implemented naming-strategy that derives from 'de
                                                                    .zebrajaeger.opencms.resourceplugin
                                                                    .namingstrategy.NamingStrategy'   
* Description: The strategy how to transform the 'newRecourceName'-Parameter to the type-name that is 
used in recourceType-schema.  

### Parameter "resourceTypeNamingStrategyClass"  
* Default value: "de.zebrajaeger.opencms.resourceplugin.namingstrategy.LowerCaseWithHyphenNamingStrategy"   
* Required: true
* Possible values are
** 'de.zebrajaeger.opencms.resourceplugin.namingstrategy.CamelCaseNamingStrategy'
** 'de.zebrajaeger.opencms.resourceplugin.namingstrategy.LowerCaseWithHyphenNamingStrategy'
** 'de.zebrajaeger.opencms.resourceplugin.namingstrategy.PassThroughNamingStrategy'
** or any other self implemented naming-strategy that derives from 'de
                                                                    .zebrajaeger.opencms.resourceplugin
                                                                    .namingstrategy.NamingStrategy'   
* Description: The strategy how to transform the 'newRecourceName'-Parameter to the type-name that is 
used for filenames and for OpenCms recourceType-name.  

### Parameter "workplaceBundlePath"  
* Default value: "${project.artifactId}.workplace"   
* Required: true
* Description: Path to workplace VFSResourceBundle.  

### Parameter "workplacePropertiesPath"  
* Default value: "src/main/resources/workplace.properties"   
* Required: true
* Description: Path to workplace properties file. Alternative to the VFSResourceBundle.

### Parameter "addResourceTypeToModuleConfig"  
* Default value: "true"
* Possible values are:
** 'true'
** 'false'
* Required: true
* Description: Add the new resourceType to the module .config file. 

## TODO
* make module .config path modifiable 
* The module config may have more then one language
** see de.zebrajaeger.opencms.resourceplugin.ModuleConfigManipulator.add
* The resourcebundle may have more than one language
** see de.zebrajaeger.opencms.resourceplugin.VfsBundleManipulator.add

## Use from commandline
```
mvn de.zebrajaeger:opencms-resource-plugin:createResource -DnewResourceName=abc
```
Create multible resourceTypes at once:   
```
mvn de.zebrajaeger:opencms-resource-plugin:createResource -DnewResourceName=abc,def
```

## Add as profile to project
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
## Development

For fast building without building javadoc, sources, signing and all the 
deployment stuff execute
```
mvn clean install -Pdevelop
```

If you don't like unit-test you may prefer the fastest way:
```
mvn clean install -Pdevelop -DskipTests
```

If you fork this and deploy it on maven central via sonatype OSS
you need pgp in your path (on windows i use cygwin) and a key pair.   
The public key must be published on a public key server.   
If you fulfill these requirements, a deployment is easy but the version 
cannnot be a x.x.x-SNAPSHOT version. It has to be a release version number 
(without the -SNAPSHOT postfix).    
Than execute this command line call:
```
mvn clean deploy
```
