package org.kuali.ole.select.service;

import org.junit.Assert;
import org.junit.Test;
import org.kuali.ole.OLETestCaseBase;
import org.kuali.ole.select.bo.OLEAccessActivationConfiguration;
import org.kuali.ole.select.bo.OLEAccessActivationWorkFlow;
import org.kuali.ole.select.service.OLEAccessActivationService;
import org.kuali.ole.select.service.impl.OLEAccessActivationServiceImpl;
import org.kuali.rice.kim.api.identity.Person;
import org.kuali.rice.kim.api.identity.principal.Principal;
import org.kuali.rice.kim.api.role.Role;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

/**
 * Created by maheswarang on 5/13/15.
 */
public class OLEAccessActivationService_UT extends OLETestCaseBase{

    @Test
    public void setRoleAndPersonNameTest(){
        OLEAccessActivationService oleAccessActivationService = new OLEAccessActivationServiceImpl();
        OLEAccessActivationConfiguration oleAccessActivationConfiguration = new OLEAccessActivationConfiguration();
        List<OLEAccessActivationWorkFlow> oleAccessActivationWorkFlows = new ArrayList<OLEAccessActivationWorkFlow>();
        OLEAccessActivationWorkFlow oleAccessActivationWorkFlow = new OLEAccessActivationWorkFlow();
        Role role = KimApiServiceLocator.getRoleService().getRole("OLE7");
        Person person = KimApiServiceLocator.getPersonService().getPerson("olequickstart");
        oleAccessActivationWorkFlow.setPersonId(person.getPrincipalId());
        oleAccessActivationWorkFlow.setRoleId(role.getId());
        oleAccessActivationWorkFlows.add(oleAccessActivationWorkFlow);
        oleAccessActivationConfiguration.setAccessActivationWorkflowList(oleAccessActivationWorkFlows);
        oleAccessActivationConfiguration = oleAccessActivationService.setRoleAndPersonName(oleAccessActivationConfiguration);
        Assert.assertTrue("Person Name is Matched ",oleAccessActivationConfiguration.getAccessActivationWorkflowList().get(0).getPersonName().equals(person.getPrincipalName()));
        Assert.assertTrue("Suceess", oleAccessActivationConfiguration.getAccessActivationWorkflowList().get(0).getRoleName().equals(role.getName()));
        System.out.println("Role Name :  "+oleAccessActivationConfiguration.getAccessActivationWorkflowList().get(0).getRoleName() + "Person Name : " +oleAccessActivationConfiguration.getAccessActivationWorkflowList().get(0).getPersonName());

    }


    @Test
    public void validateAccessActivationWorkFlowTest(){
        OLEAccessActivationService oleAccessActivationService = new OLEAccessActivationServiceImpl();
        List<OLEAccessActivationWorkFlow> oleAccessActivationWorkFlows = new ArrayList<OLEAccessActivationWorkFlow>();
        OLEAccessActivationWorkFlow oleAccessActivationWorkFlow = new OLEAccessActivationWorkFlow();
        Role role = KimApiServiceLocator.getRoleService().getRole("OLE7");
        Person person = KimApiServiceLocator.getPersonService().getPerson("olequickstart");
        oleAccessActivationWorkFlow.setPersonId(person.getPrincipalId());
        oleAccessActivationWorkFlow.setRoleId(role.getId());
        oleAccessActivationWorkFlows.add(oleAccessActivationWorkFlow);
        Assert.assertTrue(oleAccessActivationService.validateAccessActivationWorkFlow(oleAccessActivationWorkFlows,oleAccessActivationWorkFlow));
        oleAccessActivationWorkFlow.setRoleName("Test Role");
        Assert.assertFalse(oleAccessActivationService.validateAccessActivationWorkFlow(oleAccessActivationWorkFlows, oleAccessActivationWorkFlow));


    }


    @Test
    public void getPrincipalsTest(){
        OLEAccessActivationService oleAccessActivationService = new OLEAccessActivationServiceImpl();
        List<OLEAccessActivationWorkFlow> oleAccessActivationWorkFlows = new ArrayList<OLEAccessActivationWorkFlow>();
        OLEAccessActivationWorkFlow oleAccessActivationWorkFlow = new OLEAccessActivationWorkFlow();
        Role role = KimApiServiceLocator.getRoleService().getRole("OLE7");
        Collection<String> principalIds = (Collection<String>) KimApiServiceLocator.getRoleService().getRoleMemberPrincipalIds(role.getNamespaceCode(), role.getName(), new HashMap<String, String>());
        Person person = KimApiServiceLocator.getPersonService().getPerson("olequickstart");
        oleAccessActivationWorkFlow.setPersonId(person.getPrincipalId());
        oleAccessActivationWorkFlow.setRoleId(role.getId());
        oleAccessActivationWorkFlows.add(oleAccessActivationWorkFlow);
        List<Principal> principals = oleAccessActivationService.getPrincipals(oleAccessActivationWorkFlow);
        System.out.println("Number of principals "+principals.size());
        Assert.assertTrue("Number of principalsIds matched" ,principalIds.size()+1 == principals.size());


    }



}
