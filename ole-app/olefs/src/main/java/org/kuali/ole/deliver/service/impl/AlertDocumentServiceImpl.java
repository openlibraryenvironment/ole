package org.kuali.ole.deliver.service.impl;

import org.apache.commons.lang.StringUtils;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.alert.bo.AlertConditionAndReceiverInformation;
import org.kuali.ole.deliver.service.AlertDocumentService;
import org.kuali.rice.kim.api.identity.Person;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;
import org.kuali.rice.kim.impl.group.GroupBo;
import org.kuali.rice.kim.impl.role.RoleBo;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.util.ObjectUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by arunag on 12/30/14.
 */
public class AlertDocumentServiceImpl implements AlertDocumentService {

    private BusinessObjectService businessObjectService;

    public BusinessObjectService getBusinessObjectService() {
        if (this.businessObjectService == null) {
            this.businessObjectService = KRADServiceLocator.getBusinessObjectService();
        }
        return this.businessObjectService;
    }
    public void setBusinessObjectService(BusinessObjectService businessObjectService) {
        this.businessObjectService = businessObjectService;
    }

    public boolean validateRole(AlertConditionAndReceiverInformation alertConditionAndReceiverInformation){
        Map<String, String> criteria = new HashMap<String, String>();
        List<RoleBo> roleBos ;
        RoleBo roleBo;

        boolean validRole = false;


        if(StringUtils.isNotEmpty(alertConditionAndReceiverInformation.getRoleId()) && StringUtils.isNotEmpty(alertConditionAndReceiverInformation.getRoleName())){
            criteria.put(OLEConstants.ACCESS_ROLE_ID,alertConditionAndReceiverInformation.getRoleId());
            criteria.put(OLEConstants.ACCESS_ROLE_NAME, alertConditionAndReceiverInformation.getRoleName());
            roleBos =  (List<RoleBo>) getBusinessObjectService().findMatching(RoleBo.class, criteria);

            if(roleBos!=null && roleBos.size()>0){
                validRole= false;
            }else{
                GlobalVariables.getMessageMap().putErrorForSectionId(OLEConstants.ALERT_DOC_CONDITION, OLEConstants.ERROR_INVALID_ID_NAME);
                validRole= true;
            }
        }else if(StringUtils.isEmpty(alertConditionAndReceiverInformation.getRoleId()) && StringUtils.isNotEmpty(alertConditionAndReceiverInformation.getRoleName())){
            criteria = new HashMap<String,String>();
            criteria.put(OLEConstants.ACCESS_ROLE_NAME,alertConditionAndReceiverInformation.getRoleName());
            roleBos =  (List<RoleBo>) getBusinessObjectService()
                    .findMatching(RoleBo.class, criteria);
            if(roleBos!=null && roleBos.size()>0){
                roleBo = roleBos.get(0);
                alertConditionAndReceiverInformation.setRoleId(roleBo.getId());
                validRole= false;
            }else{
                GlobalVariables.getMessageMap().putErrorForSectionId(OLEConstants.ALERT_DOC_CONDITION, OLEConstants.ERROR_INVALID_NAME);
                validRole= true;
            }
        }else if(StringUtils.isNotEmpty(alertConditionAndReceiverInformation.getRoleId()) && StringUtils.isEmpty(alertConditionAndReceiverInformation.getRoleName())){
            criteria = new HashMap<String,String>();
            criteria.put(OLEConstants.ACCESS_ROLE_ID,alertConditionAndReceiverInformation.getRoleId());
            roleBos =  (List<RoleBo>) getBusinessObjectService()
                    .findMatching(RoleBo.class, criteria);
            if(roleBos!=null && roleBos.size()>0){
                roleBo = roleBos.get(0);
                alertConditionAndReceiverInformation.setRoleName(roleBo.getName());
                validRole= false;
            }else{
                GlobalVariables.getMessageMap().putErrorForSectionId(OLEConstants.ALERT_DOC_CONDITION, OLEConstants.ERROR_INVALID_ID);
                validRole= true;
            }
        }
        /*else if(StringUtils.isEmpty(alertConditionAndReceiverInformation.getRoleName()) && StringUtils.isEmpty(alertConditionAndReceiverInformation.getRoleId())){
            GlobalVariables.getMessageMap().putErrorForSectionId(OLEConstants.ALERT_DOC_CONDITION, OLEConstants.ERROR_EMPTY_ROLE);
            validRole = true;
        }*/

        return validRole;
    }



