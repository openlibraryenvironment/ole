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
package org.kuali.rice.kew.workgroup;

import org.junit.Test;
import org.kuali.rice.kew.test.KEWTestCase;
import org.kuali.rice.kim.api.KimConstants;
import org.kuali.rice.kim.api.group.Group;
import org.kuali.rice.kim.api.group.GroupService;
import org.kuali.rice.kim.api.identity.IdentityService;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;
import org.kuali.rice.test.BaselineTestCase;

import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * This is a description of what this class does - jjhanso don't forget to fill this in.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
@BaselineTestCase.BaselineMode(BaselineTestCase.Mode.NONE)
public class GroupXmlImportTest extends KEWTestCase {
    /**
     *
     * Verify that a workgroup with a bad user in the xml is not going to be put in the db.
     *
     * @throws Exception
     */

    @Test public void testGroupImportXml() throws Exception {
    	loadXmlFile("GroupXmlImportTest.xml");

        IdentityService identityService = KimApiServiceLocator.getIdentityService();
        GroupService groupService = KimApiServiceLocator.getGroupService();
        //verify that the group was ingested
        Group group = groupService.getGroupByNamespaceCodeAndName(KimConstants.KIM_GROUP_WORKFLOW_NAMESPACE_CODE,
                "TestUserGroup");

        assertNotNull(group);
        List<String> members = groupService.getMemberPrincipalIds(group.getId());
        List<String> groups = groupService.getMemberGroupIds(group.getId());
        assertTrue(groupService.isMemberOfGroup(identityService.getPrincipalByPrincipalName("ewestfal").getPrincipalId(), group.getId()));
        assertTrue(groupService.isMemberOfGroup(identityService.getPrincipalByPrincipalName("rkirkend").getPrincipalId(), group.getId()));
        assertTrue(groupService.isMemberOfGroup("2015", group.getId()));
        assertTrue(KimApiServiceLocator.getGroupService().isGroupMemberOfGroup(groupService.getGroupByNamespaceCodeAndName(
                KimConstants.KIM_GROUP_WORKFLOW_NAMESPACE_CODE, "TestWorkgroup").getId(), group.getId()));
    }
}
