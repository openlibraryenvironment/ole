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
package org.kuali.rice.kew.lookup.valuefinder;

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.kew.service.KEWServiceLocator;
import org.kuali.rice.krad.keyvalues.KeyValuesBase;
import org.kuali.rice.krad.util.GlobalVariables;

import java.util.ArrayList;
import java.util.List;

/**
 * This is a description of what this class does - chris don't forget to fill this in. 
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class SavedSearchValuesFinder extends KeyValuesBase {

	/**
	 * @see org.kuali.rice.krad.keyvalues.KeyValuesFinder#getKeyValues()
	 */
	@Override
	public List<KeyValue> getKeyValues() {
		List<KeyValue> savedSearchValues = new ArrayList<KeyValue>();
		savedSearchValues.add(new ConcreteKeyValue("", "Searches"));
		savedSearchValues.add(new ConcreteKeyValue("*ignore*", "-----"));
		savedSearchValues.add(new ConcreteKeyValue("*ignore*", "-Named Searches"));
		List<KeyValue> namedSearches = KEWServiceLocator.getDocumentSearchService().getNamedSearches(GlobalVariables.getUserSession().getPrincipalId());
		for (KeyValue keyValue : namedSearches) {
			String label = StringUtils.abbreviate(keyValue.getValue(), 75);
			KeyValue keyLabel = new ConcreteKeyValue(keyValue.getKey(),label);
			savedSearchValues.add(keyLabel);
		}
		savedSearchValues.add(new ConcreteKeyValue("*ignore*", "-----"));
		savedSearchValues.add(new ConcreteKeyValue("*ignore*", "-Recent Searches"));
		List<KeyValue> mostRecentSearches = KEWServiceLocator.getDocumentSearchService().getMostRecentSearches(GlobalVariables.getUserSession().getPrincipalId());
		for (KeyValue keyValue : mostRecentSearches) {
			String label = StringUtils.abbreviate(keyValue.getValue(), 75);
			KeyValue keyLabel = new ConcreteKeyValue(keyValue.getKey(),label);
			savedSearchValues.add(keyLabel);
		}
		return savedSearchValues;
	}

}
