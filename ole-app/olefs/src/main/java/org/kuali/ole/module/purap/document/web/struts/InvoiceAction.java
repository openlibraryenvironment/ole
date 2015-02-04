/*
 * Copyright 2007 The Kuali Foundation
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
package org.kuali.ole.module.purap.document.web.struts;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.kuali.ole.module.purap.PurapConstants;
import org.kuali.ole.module.purap.PurapConstants.InvoiceStatuses;
import org.kuali.ole.module.purap.PurapConstants.PRQSDocumentsStrings;
import org.kuali.ole.module.purap.PurapKeyConstants;
import org.kuali.ole.module.purap.PurapPropertyConstants;
import org.kuali.ole.module.purap.document.AccountsPayableDocument;
import org.kuali.ole.module.purap.document.InvoiceDocument;
import org.kuali.ole.module.purap.document.PurchaseOrderDocument;
import org.kuali.ole.module.purap.document.PurchasingAccountsPayableDocument;
import org.kuali.ole.module.purap.document.service.InvoiceService;
import org.kuali.ole.module.purap.document.service.PurapService;
import org.kuali.ole.module.purap.document.service.PurchaseOrderService;
import org.kuali.ole.module.purap.document.validation.event.AttributedCalculateAccountsPayableEvent;
import org.kuali.ole.module.purap.document.validation.event.AttributedContinuePurapEvent;
import org.kuali.ole.module.purap.document.validation.event.AttributedPreCalculateAccountsPayableEvent;
import org.kuali.ole.module.purap.service.PurapAccountingService;
import org.kuali.ole.module.purap.util.PurQuestionCallback;
import org.kuali.ole.sys.OLEConstants;
import org.kuali.ole.sys.OLEKeyConstants;
import org.kuali.ole.sys.OLEPropertyConstants;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.ole.sys.service.UniversityDateService;
import org.kuali.rice.core.api.config.property.ConfigurationService;
import org.kuali.rice.core.api.util.RiceConstants;
import org.kuali.rice.kew.api.exception.WorkflowException;
import org.kuali.rice.kim.api.KimConstants;
import org.kuali.rice.kns.question.ConfirmationQuestion;
import org.kuali.rice.kns.service.DocumentHelperService;
import org.kuali.rice.kns.web.struts.form.KualiDocumentFormBase;
import org.kuali.rice.krad.service.KualiRuleService;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.util.KRADConstants;
import org.kuali.rice.krad.util.ObjectUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

/**
 * Struts Action for Payment Request document.
 */
public class InvoiceAction extends AccountsPayableActionBase {
    static org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(InvoiceAction.class);

    /**
     * Do initialization for a new payment request.
     *
     * @see org.kuali.rice.kns.web.struts.action.KualiDocumentActionBase#createDocument(org.kuali.rice.kns.web.struts.form.KualiDocumentFormBase)
     */
    @Override
    protected void createDocument(KualiDocumentFormBase kualiDocumentFormBase) throws WorkflowException {
        super.createDocument(kualiDocumentFormBase);
        ((InvoiceDocument) kualiDocumentFormBase.getDocument()).initiateDocument();
    }

