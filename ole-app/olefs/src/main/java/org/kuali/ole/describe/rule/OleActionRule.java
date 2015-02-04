package org.kuali.ole.describe.rule;

import org.kuali.ole.OLEConstants;
import org.kuali.ole.describe.bo.OleAction;
import org.kuali.rice.krad.maintenance.MaintenanceDocument;
import org.kuali.rice.krad.rules.MaintenanceDocumentRuleBase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * OleActionRule validates maintenance object for Action Maintenance Document
 */
public class OleActionRule extends MaintenanceDocumentRuleBase {


    protected boolean processCustomSaveDocumentBusinessRules(MaintenanceDocument document) {
        boolean isValid = true;
        OleAction oleAction = (OleAction) document.getNewMaintainableObject().getDataObject();
        isValid &= validateActionCode(oleAction);
        return isValid;
    }

    /**
     * This method  validates duplicate action Id and return boolean value.
     *
     * @param oleNewAction
     * @return boolean
     */
    private boolean validateActionCode(OleAction oleNewAction) {

        if (oleNewAction.getActionCode() != null) {
            Map<String, String> criteria = new HashMap<String, String>();
            criteria.put(OLEConstants.OleAction.ACTION_CD, oleNewAction.getActionCode());
            List<OleAction> actionCodeInDatabase = (List<OleAction>) getBoService().findMatching(OleAction.class, criteria);
            if ((actionCodeInDatabase.size() > 0)) {
                for (OleAction actionObj : actionCodeInDatabase) {
                    Integer actionId = actionObj.getActionId();
                    if (null == oleNewAction.getActionId() || (oleNewAction.getActionId().intValue() != actionId.intValue())) {
                        this.putFieldError(OLEConstants.OleAction.ACTION_CODE, "error.duplicate.code");
                        return false;
                    }
                }
            }
        }
        return true;
    }
}