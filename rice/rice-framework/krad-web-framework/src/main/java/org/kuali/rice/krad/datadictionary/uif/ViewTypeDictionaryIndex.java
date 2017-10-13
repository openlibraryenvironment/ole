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
package org.kuali.rice.krad.datadictionary.uif;

import org.kuali.rice.krad.datadictionary.DataDictionaryException;

import java.util.HashMap;
import java.util.Map;

/**
 * Holds view index information for a view type, where the index keys are built
 * from the supported view type parameters
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class ViewTypeDictionaryIndex {
    private Map<String, String> viewIndex;

    public ViewTypeDictionaryIndex() {
        viewIndex = new HashMap<String, String>();
    }

    public Map<String, String> getViewIndex() {
        return this.viewIndex;
    }

    public void setViewIndex(Map<String, String> viewIndex) {
        this.viewIndex = viewIndex;
    }

    public void put(String index, String beanName) {
        if (viewIndex.containsKey(index)) {
            throw new DataDictionaryException("Two Views must not share the same type index: " + index);
        }

        viewIndex.put(index, beanName);
    }

    public String get(String index) {
        if (viewIndex.containsKey(index)) {
            return viewIndex.get(index);
        }

        return null;
    }

}
