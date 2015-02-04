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

import org.kuali.ole.fp.businessobject.DisbursementVoucherPreConferenceDetail;
import org.kuali.ole.fp.document.DisbursementVoucherConstants;
import org.kuali.ole.fp.document.DisbursementVoucherDocument;
import org.kuali.ole.fp.document.service.DisbursementVoucherTaxService;
import org.kuali.ole.sys.OLEConstants;
import org.kuali.ole.sys.OLEKeyConstants;
import org.kuali.ole.sys.OLEPropertyConstants;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.ole.sys.document.AccountingDocument;
import org.kuali.ole.sys.document.validation.GenericValidation;
import org.kuali.ole.sys.document.validation.event.AttributedDocumentEvent;
import org.kuali.rice.core.api.parameter.ParameterEvaluator;
import org.kuali.rice.core.api.parameter.ParameterEvaluatorService;
import org.kuali.rice.core.api.util.type.KualiDecimal;
import org.kuali.rice.coreservice.framework.parameter.ParameterService;
import org.kuali.rice.kns.service.DictionaryValidationService;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.util.MessageMap;

public class DisbursementVoucherPrePaidTravelValidation extends GenericValidation {
    private static org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(DisbursementVoucherPrePaidTravelValidation.class);

    private ParameterService parameterService;
    private AccountingDocument accountingDocumentForValidation;

    /**
     * @see org.kuali.ole.sys.document.validation.Validation#validate(org.kuali.ole.sys.document.validation.event.AttributedDocumentEvent)
     */
    public boolean validate(AttributedDocumentEvent event) {
        LOG.debug("validate start");
        boolean isValid = true;
        
        DisbursementVoucherDocument document = (DisbursementVoucherDocument) accountingDocumentForValidation;
        DisbursementVoucherPreConferenceDetail preConferenceDetail = document.getDvPreConferenceDetail();
        
        if (!isTravelPrepaidPaymentReason(document)) {
            return true;
        }
        
        MessageMap errors = GlobalVariables.getMessageMap();
        errors.addToErrorPath(OLEPropertyConstants.DOCUMENT);
        errors.addToErrorPath(OLEPropertyConstants.DV_PRE_CONFERENCE_DETAIL);
        
        SpringContext.getBean(DictionaryValidationService.class).validateBusinessObjectsRecursively(preConferenceDetail, 1);
        if (errors.hasErrors()) {
            errors.removeFromErrorPath(OLEPropertyConstants.DV_PRE_CONFERENCE_DETAIL);
            errors.addToErrorPath(OLEPropertyConstants.DOCUMENT);
            return false;
        }

        /* check conference end date is not before conference start date */
        if (preConferenceDetail.getDisbVchrConferenceEndDate().compareTo(preConferenceDetail.getDisbVchrConferenceStartDate()) < 0) {
            errors.putError(OLEPropertyConstants.DISB_VCHR_CONFERENCE_END_DATE, OLEKeyConstants.ERROR_DV_CONF_END_DATE);
            isValid = false;
        }

        /* total on prepaid travel must equal Check Total */
        /* if tax has been taken out, need to add back in the tax amount for the check */
        KualiDecimal paidAmount = document.getDisbVchrCheckTotalAmount();
        paidAmount = paidAmount.add(SpringContext.getBean(DisbursementVoucherTaxService.class).getNonResidentAlienTaxAmount(document));
        if (paidAmount.compareTo(preConferenceDetail.getDisbVchrConferenceTotalAmt()) != 0) {
            errors.putErrorWithoutFullErrorPath(OLEConstants.GENERAL_PREPAID_TAB_ERRORS, OLEKeyConstants.ERROR_DV_PREPAID_CHECK_TOTAL);
            isValid = false;
        }

        errors.removeFromErrorPath(OLEPropertyConstants.DV_PRE_CONFERENCE_DETAIL);
        errors.removeFromErrorPath(OLEPropertyConstants.DOCUMENT);

        return isValid;
    }
    
    /**
     * Returns whether the document's payment reason is for prepaid travel
     * 
     * @param disbursementVoucherDocument
     * @return true if payment reason is for pre-paid travel reason
     */
    protected boolean isTravelPrepaidPaymentReason(DisbursementVoucherDocument disbursementVoucherDocument) {
        ParameterEvaluator travelPrepaidPaymentReasonEvaluator = /*REFACTORME*/SpringContext.getBean(ParameterEvaluatorService.class).getParameterEvaluator(DisbursementVoucherDocument.class, DisbursementVoucherConstants.PREPAID_TRAVEL_PAYMENT_REASONS_PARM_NM, disbursementVoucherDocument.getDvPayeeDetail().getDisbVchrPaymentReasonCode());
        return travelPrepaidPaymentReasonEvaluator.evaluationSucceeds();
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
     * Sets the parameterService attribute value.
     * @param parameterService The parameterService to set.
     */
    public void setParameterService(ParameterService parameterService) {
        this.parameterService = parameterService;
    }

    /**
     * Gets the accountingDocumentForValidation attribute. 
     * @return Returns the accountingDocumentForValidation.
     */
    public AccountingDocument getAccountingDocumentForValidation() {
        return accountingDocumentForValidation;
    }
}
