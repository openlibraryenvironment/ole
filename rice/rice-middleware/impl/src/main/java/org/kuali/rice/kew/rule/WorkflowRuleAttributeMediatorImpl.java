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

import org.kuali.rice.kew.api.extension.ExtensionDefinition;
import org.kuali.rice.kew.api.rule.RoleName;
import org.kuali.rice.kew.api.rule.RuleTemplateAttribute;
import org.kuali.rice.kew.framework.KewFrameworkServiceLocator;
import org.kuali.rice.kew.framework.rule.attribute.WorkflowRuleAttributeFields;
import org.kuali.rice.kew.framework.rule.attribute.WorkflowRuleAttributeHandlerService;
import org.kuali.rice.kew.rule.bo.RuleAttribute;
import org.kuali.rice.kew.rule.bo.RuleTemplateAttributeBo;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Reference implementation of {@code WorkflowRuleAttributeMediator}.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class WorkflowRuleAttributeMediatorImpl implements WorkflowRuleAttributeMediator {

    @Override
    public WorkflowRuleAttributeRows getRuleRows(Map<String, String> parameters, RuleTemplateAttributeBo ruleTemplateAttribute) {
        required(ruleTemplateAttribute, "ruleTemplateAttribute");
        if (parameters == null) {
            parameters = Collections.emptyMap();
        }
        RuleAttribute ruleAttribute = ruleTemplateAttribute.getRuleAttribute();
        ExtensionDefinition extensionDefinition = RuleAttribute.to(ruleAttribute);
        WorkflowRuleAttributeHandlerService handler = getHandler(extensionDefinition);
        WorkflowRuleAttributeFields fields = handler.getRuleFields(parameters, extensionDefinition, ruleTemplateAttribute.isRequired());
        return new WorkflowRuleAttributeRows(fields);
    }

    @Override
    public WorkflowRuleAttributeRows getRuleRows(Map<String, String> parameters, RuleTemplateAttribute ruleTemplateAttribute) {
        required(ruleTemplateAttribute, "ruleTemplateAttribute");
        if (parameters == null) {
            parameters = Collections.emptyMap();
        }
        ExtensionDefinition extensionDefinition = ruleTemplateAttribute.getRuleAttribute();
        WorkflowRuleAttributeHandlerService handler = getHandler(extensionDefinition);
        WorkflowRuleAttributeFields fields = handler.getRuleFields(parameters, extensionDefinition, ruleTemplateAttribute.isRequired());
        return new WorkflowRuleAttributeRows(fields);
    }
    
    @Override
    public WorkflowRuleAttributeRows getRoutingDataRows(Map<String, String> parameters, RuleTemplateAttributeBo ruleTemplateAttribute) {
        required(ruleTemplateAttribute, "ruleTemplateAttribute");
        if (parameters == null) {
            parameters = Collections.emptyMap();
        }
        RuleAttribute ruleAttribute = ruleTemplateAttribute.getRuleAttribute();
        ExtensionDefinition extensionDefinition = RuleAttribute.to(ruleAttribute);
        WorkflowRuleAttributeHandlerService handler = getHandler(extensionDefinition);
        WorkflowRuleAttributeFields fields = handler.getRoutingDataFields(parameters, extensionDefinition, ruleTemplateAttribute.isRequired());
        return new WorkflowRuleAttributeRows(fields);
    }
    
    @Override
    public WorkflowRuleAttributeRows getRoutingDataRows(Map<String, String> parameters, RuleTemplateAttribute ruleTemplateAttribute) {
        required(ruleTemplateAttribute, "ruleTemplateAttribute");
        if (parameters == null) {
            parameters = Collections.emptyMap();
        }
        ExtensionDefinition extensionDefinition = ruleTemplateAttribute.getRuleAttribute();
        WorkflowRuleAttributeHandlerService handler = getHandler(extensionDefinition);
        WorkflowRuleAttributeFields fields = handler.getRoutingDataFields(parameters, extensionDefinition, ruleTemplateAttribute.isRequired());
        return new WorkflowRuleAttributeRows(fields);
    }

    @Override
    public WorkflowRuleAttributeRows getSearchRows(Map<String, String> parameters, RuleTemplateAttribute ruleTemplateAttribute) {
        required(ruleTemplateAttribute, "ruleTemplateAttribute");
        if (parameters == null) {
            parameters = Collections.emptyMap();
        }
        ExtensionDefinition extensionDefinition = ruleTemplateAttribute.getRuleAttribute();
        WorkflowRuleAttributeHandlerService handler = getHandler(extensionDefinition);
        WorkflowRuleAttributeFields fields = handler.getSearchFields(parameters, extensionDefinition, ruleTemplateAttribute.isRequired());
        return new WorkflowRuleAttributeRows(fields);

    }

    @Override
    public WorkflowRuleAttributeRows getSearchRows(Map<String, String> parameters, RuleTemplateAttribute ruleTemplateAttribute, boolean required) {
        required(ruleTemplateAttribute, "ruleTemplateAttribute");
        if (parameters == null) {
            parameters = Collections.emptyMap();
        }
        ExtensionDefinition extensionDefinition = ruleTemplateAttribute.getRuleAttribute();
        WorkflowRuleAttributeHandlerService handler = getHandler(extensionDefinition);
        WorkflowRuleAttributeFields fields = handler.getSearchFields(parameters, extensionDefinition, required);
        return new WorkflowRuleAttributeRows(fields);

    }

    @Override
    public List<RoleName> getRoleNames(RuleTemplateAttributeBo ruleTemplateAttribute) {
        required(ruleTemplateAttribute, "ruleTemplateAttribute");
        RuleAttribute ruleAttribute = ruleTemplateAttribute.getRuleAttribute();
        ExtensionDefinition extensionDefinition = RuleAttribute.to(ruleAttribute);
        WorkflowRuleAttributeHandlerService handler = getHandler(extensionDefinition);
        return handler.getRoleNames(extensionDefinition);
    }

    private void required(Object object, String name) {
        if (object == null) {
            throw new IllegalArgumentException(name + " must not be null");
        }
    }

    /**
     * Retrieves the WorkflowRuleAttributeHandlerService which can be used to handle the given ExtensionDefinition. If
     * such a service cannot be loaded, an IllegalStateException is thrown.
     *
     * @param extensionDefinition the definition of the extension which will be accessed through the handler service
     *
     * @return a reference to a handler service which can be used to interact with the given extension definition
     *
     * @throws IllegalStateException if the handler cannot be found
     */
    protected WorkflowRuleAttributeHandlerService getHandler(ExtensionDefinition extensionDefinition) {
        WorkflowRuleAttributeHandlerService handler =
                KewFrameworkServiceLocator.getWorkflowRuleAttributeHandlerService(extensionDefinition.getApplicationId());
        if (handler == null) {
            throw new IllegalStateException("Failed to locate a WorkflowRuleAttributeHandlerService for the given ExtensionDefinition: " + extensionDefinition);
        }
        return handler;
    }


}
