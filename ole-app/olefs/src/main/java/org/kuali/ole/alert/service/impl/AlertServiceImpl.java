package org.kuali.ole.alert.service.impl;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.alert.bo.ActionListAlertBo;
import org.kuali.ole.alert.bo.AlertBo;
import org.kuali.ole.alert.document.OlePersistableBusinessObjectBase;
import org.kuali.ole.alert.document.OleTransactionalDocumentBase;
import org.kuali.ole.alert.service.AlertService;
import org.kuali.rice.kim.api.group.Group;
import org.kuali.rice.kim.api.group.GroupService;
import org.kuali.rice.kim.api.identity.principal.Principal;
import org.kuali.rice.kim.api.role.Role;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;
import org.kuali.rice.kim.impl.group.GroupBo;
import org.kuali.rice.kim.impl.role.RoleBo;
import org.kuali.rice.krad.document.DocumentBase;
import org.kuali.rice.krad.maintenance.MaintenanceDocumentBase;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;
import java.sql.Date;
import java.util.*;

/**
 * Created by maheswarang on 11/10/14.
 */
public class AlertServiceImpl implements AlertService{
    private static final Logger LOG = Logger.getLogger(AlertHelperServiceImpl.class);
    private BusinessObjectService businessObjectService = KRADServiceLocator.getBusinessObjectService();
    private GroupService groupService = KimApiServiceLocator.getGroupService();
    private org.kuali.rice.kim.api.role.RoleService roleService = KimApiServiceLocator.getRoleService();

    /**
     * This method is used to update the alert table for the transaction document
     * @param documentBase
     */
    public void saveAlert(DocumentBase documentBase){
        LOG.info("Inside saveAlert for updating the alert table for the document with document Number :  " +documentBase.getDocumentNumber());
        List<AlertBo> alertBoList = new ArrayList<>();
        if(documentBase instanceof OleTransactionalDocumentBase){
            OleTransactionalDocumentBase oleTransactionalDocumentBase = (OleTransactionalDocumentBase)documentBase;
            if(oleTransactionalDocumentBase.getAlertBoList()!=null && oleTransactionalDocumentBase.getAlertBoList().size()>0){
                for(AlertBo alertBo : oleTransactionalDocumentBase.getAlertBoList()){
                    alertBo.setDocumentId(oleTransactionalDocumentBase.getDocumentNumber());
                    alertBoList.add(updateActionList(alertBo, oleTransactionalDocumentBase));
                }
            }
            oleTransactionalDocumentBase.setAlertBoList(alertBoList);
            oleTransactionalDocumentBase.setTempAlertBoList(retrieveApprovedAlertList(oleTransactionalDocumentBase.getDocumentNumber()));
        }
        else if(documentBase instanceof MaintenanceDocumentBase){
            MaintenanceDocumentBase maintenanceDocumentBase = (MaintenanceDocumentBase)documentBase;
            if(maintenanceDocumentBase.getDocumentDataObject() instanceof OlePersistableBusinessObjectBase){
                OlePersistableBusinessObjectBase olePersistableBusinessObjectBase = (OlePersistableBusinessObjectBase)maintenanceDocumentBase.getDocumentDataObject();
                if(olePersistableBusinessObjectBase!=null && olePersistableBusinessObjectBase.getAlertBoList()!=null && olePersistableBusinessObjectBase.getAlertBoList().size()>0){
                    for(AlertBo alertBo : olePersistableBusinessObjectBase.getAlertBoList()){
                        alertBo.setDocumentId(maintenanceDocumentBase.getDocumentNumber());
                        alertBoList.add(updateActionList(alertBo, maintenanceDocumentBase));
                    }
                }
                olePersistableBusinessObjectBase.setAlertBoList(alertBoList);
            }
        }

    }

