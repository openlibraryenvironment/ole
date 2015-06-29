package org.kuali.ole.alert.service.impl;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.kuali.ole.alert.bo.ActionListAlertBo;
import org.kuali.ole.alert.bo.AlertBo;
import org.kuali.ole.alert.bo.AlertDocumentType;
import org.kuali.ole.alert.service.AlertGlobalConfigurationServiceImpl;
import org.kuali.ole.alert.service.AlertHelperService;
import org.kuali.ole.deliver.calendar.service.DateUtil;
import org.kuali.rice.kew.api.KewApiServiceLocator;
import org.kuali.rice.kew.api.action.ActionItem;
import org.kuali.rice.kew.identity.service.impl.IdentityHelperServiceImpl;
import org.kuali.rice.kim.api.identity.principal.Principal;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;
import org.kuali.rice.kim.impl.identity.principal.PrincipalBo;
import org.kuali.rice.krad.service.*;
import org.kuali.rice.krad.util.GlobalVariables;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by maheswarang on 11/4/14.
 */
public class AlertHelperServiceImpl implements AlertHelperService {
    private static final Logger LOG = Logger.getLogger(AlertHelperServiceImpl.class);
    private BusinessObjectService businessObjectService = KRADServiceLocator.getBusinessObjectService();
    private AlertGlobalConfigurationServiceImpl alertGlobalConfigurationService = new AlertGlobalConfigurationServiceImpl();
    private AlertServiceImpl alertService = new AlertServiceImpl();


    /**
     * This method is used to get the the list of alerts for the current day to the given user id
     * @param userId
     * @return
     */
    @Override
    public List<ActionListAlertBo> getActionListAlertsByUserId(String userId) {
        LOG.info("Inside the getActionListAlertsByUserId method for the user Id : " + userId);
        List<ActionListAlertBo> actionListAlertBos = new ArrayList<>();
        try{
            Map<String,Object> actionMap = new HashMap();
            actionMap.put("alertUserId",userId);
            actionMap.put("active",Boolean.TRUE);
            List<ActionListAlertBo> actionListAlertBosFromDb = (List<ActionListAlertBo>)businessObjectService.findMatchingOrderBy(ActionListAlertBo.class,actionMap,"alertDate",false);
            if(actionListAlertBosFromDb!=null && actionListAlertBosFromDb.size()>0){
                for(ActionListAlertBo actionListAlertBo : actionListAlertBosFromDb){
                    if(actionListAlertBo.getAlertApproverId()==null && actionListAlertBo.getAlertDate().compareTo(new Date(System.currentTimeMillis()))<=0){
                        actionListAlertBo.setAlertInitiatorName(getName(actionListAlertBo.getAlertInitiatorId()));
                        actionListAlertBos.add(actionListAlertBo);
                    }
                }
            }
        }catch(Exception e){
            LOG.info("Exception occured while getting the alert information to the user : " + userId);
            LOG.error(e,e);
        }
        return actionListAlertBos;
    }

    /**
     * This method is used to approve the selected alert and also updates the corresponding alert
     * @param actionListAlertBo
     */
    @Override
    public void approveActionListAlert(ActionListAlertBo actionListAlertBo){
        LOG.info("Inside approveActionListAlert for the alert with alert id : " + actionListAlertBo.getAlertId());
        AlertBo alertBo = new AlertBo();
        List<AlertBo> alertBos = new ArrayList<>();
        List<ActionListAlertBo> actionListAlertBos = new ArrayList<>();
        Map<String,String> alertMap = new HashMap<>();
        alertMap.put("documentId", actionListAlertBo.getDocumentId());
        alertBos = (List<AlertBo>)businessObjectService.findMatching(AlertBo.class, alertMap);
        actionListAlertBos = (List<ActionListAlertBo>)businessObjectService.findMatching(ActionListAlertBo.class, alertMap);
        if(CollectionUtils.isNotEmpty(alertBos)) {
            for(AlertBo alertBo1 : alertBos) {
                if(actionListAlertBo.getAlertId().equals(alertBo1.getAlertId())) {
                    alertBo = alertBo1;
                    break;
                }
            }
        }
        if(StringUtils.isNotEmpty(alertBo.getReceivingRoleId())) {
            approveAlertBaseOnRole(alertBos, alertBo, actionListAlertBos);
        } else if(StringUtils.isNotEmpty(alertBo.getReceivingGroupId())) {
            approveAlertBaseOnGroup(alertBos, alertBo, actionListAlertBos);
        } else {
            saveAlert(actionListAlertBo, alertBo);
        }
    }

