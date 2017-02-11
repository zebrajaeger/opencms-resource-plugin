package de.zebrajaeger.opencms.resourceplugin.data.explorertype;

import de.zebrajaeger.opencms.resourceplugin.data.BasisType;
import de.zebrajaeger.opencms.resourceplugin.util.XmlUtils;
import org.jdom2.Element;

import java.text.DecimalFormat;

/**
 * Created by lars on 11.02.2017.
 */
public class NewResource extends BasisType {
    private String page;
    private String uri;
    private Double order;
    private Boolean autosetnavigation;
    private Boolean autosettitle;
    private String info;
    private String key;

    private NewResource() {
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public Double getOrder() {
        return order;
    }

    public void setOrder(Double order) {
        this.order = order;
    }

    public Boolean getAutosetnavigation() {
        return autosetnavigation;
    }

    public void setAutosetnavigation(Boolean autosetnavigation) {
        this.autosetnavigation = autosetnavigation;
    }

    public Boolean getAutosettitle() {
        return autosettitle;
    }

    public void setAutosettitle(Boolean autosettitle) {
        this.autosettitle = autosettitle;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public NewResource page(String value) {
        page = value;
        return this;
    }

    public NewResource uri(String value) {
        uri = value;
        return this;
    }

    public NewResource order(Double value) {
        order = value;
        return this;
    }

    public NewResource autosetnavigation(Boolean value) {
        autosetnavigation = value;
        return this;
    }

    public NewResource autosettitle(Boolean value) {
        autosettitle = value;
        return this;
    }

    public NewResource info(String value) {
        info = value;
        return this;
    }

    public NewResource key(String value) {
        key = value;
        return this;
    }

    public Element toXml() {
        Element result = new Element("newresource");
        if (page != null) {
            result.setAttribute("page", page);
        }
        if (uri != null) {
            result.setAttribute("uri", uri);
        }
        if (order != null) {
            DecimalFormat format = new DecimalFormat();
            format.setDecimalSeparatorAlwaysShown(false);
            result.setAttribute("order", format.format(order));
        }
        if (autosetnavigation != null) {
            result.setAttribute("autosetnavigation", autosetnavigation.toString());
        }
        if (autosettitle != null) {
            result.setAttribute("autosettitle", autosettitle.toString());
        }
        if (info != null) {
            result.setAttribute("info", info);
        }
        if (key != null) {
            result.setAttribute("key", key);
        }
        return result;
    }

    public static NewResource of(String name) {
        return new NewResource()
                .page("structurecontent")
                .uri("newresource_xmlcontent.jsp?newresourcetype=" + name)
                .order(10D)
                .autosetnavigation(false)
                .autosettitle(false)
                .info("desc." + name)
                .key("title." + name);
    }

    public static NewResource of(Element e) {
        XmlUtils.checkType(e, "newresource");
        return new NewResource()
                .page(XmlUtils.readStringAttribute(e, "page"))
                .uri(XmlUtils.readStringAttribute(e, "uri"))
                .order(XmlUtils.readDoubleAttribute(e, "order"))
                .autosetnavigation(XmlUtils.readBooleanAttribute(e, "autosetnavigation"))
                .autosettitle(XmlUtils.readBooleanAttribute(e, "autosettitle"))
                .info(XmlUtils.readStringAttribute(e, "info"))
                .key(XmlUtils.readStringAttribute(e, "key"));
    }
}
