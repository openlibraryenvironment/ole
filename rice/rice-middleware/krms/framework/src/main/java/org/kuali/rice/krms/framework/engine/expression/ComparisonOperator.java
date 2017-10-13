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
package org.kuali.rice.krms.framework.engine.expression;

import org.kuali.rice.core.api.mo.common.Coded;
import org.kuali.rice.core.api.util.jaxb.EnumStringAdapter;
import org.kuali.rice.krms.api.KrmsApiServiceLocator;
import org.kuali.rice.krms.api.engine.expression.ComparisonOperatorService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Operators enumeration for comparing objects.  EQUALS NOT_EQUALS GREATER_THAN GREATER_THAN_EQUAL LESS_THAN LESS_THAN_EQUAL.
 * Uses registered {@link EngineComparatorExtension} for the given objects or the {@link DefaultComparisonOperator}.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public enum ComparisonOperator implements Coded {

    /**
     * use this flag with the static factory to get a {@link ComparisonOperator} EQUALS
     */
	EQUALS("="),

    /**
     * use this flag with the static factory to get a {@link ComparisonOperator} NOT_EQUALS
     */
	NOT_EQUALS("!="),

    /**
     * use this flag with the static factory to get a {@link ComparisonOperator} GREATER_THAN
     */
	GREATER_THAN(">"),

    /**
     * use this flag with the static factory to get a {@link ComparisonOperator} GREATER_THAN_EQUAL
     */
	GREATER_THAN_EQUAL(">="),

    /**
     * use this flag with the static factory to get a {@link ComparisonOperator} LESS_THAN
     */
	LESS_THAN("<"),

    /**
     * use this flag with the static factory to get a {@link ComparisonOperator} LESS_THAN_EQUAL
     */
	LESS_THAN_EQUAL("<="),

    /**
     * use this flag with the static factory to get a {@link ComparisonOperator} EXISTS
     */
    EXISTS("!=null"),

    /**
     * use this flag with the static factory to get a {@link ComparisonOperator} DOES_NOT_EXISTS
     */
    DOES_NOT_EXIST("=null");

	private final String code;

    ComparisonOperatorService comparisonOperatorService;

    /**
     * Create a ComparisonOperator from the given code
     * @param code code the ComparisonOperator should be of.
     */
	private ComparisonOperator(String code) {
		this.code = code;
	}

    /**
     *
     * @return code representing the type of operator
     */
    @Override
	public String getCode() {
		return code;
	}

    /**
     * Create a ComparisonOperator from the given code
     * @param code for type of ComparisonOperator to create
     * @return a ComparisonOperator created with the given code.
     * @throws IllegalArgumentException if the given code does not exist
     */
	public static ComparisonOperator fromCode(String code) {
		if (code == null) {
			return null;
		}
		for (ComparisonOperator comparisonOperator : values()) {
			if (comparisonOperator.code.equals(code)) {
				return comparisonOperator;
			}
		}
		throw new IllegalArgumentException("Failed to locate the ComparisionOperator with the given code: " + code);
	}

    /**
     * Compare the given objects
     * @param lhs left hand side object
     * @param rhs right hand side object
     * @return boolean value of comparison results based on the type of operator.
     */
	public boolean compare(Object lhs, Object rhs) {
        if (comparisonOperatorService == null) {
            setComparisonOperatorService(KrmsApiServiceLocator.getComparisonOperatorService());
        }
        int result = comparisonOperatorService.compare(lhs, rhs);

        if (this == EQUALS) {
            return result == 0;
        } else if (this == NOT_EQUALS) {
            return result != 0;
        } else if (this == GREATER_THAN) {
            return result > 0;
        } else if (this == GREATER_THAN_EQUAL) {
            return result >= 0;
        } else if (this == LESS_THAN) {
            return result < 0;
        } else if (this == LESS_THAN_EQUAL) {
            return result <= 0;
        } else if (this == EXISTS) {
            return rhs != null;
        } else if (this == DOES_NOT_EXIST) {
            return rhs == null;
        }
        throw new IllegalStateException("Invalid comparison operator detected: " + this);
	}


    /**
     * Operator codes, unmodifiable Collection
     */
    public static final Collection<String> OPERATOR_CODES;

    /**
     * Operator names, unmodifiable Collection
     */
    public static final Collection<String> OPERATOR_NAMES;

    static {
        List<String> operatorCodes = new ArrayList<String>();
        List<String> operatorNames = new ArrayList<String>();

        for (ComparisonOperator operator : values()) {
            operatorCodes.add(operator.getCode());
            operatorNames.add(operator.name());
        }

        OPERATOR_CODES = Collections.unmodifiableCollection(operatorCodes);
        OPERATOR_NAMES = Collections.unmodifiableCollection(operatorNames);
    }

    public void setComparisonOperatorService(ComparisonOperatorService comparisonOperatorService) {
        this.comparisonOperatorService = comparisonOperatorService;
    }

    /**
     *
     * @return type code
     */
    @Override
    public String toString(){
        return code;
    }

    static final class Adapter extends EnumStringAdapter<ComparisonOperator> {

        @Override
        protected Class<ComparisonOperator> getEnumClass() {
            return ComparisonOperator.class;
        }
    }
}
