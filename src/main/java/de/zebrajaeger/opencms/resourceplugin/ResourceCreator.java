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
import org.jdom2.JDOMException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by lars on 11.02.2017.
 */
@SuppressWarnings("checkstyle:classfanoutcomplexity")
public class ResourceCreator {
    private static final Logger LOG = LoggerFactory.getLogger(ResourceCreator.class);

    public void createResource(ResourceCreatorConfig cfg) throws ResourceCreatorException {
        FileLayout fileLayout = FileLayout.of(cfg);
        String typeName = ResourceUtils.toResourceName(cfg.getResourceTypeName());

        try {
            // before we start with modifications, we check if the resource name already is in use
            ManifestStubManipulator manifestStubManipulator = new ManifestStubManipulator(fileLayout.getManifestStub());
            if (manifestStubManipulator.existsRecourceType(typeName)) {
                throw new ResourceCreatorException("The recourceType '" + typeName + "' already exists");
            }

            LOG.info("create resources");

            // directories within module
            LOG.info("== create directories ==");
            for (FilePair p : fileLayout.getDirectories()) {
                createDirectory(p);
            }

            // formatter
            LOG.info("== create formatter ==");
            createFile(
                    fileLayout.getFormatter(),
                    new FormatterTemplate(cfg.getResourceTypeName()),
                    ResourceType.JSP);

            // formatter config
            LOG.info("== create formatter config ==");
            createFile(
                    fileLayout.getFormatterConfig(),
                    new FormatterConfigTemplate(
                            cfg.getResourceTypeName(),
                            fileLayout.getVfsFormatterPath(),
                            cfg.getTypesMatch(),
                            cfg.getWidthMatch()),
                    ResourceType.FORMATTER_CONFIG);

            // schema
            LOG.info("== create schema ==");
            String bundleName = cfg.getModuleName() + "." + typeName;
            createFile(
                    fileLayout.getSchema(),
                    new SchemaTemplate(cfg.getResourceSchemaName(), bundleName),
                    ResourceType.PLAIN);

            // bundles
            LOG.info("== create resource bundle ==");
            createFile(
                    fileLayout.getResourceBundle(),
                    new BundleTemplate(cfg.getResourceSchemaName()),
                    ResourceType.XMLVFSBUNDLE);

            // manifest Stub
            LOG.info("== modify manifest_stub ==");
            modifyManifestStub(cfg, fileLayout, typeName);

            // module config
            LOG.info("== modify module config== ");
            if (cfg.isAddResourceTypeToModuleConfig()) {
                modifyModuleConfig(fileLayout, typeName);
            } else {
                LOG.info("  module config modification not required");
            }

            // workplace bundle
            LOG.info("== modify workplace bundle ==");
            modifyWorkplaceBundle(cfg, fileLayout, typeName);

            //icons
            if (cfg.getIconSource() != null) {
                createIcon(fileLayout, cfg.getIconImage(), fileLayout.getIcon(), cfg.getFileIconVFSPath() + cfg.getIcon());
                createIcon(fileLayout, cfg.getBigIconImage(), fileLayout.getBigIcon(), cfg.getFileIconVFSPath() + cfg.getBigicon());
            }

        } catch (FileTemplateFactoryException | JDOMException | IOException e) {
            LOG.error("Error", e);
            throw new ResourceCreatorException("could not create resource", e);
        }
    }

    private void modifyWorkplaceBundle(
            ResourceCreatorConfig cfg,
            FileLayout fileLayout,
            String typeName) throws FileTemplateFactoryException,
            IOException, JDOMException {

        // exists vfsbundle?
        boolean useVfsBundle = fileLayout.getWorkplaceBundle().getVfs().exists();
        if (useVfsBundle) {
            LOG.info("  Found VFSBundle -> so we use it ('{}')", fileLayout.getWorkplaceBundle().getVfs().getAbsolutePath());
        } else {
            LOG.info("  VFSBundle not found ('{}')", fileLayout.getWorkplaceBundle().getVfs().getAbsolutePath());
        }

        // no. exists workplace.properties? no -> create and use vfsbundle

        if (!useVfsBundle) {
            if (fileLayout.getWorkplaceProperties().exists()) {
                LOG.info("  Found workplace.properties -> so we use it");
            } else {
                LOG.info("  workplace.properties does not exist '{}'", fileLayout.getWorkplaceProperties().getAbsolutePath());
                LOG.info("    -> Create and use VFSBundle bundle");
                createFile(fileLayout.getWorkplaceBundle(), new WorkplaceBundleTemplate(typeName), ResourceType.XMLVFSBUNDLE);
                useVfsBundle = true;
            }
        }

        if (useVfsBundle) {
            LOG.info("  Add new resourceType to workplace bundle");
            LOG.info("    Add fileicon: '{}'", typeName);
            LOG.info("    Add desc: '{}'", typeName);
            LOG.info("    Add title: '{}'", typeName);

            VfsBundleManipulator vfsBundleManipulator = new VfsBundleManipulator(fileLayout.getWorkplaceBundle().getVfs());

            vfsBundleManipulator.add("fileicon." + typeName, cfg.getResourceSchemaName());
            vfsBundleManipulator.add("desc." + typeName, typeName);
            vfsBundleManipulator.add("title." + typeName, typeName);

            vfsBundleManipulator.writeToFile(fileLayout.getWorkplaceBundle().getVfs());
        } else {
            LOG.info("  Add new resourceType to workplace-properties");
            LOG.info("    Add fileicon: '{}'", typeName);
            LOG.info("    Add desc: '{}'", typeName);
            LOG.info("    Add title: '{}'", typeName);
            PropertiesFileManipulator pfm = new PropertiesFileManipulator(fileLayout.getWorkplaceProperties());

            pfm.addEmptyLine();
            pfm.addComment(typeName);
            pfm.add("fileicon." + typeName, cfg.getResourceSchemaName());
            pfm.add("desc." + typeName, typeName);
            pfm.add("title." + typeName, typeName);

            pfm.save();
        }
    }

