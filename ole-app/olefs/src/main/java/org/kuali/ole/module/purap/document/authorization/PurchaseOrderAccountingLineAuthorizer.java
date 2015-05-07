/*
 * Copyright 2009 The Kuali Foundation
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
import org.kuali.ole.OLEConstants;
import org.kuali.ole.module.purap.PurapConstants;
import org.kuali.ole.module.purap.PurapPropertyConstants;
import org.kuali.ole.module.purap.businessobject.PurApAccountingLine;
import org.kuali.ole.module.purap.businessobject.PurchaseOrderItem;
import org.kuali.ole.module.purap.businessobject.PurchaseOrderType;
import org.kuali.ole.module.purap.document.PurchaseOrderAmendmentDocument;
import org.kuali.ole.module.purap.document.PurchaseOrderDocument;
import org.kuali.ole.module.purap.document.PurchasingAccountsPayableDocument;
import org.kuali.ole.select.OleSelectConstant;
import org.kuali.ole.sys.OLEPropertyConstants;
import org.kuali.ole.sys.businessobject.AccountingLine;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.ole.sys.document.AccountingDocument;
import org.kuali.rice.core.api.util.type.KualiDecimal;
import org.kuali.rice.kew.api.WorkflowDocument;
import org.kuali.rice.kim.api.identity.Person;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocatorWeb;

import java.math.BigDecimal;
import java.util.*;

/**
 * Accounting line authorizer for Requisition document which allows adding accounting lines at specified nodes
 */
public class PurchaseOrderAccountingLineAuthorizer extends PurapAccountingLineAuthorizer {
    private static final String NEW_UNORDERED_ITEMS_NODE = "NewUnorderedItems";

    /**
     * Allow new lines to be rendered at NewUnorderedItems node
     *
     * @see org.kuali.ole.sys.document.authorization.AccountingLineAuthorizerBase#renderNewLine(org.kuali.ole.sys.document.AccountingDocument, java.lang.String)
     */
    @Override
    public boolean renderNewLine(AccountingDocument accountingDocument, String accountingGroupProperty) {
        WorkflowDocument workflowDocument = ((PurchasingAccountsPayableDocument) accountingDocument).getFinancialSystemDocumentHeader().getWorkflowDocument();

        Set<String> currentRouteNodeName = workflowDocument.getCurrentNodeNames();

        //  if its in the NEW_UNORDERED_ITEMS node, then allow the new line to be drawn
        if (PurchaseOrderAccountingLineAuthorizer.NEW_UNORDERED_ITEMS_NODE.equals(currentRouteNodeName.toString())) {
            return true;
        }

        if (PurapConstants.PurchaseOrderDocTypes.PURCHASE_ORDER_AMENDMENT_DOCUMENT.equals(workflowDocument.getDocumentTypeName()) && StringUtils.isNotBlank(accountingGroupProperty) && accountingGroupProperty.contains(PurapPropertyConstants.ITEM)) {
            int itemNumber = determineItemNumberFromGroupProperty(accountingGroupProperty);
            PurchaseOrderAmendmentDocument poaDoc = (PurchaseOrderAmendmentDocument) accountingDocument;
            List<PurchaseOrderItem> items = poaDoc.getItems();
            PurchaseOrderItem item = items.get(itemNumber);
            return item.isNewItemForAmendment() || item.getSourceAccountingLines().size() == 0;
        }

        return super.renderNewLine(accountingDocument, accountingGroupProperty);
    }

    private int determineItemNumberFromGroupProperty(String accountingGroupProperty) {
        int openBracketPos = accountingGroupProperty.indexOf("[");
        int closeBracketPos = accountingGroupProperty.indexOf("]");
        String itemNumberString = accountingGroupProperty.substring(openBracketPos + 1, closeBracketPos);
        int itemNumber = new Integer(itemNumberString).intValue();
        return itemNumber;
    }

