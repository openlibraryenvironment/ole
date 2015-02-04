package org.kuali.ole.alert.rule;

import org.kuali.ole.OLEConstants;
import org.kuali.ole.alert.bo.AlertDocumentType;
import org.kuali.ole.alert.bo.AlertEvent;
import org.kuali.ole.deliver.bo.OleDeliverRequestType;
import org.kuali.rice.krad.maintenance.MaintenanceDocument;
import org.kuali.rice.krad.rules.MaintenanceDocumentRuleBase;
import org.kuali.rice.krad.service.KRADServiceLocator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by maheswarang on 12/27/14.
 */
public class AlertEventRule extends MaintenanceDocumentRuleBase {
    protected boolean processCustomSaveDocumentBusinessRules(MaintenanceDocument document) {
        boolean isValid = true;
        AlertEvent alertEvent = (AlertEvent) document.getNewMaintainableObject().getDataObject();
        isValid &= validateDocumentTypeName(alertEvent);
        return isValid;
    }

    /**
     * This method  validates duplicate completeness Id and return boolean value.
     *
     * @param alertEvent
     * @return boolean
     */
    private boolean validateDocumentTypeName(AlertEvent alertEvent) {

  if(alertEvent.getAlertEventId()==null &&  alertEvent.getAlertEventName() != null){
    Map<String,String> alertEventMap = new HashMap<String,String>();
      alertEventMap.put("alertDocumentTypeId",alertEvent.getAlertDocumentTypeId());
      alertEventMap.put("alertEventName",alertEvent.getAlertEventName());
      List<AlertEvent> alertEventList = (List<AlertEvent>)KRADServiceLocator.getBusinessObjectService().findMatching(AlertEvent.class, alertEventMap);
      if(alertEventList!=null && alertEventList.size()>0){
          //event name already defined for the same document type
          this.putFieldError(OLEConstants.ALERT_EVENT_NAME, "error.duplicate.event.name");
          return false;
      }
}
  return true;
    }

}
