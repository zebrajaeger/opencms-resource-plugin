package de.zebrajaeger.opencms.resourceplugin.manipulator.xml;

import org.jdom2.CDATA;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by lars on 12.02.2017.
 */
public class VfsBundleManipulator extends XmlManipulator {
    public VfsBundleManipulator(Document doc) {
        super(doc);
    }

    public VfsBundleManipulator(File xmlFile) throws JDOMException, IOException {
        super(xmlFile);
    }

    public VfsBundleManipulator(InputStream xmlInput) throws JDOMException, IOException {
        super(xmlInput);
    }

    public Element add(String key, String value) {
        Element bundleElement = findSingleElement("/XmlVfsBundles/Bundle");
        Element messageElement = new Element("Message");
        bundleElement.addContent(messageElement);

        Element keyElement = new Element("Key");
        messageElement.addContent(keyElement);
        keyElement.addContent(new CDATA(key));

        Element valueElement = new Element("Value");
        messageElement.addContent(valueElement);
        valueElement.addContent(new CDATA(value));

        return messageElement;
    }
}
