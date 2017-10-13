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

import org.apache.commons.collections.ListUtils;
import org.apache.commons.lang.StringUtils;
import org.kuali.rice.kew.api.KewApiServiceLocator;
import org.kuali.rice.kew.api.KewApiConstants;
import org.kuali.rice.kim.api.group.GroupService;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Concrete Implementation of {@link GroupInternalService}
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class GroupInternalServiceImpl implements GroupInternalService {
    protected BusinessObjectService getBusinessObjectService() {
    	return KRADServiceLocator.getBusinessObjectService();
    }


    public GroupService getGroupService(){
    	return KimApiServiceLocator.getGroupService();
    }

    @Override
    public GroupBo saveWorkgroup(GroupBo group) {
    	GroupService ims = getGroupService();
        List<String> oldIds = Collections.emptyList();
    	if (StringUtils.isNotEmpty(group.getId())) {
            oldIds = ims.getMemberPrincipalIds(group.getId());
        }
        group = (GroupBo)getBusinessObjectService().save( group );
        List<String> newIds = ims.getMemberPrincipalIds(group.getId());
        updateForWorkgroupChange(group.getId(), oldIds, newIds);
        return group;
    }

    @Override
    public void updateForWorkgroupChange(String groupId,
    		List<String> oldPrincipalIds, List<String> newPrincipalIds) {
        MembersDiff membersDiff = getMembersDiff(oldPrincipalIds, newPrincipalIds);
        for (String removedPrincipalId : membersDiff.getRemovedPrincipalIds()) {
        	updateForUserRemovedFromGroup(removedPrincipalId, groupId);
        }
        for (String addedPrincipalId : membersDiff.getAddedPrincipalIds()) {
        	updateForUserAddedToGroup(addedPrincipalId, groupId);
        }
    }

    @Override
    public void updateForUserAddedToGroup(String principalId, String groupId) {
        // first verify that the user is still a member of the workgroup
    	if(getGroupService().isMemberOfGroup(principalId, groupId))
    	{
    	    KewApiServiceLocator.getGroupMembershipChangeQueue()
    	        .notifyMembershipChange(KewApiConstants.GroupMembershipChangeOperations.ADDED, groupId, principalId);
    	}
    }

    @Override
    public void updateForUserRemovedFromGroup(String principalId, String groupId) {
        // first verify that the user is no longer a member of the workgroup
    	if(!getGroupService().isMemberOfGroup(principalId, groupId))
    	{
            KewApiServiceLocator.getGroupMembershipChangeQueue()
                .notifyMembershipChange(KewApiConstants.GroupMembershipChangeOperations.REMOVED, groupId, principalId);
    	}

    }

    private MembersDiff getMembersDiff(List<String> oldMemberPrincipalIds, List<String> newMemberPrincipalIds) {

    	// ListUtils does not check the null case.  Which can happen when adding a new group
    	// so, if they're null make them empty lists.
    	if(oldMemberPrincipalIds == null) oldMemberPrincipalIds = new ArrayList<String>();
    	if(newMemberPrincipalIds == null) newMemberPrincipalIds = new ArrayList<String>();

        Set<String> addedPrincipalIds = new HashSet<String>(ListUtils.subtract(newMemberPrincipalIds, oldMemberPrincipalIds));
        Set<String> removedPrincipalIds = new HashSet<String>(ListUtils.subtract(oldMemberPrincipalIds, newMemberPrincipalIds));
        return new MembersDiff(addedPrincipalIds, removedPrincipalIds);
    }

    private class MembersDiff {
        private final Set<String> addedPrincipalIds;

        private final Set<String> removedPrincipalIds;

        public MembersDiff(Set<String> addedPrincipalIds, Set<String>removedPrincipalIds) {
            this.addedPrincipalIds = addedPrincipalIds;
            this.removedPrincipalIds = removedPrincipalIds;
        }

        public Set<String> getAddedPrincipalIds() {
            return addedPrincipalIds;
        }

        public Set<String> getRemovedPrincipalIds() {
            return removedPrincipalIds;
        }
    }

}
