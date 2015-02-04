package org.kuali.ole.alert.service;

import org.kuali.ole.alert.bo.AlertConditionAndReceiverInformation;
import org.kuali.ole.alert.bo.AlertDocument;
import org.kuali.ole.alert.bo.AlertBo;
import org.kuali.ole.alert.bo.AlertEvent;
import org.kuali.ole.alert.bo.AlertEventField;
import org.kuali.ole.alert.document.OlePersistableBusinessObjectBase;
import org.kuali.ole.alert.document.OleTransactionalDocumentBase;
import org.kuali.ole.alert.service.impl.AlertServiceImpl;
import org.kuali.rice.core.api.util.type.KualiDecimal;
import org.kuali.rice.kim.api.group.GroupService;
import org.kuali.rice.kim.api.role.Role;
import org.kuali.rice.kim.api.role.RoleService;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;
import org.kuali.rice.krad.document.Document;
import org.kuali.rice.krad.maintenance.MaintenanceDocumentBase;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krad.util.GlobalVariables;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.*;
import java.util.*;
import java.util.Date;

/**
 * Created by maheswarang on 12/26/14.
 */
public class AlertGlobalConfigurationServiceImpl {
    private BusinessObjectService businessObjectService;
    private GroupService groupService = KimApiServiceLocator.getGroupService();
    private RoleService roleService = KimApiServiceLocator.getRoleService();
    private AlertServiceImpl alertService = new AlertServiceImpl();
    public BusinessObjectService getBusinessObjectService() {
        if(businessObjectService == null){
            businessObjectService = KRADServiceLocator.getBusinessObjectService();
        }
        return businessObjectService;
    }

    public void setBusinessObjectService(BusinessObjectService businessObjectService) {
        this.businessObjectService = businessObjectService;
    }



    public void processAlert(Document document){

        List<AlertBo> alertBos = new ArrayList<>();
        if(document instanceof OleTransactionalDocumentBase){
            OleTransactionalDocumentBase oleTransactionalDocumentBase = (OleTransactionalDocumentBase)document;
            alertBos =   processDocument(oleTransactionalDocumentBase,oleTransactionalDocumentBase.getDocumentHeader().getWorkflowDocument().getDocumentTypeName(),oleTransactionalDocumentBase.getDocumentNumber());
           if(alertBos!=null && alertBos.size()>0){
               for(AlertBo alert : alertBos){
                   alertService.updateActionList(alert,oleTransactionalDocumentBase);
               }
           }

        }
        if(document instanceof MaintenanceDocumentBase){
            MaintenanceDocumentBase maintenanceDocumentBase = (MaintenanceDocumentBase) document;
            if(maintenanceDocumentBase.getDocumentDataObject() instanceof OlePersistableBusinessObjectBase){
                OlePersistableBusinessObjectBase olePersistableBusinessObjectBase = (OlePersistableBusinessObjectBase)maintenanceDocumentBase.getDocumentDataObject();
                alertBos = processDocument(olePersistableBusinessObjectBase,maintenanceDocumentBase.getDocumentHeader().getWorkflowDocument().getDocumentTypeName(),maintenanceDocumentBase.getDocumentNumber());
                if(alertBos!=null && alertBos.size()>0){
                    for(AlertBo alert : alertBos){
                        olePersistableBusinessObjectBase.getAlertBoList().add(alert);
                        alertService.updateActionList(alert,maintenanceDocumentBase);
                    }
                }
            }
    }


    }


    public List<AlertBo> processDocument(Object olePersistableBusinessObjectBase,String documentTypeName,String documentNumber){
        AlertDocument alertDocument = getAlertDocument(documentTypeName);
        List<AlertBo> alertBoList = new ArrayList<AlertBo>();
        AlertBo alertBo = null;
        if(alertDocument!=null && alertDocument.getAlertConditionAndReceiverInformations()!=null && alertDocument.getAlertConditionAndReceiverInformations().size()>0){
            for(AlertConditionAndReceiverInformation alertConditionAndReceiverInformation : alertDocument.getAlertConditionAndReceiverInformations()){
                if(processEvent(alertConditionAndReceiverInformation.getAlertEvent(),olePersistableBusinessObjectBase)){
                  alertBoList = generateAlertList(alertConditionAndReceiverInformation,documentNumber,alertDocument.isRepeatable());
                }
            }
        }
        return alertBoList;
    }






