/*
 * Copyright 2012 The Kuali Foundation.
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
package org.kuali.ole.coa.businessobject.defaultvalue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.kuali.ole.coa.businessobject.OleStewardship;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.krad.keyvalues.KeyValuesBase;
import org.kuali.rice.krad.service.KeyValuesService;

public class OleStewardshipValueFinder extends KeyValuesBase {
    @Override
    public List<KeyValue> getKeyValues() {
        KeyValuesService boService = SpringContext.getBean(KeyValuesService.class);
        Collection<OleStewardship> codes = boService.findAll(OleStewardship.class);
        List<KeyValue> labels = new ArrayList<KeyValue>();
        labels.add(new ConcreteKeyValue("", ""));
        for ( OleStewardship oleStewardshipType : codes ) {
          labels.add(new ConcreteKeyValue(oleStewardshipType.getStewardshipTypeCode().toString(), oleStewardshipType.getStewardshipTypeName()));
        }
        return labels;
    }
}
