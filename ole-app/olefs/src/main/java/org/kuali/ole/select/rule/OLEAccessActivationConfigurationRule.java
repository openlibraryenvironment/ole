package org.kuali.ole.select.rule;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.collections.CollectionUtils;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.select.bo.OLEAccessActivationConfiguration;
import org.kuali.ole.select.bo.OLEAccessActivationWorkFlow;
import org.kuali.rice.kim.api.identity.Person;
import org.kuali.rice.kim.api.identity.PersonService;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;
import org.kuali.rice.kim.impl.role.RoleBo;
import org.kuali.rice.krad.maintenance.MaintenanceDocument;
import org.kuali.rice.krad.rules.MaintenanceDocumentRuleBase;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krad.util.GlobalVariables;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hemalathas on 12/17/14.
 */

public class OLEAccessActivationConfigurationRule extends MaintenanceDocumentRuleBase {

    private PersonService personService;
    private BusinessObjectService businessObjectService;

    public PersonService getPersonService() {
    if(personService == null){
        personService = KimApiServiceLocator.getPersonService();
    }
        return personService;
    }
    public BusinessObjectService getBusinessObjectService() {
        if(businessObjectService == null){
            businessObjectService = KRADServiceLocator.getBusinessObjectService();
        }
        return businessObjectService;
    }

    public void setPersonService(PersonService personService) {
        this.personService = personService;
    }

    @Override
    protected boolean processCustomSaveDocumentBusinessRules(MaintenanceDocument document) {
        boolean isValid = true;
        OLEAccessActivationConfiguration accessConfiguration = (OLEAccessActivationConfiguration) document.getNewMaintainableObject().getDataObject();
        //OLERoleBo rolebo = (OLERoleBo ) document.getNewMaintainableObject().getDataObject()
        isValid &= validateUniqueAccessName(accessConfiguration);
        isValid &= validateAccessActivationWorkflows(accessConfiguration);
        isValid &= validateUniqueStatus(accessConfiguration);
        processNotificationUser(accessConfiguration);
        isValid &= validateRole(accessConfiguration);
        isValid &= validatePerson(accessConfiguration);
        return isValid;
    }

