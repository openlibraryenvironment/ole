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
import org.kuali.rice.krms.api.engine.IncompatibleTypeException;

/**
 * The {@link Expression} used in construction, will be invoked with the given {@link ExecutionEnvironment}.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public final class BooleanValidatingExpression implements Expression<Boolean> {

	private final Expression<? extends Object> expression;

    /**
     * Constructor
     * @param expression {@link Expression} to set the expression to
     */
	public BooleanValidatingExpression(Expression<? extends Object> expression) {
		this.expression = expression;
	}

    @Override
	public Boolean invoke(ExecutionEnvironment environment) {
		Object result = expression.invoke(environment);
		if (result instanceof Boolean) {
			return (Boolean)result;
		}
		throw new IncompatibleTypeException("Type mismatch when executing expression.", result, Boolean.class);
	}
	
}