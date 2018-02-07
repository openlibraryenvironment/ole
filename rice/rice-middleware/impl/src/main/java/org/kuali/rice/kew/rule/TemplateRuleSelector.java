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

import org.joda.time.DateTime;
import org.kuali.rice.core.api.exception.RiceIllegalArgumentException;
import org.kuali.rice.kew.api.KewApiServiceLocator;
import org.kuali.rice.kew.api.WorkflowRuntimeException;
import org.kuali.rice.kew.api.extension.ExtensionDefinition;
import org.kuali.rice.kew.api.extension.ExtensionUtils;
import org.kuali.rice.kew.api.rule.RuleTemplate;
import org.kuali.rice.kew.api.rule.RuleTemplateAttribute;
import org.kuali.rice.kew.engine.RouteContext;
import org.kuali.rice.kew.engine.node.RouteNodeInstance;
import org.kuali.rice.kew.routeheader.DocumentContent;
import org.kuali.rice.kew.routeheader.DocumentRouteHeaderValue;
import org.kuali.rice.kew.rule.bo.RuleAttribute;
import org.kuali.rice.kew.util.PerformanceLogger;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Rule selector that selects rules based on configured template name 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
class TemplateRuleSelector implements RuleSelector {
    /**
     * Records the number of selected rules, prior to MassRuleAttribute filtering
     */
    private int numberOfSelectedRules;

    /**
     * @return the number of selected rules, prior to MassRuleAttribute filtering
     */
    int getNumberOfSelectedRules() {
	return numberOfSelectedRules;
    }

    public List<Rule> selectRules(RouteContext context, DocumentRouteHeaderValue routeHeader, RouteNodeInstance nodeInstance, String selectionCriterion, Timestamp effectiveDate) {
        // for TemplateRuleSelector, the criterion is taken as a ruletemplate name
        final String ruleTemplateName = selectionCriterion;

        Set<MassRuleAttribute> massRules = new HashSet<MassRuleAttribute>();
        RuleTemplate template = KewApiServiceLocator.getRuleService().getRuleTemplateByName(ruleTemplateName);
        if (template == null) {
            throw new WorkflowRuntimeException("Could not locate the rule template with name " + ruleTemplateName + " on document " + routeHeader.getDocumentId());
        }
        for (RuleTemplateAttribute templateAttribute : template.getActiveRuleTemplateAttributes()) {
            String ruleAttributeName = templateAttribute.getRuleAttribute().getName();
            if (!RuleAttribute.isWorkflowAttribute(templateAttribute.getRuleAttribute().getType())) {
                continue;
            }
            ExtensionDefinition extensionDefinition = KewApiServiceLocator.getExtensionRepositoryService().getExtensionByName(ruleAttributeName);
            Object attribute = ExtensionUtils.loadExtension(extensionDefinition);
            if (attribute == null) {
                throw new RiceIllegalArgumentException("Failed to load WorkflowRuleAttribute for: " + extensionDefinition);
            }
            if (!WorkflowRuleAttribute.class.isAssignableFrom(attribute.getClass())) {
                throw new RiceIllegalArgumentException("Failed to locate a WorkflowRuleAttribute with the given name: " + ruleAttributeName);
            }
            if (attribute instanceof XmlConfiguredAttribute) {
                ((XmlConfiguredAttribute)attribute).setExtensionDefinition(extensionDefinition);
            }

            WorkflowRuleAttribute ruleAttribute = (WorkflowRuleAttribute)attribute;
            if (ruleAttribute instanceof MassRuleAttribute) {
                massRules.add((MassRuleAttribute) attribute);
            }

        }

        List<org.kuali.rice.kew.api.rule.Rule> rules;
        if (effectiveDate == null) {
            rules = KewApiServiceLocator.getRuleService()
                    .getRulesByTemplateNameAndDocumentTypeName(ruleTemplateName,
                            routeHeader.getDocumentType().getName());
        } else {
            rules = KewApiServiceLocator.getRuleService()
                    .getRulesByTemplateNameAndDocumentTypeNameAndEffectiveDate(ruleTemplateName,
                            routeHeader.getDocumentType().getName(), new DateTime(effectiveDate.getTime()));
        }
        numberOfSelectedRules = rules.size();

        // TODO really the route context just needs to be able to support nested create and clears
        // (i.e. a Stack model similar to transaction intercepting in Spring) and we wouldn't have to do this
        if (context.getDocument() == null) {
            context.setDocument(routeHeader);
        }
        if (context.getNodeInstance() == null) {
            context.setNodeInstance(nodeInstance);
        }
        DocumentContent documentContent = context.getDocumentContent();
        PerformanceLogger performanceLogger = new PerformanceLogger();
        // have all mass rule attributes filter the list of non applicable rules
        for (MassRuleAttribute massRuleAttribute : massRules) {
            rules = massRuleAttribute.filterNonMatchingRules(context, rules);
        }
        performanceLogger.log("Time to filter massRules for template " + template.getName());

        List<Rule> ruleList = new ArrayList<Rule>(rules.size());
        for (org.kuali.rice.kew.api.rule.Rule ruleDefinition: rules) {
            ruleList.add(new RuleImpl(ruleDefinition));
        }
        return ruleList;
    }

}
