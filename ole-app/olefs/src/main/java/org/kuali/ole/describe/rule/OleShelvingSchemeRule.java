package org.kuali.ole.describe.rule;

import org.kuali.ole.OLEConstants;
import org.kuali.ole.describe.bo.OleShelvingScheme;
import org.kuali.rice.krad.maintenance.MaintenanceDocument;
import org.kuali.rice.krad.rules.MaintenanceDocumentRuleBase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * OleShelvingSchemeRule validates maintenance object for Shelving Scheme Maintenance Document
 */
public class OleShelvingSchemeRule extends MaintenanceDocumentRuleBase {


    protected boolean processCustomSaveDocumentBusinessRules(MaintenanceDocument document) {
        boolean isValid = true;
        OleShelvingScheme oleShelvingScheme = (OleShelvingScheme) document.getNewMaintainableObject().getDataObject();

        isValid &= validateShelvingSchemeCode(oleShelvingScheme);
        return isValid;
    }

    /**
     * This method  validates duplicate shelvingScheme Id and return boolean value.
     *
     * @param oleShelvingScheme
     * @return boolean
     */
    private boolean validateShelvingSchemeCode(OleShelvingScheme oleShelvingScheme) {

        if (oleShelvingScheme.getShelvingSchemeCode() != null) {
            Map<String, String> criteria = new HashMap<String, String>();
            criteria.put(OLEConstants.OleShelvingScheme.SHELVING_SCHEME_CD, oleShelvingScheme.getShelvingSchemeCode());
            List<OleShelvingScheme> shelvingSchemeCodeInDatabase = (List<OleShelvingScheme>) getBoService().findMatching(OleShelvingScheme.class, criteria);
            if ((shelvingSchemeCodeInDatabase.size() > 0)) {
                for (OleShelvingScheme shelvingSchemeObj : shelvingSchemeCodeInDatabase) {
                    Integer shelvingSchemeId = shelvingSchemeObj.getShelvingSchemeId();
                    if (null == oleShelvingScheme.getShelvingSchemeId() || (oleShelvingScheme.getShelvingSchemeId().intValue() != shelvingSchemeId.intValue())) {
                        this.putFieldError(OLEConstants.OleShelvingScheme.SHELVING_SCHEME_CODE, "error.duplicate.code");
                        return false;
                    }
                }
            }
        }
        return true;
    }
}