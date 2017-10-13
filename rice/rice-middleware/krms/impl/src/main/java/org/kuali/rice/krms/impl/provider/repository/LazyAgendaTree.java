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
import org.kuali.rice.krms.api.repository.agenda.AgendaDefinition;
import org.kuali.rice.krms.framework.engine.AgendaTree;

/**
 * TODO... 
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public final class LazyAgendaTree implements AgendaTree {

	private final Object mutex = new Object();
	private final AgendaDefinition agendaDefinition;
	private final RepositoryToEngineTranslator translator;
	
	private AgendaTree agendaTree;
	
	public LazyAgendaTree(AgendaDefinition agendaDefinition, RepositoryToEngineTranslator translator) {
		this.agendaDefinition = agendaDefinition;
		this.translator = translator;
	}

	public void execute(ExecutionEnvironment environment) {
		initialize();
		agendaTree.execute(environment);
	}
	
	public void initialize() {
		// could use double-checked locking here for max efficiency
		synchronized (mutex) {
			if (agendaTree == null) {
				agendaTree = translator.translateAgendaDefinitionToAgendaTree(agendaDefinition);
			}
		}
	}

}
