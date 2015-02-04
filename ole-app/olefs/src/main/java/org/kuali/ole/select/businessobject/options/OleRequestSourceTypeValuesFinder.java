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

import org.kuali.ole.select.OleSelectConstant;
import org.kuali.ole.select.businessobject.OleRequestSourceType;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.krad.keyvalues.KeyValuesBase;
import org.kuali.rice.krad.service.KeyValuesService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class OleRequestSourceTypeValuesFinder extends KeyValuesBase {


    @Override
    public List getKeyValues() {

        KeyValuesService boService = SpringContext.getBean(KeyValuesService.class);
        Collection codes = boService.findAll(OleRequestSourceType.class);
        Iterator iterator = codes.iterator();
        List labels = new ArrayList();

        while (iterator.hasNext()) {
            OleRequestSourceType oleRequestSourceType = (OleRequestSourceType) iterator.next();
            if (oleRequestSourceType.getRequestSourceType().equalsIgnoreCase(OleSelectConstant.REQUEST_SRC_TYPE_STAFF)) {
                labels.add(new ConcreteKeyValue(oleRequestSourceType.getRequestSourceTypeId().toString(), oleRequestSourceType.getRequestSourceType()));
                break;
            }
        }
        iterator = codes.iterator();
        while (iterator.hasNext()) {
            OleRequestSourceType oleRequestSourceType = (OleRequestSourceType) iterator.next();
            if (!oleRequestSourceType.getRequestSourceType().equalsIgnoreCase(OleSelectConstant.REQUEST_SRC_TYPE_STAFF))
                labels.add(new ConcreteKeyValue(oleRequestSourceType.getRequestSourceTypeId().toString(), oleRequestSourceType.getRequestSourceType()));
        }
        return labels;

    }
}

