package org.kuali.ole.select.rule;

import org.kuali.ole.OLEConstants;
import org.kuali.ole.select.bo.OleAgreementMethod;
import org.kuali.rice.krad.maintenance.MaintenanceDocument;
import org.kuali.rice.krad.rules.MaintenanceDocumentRuleBase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 3/21/13
 * Time: 1:16 PM
 * To change this template use File | Settings | File Templates.
 */
public class OleAgreementMethodRule extends MaintenanceDocumentRuleBase {

    protected boolean processCustomSaveDocumentBusinessRules(MaintenanceDocument document) {
        boolean isValid = true;
        OleAgreementMethod oleAgreementMethod = (OleAgreementMethod) document.getNewMaintainableObject().getDataObject();

        isValid &= validateOleAgreementMethodName(oleAgreementMethod);
        return isValid;
    }

    /**
     * This method  validates duplicate Agreement Method Id and return boolean value.
     *
     * @param oleAgreementMethod
     * @return boolean
     */
    private boolean validateOleAgreementMethodName(OleAgreementMethod oleAgreementMethod) {

        if (oleAgreementMethod.getAgreementMethodName() != null) {
            Map<String, String> criteria = new HashMap<String, String>();
            criteria.put(OLEConstants.OleAgreementMethodRule.AGR_MTHD_NAME, oleAgreementMethod.getAgreementMethodName());
            List<OleAgreementMethod> savedOleAgreementMethod = (List<OleAgreementMethod>) getBoService().findMatching(OleAgreementMethod.class, criteria);
            if ((savedOleAgreementMethod.size() > 0)) {
                for (OleAgreementMethod agreementMethod : savedOleAgreementMethod) {
                    String agreementMethodId = agreementMethod.getAgreementMethodId();
                    if (null == oleAgreementMethod.getAgreementMethodId() || (!oleAgreementMethod.getAgreementMethodId().equalsIgnoreCase(agreementMethodId))) {
                        this.putFieldError(OLEConstants.OleAgreementMethodRule.AGR_MTHD_NAME_FIELD, OLEConstants.ERROR_DUPLICATE_CODE);
                        return false;
                    }
                }
            }
        }
        return true;
    }
}
