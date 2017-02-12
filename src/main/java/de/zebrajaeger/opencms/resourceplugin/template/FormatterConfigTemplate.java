package de.zebrajaeger.opencms.resourceplugin.template;

import de.zebrajaeger.opencms.resourceplugin.util.ResourceUtils;

/**
 * Created by lars on 11.02.2017.
 */
public class FormatterConfigTemplate extends FileTemplate {
    private String name;
    private String resourcename;
    private String formatterpath;

    public FormatterConfigTemplate(String name, String formatterpath) {
        super("templates/formatter-config.xml");
        this.name = name;
        this.resourcename = ResourceUtils.toResourceName(name);
        this.formatterpath = formatterpath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getResourcename() {
        return resourcename;
    }

    public void setResourcename(String resourcename) {
        this.resourcename = resourcename;
    }

    public String getFormatterpath() {
        return formatterpath;
    }

    public void setFormatterpath(String formatterpath) {
        this.formatterpath = formatterpath;
    }
}