    @Override
    protected boolean allowAccountingLinesAreEditable(AccountingDocument accountingDocument,
                                                      AccountingLine accountingLine) {
        PurApAccountingLine purapAccount = (PurApAccountingLine) accountingLine;
        PurchaseOrderItem poItem = (PurchaseOrderItem) purapAccount.getPurapItem();
        PurchaseOrderDocument po = (PurchaseOrderDocument) accountingDocument;


        if (poItem != null && !poItem.getItemType().isAdditionalChargeIndicator()) {
            if (!poItem.isItemActiveIndicator()) {
                return false;
            }

            // if total amount has a value and is non-zero
            if (poItem.getItemInvoicedTotalAmount() != null && poItem.getItemInvoicedTotalAmount().isGreaterThan(KualiDecimal.ZERO) && getOrderType(po.getPurchaseOrderTypeId())) {
                return false;
            }

           /* if (po.getContainsUnpaidPaymentRequestsOrCreditMemos() && !poItem.isNewItemForAmendment()) {
                return false;
            }*/

        }
        return super.allowAccountingLinesAreEditable(accountingDocument, accountingLine);
    }

    @Override
    public boolean determineEditPermissionOnLine(AccountingDocument accountingDocument, AccountingLine accountingLine, String accountingLineCollectionProperty, boolean currentUserIsDocumentInitiator, boolean pageIsEditable) {
        // the fields in a new line should be always editable
        if (accountingLine.getSequenceNumber() == null) {
            return true;
        }

        // check the initiation permission on the document if it is in the state of preroute, but only if
        // the PO status is not In Process.
        WorkflowDocument workflowDocument = ((PurchasingAccountsPayableDocument) accountingDocument).getFinancialSystemDocumentHeader().getWorkflowDocument();
        PurchaseOrderDocument poDocument = ((PurchaseOrderDocument) accountingDocument);

        if (!poDocument.getApplicationDocumentStatus().equals(PurapConstants.PurchaseOrderStatuses.APPDOC_IN_PROCESS) && (workflowDocument.isInitiated() || workflowDocument.isSaved())) {
            if (PurapConstants.PurchaseOrderDocTypes.PURCHASE_ORDER_AMENDMENT_DOCUMENT.equals(workflowDocument.getDocumentTypeName())) {
                PurApAccountingLine purapAccount = (PurApAccountingLine) accountingLine;
                PurchaseOrderItem item = (PurchaseOrderItem) purapAccount.getPurapItem();
                // changes for jira OLE-2354
                return item.isNewItemForAmendment() || item.getSourceAccountingLines().size() == OleSelectConstant.ONE;
            } else {
                return currentUserIsDocumentInitiator;
            }
        } else {
            return true;
        }
    }

    /**
     * @see org.kuali.ole.sys.document.authorization.AccountingLineAuthorizerBase#getUnviewableBlocks(org.kuali.ole.sys.document.AccountingDocument, org.kuali.ole.sys.businessobject.AccountingLine, boolean, org.kuali.rice.kim.api.identity.Person)
     */
    @Override
    public Set<String> getUnviewableBlocks(AccountingDocument accountingDocument, AccountingLine accountingLine, boolean newLine, Person currentUser) {
        Set<String> unviewableBlocks = super.getUnviewableBlocks(accountingDocument, accountingLine, newLine, currentUser);
        unviewableBlocks.remove(OLEPropertyConstants.PERCENT);
        unviewableBlocks.remove(OLEPropertyConstants.AMOUNT);

        return unviewableBlocks;
    }

    public boolean getOrderType(BigDecimal purchaseOrderTypeId) {
        BusinessObjectService businessObjectService = SpringContext.getBean(BusinessObjectService.class);
        Map map = new HashMap();
        map.put("purchaseOrderTypeId",purchaseOrderTypeId);
        PurchaseOrderType purchaseOrderType = businessObjectService.findByPrimaryKey(PurchaseOrderType.class,map);
        if(purchaseOrderType != null && purchaseOrderType.getPurchaseOrderType().equals(OLEConstants.ORDER_TYPE_VALUE)) {
            return true;
        }
        return false;
    }
}