    public void approveAlertBaseOnRole(List<AlertBo> alertBos, AlertBo alertBo, List<ActionListAlertBo> actionListAlertBos) {
        for(AlertBo alertBo1 : alertBos) {
            if(alertBo.getReceivingRoleId().equals(alertBo1.getReceivingRoleId())) {
                if(alertBo.getAlertCreateDate().equals(alertBo1.getAlertCreateDate())) {
                    for(ActionListAlertBo actionListAlertBo : actionListAlertBos) {
                        if(StringUtils.isNotEmpty(actionListAlertBo.getAlertId()) && actionListAlertBo.getAlertId().equals(alertBo1.getAlertId())) {
                            saveAlert(actionListAlertBo, alertBo1);
                            break;
                        }
                    }
                }
            }
        }
    }

    public void approveAlertBaseOnGroup(List<AlertBo> alertBos, AlertBo alertBo, List<ActionListAlertBo> actionListAlertBos) {
        for(AlertBo alertBo1 : alertBos) {
            if(alertBo.getReceivingGroupId().equals(alertBo1.getReceivingGroupId())) {
                if(alertBo.getAlertCreateDate().equals(alertBo1.getAlertCreateDate())) {
                    for(ActionListAlertBo actionListAlertBo : actionListAlertBos) {
                        if(StringUtils.isNotEmpty(actionListAlertBo.getAlertId()) && actionListAlertBo.getAlertId().equals(alertBo1.getAlertId())) {
                            saveAlert(actionListAlertBo,alertBo1);
                            break;
                        }
                    }
                }
            }
        }
    }

    private void saveAlert(ActionListAlertBo actionListAlertBo, AlertBo alertBo) {
        actionListAlertBo.setActive(false);
        actionListAlertBo.setAlertApprovedDate(new Date(System.currentTimeMillis()));
        actionListAlertBo.setAlertApproverId(GlobalVariables.getUserSession().getPrincipalId());
        alertBo.setAlertStatus(false);
        alertBo.setAlertApproverId(GlobalVariables.getUserSession().getPrincipalId());
        alertBo.setAlertApprovedDate(new Date(System.currentTimeMillis()));
        businessObjectService.save(alertBo);
        businessObjectService.save(actionListAlertBo);
        if(alertBo.isRepeatable()) {
            AlertBo alertBo1 = createNewAlertBo(alertBo);
            businessObjectService.save(alertBo1);
            ActionListAlertBo actionListAlertBo1 = getActionListAlertBo(alertBo1,actionListAlertBo.getRecordType(),actionListAlertBo.getTitle(),actionListAlertBo.getAlertUserId());
            businessObjectService.save(actionListAlertBo1);
        }
    }


    /**
     * This method is used to retrieve alert based on the given alert id
     * @param alertId
     * @return
     */
    public AlertBo getAlertBo(String alertId){
        AlertBo alertBo = null;
        List<AlertBo> alertBos = new ArrayList<>();
        try{
            Map<String,Object> alertMap = new HashMap<String,Object>();
/*        alertMap.put("documentId",actionListAlertBo.getDocumentId());
        alertMap.put("receivingUserId",actionListAlertBo.getAlertUserId());
        alertMap.put("alertDate",actionListAlertBo.getAlertDate());*/
            alertMap.put("alertId",alertId);
            alertBos = (List<AlertBo>)businessObjectService.findMatching(AlertBo.class,alertMap);
        }catch(Exception e){
            LOG.info("Exception occured while getting the alert information for an alert with alert Id : " + alertId);
        }
        if(alertBos!=null && alertBos.size()>0){
            alertBo = alertBos.get(0);
        }
        return alertBo;
    }


    /**
     * This method is used to get the principal name for the given principal id
     * @param principalId
     * @return
     */
    public String getName(String principalId){
        Principal principal = KimApiServiceLocator.getIdentityService().getPrincipal(principalId);
        return   principal.getPrincipalName();
    }



