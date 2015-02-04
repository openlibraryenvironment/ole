package org.kuali.ole.select.rule;

import org.kuali.ole.OLEConstants;
import org.kuali.ole.select.bo.OleAgreementType;
import org.kuali.rice.krad.maintenance.MaintenanceDocument;
import org.kuali.rice.krad.rules.MaintenanceDocumentRuleBase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 3/21/13
 * Time: 1:19 PM
 * To change this template use File | Settings | File Templates.
 */
public class OleAgreementTypeRule extends MaintenanceDocumentRuleBase {
    protected boolean processCustomSaveDocumentBusinessRules(MaintenanceDocument document) {
        boolean isValid = true;
        OleAgreementType oleAgreementType = (OleAgreementType) document.getNewMaintainableObject().getDataObject();

        isValid &= validateOleAgreementTypeName(oleAgreementType);
        return isValid;
    }

    /**
     * This method  validates duplicate Agreement Type Id and return boolean value.
     *
     * @param oleAgreementType
     * @return boolean
     */
    private boolean validateOleAgreementTypeName(OleAgreementType oleAgreementType) {

        if (oleAgreementType.getAgreementTypeName() != null) {
            Map<String, String> criteria = new HashMap<String, String>();
            criteria.put(OLEConstants.OleAgreementTypeRule.AGR_TYPE_NAME, oleAgreementType.getAgreementTypeName());
            List<OleAgreementType> savedOleAgreementType = (List<OleAgreementType>) getBoService().findMatching(OleAgreementType.class, criteria);
            if ((savedOleAgreementType.size() > 0)) {
                for (OleAgreementType agreementType : savedOleAgreementType) {
                    String agreementTypeId = agreementType.getAgreementTypeId();
                    if (null == oleAgreementType.getAgreementTypeId() || (!oleAgreementType.getAgreementTypeId().equalsIgnoreCase(agreementTypeId))) {
                        this.putFieldError(OLEConstants.OleAgreementTypeRule.AGR_TYPE_NAME_FIELD, OLEConstants.ERROR_DUPLICATE_CODE);
                        return false;
                    }
                }
            }
        }
        return true;
    }
}