    /**
     *This method is used to retrieve the alerts for the given document number
     * @param documentNumber
     * @return
     */
    public List<AlertBo> retrieveAlertList(String documentNumber){
        LOG.info("Inside the retrieveAlertList for getting the alerts related to the document with the document number : "+documentNumber);
        List<AlertBo> alertBos = new ArrayList<AlertBo>();
        List<AlertBo> unModifiableList = new ArrayList<AlertBo>();
        try{
            Map<String,String> actionMap = new HashMap();
            actionMap.put("documentId",documentNumber);
            unModifiableList = (List<AlertBo>)businessObjectService.findMatching(AlertBo.class,actionMap);
        }catch(Exception e){
            LOG.info("Exception occured while getting the alert information to the user : " + documentNumber);
            LOG.error(e,e);
        }
        if(unModifiableList!=null && unModifiableList.size()>0){
            for(AlertBo alertBo : unModifiableList){
                if (StringUtils.isEmpty(alertBo.getAlertApproverId())) {
                    String status = null;
                    if (alertBo.getAlertDate() != null) {
                        int dateCompare = alertBo.getAlertDate().compareTo(new Date(System.currentTimeMillis()));
                        if (dateCompare > 0) {
                            status = "Future";
                        } else if (dateCompare <= 0) {
                            status = "Active";
                        }

                        alertBo.setStatus(status);
                        alertBo.setAlertInitiatorName(getName(alertBo.getAlertInitiatorId()));
                        if (alertBo.getAlertModifierId() != null) {
                            alertBo.setAlertModifierName(getName(alertBo.getAlertModifierId()));
                        }
                        if (StringUtils.isNotBlank(alertBo.getReceivingUserId())) {
                            alertBo.setReceivingUserName(getName(alertBo.getReceivingUserId()));
                        }
                        if (StringUtils.isNotBlank(alertBo.getReceivingGroupId())) {
                            alertBo.setReceivingGroupName(getGroupName(alertBo.getReceivingGroupId()));
                        }
                        if (StringUtils.isNotBlank(alertBo.getReceivingRoleId())) {
                            alertBo.setReceivingRoleName(getRoleName(alertBo.getReceivingRoleId()));
                        }
                    }
                    alertBos.add(alertBo);
                }
            }
        }
        return alertBos;
    }

    public List<AlertBo> retrieveApprovedAlertList(String documentNumber) {
        List<AlertBo> alertBos = new ArrayList<AlertBo>();
        List<AlertBo> approvedAlertBos = new ArrayList<AlertBo>();
        try{
            Map<String,String> actionMap = new HashMap();
            actionMap.put("documentId",documentNumber);
            approvedAlertBos = (List<AlertBo>)businessObjectService.findMatching(AlertBo.class,actionMap);
        }catch(Exception e){
            LOG.info("Exception occured while getting the alert information to the user : " + documentNumber);
            LOG.error(e,e);
        }
        if(CollectionUtils.isNotEmpty(approvedAlertBos)) {
            for(AlertBo alertBo : approvedAlertBos) {
                if(StringUtils.isNotEmpty(alertBo.getAlertApproverId())) {
                    alertBos.add(alertBo);
                }
            }
        }
        return alertBos;
    }

    /**
     *This method is used to delete the alerts related to the given document number
     * @param documentNumber
     */
    public void deleteAlerts(String documentNumber){
        LOG.info("Inside the deleteAlerts for deleting the alerts related to the document with the document number : "+documentNumber);
        Map<String,String> actionMap = new HashMap<>();
        actionMap.put("documentId",documentNumber);
        businessObjectService.deleteMatching(AlertBo.class, actionMap);
        deleteActionListAlerts(documentNumber);
    }

    /**
     *This method is used to delete the alerts in the action list for the given document number
     * @param documentNumber
     */
    public void deleteActionListAlerts(String documentNumber){
        LOG.info("Inside the deleteActionListAlerts for deleting the alerts in the action list related to the document with the document number : "+documentNumber);
        Map<String,String> actionMap = new HashMap<>();
        actionMap.put("documentId",documentNumber);
        businessObjectService.deleteMatching(ActionListAlertBo.class, actionMap);
    }


