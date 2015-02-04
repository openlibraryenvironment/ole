/*
 * Copyright 2009 The Kuali Foundation
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.kuali.ole.KualiTestBase;
import org.kuali.ole.coa.service.ChartService;
import org.kuali.ole.sys.OLEConstants;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.ole.sys.identity.OleKimAttributes;
import org.kuali.rice.kim.api.identity.Person;
import org.kuali.rice.kim.api.identity.PersonService;
import org.kuali.rice.kim.api.role.RoleMembership;
import org.kuali.rice.kim.api.role.RoleService;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;

import static junit.framework.Assert.assertEquals;
import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertNotNull;

public class ChartManagerRoleTest extends KualiTestBase {


    @Test
    public void testGettingPersonForChartManagers() {
        RoleService rms = KimApiServiceLocator.getRoleService();
        List<String> roleIds = new ArrayList<String>();

        Map<String,String> qualification = new HashMap<String,String>();

        for ( String chart : SpringContext.getBean(ChartService.class).getAllChartCodes() ) {

            qualification.put(OleKimAttributes.CHART_OF_ACCOUNTS_CODE, chart);

//            System.out.println( chart );
            roleIds.add(rms.getRoleIdByNamespaceCodeAndName(OLEConstants.CoreModuleNamespaces.OLE, OLEConstants.SysKimApiConstants.CHART_MANAGER_KIM_ROLE_NAME));
            Collection<RoleMembership> chartManagers = rms.getRoleMembers(roleIds, qualification);
//            System.out.println( chartManagers );

            assertEquals( "There should be only one chart manager per chart: " + chart, 1, chartManagers.size() );

            for ( RoleMembership rmi : chartManagers ) {
                Person chartManager = SpringContext.getBean(PersonService.class).getPerson( rmi.getMemberId() );
                assertNotNull( "unable to retrieve person object for principalId: " + rmi.getMemberId(), chartManager );
                assertFalse( "name should not have been blank", chartManager.getName().equals("") );
                assertFalse( "campus code should not have been blank", chartManager.getCampusCode().equals("") );
            }
        }
    }
}
