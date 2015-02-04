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

import org.apache.commons.lang.StringUtils;
import org.kuali.ole.module.purap.PurapKeyConstants;
import org.kuali.ole.module.purap.PurapPropertyConstants;
import org.kuali.ole.module.purap.businessobject.PurchaseOrderVendorStipulation;
import org.kuali.ole.module.purap.document.PurchaseOrderDocument;
import org.kuali.ole.sys.OLEConstants;
import org.kuali.ole.sys.document.validation.GenericValidation;
import org.kuali.ole.sys.document.validation.event.AttributedDocumentEvent;
import org.kuali.rice.krad.util.GlobalVariables;

import java.util.List;

public class PurchaseOrderProcessVendorStipulationValidation extends GenericValidation {

    /**
     * Validation for the Stipulation tab.
     *
     * @param poDocument the purchase order document to be validated
     * @return boolean false if the vendor stipulation description is blank.
     */
    public boolean validate(AttributedDocumentEvent event) {
        boolean valid = true;
        List<PurchaseOrderVendorStipulation> stipulations = ((PurchaseOrderDocument) event.getDocument()).getPurchaseOrderVendorStipulations();
        for (int i = 0; i < stipulations.size(); i++) {
            PurchaseOrderVendorStipulation stipulation = stipulations.get(i);
            if (StringUtils.isBlank(stipulation.getVendorStipulationDescription())) {
                GlobalVariables.getMessageMap().putError(OLEConstants.DOCUMENT_PROPERTY_NAME + "." + PurapPropertyConstants.VENDOR_STIPULATION + "[" + i + "]." + PurapPropertyConstants.VENDOR_STIPULATION_DESCRIPTION, PurapKeyConstants.ERROR_STIPULATION_DESCRIPTION);
                valid = false;
            }
        }

        return valid;
    }

}
