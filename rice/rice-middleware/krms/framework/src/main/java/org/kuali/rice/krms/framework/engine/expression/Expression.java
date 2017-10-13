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
 * Interface for implementing Expressions, invoked on a {@link ExecutionEnvironment}
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public interface Expression<T> {

    /**
     * Invoke on the given {@link ExecutionEnvironment}.
     * @param environment {@link ExecutionEnvironment} to invoke on.
     * @return T
     */
	T invoke(ExecutionEnvironment environment);
	
}
