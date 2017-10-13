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
package org.kuali.rice.krms.impl.provider.repository;

import org.kuali.rice.krms.api.engine.ExecutionEnvironment;
import org.kuali.rice.krms.api.repository.action.ActionDefinition;
import org.kuali.rice.krms.framework.engine.Action;
import org.kuali.rice.krms.framework.type.ActionTypeService;
import org.kuali.rice.krms.impl.type.KrmsTypeResolver;

/**
 * An {@link Action} that doesn't load its members until execution.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
final class LazyAction implements Action {

	private final ActionDefinition actionDefinition;
	private final KrmsTypeResolver typeResolver;

	private final Object mutex = new Object();
	
	// volatile for double-checked locking idiom
	private volatile Action action;

    /**
     *
     * @param actionDefinition ActionDefinition
     * @param typeResolver KrmsTypeResolver
     */
	LazyAction(ActionDefinition actionDefinition, KrmsTypeResolver typeResolver) {
		this.actionDefinition = actionDefinition;
		this.typeResolver = typeResolver;
		this.action = null;
	}
	
	@Override
	public void execute(ExecutionEnvironment environment) {
		getAction().execute(environment);
	}

	@Override
	public void executeSimulation(ExecutionEnvironment environment) {
		getAction().executeSimulation(environment);
	}
	
	/**
	 * Gets the action using a lazy double-checked locking mechanism as documented in Effective Java Item 71.
     * @return Action
	 */
	private Action getAction() {
		Action localAction = action;
		if (localAction == null) {
			synchronized (mutex) {
				localAction = action;
				if (localAction == null) {
					action = localAction = constructAction();
				}
			}
		}
		return localAction;
	}

    /**
     * builder method
     * @return Action
     */
	private Action constructAction() {
		ActionTypeService actionTypeService = typeResolver.getActionTypeService(actionDefinition);
		Action action = actionTypeService.loadAction(actionDefinition);
		if (action == null) {
			action = new Action() {
				@Override
				public void execute(ExecutionEnvironment environment) {
				}
				public void executeSimulation(ExecutionEnvironment environment) {
				}
			};
		}
		return action;
	}

	
}
