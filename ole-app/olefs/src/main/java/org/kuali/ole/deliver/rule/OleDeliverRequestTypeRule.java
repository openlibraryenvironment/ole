package org.kuali.ole.deliver.rule;

import org.kuali.ole.OLEConstants;
import org.kuali.ole.deliver.bo.OleDeliverRequestType;
import org.kuali.rice.krad.maintenance.MaintenanceDocument;
import org.kuali.rice.krad.rules.MaintenanceDocumentRuleBase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 11/7/12
 * Time: 7:10 PM
 * To change this template use File | Settings | File Templates.
 */
public class OleDeliverRequestTypeRule extends MaintenanceDocumentRuleBase {


    protected boolean processCustomSaveDocumentBusinessRules(MaintenanceDocument document) {
        boolean isValid = true;
        OleDeliverRequestType oleDeliverRequestType = (OleDeliverRequestType) document.getNewMaintainableObject().getDataObject();

        isValid &= validateCompletenessCode(oleDeliverRequestType);
        return isValid;
    }

    /**
     * This method  validates duplicate completeness Id and return boolean value.
     *
     * @param oleDeliverRequestType
     * @return boolean
     */
    private boolean validateCompletenessCode(OleDeliverRequestType oleDeliverRequestType) {

        if (oleDeliverRequestType.getRequestTypeCode() != null) {
            Map<String, String> criteria = new HashMap<String, String>();
            criteria.put(OLEConstants.OleDeliverRequest.REQUEST_TYPE_CD, oleDeliverRequestType.getRequestTypeCode());
            List<OleDeliverRequestType> oleDeliverRequestTypeCodeInDatabase = (List<OleDeliverRequestType>) getBoService().findMatching(OleDeliverRequestType.class, criteria);
            if ((oleDeliverRequestTypeCodeInDatabase.size() > 0)) {
                for (OleDeliverRequestType oleDeliverRequestTypeObj : oleDeliverRequestTypeCodeInDatabase) {
                    if (null == oleDeliverRequestType.getRequestTypeId() || (!oleDeliverRequestType.getRequestTypeId().equals(oleDeliverRequestTypeObj.getRequestTypeId()))) {
                        this.putFieldError(OLEConstants.OleDeliverRequest.REQUEST_TYPE_CD, "error.duplicate.code");
                        return false;
                    }
                }
            }
        }
        return true;
    }


}
