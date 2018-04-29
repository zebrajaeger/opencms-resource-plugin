package de.zebrajaeger.opencms.resourceplugin.file;

import java.io.File;

public class VfsFile extends VfsResource {
    private File file;
    private File mainifestFile;

    public VfsFile(File file, File mainifestFile) {
        this.file = file;
        this.mainifestFile = mainifestFile;
    }

    public static VfsFile of(String name, File vfsRoot, File manifestRoot) {
        return new VfsFile(
                new File(vfsRoot, name),
                new File(manifestRoot, name + "ocmsfile.xml"));
    }

    public File getFile() {
        return file;
    }

    public File getMainifestFile() {
        return mainifestFile;
    }

    public boolean isComplete() {
        return existsFile() && existsManifestFile();
    }

    public boolean existsFile() {
        return file.exists();
    }

    public boolean existsManifestFile() {
        return mainifestFile.exists();
    }
}
