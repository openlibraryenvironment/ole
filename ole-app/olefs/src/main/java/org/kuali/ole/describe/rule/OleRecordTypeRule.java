package org.kuali.ole.describe.rule;

import org.kuali.ole.OLEConstants;
import org.kuali.ole.describe.bo.OleRecordType;
import org.kuali.rice.krad.maintenance.MaintenanceDocument;
import org.kuali.rice.krad.rules.MaintenanceDocumentRuleBase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * OleRecordTypeRule validates maintenance object for Record Type Maintenance Document
 */
public class OleRecordTypeRule extends MaintenanceDocumentRuleBase {


    protected boolean processCustomSaveDocumentBusinessRules(MaintenanceDocument document) {
        boolean isValid = true;
        OleRecordType oleRecordType = (OleRecordType) document.getNewMaintainableObject().getDataObject();

        isValid &= validateRecordTypeCode(oleRecordType);
        return isValid;
    }

    /**
     * This method  validates duplicate recordType Id and return boolean value.
     *
     * @param oleRecordType
     * @return boolean
     */
    private boolean validateRecordTypeCode(OleRecordType oleRecordType) {

        if (oleRecordType.getRecordTypeCode() != null) {

            Map<String, String> criteria = new HashMap<String, String>();

            criteria.put(OLEConstants.OleRecordType.RECORD_TYPE_CD, oleRecordType.getRecordTypeCode());

            List<OleRecordType> recordTypeCodeInDatabase = (List<OleRecordType>) getBoService().findMatching(OleRecordType.class, criteria);

            if ((recordTypeCodeInDatabase.size() > 0)) {

                for (OleRecordType recordTypeObj : recordTypeCodeInDatabase) {
                    Integer recordTypeId = recordTypeObj.getRecordTypeId();
                    if (null == oleRecordType.getRecordTypeId() ||
                            recordTypeId.intValue() != oleRecordType.getRecordTypeId().intValue()) {
                        this.putFieldError(OLEConstants.OleRecordType.RECORD_TYPE_CODE, "error.duplicate.code");
                        return false;
                    }
                }
            }
            return true;
        }
        return false;
    }
}