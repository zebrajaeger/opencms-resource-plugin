package de.zebrajaeger.opencms.resourceplugin;

import de.zebrajaeger.opencms.resourceplugin.data.ModuleConfigResourceType;
import de.zebrajaeger.opencms.resourceplugin.template.BundleTemplate;
import de.zebrajaeger.opencms.resourceplugin.template.FileTemplate;
import de.zebrajaeger.opencms.resourceplugin.template.FileTemplateFactory;
import de.zebrajaeger.opencms.resourceplugin.template.FileTemplateFactoryException;
import de.zebrajaeger.opencms.resourceplugin.template.FormatterConfigTemplate;
import de.zebrajaeger.opencms.resourceplugin.template.FormatterTemplate;
import de.zebrajaeger.opencms.resourceplugin.template.ModuleConfigTemplate;
import de.zebrajaeger.opencms.resourceplugin.template.OcmsFileTemplate;
import de.zebrajaeger.opencms.resourceplugin.template.OcmsFolderTemplate;
import de.zebrajaeger.opencms.resourceplugin.template.SchemaTemplate;
import de.zebrajaeger.opencms.resourceplugin.template.WorkplaceBundleTemplate;
import de.zebrajaeger.opencms.resourceplugin.util.ResourceUtils;
import org.apache.commons.io.FileUtils;
import org.jdom2.JDOMException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * Created by lars on 11.02.2017.
 */
@SuppressWarnings("checkstyle:classfanoutcomplexity")
public class ResourceCreator {
    private static final Logger LOG = LoggerFactory.getLogger(ResourceCreator.class);

    public void createResource(ResourceCreatorConfig cfg) throws ResourceCreatorException {
        FileLayout files = FileLayout.of(cfg);
        String typeName = ResourceUtils.toResourceName(cfg.getNewResourceName());

        LOG.info("create resources");

        try {
            // directories within module
            LOG.info("create directories");
            for (FilePair p : files.getDirectories()) {
                createDirectory(typeName, p);
            }

            // formatter
            LOG.info("create formatter");
            createFile(
                    files.getFormatter(),
                    new FormatterTemplate(cfg.getNewResourceName()),
                    ResourceType.JSP);

            // formatter config
            LOG.info("create formatter config");
            createFile(
                    files.getFormatterConfig(),
                    new FormatterConfigTemplate(cfg.getNewResourceName(), files.getVfsFormatterPath()),
                    ResourceType.FORMATTER_CONFIG);

            // schema
            LOG.info("create schema");
            String bundleName = cfg.getModuleName() + "." + typeName;
            createFile(
                    files.getSchema(),
                    new SchemaTemplate(cfg.getNewResourceName(), bundleName),
                    ResourceType.PLAIN);

            // bundles
            LOG.info("create resource bundle");
            createFile(
                    files.getResourceBundle(),
                    new BundleTemplate(cfg.getNewResourceName()),
                    ResourceType.XMLVFSBUNDLE);

            // manifest Stub
            LOG.info("modify manifest_stub");
            modifyManifestStub(cfg, files, typeName);

            // module config
            LOG.info("modify module config");
            modifyModuleConfig(files, typeName);

            // workplace bundle
            LOG.info("modify workplace bundle");
            modifyWorkplaceBundle(cfg, files, typeName);

        } catch (FileTemplateFactoryException | JDOMException | IOException e) {
            LOG.error("Error", e);
            throw new ResourceCreatorException("could not create resource", e);
        }
    }

    private void modifyWorkplaceBundle(ResourceCreatorConfig cfg, FileLayout files, String typeName) throws FileTemplateFactoryException,
            IOException, JDOMException {
        if (!files.getWorkplaceBundle().getVfs().exists()) {
            LOG.debug("Workplace bundle does not exist -> create");
            createFile(files.getWorkplaceBundle(), new WorkplaceBundleTemplate(cfg.getNewResourceName()), ResourceType.XMLVFSBUNDLE);
        }

        LOG.info("Add new resourceType to workplace bundle");
        VfsBundleManipulator vfsBundleManipulator = new VfsBundleManipulator(files.getWorkplaceBundle().getVfs());
        LOG.info("   Add fileicon: '{}'", cfg.getNewResourceName());
        vfsBundleManipulator.add("fileicon." + typeName, cfg.getNewResourceName());
        LOG.info("   Add desc: '{}'", typeName);
        vfsBundleManipulator.add("desc." + typeName, typeName);
        LOG.info("   Add title: '{}'", cfg.getNewResourceName());
        vfsBundleManipulator.add("title." + typeName, cfg.getNewResourceName());
        FileUtils.write(files.getWorkplaceBundle().getVfs(), vfsBundleManipulator.toString(), StandardCharsets.UTF_8);
    }

    private void modifyModuleConfig(FileLayout files, String typeName) throws FileTemplateFactoryException, IOException, JDOMException {
        if (!files.getModuleConfig().getVfs().exists()) {
            LOG.debug("Module config file does not exist -> create");
            createFile(
                    files.getModuleConfig(),
                    new ModuleConfigTemplate(),
                    ResourceType.MODULE_CONFIG);
        }

        LOG.info("Add new resourceType to module config file");

        ModuleConfigManipulator moduleConfigManipulator = new ModuleConfigManipulator(files.getModuleConfig().getVfs());
        moduleConfigManipulator.add(ModuleConfigResourceType.of(typeName));
        FileUtils.write(files.getModuleConfig().getVfs(), moduleConfigManipulator.toString(), StandardCharsets.UTF_8);
    }

    private void modifyManifestStub(ResourceCreatorConfig cfg, FileLayout files, String typeName) throws JDOMException, IOException,
            ResourceCreatorException {
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
            LOG.info("Choose minimum resourceID for new resource: '{}'", resourceIdValue);
        }

        manifestStubManipulator.addResource(typeName, files.getVfsSchemaPath(), resourceIdValue);
        FileUtils.write(files.getManifestStub(), manifestStubManipulator.toString(), StandardCharsets.UTF_8);
    }

    private void createFile(FilePair files, FileTemplate template, ResourceType resourceType) throws FileTemplateFactoryException,
            IOException {
        LOG.info("Create files for '{}'", resourceType.getName());
        FileTemplateFactory factory = new FileTemplateFactory();

        files.getVfs().getParentFile().mkdirs();
        factory.writeToFileFile(template, files.getVfs());

        files.getManifest().getParentFile().mkdirs();
        factory.writeToFileFile(new OcmsFileTemplate(resourceType.getName()), files.getManifest());
    }

    private void createDirectory(String resourceTypeName, FilePair files) throws FileTemplateFactoryException, IOException {
        FileTemplateFactory factory = new FileTemplateFactory();

        // create vfs directory
        if (!files.getVfs().exists()) {
            LOG.info("Create VFS Directory '{}'", files.getVfs().getAbsolutePath());
            files.getVfs().mkdir();
        }

        // create manifest directory
        if (!files.getManifest().exists()) {
            LOG.info("Create MANIFEST Directory '{}'", files.getManifest().getAbsolutePath());
            files.getManifest().mkdir();
        }

        // create manifest file for folder
        File manifestFile = new File(files.getManifest().getParent(), resourceTypeName + ".ocmsfolder.xml");
        if (!manifestFile.exists()) {
            LOG.info("Create OCMSFOLDER file '{}'", manifestFile.getAbsolutePath());
            factory.writeToFileFile(new OcmsFolderTemplate(), manifestFile);
        }
    }
}
