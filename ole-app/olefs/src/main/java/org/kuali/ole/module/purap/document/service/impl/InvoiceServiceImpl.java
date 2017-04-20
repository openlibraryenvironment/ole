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
package org.kuali.ole.module.purap.document.service.impl;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.text.StrBuilder;
import org.kuali.ole.module.purap.*;
import org.kuali.ole.module.purap.PurapConstants.InvoiceStatuses;
import org.kuali.ole.module.purap.PurapConstants.ItemTypeCodes;
import org.kuali.ole.module.purap.PurapConstants.PRQSDocumentsStrings;
import org.kuali.ole.module.purap.PurapParameterConstants.NRATaxParameters;
import org.kuali.ole.module.purap.businessobject.*;
import org.kuali.ole.module.purap.document.*;
import org.kuali.ole.module.purap.document.dataaccess.InvoiceDao;
import org.kuali.ole.module.purap.document.service.*;
import org.kuali.ole.module.purap.document.validation.event.AttributedContinuePurapEvent;
import org.kuali.ole.module.purap.exception.PurError;
import org.kuali.ole.module.purap.service.PurapAccountingService;
import org.kuali.ole.module.purap.service.PurapGeneralLedgerService;
import org.kuali.ole.module.purap.util.PurApItemUtils;
import org.kuali.ole.module.purap.util.VendorGroupingHelper;
import org.kuali.ole.select.OleSelectConstant;
import org.kuali.ole.select.document.OleInvoiceDocument;
import org.kuali.ole.sys.OLEConstants;
import org.kuali.ole.sys.OLEPropertyConstants;
import org.kuali.ole.sys.businessobject.AccountingLine;
import org.kuali.ole.sys.businessobject.SourceAccountingLine;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.ole.sys.service.BankService;
import org.kuali.ole.sys.service.FinancialSystemWorkflowHelperService;
import org.kuali.ole.sys.service.UniversityDateService;
import org.kuali.ole.sys.service.impl.OleParameterConstants;
import org.kuali.ole.vnd.VendorConstants;
import org.kuali.ole.vnd.businessobject.PaymentTermType;
import org.kuali.ole.vnd.businessobject.VendorAddress;
import org.kuali.ole.vnd.businessobject.VendorDetail;
import org.kuali.ole.vnd.document.service.VendorService;
import org.kuali.rice.core.api.config.property.ConfigurationService;
import org.kuali.rice.core.api.datetime.DateTimeService;
import org.kuali.rice.core.api.util.type.KualiDecimal;
import org.kuali.rice.coreservice.api.CoreServiceApiServiceLocator;
import org.kuali.rice.coreservice.api.parameter.Parameter;
import org.kuali.rice.coreservice.api.parameter.ParameterKey;
import org.kuali.rice.coreservice.framework.parameter.ParameterService;
import org.kuali.rice.kew.api.KewApiServiceLocator;
import org.kuali.rice.kew.api.WorkflowDocument;
import org.kuali.rice.kew.api.document.search.DocumentSearchCriteria;
import org.kuali.rice.kew.api.document.search.DocumentSearchResult;
import org.kuali.rice.kew.api.document.search.DocumentSearchResults;
import org.kuali.rice.kew.api.exception.WorkflowException;
import org.kuali.rice.kim.api.identity.Person;
import org.kuali.rice.kns.service.DataDictionaryService;
import org.kuali.rice.krad.bo.DocumentHeader;
import org.kuali.rice.krad.bo.Note;
import org.kuali.rice.krad.exception.ValidationException;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.DocumentService;
import org.kuali.rice.krad.service.KualiRuleService;
import org.kuali.rice.krad.service.NoteService;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.util.KRADPropertyConstants;
import org.kuali.rice.krad.util.ObjectUtils;
import org.kuali.rice.krad.workflow.service.WorkflowDocumentService;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.*;

/**
 * This class provides services of use to a payment request document
 */
@Transactional
public class InvoiceServiceImpl implements InvoiceService {
    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(InvoiceServiceImpl.class);

    protected DateTimeService dateTimeService;
    protected DocumentService documentService;
    protected NoteService noteService;
    protected PurapService purapService;
    protected InvoiceDao invoiceDao;
    protected ParameterService parameterService;
    protected ConfigurationService configurationService;
    protected NegativeInvoiceApprovalLimitService negativeInvoiceApprovalLimitService;
    protected PurapAccountingService purapAccountingService;
    protected BusinessObjectService businessObjectService;
    protected PurApWorkflowIntegrationService purapWorkflowIntegrationService;
    protected WorkflowDocumentService workflowDocumentService;
    protected AccountsPayableService accountsPayableService;
    protected VendorService vendorService;
    protected DataDictionaryService dataDictionaryService;
    protected UniversityDateService universityDateService;
    protected BankService bankService;
    protected PurchaseOrderService purchaseOrderService;
    protected FinancialSystemWorkflowHelperService financialSystemWorkflowHelperService;
    protected KualiRuleService kualiRuleService;
    protected boolean currencyTypeIndicator;
    /**
     * NOTE: unused
     *
     * @see org.kuali.ole.module.purap.document.service.InvoiceService#getInvoicesToExtractByCM(String, org.kuali.ole.module.purap.document.VendorCreditMemoDocument)
     */
    @Override
    @Deprecated
    public Collection<InvoiceDocument> getInvoicesToExtractByCM(String campusCode, VendorCreditMemoDocument cmd) {
        LOG.debug("getInvoicesByCM() started");
        Date currentSqlDateMidnight = dateTimeService.getCurrentSqlDateMidnight();
        List<InvoiceDocument> invoiceIterator = invoiceDao.getInvoicesToExtract(campusCode, null, null, cmd.getVendorHeaderGeneratedIdentifier(), cmd.getVendorDetailAssignedIdentifier(), currentSqlDateMidnight);

        return filterInvoiceByAppDocStatus(invoiceIterator,
                InvoiceStatuses.APPDOC_AUTO_APPROVED,
                InvoiceStatuses.APPDOC_DEPARTMENT_APPROVED);
    }


    /**
     * @see org.kuali.ole.module.purap.document.service.InvoiceService#getInvoicesToExtractByVendor(String,
     *      org.kuali.ole.module.purap.util.VendorGroupingHelper, java.sql.Date)
     */
    @Override
    public Collection<InvoiceDocument> getInvoicesToExtractByVendor(String campusCode, VendorGroupingHelper vendor, Date onOrBeforeInvoicePayDate) {
        LOG.debug("getInvoicesByVendor() started");
        Collection<InvoiceDocument> invoiceDocuments = invoiceDao.getInvoicesToExtractForVendor(campusCode, vendor, onOrBeforeInvoicePayDate);

        return filterInvoiceByAppDocStatus(invoiceDocuments,
                InvoiceStatuses.APPDOC_AUTO_APPROVED,
                InvoiceStatuses.APPDOC_DEPARTMENT_APPROVED);
    }

    /**
     * @see org.kuali.ole.module.purap.document.service.InvoiceService#getInvoicesToExtract(java.sql.Date)
     */
    @Override
    public Collection<InvoiceDocument> getInvoicesToExtract(Date onOrBeforeInvoicePayDate) {
        LOG.debug("getInvoicesToExtract() started");

        Collection<InvoiceDocument> invoiceIterator = invoiceDao.getInvoicesToExtract(false, null, onOrBeforeInvoicePayDate);
        return filterInvoiceByAppDocStatus(invoiceIterator,
                InvoiceStatuses.STATUSES_ALLOWED_FOR_EXTRACTION);
    }

    /**
     * @see org.kuali.ole.module.purap.document.service.InvoiceService#getInvoicesToExtractSpecialPayments(String,
     *      java.sql.Date)
     */
    @Override
    public Collection<InvoiceDocument> getInvoicesToExtractSpecialPayments(String chartCode, Date onOrBeforeInvoicePayDate) {
        LOG.debug("getInvoicesToExtractSpecialPayments() started");

        Collection<InvoiceDocument> invoiceIterator = invoiceDao.getInvoicesToExtract(true, chartCode, onOrBeforeInvoicePayDate);
        return filterInvoiceByAppDocStatus(invoiceIterator,
                InvoiceStatuses.STATUSES_ALLOWED_FOR_EXTRACTION);
    }

    /**
     * @see org.kuali.ole.module.purap.document.service.InvoiceService#getImmediateInvoicesToExtract(String)
     */
    @Override
    public Collection<InvoiceDocument> getImmediateInvoicesToExtract(String chartCode) {
        LOG.debug("getImmediateInvoicesToExtract() started");

        Collection<InvoiceDocument> invoiceIterator = invoiceDao.getImmediateInvoicesToExtract(chartCode);
        return filterInvoiceByAppDocStatus(invoiceIterator,
                InvoiceStatuses.STATUSES_ALLOWED_FOR_EXTRACTION);
    }

    /**
     * @see org.kuali.ole.module.purap.document.service.InvoiceService#getInvoiceToExtractByChart(String,
     *      java.sql.Date)
     */
    @Override
    public Collection<InvoiceDocument> getInvoiceToExtractByChart(String chartCode, Date onOrBeforeInvoicePayDate) {
        LOG.debug("getInvoiceToExtractByChart() started");

        Collection<InvoiceDocument> invoiceIterator = invoiceDao.getInvoicesToExtract(false, chartCode, onOrBeforeInvoicePayDate);
        return filterInvoiceByAppDocStatus(invoiceIterator,
                InvoiceStatuses.STATUSES_ALLOWED_FOR_EXTRACTION);
    }

    /**
     * @see org.kuali.ole.module.purap.document.service.InvoiceService#autoApproveInvoices()
     */
/*
    @Override
    public boolean autoApproveInvoices() {
        if (LOG.isInfoEnabled()) {
            LOG.info("Starting autoApproveInvoices.");
        }
        boolean hadErrorAtLeastOneError = true;
        // should objects from existing user session be copied over
        Date todayAtMidnight = dateTimeService.getCurrentSqlDateMidnight();

        List<String> docNumbers = invoiceDao.getEligibleForAutoApproval(todayAtMidnight);
        docNumbers = filterInvoiceByAppDocStatus(docNumbers, InvoiceStatuses.PRQS_STATUSES_FOR_AUTO_APPROVE);
        if (LOG.isInfoEnabled()) {
            LOG.info(" -- Initial filtering complete, returned " + new Integer(docNumbers.size()).toString() + " docs.");
        }

        List<InvoiceDocument> docs = new ArrayList<InvoiceDocument>();
        for (String docNumber : docNumbers) {
            InvoiceDocument prqs = getInvoiceByDocumentNumber(docNumber);
            if (ObjectUtils.isNotNull(prqs)) {
                docs.add(prqs);
            }
        }
        if (LOG.isInfoEnabled()) {
            LOG.info(" -- Initial filtering complete, returned " + new Integer((docs == null ? 0 : docs.size())).toString() + " docs.");
        }
        if (docs != null) {
            String samt = getParameter(OLEConstants.OptionalModuleNamespaces.PURCHASING_ACCOUNTS_PAYABLE, OLEConstants.InvoiceDocument.CMPNT_CD,PurapParameterConstants.PURAP_DEFAULT_NEGATIVE_PAYMENT_REQUEST_APPROVAL_LIMIT);
            KualiDecimal defaultMinimumLimit = new KualiDecimal(samt);
            if (LOG.isInfoEnabled()) {
                LOG.info(" -- Using default limit value of " + defaultMinimumLimit.toString() + ".");
            }
            for (InvoiceDocument invoiceDocument : docs) {
                hadErrorAtLeastOneError |= !autoApproveInvoice(invoiceDocument, defaultMinimumLimit);
            }
        }
        return hadErrorAtLeastOneError;
    }
*/

    /**
     * NOTE: in the event of auto-approval failure, this method may throw a RuntimeException, indicating to Spring transactional
     * management that the transaction should be rolled back.
     *
     * @see org.kuali.ole.module.purap.document.service.InvoiceService#autoApproveInvoice(String,
     *      org.kuali.rice.core.api.util.type.KualiDecimal)
     */
   /* @Override
    public boolean autoApproveInvoice(String docNumber, KualiDecimal defaultMinimumLimit) {
        InvoiceDocument invoiceDocument = null;
        try {
            invoiceDocument = (InvoiceDocument) documentService.getByDocumentHeaderId(docNumber);
            if (invoiceDocument.isHoldIndicator() || invoiceDocument.isInvoiceCancelIndicator() || !Arrays.asList(InvoiceStatuses.PRQS_STATUSES_FOR_AUTO_APPROVE).contains(invoiceDocument.getApplicationDocumentStatus())) {
                // this condition is based on the conditions that InvoiceDaoOjb.getEligibleDocumentNumbersForAutoApproval()
                // uses to query
                // the database. Rechecking these conditions to ensure that the document is eligible for auto-approval, because
                // we're not running things
                // within the same transaction anymore and changes could have occurred since we called that method that make this
                // document not auto-approvable

                // note that this block does not catch all race conditions
                // however, this error condition is not enough to make us return an error code, so just skip the document
                LOG.warn("Invoice Document " + invoiceDocument.getDocumentNumber() + " could not be auto-approved because it has either been placed on hold, " + " requested cancel, or does not have one of the PRQS statuses for auto-approve.");
                return true;
            }
            if (autoApproveInvoice(invoiceDocument, defaultMinimumLimit)) {
                if (LOG.isInfoEnabled()) {
                    LOG.info("Auto-approval for payment request successful.  Doc number: " + docNumber);
                }
                return true;
            } else {
                LOG.error("Invoice Document " + docNumber + " could not be auto-approved.");
                return false;
            }
        } catch (WorkflowException we) {
            LOG.error("Exception encountered when retrieving document number " + docNumber + ".", we);
            // throw a runtime exception up so that we can force a rollback
            throw new RuntimeException("Exception encountered when retrieving document number " + docNumber + ".", we);
        }
    }*/

