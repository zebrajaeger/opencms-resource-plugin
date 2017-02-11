package de.zebrajaeger.opencms.resourceplugin.data.moduleconfig;

import de.zebrajaeger.opencms.resourceplugin.data.BasisType;
import de.zebrajaeger.opencms.resourceplugin.util.XmlUtils;
import org.jdom2.CDATA;
import org.jdom2.Element;

/**
 * Created by lars on 11.02.2017.
 */
public class ResourceType extends BasisType {
    private String typeName;
    private Boolean detailPagesDisabled;


    public ResourceType typeName(String value) {
        this.typeName = value;
        return this;
    }

    public ResourceType detailPagesDisabled(Boolean value) {
        this.detailPagesDisabled = value;
        return this;
    }

    @Override
    public Element toXml() {
        Element result = new Element("ResourceType");

        if (typeName != null) {
            Element e = new Element("TypeName");
            e.addContent(new CDATA(this.typeName));
            result.addContent(e);
        }
        if (detailPagesDisabled != null) {
            Element e = new Element("DetailPagesDisabled");
            e.addContent(detailPagesDisabled.toString());
            result.addContent(e);
        }

        return result;
    }

    public static ResourceType of(String resourcename) {
        return new ResourceType()
                .typeName(resourcename)
                .detailPagesDisabled(true);
    }

    public static ResourceType of(Element e) {
        XmlUtils.checkType(e, "ResourceType");
        return new ResourceType()
                .typeName(XmlUtils.getFirstChild(e, "TypeName").getText())
                .detailPagesDisabled(Boolean.parseBoolean(XmlUtils.getFirstChild(e, "DetailPagesDisabled").getText()));
    }

}
