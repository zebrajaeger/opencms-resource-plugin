package de.zebrajaeger.opencms.resourceplugin.data;

import de.zebrajaeger.opencms.resourceplugin.util.XmlUtils;
import org.jdom2.CDATA;
import org.jdom2.Element;

/**
 * Created by lars on 11.02.2017.
 */
public class ModuleConfigResourceType extends BasisType {
    private String typeName;
    private Boolean detailPagesDisabled;

    public ModuleConfigResourceType typeName(String value) {
        this.typeName = value;
        return this;
    }

    public ModuleConfigResourceType detailPagesDisabled(Boolean value) {
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

    public static ModuleConfigResourceType of(String resourcename) {
        return new ModuleConfigResourceType()
                .typeName(resourcename)
                .detailPagesDisabled(true);
    }

    public static ModuleConfigResourceType of(Element e) {
        XmlUtils.checkType(e, "ModuleConfigResourceType");
        return new ModuleConfigResourceType()
                .typeName(XmlUtils.getFirstChild(e, "TypeName").getText())
                .detailPagesDisabled(Boolean.parseBoolean(XmlUtils.getFirstChild(e, "DetailPagesDisabled").getText()));
    }
}
