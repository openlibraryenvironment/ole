/*
 * Copyright 2011 The Kuali Foundation.
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
package org.kuali.ole.select.document.validation.impl;

import org.kuali.ole.module.purap.PurapConstants;
import org.kuali.ole.module.purap.document.validation.impl.CorrectionReceivingDocumentRule;
import org.kuali.ole.select.businessobject.OleCopies;
import org.kuali.ole.select.businessobject.OleCorrectionReceivingItem;
import org.kuali.ole.select.businessobject.OleCorrectionReceivingItemReceiptNotes;
import org.kuali.ole.select.document.OleCorrectionReceivingDocument;
import org.kuali.ole.sys.OLEKeyConstants;
import org.kuali.rice.core.api.util.type.KualiDecimal;
import org.kuali.rice.krad.document.Document;
import org.kuali.rice.krad.util.GlobalVariables;

import java.util.List;

/**
 * This class handles validation rules for OLE Receiving Correction Line Item Document.
 */

public class OleCorrectionReceivingDocumentRule extends CorrectionReceivingDocumentRule {

    private static org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(OleCorrectionReceivingDocumentRule.class);

    /**
     * Overridden method to include validation for parts along with the existing validation
     * for OLE Correction Receiving Document.
     *
     * @param document
     * @return If the document passed all validations
     */
    @Override
    protected boolean processCustomRouteDocumentBusinessRules(Document document) {
        LOG.debug("Inside processCustomRouteDocumentBusinessRules of OleCorrectionReceivingDocumentRule");
        boolean valid = super.processCustomRouteDocumentBusinessRules(document);
        OleCorrectionReceivingDocument receivingDocument = (OleCorrectionReceivingDocument) document;
        // valid &= validateCopies(receivingDocument);
        valid &= isAcknowledged(receivingDocument);
        return valid;
    }

    /**
     * This method validates if all Special Handling notes are acknowledged for the line item entered.
     *
     * @param receivingDocument
     * @return boolean
     */
    protected boolean isAcknowledged(OleCorrectionReceivingDocument receivingDocument) {
        LOG.debug("Inside isAcknowledged of OleCorrectionReceivingDocumentRule");
        boolean isNotesAck = true;
        for (OleCorrectionReceivingItem item : (List<OleCorrectionReceivingItem>) receivingDocument.getItems()) {
            boolean ack = item.isConsideredEntered();
            boolean isAck = false;
            for (OleCorrectionReceivingItemReceiptNotes notes : item.getCorrectionSpecialHandlingNoteList()) {
                isAck = notes.isNotesAck();
                isNotesAck &= isAck;
            }
            if (ack & isNotesAck) {
                return true;
            } else if (!ack) {
                return true;
            }
        }
        GlobalVariables.getMessageMap().putError(PurapConstants.ITEM_TAB_ERROR_PROPERTY,
                OLEKeyConstants.ERROR_RECEIVING_LINE_NOTACKNOWLEDGED);
        return false;
    }

    /**
     * This method validates the copies entered for the line item
     */
    private boolean validateCopies(OleCorrectionReceivingDocument receivingDocument) {
        LOG.debug("Inside validateCopies of OleCorrectionReceivingDocumentRule");
        boolean isValid = true;
        for (OleCorrectionReceivingItem item : (List<OleCorrectionReceivingItem>) receivingDocument.getItems()) {
            KualiDecimal itemQuantity = item.getItemReceivedTotalQuantity();
            KualiDecimal itemCopies = KualiDecimal.ZERO;
            for (OleCopies copies : item.getCopies()) {
                itemCopies = itemCopies.add(copies.getItemCopies());
            }
            if (item.getItemReceivedTotalQuantity().isGreaterThan(new KualiDecimal(1))
                    || item.getItemReceivedTotalParts().isGreaterThan(new KualiDecimal(1))) {
                if (!itemQuantity.equals(itemCopies)) {
                    GlobalVariables.getMessageMap().putError(PurapConstants.ITEM_TAB_ERROR_PROPERTY,
                            OLEKeyConstants.ERROR_RECEIVING_LINE_TOTAL_COPIES_NOT_EQUAL_QUANITY);
                    return false;
                }
            }
        }
        return isValid;
    }
}