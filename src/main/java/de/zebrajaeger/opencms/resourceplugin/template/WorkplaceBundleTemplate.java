package de.zebrajaeger.opencms.resourceplugin.template;

/**
 * Created by lars on 11.02.2017.
 */
public class WorkplaceBundleTemplate extends FileTemplate {
    private String name;

    public WorkplaceBundleTemplate(String name) {
        super("templates/workplacebundle.xml");
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
