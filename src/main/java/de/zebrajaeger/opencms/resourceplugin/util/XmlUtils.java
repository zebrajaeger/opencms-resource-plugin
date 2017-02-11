package de.zebrajaeger.opencms.resourceplugin.util;

import org.apache.commons.lang3.StringUtils;
import org.jdom2.Attribute;
import org.jdom2.Element;
import org.jdom2.xpath.XPathExpression;
import org.jdom2.xpath.XPathFactory;
import org.jdom2.xpath.XPathHelper;

import java.util.List;

/**
 * Created by lars on 11.02.2017.
 */
public class XmlUtils {
    private XmlUtils() {
    }

    public static Element getFirstChild(Element e, String childType) {
        XPathFactory xFac = XPathFactory.instance();
        XPathExpression<Object> x = xFac.compile(childType);
        List<Object> children = x.evaluate(e);
        if (children.isEmpty()) {
            String msg = String.format("there is no child with type '%s' of the name-attribute in element '%s", childType, XPathHelper.getAbsolutePath(e));
            throw new IllegalArgumentException(msg);
        }
        return (Element) children.get(0);
    }

    public static Element getFirstChildWithName(Element e, String childType, String nameAttrValue) {
        XPathFactory xFac = XPathFactory.instance();
        XPathExpression<Object> x = xFac.compile(childType + "[@name='" + nameAttrValue + "']");
        List<Object> children = x.evaluate(e);
        if (children.isEmpty()) {
            String msg = String.format("there is no child '%s' with attribute 'name' and value '%s' of the name-attribute in element '%s", childType, nameAttrValue, XPathHelper.getAbsolutePath(e));
            throw new IllegalArgumentException(msg);
        }
        return (Element) children.get(0);
    }

    public static String readStringAttribute(Element e, String attributeName) {
        Attribute att = e.getAttribute(attributeName);
        if (att == null) {
            String msg = String.format("there is no attribute '%s' in element '%s", attributeName, XPathHelper.getAbsolutePath(e));
            throw new IllegalArgumentException(msg);
        }
        return att.getValue();
    }

    public static String readStringAttributeNotBlank(Element e, String attributeName) {
        String result = readStringAttribute(e, attributeName);
        if (StringUtils.isBlank(result)) {
            String msg = String.format("attribute '%s' in element '%s' cannot be blank", attributeName, XPathHelper.getAbsolutePath(e));
            throw new IllegalArgumentException(msg);
        }

        return result;
    }

    public static Long readLongAttribute(Element e, String attributeName) {
        String val = readStringAttributeNotBlank(e, attributeName);
        try {
            return Long.parseLong(val);
        } catch (NumberFormatException ignore) {
            String msg = String.format("attribute '%s' with value '%s' in element '%s' cannot be parsed to Long", attributeName, val, XPathHelper.getAbsolutePath(e));
            throw new IllegalArgumentException(msg);
        }
    }

    public static Double readDoubleAttribute(Element e, String attributeName) {
        String val = readStringAttributeNotBlank(e, attributeName);
        try {
            return Double.parseDouble(val);
        } catch (NumberFormatException ignore) {
            String msg = String.format("attribute '%s' with value '%s' in element '%s' cannot be parsed to Double", attributeName, val, XPathHelper.getAbsolutePath(e));
            throw new IllegalArgumentException(msg);
        }
    }

    public static Boolean readBooleanAttribute(Element e, String attributeName) {
        String val = readStringAttributeNotBlank(e, attributeName);
        try {
            return Boolean.valueOf(val);
        } catch (NumberFormatException ignore) {
            String msg = String.format("attribute '%s' with value '%s' in element '%s' cannot be parsed to Double", attributeName, val, XPathHelper.getAbsolutePath(e));
            throw new IllegalArgumentException(msg);
        }
    }

    public static void checkType(Element e, String type) {
        if (!type.equals(e.getName())) {
            String msg = String.format("Element '%s' is wrong type. must be '%s' but is '%s'", XPathHelper.getAbsolutePath(e), type, e.getName());
            throw new IllegalArgumentException(msg);
        }

    }
}
