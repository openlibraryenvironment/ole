package org.kuali.ole.describe.rule;

import org.kuali.ole.OLEConstants;
import org.kuali.ole.describe.bo.OleReproductionPolicy;
import org.kuali.rice.krad.maintenance.MaintenanceDocument;
import org.kuali.rice.krad.rules.MaintenanceDocumentRuleBase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * OleReproductionPolicyRule validates maintenance object for Reproduction Policy Maintenance Document
 */
public class OleReproductionPolicyRule extends MaintenanceDocumentRuleBase {


    protected boolean processCustomSaveDocumentBusinessRules(MaintenanceDocument document) {
        boolean isValid = true;
        OleReproductionPolicy oleReproductionPolicy = (OleReproductionPolicy) document.getNewMaintainableObject().getDataObject();
        isValid &= validateReproductionPolicyCode(oleReproductionPolicy);
        return isValid;
    }

    /**
     * This method  validates duplicate reproductionPolicy Id and return boolean value.
     *
     * @param oleReproductionPolicy
     * @return boolean
     */
    private boolean validateReproductionPolicyCode(OleReproductionPolicy oleReproductionPolicy) {

        if (oleReproductionPolicy.getReproductionPolicyCode() != null) {
            Map<String, String> criteria = new HashMap<String, String>();
            criteria.put(OLEConstants.OleReproductionPolicy.REPRODUCTION_POLICY_CD, oleReproductionPolicy.getReproductionPolicyCode());
            List<OleReproductionPolicy> reproductionPolicyCodeInDatabase = (List<OleReproductionPolicy>) getBoService().findMatching(OleReproductionPolicy.class, criteria);
            if ((reproductionPolicyCodeInDatabase.size() > 0)) {
                for (OleReproductionPolicy reproductionPolicyObj : reproductionPolicyCodeInDatabase) {
                    Integer reproductionPolicyId = reproductionPolicyObj.getReproductionPolicyId();
                    if (null == oleReproductionPolicy.getReproductionPolicyId() || (reproductionPolicyId.intValue() != oleReproductionPolicy.getReproductionPolicyId().intValue())) {
                        this.putFieldError(OLEConstants.OleReproductionPolicy.REPRODUCTION_POLICY_CODE, "error.duplicate.code");
                        return false;
                    }
                }
            }
        }
        return true;
    }
}