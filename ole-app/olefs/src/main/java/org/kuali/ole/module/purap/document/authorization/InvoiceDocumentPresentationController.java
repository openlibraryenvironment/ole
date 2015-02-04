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
package org.kuali.ole.module.purap.document.authorization;

import org.apache.commons.lang.StringUtils;
import org.kuali.ole.module.purap.PurapAuthorizationConstants.InvoiceEditMode;
import org.kuali.ole.module.purap.PurapConstants.InvoiceStatuses;
import org.kuali.ole.module.purap.PurapConstants.PurchaseOrderStatuses;
import org.kuali.ole.module.purap.PurapParameterConstants;
import org.kuali.ole.module.purap.businessobject.InvoiceItem;
import org.kuali.ole.module.purap.document.InvoiceDocument;
import org.kuali.ole.module.purap.document.service.PurapService;
import org.kuali.ole.sys.OLEConstants;
import org.kuali.ole.sys.OleAuthorizationConstants;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.ole.sys.service.FinancialSystemWorkflowHelperService;
import org.kuali.ole.sys.service.impl.OleParameterConstants;
import org.kuali.rice.coreservice.framework.parameter.ParameterService;
import org.kuali.rice.kew.api.WorkflowDocument;
import org.kuali.rice.kew.api.action.ActionType;
import org.kuali.rice.kew.api.action.ValidActions;
import org.kuali.rice.krad.document.Document;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.util.ObjectUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;


public class InvoiceDocumentPresentationController extends PurchasingAccountsPayableDocumentPresentationController {

    Boolean canHold;
    Boolean canRequestCancel;
    Boolean canEditPreExtraction;

    @Override
    public boolean canSave(Document document) {
        InvoiceDocument invoiceDocument = (InvoiceDocument) document;

        if (!StringUtils.equalsIgnoreCase(invoiceDocument.getDocumentHeader().getWorkflowDocument().getStatus().name(), InvoiceStatuses.APPDOC_INITIATE)
               && !StringUtils.equalsIgnoreCase(invoiceDocument.getDocumentHeader().getWorkflowDocument().getStatus().name(), OLEConstants.OleInvoice.INVOICE_SAVED)) {
            return false;
        }

        if (canEditPreExtraction(invoiceDocument)) {
            return true;
        }

        return super.canSave(document);
    }

    @Override
    public boolean canReload(Document document) {
        InvoiceDocument invoiceDocument = (InvoiceDocument) document;

        if (StringUtils.equals(invoiceDocument.getApplicationDocumentStatus(), InvoiceStatuses.APPDOC_INITIATE)) {
            return false;
        }

        if (canEditPreExtraction(invoiceDocument)) {
            return true;
        }

        return super.canReload(document);
    }

    @Override
    public boolean canCancel(Document document) {
        //controlling the cancel button through getExtraButtons in InvoiceForm
        return false;
    }

    @Override
    public boolean canApprove(Document document) {
        InvoiceDocument invoiceDocument = (InvoiceDocument) document;

        if (invoiceDocument.isInvoiceCancelIndicator() || invoiceDocument.isHoldIndicator()) {
            return false;
        }

        return super.canApprove(document);
    }

    @Override
    public boolean canCopy (Document document) {
        InvoiceDocument invoiceDocument = (InvoiceDocument) document;
        if (super.canCopy(document)) {
            if (!StringUtils.equalsIgnoreCase(invoiceDocument.getDocumentHeader().getWorkflowDocument().getStatus().name(), InvoiceStatuses.APPDOC_INITIATE)) {
                return true;
            }
        }
        return false;

    }

    @Override
    public boolean canDisapprove(Document document) {

        WorkflowDocument workflowDocument = document.getDocumentHeader().getWorkflowDocument();
        if (workflowDocument.isEnroute()) {
            ValidActions validActions = workflowDocument.getValidActions();
            return validActions.getValidActions().contains(ActionType.DISAPPROVE);
        }

        return super.canDisapprove(document);
    }

