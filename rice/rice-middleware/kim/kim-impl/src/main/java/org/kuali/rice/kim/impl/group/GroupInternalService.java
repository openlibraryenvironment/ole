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
package org.kuali.rice.kim.impl.group;

import org.kuali.rice.kim.impl.group.GroupBo;

import java.util.List;


/**
 * Provides internal notification services for the GroupServiceImpl.  It
 * specifically allows GroupServiceImpl to notify interested parties that
 * a group's membership has changed.  
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public interface GroupInternalService {
	/**
	 * Save the GroupBo, being careful to reset the action document
	 * assignments based on any membership changes.
	 * 
	 * @param group
	 */
    public GroupBo saveWorkgroup(GroupBo group);

    /**
     * Updates KEW for workgroup members according to membership differences between the
     * two workgroups.  Since the changeset of such an operation could potentially be quite large,
     * this method should schedule the changes to occur asynchronously to mitigate transaction
     * and concurrent document modification issues.
     */
    public void updateForWorkgroupChange( String groupId,
    		List<String> oldPrincipalIds, List<String> newPrincipalIds);

    /**
     * Updates KEW for a the given document for a user who was added to a Group.  This method will generate
     * new action items for the requests on the document which are for the Group.  This method will also verify that
     * the user is, in fact, still a member of the Group at the time of the invocation of this method before
     * generating the action items.
     */
    public void updateForUserAddedToGroup(String principalId, String groupId);

    /**
     * Updates KEW for a the given document for a user who was removed from a Group.  This will delete
     * any action items for the given user on the document which were sent to that user because they were a
     * member of the Group.  This method will also verify that the user is still no longer a member of the Group
     * at the time of the method invocation before removing the action items.
     */
    public void updateForUserRemovedFromGroup(String principalId, String groupId);
}
