package de.zebrajaeger.opencms.resourceplugin;

import de.zebrajaeger.opencms.resourceplugin.data.explorertype.ExplorerType;
import de.zebrajaeger.opencms.resourceplugin.data.resourcetype.ResourceType;
import org.jdom2.JDOMException;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

/**
 * Created by lars on 11.02.2017.
 */
public class ManifestStubManipulatorTest {
    @Test
    public void getHighestResourceId() throws Exception {
        ManifestStubManipulator manipulator = createManipulator();
        assertThat(manipulator.getHighestResourceId(), is(100012L));
    }

    @Test
    public void read() throws JDOMException, IOException {
        ManifestStubManipulator manipulator = createManipulator();
        List<ResourceType> resourceTypes = manipulator.readResourceTypes();
        assertThat(resourceTypes.size(), is(9));
        System.out.println(resourceTypes);
    }

    @Test
    public void firstResourceType() throws JDOMException, IOException {
        ManifestStubManipulator manipulator = createManipulator();
        List<ResourceType> resourceTypes = manipulator.readResourceTypes();

        ResourceType rt = resourceTypes.get(0);
        System.out.println(rt);
        assertThat(rt.getClazz(), is("org.opencms.file.types.CmsResourceTypeXmlContent"));
        assertThat(rt.getName(), is("breadcrumb"));
        assertThat(rt.getId(), is(100000L));
        assertThat(rt.getXsd(), is("/system/modules/de.zebrajaeger.foo/breadcrumb/breadcrumb.xsd"));
    }

    @Test
    public void firstExplorerType() throws JDOMException, IOException {
        ManifestStubManipulator manipulator = createManipulator();
        List<ExplorerType> resourceTypes = manipulator.readExplorerTypes();

        ExplorerType rt = resourceTypes.get(0);
        System.out.println(rt);
        assertThat(rt.getName(), is("breadcrumb"));
        assertThat(rt.getKey(), is("fileicon.breadcrumb"));
        assertThat(rt.getIcon(), is("foo-default.png"));
        assertThat(rt.getBigicon(), is("foo-default-big.png"));
        assertThat(rt.getReference(), is("xmlcontent"));
        assertThat(rt.getNewResource(), notNullValue());
        assertThat(rt.getNewResource().getPage(), is("structurecontent"));
        assertThat(rt.getNewResource().getUri(), is("newresource_xmlcontent.jsp?newresourcetype=breadcrumb"));
        assertThat(rt.getNewResource().getOrder(), is(10D));
        assertThat(rt.getNewResource().getAutosetnavigation(), is(false));
        assertThat(rt.getNewResource().getAutosettitle(), is(false));
        assertThat(rt.getNewResource().getInfo(), is("desc.breadcrumb"));
        assertThat(rt.getNewResource().getKey(), is("title.breadcrumb"));
    }

    /*
    @Test
    public void testToString() throws JDOMException, IOException {
        ManifestStubManipulator manipulator = createManipulator();
        System.out.println(manipulator.toString());
    }
    */

    /*
    @Test
    public void testAddString() throws JDOMException, IOException {
        ManifestStubManipulator manipulator = createManipulator();
        manipulator.addResource("meep", "/fo/bar/meep.xsd", 100000);
        System.out.println(manipulator.toString());
    }
    */

    private ManifestStubManipulator createManipulator() throws JDOMException, IOException {
        return new ManifestStubManipulator(ClassLoader.getSystemClassLoader().getResourceAsStream("manifest_stub.xml"));
    }
}