    /**
     * @see org.kuali.rice.kns.web.struts.action.KualiAction#refresh(org.apache.struts.action.ActionMapping,
     *      org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    public ActionForward refresh(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        InvoiceForm prqsForm = (InvoiceForm) form;
        InvoiceDocument document = (InvoiceDocument) prqsForm.getDocument();

        return super.refresh(mapping, form, request, response);
    }

    /**
     * Executes the continue action on a payment request. Populates and initializes the rest of the payment request besides what was
     * shown on the init screen.
     *
     * @param mapping  An ActionMapping
     * @param form     An ActionForm
     * @param request  The HttpServletRequest
     * @param response The HttpServletResponse
     * @return An ActionForward
     * @throws Exception
     */
    public ActionForward continuePRQS(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LOG.debug("continuePRQS() method");
        InvoiceForm prqsForm = (InvoiceForm) form;
        InvoiceDocument invoiceDocument = (InvoiceDocument) prqsForm.getDocument();

        boolean poNotNull = true;

        boolean rulePassed = SpringContext.getBean(KualiRuleService.class).applyRules(new AttributedContinuePurapEvent(invoiceDocument));
        if (!rulePassed) {
            return mapping.findForward(OLEConstants.MAPPING_BASIC);
        }

        GlobalVariables.getMessageMap().clearErrorPath();
        GlobalVariables.getMessageMap().addToErrorPath(OLEPropertyConstants.DOCUMENT);

        //check for a po id
        if (ObjectUtils.isNull(invoiceDocument.getPurchaseOrderIdentifier())) {
            GlobalVariables.getMessageMap().putError(PurapPropertyConstants.PURCHASE_ORDER_IDENTIFIER, OLEKeyConstants.ERROR_REQUIRED, PRQSDocumentsStrings.PURCHASE_ORDER_ID);
            poNotNull = false;
        }

        if (ObjectUtils.isNull(invoiceDocument.getInvoiceDate())) {
            GlobalVariables.getMessageMap().putError(PurapPropertyConstants.INVOICE_DATE, OLEKeyConstants.ERROR_REQUIRED, PRQSDocumentsStrings.INVOICE_DATE);
            poNotNull = false;
        }

        /*if (ObjectUtils.isNull(invoiceDocument.getInvoiceNumber())) {
            GlobalVariables.getMessageMap().putError(PurapPropertyConstants.INVOICE_NUMBER, OLEKeyConstants.ERROR_REQUIRED, PRQSDocumentsStrings.INVOICE_NUMBER);
            poNotNull = false;
        }*/
        invoiceDocument.setInvoiceNumber(invoiceDocument.getInvoiceNumber().toUpperCase());

        if (ObjectUtils.isNull(invoiceDocument.getVendorInvoiceAmount())) {
            GlobalVariables.getMessageMap().putError(PurapPropertyConstants.VENDOR_INVOICE_AMOUNT, OLEKeyConstants.ERROR_REQUIRED, PRQSDocumentsStrings.VENDOR_INVOICE_AMOUNT);
            poNotNull = false;
        }

        //exit early as the po is null, no need to proceed further until this is taken care of
        if (poNotNull == false) {
            return mapping.findForward(OLEConstants.MAPPING_BASIC);
        }


        PurchaseOrderDocument po = SpringContext.getBean(PurchaseOrderService.class).getCurrentPurchaseOrder(invoiceDocument.getPurchaseOrderIdentifier());
        if (ObjectUtils.isNotNull(po)) {
            // TODO figure out a more straightforward way to do this.  ailish put this in so the link id would be set and the perm check would work
            invoiceDocument.setAccountsPayablePurchasingDocumentLinkIdentifier(po.getAccountsPayablePurchasingDocumentLinkIdentifier());

            //check to see if user is allowed to initiate doc based on PO sensitive data
            if (!SpringContext.getBean(DocumentHelperService.class).getDocumentAuthorizer(invoiceDocument).isAuthorizedByTemplate(invoiceDocument, KRADConstants.KNS_NAMESPACE, KimConstants.PermissionTemplateNames.OPEN_DOCUMENT, GlobalVariables.getUserSession().getPrincipalId())) {
                throw buildAuthorizationException("initiate document", invoiceDocument);
            }
        }

        if (!SpringContext.getBean(InvoiceService.class).isPurchaseOrderValidForInvoiceDocumentCreation(invoiceDocument, po)) {
            return mapping.findForward(OLEConstants.MAPPING_BASIC);
        }

        // perform duplicate check which will forward to a question prompt if one is found
        ActionForward forward = performDuplicateInvoiceAndEncumberFiscalYearCheck(mapping, form, request, response, invoiceDocument);
        if (forward != null) {
            return forward;
        }

        // If we are here either there was no duplicate or there was a duplicate and the user hits continue, in either case we need
        // to validate the business rules
        SpringContext.getBean(InvoiceService.class).populateAndSaveInvoice(invoiceDocument);

        // force calculation
        prqsForm.setCalculated(false);

        //TODO if better, move this to the action just before prqs goes into ATAX status
        // force calculation for tax
        prqsForm.setCalculatedTax(false);

        // sort below the line
        SpringContext.getBean(PurapService.class).sortBelowTheLine(invoiceDocument);

        // update the counts on the form
        prqsForm.updateItemCounts();

        return mapping.findForward(OLEConstants.MAPPING_BASIC);
    }


