package de.zebrajaeger.opencms.resourceplugin.data;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.jdom2.Element;

/**
 * Created by lars on 11.02.2017.
 */
public abstract class BasisType {

    public abstract Element toXml();

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
