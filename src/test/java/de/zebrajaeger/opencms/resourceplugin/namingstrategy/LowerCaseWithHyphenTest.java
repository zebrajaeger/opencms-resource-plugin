package de.zebrajaeger.opencms.resourceplugin.namingstrategy;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class LowerCaseWithHyphenTest {

    @Test
    public void convert() {
        assertThat(new LowerCaseWithHyphenNamingStrategy("foo").getConvertedName(), is("foo"));
        assertThat(new LowerCaseWithHyphenNamingStrategy("foo-bar").getConvertedName(), is("foo-bar"));
        assertThat(new LowerCaseWithHyphenNamingStrategy("-foo-bar").getConvertedName(), is("foo-bar"));
        assertThat(new LowerCaseWithHyphenNamingStrategy("foo-bar-").getConvertedName(), is("foo-bar"));
        assertThat(new LowerCaseWithHyphenNamingStrategy("-foo-bar-").getConvertedName(), is("foo-bar"));
        assertThat(new LowerCaseWithHyphenNamingStrategy("foo--bar").getConvertedName(), is("foo-bar"));
        assertThat(new LowerCaseWithHyphenNamingStrategy("foo-----bar").getConvertedName(), is("foo-bar"));
        assertThat(new LowerCaseWithHyphenNamingStrategy("fooBar").getConvertedName(), is("foo-bar"));
        assertThat(new LowerCaseWithHyphenNamingStrategy("foo-Bar").getConvertedName(), is("foo-bar"));
        assertThat(new LowerCaseWithHyphenNamingStrategy("FooBar").getConvertedName(), is("foo-bar"));
        assertThat(new LowerCaseWithHyphenNamingStrategy("Foo-Bar").getConvertedName(), is("foo-bar"));
    }
}