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

import org.kuali.ole.module.purap.PurapKeyConstants;
import org.kuali.ole.module.purap.PurapPropertyConstants;
import org.kuali.ole.module.purap.document.VendorCreditMemoDocument;
import org.kuali.ole.select.document.OleVendorCreditMemoDocument;
import org.kuali.ole.sys.OLEPropertyConstants;
import org.kuali.ole.sys.document.validation.GenericValidation;
import org.kuali.ole.sys.document.validation.event.AttributedDocumentEvent;
import org.kuali.rice.krad.util.GlobalVariables;

public class VendorCreditMemoTotalOverZeroValidation extends GenericValidation {

    public boolean validate(AttributedDocumentEvent event) {
        boolean valid = true;
        if (((OleVendorCreditMemoDocument) event.getDocument()).getInvoiceIdentifier() == null) {
            if (!((VendorCreditMemoDocument) event.getDocument()).getGrandTotal().isPositive()) {
                GlobalVariables.getMessageMap().putError(OLEPropertyConstants.DOCUMENT + "." + PurapPropertyConstants.ITEM, PurapKeyConstants.ERROR_CREDIT_MEMO_TOTAL_ZERO);
                valid = false;
            }
        }

        return valid;
    }

}
