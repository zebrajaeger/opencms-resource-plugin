package de.zebrajaeger.opencms.resourceplugin;

import org.apache.commons.io.FileUtils;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.jdom2.xpath.XPathExpression;
import org.jdom2.xpath.XPathFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by lars on 11.02.2017.
 */
public class XmlManipulator {

    private Document doc;
    private org.jdom2.Element root;
    private XPathFactory xFac = XPathFactory.instance();

    public XmlManipulator(Document doc) {
        this.doc = doc;
        root = doc.getRootElement();
    }

    public XmlManipulator(File xmlFile) throws JDOMException, IOException {
        this(new SAXBuilder().build(xmlFile));
    }

    public XmlManipulator(InputStream xmlInput) throws JDOMException, IOException {
        this(new SAXBuilder().build(xmlInput));
    }

    protected Element findSingleElement(String xpath) {
        XPathExpression<Object> x = xFac.compile(xpath);
        for (Object o : x.evaluate(doc)) {
            return (Element) o;
        }
        return null;
    }

    protected List<Element> findElements(String xpath) {
        List<Element> result = new LinkedList<>();

        XPathExpression<Object> x = xFac.compile(xpath);
        for (Object o : x.evaluate(doc)) {
            result.add((Element) o);
        }

        return result;
    }

    @Override
    public String toString() {
        Format prettyFormat = Format.getPrettyFormat();
        prettyFormat.setIndent("    ");
        XMLOutputter xmOut = new XMLOutputter(prettyFormat);
        return xmOut.outputString(doc);
    }

    public void writeToFile(File target) throws IOException {
        FileUtils.write(target, toString(), StandardCharsets.UTF_8);
    }
}
