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

import org.kuali.rice.core.api.delegation.DelegationType;
import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.kew.api.KewApiConstants;
import org.kuali.rice.kns.util.KNSGlobalVariables;
import org.kuali.rice.kns.web.struts.form.KualiMaintenanceForm;
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
	private static final List<KeyValue> C_DELEGATION_TYPES_FOR_MAIN_DOCS;
	static {
		
		final List<KeyValue> delegationTypes = new ArrayList<KeyValue>();
		final List<KeyValue> delegationTypesForMaintDocs = new ArrayList<KeyValue>();
		
		for (DelegationType delegationType : DelegationType.values()) {
			delegationTypes.add(new ConcreteKeyValue(delegationType.getCode(), delegationType.getLabel()));
			delegationTypesForMaintDocs.add(new ConcreteKeyValue(delegationType.getCode(), delegationType.getLabel()));
		}
		// for non maintenance documents, add a "both" option
		delegationTypes.add(new ConcreteKeyValue(KewApiConstants.DELEGATION_BOTH, KewApiConstants.DELEGATION_BOTH_LABEL));
		
		C_DELEGATION_TYPES = Collections.unmodifiableList(delegationTypes);
		C_DELEGATION_TYPES_FOR_MAIN_DOCS = Collections.unmodifiableList(delegationTypesForMaintDocs);
	}
	
	@Override
	public List<KeyValue> getKeyValues() {
		// Return the appropriate delegation types list, depending on whether or not it is needed for a maintenance doc.
		return (KNSGlobalVariables.getKualiForm() instanceof KualiMaintenanceForm) ? C_DELEGATION_TYPES_FOR_MAIN_DOCS : C_DELEGATION_TYPES;
	}

}
