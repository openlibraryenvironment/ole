package org.kuali.ole.select.rule;

import org.kuali.ole.OLEConstants;
import org.kuali.ole.select.bo.OLEAccessLocation;
import org.kuali.rice.krad.maintenance.MaintenanceDocument;
import org.kuali.rice.krad.rules.MaintenanceDocumentRuleBase;
import org.kuali.rice.krad.util.GlobalVariables;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: chenchulakshmig
 * Date: 6/21/13
 * Time: 3:58 PM
 * To change this template use File | Settings | File Templates.
 */
public class OLEAccessLocationRule extends MaintenanceDocumentRuleBase {

    protected boolean processCustomSaveDocumentBusinessRules(MaintenanceDocument document) {
        boolean isValid = true;
        OLEAccessLocation accessMethod = (OLEAccessLocation) document.getNewMaintainableObject().getDataObject();
        isValid &= validateAccessMethodCode(accessMethod);
        return isValid;
    }

    /**
     * This method  validates duplicate authenticationMethod Id and return boolean value.
     *
     * @param accessMethod
     * @return boolean
     */
    private boolean validateAccessMethodCode(OLEAccessLocation accessMethod) {
        if (accessMethod.getOleAccessLocationName() != null) {
            Map<String, String> criteria = new HashMap<String, String>();
            criteria.put(OLEConstants.OleAccessLocation.ACCESS_TYPE_NAME, accessMethod.getOleAccessLocationName());
            List<OLEAccessLocation> authenticationMethodInDatabase = (List<OLEAccessLocation>) getBoService().findMatching(OLEAccessLocation.class, criteria);
            if ((authenticationMethodInDatabase.size() > 0)) {
                for (OLEAccessLocation authenticationMethodObj : authenticationMethodInDatabase) {
                    String accessLocationId = authenticationMethodObj.getOleAccessLocationId();
                    if (null == accessMethod.getOleAccessLocationId() || (!accessMethod.getOleAccessLocationId().equalsIgnoreCase(accessLocationId))) {
                        GlobalVariables.getMessageMap().putError(OLEConstants.OleAccessLocation.ACCESS_TYPE_NAME_FIELD, OLEConstants.ERROR_DUPLICATE_CODE);
                        return false;
                    }
                }
            }
        }
        return true;
    }
}
