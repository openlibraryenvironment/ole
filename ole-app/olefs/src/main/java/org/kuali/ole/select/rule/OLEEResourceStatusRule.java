package org.kuali.ole.select.rule;

import org.kuali.ole.OLEConstants;
import org.kuali.ole.select.bo.OLEEResourceStatus;
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
public class OLEEResourceStatusRule extends MaintenanceDocumentRuleBase {

    protected boolean processCustomSaveDocumentBusinessRules(MaintenanceDocument document) {
        boolean isValid = true;
        OLEEResourceStatus statusMethod = (OLEEResourceStatus) document.getNewMaintainableObject().getDataObject();
        isValid &= validateAccessMethodCode(statusMethod);
        return isValid;
    }

    /**
     * This method  validates duplicate authenticationMethod Id and return boolean value.
     *
     * @param statusMethod
     * @return boolean
     */
    private boolean validateAccessMethodCode(OLEEResourceStatus statusMethod) {
        if (statusMethod.getOleEResourceStatusName() != null) {
            Map<String, String> criteria = new HashMap<String, String>();
            criteria.put(OLEConstants.OleEResourceStatus.E_RES_STAT_TYPE_NAME, statusMethod.getOleEResourceStatusName());
            List<OLEEResourceStatus> authenticationMethodInDatabase = (List<OLEEResourceStatus>) getBoService().findMatching(OLEEResourceStatus.class, criteria);
            if ((authenticationMethodInDatabase.size() > 0)) {
                for (OLEEResourceStatus authenticationMethodObj : authenticationMethodInDatabase) {
                    String oleEResourceStatusId = authenticationMethodObj.getOleEResourceStatusId();
                    if (null == statusMethod.getOleEResourceStatusId() || (!statusMethod.getOleEResourceStatusId().equalsIgnoreCase(oleEResourceStatusId))) {
                        GlobalVariables.getMessageMap().putError(OLEConstants.OleEResourceStatus.E_RES_STAT_TYPE_NAME_FIELD, OLEConstants.ERROR_DUPLICATE_CODE);
                        return false;
                    }
                }
            }
        }
        return true;
    }
}
