package org.kuali.ole.describe.rule;

import org.kuali.ole.OLEConstants;
import org.kuali.ole.describe.bo.OlePrivacy;
import org.kuali.rice.krad.maintenance.MaintenanceDocument;
import org.kuali.rice.krad.rules.MaintenanceDocumentRuleBase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * OlePrivacyRule validates maintenance object for Privacy Maintenance Document
 */
public class OlePrivacyRule extends MaintenanceDocumentRuleBase {


    protected boolean processCustomSaveDocumentBusinessRules(MaintenanceDocument document) {
        boolean isValid = true;
        OlePrivacy OlePrivacy = (OlePrivacy) document.getNewMaintainableObject().getDataObject();

        isValid &= validatePrivacyCode(OlePrivacy);
        return isValid;
    }

    /**
     * This method  validates duplicate privacy Id and return boolean value.
     *
     * @param olePrivacy
     * @return boolean
     */
    private boolean validatePrivacyCode(OlePrivacy olePrivacy) {

        if (olePrivacy.getPrivacyCode() != null) {

            Map<String, String> criteria = new HashMap<String, String>();

            criteria.put(OLEConstants.OlePrivacy.PRIVACY_CD, olePrivacy.getPrivacyCode());

            List<OlePrivacy> privacyCodeInDatabase = (List<OlePrivacy>) getBoService().findMatching(OlePrivacy.class, criteria);

            if ((privacyCodeInDatabase.size() > 0)) {

                for (OlePrivacy privacyCodeObj : privacyCodeInDatabase) {
                    String privacyId = privacyCodeObj.getPrivacyId();
                    if (null == olePrivacy.getPrivacyId() ||
                            !(olePrivacy.getPrivacyId().equalsIgnoreCase(privacyId))) {
                        this.putFieldError(OLEConstants.OlePrivacy.PRIVACY_CODE, "error.duplicate.code");
                        return false;
                    }
                }
            }
            return true;
        }
        return false;
    }
}