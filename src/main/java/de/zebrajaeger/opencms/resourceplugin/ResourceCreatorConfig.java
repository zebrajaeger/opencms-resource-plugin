package de.zebrajaeger.opencms.resourceplugin;

import java.io.File;

/**
 * Created by lars on 12.02.2017.
 */
public interface ResourceCreatorConfig {
    enum Layout{
        DISTRIBUTED, RESOURCE
    }

    File getManifestDir();

    String getManifestStubFile();

    File getVfsDir();

    String getNewResourceName();

    String getResourceId();

    String getIcon();

    String getBigicon();

    String getModuleName();

    Layout getLayout();

    String getResourceTypeSubDirectory();
}
