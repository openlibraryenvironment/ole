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
package org.kuali.rice.kim.impl.role;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.kuali.rice.kim.api.group.GroupMember;
import org.kuali.rice.kim.impl.common.delegate.DelegateMemberBo;
import org.kuali.rice.kim.impl.common.delegate.DelegateTypeBo;
import org.kuali.rice.kim.impl.group.GroupMemberBo;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class RoleInternalServiceImpl extends RoleServiceBase implements RoleInternalService{
    @Override
    public void principalInactivated(String principalId) {
        if (StringUtils.isBlank(principalId)) {
            throw new IllegalArgumentException("principalId is null or blank");
        }

        long oneDayInMillis = TimeUnit.DAYS.toMillis(1);
        Timestamp yesterday = new Timestamp(System.currentTimeMillis() - oneDayInMillis);

        inactivatePrincipalRoleMemberships(principalId, yesterday);
        inactivatePrincipalGroupMemberships(principalId, yesterday);
        inactivatePrincipalDelegations(principalId, yesterday);
        inactivateApplicationRoleMemberships(principalId, yesterday);
    }

    @Override
    public void roleInactivated(String roleId) {
        if (StringUtils.isBlank(roleId)) {
            throw new IllegalArgumentException("roleId is null or blank");
        }

        long oneDayInMillis = TimeUnit.DAYS.toMillis(1);
        Timestamp yesterday = new Timestamp(System.currentTimeMillis() - oneDayInMillis);

        List<String> roleIds = new ArrayList<String>();
        roleIds.add(roleId);
        inactivateRoleMemberships(roleIds, yesterday);
        inactivateRoleDelegations(roleIds, yesterday);
        inactivateMembershipsForRoleAsMember(roleIds, yesterday);
    }

    private void inactivateRoleMemberships(List<String> roleIds, Timestamp yesterday) {
        List<RoleMemberBo> roleMemberBoList = getStoredRoleMembersForRoleIds(roleIds, null, null);
        for (RoleMemberBo roleMemberBo : roleMemberBoList) {
            roleMemberBo.setActiveToDateValue(yesterday);
        }
        getBusinessObjectService().save(roleMemberBoList);
    }

    private void inactivateRoleDelegations(List<String> roleIds, Timestamp yesterday) {
        List<DelegateTypeBo> delegations = getStoredDelegationImplsForRoleIds(roleIds);
        for (DelegateTypeBo delegation : delegations) {
            delegation.setActive(false);
            for (DelegateMemberBo delegationMember : delegation.getMembers()) {
                delegationMember.setActiveToDateValue(yesterday);
            }
        }
        getBusinessObjectService().save(delegations);
    }

    private void inactivateMembershipsForRoleAsMember(List<String> roleIds, Timestamp yesterday) {
        List<RoleMemberBo> roleMemberBoList = getStoredRoleMembershipsForRoleIdsAsMembers(roleIds, null);
        for (RoleMemberBo roleMemberBo : roleMemberBoList) {
            roleMemberBo.setActiveToDateValue(yesterday);
        }
        getBusinessObjectService().save(roleMemberBoList);
    }

    @Override
    public void groupInactivated(String groupId) {
        if (StringUtils.isBlank(groupId)) {
            throw new IllegalArgumentException("groupId is null or blank");
        }

        long oneDayInMillis = TimeUnit.DAYS.toMillis(1);
        Timestamp yesterday = new Timestamp(System.currentTimeMillis() - oneDayInMillis);

        List<String> groupIds = new ArrayList<String>();
        groupIds.add(groupId);
        inactivatePrincipalGroupMemberships(groupIds, yesterday);
        inactivateGroupRoleMemberships(groupIds, yesterday);
    }

    protected void inactivateApplicationRoleMemberships(String principalId, Timestamp yesterday) {

    }

    protected void inactivatePrincipalRoleMemberships(String principalId, Timestamp yesterday) {
        // go through all roles and post-date them
        List<RoleMemberBo> roleMembers = getStoredRolePrincipalsForPrincipalIdAndRoleIds(null, principalId, null);
        Set<String> roleIds = new HashSet<String>(roleMembers.size());
        for (RoleMemberBo roleMemberBo : roleMembers) {
            roleMemberBo.setActiveToDateValue(yesterday);
            roleIds.add(roleMemberBo.getRoleId()); // add to the set of IDs
        }
        getBusinessObjectService().save(roleMembers);
    }

    protected void inactivateGroupRoleMemberships(List<String> groupIds, Timestamp yesterday) {
        List<RoleMemberBo> roleMemberBosOfGroupType = getStoredRoleGroupsForGroupIdsAndRoleIds(null, groupIds, null);
        for (RoleMemberBo roleMemberbo : roleMemberBosOfGroupType) {
            roleMemberbo.setActiveToDateValue(yesterday);
        }
        getBusinessObjectService().save(roleMemberBosOfGroupType);
    }

    protected void inactivatePrincipalGroupMemberships(String principalId, Timestamp yesterday) {
        List<GroupMember> groupMembers = getRoleDao().getGroupPrincipalsForPrincipalIdAndGroupIds(null, principalId);
        List<GroupMemberBo> groupMemberBoList = new ArrayList<GroupMemberBo>(groupMembers.size());
        for (GroupMember gm : groupMembers) {
            GroupMember.Builder builder = GroupMember.Builder.create(gm);
            builder.setActiveToDate(new DateTime(yesterday.getTime()));
            groupMemberBoList.add(GroupMemberBo.from(builder.build()));
        }
        getBusinessObjectService().save(groupMemberBoList);
    }

    protected void inactivatePrincipalGroupMemberships(List<String> groupIds, Timestamp yesterday) {
        List<GroupMember> groupMembers = getRoleDao().getGroupMembers(groupIds);
        List<GroupMemberBo> groupMemberBoList = new ArrayList<GroupMemberBo>(groupMembers.size());
        for (GroupMember groupMember : groupMembers) {
            GroupMember.Builder builder = GroupMember.Builder.create(groupMember);
            builder.setActiveToDate(new DateTime(yesterday.getTime()));
            groupMemberBoList.add(GroupMemberBo.from(builder.build()));
        }
        getBusinessObjectService().save(groupMemberBoList);
    }

    protected void inactivatePrincipalDelegations(String principalId, Timestamp yesterday) {
        List<DelegateMemberBo> delegationMembers = getStoredDelegationPrincipalsForPrincipalIdAndDelegationIds(null,
                principalId);
        for (DelegateMemberBo delegateMemberBo : delegationMembers) {
            delegateMemberBo.setActiveToDateValue(yesterday);
        }
        getBusinessObjectService().save(delegationMembers);
    }
}
