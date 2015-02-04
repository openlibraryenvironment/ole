package org.kuali.ole.select.rule;

import org.kuali.ole.OLEConstants;
import org.kuali.ole.select.bo.OLEPaymentType;
import org.kuali.rice.krad.maintenance.MaintenanceDocument;
import org.kuali.rice.krad.rules.MaintenanceDocumentRuleBase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: divyaj
 * Date: 6/21/13
 * Time: 4:57 PM
 * To change this template use File | Settings | File Templates.
 */
public class OLEPaymentTypeRule extends MaintenanceDocumentRuleBase {


    protected boolean processCustomSaveDocumentBusinessRules(MaintenanceDocument document) {
        boolean isValid = true;
        OLEPaymentType OLEPaymentType = (OLEPaymentType) document.getNewMaintainableObject().getDataObject();

        isValid &= validateOleMaterialTypeCode(OLEPaymentType);
        return isValid;
    }


    private boolean validateOleMaterialTypeCode(OLEPaymentType OLEPaymentType) {

        if (OLEPaymentType.getOlePaymentTypeName() != null) {
            Map<String, String> criteria = new HashMap<String, String>();
            criteria.put(OLEConstants.OlePaymentType.PAYMENT_TYPE_NAME, OLEPaymentType.getOlePaymentTypeName());
            List<OLEPaymentType> savedOLEPaymentType = (List<OLEPaymentType>) getBoService().findMatching(OLEPaymentType.class, criteria);
            if ((savedOLEPaymentType.size() > 0)) {
                for (OLEPaymentType PaymentType : savedOLEPaymentType) {
                    String paymentTypeId = PaymentType.getOlePaymentTypeId();
                    if (null == OLEPaymentType.getOlePaymentTypeId() || (!OLEPaymentType.getOlePaymentTypeId().equalsIgnoreCase(paymentTypeId))) {
                        this.putFieldError(OLEConstants.OlePaymentType.PAYMENT_TYPE_FIELD, OLEConstants.OlePaymentType.ERROR_PAYMENT_TYPE_NAME);
                        return false;
                    }
                }
            }
        }
        return true;
    }
}

