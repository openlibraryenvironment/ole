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

import org.apache.commons.lang.StringUtils;
import org.kuali.ole.fp.document.DisbursementVoucherConstants;
import org.kuali.ole.fp.document.DisbursementVoucherDocument;
import org.kuali.ole.sys.OLEKeyConstants;
import org.kuali.ole.sys.OLEPropertyConstants;
import org.kuali.ole.sys.document.AccountingDocument;
import org.kuali.ole.sys.document.validation.GenericValidation;
import org.kuali.ole.sys.document.validation.event.AttributedDocumentEvent;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.util.MessageMap;

public class DisbursementVoucherForeignDraftValidation extends GenericValidation implements DisbursementVoucherConstants {
    private static org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(DisbursementVoucherEmployeeInformationValidation.class);

    private AccountingDocument accountingDocumentForValidation;

    /**
     * @see org.kuali.ole.sys.document.validation.Validation#validate(org.kuali.ole.sys.document.validation.event.AttributedDocumentEvent)
     */
    public boolean validate(AttributedDocumentEvent event) {
        LOG.debug("validate start");
        boolean isValid = true;
        
        DisbursementVoucherDocument document = (DisbursementVoucherDocument) accountingDocumentForValidation;
        if (!PAYMENT_METHOD_DRAFT.equals(document.getDisbVchrPaymentMethodCode())) {
            return true;
        }

        MessageMap errors = GlobalVariables.getMessageMap();
        errors.addToErrorPath(OLEPropertyConstants.DOCUMENT);
        errors.addToErrorPath(OLEPropertyConstants.DV_WIRE_TRANSFER);

        /* currency type code required */
        if (StringUtils.isBlank(document.getDvWireTransfer().getDisbursementVoucherForeignCurrencyTypeCode())) {
            errors.putError(OLEPropertyConstants.DISB_VCHR_FD_CURRENCY_TYPE_CODE, OLEKeyConstants.ERROR_DV_CURRENCY_TYPE_CODE);
            isValid = false;
        }

        /* currency type name required */
        if (StringUtils.isBlank(document.getDvWireTransfer().getDisbursementVoucherForeignCurrencyTypeName())) {
            errors.putError(OLEPropertyConstants.DISB_VCHR_FD_CURRENCY_TYPE_NAME, OLEKeyConstants.ERROR_DV_CURRENCY_TYPE_NAME);
            isValid = false;
        }

        errors.removeFromErrorPath(OLEPropertyConstants.DV_WIRE_TRANSFER);
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
