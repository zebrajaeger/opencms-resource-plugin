package de.zebrajaeger.opencms.resourceplugin;

import de.zebrajaeger.opencms.resourceplugin.data.explorertype.ExplorerType;
import de.zebrajaeger.opencms.resourceplugin.data.resourcetype.ResourceType;
import org.jdom2.Element;
import org.jdom2.JDOMException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by lars on 11.02.2017.
 */
public class ManifestStubManipulator extends XmlManipulator{

    public ManifestStubManipulator(File xmlFile) throws JDOMException, IOException {
        super(xmlFile);
    }

    public ManifestStubManipulator(InputStream xmlInput) throws JDOMException, IOException {
        super(xmlInput);
    }

    public List<ResourceType> readResourceTypes() {
        List<ResourceType> result = new LinkedList<>();
        for (Element e : findElements("/export/module/resourcetypes/type")) {
            result.add(ResourceType.of(e));
        }
        return result;
    }

    public List<ExplorerType> readExplorerTypes() {
        List<ExplorerType> result = new LinkedList<>();
        for (Element e :findElements("/export/module/explorertypes/explorertype")) {
            result.add(ExplorerType.of(e));
        }
        return result;
    }

    public Element add(ResourceType resourceType){
        Element result = resourceType.toXml();
        Element e = findSingleElement("/export/module/resourcetypes");
        e.addContent(result);
        return result;
    }

    public Element add(ExplorerType explorerType){
        Element result = explorerType.toXml();
        Element e = findSingleElement("/export/module/explorertypes");
        e.addContent(result);
        return result;
    }

    public Long getHighestResourceId() {
        Long result = null;

        for (ResourceType et : readResourceTypes()) {
            Long id = et.getId();
            if (id != null && (result == null || result < id)) {
                result = id;
            }
        }
        return result;
    }

    public void addResource(String name, String xsd, long minimumId) {
        Long id = getHighestResourceId();
        if (id == null || id < minimumId) {
            id = minimumId;
        }
        ++id;

        add(ResourceType.of(name, id, xsd));
        add(ExplorerType.of(name));
    }
}
