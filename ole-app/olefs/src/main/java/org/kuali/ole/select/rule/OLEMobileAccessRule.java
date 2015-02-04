package org.kuali.ole.select.rule;

import org.kuali.ole.OLEConstants;
import org.kuali.ole.select.bo.OLEMobileAccess;
import org.kuali.rice.krad.maintenance.MaintenanceDocument;
import org.kuali.rice.krad.rules.MaintenanceDocumentRuleBase;
import org.kuali.rice.krad.service.KRADServiceLocator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by chenchulakshmig on 11/11/14.
 */
public class OLEMobileAccessRule extends MaintenanceDocumentRuleBase {

    protected boolean processCustomSaveDocumentBusinessRules(MaintenanceDocument document) {
        boolean isValid = true;
        OLEMobileAccess mobileAccess = (OLEMobileAccess) document.getNewMaintainableObject().getDataObject();
        isValid &= validateMobileAccessName(mobileAccess);
        return isValid;
    }

    /**
     * This method  validates duplicate mobileAccessId and return boolean value.
     *
     * @param mobileAccess
     * @return boolean
     */
    private boolean validateMobileAccessName(OLEMobileAccess mobileAccess) {
        if (mobileAccess.getMobileAccessName() != null) {
            Map<String, String> criteria = new HashMap<String, String>();
            criteria.put(OLEConstants.OLEMobileAccess.MOB_ACC_NAME, mobileAccess.getMobileAccessName());
            List<OLEMobileAccess> oleMobileAccessList = (List<OLEMobileAccess>) KRADServiceLocator.getBusinessObjectService().findMatching(OLEMobileAccess.class, criteria);
            if ((oleMobileAccessList.size() > 0)) {
                for (OLEMobileAccess oleMobileAccess : oleMobileAccessList) {
                    String mobileAccessId = oleMobileAccess.getMobileAccessId();
                    if (null == mobileAccess.getMobileAccessId() || (!mobileAccess.getMobileAccessId().equalsIgnoreCase(mobileAccessId))) {
                        this.putFieldError(OLEConstants.OLEMobileAccess.MOB_ACC_NAME_FIELD, OLEConstants.DUPLICATE);
                        return false;
                    }
                }
            }
        }
        return true;
    }

}
