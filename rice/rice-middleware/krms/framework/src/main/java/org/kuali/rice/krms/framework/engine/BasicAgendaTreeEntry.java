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
 * A {@link AgendaTreeEntry} which executes its ifTrue {@link AgendaTree} if the given {@link Rule} result is true or
 * its ifFalse {@link AgendaTree} if the result is false.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public final class BasicAgendaTreeEntry implements AgendaTreeEntry {
	
	private final Rule rule;
	private final AgendaTree ifTrue;
	private final AgendaTree ifFalse;

    /**
     * Create a BasicAgendaTreeEntry with the given {@link Rule}.
     * @param rule {@link Rule} to create the BasicAgendaTreeEntry with.
     * @throws IllegalArgumentException if the rule is null.
     */
	public BasicAgendaTreeEntry(Rule rule) {
		this(rule, null, null);
	}

    /**
     * Create a BasicAgendaTreeEntry with the given {@link Rule} and ifTrue, ifFalse {@link AgendaTree}s.
     * @param rule {@link Rule} to create the BasicAgendaTreeEntry with.
     * @param ifTrue executed if the given rule's result is true.
     * @param ifTrue executed if the given rule's result is false.
     * @throws IllegalArgumentException if the rule is null.
     */
	public BasicAgendaTreeEntry(Rule rule, AgendaTree ifTrue, AgendaTree ifFalse) {
		if (rule == null) {
			throw new IllegalArgumentException("rule was null");
		}
		this.rule = rule;
		this.ifTrue = ifTrue;
		this.ifFalse = ifFalse;
	}
	
	@Override
	public void execute(ExecutionEnvironment environment) {
		boolean result = rule.evaluate(environment);
		if (result && ifTrue != null) {
			ifTrue.execute(environment);
		}
		if (!result && ifFalse != null) {
			ifFalse.execute(environment);
		}
	}
	
}
