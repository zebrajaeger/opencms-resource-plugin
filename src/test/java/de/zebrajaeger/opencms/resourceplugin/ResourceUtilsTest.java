package de.zebrajaeger.opencms.resourceplugin;

import de.zebrajaeger.opencms.resourceplugin.util.ResourceUtils;
import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by lars on 11.02.2017.
 */
public class ResourceUtilsTest {
    @Test
    public void toResourceName() {
        assertThat(ResourceUtils.toResourceName("foobar"), is("foobar"));
        assertThat(ResourceUtils.toResourceName("foo-bar"), is("foo-bar"));
        assertThat(ResourceUtils.toResourceName("fooBar"), is("foo-bar"));
        assertThat(ResourceUtils.toResourceName("FooBar"), is("foo-bar"));
        assertThat(ResourceUtils.toResourceName("FooBaR"), is("foo-ba-r"));
    }
}