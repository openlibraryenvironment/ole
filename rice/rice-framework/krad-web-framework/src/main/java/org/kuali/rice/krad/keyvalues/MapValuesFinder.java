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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** a simple values finder that uses a map as the key/value source. */
public class MapValuesFinder implements KeyValuesFinder {

    private Map<String, String> keyValues;

    public MapValuesFinder(Map<String, String> keyValues) {
        setKeyValues(keyValues);
    }

    public void setKeyValues(Map<String, String> keyValues) {
        if (keyValues == null) {
            throw new IllegalArgumentException("keyValues was null");
        }
        this.keyValues = Collections.unmodifiableMap(new HashMap<String, String>(keyValues));
    }

    @Override
    public List<KeyValue> getKeyValues() {
        final List<KeyValue> list = new ArrayList<KeyValue>();
        for (Map.Entry<String, String> entry : keyValues.entrySet()) {
            list.add(new ConcreteKeyValue(entry));
        }
        return Collections.unmodifiableList(list);
    }

    @Override
    public List<KeyValue> getKeyValues(boolean includeActiveOnly) {
        return getKeyValues();
    }

    @Override
    public Map<String, String> getKeyLabelMap() {
        return keyValues;
    }

    @Override
    public String getKeyLabel(String key) {
        return keyValues.get(key);
    }

    @Override
    public void clearInternalCache() {
        //do nothing
    }
}
