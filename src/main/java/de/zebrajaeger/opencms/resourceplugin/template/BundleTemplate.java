package de.zebrajaeger.opencms.resourceplugin.template;

/**
 * Created by lars on 11.02.2017.
 */
public class BundleTemplate extends FileTemplate {
    private String name;

    public BundleTemplate(String name) {
        super("templates/bundle.xml");
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
