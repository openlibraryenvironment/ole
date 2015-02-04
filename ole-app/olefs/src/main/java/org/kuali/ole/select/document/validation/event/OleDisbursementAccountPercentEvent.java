/*
 * Copyright 2013 The Kuali Foundation.
 *
 * Licensed under the Educational Community License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.opensource.org/licenses/ecl1.php
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kuali.ole.select.document.validation.event;

import org.kuali.ole.select.businessobject.OleDisbursementVoucherAccountingLine;
import org.kuali.ole.sys.document.validation.event.AttributedDocumentEventBase;
import org.kuali.rice.krad.document.Document;
import org.kuali.rice.krad.rules.rule.BusinessRule;
import org.kuali.rice.krad.rules.rule.event.KualiDocumentEvent;

import java.util.List;

public class OleDisbursementAccountPercentEvent extends AttributedDocumentEventBase implements KualiDocumentEvent {
    private final List<OleDisbursementVoucherAccountingLine> accountingDocument;

    /**
     * Constructs an AddAccountingLineEvent with the given errorPathPrefix, document, and accountingLine.
     *
     * @param errorPathPrefix
     * @param document
     * @param accountingLine
     */
    public OleDisbursementAccountPercentEvent(String errorPathPrefix, Document document, List<OleDisbursementVoucherAccountingLine> accountingDocument) {
        super("Accounting Details of" + getDocumentId(document), errorPathPrefix, document);
        this.accountingDocument = accountingDocument;
    }

    /**
     * @see org.kuali.rice.kns.rule.event.AccountingLineEvent#getAccountingLine()
     */
    public List<OleDisbursementVoucherAccountingLine> getAccountingDocument() {
        return accountingDocument;
    }

    /**
     * Overridden to call parent and then clean up the error messages.
     *
     * @see org.kuali.ole.sys.document.validation.event.AttributedDocumentEventBase#invokeRuleMethod(org.kuali.rice.kns.rule.BusinessRule)
     */
    @Override
    public boolean invokeRuleMethod(BusinessRule rule) {
        boolean result = super.invokeRuleMethod(rule);
        cleanErrorMessages();
        return result;
    }

    /**
     * Logic to replace generic amount error messages, especially those where extraordinarily large amounts caused format errors
     */
    public void cleanErrorMessages() {
    }

}
