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
import org.kuali.rice.krad.rules.rule.DocumentAuditRule;

/**
 * Event class for document audit
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class DocumentAuditEvent extends KualiDocumentEventBase {

    /**
     * Constructs a RunAuditEvent with the given errorPathPrefix and document.
     *
     * @param errorPathPrefix
     * @param document
     * @param accountingLine
     */
    public DocumentAuditEvent(String errorPathPrefix, Document document) {
        super("Running audit on " + KualiDocumentEventBase.getDocumentId(document), errorPathPrefix, document);
    }

    /**
     * Constructs a RunAuditEvent with the given document.
     *
     * @param errorPathPrefix
     * @param document
     * @param accountingLine
     */
    public DocumentAuditEvent(Document document) {
        this("", document);
    }

    /**
     * @see org.kuali.rice.krad.rules.rule.event.KualiDocumentEvent#getRuleInterfaceClass()
     */
    public Class<? extends BusinessRule> getRuleInterfaceClass() {
        return DocumentAuditRule.class;
    }

    /**
     * @see org.kuali.rice.krad.rules.rule.event.KualiDocumentEvent#invokeRuleMethod(org.kuali.rice.krad.rules.rule.BusinessRule)
     */
    public boolean invokeRuleMethod(BusinessRule rule) {
        return ((DocumentAuditRule) rule).processRunAuditBusinessRules(getDocument());
    }
}

