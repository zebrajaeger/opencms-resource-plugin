package de.zebrajaeger.opencms.resourceplugin.file;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.File;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class RfsFolder {
    public static final String OCMSFILE_XML = ".ocmsfile.xml";
    public static final String OCMSFOLDER_XML = ".ocmsfolder.xml";

    private String name;
    private File vfsRoot;
    private File manifestRoot;

    private Set<String> fileNames = new HashSet<>();
    private Set<String> folderNames = new HashSet<>();
    private Set<String> unexpectedFileNames = new HashSet<>();

    private Set<String> vfsFileNames = new HashSet<>();
    private Set<String> vfsFolderNames = new HashSet<>();

    private Set<String> manifestFileNames = new HashSet<>();
    private Set<String> manifestFolderNames = new HashSet<>();
    private Set<String> manifestFolderFileNames = new HashSet<>();

    protected RfsFolder(String name, File vfsRoot, File manifestRoot) {
        this.name = name;
        this.vfsRoot = vfsRoot;
        this.manifestRoot = manifestRoot;
    }

    public static RfsFolder of(String name, File vfsRoot, File manifestRoot) {
        RfsFolder result = new RfsFolder(name, vfsRoot, manifestRoot);
        result.populate();
        return result;
    }

    public static RfsFolder of(File vfsFolder, File manifestFolder) {
        return of(null, vfsFolder, manifestFolder);
    }

    protected void populate() {
        expandVfs();
        expandManifest();
        generalizeNames();
    }

    protected void expandVfs() {
        File folder = (name != null) ? new File(vfsRoot, name) : vfsRoot;
        if (folder.exists()) {
            for (File f : folder.listFiles()) {
                if (f.isFile()) {
                    vfsFileNames.add(f.getName());
                } else if (f.isDirectory()) {
                    vfsFolderNames.add(f.getName());
                }
            }
        }
    }

    protected void expandManifest() {
        File folder = (name != null) ? new File(manifestRoot, name) : manifestRoot;
        if (folder.exists()) {
            for (File f : folder.listFiles()) {
                String name = f.getName();
                if (f.isFile()) {
                    if (name.endsWith(OCMSFILE_XML)) {
                        manifestFileNames.add(name);
                    } else if (name.endsWith(OCMSFOLDER_XML)) {
                        manifestFolderFileNames.add(name);
                    } else {
                        unexpectedFileNames.add(name);
                    }
                } else if (f.isDirectory()) {
                    manifestFolderNames.add(name);
                }
            }
        }
    }

    protected void generalizeNames() {
        fileNames.addAll(vfsFileNames);
        for (String n : manifestFileNames) {
            fileNames.add(n.substring(n.length() - OCMSFILE_XML.length()));
        }

        folderNames.addAll(vfsFolderNames);
        folderNames.addAll(manifestFolderNames);
        for (String n : manifestFolderFileNames) {
            folderNames.add(n.substring(n.length() - OCMSFOLDER_XML.length()));
        }
    }

    public List<VfsFile> createVfsFileList() {
        List<VfsFile> result = new LinkedList<>();
        for (String n : fileNames) {
            result.add(VfsFile.of(n, vfsRoot, manifestRoot));
        }

        return result;
    }

    public List<VfsFolder> createVfsFolderList() {
        List<VfsFolder> result = new LinkedList<>();
        for (String n : folderNames) {
            result.add(VfsFolder.of(n, vfsRoot, manifestRoot));
        }
        return result;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
}
