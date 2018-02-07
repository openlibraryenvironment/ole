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

import org.joda.time.DateTime;
import org.kuali.rice.core.api.mo.common.Identifiable;

import java.util.List;
import java.util.Map;

public interface RuleContract extends Identifiable {
    /**
     * Unique Name for the Rule.
     *
     * <p>
     * This is the unique name of the Rule
     *
     * </p>
     *
     * @return name
     */
    String getName();

    /**
     * Unique id for the previous version of this Rule.
     *
     * <p>
     * This is the unique id value of the previous version of this Rule.
     * </p>
     *
     * @return previousVersionId
     */
    String getPreviousRuleId();

    /**
     * Unique Id for Template of Rule.
     *
     * <p>
     * This is the unique Id of the rule template of the rule
     * </p>
     *
     * @return ruleTemplateId
     */
    RuleTemplateContract getRuleTemplate();

    /**
     * The active indicator for the rule.
     *
     * @return true if active false if not.
     */
    boolean isActive();

    /**
     * The description of the rule.
     *
     * @return description
     */
    String getDescription();

    /**
     * The documentTypeName of the rule.
     *
     * @return documentTypeName
     */
    String getDocTypeName();

    /**
     * The ending date of the rule.
     *
     * <p>This is the date from which the rule stops being be used</p>
     *
     * @return fromDate
     */
    DateTime getFromDate();

    /**
     * The ending date of the rule.
     *
     * <p>This is the date from which the rule starts to be used</p>
     *
     * @return toDate
     */
    DateTime getToDate();

    /**
     * Shows if rule will force its action.
     *
     * @return boolean value representing if action is forced
     */
    boolean isForceAction();

    /**
     * List of rule responsibilities associated with the Rule
     *
     * @return ruleResponsibilities
     */
    List<? extends RuleResponsibilityContract> getRuleResponsibilities();

    /**
     * List of rule extensions associated with the Rule
     *
     * @return ruleExtensions
     */
    List<? extends RuleExtensionContract> getRuleExtensions();

    /**
     * rule Extensions are a key, value representation provided in a Map that extend a normal Rule
     *
     * @return ruleExtensionMap
     */
    //Map<String, String> getRuleExtensionMap();

    /**
     * Template Name of the Rule.
     *
     * <p>
     * This is the name of the rule template for the rule
     * </p>
     *
     * @return ruleTemplateName
     */
    String getRuleTemplateName();

    /**
     * Expression for rule to evaluate.
     *
     * <p>
     * This is expression definition of the rule
     * </p>
     *
     * @return ruleExpressionDef
     */
    RuleExpressionContract getRuleExpressionDef();
}
