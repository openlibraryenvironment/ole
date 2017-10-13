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
package org.kuali.rice.kew.rule;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Result of a {@link RuleExpression} evaluation 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class RuleExpressionResult {
    /**
     * The rule whose evaluation yielded this result
     */
    private final Rule rule;
    /**
     * Whether the expression succeeded
     */
    private final boolean success;
    /**
     * Any responsibilities generated from a successful evaluation
     */
    private final List<org.kuali.rice.kew.api.rule.RuleResponsibility> responsibilities;

    /**
     * Constructs a rule expression result with a success indicator but no responsibilities 
     * @param success whether the expression succeeded
     */
    public RuleExpressionResult(Rule rule, boolean success) {
        this.rule = rule;
        this.success = success;
        this.responsibilities = null;
    }

    /**
     * Constructs a rule expression result with both a success indicator and a list of responsibilities
     * @param success whether the expression succeeded
     * @param responsibilities any responsibilities generated from a successful evaluation
     */
    public RuleExpressionResult(Rule rule, boolean success, List<org.kuali.rice.kew.api.rule.RuleResponsibility> responsibilities) {
        this.rule = rule;
        this.success = success;
        this.responsibilities = responsibilities;
    }

    /**
     * Constructs a rule expression result with both a success indicator and a single responsibilities
     * @param success whether the expression succeeded
     * @param responsibility a single responsibility generated from a successful evaluation
     */
    public RuleExpressionResult(Rule rule, boolean success, org.kuali.rice.kew.api.rule.RuleResponsibility responsibility) {
        this.rule = rule;
        this.success = success;
        if (responsibility != null) {
            responsibilities = Collections.singletonList(responsibility);
        } else {
            responsibilities = null;
        }
    }

    /**
     * @return the rule that this expression result is associated with
     */
    public Rule getRule() {
        return rule;
    }

    /**
     * @return whether the evaluation was successful
     */
    public boolean isSuccess() {
        return success;
    }

    /**
     * @return any responsibilities generated from a successful evaluation
     */
    public List<org.kuali.rice.kew.api.rule.RuleResponsibility> getResponsibilities() {
        return responsibilities;
    }

    public String toString() {
        return new ToStringBuilder(this).append("rule", rule)
                                        .append("success", success)
                                        .append("responsibilities", responsibilities).toString();
    }
}
