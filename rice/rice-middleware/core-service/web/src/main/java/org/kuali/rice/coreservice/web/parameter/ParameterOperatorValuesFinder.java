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
package org.kuali.rice.coreservice.web.parameter;

import org.kuali.rice.coreservice.api.parameter.EvaluationOperator;
import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.krad.keyvalues.KeyValuesBase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This class returns list of parameter operator value pairs.
 */
public class ParameterOperatorValuesFinder extends KeyValuesBase {

	private static final List<KeyValue> KEY_VALUES;
	
	static {
		final List<KeyValue> keyValues = new ArrayList<KeyValue>();
        keyValues.add(new ConcreteKeyValue(EvaluationOperator.ALLOW.getCode(), "Allowed"));
        keyValues.add(new ConcreteKeyValue(EvaluationOperator.DISALLOW.getCode(), "Denied"));
		KEY_VALUES = Collections.unmodifiableList(keyValues);
	}
	
    @Override
	public List<KeyValue> getKeyValues() {
        return KEY_VALUES;
    }
}
