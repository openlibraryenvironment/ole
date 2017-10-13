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
package org.kuali.rice.krms.api.engine;

import java.util.Map;

/**
 * An Engine executes using the given {@link SelectionCriteria}, @{link Facts}, and {@link ExecutionOptions} returning {@link EngineResults}
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public interface Engine {

	/**
	 * Initiates execution of the rules engine.
	 * 
	 * @param selectionCriteria informs the engine of the {@link SelectionCriteria} to use for selection of contexts and agendas
	 * @param facts the facts that the rule engine can use during execution
	 * @param executionOptions defines various {@link ExecutionOptions} that instruct the rules engine on how to perform it's execution
	 * 
	 * @return {@link EngineResults} the results of engine execution
	 */
	EngineResults execute(SelectionCriteria selectionCriteria, Facts facts, ExecutionOptions executionOptions);

    /**
     * Initiates execution of the rules engine.
     *
     * @param selectionCriteria informs the engine of the {@link SelectionCriteria} to use for selection of contexts and agendas
     * @param facts the facts that the rule engine can use during execution.  Since this signature does not pass in
     * {@link Term}s, all terms are defined with only a name, and term parameters can not be specified.
     * @param executionOptions defines various {@link ExecutionOptions} that instruct the rules engine on how to perform it's execution
     *
     * @return the results of engine execution
     */
    EngineResults execute(SelectionCriteria selectionCriteria, Map<String, Object> facts, ExecutionOptions executionOptions);

}
