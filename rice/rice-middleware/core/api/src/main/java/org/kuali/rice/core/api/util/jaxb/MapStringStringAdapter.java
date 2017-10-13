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
package org.kuali.rice.core.api.util.jaxb;

import org.kuali.rice.core.api.mo.AbstractDataTransferObject;
import org.w3c.dom.Element;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Do JAXB mapping of Map<String, String> to a format like the following for a
 * map containing { key1:value1, key2:value2 }:
 * 
 * <pre>
 * {@code
 * <...>
 *   <entry key="key1">value1</entry>
 *   <entry key="key2">value2</entry>
 * </...>
 * }
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class MapStringStringAdapter extends XmlAdapter<MapStringStringAdapter.StringMapEntryList, Map<String, String>> {

	@Override
	public StringMapEntryList marshal(Map<String, String> map) throws Exception {
		if (map == null) {
			return null;
		}
		List<StringMapEntry> entries = new ArrayList<StringMapEntry>();
		for (Map.Entry<String, String> entry : map.entrySet()) {
			entries.add(new StringMapEntry(entry));
		}
		return new StringMapEntryList(entries);
	}

	@Override
	public Map<String, String> unmarshal(StringMapEntryList entryList) throws Exception {
		if (entryList == null || entryList.getEntries() == null) {
			return null;
		}
		List<StringMapEntry> entries = entryList.getEntries();
        // LinkedHashMap so that order is preserved!
		Map<String, String> resultMap = new LinkedHashMap<String, String>(entries.size());
		for (StringMapEntry entry : entries) {
			resultMap.put(entry.getKey(), entry.getValue());
		}
		return Collections.unmodifiableMap(resultMap);
	}

    /**
     * Single String-String key-value pair for
     * marshalling/unmarshalling. Need this rather than
     * general Map.Entry<String, String> to specify
     * cardinality in resulting wsdl's.
     *
     * @author Kuali Rice Team (rice.collab@kuali.org)
     *
     */
    @XmlAccessorType(XmlAccessType.NONE)
    @XmlType(name = "StringMapEntryType")
    public static final class StringMapEntry implements Serializable {

        private static final long serialVersionUID = -9609663434312103L;

        @XmlAttribute(name = "key")
        private final String key;

        @XmlValue
        private final String value;

        /**
         * Used only by JAXB.
         */
        @SuppressWarnings("unused")
        private StringMapEntry() {
            this.key = null;
            this.value = null;
        }

        public StringMapEntry(String key, String value) {
            this.key = key;
            this.value = value;
        }

        public StringMapEntry(Map.Entry<String, String> e) {
            this.key = e.getKey();
            this.value = e.getValue();
        }

        public String getKey() {
            return this.key;
        }

        public String getValue() {
            return this.value;
        }

    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "StringMapEntryListType")
    public static class StringMapEntryList extends AbstractDataTransferObject {

        private static final long serialVersionUID = 1L;

        @XmlElement(name = "entry")
        private final List<StringMapEntry> entries;

        @SuppressWarnings("unused")
        @XmlAnyElement
        private final Collection<Element> _futureElements = null;

        @SuppressWarnings("unused")
        private StringMapEntryList() {
            this.entries = null;
        }

        public StringMapEntryList(List<StringMapEntry> entries) {
            this.entries = new ArrayList<StringMapEntry>(entries);
        }

        /**
         * @return the attribute
         */
        public List<StringMapEntry> getEntries() {
            if (this.entries == null) {
                return Collections.emptyList();
            }
            return Collections.unmodifiableList(entries);
        }
    }
}
