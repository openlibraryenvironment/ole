package org.kuali.ole.select.rule;

import org.kuali.ole.OLEConstants;
import org.kuali.ole.select.bo.OLERequestPriority;
import org.kuali.rice.krad.maintenance.MaintenanceDocument;
import org.kuali.rice.krad.rules.MaintenanceDocumentRuleBase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: divyaj
 * Date: 6/21/13
 * Time: 4:56 PM
 * To change this template use File | Settings | File Templates.
 */
public class OLERequestPriorityRule extends MaintenanceDocumentRuleBase {


    protected boolean processCustomSaveDocumentBusinessRules(MaintenanceDocument document) {
        boolean isValid = true;
        OLERequestPriority OLERequestPriority = (OLERequestPriority) document.getNewMaintainableObject().getDataObject();

        isValid &= validateOleMaterialTypeCode(OLERequestPriority);
        return isValid;
    }


    private boolean validateOleMaterialTypeCode(OLERequestPriority OLERequestPriority) {

        if (OLERequestPriority.getOleRequestPriorityName() != null) {
            Map<String, String> criteria = new HashMap<String, String>();
            criteria.put(OLEConstants.OleRequestPriority.REQUEST_PRIORITY_NAME, OLERequestPriority.getOleRequestPriorityName());
            List<OLERequestPriority> savedOLERequestPriority = (List<OLERequestPriority>) getBoService().findMatching(OLERequestPriority.class, criteria);
            if ((savedOLERequestPriority.size() > 0)) {
                for (OLERequestPriority requestPriority : savedOLERequestPriority) {
                    String requestPriorityId = requestPriority.getOleRequestPriorityId();
                    if (null == OLERequestPriority.getOleRequestPriorityId() || (!OLERequestPriority.getOleRequestPriorityId().equalsIgnoreCase(requestPriorityId))) {
                        this.putFieldError(OLEConstants.OleRequestPriority.REQUEST_PRIORITY_FIELD, OLEConstants.OleRequestPriority.ERROR_REQUEST_PRIORITY_NAME);
                        return false;
                    }
                }
            }
        }
        return true;
    }
}
