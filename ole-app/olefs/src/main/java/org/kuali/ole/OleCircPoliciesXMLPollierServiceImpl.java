package org.kuali.ole;

import org.kuali.rice.core.api.impex.xml.XmlIngesterService;

/**
 * Created by pvsubrah on 12/6/13.
 */
public class OleCircPoliciesXMLPollierServiceImpl extends OleXmlPollerServiceImpl {
    CircPoliciesIngesterService circPoliciesIngesterService;

    @Override
    protected XmlIngesterService getIngesterService() {
          return circPoliciesIngesterService;
    }

    public CircPoliciesIngesterService getCircPoliciesIngesterService() {
        return circPoliciesIngesterService;
    }

    public void setCircPoliciesIngesterService(CircPoliciesIngesterService circPoliciesIngesterService) {
        this.circPoliciesIngesterService = circPoliciesIngesterService;
    }
}
