package org.kuali.ole.describe.rule;

import org.kuali.ole.OLEConstants;
import org.kuali.ole.describe.bo.OleSpecificRetentionPolicyTypeUnit;
import org.kuali.rice.krad.maintenance.MaintenanceDocument;
import org.kuali.rice.krad.rules.MaintenanceDocumentRuleBase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * OleSpecificRetentionPolicyTypeUnitRule validates maintenance object for Specific Retention Policy Type Unit Maintenance Document
 */
public class OleSpecificRetentionPolicyTypeUnitRule extends MaintenanceDocumentRuleBase {

    protected boolean processCustomSaveDocumentBusinessRules(MaintenanceDocument document) {
        boolean isValid = true;
        OleSpecificRetentionPolicyTypeUnit oleSpecificRetentionPolicyTypeUnit = (OleSpecificRetentionPolicyTypeUnit) document.getNewMaintainableObject().getDataObject();

        isValid &= validateSpecificPolicyUnitTypeCode(oleSpecificRetentionPolicyTypeUnit);
        return isValid;
    }

    /**
     * This method  validates duplicate specificPolicyUnitType Id and return boolean value.
     *
     * @param oleSpecificRetentionPolicyTypeUnit
     *
     * @return boolean
     */
    private boolean validateSpecificPolicyUnitTypeCode(OleSpecificRetentionPolicyTypeUnit oleSpecificRetentionPolicyTypeUnit) {

        if (oleSpecificRetentionPolicyTypeUnit.getSpecificPolicyUnitTypeCode() != null) {

            Map<String, String> criteria = new HashMap<String, String>();

            criteria.put(OLEConstants.OleSpecificRetentionPolicyTypeUnit.SPECIFIC_POLICY_UNIT_TYPE_CD, oleSpecificRetentionPolicyTypeUnit.getSpecificPolicyUnitTypeCode());

            List<OleSpecificRetentionPolicyTypeUnit> specificPolicyUnitTypeCodeInDatabase = (List<OleSpecificRetentionPolicyTypeUnit>) getBoService().findMatching(OleSpecificRetentionPolicyTypeUnit.class, criteria);

            if ((specificPolicyUnitTypeCodeInDatabase.size() > 0)) {

                for (OleSpecificRetentionPolicyTypeUnit specificRetentionPolicyTypeUnitObj : specificPolicyUnitTypeCodeInDatabase) {
                    String specificPolicyUnitTypeId = specificRetentionPolicyTypeUnitObj.getSpecificPolicyUnitTypeId();
                    if (null == oleSpecificRetentionPolicyTypeUnit.getSpecificPolicyUnitTypeId() ||
                            !(specificPolicyUnitTypeId.equalsIgnoreCase(oleSpecificRetentionPolicyTypeUnit.getSpecificPolicyUnitTypeId()))) {
                        this.putFieldError(OLEConstants.OleSpecificRetentionPolicyTypeUnit.SPECIFIC_POLICY_UNIT_TYPE_CODE, "error.duplicate.code");
                        return false;
                    }
                }
            }
            return true;
        }
        return false;
    }
}