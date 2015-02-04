package org.kuali.ole.describe.rule;

import org.kuali.ole.OLEConstants;
import org.kuali.ole.describe.bo.OleStatisticalSearchingCodes;
import org.kuali.rice.krad.maintenance.MaintenanceDocument;
import org.kuali.rice.krad.rules.MaintenanceDocumentRuleBase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * OleStatisticalSearchingCodesRule validates maintenance object for Statistical Searching Codes Maintenance Document
 */
public class OleStatisticalSearchingCodesRule extends MaintenanceDocumentRuleBase {

    protected boolean processCustomSaveDocumentBusinessRules(MaintenanceDocument document) {
        boolean isValid = true;
        OleStatisticalSearchingCodes oleStatisticalSearchingCodes = (OleStatisticalSearchingCodes) document.getNewMaintainableObject().getDataObject();

        isValid &= validateStatisticalSearchingCode(oleStatisticalSearchingCodes);
        return isValid;
    }

    /**
     * This method  validates duplicate statisticalSearchingCode Id and return boolean value.
     *
     * @param oleStatisticalSearchingCodes
     * @return boolean
     */
    private boolean validateStatisticalSearchingCode(OleStatisticalSearchingCodes oleStatisticalSearchingCodes) {

        if (oleStatisticalSearchingCodes.getStatisticalSearchingCode() != null) {

            Map<String, String> criteria = new HashMap<String, String>();

            criteria.put(OLEConstants.OleStatisticalSearchingCodes.STATISTICAL_SEARCHING_CD, oleStatisticalSearchingCodes.getStatisticalSearchingCode());

            List<OleStatisticalSearchingCodes> statisticalSearchingCodeInDatabase = (List<OleStatisticalSearchingCodes>) getBoService().findMatching(OleStatisticalSearchingCodes.class, criteria);

            if ((statisticalSearchingCodeInDatabase.size() > 0)) {

                for (OleStatisticalSearchingCodes oleStatisticalSearchingCodesObj : statisticalSearchingCodeInDatabase) {
                    Integer statisticalSearchingCodeId = oleStatisticalSearchingCodesObj.getStatisticalSearchingCodeId();
                    if (null == oleStatisticalSearchingCodes.getStatisticalSearchingCodeId() ||
                            statisticalSearchingCodeId.intValue() != oleStatisticalSearchingCodes.getStatisticalSearchingCodeId().intValue()) {
                        this.putFieldError(OLEConstants.OleStatisticalSearchingCodes.STATISTICAL_SEARCHING_CODE, "error.duplicate.code");
                        return false;
                    }
                }

            }
            return true;
        }
        return false;
    }
}
