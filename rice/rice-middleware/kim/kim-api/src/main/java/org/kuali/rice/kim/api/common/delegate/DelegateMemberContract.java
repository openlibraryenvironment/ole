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
package org.kuali.rice.kim.api.common.delegate;

import org.kuali.rice.core.api.membership.MemberType;
import org.kuali.rice.core.api.mo.common.Versioned;
import org.kuali.rice.core.api.mo.common.active.InactivatableFromTo;

import java.util.Map;
/**
 * This is a contract for a DelegateMember. Delegates are users that a member of a
 * Role has authorized to have the same Permissions and take the same actions as that
 * member is authorized to take.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */

public interface DelegateMemberContract extends Versioned, InactivatableFromTo {

    String getDelegationMemberId();

    String getDelegationId();

    String getRoleMemberId();

    MemberType getType();

    String getMemberId();

    Map<String, String> getAttributes();
}
