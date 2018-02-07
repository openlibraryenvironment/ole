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
package org.kuali.rice.kew.mail.service.impl;

import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.coreservice.framework.CoreFrameworkServiceLocator;
import org.kuali.rice.kew.actionrequest.ActionRequestValue;
import org.kuali.rice.kew.api.action.ActionItem;
import org.kuali.rice.kew.api.action.ActionRequest;
import org.kuali.rice.kew.api.document.Document;
import org.kuali.rice.kew.api.exception.WorkflowException;
import org.kuali.rice.kew.doctype.bo.DocumentType;
import org.kuali.rice.kew.mail.CustomEmailAttribute;
import org.kuali.rice.kew.mail.service.EmailContentService;
import org.kuali.rice.kew.routeheader.DocumentRouteHeaderValue;
import org.kuali.rice.kew.service.KEWServiceLocator;
import org.kuali.rice.kew.api.KewApiConstants;
import org.kuali.rice.kim.api.identity.Person;
import org.kuali.rice.krad.util.KRADConstants;

/**
 * Base EmailContentService implementation with a default email from address that can be
 * configured via Spring property injection
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public abstract class BaseEmailContentServiceImpl implements EmailContentService {
    protected String defaultEmailFromAddress = "admin@localhost";
    protected String deploymentEnvironment;

    public void setDefaultEmailFromAddress(String defaultEmailFromAddress) {
        this.defaultEmailFromAddress = defaultEmailFromAddress;
    }

    public String getApplicationEmailAddress() {
        // first check the configured value
        String fromAddress = CoreFrameworkServiceLocator.getParameterService().getParameterValueAsString(KewApiConstants.KEW_NAMESPACE, KRADConstants.DetailTypes.MAILER_DETAIL_TYPE, KewApiConstants.EMAIL_REMINDER_FROM_ADDRESS);
        // if there's no value configured, use the default
        if (org.apache.commons.lang.StringUtils.isEmpty(fromAddress)) {
            fromAddress = defaultEmailFromAddress;
        }
        return fromAddress;
    }

    public String getDocumentTypeEmailAddress(DocumentType documentType) {
        String fromAddress = (documentType == null ? null : documentType.getNotificationFromAddress());
        if (org.apache.commons.lang.StringUtils.isEmpty(fromAddress)) {
            fromAddress = getApplicationEmailAddress();
        }
        return fromAddress;
    }

    public String getDeploymentEnvironment() {
        return deploymentEnvironment;
    }

    public void setDeploymentEnvironment(String deploymentEnvironment) {
        this.deploymentEnvironment = deploymentEnvironment;
    }

    protected static CustomEmailAttribute getCustomEmailAttribute(Person user, ActionItem actionItem) throws WorkflowException {
    	DocumentRouteHeaderValue routeHeader = KEWServiceLocator.getRouteHeaderService().getRouteHeader(actionItem.getDocumentId());
        CustomEmailAttribute customEmailAttribute = routeHeader.getCustomEmailAttribute();
        if (customEmailAttribute != null) {
            Document routeHeaderVO = DocumentRouteHeaderValue.to(routeHeader);
            ActionRequestValue actionRequest = KEWServiceLocator.getActionRequestService().findByActionRequestId(actionItem.getActionRequestId());
            ActionRequest actionRequestVO = ActionRequestValue.to(actionRequest);
            customEmailAttribute.setRouteHeaderVO(routeHeaderVO);
            customEmailAttribute.setActionRequestVO(actionRequestVO);
        }
        return customEmailAttribute;
    }

    protected String getActionListUrl() {
        return ConfigContext.getCurrentContextConfig().getProperty(KRADConstants.WORKFLOW_URL_KEY) + "/" + "ActionList.do";
    }

    protected String getPreferencesUrl() {
        return ConfigContext.getCurrentContextConfig().getProperty(KRADConstants.WORKFLOW_URL_KEY) + "/" + "Preferences.do";
    }
}
