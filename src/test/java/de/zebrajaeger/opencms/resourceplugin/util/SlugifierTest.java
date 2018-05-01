package de.zebrajaeger.opencms.resourceplugin.util;

import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URL;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class SlugifierTest {
    @Test
    public void url() throws MalformedURLException {
        URL url = new URL("https://en.wikipedia.org/wiki/Pinky_and_the_Brain/media/File:PinkyandtheBrain.Pinky.png");
        assertThat(Slugifier.nameFromUrl(url), is("file-pinkyandthebrain-pinky.png"));
    }
}