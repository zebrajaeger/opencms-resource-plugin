package de.zebrajaeger.opencms.resourceplugin;

import java.io.File;

/**
 * Created by lars on 12.02.2017.
 */
public interface ResourceCreatorConfig {
    enum Layout {
        DISTRIBUTED, RESOURCE
    }

    File getManifestDir();

    String getManifestStubFile();

    File getVfsDir();

    String getResourceSchemaName();

    String getResourceTypeName();

    String getResourceId();

    String getIcon();

    String getBigicon();

    String getModuleName();

    Layout getLayout();

    String getResourceTypeSubDirectory();

    String getWorkplaceBundlePath();

    String getWorkplacePropertiesPath();

    boolean isAddResourceTypeToModuleConfig();
}
