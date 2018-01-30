package de.zebrajaeger.opencms.resourceplugin.template;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.LinkedList;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class FormatterConfigTemplateTest {

    @Test
    public void width1() throws IOException, FileTemplateFactoryException {
        String soll = IOUtils.toString(getClass().getResourceAsStream("formatter-config-width-1.xml"), StandardCharsets.UTF_8);
        FileTemplateFactory f = new FileTemplateFactory();
        String ist = f.generate(new FormatterConfigTemplate("foo-bar", "/foo-bar.jsp", null, -1));
        assertThat(normalize(ist), is(normalize(soll)));
    }
    @Test
    public void width2() throws IOException, FileTemplateFactoryException {
        String soll = IOUtils.toString(getClass().getResourceAsStream("formatter-config-width-2.xml"), StandardCharsets.UTF_8);
        FileTemplateFactory f = new FileTemplateFactory();
        String ist = f.generate(new FormatterConfigTemplate("foo-bar", "/foo-bar.jsp", null, -2));
        assertThat(normalize(ist), is(normalize(soll)));
    }

    @Test
    public void types() throws IOException, FileTemplateFactoryException {
        String soll = IOUtils.toString(getClass().getResourceAsStream("formatter-config-types.xml"), StandardCharsets.UTF_8);
        FileTemplateFactory f = new FileTemplateFactory();
        String ist = f.generate(new FormatterConfigTemplate(
                "foo-bar",
                "/foo-bar.jsp",
                new LinkedList(Arrays.asList("narf", "meep")),
                null));
        System.out.println(ist);
        assertThat(normalize(ist), is(normalize(soll)));
    }

    private String normalize(String value) {
        return value.replace("\r\n", "\n");
    }

}