    /**
     * NOTE: in the event of auto-approval failure, this method may throw a RuntimeException, indicating to Spring transactional
     * management that the transaction should be rolled back.
     *
     * @see org.kuali.ole.module.purap.document.service.InvoiceService#autoApproveInvoice(org.kuali.ole.module.purap.document.InvoiceDocument,
     *      org.kuali.rice.core.api.util.type.KualiDecimal)
     */
    @Override
    public boolean autoApproveInvoice(InvoiceDocument doc, KualiDecimal defaultMinimumLimit) {
        if (isEligibleForAutoApproval(doc, defaultMinimumLimit)) {
            try {
                // Much of the rice frameworks assumes that document instances that are saved via DocumentService.saveDocument are
                // those
                // that were dynamically created by PojoFormBase (i.e., the Document instance wasn't created from OJB). We need to
                // make
                // a deep copy and materialize collections to fulfill that assumption so that collection elements will delete
                // properly

                // TODO: maybe rewriting PurapService.calculateItemTax could be rewritten so that the a deep copy doesn't need to be
                // made
                // by taking advantage of OJB's managed array lists
                try {
                    ObjectUtils.materializeUpdateableCollections(doc);
                    for (InvoiceItem item : (List<InvoiceItem>) doc.getItems()) {
                        ObjectUtils.materializeUpdateableCollections(item);
                    }
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
                doc = (InvoiceDocument) ObjectUtils.deepCopy(doc);
                //purapService.updateStatus(doc, InvoiceStatuses.AUTO_APPROVED);
                doc.updateAndSaveAppDocStatus(InvoiceStatuses.APPDOC_AUTO_APPROVED);

                documentService.blanketApproveDocument(doc, "auto-approving: Total is below threshold.", null);
            } catch (WorkflowException we) {
                LOG.error("Exception encountered when approving document number " + doc.getDocumentNumber() + ".", we);
                // throw a runtime exception up so that we can force a rollback
                throw new RuntimeException("Exception encountered when approving document number " + doc.getDocumentNumber() + ".", we);
            }
        }
        return true;
    }

    /**
     * NOTE: in the event of auto-approval failure, this method may throw a RuntimeException, indicating to Spring transactional
     * management that the transaction should be rolled back.
     *
     * @see org.kuali.ole.module.purap.document.service.PaymentRequestService#autoApprovePaymentRequest(java.lang.String,
     *      org.kuali.rice.core.api.util.type.KualiDecimal)
     */
    @Override
    public boolean autoApprovePaymentRequest(String docNumber) {
        PaymentRequestDocument paymentRequestDocument = null;
        try {
            paymentRequestDocument = (PaymentRequestDocument) documentService.getByDocumentHeaderId(docNumber);
            if (paymentRequestDocument.isHoldIndicator() || paymentRequestDocument.isPaymentRequestedCancelIndicator() || !Arrays.asList(PurapConstants.PaymentRequestStatuses.PREQ_STATUSES_FOR_AUTO_APPROVE).contains(paymentRequestDocument.getApplicationDocumentStatus())) {
                // this condition is based on the conditions that PaymentRequestDaoOjb.getEligibleDocumentNumbersForAutoApproval()
                // uses to query
                // the database. Rechecking these conditions to ensure that the document is eligible for auto-approval, because
                // we're not running things
                // within the same transaction anymore and changes could have occurred since we called that method that make this
                // document not auto-approvable

                // note that this block does not catch all race conditions
                // however, this error condition is not enough to make us return an error code, so just skip the document
                LOG.warn("Payment Request Document " + paymentRequestDocument.getDocumentNumber() + " could not be auto-approved because it has either been placed on hold, " + " requested cancel, or does not have one of the PREQ statuses for auto-approve.");
                return true;
            }
            if (autoApprovePaymentRequest(paymentRequestDocument)) {
                if (LOG.isInfoEnabled()) {
                    LOG.info("Auto-approval for payment request successful.  Doc number: " + docNumber);
                }
                return true;
            } else {
                LOG.error("Payment Request Document " + docNumber + " could not be auto-approved.");
                return false;
            }
        } catch (WorkflowException we) {
            LOG.error("Exception encountered when retrieving document number " + docNumber + ".", we);
            // throw a runtime exception up so that we can force a rollback
            throw new RuntimeException("Exception encountered when retrieving document number " + docNumber + ".", we);
        }
    }


    /**
     * NOTE: in the event of auto-approval failure, this method may throw a RuntimeException, indicating to Spring transactional
     * management that the transaction should be rolled back.
     *
     * @see org.kuali.ole.module.purap.document.service.PaymentRequestService#autoApprovePaymentRequest(org.kuali.ole.module.purap.document.PaymentRequestDocument,
     *      org.kuali.rice.core.api.util.type.KualiDecimal)
     */
    public boolean autoApprovePaymentRequest(PaymentRequestDocument doc) {
        try {
            // Much of the rice frameworks assumes that document instances that are saved via DocumentService.saveDocument are
            // those
            // that were dynamically created by PojoFormBase (i.e., the Document instance wasn't created from OJB). We need to
            // make
            // a deep copy and materialize collections to fulfill that assumption so that collection elements will delete
            // properly

            // TODO: maybe rewriting PurapService.calculateItemTax could be rewritten so that the a deep copy doesn't need to be
            // made
            // by taking advantage of OJB's managed array lists
            try {
                ObjectUtils.materializeUpdateableCollections(doc);
                for (PaymentRequestItem item : (List<PaymentRequestItem>) doc.getItems()) {
                    ObjectUtils.materializeUpdateableCollections(item);
                }
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
            doc = (PaymentRequestDocument) ObjectUtils.deepCopy(doc);
            //purapService.updateStatus(doc, PaymentRequestStatuses.AUTO_APPROVED);
            doc.updateAndSaveAppDocStatus(PurapConstants.PaymentRequestStatuses.APPDOC_AUTO_APPROVED);

            documentService.blanketApproveDocument(doc, "auto-approving: Document Created from Invoice.", null);
        } catch (WorkflowException we) {
            LOG.error("Exception encountered when approving document number " + doc.getDocumentNumber() + ".", we);
            // throw a runtime exception up so that we can force a rollback
            throw new RuntimeException("Exception encountered when approving document number " + doc.getDocumentNumber() + ".", we);
        }
        return true;
    }

    /**
     * Determines whether or not a payment request document can be automatically approved. FYI - If fiscal reviewers are allowed to
     * save account changes without the full account validation running then this method must call full account validation to make
     * sure auto approver is not blanket approving an invalid document according the the accounts on the items
     *
     * @param document            The payment request document to be determined whether it can be automatically approved.
     * @param defaultMinimumLimit The amount to be used as the minimum amount if no limit was found or the default is less than the
     *                            limit.
     * @return boolean true if the payment request document is eligible for auto approval.
     */
    protected boolean isEligibleForAutoApproval(InvoiceDocument document, KualiDecimal defaultMinimumLimit) {
        // Check if vendor is foreign.
        if (document.getVendorDetail().getCurrencyType()!=null){
            if(document.getVendorDetail().getCurrencyType().getCurrencyType().equalsIgnoreCase(OleSelectConstant.CURRENCY_TYPE_NAME)){
                currencyTypeIndicator=true;
            }
            else{
                currencyTypeIndicator=false;
            }
        }
        if (!currencyTypeIndicator) {
            if (LOG.isInfoEnabled()) {
                LOG.info(" -- PayReq [" + document.getDocumentNumber() + "] skipped due to a Foreign Vendor.");
            }
            return false;
        }

        // check to make sure the payment request isn't scheduled to stop in tax review.
        if (purapWorkflowIntegrationService.willDocumentStopAtGivenFutureRouteNode(document, InvoiceStatuses.NODE_VENDOR_TAX_REVIEW)) {
            if (LOG.isInfoEnabled()) {
                LOG.info(" -- PayReq [" + document.getDocumentNumber() + "] skipped due to requiring Tax Review.");
            }
            return false;
        }

        // Change to not auto approve if positive approval required indicator set to Yes
        if (document.isInvoicePositiveApprovalIndicator()) {
            if (LOG.isInfoEnabled()) {
                LOG.info(" -- PayReq [" + document.getDocumentNumber()
                        + "] skipped due to a Positive Approval Required Indicator set to Yes.");
            }
            return false;
        }

        // This minimum will be set to the minimum limit derived from all
        // accounting lines on the document. If no limit is determined, the
        // default will be used.
        KualiDecimal minimumAmount = null;

        // Iterate all source accounting lines on the document, deriving a
        // minimum limit from each according to chart, chart and account, and
        // chart and organization.
        for (SourceAccountingLine line : purapAccountingService.generateSummary((List<PurApItem>)document.getItems())) {
            // check to make sure the account is in the auto approve exclusion list
            Map<String, Object> autoApproveMap = new HashMap<String, Object>();
            autoApproveMap.put("chartOfAccountsCode", line.getChartOfAccountsCode());
            autoApproveMap.put("accountNumber", line.getAccountNumber());
            autoApproveMap.put("active", true);
            AutoApproveExclude autoApproveExclude = businessObjectService.findByPrimaryKey(AutoApproveExclude.class, autoApproveMap);
            if (autoApproveExclude != null) {
                if (LOG.isInfoEnabled()) {
                    LOG.info(" -- PayReq [" + document.getDocumentNumber() + "] skipped due to source accounting line "
                            + line.getSequenceNumber() + " using Chart/Account [" + line.getChartOfAccountsCode() + "-"
                            + line.getAccountNumber() + "], which is excluded in the Auto Approve Exclusions table.");
                }
                return false;
            }

            minimumAmount = getMinimumLimitAmount(negativeInvoiceApprovalLimitService.findByChart(line.getChartOfAccountsCode()), minimumAmount);
            minimumAmount = getMinimumLimitAmount(negativeInvoiceApprovalLimitService.findByChartAndAccount(line.getChartOfAccountsCode(), line.getAccountNumber()), minimumAmount);
            minimumAmount = getMinimumLimitAmount(negativeInvoiceApprovalLimitService.findByChartAndOrganization(line.getChartOfAccountsCode(), line.getOrganizationReferenceId()), minimumAmount);
        }

        // If Receiving required is set, it's not needed to check the negative payment request approval limit
        if (document.isReceivingDocumentRequiredIndicator()) {
            if (LOG.isInfoEnabled()) {
                LOG.info(" -- PayReq ["
                        + document.getDocumentNumber()
                        + "] auto-approved (ignored dollar limit) due to Receiving Document Required Indicator set to Yes.");
            }
            return true;
        }

        // If no limit was found or the default is less than the limit, the default limit is used.
        if (ObjectUtils.isNull(minimumAmount) || defaultMinimumLimit.compareTo(minimumAmount) < 0) {
            minimumAmount = defaultMinimumLimit;
        }

        // The document is eligible for auto-approval if the document total is below the limit.
        if (document.getFinancialSystemDocumentHeader().getFinancialDocumentTotalAmount().isLessThan(minimumAmount)) {
            if (LOG.isInfoEnabled()) {
                LOG.info(" -- PayReq ["
                        + document.getDocumentNumber()
                        + "] auto-approved due to document Total ["
                        + document.getFinancialSystemDocumentHeader().getFinancialDocumentTotalAmount()
                        + "] being less than "
                        + (minimumAmount == defaultMinimumLimit ? "Default Auto-Approval Limit "
                        : "Configured Auto-Approval Limit ") + "of "
                        + (minimumAmount == null ? "null" : minimumAmount.toString()) + ".");
            }
            return true;
        }

        if (LOG.isInfoEnabled()) {
            LOG.info(" -- PayReq ["
                    + document.getDocumentNumber()
                    + "] skipped due to document Total ["
                    + document.getFinancialSystemDocumentHeader().getFinancialDocumentTotalAmount()
                    + "] being greater than "
                    + (minimumAmount == defaultMinimumLimit ? "Default Auto-Approval Limit "
                    : "Configured Auto-Approval Limit ") + "of "
                    + (minimumAmount == null ? "null" : minimumAmount.toString()) + ".");
        }

        return false;
    }

    /**
     * This method iterates a collection of negative payment request approval limits and returns the minimum of a given minimum
     * amount and the least among the limits in the collection.
     *
     * @param limits        The collection of NegativeInvoiceApprovalLimit to be used in determining the minimum limit amount.
     * @param minimumAmount The amount to be compared with the collection of NegativeInvoiceApprovalLimit to determine the
     *                      minimum limit amount.
     * @return The minimum of the given minimum amount and the least among the limits in the collection.
     */
    protected KualiDecimal getMinimumLimitAmount(Collection<NegativeInvoiceApprovalLimit> limits, KualiDecimal minimumAmount) {
        for (NegativeInvoiceApprovalLimit limit : limits) {
            KualiDecimal amount = limit.getNegativeInvoiceApprovalLimitAmount();
            if (null == minimumAmount) {
                minimumAmount = amount;
            } else if (minimumAmount.isGreaterThan(amount)) {
                minimumAmount = amount;
            }
        }
        return minimumAmount;
    }

    /**
     * Retrieves a list of payment request documents with the given vendor id and invoice number.
     *
     * @param vendorHeaderGeneratedId The vendor header generated id.
     * @param vendorDetailAssignedId  The vendor detail assigned id.
     * @return List of payment request document.
     */
    @Override
    public List getInvoicesByVendorNumber(Integer vendorHeaderGeneratedId, Integer vendorDetailAssignedId) {
        LOG.debug("getActiveInvoicesByVendorNumber() started");
        return invoiceDao.getActiveInvoicesByVendorNumber(vendorHeaderGeneratedId, vendorDetailAssignedId);
    }

    /**
     * Retrieves a list of payment request documents with the given vendor id and invoice number.
     *
     * @param vendorHeaderGeneratedId The vendor header generated id.
     * @param vendorDetailAssignedId  The vendor detail assigned id.
     * @param invoiceNumber           The invoice number as entered by AP.
     * @return List of payment request document.
     */
    @Override
    public List getInvoicesByVendorNumberInvoiceNumber(Integer vendorHeaderGeneratedId, Integer vendorDetailAssignedId, String invoiceNumber) {
        LOG.debug("getActiveInvoicesByVendorNumberInvoiceNumber() started");
        return invoiceDao.getActiveInvoicesByVendorNumberInvoiceNumber(vendorHeaderGeneratedId, vendorDetailAssignedId, invoiceNumber);
    }

    /**
     * @see org.kuali.ole.module.purap.document.service.InvoiceService#invoiceDuplicateMessages(org.kuali.ole.module.purap.document.InvoiceDocument)
     */
    @Override
    public HashMap<String, String> invoiceDuplicateMessages(InvoiceDocument document) {
        HashMap<String, String> msgs;
        msgs = new HashMap<String, String>();

        Integer purchaseOrderId = document.getPurchaseOrderIdentifier();

        if (ObjectUtils.isNotNull(document.getInvoiceDate())) {
            if (purapService.isDateAYearBeforeToday(document.getInvoiceDate())) {
                msgs.put(PRQSDocumentsStrings.DUPLICATE_INVOICE_QUESTION, configurationService.getPropertyValueAsString(PurapKeyConstants.MESSAGE_INVOICE_DATE_A_YEAR_OR_MORE_PAST));
            }
        }
        PurchaseOrderDocument po = document.getPurchaseOrderDocument();

        if (po != null) {
            Integer vendorDetailAssignedId = po.getVendorDetailAssignedIdentifier();
            Integer vendorHeaderGeneratedId = po.getVendorHeaderGeneratedIdentifier();

            List<InvoiceDocument> prqss = new ArrayList();

            List<InvoiceDocument> prqssDuplicates = getInvoicesByVendorNumber(vendorHeaderGeneratedId, vendorDetailAssignedId);
            for (InvoiceDocument duplicatePRQS : prqssDuplicates) {
                if (duplicatePRQS.getInvoiceNumber().toUpperCase().equals(document.getInvoiceNumber().toUpperCase())) {
                    // found the duplicate row... so add to the prqss list...
                    prqss.add(duplicatePRQS);
                }
            }

            if (prqss.size() > 0) {
                boolean addedMessage = false;
                boolean foundCanceledPostApprove = false; // cancelled
            //    boolean foundCanceledPreApprove = false; // voided
                for (InvoiceDocument testPRQS : prqss) {
                    if (StringUtils.equals(testPRQS.getApplicationDocumentStatus(), InvoiceStatuses.APPDOC_CANCELLED_POST_AP_APPROVE)) {
                        foundCanceledPostApprove |= true;
                    } /*else if (StringUtils.equals(testPRQS.getApplicationDocumentStatus(), InvoiceStatuses.APPDOC_CANCELLED_IN_PROCESS)) {
                        foundCanceledPreApprove |= true;
                    }*/ else {
                        msgs.put(PRQSDocumentsStrings.DUPLICATE_INVOICE_QUESTION, configurationService.getPropertyValueAsString(PurapKeyConstants.MESSAGE_DUPLICATE_INVOICE));
                        addedMessage = true;
                        break;
                    }
                }
                // Custom error message for duplicates related to cancelled/voided PRQSs
                if (!addedMessage) {
                    /*if (foundCanceledPostApprove && foundCanceledPreApprove) {
                        msgs.put(PRQSDocumentsStrings.DUPLICATE_INVOICE_QUESTION, configurationService.getPropertyValueAsString(PurapKeyConstants.MESSAGE_DUPLICATE_INVOICE_CANCELLEDORVOIDED));
                    } else if (foundCanceledPreApprove) {
                        msgs.put(PRQSDocumentsStrings.DUPLICATE_INVOICE_QUESTION, configurationService.getPropertyValueAsString(PurapKeyConstants.MESSAGE_DUPLICATE_INVOICE_VOIDED));
                    } */
                    if (foundCanceledPostApprove) {
                        // messages.add("errors.duplicate.vendor.invoice.cancelled");
                        msgs.put(PRQSDocumentsStrings.DUPLICATE_INVOICE_QUESTION, configurationService.getPropertyValueAsString(PurapKeyConstants.MESSAGE_DUPLICATE_INVOICE_CANCELLED));
                    }
                }
            }

            // Check that the invoice date and invoice total amount entered are not on any existing non-cancelled PRQSs for this PO
            prqss = getInvoicesByPOIdInvoiceAmountInvoiceDate(purchaseOrderId, document.getVendorInvoiceAmount(), document.getInvoiceDate());
            if (prqss.size() > 0) {
                boolean addedMessage = false;
                boolean foundCanceledPostApprove = false; // cancelled
             //   boolean foundCanceledPreApprove = false; // voided
                msgs.put(PRQSDocumentsStrings.DUPLICATE_INVOICE_QUESTION, configurationService.getPropertyValueAsString(PurapKeyConstants.MESSAGE_DUPLICATE_INVOICE_DATE_AMOUNT));
                for (InvoiceDocument testPRQS : prqss) {
                    if (StringUtils.equalsIgnoreCase(testPRQS.getApplicationDocumentStatus(), InvoiceStatuses.APPDOC_CANCELLED_POST_AP_APPROVE)) {
                        foundCanceledPostApprove |= true;
                    } /*else if (StringUtils.equalsIgnoreCase(testPRQS.getApplicationDocumentStatus(), InvoiceStatuses.APPDOC_CANCELLED_IN_PROCESS)) {
                        foundCanceledPreApprove |= true;
                    }*/ else {
                        msgs.put(PRQSDocumentsStrings.DUPLICATE_INVOICE_QUESTION, configurationService.getPropertyValueAsString(PurapKeyConstants.MESSAGE_DUPLICATE_INVOICE_DATE_AMOUNT));
                        addedMessage = true;
                        break;
                    }
                }

                // Custom error message for duplicates related to cancelled/voided PRQSs
                if (!addedMessage) {
                   /* if (foundCanceledPostApprove && foundCanceledPreApprove) {
                        msgs.put(PRQSDocumentsStrings.DUPLICATE_INVOICE_QUESTION, configurationService.getPropertyValueAsString(PurapKeyConstants.MESSAGE_DUPLICATE_INVOICE_DATE_AMOUNT_CANCELLEDORVOIDED));
                    } else if (foundCanceledPreApprove) {
                        msgs.put(PRQSDocumentsStrings.DUPLICATE_INVOICE_QUESTION, configurationService.getPropertyValueAsString(PurapKeyConstants.MESSAGE_DUPLICATE_INVOICE_DATE_AMOUNT_VOIDED));
                        addedMessage = true;
                    }*/
                    if (foundCanceledPostApprove) {
                        msgs.put(PRQSDocumentsStrings.DUPLICATE_INVOICE_QUESTION, configurationService.getPropertyValueAsString(PurapKeyConstants.MESSAGE_DUPLICATE_INVOICE_DATE_AMOUNT_CANCELLED));
                        addedMessage = true;
                    }

                }
            }
        }
        return msgs;
    }

    /**
     * @see org.kuali.ole.module.purap.document.service.InvoiceService#getInvoiceByDocumentNumber(String)
     */
    @Override
    public InvoiceDocument getInvoiceByDocumentNumber(String documentNumber) {
        LOG.debug("getInvoiceByDocumentNumber() started");

        if (ObjectUtils.isNotNull(documentNumber)) {
            try {
                InvoiceDocument doc = (InvoiceDocument) documentService.getByDocumentHeaderId(documentNumber);
                return doc;
            } catch (WorkflowException e) {
                String errorMessage = "Error getting payment request document from document service";
                LOG.error("getInvoiceByDocumentNumber() " + errorMessage, e);
                throw new RuntimeException(errorMessage, e);
            }
        }
        return null;
    }

    /**
     * @see org.kuali.ole.module.purap.document.service.InvoiceService#getInvoiceById(Integer)
     */
    @Override
    public InvoiceDocument getInvoiceById(Integer poDocId) {
        return getInvoiceByDocumentNumber(invoiceDao.getDocumentNumberByInvoiceId(poDocId));
    }

    /**
     * @see org.kuali.ole.module.purap.document.service.InvoiceService#getInvoicesByPurchaseOrderId(Integer)
     */
    /*@Override
    public List<InvoiceDocument> getInvoicesByPurchaseOrderId(Integer poDocId) {
        List<InvoiceDocument> prqss = new ArrayList<InvoiceDocument>();
        List<String> docNumbers = invoiceDao.getDocumentNumbersByPurchaseOrderId(poDocId);
        for (String docNumber : docNumbers) {
            InvoiceDocument prqs = getInvoiceByDocumentNumber(docNumber);
            if (ObjectUtils.isNotNull(prqs)) {
                prqss.add(prqs);
            }
        }
        return prqss;
    }*/

    /**
     * @see org.kuali.ole.module.purap.document.service.InvoiceService#getInvoicesByPOIdInvoiceAmountInvoiceDate(Integer,
     *      org.kuali.rice.core.api.util.type.KualiDecimal, java.sql.Date)
     */
    @Override
    public List<InvoiceDocument> getInvoicesByPOIdInvoiceAmountInvoiceDate(Integer poId, KualiDecimal invoiceAmount, Date invoiceDate) {
        LOG.debug("getInvoicesByPOIdInvoiceAmountInvoiceDate() started");
        return invoiceDao.getActiveInvoicesByPOIdInvoiceAmountInvoiceDate(poId, invoiceAmount, invoiceDate);
    }

    /**
     * @see org.kuali.ole.module.purap.document.service.InvoiceService#isInvoiceDateAfterToday(java.sql.Date)
     */
    @Override
    public boolean isInvoiceDateAfterToday(Date invoiceDate) {
        // Check invoice date to make sure it is today or before
        Calendar now = Calendar.getInstance();
        now.set(Calendar.HOUR, 11);
        now.set(Calendar.MINUTE, 59);
        now.set(Calendar.SECOND, 59);
        now.set(Calendar.MILLISECOND, 59);
        Timestamp nowTime = new Timestamp(now.getTimeInMillis());
        Calendar invoiceDateC = Calendar.getInstance();
        invoiceDateC.setTime(invoiceDate);
        // set time to midnight
        invoiceDateC.set(Calendar.HOUR, 0);
        invoiceDateC.set(Calendar.MINUTE, 0);
        invoiceDateC.set(Calendar.SECOND, 0);
        invoiceDateC.set(Calendar.MILLISECOND, 0);
        Timestamp invoiceDateTime = new Timestamp(invoiceDateC.getTimeInMillis());
        return ((invoiceDateTime.compareTo(nowTime)) > 0);
    }

    /**
     * @see org.kuali.ole.module.purap.document.service.InvoiceService#calculatePayDate(java.sql.Date,
     *      org.kuali.ole.vnd.businessobject.PaymentTermType)
     */
    @Override
    public Date calculatePayDate(Date invoiceDate, PaymentTermType terms) {
        LOG.debug("calculatePayDate() started");
        // calculate the invoice + processed calendar
        // invoice date mandatory validations will be done at submit , in future calculate will come under submit
        if (invoiceDate == null) {
            return null;
        }
        Calendar invoicedDateCalendar = dateTimeService.getCalendar(invoiceDate);
        Calendar processedDateCalendar = dateTimeService.getCurrentCalendar();

        // add default number of days to processed

        String defaultDays = getParameter(OLEConstants.OptionalModuleNamespaces.PURCHASING_ACCOUNTS_PAYABLE, OLEConstants.InvoiceDocument.CMPNT_CD,PurapParameterConstants.PURAP_PRQS_PAY_DATE_DEFAULT_NUMBER_OF_DAYS);
        processedDateCalendar.add(Calendar.DAY_OF_MONTH, Integer.parseInt(defaultDays));

        if (ObjectUtils.isNull(terms) || StringUtils.isEmpty(terms.getVendorPaymentTermsCode())) {
            invoicedDateCalendar.add(Calendar.DAY_OF_MONTH, PurapConstants.PRQS_PAY_DATE_EMPTY_TERMS_DEFAULT_DAYS);
            return returnLaterDate(invoicedDateCalendar, processedDateCalendar);
        }

        Integer discountDueNumber = terms.getVendorDiscountDueNumber();
        Integer netDueNumber = terms.getVendorNetDueNumber();
        if (ObjectUtils.isNotNull(discountDueNumber)) {
            String discountDueTypeDescription = terms.getVendorDiscountDueTypeDescription();
            paymentTermsDateCalculation(discountDueTypeDescription, invoicedDateCalendar, discountDueNumber);
        } else if (ObjectUtils.isNotNull(netDueNumber)) {
            String netDueTypeDescription = terms.getVendorNetDueTypeDescription();
            paymentTermsDateCalculation(netDueTypeDescription, invoicedDateCalendar, netDueNumber);
        } else {
            throw new RuntimeException("Neither discount or net number were specified for this payment terms type");
        }

        // return the later date
        return returnLaterDate(invoicedDateCalendar, processedDateCalendar);
    }

    /**
     * Returns whichever date is later, the invoicedDateCalendar or the processedDateCalendar.
     *
     * @param invoicedDateCalendar  One of the dates to be used in determining which date is later.
     * @param processedDateCalendar The other date to be used in determining which date is later.
     * @return The date which is the later of the two given dates in the input parameters.
     */
    protected Date returnLaterDate(Calendar invoicedDateCalendar, Calendar processedDateCalendar) {
        if (invoicedDateCalendar.after(processedDateCalendar)) {
            return new Date(invoicedDateCalendar.getTimeInMillis());
        } else {
            return new Date(processedDateCalendar.getTimeInMillis());
        }
    }

    /**
     * Calculates the paymentTermsDate given the dueTypeDescription, invoicedDateCalendar and the dueNumber.
     *
     * @param dueTypeDescription   The due type description of the payment term.
     * @param invoicedDateCalendar The Calendar object of the invoice date.
     * @param dueNumber            Either the vendorDiscountDueNumber or the vendorDiscountDueNumber of the payment term.
     */
    protected void paymentTermsDateCalculation(String dueTypeDescription, Calendar invoicedDateCalendar, Integer dueNumber) {

        if (StringUtils.equals(dueTypeDescription, PurapConstants.PRQS_PAY_DATE_DATE)) {
            // date specified set to date in next month
            invoicedDateCalendar.add(Calendar.MONTH, 1);
            invoicedDateCalendar.set(Calendar.DAY_OF_MONTH, dueNumber.intValue());
        } else if (StringUtils.equals(PurapConstants.PRQS_PAY_DATE_DAYS, dueTypeDescription)) {
            // days specified go forward that number
            invoicedDateCalendar.add(Calendar.DAY_OF_MONTH, dueNumber.intValue());
        } else {
            // improper string
            throw new RuntimeException("missing payment terms description or not properly enterred on payment term maintenance doc");
        }
    }

    /**
     * @see org.kuali.ole.module.purap.document.service.InvoiceService#calculateInvoice(org.kuali.ole.module.purap.document.InvoiceDocument,
     *      boolean)
     */
    @Override
    public void calculateInvoice(InvoiceDocument invoice, boolean updateDiscount) {
        LOG.debug("calculateInvoice() started");

        // general calculation, i.e. for the whole prqs document
        if (ObjectUtils.isNull(invoice.getInvoicePayDate())) {
            invoice.setInvoicePayDate(calculatePayDate(invoice.getInvoiceDate(), invoice.getVendorPaymentTerms()));
        }

        distributeAccounting(invoice);

        calculateTax(invoice);

        // do proration for full order and trade in
        purapService.prorateForTradeInAndFullOrderDiscount(invoice);

        // do proration for payment terms discount
        if (updateDiscount) {
            calculateDiscount(invoice);
        }

        distributeAccounting(invoice);
    }

    public void calculateTax(InvoiceDocument purapDocument) {
        PurchaseOrderDocument pDoc = purapDocument.getPurchaseOrderDocument();
        String deliveryState = pDoc.getDeliveryStateCode();
        String deliveryPostalCode = pDoc.getBillingPostalCode();
        purapService.calculateTaxForPREQ(purapDocument,deliveryState,deliveryPostalCode);
    }


    /**
     * Calculates the discount item for this invoice.
     *
     * @param invoiceDocument The payment request document whose discount to be calculated.
     */
    protected void calculateDiscount(InvoiceDocument invoiceDocument) {
        InvoiceItem discountItem = findDiscountItem(invoiceDocument);
        // find out if we really need the discount item
        PaymentTermType pt = invoiceDocument.getVendorPaymentTerms();
        if ((pt != null) && (pt.getVendorPaymentTermsPercent() != null) && (BigDecimal.ZERO.compareTo(pt.getVendorPaymentTermsPercent()) != 0)) {
            if (discountItem == null) {
                // set discountItem and add to items
                // this is probably not the best way of doing it but should work for now if we start excluding discount from below
                // we will need to manually add
                purapService.addBelowLineItems(invoiceDocument);

                // fix up below the line items
                removeIneligibleAdditionalCharges(invoiceDocument);

                discountItem = findDiscountItem(invoiceDocument);
            }

            // Deleted the discountItem.getExtendedPrice() null and isZero
            InvoiceItem fullOrderItem = findFullOrderDiscountItem(invoiceDocument);
            KualiDecimal fullOrderAmount = KualiDecimal.ZERO;
            KualiDecimal fullOrderTaxAmount = KualiDecimal.ZERO;

            if (fullOrderItem != null) {
                fullOrderAmount = (ObjectUtils.isNotNull(fullOrderItem.getExtendedPrice())) ? fullOrderItem.getExtendedPrice() : KualiDecimal.ZERO;
                fullOrderTaxAmount = (ObjectUtils.isNotNull(fullOrderItem.getItemTaxAmount())) ? fullOrderItem.getItemTaxAmount() : KualiDecimal.ZERO;
            }
            KualiDecimal totalCost = invoiceDocument.getTotalPreTaxDollarAmountAboveLineItems().add(fullOrderAmount);
            PurApItem tradeInItem = invoiceDocument.getTradeInItem();
            if (ObjectUtils.isNotNull(tradeInItem)) {
                totalCost = totalCost.subtract(tradeInItem.getTotalAmount());
            }
            BigDecimal discountAmount = pt.getVendorPaymentTermsPercent().multiply(totalCost.bigDecimalValue()).multiply(new BigDecimal(PurapConstants.PRQS_DISCOUNT_MULT));

            // do we really need to set both, not positive, but probably won't hurt
            discountItem.setItemUnitPrice(discountAmount.setScale(4, KualiDecimal.ROUND_BEHAVIOR));
            discountItem.setExtendedPrice(new KualiDecimal(discountAmount));

            // set tax amount
            boolean salesTaxInd = parameterService.getParameterValueAsBoolean(OleParameterConstants.PURCHASING_DOCUMENT.class, PurapParameterConstants.ENABLE_SALES_TAX_IND);
            boolean useTaxIndicator = invoiceDocument.isUseTaxIndicator();

            if (salesTaxInd == true && useTaxIndicator == false) {
                KualiDecimal totalTax = invoiceDocument.getTotalTaxAmountAboveLineItems().add(fullOrderTaxAmount);
                BigDecimal discountTaxAmount = null;
                if (totalCost.isNonZero()) {
                    discountTaxAmount = discountAmount.divide(totalCost.bigDecimalValue()).multiply(totalTax.bigDecimalValue());
                } else {
                    discountTaxAmount = BigDecimal.ZERO;
                }

                discountItem.setItemTaxAmount(new KualiDecimal(discountTaxAmount.setScale(KualiDecimal.SCALE, KualiDecimal.ROUND_BEHAVIOR)));
            }

            // set document
            discountItem.setPurapDocument(invoiceDocument);
        } else { // no discount
            if (discountItem != null) {
                invoiceDocument.getItems().remove(discountItem);
            }
        }

    }


    @Override
    public void clearTax(InvoiceDocument document) {
        // remove all existing tax items added by previous calculation
        removeTaxItems(document);
        // reset values
        document.setTaxClassificationCode(null);
        document.setTaxFederalPercent(null);
        document.setTaxStatePercent(null);
        document.setTaxCountryCode(null);
        document.setTaxNQIId(null);

        document.setTaxForeignSourceIndicator(false);
        document.setTaxExemptTreatyIndicator(false);
        document.setTaxOtherExemptIndicator(false);
        document.setTaxGrossUpIndicator(false);
        document.setTaxUSAIDPerDiemIndicator(false);
        document.setTaxSpecialW4Amount(null);

    }

    /**
     * @see org.kuali.ole.module.purap.document.service.InvoiceService#calculateTaxArea(org.kuali.ole.module.purap.document.InvoiceDocument)
     */
    @Override
    public void calculateTaxArea(InvoiceDocument prqs) {
        LOG.debug("calculateTaxArea() started");

        // remove all existing tax items added by previous calculation
        removeTaxItems(prqs);

        // don't need to calculate tax items if TaxClassificationCode is N (Non_Reportable)
        if (StringUtils.equalsIgnoreCase(prqs.getTaxClassificationCode(), "N")) {
            return;
        }

        // reserve the grand total excluding any tax amount, to be used as the base to compute all tax items
        // if we don't reserve this, the pre-tax total could be changed as new tax items are added
        BigDecimal taxableAmount = prqs.getGrandPreTaxTotal().bigDecimalValue();

        // generate and add state tax gross up item and its accounting line, update total amount,
        // if gross up indicator is true and tax rate is non-zero
        if (prqs.getTaxGrossUpIndicator() && prqs.getTaxStatePercent().compareTo(new BigDecimal(0)) != 0) {
            PurApItem stateGrossItem = addTaxItem(prqs, ItemTypeCodes.ITEM_TYPE_STATE_GROSS_CODE, taxableAmount);
        }

        // generate and add state tax item and its accounting line, update total amount, if tax rate is non-zero
        if (prqs.getTaxStatePercent().compareTo(new BigDecimal(0)) != 0) {
            PurApItem stateTaxItem = addTaxItem(prqs, ItemTypeCodes.ITEM_TYPE_STATE_TAX_CODE, taxableAmount);
        }

        // generate and add federal tax gross up item and its accounting line, update total amount,
        // if gross up indicator is true and tax rate is non-zero
        if (prqs.getTaxGrossUpIndicator() && prqs.getTaxFederalPercent().compareTo(new BigDecimal(0)) != 0) {
            PurApItem federalGrossItem = addTaxItem(prqs, ItemTypeCodes.ITEM_TYPE_FEDERAL_GROSS_CODE, taxableAmount);
        }

        // generate and add federal tax item and its accounting line, update total amount, if tax rate is non-zero
        if (prqs.getTaxFederalPercent().compareTo(new BigDecimal(0)) != 0) {
            PurApItem federalTaxItem = addTaxItem(prqs, ItemTypeCodes.ITEM_TYPE_FEDERAL_TAX_CODE, taxableAmount);
        }

        // FIXME if user request to add zero tax lines and remove them after tax approval,
        // then remove the conditions above when adding the tax lines, and
        // add a branch in InvoiceDocument.processNodeChange to call PurapService.deleteUnenteredItems
    }

    /**
     * Removes all existing NRA tax items from the specified payment request.
     *
     * @param prqs The payment request from which all tax items are to be removed.
     */
    protected void removeTaxItems(InvoiceDocument prqs) {
        List<PurApItem> items = prqs.getItems();
        for (int i = 0; i < items.size(); i++) {
            PurApItem item = items.get(i);
            String code = item.getItemTypeCode();
            if (ItemTypeCodes.ITEM_TYPE_FEDERAL_TAX_CODE.equals(code) || ItemTypeCodes.ITEM_TYPE_STATE_TAX_CODE.equals(code) || ItemTypeCodes.ITEM_TYPE_FEDERAL_GROSS_CODE.equals(code) || ItemTypeCodes.ITEM_TYPE_STATE_GROSS_CODE.equals(code)) {
                items.remove(i--);
            }
        }
    }

    /**
     * Generates a NRA tax item and adds to the specified payment request, according to the specified item type code.
     *
     * @param prqs          The payment request the tax item will be added to.
     * @param itemTypeCode  The item type code for the tax item.
     * @param taxableAmount The amount to which tax is computed against.
     * @return A fully populated PurApItem instance representing NRA tax amount data for the specified payment request.
     */
    protected PurApItem addTaxItem(InvoiceDocument prqs, String itemTypeCode, BigDecimal taxableAmount) {
        PurApItem taxItem = null;

        try {
            taxItem = (PurApItem) prqs.getItemClass().newInstance();
        } catch (IllegalAccessException e) {
            throw new IllegalArgumentException("Unable to access itemClass", e);
        } catch (InstantiationException e) {
            throw new IllegalArgumentException("Unable to instantiate itemClass", e);
        }

        // add item to prqs before adding the accounting line
        taxItem.setItemTypeCode(itemTypeCode);
        prqs.addItem(taxItem);

        // generate and add tax accounting line
        PurApAccountingLine taxLine = addTaxAccountingLine(taxItem, taxableAmount);

        // set extended price amount as now it's calculated when accounting line is generated
        taxItem.setItemUnitPrice(taxLine.getAmount().bigDecimalValue());
        taxItem.setExtendedPrice(taxLine.getAmount());

        // use item type description as the item description
        ItemType itemType = new ItemType();
        itemType.setItemTypeCode(itemTypeCode);
        itemType = (ItemType) businessObjectService.retrieve(itemType);
        taxItem.setItemType(itemType);
        taxItem.setItemDescription(itemType.getItemTypeDescription());

        return taxItem;
    }

    /**
     * Generates a PurAP accounting line and adds to the specified tax item.
     *
     * @param taxItem       The specified tax item the accounting line will be associated with.
     * @param taxableAmount The amount to which tax is computed against.
     * @return A fully populated PurApAccountingLine instance for the specified tax item.
     */
    protected PurApAccountingLine addTaxAccountingLine(PurApItem taxItem, BigDecimal taxableAmount) {
        InvoiceDocument prqs = taxItem.getPurapDocument();
        PurApAccountingLine taxLine = null;

        try {
            taxLine = (PurApAccountingLine) taxItem.getAccountingLineClass().newInstance();
        } catch (IllegalAccessException e) {
            throw new IllegalArgumentException("Unable to access sourceAccountingLineClass", e);
        } catch (InstantiationException e) {
            throw new IllegalArgumentException("Unable to instantiate sourceAccountingLineClass", e);
        }

        // tax item type indicators
        boolean isFederalTax = ItemTypeCodes.ITEM_TYPE_FEDERAL_TAX_CODE.equals(taxItem.getItemTypeCode());
        boolean isFederalGross = ItemTypeCodes.ITEM_TYPE_FEDERAL_GROSS_CODE.equals(taxItem.getItemTypeCode());
        boolean isStateTax = ItemTypeCodes.ITEM_TYPE_STATE_TAX_CODE.equals(taxItem.getItemTypeCode());
        boolean isStateGross = ItemTypeCodes.ITEM_TYPE_STATE_GROSS_CODE.equals(taxItem.getItemTypeCode());
        boolean isFederal = isFederalTax || isFederalGross; // true for federal tax/gross; false for state tax/gross
        boolean isGross = isFederalGross || isStateGross; // true for federal/state gross, false for federal/state tax

        // obtain accounting line info according to tax item type code
        String taxChart = null;
        String taxAccount = null;
        String taxObjectCode = null;

        if (isGross) {
            // for gross up tax items, use prqs's first item's first accounting line, which shall exist at this point
            AccountingLine line1 = prqs.getFirstAccount();
            taxChart = line1.getChartOfAccountsCode();
            taxAccount = line1.getAccountNumber();
            taxObjectCode = line1.getFinancialObjectCode();
        } else if (isFederalTax) {
            // for federal tax item, get chart, account, object code info from parameters
            taxChart = getParameter(OLEConstants.OptionalModuleNamespaces.PURCHASING_ACCOUNTS_PAYABLE, OLEConstants.InvoiceDocument.CMPNT_CD,NRATaxParameters.FEDERAL_TAX_PARM_PREFIX + NRATaxParameters.TAX_PARM_CHART_SUFFIX);
            taxAccount = getParameter(OLEConstants.OptionalModuleNamespaces.PURCHASING_ACCOUNTS_PAYABLE, OLEConstants.InvoiceDocument.CMPNT_CD,NRATaxParameters.FEDERAL_TAX_PARM_PREFIX + NRATaxParameters.TAX_PARM_ACCOUNT_SUFFIX);
            taxObjectCode = parameterService.getSubParameterValueAsString(InvoiceDocument.class, NRATaxParameters.FEDERAL_TAX_PARM_PREFIX + NRATaxParameters.TAX_PARM_OBJECT_BY_INCOME_CLASS_SUFFIX, prqs.getTaxClassificationCode());
            if (StringUtils.isBlank(taxChart) || StringUtils.isBlank(taxAccount) || StringUtils.isBlank(taxObjectCode)) {
                LOG.error("Unable to retrieve federal tax parameters.");
                throw new RuntimeException("Unable to retrieve federal tax parameters.");
            }
        } else if (isStateTax) {
            // for state tax item, get chart, account, object code info from parameters
            taxChart = getParameter(OLEConstants.OptionalModuleNamespaces.PURCHASING_ACCOUNTS_PAYABLE, OLEConstants.InvoiceDocument.CMPNT_CD,NRATaxParameters.STATE_TAX_PARM_PREFIX + NRATaxParameters.TAX_PARM_CHART_SUFFIX);
            taxAccount = getParameter(OLEConstants.OptionalModuleNamespaces.PURCHASING_ACCOUNTS_PAYABLE, OLEConstants.InvoiceDocument.CMPNT_CD,NRATaxParameters.STATE_TAX_PARM_PREFIX + NRATaxParameters.TAX_PARM_ACCOUNT_SUFFIX);
            taxObjectCode = parameterService.getSubParameterValueAsString(InvoiceDocument.class, NRATaxParameters.STATE_TAX_PARM_PREFIX + NRATaxParameters.TAX_PARM_OBJECT_BY_INCOME_CLASS_SUFFIX, prqs.getTaxClassificationCode());
            if (StringUtils.isBlank(taxChart) || StringUtils.isBlank(taxAccount) || StringUtils.isBlank(taxObjectCode)) {
                LOG.error("Unable to retrieve state tax parameters.");
                throw new RuntimeException("Unable to retrieve state tax parameters.");
            }
        }

        // calculate tax amount according to gross up indicator and federal/state tax type
        /*
         * The formula of tax and gross up amount are as follows: if (not gross up) gross not existing taxFederal/State = - amount *
         * rateFederal/State otherwise gross up grossFederal/State = amount * rateFederal/State / (1 - rateFederal - rateState) tax
         * = - gross
         */

        // pick federal/state tax rate
        BigDecimal taxPercentFederal = prqs.getTaxFederalPercent();
        BigDecimal taxPercentState = prqs.getTaxStatePercent();
        BigDecimal taxPercent = isFederal ? taxPercentFederal : taxPercentState;

        // divider value according to gross up or not
        BigDecimal taxDivider = new BigDecimal(100);
        if (prqs.getTaxGrossUpIndicator()) {
            taxDivider = taxDivider.subtract(taxPercentFederal.add(taxPercentState));
        }

        // tax = amount * rate / divider
        BigDecimal taxAmount = taxableAmount.multiply(taxPercent);
        taxAmount = taxAmount.divide(taxDivider, 5, BigDecimal.ROUND_HALF_UP);

        // tax is always negative, since it reduces the total amount; while gross up is always the positive of tax
        if (!isGross) {
            taxAmount = taxAmount.negate();
        }

        // populate necessary accounting line fields
        taxLine.setDocumentNumber(prqs.getDocumentNumber());
        taxLine.setSequenceNumber(prqs.getNextSourceLineNumber());
        taxLine.setChartOfAccountsCode(taxChart);
        taxLine.setAccountNumber(taxAccount);
        taxLine.setFinancialObjectCode(taxObjectCode);
        taxLine.setAmount(new KualiDecimal(taxAmount));

        // add the accounting line to the item
        taxLine.setItemIdentifier(taxItem.getItemIdentifier());
        taxLine.setPurapItem(taxItem);
        taxItem.getSourceAccountingLines().add(taxLine);

        return taxLine;
    }

    /**
     * Finds the discount item of the payment request document.
     *
     * @param invoiceDocument The payment request document to be used to find the discount item.
     * @return The discount item if it exists.
     */
    protected InvoiceItem findDiscountItem(InvoiceDocument invoiceDocument) {
        InvoiceItem discountItem = null;
        for (InvoiceItem prqsItem : (List<InvoiceItem>) invoiceDocument.getItems()) {
            if (StringUtils.equals(prqsItem.getItemTypeCode(), ItemTypeCodes.ITEM_TYPE_PMT_TERMS_DISCOUNT_CODE)) {
                discountItem = prqsItem;
                break;
            }
        }
        return discountItem;
    }

    /**
     * Finds the full order discount item of the payment request document.
     *
     * @param invoiceDocument The payment request document to be used to find the full order discount item.
     * @return The discount item if it exists.
     */
    protected InvoiceItem findFullOrderDiscountItem(InvoiceDocument invoiceDocument) {
        InvoiceItem discountItem = null;
        for (InvoiceItem prqsItem : (List<InvoiceItem>) invoiceDocument.getItems()) {
            if (StringUtils.equals(prqsItem.getItemTypeCode(), ItemTypeCodes.ITEM_TYPE_ORDER_DISCOUNT_CODE)) {
                discountItem = prqsItem;
                break;
            }
        }
        return discountItem;
    }

    /**
     * Distributes accounts for a payment request document.
     *
     * @param invoiceDocument
     */
    protected void distributeAccounting(InvoiceDocument invoiceDocument) {
        // update the account amounts before doing any distribution
        purapAccountingService.updateAccountAmounts(invoiceDocument);

        String accountDistributionMethod = invoiceDocument.getAccountDistributionMethod();

        for (InvoiceItem item : (List<InvoiceItem>) invoiceDocument.getItems()) {
            KualiDecimal totalAmount = KualiDecimal.ZERO;
            List<PurApAccountingLine> distributedAccounts = null;
            List<SourceAccountingLine> summaryAccounts = null;
            Set excludedItemTypeCodes = new HashSet();
            excludedItemTypeCodes.add(ItemTypeCodes.ITEM_TYPE_PMT_TERMS_DISCOUNT_CODE);

            // skip above the line
            if (item.getItemType().isLineItemIndicator()) {
                continue;
            }

            if ((item.getSourceAccountingLines().isEmpty()) && (ObjectUtils.isNotNull(item.getExtendedPrice())) && (KualiDecimal.ZERO.compareTo(item.getExtendedPrice()) != 0)) {
                if ((StringUtils.equals(ItemTypeCodes.ITEM_TYPE_PMT_TERMS_DISCOUNT_CODE, item.getItemType().getItemTypeCode())) && (invoiceDocument.getGrandTotal() != null) && ((KualiDecimal.ZERO.compareTo(invoiceDocument.getGrandTotal()) != 0))) {

                    // No discount is applied to other item types other than item line
                    // See KFSMI-5210 for details

                    // total amount should be the line item total, not the grand total
                    totalAmount = invoiceDocument.getLineItemTotal();

                    // prorate item line accounts only
                    Set includedItemTypeCodes = new HashSet();
                    includedItemTypeCodes.add(ItemTypeCodes.ITEM_TYPE_ITEM_CODE);
                    includedItemTypeCodes.add(ItemTypeCodes.ITEM_TYPE_SERVICE_CODE);

                    summaryAccounts = purapAccountingService.generateSummaryIncludeItemTypesAndNoZeroTotals(invoiceDocument.getItems(), includedItemTypeCodes);
                    //if summaryAccount is empty then do not call generateAccountDistributionForProration as
                    //there is a check in that method to throw NPE if accounts percents == 0..
                    //OLEMI-8487
                    if (summaryAccounts != null) {
                        distributedAccounts = purapAccountingService.generateAccountDistributionForProration(summaryAccounts, totalAmount, PurapConstants.PRORATION_SCALE, InvoiceAccount.class);
                    }
                    // OLE-3405 : disabling the distribution method choice
//                    if (PurapConstants.AccountDistributionMethodCodes.SEQUENTIAL_CODE.equalsIgnoreCase(accountDistributionMethod)) {
//                        purapAccountingService.updatePreqAccountAmountsWithTotal(distributedAccounts, item.getTotalAmount());
//
//                    } else {
//
//                        boolean rulePassed = true;
//                        // check any business rules
//                        rulePassed &= kualiRuleService.applyRules(new PurchasingAccountsPayableItemPreCalculateEvent(invoiceDocument, item));
//
//                        if (rulePassed) {
//                            purapAccountingService.updatePreqProporationalAccountAmountsWithTotal(distributedAccounts, item.getTotalAmount());
//                        }
//                    }
                } else {
                    PurchaseOrderItem poi = item.getPurchaseOrderItem();
                    if ((poi != null) && (poi.getSourceAccountingLines() != null) && (!(poi.getSourceAccountingLines().isEmpty())) && (poi.getExtendedPrice() != null) && ((KualiDecimal.ZERO.compareTo(poi.getExtendedPrice())) != 0)) {
                        // use accounts from purchase order item matching this item
                        // account list of current item is already empty
                        item.generateAccountListFromPoItemAccounts(poi.getSourceAccountingLines());
                    } else {
                        totalAmount = invoiceDocument.getPurchaseOrderDocument().getTotalDollarAmountAboveLineItems();
                        purapAccountingService.updateAccountAmounts(invoiceDocument.getPurchaseOrderDocument());
                        summaryAccounts = purapAccountingService.generateSummary(PurApItemUtils.getAboveTheLineOnly(invoiceDocument.getPurchaseOrderDocument().getItems()));
                        distributedAccounts = purapAccountingService.generateAccountDistributionForProration(summaryAccounts, totalAmount, new Integer("6"), InvoiceAccount.class);
                    }
                }
                if (CollectionUtils.isNotEmpty(distributedAccounts) && CollectionUtils.isEmpty(item.getSourceAccountingLines())) {
                    item.setSourceAccountingLines(distributedAccounts);
                }
            }
            // update the item
            purapAccountingService.updateItemAccountAmounts(item);
        }

        // update again now that distribute is finished. (Note: we may not need this anymore now that I added updateItem line above
        //leave the call below since we need to this when sequential method is used on the document.
        purapAccountingService.updateAccountAmounts(invoiceDocument);
    }

    /**
     * @see org.kuali.ole.module.purap.document.service.InvoiceService#addHoldOnInvoice(org.kuali.ole.module.purap.document.InvoiceDocument,
     *      String)
     */
    @Override
    public InvoiceDocument addHoldOnInvoice(InvoiceDocument document, String note) throws Exception {
        // save the note
        Note noteObj = documentService.createNoteFromDocument(document, note);
        document.addNote(noteObj);
        noteService.save(noteObj);

        // MSU Contribution OLEMI-8456 DT 3822 OLECNTRB-959

        document.setHoldIndicator(true);
        document.setLastActionPerformedByPersonId(GlobalVariables.getUserSession().getPerson().getPrincipalId());
        purapService.saveDocumentNoValidation(document);

        return document;
    }

    /**
     * @see org.kuali.ole.module.purap.document.service.InvoiceService#removeHoldOnInvoice(org.kuali.ole.module.purap.document.InvoiceDocument, String)
     */
    @Override
    public InvoiceDocument removeHoldOnInvoice(InvoiceDocument document, String note) throws Exception {
        // save the note
        Note noteObj = documentService.createNoteFromDocument(document, note);
        document.addNote(noteObj);
        noteService.save(noteObj);

        // MSU Contribution OLEMI-8456 DT 3822 OLECNTRB-959

        document.setHoldIndicator(false);
        document.setLastActionPerformedByPersonId(null);
        purapService.saveDocumentNoValidation(document);

        return document;
    }

    /**
     * @see org.kuali.ole.module.purap.document.service.InvoiceService#addHoldOnInvoice(org.kuali.ole.module.purap.document.InvoiceDocument,
     *      String)
     */
    @Override
    public void requestCancelOnInvoice(InvoiceDocument document, String note) throws Exception {
        // save the note
        Note noteObj = documentService.createNoteFromDocument(document, note);
        document.addNote(noteObj);
        noteService.save(noteObj);

        // MSU Contribution OLEMI-8456 DT 3822 OLECNTRB-959
        document.setInvoiceCancelIndicator(true);
        document.setLastActionPerformedByPersonId(GlobalVariables.getUserSession().getPerson().getPrincipalId());
        document.setAccountsPayableRequestCancelIdentifier(GlobalVariables.getUserSession().getPerson().getPrincipalId());
        purapService.saveDocumentNoValidation(document);
    }

    /**
     * @see org.kuali.ole.module.purap.document.service.InvoiceService#removeHoldOnInvoice(org.kuali.ole.module.purap.document.InvoiceDocument, String)
     */
    @Override
    public void removeRequestCancelOnInvoice(InvoiceDocument document, String note) throws Exception {
        // save the note
        Note noteObj = documentService.createNoteFromDocument(document, note);
        document.addNote(noteObj);
        noteService.save(noteObj);

        clearRequestCancelFields(document);

        purapService.saveDocumentNoValidation(document);

    }

    /**
     * Clears the request cancel fields.
     *
     * @param document The payment request document whose request cancel fields to be cleared.
     */
    protected void clearRequestCancelFields(InvoiceDocument document) {
        document.setInvoiceCancelIndicator(false);
        document.setLastActionPerformedByPersonId(null);
        document.setAccountsPayableRequestCancelIdentifier(null);
    }

    /**
     * @see org.kuali.ole.module.purap.document.service.InvoiceService#isExtracted(org.kuali.ole.module.purap.document.InvoiceDocument)
     */
    @Override
    public boolean isExtracted(InvoiceDocument document) {
        return (ObjectUtils.isNull(document.getExtractedTimestamp()) ? false : true);
    }

    protected boolean isBeingAdHocRouted(InvoiceDocument document) {
        return financialSystemWorkflowHelperService.isAdhocApprovalRequestedForPrincipal(document.getDocumentHeader().getWorkflowDocument(), GlobalVariables.getUserSession().getPrincipalId());
    }

    /**
     * @see org.kuali.ole.module.purap.document.service.InvoiceService#cancelExtractedInvoice(org.kuali.ole.module.purap.document.InvoiceDocument,
     *      String)
     */
    @Override
    public void cancelExtractedInvoice(InvoiceDocument invoice, String note) {
        LOG.debug("cancelExtractedInvoice() started");
        if (InvoiceStatuses.CANCELLED_STATUSES.contains(invoice.getApplicationDocumentStatus())) {
            LOG.debug("cancelExtractedInvoice() ended");
            return;
        }

        try {
            Note cancelNote = documentService.createNoteFromDocument(invoice, note);
            invoice.addNote(cancelNote);
            noteService.save(cancelNote);
        } catch (Exception e) {
            throw new RuntimeException(PurapConstants.REQ_UNABLE_TO_CREATE_NOTE, e);
        }

        // cancel extracted should not reopen PO
        invoice.setReopenPurchaseOrderIndicator(false);

        getAccountsPayableService().cancelAccountsPayableDocument(invoice, ""); // Performs save, so
        // no explicit save
        // is necessary
        if (LOG.isDebugEnabled()) {
            LOG.debug("cancelExtractedInvoice() PRQS " + invoice.getPurapDocumentIdentifier() + " Cancelled Without Workflow");
            LOG.debug("cancelExtractedInvoice() ended");
        }
    }

    /**
     * @see org.kuali.ole.module.purap.document.service.InvoiceService#resetExtractedInvoice(org.kuali.ole.module.purap.document.InvoiceDocument,
     *      String)
     */
    @Override
    public void resetExtractedInvoice(InvoiceDocument invoice, String note) {
        LOG.debug("resetExtractedInvoice() started");
        if (InvoiceStatuses.CANCELLED_STATUSES.contains(invoice.getApplicationDocumentStatus())) {
            LOG.debug("resetExtractedInvoice() ended");
            return;
        }
        invoice.setExtractedTimestamp(null);
        invoice.setPaymentPaidTimestamp(null);
        String noteText = "This Invoice is being reset for extraction by PDP " + note;
        try {
            Note resetNote = documentService.createNoteFromDocument(invoice, noteText);
            invoice.addNote(resetNote);
            noteService.save(resetNote);
        } catch (Exception e) {
            throw new RuntimeException(PurapConstants.REQ_UNABLE_TO_CREATE_NOTE + " " + e);
        }
        purapService.saveDocumentNoValidation(invoice);
        if (LOG.isDebugEnabled()) {
            LOG.debug("resetExtractedInvoice() PRQS " + invoice.getPurapDocumentIdentifier() + " Reset from Extracted status");
        }
    }

    /**
     * @see org.kuali.ole.module.purap.document.service.InvoiceService#populateInvoice(org.kuali.ole.module.purap.document.InvoiceDocument)
     */
    /*@Override
    public void populateInvoice(InvoiceDocument invoiceDocument) {

        PurchaseOrderDocument purchaseOrderDocument = invoiceDocument.getPurchaseOrderDocument();

        // make a call to search for expired/closed accounts
        HashMap<String, ExpiredOrClosedAccountEntry> expiredOrClosedAccountList = getAccountsPayableService().getExpiredOrClosedAccountList(invoiceDocument);

        invoiceDocument.populateInvoiceFromPurchaseOrders(invoiceDocument, expiredOrClosedAccountList);

       // invoiceDocument.getDocumentHeader().setDocumentDescription(createPreqDocumentDescription(invoiceDocument.getPurchaseOrderIdentifier(), invoiceDocument.getVendorName()));

        // write a note for expired/closed accounts if any exist and add a message stating there were expired/closed accounts at the
        // top of the document
        getAccountsPayableService().generateExpiredOrClosedAccountNote(invoiceDocument, expiredOrClosedAccountList);

        // set indicator so a message is displayed for accounts that were replaced due to expired/closed status
        if (!expiredOrClosedAccountList.isEmpty()) {
            invoiceDocument.setContinuationAccountIndicator(true);
        }

        // add discount item
        calculateDiscount(invoiceDocument);
        // distribute accounts (i.e. proration)
        distributeAccounting(invoiceDocument);

        // set bank code to default bank code in the system parameter
        Bank defaultBank = bankService.getDefaultBankByDocType(invoiceDocument.getClass());
        if (defaultBank != null) {
            invoiceDocument.setBankCode(defaultBank.getBankCode());
            invoiceDocument.setBank(defaultBank);
        }
    }*/

    /**
     * @see org.kuali.ole.module.purap.document.service.InvoiceService#createPreqDocumentDescription(Integer,
     *      String)
     */
    @Override
    public String createPreqDocumentDescription(Integer purchaseOrderIdentifier, String vendorName) {
        StringBuffer descr = new StringBuffer("");
        descr.append("PO: ");
        descr.append(purchaseOrderIdentifier);
        descr.append(" Vendor: ");
        descr.append(StringUtils.trimToEmpty(vendorName));

        int noteTextMaxLength = dataDictionaryService.getAttributeMaxLength(DocumentHeader.class, KRADPropertyConstants.DOCUMENT_DESCRIPTION).intValue();
        if (noteTextMaxLength >= descr.length()) {
            return descr.toString();
        } else {
            return descr.toString().substring(0, noteTextMaxLength);
        }
    }

    /**
     * @see org.kuali.ole.module.purap.document.service.InvoiceService#populateAndSaveInvoice(org.kuali.ole.module.purap.document.InvoiceDocument)
     */
    @Override
    public void populateAndSaveInvoice(InvoiceDocument prqs) throws WorkflowException {
        try {
            prqs.updateAndSaveAppDocStatus(InvoiceStatuses.APPDOC_IN_PROCESS);
            documentService.saveDocument(prqs, AttributedContinuePurapEvent.class);
        } catch (ValidationException ve) {
            prqs.updateAndSaveAppDocStatus(InvoiceStatuses.APPDOC_INITIATE);
        } catch (WorkflowException we) {
            prqs.updateAndSaveAppDocStatus(InvoiceStatuses.APPDOC_INITIATE);

            String errorMsg = "Error saving document # " + prqs.getDocumentHeader().getDocumentNumber() + " " + we.getMessage();
            LOG.error(errorMsg, we);
            throw new RuntimeException(errorMsg, we);
        }
    }

    /**
     * If the full document entry has been completed and the status of the related purchase order document is closed, return true,
     * otherwise return false.
     *
     * @param apDoc The AccountsPayableDocument to be determined whether its purchase order should be reversed.
     * @return boolean true if the purchase order should be reversed.
     * @see org.kuali.ole.module.purap.document.service.AccountsPayableDocumentSpecificService#shouldPurchaseOrderBeReversed
     *      (org.kuali.ole.module.purap.document.AccountsPayableDocument)
     */
    @Override
    public boolean shouldPurchaseOrderBeReversed(AccountsPayableDocument apDoc) {
        PurchaseOrderDocument po = apDoc.getPurchaseOrderDocument();
        if (ObjectUtils.isNull(po)) {
            throw new RuntimeException("po should never be null on PRQS");
        }
        // if past full entry and already closed return true
        if (purapService.isFullDocumentEntryCompleted(apDoc) && StringUtils.equalsIgnoreCase(PurapConstants.PurchaseOrderStatuses.APPDOC_CLOSED, po.getApplicationDocumentStatus())) {
            return true;
        }
        return false;
    }

    /**
     * @see org.kuali.ole.module.purap.document.service.AccountsPayableDocumentSpecificService#getPersonForCancel(org.kuali.ole.module.purap.document.AccountsPayableDocument)
     */
    @Override
    public Person getPersonForCancel(AccountsPayableDocument apDoc) {
        InvoiceDocument prqsDoc = (InvoiceDocument) apDoc;
        Person user = null;
        if (prqsDoc.isInvoiceCancelIndicator()) {
            user = prqsDoc.getLastActionPerformedByUser();
        }
        return user;
    }

    /**
     * @see org.kuali.ole.module.purap.document.service.AccountsPayableDocumentSpecificService#takePurchaseOrderCancelAction(org.kuali.ole.module.purap.document.AccountsPayableDocument)
     */
    @Override
    public void takePurchaseOrderCancelAction(AccountsPayableDocument apDoc) {
        InvoiceDocument prqsDocument = (InvoiceDocument) apDoc;
        if (prqsDocument.isReopenPurchaseOrderIndicator()) {
            String docType = PurapConstants.PurchaseOrderDocTypes.PURCHASE_ORDER_REOPEN_DOCUMENT;
            purchaseOrderService.createAndRoutePotentialChangeDocument(prqsDocument.getPurchaseOrderDocument(), docType, "reopened by Credit Memo " + apDoc.getPurapDocumentIdentifier() + "cancel", new ArrayList(), PurapConstants.PurchaseOrderStatuses.APPDOC_PENDING_REOPEN);
        }
    }

    /**
     * @see org.kuali.ole.module.purap.document.service.AccountsPayableDocumentSpecificService#updateStatusByNode(String,
     *      org.kuali.ole.module.purap.document.AccountsPayableDocument)
     */
    @Override
    public String updateStatusByNode(String currentNodeName, AccountsPayableDocument apDoc) {
        return updateStatusByNode(currentNodeName, (InvoiceDocument) apDoc);
    }

    /**
     * Updates the status of the payment request document.
     *
     * @param currentNodeName The current node name.
     * @param prqsDoc         The payment request document whose status to be updated.
     * @return The canceled status code.
     */
    protected String updateStatusByNode(String currentNodeName, InvoiceDocument prqsDoc) {
        // remove request cancel if necessary
        clearRequestCancelFields(prqsDoc);

        // update the status on the document

        String cancelledStatus = "";
        if (StringUtils.isEmpty(currentNodeName)) {
            // if empty probably not coming from workflow
            cancelledStatus = InvoiceStatuses.APPDOC_CANCELLED_POST_AP_APPROVE;
        } else {
            cancelledStatus = InvoiceStatuses.getInvoiceAppDocDisapproveStatuses().get(currentNodeName);
        }

        if (StringUtils.isNotBlank(cancelledStatus)) {
            try {
                prqsDoc.updateAndSaveAppDocStatus(cancelledStatus);
            } catch (WorkflowException we) {
                throw new RuntimeException("Unable to save the route status data for document: " + prqsDoc.getDocumentNumber(), we);
            }
            purapService.saveDocumentNoValidation(prqsDoc);
        } else {
            logAndThrowRuntimeException("No status found to set for document being disapproved in node '" + currentNodeName + "'");
        }
        return cancelledStatus;
    }

    /**
     * @see org.kuali.ole.module.purap.document.service.InvoiceService#markPaid(org.kuali.ole.module.purap.document.InvoiceDocument,
     *      java.sql.Date)
     */
    @Override
    public void markPaid(InvoiceDocument pr, Date processDate) {
        LOG.debug("markPaid() started");

        pr.setPaymentPaidTimestamp(new Timestamp(processDate.getTime()));
        purapService.saveDocumentNoValidation(pr);
    }

    /**
     * @see org.kuali.ole.module.purap.document.service.InvoiceService#hasDiscountItem(org.kuali.ole.module.purap.document.InvoiceDocument)
     */
    @Override
    public boolean hasDiscountItem(InvoiceDocument prqs) {
        return ObjectUtils.isNotNull(findDiscountItem(prqs));
    }

    /**
     * @see org.kuali.ole.module.purap.document.service.AccountsPayableDocumentSpecificService#poItemEligibleForAp(org.kuali.ole.module.purap.document.AccountsPayableDocument,
     *      org.kuali.ole.module.purap.businessobject.PurchaseOrderItem)
     */
    @Override
    public boolean poItemEligibleForAp(AccountsPayableDocument apDoc, PurchaseOrderItem poi) {
        if (ObjectUtils.isNull(poi)) {
            throw new RuntimeException("item null in purchaseOrderItemEligibleForPayment ... this should never happen");
        }
        // if the po item is not active... skip it
        if (!poi.isItemActiveIndicator()) {
            return false;
        }

        ItemType poiType = poi.getItemType();
        if (ObjectUtils.isNull(poiType)) {
            return false;
        }

        if (poiType.isQuantityBasedGeneralLedgerIndicator()) {
            if (poi.getItemQuantity().isGreaterThan(poi.getItemInvoicedTotalQuantity())) {
                return true;
            }
            return false;
        } else { // not quantity based
            // As long as it contains a number (whether it's 0, negative or positive number), we'll
            // have to return true. This is so that the OutstandingEncumberedAmount and the
            // Original Amount from PO column would appear on the page for Trade In.
            if (poi.getItemOutstandingEncumberedAmount() != null) {
                return true;
            }
            return false;
        }
    }

    @Override
    public void removeIneligibleAdditionalCharges(InvoiceDocument document) {

        List<InvoiceItem> itemsToRemove = new ArrayList<InvoiceItem>();

        for (InvoiceItem item : (List<InvoiceItem>) document.getItems()) {

            // if no extended price and its an order discount or trade in, remove
            if (ObjectUtils.isNull(item.getPurchaseOrderItemUnitPrice()) && (ItemTypeCodes.ITEM_TYPE_ORDER_DISCOUNT_CODE.equals(item.getItemTypeCode()) || ItemTypeCodes.ITEM_TYPE_TRADE_IN_CODE.equals(item.getItemTypeCode()))) {
                itemsToRemove.add(item);
                continue;
            }

            // if a payment terms discount exists but not set on teh doc, remove
            if (StringUtils.equals(item.getItemTypeCode(), ItemTypeCodes.ITEM_TYPE_PMT_TERMS_DISCOUNT_CODE)) {
                PaymentTermType pt = document.getVendorPaymentTerms();
                if ((pt != null) && (pt.getVendorPaymentTermsPercent() != null) && (BigDecimal.ZERO.compareTo(pt.getVendorPaymentTermsPercent()) != 0)) {
                    // discount ok
                } else {
                    // remove discount
                    itemsToRemove.add(item);
                }
                continue;
            }

        }

        // remove items marked for removal
        for (InvoiceItem item : itemsToRemove) {
            document.getItems().remove(item);
        }
    }

    @Override
    public void changeVendor(InvoiceDocument prqs, Integer headerId, Integer detailId) {

        VendorDetail primaryVendor = vendorService.getVendorDetail(prqs.getOriginalVendorHeaderGeneratedIdentifier(), prqs.getOriginalVendorDetailAssignedIdentifier());

        if (primaryVendor == null) {
            LOG.error("useAlternateVendor() primaryVendorDetail from database for header id " + headerId + " and detail id " + detailId + "is null");
            throw new PurError("AlternateVendor: VendorDetail from database for header id " + headerId + " and detail id " + detailId + "is null");
        }

        // set vendor detail
        VendorDetail vd = vendorService.getVendorDetail(headerId, detailId);
        if (vd == null) {
            LOG.error("changeVendor() VendorDetail from database for header id " + headerId + " and detail id " + detailId + "is null");
            throw new PurError("changeVendor: VendorDetail from database for header id " + headerId + " and detail id " + detailId + "is null");
        }
        prqs.setVendorDetail(vd);
        prqs.setVendorName(vd.getVendorName());
        prqs.setVendorNumber(vd.getVendorNumber());
        prqs.setVendorHeaderGeneratedIdentifier(vd.getVendorHeaderGeneratedIdentifier());
        prqs.setVendorDetailAssignedIdentifier(vd.getVendorDetailAssignedIdentifier());
        prqs.setVendorPaymentTermsCode(vd.getVendorPaymentTermsCode());
        prqs.setVendorShippingPaymentTermsCode(vd.getVendorShippingPaymentTermsCode());
        prqs.setVendorShippingTitleCode(vd.getVendorShippingTitleCode());
        prqs.refreshReferenceObject("vendorPaymentTerms");
        prqs.refreshReferenceObject("vendorShippingPaymentTerms");

        // Set vendor address
        String deliveryCampus = prqs.getPurchaseOrderDocument().getDeliveryCampusCode();
        VendorAddress va = vendorService.getVendorDefaultAddress(headerId, detailId, VendorConstants.AddressTypes.REMIT, deliveryCampus);
        if (va == null) {
            va = vendorService.getVendorDefaultAddress(headerId, detailId, VendorConstants.AddressTypes.PURCHASE_ORDER, deliveryCampus);
        }
        if (va == null) {
            LOG.error("changeVendor() VendorAddress from database for header id " + headerId + " and detail id " + detailId + "is null");
            throw new PurError("changeVendor  VendorAddress from database for header id " + headerId + " and detail id " + detailId + "is null");
        }

        if (prqs != null) {
            setVendorAddress(va, prqs);
        } else {
            LOG.error("changeVendor(): Null link back to the Purchase Order.");
            throw new PurError("Null link back to the Purchase Order.");
        }

        // change document description
        //    prqs.getDocumentHeader().setDocumentDescription(createPreqDocumentDescription(prqs.getPurchaseOrderIdentifier(), prqs.getVendorName()));
    }

    /**
     * Set the Vendor address of the given ID.
     *
     * @param va  ID of the address to set
     * @param inv Invoice to set in
     * @return New Invoice to use
     */
    protected void setVendorAddress(VendorAddress va, InvoiceDocument inv) {

        if (va != null) {
            inv.setVendorAddressGeneratedIdentifier(va.getVendorAddressGeneratedIdentifier());
            inv.setVendorAddressInternationalProvinceName(va.getVendorAddressInternationalProvinceName());
            inv.setVendorLine1Address(va.getVendorLine1Address());
            inv.setVendorLine2Address(va.getVendorLine2Address());
            inv.setVendorCityName(va.getVendorCityName());
            inv.setVendorStateCode(va.getVendorStateCode());
            inv.setVendorPostalCode(va.getVendorZipCode());
            inv.setVendorCountryCode(va.getVendorCountryCode());
        }

    }

    /**
     * Records the specified error message into the Log file and throws a runtime exception.
     *
     * @param errorMessage the error message to be logged.
     */
    protected void logAndThrowRuntimeException(String errorMessage) {
        this.logAndThrowRuntimeException(errorMessage, null);
    }

    /**
     * Records the specified error message into the Log file and throws the specified runtime exception.
     *
     * @param errorMessage the specified error message.
     * @param e            the specified runtime exception.
     */
    protected void logAndThrowRuntimeException(String errorMessage, Exception e) {
        if (ObjectUtils.isNotNull(e)) {
            LOG.error(errorMessage, e);
            throw new RuntimeException(errorMessage, e);
        } else {
            LOG.error(errorMessage);
            throw new RuntimeException(errorMessage);
        }
    }

    /**
     * The given document here actually needs to be a Invoice.
     *
     * @see org.kuali.ole.module.purap.document.service.AccountsPayableDocumentSpecificService#generateGLEntriesCreateAccountsPayableDocument(org.kuali.ole.module.purap.document.AccountsPayableDocument)
     */
    @Override
    public void generateGLEntriesCreateAccountsPayableDocument(AccountsPayableDocument apDocument) {
        //  if(apDocument.)

        InvoiceDocument invoice = (InvoiceDocument) apDocument;
        //System.out.println(" generateGLEntriesCreateAccountsPayableDocument apDocument" + ((InvoiceDocument) apDocument).getDocumentType());
        // JHK: this is not being injected because it would cause a circular reference in the Spring definitions
     //   SpringContext.getBean(PurapGeneralLedgerService.class).generateEntriesCreateInvoice(invoice);
    }

    /**
     * @see org.kuali.ole.module.purap.document.service.InvoiceService#hasActiveInvoicesForPurchaseOrder(Integer)
     */
    @Override
    public boolean hasActiveInvoicesForPurchaseOrder(Integer purchaseOrderIdentifier) {

        boolean hasActivePreqs = false;
        List<String> docNumbers = null;
        WorkflowDocument workflowDocument = null;

        docNumbers = invoiceDao.getActiveInvoiceDocumentNumbersForPurchaseOrder(purchaseOrderIdentifier);
        docNumbers = filterInvoiceByAppDocStatus(docNumbers, InvoiceStatuses.STATUSES_POTENTIALLY_ACTIVE);

        for (String docNumber : docNumbers) {
            try {
                workflowDocument = workflowDocumentService.loadWorkflowDocument(docNumber, GlobalVariables.getUserSession().getPerson());
            } catch (WorkflowException we) {
                throw new RuntimeException(we);
            }
            // if the document is not in a non-active status then return true and stop evaluation
            if (!(workflowDocument.isCanceled() || workflowDocument.isException())) {
                hasActivePreqs = true;
                break;
            }
        }
        return hasActivePreqs;
    }

    /**
     * Since Invoice does not have the app doc status, perform an additional lookup
     * through doc search by using list of Invoice Doc numbers.  Query appDocStatus
     * from workflow document and filter against the provided status
     * <p/>
     * DocumentSearch allows for multiple docNumber lookup by docId|docId|docId conversion
     *
     * @param lookupDocNumbers
     * @param appDocStatus
     * @return
     */
    protected List<String> filterInvoiceByAppDocStatus(List<String> lookupDocNumbers, String... appDocStatus) {
        boolean valid = false;

        final String DOC_NUM_DELIM = "|";
        StrBuilder routerHeaderIdBuilder = new StrBuilder().appendWithSeparators(lookupDocNumbers, DOC_NUM_DELIM);

        List<String> invoiceDocNumbers = new ArrayList<String>();

        DocumentSearchCriteria.Builder documentSearchCriteriaDTO = DocumentSearchCriteria.Builder.create();
        documentSearchCriteriaDTO.setDocumentId(routerHeaderIdBuilder.toString());
        documentSearchCriteriaDTO.setDocumentTypeName(PurapConstants.PurapDocTypeCodes.PAYMENT_REQUEST_DOCUMENT);
        documentSearchCriteriaDTO.setApplicationDocumentStatuses(Arrays.asList(appDocStatus));

        DocumentSearchResults reqDocumentsList = KewApiServiceLocator.getWorkflowDocumentService().documentSearch(
                "", documentSearchCriteriaDTO.build());

        for (DocumentSearchResult reqDocument : reqDocumentsList.getSearchResults()) {
            ///use the appDocStatus from the KeyValueDTO result to look up custom status
            if (Arrays.asList(appDocStatus).contains(reqDocument.getDocument().getApplicationDocumentStatus())) {
                //found the matching status, retrieve the routeHeaderId and add to the list
                invoiceDocNumbers.add(reqDocument.getDocument().getDocumentId());
            }

        }

        return invoiceDocNumbers;
    }

    /**
     * Wrapper class to the filterInvoiceByAppDocStatus
     * <p/>
     * This class first extract the payment request document numbers from the Invoice Collections,
     * then perform the filterInvoiceByAppDocStatus function.  Base on the filtered payment request
     * doc number, reconstruct the filtered Invoice Collection
     *
     * @param invoiceDocuments
     * @param appDocStatus
     * @return
     */
    protected Collection<InvoiceDocument> filterInvoiceByAppDocStatus(Collection<InvoiceDocument> invoiceDocuments, String... appDocStatus) {
        List<String> invoiceDocNumbers = new ArrayList<String>();
        for (InvoiceDocument invoice : invoiceDocuments) {
            invoiceDocNumbers.add(invoice.getDocumentNumber());
        }

        List<String> filteredInvoiceDocNumbers = filterInvoiceByAppDocStatus(invoiceDocNumbers, appDocStatus);

        Collection<InvoiceDocument> filteredInvoiceDocuments = new ArrayList<InvoiceDocument>();
        //add to filtered collection if it is in the filtered payment request doc number list
        for (InvoiceDocument invoice : invoiceDocuments) {
            if (filteredInvoiceDocNumbers.contains(invoice.getDocumentNumber())) {
                filteredInvoiceDocuments.add(invoice);
            }
        }
        return filteredInvoiceDocuments;
    }


    /**
     * Wrapper class to the filterInvoiceByAppDocStatus (Collection<InvoiceDocument>)
     * <p/>
     * This class first construct the Invoice Collection from the iterator, and then process through
     * filterInvoiceByAppDocStatus
     *
     * @param invoiceIterator
     * @param appDocStatus
     * @return
     */
    protected Iterator<InvoiceDocument> filterInvoiceByAppDocStatus(Iterator<InvoiceDocument> invoiceIterator, String... appDocStatus) {
        Collection<InvoiceDocument> invoiceDocuments = new ArrayList<InvoiceDocument>();
        for (; invoiceIterator.hasNext(); ) {
            invoiceDocuments.add(invoiceIterator.next());
        }

        return filterInvoiceByAppDocStatus(invoiceDocuments, appDocStatus).iterator();
    }

    /**
     * @see org.kuali.ole.module.purap.document.service.InvoiceService#processInvoiceInReceivingStatus()
     */
    @Override
    public void processInvoiceInReceivingStatus() {
        List<OleInvoiceDocument> docNumbers = invoiceDao.getInvoiceInReceivingStatus();
        List<InvoiceDocument> prqssAwaitingReceiving = new ArrayList<InvoiceDocument>();
        for(OleInvoiceDocument invDocument : docNumbers) {
            if (Arrays.asList(InvoiceStatuses.APPDOC_AWAITING_RECEIVING_REVIEW).contains(invDocument.getApplicationDocumentStatus())) {
                prqssAwaitingReceiving.add(invDocument);
            }
        }


      //  docNumbers = filterInvoiceByAppDocStatus(docNumbers, InvoiceStatuses.APPDOC_AWAITING_RECEIVING_REVIEW);


        /*for (String docNumber : docNumbers) {
            InvoiceDocument prqs = getInvoiceByDocumentNumber(docNumber);
            if (ObjectUtils.isNotNull(prqs)) {
                prqssAwaitingReceiving.add(prqs);
            }
        }*/
        if (ObjectUtils.isNotNull(prqssAwaitingReceiving)) {
            for (InvoiceDocument prqsDoc : prqssAwaitingReceiving) {
                if (prqsDoc.isReceivingRequirementMet()) {
                    try {
                        documentService.approveDocument(prqsDoc, "Approved by Receiving Required PRQS job", null);
                    } catch (WorkflowException e) {
                        LOG.error("processInvoiceInReceivingStatus() Error approving payment request document from awaiting receiving", e);
                        throw new RuntimeException("Error approving payment request document from awaiting receiving", e);
                    }
                }
            }
        }
    }

    /**
     * @see org.kuali.ole.module.purap.document.service.InvoiceService#allowBackpost(org.kuali.ole.module.purap.document.InvoiceDocument)
     */
    @Override
    public boolean allowBackpost(InvoiceDocument invoiceDocument) {
        int allowBackpost = (Integer.parseInt(getParameter(OLEConstants.OptionalModuleNamespaces.PURCHASING_ACCOUNTS_PAYABLE, OLEConstants.InvoiceDocument.CMPNT_CD,PurapRuleConstants.ALLOW_BACKPOST_DAYS)));

        Calendar today = dateTimeService.getCurrentCalendar();
        Integer currentFY = universityDateService.getCurrentUniversityDate().getUniversityFiscalYear();
        java.util.Date priorClosingDateTemp = universityDateService.getLastDateOfFiscalYear(currentFY - 1);
        Calendar priorClosingDate = Calendar.getInstance();
        priorClosingDate.setTime(priorClosingDateTemp);

        // adding 1 to set the date to midnight the day after backpost is allowed so that prqss allow backpost on the last day
        Calendar allowBackpostDate = Calendar.getInstance();
        allowBackpostDate.setTime(priorClosingDate.getTime());
        allowBackpostDate.add(Calendar.DATE, allowBackpost + 1);

        Calendar prqsInvoiceDate = Calendar.getInstance();
        prqsInvoiceDate.setTime(invoiceDocument.getInvoiceDate() != null ? invoiceDocument.getInvoiceDate() : new java.util.Date());

        // if today is after the closing date but before/equal to the allowed backpost date and the invoice date is for the
        // prior year, set the year to prior year
        if ((today.compareTo(priorClosingDate) > 0) && (today.compareTo(allowBackpostDate) <= 0) && (prqsInvoiceDate.compareTo(priorClosingDate) <= 0)) {
            LOG.debug("allowBackpost() within range to allow backpost; posting entry to period 12 of previous FY");
            return true;
        }

        LOG.debug("allowBackpost() not within range to allow backpost; posting entry to current FY");
        return false;
    }

    @Override
    public boolean isPurchaseOrderValidForInvoiceDocumentCreation(InvoiceDocument invoiceDocument, PurchaseOrderDocument po) {
        Integer POID = invoiceDocument.getPurchaseOrderIdentifier();

        boolean valid = true;

        PurchaseOrderDocument purchaseOrderDocument = invoiceDocument.getPurchaseOrderDocument();
        if (ObjectUtils.isNull(purchaseOrderDocument)) {
            GlobalVariables.getMessageMap().putError(PurapPropertyConstants.PURCHASE_ORDER_IDENTIFIER, PurapKeyConstants.ERROR_PURCHASE_ORDER_NOT_EXIST);
            valid &= false;
        } else if (purchaseOrderDocument.isPendingActionIndicator()) {
            GlobalVariables.getMessageMap().putError(PurapPropertyConstants.PURCHASE_ORDER_IDENTIFIER, PurapKeyConstants.ERROR_PURCHASE_PENDING_ACTION);
            valid &= false;
        } else if (!StringUtils.equals(purchaseOrderDocument.getApplicationDocumentStatus(), PurapConstants.PurchaseOrderStatuses.APPDOC_OPEN)) {
            GlobalVariables.getMessageMap().putError(PurapPropertyConstants.PURCHASE_ORDER_IDENTIFIER, PurapKeyConstants.ERROR_PURCHASE_ORDER_NOT_OPEN);
            valid &= false;
            // if the PO is pending and it is not a Retransmit, we cannot generate a Invoice for it
        } else {
            // Verify that there exists at least 1 item left to be invoiced
            // valid &= encumberedItemExistsForInvoicing(purchaseOrderDocument);
        }

        return valid;
    }

    @Override
    public boolean encumberedItemExistsForInvoicing(PurchaseOrderDocument document) {
        boolean zeroDollar = true;
        GlobalVariables.getMessageMap().clearErrorPath();
        GlobalVariables.getMessageMap().addToErrorPath(OLEPropertyConstants.DOCUMENT);
        for (PurchaseOrderItem poi : (List<PurchaseOrderItem>) document.getItems()) {
            // Quantity-based items
            if (poi.getItemType().isLineItemIndicator() && poi.getItemType().isQuantityBasedGeneralLedgerIndicator()) {
                KualiDecimal encumberedQuantity = poi.getItemOutstandingEncumberedQuantity() == null ? KualiDecimal.ZERO : poi.getItemOutstandingEncumberedQuantity();
                if (encumberedQuantity.compareTo(KualiDecimal.ZERO) == 1) {
                    zeroDollar = false;
                    break;
                }
            }
            // Service Items or Below-the-line Items
            else if (poi.getItemType().isAmountBasedGeneralLedgerIndicator() || poi.getItemType().isAdditionalChargeIndicator()) {
                KualiDecimal encumberedAmount = poi.getItemOutstandingEncumberedAmount() == null ? KualiDecimal.ZERO : poi.getItemOutstandingEncumberedAmount();
                if (encumberedAmount.compareTo(KualiDecimal.ZERO) == 1) {
                    zeroDollar = false;
                    break;
                }
            }
        }

        return !zeroDollar;
    }

    public void setDateTimeService(DateTimeService dateTimeService) {
        this.dateTimeService = dateTimeService;
    }

    public void setParameterService(ParameterService parameterService) {
        this.parameterService = parameterService;
    }

    public void setConfigurationService(ConfigurationService configurationService) {
        this.configurationService = configurationService;
    }

    public void setDocumentService(DocumentService documentService) {
        this.documentService = documentService;
    }

    public void setNoteService(NoteService noteService) {
        this.noteService = noteService;
    }

    public void setPurapService(PurapService purapService) {
        this.purapService = purapService;
    }

    public void setInvoiceDao(InvoiceDao invoiceDao) {
        this.invoiceDao = invoiceDao;
    }

    public void setNegativeInvoiceApprovalLimitService(NegativeInvoiceApprovalLimitService negativeInvoiceApprovalLimitService) {
        this.negativeInvoiceApprovalLimitService = negativeInvoiceApprovalLimitService;
    }

    public void setPurapAccountingService(PurapAccountingService purapAccountingService) {
        this.purapAccountingService = purapAccountingService;
    }

    public void setBusinessObjectService(BusinessObjectService businessObjectService) {
        this.businessObjectService = businessObjectService;
    }

    public void setPurapWorkflowIntegrationService(PurApWorkflowIntegrationService purapWorkflowIntegrationService) {
        this.purapWorkflowIntegrationService = purapWorkflowIntegrationService;
    }

    public void setWorkflowDocumentService(WorkflowDocumentService workflowDocumentService) {
        this.workflowDocumentService = workflowDocumentService;
    }

    public void setAccountsPayableService(AccountsPayableService accountsPayableService) {
        this.accountsPayableService = accountsPayableService;
    }

    public void setVendorService(VendorService vendorService) {
        this.vendorService = vendorService;
    }

    public void setDataDictionaryService(DataDictionaryService dataDictionaryService) {
        this.dataDictionaryService = dataDictionaryService;
    }

    public void setUniversityDateService(UniversityDateService universityDateService) {
        this.universityDateService = universityDateService;
    }


    public void setBankService(BankService bankService) {
        this.bankService = bankService;
    }


    public void setPurchaseOrderService(PurchaseOrderService purchaseOrderService) {
        this.purchaseOrderService = purchaseOrderService;
    }

    public void setFinancialSystemWorkflowHelperService(FinancialSystemWorkflowHelperService financialSystemWorkflowHelperService) {
        this.financialSystemWorkflowHelperService = financialSystemWorkflowHelperService;
    }


    public void setKualiRuleService(KualiRuleService kualiRuleService) {
        this.kualiRuleService = kualiRuleService;
    }

    /**
     * Gets the accountsPayableService attribute.
     *
     * @return Returns the accountsPayableService
     */

    public AccountsPayableService getAccountsPayableService() {
        return SpringContext.getBean(AccountsPayableService.class);
    }

    public String getParameter(String namespaceCode, String componentCode, String parameterName) {
        ParameterKey parameterKey = ParameterKey.create(OLEConstants.OLE_CMPNT,
                namespaceCode, componentCode, parameterName);
        Parameter parameter = CoreServiceApiServiceLocator.getParameterRepositoryService().getParameter(parameterKey);
        return parameter != null ? parameter.getValue() : null;
    }

    public boolean getParameterBoolean(String namespaceCode, String componentCode, String parameterName) {
        ParameterKey parameterKey = ParameterKey.create(OLEConstants.OLE_CMPNT,
                namespaceCode, componentCode, parameterName);
        Parameter parameter = CoreServiceApiServiceLocator.getParameterRepositoryService().getParameter(parameterKey);
        if (parameter != null && parameter.getValue().equalsIgnoreCase("y")) {
            return true;
        }
        return false;
    }
}
