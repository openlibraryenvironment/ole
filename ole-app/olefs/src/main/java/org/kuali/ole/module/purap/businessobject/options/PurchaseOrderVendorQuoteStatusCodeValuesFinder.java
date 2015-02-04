/*
 * Copyright 2007 The Kuali Foundation
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
package org.kuali.ole.module.purap.businessobject.options;

import org.kuali.ole.module.purap.businessobject.PurchaseOrderQuoteStatus;
import org.kuali.ole.module.purap.document.service.PurchaseOrderService;
import org.kuali.ole.sys.OLEConstants;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.krad.keyvalues.KeyValuesBase;

import java.util.ArrayList;
import java.util.List;

/**
 * Value Finder for Purchase Order Vendor Quote Statuses.
 */
public class PurchaseOrderVendorQuoteStatusCodeValuesFinder extends KeyValuesBase {

    /**
     * Returns code/description pairs of all Purchase Order Vendor Quote Statuses.
     *
     * @see org.kuali.rice.kns.lookup.keyvalues.KeyValuesFinder#getKeyValues()
     */
    public List getKeyValues() {
        List keyValues = new ArrayList();
        keyValues.add(new ConcreteKeyValue(OLEConstants.EMPTY_STRING, OLEConstants.EMPTY_STRING));
        List<PurchaseOrderQuoteStatus> poQuoteStatuses = SpringContext.getBean(PurchaseOrderService.class).getPurchaseOrderQuoteStatusCodes();
        for (PurchaseOrderQuoteStatus status : poQuoteStatuses) {
            keyValues.add(new ConcreteKeyValue(status.getStatusCode(), status.getStatusDescription()));
        }
        return keyValues;
    }
}