    /**
     *This method is used to update the alerts in the action list for the given document
     * @param alertBo
     * @param documentBase
     */
    public AlertBo updateActionList(AlertBo alertBo,DocumentBase documentBase){
        LOG.info("Inside the updateActionList for  updating the alerts in the action list related to the document with the document number : "+documentBase.getDocumentNumber() + " for an alert with the alert id : " +alertBo.getAlertId());
        List<ActionListAlertBo> actionListAlertBos = new ArrayList<ActionListAlertBo>();
        ActionListAlertBo actionListAlertBo = new ActionListAlertBo();
        AlertBo alertBo1 = new AlertBo();
        alertBo1.setDocumentId(alertBo.getDocumentId());
        alertBo1.setAlertDate(alertBo.getAlertDate());
        alertBo1.setAlertCreateDate(alertBo.getAlertCreateDate());
        alertBo1.setAlertInitiatorId(alertBo.getAlertInitiatorId());
        alertBo1.setAlertModifiedDate(alertBo.getAlertModifiedDate());
        alertBo1.setAlertInitiatorId(alertBo.getAlertInitiatorId());
        alertBo1.setAlertModifierId(alertBo.getAlertModifierId());
        alertBo1.setAlertNote(alertBo.getAlertNote());
        alertBo1.setReceivingUserId(alertBo.getReceivingUserId());
        alertBo1.setAlertStatus(alertBo.isAlertStatus());
        alertBo1.setStatus(alertBo.getStatus());
        alertBo1.setAlertApproverId(alertBo.getAlertApproverId());
        alertBo1.setAlertApprovedDate(alertBo.getAlertApprovedDate());
        alertBo1.setReceivingGroupId(alertBo.getReceivingGroupId());
        alertBo1.setReceivingRoleId(alertBo.getReceivingRoleId());
        alertBo1.setReceivingRoleName(alertBo.getReceivingRoleName());
        alertBo1.setReceivingGroupName(alertBo.getReceivingGroupName());
        alertBo1.setReceivingUserName(alertBo.getReceivingUserName());
        alertBo1.setAlertSelector(alertBo.getAlertSelector());
        alertBo1 = businessObjectService.save(alertBo1);

        if(documentBase instanceof  OleTransactionalDocumentBase){
            OleTransactionalDocumentBase oleTransactionalDocumentBase = (OleTransactionalDocumentBase)documentBase;
            if(StringUtils.isNotBlank(alertBo.getAlertSelector())) {
                if(alertBo.getAlertSelector().equalsIgnoreCase(OLEConstants.SELECTOR_ROLE)) {
                    List<String> roleIds = new ArrayList<String>();
                    roleIds.add(alertBo.getReceivingRoleId());
                    Role role =  roleService.getRole(alertBo.getReceivingRoleId());
                    Collection collection  =  (Collection)roleService.getRoleMemberPrincipalIds(role.getNamespaceCode(),role.getName(),new HashMap<String, String>());
                    List<String> memberIds = new ArrayList<String>();
                    memberIds.addAll(collection);
                    if(CollectionUtils.isNotEmpty(memberIds)) {
                        for(String receivingUserId : memberIds) {
                            actionListAlertBo = getActionListAlertBo(alertBo1, oleTransactionalDocumentBase.getDocumentHeader().getWorkflowDocument().getDocumentTypeName(), oleTransactionalDocumentBase.getDocumentTitle(), receivingUserId);
                            actionListAlertBos.add(actionListAlertBo);
                        }
                    }
                } else if(alertBo.getAlertSelector().equalsIgnoreCase(OLEConstants.SELECTOR_GROUP)) {
                    List<String> memberIds = groupService.getMemberPrincipalIds(alertBo.getReceivingGroupId());
                    if(CollectionUtils.isNotEmpty(memberIds)) {
                        for(String receivingUserId : memberIds) {
                            actionListAlertBo = getActionListAlertBo(alertBo1, oleTransactionalDocumentBase.getDocumentHeader().getWorkflowDocument().getDocumentTypeName(), oleTransactionalDocumentBase.getDocumentTitle(), receivingUserId);
                            actionListAlertBos.add(actionListAlertBo);
                        }
                    }
                } else if(alertBo.getAlertSelector().equalsIgnoreCase(OLEConstants.SELECTOR_PERSON)) {
                    actionListAlertBo = getActionListAlertBo(alertBo1, oleTransactionalDocumentBase.getDocumentHeader().getWorkflowDocument().getDocumentTypeName(), oleTransactionalDocumentBase.getDocumentTitle(), alertBo1.getReceivingUserId());
                    actionListAlertBos.add(actionListAlertBo);
                }
            }
        }

        if(documentBase instanceof  MaintenanceDocumentBase){
            MaintenanceDocumentBase maintenanceDocumentBase = (MaintenanceDocumentBase)documentBase;
            if(StringUtils.isNotBlank(alertBo.getAlertSelector())) {
                if(alertBo.getAlertSelector().equalsIgnoreCase(OLEConstants.SELECTOR_ROLE)) {
                    List<String> roleIds = new ArrayList<String>();
                    roleIds.add(alertBo.getReceivingRoleId());
                    Role role =  roleService.getRole(alertBo.getReceivingRoleId());
                    Collection collection  =  (Collection)roleService.getRoleMemberPrincipalIds(role.getNamespaceCode(),role.getName(),new HashMap<String, String>());
                    List<String> memberIds = new ArrayList<String>();
                    memberIds.addAll(collection);
                    if(CollectionUtils.isNotEmpty(memberIds)) {
                        for(String receivingUserId : memberIds) {
                            actionListAlertBo = getActionListAlertBo(alertBo1, maintenanceDocumentBase.getDocumentHeader().getWorkflowDocument().getDocumentTypeName(), maintenanceDocumentBase.getDocumentTitle(), receivingUserId);
                            actionListAlertBos.add(actionListAlertBo);
                        }
                    }
                } else if(alertBo.getAlertSelector().equalsIgnoreCase(OLEConstants.SELECTOR_GROUP)) {
                    List<String> memberIds = groupService.getMemberPrincipalIds(alertBo.getReceivingGroupId());
                    if(CollectionUtils.isNotEmpty(memberIds)) {
                        for(String receivingUserId : memberIds) {
                            actionListAlertBo = getActionListAlertBo(alertBo1, maintenanceDocumentBase.getDocumentHeader().getWorkflowDocument().getDocumentTypeName(), maintenanceDocumentBase.getDocumentTitle(), receivingUserId);
                            actionListAlertBos.add(actionListAlertBo);
                        }
                    }
                } else if(alertBo.getAlertSelector().equalsIgnoreCase(OLEConstants.SELECTOR_PERSON)) {
                    actionListAlertBo = getActionListAlertBo(alertBo1, maintenanceDocumentBase.getDocumentHeader().getWorkflowDocument().getDocumentTypeName(), maintenanceDocumentBase.getDocumentTitle(), alertBo1.getReceivingUserId());
                    actionListAlertBos.add(actionListAlertBo);
                }
            }
        }
        businessObjectService.save(actionListAlertBos);
        return alertBo1;
    }