    public AlertDocument getAlertDocument(String documentTypeName){
        Map<String,String> alertDocumentMap = new HashMap<String,String>();
        AlertDocument alertDocument= null;
        alertDocumentMap.put("documentTypeName",documentTypeName);
        List<AlertDocument> alertDocumentList = (List<AlertDocument>)getBusinessObjectService().findMatching(AlertDocument.class,alertDocumentMap);
        if(alertDocumentList!=null && alertDocumentList.size()>0){
            alertDocument = alertDocumentList.get(0);
        }

        return alertDocument;
    }


    public boolean processEvent(AlertEvent alertEvent,Object olePersistableBusinessObjectBase){
       boolean processed = true;
        if(alertEvent.getAlertEventFieldList()!=null && alertEvent.getAlertEventFieldList().size()>0){
            for(AlertEventField alertEventField  :  alertEvent.getAlertEventFieldList()){
                 if(!processEventField(alertEventField,olePersistableBusinessObjectBase)){
                     processed = false;
                 break;
                 }
            }

        }

            return processed;
    }




public boolean processEventField(AlertEventField alertEventField , Object olePersistableBusinessObjectBase){
    boolean processed = true;

    if(alertEventField.getAlertFieldType().equals("boolean")){
        processed = validateBooleanField(alertEventField.getAlertFieldName(), alertEventField.getAlertFieldValue(), alertEventField.getAlertCriteria(), olePersistableBusinessObjectBase);
    }
   else if(alertEventField.getAlertFieldType().equals("String")){
       processed =validateStringField(alertEventField.getAlertFieldName(),alertEventField.getAlertFieldValue(),alertEventField.getAlertCriteria(),olePersistableBusinessObjectBase);
    }
    else if(alertEventField.getAlertFieldType().equals("Integer")){
        processed =validateIntegerField(alertEventField.getAlertFieldName(),alertEventField.getAlertFieldValue(),alertEventField.getAlertCriteria(),olePersistableBusinessObjectBase);
    }
    else if(alertEventField.getAlertFieldType().equals("BigDecimal")){
        processed =validateBigDecimalField(alertEventField.getAlertFieldName(),alertEventField.getAlertFieldValue(),alertEventField.getAlertCriteria(),olePersistableBusinessObjectBase);
    }

    else if(alertEventField.getAlertFieldType().equals("KualiDecimal")){
        processed =validateKualiDecimalField(alertEventField.getAlertFieldName(),alertEventField.getAlertFieldValue(),alertEventField.getAlertCriteria(),olePersistableBusinessObjectBase);
    }

    else if(alertEventField.getAlertFieldType().equals("Timestamp")){
        processed =validateTimeStampField(alertEventField.getAlertFieldName(),alertEventField.getAlertFieldValue(),alertEventField.getAlertCriteria(),olePersistableBusinessObjectBase);
    }
    else if(alertEventField.getAlertFieldType().equals("util.Date")){
        processed =validateUtilDateField(alertEventField.getAlertFieldName(),alertEventField.getAlertFieldValue(),alertEventField.getAlertCriteria(),olePersistableBusinessObjectBase);
    }
    else if(alertEventField.getAlertFieldType().equals("sql.Date")){
        processed =validateSqlDateField(alertEventField.getAlertFieldName(),alertEventField.getAlertFieldValue(),alertEventField.getAlertCriteria(),olePersistableBusinessObjectBase);
    }

    return processed;
}





public boolean validateBooleanField(String fieldName,String fieldValue,String criteria,Object olePersistableBusinessObjectBase){
    boolean processed = false;
    boolean objectValue = (boolean)getObjectValue(fieldName, olePersistableBusinessObjectBase);
    boolean exactFieldValue = false;
    if(fieldValue.equals("true") || fieldValue.equalsIgnoreCase("Y")){
        exactFieldValue = true;
    }
    if(criteria.equals("equalTo")){
    if(exactFieldValue = objectValue){
        processed = true;
    }
    }else if(criteria.equals("notEqualTo")){
        if(exactFieldValue != objectValue){
            processed = true;
        }
    }
return processed;
}



public Object getObjectValue(String fieldName,Object olePersistableBusinessObjectBase){
    Class clas = olePersistableBusinessObjectBase.getClass();
    Object value = null;
    try{
    Field field = clas.getDeclaredField(fieldName);
        field.setAccessible(true);
      value = field.get(olePersistableBusinessObjectBase);
    }catch(Exception e){

    }
    return value;
}
 
