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

import org.kuali.rice.krad.bo.AdHocRouteWorkgroup;
import org.kuali.rice.krad.document.Document;
import org.kuali.rice.krad.rules.rule.AddAdHocRouteWorkgroupRule;
import org.kuali.rice.krad.rules.rule.BusinessRule;

/**
 * This class represents the add AdHocRouteWorkgroup event that is part of an eDoc in Kuali. This is triggered when a user presses
 * the add button for a given adHocRouteWorkgroup.
 *
 *
 */
public final class AddAdHocRouteWorkgroupEvent extends KualiDocumentEventBase {
    private AdHocRouteWorkgroup adHocRouteWorkgroup;

    /**
     * Constructs an AddAdHocRouteWorkgroupEvent with the specified errorPathPrefix, document, and adHocRouteWorkgroup
     *
     * @param document
     * @param adHocRouteWorkgroup
     * @param errorPathPrefix
     */
    public AddAdHocRouteWorkgroupEvent(String errorPathPrefix, Document document, AdHocRouteWorkgroup adHocRouteWorkgroup) {
        super("creating add ad hoc route workgroup event for document " + KualiDocumentEventBase.getDocumentId(document), errorPathPrefix, document);
        this.adHocRouteWorkgroup = adHocRouteWorkgroup;
    }

    /**
     * Constructs an AddAdHocRouteWorkgroupEvent with the given document
     *
     * @param document
     * @param adHocRouteWorkgroup
     */
    public AddAdHocRouteWorkgroupEvent(Document document, AdHocRouteWorkgroup adHocRouteWorkgroup) {
        this("", document, adHocRouteWorkgroup);
    }

    /**
     * This method retrieves the document adHocRouteWorkgroup associated with this event.
     *
     * @return AdHocRouteWorkgroup
     */
    public AdHocRouteWorkgroup getAdHocRouteWorkgroup() {
        return adHocRouteWorkgroup;
    }

    /**
     * @see org.kuali.rice.krad.rules.rule.event.KualiDocumentEvent#validate()
     */
    @Override
    public void validate() {
        super.validate();
        if (this.adHocRouteWorkgroup == null) {
            throw new IllegalArgumentException("invalid (null) document adHocRouteWorkgroup");
        }
    }

    /**
     * @see org.kuali.rice.krad.rules.rule.event.KualiDocumentEvent#getRuleInterfaceClass()
     */
    public Class<? extends BusinessRule> getRuleInterfaceClass() {
        return AddAdHocRouteWorkgroupRule.class;
    }

    /**
     * @see org.kuali.rice.krad.rules.rule.event.KualiDocumentEvent#invokeRuleMethod(org.kuali.rice.krad.rules.rule.BusinessRule)
     */
    public boolean invokeRuleMethod(BusinessRule rule) {
        return ((AddAdHocRouteWorkgroupRule) rule).processAddAdHocRouteWorkgroup(getDocument(), this.adHocRouteWorkgroup);
    }
}
