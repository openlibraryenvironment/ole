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

package org.kuali.ole.module.purap.document;

import org.kuali.ole.module.purap.PurapConstants;
import org.kuali.ole.module.purap.PurapConstants.PurapDocTypeCodes;
import org.kuali.ole.module.purap.PurapConstants.PurchaseOrderStatuses;
import org.kuali.ole.module.purap.PurapWorkflowConstants;
import org.kuali.ole.module.purap.businessobject.PurchaseOrderAccount;
import org.kuali.ole.module.purap.businessobject.PurchaseOrderItem;
import org.kuali.ole.module.purap.document.service.PurapService;
import org.kuali.ole.module.purap.document.service.PurchaseOrderService;
import org.kuali.ole.module.purap.document.service.ReceivingService;
import org.kuali.ole.module.purap.service.PurapAccountingService;
import org.kuali.ole.module.purap.service.PurapGeneralLedgerService;
import org.kuali.ole.sys.OLEConstants;
import org.kuali.ole.sys.businessobject.AccountingLine;
import org.kuali.ole.sys.businessobject.GeneralLedgerPendingEntry;
import org.kuali.ole.sys.businessobject.GeneralLedgerPendingEntrySourceDetail;
import org.kuali.ole.sys.businessobject.SourceAccountingLine;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.ole.sys.document.AccountingDocument;
import org.kuali.rice.core.api.util.type.KualiDecimal;
import org.kuali.rice.coreservice.api.CoreServiceApiServiceLocator;
import org.kuali.rice.coreservice.api.parameter.Parameter;
import org.kuali.rice.coreservice.api.parameter.ParameterKey;
import org.kuali.rice.kew.api.exception.WorkflowException;
import org.kuali.rice.kew.framework.postprocessor.DocumentRouteStatusChange;
import org.kuali.rice.krad.bo.Note;
import org.kuali.rice.krad.rules.rule.event.KualiDocumentEvent;
import org.kuali.rice.krad.util.ObjectUtils;
import org.kuali.rice.krad.workflow.service.WorkflowDocumentService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import static org.kuali.ole.sys.OLEConstants.GL_DEBIT_CODE;
import static org.kuali.rice.core.api.util.type.KualiDecimal.ZERO;

/**
 * Purchase Order Amendment Document
 */
public class PurchaseOrderAmendmentDocument extends PurchaseOrderDocument {
    protected static org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(PurchaseOrderAmendmentDocument.class);

    boolean newUnorderedItem; //Used for routing
    String receivingDeliveryCampusCode; //Used for routing

    /**
     * When Purchase Order Amendment document has been Processed through Workflow, the general ledger entries are created and the PO
     * status remains "OPEN".
     *
     * @see org.kuali.ole.module.purap.document.PurchaseOrderDocument#doRouteStatusChange()
     */
    @Override
    public void doRouteStatusChange(DocumentRouteStatusChange statusChangeEvent) {
        super.doRouteStatusChange(statusChangeEvent);

        try {
            // DOCUMENT PROCESSED
            if (this.getFinancialSystemDocumentHeader().getWorkflowDocument().isProcessed()) {
                // generate GL entries
                SpringContext.getBean(PurapGeneralLedgerService.class).generateEntriesApproveAmendPurchaseOrder(this);

                // if gl entries created(means there is amount change) for amend purchase order send an FYI to all fiscal officers
                if ((getGlOnlySourceAccountingLines() != null && !getGlOnlySourceAccountingLines().isEmpty())) {
                    SpringContext.getBean(PurchaseOrderService.class).sendFyiForGLEntries(this);
                }

                // update indicators
                SpringContext.getBean(PurchaseOrderService.class).completePurchaseOrderAmendment(this);

                // update vendor commodity code by automatically spawning vendor maintenance document
                SpringContext.getBean(PurchaseOrderService.class).updateVendorCommodityCode(this);

                // for app doc status
                updateAndSaveAppDocStatus(PurchaseOrderStatuses.APPDOC_OPEN);
            }
            // DOCUMENT DISAPPROVED
            else if (this.getFinancialSystemDocumentHeader().getWorkflowDocument().isDisapproved()) {
                SpringContext.getBean(PurchaseOrderService.class).setCurrentAndPendingIndicatorsForDisapprovedChangePODocuments(this);
                SpringContext.getBean(PurapService.class).saveDocumentNoValidation(this);

                // for app doc status
                try {
                    String nodeName = SpringContext.getBean(WorkflowDocumentService.class).getCurrentRouteLevelName(this.getFinancialSystemDocumentHeader().getWorkflowDocument());
                    String reqStatus = PurapConstants.PurchaseOrderStatuses.getPurchaseOrderAppDocDisapproveStatuses().get(nodeName);
                    updateAndSaveAppDocStatus(PurapConstants.PurchaseOrderStatuses.getPurchaseOrderAppDocDisapproveStatuses().get(reqStatus));

                    RequisitionDocument req = getPurApSourceDocumentIfPossible();
                    appSpecificRouteDocumentToUser(this.getFinancialSystemDocumentHeader().getWorkflowDocument(), req.getFinancialSystemDocumentHeader().getWorkflowDocument().getRoutedByPrincipalId(), "Notification of Order Disapproval for Requisition " + req.getPurapDocumentIdentifier() + "(document id " + req.getDocumentNumber() + ")", "Requisition Routed By User");
                    return;

                } catch (WorkflowException e) {
                    logAndThrowRuntimeException("Error saving routing data while saving App Doc Status " + getDocumentNumber(), e);
                }
            }
            // DOCUMENT CANCELED
            else if (this.getFinancialSystemDocumentHeader().getWorkflowDocument().isCanceled()) {
                SpringContext.getBean(PurchaseOrderService.class).setCurrentAndPendingIndicatorsForCancelledChangePODocuments(this);

                // for app doc status
                updateAndSaveAppDocStatus(PurapConstants.PurchaseOrderStatuses.APPDOC_CANCELLED);
            } else if(this.getApplicationDocumentStatus().equals(PurchaseOrderStatuses.APPDOC_PENDING_PRINT) && checkForPdfGeneration(this)) {
                updateAndSaveAppDocStatus(PurchaseOrderStatuses.APPDOC_OPEN);
            }
        } catch (WorkflowException e) {
            logAndThrowRuntimeException("Error saving routing data while saving document with id " + getDocumentNumber(), e);
        }
    }


