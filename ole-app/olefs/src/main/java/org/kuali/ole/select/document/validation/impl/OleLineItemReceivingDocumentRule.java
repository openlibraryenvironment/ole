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

import org.apache.commons.lang.StringUtils;
import org.kuali.ole.module.purap.PurapConstants;
import org.kuali.ole.module.purap.businessobject.LineItemReceivingItem;
import org.kuali.ole.module.purap.businessobject.PurapEnterableItem;
import org.kuali.ole.module.purap.businessobject.ReceivingItem;
import org.kuali.ole.module.purap.document.LineItemReceivingDocument;
import org.kuali.ole.module.purap.document.ReceivingDocument;
import org.kuali.ole.module.purap.document.validation.impl.LineItemReceivingDocumentRule;
import org.kuali.ole.select.OleSelectConstant;
import org.kuali.ole.select.businessobject.*;
import org.kuali.ole.select.document.OleLineItemReceivingDocument;
import org.kuali.ole.sys.OLEKeyConstants;
import org.kuali.rice.core.api.util.type.KualiDecimal;
import org.kuali.rice.krad.document.Document;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.util.ObjectUtils;

import java.util.List;

/**
 * This class handles validation rules for OLE Line Item Receiving Document.
 */

public class OleLineItemReceivingDocumentRule extends LineItemReceivingDocumentRule {

    private static org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(OleLineItemReceivingDocumentRule.class);

    /**
     * Overridden method to include validation for parts along with the existing validation
     * for OLE Line Item Receiving Document.
     *
     * @param document
     * @return If the document passed all validations
     */
    @Override
    protected boolean processCustomRouteDocumentBusinessRules(Document document) {
        LOG.debug("Inside processCustomRouteDocumentBusinessRules of OleLineItemReceivingDocumentRule");
        boolean valid = true;

        LineItemReceivingDocument lineItemReceivingDocument = (LineItemReceivingDocument) document;
        valid &= canCreateLineItemReceivingDocument(lineItemReceivingDocument);
        valid &= isAtLeastOneItemEntered(lineItemReceivingDocument);
        valid &= validateItemUnitOfMeasure(lineItemReceivingDocument);
        //  makes sure all of the lines adhere to the rule that quantityDamaged and
        // quantityReturned cannot (each) equal more than the quantityReceived
        valid &= validateAllReceivingLinesHaveSaneQuantities(lineItemReceivingDocument);

        valid &= validateAllReceivingLinesHaveSaneParts(lineItemReceivingDocument);
        //valid &= validateReceivedQuantityAndParts(lineItemReceivingDocument);
        valid &= isExceptionNotesMandatory(lineItemReceivingDocument);
        valid &= isAcknowledged(lineItemReceivingDocument);
        valid &= validateItemDescriptionRequired(lineItemReceivingDocument);
        /*
         * valid &= checkForValidCopiesAndPartsForSubmit((OleLineItemReceivingDocument) lineItemReceivingDocument); valid &=
         * validateCopies((OleLineItemReceivingDocument) lineItemReceivingDocument);
         */
        LOG.debug("Leaving processCustomRouteDocumentBusinessRules of OleLineItemReceivingDocumentRule");
        return valid;
    }

    /**
     * Overridden method to include validation for parts along with the existing validation
     * when adding a new line item in OLE Line Item Receiving Document.
     *
     * @param document
     * @param item
     * @param errorPathPrefix
     * @return If the newly added item passed all validations
     */
    @Override
    public boolean processAddReceivingItemRules(ReceivingDocument document, LineItemReceivingItem item, String errorPathPrefix) {
        LOG.debug("Inside processAddReceivingItemRules of OleLineItemReceivingDocumentRule");
        boolean valid = super.processAddReceivingItemRules(document, item, errorPathPrefix);

        valid &= validatePartsReturnedNotMoreThanReceived(document, item, errorPathPrefix, new Integer(0));
        valid &= validatePartsDamagedNotMoreThanReceived(document, item, errorPathPrefix, new Integer(0));
        LOG.debug("Leaving processAddReceivingItemRules of OleLineItemReceivingDocumentRule");
        return valid;
    }

