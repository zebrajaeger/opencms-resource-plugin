package de.zebrajaeger.opencms.resourceplugin.manipulator.xml;

import org.jdom2.Document;
import org.jdom2.JDOMException;
import org.safehaus.uuid.UUID;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class OcmsFileManipulator extends XmlManipulator {
    public OcmsFileManipulator(Document doc) {
        super(doc);
    }

    public OcmsFileManipulator(File xmlFile) throws JDOMException, IOException {
        super(xmlFile);
    }

    public OcmsFileManipulator(InputStream xmlInput) throws JDOMException, IOException {
        super(xmlInput);
    }

    public String getStructureIdString() {
        return findText("/fileinfo/file/uuidstructure");
    }

    public UUID getStructureId() {
        String structureIdString = getStructureIdString();
        try {
            return (structureIdString == null) ? null : UUID.valueOf(structureIdString);
        } catch (NumberFormatException e) {
            // return null
        }
        return null;
    }

    public boolean isStructureIdValid() {
        return getStructureId() != null;
    }

    public String getResourceIdString() {
        return findText("/fileinfo/file/uuidresource");
    }

    public UUID getRecourceIdId() {
        String resourceIdString = getResourceIdString();
        try {
            return (resourceIdString == null) ? null : UUID.valueOf(resourceIdString);
        } catch (NumberFormatException e) {
            // return null
        }
        return null;
    }

    public boolean isRecourceIdValid() {
        return getRecourceIdId() != null;
    }

    public String getDateCreatedString() {
        return findText("/fileinfo/datecreated");
    }

    public boolean isDateCreatedPlaceholder() {
        String dcs = getDateCreatedString();
        return (dcs == null) ? false : "${datecreated}".equals(dcs);
    }

    public String getDateLastModifiedString() {
        return findText("/fileinfo/datelastmodified");
    }

    public boolean isDateLastModifiedPlaceholder() {
        String dcs = getDateCreatedString();
        return (dcs == null) ? false : "${datelastmodified}".equals(dcs);
    }

}
