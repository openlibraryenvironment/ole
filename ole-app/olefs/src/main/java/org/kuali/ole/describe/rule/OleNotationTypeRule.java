package org.kuali.ole.describe.rule;

import org.kuali.ole.OLEConstants;
import org.kuali.ole.describe.bo.OleNotationType;
import org.kuali.rice.krad.maintenance.MaintenanceDocument;
import org.kuali.rice.krad.rules.MaintenanceDocumentRuleBase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * OleNotationTypeRule validates maintenance object for Notation Type Maintenance Document
 */
public class OleNotationTypeRule extends MaintenanceDocumentRuleBase {


    protected boolean processCustomSaveDocumentBusinessRules(MaintenanceDocument document) {
        boolean isValid = true;
        OleNotationType oleNotationType = (OleNotationType) document.getNewMaintainableObject().getDataObject();

        isValid &= validateNotationTypeCode(oleNotationType);
        return isValid;
    }

    /**
     * This method  validates duplicate notationType Id and return boolean value.
     *
     * @param oleNotationType
     * @return boolean
     */
    private boolean validateNotationTypeCode(OleNotationType oleNotationType) {

        if (oleNotationType.getNotationTypeCode() != null) {

            Map<String, String> criteria = new HashMap<String, String>();

            criteria.put(OLEConstants.OleNotationType.NOTATION_TYPE_CD, oleNotationType.getNotationTypeCode());

            List<OleNotationType> notationTypeCodeInDatabase = (List<OleNotationType>) getBoService().findMatching(OleNotationType.class, criteria);

            if ((notationTypeCodeInDatabase.size() > 0)) {
                for (OleNotationType notationTypeObj : notationTypeCodeInDatabase) {
                    String notationTypeId = notationTypeObj.getNotationTypeId();
                    if (null == oleNotationType.getNotationTypeId() ||
                            !(oleNotationType.getNotationTypeId().equalsIgnoreCase(notationTypeId))) {
                        this.putFieldError(OLEConstants.OleNotationType.NOTATION_TYPE_CODE, "error.duplicate.code");
                        return false;
                    }
                }
            }
            return true;
        }
        return false;
    }
}