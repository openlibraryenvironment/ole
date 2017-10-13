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
import org.kuali.rice.core.api.mo.common.Identifiable;
import org.kuali.rice.kim.api.common.delegate.DelegateTypeContract;

import java.util.List;
import java.util.Map;
/**
 * This is a contract for RoleMembership. Role members can be Principals, Groups, or other Roles.
 * Delegates can also be associated with a Role.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */

public interface RoleMembershipContract extends Identifiable {

    String getRoleId();

    Map<String, String> getQualifier();

    List<? extends DelegateTypeContract> getDelegates();

    String getMemberId();

    MemberType getType();

    /**
     * @return String Identifier of the role from which the group or principal was derived.
     */
    String getEmbeddedRoleId();

    /**
     * @return String value used to sort the role members into a meaningful order
     */
    String getRoleSortingCode();
}