    /**
     * Clears the initial fields on the <code>InvoiceDocument</code> which should be accessible from the given form.
     *
     * @param mapping  An ActionMapping
     * @param form     An ActionForm, which must be a InvoiceForm
     * @param request  The HttpServletRequest
     * @param response The HttpServletResponse
     * @return An ActionForward
     * @throws Exception
     */
    public ActionForward clearInitFields(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LOG.debug("clearInitValues() method");
        InvoiceForm prqsForm = (InvoiceForm) form;
        InvoiceDocument invoiceDocument = (InvoiceDocument) prqsForm.getDocument();
        invoiceDocument.clearInitFields();

        return super.refresh(mapping, form, request, response);
    }

    /**
     * This method runs two checks based on the user input on PRQS initiate screen: Encumber next fiscal year check and Duplicate
     * payment request check. Encumber next fiscal year is checked first and will display a warning message to the user if it's the
     * case. Duplicate payment request check calls <code>InvoiceService</code> to perform the duplicate payment request
     * check. If one is found, a question is setup and control is forwarded to the question action method. Coming back from the
     * question prompt the button that was clicked is checked and if 'no' was selected they are forward back to the page still in
     * init mode.
     *
     * @param mapping         An ActionMapping
     * @param form            An ActionForm
     * @param request         The HttpServletRequest
     * @param response        The HttpServletResponse
     * @param invoiceDocument The InvoiceDocument
     * @return An ActionForward
     * @throws Exception
     * @see org.kuali.ole.module.purap.document.service.InvoiceService
     */
    protected ActionForward performDuplicateInvoiceAndEncumberFiscalYearCheck(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response, InvoiceDocument invoiceDocument) throws Exception {
        ActionForward forward = null;
        Object question = request.getParameter(OLEConstants.QUESTION_INST_ATTRIBUTE_NAME);
        if (question == null) {
            // perform encumber next fiscal year check and prompt warning message if needs
            if (isEncumberNextFiscalYear(invoiceDocument)) {
                String questionText = SpringContext.getBean(ConfigurationService.class).getPropertyValueAsString(PurapKeyConstants.WARNING_ENCUMBER_NEXT_FY);
                return this.performQuestionWithoutInput(mapping, form, request, response, PRQSDocumentsStrings.ENCUMBER_NEXT_FISCAL_YEAR_QUESTION, questionText, OLEConstants.CONFIRMATION_QUESTION, OLEConstants.ROUTE_METHOD, "");
            } else {
                // perform duplicate payment request check
                HashMap<String, String> duplicateMessages = SpringContext.getBean(InvoiceService.class).invoiceDuplicateMessages(invoiceDocument);
                if (!duplicateMessages.isEmpty()) {
                    return this.performQuestionWithoutInput(mapping, form, request, response, PRQSDocumentsStrings.DUPLICATE_INVOICE_QUESTION, duplicateMessages.get(PRQSDocumentsStrings.DUPLICATE_INVOICE_QUESTION), OLEConstants.CONFIRMATION_QUESTION, OLEConstants.ROUTE_METHOD, "");
                }
            }
        } else {
            Object buttonClicked = request.getParameter(OLEConstants.QUESTION_CLICKED_BUTTON);
            // If the user replies 'Yes' to the encumber-next-year-question, proceed with duplicate payment check
            if (PRQSDocumentsStrings.ENCUMBER_NEXT_FISCAL_YEAR_QUESTION.equals(question) && ConfirmationQuestion.YES.equals(buttonClicked)) {
                HashMap<String, String> duplicateMessages = SpringContext.getBean(InvoiceService.class).invoiceDuplicateMessages(invoiceDocument);
                if (!duplicateMessages.isEmpty()) {
                    return this.performQuestionWithoutInput(mapping, form, request, response, PRQSDocumentsStrings.DUPLICATE_INVOICE_QUESTION, duplicateMessages.get(PRQSDocumentsStrings.DUPLICATE_INVOICE_QUESTION), OLEConstants.CONFIRMATION_QUESTION, OLEConstants.ROUTE_METHOD, "");
                }
            }
            // If the user replies 'No' to either of the questions, redirect to the PRQS initiate page.
            else if ((PRQSDocumentsStrings.ENCUMBER_NEXT_FISCAL_YEAR_QUESTION.equals(question) || PRQSDocumentsStrings.DUPLICATE_INVOICE_QUESTION.equals(question)) && ConfirmationQuestion.NO.equals(buttonClicked)) {
                invoiceDocument.updateAndSaveAppDocStatus(InvoiceStatuses.APPDOC_INITIATE);
                forward = mapping.findForward(OLEConstants.MAPPING_BASIC);
            }
        }

        return forward;
    }

