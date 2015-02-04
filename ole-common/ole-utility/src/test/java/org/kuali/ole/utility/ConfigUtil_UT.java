package org.kuali.ole.utility;

import org.junit.Test;
import org.kuali.ole.docstore.xstream.BaseTestCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static junit.framework.Assert.assertNotNull;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: ?
 * Time: ?
 * To change this template use File | Settings | File Templates.
 */
public class ConfigUtil_UT extends BaseTestCase {
    private static final Logger LOG = LoggerFactory.getLogger(ConfigUtil_UT.class);

    @Test
    public void testConfigUtil() throws Exception {
        ConfigUtil configUtil = new ConfigUtil();
        String kualiHome = configUtil.getKualiHome();
        LOG.info(kualiHome);
        String applicationHome = configUtil.getApplicationHome(Constants.KUALI_GROUP, Constants.OLE_DOCSTORE_APP);
        LOG.info(applicationHome);
        assertNotNull(configUtil.getValue(null, "JAVA_HOME", null));


    }

}