    public List<AlertBo> getAlertBo(AlertBo alertBo,List<String> receivingPrincipalIds,boolean roleMember,boolean groupMember){
        AlertBo alertBo1 = null;
        List<AlertBo> alertBos = new ArrayList<AlertBo>();
        for(String receivingUserId : receivingPrincipalIds){
            alertBo1 = new AlertBo();
            alertBo1.setDocumentId(alertBo.getDocumentId());
            alertBo1.setAlertDate(alertBo.getAlertDate());
            alertBo1.setAlertCreateDate(alertBo.getAlertCreateDate());
            alertBo1.setAlertInitiatorId(alertBo.getAlertInitiatorId());
            alertBo1.setAlertModifiedDate(alertBo.getAlertModifiedDate());
            alertBo1.setAlertInitiatorId(alertBo.getAlertInitiatorId());
            alertBo1.setAlertInitiatorName(alertBo.getAlertInitiatorName());
            alertBo1.setAlertModifierId(alertBo.getAlertModifierId());
            alertBo1.setAlertNote(alertBo.getAlertNote());
            alertBo1.setReceivingUserId(receivingUserId);
            alertBo1.setAlertStatus(alertBo.isAlertStatus());
            alertBo1.setAlertApproverId(alertBo.getAlertApproverId());
            alertBo1.setAlertApprovedDate(alertBo.getAlertApprovedDate());
            alertBo1.setReceivingUserName(getName(receivingUserId));
            alertBo1.setAlertDetails(alertBo.getAlertDetails());
            if(groupMember){
            alertBo1.setReceivingGroupId(alertBo.getReceivingGroupId());
            alertBo1.setReceivingGroupName(getGroupName(alertBo.getReceivingGroupId()));

            }if(roleMember){
            alertBo1.setReceivingRoleId(alertBo.getReceivingRoleId());
            alertBo1.setReceivingRoleName(alertBo.getReceivingRoleName());
            }
                alertBo1.setStatus(alertBo.getStatus());
            alertBos.add(alertBo1);
        }
        return alertBos;
    }



