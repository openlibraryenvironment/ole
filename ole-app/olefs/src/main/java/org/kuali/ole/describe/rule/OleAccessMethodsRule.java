package org.kuali.ole.describe.rule;


import org.kuali.ole.OLEConstants;
import org.kuali.ole.describe.bo.OleAccessMethod;
import org.kuali.rice.krad.maintenance.MaintenanceDocument;
import org.kuali.rice.krad.rules.MaintenanceDocumentRuleBase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * OleAccessMethodsRule validates maintenance object for Access Method Maintenance Document
 */
public class OleAccessMethodsRule extends MaintenanceDocumentRuleBase {

    /**
     * @param document
     * @return boolean
     */

    protected boolean processCustomSaveDocumentBusinessRules(MaintenanceDocument document) {
        boolean isValid = true;
        OleAccessMethod accessMethod = (OleAccessMethod) document.getNewMaintainableObject().getDataObject();
        isValid &= validateAccessMethodCode(accessMethod);
        return isValid;
    }

    /**
     * This method  validates duplicate accessMethod Id and return boolean value.
     *
     * @param accessMethod
     * @return boolean
     */
    private boolean validateAccessMethodCode(OleAccessMethod accessMethod) {
        if (accessMethod.getAccessMethodCode() != null) {
            Map<String, String> criteria = new HashMap<String, String>();
            criteria.put(OLEConstants.OleAccessMethod.ACCESS_METHOD_CD, accessMethod.getAccessMethodCode());
            List<OleAccessMethod> accessMethodInDatabase = (List<OleAccessMethod>) getBoService().findMatching(OleAccessMethod.class, criteria);
            if ((accessMethodInDatabase.size() > 0)) {
                for (OleAccessMethod accessMethodObj : accessMethodInDatabase) {
                    Integer accessMethodId = accessMethodObj.getAccessMethodId();
                    if (null == accessMethod.getAccessMethodId() || (accessMethod.getAccessMethodId().intValue() != accessMethodId.intValue())) {
                        this.putFieldError(OLEConstants.OleAccessMethod.ACCESS_METHOD_CODE, "error.duplicate.code");
                        return false;
                    }
                }
            }
        }
        return true;
    }
}

