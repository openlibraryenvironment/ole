package org.kuali.ole.alert.bo;

import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

import java.sql.Date;

/**
 * Created by maheswarang on 11/4/14.
 */
public class ActionListAlertBo extends PersistableBusinessObjectBase {

    private String actionListAlertId;

    private String documentId;

    private String title;

    private String recordType;

    private String note;

    private boolean active;

    private String alertUserId;

    private Date alertDate;

    private String alertInitiatorId;

    private String alertApproverId;

    private Date alertApprovedDate;

    private String alertId;


    private String action;

    private String alertInitiatorName;


    public String getAlertId() {
        return alertId;
    }

    public void setAlertId(String alertId) {
        this.alertId = alertId;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRecordType() {
        return recordType;
    }

    public void setRecordType(String recordType) {
        this.recordType = recordType;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getAlertUserId() {
        return alertUserId;
    }

    public void setAlertUserId(String alertUserId) {
        this.alertUserId = alertUserId;
    }

/*    public DocumentRouteHeaderValue getRouteHeader() {
        return routeHeader;
    }

    public void setRouteHeader(DocumentRouteHeaderValue routeHeader) {
        this.routeHeader = routeHeader;
    }*/

    public Date getAlertDate() {
        return alertDate;
    }

    public void setAlertDate(Date alertDate) {
        this.alertDate = alertDate;
    }

    public String getAlertInitiatorId() {
        return alertInitiatorId;
    }

    public void setAlertInitiatorId(String alertInitiatorId) {
        this.alertInitiatorId = alertInitiatorId;
    }

    public String getAlertApproverId() {
        return alertApproverId;
    }

    public void setAlertApproverId(String alertApproverId) {
        this.alertApproverId = alertApproverId;
    }

    public Date getAlertApprovedDate() {
        return alertApprovedDate;
    }

    public void setAlertApprovedDate(Date alertApprovedDate) {
        this.alertApprovedDate = alertApprovedDate;
    }

    public String getAlertInitiatorName() {
        return alertInitiatorName;
    }

    public void setAlertInitiatorName(String alertInitiatorName) {
        this.alertInitiatorName = alertInitiatorName;
    }

    public String getActionListAlertId() {
        return actionListAlertId;
    }

    public void setActionListAlertId(String actionListAlertId) {
        this.actionListAlertId = actionListAlertId;
    }
}
