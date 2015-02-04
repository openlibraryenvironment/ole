package org.kuali.ole.describe.rule;

import org.kuali.ole.OLEConstants;
import org.kuali.ole.describe.bo.OleCountryCodes;
import org.kuali.rice.krad.maintenance.MaintenanceDocument;
import org.kuali.rice.krad.rules.MaintenanceDocumentRuleBase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * OleCountryCodesRule validates maintenance object for Country Codes Maintenance Document
 */
public class OleCountryCodesRule extends MaintenanceDocumentRuleBase {


    protected boolean processCustomSaveDocumentBusinessRules(MaintenanceDocument document) {
        boolean isValid = true;
        OleCountryCodes oleCountryCodes = (OleCountryCodes) document.getNewMaintainableObject().getDataObject();

        isValid &= validateCountryCode(oleCountryCodes);
        return isValid;
    }

    /**
     * This method  validates duplicate countryCode Id and return boolean value.
     *
     * @param oleCountryCodes
     * @return boolean
     */
    private boolean validateCountryCode(OleCountryCodes oleCountryCodes) {
        if (oleCountryCodes.getCountryCode() != null) {
            Map<String, String> criteria = new HashMap<String, String>();
            criteria.put(OLEConstants.OleCountryCodes.COUNTRY_CD, oleCountryCodes.getCountryCode());
            List<OleCountryCodes> countryCodeInDatabase = (List<OleCountryCodes>) getBoService().findMatching(OleCountryCodes.class, criteria);
            if ((countryCodeInDatabase.size() > 0)) {
                for (OleCountryCodes countryCodeObj : countryCodeInDatabase) {
                    Integer countryCodeId = countryCodeObj.getCountryCodeId();
                    if (null == oleCountryCodes.getCountryCodeId() || (oleCountryCodes.getCountryCodeId().intValue() != countryCodeId.intValue())) {
                        this.putFieldError(OLEConstants.OleCountryCodes.COUNTRY_CODE, "error.duplicate.code");
                        return false;
                    }
                }
            }
        }
        return true;
    }
}
