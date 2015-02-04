package org.kuali.ole.ingest.function;

import org.kuali.ole.DataCarrierService;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.describe.service.MockDiscoveryHelperService;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.rice.krms.framework.engine.Function;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 12/20/12
 * Time: 11:46 AM
 * To change this template use File | Settings | File Templates.
 */
public class MockISSNFunction implements Function {

    private MockDiscoveryHelperService discoveryHelperService;

    @Override
    public Object invoke(Object... arguments) {
        DataCarrierService dataCarrierService = GlobalResourceLoader.getService(OLEConstants.DATA_CARRIER_SERVICE);
        Object argument = arguments[0];
        String existingDocstoreField = (String)((ArrayList) argument).get(0);
        String issn = (String)((ArrayList) argument).get(1);
        boolean exists = getDiscoveryHelperService().isIssnExists(issn);
        return exists;
    }

    public MockDiscoveryHelperService getDiscoveryHelperService() {
        if (null == discoveryHelperService) {
            discoveryHelperService = new MockDiscoveryHelperService();
        }
        return discoveryHelperService;
    }

    public void setDiscoveryHelperService(MockDiscoveryHelperService discoveryHelperService) {
        this.discoveryHelperService = discoveryHelperService;
    }
}
