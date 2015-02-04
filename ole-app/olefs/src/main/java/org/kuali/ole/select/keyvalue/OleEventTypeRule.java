package org.kuali.ole.select.keyvalue;

import org.kuali.ole.OLEConstants;
import org.kuali.ole.select.bo.OleEventType;
import org.kuali.rice.kns.maintenance.rules.MaintenanceDocumentRuleBase;
import org.kuali.rice.krad.maintenance.MaintenanceDocument;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 10/10/12
 * Time: 10:08 PM
 * To change this template use File | Settings | File Templates.
 */
public class OleEventTypeRule extends MaintenanceDocumentRuleBase {


    protected boolean processCustomSaveDocumentBusinessRules(MaintenanceDocument document) {
        boolean isValid = true;
        OleEventType oleEventType = (OleEventType) document.getNewMaintainableObject().getDataObject();
        isValid &= validateEventTypeName(oleEventType);
        return isValid;
    }


    private boolean validateEventTypeName(OleEventType oleNewEventType) {

        if (oleNewEventType.getEventTypeName() != null) {
            Map<String, String> criteria = new HashMap<String, String>();
            criteria.put(OLEConstants.OleLicenseRequest.EVENT_TYPE_NM, oleNewEventType.getEventTypeName());
            List<OleEventType> eventTypeNameInDatabase = (List<OleEventType>) getBoService().findMatching(OleEventType.class, criteria);
            if ((eventTypeNameInDatabase.size() > 0)) {
                for (OleEventType eventTypeObj : eventTypeNameInDatabase) {
                    String eventTypeId = eventTypeObj.getEventTypeId();
                    if (null == oleNewEventType.getEventTypeId() || !(oleNewEventType.getEventTypeId().equals(eventTypeId))) {
                        this.putFieldError(OLEConstants.OleLicenseRequest.EVENT_TYPE_NAME, "error.duplicate.name");
                        return false;
                    }
                }
            }
        }
        return true;
    }
}
