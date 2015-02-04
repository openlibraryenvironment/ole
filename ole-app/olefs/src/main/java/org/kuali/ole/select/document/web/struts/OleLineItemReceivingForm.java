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
package org.kuali.ole.select.document.web.struts;

import org.kuali.ole.module.purap.PurapConstants;
import org.kuali.ole.module.purap.businessobject.LineItemReceivingItem;
import org.kuali.ole.module.purap.document.LineItemReceivingDocument;
import org.kuali.ole.module.purap.document.web.struts.LineItemReceivingForm;
import org.kuali.ole.select.businessobject.OleLineItemReceivingItem;
import org.kuali.ole.select.businessobject.OleRequisitionCopies;

/**
 * This class is the KualiForm class for Ole Line Item Receiving
 */
public class OleLineItemReceivingForm extends LineItemReceivingForm {

    private OleRequisitionCopies newOleCopies = new OleRequisitionCopies();


    /**
     * Gets the newOleCopies attribute.
     *
     * @return Returns the newOleCopies.
     */
    public OleRequisitionCopies getNewOleCopies() {
        return newOleCopies;
    }

    /**
     * Sets the newOleCopies attribute value.
     *
     * @param newOleCopies The newOleCopies to set.
     */
    public void setNewOleCopies(OleRequisitionCopies newOleCopies) {
        this.newOleCopies = newOleCopies;
    }

    /**
     * This method is overridden to return OleLineItemReceivingItem as the Receiving Line Item
     *
     * @return LineItemReceivingItem
     * @see org.kuali.ole.module.purap.document.web.struts.LineItemReceivingForm#setupNewLineIemReceivingItemLine()
     */
    @Override
    public LineItemReceivingItem setupNewLineItemReceivingItemLine() {
        return new OleLineItemReceivingItem();
    }

    /**
     * This method is used to set ItemTypeCode for the new receiving lineitem
     *
     * @return LineItemReceivingItem
     * @see org.kuali.ole.module.purap.document.web.struts.LineItemReceivingForm#setupNewReceivingItemLine()
     */
    @Override
    public LineItemReceivingItem setupNewReceivingItemLine() {
        OleLineItemReceivingItem lineItemReceivingItem = new OleLineItemReceivingItem((LineItemReceivingDocument) getDocument());
        newLineItemReceivingItemLine.setItemTypeCode(PurapConstants.ItemTypeCodes.ITEM_TYPE_UNORDERED_ITEM_CODE);
        lineItemReceivingItem.setItemUnitOfMeasureCode(PurapConstants.LineItemReceivingDocumentStrings.CUSTOMER_INVOICE_DETAIL_UOM_DEFAULT);
        return lineItemReceivingItem;
    }
}
