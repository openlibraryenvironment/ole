package org.kuali.ole;

import org.junit.Test;
import org.kuali.rice.core.api.config.property.ConfigContext;

import static junit.framework.Assert.assertNotNull;

/**
 * Created by IntelliJ IDEA.
 * User: pvsubrah
 * Date: 4/4/12
 * Time: 3:41 PM
 * To change this template use File | Settings | File Templates.
 */
public class PropertyUtil_IT extends KFSTestCaseBase {

    @Test
    public void testOleProperties() throws Exception {
        String devUrl = ConfigContext.getCurrentContextConfig().getProperty("ole.fs.url.base");
        assertNotNull(devUrl);
        System.out.println(devUrl);
    }
}
