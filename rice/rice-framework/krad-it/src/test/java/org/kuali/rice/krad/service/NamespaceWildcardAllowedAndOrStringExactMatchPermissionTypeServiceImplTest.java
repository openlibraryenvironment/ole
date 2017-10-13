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
import org.kuali.rice.krad.kim.NamespaceWildcardAllowedAndOrStringExactMatchPermissionTypeServiceImpl;
import org.kuali.rice.krad.test.KRADTestCase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertTrue;

/**
 * NamespaceWildcardAllowedAndOrStringExactMatchPermissionTypeServiceImplTest tests {@link NamespaceWildcardAllowedAndOrStringExactMatchPermissionTypeServiceImpl}
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class NamespaceWildcardAllowedAndOrStringExactMatchPermissionTypeServiceImplTest extends KRADTestCase {

    final static String INGESTER_ACTION = "org.kuali.rice.core.web.impex.IngesterAction";
    NamespaceWildcardAllowedAndOrStringExactMatchPermissionTypeServiceImpl permissionService;

    @Before
    @Override
    public void setUp() throws Exception {
        super.setUp();
        permissionService = 
            new NamespaceWildcardAllowedAndOrStringExactMatchPermissionTypeServiceImpl() {
                @Override protected boolean isCheckRequiredAttributes() {
                    return true;
                }
            };
        
        permissionService.setExactMatchStringAttributeName("actionClass");
        permissionService.setNamespaceRequiredOnStoredMap(false);

    }

    @Test
    /**
     * tests {@link NamespaceWildcardAllowedAndOrStringExactMatchPermissionTypeServiceImpl#getMatchingPermissions(java.util.Map, java.util.List)}
     */
    public void testIngesterPermissionExampleLikeRice() {
        Map<String, String> requestedDetails = getUseIngesterRequestedDetails();
        
        List<PermissionBo> permissionsList = new ArrayList<PermissionBo>();

        Template template = KimApiServiceLocator.getPermissionService().findPermTemplateByNamespaceCodeAndName("KR-NS",
                "Use Screen");
        permissionsList.add(createPermission(template, "Use All Screens", "KR-SYS", "namespaceCode=KR*"));
        PermissionBo exactMatch = createPermission(template, "Use Ingester Screen", "KR-WKFLW", "actionClass=" + INGESTER_ACTION, "namespaceCode=KR-WKFLW");
        permissionsList.add(exactMatch);

        List<Permission> immutablePermissionList = new ArrayList<Permission>();
        for (PermissionBo bo : permissionsList) {
            immutablePermissionList.add(PermissionBo.to(bo));
        }
       
        List<Permission> returnedPermissions = permissionService.getMatchingPermissions(requestedDetails, immutablePermissionList);
        assertTrue(returnedPermissions.size() == 1);
        assertTrue(returnedPermissions.get(0).equals(PermissionBo.to(exactMatch)));
    }
    
    /**
     * tests for the data described in KULRICE-3770
     */
    @Test
    public void testIngesterPermissionExampleLikeKFS() {
        Map<String, String> requestedDetails = getUseIngesterRequestedDetails();
        
        List<PermissionBo> permissionsList = new ArrayList<PermissionBo>();

        Template template = KimApiServiceLocator.getPermissionService().findPermTemplateByNamespaceCodeAndName("KR-NS", "Use Screen");
        permissionsList.add(createPermission(template, "Use Screen", "KR-SYS", "namespaceCode=KR*"));
        PermissionBo exactMatch = createPermission(template, "Use Screen", "KR-WKFLW", "actionClass=" + INGESTER_ACTION);
        permissionsList.add(exactMatch);

        List<Permission> immutablePermissionList = new ArrayList<Permission>();
        for (PermissionBo bo : permissionsList) { immutablePermissionList.add(PermissionBo.to(bo));}
       
        List<Permission> returnedPermissions = permissionService.getMatchingPermissions(requestedDetails, immutablePermissionList);
        assertTrue(returnedPermissions.size() == 1);
        assertTrue(returnedPermissions.get(0).equals(PermissionBo.to(exactMatch)));
    }
    
    /**
     * recreates the requested details that would be encountered when accessing
     * the xml ingester screen.
     */
    private Map<String, String> getUseIngesterRequestedDetails() {
        Map<String, String> requestedDetails = new HashMap<String, String>();
        requestedDetails.put("actionClass", INGESTER_ACTION);
        requestedDetails.put("namespaceCode", "KR-WKFLW");
        return requestedDetails;
    }

    /**
     * creates a test permission
     *
     * @param permissionTemplate - the Template to use to create this permission
     * @param name - the permission name
     * @param namespace - the namespace code to use
     * @param attrs - attributes to set for this permission
     *
     * @return a KimPermissionInfo object for the given name, namespace, and varargs "=" delimited attributes
     */
    private PermissionBo createPermission(Template permissionTemplate, String name, String namespace, String ... attrs) {
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
