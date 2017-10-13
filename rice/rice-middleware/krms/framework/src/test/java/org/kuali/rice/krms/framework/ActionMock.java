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
package org.kuali.rice.krms.framework;

import java.util.HashSet;
import java.util.Set;

import org.kuali.rice.krms.api.engine.ExecutionEnvironment;
import org.kuali.rice.krms.framework.engine.Action;

/**
 * Used to help test agendas
 * @author gilesp
 *
 */
public class ActionMock implements Action {

	private static final Set<String> actionsFired = new HashSet<String>();
	
	public static void resetActionsFired() {
		actionsFired.clear();
	}
	
	public static boolean actionFired(String name) {
		return actionsFired.contains(name);
	}
	
	public ActionMock(String name) {
		this.name = name;
	}
	
	private final String name;
	
	@Override
	public void execute(ExecutionEnvironment environment) {
		actionsFired.add(name);
	}
	
	/**
	 * @see org.kuali.rice.krms.framework.engine.Action#executeSimulation(org.kuali.rice.krms.api.engine.ExecutionEnvironment)
	 */
	@Override
	public void executeSimulation(ExecutionEnvironment environment) {
		throw new UnsupportedOperationException();
	}
	
	public boolean actionFired() {
		return actionFired(name);
	}
	
}
