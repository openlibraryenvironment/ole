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
import org.kuali.ole.module.purap.PurapConstants.ItemFields;
import org.kuali.ole.module.purap.PurapKeyConstants;
import org.kuali.ole.module.purap.businessobject.PaymentRequestItem;
import org.kuali.ole.module.purap.document.validation.impl.PaymentRequestProcessItemValidation;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.util.ObjectUtils;

public class OlePaymentRequestProcessItemValidation extends PaymentRequestProcessItemValidation {

    /**
     * Validates above the line items.
     *
     * @param item             - payment request item
     * @param identifierString - identifier string used to mark in an error map
     * @return
     */
    @Override
    protected boolean validateAboveTheLineItems(PaymentRequestItem item, String identifierString, boolean isReceivingDocumentRequiredIndicator) {
        boolean valid = true;
        // Currently Quantity is allowed to be NULL on screen;
        // must be either a positive number or NULL for DB
        if (ObjectUtils.isNotNull(item.getItemQuantity())) {
            if (item.getItemQuantity().isNegative()) {
                // if quantity is negative give an error
                valid = false;
                GlobalVariables.getMessageMap().putError(PurapConstants.ITEM_TAB_ERROR_PROPERTY, PurapKeyConstants.ERROR_ITEM_AMOUNT_BELOW_ZERO, ItemFields.INVOICE_QUANTITY, identifierString);
            }
            if (!isReceivingDocumentRequiredIndicator) {
                if (!StringUtils.equalsIgnoreCase(item.getItemTypeCode(), PurapConstants.ItemTypeCodes.ITEM_TYPE_UNORDERED_ITEM_CODE)) {
                    if (item.getPoOutstandingQuantity().isLessThan(item.getItemQuantity())) {
                        valid = false;
                        GlobalVariables.getMessageMap().putError(PurapConstants.ITEM_TAB_ERROR_PROPERTY, PurapKeyConstants.ERROR_ITEM_QUANTITY_TOO_MANY, ItemFields.INVOICE_QUANTITY, identifierString, ItemFields.OPEN_QUANTITY);
                    }
                }
            }

        }
        if (ObjectUtils.isNotNull(item.getExtendedPrice()) && item.getExtendedPrice().isPositive() && ObjectUtils.isNotNull(item.getPoOutstandingQuantity()) && item.getPoOutstandingQuantity().isPositive()) {

            // here we must require the user to enter some value for quantity if they want a credit amount associated
            if (ObjectUtils.isNull(item.getItemQuantity()) || item.getItemQuantity().isZero()) {
                // here we have a user not entering a quantity with an extended amount but the PO has a quantity...require user to
                // enter a quantity
                valid = false;
                GlobalVariables.getMessageMap().putError(PurapConstants.ITEM_TAB_ERROR_PROPERTY, PurapKeyConstants.ERROR_ITEM_QUANTITY_REQUIRED, ItemFields.INVOICE_QUANTITY, identifierString, ItemFields.OPEN_QUANTITY);
            }
        }

        // check that non-quantity based items are not trying to pay on a zero encumbrance amount (check only prior to ap approval)
        if ((ObjectUtils.isNull(item.getPaymentRequest().getPurapDocumentIdentifier())) || (PurapConstants.PaymentRequestStatuses.APPDOC_IN_PROCESS.equals(item.getPaymentRequest().getApplicationDocumentStatus()))) {
            if ((item.getItemType().isAmountBasedGeneralLedgerIndicator()) && ((item.getExtendedPrice() != null) && item.getExtendedPrice().isNonZero())) {
                if (item.getPoOutstandingAmount() == null || item.getPoOutstandingAmount().isZero()) {
                    valid = false;
                    GlobalVariables.getMessageMap().putError(PurapConstants.ITEM_TAB_ERROR_PROPERTY, PurapKeyConstants.ERROR_ITEM_AMOUNT_ALREADY_PAID, identifierString);
                }
            }
        }
       /* if(StringUtils.equalsIgnoreCase(item.getItemTypeCode(),PurapConstants.ItemTypeCodes.ITEM_TYPE_UNORDERED_ITEM_CODE)){
            if(item.getItemQuantity()==null || item.getItemQuantity().isLessEqual(KualiDecimal.ZERO)){
                valid=false;
                GlobalVariables.getMessageMap().putError(PurapConstants.ITEM_TAB_ERROR_PROPERTY,OleSelectConstant.ERROR_ITEM_QUANTITY_REQUIRED , identifierString);
            }
        }
        if(StringUtils.equalsIgnoreCase(item.getItemTypeCode(),PurapConstants.ItemTypeCodes.ITEM_TYPE_UNORDERED_ITEM_CODE) || StringUtils.equalsIgnoreCase(item.getItemTypeCode(),PurapConstants.ItemTypeCodes.ITEM_TYPE_ITEM_CODE) ){
            if (item.getItemDescription() == null || item.getItemDescription().trim().length() <= 0) {
                GlobalVariables.getMessageMap().putError(PurapConstants.ITEM_TAB_ERROR_PROPERTY, OleSelectConstant.ERROR_REQUIRED, identifierString);
                valid= false;
            }
        }
        OlePaymentRequestItem oleItem = (OlePaymentRequestItem) item;       
        if(oleItem.getItemQuantity()!=null && oleItem.getItemQuantity().isGreaterThan(KualiDecimal.ZERO)){   
            if(oleItem.getItemNoOfParts()==null ||new KualiDecimal(oleItem.getItemNoOfParts()).isLessEqual(KualiDecimal.ZERO)){
                GlobalVariables.getMessageMap().putError(PurapConstants.ITEM_TAB_ERROR_PROPERTY, OleSelectConstant.ERROR_NO_OF_PARTS_REQUIRED, identifierString);
                valid=false;
            }
        }*/
        return valid;
    }


}
