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
package org.kuali.ole.module.purap.document.validation.impl;

import org.kuali.ole.module.purap.businessobject.InvoiceItem;
import org.kuali.ole.module.purap.document.InvoiceDocument;
import org.kuali.ole.sys.document.validation.GenericValidation;
import org.kuali.ole.sys.document.validation.event.AttributedDocumentEvent;

import java.math.BigDecimal;

public class InvoiceReviewValidation extends GenericValidation {
    protected static org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(InvoiceReviewValidation.class);

    private InvoiceItem itemForValidation;

    public boolean validate(AttributedDocumentEvent event) {
        boolean valid = true;
        InvoiceDocument invoice = (InvoiceDocument) event.getDocument();


        boolean containsAccounts = false;
        int accountLineNbr = 0;

        String identifier = itemForValidation.getItemIdentifierString();
        BigDecimal total = BigDecimal.ZERO;
        if (LOG.isDebugEnabled()) {
            LOG.debug("validateInvoiceReview() The " + identifier + " is getting the total percent field set to " + BigDecimal.ZERO);
        }

        //if ((itemForValidation.getItemType().isLineItemIndicator() && itemForValidation.getTotalAmount() != null &&
        //        itemForValidation.getTotalAmount().isNonZero() &&
        //       ((itemForValidation.getItemType().isAmountBasedGeneralLedgerIndicator() && (itemForValidation.getPoOutstandingAmount() == null || itemForValidation.getPoOutstandingAmount().isZero())) || (itemForValidation.getItemType().isQuantityBasedGeneralLedgerIndicator() && (itemForValidation.getPoOutstandingQuantity() == null || itemForValidation.getPoOutstandingQuantity().isZero()))))) {


        if ((itemForValidation.getItemType().isLineItemIndicator() && itemForValidation.getTotalAmount() != null &&
                itemForValidation.getTotalAmount().isNonZero() &&
                ((itemForValidation.getItemType().isAmountBasedGeneralLedgerIndicator() || (itemForValidation.getItemType().isQuantityBasedGeneralLedgerIndicator()))))) {


            // ERROR because we have total amount and no open encumberance on the PO item
            // this error should have been caught at an earlier level
            if (itemForValidation.getItemType().isAmountBasedGeneralLedgerIndicator()) {
                String error = "Payment Request " + invoice.getPurapDocumentIdentifier() + ", " + identifier + " has total amount '" + itemForValidation.getTotalAmount() + "' but outstanding encumbered amount " + itemForValidation.getPoOutstandingAmount();
                LOG.error("validateInvoiceReview() " + error);
            } /*else {
                String error = "Payment Request " + invoice.getPurapDocumentIdentifier() + ", " + identifier + " has quantity '" + itemForValidation.getItemQuantity() + "' but outstanding encumbered quantity " + itemForValidation.getPoOutstandingQuantity();
                LOG.error("validateInvoiceReview() " + error);
            }*/
        } else {
            // not validating but ok
            String error = "Payment Request " + invoice.getPurapDocumentIdentifier() + ", " + identifier + " has total amount '" + itemForValidation.getTotalAmount() + "'";
            if (itemForValidation.getItemType().isLineItemIndicator()) {
                if (itemForValidation.getItemType().isAmountBasedGeneralLedgerIndicator()) {
                    error = error + " with outstanding encumbered amount " + itemForValidation.getPoOutstandingAmount();
                } else {
                    error = error + " with outstanding encumbered quantity " + itemForValidation.getPoOutstandingQuantity();
                }
            }
            LOG.info("validateInvoiceReview() " + error);
        }

        return valid;
    }


    public InvoiceItem getItemForValidation() {
        return itemForValidation;
    }

    public void setItemForValidation(InvoiceItem itemForValidation) {
        this.itemForValidation = itemForValidation;
    }


}
