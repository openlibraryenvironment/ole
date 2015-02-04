package org.kuali.ole.docstore.discovery.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.kuali.ole.utility.Constants;
import org.kuali.ole.utility.ResourceItem;
import org.kuali.ole.utility.ResourceUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by IntelliJ IDEA. User: Sreekanth Date: 6/6/12 Time: 2:41 PM To change this template use File | Settings | File Templates.
 */
public class DiscoveryEnvUtil {
    private static final Logger LOG = LoggerFactory.getLogger(DiscoveryEnvUtil.class);
    public static final String DISCOVERY_ROOT_DIR = "discovery";
    public static final String DISCOVERY_ROOT_DEFAULT = "/opt/docstore/" + DISCOVERY_ROOT_DIR;
    public static final String DISCOVERY_ROOT_TEST_DEFAULT = System.getProperty("user.home")+"/kuali/main/local/docstore-test/discovery-test";
    public static final String SOLR_CONFIG_FOLDER = "solr-config";
    public static final String SOLR_DATA_FOLDER = "solr-indexes";
    public static final String DISCOVERY_PROPERTIES_FOLDER = "properties";
    public static final String DISCOVERY_PROPERTIES_FILE = "ole-discovery.properties";
    public static final String UTF8 = "UTF-8";

    protected String rootFolderPath;
    protected String solrConfigFolderPath;
    protected String solrDataFolderPath;
    protected String discoveryPropertyFolderPath;
    protected String discoveryPropertiesFilePath;

    public String getSolrConfigDirectoryPath() {
        return solrConfigDirectoryPath;
    }

    private String solrConfigDirectoryPath;
    private boolean testEnv = false;
    ResourceUtil resourceUtil = new ResourceUtil();

    /**
     * Initializes environment for normal use of discovery. This should be called during web app initialization.
     */
    public void initEnvironment() {
        initEnv(DISCOVERY_ROOT_DEFAULT);
    }

    /**
     * Initializes environment for testing. This should be called in all test cases.
     */
    public void initTestEnvironment() {
        testEnv = true;
        initEnv(DISCOVERY_ROOT_TEST_DEFAULT);
    }

    /**
     * Configure the home directory for discovery. When this method return, the system property <code>ole.discover.home</code> is guaranteed to be set.
     */
    protected void initEnv(String defaultDiscoveryHome) {
        String discoveryHome = getDiscoveryHome(defaultDiscoveryHome);
        LOG.info(Constants.OLE_DISCOVERY_HOME_SYSTEM_PROPERTY + "=" + discoveryHome);
        System.setProperty(Constants.OLE_DISCOVERY_HOME_SYSTEM_PROPERTY, discoveryHome);
        initEnvironmentCommon(discoveryHome);
    }

    /**
     * Logic for determining the correct home directory for discovery. The directory to use can be configured by system property settings. If no system property is set, discovery
     * home becomes a path relative to docstore home. If docstore home is not set, it falls through to the default value.
     */
    public String getDiscoveryHome(String defaultDiscoveryHome) {
        String direct = System.getProperty(Constants.OLE_DISCOVERY_HOME_SYSTEM_PROPERTY);
        if (!StringUtils.isBlank(direct)) {
            return direct;
        }
        String docstoreHome = System.getProperty(Constants.OLE_DOCSTORE_HOME_SYSTEM_PROPERTY);
        if (StringUtils.isBlank(docstoreHome)) {
            return defaultDiscoveryHome;
        } else {
            return docstoreHome + "/" + DISCOVERY_ROOT_DIR;
        }
    }

    public void initPathValues(String discoveryRoot) {
        rootFolderPath = discoveryRoot;
        solrConfigFolderPath = discoveryRoot.concat("/").concat(SOLR_CONFIG_FOLDER);
        solrConfigDirectoryPath = solrConfigFolderPath;
        solrDataFolderPath = discoveryRoot.concat("/").concat(SOLR_DATA_FOLDER);
        discoveryPropertyFolderPath = discoveryRoot.concat("/").concat(DISCOVERY_PROPERTIES_FOLDER);
        discoveryPropertiesFilePath = discoveryPropertyFolderPath.concat("/").concat(DISCOVERY_PROPERTIES_FILE);
    }

    /**
     * Initializes an environment for discovery at the given root folder. Looks for necessary folders and files under the root folder. Any resources that already exist are left
     * untouched. Any that are missing are created using default values.
     */
    public void initEnvironmentCommon(String docStoreRoot) {
        initPathValues(docStoreRoot);
        handleSolrConfig();
        List<ResourceItem> resourceItems = getResourceItems();
        for (ResourceItem item : resourceItems) {
            resourceUtil.validate(item);
        }
    }

