package org.kuali.ole.describe.rule;

import org.kuali.ole.OLEConstants;
import org.kuali.ole.deliver.bo.OleBorrowerType;
import org.kuali.ole.deliver.bo.OlePatronDocument;
import org.kuali.rice.krad.maintenance.MaintenanceDocument;
import org.kuali.rice.krad.rules.MaintenanceDocumentRuleBase;
import org.kuali.rice.krad.service.KRADServiceLocator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * OleBorrowerTypeRule validates maintenance object for Borrower Type Maintenance Document
 */
public class OleBorrowerTypeRule extends MaintenanceDocumentRuleBase {


    protected boolean processCustomSaveDocumentBusinessRules(MaintenanceDocument document) {
        boolean isValid = true;
        OleBorrowerType borrowerType = (OleBorrowerType) document.getNewMaintainableObject().getDataObject();
        isValid &= validateBorrowerTypeCode(borrowerType);
        return isValid;
    }

    /**
     * This method  validates duplicate borrowerTypeId and return boolean value.
     *
     * @param borrowerType
     * @return boolean
     */
    private boolean validateBorrowerTypeCode(OleBorrowerType borrowerType) {
        if (borrowerType.getBorrowerTypeCode() != null) {

            Map<String, String> criteria = new HashMap<String, String>();

            criteria.put(OLEConstants.OleBorrowerType.BORROWER_TYPE_CD, borrowerType.getBorrowerTypeCode());

            List<OleBorrowerType> borrowerTypeInDatabase = (List<OleBorrowerType>) getBoService().findMatching(OleBorrowerType.class, criteria);
            if (!borrowerType.isActive()) {
                Map<String, String> borrowerTypeMap = new HashMap<String, String>();
                borrowerTypeMap.put("borrowerType", borrowerType.getBorrowerTypeId());
                List<OlePatronDocument> patronDocumentList = (List<OlePatronDocument>) KRADServiceLocator.getBusinessObjectService().findMatching(OlePatronDocument.class, borrowerTypeMap);
                if (patronDocumentList != null && patronDocumentList.size() > 0) {
                    this.putFieldError(OLEConstants.OleBorrowerType.BORROWER_TYPE_ACTIVE, OLEConstants.OleBorrowerType.BORROWER_TYPE_ACTIVE_ERROR);
                    return false;
                }
            }
            if ((borrowerTypeInDatabase.size() > 0)) {

                for (OleBorrowerType borrowerObj : borrowerTypeInDatabase) {
                    String borrowerTypeId = borrowerObj.getBorrowerTypeId();
                    if (null == borrowerType.getBorrowerTypeId() ||
                            !(borrowerType.getBorrowerTypeId().equalsIgnoreCase(borrowerTypeId))) {
                        this.putFieldError(OLEConstants.OleBorrowerType.BORROWER_TYPE_CODE, "error.duplicate.code");
                        return false;
                    }
                }

            }
            return true;
        }
        return false;
    }
}
