package de.zebrajaeger.opencms.resourceplugin;

import de.zebrajaeger.opencms.resourceplugin.util.ResourceUtils;

import java.io.File;

/**
 * Created by lars on 11.02.2017.
 */
public class ResourceCreator {
    private File manifestModuleRoot;
    private File vfsModuleRoot;

    public void createResource(String name){
        String recourcename = ResourceUtils.toResourceName(name);

        File manifestResourceFolder = new File(manifestModuleRoot, recourcename);
        manifestResourceFolder.mkdirs();



        File vfsResourceFolder = new File(manifestModuleRoot, recourcename);
        vfsResourceFolder.mkdirs();
    }
}
