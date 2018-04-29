package de.zebrajaeger.opencms.resourceplugin.file;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

public abstract class VfsResource {
    String name;

    public String getName() {
        return name;
    }

    public abstract boolean isComplete();

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
