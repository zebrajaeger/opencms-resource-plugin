package de.zebrajaeger.opencms.resourceplugin;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;

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

    BufferedImage getIconImage();

    BufferedImage getBigIconImage();

    String getIconSource();

    String getFileIconVFSPath();

    String getModuleName();

    Layout getLayout();

    String getResourceTypeSubDirectory();

    String getWorkplaceBundlePath();

    String getWorkplacePropertiesPath();

    boolean isAddResourceTypeToModuleConfig();

    List<String> getTypesMatch();

    Integer getWidthMatch();
}