    /**
     * This method validates if parts returned is greater than parts received for a line item
     * in OLE Line Item Receiving document and sets error accordingly.
     *
     * @param document
     * @param item
     * @param errorPathPrefix
     * @param lineNumber
     * @return boolean value (If the line item is valid or not)
     */
    protected boolean validatePartsReturnedNotMoreThanReceived(ReceivingDocument document, LineItemReceivingItem item, String errorPathPrefix, Integer lineNumber) {
        LOG.debug("Inside validatePartsReturnedNotMoreThanReceived of OleLineItemReceivingDocumentRule");
        OleLineItemReceivingItem oleItem = (OleLineItemReceivingItem) item;
        if (oleItem.getItemReturnedTotalParts() != null && oleItem.getItemReceivedTotalParts() != null) {
            if (oleItem.getItemReturnedTotalParts().isGreaterThan(oleItem.getItemReceivedTotalParts())) {
                GlobalVariables.getMessageMap().putError(PurapConstants.ITEM_TAB_ERROR_PROPERTY, OLEKeyConstants.ERROR_RECEIVING_LINE_PRTRETURNED_GT_PRTRECEIVED, (lineNumber.intValue() == 0 ? "Add Line" : lineNumber.toString()));
                return false;
            }
        }
        LOG.debug("Leaving validatePartsReturnedNotMoreThanReceived of OleLineItemReceivingDocumentRule");
        return true;
    }

    /**
     * This method validates if parts damaged is greater than parts received for a line item
     * in OLE Line Item Receiving document and sets error accordingly.
     *
     * @param document
     * @param item
     * @param errorPathPrefix
     * @param lineNumber
     * @return boolean value (If the line item is valid or not)
     */
    protected boolean validatePartsDamagedNotMoreThanReceived(ReceivingDocument document, LineItemReceivingItem item, String errorPathPrefix, Integer lineNumber) {
        LOG.debug("Inside validatePartsDamagedNotMoreThanReceived of OleLineItemReceivingDocumentRule");
        OleLineItemReceivingItem oleItem = (OleLineItemReceivingItem) item;
        if (oleItem.getItemDamagedTotalParts() != null && oleItem.getItemReceivedTotalParts() != null) {
            if (oleItem.getItemDamagedTotalParts().isGreaterThan(oleItem.getItemReceivedTotalParts())) {
                GlobalVariables.getMessageMap().putError(PurapConstants.ITEM_TAB_ERROR_PROPERTY, OLEKeyConstants.ERROR_RECEIVING_LINE_PRTDAMAGED_GT_PRTRECEIVED, (lineNumber.intValue() == 0 ? "Add Line" : lineNumber.toString()));
                return false;
            }
        }
        LOG.debug("Leaving validatePartsDamagedNotMoreThanReceived of OleLineItemReceivingDocumentRule");
        return true;
    }

    /**
     * This method validates if parts returned/damaged is greater than parts received for a line item
     * in OLE Line Item Receiving document
     *
     * @param document
     * @return boolean value (If the line item is valid or not)
     */
    protected boolean validateAllReceivingLinesHaveSaneParts(ReceivingDocument document) {
        LOG.debug("Inside validateAllReceivingLinesHaveSaneParts of OleLineItemReceivingDocumentRule");
        GlobalVariables.getMessageMap().clearErrorPath();
        boolean valid = true;
        for (int i = 0; i < document.getItems().size(); i++) {
            LineItemReceivingItem item = (LineItemReceivingItem) document.getItems().get(i);

            valid &= validatePartsReturnedNotMoreThanReceived(document, item, "", new Integer(i + 1));
            valid &= validatePartsDamagedNotMoreThanReceived(document, item, "", new Integer(i + 1));
        }
        LOG.debug("Leaving validateAllReceivingLinesHaveSaneParts of OleLineItemReceivingDocumentRule");
        return valid;
    }