    public ActionListAlertBo getActionListAlertBo(AlertBo alertBo,String documentTypeName,String title,String alertUserId){
        ActionListAlertBo actionListAlertBo = new ActionListAlertBo();
        actionListAlertBo.setDocumentId(alertBo.getDocumentId());
        actionListAlertBo.setActive(alertBo.isAlertStatus());
        actionListAlertBo.setAlertDate(alertBo.getAlertDate());
        actionListAlertBo.setRecordType(documentTypeName);
        actionListAlertBo.setTitle(title);
        actionListAlertBo.setNote(alertBo.getAlertNote());
        actionListAlertBo.setAlertUserId(alertUserId);
        actionListAlertBo.setAlertInitiatorId(alertBo.getAlertInitiatorId());
        actionListAlertBo.setAlertApproverId(alertBo.getAlertApproverId());
        actionListAlertBo.setAlertApprovedDate(alertBo.getAlertApprovedDate());
        actionListAlertBo.setAlertId(alertBo.getAlertId());
        return actionListAlertBo;
    }





    /**
     *
     * @param principalId
     * @return
     */
    public String getName(String principalId){
        Principal principal = KimApiServiceLocator.getIdentityService().getPrincipal(principalId);
        if(principal != null) {
            return principal.getPrincipalName();
        } else {
            return null;
        }
    }


    /**
     * This method returns the group name
     * @param groupId
     * @return
     */
    public String getGroupName(String groupId){
        Group group = groupService.getGroup(groupId);
        if(group != null) {
            return  group.getName();
        } else {
            return null;
        }
    }


    public String getRoleName(String roleId){
        Role role = KimApiServiceLocator.getRoleService().getRole(roleId);
        if(role != null) {
            return role.getName();
        } else {
            return null;
        }
    }


    public String getPersonId(String personName){
        Principal principal = KimApiServiceLocator.getIdentityService().getPrincipalByPrincipalName(personName);
        if(principal != null) {
            return principal.getPrincipalId();
        } else {
            return null;
        }
    }

    public String getGroupId(String groupName){
        GroupBo group = null;
        Map<String,String> map = new HashMap<>();
        map.put(OLEConstants.ALERT_GROUP_NAME, groupName);
        List<GroupBo> groupBoList = (List<GroupBo>) KRADServiceLocator.getBusinessObjectService().findMatching(GroupBo.class, map);
        if(CollectionUtils.isNotEmpty(groupBoList)) {
            group = groupBoList.get(0);
        }
        if(group != null) {
            return group.getId();
        } else {
            return null;
        }
    }

    public String getRoleId(String roleName){
        RoleBo role = null;
        Map<String,String> map = new HashMap<>();
        map.put(OLEConstants.ALERT_ROLE_NAME,roleName);
        List<RoleBo> roleBoList = (List<RoleBo>) KRADServiceLocator.getBusinessObjectService()
                .findMatching(RoleBo.class, map);
        if(CollectionUtils.isNotEmpty(roleBoList)) {
             role = roleBoList.get(0);
        }
        if(role != null) {
            return role.getId();
        } else {
            return null;
        }
    }

}