    private void modifyModuleConfig(FileLayout fileLayout, String typeName) throws FileTemplateFactoryException, IOException, JDOMException {
        if (!fileLayout.getModuleConfig().getVfs().exists()) {
            LOG.debug("   Module config file does not exist -> create");
            createFile(
                    fileLayout.getModuleConfig(),
                    new ModuleConfigTemplate(),
                    ResourceType.MODULE_CONFIG);
        }

        LOG.info("   Add new resourceType to module config file");

        ModuleConfigManipulator moduleConfigManipulator = new ModuleConfigManipulator(fileLayout.getModuleConfig().getVfs());
        moduleConfigManipulator.add(ModuleConfigResourceType.of(typeName));
        moduleConfigManipulator.writeToFile(fileLayout.getModuleConfig().getVfs());
    }

    private void modifyManifestStub(ResourceCreatorConfig cfg, FileLayout fileLayout, String typeName) throws JDOMException, IOException,
            ResourceCreatorException {
        ManifestStubManipulator manifestStubManipulator = new ManifestStubManipulator(fileLayout.getManifestStub());

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
                String msg = String.format("  ResourceId '%s'already exist", resourceIdValue);
                throw new ResourceCreatorException(msg);
            }
            LOG.info("  Choose minimum resourceID for new resource: '{}'", resourceIdValue);
        }

        manifestStubManipulator.addResourceType(typeName, fileLayout.getVfsSchemaPath(), cfg.getIcon(), cfg.getBigicon(), resourceIdValue);
        manifestStubManipulator.writeToFile(fileLayout.getManifestStub());
    }

    private void createIcon(FileLayout fileLayout,
                            BufferedImage iconImage,
                            FilePair files,
                            String vfsPath) throws IOException, FileTemplateFactoryException, JDOMException {
        ResourceType resourceType = ResourceType.IMAGE;

        LOG.info("  Create files for '{}'", resourceType.getName());
        FileTemplateFactory factory = new FileTemplateFactory();

        files.getVfs().getParentFile().mkdirs();
        ImageIO.write(iconImage, "png", files.getVfs());

        files.getManifest().getParentFile().mkdirs();
        factory.writeToFile(new OcmsFileTemplate(resourceType.getName()), files.getManifest());

        ManifestStubManipulator manifestStubManipulator = new ManifestStubManipulator(fileLayout.getManifestStub());
        manifestStubManipulator.addResource(vfsPath);
        manifestStubManipulator.writeToFile(fileLayout.getManifestStub());
    }

    private void createFile(FilePair files, FileTemplate template, ResourceType resourceType) throws FileTemplateFactoryException,
            IOException {
        LOG.info("  Create files for '{}'", resourceType.getName());
        FileTemplateFactory factory = new FileTemplateFactory();

        files.getVfs().getParentFile().mkdirs();
        factory.writeToFile(template, files.getVfs());

        files.getManifest().getParentFile().mkdirs();
        factory.writeToFile(new OcmsFileTemplate(resourceType.getName()), files.getManifest());
    }

    private void createDirectory(FilePair files) throws FileTemplateFactoryException, IOException {
        String dirName = files.getVfs().getName();
        LOG.info("Create directory: '{}'", dirName);
        FileTemplateFactory factory = new FileTemplateFactory();

        // create vfs directory
        if (!files.getVfs().exists()) {
            LOG.info("  Create VFS Directory '{}'", files.getVfs().getAbsolutePath());
            files.getVfs().mkdir();
        }

        // create manifest directory
        if (!files.getManifest().exists()) {
            LOG.info("  Create MANIFEST Directory '{}'", files.getManifest().getAbsolutePath());
            files.getManifest().mkdir();
        }

        // create manifest file for folder
        File manifestFile = new File(files.getManifest().getParent(), files.getManifest().getName() + ".ocmsfolder.xml");
        if (!manifestFile.exists()) {
            LOG.info("  Create OCMSFOLDER file '{}'", manifestFile.getAbsolutePath());
            factory.writeToFile(new OcmsFolderTemplate(), manifestFile);
        }
    }
}
