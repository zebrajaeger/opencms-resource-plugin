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
    private List<FilePair> directories = new LinkedList<>();
    private File manifestStub;
    private FilePair moduleConfig;
    private FilePair workplaceBundle;
    private FilePair formatter;
    private String vfsFormatterPath;
    private FilePair formatterConfig;
    private FilePair schema;
    private String vfsSchemaPath;
    private FilePair resourceBundle;

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

    public static FileLayout of(ResourceCreatorConfig cfg) {
        FileLayout result = new FileLayout();
        String typeName = ResourceUtils.toResourceName(cfg.getResourceTypeName());

        FilePair moduleRoot = new FilePair(
                new File(cfg.getVfsDir(), "system/modules/" + cfg.getModuleName()),
                new File(cfg.getManifestDir(), "system/modules/" + cfg.getModuleName()));

        result.manifestStub = new File(cfg.getManifestDir(), cfg.getManifestStubFile());

        result.moduleConfig = new FilePair(
                new File(moduleRoot.getVfs(), ".config"),
                new File(moduleRoot.getManifest(), ".config.ocmsfile.xml"));

        result.workplaceBundle = new FilePair(
                new File(moduleRoot.getVfs(), cfg.getModuleName() + ".workplace"),
                new File(moduleRoot.getManifest(), cfg.getModuleName() + ".workplace.ocmsfile.xml"));

        if (cfg.getLayout() == ResourceCreatorConfig.Layout.RESOURCE) {

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

            result.formatter = initFileForResourceLayout(resourceRoot, typeName, "jsp");
            result.vfsFormatterPath = "/system/modules/" + cfg.getModuleName() + "/" + typeName + "/" + typeName + ".jsp";
            result.formatterConfig = initFileForResourceLayout(resourceRoot, typeName, "xml");
            result.schema = initFileForResourceLayout(resourceRoot, typeName, "xsd");
            result.vfsSchemaPath = "/system/modules/" + cfg.getModuleName() + "/" + typeName + "/" + typeName + ".xsd";
            result.resourceBundle = initFileForResourceLayout(resourceRoot, cfg.getModuleName() + "." + typeName, null);

        } else if (cfg.getLayout() == ResourceCreatorConfig.Layout.DISTRIBUTED) {
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
                    new File(dir.getManifest(), typeName + "." + fileExtension + ".ocmsfile.xml"));
        } else {
            return new FilePair(
                    new File(dir.getVfs(), typeName),
                    new File(dir.getManifest(), typeName + ".ocmsfile.xml"));
        }
    }

    private static FilePair initFileForResourceLayout(FilePair resourceRoot, String typeName, String fileExtension) {
        if (StringUtils.isNotBlank(fileExtension)) {
            return new FilePair(
                    new File(resourceRoot.getVfs(), typeName + "." + fileExtension),
                    new File(resourceRoot.getManifest(), typeName + "." + fileExtension + ".ocmsfile.xml"));
        } else {
            return new FilePair(
                    new File(resourceRoot.getVfs(), typeName),
                    new File(resourceRoot.getManifest(), typeName + ".ocmsfile.xml"));
        }
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
}