    public boolean checkForPdfGeneration(PurchaseOrderDocument  purchaseOrderDocument) {
        if (purchaseOrderDocument.getDocumentHeader() != null && purchaseOrderDocument.getDocumentHeader().getWorkflowDocument() != null &&
                purchaseOrderDocument.getDocumentHeader().getWorkflowDocument().getDocumentTypeName().equals(OLEConstants.FinancialDocumentTypeCodes.PURCHASE_ORDER_AMENDMENT)
                && getParameterValue(OLEConstants.CANCEL_PDF_CREATION).equalsIgnoreCase("true")) {
            if (purchaseOrderDocument.getNotes().size() > 0) {
                for (Note note : purchaseOrderDocument.getNotes()) {
                    if (note.getNoteText().contains(OLEConstants.POA_BATCH_NOTE)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * @see org.kuali.module.purap.rules.PurapAccountingDocumentRuleBase#customizeExplicitGeneralLedgerPendingEntry(org.kuali.ole.sys.document.AccountingDocument,
     *      org.kuali.ole.sys.businessobject.AccountingLine, org.kuali.ole.sys.businessobject.GeneralLedgerPendingEntry)
     */
    @Override
    public void customizeExplicitGeneralLedgerPendingEntry(GeneralLedgerPendingEntrySourceDetail postable, GeneralLedgerPendingEntry explicitEntry) {
        //super.customizeExplicitGeneralLedgerPendingEntry(postable, explicitEntry);

        SpringContext.getBean(PurapGeneralLedgerService.class).customizeGeneralLedgerPendingEntry(this, (AccountingLine) postable, explicitEntry, getPurapDocumentIdentifier(), GL_DEBIT_CODE, PurapDocTypeCodes.PO_DOCUMENT, true);

        // don't think i should have to override this, but default isn't getting the right PO doc
        explicitEntry.setFinancialDocumentTypeCode(PurapDocTypeCodes.PO_AMENDMENT_DOCUMENT);
        explicitEntry.setFinancialDocumentApprovedCode(OLEConstants.PENDING_ENTRY_APPROVED_STATUS_CODE.APPROVED);
    }

    @Override
    public List<GeneralLedgerPendingEntrySourceDetail> getGeneralLedgerPendingEntrySourceDetails() {
        List<GeneralLedgerPendingEntrySourceDetail> accountingLines = new ArrayList<GeneralLedgerPendingEntrySourceDetail>();
        if (getGlOnlySourceAccountingLines() != null) {
            Iterator iter = getGlOnlySourceAccountingLines().iterator();
            while (iter.hasNext()) {
                accountingLines.add((GeneralLedgerPendingEntrySourceDetail) iter.next());
            }
        }
        return accountingLines;
    }


    @Override
    public void populateDocumentForRouting() {
        newUnorderedItem = SpringContext.getBean(PurchaseOrderService.class).hasNewUnorderedItem(this);
        receivingDeliveryCampusCode = SpringContext.getBean(ReceivingService.class).getReceivingDeliveryCampusCode(this);
        super.populateDocumentForRouting();
    }

    public boolean isNewUnorderedItem() {
        return newUnorderedItem;
    }

    public void setNewUnorderedItem(boolean newUnorderedItem) {
        this.newUnorderedItem = newUnorderedItem;
    }

    public String getReceivingDeliveryCampusCode() {
        return receivingDeliveryCampusCode;
    }

    public void setReceivingDeliveryCampusCode(String receivingDeliveryCampusCode) {
        this.receivingDeliveryCampusCode = receivingDeliveryCampusCode;
    }

    @Override
    public boolean answerSplitNodeQuestion(String nodeName) throws UnsupportedOperationException {
        if (nodeName.equals(PurapWorkflowConstants.HAS_NEW_UNORDERED_ITEMS)) {
            return isNewUnorderedItem();
        }
        throw new UnsupportedOperationException("Cannot answer split question for this node you call \"" + nodeName + "\"");
    }

    // MSU Contribution OLEMI-8600 DTT-3159 OLECNTRB-953
    @Override
    public Class<? extends AccountingDocument> getDocumentClassForAccountingLineValueAllowedValidation() {
        return PurchaseOrderDocument.class;
    }

    // MSU Contribution DTT-3812 OLEMI-8642 OLECNTRB-957
    @Override
    public void customPrepareForSave(KualiDocumentEvent event) {
        super.customPrepareForSave(event);

        // Set outstanding encumbered quantity/amount on items
        List<PurchaseOrderItem> poItems = this.getItems();
        for (PurchaseOrderItem item : poItems) {
            if (item.isItemActiveIndicator()) {
                KualiDecimal invoicedTotalQuantity = item.getItemInvoicedTotalQuantity();
                if (item.getItemInvoicedTotalQuantity() == null) {
                    invoicedTotalQuantity = ZERO;
                    item.setItemInvoicedTotalQuantity(ZERO);
                }

                if (item.getItemInvoicedTotalAmount() == null) {
                    item.setItemInvoicedTotalAmount(ZERO);
                }
                if (item.getItemType().isQuantityBasedGeneralLedgerIndicator()) {
                    KualiDecimal itemQuantity = item.getItemQuantity() == null ? ZERO : item.getItemQuantity();
                    KualiDecimal outstandingEncumberedQuantity = itemQuantity.subtract(invoicedTotalQuantity);
                    item.setItemOutstandingEncumberedQuantity(outstandingEncumberedQuantity);
                }
                KualiDecimal outstandingEncumbrance = null;

                // If the item is amount based (not quantity based)
                if (item.getItemType().isAmountBasedGeneralLedgerIndicator()) {
                    KualiDecimal totalAmount = item.getTotalAmount() == null ? ZERO : item.getTotalAmount();
                    outstandingEncumbrance = totalAmount.subtract(item.getItemInvoicedTotalAmount());
                    item.setItemOutstandingEncumberedAmount(outstandingEncumbrance);
                } else {
                    // if the item is quantity based
                    BigDecimal itemUnitPrice = ObjectUtils.isNull(item.getItemUnitPrice()) ? BigDecimal.ZERO : item
                            .getItemUnitPrice();
                    outstandingEncumbrance = new KualiDecimal(item.getItemOutstandingEncumberedQuantity()
                            .bigDecimalValue().multiply(itemUnitPrice));
                    item.setItemOutstandingEncumberedAmount(outstandingEncumbrance);
                }

                List accountLines = item.getSourceAccountingLines();
                Collections.sort(accountLines);

                updateEncumbranceOnAccountingLines(outstandingEncumbrance, accountLines);
            }
        }

        List<SourceAccountingLine> sourceLines = SpringContext.getBean(PurapAccountingService.class)
                .generateSummaryWithNoZeroTotals(this.getItems());
        this.setSourceAccountingLines(sourceLines);
    }

    // MSU Contribution DTT-3812 OLEMI-8642 OLECNTRB-957
    protected void updateEncumbranceOnAccountingLines(KualiDecimal outstandingEcumbrance, List accountLines) {
        for (Object accountLineObject : accountLines) {
            PurchaseOrderAccount accountLine = (PurchaseOrderAccount) accountLineObject;
            if (!accountLine.isEmpty()) {
                BigDecimal linePercent = accountLine.getAccountLinePercent();
                KualiDecimal accountOutstandingEncumbrance = new KualiDecimal(outstandingEcumbrance.bigDecimalValue()
                        .multiply(linePercent).divide(OLEConstants.ONE_HUNDRED.bigDecimalValue()));
                accountLine.setItemAccountOutstandingEncumbranceAmount(accountOutstandingEncumbrance);
            }
        }
    }

    public String getParameterValue(String key) {
        ParameterKey parameterKey = ParameterKey.create(org.kuali.ole.OLEConstants.APPL_ID_OLE, org.kuali.ole.OLEConstants.SELECT_NMSPC, org.kuali.ole.OLEConstants.SELECT_CMPNT, key);
        Parameter parameter = CoreServiceApiServiceLocator.getParameterRepositoryService().getParameter(parameterKey);
        return parameter != null ? parameter.getValue() : null;
    }
}
