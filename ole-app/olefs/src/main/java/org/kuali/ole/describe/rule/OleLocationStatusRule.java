package org.kuali.ole.describe.rule;

import org.kuali.ole.describe.bo.OleLocationStatus;
import org.kuali.rice.krad.maintenance.MaintenanceDocument;
import org.kuali.rice.krad.rules.MaintenanceDocumentRuleBase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * OleLocationStatusRule validates maintenance object for Location Status Maintenance Document
 */
public class OleLocationStatusRule extends MaintenanceDocumentRuleBase {

    /**
     * @param document
     * @return
     */

    protected boolean processCustomSaveDocumentBusinessRules(MaintenanceDocument document) {
        boolean isValid = true;
        OleLocationStatus oleLocationStatus = (OleLocationStatus) document.getNewMaintainableObject().getDataObject();

        isValid &= validateLocationStatusCode(oleLocationStatus);
        return isValid;
    }

    /**
     * This method  validates duplicate locationStatus Id and return boolean value.
     *
     * @param oleLocationStatus
     * @return boolean
     */
    private boolean validateLocationStatusCode(OleLocationStatus oleLocationStatus) {

        if (oleLocationStatus.getLocationStatusCode() != null) {

            Map<String, String> criteria = new HashMap<String, String>();

            //    criteria.put(OLEConstants.OleLocationStatus.LOCATION_STATUS_CD, oleLocationStatus.getLocationStatusCode());

            List<OleLocationStatus> locationStatusCodeInDatabase = (List<OleLocationStatus>) getBoService().findMatching(OleLocationStatus.class, criteria);

            if ((locationStatusCodeInDatabase.size() > 0)) {

                for (OleLocationStatus locationStatusObj : locationStatusCodeInDatabase) {
                    String locationStatusId = locationStatusObj.getLocationStatusId();
                    if (null == oleLocationStatus.getLocationStatusId() ||
                            !(oleLocationStatus.getLocationStatusId().equalsIgnoreCase(locationStatusId))) {
                        //         this.putFieldError(OLEConstants.OleLocationStatus.LOCATION_STATUS_CODE, "error.duplicate.code");
                        return false;
                    }
                }
            }
            return true;
        }
        return false;
    }
}