package org.kuali.ole.select.rule;

import org.kuali.ole.OLEConstants;
import org.kuali.ole.select.bo.OLEPlatformStatus;
import org.kuali.rice.krad.maintenance.MaintenanceDocument;
import org.kuali.rice.krad.rules.MaintenanceDocumentRuleBase;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krad.service.KRADServiceLocatorWeb;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by chenchulakshmig on 9/16/14.
 * OLEPlatformStatusRule validates maintenance object for Platform Status Maintenance Document
 */
public class OLEPlatformStatusRule extends MaintenanceDocumentRuleBase {

    /**
     * @param document
     * @return boolean
     */
    protected boolean processCustomSaveDocumentBusinessRules(MaintenanceDocument document) {
        boolean isValid = true;
        OLEPlatformStatus olePlatformStatus = (OLEPlatformStatus) document.getNewMaintainableObject().getDataObject();
        isValid &= validatePlatformStatusName(olePlatformStatus);
        return isValid;
    }

    /**
     * * This method  validates duplicate platform status name and return boolean value.
     *
     * @param olePlatformStatus
     * @return boolean
     */
    private boolean validatePlatformStatusName(OLEPlatformStatus olePlatformStatus) {
        if (olePlatformStatus.getPlatformStatusName() != null) {
            Map<String, String> criteria = new HashMap<String, String>();
            criteria.put(OLEConstants.PLATFORM_STATUS_NAME, olePlatformStatus.getPlatformStatusName());
            List<OLEPlatformStatus> authenticationMethodInDatabase = (List<OLEPlatformStatus>) KRADServiceLocator.getBusinessObjectService().findMatching(OLEPlatformStatus.class, criteria);
            if ((authenticationMethodInDatabase.size() > 0)) {
                for (OLEPlatformStatus authenticationMethodObj : authenticationMethodInDatabase) {
                    String platformStatusId = authenticationMethodObj.getPlatformStatusId();
                    if (null == olePlatformStatus.getPlatformStatusId() || (!olePlatformStatus.getPlatformStatusId().equals(platformStatusId))) {
                        this.putFieldError(OLEConstants.PLATFORM_STATUS_NAM_FIELD, OLEConstants.DUPLICATE);
                        return false;
                    }
                }
            }
        }
        return true;
    }
}
