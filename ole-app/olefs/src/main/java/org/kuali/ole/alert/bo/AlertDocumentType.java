package org.kuali.ole.alert.bo;

import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

/**
 * Created by angelind on 12/22/14.
 */
public class AlertDocumentType extends PersistableBusinessObjectBase {

    private String alertDocumentId;

    private String alertDocumentTypeName;

    private String alertDocumentTypeDescription;

    private String alertDocumentClass;

    private boolean activeIndicator;

    private String alertReminderInterval;

    public String getAlertDocumentId() {
        return alertDocumentId;
    }

    public void setAlertDocumentId(String alertDocumentId) {
        this.alertDocumentId = alertDocumentId;
    }

    public String getAlertDocumentTypeName() {
        return alertDocumentTypeName;
    }

    public void setAlertDocumentTypeName(String alertDocumentTypeName) {
        this.alertDocumentTypeName = alertDocumentTypeName;
    }

    public String getAlertDocumentTypeDescription() {
        return alertDocumentTypeDescription;
    }

    public void setAlertDocumentTypeDescription(String alertDocumentTypeDescription) {
        this.alertDocumentTypeDescription = alertDocumentTypeDescription;
    }

    public String getAlertDocumentClass() {
        return alertDocumentClass;
    }

    public void setAlertDocumentClass(String alertDocumentClass) {
        this.alertDocumentClass = alertDocumentClass;
    }

    public boolean isActiveIndicator() {
        return activeIndicator;
    }

    public void setActiveIndicator(boolean activeIndicator) {
        this.activeIndicator = activeIndicator;
    }

    public String getAlertReminderInterval() {
        return alertReminderInterval;
    }

    public void setAlertReminderInterval(String alertReminderInterval) {
        this.alertReminderInterval = alertReminderInterval;
    }
}