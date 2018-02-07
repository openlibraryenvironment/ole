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
package org.kuali.rice.krad.service;

import org.junit.Before;
import org.junit.Test;
import org.kuali.rice.kim.api.common.template.Template;
import org.kuali.rice.kim.api.permission.Permission;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;
import org.kuali.rice.kim.impl.common.attribute.KimAttributeDataBo;
import org.kuali.rice.kim.impl.permission.PermissionAttributeBo;
import org.kuali.rice.kim.impl.permission.PermissionBo;
import org.kuali.rice.kim.impl.permission.PermissionTemplateBo;
import org.kuali.rice.krad.kim.PopulateGroupPermissionTypeServiceImpl;
import org.kuali.rice.krad.test.KRADTestCase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertTrue;

/**
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class PopulateGroupPermissionTypeServiceImplTest extends KRADTestCase {

    PopulateGroupPermissionTypeServiceImpl permissionService;
    private String PERM_NM_POP_GROUP_GROUP_NM = "Populate Group WorkflowAdmin";
    private String PERM_NM_POP_GROUP_TYPE_DEFAULT = "Populate Groups Namespace KR*, Kim Type Default";
    private String PERM_NM_POP_GROUP_NOT_TYPE_DEFAULT = "Populate Groups Namespace KR*, Kim Type NotDefault";
    private String PERM_NM_POP_GROUP_TYPE_DEFAULT_KFS = "Populate Groups Namespace KFS*, Kim Type Default";
    private String PERM_NM_POP_GROUP_NMSPC_ALL = "Populate Groups Namespace *";
    private String PERM_NM_POP_GROUP_NMSPC_KR = "Populate Groups Namespace KR*";
    private String PERM_NM_POP_GROUP_NMSPC_KFS = "Populate Groups Namespace KFS*";
    private String PERM_NM_POP_GROUP_NMSPC_KR_WKFLW = "Populate Groups Namespace KR-WKFLW";

    @Before
    @Override
    public void setUp() throws Exception {
        super.setUp();
        permissionService = 
            new PopulateGroupPermissionTypeServiceImpl() {
            };
        
        permissionService.setExactMatchStringAttributeName("groupName");
        permissionService.setNamespaceRequiredOnStoredMap(true);
    }

    @Test
    public void testGroupNameMatchOnPopulateGroupPermission() {
        Map<String, String> requestedDetails = getGroupRequestedDetails();
        List<PermissionBo> permissionsList = new ArrayList<PermissionBo>();

        Template template = KimApiServiceLocator.getPermissionService().findPermTemplateByNamespaceCodeAndName
            ("KR-IDM","Populate Group");

        List <String> permAttrsGeneric= new ArrayList<String>();
        permAttrsGeneric.add("namespaceCode=KR*");

        List <String> permAttrsForSpecificKimTypeName= new ArrayList<String>();
        permAttrsForSpecificKimTypeName.add("namespaceCode=KR*");
        permAttrsForSpecificKimTypeName.add("kimTypeName=Default");

        List <String> permAttrsForSpecificGroupName= new ArrayList<String>();
        permAttrsForSpecificGroupName.add("groupName=WorkflowAdmin");
        permAttrsForSpecificGroupName.add("namespaceCode=KR*");

        permissionsList.add(createPermission(template, PERM_NM_POP_GROUP_NMSPC_KR, "KR-SYS", permAttrsGeneric));
        permissionsList.add(createPermission(template, PERM_NM_POP_GROUP_TYPE_DEFAULT, "KR-SYS", permAttrsForSpecificKimTypeName));
        permissionsList.add(createPermission(template, PERM_NM_POP_GROUP_GROUP_NM, "KR-SYS", permAttrsForSpecificGroupName));

        List<Permission> immutablePermissionList = new ArrayList<Permission>();
        for (PermissionBo bo : permissionsList) {
            immutablePermissionList.add(PermissionBo.to(bo));
        }
       
        List<Permission> returnedPermissions = permissionService.getMatchingPermissions(requestedDetails, immutablePermissionList);
        assertTrue(returnedPermissions.size() == 1);
        assertTrue(returnedPermissions.get(0).getName().equals(PERM_NM_POP_GROUP_GROUP_NM));
    }

    @Test
    public void testTypeNameMatchOnPopulateGroupPermission() {
        Map<String, String> requestedDetails = getGroupRequestedDetails();
        List<PermissionBo> permissionsList = new ArrayList<PermissionBo>();

        Template template = KimApiServiceLocator.getPermissionService().findPermTemplateByNamespaceCodeAndName
                ("KR-IDM","Populate Group");

        List <String> permAttrsGeneric= new ArrayList<String>();
        permAttrsGeneric.add("namespaceCode=KR*");

        List <String> permAttrsForSpecificKimTypeName= new ArrayList<String>();
        permAttrsForSpecificKimTypeName.add("namespaceCode=KR*");
        permAttrsForSpecificKimTypeName.add("kimTypeName=Default");

        List <String> permAttrsForSpecificGroupName= new ArrayList<String>();
        permAttrsForSpecificGroupName.add("groupName=NotTheGroupWeAreModifying");
        permAttrsForSpecificGroupName.add("namespaceCode=KR*");

        permissionsList.add(createPermission(template, PERM_NM_POP_GROUP_NMSPC_KR, "KR-SYS", permAttrsGeneric));
        permissionsList.add(createPermission(template, PERM_NM_POP_GROUP_TYPE_DEFAULT, "KR-SYS", permAttrsForSpecificKimTypeName));
        permissionsList.add(createPermission(template, PERM_NM_POP_GROUP_GROUP_NM, "KR-SYS", permAttrsForSpecificGroupName));

        List<Permission> immutablePermissionList = new ArrayList<Permission>();
        for (PermissionBo bo : permissionsList) {
            immutablePermissionList.add(PermissionBo.to(bo));
        }

        List<Permission> returnedPermissions = permissionService.getMatchingPermissions(requestedDetails, immutablePermissionList);
        assertTrue(returnedPermissions.size() == 1);
        assertTrue(returnedPermissions.get(0).getName().equals(PERM_NM_POP_GROUP_TYPE_DEFAULT));
    }


    @Test
    public void testTypeNameMatchOnPopulateGroupPermission2() {
        Map<String, String> requestedDetails = getGroupRequestedDetails();
        List<PermissionBo> permissionsList = new ArrayList<PermissionBo>();

        Template template = KimApiServiceLocator.getPermissionService().findPermTemplateByNamespaceCodeAndName
                ("KR-IDM","Populate Group");

        List <String> permAttrsGeneric= new ArrayList<String>();
        permAttrsGeneric.add("namespaceCode=KR*");

        List <String> permAttrsForSpecificKimTypeName= new ArrayList<String>();
        permAttrsForSpecificKimTypeName.add("namespaceCode=KR*");
        permAttrsForSpecificKimTypeName.add("kimTypeName=Default");


        List <String> permAttrsForSpecificKimTypeName2= new ArrayList<String>();
        permAttrsForSpecificKimTypeName2.add("namespaceCode=KFS*");
        permAttrsForSpecificKimTypeName2.add("kimTypeName=Default");

        List <String> permAttrsForSpecificGroupName= new ArrayList<String>();
        permAttrsForSpecificGroupName.add("groupName=NotTheGroupWeAreModifying");
        permAttrsForSpecificGroupName.add("namespaceCode=KR*");

        permissionsList.add(createPermission(template, PERM_NM_POP_GROUP_NMSPC_KR, "KR-SYS", permAttrsGeneric));
        permissionsList.add(createPermission(template, PERM_NM_POP_GROUP_TYPE_DEFAULT, "KR-SYS", permAttrsForSpecificKimTypeName));
        permissionsList.add(createPermission(template, PERM_NM_POP_GROUP_TYPE_DEFAULT_KFS, "KFS-SYS", permAttrsForSpecificKimTypeName2));
        permissionsList.add(createPermission(template, PERM_NM_POP_GROUP_GROUP_NM, "KR-SYS", permAttrsForSpecificGroupName));

        List<Permission> immutablePermissionList = new ArrayList<Permission>();
        for (PermissionBo bo : permissionsList) {
            immutablePermissionList.add(PermissionBo.to(bo));
        }

        List<Permission> returnedPermissions = permissionService.getMatchingPermissions(requestedDetails, immutablePermissionList);
        assertTrue(returnedPermissions.size() == 1);
        assertTrue(returnedPermissions.get(0).getName().equals(PERM_NM_POP_GROUP_TYPE_DEFAULT));
    }



    @Test
    public void testNamespaceMatchOnPopulateGroupPermission() {
        Map<String, String> requestedDetails = getGroupRequestedDetails();
        List<PermissionBo> permissionsList = new ArrayList<PermissionBo>();

        Template template = KimApiServiceLocator.getPermissionService().findPermTemplateByNamespaceCodeAndName
                ("KR-IDM","Populate Group");

        List <String> permAttrsGeneric= new ArrayList<String>();
        permAttrsGeneric.add("namespaceCode=KR*");

        List <String> permAttrsForSpecificKimTypeName= new ArrayList<String>();
        permAttrsForSpecificKimTypeName.add("namespaceCode=KR*");
        permAttrsForSpecificKimTypeName.add("kimTypeName=NotDefault");

        List <String> permAttrsForSpecificGroupName= new ArrayList<String>();
        permAttrsForSpecificGroupName.add("groupName=NotTheGroupWeAreModifying");
        permAttrsForSpecificGroupName.add("namespaceCode=KR*");

        permissionsList.add(createPermission(template, PERM_NM_POP_GROUP_NMSPC_KR, "KR-SYS", permAttrsGeneric));
        permissionsList.add(createPermission(template, PERM_NM_POP_GROUP_NOT_TYPE_DEFAULT, "KR-SYS", permAttrsForSpecificKimTypeName));
        permissionsList.add(createPermission(template, PERM_NM_POP_GROUP_GROUP_NM, "KR-SYS", permAttrsForSpecificGroupName));

        List<Permission> immutablePermissionList = new ArrayList<Permission>();
        for (PermissionBo bo : permissionsList) {
            immutablePermissionList.add(PermissionBo.to(bo));
        }

        List<Permission> returnedPermissions = permissionService.getMatchingPermissions(requestedDetails, immutablePermissionList);
        assertTrue(returnedPermissions.size() == 1);
        assertTrue(returnedPermissions.get(0).getName().equals(PERM_NM_POP_GROUP_NMSPC_KR));
    }

    @Test
    public void testNamespaceExactMatchOnPopulateGroupPermission() {
        Map<String, String> requestedDetails = getGroupRequestedDetails();
        List<PermissionBo> permissionsList = new ArrayList<PermissionBo>();

        Template template = KimApiServiceLocator.getPermissionService().findPermTemplateByNamespaceCodeAndName
                ("KR-IDM","Populate Group");

        List <String> permAttrsSpecficNmspc = new ArrayList<String>();
        permAttrsSpecficNmspc.add("namespaceCode=KR-WKFLW");

        List <String> permAttrsGeneric = new ArrayList<String>();
        permAttrsGeneric.add("namespaceCode=KR*");

        List <String> permAttrsGeneric2 = new ArrayList<String>();
        permAttrsGeneric2.add("namespaceCode=KFS*");

        permissionsList.add(createPermission(template, PERM_NM_POP_GROUP_NMSPC_KR, "KR-SYS", permAttrsGeneric));
        permissionsList.add(createPermission(template, PERM_NM_POP_GROUP_NMSPC_KR_WKFLW, "KR-SYS", permAttrsSpecficNmspc));
        permissionsList.add(createPermission(template, PERM_NM_POP_GROUP_NMSPC_KFS, "KR-SYS", permAttrsGeneric2));

        List<Permission> immutablePermissionList = new ArrayList<Permission>();
        for (PermissionBo bo : permissionsList) {
            immutablePermissionList.add(PermissionBo.to(bo));
        }

        List<Permission> returnedPermissions = permissionService.getMatchingPermissions(requestedDetails, immutablePermissionList);
        assertTrue(returnedPermissions.size() == 1);
        assertTrue(returnedPermissions.get(0).getName().equals(PERM_NM_POP_GROUP_NMSPC_KR_WKFLW));
    }

    @Test
    public void testNamespaceMultipleMatchOnPopulateGroupPermission() {
        Map<String, String> requestedDetails = getGroupRequestedDetails();
        List<PermissionBo> permissionsList = new ArrayList<PermissionBo>();

        Template template = KimApiServiceLocator.getPermissionService().findPermTemplateByNamespaceCodeAndName
                ("KR-IDM","Populate Group");

        List <String> permAttrsAllNmspc = new ArrayList<String>();
        permAttrsAllNmspc.add("namespaceCode=*");

        List <String> permAttrsGeneric = new ArrayList<String>();
        permAttrsGeneric.add("namespaceCode=KR*");

        List <String> permAttrsGeneric2 = new ArrayList<String>();
        permAttrsGeneric2.add("namespaceCode=KFS*");

        permissionsList.add(createPermission(template, PERM_NM_POP_GROUP_NMSPC_KR, "KR-SYS", permAttrsGeneric));
        permissionsList.add(createPermission(template, PERM_NM_POP_GROUP_NMSPC_ALL, "KR-SYS", permAttrsAllNmspc));
        permissionsList.add(createPermission(template, PERM_NM_POP_GROUP_NMSPC_KFS, "KR-SYS", permAttrsGeneric2));

        List<Permission> immutablePermissionList = new ArrayList<Permission>();
        for (PermissionBo bo : permissionsList) {
            immutablePermissionList.add(PermissionBo.to(bo));
        }

        List<Permission> returnedPermissions = permissionService.getMatchingPermissions(requestedDetails, immutablePermissionList);
        assertTrue(returnedPermissions.size() == 2);
        assertTrue(returnedPermissions.get(0).getName().equals(PERM_NM_POP_GROUP_NMSPC_KR));
        assertTrue(returnedPermissions.get(1).getName().equals(PERM_NM_POP_GROUP_NMSPC_ALL));

    }

    /**
     * This method recreates the requested details that would be encountered when trying to edit a group
     */
    private Map<String, String> getGroupRequestedDetails() {
        Map<String, String> requestedDetails = new HashMap<String, String>();
        requestedDetails.put("groupName", "WorkflowAdmin");
        requestedDetails.put("namespaceCode", "KR-WKFLW");
        requestedDetails.put("kimTypeName", "Default");
        return requestedDetails;
    }

    /**
     * @return a KimPermissionInfo object for the given name, namespace, and varargs "=" delimited attributes
     */
    private PermissionBo createPermission(Template permissionTemplate, String name, String namespace, List<String> attrs) {
        PermissionBo perm = new PermissionBo();

        perm.setName(name);
        perm.setNamespaceCode(namespace);
        perm.setTemplate(PermissionTemplateBo.from(permissionTemplate));
        perm.setTemplateId(permissionTemplate.getId());

        Map<String,String> permissionDetails = new HashMap<String,String>();
        
        for (String attr : attrs) {
            String [] splitAttr = attr.split("=", 2);
            permissionDetails.put(splitAttr[0], splitAttr[1]);
        }

        List<PermissionAttributeBo> attrBos = KimAttributeDataBo
                .createFrom(PermissionAttributeBo.class, permissionDetails,
                        perm.getTemplate().getKimTypeId());

        perm.setAttributeDetails(attrBos);
        return perm;
    }

}
