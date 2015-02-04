package org.kuali.ole.select.rule;

import org.kuali.ole.OLEConstants;
import org.kuali.ole.select.bo.OleLicenseRequestType;
import org.kuali.rice.krad.maintenance.MaintenanceDocument;
import org.kuali.rice.krad.rules.MaintenanceDocumentRuleBase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 3/22/13
 * Time: 12:28 PM
 * To change this template use File | Settings | File Templates.
 */
public class OleLicenseRequestTypeRule extends MaintenanceDocumentRuleBase {
    protected boolean processCustomSaveDocumentBusinessRules(MaintenanceDocument document) {
        boolean isValid = true;
        OleLicenseRequestType oleLicenseRequestType = (OleLicenseRequestType) document.getNewMaintainableObject().getDataObject();

        isValid &= validateOleLicenseRequestTypeName(oleLicenseRequestType);
        return isValid;
    }

    /**
     * This method  validates duplicate License Request Type Id and return boolean value.
     *
     * @param oleLicenseRequestType
     * @return boolean
     */
    private boolean validateOleLicenseRequestTypeName(OleLicenseRequestType oleLicenseRequestType) {

        if (oleLicenseRequestType.getName() != null) {
            Map<String, String> criteria = new HashMap<String, String>();
            criteria.put(OLEConstants.NAME, oleLicenseRequestType.getName());
            List<OleLicenseRequestType> savedOleLicenseRequestType = (List<OleLicenseRequestType>) getBoService().findMatching(OleLicenseRequestType.class, criteria);
            if ((savedOleLicenseRequestType.size() > 0)) {
                for (OleLicenseRequestType licenseRequestType : savedOleLicenseRequestType) {
                    String licenseTypeId = licenseRequestType.getId();
                    if (null == oleLicenseRequestType.getId() || (!oleLicenseRequestType.getId().equalsIgnoreCase(licenseTypeId))) {
                        this.putFieldError(OLEConstants.NAME_FIELD, OLEConstants.ERROR_DUPLICATE_CODE);
                        return false;
                    }
                }
            }
        }
        return true;
    }

}
