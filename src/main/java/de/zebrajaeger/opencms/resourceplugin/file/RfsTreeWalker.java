package de.zebrajaeger.opencms.resourceplugin.file;

import de.zebrajaeger.opencms.resourceplugin.FilePair;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public abstract class RfsTreeWalker {
    private RfsFolder root;


    public void walk() {
        walk(root);
    }

    public void walk(RfsFolder rfsFolder) {
        for( VfsFile f : rfsFolder.createVfsFileList()){
            onFile(f);
        }
        for( VfsFolder f : rfsFolder.createVfsFolderList()){
            onFolder(f);
            walk()
            // TODO REKUSRION
        }
    }

    public abstract void onFile(VfsFile vfsFile );
    public abstract void onFolder(VfsFolder vfsFolder );
}