    public boolean validateStringField(String fieldName,String fieldValue,String criteria,Object olePersistableBusinessObjectBase){

        boolean processed = false;
        String objectValue = (String)getObjectValue(fieldName, olePersistableBusinessObjectBase);
        if(criteria.equals("equalTo")){
            if(objectValue.equalsIgnoreCase(fieldValue)){
                processed = true;
            }
        }else if(criteria.equals("notEqualTo")){
            if(!objectValue.equalsIgnoreCase(fieldValue)){
                processed = true;
            }
        }

        return processed;
    }

    public boolean validateIntegerField(String fieldName,String fieldValue,String criteria,Object olePersistableBusinessObjectBase){
        boolean processed = false;
        Integer objectValue = (Integer)getObjectValue(fieldName, olePersistableBusinessObjectBase);
        Integer exactFieldValue = new Integer(fieldValue);

        if(criteria.equals("equalTo")){
            if(objectValue.compareTo(exactFieldValue) == 0){
                processed = true;
            }
        }else if(criteria.equals("notEqualTo")){
            if(objectValue.compareTo(exactFieldValue) != 0){
                processed = true;
            }
        }else if (criteria.equals("greaterThan")){
            if(objectValue.compareTo(exactFieldValue) > 0){
                processed = true;
            }
        }else if(criteria.equals("lessThan")){
            if(objectValue.compareTo(exactFieldValue) < 0 ){
                processed = true;
            }
        }else if (criteria.equals("greaterThanOrEqualTo")){
            if(objectValue.compareTo(exactFieldValue) >= 0){
                processed = true;
            }
        }else if(criteria.equals("lessThanOrEqualTo")){
            if(objectValue.compareTo(exactFieldValue) <= 0){
                processed = true;
            }
        }
   return processed;
    }



    public boolean validateBigDecimalField(String fieldName,String fieldValue,String criteria,Object olePersistableBusinessObjectBase){
        boolean processed = false;
        BigDecimal objectValue = (BigDecimal)getObjectValue(fieldName, olePersistableBusinessObjectBase);
        double doubleValue = Double.valueOf(fieldValue);
        BigDecimal exactFieldValue = BigDecimal.valueOf(doubleValue);

        if(criteria.equals("equalTo")){
            if(objectValue.compareTo(exactFieldValue) == 0){
                processed = true;
            }
        }else if(criteria.equals("notEqualTo")){
            if(objectValue.compareTo(exactFieldValue) != 0){
                processed = true;
            }
        }else if (criteria.equals("greaterThan")){
            if(objectValue.compareTo(exactFieldValue) > 0){
                processed = true;
            }
        }else if(criteria.equals("lessThan")){
            if(objectValue.compareTo(exactFieldValue) < 0 ){
                processed = true;
            }
        }else if (criteria.equals("greaterThanOrEqualTo")){
            if(objectValue.compareTo(exactFieldValue) >= 0){
                processed = true;
            }
        }else if(criteria.equals("lessThanOrEqualTo")){
            if(objectValue.compareTo(exactFieldValue) <= 0){
                processed = true;
            }
        }
        return processed;
    }

    public boolean validateKualiDecimalField(String fieldName,String fieldValue,String criteria,Object olePersistableBusinessObjectBase){
        boolean processed = false;
        KualiDecimal objectValue = (KualiDecimal)getObjectValue(fieldName, olePersistableBusinessObjectBase);

        KualiDecimal exactFieldValue = new KualiDecimal(fieldValue);
        if(criteria.equals("equalTo")){
            if(objectValue.compareTo(exactFieldValue) == 0){
                processed = true;
            }
        }else if(criteria.equals("notEqualTo")){
            if(objectValue.compareTo(exactFieldValue) != 0){
                processed = true;
            }
        }else if (criteria.equals("greaterThan")){
            if(objectValue.compareTo(exactFieldValue) > 0){
                processed = true;
            }
        }else if(criteria.equals("lessThan")){
            if(objectValue.compareTo(exactFieldValue) < 0 ){
                processed = true;
            }
        }else if (criteria.equals("greaterThanOrEqualTo")){
            if(objectValue.compareTo(exactFieldValue) >= 0){
                processed = true;
            }
        }else if(criteria.equals("lessThanOrEqualTo")){
            if(objectValue.compareTo(exactFieldValue) <= 0){
                processed = true;
            }
        }
        return processed;
    }



