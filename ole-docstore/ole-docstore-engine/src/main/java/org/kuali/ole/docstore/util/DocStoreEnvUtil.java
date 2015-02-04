package org.kuali.ole.docstore.util;

import java.io.File;
import java.io.IOException;
import java.util.*;

import org.apache.commons.lang.StringUtils;
import org.kuali.ole.utility.ConfigUtil;
import org.kuali.ole.utility.Constants;
import org.kuali.ole.utility.ResourceItem;
import org.kuali.ole.utility.ResourceUtil;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA. User: Sreekanth Date: 6/6/12 Time: 3:19 PM To change this template use File | Settings | File Templates.
 */
public class DocStoreEnvUtil {
    private static final Logger LOG = LoggerFactory.getLogger(DocStoreEnvUtil.class);
    public static final String DOCSTORE_ROOT_DIR = "docstore";
    public static final String DOCSTORE_ROOT_DEFAULT = "/opt/" + DOCSTORE_ROOT_DIR;
    public static final String DOCSTORE_ROOT_TEST_DEFAULT = "/opt/docstore-test";
    public static final String DOCSTORE_PROPERTIES_FOLDER = "properties";
    public static final String DOCSTORE_PROPERTIES_FILE = "documentstore.properties";
//    public static final String DOCSTORE_JACKRABBIT_FOLDER = "jackrabbit";
//    public static final String DOCSTORE_JACKRABBIT_CONFIG_FOLDER = "jackrabbit-config";
//    public static final String DOCSTORE_JACKRABBIT_PROPERTIES_FILE = "repository.xml";
    public static final String DOCSTORE_LOG4J_PROPERTIES_FILE = "log4j.properties";
    public static final String DOCSTORE_DOCUMENTCONFIG_FILE = "DocumentConfig.xml";
    public static final String DOC_STORE_PROPERTIES_FILE_SYS_PROP = "ole.docstore.properties";

    protected String docStorePropertiesFilePath;
    protected String rootFolderPath;
    protected String docStorePropertiesFolderPath;
//    protected String jackrabbitFolderPath;
//    protected String jackrabbitPropertyFilePath;
    // protected String log4jPropertyFilePath;
    protected String docStoreDocumentConfigFilePath;
//    protected String jackrabbitConfigFolderPath;
    private boolean testEnv = false;
    ResourceUtil resourceUtil = new ResourceUtil();
    private String vendor;
    private Properties properties;

    /**
     * Initializes environment for normal use of docStore. This should be called during web app initialization.
     */
    public void initEnvironment() throws IOException {
        initEnv(DOCSTORE_ROOT_DEFAULT);
    }

    /**
     * Initializes environment for testing. This should be called in all test cases.
     */
    public void initTestEnvironment() throws IOException {
        testEnv = true;
        initEnv(DOCSTORE_ROOT_TEST_DEFAULT);
    }

    /**
     * Configure the home directory for docstore. When this method returns the system property <code>ole.docstore.home</code> is guaranteed to be set.
     */
    protected void initEnv(String defaultRootValue) throws IOException {
        String docStoreRoot = getDocStoreRoot(defaultRootValue);
        resourceUtil.setSystemProperty(Constants.OLE_DOCSTORE_HOME_SYSTEM_PROPERTY, docStoreRoot);
        initEnvironmentCommon(docStoreRoot);
    }

    /**
     * Calculate the correct home directory for docstore based on system properties and environment. If no system properties or environment variables are set, fall through to the
     * default directory.
     */
    public String getDocStoreRoot(String defaultValue) {
        ConfigUtil configUtil = new ConfigUtil();
        String suppliedValue = System.getProperty(Constants.OLE_DOCSTORE_HOME_SYSTEM_PROPERTY);
        if (StringUtils.isBlank(suppliedValue)) {
            return configUtil.getApplicationHome(Constants.KUALI_GROUP, DOCSTORE_ROOT_DIR);
        } else {
            return System.getProperty(Constants.OLE_DOCSTORE_HOME_SYSTEM_PROPERTY, defaultValue);
        }
    }

