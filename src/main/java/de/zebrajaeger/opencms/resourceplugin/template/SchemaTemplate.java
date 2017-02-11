package de.zebrajaeger.opencms.resourceplugin.template;

/**
 * Created by lars on 11.02.2017.
 */
public class SchemaTemplate extends FileTemplate {
    private String name;
    private String bundle;

    public SchemaTemplate(String name, String bundle) {
        super("template/schema.xsd");
        this.name = name;
        this.bundle = bundle;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBundle() {
        return bundle;
    }

    public void setBundle(String bundle) {
        this.bundle = bundle;
    }
}
