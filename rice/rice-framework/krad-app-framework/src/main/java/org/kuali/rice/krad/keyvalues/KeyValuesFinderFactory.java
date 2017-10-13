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

import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.core.api.util.KeyValue;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/** a factory for creating key-values finders. */
public final class KeyValuesFinderFactory {
    private KeyValuesFinderFactory() {
        throw new UnsupportedOperationException("do not call");
    }

    public static KeyValuesFinder fromMap(Map<String, String> map) {
        if (map == null) {
            throw new IllegalArgumentException("map is null");
        }

        return new MapBased(map);
    }

    private static final class MapBased implements KeyValuesFinder {
        private final Map<String, String> map;

        private MapBased(Map<String, String> map) {
            this.map = ImmutableMap.copyOf(map);
        }

        @Override
        public List<KeyValue> getKeyValues() {
            Collection<KeyValue> kvs = Collections2.transform(map.entrySet(), new Function<Map.Entry<String, String>, KeyValue>() {
                @Override
                public KeyValue apply(Map.Entry<String, String> input) {
                    return new ConcreteKeyValue(input);
                }
            });
            return ImmutableList.copyOf(kvs);
        }

        @Override
        public List<KeyValue> getKeyValues(boolean includeActiveOnly) {
            return getKeyValues();
        }

        @Override
        public Map<String, String> getKeyLabelMap() {
            return map;
        }

        @Override
        public String getKeyLabel(String key) {
            return map.get(key);
        }

        @Override
        public void clearInternalCache() {
            //do nothing
        }
    }
}
