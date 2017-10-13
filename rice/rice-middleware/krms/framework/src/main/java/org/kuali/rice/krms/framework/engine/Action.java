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
package org.kuali.rice.krms.framework.engine;

import org.kuali.rice.krms.api.engine.ExecutionEnvironment;

/**
 * An Action executes on a given {@link org.kuali.rice.krms.api.engine.ExecutionEnvironment}
 *
 * @see org.kuali.rice.krms.api.repository.action.ActionDefinitionContract
 * @see org.kuali.rice.krms.api.repository.action.ActionDefinition
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public interface Action {

    /**
     * Execute on a given {@link org.kuali.rice.krms.api.engine.ExecutionEnvironment}.
     * @param environment {@link org.kuali.rice.krms.api.engine.ExecutionEnvironment} to execute.
     */
	public void execute(ExecutionEnvironment environment);

	/**
	 * The engine may be run in a simulation mode and in this case,
	 * most actions should not be executed.  However, if part or all of 
	 * an action needs to be run in order for proper rule evaluation to 
	 * proceed, it should be called herein.
     * @param environment {@link org.kuali.rice.krms.api.engine.ExecutionEnvironment} to simulate execution on.
	 */
	public void executeSimulation(ExecutionEnvironment environment);
}
