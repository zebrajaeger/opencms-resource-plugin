package de.zebrajaeger.opencms.resourceplugin.data;

import de.zebrajaeger.opencms.resourceplugin.util.XmlUtils;
import org.jdom2.Element;

/**
 * Created by lars on 11.02.2017.
 */
public class ResourceTypeResourceType extends BasisType {

    private String clazz;
    private String name;
    private Long id;
    private String xsd;

    private ResourceTypeResourceType() {
    }

    public String getClazz() {
        return clazz;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getXsd() {
        return xsd;
    }

    public void setXsd(String xsd) {
        this.xsd = xsd;
    }

    public void setClazz(String clazz) {
        this.clazz = clazz;
    }

    public ResourceTypeResourceType clazz(String value) {
        clazz = value;
        return this;
    }

    public ResourceTypeResourceType name(String value) {
        name = value;
        return this;
    }

    public ResourceTypeResourceType id(Long value) {
        id = value;
        return this;
    }

    public ResourceTypeResourceType xsd(String value) {
        xsd = value;
        return this;
    }

    @Override
    public Element toXml() {
        Element result = new Element("type");

        if (clazz != null) {
            result.setAttribute("class", clazz);
        }
        if (name != null) {
            result.setAttribute("name", name);
        }
        if (id != null) {
            result.setAttribute("id", id.toString());
        }

        Element paramSchema = new Element("param");
        result.addContent(paramSchema);

        paramSchema.setAttribute("name", "schema");
        if (xsd != null) {
            paramSchema.setText(xsd);
        }

        return result;
    }

    public static ResourceTypeResourceType of(String name, long id, String xsd) {
        return new ResourceTypeResourceType()
                .clazz("org.opencms.file.types.CmsResourceTypeXmlContent")
                .name(name)
                .id(id)
                .xsd(xsd);
    }

    public static ResourceTypeResourceType of(Element e) {
        XmlUtils.checkType(e, "type");
        Element param = XmlUtils.getFirstChildWithName(e, "param", "schema");
        return new ResourceTypeResourceType()
                .clazz(XmlUtils.readStringAttribute(e, "class"))
                .name(XmlUtils.readStringAttribute(e, "name"))
                .id(XmlUtils.readLongAttribute(e, "id"))
                .xsd(param.getText());
    }
}
