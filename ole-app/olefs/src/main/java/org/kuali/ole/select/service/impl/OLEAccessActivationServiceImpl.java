package org.kuali.ole.select.service.impl;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.select.bo.OLEAccessActivationConfiguration;
import org.kuali.ole.select.bo.OLEAccessActivationWorkFlow;
import org.kuali.ole.select.bo.OLERoleBo;
import org.kuali.ole.select.service.OLEAccessActivationService;
import org.kuali.rice.kim.api.identity.IdentityService;
import org.kuali.rice.kim.api.identity.Person;
import org.kuali.rice.kim.api.identity.PersonService;
import org.kuali.rice.kim.api.identity.principal.Principal;
import org.kuali.rice.kim.api.role.Role;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;
import org.kuali.rice.kim.impl.role.RoleBo;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krad.util.GlobalVariables;

import java.util.*;

/**
 * Created by maheswarang on 5/13/15.
 */
public class OLEAccessActivationServiceImpl implements OLEAccessActivationService {

    private PersonService personService;
    private BusinessObjectService businessObjectService;

    public PersonService getPersonService() {
        if(personService == null){
            personService = KimApiServiceLocator.getPersonService();
        }
        return personService;
    }
    public void setPersonService(PersonService personService) {
        this.personService = personService;
    }

    public BusinessObjectService getBusinessObjectService() {
        if(businessObjectService == null){
            businessObjectService = KRADServiceLocator.getBusinessObjectService();
        }
        return businessObjectService;
    }

    public void setBusinessObjectService(BusinessObjectService businessObjectService) {
        this.businessObjectService = businessObjectService;
    }

    @Override
    public OLEAccessActivationConfiguration setRoleAndPersonName(OLEAccessActivationConfiguration oleAccessActivationConfiguration) {
        List<OLEAccessActivationWorkFlow> accessActivationWorkFlowList = oleAccessActivationConfiguration.getAccessActivationWorkflowList();
        for (OLEAccessActivationWorkFlow oleAccessActivationWorkFlow : accessActivationWorkFlowList) {
            Map<String, Object> oleBoMap = new HashMap<>();
            if(StringUtils.isNotBlank(oleAccessActivationWorkFlow.getRoleId())) {
                oleBoMap.put(OLEConstants.ID, oleAccessActivationWorkFlow.getRoleId());
                OLERoleBo oleRoleBo = KRADServiceLocator.getBusinessObjectService().findByPrimaryKey(OLERoleBo.class, oleBoMap);
                if(oleRoleBo != null) {
                    oleAccessActivationWorkFlow.setRoleName(oleRoleBo.getName());
                }
            }
            if(StringUtils.isNotBlank(oleAccessActivationWorkFlow.getPersonId())){
                Person person = KimApiServiceLocator.getPersonService().getPerson(oleAccessActivationWorkFlow.getPersonId());
                if(person !=null){
                    oleAccessActivationWorkFlow.setPersonName(person.getPrincipalName());
                }
            }

        }
        return oleAccessActivationConfiguration;
    }



     @Override
     public boolean validateAccessActivationWorkFlow(List<OLEAccessActivationWorkFlow> accessActivationWorkFlowList, OLEAccessActivationWorkFlow accessActivationWorkFlow, String selector) {

        boolean duplicateOrderNumber = false;
        boolean duplicateStatus = false;
        boolean validRole = true;
        boolean validPerson = false;
        for (OLEAccessActivationWorkFlow activationWorkFlow : accessActivationWorkFlowList) {
            if (accessActivationWorkFlow.getOrderNo() != null) {
                if (activationWorkFlow.getOrderNo() == accessActivationWorkFlow.getOrderNo()) {
                    GlobalVariables.getMessageMap().putErrorForSectionId(OLEConstants.OLE_ACCESS_ACTIVATION, OLEConstants.ERROR_DUPLICATE_ORDER_NO);
                    duplicateOrderNumber = true;
                }
            }
            if (StringUtils.isNotBlank(accessActivationWorkFlow.getStatus())) {
                if (activationWorkFlow.getStatus().equalsIgnoreCase(accessActivationWorkFlow.getStatus())) {
                    GlobalVariables.getMessageMap().putErrorForSectionId(OLEConstants.OLE_ACCESS_ACTIVATION, OLEConstants.ERROR_DUPLICATE_STATUS);
                    duplicateStatus = true;
                }
            }
        }
         if(StringUtils.isNotBlank(selector) && selector.equals(OLEConstants.SELECTOR_ROLE)) {
             validRole = validateRole(accessActivationWorkFlow);
         }

        if (duplicateStatus || duplicateOrderNumber || !validRole || !validatePerson(accessActivationWorkFlow)) {
            return false;
        }
        return true;
    }

