package org.kuali.ole.select.rule;

import org.kuali.ole.OLEConstants;
import org.kuali.ole.select.bo.OleSubscriptionStatus;
import org.kuali.rice.krad.maintenance.MaintenanceDocument;
import org.kuali.rice.krad.rules.MaintenanceDocumentRuleBase;
import org.kuali.rice.krad.service.KRADServiceLocator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jating on 24/9/14.
 * To add additional rules for the Subscription Status Maintenance Document
 */
public class OleSubscriptionStatusRule extends MaintenanceDocumentRuleBase {


        protected boolean processCustomSaveDocumentBusinessRules(MaintenanceDocument document) {
            boolean isValid = true;
            OleSubscriptionStatus oleSubscriptionStatus = (OleSubscriptionStatus) document.getNewMaintainableObject().getDataObject();

            isValid &= validateOlePurposeCode(oleSubscriptionStatus, document);

            return isValid;
        }



    /**
     * To validate duplicate values for the field SubscriptionStatusCode
     *
     * @param oleSubscriptionStatus
     * @param document
     * @return
     */
        private boolean validateOlePurposeCode(OleSubscriptionStatus oleSubscriptionStatus, MaintenanceDocument document ) {

                if (oleSubscriptionStatus.getSubscriptionStatusCode() != null) {
                Map<String, String> criteria = new HashMap<String, String>();
                criteria.put(OLEConstants.OleSubscriptionStatus.SUBSCRIPTION_STATUS_CODE, oleSubscriptionStatus.getSubscriptionStatusCode());
                List<OleSubscriptionStatus> savedOleSubscriptionStatus = (List<OleSubscriptionStatus>) KRADServiceLocator.getBusinessObjectService().findMatching(OleSubscriptionStatus.class,criteria);
                if ((savedOleSubscriptionStatus.size() > 0)) {
                    for (OleSubscriptionStatus subscriptionStatus : savedOleSubscriptionStatus) {
                        String purposeId = subscriptionStatus.getSubscriptionStatusId();
                        if (null == oleSubscriptionStatus.getSubscriptionStatusId() || (!oleSubscriptionStatus.getSubscriptionStatusId().equalsIgnoreCase(purposeId))) {//
                            this.putFieldError(OLEConstants.OleSubscriptionStatus.SUBSCRIPTION_STATUS_CODE, OLEConstants.OleSubscriptionStatus.ERROR_SUBSCRIPTION_STATUS_CODE);
                            return false;
                        }
                    }
                }
            }
            return true;
        }
    }



