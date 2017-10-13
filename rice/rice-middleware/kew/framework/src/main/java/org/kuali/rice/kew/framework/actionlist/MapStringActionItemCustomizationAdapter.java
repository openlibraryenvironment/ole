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
package org.kuali.rice.kew.framework.actionlist;

import org.kuali.rice.core.api.mo.AbstractDataTransferObject;
import org.kuali.rice.kew.api.action.ActionItemCustomization;
import org.w3c.dom.Element;

import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Do jax-ws mapping of Map<String, ActionItemCustomization> for KIM service method parameters, etc.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class MapStringActionItemCustomizationAdapter extends XmlAdapter<MapStringActionItemCustomizationAdapter.StringActionItemCustomizationMapEntry[], Map<String, ActionItemCustomization>> {

    /**
     * converts the map to an array
     *
     * @see javax.xml.bind.annotation.adapters.XmlAdapter#marshal(java.lang.Object)
     */
    @Override
    public StringActionItemCustomizationMapEntry[] marshal(Map<String, ActionItemCustomization> map) throws Exception {
        if(null == map) return null;
        StringActionItemCustomizationMapEntry[] entryArray = new StringActionItemCustomizationMapEntry[map.size()];
        int i = 0;
        for (Map.Entry<String, ActionItemCustomization> e : map.entrySet()) {
            entryArray[i] = new StringActionItemCustomizationMapEntry(e.getKey(), e.getValue());
            i++;
        }
        return entryArray;
    }

    /**
     * converts the array back to a map
     *
     * @see javax.xml.bind.annotation.adapters.XmlAdapter#unmarshal(java.lang.Object)
     */
    @Override
    public Map<String, ActionItemCustomization> unmarshal(StringActionItemCustomizationMapEntry[] entryArray) throws Exception {
        if (null == entryArray) return null;
        Map<String, ActionItemCustomization> resultMap = new HashMap<String, ActionItemCustomization>(entryArray.length);
        for (int i = 0; i < entryArray.length; i++) {
            StringActionItemCustomizationMapEntry entry = entryArray[i];
            resultMap.put(entry.getKey(), entry.getValue());
        }
        return resultMap;
    }

    public static class StringActionItemCustomizationMapEntry extends AbstractDataTransferObject {

        private static final long serialVersionUID = 1L;

        @XmlAttribute
        private final String key;

        @XmlElement(required=true)
        private final ActionItemCustomization value;

        @SuppressWarnings("unused")
        @XmlAnyElement
        private final Collection<Element> _futureElements = null;

        /**
         * constructor used by JAXB.
         */
        public StringActionItemCustomizationMapEntry() {
            key = null;
            value = null;
        }

        public StringActionItemCustomizationMapEntry(String key, ActionItemCustomization value) {
            super();

            this.key = key;
            this.value = value;
        }

        public String getKey() {
            return key;
        }

        public ActionItemCustomization getValue() {
            return value;
        }
    }
}
