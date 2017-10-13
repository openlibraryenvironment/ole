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
package org.kuali.rice.coreservice.api.parameter;

import org.kuali.rice.core.api.mo.common.Coded;
import org.kuali.rice.core.api.util.jaxb.EnumStringAdapter;

/**
 * Defines the possible evaluation operators that can be supported on system parameters.
 */
public enum EvaluationOperator implements Coded {

	/**
	 * Indicates that evaluation will determine if the value being tested in present
	 * in the set of values defined on the parameter.  If it is present in this set,
	 * then evaluation will succeed.
	 */
	ALLOW("A"),
	
	/**
	 * Indicates that evaluation will determine if the value being tested is absent
	 * from the set of values defined on the parameter.  If it is absent from this
	 * set, then the evaluation will succeed.
	 */
	DISALLOW("D");
	
	private final String code;
	
	EvaluationOperator(final String code) {
		this.code = code;
	}
		
	/**
	 * Returns the operator code for this evaluation operator.
	 * 
	 * @return the code
	 */
	@Override
	public String getCode() {
		return code;
	}
	
	public static EvaluationOperator fromCode(String code) {
		if (code == null) {
			return null;
		}
		for (EvaluationOperator operator : values()) {
			if (operator.code.equals(code)) {
				return operator;
			}
		}
		throw new IllegalArgumentException("Failed to locate the EvaluationOperator with the given code: " + code);
	}
	
	static final class Adapter extends EnumStringAdapter<EvaluationOperator> {
		
		protected Class<EvaluationOperator> getEnumClass() {
			return EvaluationOperator.class;
		}
		
	}
	
}
