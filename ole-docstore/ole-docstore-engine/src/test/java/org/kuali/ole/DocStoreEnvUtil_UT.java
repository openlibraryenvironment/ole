package org.kuali.ole;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;

/**
 * Created by IntelliJ IDEA.
 * User: Sreekanth
 * Date: 6/6/12
 * Time: 4:30 PM
 * To change this template use File | Settings | File Templates.
 */
public class DocStoreEnvUtil_UT
        extends BaseTestCase {

    @Test
    public void testInitEnvironment() throws Exception {
        String docStorePropertyFilePath = docStoreEnvUtil.getDocStorePropertiesFilePath();
        assertNotNull(docStorePropertyFilePath);
    }
}
