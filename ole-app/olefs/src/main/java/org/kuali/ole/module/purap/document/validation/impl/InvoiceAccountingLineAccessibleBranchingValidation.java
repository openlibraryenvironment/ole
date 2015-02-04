/*
 * Copyright 2008-2009 The Kuali Foundation
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
package org.kuali.ole.module.purap.document.validation.impl;

import org.apache.commons.lang.StringUtils;
import org.kuali.ole.module.purap.PurapConstants.InvoiceStatuses;
import org.kuali.ole.module.purap.document.InvoiceDocument;
import org.kuali.ole.sys.document.validation.BranchingValidation;
import org.kuali.ole.sys.document.validation.event.AttributedDocumentEvent;

public class InvoiceAccountingLineAccessibleBranchingValidation extends BranchingValidation {

    protected static final String USE_DEFAULT_ACCOUNTING_LINE_ACCESSIBLE = "useDefaultAccountingLineAccessible";

    @Override
    protected String determineBranch(AttributedDocumentEvent event) {
        String status = ((InvoiceDocument) event.getDocument()).getApplicationDocumentStatus();
        if (StringUtils.equals(InvoiceStatuses.APPDOC_AWAITING_ACCOUNTS_PAYABLE_REVIEW, status)) {
            return null;
        } else if (StringUtils.equals(InvoiceStatuses.APPDOC_AWAITING_PAYMENT_REVIEW, status)) {
            return null;
        } else if (StringUtils.equals(InvoiceStatuses.APPDOC_AWAITING_FISCAL_REVIEW, status)) {
            return null;
        } else {
            return USE_DEFAULT_ACCOUNTING_LINE_ACCESSIBLE;
        }
    }

}
