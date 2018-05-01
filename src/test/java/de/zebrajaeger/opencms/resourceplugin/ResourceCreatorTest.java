package de.zebrajaeger.opencms.resourceplugin;

import org.junit.Test;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ResourceCreatorTest {

    @Test
    public void foo() {
        File f = new File("");
        for (Path p : f.toPath()) {
            System.out.println("> '" + p + "'");

        }
    }

    @Test
    public void bar() {
        Path f = Paths.get("foo/bar");
        for (Path p : f) {
            System.out.println("> '" + p + "'");

        }

    }
}