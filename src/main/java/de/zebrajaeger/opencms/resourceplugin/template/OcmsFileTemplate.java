package de.zebrajaeger.opencms.resourceplugin.template;

import java.util.UUID;

/**
 * Created by lars on 11.02.2017.
 */
public class OcmsFileTemplate extends FileTemplate {
    private String type;
    private UUID uuidstructure = UUID.randomUUID();
    private UUID uuidresource = UUID.randomUUID();

    public OcmsFileTemplate(String type) {
        super("templates/opencmsfile.xml");
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public UUID getUuidstructure() {
        return uuidstructure;
    }

    public void setUuidstructure(UUID uuidstructure) {
        this.uuidstructure = uuidstructure;
    }

    public UUID getUuidresource() {
        return uuidresource;
    }

    public void setUuidresource(UUID uuidresource) {
        this.uuidresource = uuidresource;
    }
}
