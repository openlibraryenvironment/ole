package org.kuali.ole.describe.rule;

import org.kuali.ole.OLEConstants;
import org.kuali.ole.describe.bo.OleEncodingLevel;
import org.kuali.rice.krad.maintenance.MaintenanceDocument;
import org.kuali.rice.krad.rules.MaintenanceDocumentRuleBase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * OleEncodingLevelRule validates maintenance object for Encoding Level Maintenance Document
 */
public class OleEncodingLevelRule extends MaintenanceDocumentRuleBase {


    protected boolean processCustomSaveDocumentBusinessRules(MaintenanceDocument document) {
        boolean isValid = true;
        OleEncodingLevel oleEncodingLevel = (OleEncodingLevel) document.getNewMaintainableObject().getDataObject();

        isValid &= validateEncodingLevelCode(oleEncodingLevel);
        return isValid;
    }

    /**
     * This method  validates duplicate encodingLevel Id and return boolean value.
     *
     * @param oleEncodingLevel
     * @return boolean
     */
    private boolean validateEncodingLevelCode(OleEncodingLevel oleEncodingLevel) {

        if (oleEncodingLevel.getEncodingLevelCode() != null) {

            Map<String, String> criteria = new HashMap<String, String>();

            criteria.put(OLEConstants.OleEncodingLevel.ENCODING_LEVEL_CD, oleEncodingLevel.getEncodingLevelCode());


            List<OleEncodingLevel> encodingLevelInDatabase = (List<OleEncodingLevel>) getBoService().findMatching(OleEncodingLevel.class, criteria);

            if ((encodingLevelInDatabase.size() > 0)) {
                for (OleEncodingLevel oleEncodingLevelObj : encodingLevelInDatabase) {
                    Integer encodingLevelId = oleEncodingLevelObj.getEncodingLevelId();
                    if (null == oleEncodingLevel.getEncodingLevelId() || oleEncodingLevel.getEncodingLevelId().intValue() != encodingLevelId) {
                        this.putFieldError(OLEConstants.OleEncodingLevel.ENCODING_LEVEL_CODE, "error.duplicate.code");
                        return false;
                    }
                }
            }
        }

        return true;
    }
}