    public void initPathValues(String docStoreRoot) {
        rootFolderPath = docStoreRoot;
        docStorePropertiesFolderPath = rootFolderPath.concat("/").concat(DOCSTORE_PROPERTIES_FOLDER);
//        jackrabbitFolderPath = rootFolderPath.concat("/").concat(DOCSTORE_JACKRABBIT_FOLDER);
//        jackrabbitConfigFolderPath = rootFolderPath.concat("/").concat(DOCSTORE_JACKRABBIT_CONFIG_FOLDER);
//        jackrabbitPropertyFilePath = jackrabbitConfigFolderPath.concat("/").concat(DOCSTORE_JACKRABBIT_PROPERTIES_FILE);
        docStorePropertiesFilePath = docStorePropertiesFolderPath.concat("/").concat(DOCSTORE_PROPERTIES_FILE);
        // log4jPropertyFilePath = docStorePropertiesFolderPath.concat("/").concat(DOCSTORE_LOG4J_PROPERTIES_FILE);
        docStoreDocumentConfigFilePath = docStorePropertiesFolderPath.concat("/").concat(DOCSTORE_DOCUMENTCONFIG_FILE);
    }

    /**
     * Initializes an environment for the DocStore application at the given root folder. Looks for necessary folders and files under the root folder. Any resources that already
     * exist are left untouched. Any that are missing are created using default values.
     */
    public void initEnvironmentCommon(String docStoreRoot) {
        initPathValues(docStoreRoot);
        List<ResourceItem> resourceItems = getResourceItems();
        for (ResourceItem item : resourceItems) {
            resourceUtil.validate(item);
        }
    }

    /**
     * Return a list of <code>ResourceItem</code> objects representing resources required by the docstore application.
     */
    protected List<ResourceItem> getResourceItems() {
        String docStorePath = "";
        String path = "";
        if (testEnv) {
            String rootPath = getClass().getClassLoader().getResource(".").getPath();
            docStorePath = "File:" + rootPath + "/../../../ole-docstore-webapp/src/main/resources/";
            path = docStorePath;
        } else {
            path = "classpath:";
        }
        ResourceItem docStorePropsFolder = new ResourceItem();
        docStorePropsFolder.setDestination(new File(docStorePropertiesFolderPath));
        docStorePropsFolder.setSystemProperty("docstore.properties.home");
        docStorePropsFolder.setLocalDirectory(true);

        // ResourceItem docStorePropsFile = new ResourceItem();
        // docStorePropsFile.setDestination(new File(docStorePropertiesFilePath));
        // docStorePropsFile.setSourceLocation(path + "documentstore.properties");
        // docStorePropsFile.setSystemProperty("ole.docstore.properties");

//        ResourceItem jackrabbitFolder = new ResourceItem();
//        jackrabbitFolder.setDestination(new File(jackrabbitFolderPath));
//        jackrabbitFolder.setSystemProperty("org.apache.jackrabbit.repository.home");
//        jackrabbitFolder.setLocalDirectory(true);

//        ResourceItem jackrabbitConfigFolder = new ResourceItem();
//        jackrabbitConfigFolder.setDestination(new File(jackrabbitConfigFolderPath));
//        jackrabbitConfigFolder.setLocalDirectory(true);

//        String vendor = getVendor();
//        if (StringUtils.isBlank(vendor)) {
//            throw new IllegalArgumentException("The property 'db.vendor' cannot be blank");
//        }
//        ResourceItem jackrabbitRepositoryFile = new ResourceItem();
//        jackrabbitRepositoryFile.setDestination(new File(jackrabbitPropertyFilePath));
//        jackrabbitRepositoryFile.setSourceLocation(path + "repository-" + vendor + ".xml");
//        jackrabbitRepositoryFile.setSystemProperty("org.apache.jackrabbit.repository.conf");
//        jackrabbitRepositoryFile.setFilter(true);
//        jackrabbitRepositoryFile.setProperties(getProperties());

        // ResourceItem log4jConfigFile = new ResourceItem();
        // log4jConfigFile.setDestination(new File(log4jPropertyFilePath));
        // log4jConfigFile.setSourceLocation(path + "log4j.properties");
        // log4jConfigFile.setSystemProperty("log4j.configuration");

        ResourceItem documentConfigFile = new ResourceItem();
        documentConfigFile.setDestination(new File(docStoreDocumentConfigFilePath));
        documentConfigFile.setSourceLocation(path + "DocumentConfig.xml");
        documentConfigFile.setSystemProperty("document.config.file");

        List<ResourceItem> items = new ArrayList<ResourceItem>();
        items.add(docStorePropsFolder);
        // items.add(docStorePropsFile);
//        items.add(jackrabbitFolder);
//        items.add(jackrabbitRepositoryFile);
        // items.add(log4jConfigFile);
        items.add(documentConfigFile);
        return items;
    }

