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
package org.kuali.rice.kew.api.doctype;

import org.kuali.rice.core.api.util.jaxb.MapStringStringAdapter;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * TODO...
 * 
 * @author Kuali Rice Team (kuali-rice@googlegroups.com)
 *
 */
class DocumentTypePolicyMapAdapter extends XmlAdapter<MapStringStringAdapter.StringMapEntryList, Map<DocumentTypePolicy, String>> {

	@Override
	public MapStringStringAdapter.StringMapEntryList marshal(Map<DocumentTypePolicy, String> map) {
	    List<MapStringStringAdapter.StringMapEntry> entries = new ArrayList<MapStringStringAdapter.StringMapEntry>();
	    if (map != null) {
	        for (DocumentTypePolicy policy : map.keySet()) {
	            entries.add(new MapStringStringAdapter.StringMapEntry(policy.getCode(), map.get(policy)));
	        }
	    }
	    return new MapStringStringAdapter.StringMapEntryList(entries);
	}

	@Override
	public Map<DocumentTypePolicy, String> unmarshal(MapStringStringAdapter.StringMapEntryList entryList) {
	    Map<DocumentTypePolicy, String> policies = new HashMap<DocumentTypePolicy, String>();
	    if (entryList != null) {
	        for (MapStringStringAdapter.StringMapEntry entry : entryList.getEntries()) {
	            DocumentTypePolicy policy = DocumentTypePolicy.fromCode(entry.getKey());
	            // if the policy value comes back as null, that means we recieved a policy that we
	            // don't understand...ignore it
	            if (policy != null) {
	                policies.put(policy, entry.getValue());
	            }
	        }
	    }
	    return policies;
	}
	
}
