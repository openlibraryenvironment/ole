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
package org.kuali.rice.krms.api.repository.proposition;

import org.kuali.rice.core.api.mo.common.Coded;

import java.util.HashSet;
import java.util.Set;

/**
 * enum used to specify the PropositionParameterType CONSTANT TERM FUNCTION OPERATOR
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public enum PropositionParameterType implements Coded {

    /**
     * use this flag with the static factory to get a {@link PropositionParameterType} Constant
     */
	CONSTANT("C"),

    /**
     * use this flag with the static factory to get a {@link PropositionParameterType} Term
     */
	TERM("T"),

    /**
     * use this flag with the static factory to get a {@link PropositionParameterType} Function
     */
	FUNCTION("F"),

    /**
     * use this flag with the static factory to get a {@link PropositionParameterType} Operator
     */
	OPERATOR("O");
	
	private final String code;

    /**
     * Create a PropositionParameterType of the given code
     * @param code code the PropositionParameterType should be of
     */
	private PropositionParameterType(String code) {
		this.code = code;
	}

    @Override
	public String getCode() {
		return code;
	}

    /**
     * Set of valid type codes
     */
	public static final Set<String> VALID_TYPE_CODES = new HashSet<String>();
	static {
		for (PropositionParameterType propositionParameterType : values()) {
			VALID_TYPE_CODES.add(propositionParameterType.getCode());
		}
	}

    /**
     * Create a PropositionParameterType from the given code
     * @param code to type the PropositionParameter
     * @return PropositionParameterType of the given code
     * @throws IllegalArgumentException if the type code does not exist
     */
	public static PropositionParameterType fromCode(String code) {
		if (code == null) {
			return null;
		}
		for (PropositionParameterType propositionParameterType : values()) {
			if (propositionParameterType.code.equals(code)) {
				return propositionParameterType;
			}
		}
		throw new IllegalArgumentException("Failed to locate the PropositionParameterType with the given code: " + code);
	}
	
}
