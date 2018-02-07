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
package org.kuali.rice.krad.uif.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Map implementation takes a <code>Set</code> of Strings and converts to Map
 * where the string is the map key and value is the Boolean true, convenience
 * collection for expression language
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class BooleanMap extends HashMap<String, Boolean> {
    private static final long serialVersionUID = 4042557657401395547L;

    public BooleanMap(Map<? extends String, ? extends Boolean> m) {
        super(m);
    }

    public BooleanMap(Set<String> keys) {
        super();

        for (String key : keys) {
            this.put(key, Boolean.TRUE);
        }
    }

    /**
     * Overrides the get method to return Boolean false if the key does not
     * exist in the Map
     *
     * @see java.util.HashMap#get(java.lang.Object)
     */
    @Override
    public Boolean get(Object key) {
        if (super.containsKey(key)) {
            return super.get(key);
        }

        return Boolean.FALSE;
    }

}
