/**
 * Copyright 2005-2014 The Kuali Foundation
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
package org.kuali.rice.krad.keyvalues;

import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.krad.service.KRADServiceLocatorWeb;

import java.util.ArrayList;
import java.util.List;

/**
 * This class returns list of business objects defined in the data dictionary.
 */
public class BusinessObjectDictionaryEntriesFinder extends KeyValuesBase {

    /**
     * @see org.kuali.rice.krad.keyvalues.KeyValuesFinder#getKeyValues()
     */
    @Override
	public List<KeyValue> getKeyValues() {
        List<String> businessObjects =
                KRADServiceLocatorWeb.getDataDictionaryService().getDataDictionary().getBusinessObjectClassNames();
        List<KeyValue> boKeyLabels = new ArrayList<KeyValue>();

        for (String string : businessObjects) {
            String className = string;
            boKeyLabels.add(new ConcreteKeyValue(className, className));
        }

        return boKeyLabels;
    }

}
