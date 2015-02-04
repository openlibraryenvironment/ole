/*
 * Copyright 2008 The Kuali Foundation
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
package org.kuali.ole.fp.document.validation.impl;

import org.kuali.ole.fp.document.DisbursementVoucherDocument;
import org.kuali.ole.sys.OLEConstants;
import org.kuali.ole.sys.OLEKeyConstants;
import org.kuali.ole.sys.OLEPropertyConstants;
import org.kuali.ole.sys.document.AccountingDocument;
import org.kuali.ole.sys.document.validation.GenericValidation;
import org.kuali.ole.sys.document.validation.event.AttributedDocumentEvent;
import org.kuali.rice.core.api.util.type.KualiDecimal;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.util.MessageMap;

public class DisbursementVoucherDocumentAmountValidation extends GenericValidation {
    private static org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(DisbursementVoucherDocumentAmountValidation.class);

    private AccountingDocument accountingDocumentForValidation;

    /**
     * @see org.kuali.ole.sys.document.validation.Validation#validate(org.kuali.ole.sys.document.validation.event.AttributedDocumentEvent)
     */
    public boolean validate(AttributedDocumentEvent event) {
        LOG.debug("validate start");
        
        boolean isValid = true;
        DisbursementVoucherDocument disbursementVoucherDocument = (DisbursementVoucherDocument) accountingDocumentForValidation;
        
        MessageMap errors = GlobalVariables.getMessageMap();
        errors.addToErrorPath(OLEPropertyConstants.DOCUMENT);

        /* check total cannot be negative or zero */
        if (!disbursementVoucherDocument.getDisbVchrCheckTotalAmount().isPositive()) {
            errors.putError(OLEPropertyConstants.DISB_VCHR_CHECK_TOTAL_AMOUNT, OLEKeyConstants.ERROR_NEGATIVE_OR_ZERO_CHECK_TOTAL);
            isValid = false;
        }

        /* total accounting lines cannot be negative */
        if (KualiDecimal.ZERO.compareTo(disbursementVoucherDocument.getSourceTotal()) == 1) {
            errors.putErrorWithoutFullErrorPath(OLEConstants.ACCOUNTING_LINE_ERRORS, OLEKeyConstants.ERROR_NEGATIVE_ACCOUNTING_TOTAL);
            isValid = false;
        }

        /* total of accounting lines must match check total */
        if (disbursementVoucherDocument.getDisbVchrCheckTotalAmount().compareTo(disbursementVoucherDocument.getSourceTotal()) != 0) {
            errors.putErrorWithoutFullErrorPath(OLEConstants.ACCOUNTING_LINE_ERRORS, OLEKeyConstants.ERROR_CHECK_ACCOUNTING_TOTAL);
            isValid = false;
        }
        
        errors.removeFromErrorPath(OLEPropertyConstants.DOCUMENT);

        return isValid;
    }

    /**
     * Sets the accountingDocumentForValidation attribute value.
     * 
     * @param accountingDocumentForValidation The accountingDocumentForValidation to set.
     */
    public void setAccountingDocumentForValidation(AccountingDocument accountingDocumentForValidation) {
        this.accountingDocumentForValidation = accountingDocumentForValidation;
    }

    /**
     * Gets the accountingDocumentForValidation attribute. 
     * @return Returns the accountingDocumentForValidation.
     */
    public AccountingDocument getAccountingDocumentForValidation() {
        return accountingDocumentForValidation;
    }

}
