package de.zebrajaeger.opencms.resourceplugin;

/**
 * Created by lars on 12.02.2017.
 */
public enum ResourceType {
    JSP("jsp"),
    PLAIN("plain"),
    XMLVFSBUNDLE("xmlvfsbundle"),
    FORMATTER_CONFIG("formatter_config"),
    MODULE_CONFIG("module_config"),
    FOLDER("folder");

    ResourceType(String name) {
        this.name = name;
    }

    private String name;

    public String getName() {
        return name;
    }
}
