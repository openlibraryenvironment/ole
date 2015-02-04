package org.kuali.ole.describe.rule;

import org.kuali.ole.OLEConstants;
import org.kuali.ole.describe.bo.OleGeneralRetentionPolicy;
import org.kuali.rice.krad.maintenance.MaintenanceDocument;
import org.kuali.rice.krad.rules.MaintenanceDocumentRuleBase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * OleGeneralRetentionPolicyRule validates maintenance object for General Retention Policy Maintenance Document
 */
public class OleGeneralRetentionPolicyRule extends MaintenanceDocumentRuleBase {


    protected boolean processCustomSaveDocumentBusinessRules(MaintenanceDocument document) {
        boolean isValid = true;
        OleGeneralRetentionPolicy oleGeneralRetentionPolicy = (OleGeneralRetentionPolicy) document.getNewMaintainableObject().getDataObject();

        isValid &= validateGeneralRetentionPolicyCode(oleGeneralRetentionPolicy);
        return isValid;
    }

    /**
     * This method  validates duplicate  generalRetentionPolicy Id and return boolean value.
     *
     * @param oleGeneralRetentionPolicy
     * @return boolean
     */
    private boolean validateGeneralRetentionPolicyCode(OleGeneralRetentionPolicy oleGeneralRetentionPolicy) {

        if (oleGeneralRetentionPolicy.getGeneralRetentionPolicyCode() != null) {

            Map<String, String> criteria = new HashMap<String, String>();

            criteria.put(OLEConstants.OleGeneralRetentionPolicy.GENERAL_RETENTION_POLICY_CD, oleGeneralRetentionPolicy.getGeneralRetentionPolicyCode());


            List<OleGeneralRetentionPolicy> generalRetentionPolicyInDatabase = (List<OleGeneralRetentionPolicy>) getBoService().findMatching(OleGeneralRetentionPolicy.class, criteria);

            if ((generalRetentionPolicyInDatabase.size() > 0)) {
                for (OleGeneralRetentionPolicy oleGeneralRetentionPolicyObj : generalRetentionPolicyInDatabase) {
                    Integer generalRetentionPolicyId = oleGeneralRetentionPolicyObj.getGeneralRetentionPolicyId();
                    if (null == oleGeneralRetentionPolicy.getGeneralRetentionPolicyId() || oleGeneralRetentionPolicy.getGeneralRetentionPolicyId().intValue() != generalRetentionPolicyId) {
                        this.putFieldError(OLEConstants.OleGeneralRetentionPolicy.GENERAL_RETENTION_POLICY_CODE, "error.duplicate.code");
                        return false;
                    }
                }
            }
        }

        return true;
    }
}