    protected void handleSolrConfig() {
        String srcLoc = "";
        try {
            File solrConfigDir = new File(solrConfigFolderPath);
            // The directory already exists, leave it alone
            if (solrConfigDir.isDirectory()) {
                return;
            }
            if (testEnv) {
                String path = getClass().getClassLoader().getResource(".").getPath();
                srcLoc = "File:" + path + "/../../../ole-docstore-webapp/src/main/resources/solrconfig";
            } else {
                srcLoc = "classpath:solrconfig";
            }
            File srcDir = resourceUtil.getFile(srcLoc);
            LOG.info("***** Begin Solr Config *****");
            LOG.info("Source Loc: " + srcLoc);
            LOG.info("Source Dir: " + srcDir);
            LOG.info("Dest Dir: " + solrConfigDir);
            LOG.info("Copying source dir to dest dir...");

            resourceUtil.copyDirectory(srcLoc, solrConfigDir);
            File solrConfigXmlFile = new File(solrConfigFolderPath + "/bib/conf/solrconfig.xml");
            LOG.info("Updating " + solrConfigXmlFile);
            String oldContent = FileUtils.readFileToString(solrConfigXmlFile, UTF8);
            int beginIndex = oldContent.indexOf("<dataDir>");
            int endIndex = oldContent.indexOf("</dataDir>");
            String oldValue = oldContent.substring(beginIndex, endIndex).concat("</dataDir>");
            String newValue = "<dataDir>" + solrDataFolderPath + "</dataDir>";
            LOG.info("Setting datadir=" + solrDataFolderPath);
            String newContent = oldContent.replace(oldValue, newValue);
            FileUtils.writeStringToFile(solrConfigXmlFile, newContent, UTF8);
            LOG.info("***** End Solr Config *****");
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }

    }

    /**
     * Return a list of <code>ResourceItem</code> objects representing resources required by discovery
     */
    protected List<ResourceItem> getResourceItems() {
        ResourceItem solrConfigFolder = new ResourceItem();
        solrConfigFolder.setDestination(new File(solrConfigFolderPath));
        solrConfigFolder.setSystemProperty("solr.solr.home");
        solrConfigFolder.setLocalDirectory(true);

        // ResourceItem discoveryPropertiesFile = new ResourceItem();
        // discoveryPropertiesFile.setDestination(new File(discoveryPropertiesFilePath));
        // discoveryPropertiesFile.setSystemProperty("discovery.properties.file");
        // discoveryPropertiesFile.setSourceLocation("classpath:ole-discovery.properties");

        List<ResourceItem> items = new ArrayList<ResourceItem>();
        items.add(solrConfigFolder);
        // items.add(discoveryPropertiesFile);
        return items;
    }

    public void printEnvironment() {

        // initEnvironmentCommon(SOLR_CONFIG_FOLDER);
        System.out.println("**********{{ Discovery Environment **********");
        System.out.println(Constants.OLE_DISCOVERY_HOME_SYSTEM_PROPERTY + "=" + rootFolderPath);
        System.out.println("solrConfigFolderPath=" + solrConfigFolderPath);
        System.out.println("solrDataFolderPath=" + solrDataFolderPath);
        System.out.println("discoveryPropertyFolderPath=" + discoveryPropertyFolderPath);
        System.out.println("discoveryPropertiesFilePath=" + discoveryPropertiesFilePath);
        System.out.println("**********}} Discovery Environment **********");
    }

    public void logEnvironment() {
        LOG.info("**********{{ Discovery Environment **********");
        LOG.info(Constants.OLE_DISCOVERY_HOME_SYSTEM_PROPERTY + "=" + rootFolderPath);
        LOG.info("solrConfigFolderPath=" + solrConfigFolderPath);
        LOG.info("solrDataFolderPath=" + solrDataFolderPath);
        LOG.info("discoveryPropertyFolderPath=" + discoveryPropertyFolderPath);
        LOG.info("discoveryPropertiesFilePath=" + discoveryPropertiesFilePath);
        LOG.info("**********}} Discovery Environment **********");

    }

    public String getRootFolderPath() {
        return rootFolderPath;
    }

    public void setRootFolderPath(String rootFolderPath) {
        this.rootFolderPath = rootFolderPath;
    }

    public String getSolrConfigFolderPath() {
        return solrConfigFolderPath;
    }

    public void setSolrConfigFolderPath(String solrConfigFolderPath) {
        this.solrConfigFolderPath = solrConfigFolderPath;
    }

    public String getSolrDataFolderPath() {
        return solrDataFolderPath;
    }

    public void setSolrDataFolderPath(String solrDataFolderPath) {
        this.solrDataFolderPath = solrDataFolderPath;
    }

    public String getDiscoveryPropertyFolderPath() {
        return discoveryPropertyFolderPath;
    }

    public void setDiscoveryPropertyFolderPath(String discoveryPropertyFolderPath) {
        this.discoveryPropertyFolderPath = discoveryPropertyFolderPath;
    }

    public String getDiscoveryPropertiesFilePath() {
        return discoveryPropertiesFilePath;
    }

    public void setDiscoveryPropertiesFilePath(String discoveryPropertiesFilePath) {
        this.discoveryPropertiesFilePath = discoveryPropertiesFilePath;
    }
}
