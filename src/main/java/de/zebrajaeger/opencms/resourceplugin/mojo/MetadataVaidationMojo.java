package de.zebrajaeger.opencms.resourceplugin.mojo;

import org.apache.commons.io.IOUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.jdom2.xpath.XPathExpression;
import org.jdom2.xpath.XPathFactory;
import org.safehaus.uuid.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

@SuppressWarnings("unused")
@Mojo(name = "validateMetadata", defaultPhase = LifecyclePhase.VALIDATE)
public class MetadataVaidationMojo extends AbstractMojo {

    private static final Logger LOG = LoggerFactory.getLogger(MetadataVaidationMojo.class);

    @SuppressWarnings("unused")
    @Parameter(defaultValue = "${project.basedir}/src/main/opencms/manifest", property = "manifestDir", required = true)
    private File manifestDir;

    @SuppressWarnings("unused")
    @Parameter(defaultValue = "manifest_stub.xml", property = "manifestStubFile", required = true)
    private String manifestStubFile;

    @SuppressWarnings("unused")
    @Parameter(defaultValue = "${project.basedir}/src/main/opencms/vfs", property = "vfsDir", required = true)
    private File vfsDir;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {

        hasXPath("/file/uuidstructure", is(validUUID()));

        try {
            Document d = loadXml(null);
            d.

        } catch (IOException e) {
            throw new MojoExecutionException("msg", e);
        }
    }

    private void getXpath(String xpath, Object context) {
        XPathFactory xPathFactory = XPathFactory.instance();
        XPathExpression<Object> exp = xPathFactory.compile(xpath);
        exp.evaluate(context);
    }

    private boolean isValidUUID(String uuid) {
        return createUUID(uuid) != null;
    }

    private UUID createUUID(String uuid) {
        try {
            return (uuid == null) ? null : UUID.valueOf(uuid);
        } catch (NumberFormatException e) {
            // return null
        }
        return null;
    }

    protected Document loadXml(Path file) throws IOException {
        try {
            String xml = IOUtils.toString(Files.newInputStream(file), StandardCharsets.UTF_8);

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setFeature("http://apache.org/xml/features/nonvalidating/load-dtd-grammar", false);
            factory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
            DocumentBuilder documentBuilder = factory.newDocumentBuilder();

            return documentBuilder.parse(new InputSource(new StringReader(xml)));
        } catch (Exception e) {
            throw new IOException(e);
        }
    }
}
