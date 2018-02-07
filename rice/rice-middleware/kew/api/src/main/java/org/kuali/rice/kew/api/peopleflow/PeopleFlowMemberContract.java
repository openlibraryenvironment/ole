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
package org.kuali.rice.kew.api.peopleflow;

import org.kuali.rice.core.api.membership.MemberType;
import org.kuali.rice.kew.api.action.ActionRequestPolicy;

import java.util.List;

/**
 * Interface contract for PeopleFlowDefinition members.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public interface PeopleFlowMemberContract {

    /**
     * @return the id of the member.  This will key in to different types depending on the {@link MemberType} of the
     * instance.
     */
    String getMemberId();

    /**
     * @return the {@link MemberType} of this member.  Never null.
     */
    MemberType getMemberType();

    /**
     * Returns the action request policy to use for this people flow member.  This value is only applicable in the
     * case where the {@code MemberType} is {@code ROLE}.  If the member type is anything else, this value will not
     * be considered and should ideally be set to null
     *
     * @return the action request policy to use for this people flow member if it is a role member, null if this
     * member has no request policy
     */
    ActionRequestPolicy getActionRequestPolicy();

    /**
     * @return the priority of the member.  This is equivalent to the sequential stop in the PeopleFlowDefinition, which means
     * lower integer value equates to higher priority.  The minimum priority is 1.
     */
    int getPriority();

    /**
     * Returns the responsibility id of this member.  This is a unique id which KEW can use to help identify and track
     * the responsibility represented by this people flow member.  It will be associated with any action requests that
     * are generated from this people flow membership.
     *
     * @return the responsibility id for this people flow membership
     */
    String getResponsibilityId();

    /**
     * @return the list of delegates for this member.  Should never be null but may be an empty list in the case where
     * this member has no delegates
     */
    List<? extends PeopleFlowDelegateContract> getDelegates();

}
