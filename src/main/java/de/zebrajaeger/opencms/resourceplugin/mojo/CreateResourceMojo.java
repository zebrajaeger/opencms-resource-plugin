package de.zebrajaeger.opencms.resourceplugin.mojo;

import de.zebrajaeger.opencms.resourceplugin.ResourceCreator;
import de.zebrajaeger.opencms.resourceplugin.ResourceCreatorConfig;
import de.zebrajaeger.opencms.resourceplugin.ResourceCreatorException;
import de.zebrajaeger.opencms.resourceplugin.namingstrategy.NamingStrategy;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

/**
 * Created by lars on 11.02.2017.
 */
@SuppressWarnings("unused")
@Mojo(name = "createResource", defaultPhase = LifecyclePhase.GENERATE_SOURCES)
public class CreateResourceMojo extends AbstractMojo implements ResourceCreatorConfig {

    private static final Logger LOG = LoggerFactory.getLogger(CreateResourceMojo.class);

    @SuppressWarnings("unused")
    @Parameter(defaultValue = "${project.basedir}/src/main/opencms/manifest", property = "manifestDir", required = true)
    private File manifestDir;

    @SuppressWarnings("unused")
    @Parameter(defaultValue = "manifest_stub.xml", property = "manifestStubFile", required = true)
    private String manifestStubFile;

    @SuppressWarnings("unused")
    @Parameter(defaultValue = "${project.basedir}/src/main/opencms/vfs", property = "vfsDir", required = true)
    private File vfsDir;

    @SuppressWarnings("unused")
    @Parameter(property = "newResourceName", required = true)
    private String newResourceName;

    /**
     * 'auto' or a long >0
     */
    @SuppressWarnings("unused")
    @Parameter(defaultValue = "auto", property = "resourceId", required = true)
    private String resourceId;

    @SuppressWarnings("unused")
    @Parameter(defaultValue = "default.png", property = "icon", required = true)
    private String icon;

    @SuppressWarnings("unused")
    @Parameter(defaultValue = "default-big.png", property = "bigicon", required = true)
    private String bigicon;

    @SuppressWarnings("unused")
    @Parameter(defaultValue = "${project.artifactId}", property = "moduleName", required = true)
    private String moduleName;

    @SuppressWarnings("unused")
    @Parameter(defaultValue = "ce", property = "resourceTypeSubDirectory", required = true)
    private String resourceTypeSubDirectory;

    @SuppressWarnings("unused")
    @Parameter(
            defaultValue = "de.zebrajaeger.opencms.resourceplugin.namingstrategy.CamelCaseNamingStrategy",
            property = "schemaTypeNamingStrategyClass",
            required = true)
    private String schemaTypeNamingStrategyClass;
    private NamingStrategy schemaTypeNamingStrategy;

    @SuppressWarnings("unused")
    @Parameter(
            defaultValue = "de.zebrajaeger.opencms.resourceplugin.namingstrategy.LowerCaseWithHyphenNamingStrategy",
            property = "resourceTypeNamingStrategyClass",
            required = true)
    private String resourceTypeNamingStrategyClass;
    private NamingStrategy resourceTypeNamingStrategy;

    @SuppressWarnings("unused")
    @Parameter(
            defaultValue = "${project.artifactId}.workplace",
            property = "workplaceBundlePath",
            required = true)
    private String workplaceBundlePath;

    @SuppressWarnings("unused")
    @Parameter(
            defaultValue = "src/main/resources/workplace.properties",
            property = "workplacePropertiesPath",
            required = true)
    private String workplacePropertiesPath;

    @SuppressWarnings("unused")
    @Parameter(
            defaultValue = "true",
            property = "addResourceTypeToModuleConfig",
            required = true)
    private boolean addResourceTypeToModuleConfig;

    /**
     * 'distributed' or 'resource'
     */
    @SuppressWarnings("unused")
    @Parameter(defaultValue = "resource", property = "layout", required = true)
    private String layout;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        checkAndConvertParameters();

