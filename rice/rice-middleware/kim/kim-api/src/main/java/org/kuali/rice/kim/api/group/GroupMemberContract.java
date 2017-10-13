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
package org.kuali.rice.kim.api.group;


import org.kuali.rice.core.api.membership.MemberType;
import org.kuali.rice.core.api.mo.common.GloballyUnique;
import org.kuali.rice.core.api.mo.common.Identifiable;
import org.kuali.rice.core.api.mo.common.Versioned;
import org.kuali.rice.core.api.mo.common.active.InactivatableFromTo;
/**
 *
 * This is a contract for a GroupMember
 *
 * A principal or group is considered to be a "member" of a group if it is either directly assigned to the group or
 * indirectly assigned (via a nested group membership).
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public interface GroupMemberContract extends Versioned, GloballyUnique, InactivatableFromTo, Identifiable {

    /**
     * This is the id for the Group.
     *
     * <p>
     * This is a id assigned to a Group.  It defines the group this group member belongs to.
     * </p>
     *
     * @return groupId
     */
    String getGroupId();

    /**
     * This is the member id for the Group Member.
     *
     * <p>
     * This is a id value that defines the Group Member.  This value is either a Principal Id or Group Id
     * depending on the type code
     * </p>
     *
     * @return memberId
     */
    String getMemberId();

    /**
     * This is the type code for the Group Member.
     *
     * <p>
     * This is a value that defines the type of Group Member.  This value determines whether memberId is
     * either a Principal Id or Group Id
     * </p>
     *
     * @return typeCode
     */
    MemberType getType();
}