    /**
     * @see org.kuali.rice.krad.document.DocumentPresentationControllerBase#canEdit(org.kuali.rice.krad.document.Document)
     */
    @Override
    public boolean canEdit(Document document) {
        InvoiceDocument invoiceDocument = (InvoiceDocument) document;
        boolean fullDocEntryCompleted = SpringContext.getBean(PurapService.class).isFullDocumentEntryCompleted(invoiceDocument);

        // if the hold or cancel indicator is true, don't allow editing
        if (invoiceDocument.isHoldIndicator() || invoiceDocument.isInvoiceCancelIndicator()) {
            return false;
        }
        if (fullDocEntryCompleted) {
            //  after fullDocEntry is completed, only fiscal officer reviewers can edit
            if (invoiceDocument.isDocumentStoppedInRouteNode(InvoiceStatuses.NODE_ACCOUNT_REVIEW)) {
                return true;
            }
            return false;
        } else {
            //before fullDocEntry is completed, document can be edited (could be preroute or enroute)
            return true;
        }
    }

    /**
     * @see org.kuali.rice.kns.document.authorization.TransactionalDocumentPresentationControllerBase#getEditModes(org.kuali.rice.krad.document.Document)
     */
    @Override
    public Set<String> getEditModes(Document document) {
        Set<String> editModes = super.getEditModes(document);

        InvoiceDocument invoiceDocument = (InvoiceDocument) document;

        if (canProcessorCancel(invoiceDocument)) {
            editModes.add(InvoiceEditMode.ACCOUNTS_PAYABLE_PROCESSOR_CANCEL);
        }

        if (canManagerCancel(invoiceDocument)) {
            editModes.add(InvoiceEditMode.ACCOUNTS_PAYABLE_MANAGER_CANCEL);
        }

        if (canHold(invoiceDocument)) {
            editModes.add(InvoiceEditMode.HOLD);
        }

        if (canRequestCancel(invoiceDocument)) {
            editModes.add(InvoiceEditMode.REQUEST_CANCEL);
        }

        if (canRemoveHold(invoiceDocument)) {
            editModes.add(InvoiceEditMode.REMOVE_HOLD);
        }

        if (canRemoveRequestCancel(invoiceDocument)) {
            editModes.add(InvoiceEditMode.REMOVE_REQUEST_CANCEL);
        }

        if (canProcessorInit(invoiceDocument)) {
            editModes.add(InvoiceEditMode.DISPLAY_INIT_TAB);
        }

        if (ObjectUtils.isNotNull(invoiceDocument.getVendorHeaderGeneratedIdentifier())) {
            editModes.add(InvoiceEditMode.LOCK_VENDOR_ENTRY);
        }

        if (SpringContext.getBean(PurapService.class).isFullDocumentEntryCompleted(invoiceDocument)) {
            editModes.add(InvoiceEditMode.FULL_DOCUMENT_ENTRY_COMPLETED);
        }
        //else if (ObjectUtils.isNotNull(invoiceDocument.getPurchaseOrderDocument()) && PurchaseOrderStatuses.APPDOC_OPEN.equals(invoiceDocument.getPurchaseOrderDocument().getApplicationDocumentStatus())) {
          /*
            String documentTypeName = OLEConstants.FinancialDocumentTypeCodes.PAYMENT_REQUEST;
            String nameSpaceCode = OLEConstants.CoreModuleNamespaces.SELECT;

            AttributeSet permissionDetails = new AttributeSet();
            permissionDetails.put(KimAttributes.DOCUMENT_TYPE_NAME,documentTypeName);

            boolean canClosePO = KIMServiceLocator.getIdentityManagementService().hasPermission(GlobalVariables.getUserSession().getPerson().getPrincipalId(), nameSpaceCode,
                    OLEConstants.OleInvoice.CAN_CLOSE_PO, permissionDetails);
            if(canClosePO) {
            editModes.add(InvoiceEditMode.ALLOW_CLOSE_PURCHASE_ORDER);
            }*/
        //  editModes.add(InvoiceEditMode.ALLOW_CLOSE_PURCHASE_ORDER);
        // }

        //FIXME hjs: alter to restrict what AP shouldn't be allowed to edit
        if (canEditPreExtraction(invoiceDocument)) {
            editModes.add(InvoiceEditMode.EDIT_PRE_EXTRACT);
        }

        // See if purap tax is enabled
        boolean salesTaxInd = SpringContext.getBean(ParameterService.class).getParameterValueAsBoolean(OleParameterConstants.PURCHASING_DOCUMENT.class, PurapParameterConstants.ENABLE_SALES_TAX_IND);
        if (salesTaxInd) {
            editModes.add(InvoiceEditMode.PURAP_TAX_ENABLED);

            if (invoiceDocument.isUseTaxIndicator()) {
                // if use tax, don't allow editing of tax fields
                editModes.add(InvoiceEditMode.LOCK_TAX_AMOUNT_ENTRY);
            } else {
                // display the "clear all taxes" button if doc is not using use tax
                editModes.add(InvoiceEditMode.CLEAR_ALL_TAXES);

            }
        }

        // tax area tab is editable while waiting for tax review
        if (invoiceDocument.isDocumentStoppedInRouteNode(InvoiceStatuses.NODE_VENDOR_TAX_REVIEW)) {
            editModes.add(InvoiceEditMode.TAX_AREA_EDITABLE);
        }

        if (PurchaseOrderStatuses.APPDOC_AWAIT_TAX_REVIEW
                .equals(invoiceDocument.getApplicationDocumentStatus())) {
            editModes.add(InvoiceEditMode.TAX_AREA_EDITABLE);
        }


        // the tax tab is viewable to everyone after tax is approved
        if (InvoiceStatuses.APPDOC_DEPARTMENT_APPROVED.equals(invoiceDocument.getApplicationDocumentStatus()) &&
                // if and only if the invoice has gone through tax review would TaxClassificationCode be non-empty
                !StringUtils.isEmpty(invoiceDocument.getTaxClassificationCode())) {
            editModes.add(InvoiceEditMode.TAX_INFO_VIEWABLE);
        }

        if (invoiceDocument.isDocumentStoppedInRouteNode(InvoiceStatuses.NODE_ACCOUNT_REVIEW)) {
            // remove FULL_ENTRY because FO cannot edit rest of doc; only their own acct lines
            editModes.add(InvoiceEditMode.RESTRICT_FISCAL_ENTRY);

            // only do line item check if the hold/cancel indicator is false, otherwise document editing should be turned off.
            if (!invoiceDocument.isHoldIndicator() && !invoiceDocument.isInvoiceCancelIndicator()) {
                List lineList = new ArrayList();
                for (Iterator iter = invoiceDocument.getItems().iterator(); iter.hasNext(); ) {
                    InvoiceItem item = (InvoiceItem) iter.next();
                    lineList.addAll(item.getSourceAccountingLines());
                    // If FO has deleted the last accounting line for an item, set entry mode to full so they can add another one
                    if (item.getItemType().isLineItemIndicator() && item.getSourceAccountingLines().size() == 0) {
                        editModes.add(OleAuthorizationConstants.TransactionalEditMode.EXPENSE_ENTRY);
                    }
                }
            }
        }

        // Remove editBank edit mode if the document has been extracted
        if (invoiceDocument.isExtracted()) {
            editModes.remove(OLEConstants.BANK_ENTRY_EDITABLE_EDITING_MODE);
        }

        return editModes;
    }

