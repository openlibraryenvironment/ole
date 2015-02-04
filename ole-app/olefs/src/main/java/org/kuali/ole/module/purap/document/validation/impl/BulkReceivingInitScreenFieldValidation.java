/*
 * Copyright 2008-2009 The Kuali Foundation
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

import org.kuali.ole.module.purap.document.BulkReceivingDocument;
import org.kuali.ole.sys.OLEKeyConstants;
import org.kuali.ole.sys.OLEPropertyConstants;
import org.kuali.ole.sys.document.validation.GenericValidation;
import org.kuali.ole.sys.document.validation.event.AttributedDocumentEvent;
import org.kuali.rice.kns.service.DictionaryValidationService;
import org.kuali.rice.krad.util.GlobalVariables;

public class BulkReceivingInitScreenFieldValidation extends GenericValidation {

    private DictionaryValidationService dictionaryValidationService;

    public boolean validate(AttributedDocumentEvent event) {

        BulkReceivingDocument bulkReceivingDocument = (BulkReceivingDocument) event.getDocument();

        GlobalVariables.getMessageMap().clearErrorPath();
        GlobalVariables.getMessageMap().addToErrorPath(OLEPropertyConstants.DOCUMENT);

        dictionaryValidationService.validateAttributeFormat("BulkReceivingDocument", "shipmentPackingSlipNumber", bulkReceivingDocument.getShipmentPackingSlipNumber(), OLEKeyConstants.ERROR_INVALID_FORMAT);
        dictionaryValidationService.validateAttributeFormat("BulkReceivingDocument", "shipmentBillOfLadingNumber", bulkReceivingDocument.getShipmentBillOfLadingNumber(), OLEKeyConstants.ERROR_INVALID_FORMAT);

        return true;
    }

    public DictionaryValidationService getDictionaryValidationService() {
        return dictionaryValidationService;
    }

    public void setDictionaryValidationService(DictionaryValidationService dictionaryValidationService) {
        this.dictionaryValidationService = dictionaryValidationService;
    }

}
