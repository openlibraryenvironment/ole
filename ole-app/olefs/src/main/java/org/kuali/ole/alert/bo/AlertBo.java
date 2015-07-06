package org.kuali.ole.alert.bo;

import org.apache.commons.lang.StringUtils;
import org.kuali.ole.OLEConstants;
import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

import javax.persistence.Transient;
import java.sql.Date;

/**
 * Created by maheswarang on 11/4/14.
 */
public class AlertBo extends PersistableBusinessObjectBase {

    private String alertId;

    private String documentId;

    private Date alertDate;

    private String alertNote;

    private Date alertCreateDate;

    private Date alertModifiedDate;

    private String alertInitiatorId;

    private String alertModifierId;

    private boolean alertStatus;

    private String receivingUserId;

    private String receivingGroupId;

    private String receivingRoleId;

    private boolean editFlag = false;

    private String alertApproverId;

    private Date alertApprovedDate;

    private String action;

    private String status;

    private String receivingUserName;

    private String alertInitiatorName;

    private String alertModifierName;

    private String receivingGroupName;

    private String receivingRoleName;

    private String alertInterval;

    private boolean repeatable;

    private String alertSelector;

    private String alertDetails;

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public boolean isAlertStatus() {
        return alertStatus;
    }

    public void setAlertStatus(boolean alertStatus) {
        this.alertStatus = alertStatus;
    }

    public String getReceivingUserId() {
        return receivingUserId;
    }

    public void setReceivingUserId(String receivingUserId) {
        this.receivingUserId = receivingUserId;
    }

    public String getReceivingGroupId() {
        return receivingGroupId;
    }

    public void setReceivingGroupId(String receivingGroupId) {
        this.receivingGroupId = receivingGroupId;
    }

    public String getAlertId() {
        return alertId;
    }

    public void setAlertId(String alertId) {
        this.alertId = alertId;
    }

    public Date getAlertDate() {
        return alertDate;
    }

    public void setAlertDate(Date alertDate) {
        this.alertDate = alertDate;
    }

    public String getAlertNote() {
        return alertNote;
    }

    public void setAlertNote(String alertNote) {
        this.alertNote = alertNote;
    }

    public Date getAlertCreateDate() {
        return alertCreateDate;
    }

    public void setAlertCreateDate(Date alertCreateDate) {
        this.alertCreateDate = alertCreateDate;
    }

    public Date getAlertModifiedDate() {
        return alertModifiedDate;
    }

    public void setAlertModifiedDate(Date alertModifiedDate) {
        this.alertModifiedDate = alertModifiedDate;
    }

    public String getAlertInitiatorId() {
        return alertInitiatorId;
    }

    public void setAlertInitiatorId(String alertInitiatorId) {
        this.alertInitiatorId = alertInitiatorId;
    }

    public String getAlertModifierId() {
        return alertModifierId;
    }

    public void setAlertModifierId(String alertModifierId) {
        this.alertModifierId = alertModifierId;
    }

    public boolean isEditFlag() {
        return editFlag;
    }

    public void setEditFlag(boolean editFlag) {
        this.editFlag = editFlag;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public String getReceivingUserName() {
        return receivingUserName;
    }

    public void setReceivingUserName(String receivingUserName) {
        this.receivingUserName = receivingUserName;
    }

    public String getAlertInitiatorName() {
        return alertInitiatorName;
    }

    public void setAlertInitiatorName(String alertInitiatorName) {
        this.alertInitiatorName = alertInitiatorName;
    }

    public String getAlertModifierName() {
        return alertModifierName;
    }

    public void setAlertModifierName(String alertModifierName) {
        this.alertModifierName = alertModifierName;
    }

    public String getReceivingGroupName() {
        return receivingGroupName;
    }

    public void setReceivingGroupName(String receivingGroupName) {
        this.receivingGroupName = receivingGroupName;
    }

    public String getAlertInterval() {
        return alertInterval;
    }

    public void setAlertInterval(String alertInterval) {
        this.alertInterval = alertInterval;
    }

    public boolean isRepeatable() {
        return repeatable;
    }

    public void setRepeatable(boolean repeatable) {
        this.repeatable = repeatable;
    }

    public String getReceivingRoleId() {
        return receivingRoleId;
    }

    public void setReceivingRoleId(String receivingRoleId) {
        this.receivingRoleId = receivingRoleId;
    }

    public String getReceivingRoleName() {
        return receivingRoleName;
    }

    public void setReceivingRoleName(String receivingRoleName) {
        this.receivingRoleName = receivingRoleName;
    }

    public String getAlertSelector() {
        if(StringUtils.isBlank(alertSelector)) {
            if(StringUtils.isNotBlank(receivingRoleId)){
                alertSelector = OLEConstants.SELECTOR_ROLE;
            }
            else if(StringUtils.isNotBlank(receivingGroupId)){
                alertSelector = OLEConstants.SELECTOR_GROUP;
            }
            else if(StringUtils.isNotBlank(receivingUserId)){
                alertSelector = OLEConstants.SELECTOR_PERSON;
            }
        }
        return alertSelector;
    }

    public void setAlertSelector(String alertSelector) {
        this.alertSelector = alertSelector;
    }

    public String getAlertDetails() {
        return alertDetails;
    }

    public void setAlertDetails(String alertDetails) {
        this.alertDetails = alertDetails;
    }
}
