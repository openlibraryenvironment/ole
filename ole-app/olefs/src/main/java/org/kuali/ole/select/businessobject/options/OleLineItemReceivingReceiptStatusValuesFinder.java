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
package org.kuali.ole.select.businessobject.options;

import org.kuali.ole.select.businessobject.OleReceiptStatus;
import org.kuali.ole.sys.OLEConstants;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.krad.keyvalues.KeyValuesBase;
import org.kuali.rice.krad.service.KeyValuesService;

import java.util.*;

public class OleLineItemReceivingReceiptStatusValuesFinder extends KeyValuesBase {

    @Override
    public List<KeyValue> getKeyValues() {
        // TODO Auto-generated method stub
        List labels = new ArrayList();
        Map<String, Object> receiptStatusDocTypMap = new HashMap<String, Object>();
        receiptStatusDocTypMap.put(OLEConstants.RCPT_STS_DOC_TYP, OLEConstants.RCV_RCPT_STS_DOC_TYP);
        KeyValuesService boService = SpringContext.getBean(KeyValuesService.class);
        Collection receiptStatusCollection = boService.findMatching(OleReceiptStatus.class, receiptStatusDocTypMap);
        labels.add(new ConcreteKeyValue("", ""));
        for (Iterator iter = receiptStatusCollection.iterator(); iter.hasNext(); ) {
            OleReceiptStatus oleReceiptStatus = (OleReceiptStatus) iter.next();
            labels.add(new ConcreteKeyValue(oleReceiptStatus.getReceiptStatusId().toString(), oleReceiptStatus
                    .getReceiptStatus()));
        }
        return labels;
    }

}
