package de.zebrajaeger.opencms.resourceplugin;

import de.zebrajaeger.opencms.resourceplugin.util.ResourceUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by lars on 12.02.2017.
 */
public class FileLayout {
    public static final String OCMSFILE_XML = ".ocmsfile.xml";
    private List<FilePair> directories = new LinkedList<>();
    private File manifestStub;
    private FilePair moduleConfig;
    private FilePair workplaceBundle;
    private File workplacePropertiesPath;
    private FilePair formatter;
    private String vfsFormatterPath;
    private FilePair formatterConfig;
    private FilePair schema;
    private String vfsSchemaPath;
    private FilePair resourceBundle;
    private FilePair icon;
    private FilePair bigIcon;

    //<editor-fold desc="Getter/Setter">
    public List<FilePair> getDirectories() {
        return directories;
    }

    public File getManifestStub() {
        return manifestStub;
    }

    public FilePair getModuleConfig() {
        return moduleConfig;
    }

    public FilePair getWorkplaceBundle() {
        return workplaceBundle;
    }

    public File getWorkplaceProperties() {
        return workplacePropertiesPath;
    }

    public FilePair getFormatter() {
        return formatter;
    }

    public String getVfsFormatterPath() {
        return vfsFormatterPath;
    }

    public FilePair getFormatterConfig() {
        return formatterConfig;
    }

    public FilePair getSchema() {
        return schema;
    }

    public String getVfsSchemaPath() {
        return vfsSchemaPath;
    }

    public FilePair getResourceBundle() {
        return resourceBundle;
    }

    public FilePair getIcon() {
        return icon;
    }

    public FilePair getBigIcon() {
        return bigIcon;
    }
    //</editor-fold>

    public static FileLayout of(ResourceCreatorConfig cfg) {
        FileLayout result = new FileLayout();
        String typeName = ResourceUtils.toResourceName(cfg.getResourceTypeName());

        FilePair root = new FilePair(cfg.getVfsDir(), cfg.getManifestDir());

        FilePair icons = new FilePair(
                new File(root.getVfs(), cfg.getFileIconVFSPath()),
                new File(root.getManifest(), cfg.getFileIconVFSPath()));

        FilePair moduleRoot = new FilePair(
                new File(root.getVfs(), "system/modules/" + cfg.getModuleName()),
                new File(root.getManifest(), "system/modules/" + cfg.getModuleName()));

        // manifest stub
        result.manifestStub = new File(cfg.getManifestDir(), cfg.getManifestStubFile());

        // module config
        result.moduleConfig = new FilePair(
                new File(moduleRoot.getVfs(), ".config"),
                new File(moduleRoot.getManifest(), ".config" + OCMSFILE_XML));

        // workplace VFS bundle
        result.workplaceBundle = new FilePair(
                new File(moduleRoot.getVfs(), cfg.getWorkplaceBundlePath()),
                new File(moduleRoot.getManifest(), cfg.getWorkplaceBundlePath() + OCMSFILE_XML));

        // workplace properties
        result.workplacePropertiesPath = new File(cfg.getWorkplacePropertiesPath());

        // icon
        result.icon = new FilePair(
                new File(icons.getVfs(), cfg.getIcon()),
                new File(icons.getManifest(), cfg.getIcon() + OCMSFILE_XML));
        result.bigIcon = new FilePair(
                new File(icons.getVfs(), cfg.getBigicon()),
                new File(icons.getManifest(), cfg.getBigicon() + OCMSFILE_XML));

        if (cfg.getLayout() == ResourceCreatorConfig.Layout.RESOURCE) {

            // path to resource folders with additional sub-directory(s)
            File vfsParent = moduleRoot.getVfs();
            File manifestParent = moduleRoot.getManifest();
            if (StringUtils.isNotBlank(cfg.getResourceTypeSubDirectory())) {
                for (Path p : Paths.get(cfg.getResourceTypeSubDirectory())) {
                    vfsParent = new File(vfsParent, p.toString());
                    manifestParent = new File(manifestParent, p.toString());

                    FilePair dir = new FilePair(vfsParent, manifestParent);
                    result.directories.add(dir);
                }
            }

            FilePair resourceRoot = new FilePair(new File(vfsParent, typeName), new File(manifestParent, typeName));

            result.directories.add(resourceRoot);

            String sub = cfg.getResourceTypeSubDirectory();
            if (StringUtils.isBlank(sub)) {
                sub = "";
            } else {
                sub = "/" + sub;
            }

            // resource type
            result.formatter = initFileForResourceLayout(resourceRoot, typeName, "jsp");
            result.vfsFormatterPath = "/system/modules/" + cfg.getModuleName() + sub + "/" + typeName + "/" + typeName + ".jsp";
            result.formatterConfig = initFileForResourceLayout(resourceRoot, typeName, "xml");
            result.schema = initFileForResourceLayout(resourceRoot, typeName, "xsd");
            result.vfsSchemaPath = "/system/modules/" + cfg.getModuleName() + sub + "/" + typeName + "/" + typeName + ".xsd";
            result.resourceBundle = initFileForResourceLayout(resourceRoot, cfg.getModuleName() + "." + typeName, null);

        } else if (cfg.getLayout() == ResourceCreatorConfig.Layout.DISTRIBUTED) {
            // resource type
            result.formatter = initFileForDistributedLayout(result, moduleRoot, "formatters", "jsp", typeName);
            result.formatterConfig = initFileForDistributedLayout(result, moduleRoot, "formatters", "xml", typeName);
            result.schema = initFileForDistributedLayout(result, moduleRoot, "schemas", "xsd", typeName);
            result.vfsSchemaPath = "/system/modules/" + cfg.getModuleName() + "/schemas/" + typeName + ".xsd";
            result.resourceBundle = initFileForDistributedLayout(result, moduleRoot, "i18n", null, typeName);
        }

        return result;
    }

    private static FilePair initFileForDistributedLayout(FileLayout result, FilePair moduleRoot, String subDirName, String typeName,
                                                         String fileExtension) {
        FilePair dir;
        dir = new FilePair(
                new File(moduleRoot.getVfs(), subDirName),
                new File(moduleRoot.getManifest(), subDirName));

        result.directories.add(dir);

        if (StringUtils.isNotBlank(fileExtension)) {
            return new FilePair(
                    new File(dir.getVfs(), typeName + "." + fileExtension),
                    new File(dir.getManifest(), typeName + "." + fileExtension + OCMSFILE_XML));
        } else {
            return new FilePair(
                    new File(dir.getVfs(), typeName),
                    new File(dir.getManifest(), typeName + OCMSFILE_XML));
        }
    }

    private static FilePair initFileForResourceLayout(FilePair resourceRoot, String typeName, String fileExtension) {
        if (StringUtils.isNotBlank(fileExtension)) {
            return new FilePair(
                    new File(resourceRoot.getVfs(), typeName + "." + fileExtension),
                    new File(resourceRoot.getManifest(), typeName + "." + fileExtension + OCMSFILE_XML));
        } else {
            return new FilePair(
                    new File(resourceRoot.getVfs(), typeName),
                    new File(resourceRoot.getManifest(), typeName + OCMSFILE_XML));
        }
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
}
