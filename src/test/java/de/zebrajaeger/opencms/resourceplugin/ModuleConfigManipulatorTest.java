package de.zebrajaeger.opencms.resourceplugin;

import de.zebrajaeger.opencms.resourceplugin.data.ModuleConfigResourceType;
import org.jdom2.JDOMException;
import org.junit.Test;

import java.io.IOException;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by lars on 11.02.2017.
 */
public class ModuleConfigManipulatorTest {

    @Test
    public void read() throws JDOMException, IOException {
        ModuleConfigManipulator manipulator = createManipulator();

        ModuleConfigResourceType r = new ModuleConfigResourceType().typeName("fooo").detailPagesDisabled(true);
        manipulator.add( r);

        System.out.println(manipulator);
    }

    private ModuleConfigManipulator createManipulator() throws JDOMException, IOException {
        return new ModuleConfigManipulator(ClassLoader.getSystemClassLoader().getResourceAsStream(".config"));
    }
}