    protected boolean canProcessorInit(InvoiceDocument invoiceDocument) {
        // if Payment Request is in INITIATE status or NULL returned from getAppDocStatus
        String status = invoiceDocument.getApplicationDocumentStatus();
        if (StringUtils.equals(status, InvoiceStatuses.APPDOC_INITIATE)) {
            return true;
        }
        return false;
    }


    protected boolean canProcessorCancel(InvoiceDocument invoiceDocument) {
        // if Payment Request is in INITIATE status, user cannot cancel doc
        if (canProcessorInit(invoiceDocument)) {
            return false;
        }

        String docStatus = invoiceDocument.getApplicationDocumentStatus();
        boolean requestCancelIndicator = invoiceDocument.getInvoiceCancelIndicator();
        boolean holdIndicator = invoiceDocument.isHoldIndicator();
        boolean extracted = invoiceDocument.isExtracted();

        boolean preroute =
                InvoiceStatuses.APPDOC_IN_PROCESS.equals(docStatus) ||
                        InvoiceStatuses.APPDOC_AWAITING_ACCOUNTS_PAYABLE_REVIEW.equals(docStatus);
        boolean enroute =
                InvoiceStatuses.APPDOC_AWAITING_SUB_ACCT_MGR_REVIEW.equals(docStatus) ||
                        InvoiceStatuses.APPDOC_AWAITING_FISCAL_REVIEW.equals(docStatus) ||
                        InvoiceStatuses.APPDOC_AWAITING_ORG_REVIEW.equals(docStatus) ||
                        InvoiceStatuses.APPDOC_AWAITING_PAYMENT_REVIEW.equals(docStatus)
                        ||
                        InvoiceStatuses.APPDOC_AWAITING_TAX_REVIEW.equals(docStatus);
        boolean postroute =
                InvoiceStatuses.APPDOC_DEPARTMENT_APPROVED.equals(docStatus) ||
                        InvoiceStatuses.APPDOC_AUTO_APPROVED.equals(docStatus);

        boolean can = false;
        if (InvoiceStatuses.STATUSES_PREROUTE.contains(docStatus)) {
            can = true;
        } else if (InvoiceStatuses.STATUSES_ENROUTE.contains(docStatus)) {
            can = requestCancelIndicator;
        } else if (InvoiceStatuses.STATUSES_POSTROUTE.contains(docStatus)) {
            can = !requestCancelIndicator && !holdIndicator && !extracted;
        }

        return can;
    }

