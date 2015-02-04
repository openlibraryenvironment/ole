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
package org.kuali.ole.select.businessobject.options;

import org.kuali.ole.select.businessobject.OleReceiptStatus;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.krad.keyvalues.KeyValuesBase;
import org.kuali.rice.krad.service.KeyValuesService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class OleReceiptStatusValuesFinder extends KeyValuesBase {


    @Override
    public List getKeyValues() {

        KeyValuesService boService = SpringContext.getBean(KeyValuesService.class);
        Collection codes = boService.findAll(OleReceiptStatus.class);
        Iterator iterator = codes.iterator();
        List labels = new ArrayList();
        labels.add(new ConcreteKeyValue("", ""));
        while (iterator.hasNext()) {
            OleReceiptStatus oleReceiptStatus = (OleReceiptStatus) iterator.next();
            labels.add(new ConcreteKeyValue(oleReceiptStatus.getReceiptStatusId().toString(), oleReceiptStatus
                    .getReceiptStatus()));
        }
        return labels;

    }
}

