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

import org.kuali.rice.kew.api.rule.RoleName;
import org.kuali.rice.kew.api.rule.RuleTemplateAttribute;
import org.kuali.rice.kew.rule.bo.RuleTemplateAttributeBo;

import java.util.List;
import java.util.Map;

/**
 * Handles communication between {@link org.kuali.rice.kew.framework.rule.attribute.WorkflowRuleAttributeHandlerService}
 * endpoints in order to invoke {@link WorkflowRuleAttribute} code which might be hosted from various applications.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public interface WorkflowRuleAttributeMediator {

    /**
     * Returns rule row configuration for the workflow rule attribute defined on the given RuleTemplateAttribute. These
     * are the rows which show up on the rule and rule delegation maintenance screen when creating or editing a rule
     * or rule delegation which uses the given attribute, as well as on the inquiry for the rule.
     *
     * <p>Will use the supplied parameters to populate the WorkflowRuleAttribute by running the validateRuleData method
     * on that attribute. The result of this call contains any validation errors, the Row objects as constructed by the
     * attribute, and the final values for the rule extensions after validation and processing has been executed.</p>
     *
     * @param parameters a map of parameters to pass to the backend WorkflowRuleAttribute for validation, may be null or
     *                   an empty map
     * @param ruleTemplateAttribute the RuleTemplateAttribute which defines the ExtensionDefinition for the
     *                              WorkflowRuleAttribute against which to validate and retrieve the rows, as well as
     *                              the required flag which should be passed to the attribute
     *
     * @return the result of validating and retrieving rule rows, will never return null
     */
    WorkflowRuleAttributeRows getRuleRows(Map<String, String> parameters, RuleTemplateAttribute ruleTemplateAttribute);

    /**
     * Equivalent to {@link #getRuleRows(java.util.Map, org.kuali.rice.kew.rule.bo.RuleTemplateAttributeBo)} but takes
     * a {@link RuleTemplateAttributeBo} instead of a {@link RuleTemplateAttribute}. Exists primarily for convenience of
     * the calling code.
     */
    WorkflowRuleAttributeRows getRuleRows(Map<String, String> parameters, RuleTemplateAttributeBo ruleTemplateAttribute);

    /**
     * Returns rule attribute row configuration for the workflow rule attribute defined on the given RuleTemplateAttribute.
     * These are the rows which show up on the Routing Report screen when using a RuleTemplate which includes the 
     * given RuleTemplateAttribute.
     *
     * <p>Will use the supplied parameters to populate the WorkflowRuleAttribute by running the validateRoutingData method
     * on that attribute.  The result of this call contains any validation errors, the Row objects as constructed by the
     * attribute, and the final values for the rule extensions after validation and processing has been executed.</p>
     *
     * @param parameters a map of parameters to pass to the backend WorkflowRuleAttribute for validation, may be null or
     *                   an empty map
     * @param ruleTemplateAttribute the RuleTemplateAttribute which defines the ExtensionDefinition for the
     *                              WorkflowRuleAttribute against which to validate and retrieve the search rows, as
     *                              well as the required flag which should be passed to the attribute
     *
     * @return the result of validating and retrieving rule search rows, will never return null
     */
    WorkflowRuleAttributeRows getRoutingDataRows(Map<String, String> parameters, RuleTemplateAttribute ruleTemplateAttribute);

    /**
     * Equivalent to {@link #getRoutingDataRows(java.util.Map, org.kuali.rice.kew.rule.bo.RuleTemplateAttributeBo)} but takes
     * a {@link RuleTemplateAttributeBo} instead of a {@link RuleTemplateAttribute}. Exists primarily for convenience of
     * the calling code.
     */
    WorkflowRuleAttributeRows getRoutingDataRows(Map<String, String> parameters, RuleTemplateAttributeBo ruleTemplateAttribute);

    /**
     * Returns rule search row configuration for the workflow rule attribute defined on the given RuleTemplateAttribute.
     * These are the rows which show up on the rule and rule delegation lookup screen when searching for rules
     * or rule delegations using a RuleTemplate which includes the given RuleTemplateAttribute.
     *
     * <p>Will use the supplied parameters to populate the WorkflowRuleAttribute by running the validateRuleData method
     * on that attribute (or if the attribute implements WorkflowRuleSearchAttribute, will execute validateSearchData).
     * The result of this call contains any validation errors, the Row objects as constructed by the attribute, and the
     * final values for the rule extensions after validation and processing has been executed.</p>
     *
     * @param parameters a map of parameters to pass to the backend WorkflowRuleAttribute for validation, may be null or
     *                   an empty map
     * @param ruleTemplateAttribute the RuleTemplateAttribute which defines the ExtensionDefinition for the
     *                              WorkflowRuleAttribute against which to validate and retrieve the search rows, as
     *                              well as the required flag which should be passed to the attribute
     *
     * @return the result of validating and retrieving rule search rows, will never return null
     */
    WorkflowRuleAttributeRows getSearchRows(Map<String, String> parameters, RuleTemplateAttribute ruleTemplateAttribute);

    /**
     * Equivalent to call {@link #getSearchRows(java.util.Map, org.kuali.rice.kew.api.rule.RuleTemplateAttribute)} but
     * passes a custom value for the required flag that is passed to the attribute instead of using the one defined by
     * the given {@link org.kuali.rice.kew.api.rule.RuleTemplateAttribute#isRequired()}. The main use case for this
     * is in the case of a lookup where it's desirable to disable required validation for search fields that have not
     * been filled in (in that case a value of "false" would be passed as the last argument to this method).
     */
    WorkflowRuleAttributeRows getSearchRows(Map<String, String> parameters, RuleTemplateAttribute ruleTemplateAttribute, boolean required);

    /**
     * Returns the list of role names as defined on the {@link RoleAttribute} defined by the given rule template
     * attribute. In this case that the defined rule template attribute does not represent a role attribute or does
     * not return any role names, this method will return an empty list.
     *
     * @param ruleTemplateAttribute the RuleTemplateAttribute which defines the ExtensionDefinition for the
     *                              RoleAttribute from which to fetch the role names
     *
     * @retun the list of role names from the attribute, or an empty list if none are defined, will never return null
     */
    List<RoleName> getRoleNames(RuleTemplateAttributeBo ruleTemplateAttribute);

}


