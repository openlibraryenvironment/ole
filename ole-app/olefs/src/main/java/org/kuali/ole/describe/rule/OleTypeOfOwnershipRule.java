package org.kuali.ole.describe.rule;

import org.kuali.ole.OLEConstants;
import org.kuali.ole.describe.bo.OleTypeOfOwnership;
import org.kuali.rice.krad.maintenance.MaintenanceDocument;
import org.kuali.rice.krad.rules.MaintenanceDocumentRuleBase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * OleTypeOfOwnershipRule validates maintenance object for Type Of Ownership Maintenance Document
 */
public class OleTypeOfOwnershipRule extends MaintenanceDocumentRuleBase {

    protected boolean processCustomSaveDocumentBusinessRules(MaintenanceDocument document) {
        boolean isValid = true;
        OleTypeOfOwnership oleTypeOfOwnership = (OleTypeOfOwnership) document.getNewMaintainableObject().getDataObject();

        isValid &= validateTypeOfOwnershipCode(oleTypeOfOwnership);
        return isValid;
    }

    /**
     * This method  validates duplicate typeOfOwnership Id and return boolean value.
     *
     * @param oleTypeOfOwnership
     * @return boolean
     */
    private boolean validateTypeOfOwnershipCode(OleTypeOfOwnership oleTypeOfOwnership) {

        if (oleTypeOfOwnership.getTypeOfOwnershipCode() != null) {

            Map<String, String> criteria = new HashMap<String, String>();

            criteria.put(OLEConstants.OleTypeOfOwnership.TYPE_OF_OWNERSHIP_CD, oleTypeOfOwnership.getTypeOfOwnershipCode());

            List<OleTypeOfOwnership> typeOfOwnershipCodeInDatabase = (List<OleTypeOfOwnership>) getBoService().findMatching(OleTypeOfOwnership.class, criteria);

            if ((typeOfOwnershipCodeInDatabase.size() > 0)) {

                for (OleTypeOfOwnership oleTypeOfOwnershipObj : typeOfOwnershipCodeInDatabase) {
                    Integer typeOfOwnershipId = oleTypeOfOwnershipObj.getTypeOfOwnershipId();
                    if (null == oleTypeOfOwnership.getTypeOfOwnershipId() ||
                            typeOfOwnershipId.intValue() != oleTypeOfOwnership.getTypeOfOwnershipId().intValue()) {
                        this.putFieldError(OLEConstants.OleTypeOfOwnership.TYPE_OF_OWNERSHIP_CODE, "error.duplicate.code");
                        return false;
                    }
                }

            }
            return true;
        }
        return false;
    }
}