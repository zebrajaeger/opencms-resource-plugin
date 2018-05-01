package de.zebrajaeger.opencms.resourceplugin.mojo;

import de.zebrajaeger.opencms.resourceplugin.ResourceCreator;
import de.zebrajaeger.opencms.resourceplugin.ResourceCreatorConfig;
import de.zebrajaeger.opencms.resourceplugin.ResourceCreatorException;
import de.zebrajaeger.opencms.resourceplugin.namingstrategy.NamingStrategy;
import de.zebrajaeger.opencms.resourceplugin.util.ImageScaler;
import de.zebrajaeger.opencms.resourceplugin.util.Slugifier;
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

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by lars on 11.02.2017.
 */
@SuppressWarnings("unused")
@Mojo(name = "createResource", defaultPhase = LifecyclePhase.GENERATE_SOURCES)
public class CreateResourceMojo extends AbstractMojo implements ResourceCreatorConfig {

    private static final Logger LOG = LoggerFactory.getLogger(CreateResourceMojo.class);
    public static final String DEFAULT_ICON_NAME = "plain.png";
    public static final String DEFAULT_ICON_BIG_NAME = "plain_big.png";

    //<editor-fold desc="Mojo Properties">
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
    @Parameter(defaultValue = DEFAULT_ICON_NAME, property = "icon", required = true)
    private String icon;

    @SuppressWarnings("unused")
    @Parameter(defaultValue = DEFAULT_ICON_BIG_NAME, property = "bigicon", required = true)
    private String bigicon;

    @SuppressWarnings("unused")
    @Parameter(property = "iconSource")
    private String iconSource;

    @SuppressWarnings("unused")
    @Parameter(defaultValue = "/system/workplace/resources/filetypes/", property = "fileIconVFSPath", required = true)
    private String fileIconVFSPath;

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

    @SuppressWarnings("unused")
    @Parameter(defaultValue = "width:-1", property = "match", required = true)
    private String match;
    //</editor-fold>

    private List<String> matchTypes;
    private Integer matchWidth;

    private BufferedImage iconImage;
    private BufferedImage bigIconImage;

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

    //<editor-fold desc="Checks">
    private void checkAndConvertParameters() throws MojoExecutionException {
        checkDirectory(manifestDir, "manifestDir");
        File manifestStub = new File(manifestDir, manifestStubFile);
        checkFile(manifestStub, "manifestStubFile");
        checkDirectory(vfsDir, "vfsDir");
        checkStringNotBlank(newResourceName, "newResourceName");
        checkRecourceNameChars(newResourceName);
        checkResourceId();
        checkIcon();

        resourceTypeSubDirectory = normalizePath(resourceTypeSubDirectory);

        checkStringNotBlank(icon, "icon");
        checkStringNotBlank(bigicon, "bigicon");
        checkStringNotBlank(moduleName, "moduleName");
        checkStringOneOf(layout, "layout", true, "distributed", "resource");

        resourceTypeNamingStrategy = createNamingStrategyInstance(resourceTypeNamingStrategyClass);
        LOG.info("Choose name for resourceType: '{}'", resourceTypeNamingStrategy);

        schemaTypeNamingStrategy = createNamingStrategyInstance(schemaTypeNamingStrategyClass);
        LOG.info("Choose name for schemaType: 'OpenCms{}'", schemaTypeNamingStrategy);

        boolean hasMatch = false;
        String typePatternString = "\\s*types\\s*:\\s*([\\w-]+\\s*(?:,\\s*[\\w-]+\\s*)*)";
        Matcher typeMatcher = Pattern.compile(typePatternString).matcher(match);
        if (typeMatcher.matches()) {
            StringTokenizer st = new StringTokenizer(typeMatcher.group(1), ",");
            matchTypes = new LinkedList<>();
            while (st.hasMoreTokens()) {
                matchTypes.add(st.nextToken().trim());
                hasMatch = true;
            }
            LOG.info("choose match = types(" + matchTypes + ")");
        }

        String widthPatternString = "\\s*width\\s*:\\s*(-?\\d*)\\s*";
        Matcher widthMatcher = Pattern.compile(widthPatternString).matcher(match);
        if (widthMatcher.matches()) {
            String widthString = widthMatcher.group(1).trim();
            try {
                matchWidth = Integer.parseInt(widthString);
            } catch (NumberFormatException e) {
                throw new MojoExecutionException("Could not parse width-match in '"
                        + match
                        + "'. Valid pattern is '\\s*width\\s*:\\s*(-?\\d*)\\s*'");
            }
            LOG.info("choose match = width(" + matchWidth + ")");
            hasMatch = true;
        }

        if (!hasMatch) {
            throw new MojoExecutionException("Could not parse width-match and type-match in '"
                    + match
                    + "'. Valid patterns are '\\s*width\\s*:\\s*(-?\\d*)\\s*' and '\\s*types\\s*:\\s*([\\w-]+\\s*(?:,\\s*[\\w-]+\\s*)*)'");
        }
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

    private void checkIcon() throws MojoExecutionException {
        if (StringUtils.isBlank(iconSource)) {
            iconSource = null; // if it is just blank

            if (StringUtils.isBlank(icon)) {
                String msg = String.format("icon name must not be blank");
                throw new MojoExecutionException(msg);
            }

            if (StringUtils.isBlank(bigicon)) {
                String msg = String.format("bigicon name must not be blank");
                throw new MojoExecutionException(msg);
            }
        } else {
            // first: iconsource to url
            URL url;
            try {
                File f = new File(iconSource);
                if (f.exists()) {
                    url = f.toURI().toURL();
                } else {
                    url = new URL(iconSource);
                }
            } catch (MalformedURLException e) {
                String msg = String.format("iconSource '%s' cannot be converted to URL object: '%s'", iconSource, e.getMessage(), e);
                throw new MojoExecutionException(msg);
            }

            // second: names from url if icon or bigIcon has its default value
            if (DEFAULT_ICON_NAME.equals(icon) || DEFAULT_ICON_BIG_NAME.equals(bigicon)) {
                String name = Slugifier.nameFromUrl(url);
                String base = FilenameUtils.getBaseName(name);
                icon = base + ".png";
                bigicon = base + "-big.png";
            }

            // third: create imagedata
            try {
                BufferedImage src = ImageIO.read(url);
                iconImage = new ImageScaler().scaleTo(src, 16);
                bigIconImage = new ImageScaler().scaleTo(src, 24);
            } catch (IOException e) {
                String msg = String.format("could not load Image from iconSource '%s': '%s'", iconSource, e.getMessage(), e);
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
    //</editor-fold>

    public String getResourceSchemaName() {
        return schemaTypeNamingStrategy.getConvertedName();
    }

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

    public String getIconSource() {
        return iconSource;
    }

    public String getIcon() {
        return icon;
    }

    public String getBigicon() {
        return bigicon;
    }

    public BufferedImage getIconImage() {
        return iconImage;
    }

    public BufferedImage getBigIconImage() {
        return bigIconImage;
    }

    public String getModuleName() {
        return moduleName;
    }

    public String getFileIconVFSPath() {
        return fileIconVFSPath;
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

    public String getWorkplacePropertiesPath() {
        return workplacePropertiesPath;
    }

    public boolean isAddResourceTypeToModuleConfig() {
        return addResourceTypeToModuleConfig;
    }

    public List<String> getTypesMatch() {
        return matchTypes;
    }

    public Integer getWidthMatch() {
        return matchWidth;
    }
    //</editor-fold>

    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