    public AlertBo createNewAlertBo(AlertBo alertBo){
        AlertBo alertBo1 = new AlertBo();
        alertBo1.setDocumentId(alertBo.getDocumentId());
        alertBo1.setAlertCreateDate(alertBo.getAlertCreateDate());
        alertBo1.setAlertInitiatorId(alertBo.getAlertInitiatorId());
        alertBo1.setAlertNote(alertBo.getAlertNote());
        alertBo1.setReceivingUserId(alertBo.getReceivingUserId());
        alertBo1.setAlertStatus(true);
        alertBo1.setReceivingGroupId(alertBo.getReceivingGroupId());
        alertBo1.setAlertInterval(alertBo.getAlertInterval());
        alertBo1.setReceivingRoleId(alertBo.getReceivingRoleId());
        alertBo1.setRepeatable(alertBo.isRepeatable());
        if(alertBo.getAlertInterval()==null || (alertBo.getAlertInterval()!=null && !alertBo.getAlertInterval().trim().isEmpty())){
            java.util.Date alertDate = DateUtils.addDays(new java.util.Date(alertBo.getAlertDate().getTime()), Integer.parseInt(alertBo.getAlertInterval()));
            alertBo1.setAlertDate(new Date(alertDate.getTime()));
        }else{
            alertBo1.setAlertDate(alertBo.getAlertDate());
        }


        return alertBo1;
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



    public void processApprovalPendingDocuments(String principalId){
        List<ActionItem> actionItems = KewApiServiceLocator.getActionListService().getActionItemsForPrincipal(principalId);
        String reminderInterval = null;
        AlertBo alertBo = null;
        ActionListAlertBo actionListAlertBo = null;
        if(actionItems!=null && actionItems.size()>0){
            for(ActionItem actionItem : actionItems){
                List<AlertBo> alertBos = getAlertBoBasedOnDocumentNumber(actionItem.getDocumentId(),false,principalId);
              if(alertBos == null || (alertBos!=null && alertBos.size()==0)){
               reminderInterval =  getReminderInterval(actionItem.getDocName());
                if(reminderInterval!=null){
                     if(isApprovalDateBarred(actionItem.getDateTimeAssigned(),reminderInterval)){
                    alertBo = alertGlobalConfigurationService.getAlert(actionItem.getDocumentId(),null,false,principalId,"note",principalId,principalId,principalId,null,null,null,null);
                       if(alertBo!=null){
                        businessObjectService.save(alertBo);
                        actionListAlertBo =  alertService.getActionListAlertBo(alertBo,actionItem.getDocName(),actionItem.getDocTitle(),principalId);
                           businessObjectService.save(actionListAlertBo);
                     }
                     }
                }

            }
           }
        }
    }

    public String getReminderInterval(String docTypeName){
        String interval= null;
        List<AlertDocumentType> alertDocumentTypes = null;
        Map<String,String> alertDocumentMap = new HashMap<String,String>();
        alertDocumentMap.put("alertDocumentTypeName",docTypeName);
        alertDocumentTypes = (List<AlertDocumentType>)KRADServiceLocator.getBusinessObjectService().findMatching(AlertDocumentType.class,alertDocumentMap);
        if(alertDocumentTypes!=null && alertDocumentTypes.size()>0){
            interval = alertDocumentTypes.get(0).getAlertReminderInterval();
        }
        return interval;
    }

   public boolean isApprovalDateBarred(DateTime dateTime,String reminderInterval){
       boolean expired = false;
       DateTime expiryDate = dateTime.plusDays(Integer.parseInt(reminderInterval));
       if(expiryDate.isEqualNow() || expiryDate.isBeforeNow()){
          expired = true;
       }
        return expired;
    }

public List<AlertBo> getAlertBoBasedOnDocumentNumber (String documentNumber,boolean active,String receivingUserId){
    Map<String,String> alertMap = new HashMap<String,String>();
    alertMap.put("documentId",documentNumber);
    alertMap.put("receivingUserId",receivingUserId);
    if(active){
        alertMap.put("alertStatus","true");
    }
    alertMap.put("alertDate",new java.sql.Date(System.currentTimeMillis()).toString());
    List<AlertBo> alertBoList = (List<AlertBo>)KRADServiceLocator.getBusinessObjectService().findMatching(AlertBo.class,alertMap);

    return alertBoList;


}



}
