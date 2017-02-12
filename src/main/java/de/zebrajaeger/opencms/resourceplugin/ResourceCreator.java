package de.zebrajaeger.opencms.resourceplugin;

import de.zebrajaeger.opencms.resourceplugin.data.ModuleConfigResourceType;
import de.zebrajaeger.opencms.resourceplugin.template.*;
import de.zebrajaeger.opencms.resourceplugin.util.ResourceUtils;
import org.jdom2.JDOMException;

import java.io.File;
import java.io.IOException;

/**
 * Created by lars on 11.02.2017.
 */
public class ResourceCreator {
    public void createResource(ResourceCreatorConfig cfg) throws ResourceCreatorException {
        FileLayout files = FileLayout.of(cfg);
        String typeName = ResourceUtils.toResourceName(cfg.getNewResourceName());

        try {
            // directories within module
            for (FilePair p : files.getDirectories()) {
                createDirectory(typeName, p);
            }

            // formatter
            createFile(files.getFormatter(), new FormatterTemplate(cfg.getNewResourceName()), ResourceType.JSP);

            // formatter config
            createFile(files.getFormatterConfig(), new FormatterConfigTemplate(cfg.getNewResourceName()), ResourceType.FORMATTER_CONFIG);

            // schema
            String bundleName = cfg.getModuleName() + "." + typeName;
            createFile(files.getSchema(), new SchemaTemplate(cfg.getNewResourceName(), bundleName), ResourceType.PLAIN);

            // bundles
            createFile(files.getResourceBundle(), new BundleTemplate(cfg.getNewResourceName()), ResourceType.XMLVFSBUNDLE);

            if (!files.getModuleConfig().getVfs().exists()) {
                createFile(files.getModuleConfig(), new ModuleConfigTemplate(), ResourceType.MODULE_CONFIG);
            }

            // manifest Stub
            ManifestStubManipulator manifestStubManipulator = new ManifestStubManipulator(files.getManifestStub());

            String resourceId = cfg.getResourceId();
            Long resourceIdValue;
            if ("auto".equals(resourceId)) {
                resourceIdValue = manifestStubManipulator.getHighestResourceId();
                if (resourceIdValue == null) {
                    resourceIdValue = 100000L;
                } else {
                    resourceIdValue++;
                }
            } else {
                resourceIdValue = Long.parseLong(resourceId);
                if (manifestStubManipulator.existsResourceId(resourceIdValue)) {
                    String msg = String.format("ResourceId '%s'already exist", resourceIdValue);
                    throw new ResourceCreatorException(msg);
                }
            }

            manifestStubManipulator.addResource(typeName, files.getVfsSchemaPath(), resourceIdValue);

            // module config
            if(!files.getModuleConfig().getVfs().exists()){
                createFile(files.getModuleConfig(), new ModuleConfigTemplate(), ResourceType.MODULE_CONFIG);
            }

            ModuleConfigManipulator moduleConfigManipulator = new ModuleConfigManipulator(files.getModuleConfig().getVfs());
            moduleConfigManipulator.add(ModuleConfigResourceType.of(typeName));

        } catch (FileTemplateFactoryException | JDOMException | IOException e) {
            throw new ResourceCreatorException("could not create resouce", e);
        }
    }

    private void createFile(FilePair files, FileTemplate template, ResourceType resourceType) throws FileTemplateFactoryException, IOException {
        FileTemplateFactory factory = new FileTemplateFactory();

        files.getVfs().getParentFile().mkdirs();
        factory.generate(template, files.getVfs());

        files.getManifest().getParentFile().mkdirs();
        factory.generate(new OcmsFileTemplate(resourceType.getName()),files.getManifest());
    }

    private void createDirectory(String resourceTypeName, FilePair files) throws FileTemplateFactoryException, IOException {
        FileTemplateFactory factory = new FileTemplateFactory();

        files.getVfs().mkdirs();
        files.getManifest().mkdirs();
        factory.generate(
                new OcmsFolderTemplate(),
                new File(files.getManifest().getParent(),resourceTypeName + ".ocmsfolder.xml" ));
    }
}