    /**
     * Check if the current PRQS encumber next fiscal year from PO document.
     *
     * @param invoiceDocument
     * @return
     */
    protected boolean isEncumberNextFiscalYear(InvoiceDocument invoiceDocument) {
        Integer fiscalYear = SpringContext.getBean(UniversityDateService.class).getCurrentFiscalYear();
        if (invoiceDocument.getPurchaseOrderDocument().getPostingYear().intValue() > fiscalYear) {
            return true;
        }
        return false;
    }

    /**
     * Puts a payment on hold, prompting for a reason beforehand. This stops further approvals or routing.
     *
     * @param mapping  An ActionMapping
     * @param form     An ActionForm
     * @param request  The HttpServletRequest
     * @param response The HttpServletResponse
     * @return An ActionForward
     * @throws Exception
     */
    public ActionForward addHoldOnPayment(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String operation = "Hold ";

        PurQuestionCallback callback = new PurQuestionCallback() {
            @Override
            public AccountsPayableDocument doPostQuestion(AccountsPayableDocument document, String noteText) throws Exception {
                document = SpringContext.getBean(InvoiceService.class).addHoldOnInvoice((InvoiceDocument) document, noteText);
                return document;
            }
        };

        return askQuestionWithInput(mapping, form, request, response, PRQSDocumentsStrings.HOLD_PRQS_QUESTION, PRQSDocumentsStrings.HOLD_NOTE_PREFIX, operation, PurapKeyConstants.PAYMENT_REQUEST_MESSAGE_HOLD_DOCUMENT, callback);
    }

    /**
     * Removes a hold on the payment request.
     *
     * @param mapping  An ActionMapping
     * @param form     An ActionForm
     * @param request  The HttpServletRequest
     * @param response The HttpServletResponse
     * @return An ActionForward
     * @throws Exception
     */
    public ActionForward removeHoldFromPayment(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String operation = "Remove ";

        PurQuestionCallback callback = new PurQuestionCallback() {
            @Override
            public AccountsPayableDocument doPostQuestion(AccountsPayableDocument document, String noteText) throws Exception {
                document = SpringContext.getBean(InvoiceService.class).removeHoldOnInvoice((InvoiceDocument) document, noteText);
                return document;
            }
        };

        return askQuestionWithInput(mapping, form, request, response, PRQSDocumentsStrings.REMOVE_HOLD_PRQS_QUESTION, PRQSDocumentsStrings.REMOVE_HOLD_NOTE_PREFIX, operation, PurapKeyConstants.PAYMENT_REQUEST_MESSAGE_REMOVE_HOLD_DOCUMENT, callback);
    }

    /**
     * This action requests a cancel on a prqs, prompting for a reason before hand. This stops further approvals or routing.
     *
     * @param mapping  An ActionMapping
     * @param form     An ActionForm
     * @param request  The HttpServletRequest
     * @param response The HttpServletResponse
     * @return An ActionForward
     * @throws Exception
     */
    public ActionForward requestCancelOnPayment(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String operation = "Cancel ";

        PurQuestionCallback callback = new PurQuestionCallback() {
            @Override
            public AccountsPayableDocument doPostQuestion(AccountsPayableDocument document, String noteText) throws Exception {
                SpringContext.getBean(InvoiceService.class).requestCancelOnInvoice((InvoiceDocument) document, noteText);
                return document;
            }
        };

        return askQuestionWithInput(mapping, form, request, response, PRQSDocumentsStrings.CANCEL_PRQS_QUESTION, PRQSDocumentsStrings.CANCEL_NOTE_PREFIX, operation, PurapKeyConstants.PAYMENT_REQUEST_MESSAGE_CANCEL_DOCUMENT, callback);
    }

    /**
     * @see AccountsPayableActionBase#cancelPOActionCallbackMethod()
     */
//    @Override
//    protected PurQuestionCallback cancelPOActionCallbackMethod() {
//
//        return new PurQuestionCallback() {
//            public AccountsPayableDocument doPostQuestion(AccountsPayableDocument document, String noteText) throws Exception {
//                InvoiceDocument prqsDocument = (InvoiceDocument) document;
//                prqsDocument.setReopenPurchaseOrderIndicator(true);
//                return prqsDocument;
//            }
//        };
//    }

