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
package org.kuali.rice.coreservice.web.parameter;

import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.coreservice.impl.parameter.ParameterTypeBo;
import org.kuali.rice.krad.keyvalues.KeyValuesBase;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krad.service.KeyValuesService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ParameterTypeValuesFinder extends KeyValuesBase {

    @Override
    public List<KeyValue> getKeyValues() {

        // get a list of all ParameterTypes
        KeyValuesService boService = KRADServiceLocator.getKeyValuesService();
        List<ParameterTypeBo> bos = (List<ParameterTypeBo>) boService.findAll(ParameterTypeBo.class);
        // copy the list of codes before sorting, since we can't modify the results from this method
        if (bos == null) {
            return Collections.emptyList();
        }
        final List<ParameterTypeBo> toReturn = new ArrayList<ParameterTypeBo>(bos);

        // sort using comparator.
        Collections.sort(bos, ParameterTypeComparator.INSTANCE);

        // create a new list (code, descriptive-name)
        List<KeyValue> labels = new ArrayList<KeyValue>(bos.size());

        for (ParameterTypeBo bo : bos) {
            labels.add(new ConcreteKeyValue(bo.getCode(), bo.getName()));
        }

        return labels;
    }

    private static class ParameterTypeComparator implements Comparator<ParameterTypeBo> {
        public static final Comparator<ParameterTypeBo> INSTANCE = new ParameterTypeComparator();

        @Override
        public int compare(ParameterTypeBo o1, ParameterTypeBo o2) {
            return o1.getCode().compareTo(o2.getCode());
        }

    }
}
