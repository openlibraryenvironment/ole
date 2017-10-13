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
package org.kuali.rice.kim.api.identity.principal;

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
public class StringToKimEntityNamePrincipalInfoMapAdapter extends XmlAdapter<StringToKimEntityNamePrincipalInfoMapAdapter.StringEntNmPrncpInfoMapEntry[], Map<String, EntityNamePrincipalName>> {

	/**
	 * This overridden method ...
	 * 
	 * @see javax.xml.bind.annotation.adapters.XmlAdapter#marshal(java.lang.Object)
	 */
	@Override
	public StringEntNmPrncpInfoMapEntry[] marshal(Map<String, EntityNamePrincipalName> map) throws Exception {
		if(null == map) return null;
		StringEntNmPrncpInfoMapEntry[] entryArray = new StringEntNmPrncpInfoMapEntry[map.size()];
		int i = 0;
		for (Map.Entry<String, EntityNamePrincipalName> e : map.entrySet()) {
			entryArray[i] = new StringEntNmPrncpInfoMapEntry(e.getKey(), e.getValue());
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
	public Map<String, EntityNamePrincipalName> unmarshal(StringEntNmPrncpInfoMapEntry[] entryArray) throws Exception {
		if (null == entryArray) return null;
		Map<String, EntityNamePrincipalName> resultMap = new HashMap<String, EntityNamePrincipalName>(entryArray.length);
		for (int i = 0; i < entryArray.length; i++) {
			StringEntNmPrncpInfoMapEntry entry = entryArray[i];
			resultMap.put(entry.getKey(), entry.getValue());
		}
		return resultMap;
	}

    public static class StringEntNmPrncpInfoMapEntry extends AbstractDataTransferObject {

        private static final long serialVersionUID = 1L;

        @XmlAttribute
        private final String key;

        @XmlElement(required=true)
        private final EntityNamePrincipalName value;

        @SuppressWarnings("unused")
        @XmlAnyElement
        private final Collection<Element> _futureElements = null;

        /**
         * Private constructor used only by JAXB.
         */
        private StringEntNmPrncpInfoMapEntry() {
            key = null;
            value = null;
        }

        public StringEntNmPrncpInfoMapEntry(String key, EntityNamePrincipalName value) {
            super();

            this.key = key;
            this.value = value;
        }

        public String getKey() {
            return key;
        }

        public EntityNamePrincipalName getValue() {
            return value;
        }
    }
}
