package org.kuali.ole.select.rule;

import org.kuali.ole.OLEConstants;
import org.kuali.ole.select.bo.OLEAuthenticationType;
import org.kuali.rice.krad.maintenance.MaintenanceDocument;
import org.kuali.rice.krad.rules.MaintenanceDocumentRuleBase;
import org.kuali.rice.krad.util.GlobalVariables;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: chenchulakshmig
 * Date: 6/20/13
 * Time: 7:38 PM
 * To change this template use File | Settings | File Templates.
 */
public class OLEAuthenticationTypeRule extends MaintenanceDocumentRuleBase {

    protected boolean processCustomSaveDocumentBusinessRules(MaintenanceDocument document) {
        boolean isValid = true;
        OLEAuthenticationType authenticationMethod = (OLEAuthenticationType) document.getNewMaintainableObject().getDataObject();
        isValid &= validateAccessMethodCode(authenticationMethod);
        return isValid;
    }

    /**
     * This method  validates duplicate authenticationMethod Id and return boolean value.
     *
     * @param authenticationMethod
     * @return boolean
     */
    private boolean validateAccessMethodCode(OLEAuthenticationType authenticationMethod) {
        if (authenticationMethod.getOleAuthenticationTypeName() != null) {
            Map<String, String> criteria = new HashMap<String, String>();
            criteria.put(OLEConstants.OleAuthenticationType.AUTH_TYPE_NAME, authenticationMethod.getOleAuthenticationTypeName());
            List<OLEAuthenticationType> authenticationMethodInDatabase = (List<OLEAuthenticationType>) getBoService().findMatching(OLEAuthenticationType.class, criteria);
            if ((authenticationMethodInDatabase.size() > 0)) {
                for (OLEAuthenticationType authenticationMethodObj : authenticationMethodInDatabase) {
                    String oleAuthenticationTypeId = authenticationMethodObj.getOleAuthenticationTypeId();
                    if (null == authenticationMethod.getOleAuthenticationTypeId() || (!authenticationMethod.getOleAuthenticationTypeId().equalsIgnoreCase(oleAuthenticationTypeId))) {
                        GlobalVariables.getMessageMap().putError(OLEConstants.OleAuthenticationType.AUTH_TYPE_NAME_FIELD, OLEConstants.ERROR_DUPLICATE_CODE);
                        return false;
                    }
                }
            }
        }
        return true;
    }
}