    /**
     * This method validates if exception note is mandatory for the line item entered.
     * Sets an error message accordingly.
     *
     * @param receivingDocument
     * @return boolean
     */
    private boolean isExceptionNotesMandatory(LineItemReceivingDocument receivingDocument) {
        LOG.debug("Inside isExceptionNotesMandatory of OleLineItemReceivingDocumentRule");
        boolean isMandatory = false;
        boolean isMandatoryAll = true;
        for (OleLineItemReceivingItem item : (List<OleLineItemReceivingItem>) receivingDocument.getItems()) {
            if (ObjectUtils.isNotNull(item.getItemDamagedTotalParts()) && ObjectUtils.isNotNull(item.getItemDamagedTotalQuantity()) && ObjectUtils.isNotNull(item.getItemReturnedTotalParts()) && ObjectUtils.isNotNull(item.getItemReturnedTotalQuantity())) {
                if ((item.getItemDamagedTotalParts().isNonZero() || item.getItemDamagedTotalQuantity().isNonZero() || item.getItemReturnedTotalParts().isNonZero() || item.getItemReturnedTotalQuantity().isNonZero()) && !item.getExceptionNoteList().isEmpty()) {
                    isMandatory = true;
                } else if (item.getItemDamagedTotalParts().isZero() && item.getItemDamagedTotalQuantity().isZero() && item.getItemReturnedTotalParts().isZero() && item.getItemReturnedTotalQuantity().isZero()) {
                    isMandatory = true;
                } else {
                    GlobalVariables.getMessageMap().putError(PurapConstants.ITEM_TAB_ERROR_PROPERTY, OLEKeyConstants.ERROR_RECEIVING_LINE_EXCEPTION_NOTE_MANDATORY);
                    isMandatory = false;
                }
                isMandatoryAll &= isMandatory;
            }
        }

        return isMandatoryAll;
    }

    /**
     * This method validates if all Special Handling notes are acknowledged
     * for the line item entered.
     *
     * @param receivingDocument
     * @return boolean
     */
    protected boolean isAcknowledged(LineItemReceivingDocument receivingDocument) {
        LOG.debug("Inside isAcknowledged of OleLineItemReceivingDocumentRule");
        boolean isNotesAck = true;
        for (OleLineItemReceivingItem item : (List<OleLineItemReceivingItem>) receivingDocument.getItems()) {
            boolean ack = item.isConsideredEntered();
            boolean received = false;
            List<OleCopy> copyList = item.getCopyList();
            boolean copiesReceived = false;
            boolean isAcknowledge = false;
            if(ack) {
                if(item.getOleReceiptStatus() != null) {
                    if(item.getOleReceiptStatus().getReceiptStatus().equalsIgnoreCase("Fully Received")) {
                        received = true;
                        continue;
                    }
                }
                else {
                    if(!received) {
                        for(OleCopy oleCopy :copyList) {
                            if(oleCopy.getReceiptStatus().equalsIgnoreCase("Received")) {
                                copiesReceived = true;
                            }
                        }
                    }
                }
            }
            if(copiesReceived) {
            for (OleLineItemReceivingReceiptNotes notes : item.getSpecialHandlingNoteList()) {
                isAcknowledge = notes.isNotesAck();
                isNotesAck &= isAcknowledge;
            }
            }

           /* if (ack & isNotesAck) {
                return true;
            }*/
           /* else if (!ack) {
                return true;
            }*/
        }
        if(isNotesAck) {
            return true;
        }
        GlobalVariables.getMessageMap().putError(PurapConstants.ITEM_TAB_ERROR_PROPERTY, OLEKeyConstants.ERROR_RECEIVING_LINE_NOTACKNOWLEDGED);
        return false;
    }