        try {
            StringTokenizer st = new StringTokenizer(newResourceName, ",");
            while (st.hasMoreTokens()) {
                newResourceName = st.nextToken().trim();
                new ResourceCreator().createResource(this);
            }
        } catch (ResourceCreatorException e) {
            throw new MojoExecutionException("error while creating resource", e);
        }
    }

    private void checkAndConvertParameters() throws MojoExecutionException {
        checkDirectory(manifestDir, "manifestDir");
        File manifestStub = new File(manifestDir, manifestStubFile);
        checkFile(manifestStub, "manifestStubFile");
        checkDirectory(vfsDir, "vfsDir");
        checkStringNotBlank(newResourceName, "newResourceName");
        checkRecourceNameChars(newResourceName);
        checkResourceId();

        resourceTypeSubDirectory = normalizePath(resourceTypeSubDirectory);

        checkStringNotBlank(icon, "icon");
        checkStringNotBlank(bigicon, "bigicon");
        checkStringNotBlank(moduleName, "moduleName");
        checkStringOneOf(layout, "layout", true, "distributed", "resource");

        resourceTypeNamingStrategy = createNamingStrategyInstance(resourceTypeNamingStrategyClass);
        LOG.info("Choose name for resourceType: '{}'", resourceTypeNamingStrategy);

        schemaTypeNamingStrategy = createNamingStrategyInstance(schemaTypeNamingStrategyClass);
        LOG.info("Choose name for schemaType: 'OpenCms{}'", schemaTypeNamingStrategy);
    }

    private String normalizePath(String path) {
        if (StringUtils.isBlank(path)) {
            return "";
        } else {
            return FilenameUtils.separatorsToUnix(path.trim());
        }
    }

    private NamingStrategy createNamingStrategyInstance(String className) throws MojoExecutionException {
        try {
            Class<NamingStrategy> clazz = (Class<NamingStrategy>) Class.forName(className);
            Constructor<NamingStrategy> constructor = clazz.getConstructor(String.class);
            return constructor.newInstance(getNewResourceName());
        } catch (ClassNotFoundException
                | IllegalAccessException
                | InstantiationException
                | ClassCastException
                | NoSuchMethodException
                | InvocationTargetException e) {
            String msg = String.format("Can not create instance of class '%'.", className);
            throw new MojoExecutionException(msg, e);
        }
    }

    /**
     * comma separated list of names that can contain word-characters and minus-chars. Except fist char that can be 'a'...'z' and 'A'...'Z'.
     *
     * @throws MojoExecutionException
     */
    private void checkRecourceNameChars(String value) throws MojoExecutionException {
        String name = "\\s*[a-zA-Z][\\w-]+\\s*";
        String nameList = name + "(," + name + ")*";
        if (!Pattern.compile(nameList).matcher(value).matches()) {
            String msg = String.format("newResourceName '%s' does not match the pattern '%s'", value, nameList);
            throw new MojoExecutionException(msg);
        }
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

    private void checkResourceId() throws MojoExecutionException {
        checkStringNotBlank(resourceId, "resourceId");

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

    private void checkStringNotBlank(String value, String name) throws MojoExecutionException {
        if (StringUtils.isBlank(value)) {
            String msg = String.format("'%s' variable cannot be blank", name);
            throw new MojoExecutionException(msg);
        }
    }

    private void checkFirstCharUppercase(String value, String name) throws MojoExecutionException {
        if (!Character.isUpperCase(value.toCharArray()[0])) {
            String msg = String.format("First char must be uppercase in variable '%s'", name);
            throw new MojoExecutionException(msg);
        }
    }

    private void checkStringOneOf(String value, String name, boolean notEmpty, String... occurrence) throws MojoExecutionException {
        if (notEmpty) {
            if (StringUtils.isBlank(value)) {
                String msg = String.format("'%s' variable cannot be blank", name);
                throw new MojoExecutionException(msg);
            }
        }

        boolean ok = false;
        for (String x : occurrence) {
            ok |= x.equals(value);
        }
        if (!ok) {
            String msg = String.format("'%s' variable must have one of these values: '%s'", name, occurrence);
            throw new MojoExecutionException(msg);
        }
    }

    @Override
    public String getResourceSchemaName() {
        return schemaTypeNamingStrategy.getConvertedName();
    }

    @Override
    public String getResourceTypeName() {
        return resourceTypeNamingStrategy.getConvertedName();
    }

    //<editor-fold desc="Getter/Setter">
    public File getManifestDir() {
        return manifestDir;
    }

    public String getManifestStubFile() {
        return manifestStubFile;
    }

    public File getVfsDir() {
        return vfsDir;
    }

    public String getNewResourceName() {
        return newResourceName;
    }

    public String getResourceId() {
        return resourceId;
    }

    public String getIcon() {
        return icon;
    }

    public String getBigicon() {
        return bigicon;
    }

    public String getModuleName() {
        return moduleName;
    }

    public Layout getLayout() {
        return Layout.valueOf(layout.toUpperCase());
    }

    public String getResourceTypeSubDirectory() {
        return resourceTypeSubDirectory;
    }

    public String getWorkplaceBundlePath() {
        return workplaceBundlePath;
    }

    @Override
    public String getWorkplacePropertiesPath() {
        return workplacePropertiesPath;
    }

    public boolean isAddResourceTypeToModuleConfig() {
        return addResourceTypeToModuleConfig;
    }

    //</editor-fold>

    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
