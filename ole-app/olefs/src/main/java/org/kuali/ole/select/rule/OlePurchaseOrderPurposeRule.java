package org.kuali.ole.select.rule;

import org.kuali.ole.OLEConstants;
import org.kuali.ole.select.bo.OlePurchaseOrderPurpose;
import org.kuali.rice.core.api.criteria.QueryByCriteria;
import org.kuali.rice.krad.maintenance.MaintenanceDocument;
import org.kuali.rice.krad.rules.MaintenanceDocumentRuleBase;
import org.kuali.rice.krad.service.KRADServiceLocator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jating on 24/9/14.
 */
public class OlePurchaseOrderPurposeRule extends MaintenanceDocumentRuleBase {


        protected boolean processCustomSaveDocumentBusinessRules(MaintenanceDocument document) {
            boolean isValid = true;
            OlePurchaseOrderPurpose olePurchaseOrderPurpose = (org.kuali.ole.select.bo.OlePurchaseOrderPurpose) document.getNewMaintainableObject().getDataObject();

            isValid &= validateOlePurposeCode(olePurchaseOrderPurpose, document);

            return isValid;
        }

// validates duplicate values for the field PurchaseOrderPurposeCode

        private boolean validateOlePurposeCode(OlePurchaseOrderPurpose olePurchaseOrderPurpose, MaintenanceDocument document ) {

                if (olePurchaseOrderPurpose.getPurchaseOrderPurposeCode() != null) {
                Map<String, String> criteria = new HashMap<String, String>();
                criteria.put(OLEConstants.OlePurchaseOrderPurpose.PURCHASE_ORDER_PURPOSE_CODE, olePurchaseOrderPurpose.getPurchaseOrderPurposeCode());
                List<OlePurchaseOrderPurpose> savedOlePurchaseOrderPurpose = (List<OlePurchaseOrderPurpose>) KRADServiceLocator.getBusinessObjectService().findMatching(org.kuali.ole.select.bo.OlePurchaseOrderPurpose.class, criteria);
                if ((savedOlePurchaseOrderPurpose.size() > 0)) {
                    for (org.kuali.ole.select.bo.OlePurchaseOrderPurpose purchaseOrderPurpose : savedOlePurchaseOrderPurpose) {
                        String purposeId = purchaseOrderPurpose.getPurchaseOrderPurposeId();
                        if (null == olePurchaseOrderPurpose.getPurchaseOrderPurposeId() || (!olePurchaseOrderPurpose.getPurchaseOrderPurposeId().equalsIgnoreCase(purposeId))) {//
                            this.putFieldError(OLEConstants.OlePurchaseOrderPurpose.PURCHASE_ORDER_PURPOSE_CODE, OLEConstants.OlePurchaseOrderPurpose.ERROR_PURCHASE_ORDER_PURPOSE_CODE);
                            return false;
                        }
                    }
                }
            }
            return true;
        }
    }



