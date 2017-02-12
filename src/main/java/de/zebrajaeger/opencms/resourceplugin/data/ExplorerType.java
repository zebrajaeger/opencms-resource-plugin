package de.zebrajaeger.opencms.resourceplugin.data;

import de.zebrajaeger.opencms.resourceplugin.util.XmlUtils;
import org.jdom2.Element;

/**
 * Created by lars on 11.02.2017.
 */
public class ExplorerType extends BasisType {
    private String name;
    private String key;
    private String icon;
    private String bigicon;
    private String reference;
    private ExplorerTypeNewResource newResource;

    private ExplorerType() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getBigicon() {
        return bigicon;
    }

    public void setBigicon(String bigicon) {
        this.bigicon = bigicon;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public ExplorerTypeNewResource getNewResource() {
        return newResource;
    }

    public void setNewResource(ExplorerTypeNewResource newResource) {
        this.newResource = newResource;
    }

    public ExplorerType name(String value) {
        this.name = value;
        return this;
    }

    public ExplorerType key(String value) {
        this.key = value;
        return this;
    }

    public ExplorerType icon(String value) {
        this.icon = value;
        return this;
    }

    public ExplorerType bigicon(String value) {
        this.bigicon = value;
        return this;
    }

    public ExplorerType reference(String value) {
        this.reference = value;
        return this;
    }

    public ExplorerType newResource(ExplorerTypeNewResource value) {
        this.newResource = value;
        return this;
    }

    @Override
    public Element toXml() {
        Element result = new Element("explorertype");

        if (name != null) {
            result.setAttribute("name", name);
        }
        if (key != null) {
            result.setAttribute("key", key);
        }
        if (icon != null) {
            result.setAttribute("icon", icon);
        }
        if (bigicon != null) {
            result.setAttribute("bigicon", bigicon);
        }
        if (reference != null) {
            result.setAttribute("reference", reference);
        }
        if (newResource != null) {
            result.addContent(newResource.toXml());
        }

        return result;
    }

    public static ExplorerType of(String name) {
        return new ExplorerType()
                .name(name)
                .key("fileicon." + name)
                .icon("default.png")
                .bigicon("default-big.png")
                .reference("xmlcontent")
                .newResource(ExplorerTypeNewResource.of(name));
    }

    public static ExplorerType of(Element e) {
        XmlUtils.checkType(e, "explorertype");
        return new ExplorerType()
                .name(XmlUtils.readStringAttribute(e, "name"))
                .key(XmlUtils.readStringAttribute(e, "key"))
                .icon(XmlUtils.readStringAttribute(e, "icon"))
                .bigicon(XmlUtils.readStringAttribute(e, "bigicon"))
                .reference(XmlUtils.readStringAttribute(e, "reference"))
                .newResource(ExplorerTypeNewResource.of(XmlUtils.getFirstChild(e, "newresource")));
    }

}
