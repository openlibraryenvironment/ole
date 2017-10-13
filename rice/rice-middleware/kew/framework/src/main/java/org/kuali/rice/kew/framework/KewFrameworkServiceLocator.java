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
package org.kuali.rice.kew.framework;

import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.rice.kew.api.KewApiConstants;
import org.kuali.rice.kew.framework.actionlist.ActionListCustomizationHandlerService;
import org.kuali.rice.kew.framework.actionlist.ActionListCustomizationMediator;
import org.kuali.rice.kew.framework.document.search.DocumentSearchCustomizationHandlerService;
import org.kuali.rice.kew.framework.document.security.DocumentSecurityHandlerService;
import org.kuali.rice.kew.framework.rule.attribute.WorkflowRuleAttributeHandlerService;
import org.kuali.rice.kew.framework.validation.RuleValidationAttributeExporterService;
import org.kuali.rice.ksb.api.KsbApiServiceLocator;

import javax.xml.namespace.QName;

/**
 * A static service locator which aids in locating the various KEW framework services.
 */
public class KewFrameworkServiceLocator {

    public static final String DOCUMENT_SEARCH_CUSTOMIZATION_HANDLER_SERVICE = "documentSearchCustomizationHandlerService";
    public static final String DOCUMENT_SECURITY_HANDLER_SERVICE = "documentSecurityHandlerService";
    public static final String RULE_VALIDATION_ATTRIBUTE_EXPORTER_SERVICE = "ruleValidationAttributeExporterService";
    public static final String ACTION_LIST_CUSTOMIZATION_MEDIATOR = "rice.kew.actionListCustomizationMediator";
    public static final String ACTION_LIST_CUSTOMIZATION_HANDLER_SERVICE = "actionListCustomizationHandlerService";
    public static final String WORKFLOW_RULE_ATTRIBUTE_HANDLER_SERVICE = "workflowRuleAttributeHandlerService";


    static <T> T getService(String serviceName) {
        return GlobalResourceLoader.<T>getService(serviceName);
    }

    static <T> T getServiceOnBus(String serviceName, String applicationId) {
        return (T)KsbApiServiceLocator.getServiceBus().getService(new QName(KewApiConstants.Namespaces.KEW_NAMESPACE_2_0, serviceName), applicationId);
    }

    public static DocumentSearchCustomizationHandlerService getDocumentSearchCustomizationHandlerService(
            String applicationId) {
        return getServiceOnBus(DOCUMENT_SEARCH_CUSTOMIZATION_HANDLER_SERVICE, applicationId);
    }

    public static DocumentSecurityHandlerService getDocumentSecurityHandlerService(
            String applicationId) {
        return getServiceOnBus(DOCUMENT_SECURITY_HANDLER_SERVICE, applicationId);
    }

    public static RuleValidationAttributeExporterService getRuleValidationAttributeExporterService(String applicationId) {
        return getServiceOnBus(RULE_VALIDATION_ATTRIBUTE_EXPORTER_SERVICE, applicationId);
    }

    public static WorkflowRuleAttributeHandlerService getWorkflowRuleAttributeHandlerService(String applicationId) {
        return getServiceOnBus(WORKFLOW_RULE_ATTRIBUTE_HANDLER_SERVICE, applicationId);
    }

    public static ActionListCustomizationMediator getActionListCustomizationMediator() {
        return getService(ACTION_LIST_CUSTOMIZATION_MEDIATOR);
    }
    
    public static ActionListCustomizationHandlerService getActionListCustomizationHandlerService(
            String applicationId) {
        return getServiceOnBus(ACTION_LIST_CUSTOMIZATION_HANDLER_SERVICE, applicationId);
    }
}