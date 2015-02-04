package org.kuali.ole.describe.rule;

import org.kuali.ole.OLEConstants;
import org.kuali.ole.describe.bo.OleSpecificRetentionPolicyType;
import org.kuali.rice.krad.maintenance.MaintenanceDocument;
import org.kuali.rice.krad.rules.MaintenanceDocumentRuleBase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * OleSpecificRetentionPolicyTypeRule validates maintenance object for Specific Retention Policy Type Maintenance Document
 */
public class OleSpecificRetentionPolicyTypeRule extends MaintenanceDocumentRuleBase {


    protected boolean processCustomSaveDocumentBusinessRules(MaintenanceDocument document) {
        boolean isValid = true;
        OleSpecificRetentionPolicyType oleSpecificRetentionPolicyType = (OleSpecificRetentionPolicyType) document.getNewMaintainableObject().getDataObject();

        isValid &= validateSpecificRetentionPolicyTypeCode(oleSpecificRetentionPolicyType);
        return isValid;
    }

    /**
     * This method  validates duplicate specificRetentionPolicyType Id and return boolean value.
     *
     * @param oleSpecificRetentionPolicyType
     * @return boolean
     */
    private boolean validateSpecificRetentionPolicyTypeCode(OleSpecificRetentionPolicyType oleSpecificRetentionPolicyType) {

        if (oleSpecificRetentionPolicyType.getSpecificRetentionPolicyTypeCode() != null) {

            Map<String, String> criteria = new HashMap<String, String>();

            criteria.put(OLEConstants.OleSpecificRetentionPolicyType.SPECIFIC_RETENTION_POLICY_TYPE_CD, oleSpecificRetentionPolicyType.getSpecificRetentionPolicyTypeCode());


            List<OleSpecificRetentionPolicyType> specificRetentionPolicyTypeCodeInDatabase = (List<OleSpecificRetentionPolicyType>) getBoService().findMatching(OleSpecificRetentionPolicyType.class, criteria);

            if ((specificRetentionPolicyTypeCodeInDatabase.size() > 0)) {
                for (OleSpecificRetentionPolicyType oleSpecificRetentionPolicyTypeObj : specificRetentionPolicyTypeCodeInDatabase) {
                    Integer specificRetentionPolicyTypeId = oleSpecificRetentionPolicyTypeObj.getSpecificRetentionPolicyTypeId();
                    if (null == oleSpecificRetentionPolicyType.getSpecificRetentionPolicyTypeId() || specificRetentionPolicyTypeId.intValue() != oleSpecificRetentionPolicyType.getSpecificRetentionPolicyTypeId().intValue()) {
                        this.putFieldError(OLEConstants.OleSpecificRetentionPolicyType.SPECIFIC_RETENTION_POLICY_TYPE_CODE, "error.duplicate.code");
                        return false;
                    }
                }
            }
        }

        return true;
    }
}