/*
 * Copyright 2008 The Kuali Foundation
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

import org.kuali.ole.module.purap.PurapConstants;
import org.kuali.ole.module.purap.PurapKeyConstants;
import org.kuali.ole.module.purap.businessobject.PurApItem;
import org.kuali.ole.module.purap.businessobject.PurchaseOrderItem;
import org.kuali.ole.module.purap.document.PurchasingAccountsPayableDocument;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.ole.sys.document.validation.GenericValidation;
import org.kuali.ole.sys.document.validation.event.AttributedDocumentEvent;
import org.kuali.rice.kew.api.doctype.DocumentTypeService;
import org.kuali.rice.krad.util.GlobalVariables;

import java.util.List;

public class PurchaseOrderAmendmentContainsAtLeastOneActiveItemValidation extends GenericValidation {

    /**
     * Validates that the given document contains at least one active item.
     *
     * @param purapDocument A PurchasingAccountsPayableDocument. (Should contain PurchaseOrderItems.)
     * @return True if the document contains at least one active item
     */
    public boolean validate(AttributedDocumentEvent event) {
        PurchasingAccountsPayableDocument purapDocument = (PurchasingAccountsPayableDocument) event.getDocument();
        List<PurApItem> items = purapDocument.getItems();
        for (PurApItem item : items) {
            if (((PurchaseOrderItem) item).isItemActiveIndicator() && (!((PurchaseOrderItem) item).isEmpty() && item.getItemType().isLineItemIndicator())) {
                return true;
            }
        }
        String documentType = getDocumentTypeLabel(purapDocument.getDocumentHeader().getWorkflowDocument().getDocumentTypeName());
        GlobalVariables.getMessageMap().putError(PurapConstants.ITEM_TAB_ERROR_PROPERTY, PurapKeyConstants.ERROR_NO_ACTIVE_ITEMS, documentType);

        return false;
    }

    protected String getDocumentTypeLabel(String documentTypeName) {
        return SpringContext.getBean(DocumentTypeService.class).getDocumentTypeByName(documentTypeName).getLabel();

    }

}