    /**
     * This method validates if exception note is mandatory for the line item entered.
     * Sets an error message accordingly.
     *
     * @param receivingDocument
     * @return boolean
     */
    private boolean validateReceivedQuantityAndParts(LineItemReceivingDocument receivingDocument) {
        LOG.debug("Inside isExceptionNotesMandatory of OleLineItemReceivingDocumentRule");
        boolean isNonZero = false;
        boolean isNonZeroAll = true;
        int index = 0;
        for (OleLineItemReceivingItem item : (List<OleLineItemReceivingItem>) receivingDocument.getItems()) {
            index += 1;
            if (StringUtils.equalsIgnoreCase(item.getItemTypeCode(), PurapConstants.ItemTypeCodes.ITEM_TYPE_UNORDERED_ITEM_CODE)) {
                if ((ObjectUtils.isNull(item.getItemReceivedTotalQuantity()) || item.getItemReceivedTotalQuantity().isZero()) && (ObjectUtils.isNull(item.getItemReceivedTotalParts()) || item.getItemReceivedTotalParts().isZero())) {
                    GlobalVariables.getMessageMap().putError(PurapConstants.ITEM_TAB_ERROR_PROPERTY, OLEKeyConstants.ERROR_RECEIVING_LINE_RECEIVED_TOTAL_QUANTITY_NON_ZERO_UNORDERED, String.valueOf(index));
                    GlobalVariables.getMessageMap().putError(PurapConstants.ITEM_TAB_ERROR_PROPERTY, OLEKeyConstants.ERROR_RECEIVING_LINE_RECEIVED_TOTAL_PARTS_NON_ZERO_UNORDERED, String.valueOf(index));
                    isNonZero = false;
                } else if ((ObjectUtils.isNull(item.getItemReceivedTotalParts()) || item.getItemReceivedTotalParts().isZero()) && (ObjectUtils.isNotNull(item.getItemReceivedTotalQuantity()) && item.getItemReceivedTotalQuantity().isGreaterThan(KualiDecimal.ZERO))) {
                    GlobalVariables.getMessageMap().putError(PurapConstants.ITEM_TAB_ERROR_PROPERTY, OLEKeyConstants.ERROR_RECEIVING_LINE_RECEIVED_TOTAL_PARTS_NON_ZERO_UNORDERED, String.valueOf(index));
                    isNonZero = false;
                } else if ((ObjectUtils.isNull(item.getItemReceivedTotalQuantity()) || item.getItemReceivedTotalQuantity().isZero()) && (ObjectUtils.isNotNull(item.getItemReceivedTotalParts()) && item.getItemReceivedTotalParts().isGreaterThan(KualiDecimal.ZERO))) {
                    GlobalVariables.getMessageMap().putError(PurapConstants.ITEM_TAB_ERROR_PROPERTY, OLEKeyConstants.ERROR_RECEIVING_LINE_RECEIVED_TOTAL_QUANTITY_NON_ZERO_UNORDERED, String.valueOf(index));
                    isNonZero = false;
                } else {
                    isNonZero = true;
                }
                isNonZeroAll &= isNonZero;
            }
        }
        return isNonZeroAll;
    }

    /**
     * This method overrided to Add the Error Message separatly for Total Quantity Received and Total Parts Received
     *
     * @param receivingDocument
     * @return
     */
    @Override
    protected boolean isAtLeastOneItemEntered(ReceivingDocument receivingDocument) {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Inside isAtLeastOneItemEntered of LineItemReceivingDocumentRule");
            LOG.debug("Number of Items :" + receivingDocument.getItems().size());
        }