    public boolean validateUniqueAccessName(OLEAccessActivationConfiguration accessConfiguration) {
        if (StringUtils.isNotBlank(accessConfiguration.getWorkflowName())) {
            Map<String, String> criteria = new HashMap<String, String>();
            criteria.put(OLEConstants.ACCESS_NAME, accessConfiguration.getWorkflowName());
            List<OLEAccessActivationConfiguration> dataSourceNameInDatabaseAccessName = (List<OLEAccessActivationConfiguration>) getBoService().findMatching(OLEAccessActivationConfiguration.class, criteria);
            if (dataSourceNameInDatabaseAccessName.size() > 0) {
                for (OLEAccessActivationConfiguration oleConfigurationName : dataSourceNameInDatabaseAccessName) {
                    String accessActivationConfigurationId = oleConfigurationName.getAccessActivationConfigurationId();
                    if (null == accessConfiguration.getAccessActivationConfigurationId() || (!accessConfiguration.getAccessActivationConfigurationId().equalsIgnoreCase(accessActivationConfigurationId))) {
                        GlobalVariables.getMessageMap().putError(OLEConstants.ACCESS_NAME_FIELD, OLEConstants.ERROR_DUPLICATE_WORKFLOW_NAME);
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public boolean validateUniqueOrderNo(OLEAccessActivationConfiguration accessConfiguration) {
        if (accessConfiguration.getAccessActivationWorkflowList() != null) {
            List<Integer> orderNos = new ArrayList<>();
            for (OLEAccessActivationWorkFlow workflow : accessConfiguration.getAccessActivationWorkflowList()) {
                if (!orderNos.contains(workflow.getOrderNo())){
                    orderNos.add(workflow.getOrderNo());
                }else {
                    GlobalVariables.getMessageMap().putError(OLEConstants.ORDER_NO_FIELD, OLEConstants.ERROR_DUPLICATE_ORDER_NO_FOUND);
                    return false;
                }
            }
        }
        return true;
    }

    public boolean validateUniqueStatus(OLEAccessActivationConfiguration accessConfiguration) {
        if (accessConfiguration.getAccessActivationWorkflowList() != null) {
            List<String> statusList = new ArrayList<>();
            for (OLEAccessActivationWorkFlow workflow : accessConfiguration.getAccessActivationWorkflowList()) {
                if (!statusList.contains(workflow.getStatus())){
                    statusList.add(workflow.getStatus());
                }else {
                    GlobalVariables.getMessageMap().putError(OLEConstants.ORDER_NO_FIELD, OLEConstants.ERROR_DUPLICATE_STATUS_FOUND);
                    return false;
                }
            }
        }
        return true;
    }


    private boolean validateAccessActivationWorkflows(OLEAccessActivationConfiguration accessConfiguration) {
        boolean isValid = true;
        if (CollectionUtils.isEmpty(accessConfiguration.getAccessActivationWorkflowList())) {
            GlobalVariables.getMessageMap().putError(OLEConstants.ACCESS_NAME_FIELD, OLEConstants.ERROR_ATLEAST_ONE_WORKFLOW);
            isValid = false;
        }else {
            isValid &= validateUniqueOrderNo(accessConfiguration);

        }
        return isValid;
    }


    private void processNotificationUser(OLEAccessActivationConfiguration oleAccessActivationConfiguration){
        if(oleAccessActivationConfiguration!=null){
            if(oleAccessActivationConfiguration.getNotificationSelector().equals("Role")){
               oleAccessActivationConfiguration.setRecipientUserName(null);
                oleAccessActivationConfiguration.setRecipientUserId(null);
                oleAccessActivationConfiguration.setMailId(null);
            }
            else if(oleAccessActivationConfiguration.getNotificationSelector().equals("Person")){
                oleAccessActivationConfiguration.setRecipientRoleName(null);
                oleAccessActivationConfiguration.setRecipientRoleId(null);
                oleAccessActivationConfiguration.setMailId(null);
            }
            else if (oleAccessActivationConfiguration.getNotificationSelector().equals("mail")){
                oleAccessActivationConfiguration.setRecipientUserName(null);
                oleAccessActivationConfiguration.setRecipientUserId(null);
                oleAccessActivationConfiguration.setRecipientRoleName(null);
                oleAccessActivationConfiguration.setRecipientRoleId(null);
            }
        }

    }




    private boolean validateRole(OLEAccessActivationConfiguration oleAccessActivationConfiguration){
        Map<String, String> criteria = new HashMap<String, String>();
        List<RoleBo> dataSourceNameInDatabaseroleName;
        RoleBo roleBo;
        boolean validRole = true;
        if (StringUtils.isNotBlank(oleAccessActivationConfiguration.getRecipientRoleName())) {
           // criteria.put(OLEConstants.ACCESS_ROLE_ID, oleAccessActivationConfiguration.getRecipientRoleId());
            criteria.put(OLEConstants.ACCESS_ROLE_NAME, oleAccessActivationConfiguration.getRecipientRoleName());
            dataSourceNameInDatabaseroleName = (List<RoleBo>) getBusinessObjectService().findMatching(RoleBo.class, criteria);
            if (dataSourceNameInDatabaseroleName != null && dataSourceNameInDatabaseroleName.size() > 0) {
                if(dataSourceNameInDatabaseroleName.size()==1){
                    oleAccessActivationConfiguration.setRecipientRoleId(dataSourceNameInDatabaseroleName.get(0).getId());
                    validRole = true;
                }else if(oleAccessActivationConfiguration.getRecipientRoleId()!=null){
                    boolean matchingIdAndName=false;
                for(RoleBo roleBo1 : dataSourceNameInDatabaseroleName){
                   {
                       if(oleAccessActivationConfiguration.getRecipientRoleId().equals(roleBo1.getId())){
                           oleAccessActivationConfiguration.setRecipientRoleId(roleBo1.getId());
                           matchingIdAndName = true;
                           validRole = true;
                           break;

                       }
                   }
                }
                 if(!matchingIdAndName){
                     GlobalVariables.getMessageMap().putErrorForSectionId(OLEConstants.OLE_ACCESS_ACTIVATION_NOTIFIER, OLEConstants.ERROR_INVALID_ID_NAME);
                     validRole = false;
                 }
                }else{
                    oleAccessActivationConfiguration.setRecipientRoleId(dataSourceNameInDatabaseroleName.get(0).getId());
                    validRole = true;
                }

            } else {
                GlobalVariables.getMessageMap().putErrorForSectionId(OLEConstants.OLE_ACCESS_ACTIVATION_NOTIFIER, OLEConstants.ERROR_INVALID_NAME);
                validRole = false;
            }
        }else{
            oleAccessActivationConfiguration.setRecipientRoleId(null);
            oleAccessActivationConfiguration.setRecipientRoleName(null);
        }

        return validRole ;
    }


    private boolean validatePerson(OLEAccessActivationConfiguration oleAccessActivationConfiguration){
        boolean validPerson = true;
        if(StringUtils.isNotBlank(oleAccessActivationConfiguration.getRecipientUserName())){
            Map<String,String> criteriaMap = new HashMap<String,String>();
          //  criteriaMap.put("principalId",oleAccessActivationConfiguration.getRecipientUserId());
            criteriaMap.put("principalName",oleAccessActivationConfiguration.getRecipientUserName());
            List<Person> personList = getPersonService().findPeople(criteriaMap);
            if(personList.size()>0){
                if(personList.size()==1){
                    oleAccessActivationConfiguration.setRecipientUserId(personList.get(0).getPrincipalId());
                    validPerson =true;
                }else if(oleAccessActivationConfiguration.getRecipientUserId()!=null){
                    boolean matchingIdAndName=false;
                    for(Person person : personList){
                        {
                            if(oleAccessActivationConfiguration.getRecipientUserId().equals(person.getPrincipalId())){
                                oleAccessActivationConfiguration.setRecipientUserId(person.getPrincipalId());
                                matchingIdAndName = true;
                                validPerson = true;
                                break;
                            }
                        }
                    }
                    if(!matchingIdAndName){
                        GlobalVariables.getMessageMap().putErrorForSectionId(OLEConstants.OLE_ACCESS_ACTIVATION_NOTIFIER, OLEConstants.ERROR_INVALID_PERSON_ID_NAME);
                        validPerson = false;
                    }
                }else{
                    oleAccessActivationConfiguration.setRecipientUserId(personList.get(0).getPrincipalId());
                    validPerson = true;
                }




            }else {
                GlobalVariables.getMessageMap().putErrorForSectionId(OLEConstants.OLE_ACCESS_ACTIVATION_NOTIFIER, OLEConstants.ERROR_INVALID_PERSON_NAME);
                validPerson = false;
            }
        }else{
            oleAccessActivationConfiguration.setRecipientUserId(null);
            oleAccessActivationConfiguration.setRecipientUserName(null);
        }

        return validPerson;
    }
}