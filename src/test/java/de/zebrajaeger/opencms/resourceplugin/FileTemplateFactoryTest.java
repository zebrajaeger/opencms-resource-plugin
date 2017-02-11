package de.zebrajaeger.opencms.resourceplugin;

import de.zebrajaeger.opencms.resourceplugin.template.FileTemplateFactory;
import de.zebrajaeger.opencms.resourceplugin.template.OcmsFileTemplate;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;

/**
 * Created by lars on 11.02.2017.
 */
public class FileTemplateFactoryTest {
    @Test
    public void foo() throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        FileTemplateFactory f = new FileTemplateFactory();
        System.out.println(f.generate(new OcmsFileTemplate("plain")));
    }
}