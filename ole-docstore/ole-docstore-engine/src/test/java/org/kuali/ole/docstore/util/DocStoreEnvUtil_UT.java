package org.kuali.ole.docstore.util;

import org.junit.Test;
import org.kuali.ole.BaseTestCase;

import java.io.IOException;
import java.util.Properties;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 1/21/13
 * Time: 5:04 PM
 * To change this template use File | Settings | File Templates.
 */


public class DocStoreEnvUtil_UT extends BaseTestCase {

    @Test
    public void testDocStoreEnvUtil() throws IOException {
        DocStoreEnvUtil docStoreEnvUtil = new DocStoreEnvUtil();
        docStoreEnvUtil.getDocStorePropertiesFilePath();
        docStoreEnvUtil.setDocStorePropertiesFilePath(docStoreEnvUtil.getDocStorePropertiesFilePath());
        docStoreEnvUtil.getDocStorePropertiesFolderPath();
        docStoreEnvUtil.setDocStorePropertiesFolderPath(docStoreEnvUtil.getDocStorePropertiesFolderPath());
//        docStoreEnvUtil.getJackrabbitConfigFolderPath();
//        docStoreEnvUtil.setJackrabbitConfigFolderPath(docStoreEnvUtil.getJackrabbitConfigFolderPath());
//        docStoreEnvUtil.getJackrabbitPropertyFilePath();
//        docStoreEnvUtil.setJackrabbitPropertyFilePath(docStoreEnvUtil.getJackrabbitPropertyFilePath());
//        docStoreEnvUtil.getJackrabbitFolderPath();
//        docStoreEnvUtil.setJackrabbitFolderPath(docStoreEnvUtil.getJackrabbitFolderPath());
        docStoreEnvUtil.setRootFolderPath(docStoreEnvUtil.getRootFolderPath());
        docStoreEnvUtil.setVendor("derby");
        docStoreEnvUtil.setProperties(new Properties());
        docStoreEnvUtil.initTestEnvironment();
        docStoreEnvUtil.initEnvironment();
        docStoreEnvUtil.getRootFolderPath();
        docStoreEnvUtil.logEnvironment();
        docStoreEnvUtil.printEnvironment();
    }
}

