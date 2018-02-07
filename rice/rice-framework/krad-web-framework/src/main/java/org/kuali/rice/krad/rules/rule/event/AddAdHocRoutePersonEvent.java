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

import org.kuali.rice.krad.bo.AdHocRoutePerson;
import org.kuali.rice.krad.document.Document;
import org.kuali.rice.krad.rules.rule.AddAdHocRoutePersonRule;
import org.kuali.rice.krad.rules.rule.BusinessRule;

/**
 * This class represents the add AdHocRoutePerson event that is part of an eDoc in Kuali. This is triggered when a user presses the
 * add button for a given adHocRoutePerson.
 *
 *
 */
public final class AddAdHocRoutePersonEvent extends KualiDocumentEventBase {
    private AdHocRoutePerson adHocRoutePerson;

    /**
     * Constructs an AddAdHocRoutePersonEvent with the specified errorPathPrefix, document, and adHocRoutePerson
     *
     * @param document
     * @param adHocRoutePerson
     * @param errorPathPrefix
     */
    public AddAdHocRoutePersonEvent(String errorPathPrefix, Document document, AdHocRoutePerson adHocRoutePerson) {
        super("creating add ad hoc route person event for document " + KualiDocumentEventBase.getDocumentId(document), errorPathPrefix, document);
        this.adHocRoutePerson = adHocRoutePerson;
    }

    /**
     * Constructs an AddAdHocRoutePersonEvent with the given document
     *
     * @param document
     * @param adHocRoutePerson
     */
    public AddAdHocRoutePersonEvent(Document document, AdHocRoutePerson adHocRoutePerson) {
        this("", document, adHocRoutePerson);
    }

    /**
     * This method retrieves the document adHocRoutePerson associated with this event.
     *
     * @return AdHocRoutePerson
     */
    public AdHocRoutePerson getAdHocRoutePerson() {
        return adHocRoutePerson;
    }

    /**
     * @see org.kuali.rice.krad.rules.rule.event.KualiDocumentEvent#validate()
     */
    @Override
    public void validate() {
        super.validate();
        if (this.adHocRoutePerson == null) {
            throw new IllegalArgumentException("invalid (null) document adHocRoutePerson");
        }
    }

    /**
     * @see org.kuali.rice.krad.rules.rule.event.KualiDocumentEvent#getRuleInterfaceClass()
     */
    public Class<? extends BusinessRule> getRuleInterfaceClass() {
        return AddAdHocRoutePersonRule.class;
    }

    /**
     * @see org.kuali.rice.krad.rules.rule.event.KualiDocumentEvent#invokeRuleMethod(org.kuali.rice.krad.rules.rule.BusinessRule)
     */
    public boolean invokeRuleMethod(BusinessRule rule) {
        return ((AddAdHocRoutePersonRule) rule).processAddAdHocRoutePerson(getDocument(), this.adHocRoutePerson);
    }
}
