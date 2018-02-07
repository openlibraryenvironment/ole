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
package org.kuali.rice.kew.impl.peopleflow;

import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.kim.api.KimConstants;
import org.kuali.rice.krad.keyvalues.KeyValuesBase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A values finder for returning KEW rule delegation type codes.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class DelegationTypeValuesFinder extends KeyValuesBase {

	private static final List<KeyValue> C_DELEGATION_TYPES;
	static {
		final List<KeyValue> delegationTypes = new ArrayList<KeyValue>();
		for (String delegationType : KimConstants.KimUIConstants.DELEGATION_TYPES.keySet()) {
			delegationTypes.add(new ConcreteKeyValue(delegationType, KimConstants.KimUIConstants.DELEGATION_TYPES.get(delegationType)));
		}
		C_DELEGATION_TYPES = Collections.unmodifiableList(delegationTypes);
	}
	
	@Override
	public List<KeyValue> getKeyValues() {
		return C_DELEGATION_TYPES;
	}

}
