package org.kuali.ole.select.rule;

import org.kuali.ole.OLEConstants;
import org.kuali.ole.select.bo.OleAgreementDocType;
import org.kuali.rice.krad.maintenance.MaintenanceDocument;
import org.kuali.rice.krad.rules.MaintenanceDocumentRuleBase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 3/20/13
 * Time: 4:41 PM
 * To change this template use File | Settings | File Templates.
 */
public class OleAgreementDocTypeRule extends MaintenanceDocumentRuleBase {


    protected boolean processCustomSaveDocumentBusinessRules(MaintenanceDocument document) {
        boolean isValid = true;
        OleAgreementDocType oleAgreementDocType = (OleAgreementDocType) document.getNewMaintainableObject().getDataObject();

        isValid &= validateOleAgreementDocTypeName(oleAgreementDocType);
        return isValid;
    }

    /**
     * This method  validates duplicate Agreement Doc Type Id and return boolean value.
     *
     * @param oleAgreementDocType
     * @return boolean
     */
    private boolean validateOleAgreementDocTypeName(OleAgreementDocType oleAgreementDocType) {

        if (oleAgreementDocType.getAgreementDocTypeName() != null) {
            Map<String, String> criteria = new HashMap<String, String>();
            criteria.put(OLEConstants.OleAgreementDocTypeRule.AGR_DOC_TYPE_NAME, oleAgreementDocType.getAgreementDocTypeName());
            List<OleAgreementDocType> savedOleAgreementDocType = (List<OleAgreementDocType>) getBoService().findMatching(OleAgreementDocType.class, criteria);
            if ((savedOleAgreementDocType.size() > 0)) {
                for (OleAgreementDocType agreementDocType : savedOleAgreementDocType) {
                    String agreementDocTypeId = agreementDocType.getAgreementDocTypeId();
                    if (null == oleAgreementDocType.getAgreementDocTypeId() || (!oleAgreementDocType.getAgreementDocTypeId().equalsIgnoreCase(agreementDocTypeId))) {
                        this.putFieldError(OLEConstants.OleAgreementDocTypeRule.AGR_DOC_TYPE_NAME_FIELD, OLEConstants.ERROR_DUPLICATE_CODE);
                        return false;
                    }
                }
            }
        }
        return true;
    }
}
