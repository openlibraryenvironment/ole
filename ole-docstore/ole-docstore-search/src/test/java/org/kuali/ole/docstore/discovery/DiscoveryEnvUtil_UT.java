package org.kuali.ole.docstore.discovery;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;

/**
 * Created by IntelliJ IDEA.
 * User: Sreekanth
 * Date: 6/6/12
 * Time: 3:06 PM
 * To change this template use File | Settings | File Templates.
 */
public class DiscoveryEnvUtil_UT extends BaseTestCase {


    @Test
    public void testInitEnvironment() throws Exception {
        String discoveryPropertyFilePath = discoveryEnvUtil.getDiscoveryPropertiesFilePath();
        assertNotNull(discoveryPropertyFilePath);

    }
}