   @Override
    public List<Principal> getPrincipals(OLEAccessActivationWorkFlow oleAccessActivationWorkFlow){
        List<Principal> principals = new ArrayList<Principal>();
        org.kuali.rice.kim.api.role.RoleService roleService = (org.kuali.rice.kim.api.role.RoleService) KimApiServiceLocator.getRoleService();
        Collection<String> principalIds = new ArrayList<>();
        if(StringUtils.isNotBlank(oleAccessActivationWorkFlow.getRoleId())) {
            Role role = roleService.getRole(oleAccessActivationWorkFlow.getRoleId());
            principalIds = (Collection<String>) roleService.getRoleMemberPrincipalIds(role.getNamespaceCode(), role.getName(), new HashMap<String, String>());
        }
        IdentityService identityService = KimApiServiceLocator.getIdentityService();
        List<String> principalList = new ArrayList<String>();
        if(CollectionUtils.isNotEmpty(principalIds)) {
            principalList.addAll(principalIds);
        }
        if(StringUtils.isNotBlank(oleAccessActivationWorkFlow.getPersonId())){
            principalList.add(oleAccessActivationWorkFlow.getPersonId());
        }
        principals = identityService.getPrincipals(principalList);
        return principals;
    }




    private boolean validateRole(OLEAccessActivationWorkFlow accessActivationWorkFlow){
        Map<String, String> criteria = new HashMap<String, String>();
        List<RoleBo> dataSourceNameInDatabaseroleName;
        RoleBo roleBo;
        boolean validRole = false;

        if (StringUtils.isNotBlank(accessActivationWorkFlow.getRoleId()) && StringUtils.isNotBlank(accessActivationWorkFlow.getRoleName())) {
            criteria.put(OLEConstants.ACCESS_ROLE_ID, accessActivationWorkFlow.getRoleId());
            criteria.put(OLEConstants.ACCESS_ROLE_NAME, accessActivationWorkFlow.getRoleName());
            dataSourceNameInDatabaseroleName = (List<RoleBo>) getBusinessObjectService().findMatching(RoleBo.class, criteria);
            if (dataSourceNameInDatabaseroleName != null && dataSourceNameInDatabaseroleName.size() > 0) {
                validRole = true;
            } else {
                GlobalVariables.getMessageMap().putErrorForSectionId(OLEConstants.OLE_ACCESS_ACTIVATION, OLEConstants.ERROR_INVALID_ID_NAME);
                validRole = false;
            }
        } else if (StringUtils.isBlank(accessActivationWorkFlow.getRoleId()) && StringUtils.isNotBlank(accessActivationWorkFlow.getRoleName())) {
            criteria = new HashMap<String, String>();
            criteria.put(OLEConstants.ACCESS_ROLE_NAME, accessActivationWorkFlow.getRoleName());
            dataSourceNameInDatabaseroleName = (List<RoleBo>) getBusinessObjectService()
                    .findMatching(RoleBo.class, criteria);
            if (dataSourceNameInDatabaseroleName != null && dataSourceNameInDatabaseroleName.size() > 0) {
                roleBo = dataSourceNameInDatabaseroleName.get(0);
                accessActivationWorkFlow.setRoleId(roleBo.getId());
                validRole = true;
            } else {
                GlobalVariables.getMessageMap().putErrorForSectionId(OLEConstants.OLE_ACCESS_ACTIVATION, OLEConstants.ERROR_INVALID_NAME);
                validRole = false;
            }
        } else if (StringUtils.isNotBlank(accessActivationWorkFlow.getRoleId()) && StringUtils.isBlank(accessActivationWorkFlow.getRoleName())) {
            criteria = new HashMap<String, String>();
            criteria.put(OLEConstants.ACCESS_ROLE_ID, accessActivationWorkFlow.getRoleId());
            dataSourceNameInDatabaseroleName = (List<RoleBo>) getBusinessObjectService()
                    .findMatching(RoleBo.class, criteria);
            if (dataSourceNameInDatabaseroleName != null && dataSourceNameInDatabaseroleName.size() > 0) {
                roleBo = dataSourceNameInDatabaseroleName.get(0);
                accessActivationWorkFlow.setRoleName(roleBo.getName());
                validRole = true;
            } else {
                GlobalVariables.getMessageMap().putErrorForSectionId(OLEConstants.OLE_ACCESS_ACTIVATION, OLEConstants.ERROR_INVALID_ID);
                validRole = false;
            }
        }


        return validRole ;
    }


