package org.kuali.ole.select.rule;

import org.kuali.ole.OLEConstants;
import org.kuali.ole.select.bo.OleAgreementStatus;
import org.kuali.rice.krad.maintenance.MaintenanceDocument;
import org.kuali.rice.krad.rules.MaintenanceDocumentRuleBase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 3/21/13
 * Time: 1:17 PM
 * To change this template use File | Settings | File Templates.
 */
public class OleAgreementStatusRule extends MaintenanceDocumentRuleBase {

    protected boolean processCustomSaveDocumentBusinessRules(MaintenanceDocument document) {
        boolean isValid = true;
        OleAgreementStatus oleAgreementStatus = (OleAgreementStatus) document.getNewMaintainableObject().getDataObject();

        isValid &= validateOleAgreementStatusName(oleAgreementStatus);
        return isValid;
    }

    /**
     * This method  validates duplicate Agreement status Id and return boolean value.
     *
     * @param oleAgreementStatus
     * @return boolean
     */
    private boolean validateOleAgreementStatusName(OleAgreementStatus oleAgreementStatus) {

        if (oleAgreementStatus.getAgreementStatusName() != null) {
            Map<String, String> criteria = new HashMap<String, String>();
            criteria.put(OLEConstants.OleAgreementStatusRule.AGR_STATUS_NAME, oleAgreementStatus.getAgreementStatusName());
            List<OleAgreementStatus> savedOleAgreementStatus = (List<OleAgreementStatus>) getBoService().findMatching(OleAgreementStatus.class, criteria);
            if ((savedOleAgreementStatus.size() > 0)) {
                for (OleAgreementStatus agreementStatus : savedOleAgreementStatus) {
                    String agreementStatusId = agreementStatus.getAgreementStatusId();
                    if (null == oleAgreementStatus.getAgreementStatusId() || (!oleAgreementStatus.getAgreementStatusId().equalsIgnoreCase(agreementStatusId))) {
                        this.putFieldError(OLEConstants.OleAgreementStatusRule.AGR_STATUS_NAME_FIELD, OLEConstants.ERROR_DUPLICATE_CODE);
                        return false;
                    }
                }
            }
        }
        return true;
    }
}
