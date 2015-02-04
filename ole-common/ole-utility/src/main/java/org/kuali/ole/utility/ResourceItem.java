package org.kuali.ole.utility;

import java.io.File;
import java.util.Properties;

public class ResourceItem {
    String systemProperty;
    String sourceLocation;
    File destination;
    boolean localDirectory;
    boolean filter;
    Properties properties;

    public String getSystemProperty() {
        return systemProperty;
    }

    public void setSystemProperty(String systemProperty) {
        this.systemProperty = systemProperty;
    }

    public String getSourceLocation() {
        return sourceLocation;
    }

    public void setSourceLocation(String defaultURLLocation) {
        this.sourceLocation = defaultURLLocation;
    }

    public File getDestination() {
        return destination;
    }

    public void setDestination(File destinationFile) {
        this.destination = destinationFile;
    }

    public boolean isLocalDirectory() {
        return localDirectory;
    }

    public void setLocalDirectory(boolean directory) {
        this.localDirectory = directory;
    }

    public boolean isFilter() {
        return filter;
    }

    public void setFilter(boolean filter) {
        this.filter = filter;
    }

    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }

}
