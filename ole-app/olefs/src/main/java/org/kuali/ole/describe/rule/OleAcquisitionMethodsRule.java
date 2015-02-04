package org.kuali.ole.describe.rule;


import org.kuali.ole.OLEConstants;
import org.kuali.ole.describe.bo.OleAcquisitionMethod;
import org.kuali.rice.krad.maintenance.MaintenanceDocument;
import org.kuali.rice.krad.rules.MaintenanceDocumentRuleBase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * OleAcquisitionMethodsRule validates maintenance object for Acquisition Method Maintenance Document
 */
public class OleAcquisitionMethodsRule extends MaintenanceDocumentRuleBase {


    protected boolean processCustomSaveDocumentBusinessRules(MaintenanceDocument document) {
        boolean isValid = true;
        OleAcquisitionMethod acquisitionMethod = (OleAcquisitionMethod) document.getNewMaintainableObject().getDataObject();

        isValid &= validateAcquisitionCodeMethod(acquisitionMethod);
        return isValid;
    }

    /**
     * This method  validates duplicate acquisitionMethod Id and return boolean value.
     *
     * @param acquisitionMethod
     * @return boolean
     */
    private boolean validateAcquisitionCodeMethod(OleAcquisitionMethod acquisitionMethod) {

        if (acquisitionMethod.getAcquisitionMethodCode() != null) {
            Map<String, String> criteria = new HashMap<String, String>();
            criteria.put(OLEConstants.OleAcquisitionMethod.ACQUISITION_METHOD_CD, acquisitionMethod.getAcquisitionMethodCode());
            List<OleAcquisitionMethod> acquisitionMethodInDatabase = (List<OleAcquisitionMethod>) getBoService().findMatching(OleAcquisitionMethod.class, criteria);
            if ((acquisitionMethodInDatabase.size() > 0)) {
                for (OleAcquisitionMethod acquisitionObj : acquisitionMethodInDatabase) {
                    Integer acquisitionMethodId = acquisitionObj.getAcquisitionMethodId();
                    if (null == acquisitionMethod.getAcquisitionMethodId() || (acquisitionMethod.getAcquisitionMethodId().intValue() != acquisitionMethodId.intValue())) {
                        this.putFieldError(OLEConstants.OleAcquisitionMethod.ACQUISITION_METHOD_CODE, "error.duplicate.code");
                        return false;
                    }
                }
            }
        }
        return true;
    }
}
