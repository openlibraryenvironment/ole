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
package org.kuali.rice.krms.api.repository;

import org.kuali.rice.core.api.mo.common.Coded;
import org.kuali.rice.core.api.util.jaxb.EnumStringAdapter;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

/**
 * Enum for the representation of the Logical Operators AND OR
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public enum LogicalOperator implements Coded {

    /**
     * use this flag with the static factory to get a {@link LogicalOperator} AND
     */
	AND("&"),

    /**
     * use this flag with the static factory to get a {@link LogicalOperator} OR
     */
	OR("|");

	private final String code;

    /**
     * Create the LogicalOperator from the given code
     * @param code to type LogicalOperator as
     */
	private LogicalOperator(String code){
		this.code = code;
	}
	
	@Override
	public String getCode(){
		return code;
	}

    /**
     * Collection<String> OP_CODES
     */
	public static final Collection<String> OP_CODES =
		Collections.unmodifiableCollection(Arrays.asList(AND.code, OR.code));

    /**
     * Collection<String> OP_CODE_NAMES
     */
    public static final Collection<String> OP_CODE_NAMES =
        Collections.unmodifiableCollection(Arrays.asList(AND.name(), OR.name()));

    /**
     * Create a LogicalOperator from the given code
     * @param code used to type LogicalOperator
     * @return LogicalOperator whose code is given
     * @throws IllegalArgumentException if the code does not exist
     */
	public static LogicalOperator fromCode(String code) {
		if (code == null) {
			return null;
		}
		for (LogicalOperator logicalOperator : values()) {
			if (logicalOperator.code.equals(code)) {
				return logicalOperator;
			}
		}
		throw new IllegalArgumentException("Failed to locate the LogicalOperator with the given code: " + code);
	}
	
	@Override
	public String toString(){
		return code;
	}
	
	static final class Adapter extends EnumStringAdapter<LogicalOperator> {
		@Override
		protected Class<LogicalOperator> getEnumClass() {
			return LogicalOperator.class;
		}
		
	}

}
