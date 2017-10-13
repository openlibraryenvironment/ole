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
package org.kuali.rice.kim.api.role;

import org.kuali.rice.core.api.membership.MemberType;
import org.kuali.rice.core.api.mo.common.GloballyUnique;
import org.kuali.rice.core.api.mo.common.Identifiable;
import org.kuali.rice.core.api.mo.common.Versioned;
import org.kuali.rice.core.api.mo.common.active.InactivatableFromTo;

import java.util.List;
import java.util.Map;
/**
 * This is a contract for RoleMember. Role members can be Principals, Groups, or other Roles.
 * Each RoleMember has certain permissions and responsibilities assigned to it based on which role it belongs to.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public interface RoleMemberContract extends Versioned, GloballyUnique, InactivatableFromTo, Identifiable {
    /**
     * This is the member id for the Role Member.
     *
     * <p>
     * This is a id value that defines the Role Member.  This value is either a Principal Id, Group Id, or Role Id
     * depending on the type code
     * </p>
     *
     * @return memberId
     */
    String getMemberId();

    /**
     * This is the type code for the Role Member.
     *
     * <p>
     * This is a value that defines the type of Role Member.  This value determines whether memberId is
     * either a Principal Id, Group Id, or Role Id
     * </p>
     *
     * @return typeCode
     */
    MemberType getType();

    /**
     * This is the id for the Role.
     *
     * <p>
     * This is a id assigned to a Role.  It defines the role this role member belongs to.
     * </p>
     *
     * @return roleId
     */
    String getRoleId();

    /**
     * This returns specific attributes to be set on a Role Member in order to match permission and responsibilitys.
     * These attributes match the attributes set to a KimType
     *
     * @return attributes
     */
    Map<String, String> getAttributes();

    /**
     * This returns a list of role responsibility actions assigned to a role member
     *
     * @return roleRspActions
     */
    List<? extends RoleResponsibilityActionContract> getRoleRspActions();

    String getMemberName();

    String getMemberNamespaceCode();
}
