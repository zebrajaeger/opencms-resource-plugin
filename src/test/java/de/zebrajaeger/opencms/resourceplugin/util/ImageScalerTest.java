package de.zebrajaeger.opencms.resourceplugin.util;

import org.junit.Ignore;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

public class ImageScalerTest {

    public static final String GOOGLE_LOGGO = "https://www.google.com/images/srpr/logo3w.png";

    @Test
    @Ignore("Manual test case")
    public void createIcon() throws IOException {
        URL url = new URL(GOOGLE_LOGGO);
        ImageScaler imageScaler = new ImageScaler();

        File dest = File.createTempFile("createIconTest", "_24.png");
        BufferedImage src = ImageIO.read(url);
        BufferedImage img = imageScaler.scaleTo(src, 50);
        ImageIO.write(img, "png", dest);

        System.out.println("file://" + dest.getAbsolutePath());

        Desktop dt = Desktop.getDesktop();
        dt.open(dest);
    }
}