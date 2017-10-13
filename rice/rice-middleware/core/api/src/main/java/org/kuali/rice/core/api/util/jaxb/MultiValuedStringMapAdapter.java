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
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
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
 * </pre>
 * 
 * Note that this adapter isn't suitable for mutable attributes as it will unmarshal to an immutable form.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class MultiValuedStringMapAdapter extends XmlAdapter<MultiValuedStringMapAdapter.MultiValuedStringMapEntryList, Map<String, List<String>>> {

	@Override
	public MultiValuedStringMapEntryList marshal(Map<String, List<String>> map) throws Exception {
		if (map == null) {
			return null;
		}
		List<MultiValuedStringMapEntry> entries = new ArrayList<MultiValuedStringMapEntry>();
		for (Map.Entry<String, List<String>> entry : map.entrySet()) {
			entries.add(new MultiValuedStringMapEntry(entry));
		}
		return new MultiValuedStringMapEntryList(entries);
	}

    @Override
	public Map<String, List<String>> unmarshal(MultiValuedStringMapEntryList entryList) throws Exception {
		if (entryList == null || entryList.getEntries() == null) {
			return null;
		}
		List<MultiValuedStringMapEntry> entries = entryList.getEntries();
		Map<String, List<String>> resultMap = new HashMap<String, List<String>>(entries.size());
		for (MultiValuedStringMapEntry entry : entries) {
			resultMap.put(entry.getKey(), entry.getValues());
		}
		return Collections.unmodifiableMap(resultMap);
	}

    @XmlAccessorType(XmlAccessType.NONE)
    @XmlType(name = "MultiValuedStringMapEntryType")
    public static final class MultiValuedStringMapEntry implements Serializable {

        private static final long serialVersionUID = -9609663434312103L;

        @XmlAttribute(name = "key")
        private final String key;

        @XmlElementWrapper(name = "values")
        @XmlElement(name = "value")
        private final List<String> values;

        /**
         * Used only by JAXB.
         */
        @SuppressWarnings("unused")
        MultiValuedStringMapEntry() {
            this.key = null;
            this.values = null;
        }

        public MultiValuedStringMapEntry(String key, List<String> values) {
            this.key = key;
            this.values = values;
        }

        public MultiValuedStringMapEntry(Map.Entry<String, List<String>> entry) {
            this.key = entry.getKey();
            this.values = Collections.unmodifiableList(new ArrayList<String>(entry.getValue()));
        }

        public String getKey() {
            return this.key;
        }

        public List<String> getValues() {
            return this.values;
        }

    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "MultiValuedStringMapEntryListType")
    public static class MultiValuedStringMapEntryList extends AbstractDataTransferObject {

        private static final long serialVersionUID = 1L;

        @XmlElement(name = "entry")
        private final List<MultiValuedStringMapEntry> entries;

        @SuppressWarnings("unused") @XmlAnyElement
        private final Collection<Element> _futureElements = null;

        @SuppressWarnings("unused")
        MultiValuedStringMapEntryList() {
            this.entries = null;
        }

        public MultiValuedStringMapEntryList(List<MultiValuedStringMapEntry> entries) {
            this.entries = new ArrayList<MultiValuedStringMapEntry>(entries);
        }

        public List<MultiValuedStringMapEntry> getEntries() {
            if (this.entries == null) {
                return Collections.emptyList();
            }
            return Collections.unmodifiableList(entries);
        }
    }
}
