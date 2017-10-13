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
import org.kuali.rice.krad.rules.rule.BusinessRule;
import org.kuali.rice.krad.rules.rule.CompleteDocumentRule;

import java.util.ArrayList;
import java.util.List;

/**
 * Complete document event
 *
 * @author Kuali Rice Team (kuali-rice@googlegroups.com)
 */
public class CompleteDocumentEvent extends KualiDocumentEventBase {

    /**
     * Constructs a RouteDocumentEvent with the specified errorPathPrefix and document
     *
     * @param errorPathPrefix
     * @param document
     */
    public CompleteDocumentEvent(String errorPathPrefix, Document document) {
        super("creating complete event for document " + getDocumentId(document), errorPathPrefix, document);
    }

    /**
     * Constructs a RouteDocumentEvent with the given document
     *
     * @param document
     */
    public CompleteDocumentEvent(Document document) {
        this("", document);
    }

    /**
     * @see KualiDocumentEvent#getRuleInterfaceClass()
     */
    public Class getRuleInterfaceClass() {
        return CompleteDocumentRule.class;
    }

    /**
     * @see KualiDocumentEvent#invokeRuleMethod(org.kuali.rice.krad.rules.rule.BusinessRule)
     */
    public boolean invokeRuleMethod(BusinessRule rule) {
        return ((CompleteDocumentRule) rule).processCompleteDocument(document);
    }

    /**
     * @see KualiDocumentEvent#generateEvents()
     */
    public List generateEvents() {
        List events = new ArrayList();
        events.add(new RouteDocumentEvent(getDocument()));
        return events;
    }
}
