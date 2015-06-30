package org.kuali.ole.select.service;

import org.kuali.ole.select.bo.OLEAccessActivationConfiguration;
import org.kuali.ole.select.bo.OLEAccessActivationWorkFlow;
import org.kuali.rice.kim.api.identity.principal.Principal;

import java.util.List;

/**
 * Created by maheswarang on 5/13/15.
 */
public interface OLEAccessActivationService {

    public OLEAccessActivationConfiguration setRoleAndPersonName(OLEAccessActivationConfiguration oleAccessActivationConfiguration);
    public boolean validateAccessActivationWorkFlow(List<OLEAccessActivationWorkFlow> accessActivationWorkFlowList, OLEAccessActivationWorkFlow accessActivationWorkFlow, String selector);
    public List<Principal> getPrincipals(OLEAccessActivationWorkFlow oleAccessActivationWorkFlow);
}
