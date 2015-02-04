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

import static org.kuali.ole.sys.OLEConstants.DOCUMENT_ERRORS;
import static org.kuali.ole.sys.OLEKeyConstants.ERROR_DOCUMENT_ACCOUNTING_PERIOD_CLOSED;

import org.kuali.ole.coa.businessobject.AccountingPeriod;
import org.kuali.ole.coa.service.AccountingPeriodService;
import org.kuali.ole.fp.document.AuxiliaryVoucherDocument;
import org.kuali.ole.sys.document.validation.GenericValidation;
import org.kuali.ole.sys.document.validation.event.AttributedDocumentEvent;
import org.kuali.rice.krad.util.GlobalVariables;

/**
 * Validates that the accounting period given by the document is currently open
 */
public class AuxiliaryVoucherAccountingPeriodOpenValidation extends GenericValidation {
    private AuxiliaryVoucherDocument auxliaryVoucherDocumentForValidation;
    private AccountingPeriodService accountingPeriodService;

    /**
     * Uses the accounting period service to get the accounting period for the document and checks that it's open
     * @see org.kuali.ole.sys.document.validation.Validation#validate(org.kuali.ole.sys.document.validation.event.AttributedDocumentEvent)
     */
    public boolean validate(AttributedDocumentEvent event) {
        AccountingPeriod acctPeriod = getAccountingPeriodService().getByPeriod(auxliaryVoucherDocumentForValidation.getPostingPeriodCode(), auxliaryVoucherDocumentForValidation.getPostingYear());
        
        //  can't post into a closed period
        if (acctPeriod == null || acctPeriod.isActive()) {
            GlobalVariables.getMessageMap().putError(DOCUMENT_ERRORS, ERROR_DOCUMENT_ACCOUNTING_PERIOD_CLOSED);
            return false;
        }
        
        return true;
    }

    /**
     * Gets the accountingPeriodService attribute. 
     * @return Returns the accountingPeriodService.
     */
    public AccountingPeriodService getAccountingPeriodService() {
        return accountingPeriodService;
    }

    /**
     * Sets the accountingPeriodService attribute value.
     * @param accountingPeriodService The accountingPeriodService to set.
     */
    public void setAccountingPeriodService(AccountingPeriodService accountingPeriodService) {
        this.accountingPeriodService = accountingPeriodService;
    }

    /**
     * Gets the auxliaryVoucherDocumentForValidation attribute. 
     * @return Returns the auxliaryVoucherDocumentForValidation.
     */
    public AuxiliaryVoucherDocument getAuxliaryVoucherDocumentForValidation() {
        return auxliaryVoucherDocumentForValidation;
    }

    /**
     * Sets the auxliaryVoucherDocumentForValidation attribute value.
     * @param auxliaryVoucherDocumentForValidation The auxliaryVoucherDocumentForValidation to set.
     */
    public void setAuxliaryVoucherDocumentForValidation(AuxiliaryVoucherDocument auxliaryVoucherDocumentForValidation) {
        this.auxliaryVoucherDocumentForValidation = auxliaryVoucherDocumentForValidation;
    }
}
