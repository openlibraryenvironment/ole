package org.kuali.ole.alert.bo;

import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by maheswarang on 12/25/14.
 */
public class AlertEvent extends PersistableBusinessObjectBase {
    private String alertEventId;
    private String alertEventName;
    private String alertDocumentTypeId;
    private String alertDocumentTypeName;
    private String alertDocumentClassName;
    private AlertDocumentType alertDocumentType;
    private List<AlertEventField> alertEventFieldList = new ArrayList<AlertEventField>();
    private boolean active;

    public String getAlertEventId() {
        return alertEventId;
    }

    public void setAlertEventId(String alertEventId) {
        this.alertEventId = alertEventId;
    }

    public String getAlertEventName() {
        return alertEventName;
    }

    public void setAlertEventName(String alertEventName) {
        this.alertEventName = alertEventName;
    }

    public String getAlertDocumentTypeId() {
        return alertDocumentTypeId;
    }

    public void setAlertDocumentTypeId(String alertDocumentTypeId) {
        this.alertDocumentTypeId = alertDocumentTypeId;
    }

    public List<AlertEventField> getAlertEventFieldList() {
        return alertEventFieldList;
    }

    public void setAlertEventFieldList(List<AlertEventField> alertEventFieldList) {
        this.alertEventFieldList = alertEventFieldList;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getAlertDocumentTypeName()
    {
        if(alertDocumentTypeName==null && alertDocumentType!=null){
            alertDocumentTypeName = alertDocumentType.getAlertDocumentTypeName();
        }
        return alertDocumentTypeName;
    }

    public void setAlertDocumentTypeName(String alertDocumentTypeName) {
        this.alertDocumentTypeName = alertDocumentTypeName;
    }

    public AlertDocumentType getAlertDocumentType() {
        return alertDocumentType;
    }

    public void setAlertDocumentType(AlertDocumentType alertDocumentType) {
        this.alertDocumentType = alertDocumentType;
    }

    public String getAlertDocumentClassName() {

        if(alertDocumentClassName==null && alertDocumentType!=null){
            alertDocumentClassName = alertDocumentType.getAlertDocumentClass();
        }
        return alertDocumentClassName;


    }

    public void setAlertDocumentClassName(String alertDocumentClassName) {
        this.alertDocumentClassName = alertDocumentClassName;
    }
}
