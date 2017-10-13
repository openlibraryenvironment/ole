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

import org.kuali.rice.krms.api.engine.ExecutionEnvironment;

/**
 * Binary Operator implementation of Expression<Boolean>
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public final class BinaryOperatorExpression implements Expression<Boolean> {

	private final ComparisonOperator operator;
	private final Expression<? extends Object> lhs;
	private final Expression<? extends Object> rhs;

    /**
     * Create a BinaryOperatorExpression with the given values
     * @param operator {@link ComparisonOperator}
     * @param lhs left hand side Expression
     * @param rhs right hand side Expression
     */
	public BinaryOperatorExpression(ComparisonOperator operator, Expression<? extends Object> lhs, Expression<? extends Object> rhs) {
		this.operator = operator;
		this.lhs = lhs;
		this.rhs = rhs;
	}

    @Override
	public Boolean invoke(ExecutionEnvironment environment) {
		Object lhsValue = lhs.invoke(environment);
		Object rhsValue = rhs.invoke(environment);
		return operator.compare(lhsValue, rhsValue);
	}
	
}
