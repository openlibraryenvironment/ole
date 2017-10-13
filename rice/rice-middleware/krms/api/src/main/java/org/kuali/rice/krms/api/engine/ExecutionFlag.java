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

/**
 * Defines various possible flags that can be used to control how the rules
 * engine executes and performs it's evaluation of rules.  These flags are
 * meant to be set to either true or false.  This is done using the
 * {@link ExecutionOptions} that are passed to the engine at execution time.
 * 
 * @see ExecutionOptions
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public enum ExecutionFlag {

	/**
	 * Indicates that the engine should perform default logging by recording
	 * each {@link ResultEvent} in the {@link EngineResults}.  Default value
	 * is false.
	 * 
	 * @see EngineResults
	 */
	LOG_EXECUTION(false),
	
	/**
	 * Indicates that the selection criteria which is passed to the engine
	 * at the time of execution must be able to select a valid context in
	 * order for engine execution to proceed.  Default value is false,
	 * when false the engine will simply not execute if no valid context
	 * can be located for the specified selection criteria.
	 * 
	 * @see SelectionCriteria
	 */
	CONTEXT_MUST_EXIST(false),
	
	/**
	 * Instructs the engine to evaluate all propositions.  If this value is
	 * set to false, the engine may skip (aka short circuit) propositions that 
	 * do not influence the overall outcome of the proposition tree.  
	 */
	EVALUATE_ALL_PROPOSITIONS(false);
	
	private final boolean defaultValue;

    /**
     * Create an ExecutionFlag with the given value
     * @param defaultValue to set the defaultValue to
     */
	private ExecutionFlag(boolean defaultValue) {
		this.defaultValue = defaultValue;
	}
	
	/**
	 * Returns the default value for the flag if it has not been explicitly set.
	 * 
	 * @return the default value for the flag
	 */
	public boolean getDefaultValue() {
		return this.defaultValue;
	}
	
}