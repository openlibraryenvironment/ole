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
package org.kuali.rice.kim.test.service;

import org.junit.Before;
import org.junit.Test;
import org.kuali.rice.core.api.membership.MemberType;
import org.kuali.rice.kim.api.role.Role;
import org.kuali.rice.kim.api.role.RoleMembership;
import org.kuali.rice.kim.api.role.RoleService;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;
import org.kuali.rice.kim.api.permission.PermissionService;
import org.kuali.rice.kim.test.KIMTestCase;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

import static org.junit.Assert.*;

/**
 * This is a description of what this class does - kellerj don't forget to fill this in. 
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class AuthorizationServiceImplTest extends KIMTestCase {

	private PermissionService permissionService;
	private RoleService roleService;

	private String principal1Id = "p1";
	private String principal2Id = "p2";
	private String principal3Id = "p3";
	
	private String group1Id = "g1";
	
	private String role1Id = "r1";
	private String role1NamespaceCode = "AUTH_SVC_TEST1";
	private String role1Description = "Role 1 Description";
	private String role1Name = "RoleOne";
	
	private String role2Id = "r2";
	private String role2NamespaceCode = "AUTH_SVC_TEST2";
	private String role2Description = "Role 2 Description";
	private String role2Name = "RoleTwo";
	
	private String permission1Name = "perm1";
	private String permission1NamespaceCode = "KR-NS";
	private String permission1Id = "p1";
	
	private String permission2Name = "perm2";
	private String permission2NamespaceCode = "KR-NS";
	private String permission2Id = "p2";

	private String permission3Name = "perm3";
	private String permission3NamespaceCode = "KR-NS";
	private String permission3Id = "p3";

	
	@Before
	public void setUp() throws Exception {
		super.setUp();
		
		permissionService = KimApiServiceLocator.getPermissionService();
		roleService = KimApiServiceLocator.getRoleService();
		
//
//
//		// set up Role "r1" with principal p1
//		RoleBo role1 = new RoleBo();
//		role1.setId(role1Id);
//		role1.setActive(true);
//		role1.setKimTypeId(getDefaultKimType().getId());
//		role1.setNamespaceCode(role1NamespaceCode);
//		role1.setDescription(role1Description);
//		role1.setName(role1Name);
//		List<RoleMemberBo> members1 = new ArrayList<RoleMemberBo>();
//		role1.setMembers(members1);
//		RoleMemberBo p1Member = new RoleMemberBo();
//		p1Member.setMemberId(principal1Id);
//		p1Member.setMemberTypeCode("P");
//		p1Member.setRoleId(role1Id);
//		p1Member.setRoleMemberId(getNewRoleMemberId());
//		members1.add(p1Member);
//		KRADServiceLocator.getBusinessObjectService().save(role1);
//
//		// set up Role "r2" with principal p3, group g1 and role r1
//		RoleBo role2 = new RoleBo();
//		role2.setId(role2Id);
//		role2.setActive(true);
//		role2.setKimTypeId(getDefaultKimType().getId());
//		role2.setNamespaceCode(role2NamespaceCode);
//		role2.setDescription(role2Description);
//		role2.setName(role2Name);
//		List<RoleMemberBo> members2 = new ArrayList<RoleMemberBo>();
//		role2.setMembers(members2);
//		RoleMemberBo p3Member = new RoleMemberBo();
//		p3Member.setMemberId(principal3Id);
//		p3Member.setMemberTypeCode("P");
//		p3Member.setRoleId(role2Id);
//		p3Member.setRoleMemberId(getNewRoleMemberId());
//		members2.add(p3Member);
//		RoleMemberBo g1Member = new RoleMemberBo();
//		g1Member.setMemberId(group1Id);
//		g1Member.setMemberTypeCode("G");
//		g1Member.setRoleId(role2Id);
//		g1Member.setRoleMemberId(getNewRoleMemberId());
//		members2.add(g1Member);
//		RoleMemberBo r1Member = new RoleMemberBo();
//		r1Member.setMemberId(role1Id);
//		r1Member.setMemberTypeCode("R");
//		r1Member.setRoleId(role2Id);
//		r1Member.setRoleMemberId(getNewRoleMemberId());
//		members2.add(r1Member);
//		KRADServiceLocator.getBusinessObjectService().save(role2);
//
//		// setup permissions
//
//		KimPermissionTemplateImpl defaultTemplate = getDefaultPermissionTemplate();
//
//		KimPermissionImpl permission1 = new KimPermissionImpl();
//		permission1.setActive(true);
//		permission1.setDescription("permission1");
//		permission1.setName(permission1Name);
//		permission1.setNamespaceCode(permission1NamespaceCode);
//		permission1.setPermissionId(permission1Id);
//		permission1.setTemplateId(defaultTemplate.getPermissionTemplateId());
//		permission1.setTemplate(defaultTemplate);
//		KRADServiceLocator.getBusinessObjectService().save(permission1);
//
//		KimPermissionImpl permission2 = new KimPermissionImpl();
//		permission2.setActive(true);
//		permission2.setDescription("permission2");
//		permission2.setName(permission2Name);
//		permission2.setNamespaceCode(permission2NamespaceCode);
//		permission2.setPermissionId(permission2Id);
//		permission2.setTemplateId(defaultTemplate.getPermissionTemplateId());
//		permission2.setTemplate(defaultTemplate);
//		KRADServiceLocator.getBusinessObjectService().save(permission2);
//
//		KimPermissionImpl permission3 = new KimPermissionImpl();
//		permission3.setActive(true);
//		permission3.setDescription("permission3");
//		permission3.setName(permission3Name);
//		permission3.setNamespaceCode(permission3NamespaceCode);
//		permission3.setPermissionId(permission3Id);
//		permission3.setTemplateId(defaultTemplate.getPermissionTemplateId());
//		permission3.setTemplate(defaultTemplate);
//		KRADServiceLocator.getBusinessObjectService().save(permission3);
//
//		// assign permissions to roles
//		// p1 -> r1
//		// p2 -> r1
//		// p3 -> r2
//
//		RolePermissionImpl role1Perm1 = new RolePermissionImpl();
//		role1Perm1.setActive(true);
//		role1Perm1.setRoleId(role1Id);
//		role1Perm1.setPermissionId(permission1Id);
//		role1Perm1.setRolePermissionId(getNewRolePermissionId());
//		KRADServiceLocator.getBusinessObjectService().save(role1Perm1);
//
//		RolePermissionImpl role1Perm2 = new RolePermissionImpl();
//		role1Perm2.setActive(true);
//		role1Perm2.setRoleId(role1Id);
//		role1Perm2.setPermissionId(permission2Id);
//		role1Perm2.setRolePermissionId(getNewRolePermissionId());
//		KRADServiceLocator.getBusinessObjectService().save(role1Perm2);
//
//		RolePermissionImpl role2Perm3 = new RolePermissionImpl();
//		role2Perm3.setActive(true);
//		role2Perm3.setRoleId(role2Id);
//		role2Perm3.setPermissionId(permission3Id);
//		role2Perm3.setRolePermissionId(getNewRolePermissionId());
//		KRADServiceLocator.getBusinessObjectService().save(role2Perm3);
	}

	@Test
	public void testRoleMembership() {
		Role role = roleService.getRole( role2Id );
		assertNotNull( "r2 must exist", role );
		ArrayList<String> roleList = new ArrayList<String>( 1 );
		roleList.add( role2Id );
		
		Collection<String> memberPrincipalIds = roleService.getRoleMemberPrincipalIds(role2NamespaceCode, role2Name,  Collections.<String, String>emptyMap());
		assertNotNull(memberPrincipalIds);
		assertEquals("RoleTwo should have 6 principal ids", 5, memberPrincipalIds.size());
		assertTrue( "p3 must belong to role", memberPrincipalIds.contains(principal3Id) );
		assertTrue( "p2 must belong to role (assigned via group)", memberPrincipalIds.contains(principal2Id) );
		assertTrue( "p1 must belong to r2 (via r1)", memberPrincipalIds.contains(principal1Id) );
		
		Collection<RoleMembership> members = roleService.getRoleMembers( roleList, Collections.<String, String>emptyMap() );
		assertNotNull( "returned list may not be null", members );
		assertFalse( "list must not be empty", members.isEmpty() );
		assertEquals("Returned list must have 4 members.", 4, members.size());
		boolean foundP3 = false;
		boolean foundG1 = false;
		boolean foundR1 = false;
		for (RoleMembership member : members) {
			if (member.getMemberId().equals(principal3Id) && member.getType().equals(MemberType.PRINCIPAL)) {
				foundP3 = true;
			} else if (member.getMemberId().equals(group1Id) && member.getType().equals(MemberType.GROUP)) {
				foundG1 = true;
			} else if (member.getMemberId().equals(principal1Id) && member.getType().equals(MemberType.PRINCIPAL)) {
				foundR1 = true;
				assertEquals("Should have r1 embedded role id.", role1Id, member.getEmbeddedRoleId());
			}
		}
		assertTrue("Failed to find p3 principal member", foundP3);
		assertTrue("Failed to find g1 group member", foundG1);
		assertTrue("Failed to find r1 role member", foundR1);
		
		role = roleService.getRole( role1Id );
		assertNotNull( "r1 must exist", role );
		roleList.clear();
		roleList.add( role1Id );
		members = roleService.getRoleMembers( roleList,  Collections.<String, String>emptyMap() );
		assertNotNull( "returned list may not be null", members );
		assertEquals("Should have 2 members", 2, members.size());
		Iterator<RoleMembership> iter = members.iterator();
		assertTrue("One of those members should be p1.", principal1Id.equals(iter.next().getMemberId()) || principal1Id.equals(iter.next().getMemberId()));
	}
	
//	@Test
//	public void testGetPermissionsForRole() {
//		List<PermissionDetailInfo> perms = authorizationService.getPermissionsForRole( "r1" );
//		System.out.println( "r1: " + perms );
//		assertTrue( "r1 must have perm1 (direct)", hasPermission( perms, "perm1" ) );
//		assertTrue( "r1 must have perm2 (direct)", hasPermission( perms, "perm2" ) );
//		assertTrue( "r1 must have perm3 (via r2)", hasPermission( perms, "perm3" ) );
//		perms = authorizationService.getPermissionsForRole( "r2" );
//		System.out.println( "r2: " + perms );
//		assertTrue( "r2 must have perm3 (direct)", hasPermission( perms, "perm3" ) );
//		assertFalse( "r2 must not have perm1", hasPermission( perms, "perm1" ) );
//		assertFalse( "r2 must not have perm2", hasPermission( perms, "perm2" ) );
//	}
	
	@Test
	public void testHasPermission() {
		
		assertTrue( "p1 must have perm1 (via r1)", permissionService.hasPermission( "p1", "KR-NS", "perm1" ));
		assertTrue( "p1 must have perm2 (via r1)", permissionService.hasPermission( "p1", "KR-NS", "perm2" ) );
		assertTrue( "p1 must have perm3 (via r2)", permissionService.hasPermission( "p1", "KR-NS", "perm3" ) );
		assertTrue( "p3 must have perm3 (via r2)", permissionService.hasPermission( "p3", "KR-NS", "perm3" ) );
		assertFalse( "p3 must not have perm1", permissionService.hasPermission( "p3", "KR-NS", "perm1")  );
		assertFalse( "p3 must not have perm2", permissionService.hasPermission( "p3", "KR-NS", "perm2")  );
	}
	
//	protected boolean hasPermission( List<PermissionDetailsInfo> perms, String permissionId ) {
//		for ( PermissionDetailsInfo perm : perms ) {
//			if ( perm.getPermissionId().equals( permissionId ) ) {
//				return true;
//			}
//		}
//		return false;
//	}
	// test that only active roles/permissions are used
	// test that only roles attached to active groups are returned
	// check that implied/implying lists are correct
	// check qualification matching
	// need hierarchical test for qualification matching
	// check namespace filters
	
	// non-qualified role/permission checks
	// qualified role/permission checks
	// add type services in test spring startup? - how in rice?
	
}
