/*
 * Copyright 2008 The Kuali Foundation
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
package org.kuali.ole.coa.identity;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.kuali.ole.KualiTestBase;
import org.kuali.ole.coa.service.OrganizationService;
import org.kuali.ole.sys.OLEPropertyConstants;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.ole.sys.identity.OleKimAttributes;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;

public class OrganizationOptionalHierarchyRoleTypeServiceImplTest extends KualiTestBase {

    @Override
    public void setUp() throws Exception {
        super.setUp();
    }

    @Test
    public void testPerformMatch() {
        OrganizationOptionalHierarchyRoleTypeServiceImpl roleTypeService = new OrganizationOptionalHierarchyRoleTypeServiceImpl();
        roleTypeService.setOrganizationService(SpringContext.getBean(OrganizationService.class));

        Map<String,String> roleQualifier = new HashMap<String,String>();
        roleQualifier.put(OLEPropertyConstants.CHART_OF_ACCOUNTS_CODE, "BL");
        roleQualifier.put(OLEPropertyConstants.ORGANIZATION_CODE, "PHYS");
        roleQualifier.put(OleKimAttributes.DESCEND_HIERARCHY, "Y");

        Map<String,String> qualification = new HashMap<String,String>();
        qualification.put(OLEPropertyConstants.CHART_OF_ACCOUNTS_CODE, "BL");
        qualification.put(OLEPropertyConstants.ORGANIZATION_CODE, "PHYS");
        qualification.put(OleKimAttributes.DESCEND_HIERARCHY, "Y");
        assertTrue( "BL-PHYS - exact match - should have passed", roleTypeService.performMatch(qualification, roleQualifier));
        qualification.put(OLEPropertyConstants.ORGANIZATION_CODE, "BCMI");
        assertTrue( "BL-BCMI- hierarchy match - should have passed", roleTypeService.performMatch(qualification, roleQualifier));
        qualification.put(OLEPropertyConstants.ORGANIZATION_CODE, "ARSC");
        assertFalse( "BL-ARSC - higher-level org - should not have passed", roleTypeService.performMatch(qualification, roleQualifier));
    }

//    public void testPerformMatch_PSY_HMNF() {
//        OrganizationOptionalHierarchyRoleTypeServiceImpl roleTypeService = new OrganizationOptionalHierarchyRoleTypeServiceImpl();
//        roleTypeService.setOrganizationService(SpringContext.getBean(OrganizationService.class));
//
//        Map<String,String> roleQualifier = new HashMap<String,String>();
//        roleQualifier.put(OLEPropertyConstants.CHART_OF_ACCOUNTS_CODE, "BL");
//        roleQualifier.put(OLEPropertyConstants.ORGANIZATION_CODE, "PSY");
//        roleQualifier.put(OleKimAttributes.DESCEND_HIERARCHY, "Y");
//
//        Map<String,String> qualification = new HashMap<String,String>();
//        qualification.put(OLEPropertyConstants.CHART_OF_ACCOUNTS_CODE, "BL");
//        qualification.put(OLEPropertyConstants.ORGANIZATION_CODE, "HMNF");
//        qualification.put(OleKimAttributes.DESCEND_HIERARCHY, "Y");
//        assertTrue( "BL-HMNF reports to BL-PSY - should have passed", roleTypeService.performMatch(qualification, roleQualifier));
//    }

    // assumes data which may not be present in KULDBA - commenting out for now
//    public void testPrincipalHasRole() {
//        RoleService rs = SpringContext.getBean(RoleService.class);
//        ArrayList<String> roleIds = new ArrayList<String>( 1 );
//        roleIds.add( "28" );
//
//        Map<String,String> qualification = new HashMap<String,String>();
//        qualification.put(OLEPropertyConstants.CHART_OF_ACCOUNTS_CODE, "BL");
//        qualification.put(OLEPropertyConstants.ORGANIZATION_CODE, "PHYS");
//        qualification.put(OleKimAttributes.DESCEND_HIERARCHY, "Y");
//        assertTrue( "BL-PHYS - exact match - should have passed", rs.principalHasRole("3432106365", roleIds, qualification ) );
//        //qualification.put(OLEPropertyConstants.ORGANIZATION_CODE, "BCMI");
//        //assertTrue( "BL-BCMI- hierarchy match - should have passed", rs.principalHasRole("3432106365", roleIds, qualification ) );
//        //qualification.put(OLEPropertyConstants.ORGANIZATION_CODE, "ARSC");
//        //assertFalse( "BL-ARSC - higher-level org - should not have passed", rs.principalHasRole("3432106365", roleIds, qualification ) );
//    }

}
