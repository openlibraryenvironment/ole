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
package org.kuali.rice.kew.rule.bo;

import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.kew.api.KewApiConstants;
import org.kuali.rice.krad.keyvalues.KeyValuesBase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A Key Values finder which produces a list of all Rule Attributes in the KEW database.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class RuleAttributeTypeValuesFinder extends KeyValuesBase {

	private static final List<KeyValue> RULE_ATTRIBUTE_TYPES;
	
	static {
		final List<KeyValue> ruleAttributeTypes = new ArrayList<KeyValue>();
		for (String ruleAttributeType : KewApiConstants.RULE_ATTRIBUTE_TYPES) {
			ruleAttributeTypes.add(new ConcreteKeyValue(ruleAttributeType, KewApiConstants.RULE_ATTRIBUTE_TYPE_MAP.get(ruleAttributeType)));
		}
		RULE_ATTRIBUTE_TYPES = Collections.unmodifiableList(ruleAttributeTypes);
	}
	
	@Override
	public List<KeyValue> getKeyValues() {
		return RULE_ATTRIBUTE_TYPES;
	}

}
