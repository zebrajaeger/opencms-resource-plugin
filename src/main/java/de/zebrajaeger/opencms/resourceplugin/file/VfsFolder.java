package de.zebrajaeger.opencms.resourceplugin.file;

import java.io.File;

public class VfsFolder extends VfsResource{
    private File folder;
    private File manifestFolder;
    private File manifestFolderFile;

    public VfsFolder(File folder, File manifestFolder, File manifestFolderFile) {
        this.folder = folder;
        this.manifestFolder = manifestFolder;
        this.manifestFolderFile = manifestFolderFile;
    }

    public static VfsFolder of(String name, File vfsRoot, File manifestRoot) {
        return new VfsFolder(
                new File(vfsRoot, name),
                new File(manifestRoot, name),
                new File(manifestRoot, name + ".ocmsfolder.xml"));
    }

    public File getFolder() {
        return folder;
    }

    public File getManifestFolder() {
        return manifestFolder;
    }

    public File getManifestFolderFile() {
        return manifestFolderFile;
    }

    public boolean isComplete() {
        return existsFolder() && existsManifestFolder() && existsManifestFolderFile();
    }

    public boolean existsFolder() {
        return folder.exists();
    }

    public boolean existsManifestFolder() {
        return manifestFolder.exists();
    }

    public boolean existsManifestFolderFile() {
        return manifestFolderFile.exists();
    }
}
