/**
 * Copyright 2005-2014 The Kuali Foundation
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
package org.kuali.rice.kim.service.impl

import groovy.mock.interceptor.MockFor
import org.junit.Assert
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test
import org.kuali.rice.core.api.criteria.CriteriaLookupService
import org.kuali.rice.core.api.criteria.GenericQueryResults
import org.kuali.rice.core.api.membership.MemberType
import org.kuali.rice.kim.api.common.assignee.Assignee
import org.kuali.rice.kim.api.common.delegate.DelegateType
import org.kuali.rice.kim.api.permission.Permission
import org.kuali.rice.kim.api.permission.PermissionService
import org.kuali.rice.kim.api.role.RoleMembership
import org.kuali.rice.kim.api.role.RoleService
import org.kuali.rice.kim.impl.permission.PermissionBo
import org.kuali.rice.kim.impl.permission.PermissionServiceImpl
import org.kuali.rice.kim.impl.permission.PermissionTemplateBo
import org.kuali.rice.kim.impl.role.RolePermissionBo
import org.kuali.rice.krad.service.BusinessObjectService
import org.apache.log4j.LogManager
import org.apache.log4j.Level


class PermissionServiceImplTest {
    private MockFor mockRoleService;
    private MockFor mockBoService;
    private MockFor mockCriteriaLookupService;

    private RoleService roleService;
    private BusinessObjectService boService;
    private CriteriaLookupService criteriaLookupService;

    PermissionService permissionService;
    PermissionServiceImpl permissionServiceImpl;

    static Map<String, PermissionBo> samplePermissions = new HashMap<String, PermissionBo>();

    @BeforeClass
    static void createSampleBOs() {
        PermissionTemplateBo firstPermissionTemplateBo = new PermissionTemplateBo(id: "permissiontemplateidone", name: "permissiontemplateone", namespaceCode: "templatenamespaceone", kimTypeId: "kimtypeidone", versionNumber: 1);
        PermissionBo firstPermissionBo = new PermissionBo(id: "permidone", name: "permissionone", namespaceCode: "namespacecodeone", active: "Y", template: firstPermissionTemplateBo, versionNumber: 1);

        PermissionTemplateBo secondPermissionTemplateBo = new PermissionTemplateBo(id: "permissiontemplateidtwo", name: "permissiontemplatetwo", namespaceCode: "templatenamespacetwo", kimTypeId: "kimtypeidtwo", versionNumber: 1);
        PermissionBo secondPermissionBo = new PermissionBo(id: "permidtwo", name: "permissiontwo", namespaceCode: "namespacecodetwo", active: "Y", template: secondPermissionTemplateBo, versionNumber: 1);

        for (bo in [firstPermissionBo, secondPermissionBo]) {
            samplePermissions.put(bo.id, bo)
        }
    }

    // Hack to set log level above the threshold where IdentityService will need to be configured and available
    // to the GlobalResourceLoader (see PermissionServiceImpl.logAuthorizationCheck & logAuthorizationCheckByTemplate)
    // and thus break some of the tests in here.
    @Before
    void setLogLevel() {
        LogManager.getLogger(PermissionServiceImpl.class).setLevel(Level.INFO);
    }

    @Before
    void setupMockContext() {
        mockRoleService = new MockFor(RoleService.class);
        mockBoService = new MockFor(BusinessObjectService.class);
        mockCriteriaLookupService = new MockFor(CriteriaLookupService.class);
    }

    @Before
    void setupServiceUnderTest() {
        permissionServiceImpl = new PermissionServiceImpl()
        permissionService = permissionServiceImpl    //assign Interface type to implementation reference for unit test only
    }

    void injectRoleServiceIntoPermissionService() {
        roleService = mockRoleService.proxyDelegateInstance();
        permissionServiceImpl.setRoleService(roleService);
    }

    void injectBusinessObjectServiceIntoPermissionService() {
        boService = mockBoService.proxyDelegateInstance();
        permissionServiceImpl.setBusinessObjectService(boService);
    }

    void injectCriteriaLookupServiceIntoPermissionService() {
        criteriaLookupService = mockCriteriaLookupService.proxyDelegateInstance();
        permissionServiceImpl.setCriteriaLookupService(criteriaLookupService)
    }

    @Test(expected = IllegalArgumentException.class)
    void testIsAuthorizedWithNullFails() {
        permissionService.isAuthorized(null, null, null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    void testIsAuthorizedWithBlanksFails() {
        permissionService.isAuthorized("", "", "", null);
    }

    @Test
    void testIsAuthorizedSucceeds() {
        String authorizedPrincipalId = "principalid";
        String authorizedNamespaceCode = "namespacecodeone";
        String authorizedPermissionName = "permissionone";
        Map<String, String> authorizedPermissionDetails = new HashMap<String, String>();
        Map<String, String> authorizedQualification = new HashMap<String, String>();

        mockBoService.demand.findMatching(1..samplePermissions.size()) {
            Class clazz, Map map -> for (PermissionBo permissionBo in samplePermissions.values()) {
                if (permissionBo.namespaceCode.equals(map.get("namespaceCode")))
                {
                    Collection<PermissionBo> permissions = new ArrayList<PermissionBo>();
                    permissions.add(permissionBo);
                    return permissions;
                }
            }
        }

        final GenericQueryResults.Builder<RolePermissionBo> results = GenericQueryResults.Builder.create();
        results.setResults([new RolePermissionBo(["roleId":"test"])]);
        mockCriteriaLookupService.demand.lookup(1) { c, q -> results.build(); }

        mockRoleService.demand.principalHasRole(1) {
            String principalId, List<String> roleIds, Map<String, String> qualification -> return true;
        }

        injectBusinessObjectServiceIntoPermissionService();
        injectCriteriaLookupServiceIntoPermissionService();
        injectRoleServiceIntoPermissionService();

        Assert.assertEquals(true, permissionService.isAuthorized(authorizedPrincipalId, authorizedNamespaceCode, authorizedPermissionName, authorizedQualification));

        mockBoService.verify(boService)
    }

    @Test(expected = IllegalArgumentException.class)
    void testIsAuthorizedByTemplateNameWithNullFails() {
        permissionService.isAuthorizedByTemplate(null, null, null, null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    void testIsAuthorizedByTemplateNameWithBlanksFails() {
        permissionService.isAuthorizedByTemplate("", "", "", null, null);
    }

    @Test
    void testIsAuthorizedByTemplateNameSucceeds() {
        String authorizedPrincipalId = "principalid";
        String authorizedNamespaceCode = "templatenamespaceone";
        String permissionTemplateName = "permissiontemplate";
        Map<String, String> authorizedPermissionDetails = new HashMap<String, String>();
        Map<String, String> authorizedQualification = new HashMap<String, String>();

        mockBoService.demand.findMatching(1..samplePermissions.size()) {
            Class clazz, Map map -> for (PermissionBo permissionBo in samplePermissions.values()) {
                if (permissionBo.template.namespaceCode.equals(map.get("template.namespaceCode")))
                {
                    Collection<PermissionBo> permissions = new ArrayList<PermissionBo>();
                    permissions.add(permissionBo);
                    return permissions;
                }
            }
        }

        final GenericQueryResults.Builder<RolePermissionBo> results = GenericQueryResults.Builder.create();
        results.setResults([new RolePermissionBo(["roleId":"test"])]);
        mockCriteriaLookupService.demand.lookup(1) { c, q -> results.build(); }

        mockRoleService.demand.principalHasRole(1) {
            String principalId, List<String> roleIds, Map<String, String> qualification -> return true;
        }

        injectBusinessObjectServiceIntoPermissionService();
        injectCriteriaLookupServiceIntoPermissionService();
        injectRoleServiceIntoPermissionService();

        Assert.assertEquals(true, permissionService.isAuthorizedByTemplate(authorizedPrincipalId, authorizedNamespaceCode, permissionTemplateName, authorizedPermissionDetails, authorizedQualification));

        mockBoService.verify(boService)
    }

    @Test(expected = IllegalArgumentException.class)
    void testGetAuthorizedPermissionsWithNullFails() {
        permissionService.getAuthorizedPermissions(null, null, null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    void testGetAuthorizedPermissionsWithBlanksFails() {
        permissionService.getAuthorizedPermissions("", "", "", null);
    }

    @Test
    void testGetAuthorizedPermissionsSucceeds() {
        String authorizedPrincipalId = "principalid";
        String authorizedNamespaceCode = "namespacecodeone";
        String authorizedPermissionName = "permissionone";
        Map<String, String> authorizedPermissionDetails = new HashMap<String, String>();
        Map<String, String> authorizedQualification = new HashMap<String, String>();
        List<Permission> expectedPermissions = new ArrayList<Permission>();
        expectedPermissions.add(PermissionBo.to(samplePermissions.get("permidone")));

        mockBoService.demand.findMatching(1..samplePermissions.size()) {
            Class clazz, Map map -> for (PermissionBo permissionBo in samplePermissions.values()) {
                if (permissionBo.namespaceCode.equals(map.get("namespaceCode")))
                {
                    Collection<PermissionBo> permissions = new ArrayList<PermissionBo>();
                    permissions.add(permissionBo);
                    return permissions;
                }
            }
        }

        final GenericQueryResults.Builder<RolePermissionBo> results = GenericQueryResults.Builder.create();
        results.setResults([new RolePermissionBo(["roleId":"test"])]);
        mockCriteriaLookupService.demand.lookup(1) { c, q -> results.build(); }

        mockRoleService.demand.principalHasRole(1) {
            String principalId, List<String> roleIds, Map<String, String> qualification -> return true;
        }

        injectBusinessObjectServiceIntoPermissionService();
        injectCriteriaLookupServiceIntoPermissionService();
        injectRoleServiceIntoPermissionService();

		List<Permission> actualPermissions = permissionService.getAuthorizedPermissions(authorizedPrincipalId, authorizedNamespaceCode, authorizedPermissionName, authorizedQualification);

        Assert.assertEquals(expectedPermissions.size(), actualPermissions.size());
        Assert.assertEquals(expectedPermissions[0], actualPermissions[0]);

        mockBoService.verify(boService)
    }

    @Test(expected = IllegalArgumentException.class)
    void testGetAuthorizedPermissionsByTemplateNameWithNullFails() {
        permissionService.getAuthorizedPermissionsByTemplate(null, null, null, null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    void testGetAuthorizedPermissionsByTemplateNameWithBlanksFails() {
        permissionService.getAuthorizedPermissionsByTemplate("", "", "", null, null);
    }

    @Test
    void testGetAuthorizedPermissionsByTemplateNameSucceeds() {
        String authorizedPrincipalId = "principalid";
        String authorizedNamespaceCode = "templatenamespaceone";
        String permissionTemplateName = "permissiontemplate";
        Map<String, String> authorizedPermissionDetails = new HashMap<String, String>();
        Map<String, String> authorizedQualification = new HashMap<String, String>();
        List<Permission> expectedPermissions = new ArrayList<Permission>();
        expectedPermissions.add(PermissionBo.to(samplePermissions.get("permidone")));

        mockBoService.demand.findMatching(1..samplePermissions.size()) {
            Class clazz, Map map -> for (PermissionBo permissionBo in samplePermissions.values()) {
                if (permissionBo.template.namespaceCode.equals(map.get("template.namespaceCode")))
                {
                    Collection<PermissionBo> permissions = new ArrayList<PermissionBo>();
                    permissions.add(permissionBo);
                    return permissions;
                }
            }
        }

        final GenericQueryResults.Builder<RolePermissionBo> results = GenericQueryResults.Builder.create();
        results.setResults([new RolePermissionBo(["roleId":"test"])]);
        mockCriteriaLookupService.demand.lookup(1) { c, q -> results.build(); }

        mockRoleService.demand.principalHasRole(1) {
            String principalId, List<String> roleIds, Map<String, String> qualification -> return true;
        }

        injectBusinessObjectServiceIntoPermissionService();
        injectCriteriaLookupServiceIntoPermissionService();
        injectRoleServiceIntoPermissionService();

        List<Permission> actualPermissions = permissionService.getAuthorizedPermissionsByTemplate(authorizedPrincipalId, authorizedNamespaceCode, permissionTemplateName, authorizedPermissionDetails, authorizedQualification);

        Assert.assertEquals(expectedPermissions.size(), actualPermissions.size());
        Assert.assertEquals(expectedPermissions[0], actualPermissions[0]);

        mockBoService.verify(boService)
    }

    @Test
    void testGetPermissionAssigneesSucceeds() {
		String authorizedPrincipalId = "principalid";
		String authorizedNamespaceCode = "namespacecodeone";
		String authorizedPermissionName = "permissionone";
		Map<String, String> authorizedPermissionDetails = new HashMap<String, String>();
		Map<String, String> authorizedQualification = new HashMap<String, String>();

		Assignee.Builder assigneeBuilder = Assignee.Builder.create("memberid", null, new ArrayList<DelegateType.Builder>());
		List<Assignee> expectedPermissions = new ArrayList<Assignee>();
		expectedPermissions.add(assigneeBuilder.build());
		
		mockBoService.demand.findMatching(1..samplePermissions.size()) {
			Class clazz, Map map -> for (PermissionBo permissionBo in samplePermissions.values()) {
				if (permissionBo.namespaceCode.equals(map.get("namespaceCode")))
				{
					Collection<PermissionBo> permissions = new ArrayList<PermissionBo>();
					permissions.add(permissionBo);
					return permissions;
				}
			}
		}

        final GenericQueryResults.Builder<RolePermissionBo> results = GenericQueryResults.Builder.create();
        results.setResults([new RolePermissionBo(["roleId":"test"])]);
        mockCriteriaLookupService.demand.lookup(1) { c, q -> results.build(); }

		mockRoleService.demand.getRoleMembers(1) {
            List<String> roleIds, Map<String, String> qualification -> List<RoleMembership> memberships = new ArrayList<RoleMembership>(1);
            RoleMembership.Builder builder = RoleMembership.Builder.create("roleidone", "rolememberId", "memberid", MemberType.PRINCIPAL, null);
            memberships.add(builder.build());
            return memberships;
        }

		injectBusinessObjectServiceIntoPermissionService();
		injectCriteriaLookupServiceIntoPermissionService();
		injectRoleServiceIntoPermissionService();

		List<Assignee> actualPermissions = permissionService.getPermissionAssignees(authorizedNamespaceCode, authorizedPermissionName, authorizedQualification);

		Assert.assertEquals(expectedPermissions.size(), actualPermissions.size());
		Assert.assertEquals(expectedPermissions[0], actualPermissions[0]);

		mockBoService.verify(boService)
    }

    @Test
    void testGetPermissionAssigneesForTemplateNameSucceeds() {
        String authorizedNamespaceCode = "templatenamespaceone";
        String permissionName = "permission";
        Map<String, String> authorizedPermissionDetails = new HashMap<String, String>();
        Map<String, String> authorizedQualification = new HashMap<String, String>();
        List<PermissionAssigneeInfo> expectedPermissions = new ArrayList<PermissionAssigneeInfo>();
        expectedPermissions.add(new PermissionAssigneeInfo("memberid", "groupId", new ArrayList<DelegateType>()));

        mockBoService.demand.findMatching(1..samplePermissions.size()) {
            Class clazz, Map map -> for (PermissionBo permissionBo in samplePermissions.values()) {
                if (permissionBo.template.namespaceCode.equals(map.get("template.namespaceCode")))
                {
                    Collection<PermissionBo> permissions = new ArrayList<PermissionBo>();
                    permissions.add(permissionBo);
                    return permissions;
                }
            }
        }

        final GenericQueryResults.Builder<RolePermissionBo> results = GenericQueryResults.Builder.create();
        results.setResults([new RolePermissionBo(["roleId":"test"])]);
        mockCriteriaLookupService.demand.lookup(1) { c, q -> results.build(); }

        mockRoleService.demand.getRoleMembers(1) {
            List<String> roleIds, Map<String, String> qualification -> List<RoleMembership> memberships = new ArrayList<RoleMembership>(1);
            RoleMembership.Builder builder = RoleMembership.Builder.create("roleidone", "rolememberId", "memberid", MemberType.PRINCIPAL, null);
            memberships.add(builder.build());
            return memberships;
        }

        injectBusinessObjectServiceIntoPermissionService();
        injectCriteriaLookupServiceIntoPermissionService();
        injectRoleServiceIntoPermissionService();

        List<Assignee> actualPermissions = permissionService.getPermissionAssigneesByTemplate(authorizedNamespaceCode, permissionName, authorizedPermissionDetails, authorizedQualification);

        Assert.assertEquals(expectedPermissions.size(), actualPermissions.size());
        Assert.assertEquals(expectedPermissions[0].principalId, actualPermissions[0].principalId);

        mockBoService.verify(boService)
    }

    public static class PermissionAssigneeInfo {
        String principalId
        String groupId
        List<DelegateType> delegates = []

        PermissionAssigneeInfo(String pId, String gId, List<DelegateType> ds) {
            principalId = pId;
            groupId = gId;
            delegates = ds;
        }
    }

}
