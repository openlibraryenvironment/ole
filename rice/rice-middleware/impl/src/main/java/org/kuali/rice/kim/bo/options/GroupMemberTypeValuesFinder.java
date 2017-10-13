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
package org.kuali.rice.kim.bo.options;

import org.kuali.rice.core.api.membership.MemberType;
import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.kim.api.KimConstants;
import org.kuali.rice.krad.keyvalues.KeyValuesBase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class GroupMemberTypeValuesFinder extends KeyValuesBase {

	private static final List<KeyValue> LABELS;
	static {
		final List<KeyValue> labels = new ArrayList<KeyValue>( 3 );
	    labels.add(new ConcreteKeyValue(MemberType.PRINCIPAL.getCode(), KimConstants.KimUIConstants.MEMBER_TYPE_PRINCIPAL));
	    labels.add(new ConcreteKeyValue(MemberType.GROUP.getCode(), KimConstants.KimUIConstants.MEMBER_TYPE_GROUP));
	    LABELS = Collections.unmodifiableList(labels);
	}
	
	/*
	 * @see org.kuali.keyvalues.KeyValuesFinder#getKeyValues()
	 */
	@Override
	public List<KeyValue> getKeyValues() {
	    return LABELS;
	}    
}
