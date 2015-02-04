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
import org.kuali.ole.module.purap.PurapPropertyConstants;
import org.kuali.ole.select.businessobject.OleRequisitionItem;
import org.kuali.ole.sys.OLEConstants;
import org.kuali.ole.sys.OLEKeyConstants;
import org.kuali.ole.sys.document.validation.event.AttributedDocumentEvent;
import org.kuali.rice.kns.service.DataDictionaryService;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.util.GlobalVariables;

public class PurchasingAddBibInfoValidation extends PurchasingBibInfoAddItemValidation {

    private BusinessObjectService businessObjectService;
    private DataDictionaryService dataDictionaryService;

    public boolean validate(AttributedDocumentEvent event) {
        boolean valid = true;
        GlobalVariables.getMessageMap().addToErrorPath(PurapPropertyConstants.NEW_PURCHASING_ITEM_LINE);
        OleRequisitionItem refreshedItem = getItemForValidation();
        refreshedItem.refreshReferenceObject("itemType");
        super.setItemForValidation(refreshedItem);

        valid &= super.validate(event);
        //valid &= validateItemTitle(getItemForValidation());

        GlobalVariables.getMessageMap().removeFromErrorPath(PurapPropertyConstants.NEW_PURCHASING_ITEM_LINE);

        return valid;
    }

    public boolean validateItemTitle(OleRequisitionItem item) {
        boolean valid = true;
        if (StringUtils.isEmpty(item.getBibInfoBean().getTitle())) {
            valid = false;
            String attributeLabel = dataDictionaryService.
                    getDataDictionary().getBusinessObjectEntry(item.getBibInfoBean().getClass().getName()).
                    getAttributeDefinition(OLEConstants.BibInfoBean.ITEM_TITLE).getLabel();
            GlobalVariables.getMessageMap().putError(OLEConstants.BibInfoBean.ITEM_TITLE, OLEKeyConstants.ERROR_REQUIRED, attributeLabel + " in " + item.getItemIdentifierString());
        }
        return valid;
    }

    public BusinessObjectService getBusinessObjectService() {
        return businessObjectService;
    }

    public void setBusinessObjectService(BusinessObjectService businessObjectService) {
        this.businessObjectService = businessObjectService;
    }

    public DataDictionaryService getDataDictionaryService() {
        return dataDictionaryService;
    }

    public void setDataDictionaryService(DataDictionaryService dataDictionaryService) {
        this.dataDictionaryService = dataDictionaryService;
    }

}