    protected boolean canManagerCancel(InvoiceDocument invoiceDocument) {
        // if Payment Request is in INITIATE status, user cannot cancel doc
        if (canProcessorInit(invoiceDocument)) {
            return false;
        }

        String docStatus = invoiceDocument.getApplicationDocumentStatus();
        boolean requestCancelIndicator = invoiceDocument.getInvoiceCancelIndicator();
        boolean holdIndicator = invoiceDocument.isHoldIndicator();
        boolean extracted = invoiceDocument.isExtracted();

        boolean preroute =
                InvoiceStatuses.APPDOC_IN_PROCESS.equals(docStatus) ||
                        InvoiceStatuses.APPDOC_AWAITING_ACCOUNTS_PAYABLE_REVIEW.equals(docStatus);
        boolean enroute =
                InvoiceStatuses.APPDOC_AWAITING_SUB_ACCT_MGR_REVIEW.equals(docStatus) ||
                        InvoiceStatuses.APPDOC_AWAITING_FISCAL_REVIEW.equals(docStatus) ||
                        InvoiceStatuses.APPDOC_AWAITING_ORG_REVIEW.equals(docStatus) ||
                        InvoiceStatuses.APPDOC_AWAITING_PAYMENT_REVIEW.equals(docStatus)
                        ||
                        InvoiceStatuses.APPDOC_AWAITING_TAX_REVIEW.equals(docStatus);
        boolean postroute =
                InvoiceStatuses.APPDOC_DEPARTMENT_APPROVED.equals(docStatus) ||
                        InvoiceStatuses.APPDOC_AUTO_APPROVED.equals(docStatus);

        boolean can = false;
        if (InvoiceStatuses.STATUSES_PREROUTE.contains(docStatus) ||
                InvoiceStatuses.STATUSES_ENROUTE.contains(docStatus)) {
            can = true;
        } else if (InvoiceStatuses.STATUSES_POSTROUTE.contains(docStatus)) {
            can = !requestCancelIndicator && !holdIndicator && !extracted;
        }

        return can;
    }

