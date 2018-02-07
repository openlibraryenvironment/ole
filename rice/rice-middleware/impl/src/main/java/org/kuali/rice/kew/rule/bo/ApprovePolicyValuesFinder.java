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
import org.kuali.rice.kew.api.action.ActionRequestPolicy;
import org.kuali.rice.krad.keyvalues.KeyValuesBase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A values finder for returning KEW approve policy codes.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class ApprovePolicyValuesFinder extends KeyValuesBase {

	private static final List<KeyValue> REQUEST_POLICIES;
	static {
		final List<KeyValue> temp = new ArrayList<KeyValue>();
		for (ActionRequestPolicy policy : ActionRequestPolicy.values()) {
			temp.add(new ConcreteKeyValue(policy.getCode(), policy.getLabel()));
		}
		REQUEST_POLICIES = Collections.unmodifiableList(temp);
	}
	
	@Override
	public List<KeyValue> getKeyValues() {
		return REQUEST_POLICIES;
	}

}