    private boolean validatePerson(OLEAccessActivationWorkFlow accessActivationWorkFlow){
        boolean validPerson = false;
        if(StringUtils.isNotBlank(accessActivationWorkFlow.getPersonId()) && StringUtils.isNotBlank(accessActivationWorkFlow.getPersonName())){
            Map<String,String> criteriaMap = new HashMap<String,String>();
            criteriaMap.put("principalId",accessActivationWorkFlow.getPersonId());
            criteriaMap.put("principalName",accessActivationWorkFlow.getPersonName());
            List<Person> personList = getPersonService().findPeople(criteriaMap);
            if(personList.size()>0){
                validPerson = true;
            }else {
                GlobalVariables.getMessageMap().putErrorForSectionId(OLEConstants.OLE_ACCESS_ACTIVATION, OLEConstants.ERROR_INVALID_PERSON_ID_NAME);
                validPerson = false;
            }
        } else if(StringUtils.isNotBlank(accessActivationWorkFlow.getPersonId()) && StringUtils.isBlank(accessActivationWorkFlow.getPersonName())){
            Person person = getPersonService().getPerson(accessActivationWorkFlow.getPersonId());
            if(person!=null){
                validPerson =true;
                accessActivationWorkFlow.setPersonName(person.getName());
            }else{
                validPerson = false;
                GlobalVariables.getMessageMap().putErrorForSectionId(OLEConstants.OLE_ACCESS_ACTIVATION, OLEConstants.ERROR_INVALID_PERSON_ID);
            }

        }
        else if(StringUtils.isBlank(accessActivationWorkFlow.getPersonId()) && StringUtils.isNotBlank(accessActivationWorkFlow.getPersonName())){
            Person person = getPersonService().getPersonByPrincipalName(accessActivationWorkFlow.getPersonName());
            if(person!=null){
                validPerson =true;
                accessActivationWorkFlow.setPersonId(person.getPrincipalId());
            }else{
                validPerson = false;
                GlobalVariables.getMessageMap().putErrorForSectionId(OLEConstants.OLE_ACCESS_ACTIVATION, OLEConstants.ERROR_INVALID_PERSON_NAME);
            }

        }
        if(StringUtils.isBlank(accessActivationWorkFlow.getPersonId()) && StringUtils.isBlank(accessActivationWorkFlow.getPersonName())){
            accessActivationWorkFlow.setPersonName(null);
            validPerson=true;
        }
        return validPerson;
    }





}
