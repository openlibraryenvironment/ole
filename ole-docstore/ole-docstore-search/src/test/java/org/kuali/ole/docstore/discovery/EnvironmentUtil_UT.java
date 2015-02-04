/*
package org.kuali.ole.docstore.discovery;

import static junit.framework.Assert.assertTrue;

import java.io.File;
import java.net.URISyntaxException;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.kuali.ole.docstore.discovery.util.EnvironmentUtil;

*/
/**
 * Created by IntelliJ IDEA.
 * User: SG7940
 * Date: 3/15/12
 * Time: 2:59 PM
 * To change this template use File | Settings | File Templates.
 *//*

public class EnvironmentUtil_UT {
    private static final String EXPECTED_STRING = "/opt/ole-discovery/local";
    private static final String DISCOVERY_VALUE = "/opt/ole-discovery/local/solr-config";
    private static final String DATA_DIR        = "/opt/ole-discovery/local/solr-config/bib/conf/solrconfig.xml";
    private static final String PROPERTY_FILE   = "/opt/ole-discovery/local/properties/ole-discovery.properties";


    */
/**
 *  @Author
 * @throws Exception
 *//*


    */
/**
 * Set OLE_DISCOVERY_ROOT to some specific folder and
 * verify the action of EnvironmentUtil.initEnvironment()
 *//*

    @Test
    public void testInitEnvironment() throws Exception, URISyntaxException {
        String rootFolder = "/opt/ole-discovery/localtest";
        System.setProperty("OLE_DISCOVERY_ROOT", rootFolder);
        EnvironmentUtil.initEnvironment();
        // verifyEnvironment(rootFolder);
    }

    */
/**
 * Set OLE_DISCOVERY_ROOT to Empty and
 * verify the action of EnvironmentUtil.initEnvironment()
 * It should use the default folder EnvironmentUtil.DISCOVERY_ROOT_DEFAULT
 *//*

    @Test
    public void testDefaultInitEnvironment() throws Exception, URISyntaxException {
        String rootFolderForNull = "";
        System.setProperty("OLE_DISCOVERY_ROOT", rootFolderForNull);
        EnvironmentUtil.printEnvironment();
        EnvironmentUtil.initEnvironment();
        //verifyEnvironment(rootFolderForNull);
        EnvironmentUtil.printEnvironment();
    }

    */
/**
 * Verifies the contents of given rootFolder.
 *
 * @param rootFolder
 * @throws Exception
 *//*

    private void verifyEnvironment(String rootFolder) throws Exception {
        // setting the  /opt/ole-discovery/local Property
        boolean isFileExists;
        boolean isDataExists;
        boolean isDirExists;
        boolean isPropertyFileExists;
        boolean isPropertyFileDataExists;
        String value = System.setProperty("OLE_DISCOVERY_ROOT", "/opt/ole-discovery/local");
        String valueChecking = System.getProperty("OLE_DISCOVERY_ROOT");
        EnvironmentUtil.initEnvironment();
        File discoveryFile = new File(DISCOVERY_VALUE);
        isFileExists = discoveryFile.exists();
        File dataDirFile = new File(DATA_DIR);
        isDirExists = dataDirFile.exists();
        String str = FileUtils.readFileToString(dataDirFile);
        isDataExists = str.contains("<dataDir>/opt/ole-discovery/local/solr-data</dataDir>");

        File propertyFile = new File(PROPERTY_FILE);
        isPropertyFileExists = propertyFile.exists();
        String propertyFileStr = FileUtils.readFileToString(propertyFile);
        isPropertyFileDataExists = propertyFileStr.contains("docSearchURL=http://localhost:9080/ole-discovery/");

        assertTrue(isPropertyFileDataExists);
        assertTrue(isPropertyFileExists);
        assertTrue(isFileExists);
        assertTrue(isDirExists);
        assertTrue(isDataExists);


    }
}
*/
