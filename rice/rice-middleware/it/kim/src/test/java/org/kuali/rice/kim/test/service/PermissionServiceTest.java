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

import org.junit.Ignore;
import org.junit.Test;
import org.kuali.rice.kim.api.common.assignee.Assignee;
import org.kuali.rice.kim.api.permission.PermissionService;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;
import org.kuali.rice.kim.impl.common.template.TemplateBo;
import org.kuali.rice.kim.impl.permission.PermissionBo;
import org.kuali.rice.kim.test.KIMTestCase;
import org.kuali.rice.test.BaselineTestCase;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Test the PermissionService
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
@BaselineTestCase.BaselineMode(BaselineTestCase.Mode.NONE)
public class PermissionServiceTest extends KIMTestCase {

	private PermissionService permissionService;

	@Override
	public void setUp() throws Exception {
		super.setUp();
		setPermissionService(KimApiServiceLocator.getPermissionService());
	}

	@Test
	public void testHasPermission() {
		assertTrue(getPermissionService().hasPermission("entity123pId", "KR-NS", "perm1"));
		assertTrue(getPermissionService().hasPermission("entity123pId", "KR-NS", "perm2"));
		assertFalse(getPermissionService().hasPermission("entity124pId", "KR-NS", "perm2"));
	}
	
	@Test
	public void testIsAuthorized() {
		assertTrue(getPermissionService().isAuthorized("entity123pId", "KR-NS", "perm1", new HashMap<String, String>()));
		assertTrue(getPermissionService().isAuthorized("entity123pId", "KR-NS", "perm2", new HashMap<String, String>()));
		assertFalse(getPermissionService().isAuthorized("entity124pId", "KR-NS", "perm2", new HashMap<String, String>()));
	}
	
	@Test
	public void testHasPermissionByTemplateName() {
		assertTrue(getPermissionService().hasPermissionByTemplate("entity123pId", "KUALI", "Default",
                new HashMap<String, String>()));
		// TODO - getting a SOAPFaultException on this call; fix and un-comment
		// assertFalse(getPermissionService().hasPermissionByTemplate("entity124pId", "KUALI", "Default", new Map<String, String>()));
	}
	
	@Test
    @Ignore // TODO implement testIsAuthorizedByTemplateName
    public void testIsAuthorizedByTemplateName() {
		// assertTrue(getPermissionService().isAuthorizedByTemplate(principalId, namespaceCode, permissionTemplateName, permissionDetails, qualification)("entity123pId", "KR-NS", "1", new Map<String, String>()));
	}
	
	@Test
	public void testGetPermissionAssignees() {
		
		List<Assignee> assignees = getPermissionService().getPermissionAssignees("KUALI", "Log In Kuali Portal", Collections.<String, String>emptyMap());
		assertNotNull(assignees);
		assertEquals(1, assignees.size());
		Assignee permInfo = assignees.get(0);
		assertEquals("entity123pId", permInfo.getPrincipalId());
		assignees = getPermissionService().getPermissionAssignees("KUALI", "Not A Valid Permission Name", Collections.<String, String>emptyMap());
		// TODO - jax-ws remoted service returns null; local return empty List. Fix webservice return
		assertTrue(null == assignees || assignees.size() == 0);
	}
	
	@Test
    @Ignore // TODO implement testGetPermissionAssigneesForTemplateName
    public void testGetPermissionAssigneesForTemplateName() {
		/*
		List<PermissionAssigneeInfo> assignees = getPermissionService().getPermissionAssignees("KUALI", "Log In", null, null);
		assertNotNull(assignees);
		assertEquals(1, assignees.size());
		PermissionAssigneeInfo permInfo = assignees.get(0);
		assertEquals("entity123pId", permInfo.getPrincipalId());
		assignees = getPermissionService().getPermissionAssignees("KUALI", "Not A Valid Permission Name", null, null);
		assertNull(assignees);
		*/
	}
	
	@Test
    @Ignore // TODO implement testIsPermissionDefined
    public void testIsPermissionDefined() {
	}
	
	@Test
    @Ignore // TODO implement testIsPermissionDefinedForTemplateName
    public void testIsPermissionDefinedForTemplateName() {
	}
	
	@Test
    @Ignore // TODO implement testGetAuthorizedPermissions
    public void testGetAuthorizedPermissions() {
	}
	
	@Test
    @Ignore // TODO implement testGetAuthorizedPermissionsByTemplateName
    public void testGetAuthorizedPermissionsByTemplateName() {
	}
	
	@Test
	public void testGetPermission() {
		PermissionBo permissionBo = PermissionBo.from(getPermissionService().getPermission("p1"));

		assertNotNull(permissionBo);
		assertEquals("perm1", permissionBo.getName());
		assertEquals("KR-NS", permissionBo.getNamespaceCode());
		assertEquals(0, permissionBo.getDetails().size());
		assertTrue(permissionBo.isActive());
		
		TemplateBo templateBo = permissionBo.getTemplate();
		assertNotNull(templateBo);
		assertTrue(templateBo.isActive());
		assertEquals("1", templateBo.getKimTypeId());
		assertEquals("Default", templateBo.getName());
		assertEquals("KUALI", templateBo.getNamespaceCode());
		
		permissionBo = PermissionBo.from(getPermissionService().getPermission("p0"));
		assertNull(permissionBo);
	}
	
	@Test
    @Ignore // TODO implement testGetPermissionsByTemplateName
    public void testGetPermissionsByTemplateName() {
	}
	
	@Test
    @Ignore // TODO implement testGetPermissionsByName
    public void testGetPermissionsByName() {
	}
	
	@Test
    @Ignore // TODO implement testLookupPermissions
    public void testLookupPermissions() {
	}
	
	@Test
    @Ignore // TODO implement testGetRoleIdsForPermission
    public void testGetRoleIdsForPermission() {
	}
	
	@Test
    @Ignore // TODO implement testGetRoleIdsForPermissions
    public void testGetRoleIdsForPermissions() {
	}
	
	@Test
    @Ignore // TODO implement testGetPermissionDetailLabel
    public void testGetPermissionDetailLabel() {
	}

	public PermissionService getPermissionService() {
		return this.permissionService;
	}

	public void setPermissionService(PermissionService permissionService) {
		this.permissionService = permissionService;
	}

}
