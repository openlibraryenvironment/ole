package org.kuali.ole.alert.keyValue;

import org.kuali.ole.alert.bo.AlertDocument;
import org.kuali.ole.alert.bo.AlertDocumentType;
import org.kuali.ole.alert.bo.AlertEvent;
import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.krad.maintenance.MaintenanceDocument;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krad.uif.control.UifKeyValuesFinderBase;
import org.kuali.rice.krad.uif.view.ViewModel;
import org.kuali.rice.krad.web.form.MaintenanceDocumentForm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by maheswarang on 12/26/14.
 */
public class AlertEventValueFinder extends UifKeyValuesFinderBase {
    private Map<String,String> criteriaMap;
    private List<AlertDocumentType> alertDocumentTypes;
    private List<AlertEvent> alertEvents;
    public List<KeyValue> getKeyValues(ViewModel viewModel) {
        MaintenanceDocument maintenanceDocument= ((MaintenanceDocumentForm) viewModel).getDocument();
        AlertDocument alertDocument=(AlertDocument)maintenanceDocument.getDocumentDataObject();
        List<KeyValue> keyValues = new ArrayList<KeyValue>();
        String documentTypeName = alertDocument.getDocumentTypeName();
        criteriaMap = new HashMap<String,String>();
        criteriaMap.put("alertDocumentTypeName",documentTypeName);
        String documentTypeId ;
         alertDocumentTypes = (List<AlertDocumentType>)KRADServiceLocator.getBusinessObjectService().findMatching(AlertDocumentType.class,criteriaMap);
    if(alertDocumentTypes!=null && alertDocumentTypes.size()>0){
        documentTypeId = alertDocumentTypes.get(0).getAlertDocumentId();
        criteriaMap = new HashMap<String,String>();
        criteriaMap.put("alertDocumentTypeId",documentTypeId);
        criteriaMap.put("active","true");
        alertEvents = (List<AlertEvent>)KRADServiceLocator.getBusinessObjectService().findMatching(AlertEvent.class,criteriaMap);
        if(alertEvents!=null && alertEvents.size()>0){
            for(AlertEvent alertEvent : alertEvents){
                keyValues.add(new ConcreteKeyValue(alertEvent.getAlertEventId(),alertEvent.getAlertEventName()));
            }
        }
    }
        return keyValues;
    }


}
