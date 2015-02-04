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

import org.kuali.ole.fp.businessobject.DisbursementVoucherPayeeDetail;
import org.kuali.ole.fp.document.DisbursementVoucherConstants;
import org.kuali.ole.fp.document.DisbursementVoucherDocument;
import org.kuali.ole.sys.OLEConstants;
import org.kuali.ole.sys.OLEPropertyConstants;
import org.kuali.ole.sys.businessobject.ChartOrgHolder;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.ole.sys.document.AccountingDocument;
import org.kuali.ole.sys.document.validation.GenericValidation;
import org.kuali.ole.sys.document.validation.event.AttributedDocumentEvent;
import org.kuali.ole.sys.service.FinancialSystemUserService;
import org.kuali.rice.core.api.parameter.ParameterEvaluator;
import org.kuali.rice.core.api.parameter.ParameterEvaluatorService;
import org.kuali.rice.coreservice.framework.parameter.ParameterService;
import org.kuali.rice.kim.api.identity.Person;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.util.MessageMap;
import org.kuali.rice.krad.util.ObjectUtils;

public class DisbursementVoucherDocumentLocationValidation extends GenericValidation implements DisbursementVoucherConstants {
    private static org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(DisbursementVoucherDocumentLocationValidation.class);

    private ParameterService parameterService;
    private AccountingDocument accountingDocumentForValidation;

    /**
     * @see org.kuali.ole.sys.document.validation.Validation#validate(org.kuali.ole.sys.document.validation.event.AttributedDocumentEvent)
     */
    @Override
    public boolean validate(AttributedDocumentEvent event) {
        LOG.debug("validate start");
        boolean isValid = true;

        DisbursementVoucherDocument document = (DisbursementVoucherDocument) accountingDocumentForValidation;
        DisbursementVoucherPayeeDetail payeeDetail = document.getDvPayeeDetail();
        String documentationLocationCode = document.getDisbursementVoucherDocumentationLocationCode();

        MessageMap errors = GlobalVariables.getMessageMap();
        int initialErrorCount = errors.getErrorCount();
        errors.addToErrorPath(OLEPropertyConstants.DOCUMENT);

        // payment reason restrictions
        if (ObjectUtils.isNotNull(payeeDetail.getDisbVchrPaymentReasonCode())) {
            ParameterEvaluator parameterEvaluator = /*REFACTORME*/SpringContext.getBean(ParameterEvaluatorService.class).getParameterEvaluator(DisbursementVoucherDocument.class, DisbursementVoucherConstants.VALID_DOC_LOC_BY_PAYMENT_REASON_PARM, DisbursementVoucherConstants.INVALID_DOC_LOC_BY_PAYMENT_REASON_PARM, payeeDetail.getDisbVchrPaymentReasonCode(), documentationLocationCode);
            parameterEvaluator.evaluateAndAddError(document.getClass(), OLEPropertyConstants.DISBURSEMENT_VOUCHER_DOCUMENTATION_LOCATION_CODE);
        }

      //Commented for the jira issue OLE-3415
        // alien indicator restrictions
        /*if (payeeDetail.isDisbVchrAlienPaymentCode()) {
            ParameterEvaluator parameterEvaluator = SpringContext.getBean(ParameterEvaluatorService.class).getParameterEvaluator(DisbursementVoucherDocument.class, ALIEN_INDICATOR_CHECKED_PARM_NM, documentationLocationCode);
            parameterEvaluator.evaluateAndAddError(document.getClass(), OLEPropertyConstants.DISBURSEMENT_VOUCHER_DOCUMENTATION_LOCATION_CODE);
        }*/

        Person initiator = getInitiator(document);
        ChartOrgHolder chartOrg = SpringContext.getBean(FinancialSystemUserService.class).getPrimaryOrganization(initiator, OLEConstants.ParameterNamespaces.FINANCIAL);
        String locationCode = (chartOrg == null || chartOrg.getOrganization() == null) ? null : chartOrg.getOrganization().getOrganizationPhysicalCampusCode();

        // initiator campus code restrictions
        ParameterEvaluator parameterEvaluator = /*REFACTORME*/SpringContext.getBean(ParameterEvaluatorService.class).getParameterEvaluator(DisbursementVoucherDocument.class, DisbursementVoucherConstants.VALID_DOC_LOC_BY_CAMPUS_PARM, DisbursementVoucherConstants.INVALID_DOC_LOC_BY_CAMPUS_PARM, locationCode, documentationLocationCode);
        parameterEvaluator.evaluateAndAddError(document.getClass(), OLEPropertyConstants.DISBURSEMENT_VOUCHER_DOCUMENTATION_LOCATION_CODE);

        errors.removeFromErrorPath(OLEPropertyConstants.DOCUMENT);

        isValid = initialErrorCount == errors.getErrorCount();
        return isValid;
    }

    /**
     * Returns the initiator of the document as a KualiUser
     *
     * @param document submitted document
     * @return <code>KualiUser</code>
     */
    protected Person getInitiator(AccountingDocument document) {
        Person initUser = KimApiServiceLocator.getPersonService().getPerson(document.getDocumentHeader().getWorkflowDocument().getInitiatorPrincipalId());
        if (initUser == null) {
            throw new RuntimeException("Document Initiator not found ");
        }

        return initUser;
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

