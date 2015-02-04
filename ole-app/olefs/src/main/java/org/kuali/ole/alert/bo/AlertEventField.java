package org.kuali.ole.alert.bo;

import org.kuali.rice.krad.bo.PersistableBusinessObject;
import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

/**
 * Created by maheswarang on 12/25/14.
 */
public class AlertEventField extends PersistableBusinessObjectBase {
    private String alertEventFieldId;
    private String alertEventId;
    private String alertFieldName;
    private String alertFieldType;
    private String alertFieldValue;
    private String alertCriteria;
    private AlertEvent alertEvent;
    private boolean active;


    public String getAlertEventFieldId() {
        return alertEventFieldId;
    }

    public void setAlertEventFieldId(String alertEventFieldId) {
        this.alertEventFieldId = alertEventFieldId;
    }

    public String getAlertEventId() {
        return alertEventId;
    }

    public void setAlertEventId(String alertEventId) {
        this.alertEventId = alertEventId;
    }

    public AlertEvent getAlertEvent() {
        return alertEvent;
    }

    public void setAlertEvent(AlertEvent alertEvent) {
        this.alertEvent = alertEvent;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
    public String getAlertCriteria() {
        return alertCriteria;
    }

    public void setAlertCriteria(String alertCriteria) {
        this.alertCriteria = alertCriteria;
    }

    public String getAlertFieldName() {
        return alertFieldName;
    }

    public void setAlertFieldName(String alertFieldName) {
        this.alertFieldName = alertFieldName;
    }

    public String getAlertFieldType() {
        return alertFieldType;
    }

    public void setAlertFieldType(String alertFieldType) {
        this.alertFieldType = alertFieldType;
    }

    public String getAlertFieldValue() {
        return alertFieldValue;
    }

    public void setAlertFieldValue(String alertFieldValue) {
        this.alertFieldValue = alertFieldValue;
    }
}
