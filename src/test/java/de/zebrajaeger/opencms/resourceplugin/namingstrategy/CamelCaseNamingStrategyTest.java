package de.zebrajaeger.opencms.resourceplugin.namingstrategy;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class CamelCaseNamingStrategyTest {

    @Test
    public void convert() {
        assertThat(new CamelCaseNamingStrategy("FooBar").getConvertedName(), is("FooBar"));
        assertThat(new CamelCaseNamingStrategy("Foo-Bar").getConvertedName(), is("FooBar"));
        assertThat(new CamelCaseNamingStrategy("foo").getConvertedName(), is("Foo"));
        assertThat(new CamelCaseNamingStrategy("foo-bar").getConvertedName(), is("FooBar"));
        assertThat(new CamelCaseNamingStrategy("-foo-bar").getConvertedName(), is("FooBar"));
        assertThat(new CamelCaseNamingStrategy("foo-bar-").getConvertedName(), is("FooBar"));
        assertThat(new CamelCaseNamingStrategy("-foo-bar-").getConvertedName(), is("FooBar"));
        assertThat(new CamelCaseNamingStrategy("foo--bar").getConvertedName(), is("FooBar"));
        assertThat(new CamelCaseNamingStrategy("foo----bar").getConvertedName(), is("FooBar"));
        assertThat(new CamelCaseNamingStrategy("foo_bar").getConvertedName(), is("FooBar"));
        assertThat(new CamelCaseNamingStrategy("foo_-bar").getConvertedName(), is("FooBar"));
        assertThat(new CamelCaseNamingStrategy("foo-_bar").getConvertedName(), is("FooBar"));
        assertThat(new CamelCaseNamingStrategy("foo---___bar").getConvertedName(), is("FooBar"));
    }
}