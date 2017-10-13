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
package org.kuali.rice.krad.rules.rule.event;

import org.kuali.rice.krad.document.Document;
import org.kuali.rice.krad.rules.rule.BusinessRule;
import org.kuali.rice.krad.rules.rule.SendAdHocRequestsRule;
import org.kuali.rice.krad.service.KRADServiceLocatorWeb;
import org.kuali.rice.krad.service.KualiRuleService;

import java.util.ArrayList;
import java.util.List;

/**
 * This is a description of what this class does - wliang don't forget to fill this in.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class SendAdHocRequestsEvent extends KualiDocumentEventBase {

    public SendAdHocRequestsEvent(String errorPathPrefix, Document document) {
        this("creating send adhoc requests event for document " + getDocumentId(document), errorPathPrefix, document);
    }

    /**
     * Constructs a SaveDocumentEvent with the given document
     *
     * @param document
     */
    public SendAdHocRequestsEvent(Document document) {
        this("", document);
    }

    public SendAdHocRequestsEvent(String description, String errorPathPrefix, Document document) {
    	super(description, errorPathPrefix, document);
    }

	/**
	 * This overridden method ...
	 *
	 * @see org.kuali.rice.krad.rules.rule.event.KualiDocumentEvent#getRuleInterfaceClass()
	 */
	public Class<? extends BusinessRule> getRuleInterfaceClass() {
		return SendAdHocRequestsRule.class;
	}

	/**
	 * @see org.kuali.rice.krad.rules.rule.event.KualiDocumentEvent#invokeRuleMethod(org.kuali.rice.krad.rules.rule.BusinessRule)
	 */
	public boolean invokeRuleMethod(BusinessRule rule) {
		return ((SendAdHocRequestsRule) rule).processSendAdHocRequests(document);
	}

	/**
	 * @see org.kuali.rice.krad.rules.rule.event.KualiDocumentEventBase#generateEvents()
	 */
	@Override
	public List<KualiDocumentEvent> generateEvents() {
		KualiRuleService ruleService = KRADServiceLocatorWeb.getKualiRuleService();

		List<KualiDocumentEvent> events = new ArrayList<KualiDocumentEvent>();
        events.addAll(ruleService.generateAdHocRoutePersonEvents(getDocument()));
        events.addAll(ruleService.generateAdHocRouteWorkgroupEvents(getDocument()));
        return events;
	}
}
