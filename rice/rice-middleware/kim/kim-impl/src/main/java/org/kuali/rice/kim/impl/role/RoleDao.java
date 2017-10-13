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

import org.kuali.rice.kim.api.group.GroupMember;
import org.kuali.rice.kim.api.role.RoleMember;
import org.kuali.rice.kim.api.role.RoleMembership;
import org.kuali.rice.kim.impl.common.delegate.DelegateTypeBo;
import org.kuali.rice.kim.impl.common.delegate.DelegateMemberBo;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface RoleDao {

    List<RoleMemberBo> getRolePrincipalsForPrincipalIdAndRoleIds(Collection<String> roleIds, String principalId, Map<String, String> qualification);

    List<GroupMember> getGroupPrincipalsForPrincipalIdAndGroupIds(Collection<String> groupIds, String principalId);

    List<RoleMemberBo> getRoleGroupsForGroupIdsAndRoleIds(Collection<String> roleIds, Collection<String> groupIds, Map<String, String> qualification);

    Map<String, DelegateTypeBo> getDelegationImplMapFromRoleIds(Collection<String> roleIds);

    List<DelegateTypeBo> getDelegationBosForRoleIds(Collection<String> roleIds);

    List<DelegateMemberBo> getDelegationPrincipalsForPrincipalIdAndDelegationIds(Collection<String> delegationIds, String principalId);

    List<DelegateMemberBo> getDelegationGroupsForGroupIdsAndDelegationIds(Collection<String> delegationIds, List<String> groupIds);

    List<RoleMemberBo> getRoleMembersForGroupIds(String roleId, List<String> groupIds);

    List<RoleMemberBo> getRoleMembersForRoleIds(Collection<String> roleIds, String memberTypeCode, Map<String, String> qualification);

    List<RoleMemberBo> getRoleMembershipsForRoleIdsAsMembers(Collection<String> roleIds, Map<String, String> qualification);

    List<RoleMemberBo> getRoleMembershipsForMemberId(String memberType, String memberId, Map<String, String> qualification);

    List<RoleMemberBo> getRoleMembersForRoleIdsWithFilters(Collection<String> roleIds, String principalId, Collection<String> groupIds, Map<String, String> qualification);

    List<RoleBo> getRoles(Map<String, String> fieldValues);

    List<GroupMember> getGroupMembers(Collection<String> groupIds);
}
