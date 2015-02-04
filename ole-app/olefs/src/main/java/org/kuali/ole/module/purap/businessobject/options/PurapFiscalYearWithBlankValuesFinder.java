/*
 * Copyright 2009 The Kuali Foundation
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

import org.kuali.ole.sys.OLEConstants;
import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.core.api.util.KeyValue;

import java.util.List;

public class PurapFiscalYearWithBlankValuesFinder extends PurapFiscalYearValuesFinder {

    public List getKeyValues() {
        KeyValue blankPair = new ConcreteKeyValue(OLEConstants.EMPTY_STRING, OLEConstants.EMPTY_STRING);
        List keyValuePairs = super.getKeyValues();
        keyValuePairs.add(blankPair);
        return keyValuePairs;
    }

}
