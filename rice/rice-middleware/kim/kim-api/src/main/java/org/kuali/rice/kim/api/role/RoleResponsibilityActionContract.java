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

import org.kuali.rice.core.api.mo.common.Identifiable;
import org.kuali.rice.core.api.mo.common.Versioned;

/**
 *
 * <p>Associates a Role/Responsibility/(Group/Principal).<p/>
 *
 * <p>When a person is assigned to a role with a responsibility, the UI must populate this table with this additional
 * information.</p>
 *
 * Data is an action request type (approve/acknowledge/fyi/etc...), and a priority number
 */
public interface RoleResponsibilityActionContract extends Versioned, Identifiable {

    /**
     * Id of the contained RoleResponsibility object.
     * @return A RoleResponsibility Id.  Can be null if there is no associated RoleResponsibility.
     */
    String getRoleResponsibilityId();

    /**
     * <p>A string representation of the action type to be taken on this RoleResponsibilityAction.</p>
     *
     * <p>Examples are
     * <ul>
     *     <li>"A" - Approve </li>
     *     <li>"F" - FYI </li>
     *     <li>"K" - Acknowledge </li>
     * </p>
     *
     * @return String representing the action type.  This can be null.
     */
	String getActionTypeCode();

    /**
     * @return Integer representing the priority assigned to this Action.  This can be a null value.
     */
    Integer getPriorityNumber();

    /**
     * Returns the action policy code.
     * @return String for the policy code.  This can be null.
     */
    String getActionPolicyCode();

    /**
     * String identifier for an associated RoleMember
     * @return RoleMember identifier.  This can be null.
     */
	String getRoleMemberId();

    /**
     * Returns a RoleResponsibility instances associated with this action.
     * @return Associated RoleResponsibility - can be null.
     */
    RoleResponsibilityContract getRoleResponsibility();

    /**
     * @return Whether this action should be is forced or not.
     */
	boolean isForceAction();
}
