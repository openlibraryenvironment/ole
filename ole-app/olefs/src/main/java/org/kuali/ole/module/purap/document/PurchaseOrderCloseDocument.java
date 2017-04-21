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
import org.kuali.ole.module.purap.businessobject.PurchaseOrderView;
import org.kuali.ole.module.purap.document.dataaccess.PurchaseOrderDao;
import org.kuali.ole.module.purap.document.service.PurchaseOrderService;
import org.kuali.ole.module.purap.service.PurapGeneralLedgerService;
import org.kuali.ole.sys.OLEConstants;
import org.kuali.ole.sys.businessobject.AccountingLine;
import org.kuali.ole.sys.businessobject.GeneralLedgerPendingEntry;
import org.kuali.ole.sys.businessobject.GeneralLedgerPendingEntrySourceDetail;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.rice.kew.api.exception.WorkflowException;
import org.kuali.rice.kew.framework.postprocessor.DocumentRouteStatusChange;
import org.kuali.rice.krad.rules.rule.event.KualiDocumentEvent;
import org.kuali.rice.krad.workflow.service.WorkflowDocumentService;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.kuali.ole.sys.OLEConstants.GL_CREDIT_CODE;

/**
 * Purchase Order Close Document
 */
public class PurchaseOrderCloseDocument extends PurchaseOrderDocument {
    protected static org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(PurchaseOrderCloseDocument.class);

    /**
     * General Ledger pending entries are not created on save for this document. They are created when the document has been finally
     * processed. Overriding this method so that entries are not created yet.
     *
     * @see org.kuali.ole.module.purap.document.PurchaseOrderDocument#prepareForSave(org.kuali.rice.krad.rule.event.KualiDocumentEvent)
     */
    @Override
    public void prepareForSave(KualiDocumentEvent event) {
        LOG.info("prepareForSave(KualiDocumentEvent) do not create gl entries");
        setSourceAccountingLines(new ArrayList());
        setGeneralLedgerPendingEntries(new ArrayList());
    }

    /**
     * When Purchase Order Close document has been Processed through Workflow, the general ledger entries are created and the PO
     * status changes to "CLOSED".
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
                //SpringContext.getBean(PurapGeneralLedgerService.class).generateEntriesClosePurchaseOrder(this);

                // update indicators
                SpringContext.getBean(PurchaseOrderService.class).setCurrentAndPendingIndicatorsForApprovedPODocuments(this);

                // for app doc status
                updateAndSaveAppDocStatus(PurchaseOrderStatuses.APPDOC_CLOSED);
            }
            // DOCUMENT DISAPPROVED
            else if (this.getFinancialSystemDocumentHeader().getWorkflowDocument().isDisapproved()) {
                SpringContext.getBean(PurchaseOrderService.class).setCurrentAndPendingIndicatorsForDisapprovedChangePODocuments(this);

                // for app doc status
                try {
                    String nodeName = SpringContext.getBean(WorkflowDocumentService.class).getCurrentRouteLevelName(this.getFinancialSystemDocumentHeader().getWorkflowDocument());
                    String reqStatus = PurapConstants.PurchaseOrderStatuses.getPurchaseOrderAppDocDisapproveStatuses().get(nodeName);
                    updateAndSaveAppDocStatus(PurapConstants.PurchaseOrderStatuses.getPurchaseOrderAppDocDisapproveStatuses().get(reqStatus));

                } catch (WorkflowException e) {
                    logAndThrowRuntimeException("Error saving routing data while saving App Doc Status " + getDocumentNumber(), e);
                }

            }
            // DOCUMENT CANCELLED
            else if (this.getFinancialSystemDocumentHeader().getWorkflowDocument().isCanceled()) {
                SpringContext.getBean(PurchaseOrderService.class).setCurrentAndPendingIndicatorsForCancelledChangePODocuments(this);
                // for app doc status
                updateAndSaveAppDocStatus(PurchaseOrderStatuses.APPDOC_CLOSED);
            }
        } catch (WorkflowException e) {
            logAndThrowRuntimeException("Error saving routing data while saving document with id " + getDocumentNumber(), e);
        }
    }

    /**
     * @see org.kuali.module.purap.rules.PurapAccountingDocumentRuleBase#customizeExplicitGeneralLedgerPendingEntry(org.kuali.ole.sys.document.AccountingDocument,
     *      org.kuali.ole.sys.businessobject.AccountingLine, org.kuali.ole.sys.businessobject.GeneralLedgerPendingEntry)
     */
    @Override
    public void customizeExplicitGeneralLedgerPendingEntry(GeneralLedgerPendingEntrySourceDetail postable, GeneralLedgerPendingEntry explicitEntry) {
        super.customizeExplicitGeneralLedgerPendingEntry(postable, explicitEntry);

        SpringContext.getBean(PurapGeneralLedgerService.class).customizeGeneralLedgerPendingEntry(this, (AccountingLine) postable, explicitEntry, getPurapDocumentIdentifier(), GL_CREDIT_CODE, PurapDocTypeCodes.PO_DOCUMENT, true);

        // don't think i should have to override this, but default isn't getting the right PO doc
        explicitEntry.setFinancialDocumentTypeCode(PurapDocTypeCodes.PO_CLOSE_DOCUMENT);
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

    // MSU Contribution OLEMI-8643 DTT-3800 OLECNTRB-956
    @Override
    public List<String> getWorkflowEngineDocumentIdsToLock() {
        List<String> docIdStrings = new ArrayList<String>();
        docIdStrings.add(getDocumentNumber());
        String currentDocumentTypeName = this.getFinancialSystemDocumentHeader().getWorkflowDocument()
                .getDocumentTypeName();

        List<PurchaseOrderDocument> poList = SpringContext.getBean(PurchaseOrderDao.class).getCurrentPurchaseOrderListByRelatedDocId(accountsPayablePurchasingDocumentLinkIdentifier);


       // List<PurchaseOrderView> relatedPoViews = getRelatedViews().getRelatedPurchaseOrderViews();
        for (PurchaseOrderDocument po : poList) {
           // if (poView.isPurchaseOrderCurrentIndicator()) {
                docIdStrings.add(po.getDocumentNumber());
          //  }
        }
        if (LOG.isDebugEnabled()) {
            LOG.debug("***** getWorkflowEngineDocumentIdsToLock(" + this.documentNumber + ") = '" + docIdStrings + "'");
        }
        return docIdStrings;
    }

}
