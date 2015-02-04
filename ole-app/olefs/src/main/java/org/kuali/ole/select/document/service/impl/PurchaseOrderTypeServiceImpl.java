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
package org.kuali.ole.select.document.service.impl;

import org.kuali.ole.module.purap.businessobject.PurchaseOrderType;
import org.kuali.ole.select.document.service.PurchaseOrderTypeService;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.rice.krad.service.BusinessObjectService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PurchaseOrderTypeServiceImpl implements PurchaseOrderTypeService {

    public String getPurchaseOrderType(Integer purchaseOrderTypeId) throws Exception {
        String purchaseOrderType = null;
        Map purchaseOrderTypeMap = new HashMap();
        purchaseOrderTypeMap.put("purchaseOrderTypeId", purchaseOrderTypeId);
        List<PurchaseOrderType> purchaseOrderTypeList = (List) SpringContext.getBean(BusinessObjectService.class).findMatching(PurchaseOrderType.class, purchaseOrderTypeMap);
        if (purchaseOrderTypeList.iterator().hasNext()) {
            purchaseOrderType = purchaseOrderTypeList.iterator().next().getPurchaseOrderType();
        }
        return purchaseOrderType;
    }
}
