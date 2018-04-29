package de.zebrajaeger.opencms.resourceplugin.manipulator.xml;

import org.jdom2.Document;
import org.jdom2.JDOMException;
import org.safehaus.uuid.UUID;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class OcmsFolderManipulator extends XmlManipulator {
    public OcmsFolderManipulator(Document doc) {
        super(doc);
    }

    public OcmsFolderManipulator(File xmlFile) throws JDOMException, IOException {
        super(xmlFile);
    }

    public OcmsFolderManipulator(InputStream xmlInput) throws JDOMException, IOException {
        super(xmlInput);
    }

    public String getStructureIdString() {
        return findText("/file/uuidstructure");
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

    public String getDateCreatedString() {
        return findText("/file/datecreated");
    }

    public boolean isDateCreatedPlaceholder() {
        String dcs = getDateCreatedString();
        return (dcs == null) ? false : "${datecreated}".equals(dcs);
    }

    public String getDateLastModifiedString() {
        return findText("/file/datelastmodified");
    }

    public boolean isDateLastModifiedPlaceholder() {
        String dcs = getDateCreatedString();
        return (dcs == null) ? false : "${datelastmodified}".equals(dcs);
    }

}
