package org.kuali.ole.describe.rule;

import org.kuali.ole.OLEConstants;
import org.kuali.ole.describe.bo.OleCompleteness;
import org.kuali.rice.krad.maintenance.MaintenanceDocument;
import org.kuali.rice.krad.rules.MaintenanceDocumentRuleBase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * OleCompletenessRule validates maintenance object for Completeness Maintenance Document
 */
public class OleCompletenessRule extends MaintenanceDocumentRuleBase {


    protected boolean processCustomSaveDocumentBusinessRules(MaintenanceDocument document) {
        boolean isValid = true;
        OleCompleteness oleCompleteness = (OleCompleteness) document.getNewMaintainableObject().getDataObject();

        isValid &= validateCompletenessCode(oleCompleteness);
        return isValid;
    }

    /**
     * This method  validates duplicate completeness Id and return boolean value.
     *
     * @param oleCompleteness
     * @return boolean
     */
    private boolean validateCompletenessCode(OleCompleteness oleCompleteness) {

        if (oleCompleteness.getCompletenessCode() != null) {
            Map<String, String> criteria = new HashMap<String, String>();
            criteria.put(OLEConstants.OleCompleteness.COMPLETENESS_CD, oleCompleteness.getCompletenessCode());
            List<OleCompleteness> completenessCodeInDatabase = (List<OleCompleteness>) getBoService().findMatching(OleCompleteness.class, criteria);
            if ((completenessCodeInDatabase.size() > 0)) {
                for (OleCompleteness completenessObj : completenessCodeInDatabase) {
                    Integer completenessId = completenessObj.getCompletenessId();
                    if (null == oleCompleteness.getCompletenessId() || (oleCompleteness.getCompletenessId().intValue() != completenessId.intValue())) {
                        this.putFieldError(OLEConstants.OleCompleteness.COMPLETENESS_CODE, "error.duplicate.code");
                        return false;
                    }
                }
            }
        }
        return true;
    }
}