    /**
     * Removes a request for cancel on a payment request.
     *
     * @param mapping  An ActionMapping
     * @param form     An ActionForm
     * @param request  The HttpServletRequest
     * @param response The HttpServletResponse
     * @return An ActionForward
     * @throws Exception
     */
    public ActionForward removeCancelRequestFromPayment(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String operation = "Cancel ";

        PurQuestionCallback callback = new PurQuestionCallback() {
            @Override
            public AccountsPayableDocument doPostQuestion(AccountsPayableDocument document, String noteText) throws Exception {
                SpringContext.getBean(InvoiceService.class).removeRequestCancelOnInvoice((InvoiceDocument) document, noteText);
                return document;
            }
        };

        return askQuestionWithInput(mapping, form, request, response, PRQSDocumentsStrings.REMOVE_CANCEL_PRQS_QUESTION, PRQSDocumentsStrings.REMOVE_CANCEL_NOTE_PREFIX, operation, PurapKeyConstants.PAYMENT_REQUEST_MESSAGE_REMOVE_CANCEL_DOCUMENT, callback);
    }

    /**
     * Calls a service method to calculate for a payment request document.
     *
     * @param apDoc The AccountsPayableDocument
     */
    @Override
    protected void customCalculate(PurchasingAccountsPayableDocument apDoc) {
        InvoiceDocument prqsDoc = (InvoiceDocument) apDoc;

        // set amounts on any empty
        prqsDoc.updateExtendedPriceOnItems();

        // calculation just for the tax area, only at tax review stage
        // by now, the general calculation shall have been done.
        if (StringUtils.equals(prqsDoc.getApplicationDocumentStatus(), InvoiceStatuses.APPDOC_AWAITING_TAX_REVIEW)) {
            SpringContext.getBean(InvoiceService.class).calculateTaxArea(prqsDoc);
            return;
        }

        // notice we're ignoring whether the boolean, because these are just warnings they shouldn't halt anything
        //Calculate Payment request before rules since the rule check totalAmount.
        SpringContext.getBean(InvoiceService.class).calculateInvoice(prqsDoc, true);
        SpringContext.getBean(KualiRuleService.class).applyRules(new AttributedCalculateAccountsPayableEvent(prqsDoc));
    }

    /**
     * @see AccountsPayableActionBase#getActionName()
     */
    @Override
    public String getActionName() {
        return PurapConstants.INVOICE_ACTION_NAME;
    }

    public ActionForward useAlternateVendor(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        InvoiceForm prqsForm = (InvoiceForm) form;
        InvoiceDocument document = (InvoiceDocument) prqsForm.getDocument();

        SpringContext.getBean(InvoiceService.class).changeVendor(
                document, document.getAlternateVendorHeaderGeneratedIdentifier(), document.getAlternateVendorDetailAssignedIdentifier());

        return mapping.findForward(OLEConstants.MAPPING_BASIC);
    }

    public ActionForward useOriginalVendor(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        InvoiceForm prqsForm = (InvoiceForm) form;
        InvoiceDocument document = (InvoiceDocument) prqsForm.getDocument();

        SpringContext.getBean(InvoiceService.class).changeVendor(
                document, document.getOriginalVendorHeaderGeneratedIdentifier(), document.getOriginalVendorDetailAssignedIdentifier());

        return mapping.findForward(OLEConstants.MAPPING_BASIC);
    }

    @Override
    public ActionForward route(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        InvoiceDocument prqs = ((InvoiceForm) form).getInvoiceDocument();
        SpringContext.getBean(PurapService.class).prorateForTradeInAndFullOrderDiscount(prqs);
        SpringContext.getBean(PurapAccountingService.class).updateAccountAmounts(prqs);
        if (prqs.isClosePurchaseOrderIndicator()) {
            PurchaseOrderDocument po = prqs.getPurchaseOrderDocument();
            if (po.canClosePOForTradeIn()) {
                return super.route(mapping, form, request, response);
            } else {
                return mapping.findForward(OLEConstants.MAPPING_BASIC);
            }
        } else {
            return super.route(mapping, form, request, response);
        }
    }