    public boolean validateUtilDateField(String fieldName,String fieldValue,String criteria,Object olePersistableBusinessObjectBase){
        boolean processed = false;
        Date objectValue = (Date)getObjectValue(fieldName, olePersistableBusinessObjectBase);
        Date exactFieldValue = new Date(fieldValue);
        if(criteria.equals("equalTo")){
            if(objectValue.compareTo(exactFieldValue) == 0){
                processed = true;
            }
        }else if(criteria.equals("notEqualTo")){
            if(objectValue.compareTo(exactFieldValue) != 0){
                processed = true;
            }
        }else if (criteria.equals("greaterThan")){
            if(objectValue.compareTo(exactFieldValue) > 0){
                processed = true;
            }
        }else if(criteria.equals("lessThan")){
            if(objectValue.compareTo(exactFieldValue) < 0 ){
                processed = true;
            }
        }else if (criteria.equals("greaterThanOrEqualTo")){
            if(objectValue.compareTo(exactFieldValue) >= 0){
                processed = true;
            }
        }else if(criteria.equals("lessThanOrEqualTo")){
            if(objectValue.compareTo(exactFieldValue) <= 0){
                processed = true;
            }
        }
        return processed;
    }


    public boolean validateSqlDateField(String fieldName,String fieldValue,String criteria,Object olePersistableBusinessObjectBase){
        boolean processed = false;
        java.sql.Date objectValue = (java.sql.Date)getObjectValue(fieldName, olePersistableBusinessObjectBase);
        Date utilDate = new Date(fieldValue);
        java.sql.Date exactFieldValue = new java.sql.Date(utilDate.getTime());
        if(criteria.equals("equalTo")){
            if(objectValue.compareTo(exactFieldValue) == 0){
                processed = true;
            }
        }else if(criteria.equals("notEqualTo")){
            if(objectValue.compareTo(exactFieldValue) != 0){
                processed = true;
            }
        }else if (criteria.equals("greaterThan")){
            if(objectValue.compareTo(exactFieldValue) > 0){
                processed = true;
            }
        }else if(criteria.equals("lessThan")){
            if(objectValue.compareTo(exactFieldValue) < 0 ){
                processed = true;
            }
        }else if (criteria.equals("greaterThanOrEqualTo")){
            if(objectValue.compareTo(exactFieldValue) >= 0){
                processed = true;
            }
        }else if(criteria.equals("lessThanOrEqualTo")){
            if(objectValue.compareTo(exactFieldValue) <= 0){
                processed = true;
            }
        }
        return processed;
    }

    public boolean validateTimeStampField(String fieldName,String fieldValue,String criteria,Object olePersistableBusinessObjectBase){
        boolean processed = false;
        Timestamp objectValue = (Timestamp)getObjectValue(fieldName, olePersistableBusinessObjectBase);
        Date utilDate = new Date(fieldValue);
        Timestamp exactFieldValue = Timestamp.valueOf(fieldName);
        if(criteria.equals("equalTo")){
            if(objectValue.compareTo(exactFieldValue) == 0){
                processed = true;
            }
        }else if(criteria.equals("notEqualTo")){
            if(objectValue.compareTo(exactFieldValue) != 0){
                processed = true;
            }
        }else if (criteria.equals("greaterThan")){
            if(objectValue.compareTo(exactFieldValue) > 0){
                processed = true;
            }
        }else if(criteria.equals("lessThan")){
            if(objectValue.compareTo(exactFieldValue) < 0 ){
                processed = true;
            }
        }else if (criteria.equals("greaterThanOrEqualTo")){
            if(objectValue.compareTo(exactFieldValue) >= 0){
                processed = true;
            }
        }else if(criteria.equals("lessThanOrEqualTo")){
            if(objectValue.compareTo(exactFieldValue) <= 0){
                processed = true;
            }
        }
        return processed;
    }



