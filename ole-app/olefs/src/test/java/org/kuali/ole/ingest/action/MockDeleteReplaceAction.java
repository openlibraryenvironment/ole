package org.kuali.ole.ingest.action;

import org.kuali.ole.describe.service.MockDiscoveryHelperService;
import org.kuali.ole.describe.service.MockDocstoreHelperService;
import org.kuali.rice.krms.api.engine.ExecutionEnvironment;
import org.kuali.rice.krms.framework.engine.Action;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 12/18/12
 * Time: 6:06 PM
 * To change this template use File | Settings | File Templates.
 */
public class MockDeleteReplaceAction implements Action {

    private MockDocstoreHelperService docstoreHelperService;
    private MockDiscoveryHelperService discoveryHelperService;


    @Override
    public void execute(ExecutionEnvironment environment) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void executeSimulation(ExecutionEnvironment environment) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public MockDocstoreHelperService getDocstoreHelperService() {
        return docstoreHelperService;
    }

    public void setDocstoreHelperService(MockDocstoreHelperService docstoreHelperService) {
        this.docstoreHelperService = docstoreHelperService;
    }

    public MockDiscoveryHelperService getDiscoveryHelperService() {
        return discoveryHelperService;
    }

    public void setDiscoveryHelperService(MockDiscoveryHelperService discoveryHelperService) {
        this.discoveryHelperService = discoveryHelperService;
    }
}
