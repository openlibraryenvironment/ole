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

import java.util.Map;

import org.kuali.rice.krms.api.engine.ExecutionEnvironment;
import org.kuali.rice.krms.api.repository.agenda.AgendaDefinition;

/**
 * An implementation of {@link Agenda} that executes over an {@link AgendaTree}.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */

public class BasicAgenda implements Agenda {

	private Map<String, String> qualifiers;
	private AgendaTree agendaTree;

    /**
     * Create a BasicAgenda with the given qualifiers and {@link AgendaTree}
     * @param qualifiers to determine if a given {@link ExecutionEnvironment} applies.
     * @param agendaTree {@link AgendaTree} to be executed
     */
	public BasicAgenda(Map<String, String> qualifiers, AgendaTree agendaTree) {
		this.qualifiers = qualifiers;
		this.agendaTree = agendaTree;
	}
	
	@Override
	public void execute(ExecutionEnvironment environment) {
		agendaTree.execute(environment);
	}

	@Override
	public boolean appliesTo(ExecutionEnvironment environment) {

        for (Map.Entry<String, String> agendaQualifier : environment.getSelectionCriteria().getAgendaQualifiers().entrySet()) {
            String agendaQualifierValue = qualifiers.get(agendaQualifier.getKey());
            String environmentQualifierValue = agendaQualifier.getValue();
            if (!environmentQualifierValue.equals(agendaQualifierValue)) {
                return false;
            }
        }
		return true;
	}

}
