package de.zebrajaeger.opencms.resourceplugin.data;

import org.jdom2.Element;

import java.util.Objects;

public class ManifestStubResource {
    private String uri;

    public static ManifestStubResource of(String uri) {
        return new ManifestStubResource(uri);
    }

    private ManifestStubResource(String uri) {
        this.uri = Objects.requireNonNull(uri, "uri");
    }

    public Element toXml() {
        Element result = new Element("resource");

        result.setAttribute("uri", uri);

        return result;
    }
}
