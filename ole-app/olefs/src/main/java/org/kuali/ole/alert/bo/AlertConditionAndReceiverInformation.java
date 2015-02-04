package org.kuali.ole.alert.bo;

import org.kuali.ole.alert.bo.AlertDocument;
import org.kuali.ole.alert.bo.AlertEvent;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;
import org.kuali.rice.kim.framework.type.KimTypeService;
import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

import java.sql.Date;

/**
 * Created by maheswarang on 8/12/14.
 */
public class AlertConditionAndReceiverInformation extends PersistableBusinessObjectBase {

    private String alertConditionId;

    private String groupId;

    private String roleId;

    private String principalId;

    private String alertEventId;

    private String alertEventName;

    private String alertNote;

    private boolean email;

    private boolean alert;

    /*private Date alertDate;*/

    private String alertInterval;

    private String alertDocumentId;

    private AlertDocument alertDocument;

    private AlertEvent alertEvent;

    private String principalName;

    private String groupName;

    private String roleName;


    public String getAlertConditionId() {
        return alertConditionId;
    }

    public void setAlertConditionId(String alertConditionId) {
        this.alertConditionId = alertConditionId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getPrincipalId() {
        return principalId;
    }

    public void setPrincipalId(String principalId) {
        this.principalId = principalId;
    }

    public String getAlertNote() {
        return alertNote;
    }

    public void setAlertNote(String alertNote) {
        this.alertNote = alertNote;
    }

    public String getAlertEventName() {
        if(alertEventName == null && alertEvent!=null){
            alertEventName = alertEvent.getAlertEventName();
        }
        return alertEventName;
    }

    public void setAlertEventName(String alertEventName) {
        this.alertEventName = alertEventName;
    }

    public boolean isEmail() {
        return email;
    }

    public void setEmail(boolean email) {
        this.email = email;
    }

    public boolean isAlert() {
        return alert;
    }

    public void setAlert(boolean alert) {
        this.alert = alert;
    }

/*    public Date getAlertDate() {
        return alertDate;
    }

    public void setAlertDate(Date alertDate) {
        this.alertDate = alertDate;
    }*/

    public String getAlertInterval() {
        return alertInterval;
    }

    public void setAlertInterval(String alertInterval) {
        this.alertInterval = alertInterval;
    }

    public String getAlertDocumentId() {
        return alertDocumentId;
    }

    public void setAlertDocumentId(String alertDocumentId) {
        this.alertDocumentId = alertDocumentId;
    }

    public AlertDocument getAlertDocument() {
        return alertDocument;
    }

    public void setAlertDocument(AlertDocument alertDocument) {
        this.alertDocument = alertDocument;
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

    public String getPrincipalName() {
        if((principalName == null || (principalName!=null && principalName.trim().isEmpty())) && (principalId!=null && !principalId.trim().isEmpty())){
            try{
            principalName= KimApiServiceLocator.getPersonService().getPerson(principalId).getPrincipalName();
            }catch(Exception e){
                e.printStackTrace();
            }
            }
        return principalName;
    }

    public void setPrincipalName(String principalName) {
        this.principalName = principalName;
    }

    public String getGroupName() {
        if((groupName == null || (groupName!=null && groupName.trim().isEmpty())) && (groupId!=null && !groupId.trim().isEmpty())){
           try{
            groupName = KimApiServiceLocator.getGroupService().getGroup(groupId).getName();
           }catch(Exception e){
               e.printStackTrace();
           }
        }
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getRoleName() {
        if((roleName == null || (roleName!=null && roleName.trim().isEmpty())) && (roleId!=null && !roleId.trim().isEmpty())){
            try{
                roleName = KimApiServiceLocator.getRoleService().getRole(roleId).getName();
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
}
