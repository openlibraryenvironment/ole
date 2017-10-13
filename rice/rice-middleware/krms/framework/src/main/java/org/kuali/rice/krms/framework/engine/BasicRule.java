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

import java.util.List;

import org.kuali.rice.krms.api.engine.ExecutionEnvironment;
import org.kuali.rice.krms.api.engine.ResultEvent;
import org.kuali.rice.krms.framework.engine.result.BasicResult;

/**
 * A {@link org.kuali.rice.krms.framework.engine.Rule} that executes a {@link org.kuali.rice.krms.framework.engine.Action} when the {@link Proposition} is true.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class BasicRule implements Rule {
	private static final ResultLogger LOG = ResultLogger.getInstance();

	private String name;
	private Proposition proposition;
	private List<Action> actions;

    /**
     * Constructor
     * @param name Rule name to set the name to
     * @param proposition {@link Proposition} to set the proposition to, cannot be null
     * @param actions Rule Actions to set the actions to
     * @throws IllegalArgumentException if the {@link Proposition} is null
     */
    public BasicRule(String name, Proposition proposition, List<Action> actions) {
		if (proposition == null) {
			throw new IllegalArgumentException("Proposition cannot be null.");
		}
		this.name = name;
		this.proposition = proposition;
		this.actions = actions;
	}

    /**
     * Constructor
     * @param proposition {@link Proposition} to set the proposition to, cannot be null
     * @param actions Rule Actions to set the actions to
     * @throws IllegalArgumentException if the {@link Proposition} is null
     */
	public BasicRule(Proposition proposition, List<Action> actions) {
		this(null, proposition, actions);
	}
	
	@Override
	public boolean evaluate(ExecutionEnvironment environment) {
		boolean result = proposition.evaluate(environment).getResult();
		if (actions != null) {
			for (Action action : actions) {
				if (shouldExecuteAction(result)) {
					action.execute(environment);
				}
			}
		}
		if (LOG.isEnabled(environment)){
			LOG.logResult(new BasicResult(ResultEvent.RULE_EVALUATED, this, environment, result));
		}
		return result;
	}

    /**
     * Based on the ruleExecutionResult should the {@link Action} be executed?  Default behavior is to return the given ruleExecutionResult.
     * Over-writable by subclasses.
     * @param ruleExecutionResult the result of the engines evaluation method.
     * @return boolean should the action execute
     */
	protected boolean shouldExecuteAction(boolean ruleExecutionResult) {
		return ruleExecutionResult;
	}

    /**
     * Return the Rule name
     * @return name Rule name
     */
	public String getName() {
		return name;
	}
	
    @Override
	public String toString(){
		return name;
	}
}
