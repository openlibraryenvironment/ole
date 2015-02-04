/*
 * Copyright 2013 The Kuali Foundation.
 *
 * Licensed under the Educational Community License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.opensource.org/licenses/ecl1.php
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kuali.ole.select.document.validation.impl;

import org.kuali.ole.module.purap.PurapKeyConstants;
import org.kuali.ole.module.purap.PurapPropertyConstants;
import org.kuali.ole.select.businessobject.OleCreditMemoItem;
import org.kuali.ole.sys.OLEPropertyConstants;
import org.kuali.ole.sys.document.validation.GenericValidation;
import org.kuali.ole.sys.document.validation.event.AttributedDocumentEvent;
import org.kuali.rice.krad.util.GlobalVariables;

public class OleVendorCreditMemoFlagLineItemTotalsValidation extends GenericValidation {

    private OleCreditMemoItem itemForValidation;

    @Override
    public boolean validate(AttributedDocumentEvent event) {
        if (itemForValidation.getItemSurcharge() == null) {
            if (itemForValidation.getItemQuantity() != null && itemForValidation.getExtendedPrice() != null && itemForValidation.calculateExtendedPrice().compareTo(itemForValidation.getExtendedPrice()) != 0) {
                String errorKey = OLEPropertyConstants.DOCUMENT + "." + PurapPropertyConstants.ITEM + "[" + (itemForValidation.getItemLineNumber() - 1) + "]." + PurapPropertyConstants.EXTENDED_PRICE;
                GlobalVariables.getMessageMap().putError(errorKey, PurapKeyConstants.ERROR_PAYMENT_REQUEST_ITEM_TOTAL_NOT_EQUAL);
            }
        }

        return true;
    }

    public OleCreditMemoItem getItemForValidation() {
        return itemForValidation;
    }

    public void setItemForValidation(OleCreditMemoItem itemForValidation) {
        this.itemForValidation = itemForValidation;
    }

}
