package de.zebrajaeger.opencms.resourceplugin;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.File;

/**
 * Created by lars on 12.02.2017.
 */
public class FilePair {
    private File vfs;
    private File manifest;

    public FilePair(File vfs, File manifest) {
        this.vfs = vfs;
        this.manifest = manifest;
    }

    public File getVfs() {
        return vfs;
    }

    public File getManifest() {
        return manifest;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
}
