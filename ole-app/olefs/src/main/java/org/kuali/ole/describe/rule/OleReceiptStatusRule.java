package org.kuali.ole.describe.rule;

import org.kuali.ole.OLEConstants;
import org.kuali.ole.describe.bo.OleReceiptStatus;
import org.kuali.rice.krad.maintenance.MaintenanceDocument;
import org.kuali.rice.krad.rules.MaintenanceDocumentRuleBase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * OleReceiptStatusRule validates maintenance object for Receipt Status Maintenance Document
 */
public class OleReceiptStatusRule extends MaintenanceDocumentRuleBase {


    protected boolean processCustomSaveDocumentBusinessRules(MaintenanceDocument document) {
        boolean isValid = true;
        OleReceiptStatus oleReceiptStatus = (OleReceiptStatus) document.getNewMaintainableObject().getDataObject();

        isValid &= validateReceiptStatusCode(oleReceiptStatus);
        return isValid;
    }

    /**
     * This method  validates duplicate receiptStatus Id and return boolean value.
     *
     * @param oleReceiptStatus
     * @return boolean
     */
    private boolean validateReceiptStatusCode(OleReceiptStatus oleReceiptStatus) {

        if (oleReceiptStatus.getReceiptStatusCode() != null) {

            Map<String, String> criteria = new HashMap<String, String>();

            criteria.put(OLEConstants.OleReceiptStatus.RECEIPT_STATUS_CD, oleReceiptStatus.getReceiptStatusCode());

            List<OleReceiptStatus> receiptStatusCodeInDatabase = (List<OleReceiptStatus>) getBoService().findMatching(OleReceiptStatus.class, criteria);

            if ((receiptStatusCodeInDatabase.size() > 0)) {

                for (OleReceiptStatus receiptStatusObj : receiptStatusCodeInDatabase) {
                    String receiptStatusId = receiptStatusObj.getReceiptStatusId();
                    if (null == oleReceiptStatus.getReceiptStatusId() ||
                            !(receiptStatusId.equalsIgnoreCase(oleReceiptStatus.getReceiptStatusId()))) {
                        this.putFieldError(OLEConstants.OleReceiptStatus.RECEIPT_STATUS_CODE, "error.duplicate.code");
                        return false;
                    }
                }
            }
            return true;
        }
        return false;
    }
}