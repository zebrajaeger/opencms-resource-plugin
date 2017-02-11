package de.zebrajaeger.opencms.resourceplugin.template;

import java.util.UUID;

/**
 * Created by lars on 11.02.2017.
 */
public class OcmsFolderTemplate extends FileTemplate {

    private UUID uuid = UUID.randomUUID();

    public OcmsFolderTemplate() {
        super("templates/ocmsfolder.xml");
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }
}
