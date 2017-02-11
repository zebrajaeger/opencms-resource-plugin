package de.zebrajaeger.opencms.resourceplugin.mojo;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.File;

/**
 * Created by lars on 11.02.2017.
 */
@Mojo(name = "touch", defaultPhase = LifecyclePhase.PROCESS_SOURCES)
public class CreateResourceMojo extends AbstractMojo {

    @Parameter(defaultValue = "/src/opencms/manifest", property = "manifestDir", required = true)
    private File manifestDir;

    @Parameter(defaultValue = "manifest_stub.xml", property = "manifestStubFile", required = true)
    private String manifestStubFile;

    @Parameter(defaultValue = "/src/opencms/manifest", property = "vfsDir", required = true)
    private File vfsDir;

    @Parameter(defaultValue = "auto", property = "resourceId", required = true)
    private String resourceId;

    @Parameter(defaultValue = "default.png", property = "icon", required = true)
    private String icon;

    @Parameter(defaultValue = "default-big.png", property = "bigicon", required = true)
    private String bigicon;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        checkParamters();
    }

    private void checkParamters() throws MojoExecutionException {
        checkDirectory(manifestDir, "manifestDir");
        File manifestStub = new File(manifestDir, manifestStubFile);
        checkFile(manifestStub, "manifestStubFile");
        checkDirectory(vfsDir, "vfsDir");
        checkResourceid();
        checkIcons();
    }

    private void checkDirectory(File toCheck, String varName) throws MojoExecutionException {
        if (toCheck == null) {
            String msg = String.format("directory variable '%s' cannot be null", varName);
            throw new MojoExecutionException(msg);

        } else if (!toCheck.exists()) {
            String msg = String.format("directory variable '%s': '%s' does not exist", varName, toCheck.getAbsolutePath());
            throw new MojoExecutionException(msg);

        } else if (!toCheck.isDirectory()) {
            String msg = String.format("directory variable '%s': '%s' ist not a directory", varName, toCheck.getAbsolutePath());
            throw new MojoExecutionException(msg);
        }
    }

    private void checkFile(File toCheck, String varName) throws MojoExecutionException {
        if (toCheck == null) {
            String msg = String.format("file variable '%s' cannot be null", varName);
            throw new MojoExecutionException(msg);

        } else if (!toCheck.exists()) {
            String msg = String.format("file variable '%s': '%s' does not exist", varName, toCheck.getAbsolutePath());
            throw new MojoExecutionException(msg);

        } else if (!toCheck.isFile()) {
            String msg = String.format("file variable '%s': '%s' ist not a file", varName, toCheck.getAbsolutePath());
            throw new MojoExecutionException(msg);
        }
    }

    private void checkResourceid() throws MojoExecutionException {
        if (StringUtils.isBlank(resourceId)) {
            String msg = String.format("'resourceId' variable cannot be blank");
            throw new MojoExecutionException(msg);
        }

        if (!"auto".equals(resourceId)) {
            try {
                long l = Long.parseLong(resourceId);
                if (l < 1) {
                    String msg = String.format("value of 'resourceId' is '%s' bust must be 'auto' or a number greater than zero", l);
                    throw new MojoExecutionException(msg);
                }
            } catch (NumberFormatException e) {
                String msg = String.format("value of 'resourceId' must be 'auto' or a number greater than zero");
                throw new MojoExecutionException(msg);
            }
        }
    }

    private void checkIcons() throws MojoExecutionException {
        if (StringUtils.isBlank(icon)) {
            String msg = String.format("'icon' variable cannot be blank");
            throw new MojoExecutionException(msg);
        }
        if (StringUtils.isBlank(bigicon)) {
            String msg = String.format("'bigicon' variable cannot be blank");
            throw new MojoExecutionException(msg);
        }
    }

        @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }

}
