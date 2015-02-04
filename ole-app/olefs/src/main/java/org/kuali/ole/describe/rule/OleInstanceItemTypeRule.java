package org.kuali.ole.describe.rule;

import org.kuali.ole.OLEConstants;
import org.kuali.ole.describe.bo.OleInstanceItemType;
import org.kuali.rice.krad.maintenance.MaintenanceDocument;
import org.kuali.rice.krad.rules.MaintenanceDocumentRuleBase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * OleInstanceItemTypeRule validates maintenance object for Instance Item Type Maintenance Document
 */
public class OleInstanceItemTypeRule extends MaintenanceDocumentRuleBase {


    protected boolean processCustomSaveDocumentBusinessRules(MaintenanceDocument document) {
        boolean isValid = true;
        OleInstanceItemType oleInstanceItemType = (OleInstanceItemType) document.getNewMaintainableObject().getDataObject();

        isValid &= validateInstanceItemTypeCode(oleInstanceItemType);
        return isValid;
    }

    /**
     * This method  validates duplicate instanceItemType Id and return boolean value.
     *
     * @param oleInstanceItemType
     * @return boolean
     */
    private boolean validateInstanceItemTypeCode(OleInstanceItemType oleInstanceItemType) {

        if (oleInstanceItemType.getInstanceItemTypeCode() != null) {

            Map<String, String> criteria = new HashMap<String, String>();

            criteria.put(OLEConstants.OleInstanceItemType.INSTANCE_ITEM_TYPE_CD, oleInstanceItemType.getInstanceItemTypeCode());


            List<OleInstanceItemType> instanceItemTypeCodeInDatabase = (List<OleInstanceItemType>) getBoService().findMatching(OleInstanceItemType.class, criteria);

            if ((instanceItemTypeCodeInDatabase.size() > 0)) {
                for (OleInstanceItemType oleInstanceItemTypeObj : instanceItemTypeCodeInDatabase) {
                    String instanceItemTypeId = oleInstanceItemTypeObj.getInstanceItemTypeId();
                    if (null == oleInstanceItemType.getInstanceItemTypeId() || !instanceItemTypeId.equalsIgnoreCase(oleInstanceItemType.getInstanceItemTypeId())) {
                        this.putFieldError(OLEConstants.OleInstanceItemType.INSTANCE_ITEM_TYPE_CODE, "error.duplicate.code");
                        return false;
                    }
                }
            }
        }

        return true;
    }
}