    public List<AlertBo> generateAlertList(AlertConditionAndReceiverInformation alertConditionAndReceiverInformation,String documentNumber,boolean isRepeatable){
        List<AlertBo> alertBoList = new ArrayList<AlertBo>();
        String initiatorId = GlobalVariables.getUserSession().getPrincipalId();
        String principalName = GlobalVariables.getUserSession().getPrincipalName();
        AlertBo alertBo = null;
        String receivingUserName=null;
        String groupName = null;
        if(alertConditionAndReceiverInformation.getPrincipalId()!=null){
            receivingUserName = KimApiServiceLocator.getPersonService().getPerson(alertConditionAndReceiverInformation.getPrincipalId()).getPrincipalName();
            alertBo = getAlert(documentNumber,alertConditionAndReceiverInformation.getAlertInterval(),isRepeatable,initiatorId,alertConditionAndReceiverInformation.getAlertNote(),principalName,alertConditionAndReceiverInformation.getPrincipalId(),receivingUserName,null,null,null,null);
            alertBoList.add(alertBo);
        }
        if(alertConditionAndReceiverInformation.getGroupId()!=null){
                            List<String> memberIds = groupService.getMemberPrincipalIds(alertConditionAndReceiverInformation.getGroupId());
            groupName = KimApiServiceLocator.getGroupService().getGroup(alertConditionAndReceiverInformation.getGroupId()).getName();
            if(memberIds!=null && memberIds.size()>0){
                for(String memberId :memberIds ){
                    receivingUserName = KimApiServiceLocator.getPersonService().getPerson(memberId).getPrincipalName();

                 alertBo = getAlert(documentNumber,alertConditionAndReceiverInformation.getAlertInterval(),isRepeatable,initiatorId,alertConditionAndReceiverInformation.getAlertNote(),principalName,memberId,receivingUserName,null,null,alertConditionAndReceiverInformation.getGroupId(),groupName);
               alertBoList.add(alertBo);
                }
            }
            }
        if(alertConditionAndReceiverInformation.getRoleId()!=null){
            List<String> roleIds = new ArrayList<String>();
            roleIds.add(alertConditionAndReceiverInformation.getRoleId());
           Role role =  roleService.getRole(alertConditionAndReceiverInformation.getRoleId());
          Collection collection  =  (Collection)roleService.getRoleMemberPrincipalIds(role.getNamespaceCode(),role.getName(),null);
         List<String> memberIds = new ArrayList<String>();
            memberIds.addAll(collection);
            if(memberIds!=null && memberIds.size()>0){
             for(String memberId : memberIds){
                 receivingUserName = KimApiServiceLocator.getPersonService().getPerson(memberId).getPrincipalName();
                 alertBo = getAlert(documentNumber,alertConditionAndReceiverInformation.getAlertInterval(),isRepeatable,initiatorId,alertConditionAndReceiverInformation.getAlertNote(),principalName,memberId,receivingUserName,alertConditionAndReceiverInformation.getRoleId(),role.getName(),null,null);
            alertBoList.add(alertBo);
             }
           }
            }
        return alertBoList;
        }





    public AlertBo getAlert(String documentId,String alertInterval,boolean isRepeatable,String initiatorId,String note,String principalName,String receivingUserId,String receivingUserName,String roleId,String roleName,String groupId,String groupName){
        AlertBo alertBo = new AlertBo();
        alertBo.setDocumentId(documentId);
        alertBo.setAlertDate(new java.sql.Date(System.currentTimeMillis()));
        alertBo.setAlertCreateDate(new java.sql.Date(System.currentTimeMillis()));
        alertBo.setAlertInitiatorId(initiatorId);
        alertBo.setAlertInitiatorName(principalName);
        alertBo.setAlertNote(note);
        alertBo.setAlertStatus(true);
        if(alertInterval !=null && !alertInterval.trim().isEmpty()){
        alertBo.setAlertInterval(alertInterval);
        }
        if(alertBo.isRepeatable()){
            alertBo.setRepeatable(true);
        }
        alertBo.setReceivingUserId(receivingUserId);
        alertBo.setReceivingUserName(receivingUserName);
        if(groupId!=null){
        alertBo.setReceivingGroupId(groupId);
        alertBo.setReceivingGroupName(groupName);
        }
        if(roleId!=null){
        alertBo.setReceivingRoleId(roleId);
        alertBo.setReceivingRoleName(roleName);
        }
        return alertBo;
    }


}