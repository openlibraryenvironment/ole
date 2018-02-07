/**
 * Copyright 2005-2013 The Kuali Foundation
 *
 * Licensed under the Educational Community License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.opensource.org/licenses/ecl2.php
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kuali.rice.kim.service.impl;

import com.google.common.collect.Maps;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Test;
import org.kuali.rice.core.api.delegation.DelegationType;
import org.kuali.rice.core.api.exception.RiceIllegalStateException;
import org.kuali.rice.core.api.membership.MemberType;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.rice.kew.api.action.ActionType;
import org.kuali.rice.kim.api.KimApiConstants;
import org.kuali.rice.kim.api.KimConstants;
import org.kuali.rice.kim.api.common.delegate.DelegateMember;
import org.kuali.rice.kim.api.role.Role;
import org.kuali.rice.kim.api.role.RoleMember;
import org.kuali.rice.kim.api.role.RoleMembership;
import org.kuali.rice.kim.api.role.RoleResponsibilityAction;
import org.kuali.rice.kim.api.role.RoleService;
import org.kuali.rice.kim.impl.common.delegate.DelegateMemberAttributeDataBo;
import org.kuali.rice.kim.impl.common.delegate.DelegateMemberBo;
import org.kuali.rice.kim.impl.common.delegate.DelegateTypeBo;
import org.kuali.rice.kim.impl.role.RoleMemberAttributeDataBo;
import org.kuali.rice.kim.impl.role.RoleMemberBo;
import org.kuali.rice.kim.test.KIMTestCase;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;

import javax.xml.namespace.QName;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.Assert.*;

public class RoleServiceImplTest extends KIMTestCase {
    private RoleService roleService;
    static final DateTimeFormatter FORMATTER = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
    static final String ROLE_MEMBER_ID1 = "45123";
    static final String ROLE_ID = "100";
    static final String MEMBER_ID = "1";
    static final MemberType MEMBER_TYPE_R = MemberType.ROLE;
    static final String ACTIVE_FROM_STRING = "2011-01-01 12:00:00";
    static final DateTime ACTIVE_FROM = new DateTime(FORMATTER.parseDateTime(ACTIVE_FROM_STRING));
    static final String ACTIVE_TO_STRING1 = "2013-01-01 12:00:00";
    static final String ACTIVE_TO_STRING2 = "2014-01-01 12:00:00";
    static final DateTime ACTIVE_TO1 = new DateTime(FORMATTER.parseDateTime(ACTIVE_TO_STRING1));
    static final DateTime ACTIVE_TO2 = new DateTime(FORMATTER.parseDateTime(ACTIVE_TO_STRING2));
    private BusinessObjectService businessObjectService;

    public void setUp() throws Exception {
		super.setUp();
		roleService = (RoleService) GlobalResourceLoader.getService(
                new QName(KimApiConstants.Namespaces.KIM_NAMESPACE_2_0, KimApiConstants.ServiceNames.ROLE_SERVICE_SOAP));
	}

	@Test
	public void testPrincipaHasRoleOfDirectAssignment() {
		List <String>roleIds = new ArrayList<String>();
		roleIds.add("r1");
		assertTrue( "p1 has direct role r1", roleService.principalHasRole("p1", roleIds,  Collections
                .<String, String>emptyMap() ));
		//assertFalse( "p4 has no direct/higher level role r1", roleService.principalHasRole("p4", roleIds, null ));
		Map<String, String> qualification = new HashMap<String, String>();
		qualification.put("Attribute 2", "CHEM");
		assertTrue( "p1 has direct role r1 with rp2 attr data", roleService.principalHasRole("p1", roleIds, qualification));
		qualification.clear();
		//requested qualification rolls up to a higher element in some hierarchy 
		// method not implemented yet, not quite clear how this works
		qualification.put("Attribute 3", "PHYS");
		assertTrue( "p1 has direct role r1 with rp2 attr data", roleService.principalHasRole("p1", roleIds, Maps.newHashMap(
                qualification)));
	}

	@Test
	public void testPrincipalHasRoleOfHigherLevel() {
		// "p3" is in "r2" and "r2 contains "r1"
		List <String>roleIds = new ArrayList<String>();
		roleIds.add("r2");
		assertTrue( "p1 has assigned in higher level role r1", roleService.principalHasRole("p1", roleIds,  Collections.<String, String>emptyMap() ));
        assertTrue( "p1 has assigned in higher level role r1", roleService.principalHasRole("p1", roleIds,  Collections.<String, String>emptyMap() ));
	}

    @Test
    public void testDelegateMemberCreateUpdateRemove() {

        Role r2 = roleService.getRole("r2");
        RoleMember rm1 = roleService.assignPrincipalToRole("user2", r2.getNamespaceCode(), r2.getName(),
                new HashMap<String, String>());
        String kimTypeId = "1";

        //Create delegation
        String id = "" + KRADServiceLocator.getSequenceAccessorService().getNextAvailableSequenceNumber("KRIM_DLGN_MBR_ID_S");
        DelegateTypeBo delegate = new DelegateTypeBo();
        delegate.setDelegationId(id);
        delegate.setDelegationType(DelegationType.PRIMARY);
        delegate.setRoleId(r2.getId());
        delegate.setActive(true);
        delegate.setKimTypeId("" + kimTypeId);
        delegate = KRADServiceLocator.getBusinessObjectService().save(delegate);

        //Create delegate member
        DelegateMember.Builder delegateMemberInfo = DelegateMember.Builder.create();
        delegateMemberInfo.setAttributes(Collections.<String, String>emptyMap());
        delegateMemberInfo.setDelegationId(delegate.getDelegationId());
        delegateMemberInfo.setMemberId("user4");
        delegateMemberInfo.setRoleMemberId(rm1.getId());
        delegateMemberInfo.setType( MemberType.PRINCIPAL );
        DelegateMember inDelegateMember =  delegateMemberInfo.build();
        DelegateMember newDelegateMember = roleService.createDelegateMember(inDelegateMember);
        assertNotNull("delegateMember not created",newDelegateMember);

        //Update delegate member
        delegateMemberInfo.setDelegationMemberId(newDelegateMember.getDelegationMemberId());
        DateTime dateTimeFrom   = DateTime.now().minusDays(3);
        delegateMemberInfo.setActiveFromDate(dateTimeFrom);
        DateTime dateTimeTo = DateTime.now().plusDays(3);
        delegateMemberInfo.setActiveToDate(dateTimeTo);
        inDelegateMember = delegateMemberInfo.build();
        DelegateMember updatedDelegateMember = roleService.updateDelegateMember(inDelegateMember);
        assertEquals("Delegate member was updated",newDelegateMember.getDelegationMemberId(),updatedDelegateMember.getDelegationMemberId());
        assertNotNull("updateDelegateMember not created",updatedDelegateMember);
        assertEquals("activeFromDate not updated",dateTimeFrom,updatedDelegateMember.getActiveFromDate());
        assertEquals("activeToDate not updated",dateTimeTo,updatedDelegateMember.getActiveToDate());

        //remove (inactivate) delegate member
        List<DelegateMember>  removeDelegateMembers = new ArrayList<DelegateMember>();
        removeDelegateMembers.add(updatedDelegateMember);
        roleService.removeDelegateMembers(removeDelegateMembers);
        DelegateMember removedDelegateMember = roleService.getDelegationMemberById(updatedDelegateMember.getDelegationMemberId()) ;
        assertTrue("removeDelegateMembers did not remove the existing member",removedDelegateMember.getDelegationMemberId().equals(updatedDelegateMember.getDelegationMemberId()));
        assertTrue("removeDelegateMembers did not remove the existing member",removedDelegateMember.getVersionNumber().equals(updatedDelegateMember.getVersionNumber() + 1));
        assertTrue("removeDelegateMembers did not update activeToDate",removedDelegateMember.getActiveToDate().isBeforeNow());
    }

    @Test
    public void testRoleMemberCreateUpdate() {

        Role roleId = roleService.getRole(ROLE_ID);
        List<String> roleIds = new ArrayList<String>();
        roleIds.add(roleId.getId());

        Map<String,String> attributes = new HashMap<String,String>();
        attributes.put("parameterName", "parameterNameBefore");
        attributes.put("namespaceCode", "namespaceCodeBefore");
        attributes.put("componentName", "componentNameBefore");

        RoleMember roleMember =  roleService.createRoleMember(RoleMember.Builder.create(ROLE_ID, ROLE_MEMBER_ID1, MEMBER_ID, MEMBER_TYPE_R, ACTIVE_FROM, ACTIVE_TO1, attributes, "", "").build());
        RoleMemberBo rmBo = getRoleMemberBo(roleMember.getId());

        RoleMember.Builder updatedRoleMember = RoleMember.Builder.create(roleMember);
        updatedRoleMember.setActiveToDate(ACTIVE_TO2);
        Map<String,String> newAttributes = new HashMap<String,String>();
        newAttributes.put("parameterName", "parameterNameAfter");
        newAttributes.put("namespaceCode", "namespaceCodeAfter");
        newAttributes.put("componentName", "componentNameAfter");
        updatedRoleMember.setAttributes(newAttributes);

        roleService.updateRoleMember(updatedRoleMember.build());
        RoleMemberBo updatedRmBo = getRoleMemberBo(roleMember.getId());

        assertEquals(3,rmBo.getAttributeDetails().size());
        assertEquals(3,updatedRmBo.getAttributeDetails().size());

        for (RoleMemberAttributeDataBo newRoleMemberAttrDataBo :  updatedRmBo.getAttributeDetails()) {
            for (RoleMemberAttributeDataBo oldRoleMemberAttrDataBo :  rmBo.getAttributeDetails()) {
                if (newRoleMemberAttrDataBo.getKimTypeId().equals(oldRoleMemberAttrDataBo.getKimTypeId()) &&
                    newRoleMemberAttrDataBo.getKimAttributeId().equals(oldRoleMemberAttrDataBo.getKimAttributeId())) {
                        assertEquals(new Long(2), newRoleMemberAttrDataBo.getVersionNumber());
                }
            }
        }
    }

    @Test
    public void testRoleMemberCreateUpdateNoAttrChange() {

        Role roleId = roleService.getRole(ROLE_ID);
        List<String> roleIds = new ArrayList<String>();
        roleIds.add(roleId.getId());

        Map<String,String> attributes = new HashMap<String,String>();
        attributes.put("parameterName", "parameterNameBefore");
        attributes.put("namespaceCode", "namespaceCodeBefore");
        attributes.put("componentName", "componentNameBefore");

        RoleMember roleMember =  roleService.createRoleMember(RoleMember.Builder.create(ROLE_ID, ROLE_MEMBER_ID1, MEMBER_ID, MEMBER_TYPE_R, ACTIVE_FROM, ACTIVE_TO1, attributes, "", "").build());
        RoleMemberBo rmBo = getRoleMemberBo(roleMember.getId());

        RoleMember.Builder updatedRoleMember = RoleMember.Builder.create(roleMember);
        updatedRoleMember.setActiveToDate(ACTIVE_TO2);
        updatedRoleMember.setAttributes(rmBo.getAttributes());

        roleService.updateRoleMember(updatedRoleMember.build());
        RoleMemberBo updatedRmBo = getRoleMemberBo(roleMember.getId());

        assertEquals(3,rmBo.getAttributeDetails().size());
        assertEquals(3,updatedRmBo.getAttributeDetails().size());

        for (RoleMemberAttributeDataBo newRoleMemberAttrDataBo :  updatedRmBo.getAttributeDetails()) {
            for (RoleMemberAttributeDataBo oldRoleMemberAttrDataBo :  rmBo.getAttributeDetails()) {
                if (newRoleMemberAttrDataBo.getKimTypeId().equals(oldRoleMemberAttrDataBo.getKimTypeId()) &&
                        newRoleMemberAttrDataBo.getKimAttributeId().equals(oldRoleMemberAttrDataBo.getKimAttributeId())) {
                    assertEquals(oldRoleMemberAttrDataBo.getAttributeValue(), newRoleMemberAttrDataBo.getAttributeValue());
                    assertEquals(new Long(2), newRoleMemberAttrDataBo.getVersionNumber());
                }
            }
        }
    }

    @Test
    public void testRoleMemberCreateUpdateRemoveOneAttr() {

        Role roleId = roleService.getRole(ROLE_ID);
        List<String> roleIds = new ArrayList<String>();
        roleIds.add(roleId.getId());

        Map<String,String> attributes = new HashMap<String,String>();
        attributes.put("parameterName", "parameterNameBefore");
        attributes.put("namespaceCode", "namespaceCodeBefore");
        attributes.put("componentName", "componentNameBefore");

        RoleMember roleMember =  roleService.createRoleMember(RoleMember.Builder.create(ROLE_ID, ROLE_MEMBER_ID1, MEMBER_ID, MEMBER_TYPE_R, ACTIVE_FROM, ACTIVE_TO1, attributes, "", "").build());
        RoleMemberBo rmBo = getRoleMemberBo(roleMember.getId());

        RoleMember.Builder updatedRoleMember = RoleMember.Builder.create(roleMember);
        updatedRoleMember.setActiveToDate(ACTIVE_TO2);
        Map<String,String> newAttributes = new HashMap<String,String>();
        newAttributes.put("parameterName", "parameterNameAfter");
        newAttributes.put("namespaceCode", "namespaceCodeAfter");
        updatedRoleMember.setAttributes(newAttributes);

        roleService.updateRoleMember(updatedRoleMember.build());
        RoleMemberBo updatedRmBo = getRoleMemberBo(roleMember.getId());

        assertEquals(3,rmBo.getAttributeDetails().size());
        assertEquals(2, updatedRmBo.getAttributeDetails().size());

        for (RoleMemberAttributeDataBo newRoleMemberAttrDataBo :  updatedRmBo.getAttributeDetails()) {
            for (RoleMemberAttributeDataBo oldRoleMemberAttrDataBo :  rmBo.getAttributeDetails()) {
                if (newRoleMemberAttrDataBo.getKimTypeId().equals(oldRoleMemberAttrDataBo.getKimTypeId()) &&
                        newRoleMemberAttrDataBo.getKimAttributeId().equals(oldRoleMemberAttrDataBo.getKimAttributeId())) {
                    assertEquals(new Long(2), newRoleMemberAttrDataBo.getVersionNumber());
                }
            }
        }
    }

    @Test
    public void testRoleMemberCreateUpdateAddOneAttr() {

        Role roleId = roleService.getRole(ROLE_ID);
        List<String> roleIds = new ArrayList<String>();
        roleIds.add(roleId.getId());

        Map<String,String> attributes = new HashMap<String,String>();
        attributes.put("parameterName", "parameterNameBefore");
        attributes.put("namespaceCode", "namespaceCodeBefore");

        RoleMember roleMember =  roleService.createRoleMember(RoleMember.Builder.create(ROLE_ID, ROLE_MEMBER_ID1, MEMBER_ID, MEMBER_TYPE_R, ACTIVE_FROM, ACTIVE_TO1, attributes, "", "").build());
        RoleMemberBo rmBo = getRoleMemberBo(roleMember.getId());

        RoleMember.Builder updatedRoleMember = RoleMember.Builder.create(roleMember);
        updatedRoleMember.setActiveToDate(ACTIVE_TO2);
        Map<String,String> newAttributes = new HashMap<String,String>();
        newAttributes.put("parameterName", "parameterNameAfter");
        newAttributes.put("namespaceCode", "namespaceCodeAfter");
        newAttributes.put("componentName", "componentNameAfter");

        updatedRoleMember.setAttributes(newAttributes);

        roleService.updateRoleMember(updatedRoleMember.build());
        RoleMemberBo updatedRmBo = getRoleMemberBo(roleMember.getId());

        assertEquals(2,rmBo.getAttributeDetails().size());
        assertEquals(3,updatedRmBo.getAttributeDetails().size());

        for (RoleMemberAttributeDataBo newRoleMemberAttrDataBo :  updatedRmBo.getAttributeDetails()) {
            for (RoleMemberAttributeDataBo oldRoleMemberAttrDataBo :  rmBo.getAttributeDetails()) {
                if (newRoleMemberAttrDataBo.getAttributeValue().equals("componentName")) {
                    assertEquals(new Long(1), newRoleMemberAttrDataBo.getVersionNumber());
                } else if (newRoleMemberAttrDataBo.getKimTypeId().equals(oldRoleMemberAttrDataBo.getKimTypeId()) &&
                        newRoleMemberAttrDataBo.getKimAttributeId().equals(oldRoleMemberAttrDataBo.getKimAttributeId())) {
                    assertEquals(new Long(2), newRoleMemberAttrDataBo.getVersionNumber());
                }
            }
        }
    }


    @Test
    public void testDelegateMemberCreateUpdateRemoveWithAttr() {

        Role r2 = roleService.getRole(ROLE_ID);
        RoleMember rm1 = roleService.assignPrincipalToRole("user2", r2.getNamespaceCode(), r2.getName(),
                new HashMap<String, String>());
        String kimTypeId = "1";

        //Create delegation
        String id = "" + KRADServiceLocator.getSequenceAccessorService().getNextAvailableSequenceNumber("KRIM_DLGN_MBR_ID_S");
        DelegateTypeBo delegate = new DelegateTypeBo();
        delegate.setDelegationId(id);
        delegate.setDelegationType(DelegationType.PRIMARY);
        delegate.setRoleId(r2.getId());
        delegate.setActive(true);
        delegate.setKimTypeId("" + kimTypeId);
        delegate = KRADServiceLocator.getBusinessObjectService().save(delegate);

        //Create delegate member
        DelegateMember.Builder delegateMemberInfo = DelegateMember.Builder.create();
        delegateMemberInfo.setDelegationId(delegate.getDelegationId());
        delegateMemberInfo.setMemberId("user4");
        delegateMemberInfo.setRoleMemberId(rm1.getId());
        delegateMemberInfo.setType( MemberType.PRINCIPAL );
        Map<String,String> attributes = new HashMap<String,String>();
        attributes.put("parameterName", "parameterNameBefore");
        attributes.put("namespaceCode", "namespaceCodeBefore");
        attributes.put("componentName", "componentNameBefore");
        delegateMemberInfo.setAttributes(attributes);
        DelegateMember inDelegateMember =  delegateMemberInfo.build();
        DelegateMember newDelegateMember = roleService.createDelegateMember(inDelegateMember);
        assertNotNull("delegateMember not created",newDelegateMember);

        DelegateMemberBo originalDelegateMemberBo = getDelegateMemberBo(newDelegateMember.getDelegationMemberId());

        //Update delegate member
        DateTime dateTimeFrom   = DateTime.now().minusDays(3);
        delegateMemberInfo.setActiveFromDate(dateTimeFrom);
        DateTime dateTimeTo = DateTime.now().plusDays(3);
        delegateMemberInfo.setActiveToDate(dateTimeTo);
        delegateMemberInfo.setDelegationMemberId(newDelegateMember.getDelegationMemberId());
        Map<String,String> newAttributes = new HashMap<String,String>();
        newAttributes.put("parameterName", "parameterNameAfter");
        newAttributes.put("namespaceCode", "namespaceCodeAfter");
        newAttributes.put("componentName", "componentNameAfter");
        delegateMemberInfo.setAttributes(newAttributes);
        newDelegateMember = delegateMemberInfo.build();
        DelegateMember updateDelegateMember = roleService.updateDelegateMember(newDelegateMember);
        assertNotNull("updateDelegateMember not updated", updateDelegateMember);
        assertEquals("activeFromDate not updated",dateTimeFrom,updateDelegateMember.getActiveFromDate());
        assertEquals("activeToDate not updated",dateTimeTo,updateDelegateMember.getActiveToDate());

        DelegateMemberBo updatedDelegateMemberBo = getDelegateMemberBo(updateDelegateMember.getDelegationMemberId());

        for (DelegateMemberAttributeDataBo newRoleMemberAttrDataBo :  updatedDelegateMemberBo.getAttributeDetails()) {
            for (DelegateMemberAttributeDataBo oldRoleMemberAttrDataBo :  updatedDelegateMemberBo.getAttributeDetails()) {
                if (newRoleMemberAttrDataBo.getKimTypeId().equals(oldRoleMemberAttrDataBo.getKimTypeId()) &&
                        newRoleMemberAttrDataBo.getKimAttributeId().equals(oldRoleMemberAttrDataBo.getKimAttributeId())) {
                    assertEquals(new Long(2), newRoleMemberAttrDataBo.getVersionNumber());
                }
            }
        }

        //remove (inactivate) delegate member
        List<DelegateMember>  removeDelegateMembers = new ArrayList<DelegateMember>();
        removeDelegateMembers.add(updateDelegateMember);
        roleService.removeDelegateMembers(removeDelegateMembers);
        DelegateMember removedDelegateMember = roleService.getDelegationMemberById(updateDelegateMember.getDelegationMemberId()) ;
        assertTrue("removeDelegateMembers did not remove the existing member",removedDelegateMember.getDelegationMemberId().equals(updateDelegateMember.getDelegationMemberId()));
        assertTrue("removeDelegateMembers did not remove the existing member",removedDelegateMember.getVersionNumber().equals(updateDelegateMember.getVersionNumber() + 1));
        assertTrue("removeDelegateMembers did not update activeToDate",removedDelegateMember.getActiveToDate().isBeforeNow());
    }

    protected RoleMemberBo getRoleMemberBo(String roleMemberId) {
        if (StringUtils.isBlank(roleMemberId)) {
            return null;
        }

        return getBusinessObjectService().findByPrimaryKey(RoleMemberBo.class, Collections.singletonMap(
                KimConstants.PrimaryKeyConstants.ID, roleMemberId));
    }

    protected DelegateMemberBo getDelegateMemberBo(String delegationMemberId) {
        if (StringUtils.isBlank(delegationMemberId)) {
            return null;
        }

        return getBusinessObjectService().findByPrimaryKey(DelegateMemberBo.class,
                Collections.singletonMap(KimConstants.PrimaryKeyConstants.DELEGATION_MEMBER_ID, delegationMemberId));
    }

    protected BusinessObjectService getBusinessObjectService() {
        if (businessObjectService == null) {
            businessObjectService = KRADServiceLocator.getBusinessObjectService();
        }
        return businessObjectService;
    }

	@Test
	public void testPrincipalHasRoleContainsGroupAssigned() {
		// "p2" is in "g1" and "g1" assigned to "r2"
		List <String>roleIds = new ArrayList<String>();
		roleIds.add("r2");
		assertTrue( "p2 is assigned to g1 and g1 assigned to r2", roleService.principalHasRole("p2", roleIds,  Collections.<String, String>emptyMap() ));
	}

    @Test
    public void testAddPrincipalToRoleAndRemove() {
        /*Role r2 = roleService.getRole("r2");
        roleService.assignPrincipalToRole("user4", r2.getNamespaceCode(), r2.getName(),
                new HashMap<String, String>());

        assertTrue("principal should be assigned to role", roleService.principalHasRole("user4", Collections.singletonList(
                r2.getId()), new HashMap<String, String>()));
        
        roleService.removePrincipalFromRole("user4", r2.getNamespaceCode(), r2.getName(), new HashMap<String, String>());

        assertFalse("principal should not be assigned to role", roleService.principalHasRole("user4", Collections.singletonList(
                r2.getId()), new HashMap<String, String>()));*/

        Role r2 = roleService.getRole("r2");
        RoleMember rm1 = roleService.assignPrincipalToRole("user4", r2.getNamespaceCode(), r2.getName(),
                new HashMap<String, String>());

        assertTrue("principal should be assigned to role", roleService.principalHasRole("user4", Collections.singletonList(
                r2.getId()), new HashMap<String, String>()));

        roleService.removePrincipalFromRole("user4", r2.getNamespaceCode(), r2.getName(), new HashMap<String, String>());

        RoleMember rm2 = roleService.assignPrincipalToRole("user4", r2.getNamespaceCode(), r2.getName(),
                new HashMap<String, String>());

        assertFalse(rm1.getId().equals(rm2.getId()));
    }
	
	/**
	 * Tests to ensure that a circular role membership cannot be created via the RoleService.
	 * 
	 * @throws Exception
	 */
	@Test (expected=IllegalArgumentException.class)
	public void testCircularRoleAssignment() {
		Map<String, String> map = new HashMap<String, String>();
		List <String>roleIds = new ArrayList<String>();
		roleIds.add("r1");
		roleService.assignRoleToRole("r5", "AUTH_SVC_TEST2", "RoleThree", map);
	}

    protected RoleResponsibilityAction createRoleResponsibilityAction() {
        Role r = roleService.getRole("r1");
        List<RoleMembership> members = roleService.getRoleMembers(Collections.singletonList("r1"), null);
        RoleMembership rm = members.get(0);

        RoleResponsibilityAction.Builder builder = RoleResponsibilityAction.Builder.create();
        builder.setRoleMemberId(rm.getMemberId());
        builder.setActionTypeCode(ActionType.APPROVE.getCode());

        RoleResponsibilityAction saved = roleService.createRoleResponsibilityAction(builder.build());
        List<RoleResponsibilityAction> rra = roleService.getRoleMemberResponsibilityActions(rm.getMemberId());
        assertEquals(1, rra.size());
        assertEquals(saved, rra.get(0));

        return rra.get(0);
    }

    @Test
    public void testCreateRoleResponsibilityAction() {
        createRoleResponsibilityAction();
    }

    @Test
    public void testUpdateRoleResponsibilityAction() {
        RoleResponsibilityAction rra = createRoleResponsibilityAction();
        RoleResponsibilityAction.Builder builder = RoleResponsibilityAction.Builder.create(rra);
        assertFalse(builder.isForceAction());
        builder.setForceAction(true);
        builder.setActionTypeCode(ActionType.ACKNOWLEDGE.getCode());

        RoleResponsibilityAction updated = roleService.updateRoleResponsibilityAction(builder.build());
        builder.setVersionNumber(updated.getVersionNumber());
        assertEquals(builder.build(), updated);

        // test that the value for rolemember is updated and not cached
        List<RoleResponsibilityAction> rras = roleService.getRoleMemberResponsibilityActions(rra.getRoleMemberId());
        assertEquals(1, rras.size());
        assertEquals(updated, rras.get(0));
    }

    @Test
    public void testDeleteRoleResponsibilityAction() {
        RoleResponsibilityAction rra = createRoleResponsibilityAction();

        roleService.deleteRoleResponsibilityAction(rra.getId());

        List<RoleResponsibilityAction> rras = roleService.getRoleMemberResponsibilityActions(rra.getRoleMemberId());
        assertEquals(0, rras.size());

        try {
            roleService.deleteRoleResponsibilityAction(rra.getId());
            fail("Expected to throw RiceIllegalStateException due to missing RuleResponsibilityAction");
        } catch (RiceIllegalStateException rise) {
            // expected
        }
    }
}
