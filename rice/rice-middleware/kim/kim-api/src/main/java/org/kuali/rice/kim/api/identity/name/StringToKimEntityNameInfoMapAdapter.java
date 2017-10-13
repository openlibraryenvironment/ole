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
package org.kuali.rice.kim.api.identity.name;

import org.kuali.rice.core.api.mo.AbstractDataTransferObject;
import org.w3c.dom.Element;

import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Do jax-ws mapping of Map<String, String> for KIM service method parameters, etc.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class StringToKimEntityNameInfoMapAdapter extends XmlAdapter<StringToKimEntityNameInfoMapAdapter.StringEntityNameInfoMapEntry[], Map<String, EntityName>> {

	/**
	 * This overridden method ...
	 * 
	 * @see javax.xml.bind.annotation.adapters.XmlAdapter#marshal(java.lang.Object)
	 */
	@Override
	public StringEntityNameInfoMapEntry[] marshal(Map<String, EntityName> map) throws Exception {
		if(null == map) return null;
		StringEntityNameInfoMapEntry[] entryArray = new StringEntityNameInfoMapEntry[map.size()];
		int i = 0;
		for (Map.Entry<String, EntityName> e : map.entrySet()) {
			entryArray[i] = new StringEntityNameInfoMapEntry(e.getKey(), e.getValue());
			i++;
		}
		return entryArray;
	}

	/**
	 * This overridden method ...
	 * 
	 * @see javax.xml.bind.annotation.adapters.XmlAdapter#unmarshal(java.lang.Object)
	 */
	@Override
	public Map<String, EntityName> unmarshal(StringEntityNameInfoMapEntry[] entryArray) throws Exception {
		if (null == entryArray) return null;
		Map<String, EntityName> resultMap = new HashMap<String, EntityName>(entryArray.length);
		for (int i = 0; i < entryArray.length; i++) {
			StringEntityNameInfoMapEntry entry = entryArray[i];
			resultMap.put(entry.getKey(), entry.getValue());
		}
		return resultMap;
	}

    public static class StringEntityNameInfoMapEntry extends AbstractDataTransferObject {

        private static final long serialVersionUID = 1L;

        @XmlAttribute
        private final String key;

        @XmlElement(required=true)
        private final EntityName value;

        @SuppressWarnings("unused")
        @XmlAnyElement
        private final Collection<Element> _futureElements = null;

        /**
         * Private constructor used only by JAXB.
         */
        public StringEntityNameInfoMapEntry() {
            key = null;
            value = null;
        }

        public StringEntityNameInfoMapEntry(String key, EntityName value) {
            super();

            this.key = key;
            this.value = value;
        }

        public String getKey() {
            return key;
        }

        public EntityName getValue() {
            return value;
        }
    }
}
