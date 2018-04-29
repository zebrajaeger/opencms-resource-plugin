package de.zebrajaeger.opencms.resourceplugin.manipulator.properties;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;

public class PropertiesFileManipulator {
    private static final String NEWLINE = "\n";
    private File file;
    private List<String> lines = new LinkedList<>();

    public PropertiesFileManipulator(File file) {
        this.file = file;
    }

    public void addComment(String comment) {
        lines.add("# " + comment);
    }

    public void addEmptyLine() {
        lines.add("");
    }

    public void add(String key, String value) {
        lines.add(key + ": " + value);
    }

    public void save() throws IOException {
        String content = StringUtils.join(lines, NEWLINE) + NEWLINE;
        FileUtils.writeStringToFile(file, content, StandardCharsets.ISO_8859_1, true);
    }
}