    private Properties getProperties() {
        if (null == properties) {
            return ConfigContext.getCurrentContextConfig().getProperties();
        }
        return properties;
    }


    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    private String getVendor() {
        if (null == vendor) {
            return ConfigContext.getCurrentContextConfig().getProperty("db.vendor");
        }
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public void printEnvironment() {
        String docStoreRoot = System.getProperty(Constants.OLE_DOCSTORE_HOME_SYSTEM_PROPERTY);
        System.out.println("**********{{ Docstore Environment **********");
        System.out.println(Constants.OLE_DOCSTORE_HOME_SYSTEM_PROPERTY + "=" + docStoreRoot);
        System.out.println("docStorePropertiesFilePath=" + docStorePropertiesFilePath);
//        System.out.println("jackrabbitFolderPath=" + jackrabbitFolderPath);
//        System.out.println("jackrabbitPropertyFilePath=" + jackrabbitPropertyFilePath);
        // System.out.println("log4jPropertyFilePath=" + log4jPropertyFilePath);
        System.out.println("docStoreDocumentConfigFilePath=" + docStoreDocumentConfigFilePath);
        System.out.println("**********}} DOCSTORE Environment **********");
    }

    public void logEnvironment() {
        String docStoreRoot = System.getProperty(Constants.OLE_DOCSTORE_HOME_SYSTEM_PROPERTY);
        LOG.info("**********{{ Docstore Environment **********");
        LOG.info(Constants.OLE_DOCSTORE_HOME_SYSTEM_PROPERTY + "=" + docStoreRoot);
        LOG.info("docStorePropertiesFilePath=" + docStorePropertiesFilePath);
//        LOG.info("jackrabbitFolderPath=" + jackrabbitFolderPath);
//        LOG.info("jackrabbitPropertyFilePath=" + jackrabbitPropertyFilePath);
        // LOG.info("log4jPropertyFilePath=" + log4jPropertyFilePath);
        LOG.info("docStoreDocumentConfigFilePath=" + docStoreDocumentConfigFilePath);
        LOG.info("**********}} Docstore Environment **********");
    }

    public String getDocStorePropertiesFolderPath() {
        return docStorePropertiesFolderPath;
    }

    public void setDocStorePropertiesFolderPath(String docStorePropertiesFolderPath) {
        this.docStorePropertiesFolderPath = docStorePropertiesFolderPath;
    }

    public String getDocStorePropertiesFilePath() {
        return docStorePropertiesFilePath;
    }

    public void setDocStorePropertiesFilePath(String docStorePropertiesFilePath) {
        this.docStorePropertiesFilePath = docStorePropertiesFilePath;
    }

//    public String getJackrabbitFolderPath() {
//        return jackrabbitFolderPath;
//    }
//
//    public void setJackrabbitFolderPath(String jackrabbitFolderPath) {
//        this.jackrabbitFolderPath = jackrabbitFolderPath;
//    }
//
//    public String getJackrabbitPropertyFilePath() {
//        return jackrabbitPropertyFilePath;
//    }
//
//    public void setJackrabbitPropertyFilePath(String jackrabbitPropertyFilePath) {
//        this.jackrabbitPropertyFilePath = jackrabbitPropertyFilePath;
//    }

    public String getRootFolderPath() {
        return rootFolderPath;
    }

    public void setRootFolderPath(String rootFolderPath) {
        this.rootFolderPath = rootFolderPath;
    }

//    public String getJackrabbitConfigFolderPath() {
//        return jackrabbitConfigFolderPath;
//    }
//
//    public void setJackrabbitConfigFolderPath(String jackrabbitConfigFolderPath) {
//        this.jackrabbitConfigFolderPath = jackrabbitConfigFolderPath;
//    }
}
