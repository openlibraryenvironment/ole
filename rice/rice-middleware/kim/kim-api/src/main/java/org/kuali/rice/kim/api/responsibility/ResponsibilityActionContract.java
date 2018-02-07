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
package org.kuali.rice.kim.api.responsibility;


import org.kuali.rice.kim.api.common.delegate.DelegateTypeContract;

import java.util.List;
import java.util.Map;

/**
 * Contains information related to responsibilities in the kim system.
 */
public interface ResponsibilityActionContract {

    /**
     * The principalId the responsibility action is associated with.
     * <p>
     * Can be null as long as the {@link #getGroupId()} is not null.
     * </p>
     * <p>
     * When this is set the {@link #getGroupId()} will not be.
     * </p>
     * @return the principalId
     */
    String getPrincipalId();

    /**
     * The groupId the responsibility action is associated with.
     *
     * <p>
     * Can be null as long as the {@link #getPrincipalId()} is not null.
     * </p>
     * <p>
     * When this is set the {@link #getPrincipalId()} will not be.
     * </p>
     *
     * @return the groupId
     */
	String getGroupId();

    /**
     * The roleResponsibilityActionId the responsibility action is associated with.  Can be null.
     *
     * @return the roleResponsibilityActionId
     */
	String getRoleResponsibilityActionId();

    /**
     * The parallelRoutingGroupingCode the responsibility action is associated with.  Can be null.
     *
     * @return the parallelRoutingGroupingCode
     */
    String getParallelRoutingGroupingCode();

    /**
     * The actionTypeCode the responsibility action is associated with.  Can be null.
     *
     * @return the actionTypeCode
     */
    String getActionTypeCode();

    /**
     * The actionPolicyCode the responsibility action is associated with.  Can be null.
     *
     * @return the actionPolicyCode
     */
	String getActionPolicyCode();

    /**
     * The priorityNumber the responsibility action is associated with.  Can be null.
     *
     * @return the priorityNumber
     */
    Integer getPriorityNumber();

    /**
     * The memberRoleId the responsibility action is associated with.  Cannot be null or blank.
     *
     * @return the memberRoleId
     */
	String getMemberRoleId();

    /**
     * The responsibilityName the responsibility action is associated with.  Cannot be null or blank.
     *
     * @return the responsibilityName
     */
	String getResponsibilityName();

    /**
     * The responsibilityId the responsibility action is associated with.  Cannot be null or blank.
     *
     * @return the responsibilityId
     */
    String getResponsibilityId();

    /**
     * The responsibilityNamespaceCode the responsibility action is associated with.  Cannot be null or blank.
     *
     * @return the responsibilityNamespaceCode
     */
    String getResponsibilityNamespaceCode();

    /**
     * Whether is responsibility action is a force action.
     * @return true if is a force action
     */
	boolean isForceAction();

    /**
     * The qualifying attributes for the responsibility action.  Cannot be null.
     * @return attributes
     */
	Map<String, String> getQualifier();

    /**
     * The delegates for this responsibility action. Cannot be null but can be an empty list.
     * @return delegates
     */
	List<? extends DelegateTypeContract> getDelegates();

    /**
     * The roleId the responsibility action is associated with.  Cannot be null or blank.
     *
     * @return the roleId
     */
    String getRoleId();
}
