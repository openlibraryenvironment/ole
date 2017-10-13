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

import org.kuali.rice.core.api.delegation.DelegationType;

public interface RuleDelegationContract {

    /**
     * type of delegation for the RuleDelegation
     *
     * <p>Determines what kind of delegation the RuleDelegation is</p>
     *
     * @return delegationType
     */
    DelegationType getDelegationType();

    /**
     * rule associated with the RuleDelegation
     *
     * <p>This rule is run for the original rule as the delegate</p>
     *
     * @return delegationRule
     */
    RuleContract getDelegationRule();
}
