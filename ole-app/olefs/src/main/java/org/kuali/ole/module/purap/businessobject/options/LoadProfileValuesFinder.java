/*
 * Copyright 2011 The Kuali Foundation.
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
package org.kuali.ole.module.purap.businessobject.options;

import org.kuali.ole.select.businessobject.OleLoadProfile;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.krad.keyvalues.KeyValuesBase;
import org.kuali.rice.krad.service.KeyValuesService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Value Finder for Item Types.
 */
public class LoadProfileValuesFinder extends KeyValuesBase {

    @Override
    public List<KeyValue> getKeyValues() {
        KeyValuesService keyValuesService = SpringContext.getBean(KeyValuesService.class);
        List<KeyValue> labels = new ArrayList<KeyValue>();
        Collection<OleLoadProfile> profiles = keyValuesService.findAll(OleLoadProfile.class);
        for (OleLoadProfile oleLoadProfile : profiles) {
            labels.add(new ConcreteKeyValue(oleLoadProfile.getProfileId().toString(), oleLoadProfile.getProfile()));

        }
        return labels;
    }
}
