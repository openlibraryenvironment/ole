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
package org.kuali.rice.kew.api.rule;

import org.kuali.rice.core.api.mo.common.GloballyUnique;
import org.kuali.rice.core.api.mo.common.Identifiable;
import org.kuali.rice.core.api.mo.common.Versioned;

import java.util.List;

public interface RuleResponsibilityContract extends Identifiable, GloballyUnique, Versioned {


    /**
     * Unique Id for Responsibility.
     *
     * <p>
     * This is the unique Id of the Responsibility
     * </p>
     *
     * @return responsibilityId
     */
    String getResponsibilityId();

    /**
     * code for the Action Requested
     *
     * <p>
     * This code matches the unique code for an ActionRequest
     * </p>
     *
     * @return actionRequestedCd
     */
    String getActionRequestedCd();

    /**
     * integer representation of the priority of the RuleResponsibility
     *
     * @return priority
     */
    Integer getPriority();

    /**
     * approval policy for the RuleResponsibility
     *
     * @return approvalPolicy
     */
    String getApprovePolicy();

    /**
     * unique id of the Principal for the RuleResponsibility
     *
     * @return principalId
     */
    String getPrincipalId();

    /**
     * unique id of the Group for the RuleResponsibility
     *
     * @return groupId
     */
    String getGroupId();

    /**
     * unique name of the Role for the RuleResponsibility
     *
     * @return groupId
     */
    String getRoleName();

    /**
     * list of RuleDelegations for the RuleResponsibility
     *
     * @return delegationRules
     */
    List<? extends RuleDelegationContract> getDelegationRules();

    /**
     * determines if the RuleResponsibiltity is using a kim Role
     *
     * @return boolean value representing if the RuleResponsibility is using a Role
     */
    boolean isUsingRole();

    /**
     * determines if the RuleResponsibiltity is using a kim Principal
     *
     * @return boolean value representing if the RuleResponsibility is using a Principal
     */
    boolean isUsingPrincipal();

    /**
     * determines if the RuleResponsibiltity is using a kim Group
     *
     * @return boolean value representing if the RuleResponsibility is using a Group
     */
    boolean isUsingGroup();
}