        LineItemReceivingDocument lineItemReceivingDocument = (LineItemReceivingDocument) receivingDocument;
        boolean valid = false;
        for (ReceivingItem items : (List<ReceivingItem>) receivingDocument.getItems()) {
            if (StringUtils.equalsIgnoreCase(items.getItemTypeCode(), PurapConstants.ItemTypeCodes.ITEM_TYPE_UNORDERED_ITEM_CODE)) {
                valid = true;
                break;
            }
        }
        if (valid) {
            validateReceivedQuantityAndParts(lineItemReceivingDocument);
        } else {
            for (ReceivingItem item : (List<ReceivingItem>) receivingDocument.getItems()) {
                if (((PurapEnterableItem) item).isConsideredEntered()) {
                    // if any item is entered return true
                    return true;
                }
            }
            boolean quantity = true;
            boolean parts = true;
            boolean noOfQuantity=true;
            if (!valid) {
                valid = true;
                for (OleReceivingItem oleItem : (List<OleReceivingItem>) receivingDocument.getItems()) {

                    if ((ObjectUtils.isNull(oleItem.getItemReceivedTotalQuantity()) || oleItem.getItemReceivedTotalQuantity().isZero()) && (ObjectUtils.isNotNull(oleItem.getItemReceivedTotalParts()) && oleItem.getItemReceivedTotalParts().isGreaterThan(KualiDecimal.ZERO))) {
                        quantity = false;
                    } else if ((ObjectUtils.isNull(oleItem.getItemReceivedTotalParts()) || oleItem.getItemReceivedTotalParts().isZero()) && (ObjectUtils.isNotNull(oleItem.getItemReceivedTotalQuantity()) && oleItem.getItemReceivedTotalQuantity().isGreaterThan(KualiDecimal.ZERO))) {
                        parts = false;
                    }
                    if((ObjectUtils.isNull(oleItem.getItemReceivedTotalQuantity()) || oleItem.getItemReceivedTotalQuantity().isZero())&&(ObjectUtils.isNull(oleItem.getItemReceivedTotalParts()) || oleItem.getItemReceivedTotalParts().isZero()))  {
                        noOfQuantity=false;
                    }
                }
                // if no items are entered return false
                if (!quantity) {
                    GlobalVariables.getMessageMap().putError(PurapConstants.ITEM_TAB_ERROR_PROPERTY, OLEKeyConstants.ERROR_RECEIVING_LINE_RECEIVED_TOTAL_QUANTITY_NON_ZERO_ORDERED);
                    quantity = true;
                } else if (!parts) {
                    GlobalVariables.getMessageMap().putError(PurapConstants.ITEM_TAB_ERROR_PROPERTY, OLEKeyConstants.ERROR_RECEIVING_LINE_RECEIVED_TOTAL_PARTS_NON_ZERO_ORDERED);
                    parts = true;
                }
                else if(!noOfQuantity){
                    GlobalVariables.getMessageMap().putError(PurapConstants.ITEM_TAB_ERROR_PROPERTY, OLEKeyConstants.ERROR_RECEIVING_LINE_ITEM_RECEIVED_TOTAL_QUANTITY);
                }
                /* else {
                    GlobalVariables.getMessageMap().putError(PurapConstants.ITEM_TAB_ERROR_PROPERTY, OLEKeyConstants.ERROR_RECEIVING_LINE_RECEIVED_TOTAL_QUANTITY_NON_ZERO_ORDERED);
                    GlobalVariables.getMessageMap().putError(PurapConstants.ITEM_TAB_ERROR_PROPERTY, OLEKeyConstants.ERROR_RECEIVING_LINE_RECEIVED_TOTAL_PARTS_NON_ZERO_ORDERED)
                }*/

            }
        }
        return valid;
    }

    private boolean validateItemDescriptionRequired(LineItemReceivingDocument receivingDocument) {
        LOG.debug("Inside isExceptionNotesMandatory of OleLineItemReceivingDocumentRule");
        boolean isNotNull = true;
        int index = 0;
        for (LineItemReceivingItem item : (List<LineItemReceivingItem>) receivingDocument.getItems()) {
            index += 1;
            if (StringUtils.equalsIgnoreCase(item.getItemTypeCode(), PurapConstants.ItemTypeCodes.ITEM_TYPE_UNORDERED_ITEM_CODE) || StringUtils.equalsIgnoreCase(item.getItemTypeCode(), PurapConstants.ItemTypeCodes.ITEM_TYPE_ITEM_CODE)) {
                if (item.getItemDescription() == null || item.getItemDescription().trim().length() <= 0) {
                    GlobalVariables.getMessageMap().putError(PurapConstants.ITEM_TAB_ERROR_PROPERTY, OleSelectConstant.ERROR_REQUIRED, new String("Item Line " + index));
                    isNotNull = false;
                }
            }
        }
        return isNotNull;
    }

    public boolean processCustomLineItemReceivingDescriptionBusinessRules(Document document, OleLineItemReceivingItem lineItem) {
        boolean validate = true;
        if (lineItem.getItemDescription() == null || lineItem.getItemDescription().isEmpty()) {
            GlobalVariables.getMessageMap().putError(PurapConstants.ITEM_TAB_ERROR_PROPERTY, OleSelectConstant.ERROR_REQUIRED, new String[]{"Line Item"});
            validate = false;
        }
        return validate;
    }

    /**
     * This method validates the copies entered for the line item
     */
    private boolean validateCopies(OleLineItemReceivingDocument receivingDocument) {
        LOG.debug("Inside validateCopies of OleLineItemReceivingDocumentRule");
        boolean isValid = true;
        for (OleLineItemReceivingItem item : (List<OleLineItemReceivingItem>) receivingDocument.getItems()) {
            KualiDecimal itemQuantity = item.getItemReceivedTotalQuantity();
            KualiDecimal itemCopies = KualiDecimal.ZERO;
            if (item.getCopies().size() > 0) {
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
            } else {
                GlobalVariables.getMessageMap().putError(PurapConstants.ITEM_TAB_ERROR_PROPERTY,
                        OLEKeyConstants.ERROR_ATLEAST_ONE_COPY_SHOULD_ADD_WHEN_TOTAL_RECEIVED_COPY_IS_GREATERTHAN_ZERO);
                return false;
            }
        }
        return isValid;
    }

    /**
     * This method validates whether total copies and total parts are lessThan or equal to quantity to be received and parts to be
     * received
     *
     * @param receivingDocument
     * @return boolean
     */
    private boolean checkForValidCopiesAndPartsForSubmit(OleLineItemReceivingDocument receivingDocument) {
        LOG.debug("Inside checkForValidCopiesAndPartsForSubmit of OleLineItemReceivingDocumentRule");
        boolean isValid = true;
        for (OleLineItemReceivingItem item : (List<OleLineItemReceivingItem>) receivingDocument.getItems()) {
            if (null != item.getPurchaseOrderIdentifier()) {
                KualiDecimal itemTotalQuantity = item.getItemReceivedTotalQuantity();
                KualiDecimal itemTotalParts = item.getItemReceivedTotalParts();
                KualiDecimal itemQuantityToBeReceived = item.getItemReceivedToBeQuantity();
                KualiDecimal itemPartsToBeReceived = item.getItemReceivedToBeParts();
                if (!(itemTotalQuantity.isLessEqual(itemQuantityToBeReceived))
                        && !(itemTotalParts.isLessEqual(itemPartsToBeReceived))) {
                    GlobalVariables
                            .getMessageMap()
                            .putError(
                                    PurapConstants.ITEM_TAB_ERROR_PROPERTY,
                                    OLEKeyConstants.ERROR_TOTAL_COPIES_TOTAL_PARTS_SHOULDBE_LESSTHAN_OR_EQUALTO_QUANTITY_TOBE_RECEIVED_AND_PARTS_TOBE_RECEIVED);
                    return false;
                }
            }
        }
        return isValid;
    }
}
