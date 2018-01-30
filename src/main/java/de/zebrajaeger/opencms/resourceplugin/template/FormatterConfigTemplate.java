package de.zebrajaeger.opencms.resourceplugin.template;

import de.zebrajaeger.opencms.resourceplugin.util.ResourceUtils;

import java.util.List;

/**
 * Created by lars on 11.02.2017.
 */
public class FormatterConfigTemplate extends FileTemplate {
    private String name;
    private String resourcename;
    private String formatterpath;
    private String[] matchTypes;
    private Integer matchWidth;

    public FormatterConfigTemplate(String name, String formatterpath, List<String> matchTypes, Integer matchWidth) {
        super("templates/formatter-config.xml");
        this.name = name;
        this.resourcename = ResourceUtils.toResourceName(name);
        this.formatterpath = formatterpath;
        this.matchTypes = (matchTypes == null) ? null : matchTypes.toArray(new String[matchTypes.size()]);
        this.matchWidth = matchWidth;
    }

    public String getName() {
        return name;
    }

    public String getResourcename() {
        return resourcename;
    }

    public String getFormatterpath() {
        return formatterpath;
    }

    public String[] getMatchTypes() {
        return matchTypes;
    }

    public Integer getMatchWidth() {
        return matchWidth;
    }
}

