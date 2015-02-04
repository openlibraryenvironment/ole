/*
 * Copyright 2011 The Kuali Foundation.
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
package org.kuali.ole.select.document.validation.event;

import org.kuali.ole.sys.document.validation.event.AttributedDocumentEventBase;
import org.kuali.rice.krad.document.Document;
import org.kuali.rice.krad.rules.rule.BusinessRule;
import org.kuali.rice.krad.rules.rule.event.KualiDocumentEvent;

public class OleAccountFilterEvent extends AttributedDocumentEventBase implements KualiDocumentEvent {

    private String chartOfAccountsCode;
    private String accountNumber;
    private String objectCode;

    public String getChartOfAccountsCode() {
        return chartOfAccountsCode;
    }


    public void setChartOfAccountsCode(String chartOfAccountsCode) {
        this.chartOfAccountsCode = chartOfAccountsCode;
    }


    public String getAccountNumber() {
        return accountNumber;
    }


    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }


    public String getObjectCode() {
        return objectCode;
    }


    public void setObjectCode(String objectCode) {
        this.objectCode = objectCode;
    }


    /**
     * Constructs an OleAccountFilterEvent with the given errorPathPrefix, document.
     *
     * @param errorPathPrefix
     * @param document
     */
    public OleAccountFilterEvent(String errorPathPrefix, Document document) {
        super("Accounting Details of" + getDocumentId(document), errorPathPrefix, document);
    }


    /**
     * Constructs an OleAccountFilterEvent with the given document, chartOfAccountsCode, accountNumber, objectCode.
     *
     * @param document
     * @param chatrtOfAccountsCode
     * @param accountNumber
     * @param objectCode
     */
    public OleAccountFilterEvent(Document document, String chartOfAccountsCode,
                                 String accountNumber, String objectCode) {
        this("", document);
        this.accountNumber = accountNumber;
        this.chartOfAccountsCode = chartOfAccountsCode;
        this.objectCode = objectCode;

    }

    /**
     * Overridden to call parent and then clean up the error messages.
     *
     * @see org.kuali.ole.sys.document.validation.event.AttributedDocumentEventBase#invokeRuleMethod(org.kuali.rice.krad.rule.BusinessRule)
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
