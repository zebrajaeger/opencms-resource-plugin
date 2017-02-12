package de.zebrajaeger.opencms.resourceplugin;

import de.zebrajaeger.opencms.resourceplugin.data.ModuleConfigResourceType;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by lars on 11.02.2017.
 */
public class ModuleConfigManipulator extends XmlManipulator {
    public ModuleConfigManipulator(Document doc) {
        super(doc);
    }

    public ModuleConfigManipulator(File xmlFile) throws JDOMException, IOException {
        super(xmlFile);
    }

    public ModuleConfigManipulator(InputStream xmlInput) throws JDOMException, IOException {
        super(xmlInput);
    }

    public Element add(ModuleConfigResourceType resourceType) {
        Element result = resourceType.toXml();
        // TODO this could be language dependent
        Element e = findSingleElement("/ModuleConfigurations/ModuleConfiguration");
        e.addContent(0, result);
        return result;
    }
}