    /**
     * Determines whether the Invoice Hold button shall be available. Conditions:
     * - Payment Request is not already on hold, and
     * - Payment Request is not already being requested to be canceled, and
     * - Payment Request has not already been extracted to PDP, and
     * - Payment Request status is not in the list of "STATUSES_DISALLOWING_HOLD" or document is being adhoc routed; and
     *
     * @return True if the document state allows placing the Payment Request on hold.
     */
    protected boolean canHold(InvoiceDocument invoiceDocument) {
        if (canHold == null) {

            boolean can = !invoiceDocument.isHoldIndicator()
                    && !invoiceDocument.isInvoiceCancelIndicator()
                    && !invoiceDocument.isExtracted();
            if (can) {
                can = SpringContext.getBean(FinancialSystemWorkflowHelperService.class)
                        .isAdhocApprovalRequestedForPrincipal(
                                invoiceDocument.getFinancialSystemDocumentHeader().getWorkflowDocument(),
                                GlobalVariables.getUserSession().getPrincipalId());
                can = can
                        || !InvoiceStatuses.STATUSES_DISALLOWING_HOLD.contains(invoiceDocument
                        .getApplicationDocumentStatus());
            }
            canHold = can;
        }

        return canHold;
    }

    /**
     * Determines whether the Request Cancel Invoice button shall be available. Conditions:
     * - Payment Request is not already on hold, and
     * - Payment Request is not already being requested to be canceled, and
     * - Payment Request has not already been extracted to PDP, and
     * - Payment Request status is not in the list of "STATUSES_DISALLOWING_REQUEST_CANCEL" or document is being adhoc routed; and
     *
     * @return True if the document state allows placing the request that the Payment Request be canceled.
     */
    protected boolean canRequestCancel(InvoiceDocument invoiceDocument) {
        if (canRequestCancel == null) {
            boolean can = !invoiceDocument.isInvoiceCancelIndicator()
                    && !invoiceDocument.isHoldIndicator() && !invoiceDocument.isExtracted();
            if (can) {
                can = SpringContext.getBean(FinancialSystemWorkflowHelperService.class)
                        .isAdhocApprovalRequestedForPrincipal(
                                invoiceDocument.getFinancialSystemDocumentHeader().getWorkflowDocument(),
                                GlobalVariables.getUserSession().getPrincipalId());
                can = can
                        || !InvoiceStatuses.STATUSES_DISALLOWING_REQUEST_CANCEL.contains(invoiceDocument
                        .getApplicationDocumentStatus());
            }
            canRequestCancel = can;
        }
        return canRequestCancel;
    }

    /**
     * Determines whether the Remove Hold button shall be available. Conditions:
     * - the hold indicator is set to true
     * <p/>
     * Because the state of the Payment Request cannot be changed while the document is on hold,
     * we should not have to check the state of the document to remove the hold.
     * For example, the document should not be allowed to be approved or extracted while on hold.
     *
     * @return True if the document state allows removing the Payment Request from hold.
     */
    protected boolean canRemoveHold(InvoiceDocument invoiceDocument) {
        return invoiceDocument.isHoldIndicator();
    }

    /**
     * Determines whether the Remove Request Cancel button shall be available. Conditions:
     * - the request cancel indicator is set to true;  and
     * <p/>
     * Because the state of the Payment Request cannot be changed while the document is set to request cancel,
     * we should not have to check the state of the document to remove the request cancel.
     * For example, the document should not be allowed to be approved or extracted while set to request cancel.
     *
     * @return True if the document state allows removing a request that the Payment Request be canceled.
     */
    protected boolean canRemoveRequestCancel(InvoiceDocument invoiceDocument) {
        return invoiceDocument.isInvoiceCancelIndicator();
    }

    protected boolean canEditPreExtraction(InvoiceDocument invoiceDocument) {
        if (canEditPreExtraction == null) {
            boolean can = (!invoiceDocument.isExtracted()
                    && !SpringContext.getBean(FinancialSystemWorkflowHelperService.class)
                    .isAdhocApprovalRequestedForPrincipal(
                            invoiceDocument.getFinancialSystemDocumentHeader().getWorkflowDocument(),
                            GlobalVariables.getUserSession().getPrincipalId()) && !InvoiceStatuses.CANCELLED_STATUSES
                    .contains(invoiceDocument.getApplicationDocumentStatus()));
            canEditPreExtraction = can;
        }
        return canEditPreExtraction;
    }

}
