package org.kuali.ole.select.rule;

import org.kuali.ole.OLEConstants;
import org.kuali.ole.select.bo.OLEPlatformAdminUrlType;
import org.kuali.rice.krad.maintenance.MaintenanceDocument;
import org.kuali.rice.krad.rules.MaintenanceDocumentRuleBase;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krad.service.KRADServiceLocatorWeb;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by chenchulakshmig on 9/16/14.
 * OLEPlatformAdminUrlTypeRule validates maintenance object for Platform Admin Url Type Maintenance Document
 */
public class OLEPlatformAdminUrlTypeRule extends MaintenanceDocumentRuleBase {

    /**
     * @param document
     * @return boolean
     */
    protected boolean processCustomSaveDocumentBusinessRules(MaintenanceDocument document) {
        boolean isValid = true;
        OLEPlatformAdminUrlType olePlatformAdminUrlType = (OLEPlatformAdminUrlType) document.getNewMaintainableObject().getDataObject();
        isValid &= validatePlatformAdminUrlTypeName(olePlatformAdminUrlType);
        return isValid;
    }

    /**
     * * This method  validates duplicate platform admin url type name and return boolean value.
     *
     * @param platformAdminUrlType
     * @return boolean
     */
    private boolean validatePlatformAdminUrlTypeName(OLEPlatformAdminUrlType platformAdminUrlType) {
        if (platformAdminUrlType.getPlatformAdminUrlTypeName() != null) {
            Map<String, String> criteria = new HashMap<String, String>();
            criteria.put(OLEConstants.PLATFORM_ADMIN_URL_TYPE_NAME, platformAdminUrlType.getPlatformAdminUrlTypeName());
            List<OLEPlatformAdminUrlType> authenticationMethodInDatabase = (List<OLEPlatformAdminUrlType>) KRADServiceLocator.getBusinessObjectService().findMatching(OLEPlatformAdminUrlType.class, criteria);
            if ((authenticationMethodInDatabase.size() > 0)) {
                for (OLEPlatformAdminUrlType authenticationMethodObj : authenticationMethodInDatabase) {
                    String platformStatusId = authenticationMethodObj.getPlatformAdminUrlTypeId();
                    if (null == platformAdminUrlType.getPlatformAdminUrlTypeId() || (!platformAdminUrlType.getPlatformAdminUrlTypeId().equals(platformStatusId))) {
                        this.putFieldError(OLEConstants.PLATFORM_ADMIN_URL_TYPE_NAME_FIELD, OLEConstants.DUPLICATE);
                        return false;
                    }
                }
            }
        }
        return true;
    }
}
