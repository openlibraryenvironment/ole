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

import java.util.List;

import org.kuali.rice.krms.api.engine.ExecutionEnvironment;
import org.kuali.rice.krms.api.engine.expression.ComparisonOperatorService;
import org.kuali.rice.krms.framework.engine.Function;

/**
 * An implementation of {@link Expression} which invokes a {@link Function} with the results of the invocation of the given
 * List of {@link Expression}s (of the given {@link ExecutionEnvironment}).
 * @author Kuali Rice Team (rice.collab@kuali.org)
 * 
 */
public final class FunctionExpression implements Expression<Object> {

	private final Function function;
    private final String [] parameterTypes;
	private final List<Expression<? extends Object>> parameters;
    private final ComparisonOperatorService comparisonOperatorService;

    /**
     * Create a FunctionExpression with the given values.
     * @param function {@link Function} to be invoked using the invoked results of the given List of {@link Expression}s
     * @param parameterTypes the full class names for the function's parameter types in sequential order
     * @param parameters List of {@link Expression}s to be invoked whose results (of the given {@link ExecutionEnvironment})
     * @param comparisonOperatorService -- TODO:
     * will be used to invoke the given {@link Function}.
     */
	public FunctionExpression(Function function, String[] parameterTypes,
			List<Expression<? extends Object>> parameters,
            ComparisonOperatorService comparisonOperatorService) {
		this.function = function;
        this.parameterTypes = parameterTypes;
		this.parameters = parameters;
        this.comparisonOperatorService = comparisonOperatorService;
	}

	@Override
	public Object invoke(ExecutionEnvironment environment) {
        Object[] argumentValues = new Object[parameters.size()];

        int argValIndex = 0;

		for (Expression<? extends Object> argument : parameters) {
			Object argumentValue = argument.invoke(environment);
            String expectedArgumentType = parameterTypes[argValIndex];

            argumentValue = ComparisonOperatorServiceUtils.coerceIfNeeded(argumentValue, expectedArgumentType, comparisonOperatorService);

            argumentValues[argValIndex] = argumentValue;
            argValIndex += 1;
		}

		return function.invoke(argumentValues);
	}


}
