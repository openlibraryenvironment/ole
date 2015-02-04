package org.kuali.ole.describe.rule;

import org.kuali.ole.OLEConstants;
import org.kuali.ole.describe.bo.OleShelvingOrder;
import org.kuali.rice.krad.maintenance.MaintenanceDocument;
import org.kuali.rice.krad.rules.MaintenanceDocumentRuleBase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * OleShelvingOrderRule validates maintenance object for Shelving Order Maintenance Document
 */
public class OleShelvingOrderRule extends MaintenanceDocumentRuleBase {


    protected boolean processCustomSaveDocumentBusinessRules(MaintenanceDocument document) {
        boolean isValid = true;
        OleShelvingOrder oleShelvingOrder = (OleShelvingOrder) document.getNewMaintainableObject().getDataObject();

        isValid &= validateShelvingOrderCode(oleShelvingOrder);
        return isValid;
    }

    /**
     * This method  validates duplicate shelvingOrder Id and return boolean value.
     *
     * @param oleShelvingOrder
     * @return boolean
     */
    private boolean validateShelvingOrderCode(OleShelvingOrder oleShelvingOrder) {

        if (oleShelvingOrder.getShelvingOrderCode() != null) {
            Map<String, String> criteria = new HashMap<String, String>();
            criteria.put(OLEConstants.OleShelvingOrder.SHELVING_ORDER_CD, oleShelvingOrder.getShelvingOrderCode());
            List<OleShelvingOrder> shelvingOrderCodeInDatabase = (List<OleShelvingOrder>) getBoService().findMatching(OleShelvingOrder.class, criteria);
            if ((shelvingOrderCodeInDatabase.size() > 0)) {
                for (OleShelvingOrder shelvingOrderObj : shelvingOrderCodeInDatabase) {
                    Integer shelvingOrderId = shelvingOrderObj.getShelvingOrderId();
                    if (null == oleShelvingOrder.getShelvingOrderId() || (oleShelvingOrder.getShelvingOrderId().intValue() != shelvingOrderId.intValue())) {
                        this.putFieldError(OLEConstants.OleShelvingOrder.SHELVING_ORDER_CODE, "error.duplicate.code");
                        return false;
                    }
                }
            }
        }
        return true;
    }
}
