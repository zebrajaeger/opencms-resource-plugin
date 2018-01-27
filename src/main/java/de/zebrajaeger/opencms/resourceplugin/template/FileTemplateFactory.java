package de.zebrajaeger.opencms.resourceplugin.template;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.io.FileUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileAlreadyExistsException;
import java.util.Map;

/**
 * Created by lars on 11.02.2017.
 */
public class FileTemplateFactory {
    private static final Logger LOG = LoggerFactory.getLogger(FileTemplateFactory.class);

    public void writeToFile(FileTemplate template, File target) throws FileTemplateFactoryException, IOException {
        if (target.exists()) {
            throw new FileAlreadyExistsException(target.getAbsolutePath());
        }

        LOG.info("Create File '{}'", target);

        String content = generate(template);
        FileUtils.write(target, content, StandardCharsets.UTF_8);
    }

    public String generate(FileTemplate template) throws FileTemplateFactoryException {
        try {
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
        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            throw new FileTemplateFactoryException("could not render template '" + template + "'", e);
        }
    }
}
