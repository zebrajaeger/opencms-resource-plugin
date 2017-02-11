package de.zebrajaeger.opencms.resourceplugin.template;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

/**
 * Created by lars on 11.02.2017.
 */
public class FileTemplate {
    private String template;

    public FileTemplate(String template) {
        this.template = template;
    }

    public String getTemplate() {
        return template;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
