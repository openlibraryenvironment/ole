package org.kuali.ole.describe.rule;

import org.kuali.ole.OLEConstants;
import org.kuali.ole.describe.bo.OleSourceOfTerm;
import org.kuali.rice.krad.maintenance.MaintenanceDocument;
import org.kuali.rice.krad.rules.MaintenanceDocumentRuleBase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * OleSourceOfTermRule validates maintenance object for Source Of Term Maintenance Document
 */
public class OleSourceOfTermRule extends MaintenanceDocumentRuleBase {


    protected boolean processCustomSaveDocumentBusinessRules(MaintenanceDocument document) {
        boolean isValid = true;
        OleSourceOfTerm oleSourceOfTerm = (OleSourceOfTerm) document.getNewMaintainableObject().getDataObject();

        isValid &= validateSourceOfTermCode(oleSourceOfTerm);
        return isValid;
    }

    /**
     * This method  validates duplicate sourceOfTerm Id and return boolean value.
     *
     * @param oleSourceOfTerm
     * @return boolean
     */
    private boolean validateSourceOfTermCode(OleSourceOfTerm oleSourceOfTerm) {

        if (oleSourceOfTerm.getSourceOfTermCode() != null) {

            Map<String, String> criteria = new HashMap<String, String>();

            criteria.put(OLEConstants.OleSourceOfTerm.SOURCE_OF_TERM_CD, oleSourceOfTerm.getSourceOfTermCode());


            List<OleSourceOfTerm> sourceOfTermCodeInDatabase = (List<OleSourceOfTerm>) getBoService().findMatching(OleSourceOfTerm.class, criteria);

            if ((sourceOfTermCodeInDatabase.size() > 0)) {
                for (OleSourceOfTerm oleSourceOfTermObj : sourceOfTermCodeInDatabase) {
                    String sourceOfTermId = oleSourceOfTermObj.getSourceOfTermId();
                    if (null == oleSourceOfTerm.getSourceOfTermId() || !(sourceOfTermId.equalsIgnoreCase(oleSourceOfTerm.getSourceOfTermId()))) {
                        this.putFieldError(OLEConstants.OleSourceOfTerm.SOURCE_OF_TERM_CODE, "error.duplicate.code");
                        return false;
                    }
                }
            }
        }

        return true;
    }
}