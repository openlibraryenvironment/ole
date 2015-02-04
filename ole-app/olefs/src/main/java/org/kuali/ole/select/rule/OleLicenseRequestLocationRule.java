package org.kuali.ole.select.rule;

import org.kuali.ole.OLEConstants;
import org.kuali.ole.select.bo.OleLicenseRequestLocation;
import org.kuali.rice.krad.maintenance.MaintenanceDocument;
import org.kuali.rice.krad.rules.MaintenanceDocumentRuleBase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 3/22/13
 * Time: 12:36 PM
 * To change this template use File | Settings | File Templates.
 */
public class OleLicenseRequestLocationRule extends MaintenanceDocumentRuleBase {
    protected boolean processCustomSaveDocumentBusinessRules(MaintenanceDocument document) {
        boolean isValid = true;
        OleLicenseRequestLocation oleLicenseRequestLocation = (OleLicenseRequestLocation) document.getNewMaintainableObject().getDataObject();

        isValid &= validateOleLicenseRequestLocation(oleLicenseRequestLocation);
        return isValid;
    }

    /**
     * This method  validates duplicate Agreement Type Id and return boolean value.
     *
     * @param oleLicenseRequestLocation
     * @return boolean
     */
    private boolean validateOleLicenseRequestLocation(OleLicenseRequestLocation oleLicenseRequestLocation) {

        if (oleLicenseRequestLocation.getName() != null) {
            Map<String, String> criteria = new HashMap<String, String>();
            criteria.put(OLEConstants.NAME, oleLicenseRequestLocation.getName());
            List<OleLicenseRequestLocation> savedOleLicenseRequestLocation = (List<OleLicenseRequestLocation>) getBoService().findMatching(OleLicenseRequestLocation.class, criteria);
            if ((savedOleLicenseRequestLocation.size() > 0)) {
                for (OleLicenseRequestLocation location : savedOleLicenseRequestLocation) {
                    String licenseId = location.getId();
                    if (null == oleLicenseRequestLocation.getId() || (!oleLicenseRequestLocation.getId().equalsIgnoreCase(licenseId))) {
                        this.putFieldError(OLEConstants.NAME_FIELD, OLEConstants.ERROR_DUPLICATE_CODE);
                        return false;
                    }
                }
            }
        }
        return true;
    }
}
