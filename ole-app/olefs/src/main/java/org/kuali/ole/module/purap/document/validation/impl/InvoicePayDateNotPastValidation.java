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
package org.kuali.ole.module.purap.document.validation.impl;

import org.kuali.ole.module.purap.PurapKeyConstants;
import org.kuali.ole.module.purap.PurapPropertyConstants;
import org.kuali.ole.module.purap.document.InvoiceDocument;
import org.kuali.ole.module.purap.document.service.PurapService;
import org.kuali.ole.sys.OLEPropertyConstants;
import org.kuali.ole.sys.document.validation.GenericValidation;
import org.kuali.ole.sys.document.validation.event.AttributedDocumentEvent;
import org.kuali.rice.kew.api.WorkflowDocument;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.PersistenceService;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.util.ObjectUtils;

import java.util.Map;

public class InvoicePayDateNotPastValidation extends GenericValidation {

    private PurapService purapService;
    private PersistenceService persistenceService;
    private BusinessObjectService businessObjectService;

    /**
     * Validates that the payment request date does not occur in the past.
     */
    public boolean validate(AttributedDocumentEvent event) {
        boolean valid = true;
        InvoiceDocument document = (InvoiceDocument) event.getDocument();
        GlobalVariables.getMessageMap().clearErrorPath();
        GlobalVariables.getMessageMap().addToErrorPath(OLEPropertyConstants.DOCUMENT);

        java.sql.Date invoicePayDate = document.getInvoicePayDate();
        if (ObjectUtils.isNotNull(invoicePayDate) && purapService.isDateInPast(invoicePayDate)) {
            // the pay date is in the past, now we need to check whether given the state of the document to determine whether a past pay date is allowed
            WorkflowDocument workflowDocument = document.getDocumentHeader().getWorkflowDocument();
            if (workflowDocument.isInitiated() || workflowDocument.isSaved()) {
                // past pay dates are not allowed if the document has never been routed (i.e. in saved or initiated state)
                // (note that this block will be run when a document is being routed, or re-saved after being routed
                valid &= false;
                GlobalVariables.getMessageMap().putError(PurapPropertyConstants.PAYMENT_REQUEST_PAY_DATE, PurapKeyConstants.ERROR_INVALID_PAY_DATE);
            } else {
                // otherwise, this document has already been routed
                // it's an error if the pay date has been changed from the pay date in the database and the new pay date is in the past
                // retrieve doc from DB, and compare the dates
                InvoiceDocument invoiceDocumentFromDatabase = retrieveInvoiceDocumentFromDatabase(document);

                if (ObjectUtils.isNull(invoiceDocumentFromDatabase)) {
                    // this definitely should not happen
                    throw new NullPointerException("Unable to find payment request document " + document.getDocumentNumber() + " from database");
                }

                java.sql.Date invoicePayDateFromDatabase = invoiceDocumentFromDatabase.getInvoicePayDate();
                if (ObjectUtils.isNull(invoicePayDateFromDatabase) || !invoicePayDateFromDatabase.equals(invoicePayDate)) {
                    valid &= false;
                    GlobalVariables.getMessageMap().putError(PurapPropertyConstants.PAYMENT_REQUEST_PAY_DATE, PurapKeyConstants.ERROR_INVALID_PAY_DATE);
                }
            }
        }

        GlobalVariables.getMessageMap().clearErrorPath();

        return valid;
    }

    /**
     * Retrieves the payment request document from the database.  Note that the instance returned
     *
     * @param document the document to look in the database for
     * @return an instance representing what's stored in the database for this instance
     */
    protected InvoiceDocument retrieveInvoiceDocumentFromDatabase(InvoiceDocument document) {
        Map primaryKeyValues = persistenceService.getPrimaryKeyFieldValues(document);
        return (InvoiceDocument) businessObjectService.findByPrimaryKey(document.getClass(), primaryKeyValues);
    }

    public PurapService getPurapService() {
        return purapService;
    }

    public void setPurapService(PurapService purapService) {
        this.purapService = purapService;
    }

    public PersistenceService getPersistenceService() {
        return persistenceService;
    }

    public void setPersistenceService(PersistenceService persistenceService) {
        this.persistenceService = persistenceService;
    }

    public BusinessObjectService getBusinessObjectService() {
        return businessObjectService;
    }

    public void setBusinessObjectService(BusinessObjectService businessObjectService) {
        this.businessObjectService = businessObjectService;
    }

}