    public boolean validateGroup(AlertConditionAndReceiverInformation alertConditionAndReceiverInformation){
        Map<String, String> criteria = new HashMap<String, String>();
        List<GroupBo> groupBos ;
        GroupBo groupBo;

        boolean validGroup = false;


        if(StringUtils.isNotEmpty(alertConditionAndReceiverInformation.getGroupId()) && StringUtils.isNotEmpty(alertConditionAndReceiverInformation.getGroupName())){
            criteria.put(OLEConstants.ACCESS_ROLE_ID,alertConditionAndReceiverInformation.getGroupId());
            criteria.put(OLEConstants.ACCESS_ROLE_NAME, alertConditionAndReceiverInformation.getGroupName());
            groupBos =  (List<GroupBo>) getBusinessObjectService().findMatching(GroupBo.class, criteria);

            if(groupBos!=null && groupBos.size()>0){
                validGroup= false;
            }else{
                GlobalVariables.getMessageMap().putErrorForSectionId(OLEConstants.ALERT_DOC_CONDITION, OLEConstants.ERROR_INVALID_GROUP_ID_NAME);
                validGroup= true;
            }
        }else if(StringUtils.isEmpty(alertConditionAndReceiverInformation.getGroupId()) && StringUtils.isNotEmpty(alertConditionAndReceiverInformation.getGroupName())){
            criteria = new HashMap<String,String>();
            criteria.put(OLEConstants.ACCESS_ROLE_NAME,alertConditionAndReceiverInformation.getGroupName());
            groupBos =  (List<GroupBo>) getBusinessObjectService()
                    .findMatching(GroupBo.class, criteria);
            if(groupBos!=null && groupBos.size()>0){
                groupBo = groupBos.get(0);
                alertConditionAndReceiverInformation.setGroupId(groupBo.getId());
                validGroup= false;
            }else{
                GlobalVariables.getMessageMap().putErrorForSectionId(OLEConstants.ALERT_DOC_CONDITION, OLEConstants.ERROR_INVALID_GROUP_NAME);
                validGroup= true;
            }
        }else if(StringUtils.isNotEmpty(alertConditionAndReceiverInformation.getGroupId()) && StringUtils.isEmpty(alertConditionAndReceiverInformation.getGroupName())){
            criteria = new HashMap<String,String>();
            criteria.put(OLEConstants.ACCESS_ROLE_ID,alertConditionAndReceiverInformation.getGroupId());
            groupBos =  (List<GroupBo>) getBusinessObjectService()
                    .findMatching(GroupBo.class, criteria);
            if(groupBos!=null && groupBos.size()>0){
                groupBo = groupBos.get(0);
                alertConditionAndReceiverInformation.setGroupName(groupBo.getName());
                validGroup= false;
            }else{
                GlobalVariables.getMessageMap().putErrorForSectionId(OLEConstants.ALERT_DOC_CONDITION, OLEConstants.ERROR_INVALID_GROUP_ID);
                validGroup= true;
            }
        }
       /* else if(StringUtils.isEmpty(alertConditionAndReceiverInformation.getGroupName()) && StringUtils.isEmpty(alertConditionAndReceiverInformation.getGroupId())){
            GlobalVariables.getMessageMap().putErrorForSectionId(OLEConstants.ALERT_DOC_CONDITION, OLEConstants.ERROR_EMPTY_GROUP);
            validGroup = true;
        }*/

        return validGroup;
    }

    public boolean validatePerson(AlertConditionAndReceiverInformation alertConditionAndReceiverInformation){
        Map<String, String> criteria = new HashMap<String, String>();
        List<Person> personList ;
        Person person;

        boolean validPerson = false;


        if(StringUtils.isNotEmpty(alertConditionAndReceiverInformation.getPrincipalId()) && StringUtils.isNotEmpty(alertConditionAndReceiverInformation.getPrincipalName())){
            String personId = null;
            person = KimApiServiceLocator.getPersonService().getPerson(alertConditionAndReceiverInformation.getPrincipalName());
            if(ObjectUtils.isNotNull(person))
            personId=person.getPrincipalId();
            if(personId.equals((alertConditionAndReceiverInformation.getPrincipalId()))){
                validPerson= false;
            }else{
                GlobalVariables.getMessageMap().putErrorForSectionId(OLEConstants.ALERT_DOC_CONDITION, OLEConstants.ERROR_INVALID_PERSON_ID_NAME);
                validPerson= true;
            }
        }else if(StringUtils.isEmpty(alertConditionAndReceiverInformation.getPrincipalId()) && StringUtils.isNotEmpty(alertConditionAndReceiverInformation.getPrincipalName())){

            String personId = null;
            person = KimApiServiceLocator.getPersonService().getPerson(alertConditionAndReceiverInformation.getPrincipalName());
            if(ObjectUtils.isNotNull(person))
                personId=person.getPrincipalId();
            if(StringUtils.isNotEmpty(personId)){
                alertConditionAndReceiverInformation.setPrincipalId(personId);
                validPerson= false;
            }else{
                GlobalVariables.getMessageMap().putErrorForSectionId(OLEConstants.ALERT_DOC_CONDITION, OLEConstants.ERROR_INVALID_PERSON_NAME);
                validPerson= true;
            }
        }else if(StringUtils.isNotEmpty(alertConditionAndReceiverInformation.getPrincipalId()) && StringUtils.isEmpty(alertConditionAndReceiverInformation.getPrincipalName())){
            String personName = null;
            person = KimApiServiceLocator.getPersonService().getPerson(alertConditionAndReceiverInformation.getPrincipalId());
            if(ObjectUtils.isNotNull(person))
                personName=person.getPrincipalName();
            if(StringUtils.isNotEmpty(personName)){
                alertConditionAndReceiverInformation.setPrincipalId(personName);
                validPerson= false;
            }else{
                GlobalVariables.getMessageMap().putErrorForSectionId(OLEConstants.ALERT_DOC_CONDITION, OLEConstants.ERROR_INVALID_PERSON_ID);
                validPerson= true;
            }
        }
        /*else if(StringUtils.isEmpty(alertConditionAndReceiverInformation.getPrincipalName()) && StringUtils.isEmpty(alertConditionAndReceiverInformation.getPrincipalId())){
            GlobalVariables.getMessageMap().putErrorForSectionId(OLEConstants.ALERT_DOC_CONDITION, OLEConstants.ERROR_EMPTY_PERSON);
            validPerson = true;
        }*/

        return validPerson;
    }
}
