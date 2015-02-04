package org.kuali.ole.describe.rule;

import org.kuali.ole.OLEConstants;
import org.kuali.ole.describe.bo.OleLendingPolicy;
import org.kuali.rice.krad.maintenance.MaintenanceDocument;
import org.kuali.rice.krad.rules.MaintenanceDocumentRuleBase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * OleLendingPolicyRule validates maintenance object for Lending Policy Rule Maintenance Document
 */
public class OleLendingPolicyRule extends MaintenanceDocumentRuleBase {


    protected boolean processCustomSaveDocumentBusinessRules(MaintenanceDocument document) {
        boolean isValid = true;
        OleLendingPolicy oleLendingPolicy = (OleLendingPolicy) document.getNewMaintainableObject().getDataObject();

        isValid &= validateLendingPolicyCode(oleLendingPolicy);
        return isValid;
    }

    /**
     * This method  validates duplicate encodingLevel Id and return boolean value.
     *
     * @param oleLendingPolicy
     * @return boolean
     */
    private boolean validateLendingPolicyCode(OleLendingPolicy oleLendingPolicy) {

        if (oleLendingPolicy.getLendingPolicyCode() != null) {

            Map<String, String> criteria = new HashMap<String, String>();

            criteria.put(OLEConstants.OleLendingPolicy.LENDING_POLICY_CD, oleLendingPolicy.getLendingPolicyCode());

            List<OleLendingPolicy> lendingPolicyInDatabase = (List<OleLendingPolicy>) getBoService().findMatching(OleLendingPolicy.class, criteria);

            if ((lendingPolicyInDatabase.size() > 0)) {

                for (OleLendingPolicy lendingPolicyObj : lendingPolicyInDatabase) {
                    Integer lendingPolicyId = lendingPolicyObj.getLendingPolicyId();
                    if (null == oleLendingPolicy.getLendingPolicyId() ||
                            oleLendingPolicy.getLendingPolicyId().intValue() != lendingPolicyId.intValue()) {
                        this.putFieldError(OLEConstants.OleLendingPolicy.LENDING_POLICY_CODE, "error.duplicate.code");
                        return false;
                    }
                }
            }
            return true;
        }
        return false;
    }
}
