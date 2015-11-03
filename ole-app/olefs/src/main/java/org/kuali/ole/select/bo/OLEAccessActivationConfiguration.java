package org.kuali.ole.select.bo;

import org.kuali.rice.krad.bo.PersistableBusinessObject;
import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by hemalathas on 12/19/14.
 */
public class OLEAccessActivationConfiguration extends PersistableBusinessObjectBase {

    private String accessActivationConfigurationId;
    private String workflowName;
    private String workflowType;
    private String workflowCompletionStatus;
    private boolean mailNotification;
    private String mailId;
    private String mailContent;
    private String recipientUserId;
    private String recipientUserName;
    private String recipientRoleId;
    private String recipientRoleName;
    private List<OLEAccessActivationWorkFlow> accessActivationWorkflowList = new ArrayList<OLEAccessActivationWorkFlow>();
    private boolean active;
    private String selector;
    private String notificationSelector;

    public String getAccessActivationConfigurationId() {
        return accessActivationConfigurationId;
    }

    public void setAccessActivationConfigurationId(String accessActivationConfigurationId) {
        this.accessActivationConfigurationId = accessActivationConfigurationId;
    }

    public String getWorkflowName() {
        return workflowName;
    }

    public void setWorkflowName(String workflowName) {
        this.workflowName = workflowName;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public List<OLEAccessActivationWorkFlow> getAccessActivationWorkflowList() {
        return accessActivationWorkflowList;
    }

    public void setAccessActivationWorkflowList(List<OLEAccessActivationWorkFlow> accessActivationWorkflowList) {
        this.accessActivationWorkflowList = accessActivationWorkflowList;
    }

    public String getWorkflowType() {
        return workflowType;
    }

    public void setWorkflowType(String workflowType) {
        this.workflowType = workflowType;
    }

    public String getWorkflowCompletionStatus() {
        return workflowCompletionStatus;
    }

    public void setWorkflowCompletionStatus(String workflowCompletionStatus) {
        this.workflowCompletionStatus = workflowCompletionStatus;
    }

    @Override
    public List<Collection<PersistableBusinessObject>> buildListOfDeletionAwareLists() {
        List<Collection<PersistableBusinessObject>> collectionList = new ArrayList<>();
        collectionList.add((Collection)getAccessActivationWorkflowList());
        return collectionList;
    }

    public String getSelector() {
        return selector;
    }

    public void setSelector(String selector) {
        this.selector = selector;
    }

    public String getMailId() {
        return mailId;
    }

    public void setMailId(String mailId) {
        this.mailId = mailId;
    }

    public String getMailContent() {
        return mailContent;
    }

    public void setMailContent(String mailContent) {
        this.mailContent = mailContent;
    }

    public String getRecipientUserId() {
        return recipientUserId;
    }

    public void setRecipientUserId(String recipientUserId) {
        this.recipientUserId = recipientUserId;
    }

    public String getRecipientUserName() {
        return recipientUserName;
    }

    public void setRecipientUserName(String recipientUserName) {
        this.recipientUserName = recipientUserName;
    }

    public String getRecipientRoleId() {
        return recipientRoleId;
    }

    public void setRecipientRoleId(String recipientRoleId) {
        this.recipientRoleId = recipientRoleId;
    }

    public String getRecipientRoleName() {
        return recipientRoleName;
    }

    public void setRecipientRoleName(String recipientRoleName) {
        this.recipientRoleName = recipientRoleName;
    }

    public boolean isMailNotification() {
        return mailNotification;
    }

    public void setMailNotification(boolean mailNotification) {
        this.mailNotification = mailNotification;
    }

    public String getNotificationSelector() {
        return notificationSelector;
    }

    public void setNotificationSelector(String notificationSelector) {
        this.notificationSelector = notificationSelector;
    }
}
