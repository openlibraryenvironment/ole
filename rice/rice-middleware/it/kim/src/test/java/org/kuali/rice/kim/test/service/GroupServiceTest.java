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

import org.junit.Test;
import org.kuali.rice.kim.api.group.Group;
import org.kuali.rice.kim.api.group.GroupService;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;
import org.kuali.rice.kim.test.KIMTestCase;
import org.kuali.rice.test.BaselineTestCase;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Test the GroupService 
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
@BaselineTestCase.BaselineMode(BaselineTestCase.Mode.ROLLBACK_CLEAR_DB)
public class GroupServiceTest extends KIMTestCase {

	private GroupService groupService;

	public void setUp() throws Exception {
		super.setUp();
		setGroupService(KimApiServiceLocator.getGroupService());
	}

    @Test
    public void testGetGroup() {
        Group g7 = groupService.getGroup("g7");
        assertNotNull(g7);
        assertEquals("GroupSeven", g7.getName());
        assertEquals("KUALI", g7.getNamespaceCode());
        assertEquals("Group Seven", g7.getDescription());

        // now fetch another group, this will help ensure that the cache is not always returning the same group,
        // as per the issue reported here: https://jira.springsource.org/browse/SPR-8763
        Group g8 = groupService.getGroup("g8");
        assertNotNull(g8);
        assertEquals("GroupEight", g8.getName());
        assertEquals("KUALI", g8.getNamespaceCode());
        assertEquals("Group Eight", g8.getDescription());
    }

	@Test
	public void testGetDirectMemberGroupIds() {
		List<String> groupIds = groupService.getDirectMemberGroupIds("g1");

		assertTrue( "g1 must contain group g2", groupIds.contains( "g2" ) );
		assertFalse( "g1 must not contain group g3", groupIds.contains( "g3" ) );

		groupIds = groupService.getDirectMemberGroupIds("g2");
		
		assertTrue( "g2 must contain group g3", groupIds.contains( "g3" ) );
		assertFalse( "g2 must not contain group g4 (inactive)", groupIds.contains( "g4" ) );
		
	}
	
	@Test
	public void testGetMemberGroupIds() {
		List<String> groupIds = groupService.getMemberGroupIds("g1");

		assertTrue( "g1 must contain group g2", groupIds.contains( "g2" ) );
		assertTrue( "g1 must contain group g3", groupIds.contains( "g3" ) );
		assertFalse( "g1 must not contain group g4 (inactive)", groupIds.contains( "g4" ) );

		groupIds = groupService.getMemberGroupIds("g2");

		assertTrue( "g2 must contain group g3", groupIds.contains( "g3" ) );
		assertFalse( "g2 must not contain group g1", groupIds.contains( "g1" ) );
	}
	
	// test principal membership
	@Test
	public void testPrincipalMembership() {
		assertTrue( "p1 must be in g2", groupService.isMemberOfGroup("p1", "g2") );
		assertTrue( "p1 must be direct member of g2", groupService.isDirectMemberOfGroup("p1", "g2") );
		assertTrue( "p3 must be in g2", groupService.isMemberOfGroup("p3", "g2") );
		assertFalse( "p3 should not be a direct member of g2", groupService.isDirectMemberOfGroup("p3", "g2") );
		assertFalse( "p4 should not be reported as a member of g2 (g4 is inactive)", groupService.isMemberOfGroup("p4", "g2") );
		
		// re-activate group 4
		Group g4Info = groupService.getGroup("g4");
        Group.Builder builder = Group.Builder.create(g4Info);
		builder.setActive(true);

		Group ug = groupService.updateGroup("g4", builder.build());
        assertTrue(ug.isActive());

		Group gg = groupService.getGroup("g4");
        assertTrue(gg.isActive());
		assertTrue( "p4 should be reported as a member of g2 (now that g4 is active)", groupService.isMemberOfGroup("p4", "g2") );

        builder = Group.Builder.create(gg);
        builder.setActive(false);
        groupService.updateGroup(builder.build());
        assertFalse( "p4 should be reported as a member of g2 (now that g4 is active)", groupService.isMemberOfGroup("p4", "g2") );
	}

	public GroupService getGroupService() {
		return this.groupService;
	}

	public void setGroupService(GroupService groupService) {
		this.groupService = groupService;
	}


}
