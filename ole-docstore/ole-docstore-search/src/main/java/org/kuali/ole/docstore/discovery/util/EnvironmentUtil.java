/*
package org.kuali.ole.docstore.discovery.util;


import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.*;

*/
/**
 * Created by IntelliJ IDEA.
 * User: SG7940
 * Date: 3/15/12
 * Time: 2:53 PM
 * To change this template use File | Settings | File Templates.
 *//*

public class EnvironmentUtil {
    private static final Logger LOG = LoggerFactory.getLogger(EnvironmentUtil.class);
    public static final  String                  DISCOVERY_ROOT_DEFAULT      = "/opt/ole-discovery/local";
    public static final  String                  DISCOVERY_ROOT_TEST_DEFAULT = "/opt/ole-discovery/test";
    public static final  String                  SOLR_CONFIG_FOLDER          = "/solr-config";
    public static final  String                  SOLR_DATA_FOLDER            = "/solr-data";
    public static final  String                  SOLR_PROPERTIES_FOLDER      = "/properties";
    public static final  String                  SOLR_PROPERTIES_FILE        = "/properties/ole-discovery.properties";

    public static String solrConfigFolderPath;
    public static String solrDataFolderPath;
    public static String solrPropertyFolderPath;
    public static String discoveryPropertiesFilePath;


    */
/**
 * Initializes environment for normal use of discovery.
 * This should be called during web app initialization.
 *//*

    public static void initEnvironment() {
        String discoveryRoot = System.getProperty("OLE_DISCOVERY_ROOT");
        if (discoveryRoot == null || discoveryRoot.isEmpty()) {
            System.setProperty("OLE_DISCOVERY_ROOT", DISCOVERY_ROOT_DEFAULT);
        }
        initEnvironmentCommon();
    }

    */
/**
 * Initializes environment for testing.
 * This should be called in all test cases.
 *//*

    public static void initTestEnvironment() {
        String discoveryRoot = System.getProperty("OLE_DISCOVERY_ROOT");
        if (discoveryRoot == null || discoveryRoot.isEmpty()) {
            System.setProperty("OLE_DISCOVERY_ROOT", DISCOVERY_ROOT_TEST_DEFAULT);
        }
        initEnvironmentCommon();
    }

    */
/**
 * Initializes environment for Discovery app at the given root folder.
 * Looks for necessary folders and property files in the root folder,
 * and creates them if not available.
 * <p/>
 * Check for root folder.
 * If found, exit. else proceed.
 * Define the following system properties required by Discovery:
 * solr.solr.home=%rootFolder%/solr-config
 * discovery.properties.file=%rootFolder%/properties/ole-discovery.properties
 * Create the root folder and subfolders (solr-config, solr-data, properties)
 * Copy the contents of solrconfig (from discovery-core/resources) to %rootFolder%/solr-config
 * Make sure the <dataDir> in %rootFolder%/solr-config/bib/conf/solrconfig.xml is set to %rootFolder%/solr-data
 * Copy sample ole-discovery.properties file (from discovery-core/resources) to %rootFolder%/properties/ole-discovery.properties
 *
 * @throws java.io.IOException
 * @throws java.net.URISyntaxException
 *//*

    public static void initEnvironmentCommon() {
        try {
            String rootFolder = System.getProperty("OLE_DISCOVERY_ROOT");
            solrConfigFolderPath = rootFolder.concat(SOLR_CONFIG_FOLDER);
            solrDataFolderPath = rootFolder.concat(SOLR_DATA_FOLDER);
            solrPropertyFolderPath = rootFolder.concat(SOLR_PROPERTIES_FOLDER);
            discoveryPropertiesFilePath = rootFolder.concat(SOLR_PROPERTIES_FILE);

            System.setProperty("solr.solr.home", solrConfigFolderPath);
            System.setProperty("discovery.properties.file", discoveryPropertiesFilePath);

            File rootFolderPath = new File(rootFolder);
            if (!rootFolderPath.isDirectory()) {
                rootFolderPath.mkdirs();
                //creating the Solr Root directory
                createDirectory(solrConfigFolderPath);
                //creating the Solr  Data directory
                createDirectory(solrDataFolderPath);
                //creating the Solr  Discovery directory
                createDirectory(solrPropertyFolderPath);
                URL solrConfigResource = EnvironmentUtil.class.getClassLoader().getResource("solrconfig");
                File ConfigResource = new File(solrConfigResource.toURI());
                File target = new File(solrConfigFolderPath);
                FileUtils.copyDirectory(ConfigResource, target);

                //Make sure the <dataDir> in %rootFolder%/solr-config/bib/conf/solrconfig.xml is set to %rootFolder%/solr-data
                URL solrConfigFileResource = EnvironmentUtil.class.getClassLoader()
                                                                  .getResource("./solrconfig/bib/conf/solrconfig.xml");
                File solrConfigFile = new File(solrConfigFileResource.toURI());
                String configFileContent = FileUtils.readFileToString(solrConfigFile, "UTF-8");
                int startingIndex = configFileContent.indexOf("<dataDir>");
                int endingIndex = configFileContent.indexOf("</dataDir>");
                String subString = configFileContent.substring(startingIndex, endingIndex).concat("</dataDir>");
                String modifiedConfigFileContent = configFileContent
                        .replaceAll(subString, "<dataDir>" + solrDataFolderPath + "</dataDir>");
                File targetFile = new File(solrConfigFolderPath + "/bib/conf/solrconfig.xml");
                FileUtils.writeStringToFile(targetFile, modifiedConfigFileContent);

                //Copy sample ole-discovery.properties file (from discovery-core/resources) to %rootFolder%/properties/ole-discovery.properties
                URL resource = EnvironmentUtil.class.getClassLoader().getResource("ole-discovery.properties");
                File sourceProperties = new File(resource.toURI());
                File targetProperties = new File(discoveryPropertiesFilePath);
                FileUtils.copyFile(sourceProperties, targetProperties);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void printEnvironment() {

        String discoveryRoot = System.getProperty("OLE_DISCOVERY_ROOT");
        //initEnvironmentCommon(SOLR_CONFIG_FOLDER);
        System.out.println("**********{{ Discovery Environment **********");
        System.out.println("OLE_DISCOVERY_ROOT=" + discoveryRoot);
        System.out.println("solrConfigFolderPath=" + solrConfigFolderPath);
        System.out.println("solrDataFolderPath=" + solrDataFolderPath);
        System.out.println("solrPropertyFolderPath=" + solrPropertyFolderPath);
        System.out.println("discoveryPropertiesFilePath=" + discoveryPropertiesFilePath);

        System.out.println("solr.solr.home=" + System.getProperty("solr.solr.home"));
        System.out.println("discovery.properties.file=" + System.getProperty("discovery.properties.file"));
        System.out.println("**********}} Discovery Environment **********");
    }

    //creates the Directory .
    private static void createDirectory(String dirName) {
        File solarTest = new File(dirName);
        if (!solarTest.isDirectory()) {
            solarTest.mkdirs();
        }

    }


}
*/
