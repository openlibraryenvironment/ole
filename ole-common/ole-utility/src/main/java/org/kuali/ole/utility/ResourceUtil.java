package org.kuali.ole.utility;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.PropertyPlaceholderHelper;

public class ResourceUtil {

    private static final Logger log = LoggerFactory.getLogger(ResourceUtil.class);
    public static final String PREFIX = "${";
    public static final String SUFFIX = "}";

    public String getProperty(Properties props, String defaultPrefix, String envPrefix, String key) {
        String defaultValue = props.getProperty(defaultPrefix + "." + key);
        String environmentValue = props.getProperty(envPrefix + "." + key);
        if (!StringUtils.isBlank(environmentValue)) {
            // Use the environment value if one is provided
            return environmentValue;
        } else {
            // Otherwise fall through to the default value
            return defaultValue;
        }
    }

    public String getSystemProperty(String property, String defaultValue) {
        String s = System.getProperty(property);
        if (StringUtils.isBlank(s)) {
            return defaultValue;
        } else {
            return s.trim();
        }

    }

    public void validate(ResourceItem item) {
        boolean validExisting = isValidExistingResource(item);
        if (validExisting) {
            return;
        }
        createOrVerifyAsNeeded(item);
    }

    protected void createOrVerifyAsNeeded(ResourceItem item) {
        if (item.isLocalDirectory()) {
            createLocalDirectory(item.getDestination());
            setSystemProperty(item.getSystemProperty(), item.getDestination().getAbsolutePath());
            return;
        }

        String path = item.getDestination().getAbsolutePath();
        if (exists(path)) {
            log.info("Found existing resource " + path);
            setSystemProperty(item.getSystemProperty(), path);
            return;
        }

        String location = item.getSourceLocation();
        boolean sourceExists = exists(location);
        if (!sourceExists) {
            throw new IllegalArgumentException(location + " does not exist");
        }
        try {
            log.info("Creating " + path + " from " + location);
            if (item.isFilter()) {
                log.info("Filtering content with " + item.getProperties().size() + " properties");
                filterThenCopy(location, item.getDestination(), item.getProperties());
            } else {
                copy(location, item.getDestination());
            }
            setSystemProperty(item.getSystemProperty(), path);
        } catch (IOException e) {
            throw new IllegalStateException("Unable to create " + path);
        }
    }

    public void setSystemProperty(String property, String value) {
        log.info("Setting " + property + "=" + value);
        System.setProperty(property, value);
    }

    protected void filterThenCopy(String location, File file, Properties properties) throws IOException {
        InputStream in = null;
        try {
            in = getInputStream(location);
            String content = IOUtils.toString(in);
            String filtered = getFilteredContent(content, properties);
            FileUtils.writeStringToFile(file, filtered);
        } finally {
            IOUtils.closeQuietly(in);
        }
    }

    protected void copy(String location, File file) throws IOException {
        InputStream in = null;
        OutputStream out = null;
        try {
            in = getInputStream(location);
            out = FileUtils.openOutputStream(file);
            IOUtils.copy(in, out);
        } finally {
            IOUtils.closeQuietly(in);
            IOUtils.closeQuietly(out);
        }
    }

    protected boolean isValidExistingResource(ResourceItem item) {

        // Extract the the value of the system property
        String value = System.getProperty(item.getSystemProperty());

        // No system property was set for this item, we are done
        if (StringUtils.isBlank(value)) {
            return false;
        }

        // Does the resource exist?
        boolean exists = exists(value);

        boolean valid = exists;
        if (item.isLocalDirectory()) {
            valid = exists && isLocalDirectory(value);
        }

        if (valid) {
            // The resource is valid, we are done
            return true;
        }

        // They supplied a system property pointing to a required resource that
        // does not exist
        String msg = item.getSystemProperty() + "=" + value + " is required, but does not exist";
        throw new IllegalArgumentException(msg);
    }

    public Properties getPropertiesFromSystemProperty(String systemProperty) throws IOException {
        String location = System.getProperty(systemProperty);
        if (StringUtils.isBlank(location)) {
            return new Properties();
        }
        boolean exists = exists(location);
        if (!exists) {
            log.info("Skipping property loading from '" + location + "' as it does not exist");
            return new Properties();
        }
        return getProperties(location);
    }

    public Properties getProperties(List<String> locations) throws IOException {
        Properties props = new Properties();
        for (String location : locations) {
            Properties p = getProperties(location);
            props.putAll(p);
        }
        return props;
    }

    public Properties getProperties(String location) throws IOException {
        InputStream in = null;
        try {
            in = getInputStream(location);
            Properties props = new Properties();
            log.info("Loading properties from " + location);
            props.load(in);
            return props;
        } finally {
            IOUtils.closeQuietly(in);
        }
    }

    /**
     *
     */
    public InputStream getInputStream(String location) throws IOException {
        File file = new File(location);
        if (file.exists()) {
            return new FileInputStream(file);
        }
        ResourceLoader loader = new DefaultResourceLoader();
        Resource resource = loader.getResource(location);
        return resource.getInputStream();
    }

    public void createLocalDirectory(File dir) {
        try {
            FileUtils.forceMkdir(dir);
        } catch (IOException e) {
            throw new IllegalStateException("Unable to create " + dir.getAbsolutePath());
        }
    }

    public boolean isLocalDirectory(String location) {
        File file = new File(location);
        return file.isDirectory();
    }

    /**
     * Return true if <code>location</code> is a file on the local file system or points to a resource Spring's resource loading API can
     * understand and locate. For example, <code>classpath:config.properties</code>. Return false otherwise.
     */
    public boolean exists(String location) {
        File file = new File(location);
        if (file.exists()) {
            return true;
        }
        ResourceLoader loader = new DefaultResourceLoader();
        Resource resource = loader.getResource(location);
        return resource.exists();
    }

    public String getFilteredContent(String s, Properties properties) {
        PropertyPlaceholderHelper pph = new PropertyPlaceholderHelper(PREFIX, SUFFIX);
        String filtered = pph.replacePlaceholders(s, properties);
        return filtered;
    }

    public File getFile(String location) throws IOException {
        ResourceLoader loader = new DefaultResourceLoader();
        Resource resource = loader.getResource(location);
        return resource.getFile();
    }

    public void copyDirectory(String srcLoc, File dstDir) throws IOException {
        File srcDir = getFile(srcLoc);
        FileUtils.copyDirectory(srcDir, dstDir);
    }

}
