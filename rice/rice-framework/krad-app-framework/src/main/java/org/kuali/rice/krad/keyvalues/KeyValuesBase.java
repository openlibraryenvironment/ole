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

import org.kuali.rice.core.api.util.KeyValue;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Abstract base implementation of {@link KeyValuesFinder}
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public abstract class KeyValuesBase implements KeyValuesFinder, Serializable {

    public Collection<String> getOptionLabels() {
    	Collection<String> optionLabels = new ArrayList<String>();

    	Collection<KeyValue> keyLabels = getKeyValues();
        for (KeyValue keyLabel : keyLabels) {
        	optionLabels.add(keyLabel.getValue());
        }
        return optionLabels;
    }

    public Collection<String> getOptionValues() {
    	Collection<String> optionValues = new ArrayList<String>();

    	Collection<KeyValue> keyLabels = getKeyValues();
        for (KeyValue keyLabel : keyLabels) {
        	optionValues.add(keyLabel.getKey());
        }
        return optionValues;
    }

    /**
     * @see KeyValuesFinder#getKeyLabelMap()
     */
    @Override
	public Map<String, String> getKeyLabelMap() {
        Map<String, String> keyLabelMap = new HashMap<String, String>();

        List<KeyValue> keyLabels = getKeyValues();
        for (KeyValue keyLabel : keyLabels) {
        	keyLabelMap.put(keyLabel.getKey(), keyLabel.getValue());
        }

        return keyLabelMap;
    }

    /**
     * @see KeyValuesFinder#getKeyLabel(String)
     */
    @Override
	public String getKeyLabel(String key) {
        Map<String, String> keyLabelMap = getKeyLabelMap();

        if (keyLabelMap.containsKey(key)) {
            return keyLabelMap.get(key);
        }
        return null;
    }

    /**
     * @see KeyValuesFinder#getKeyValues(boolean)
     */
    @Override
	public List<KeyValue> getKeyValues(boolean includeActiveOnly){
    	return Collections.emptyList();
    }

    /**
     * @see org.kuali.rice.krad.keyvalues.KeyValuesFinder#clearInternalCache()
     */
	@Override
	public void clearInternalCache() {
		// do nothing
	}

}
