package org.kuali.ole.docstore.repository;

import org.junit.Test;
import org.kuali.ole.BaseTestCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 12/21/12
 * Time: 2:50 PM
 * To change this template use File | Settings | File Templates.
 */
public class WorkLicenseNodeManager_UT extends BaseTestCase {
    private static Logger logger = LoggerFactory.getLogger(WorkLicenseNodeManager_UT.class);

    @Test
    public void testParentNodePath() throws Exception {

        WorkLicenseNodeManager workLicenseNodeManager = WorkLicenseNodeManager.getInstance();
        String parentNodePath = workLicenseNodeManager.getParentNodePath();
        logger.info("Parent Node Path:" + parentNodePath);
    }
}
