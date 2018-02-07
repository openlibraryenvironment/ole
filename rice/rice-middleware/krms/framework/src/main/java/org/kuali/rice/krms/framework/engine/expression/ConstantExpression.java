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
 * The {@link Expression} used in Construction will be returned when invoked with any given {@link ExecutionEnvironment}
 * @author Kuali Rice Team (rice.collab@kuali.org)
 * 
 */
public final class ConstantExpression<T> implements Expression<T> {

	private final T value;

    /**
     * Create a ConstantExpression with the given value
     * @param value to set the value to
     */
	public ConstantExpression(T value) {
		this.value = value;
	}

	@Override
	public T invoke(ExecutionEnvironment environment) {
		return value;
	}

}
