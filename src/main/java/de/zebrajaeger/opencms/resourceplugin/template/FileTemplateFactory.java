package de.zebrajaeger.opencms.resourceplugin.template;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;

import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

/**
 * Created by lars on 11.02.2017.
 */
public class FileTemplateFactory {

    public String generate(FileTemplate template) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {

        VelocityEngine ve = new VelocityEngine();
        ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
        ve.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
        ve.init();

        Template t = ve.getTemplate(template.getTemplate());
        VelocityContext context = new VelocityContext();

        Map<String, String> m = BeanUtils.describe(template);
        for (Object key : m.keySet()) {
            context.put(key.toString(), m.get(key));
        }

        StringWriter writer = new StringWriter();
        t.merge(context, writer);

        return writer.toString();
    }
}
