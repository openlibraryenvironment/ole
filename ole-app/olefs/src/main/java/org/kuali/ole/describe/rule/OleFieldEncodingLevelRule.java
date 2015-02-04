package org.kuali.ole.describe.rule;

import org.kuali.ole.OLEConstants;
import org.kuali.ole.describe.bo.OleFieldEncodingLevel;
import org.kuali.rice.krad.maintenance.MaintenanceDocument;
import org.kuali.rice.krad.rules.MaintenanceDocumentRuleBase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * OleFieldEncodingLevelRule validates maintenance object for Field Encoding Level Maintenance Document
 */
public class OleFieldEncodingLevelRule extends MaintenanceDocumentRuleBase {


    protected boolean processCustomSaveDocumentBusinessRules(MaintenanceDocument document) {
        boolean isValid = true;
        OleFieldEncodingLevel oleFieldEncodingLevel = (OleFieldEncodingLevel) document.getNewMaintainableObject().getDataObject();

        isValid &= validateFieldEncodingLevelCode(oleFieldEncodingLevel);
        return isValid;
    }

    /**
     * This method  validates duplicate fieldEncodingLevel Id and return boolean value.
     *
     * @param oleFieldEncodingLevel
     * @return boolean
     */
    private boolean validateFieldEncodingLevelCode(OleFieldEncodingLevel oleFieldEncodingLevel) {

        if (oleFieldEncodingLevel.getFieldEncodingLevelCode() != null) {

            Map<String, String> criteria = new HashMap<String, String>();
            criteria.put(OLEConstants.OleFieldEncodingLevel.FIELD_ENCODING_LEVEL_CD, oleFieldEncodingLevel.getFieldEncodingLevelCode());


            List<OleFieldEncodingLevel> fieldEncodingLevelInDatabase = (List<OleFieldEncodingLevel>) getBoService().findMatching(OleFieldEncodingLevel.class, criteria);

            if ((fieldEncodingLevelInDatabase.size() > 0)) {
                for (OleFieldEncodingLevel oleFieldEncodingLevelObj : fieldEncodingLevelInDatabase) {
                    Integer fieldEncodingLevelId = oleFieldEncodingLevelObj.getFieldEncodingLevelId();
                    if (null == oleFieldEncodingLevel.getFieldEncodingLevelId() || oleFieldEncodingLevel.getFieldEncodingLevelId().intValue() != fieldEncodingLevelId) {
                        this.putFieldError(OLEConstants.OleFieldEncodingLevel.FIELD_ENCODING_LEVEL_CODE, "error.duplicate.code");
                        return false;
                    }
                }
            }
        }

        return true;
    }
}
