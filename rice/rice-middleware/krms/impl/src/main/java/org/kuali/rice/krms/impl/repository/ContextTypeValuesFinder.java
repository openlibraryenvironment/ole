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
package org.kuali.rice.krms.impl.repository;

import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krad.service.KeyValuesService;
import org.kuali.rice.krad.uif.control.UifKeyValuesFinderBase;
import org.kuali.rice.krad.uif.view.ViewModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class that returns all valid types for contexts.
 */
public class ContextTypeValuesFinder extends UifKeyValuesFinderBase {

    private boolean blankOption;

    /**
     * @return the blankOption
     */
    public boolean isBlankOption() {
        return this.blankOption;
    }

    /**
     * @param blankOption the blankOption to set
     */
    public void setBlankOption(boolean blankOption) {
        this.blankOption = blankOption;
    }

    @Override
    public List<KeyValue> getKeyValues(ViewModel model) {
        List<KeyValue> keyValues = new ArrayList<KeyValue>();

        if(blankOption){
            keyValues.add(new ConcreteKeyValue("", ""));
        }

        // ToDo: Currently we hardcoded any types named "CONTEXT" to be valid with contexts.
        KeyValuesService boService = KRADServiceLocator.getKeyValuesService();
        Map<String,Object> fieldValues = new HashMap<String, Object>();
        fieldValues.put("name", "CONTEXT");
        Collection<KrmsTypeBo> types = boService.findMatching(KrmsTypeBo.class, fieldValues);

        for (KrmsTypeBo type : types) {
            keyValues.add(new ConcreteKeyValue(type.getId(), type.getName() + " [" + type.getNamespace() + "]"));
        }

        return keyValues;
    }
}