    /**
     * Overrides to invoke the updateAccountAmounts so that the account percentage will be
     * correctly updated before validation for account percent is called.
     *
     * @see org.kuali.rice.kns.web.struts.action.KualiDocumentActionBase#approve(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    public ActionForward approve(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        InvoiceDocument prqs = ((InvoiceForm) form).getInvoiceDocument();

        SpringContext.getBean(PurapService.class).prorateForTradeInAndFullOrderDiscount(prqs);
        // if tax is required but not yet calculated, return and prompt user to calculate
        if (requiresCalculateTax((InvoiceForm) form)) {
            GlobalVariables.getMessageMap().putError(OLEConstants.DOCUMENT_ERRORS, PurapKeyConstants.ERROR_APPROVE_REQUIRES_CALCULATE);
            return mapping.findForward(OLEConstants.MAPPING_BASIC);
        }

        // enforce calculating tax again upon approval, just in case user changes tax data without calculation
        // other wise there will be a loophole, because the taxCalculated indicator is already set upon first calculation
        // and thus system wouldn't know it's not re-calculated after tax data are changed
        if (SpringContext.getBean(KualiRuleService.class).applyRules(new AttributedPreCalculateAccountsPayableEvent(prqs))) {
            // pre-calculation rules succeed, calculate tax again and go ahead with approval
            customCalculate(prqs);
            SpringContext.getBean(PurapAccountingService.class).updateAccountAmounts(prqs);
            return super.approve(mapping, form, request, response);
        } else {
            // pre-calculation rules fail, go back to same page with error messages
            return mapping.findForward(OLEConstants.MAPPING_BASIC);
        }
    }

    /**
     * Checks if tax calculation is required.
     * Currently it is required when prqs is awaiting for tax approval and tax has not already been calculated.
     *
     * @param invoiceForm A Form, which must inherit from <code>AccountsPayableFormBase</code>
     * @return true if calculation is required, false otherwise
     */
    protected boolean requiresCalculateTax(InvoiceForm invoiceForm) {
        InvoiceDocument prqs = (InvoiceDocument) invoiceForm.getDocument();
        boolean requiresCalculateTax = StringUtils.equals(prqs.getApplicationDocumentStatus(), InvoiceStatuses.APPDOC_AWAITING_TAX_REVIEW) && !invoiceForm.isCalculatedTax();
        return requiresCalculateTax;
    }

    public ActionForward changeUseTaxIndicator(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        PurchasingAccountsPayableDocument document = (PurchasingAccountsPayableDocument) ((PurchasingAccountsPayableFormBase) form).getDocument();

        //clear/recalculate tax and recreate GL entries
        SpringContext.getBean(PurapService.class).updateUseTaxIndicator(document, !document.isUseTaxIndicator());
        SpringContext.getBean(PurapService.class).calculateTax(document);

        //TODO: add recalculate GL entries hook here

        return mapping.findForward(OLEConstants.MAPPING_BASIC);
    }

    /**
     * Calls service to clear tax info.
     */
    public ActionForward clearTaxInfo(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        InvoiceForm prForm = (InvoiceForm) form;
        InvoiceDocument document = (InvoiceDocument) prForm.getDocument();

        InvoiceService taxService = SpringContext.getBean(InvoiceService.class);

        /* call service to clear previous lines */
        taxService.clearTax(document);

        return mapping.findForward(OLEConstants.MAPPING_BASIC);
    }

    //MSU Contribution OLEMI-8558 DTT-3765 OLECNTRB-963
    @Override
    public ActionForward cancel(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        InvoiceForm prqsForm = (InvoiceForm) form;

        InvoiceDocument prqsDocument = (InvoiceDocument) prqsForm.getDocument();

        ActionForward forward = mapping.findForward(RiceConstants.MAPPING_BASIC);
        if (prqsDocument.getPurchaseOrderDocument().isPendingActionIndicator()) {
            GlobalVariables.getMessageMap().putError(
                    OLEPropertyConstants.DOCUMENT + "." + OLEPropertyConstants.DOCUMENT_NUMBER,
                    PurapKeyConstants.ERROR_PAYMENT_REQUEST_CANNOT_BE_CANCELLED);
        } else {
            forward = super.cancel(mapping, form, request, response);
        }

        return forward;
    }
}
