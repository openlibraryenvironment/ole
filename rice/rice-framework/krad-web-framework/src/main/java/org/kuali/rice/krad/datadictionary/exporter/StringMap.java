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
package org.kuali.rice.krad.datadictionary.exporter;

import org.kuali.rice.core.api.exception.RiceRuntimeException;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Adds a litle strong type-checking and validation on top of the generic LinkedHashMap
 */
@Deprecated
public class StringMap extends LinkedHashMap<String, Object> {
    private static final long serialVersionUID = 7364206011639131063L;

    /**
     * Associates the given String with the given Map value.
     *
     * @param key
     * @param value
     */
    public void set(String key, Map<String, Object> value) {
        setUnique(key, value);
    }

    /**
     * Associates the given String with the given String value.
     *
     * @param key
     * @param value
     */
    public void set(String key, String value) {
        setUnique(key, value);
    }

    /**
     * Verifies that the key isn't blank, and that the value isn't null, and prevents duplicate keys from being used.
     *
     * @param key
     * @param value
     */
    private void setUnique(String key, Object value) {
        if (value == null) {
            throw new IllegalArgumentException("invalid (null) value");
        }

        if (containsKey(key)) {
            throw new RiceRuntimeException("duplicate key '" + key + "'");
        }

        super.put(key, value);
    }

    @Override
    public Object put(String key, Object value) {
        throw new UnsupportedOperationException("direct calls to put not supported");
    }

    @Override
    public void putAll(Map<? extends String, ? extends Object> m) {
        throw new UnsupportedOperationException("direct calls to put not supported");
    }
}
